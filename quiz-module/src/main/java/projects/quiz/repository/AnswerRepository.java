package projects.quiz.repository;

import org.springframework.data.repository.CrudRepository;
import projects.quiz.model.Answer;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends CrudRepository<Answer, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Answer> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}