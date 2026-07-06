package com.codecrackai.backend.dto.response;

import com.codecrackai.backend.entity.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailResponse {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private String topicName;
    private Long topicId;
    private String pattern;
    private Set<String> companyTags;
    private int frequency;
    private int importanceScore;
    private String timeComplexity;
    private String spaceComplexity;
    private String problemStatement;
    private String hints;
    private String editorial;
    private String videoLink;
}
