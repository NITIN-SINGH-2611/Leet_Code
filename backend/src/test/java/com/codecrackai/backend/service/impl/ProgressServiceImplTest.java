package com.codecrackai.backend.service.impl;

import com.codecrackai.backend.dto.request.UpdateProgressRequest;
import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.Topic;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.UserQuestionProgress;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.entity.enums.ProgressStatus;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.UserQuestionProgressRepository;
import com.codecrackai.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock
    private UserQuestionProgressRepository progressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private QuestionRepository questionRepository;

    private ProgressServiceImpl progressService;

    private User user;
    private Question mediumQuestion;

    @BeforeEach
    void setUp() {
        progressService = new ProgressServiceImpl(progressRepository, userRepository, questionRepository);

        user = User.builder().email("test@example.com").xp(0).level(1)
                .currentStreak(0).longestStreak(0).build();
        user.setId(1L);

        Topic topic = Topic.builder().name("Arrays").slug("arrays").build();
        topic.setId(1L);

        mediumQuestion = Question.builder().title("Sample").slug("sample")
                .difficulty(Difficulty.MEDIUM).topic(topic).pattern("Two Pointers").build();
        mediumQuestion.setId(10L);
    }

    @Test
    void firstTimeSolveAwardsXpAndStartsStreak() {
        UserQuestionProgress fresh = UserQuestionProgress.builder()
                .user(user).question(mediumQuestion).status(ProgressStatus.NOT_STARTED).attempts(0).build();

        when(progressRepository.findByUserIdAndQuestionId(1L, 10L)).thenReturn(Optional.of(fresh));
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateProgressRequest request = new UpdateProgressRequest();
        request.setStatus(ProgressStatus.SOLVED);

        UserQuestionProgress result = progressService.updateProgress(1L, 10L, request);

        assertThat(result.getStatus()).isEqualTo(ProgressStatus.SOLVED);
        assertThat(result.getAttempts()).isEqualTo(1);
        assertThat(user.getXp()).isEqualTo(20); // MEDIUM reward
        assertThat(user.getCurrentStreak()).isEqualTo(1);
        assertThat(result.getRevisionDueDate()).isEqualTo(LocalDate.now().plusDays(1));

        verify(userRepository).save(user);
    }

    @Test
    void reSolvingAnAlreadySolvedQuestionDoesNotAwardXpTwice() {
        UserQuestionProgress alreadySolved = UserQuestionProgress.builder()
                .user(user).question(mediumQuestion).status(ProgressStatus.SOLVED).attempts(1).build();

        when(progressRepository.findByUserIdAndQuestionId(1L, 10L)).thenReturn(Optional.of(alreadySolved));
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateProgressRequest request = new UpdateProgressRequest();
        request.setStatus(ProgressStatus.SOLVED);

        progressService.updateProgress(1L, 10L, request);

        assertThat(user.getXp()).isEqualTo(0);
        verify(userRepository, never()).save(any());
    }

    @Test
    void bookmarkingDoesNotChangeStatusOrAttempts() {
        UserQuestionProgress existing = UserQuestionProgress.builder()
                .user(user).question(mediumQuestion).status(ProgressStatus.NOT_STARTED).attempts(0).build();

        when(progressRepository.findByUserIdAndQuestionId(1L, 10L)).thenReturn(Optional.of(existing));
        ArgumentCaptor<UserQuestionProgress> captor = ArgumentCaptor.forClass(UserQuestionProgress.class);
        when(progressRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        UpdateProgressRequest request = new UpdateProgressRequest();
        request.setBookmarked(true);

        progressService.updateProgress(1L, 10L, request);

        assertThat(captor.getValue().isBookmarked()).isTrue();
        assertThat(captor.getValue().getStatus()).isEqualTo(ProgressStatus.NOT_STARTED);
        assertThat(captor.getValue().getAttempts()).isZero();
    }
}
