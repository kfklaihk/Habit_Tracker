package com.habittracker.controller;

import com.habittracker.dto.DashboardResponse;
import com.habittracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam String userId) {
        if (userId == null || userId.isBlank() || "ALL".equalsIgnoreCase(userId)) {
            return ResponseEntity.ok(dashboardService.getDashboardAllUsers());
        }
        return ResponseEntity.ok(dashboardService.getDashboardForUser(Long.parseLong(userId)));
    }
}

