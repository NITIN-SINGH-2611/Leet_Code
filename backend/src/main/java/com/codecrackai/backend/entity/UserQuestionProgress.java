package com.codecrackai.backend.entity;

import com.codecrackai.backend.entity.enums.ProgressStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Tracks one user's personal journey on one question: attempts, confidence,
 * notes, bookmark state, and the next spaced-repetition revision date.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_question_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "question_id"}))
public class UserQuestionProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    @Builder.Default
    private int attempts = 0;

    /** Self-rated confidence 1 (guessed) - 5 (could solve blindfolded). */
    private Integer confidenceRating;

    @Builder.Default
    private boolean bookmarked = false;

    @Lob
    private String personalNotes;

    private Instant lastAttemptedAt;

    private LocalDate revisionDueDate;
}
