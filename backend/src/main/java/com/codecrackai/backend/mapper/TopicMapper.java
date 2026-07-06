package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.TopicResponse;
import com.codecrackai.backend.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    @Mapping(target = "questionCount", ignore = true)
    TopicResponse toResponse(Topic topic);
}
