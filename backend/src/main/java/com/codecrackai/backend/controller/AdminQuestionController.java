package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.request.QuestionUpsertRequest;
import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionDetailResponse> create(@Valid @RequestBody QuestionUpsertRequest request) {
        return ApiResponse.success("Question created", questionService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<QuestionDetailResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody QuestionUpsertRequest request) {
        return ApiResponse.success("Question updated", questionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ApiResponse.success("Question deleted", null);
    }
}
