package projects.quiz.repository;

import org.springframework.data.repository.CrudRepository;
import projects.quiz.model.Result;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface ResultRepository extends CrudRepository<Result, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Result> findByUuid(UUID uuid);

    LinkedHashSet<Result> findAllByUserUuid(UUID userUuid);

    LinkedHashSet<Result> findAllByQuiz_Uuid(UUID quizUuid);

    LinkedHashSet<Result> findAllByUserUuidAndQuiz_Uuid(UUID userUuid, UUID quizUuid);

    void deleteByUuid(UUID uuid);

}