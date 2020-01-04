package projects.user.security.auth.ajax;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username.trim();
        this.password = password;
    }
}
