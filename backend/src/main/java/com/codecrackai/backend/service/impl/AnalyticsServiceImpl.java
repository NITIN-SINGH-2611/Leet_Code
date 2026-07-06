package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.response.AnalyticsResponse;
import com.codecrackai.backend.dto.response.DifficultyStatResponse;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import com.codecrackai.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final QuestionRepository questionRepository;
    private final UserQuestionProgressRepository progressRepository;
    private final TopicMasteryCalculator topicMasteryCalculator;

    @Override
    public AnalyticsResponse getAnalytics(Long userId) {
        List<Question> allQuestions = questionRepository.findAll().stream()
                .filter(Question::isPublished)
                .toList();

        Set<Long> solvedIds = progressRepository.findByUserIdAndStatus(userId, ProgressStatus.SOLVED)
                .stream()
                .map(p -> p.getQuestion().getId())
                .collect(Collectors.toSet());

        List<DifficultyStatResponse> breakdown = Arrays.stream(Difficulty.values())
                .map(difficulty -> {
                    long total = allQuestions.stream().filter(q -> q.getDifficulty() == difficulty).count();
                    long solved = allQuestions.stream()
                            .filter(q -> q.getDifficulty() == difficulty)
                            .filter(q -> solvedIds.contains(q.getId()))
                            .count();
                    return DifficultyStatResponse.builder()
                            .difficulty(difficulty.name())
                            .total(total)
                            .solved(solved)
                            .build();
                })
                .toList();

        return AnalyticsResponse.builder()
                .difficultyBreakdown(breakdown)
                .topicMastery(topicMasteryCalculator.calculate(userId))
                .totalQuestionsInBank(allQuestions.size())
                .totalSolved(solvedIds.size())
                .build();
    }
}
