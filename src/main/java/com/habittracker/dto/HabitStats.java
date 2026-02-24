package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitStats {
    private Long habitId;
    private String habitName;
    private Integer currentStreak;
    private Integer longestStreak;
    private Integer totalCompletions;
    private Double completionRate;
    private LocalDate lastCompletionDate;
    private List<DateValue> heatmapData;
    
    // Explicit getters
    public Long getHabitId() { return habitId; }
    public String getHabitName() { return habitName; }
    public Integer getCurrentStreak() { return currentStreak; }
    public Integer getLongestStreak() { return longestStreak; }
    public Integer getTotalCompletions() { return totalCompletions; }
    public Double getCompletionRate() { return completionRate; }
    public LocalDate getLastCompletionDate() { return lastCompletionDate; }
    public List<DateValue> getHeatmapData() { return heatmapData; }
    
    // Explicit setters
    public void setHabitId(Long habitId) { this.habitId = habitId; }
    public void setHabitName(String habitName) { this.habitName = habitName; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    public void setTotalCompletions(Integer totalCompletions) { this.totalCompletions = totalCompletions; }
    public void setCompletionRate(Double completionRate) { this.completionRate = completionRate; }
    public void setLastCompletionDate(LocalDate lastCompletionDate) { this.lastCompletionDate = lastCompletionDate; }
    public void setHeatmapData(List<DateValue> heatmapData) { this.heatmapData = heatmapData; }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateValue {
        private LocalDate date;
        private Integer value;
        
        // Explicit getters
        public LocalDate getDate() { return date; }
        public Integer getValue() { return value; }
        
        // Explicit setters
        public void setDate(LocalDate date) { this.date = date; }
        public void setValue(Integer value) { this.value = value; }
    }
}
