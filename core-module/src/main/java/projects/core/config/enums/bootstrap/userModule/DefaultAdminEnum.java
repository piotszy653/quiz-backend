package projects.core.config.enums.bootstrap.userModule;

import projects.core.config.enums.roles.RolesEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DefaultAdminEnum {
    ADMIN("admin@quiz.com", "admin", RolesEnum.ADMIN, true);

    public final String username;
    public final String password;
    public final RolesEnum rolesEnum;
    public final boolean adult;
}
