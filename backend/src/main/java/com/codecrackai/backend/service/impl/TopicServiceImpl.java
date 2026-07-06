package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.TopicUpsertRequest;
import com.codecrackai.backend.dto.response.TopicResponse;
import com.codecrackai.backend.entity.Topic;
import com.codecrackai.backend.exception.BadRequestException;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.mapper.TopicMapper;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.TopicRepository;
import com.codecrackai.backend.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final TopicMapper topicMapper;

    @Override
    public List<TopicResponse> getAllRootTopics() {
        return topicRepository.findByParentIsNullOrderBySortOrderAsc().stream()
                .map(this::toResponseWithCount)
                .toList();
    }

    @Override
    public TopicResponse getBySlug(String slug) {
        Topic topic = topicRepository.findBySlug(slug)
                .orElseThrow(() -> ResourceNotFoundException.of("Topic", slug));
        return toResponseWithCount(topic);
    }

    private TopicResponse toResponseWithCount(Topic topic) {
        TopicResponse response = topicMapper.toResponse(topic);
        response.setQuestionCount(questionRepository.countByTopicId(topic.getId()));
        return response;
    }

    @Override
    @Transactional
    public TopicResponse create(TopicUpsertRequest request) {
        if (topicRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("A topic with slug '" + request.getSlug() + "' already exists");
        }
        Topic topic = Topic.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .sortOrder(request.getSortOrder())
                .parent(resolveParent(request.getParentId()))
                .build();
        return toResponseWithCount(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public TopicResponse update(Long id, TopicUpsertRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Topic", id));
        topic.setName(request.getName());
        topic.setSlug(request.getSlug());
        topic.setDescription(request.getDescription());
        topic.setSortOrder(request.getSortOrder());
        topic.setParent(resolveParent(request.getParentId()));
        return toResponseWithCount(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!topicRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Topic", id);
        }
        topicRepository.deleteById(id);
    }

    private Topic resolveParent(Long parentId) {
        if (parentId == null) return null;
        return topicRepository.findById(parentId)
                .orElseThrow(() -> ResourceNotFoundException.of("Topic", parentId));
    }
}
