package com.habittracker.mapper;

import com.habittracker.domain.LogSkill;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogSkillMapper {

    @Select("SELECT * FROM log_skills WHERE daily_log_id = #{dailyLogId}")
    List<LogSkill> findByDailyLogId(Long dailyLogId);

    @Delete("DELETE FROM log_skills WHERE daily_log_id = #{dailyLogId}")
    void deleteByDailyLogId(Long dailyLogId);

    @Insert("INSERT INTO log_skills (daily_log_id, skill_id, minutes_practiced) VALUES (#{dailyLogId}, #{skillId}, #{minutesPracticed})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(LogSkill logSkill);
}

