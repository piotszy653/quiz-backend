package projects.core.config.enums.bootstrap.userModule;

import projects.core.config.enums.roles.RolesEnum;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum DefaultUsersEnum {
    ADMIN("admin@quiz.com", "admin", RolesEnum.ADMIN, true),
    USER("user@quiz.com", "user", RolesEnum.USER, true);

    public final String username;
    public final String password;
    public final RolesEnum rolesEnum;
    public final boolean adult;
}

