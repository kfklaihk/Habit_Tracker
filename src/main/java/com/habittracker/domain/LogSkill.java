package com.habittracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogSkill {
    private Long id;
    private Long dailyLogId;
    private Long skillId;
    private Integer minutesPracticed;

    public Long getId() { return id; }
    public Long getDailyLogId() { return dailyLogId; }
    public Long getSkillId() { return skillId; }
    public Integer getMinutesPracticed() { return minutesPracticed; }

    public void setId(Long id) { this.id = id; }
    public void setDailyLogId(Long dailyLogId) { this.dailyLogId = dailyLogId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public void setMinutesPracticed(Integer minutesPracticed) { this.minutesPracticed = minutesPracticed; }
}

