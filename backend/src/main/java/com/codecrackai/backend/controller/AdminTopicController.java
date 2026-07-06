package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.request.TopicUpsertRequest;
import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.TopicResponse;
import com.codecrackai.backend.service.TopicService;
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
@RequestMapping("/api/v1/admin/topics")
@RequiredArgsConstructor
public class AdminTopicController {

    private final TopicService topicService;

    @PostMapping
    public ApiResponse<TopicResponse> create(@Valid @RequestBody TopicUpsertRequest request) {
        return ApiResponse.success("Topic created", topicService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<TopicResponse> update(@PathVariable Long id, @Valid @RequestBody TopicUpsertRequest request) {
        return ApiResponse.success("Topic updated", topicService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ApiResponse.success("Topic deleted", null);
    }
}
