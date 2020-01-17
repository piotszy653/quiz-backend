package projects.quiz.repository;

import org.springframework.data.repository.CrudRepository;
import projects.quiz.model.Rate;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface RateRepository extends CrudRepository<Rate, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Rate> findByUuid(UUID uuid);

    LinkedHashSet<Rate> findAllByUserUuid(UUID userUuid);

    LinkedHashSet<Rate> findAllByRatedObjectUuid(UUID ratedObjectUuid);

    LinkedHashSet<Rate> findAllByUserUuidAndRatedObjectUuid(UUID userUuid, UUID ratedObjectUuid);
}