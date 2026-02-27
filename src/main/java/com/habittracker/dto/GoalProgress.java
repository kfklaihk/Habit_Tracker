package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgress {
    private Long goalId;
    private String name;
    private String goalType;
    private BigDecimal targetValue;
    private String unit;
    private BigDecimal progressValue;

    public Long getGoalId() { return goalId; }
    public String getName() { return name; }
    public String getGoalType() { return goalType; }
    public BigDecimal getTargetValue() { return targetValue; }
    public String getUnit() { return unit; }
    public BigDecimal getProgressValue() { return progressValue; }

    public void setGoalId(Long goalId) { this.goalId = goalId; }
    public void setName(String name) { this.name = name; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    public void setTargetValue(BigDecimal targetValue) { this.targetValue = targetValue; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setProgressValue(BigDecimal progressValue) { this.progressValue = progressValue; }
}

