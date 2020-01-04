package projects.core.config.enums.roles;


import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum RolesEnum {
    ADMIN(Arrays.asList(
            "ROLE_USER_CREATE",
            "ROLE_USER_READ",
            "ROLE_USER_UPDATE",
            "ROLE_USER_DELETE"
    )),
    USER(Arrays.asList(
            "ROLE_USER_READ",
            "ROLE_USER_UPDATE"
    ));

    public final List<String> roleNames;
}