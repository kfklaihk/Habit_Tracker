package com.habittracker.service;

import com.habittracker.domain.Habit;
import com.habittracker.domain.HabitEntry;
import com.habittracker.dto.HabitStats;
import com.habittracker.mapper.HabitEntryMapper;
import com.habittracker.mapper.HabitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitService {
    
    private final HabitMapper habitMapper;
    private final HabitEntryMapper habitEntryMapper;
    
    // Explicit constructor
    public HabitService(HabitMapper habitMapper, HabitEntryMapper habitEntryMapper) {
        this.habitMapper = habitMapper;
        this.habitEntryMapper = habitEntryMapper;
    }
    
    public List<Habit> getAllHabits() {
        return habitMapper.findAll();
    }
    
    public List<Habit> getHabitsByUserId(Long userId) {
        return habitMapper.findByUserId(userId);
    }
    
    public Optional<Habit> getHabitById(Long id) {
        return habitMapper.findById(id);
    }
    
    @Transactional
    public Habit createHabit(Habit habit) {
        habitMapper.insert(habit);
        return habit;
    }
    
    @Transactional
    public Habit updateHabit(Habit habit) {
        habitMapper.update(habit);
        return habitMapper.findById(habit.getId()).orElse(null);
    }
    
    @Transactional
    public void deleteHabit(Long id) {
        habitMapper.delete(id);
    }
    
    @Transactional
    public HabitEntry logHabitCompletion(Long habitId, LocalDate date, Boolean completed, String notes) {
        Optional<HabitEntry> existing = habitEntryMapper.findByHabitIdAndDate(habitId, date);
        
        if (existing.isPresent()) {
            HabitEntry entry = existing.get();
            entry.setCompleted(completed);
            entry.setNotes(notes);
            habitEntryMapper.update(entry);
            return entry;
        } else {
            HabitEntry entry = new HabitEntry();
            entry.setHabitId(habitId);
            entry.setEntryDate(date);
            entry.setCompleted(completed);
            entry.setNotes(notes);
            entry.setCreatedAt(LocalDateTime.now());
            habitEntryMapper.insert(entry);
            return entry;
        }
    }
    
    public List<HabitEntry> getHabitEntries(Long habitId, LocalDate startDate, LocalDate endDate) {
        return habitEntryMapper.findByHabitIdAndDateRange(habitId, startDate, endDate);
    }
    
    public HabitStats getHabitStats(Long habitId, int days) {
        Optional<Habit> habitOpt = habitMapper.findById(habitId);
        if (habitOpt.isEmpty()) {
            return null;
        }
        
        Habit habit = habitOpt.get();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        
        List<HabitEntry> entries = habitEntryMapper.findByHabitIdAndDateRange(habitId, startDate, endDate);
        List<LocalDate> completedDates = new ArrayList<>();
        
        for (HabitEntry entry : entries) {
            if (Boolean.TRUE.equals(entry.getCompleted())) {
                completedDates.add(entry.getEntryDate());
            }
        }
        
        // Calculate streaks
        int currentStreak = calculateCurrentStreak(completedDates);
        int longestStreak = calculateLongestStreak(completedDates);
        
        // Calculate completion rate
        int totalCompletions = habitEntryMapper.countCompletedByHabitId(habitId);
        long totalDays = ChronoUnit.DAYS.between(habit.getCreatedAt().toLocalDate(), LocalDate.now()) + 1;
        double completionRate = totalDays > 0 ? (totalCompletions * 100.0 / totalDays) : 0.0;
        
        // Prepare heatmap data
        List<HabitStats.DateValue> heatmapData = new ArrayList<>();
        for (HabitEntry entry : entries) {
            HabitStats.DateValue dv = new HabitStats.DateValue();
            dv.setDate(entry.getEntryDate());
            dv.setValue(Boolean.TRUE.equals(entry.getCompleted()) ? 1 : 0);
            heatmapData.add(dv);
        }
        
        LocalDate lastCompletion = completedDates.isEmpty() ? null : completedDates.get(0);
        
        HabitStats stats = new HabitStats();
        stats.setHabitId(habitId);
        stats.setHabitName(habit.getName());
        stats.setCurrentStreak(currentStreak);
        stats.setLongestStreak(longestStreak);
        stats.setTotalCompletions(totalCompletions);
        stats.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);
        stats.setLastCompletionDate(lastCompletion);
        stats.setHeatmapData(heatmapData);
        return stats;
    }
    
    private int calculateCurrentStreak(List<LocalDate> completedDates) {
        if (completedDates.isEmpty()) {
            return 0;
        }
        
        completedDates.sort((a, b) -> b.compareTo(a)); // Sort descending
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // Check if streak is active (completed today or yesterday)
        if (!completedDates.get(0).equals(today) && !completedDates.get(0).equals(yesterday)) {
            return 0;
        }
        
        int streak = 0;
        LocalDate expectedDate = completedDates.get(0);
        
        for (LocalDate date : completedDates) {
            if (date.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (date.isBefore(expectedDate)) {
                break;
            }
        }
        
        return streak;
    }
    
    private int calculateLongestStreak(List<LocalDate> completedDates) {
        if (completedDates.isEmpty()) {
            return 0;
        }
        
        completedDates.sort(LocalDate::compareTo); // Sort ascending
        
        int longestStreak = 1;
        int currentStreak = 1;
        
        for (int i = 1; i < completedDates.size(); i++) {
            long daysBetween = ChronoUnit.DAYS.between(completedDates.get(i - 1), completedDates.get(i));
            
            if (daysBetween == 1) {
                currentStreak++;
                longestStreak = Math.max(longestStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }
        
        return longestStreak;
    }
}
