package com.habittracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String color;
    private TargetFrequency targetFrequency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Explicit getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getColor() { return color; }
    public TargetFrequency getTargetFrequency() { return targetFrequency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Explicit setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setColor(String color) { this.color = color; }
    public void setTargetFrequency(TargetFrequency targetFrequency) { this.targetFrequency = targetFrequency; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum TargetFrequency {
        DAILY, WEEKLY, MONTHLY
    }
}
