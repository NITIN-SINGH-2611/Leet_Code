package com.codecrackai.backend.seed;

import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.Topic;
import com.codecrackai.backend.entity.User;
import com.codecrackai.backend.entity.enums.Difficulty;
import com.codecrackai.backend.entity.enums.Role;
import com.codecrackai.backend.repository.QuestionRepository;
import com.codecrackai.backend.repository.TopicRepository;
import com.codecrackai.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

/**
 * Seeds a handful of topics and original-authored sample questions so the
 * API returns real data locally. Problem statements here are written by us,
 * not copied from any third-party judge — this is illustrative starter
 * content, not the real curated question bank (that lands in Phase 5).
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        if (topicRepository.count() > 0) {
            log.info("Seed data already present, skipping.");
            return;
        }

        try {
            java.io.InputStream is = new org.springframework.core.io.ClassPathResource("leetcode_questions.json").getInputStream();
            java.util.List<java.util.Map<String, String>> questionsData = objectMapper.readValue(is, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<java.util.Map<String, String>>>() {});
            
            java.util.Map<String, Topic> topicMap = new java.util.HashMap<>();
            int sortOrder = 1;
            
            for (java.util.Map<String, String> qData : questionsData) {
                String topicName = qData.get("topic");
                if (topicName == null || topicName.isEmpty()) topicName = "General";
                
                if (!topicMap.containsKey(topicName)) {
                    Topic topic = topicRepository.save(Topic.builder()
                            .name(topicName).slug(topicName.toLowerCase().replace(" ", "-"))
                            .description("Questions related to " + topicName)
                            .sortOrder(sortOrder++).build());
                    topicMap.put(topicName, topic);
                }
            }
            
            for (java.util.Map<String, String> qData : questionsData) {
                String difficultyStr = qData.get("difficulty");
                Difficulty difficulty = Difficulty.MEDIUM;
                try {
                    difficulty = Difficulty.valueOf(difficultyStr.toUpperCase());
                } catch (Exception e) {}
                
                String topicName = qData.get("topic");
                if (topicName == null || topicName.isEmpty()) topicName = "General";
                
                String hints = qData.get("hints");
                if (hints != null && hints.length() > 500) hints = hints.substring(0, 497) + "...";
                
                String problemStatement = qData.get("problemStatement");
                if (problemStatement != null && problemStatement.length() > 2000) problemStatement = problemStatement.substring(0, 1997) + "...";

                questionRepository.save(Question.builder()
                        .title(qData.get("title")).slug(qData.get("slug"))
                        .difficulty(difficulty).topic(topicMap.get(topicName))
                        .pattern(topicName)
                        .companyTags(Set.of("General"))
                        .frequency(5).importanceScore(80)
                        .timeComplexity("O(n)").spaceComplexity("O(1)")
                        .problemStatement(problemStatement)
                        .hints(hints)
                        .editorial(qData.get("editorial"))
                        .build());
            }
        } catch (Exception e) {
            log.error("Failed to load leetcode questions from JSON", e);
        }

        User demoUser = userRepository.save(User.builder()
                .email("demo@codecrackai.dev")
                .passwordHash(passwordEncoder.encode("Demo@1234"))
                .fullName("Demo User")
                .role(Role.USER)
                .targetQuestionsPerDay(2)
                .build());

        userRepository.save(User.builder()
                .email("admin@codecrackai.dev")
                .passwordHash(passwordEncoder.encode("Admin@1234"))
                .fullName("Admin")
                .role(Role.ADMIN)
                .targetQuestionsPerDay(2)
                .build());

        log.info("Seeded {} topics, {} questions, demo user id={}",
                topicRepository.count(), questionRepository.count(), demoUser.getId());
    }
}
