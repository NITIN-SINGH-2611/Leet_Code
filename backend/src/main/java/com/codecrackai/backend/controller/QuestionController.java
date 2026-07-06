package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ApiResponse<Page<QuestionSummaryResponse>> search(
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String pattern,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.success(
                questionService.search(difficulty, topicId, company, pattern, search, pageable));
    }

    @GetMapping("/{slug}")
    public ApiResponse<QuestionDetailResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.success(questionService.getBySlug(slug));
    }
}
