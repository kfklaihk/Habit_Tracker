package com.habittracker.mapper;

import com.habittracker.domain.HabitEntry;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface HabitEntryMapper {
    
    @Select("SELECT * FROM habit_entries WHERE id = #{id}")
    Optional<HabitEntry> findById(Long id);
    
    @Select("SELECT * FROM habit_entries WHERE habit_id = #{habitId} " +
            "ORDER BY entry_date DESC")
    List<HabitEntry> findByHabitId(Long habitId);
    
    @Select("SELECT * FROM habit_entries WHERE habit_id = #{habitId} " +
            "AND entry_date BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY entry_date ASC")
    List<HabitEntry> findByHabitIdAndDateRange(
            @Param("habitId") Long habitId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Select("SELECT * FROM habit_entries WHERE habit_id = #{habitId} " +
            "AND entry_date = #{entryDate}")
    Optional<HabitEntry> findByHabitIdAndDate(
            @Param("habitId") Long habitId,
            @Param("entryDate") LocalDate entryDate);
    
    @Insert("INSERT INTO habit_entries (habit_id, entry_date, completed, notes, created_at) " +
            "VALUES (#{habitId}, #{entryDate}, #{completed}, #{notes}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(HabitEntry habitEntry);
    
    @Update("UPDATE habit_entries SET completed = #{completed}, notes = #{notes} " +
            "WHERE id = #{id}")
    void update(HabitEntry habitEntry);
    
    @Delete("DELETE FROM habit_entries WHERE id = #{id}")
    void delete(Long id);
    
    @Select("SELECT COUNT(*) FROM habit_entries WHERE habit_id = #{habitId} AND completed = TRUE")
    int countCompletedByHabitId(Long habitId);
    
    @Select("SELECT entry_date FROM habit_entries WHERE habit_id = #{habitId} " +
            "AND completed = TRUE ORDER BY entry_date DESC")
    List<LocalDate> findCompletedDatesByHabitId(Long habitId);
}
