package com.habittracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private Long id;
    private String name;
    private String category;
    private String colorHex;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getColorHex() { return colorHex; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
}

