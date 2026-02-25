package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Integer currentStreak;
    private Integer totalDaysLogged;
    private List<SkillStat> skillStats;
    private List<WeeklyHours> weeklyHours;
    private List<GoalProgress> activeGoals;

    public Integer getCurrentStreak() { return currentStreak; }
    public Integer getTotalDaysLogged() { return totalDaysLogged; }
    public List<SkillStat> getSkillStats() { return skillStats; }
    public List<WeeklyHours> getWeeklyHours() { return weeklyHours; }
    public List<GoalProgress> getActiveGoals() { return activeGoals; }

    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    public void setTotalDaysLogged(Integer totalDaysLogged) { this.totalDaysLogged = totalDaysLogged; }
    public void setSkillStats(List<SkillStat> skillStats) { this.skillStats = skillStats; }
    public void setWeeklyHours(List<WeeklyHours> weeklyHours) { this.weeklyHours = weeklyHours; }
    public void setActiveGoals(List<GoalProgress> activeGoals) { this.activeGoals = activeGoals; }
}

