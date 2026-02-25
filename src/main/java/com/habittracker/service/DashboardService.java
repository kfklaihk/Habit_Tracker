package com.habittracker.service;

import com.habittracker.domain.DailyLog;
import com.habittracker.domain.Goal;
import com.habittracker.dto.DashboardResponse;
import com.habittracker.dto.GoalProgress;
import com.habittracker.dto.SkillStat;
import com.habittracker.dto.WeeklyHours;
import com.habittracker.mapper.AnalyticsMapper;
import com.habittracker.mapper.DailyLogMapper;
import com.habittracker.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AnalyticsMapper analyticsMapper;
    private final DailyLogMapper dailyLogMapper;
    private final GoalMapper goalMapper;

    public DashboardResponse getDashboard(Long userId) {
        int totalDaysLogged = dailyLogMapper.countByUserId(userId);

        List<LocalDate> loggedDates = analyticsMapper.getLoggedDates(userId);
        int currentStreak = calculateCurrentStreak(loggedDates);

        List<SkillStat> skillStats = analyticsMapper.getSkillDistribution(userId, LocalDate.now().minusDays(30));
        List<WeeklyHours> weeklyHours = getWeeklyHoursLast12Weeks(userId);
        List<GoalProgress> activeGoals = getActiveGoalsProgress(userId);

        DashboardResponse response = new DashboardResponse();
        response.setCurrentStreak(currentStreak);
        response.setTotalDaysLogged(totalDaysLogged);
        response.setSkillStats(skillStats);
        response.setWeeklyHours(weeklyHours);
        response.setActiveGoals(activeGoals);
        return response;
    }

    private List<WeeklyHours> getWeeklyHoursLast12Weeks(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusWeeks(12);
        List<DailyLog> logs = dailyLogMapper.findByUserIdAndDateRange(userId, start, end);

        WeekFields wf = WeekFields.ISO;
        Map<String, WeeklyHoursAggregate> agg = new HashMap<>();

        for (DailyLog log : logs) {
            if (log.getLogDate() == null || log.getHoursCoded() == null) {
                continue;
            }
            if (log.getHoursCoded().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            int week = log.getLogDate().get(wf.weekOfWeekBasedYear());
            int year = log.getLogDate().get(wf.weekBasedYear());
            String label = String.format("%d-W%02d", year, week);

            WeeklyHoursAggregate a = agg.computeIfAbsent(label, k -> new WeeklyHoursAggregate(year, week));
            a.totalHours = a.totalHours.add(log.getHoursCoded());
        }

        List<WeeklyHoursAggregate> aggregates = new ArrayList<>(agg.values());
        aggregates.sort(Comparator.comparingInt((WeeklyHoursAggregate a) -> a.year).thenComparingInt(a -> a.week));

        List<WeeklyHours> result = new ArrayList<>();
        for (WeeklyHoursAggregate a : aggregates) {
            WeeklyHours wh = new WeeklyHours();
            wh.setWeekLabel(String.format("%d-W%02d", a.year, a.week));
            wh.setTotalHours(a.totalHours.setScale(1, RoundingMode.HALF_UP));
            result.add(wh);
        }
        return result;
    }

    private List<GoalProgress> getActiveGoalsProgress(Long userId) {
        List<Goal> goals = goalMapper.findActiveByUserId(userId);
        if (goals.isEmpty()) {
            return List.of();
        }

        // For WEEKLY_HOURS goals, compute current week hours.
        WeekFields wf = WeekFields.ISO;
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(wf.dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (DailyLog log : dailyLogMapper.findByUserIdAndDateRange(userId, weekStart, weekEnd)) {
            if (log.getHoursCoded() != null) {
                weeklyHours = weeklyHours.add(log.getHoursCoded());
            }
        }

        List<GoalProgress> result = new ArrayList<>();
        for (Goal g : goals) {
            GoalProgress gp = new GoalProgress();
            gp.setGoalId(g.getId());
            gp.setName(g.getName());
            gp.setGoalType(g.getGoalType());
            gp.setTargetValue(g.getTargetValue());
            gp.setUnit(g.getUnit());

            if ("WEEKLY_HOURS".equalsIgnoreCase(g.getGoalType())) {
                gp.setProgressValue(weeklyHours.setScale(1, RoundingMode.HALF_UP));
            } else {
                gp.setProgressValue(null);
            }
            result.add(gp);
        }
        return result;
    }

    private int calculateCurrentStreak(List<LocalDate> loggedDates) {
        if (loggedDates == null || loggedDates.isEmpty()) {
            return 0;
        }

        Set<LocalDate> set = new HashSet<>(loggedDates);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDate cursor;
        if (set.contains(today)) {
            cursor = today;
        } else if (set.contains(yesterday)) {
            cursor = yesterday;
        } else {
            return 0;
        }

        int streak = 0;
        while (set.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private static class WeeklyHoursAggregate {
        private final int year;
        private final int week;
        private BigDecimal totalHours = BigDecimal.ZERO;

        private WeeklyHoursAggregate(int year, int week) {
            this.year = year;
            this.week = week;
        }
    }
}

