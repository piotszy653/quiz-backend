package projects.storage.repository;

import projects.storage.model.FileData;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FileDataRepository extends PagingAndSortingRepository<FileData, Long> {

    Set<FileData> findAllById(Set<Long> ids);

    Optional<FileData> findById(Long id);

    Optional<FileData> findByUuid(UUID uuid);
}
