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
public class QuestionSummaryResponse {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private String topicName;
    private String pattern;
    private Set<String> companyTags;
    private int frequency;
    private int importanceScore;
}
