package projects.user.repository.roles;

import projects.user.model.roles.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);

    List<Role> findAllByName(List<String> names);

    List<Role> findByNameIn(List<String> names);

    boolean existsByName(String name);

}