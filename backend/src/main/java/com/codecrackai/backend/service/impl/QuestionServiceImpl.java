package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.QuestionUpsertRequest;
import com.codecrackai.backend.dto.response.QuestionDetailResponse;
import com.codecrackai.backend.dto.response.QuestionSummaryResponse;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.Topic;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.exception.BadRequestException;
import com.codecrackai.backend.exception.ResourceNotFoundException;
import com.codecrackai.backend.mapper.QuestionMapper;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.TopicRepository;
import com.codecrackai.backend.repository.spec.QuestionSpecifications;
import com.codecrackai.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Page<QuestionSummaryResponse> search(Difficulty difficulty,
                                                 Long topicId,
                                                 String company,
                                                 String pattern,
                                                 String search,
                                                 Pageable pageable) {
        var spec = QuestionSpecifications.withFilters(difficulty, topicId, company, pattern, search, true);
        return questionRepository.findAll(spec, pageable).map(questionMapper::toSummary);
    }

    @Override
    public QuestionDetailResponse getBySlug(String slug) {
        Question question = questionRepository.findBySlug(slug)
                .orElseThrow(() -> ResourceNotFoundException.of("Question", slug));
        return questionMapper.toDetail(question);
    }

    @Override
    @Transactional
    public QuestionDetailResponse create(QuestionUpsertRequest request) {
        if (questionRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("A question with slug '" + request.getSlug() + "' already exists");
        }
        Question question = new Question();
        applyRequest(question, request);
        return questionMapper.toDetail(questionRepository.save(question));
    }

    @Override
    @Transactional
    public QuestionDetailResponse update(Long id, QuestionUpsertRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Question", id));
        applyRequest(question, request);
        return questionMapper.toDetail(questionRepository.save(question));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!questionRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Question", id);
        }
        questionRepository.deleteById(id);
    }

    private void applyRequest(Question question, QuestionUpsertRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> ResourceNotFoundException.of("Topic", request.getTopicId()));

        question.setTitle(request.getTitle());
        question.setSlug(request.getSlug());
        question.setDifficulty(request.getDifficulty());
        question.setTopic(topic);
        question.setPattern(request.getPattern());
        if (request.getCompanyTags() != null) {
            question.setCompanyTags(request.getCompanyTags());
        }
        question.setFrequency(request.getFrequency());
        question.setImportanceScore(request.getImportanceScore());
        question.setTimeComplexity(request.getTimeComplexity());
        question.setSpaceComplexity(request.getSpaceComplexity());
        question.setProblemStatement(request.getProblemStatement());
        question.setHints(request.getHints());
        question.setEditorial(request.getEditorial());
        question.setVideoLink(request.getVideoLink());
        question.setPublished(request.isPublished());
    }
}
