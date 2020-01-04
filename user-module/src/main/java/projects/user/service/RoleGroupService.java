package projects.user.service;

import projects.user.model.roles.Role;
import projects.user.model.roles.RoleGroup;
import projects.user.repository.roles.RoleGroupRepository;
import projects.user.repository.roles.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RoleGroupService {

    private RoleGroupRepository roleGroupRepository;

    private RoleRepository roleRepository;

    public RoleGroup getByName(String name) {
        return roleGroupRepository.findByName(name);
    }


    public boolean existsByName(String name) {
        return roleGroupRepository.existsByName(name);
    }

    @Transactional
    public RoleGroup create(RoleGroup roleGroup) {
        return roleGroupRepository.save(roleGroup);
    }

    public List<Role> getRolesByRoleGroup(List<String> roleNames) {
        return roleRepository.findByNameIn(roleNames);
    }
}

