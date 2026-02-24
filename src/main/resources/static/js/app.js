const API_BASE_URL = window.location.origin + '/api';
let currentUserId = null;
let currentHabitId = null;
let calHeatmap = null;
let progressChart = null;

// Initialize the app
document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
});

// Load all users
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
            loadUserHabits();
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

// Load habits for selected user
async function loadUserHabits() {
    const userSelect = document.getElementById('userSelect');
    currentUserId = userSelect.value;
    
    if (!currentUserId) {
        document.getElementById('habitsContainer').innerHTML = 
            '<div class="col-12 text-center text-muted"><p>Select a user to view habits</p></div>';
        return;
    }
    
    document.getElementById('current-user').textContent = 
        userSelect.options[userSelect.selectedIndex].text;
    
    try {
        const response = await fetch(`${API_BASE_URL}/habits/user/${currentUserId}`);
        const habits = await response.json();
        
        displayHabits(habits);
        updateQuickStats(habits);
    } catch (error) {
        console.error('Error loading habits:', error);
    }
}

// Display habits as cards
function displayHabits(habits) {
    const container = document.getElementById('habitsContainer');
    
    if (habits.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-muted"><p>No habits yet. Add your first habit!</p></div>';
        return;
    }
    
    container.innerHTML = '';
    
    habits.forEach(habit => {
        const col = document.createElement('div');
        col.className = 'col-md-6 col-lg-4 mb-3';
        col.innerHTML = `
            <div class="card habit-card h-100" style="border-left: 4px solid ${habit.color}" onclick="showHabitDetail(${habit.id})">
                <div class="card-body">
                    <h5 class="card-title">${habit.name}</h5>
                    <p class="card-text text-muted small">${habit.description || 'No description'}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="badge bg-secondary">${habit.targetFrequency}</span>
                        <button class="btn btn-sm btn-success" onclick="event.stopPropagation(); quickLog(${habit.id})">
                            ✓ Log Today
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(col);
    });
}

// Update quick stats
async function updateQuickStats(habits) {
    document.getElementById('totalHabits').textContent = habits.length;
    
    let activeStreaks = 0;
    for (const habit of habits) {
        try {
            const response = await fetch(`${API_BASE_URL}/habits/${habit.id}/stats?days=7`);
            const stats = await response.json();
            if (stats.currentStreak > 0) {
                activeStreaks++;
            }
        } catch (error) {
            console.error('Error loading stats:', error);
        }
    }
    
    document.getElementById('activeStreaks').textContent = activeStreaks;
}

// Add new habit
async function addHabit() {
    const name = document.getElementById('habitName').value;
    const description = document.getElementById('habitDescription').value;
    const color = document.getElementById('habitColor').value;
    const frequency = document.getElementById('habitFrequency').value;
    
    if (!name || !currentUserId) {
        alert('Please fill in the habit name and select a user');
        return;
    }
    
    const habit = {
        userId: parseInt(currentUserId),
        name: name,
        description: description,
        color: color,
        targetFrequency: frequency
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/habits`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(habit)
        });
        
        if (response.ok) {
            // Close modal
            bootstrap.Modal.getInstance(document.getElementById('addHabitModal')).hide();
            
            // Reset form
            document.getElementById('addHabitForm').reset();
            
            // Reload habits
            loadUserHabits();
        }
    } catch (error) {
        console.error('Error adding habit:', error);
        alert('Error adding habit');
    }
}

// Quick log habit for today
async function quickLog(habitId) {
    try {
        const response = await fetch(`${API_BASE_URL}/habits/${habitId}/log`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                date: new Date().toISOString().split('T')[0],
                completed: true
            })
        });
        
        if (response.ok) {
            alert('Logged successfully!');
            loadUserHabits();
        }
    } catch (error) {
        console.error('Error logging habit:', error);
        alert('Error logging habit');
    }
}

// Show habit detail modal
async function showHabitDetail(habitId) {
    currentHabitId = habitId;
    
    try {
        // Load habit details
        const habitResponse = await fetch(`${API_BASE_URL}/habits/${habitId}`);
        const habit = await habitResponse.json();
        
        // Load habit stats
        const statsResponse = await fetch(`${API_BASE_URL}/habits/${habitId}/stats?days=90`);
        const stats = await statsResponse.json();
        
        // Update modal title
        document.getElementById('habitDetailTitle').textContent = habit.name;
        
        // Update stats
        document.getElementById('currentStreak').textContent = `${stats.currentStreak} days`;
        document.getElementById('longestStreak').textContent = `${stats.longestStreak} days`;
        document.getElementById('totalCompletions').textContent = stats.totalCompletions;
        document.getElementById('completionRate').textContent = `${stats.completionRate}%`;
        
        // Initialize Cal-Heatmap
        initializeCalHeatmap(stats.heatmapData);
        
        // Initialize Chart.js
        initializeProgressChart(stats.heatmapData);
        
        // Show modal
        new bootstrap.Modal(document.getElementById('habitDetailModal')).show();
    } catch (error) {
        console.error('Error loading habit details:', error);
    }
}

// Initialize Cal-Heatmap
function initializeCalHeatmap(heatmapData) {
    // Clear previous instance
    document.getElementById('cal-heatmap').innerHTML = '';
    
    // Prepare data for Cal-Heatmap
    const data = heatmapData.map(item => ({
        date: item.date,
        value: item.value
    }));
    
    // Initialize Cal-Heatmap v4
    calHeatmap = new CalHeatmap();
    calHeatmap.paint({
        itemSelector: '#cal-heatmap',
        domain: {
            type: 'month',
            gutter: 8,
            label: { text: 'MMM', position: 'top', align: 'start' }
        },
        subDomain: { 
            type: 'day',
            width: 15,
            height: 15,
            radius: 2
        },
        data: {
            source: data,
            x: 'date',
            y: 'value'
        },
        scale: {
            color: {
                type: 'threshold',
                range: ['#ebedf0', '#39d353'],
                domain: [1]
            }
        },
        range: 3,
        date: { start: new Date(new Date().setMonth(new Date().getMonth() - 2)) }
    });
}

// Initialize Progress Chart
function initializeProgressChart(heatmapData) {
    const ctx = document.getElementById('progressChart').getContext('2d');
    
    // Destroy previous chart if exists
    if (progressChart) {
        progressChart.destroy();
    }
    
    // Prepare data for Chart.js
    const last30Days = heatmapData.slice(-30);
    const labels = last30Days.map(item => {
        const date = new Date(item.date);
        return `${date.getMonth() + 1}/${date.getDate()}`;
    });
    const data = last30Days.map(item => item.value);
    
    progressChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Completions',
                data: data,
                borderColor: '#4CAF50',
                backgroundColor: 'rgba(76, 175, 80, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 1,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}

// Log today
async function logToday() {
    await quickLog(currentHabitId);
    showHabitDetail(currentHabitId);
}

// Delete habit
async function deleteHabit() {
    if (!confirm('Are you sure you want to delete this habit? This action cannot be undone.')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/habits/${currentHabitId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            bootstrap.Modal.getInstance(document.getElementById('habitDetailModal')).hide();
            loadUserHabits();
        }
    } catch (error) {
        console.error('Error deleting habit:', error);
        alert('Error deleting habit');
    }
}
