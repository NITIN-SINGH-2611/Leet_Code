package com.codecrackai.backend.entity;

import com.codecrackai.backend.entity.enums.Difficulty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

import java.util.HashSet;
import java.util.Set;

/**
 * A single curated interview question.
 * <p>
 * Note: we store our own original problem statements/editorials plus
 * metadata (tags, frequency, links) — we do not ingest or reproduce
 * copyrighted problem text from third-party judges.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions", uniqueConstraints = @UniqueConstraint(columnNames = "slug"))
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    /** e.g. "Sliding Window", "Two Pointers", "Backtracking" */
    @Column(nullable = false)
    private String pattern;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_company_tags", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "company")
    @Builder.Default
    private Set<String> companyTags = new HashSet<>();

    /** How often this appears in real interviews, 1 (rare) - 5 (extremely common). */
    @Builder.Default
    private int frequency = 3;

    /** Curated ranking of how essential this question is to master, 1-100. */
    @Builder.Default
    private int importanceScore = 50;

    private String timeComplexity;
    private String spaceComplexity;

    @Lob
    private String problemStatement;

    @Lob
    private String hints;

    @Lob
    private String editorial;

    private String videoLink;

    @Builder.Default
    private boolean published = true;
}
