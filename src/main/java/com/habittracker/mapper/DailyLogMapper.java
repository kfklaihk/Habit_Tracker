package com.habittracker.mapper;

import com.habittracker.domain.DailyLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface DailyLogMapper {

    @Select("SELECT * FROM daily_logs WHERE id = #{id}")
    Optional<DailyLog> findById(Long id);

    @Select("SELECT * FROM daily_logs WHERE user_id = #{userId} AND log_date = #{logDate}")
    Optional<DailyLog> findByUserIdAndDate(@Param("userId") Long userId, @Param("logDate") LocalDate logDate);

    @Select("SELECT * FROM daily_logs WHERE user_id = #{userId} AND log_date BETWEEN #{startDate} AND #{endDate} ORDER BY log_date ASC")
    List<DailyLog> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Insert("INSERT INTO daily_logs (user_id, log_date, hours_coded, focus_score, notes, created_at, updated_at) " +
            "VALUES (#{userId}, #{logDate}, #{hoursCoded}, #{focusScore}, #{notes}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(DailyLog dailyLog);

    @Update("UPDATE daily_logs SET hours_coded = #{hoursCoded}, focus_score = #{focusScore}, notes = #{notes}, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void update(DailyLog dailyLog);

    @Select("SELECT COUNT(*) FROM daily_logs WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}

