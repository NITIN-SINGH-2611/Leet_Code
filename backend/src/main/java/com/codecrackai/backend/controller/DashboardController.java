package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.DashboardSummaryResponse;
import com.codecrackai.backend.security.CurrentUser;
import com.codecrackai.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> summary(@CurrentUser Long userId) {
        return ApiResponse.success(dashboardService.getSummary(userId));
    }
}
