package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.Topic;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T22:33:34+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public QuestionSummaryResponse toSummary(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionSummaryResponse.QuestionSummaryResponseBuilder questionSummaryResponse = QuestionSummaryResponse.builder();

        questionSummaryResponse.topicName( questionTopicName( question ) );
        questionSummaryResponse.id( question.getId() );
        questionSummaryResponse.title( question.getTitle() );
        questionSummaryResponse.slug( question.getSlug() );
        questionSummaryResponse.difficulty( question.getDifficulty() );
        questionSummaryResponse.pattern( question.getPattern() );
        Set<String> set = question.getCompanyTags();
        if ( set != null ) {
            questionSummaryResponse.companyTags( new LinkedHashSet<String>( set ) );
        }
        questionSummaryResponse.frequency( question.getFrequency() );
        questionSummaryResponse.importanceScore( question.getImportanceScore() );

        return questionSummaryResponse.build();
    }

    @Override
    public QuestionDetailResponse toDetail(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionDetailResponse.QuestionDetailResponseBuilder questionDetailResponse = QuestionDetailResponse.builder();

        questionDetailResponse.topicName( questionTopicName( question ) );
        questionDetailResponse.topicId( questionTopicId( question ) );
        questionDetailResponse.id( question.getId() );
        questionDetailResponse.title( question.getTitle() );
        questionDetailResponse.slug( question.getSlug() );
        questionDetailResponse.difficulty( question.getDifficulty() );
        questionDetailResponse.pattern( question.getPattern() );
        Set<String> set = question.getCompanyTags();
        if ( set != null ) {
            questionDetailResponse.companyTags( new LinkedHashSet<String>( set ) );
        }
        questionDetailResponse.frequency( question.getFrequency() );
        questionDetailResponse.importanceScore( question.getImportanceScore() );
        questionDetailResponse.timeComplexity( question.getTimeComplexity() );
        questionDetailResponse.spaceComplexity( question.getSpaceComplexity() );
        questionDetailResponse.problemStatement( question.getProblemStatement() );
        questionDetailResponse.hints( question.getHints() );
        questionDetailResponse.editorial( question.getEditorial() );
        questionDetailResponse.videoLink( question.getVideoLink() );

        return questionDetailResponse.build();
    }

    private String questionTopicName(Question question) {
        if ( question == null ) {
            return null;
        }
        Topic topic = question.getTopic();
        if ( topic == null ) {
            return null;
        }
        String name = topic.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long questionTopicId(Question question) {
        if ( question == null ) {
            return null;
        }
        Topic topic = question.getTopic();
        if ( topic == null ) {
            return null;
        }
        Long id = topic.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
