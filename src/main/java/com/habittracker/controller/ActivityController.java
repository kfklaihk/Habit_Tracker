package com.habittracker.controller;

import com.habittracker.domain.DailyLog;
import com.habittracker.dto.ActivityPoint;
import com.habittracker.service.DailyLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final DailyLogService dailyLogService;

    @GetMapping
    public ResponseEntity<List<ActivityPoint>> getActivity(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<DailyLog> logs = dailyLogService.getDailyLogs(userId, start, end);
        List<ActivityPoint> points = new ArrayList<>();
        for (DailyLog log : logs) {
            if (log.getLogDate() == null || log.getHoursCoded() == null) {
                continue;
            }
            ActivityPoint p = new ActivityPoint();
            p.setTimestamp(log.getLogDate().atStartOfDay(ZoneOffset.UTC).toEpochSecond());
            p.setValue(log.getHoursCoded());
            points.add(p);
        }
        points.sort(Comparator.comparing(ActivityPoint::getTimestamp));
        return ResponseEntity.ok(points);
    }
}

