package com.codecrackai.backend.dto.request;

import com.codecrackai.backend.entity.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpsertRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Long topicId;

    @NotBlank
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
    private boolean published = true;
}
