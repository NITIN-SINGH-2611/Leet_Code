package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.response.StudyPlanResponse;

public interface StudyPlanService {
    StudyPlanResponse getPlanForUser(Long userId);
}
