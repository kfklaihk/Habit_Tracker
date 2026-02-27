package com.habittracker.mapper;

import com.habittracker.domain.Goal;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper
public interface GoalMapper {

    @Select("SELECT * FROM goals WHERE id = #{id}")
    Optional<Goal> findById(Long id);

    @Select("SELECT * FROM goals WHERE user_id = #{userId} AND active = TRUE ORDER BY created_at DESC")
    List<Goal> findActiveByUserId(Long userId);

    @Select("SELECT * FROM goals WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Goal> findByUserId(Long userId);

    @Select("SELECT COALESCE(SUM(target_value), 0) FROM goals WHERE active = TRUE AND UPPER(goal_type) = UPPER(#{goalType})")
    BigDecimal sumActiveTargetsAllUsersByGoalType(@Param("goalType") String goalType);

    @Insert("INSERT INTO goals (user_id, name, description, goal_type, target_value, unit, start_date, end_date, active, created_at, updated_at) " +
            "VALUES (#{userId}, #{name}, #{description}, #{goalType}, #{targetValue}, #{unit}, #{startDate}, #{endDate}, #{active}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Goal goal);

    @Update("UPDATE goals SET name = #{name}, description = #{description}, goal_type = #{goalType}, target_value = #{targetValue}, " +
            "unit = #{unit}, start_date = #{startDate}, end_date = #{endDate}, active = #{active}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void update(Goal goal);

    @Delete("DELETE FROM goals WHERE id = #{id}")
    void delete(Long id);
}

