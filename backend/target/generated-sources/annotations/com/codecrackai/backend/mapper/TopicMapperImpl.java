package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.TopicResponse;
import com.codecrackai.backend.entity.Topic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T22:33:34+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class TopicMapperImpl implements TopicMapper {

    @Override
    public TopicResponse toResponse(Topic topic) {
        if ( topic == null ) {
            return null;
        }

        TopicResponse.TopicResponseBuilder topicResponse = TopicResponse.builder();

        topicResponse.id( topic.getId() );
        topicResponse.name( topic.getName() );
        topicResponse.slug( topic.getSlug() );
        topicResponse.description( topic.getDescription() );
        topicResponse.sortOrder( topic.getSortOrder() );

        return topicResponse.build();
    }
}
