package projects.user.repository.user;

import projects.user.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);

    boolean existsByUuid(UUID uuid);

    Optional<User> findByUuid(UUID uuid);
}
