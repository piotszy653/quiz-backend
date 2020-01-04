package projects.user.service;

import projects.user.model.roles.Role;
import projects.user.repository.roles.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private RoleRepository roleRepository;

    public List<Role> findAllByName(List<String> names){
        return roleRepository.findAllByName(names);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public List<Role> findByNameIn(List<String> names) {
        return roleRepository.findByNameIn(names);
    }

    @Transactional
    public Role create(Role role) {
        return roleRepository.save(role);
    }


}

