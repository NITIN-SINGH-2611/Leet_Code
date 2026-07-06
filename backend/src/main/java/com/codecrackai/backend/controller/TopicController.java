package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.dto.response.TopicResponse;
import com.codecrackai.backend.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ApiResponse<List<TopicResponse>> getAllTopics() {
        return ApiResponse.success(topicService.getAllRootTopics());
    }

    @GetMapping("/{slug}")
    public ApiResponse<TopicResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.success(topicService.getBySlug(slug));
    }
}
