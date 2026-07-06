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
public class AnalyticsResponse {
    private List<DifficultyStatResponse> difficultyBreakdown;
    private List<TopicMasteryResponse> topicMastery;
    private int totalQuestionsInBank;
    private int totalSolved;
}
