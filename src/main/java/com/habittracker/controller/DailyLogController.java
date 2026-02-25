package com.habittracker.controller;

import com.habittracker.domain.DailyLog;
import com.habittracker.dto.DailyLogUpsertRequest;
import com.habittracker.service.DailyLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DailyLogController {

    private final DailyLogService dailyLogService;

    @GetMapping
    public ResponseEntity<List<DailyLog>> getDailyLogs(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        LocalDate start = startDate != null ? startDate : end.minusDays(30);
        return ResponseEntity.ok(dailyLogService.getDailyLogs(userId, start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyLog> getDailyLogById(@PathVariable Long id) {
        return dailyLogService.getDailyLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DailyLog> upsertDailyLog(@RequestBody DailyLogUpsertRequest request) {
        DailyLog saved = dailyLogService.upsertDailyLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

