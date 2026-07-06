package com.codecrackai.backend.repository.spec;

import com.codecrackai.backend.entity.Question;
import com.codecrackai.backend.entity.enums.Difficulty;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a dynamic JPA Specification from the optional filters supported by
 * the /api/v1/questions search endpoint: difficulty, topic, company, pattern,
 * free-text title search, and published-only.
 */
public final class QuestionSpecifications {

    private QuestionSpecifications() {
    }

    public static Specification<Question> withFilters(Difficulty difficulty,
                                                        Long topicId,
                                                        String company,
                                                        String pattern,
                                                        String search,
                                                        boolean publishedOnly) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (publishedOnly) {
                predicates.add(cb.isTrue(root.get("published")));
            }
            if (difficulty != null) {
                predicates.add(cb.equal(root.get("difficulty"), difficulty));
            }
            if (topicId != null) {
                predicates.add(cb.equal(root.get("topic").get("id"), topicId));
            }
            if (pattern != null && !pattern.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("pattern")), pattern.toLowerCase()));
            }
            if (company != null && !company.isBlank()) {
                predicates.add(cb.isMember(company, root.get("companyTags")));
            }
            if (search != null && !search.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
