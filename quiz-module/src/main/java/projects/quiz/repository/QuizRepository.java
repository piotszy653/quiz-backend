package projects.quiz.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import projects.quiz.model.Quiz;
import projects.quiz.utils.enums.PrivacyPolicy;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Quiz> findByUuid(UUID uuid);

    LinkedHashSet<Quiz> findAllByUuid(Iterable<UUID> uuids);

    LinkedHashSet<Quiz> findAllByOwnerUuid(UUID ownerUuid);

    LinkedHashSet<Quiz> findAllByPrivacyPolicy(PrivacyPolicy policy);

    LinkedHashSet<Quiz> findAllByPrivacyPolicyAndOwnerUuid(PrivacyPolicy policy, UUID ownerUuid);

    LinkedHashSet<Quiz> findAllByOwnerUuidInAndPrivacyPolicy(Collection<UUID> uuids, PrivacyPolicy policy);

    void deleteByUuid(UUID uuid);

}
