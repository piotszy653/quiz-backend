package projects.core.service.init;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projects.core.config.enums.roles.RolesEnum;
import projects.user.model.roles.Role;
import projects.user.model.roles.RoleGroup;
import projects.user.repository.roles.RoleGroupRepository;
import projects.user.repository.roles.RoleRepository;
import projects.user.repository.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public abstract class BootstrapService {

    protected final RoleGroupRepository roleGroupRepository;

    private final RoleRepository roleRepository;

    protected final UserRepository userRepository;

    protected final BootstrapPartService bootstrapPartService;

    public abstract void setup();

    public void boot() {
        createRoles();
        createRoleGroups();
    }

    private void createRoles() {
        List<Role> dbRoles = (List<Role>) roleRepository.findAll();
        List<String> dbRoleNames = dbRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        for (Role role : dbRoles) {
            if (!RolesEnum.ADMIN.roleNames.contains(role.getName())) {
                roleRepository.delete(role);
            }
        }

        for (String roleName : RolesEnum.ADMIN.roleNames) {
            if (!dbRoleNames.contains(roleName)) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void createRoleGroups() {

        List<RoleGroup> dbRoleGroups = (List<RoleGroup>) roleGroupRepository.findAll();
        List<String> dbRoleGroupsNames = dbRoleGroups.stream()
                .map(RoleGroup::getName)
                .collect(Collectors.toList());


        deleteUnnecessaryRoleGroupsFromDb(dbRoleGroups);
        updateRoleGroupsInDb(dbRoleGroupsNames);
    }

    private void deleteUnnecessaryRoleGroupsFromDb(List<RoleGroup> dbRoleGroups) {

        dbRoleGroups.forEach(roleGroup -> {
            try {
                RolesEnum.valueOf(roleGroup.getName());
            } catch (IllegalArgumentException e) {
                roleGroupRepository.delete(roleGroup);
            }
        });
    }

    private void updateRoleGroupsInDb(List<String> dbRoleGroupsNames) {
        Arrays.stream(RolesEnum.values()).forEach(roleGroupName -> {
            List<Role> enumRoles = roleRepository.findByNameIn(roleGroupName.roleNames);

            // adding missing roleGroups to db from RolesEnum
            if (!dbRoleGroupsNames.contains(roleGroupName.name())) {
                RoleGroup roleGroup = new RoleGroup(roleGroupName.name());
                roleGroup.setRoles(enumRoles);
                roleGroupRepository.save(roleGroup);
            } else {
                RoleGroup dbRoleGroup = roleGroupRepository.findByName(roleGroupName.name());

                deleteUnnecessaryRolesFromRoleGroups(dbRoleGroup, enumRoles);
                updateRolesInRoleGroups(dbRoleGroup, enumRoles);

                roleGroupRepository.save(dbRoleGroup);
            }
        });
    }

    private void deleteUnnecessaryRolesFromRoleGroups(RoleGroup dbRoleGroup, List<Role> enumRoles) {
        List<Role> removedRoles = new ArrayList<>();
        dbRoleGroup.getRoles().forEach(role -> {
            if (!enumRoles.contains(role)) {
                removedRoles.add(role);
            }
        });
        dbRoleGroup.getRoles().removeAll(removedRoles);
    }

    private void updateRolesInRoleGroups(RoleGroup dbRoleGroup, List<Role> enumRoles) {
        //adding missing roles to roleGroup in db
        for (Role role : enumRoles) {
            if (!dbRoleGroup.getRoles().contains(role)) {
                dbRoleGroup.getRoles().add(role);
            }
        }
    }
}
