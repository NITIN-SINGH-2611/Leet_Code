package com.codecrackai.backend.repository;

import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserQuestionProgressRepository extends JpaRepository<UserQuestionProgress, Long> {

    Optional<UserQuestionProgress> findByUserIdAndQuestionId(Long userId, Long questionId);

    List<UserQuestionProgress> findByUserIdAndStatus(Long userId, ProgressStatus status);

    List<UserQuestionProgress> findByUserId(Long userId);

    List<UserQuestionProgress> findByUserIdAndRevisionDueDateLessThanEqual(Long userId, LocalDate date);

    long countByUserIdAndStatus(Long userId, ProgressStatus status);
}
