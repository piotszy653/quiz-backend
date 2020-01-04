package projects.user.data;

import projects.user.model.roles.RoleGroup;


public interface IRoleGroupData {

    default RoleGroup getDefaultRoleGroup() {
        return new RoleGroup(getAdminRoleGroup().getName());
    }

    default RoleGroup getRoleGroup(String name) {
        return new RoleGroup(name);
    }

    default RoleGroup getAdminRoleGroup() {
        return new RoleGroup("ADMIN");
    }

    default RoleGroup getUserRoleGroup() {
        return new RoleGroup("USER");
    }
}