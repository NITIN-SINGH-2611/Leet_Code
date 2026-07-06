package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.response.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getAnalytics(Long userId);
}
