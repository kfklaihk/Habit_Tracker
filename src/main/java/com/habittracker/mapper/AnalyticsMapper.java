package com.habittracker.mapper;

import com.habittracker.dto.SkillStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AnalyticsMapper {

    @Select("""
            SELECT
              s.name AS skillName,
              s.color_hex AS color,
              SUM(ls.minutes_practiced) AS totalMinutes
            FROM log_skills ls
            JOIN skills s ON s.id = ls.skill_id
            JOIN daily_logs dl ON dl.id = ls.daily_log_id
            WHERE dl.user_id = #{userId}
              AND dl.log_date >= #{sinceDate}
            GROUP BY s.id, s.name, s.color_hex
            ORDER BY totalMinutes DESC
            LIMIT 10
            """)
    List<SkillStat> getSkillDistribution(@Param("userId") Long userId, @Param("sinceDate") LocalDate sinceDate);

    @Select("SELECT log_date FROM daily_logs WHERE user_id = #{userId} AND hours_coded > 0 ORDER BY log_date DESC")
    List<LocalDate> getLoggedDates(@Param("userId") Long userId);
}

