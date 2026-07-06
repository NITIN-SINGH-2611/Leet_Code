package com.codecrackai.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyPlanResponse {
    private int targetQuestionsPerDay;
    private List<QuestionSummaryResponse> todaysQuestions;
    private List<QuestionSummaryResponse> revisionDue;
    private int solvedTotal;
    private int notStartedTotal;
}
