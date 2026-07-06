package com.codecrackai.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private int questionsSolved;
    private int currentStreak;
    private int longestStreak;
    private double accuracyPercent;
    private int xp;
    private int level;
    private int xpToNextLevel;
    /** date (yyyy-MM-dd) -> number of questions attempted/solved that day, last 30 days. */
    private Map<String, Integer> activityHeatmap;
    private List<TopicMasteryResponse> topicMastery;
}
