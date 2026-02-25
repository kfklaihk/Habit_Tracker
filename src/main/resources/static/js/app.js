const API_BASE_URL = window.location.origin + '/api';
let currentUserId = null;
let calHeatmap = null;
let skillsPieChart = null;
let weeklyBarChart = null;
let allSkills = [];

// Initialize the app
document.addEventListener('DOMContentLoaded', () => {
    setDefaultDailyLogFormValues();
    loadUsers();
});

function setDefaultDailyLogFormValues() {
    const logDate = document.getElementById('logDate');
    if (logDate) {
        logDate.value = new Date().toISOString().split('T')[0];
    }
    const focusValue = document.getElementById('focusValue');
    const focusScore = document.getElementById('focusScore');
    if (focusValue && focusScore) {
        focusValue.textContent = focusScore.value;
    }
}

// Load all users for selection
async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        const users = await response.json();
        
        const userSelect = document.getElementById('userSelect');
        userSelect.innerHTML = '<option value="">Select a user...</option>';
        
        users.forEach(user => {
            const option = document.createElement('option');
            option.value = user.id;
            option.textContent = `${user.username} (${user.email})`;
            userSelect.appendChild(option);
        });
        
        // Auto-select first user if available
        if (users.length > 0) {
            userSelect.value = users[0].id;
            onUserChange();
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

function onUserChange() {
    const userSelect = document.getElementById('userSelect');
    currentUserId = userSelect.value;
    
    if (!currentUserId) {
        return;
    }

    refreshDashboard();
    loadSkills();
}

async function refreshDashboard() {
    if (!currentUserId) return;
    try {
        const response = await fetch(`${API_BASE_URL}/dashboard?userId=${currentUserId}`);
        const dashboard = await response.json();

        document.getElementById('currentStreak').textContent = dashboard.currentStreak ?? 0;
        document.getElementById('totalDaysLogged').textContent = dashboard.totalDaysLogged ?? 0;

        renderGoals(dashboard.activeGoals || []);
        renderSkillsPie(dashboard.skillStats || []);
        renderWeeklyHours(dashboard.weeklyHours || []);
        paintHeatmap();
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

function renderGoals(goals) {
    const body = document.getElementById('goalsTableBody');
    if (!body) return;
    if (!goals || goals.length === 0) {
        body.innerHTML = '<tr><td colspan="3" class="text-muted">No active goals.</td></tr>';
        return;
    }

    body.innerHTML = '';
    goals.forEach(g => {
        const progress = (g.progressValue ?? null);
        const target = (g.targetValue ?? null);
        const unit = g.unit || '';

        const progressText = progress !== null ? `${progress}` : '—';
        const targetText = target !== null ? `${target}` : '—';

        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>
            <div class="fw-semibold">${escapeHtml(g.name || 'Goal')}</div>
            <div class="text-muted small">${escapeHtml(g.goalType || '')}</div>
          </td>
          <td class="text-end">${escapeHtml(progressText)} <span class="text-muted small">${escapeHtml(unit)}</span></td>
          <td class="text-end">${escapeHtml(targetText)} <span class="text-muted small">${escapeHtml(unit)}</span></td>
        `;
        body.appendChild(tr);
    });
}

function renderSkillsPie(skillStats) {
    const hint = document.getElementById('skillsEmptyHint');
    const canvas = document.getElementById('skillsPieChart');
    if (!canvas) return;

    if (skillsPieChart) {
        skillsPieChart.destroy();
        skillsPieChart = null;
    }

    if (!skillStats || skillStats.length === 0) {
        if (hint) hint.style.display = 'block';
        return;
    }
    if (hint) hint.style.display = 'none';

    const labels = skillStats.map(s => s.skillName);
    const data = skillStats.map(s => s.totalMinutes);
    const colors = skillStats.map(s => s.color || '#6c757d');

    skillsPieChart = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels,
            datasets: [{
                data,
                backgroundColor: colors,
                hoverOffset: 4
            }]
        },
        options: {
            plugins: {
                legend: { position: 'right' },
                tooltip: {
                    callbacks: {
                        label: (ctx) => {
                            const mins = Number(ctx.raw || 0);
                            const hrs = Math.round((mins / 60) * 10) / 10;
                            return ` ${mins} min (${hrs} hrs)`;
                        }
                    }
                }
            }
        }
    });
}

function renderWeeklyHours(weeklyHours) {
    const hint = document.getElementById('weeklyEmptyHint');
    const canvas = document.getElementById('weeklyBarChart');
    if (!canvas) return;

    if (weeklyBarChart) {
        weeklyBarChart.destroy();
        weeklyBarChart = null;
    }

    if (!weeklyHours || weeklyHours.length === 0) {
        if (hint) hint.style.display = 'block';
        return;
    }
    if (hint) hint.style.display = 'none';

    const labels = weeklyHours.map(w => w.weekLabel);
    const data = weeklyHours.map(w => Number(w.totalHours || 0));

    weeklyBarChart = new Chart(canvas, {
        type: 'bar',
        data: {
            labels,
            datasets: [{
                label: 'Hours coded',
                data,
                backgroundColor: 'rgba(13, 110, 253, 0.7)',
                borderColor: 'rgba(13, 110, 253, 1)',
                borderWidth: 1,
                borderRadius: 4
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true, title: { display: true, text: 'Hours' } }
            },
            plugins: { legend: { display: false } }
        }
    });
}

function paintHeatmap() {
    const container = document.getElementById('cal-heatmap');
    if (!container || !currentUserId) return;

    // Clear previous render
    container.innerHTML = '';
    calHeatmap = new CalHeatmap();

    const start = new Date();
    start.setFullYear(start.getFullYear() - 1);

    calHeatmap.paint({
        itemSelector: "#cal-heatmap",
        domain: { type: 'month', label: { text: 'MMM', position: 'top' } },
        subDomain: { type: 'day', radius: 2, width: 14, height: 14, gutter: 2 },
        date: { start },
        range: 13,
        data: {
            source: `${API_BASE_URL}/activity?userId=${currentUserId}&start={{start=YYYY-MM-DD}}&end={{end=YYYY-MM-DD}}`,
            x: 'timestamp',
            y: (d) => +d['value'],
            groupY: 'sum'
        },
        scale: {
            color: {
                type: 'threshold',
                range: ['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39'],
                domain: [0, 2, 4, 6, 8]
            }
        }
    });
}

async function loadSkills() {
    try {
        const response = await fetch(`${API_BASE_URL}/skills`);
        allSkills = await response.json();
        renderSkillMinutesInputs();
        renderSkillsList();
    } catch (error) {
        console.error('Error loading skills:', error);
    }
}

function renderSkillMinutesInputs() {
    const container = document.getElementById('skillMinutesList');
    if (!container) return;

    if (!allSkills || allSkills.length === 0) {
        container.innerHTML = '<div class="text-muted">No skills yet. Add skills first in “Manage Skills”.</div>';
        return;
    }

    container.innerHTML = '';
    allSkills.forEach(skill => {
        const row = document.createElement('div');
        row.className = 'd-flex align-items-center justify-content-between gap-3 py-1';
        row.innerHTML = `
          <div class="d-flex align-items-center gap-2">
            <span class="skill-dot" style="background:${skill.colorHex || '#6c757d'}"></span>
            <span class="fw-semibold">${escapeHtml(skill.name)}</span>
            <span class="text-muted small">${escapeHtml(skill.category || '')}</span>
          </div>
          <div style="width: 140px;">
            <input type="number" class="form-control form-control-sm" min="0" step="5"
                   placeholder="0" id="skill_minutes_${skill.id}">
          </div>
        `;
        container.appendChild(row);
    });
}

function renderSkillsList() {
    const container = document.getElementById('skillsList');
    if (!container) return;
    if (!allSkills || allSkills.length === 0) {
        container.innerHTML = '<div class="text-muted">No skills yet.</div>';
        return;
    }

    container.innerHTML = '';
    allSkills.forEach(skill => {
        const row = document.createElement('div');
        row.className = 'd-flex align-items-center justify-content-between py-1';
        row.innerHTML = `
          <div class="d-flex align-items-center gap-2">
            <span class="skill-dot" style="background:${skill.colorHex || '#6c757d'}"></span>
            <span class="fw-semibold">${escapeHtml(skill.name)}</span>
            <span class="text-muted small">${escapeHtml(skill.category || '')}</span>
          </div>
          <button class="btn btn-sm btn-outline-danger" onclick="deleteSkill(${skill.id})">Delete</button>
        `;
        container.appendChild(row);
    });
}

async function submitDailyLog() {
    const errorEl = document.getElementById('dailyLogError');
    if (errorEl) {
        errorEl.style.display = 'none';
        errorEl.textContent = '';
    }

    if (!currentUserId) {
        showDailyLogError('Select a user first.');
        return;
    }

    const logDate = document.getElementById('logDate').value;
    const hoursCoded = document.getElementById('hoursCoded').value;
    const focusScore = document.getElementById('focusScore').value;
    const notes = document.getElementById('notes').value;

    const skills = [];
    (allSkills || []).forEach(skill => {
        const input = document.getElementById(`skill_minutes_${skill.id}`);
        if (!input) return;
        const minutes = parseInt(input.value || '0', 10);
        if (minutes > 0) {
            skills.push({ skillId: skill.id, minutesPracticed: minutes });
        }
    });

    const payload = {
        userId: parseInt(currentUserId, 10),
        logDate,
        hoursCoded: Number(hoursCoded),
        focusScore: focusScore ? parseInt(focusScore, 10) : null,
        notes,
        skills
    };

    try {
        const resp = await fetch(`${API_BASE_URL}/daily-logs`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!resp.ok) {
            const msg = await safeReadError(resp);
            showDailyLogError(msg || 'Failed to save log.');
            return;
        }

        const modalEl = document.getElementById('dailyLogModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();

        // reset minutes inputs for next time
        (allSkills || []).forEach(skill => {
            const input = document.getElementById(`skill_minutes_${skill.id}`);
            if (input) input.value = '';
        });

        await refreshDashboard();
    } catch (e) {
        console.error(e);
        showDailyLogError('Network error while saving log.');
    }
}

async function createSkill() {
    const errorEl = document.getElementById('skillError');
    if (errorEl) {
        errorEl.style.display = 'none';
        errorEl.textContent = '';
    }

    const name = (document.getElementById('newSkillName').value || '').trim();
    const category = (document.getElementById('newSkillCategory').value || '').trim();
    const colorHex = document.getElementById('newSkillColor').value || '#6c757d';

    if (!name) {
        showSkillError('Skill name is required.');
        return;
    }

    try {
        const resp = await fetch(`${API_BASE_URL}/skills`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, category, colorHex })
        });

        if (!resp.ok) {
            const msg = await safeReadError(resp);
            showSkillError(msg || 'Failed to create skill.');
            return;
        }

        document.getElementById('newSkillName').value = '';
        document.getElementById('newSkillCategory').value = '';

        await loadSkills();
        await refreshDashboard();
    } catch (error) {
        console.error('Error creating skill:', error);
        showSkillError('Network error while creating skill.');
    }
}

async function deleteSkill(skillId) {
    if (!confirm('Delete this skill? If it is referenced by logs, deletion may fail.')) return;
    const errorEl = document.getElementById('skillError');
    if (errorEl) {
        errorEl.style.display = 'none';
        errorEl.textContent = '';
    }

    try {
        const resp = await fetch(`${API_BASE_URL}/skills/${skillId}`, { method: 'DELETE' });
        if (!resp.ok) {
            const msg = await safeReadError(resp);
            showSkillError(msg || 'Failed to delete skill (it may be referenced by logs).');
            return;
        }
        await loadSkills();
        await refreshDashboard();
    } catch (e) {
        console.error(e);
        showSkillError('Network error while deleting skill.');
    }
}

function showDailyLogError(msg) {
    const errorEl = document.getElementById('dailyLogError');
    if (!errorEl) return;
    errorEl.textContent = msg;
    errorEl.style.display = 'block';
}

function showSkillError(msg) {
    const errorEl = document.getElementById('skillError');
    if (!errorEl) return;
    errorEl.textContent = msg;
    errorEl.style.display = 'block';
}

async function safeReadError(resp) {
    try {
        const text = await resp.text();
        if (!text) return null;
        // Spring error responses can be JSON or plain text; avoid dumping huge payloads
        return text.length > 300 ? text.slice(0, 300) + '…' : text;
    } catch (_) {
        return null;
    }
}

function escapeHtml(str) {
    return String(str)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}
