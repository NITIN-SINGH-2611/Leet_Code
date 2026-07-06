package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.request.QuestionUpsertRequest;
import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.entity.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Page<QuestionSummaryResponse> search(Difficulty difficulty,
                                          Long topicId,
                                          String company,
                                          String pattern,
                                          String search,
                                          Pageable pageable);

    QuestionDetailResponse getBySlug(String slug);

    // --- Admin (Phase 5) ---
    QuestionDetailResponse create(QuestionUpsertRequest request);
    QuestionDetailResponse update(Long id, QuestionUpsertRequest request);
    void delete(Long id);
}
