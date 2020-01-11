package projects.quiz.repository.question;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.question.TrueFalseQuestion;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface TrueFalseQuestionRepository extends PagingAndSortingRepository<TrueFalseQuestion, Long> {

    Optional<TrueFalseQuestion> findByUuid(UUID uuid);

    LinkedHashSet<TrueFalseQuestion> findAllByOwnerUuid(UUID ownerUuid);
}
