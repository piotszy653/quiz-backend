package projects.user.security.config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SecurityEndpoints {

    AUTHENTICATION_URL ("/api/auth/login"),
    REFRESH_TOKEN_URL ("/api/auth/token"),
    REGISTRATION_URL ("/api/auth/registration/**"),
    RESET_PASSWORD_URL ("/api/reset-password"),
    ACTUATOR_URL("/actuator/**"),
    FETCH_FILE_URL("/api/files/**");

    public final String value;
}
