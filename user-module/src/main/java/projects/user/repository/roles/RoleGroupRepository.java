package projects.user.repository.roles;

import projects.user.model.roles.RoleGroup;
import org.springframework.data.repository.CrudRepository;

public interface RoleGroupRepository extends CrudRepository<RoleGroup, Long> {

    RoleGroup findByName(String name);

    boolean existsByName(String name);

}