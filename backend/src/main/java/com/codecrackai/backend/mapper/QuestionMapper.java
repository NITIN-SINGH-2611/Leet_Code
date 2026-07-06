package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "topicName", source = "topic.name")
    QuestionSummaryResponse toSummary(Question question);

    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "topicId", source = "topic.id")
    QuestionDetailResponse toDetail(Question question);
}
