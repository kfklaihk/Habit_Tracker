package com.habittracker.service;

import com.habittracker.domain.Goal;
import com.habittracker.dto.DashboardResponse;
import com.habittracker.dto.GoalProgress;
import com.habittracker.dto.MonthlyHours;
import com.habittracker.dto.SkillStat;
import com.habittracker.mapper.AnalyticsMapper;
import com.habittracker.mapper.DailyLogMapper;
import com.habittracker.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AnalyticsMapper analyticsMapper;
    private final DailyLogMapper dailyLogMapper;
    private final GoalMapper goalMapper;

    public DashboardResponse getDashboardForUser(Long userId) {
        int totalDaysLogged = dailyLogMapper.countByUserId(userId);
        List<SkillStat> skillStats = analyticsMapper.getSkillDistribution(userId, LocalDate.now().minusDays(30));

        LocalDate sinceMonthStart = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        List<MonthlyHours> monthlyHours = analyticsMapper.getMonthlyHours(userId, sinceMonthStart);

        List<GoalProgress> activeGoals = getActiveGoalsProgressForUser(userId);

        DashboardResponse response = new DashboardResponse();
        response.setTotalDaysLogged(totalDaysLogged);
        response.setSkillStats(skillStats);
        response.setMonthlyHours(monthlyHours);
        response.setActiveGoals(activeGoals);
        return response;
    }

    public DashboardResponse getDashboardAllUsers() {
        int totalDaysLogged = dailyLogMapper.countAll();
        List<SkillStat> skillStats = analyticsMapper.getSkillDistributionAllUsers(LocalDate.now().minusDays(30));

        LocalDate sinceMonthStart = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        List<MonthlyHours> monthlyHours = analyticsMapper.getMonthlyHoursAllUsers(sinceMonthStart);

        List<GoalProgress> activeGoals = getActiveGoalsProgressAllUsers();

        DashboardResponse response = new DashboardResponse();
        response.setTotalDaysLogged(totalDaysLogged);
        response.setSkillStats(skillStats);
        response.setMonthlyHours(monthlyHours);
        response.setActiveGoals(activeGoals);
        return response;
    }

    private List<GoalProgress> getActiveGoalsProgressForUser(Long userId) {
        List<Goal> goals = goalMapper.findActiveByUserId(userId);
        if (goals.isEmpty()) {
            return List.of();
        }

        WeekFields wf = WeekFields.ISO;
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(wf.dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        BigDecimal weeklyHours = dailyLogMapper.sumHoursByUserIdAndDateRange(userId, weekStart, weekEnd)
                .setScale(1, RoundingMode.HALF_UP);

        List<GoalProgress> result = new ArrayList<>();
        for (Goal g : goals) {
            GoalProgress gp = new GoalProgress();
            gp.setGoalId(g.getId());
            gp.setName(g.getName());
            gp.setGoalType(g.getGoalType());
            gp.setTargetValue(g.getTargetValue());
            gp.setUnit(g.getUnit());

            if ("WEEKLY_HOURS".equalsIgnoreCase(g.getGoalType())) {
                gp.setProgressValue(weeklyHours);
            } else {
                gp.setProgressValue(null);
            }
            result.add(gp);
        }
        return result;
    }

    private List<GoalProgress> getActiveGoalsProgressAllUsers() {
        BigDecimal target = goalMapper.sumActiveTargetsAllUsersByGoalType("WEEKLY_HOURS");
        if (target == null || target.compareTo(BigDecimal.ZERO) <= 0) {
            return List.of();
        }

        WeekFields wf = WeekFields.ISO;
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(wf.dayOfWeek(), 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        BigDecimal weeklyHoursAll = dailyLogMapper.sumHoursAllUsersByDateRange(weekStart, weekEnd)
                .setScale(1, RoundingMode.HALF_UP);

        GoalProgress gp = new GoalProgress();
        gp.setGoalId(null);
        gp.setName("All users weekly hours");
        gp.setGoalType("WEEKLY_HOURS");
        gp.setTargetValue(target);
        gp.setUnit("hours/week");
        gp.setProgressValue(weeklyHoursAll);
        return List.of(gp);
    }
}

