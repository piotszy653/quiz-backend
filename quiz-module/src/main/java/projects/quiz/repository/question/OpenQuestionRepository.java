package projects.quiz.repository.question;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.question.OpenQuestion;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface OpenQuestionRepository extends PagingAndSortingRepository<OpenQuestion, Long> {

    Optional<OpenQuestion> findByUuid(UUID uuid);

    LinkedHashSet<OpenQuestion> findAllByOwnerUuid(UUID ownerUuid);
}
