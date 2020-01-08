package projects.quiz.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.Assessment;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentRepository extends PagingAndSortingRepository<Assessment, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Assessment> findByUuid(UUID uuid);

    LinkedHashSet<Assessment> findAllByOwnerUuid(UUID ownerUuid);

    void deleteByUuid(UUID uuid);

    void deleteAllByOwnerUuid(UUID ownerUuid);
}
