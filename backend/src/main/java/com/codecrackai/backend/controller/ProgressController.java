package com.codecrackai.backend.controller;

import com.codecrackai.backend.dto.request.UpdateProgressRequest;
import com.codecrackai.backend.dto.response.ApiResponse;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.security.CurrentUser;
import com.codecrackai.backend.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The authenticated user is now resolved from the JWT via @CurrentUser —
 * no more client-supplied userId, as promised back in Phase 2.
 */
@RestController
@RequestMapping("/api/v1/me/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/{questionId}")
    public ApiResponse<ProgressView> get(@PathVariable Long questionId, @CurrentUser Long userId) {
        return ApiResponse.success(ProgressView.from(progressService.getOrCreate(userId, questionId)));
    }

    @PutMapping("/{questionId}")
    public ApiResponse<ProgressView> update(@PathVariable Long questionId,
                                             @CurrentUser Long userId,
                                             @Valid @RequestBody UpdateProgressRequest request) {
        return ApiResponse.success(
                ProgressView.from(progressService.updateProgress(userId, questionId, request)));
    }

    /** Small inline projection so we don't leak the JPA entity graph over the wire. */
    private record ProgressView(Long questionId, ProgressStatus status, int attempts,
                                 Integer confidenceRating, boolean bookmarked, String personalNotes) {
        static ProgressView from(UserQuestionProgress p) {
            return new ProgressView(p.getQuestion().getId(), p.getStatus(), p.getAttempts(),
                    p.getConfidenceRating(), p.isBookmarked(), p.getPersonalNotes());
        }
    }
}
