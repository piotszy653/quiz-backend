package projects.quiz.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.Quiz;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Quiz> findByUuid(UUID uuid);

    LinkedHashSet<Quiz> findAllByUuid(Iterable<UUID> uuids);

    LinkedHashSet<Quiz> findAllByOwnerUuid(UUID ownerUuid);

    void deleteByUuid(UUID uuid);

}
