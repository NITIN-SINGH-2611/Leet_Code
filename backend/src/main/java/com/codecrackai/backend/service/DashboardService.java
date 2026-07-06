package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.response.DashboardSummaryResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary(Long userId);
}
