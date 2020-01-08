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
            "ROLE_USER_DELETE",
            "ROLE_ASSESSMENT_READ",
            "ROLE_ASSESSMENT_CREATE",
            "ROLE_ASSESSMENT_UPDATE",
            "ROLE_ASSESSMENT_DELETE"
    )),
    USER(Arrays.asList(
            "ROLE_USER_READ",
            "ROLE_USER_UPDATE",
            "ROLE_ASSESSMENT_READ",
            "ROLE_ASSESSMENT_CREATE",
            "ROLE_ASSESSMENT_UPDATE",
            "ROLE_ASSESSMENT_DELETE"
    ));

    public final List<String> roleNames;
}