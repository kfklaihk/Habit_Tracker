package com.habittracker.mapper;

import com.habittracker.dto.ActivityPoint;
import com.habittracker.dto.MonthlyHours;
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

    @Select("""
            SELECT
              s.name AS skillName,
              s.color_hex AS color,
              SUM(ls.minutes_practiced) AS totalMinutes
            FROM log_skills ls
            JOIN skills s ON s.id = ls.skill_id
            JOIN daily_logs dl ON dl.id = ls.daily_log_id
            WHERE dl.log_date >= #{sinceDate}
            GROUP BY s.id, s.name, s.color_hex
            ORDER BY totalMinutes DESC
            LIMIT 10
            """)
    List<SkillStat> getSkillDistributionAllUsers(@Param("sinceDate") LocalDate sinceDate);

    @Select("""
            SELECT
              to_char(log_date, 'YYYY-MM') AS monthLabel,
              COALESCE(SUM(hours_coded), 0) AS totalHours
            FROM daily_logs
            WHERE user_id = #{userId}
              AND log_date >= #{sinceDate}
            GROUP BY to_char(log_date, 'YYYY-MM')
            ORDER BY to_char(log_date, 'YYYY-MM') ASC
            """)
    List<MonthlyHours> getMonthlyHours(@Param("userId") Long userId, @Param("sinceDate") LocalDate sinceDate);

    @Select("""
            SELECT
              to_char(log_date, 'YYYY-MM') AS monthLabel,
              COALESCE(SUM(hours_coded), 0) AS totalHours
            FROM daily_logs
            WHERE log_date >= #{sinceDate}
            GROUP BY to_char(log_date, 'YYYY-MM')
            ORDER BY to_char(log_date, 'YYYY-MM') ASC
            """)
    List<MonthlyHours> getMonthlyHoursAllUsers(@Param("sinceDate") LocalDate sinceDate);

    @Select("""
            SELECT
              CAST(EXTRACT(EPOCH FROM (log_date::timestamp)) * 1000 AS BIGINT) AS timestamp,
              COALESCE(SUM(hours_coded), 0)::numeric AS value
            FROM daily_logs
            WHERE user_id = #{userId}
              AND log_date BETWEEN #{startDate} AND #{endDate}
            GROUP BY log_date
            ORDER BY log_date ASC
            """)
    List<ActivityPoint> getDailyActivity(@Param("userId") Long userId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Select("""
            SELECT
              CAST(EXTRACT(EPOCH FROM (log_date::timestamp)) * 1000 AS BIGINT) AS timestamp,
              COALESCE(SUM(hours_coded), 0)::numeric AS value
            FROM daily_logs
            WHERE log_date BETWEEN #{startDate} AND #{endDate}
            GROUP BY log_date
            ORDER BY log_date ASC
            """)
    List<ActivityPoint> getDailyActivityAllUsers(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
}

