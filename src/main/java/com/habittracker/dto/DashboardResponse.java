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
    private Integer totalDaysLogged;
    private List<SkillStat> skillStats;
    private List<MonthlyHours> monthlyHours;
    private List<GoalProgress> activeGoals;

    public Integer getTotalDaysLogged() { return totalDaysLogged; }
    public List<SkillStat> getSkillStats() { return skillStats; }
    public List<MonthlyHours> getMonthlyHours() { return monthlyHours; }
    public List<GoalProgress> getActiveGoals() { return activeGoals; }

    public void setTotalDaysLogged(Integer totalDaysLogged) { this.totalDaysLogged = totalDaysLogged; }
    public void setSkillStats(List<SkillStat> skillStats) { this.skillStats = skillStats; }
    public void setMonthlyHours(List<MonthlyHours> monthlyHours) { this.monthlyHours = monthlyHours; }
    public void setActiveGoals(List<GoalProgress> activeGoals) { this.activeGoals = activeGoals; }
}

