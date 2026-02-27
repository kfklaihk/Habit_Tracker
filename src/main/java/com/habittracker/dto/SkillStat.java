package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillStat {
    private String skillName;
    private String color;
    private Long totalMinutes;

    public String getSkillName() { return skillName; }
    public String getColor() { return color; }
    public Long getTotalMinutes() { return totalMinutes; }

    public void setSkillName(String skillName) { this.skillName = skillName; }
    public void setColor(String color) { this.color = color; }
    public void setTotalMinutes(Long totalMinutes) { this.totalMinutes = totalMinutes; }
}

