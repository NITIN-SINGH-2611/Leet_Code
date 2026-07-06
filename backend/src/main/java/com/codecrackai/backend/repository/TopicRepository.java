package com.codecrackai.backend.repository;

import com.codecrackai.backend.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findBySlug(String slug);
    List<Topic> findByParentIsNullOrderBySortOrderAsc();
    List<Topic> findByParentIdOrderBySortOrderAsc(Long parentId);
    boolean existsBySlug(String slug);
}
