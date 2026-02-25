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
public class ActivityPoint {
    private Long timestamp;
    private BigDecimal value;

    public Long getTimestamp() { return timestamp; }
    public BigDecimal getValue() { return value; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public void setValue(BigDecimal value) { this.value = value; }
}

