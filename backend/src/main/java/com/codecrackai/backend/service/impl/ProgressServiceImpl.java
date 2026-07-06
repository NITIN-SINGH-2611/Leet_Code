package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.UpdateProgressRequest;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import com.codecrackai.backend.repository.UserRepository;
import com.codecrackai.backend.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgressServiceImpl implements ProgressService {

    private final UserQuestionProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    /** Spaced-repetition intervals (days) indexed by attempt count, capped at the last value. */
    private static final int[] REVISION_INTERVALS_DAYS = {1, 3, 7, 14, 30};

    /** XP awarded the first time a question of each difficulty is solved. */
    private static final Map<Difficulty, Integer> XP_REWARDS = Map.of(
            Difficulty.EASY, 10,
            Difficulty.MEDIUM, 20,
            Difficulty.HARD, 35
    );

    /** XP required to advance one level (flat curve for now: level = xp/100 + 1). */
    private static final int XP_PER_LEVEL = 100;

    @Override
    public UserQuestionProgress getOrCreate(Long userId, Long questionId) {
        return progressRepository.findByUserIdAndQuestionId(userId, questionId)
                .orElseGet(() -> createFresh(userId, questionId));
    }

    @Override
    public UserQuestionProgress updateProgress(Long userId, Long questionId, UpdateProgressRequest request) {
        UserQuestionProgress progress = getOrCreate(userId, questionId);
        ProgressStatus previousStatus = progress.getStatus();

        if (request.getStatus() != null) {
            progress.setStatus(request.getStatus());
            progress.setLastAttemptedAt(Instant.now());
            if (request.getStatus() == ProgressStatus.ATTEMPTED || request.getStatus() == ProgressStatus.SOLVED) {
                progress.setAttempts(progress.getAttempts() + 1);
            }
            if (request.getStatus() == ProgressStatus.SOLVED) {
                progress.setRevisionDueDate(nextRevisionDate(progress.getAttempts()));

                boolean firstTimeSolve = previousStatus != ProgressStatus.SOLVED;
                if (firstTimeSolve) {
                    awardXpAndUpdateStreak(progress.getUser(), progress.getQuestion().getDifficulty());
                }
            }
        }
        if (request.getConfidenceRating() != null) {
            progress.setConfidenceRating(request.getConfidenceRating());
        }
        if (request.getBookmarked() != null) {
            progress.setBookmarked(request.getBookmarked());
        }
        if (request.getPersonalNotes() != null) {
            progress.setPersonalNotes(request.getPersonalNotes());
        }

        return progressRepository.save(progress);
    }

    private void awardXpAndUpdateStreak(User user, Difficulty difficulty) {
        int xpGain = XP_REWARDS.getOrDefault(difficulty, 10);
        user.setXp(user.getXp() + xpGain);
        user.setLevel(user.getXp() / XP_PER_LEVEL + 1);

        LocalDate today = LocalDate.now();
        LocalDate last = user.getLastActiveDate();
        if (last == null || last.isBefore(today.minusDays(1))) {
            user.setCurrentStreak(1);
        } else if (last.equals(today.minusDays(1))) {
            user.setCurrentStreak(user.getCurrentStreak() + 1);
        } // last == today: streak already counted today, no change

        user.setLongestStreak(Math.max(user.getLongestStreak(), user.getCurrentStreak()));
        user.setLastActiveDate(today);
        userRepository.save(user);
    }

    private UserQuestionProgress createFresh(Long userId, Long questionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User", userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> ResourceNotFoundException.of("Question", questionId));

        UserQuestionProgress fresh = UserQuestionProgress.builder()
                .user(user)
                .question(question)
                .status(ProgressStatus.NOT_STARTED)
                .build();
        return progressRepository.save(fresh);
    }

    private LocalDate nextRevisionDate(int attempts) {
        int index = Math.min(attempts - 1, REVISION_INTERVALS_DAYS.length - 1);
        return LocalDate.now().plusDays(REVISION_INTERVALS_DAYS[Math.max(index, 0)]);
    }
}
