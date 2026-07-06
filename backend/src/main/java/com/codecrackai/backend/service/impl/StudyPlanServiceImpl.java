package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.dto.response.StudyPlanResponse;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.mapper.QuestionMapper;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import com.codecrackai.backend.repository.UserRepository;
import com.codecrackai.backend.service.StudyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NOTE: recommendation logic here scans all published questions in memory,
 * which is fine at the current seed-data scale. Once the question bank grows
 * (Phase 5's admin panel makes that easy), swap the in-memory filter for a
 * proper "questions not yet solved by user" JPQL/Specification query.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyPlanServiceImpl implements StudyPlanService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionProgressRepository progressRepository;
    private final QuestionMapper questionMapper;

    @Override
    public StudyPlanResponse getPlanForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User", userId));

        Set<Long> solvedQuestionIds = progressRepository.findByUserIdAndStatus(userId, ProgressStatus.SOLVED)
                .stream()
                .map(p -> p.getQuestion().getId())
                .collect(Collectors.toSet());

        List<QuestionSummaryResponse> todaysQuestions = questionRepository
                .findAll(Sort.by(Sort.Direction.DESC, "importanceScore"))
                .stream()
                .filter(Question::isPublished)
                .filter(q -> !solvedQuestionIds.contains(q.getId()))
                .limit(user.getTargetQuestionsPerDay())
                .map(questionMapper::toSummary)
                .toList();

        List<QuestionSummaryResponse> revisionDue = progressRepository
                .findByUserIdAndRevisionDueDateLessThanEqual(userId, LocalDate.now())
                .stream()
                .map(UserQuestionProgress::getQuestion)
                .map(questionMapper::toSummary)
                .toList();

        long notStarted = questionRepository.count() - solvedQuestionIds.size();

        return StudyPlanResponse.builder()
                .targetQuestionsPerDay(user.getTargetQuestionsPerDay())
                .todaysQuestions(todaysQuestions)
                .revisionDue(revisionDue)
                .solvedTotal(solvedQuestionIds.size())
                .notStartedTotal((int) Math.max(notStarted, 0))
                .build();
    }
}
