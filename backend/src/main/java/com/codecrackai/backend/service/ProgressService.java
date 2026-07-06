package com.codecrackai.backend.service;

import com.codecrackai.backend.dto.request.UpdateProgressRequest;
import com.codecrackai.backend.entity.UserQuestionProgress;

public interface ProgressService {

    /**
     * Fetches the user's progress row for a question, creating a fresh
     * NOT_STARTED row on first access.
     */
    UserQuestionProgress getOrCreate(Long userId, Long questionId);

    UserQuestionProgress updateProgress(Long userId, Long questionId, UpdateProgressRequest request);
}
