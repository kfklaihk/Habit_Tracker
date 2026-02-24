package com.habittracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitEntry {
    private Long id;
    private Long habitId;
    private LocalDate entryDate;
    private Boolean completed;
    private String notes;
    private LocalDateTime createdAt;
    
    // Explicit getters
    public Long getId() { return id; }
    public Long getHabitId() { return habitId; }
    public LocalDate getEntryDate() { return entryDate; }
    public Boolean getCompleted() { return completed; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Explicit setters
    public void setId(Long id) { this.id = id; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
