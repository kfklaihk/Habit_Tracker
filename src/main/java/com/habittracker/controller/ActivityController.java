package com.habittracker.controller;

import com.habittracker.dto.ActivityPoint;
import com.habittracker.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final AnalyticsMapper analyticsMapper;

    @GetMapping
    public ResponseEntity<List<ActivityPoint>> getActivity(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        if (isAllUsers(userId)) {
            return ResponseEntity.ok(analyticsMapper.getDailyActivityAllUsers(start, end));
        }
        return ResponseEntity.ok(analyticsMapper.getDailyActivity(Long.parseLong(userId), start, end));
    }

    private boolean isAllUsers(String userId) {
        return userId == null || userId.isBlank() || "ALL".equalsIgnoreCase(userId);
    }
}

