package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogUpsertRequest {
    private Long userId;
    private LocalDate logDate;
    private BigDecimal hoursCoded;
    private Integer focusScore;
    private String notes;
    private List<SkillMinutes> skills;

    public Long getUserId() { return userId; }
    public LocalDate getLogDate() { return logDate; }
    public BigDecimal getHoursCoded() { return hoursCoded; }
    public Integer getFocusScore() { return focusScore; }
    public String getNotes() { return notes; }
    public List<SkillMinutes> getSkills() { return skills; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public void setHoursCoded(BigDecimal hoursCoded) { this.hoursCoded = hoursCoded; }
    public void setFocusScore(Integer focusScore) { this.focusScore = focusScore; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setSkills(List<SkillMinutes> skills) { this.skills = skills; }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillMinutes {
        private Long skillId;
        private Integer minutesPracticed;

        public Long getSkillId() { return skillId; }
        public Integer getMinutesPracticed() { return minutesPracticed; }

        public void setSkillId(Long skillId) { this.skillId = skillId; }
        public void setMinutesPracticed(Integer minutesPracticed) { this.minutesPracticed = minutesPracticed; }
    }
}

