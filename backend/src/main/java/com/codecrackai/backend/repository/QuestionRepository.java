package com.codecrackai.backend.repository;

import com.codecrackai.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    Optional<Question> findBySlug(String slug);
    boolean existsBySlug(String slug);
    long countByTopicId(Long topicId);
}
