package com.codecrackai.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicMasteryResponse {
    private String topicName;
    private long totalQuestions;
    private long solvedQuestions;
    private double masteryPercent;
}
