package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.response.TopicMasteryResponse;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TopicMasteryCalculator {

    private final QuestionRepository questionRepository;
    private final UserQuestionProgressRepository progressRepository;

    public List<TopicMasteryResponse> calculate(Long userId) {
        Set<Long> solvedQuestionIds = progressRepository.findByUserIdAndStatus(userId, ProgressStatus.SOLVED)
                .stream()
                .map(p -> p.getQuestion().getId())
                .collect(Collectors.toSet());

        Map<String, List<Question>> byTopic = questionRepository.findAll().stream()
                .filter(Question::isPublished)
                .collect(Collectors.groupingBy(q -> q.getTopic().getName()));

        return byTopic.entrySet().stream()
                .map(entry -> {
                    String topicName = entry.getKey();
                    List<Question> questions = entry.getValue();
                    long total = questions.size();
                    long solved = questions.stream().filter(q -> solvedQuestionIds.contains(q.getId())).count();
                    double percent = total == 0 ? 0 : (solved * 100.0) / total;
                    return TopicMasteryResponse.builder()
                            .topicName(topicName)
                            .totalQuestions(total)
                            .solvedQuestions(solved)
                            .masteryPercent(Math.round(percent * 10) / 10.0)
                            .build();
                })
                .sorted((a, b) -> b.getMasteryPercent() > a.getMasteryPercent() ? 1 : -1)
                .toList();
    }
}
