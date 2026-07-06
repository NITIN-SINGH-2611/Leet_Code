package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.StudyPlanResponse;
import com.codecrackai.backend.security.CurrentUser;
import com.codecrackai.backend.service.StudyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/study-plan")
@RequiredArgsConstructor
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    @GetMapping
    public ApiResponse<StudyPlanResponse> getPlan(@CurrentUser Long userId) {
        return ApiResponse.success(studyPlanService.getPlanForUser(userId));
    }
}
