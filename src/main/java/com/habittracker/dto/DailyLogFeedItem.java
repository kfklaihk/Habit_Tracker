package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogFeedItem {
    private Long id;
    private Long userId;
    private String username;
    private LocalDate logDate;
    private BigDecimal hoursCoded;
    private Integer focusScore;
    private String notes;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public LocalDate getLogDate() { return logDate; }
    public BigDecimal getHoursCoded() { return hoursCoded; }
    public Integer getFocusScore() { return focusScore; }
    public String getNotes() { return notes; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public void setHoursCoded(BigDecimal hoursCoded) { this.hoursCoded = hoursCoded; }
    public void setFocusScore(Integer focusScore) { this.focusScore = focusScore; }
    public void setNotes(String notes) { this.notes = notes; }
}

