package projects.core.repository.init;

import projects.core.config.init.BootstrapPartName;
import projects.core.model.init.BootstrapPart;
import org.springframework.data.repository.CrudRepository;

public interface BootstrapPartRepository extends CrudRepository<BootstrapPart, Long> {

    boolean existsByKey(BootstrapPartName key);

}
