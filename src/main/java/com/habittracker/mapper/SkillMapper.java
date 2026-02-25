package com.habittracker.mapper;

import com.habittracker.domain.Skill;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SkillMapper {

    @Select("SELECT * FROM skills ORDER BY name ASC")
    List<Skill> findAll();

    @Select("SELECT * FROM skills WHERE id = #{id}")
    Optional<Skill> findById(Long id);

    @Insert("INSERT INTO skills (name, category, color_hex) VALUES (#{name}, #{category}, #{colorHex})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Skill skill);

    @Update("UPDATE skills SET name = #{name}, category = #{category}, color_hex = #{colorHex} WHERE id = #{id}")
    void update(Skill skill);

    @Delete("DELETE FROM skills WHERE id = #{id}")
    void delete(Long id);
}

