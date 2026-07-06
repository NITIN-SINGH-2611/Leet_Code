package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.response.AnalyticsResponse;
import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.security.CurrentUser;
import com.codecrackai.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ApiResponse<AnalyticsResponse> getAnalytics(@CurrentUser Long userId) {
        return ApiResponse.success(analyticsService.getAnalytics(userId));
    }
}
