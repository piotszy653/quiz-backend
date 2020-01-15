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
            "ROLE_ASSESSMENT_DELETE",
            "ROLE_QUESTION_READ",
            "ROLE_QUESTION_CREATE",
            "ROLE_QUESTION_UPDATE",
            "ROLE_QUESTION_DELETE",
            "ROLE_QUIZ_READ",
            "ROLE_QUIZ_CREATE",
            "ROLE_QUIZ_UPDATE",
            "ROLE_QUIZ_DELETE"
    )),
    USER(Arrays.asList(
            "ROLE_USER_READ",
            "ROLE_USER_UPDATE",
            "ROLE_ASSESSMENT_READ",
            "ROLE_ASSESSMENT_CREATE",
            "ROLE_ASSESSMENT_UPDATE",
            "ROLE_ASSESSMENT_DELETE",
            "ROLE_QUESTION_READ",
            "ROLE_QUESTION_CREATE",
            "ROLE_QUESTION_UPDATE",
            "ROLE_QUESTION_DELETE",
            "ROLE_QUIZ_READ",
            "ROLE_QUIZ_CREATE",
            "ROLE_QUIZ_UPDATE",
            "ROLE_QUIZ_DELETE"
    ));

    public final List<String> roleNames;
}