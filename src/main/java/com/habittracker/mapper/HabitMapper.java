package com.habittracker.mapper;

import com.habittracker.domain.Habit;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HabitMapper {
    
    @Select("SELECT * FROM habits WHERE id = #{id}")
    Optional<Habit> findById(Long id);
    
    @Select("SELECT * FROM habits WHERE user_id = #{userId}")
    List<Habit> findByUserId(Long userId);
    
    @Select("SELECT * FROM habits")
    List<Habit> findAll();
    
    @Insert("INSERT INTO habits (user_id, name, description, color, target_frequency, " +
            "created_at, updated_at) VALUES (#{userId}, #{name}, #{description}, #{color}, " +
            "#{targetFrequency}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Habit habit);
    
    @Update("UPDATE habits SET name = #{name}, description = #{description}, " +
            "color = #{color}, target_frequency = #{targetFrequency}, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void update(Habit habit);
    
    @Delete("DELETE FROM habits WHERE id = #{id}")
    void delete(Long id);
}
