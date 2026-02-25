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
public class WeeklyHours {
    private String weekLabel;
    private BigDecimal totalHours;

    public String getWeekLabel() { return weekLabel; }
    public BigDecimal getTotalHours() { return totalHours; }

    public void setWeekLabel(String weekLabel) { this.weekLabel = weekLabel; }
    public void setTotalHours(BigDecimal totalHours) { this.totalHours = totalHours; }
}

