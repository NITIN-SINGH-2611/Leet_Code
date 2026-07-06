package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.request.TopicUpsertRequest;
import com.codecrackai.backend.dto.response.TopicResponse;

import java.util.List;

public interface TopicService {
    List<TopicResponse> getAllRootTopics();
    TopicResponse getBySlug(String slug);

    // --- Admin (Phase 5) ---
    TopicResponse create(TopicUpsertRequest request);
    TopicResponse update(Long id, TopicUpsertRequest request);
    void delete(Long id);
}
