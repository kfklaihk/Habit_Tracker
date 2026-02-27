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
public class MonthlyHours {
    private String monthLabel; // YYYY-MM
    private BigDecimal totalHours;

    public String getMonthLabel() { return monthLabel; }
    public BigDecimal getTotalHours() { return totalHours; }

    public void setMonthLabel(String monthLabel) { this.monthLabel = monthLabel; }
    public void setTotalHours(BigDecimal totalHours) { this.totalHours = totalHours; }
}

