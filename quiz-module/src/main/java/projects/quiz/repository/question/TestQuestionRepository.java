package projects.quiz.repository.question;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.question.TestQuestion;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface TestQuestionRepository extends PagingAndSortingRepository<TestQuestion, Long> {

    Optional<TestQuestion> findByUuid(UUID uuid);

    LinkedHashSet<TestQuestion> findAllByOwnerUuid(UUID ownerUuid);
}
