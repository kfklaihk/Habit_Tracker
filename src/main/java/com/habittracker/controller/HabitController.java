package com.habittracker.controller;

import com.habittracker.domain.Habit;
import com.habittracker.domain.HabitEntry;
import com.habittracker.dto.HabitStats;
import com.habittracker.service.HabitService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HabitController {
    
    private final HabitService habitService;
    
    // Explicit constructor
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }
    
    @GetMapping
    public ResponseEntity<List<Habit>> getAllHabits() {
        return ResponseEntity.ok(habitService.getAllHabits());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Habit>> getHabitsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(habitService.getHabitsByUserId(userId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Habit> getHabitById(@PathVariable Long id) {
        return habitService.getHabitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Habit> createHabit(@RequestBody Habit habit) {
        Habit created = habitService.createHabit(habit);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Habit> updateHabit(@PathVariable Long id, @RequestBody Habit habit) {
        habit.setId(id);
        Habit updated = habitService.updateHabit(habit);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/log")
    public ResponseEntity<HabitEntry> logHabitCompletion(
            @PathVariable Long id,
            @RequestBody LogRequest request) {
        HabitEntry entry = habitService.logHabitCompletion(
                id,
                request.getDate() != null ? request.getDate() : LocalDate.now(),
                request.getCompleted() != null ? request.getCompleted() : true,
                request.getNotes()
        );
        return ResponseEntity.ok(entry);
    }
    
    @GetMapping("/{id}/entries")
    public ResponseEntity<List<HabitEntry>> getHabitEntries(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        
        return ResponseEntity.ok(habitService.getHabitEntries(id, start, end));
    }
    
    @GetMapping("/{id}/stats")
    public ResponseEntity<HabitStats> getHabitStats(
            @PathVariable Long id,
            @RequestParam(defaultValue = "90") int days) {
        HabitStats stats = habitService.getHabitStats(id, days);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }
    
    @Data
    public static class LogRequest {
        private LocalDate date;
        private Boolean completed;
        private String notes;
        
        // Explicit getters
        public LocalDate getDate() { return date; }
        public Boolean getCompleted() { return completed; }
        public String getNotes() { return notes; }
        
        // Explicit setters
        public void setDate(LocalDate date) { this.date = date; }
        public void setCompleted(Boolean completed) { this.completed = completed; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
