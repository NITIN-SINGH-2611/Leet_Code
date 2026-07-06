package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.response.DashboardSummaryResponse;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import com.codecrackai.backend.repository.UserRepository;
import com.codecrackai.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final UserQuestionProgressRepository progressRepository;
    private final TopicMasteryCalculator topicMasteryCalculator;

    private static final int XP_PER_LEVEL = 100;
    private static final DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public DashboardSummaryResponse getSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User", userId));

        List<UserQuestionProgress> allProgress = progressRepository.findByUserId(userId);

        long solved = allProgress.stream().filter(p -> p.getStatus() == ProgressStatus.SOLVED).count();
        long attempted = allProgress.stream()
                .filter(p -> p.getStatus() == ProgressStatus.SOLVED || p.getStatus() == ProgressStatus.ATTEMPTED)
                .count();
        double accuracy = attempted == 0 ? 0 : Math.round((solved * 1000.0) / attempted) / 10.0;

        Map<String, Integer> heatmap = buildHeatmap(allProgress);

        int xpIntoLevel = user.getXp() % XP_PER_LEVEL;

        return DashboardSummaryResponse.builder()
                .questionsSolved((int) solved)
                .currentStreak(user.getCurrentStreak())
                .longestStreak(user.getLongestStreak())
                .accuracyPercent(accuracy)
                .xp(user.getXp())
                .level(user.getLevel())
                .xpToNextLevel(XP_PER_LEVEL - xpIntoLevel)
                .activityHeatmap(heatmap)
                .topicMastery(topicMasteryCalculator.calculate(userId))
                .build();
    }

    /** Last 30 days, oldest first, zero-filled so the frontend can render a fixed-width grid. */
    private Map<String, Integer> buildHeatmap(List<UserQuestionProgress> allProgress) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 29; i >= 0; i--) {
            counts.put(today.minusDays(i).format(DAY_FORMAT), 0);
        }

        for (UserQuestionProgress p : allProgress) {
            if (p.getLastAttemptedAt() == null) continue;
            LocalDate day = p.getLastAttemptedAt().atZone(ZoneId.systemDefault()).toLocalDate();
            String key = day.format(DAY_FORMAT);
            if (counts.containsKey(key)) {
                counts.merge(key, 1, Integer::sum);
            }
        }
        return counts;
    }
}
