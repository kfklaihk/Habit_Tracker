package com.habittracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLog {
    private Long id;
    private Long userId;
    private LocalDate logDate;
    private BigDecimal hoursCoded;
    private Integer focusScore;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public LocalDate getLogDate() { return logDate; }
    public BigDecimal getHoursCoded() { return hoursCoded; }
    public Integer getFocusScore() { return focusScore; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public void setHoursCoded(BigDecimal hoursCoded) { this.hoursCoded = hoursCoded; }
    public void setFocusScore(Integer focusScore) { this.focusScore = focusScore; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

