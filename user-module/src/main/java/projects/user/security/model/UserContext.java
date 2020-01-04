package projects.user.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import projects.user.model.user.User;

import java.util.List;

@Data
@AllArgsConstructor
public class UserContext {

    private String username;
    private final List<GrantedAuthority> authorities;

    public UserContext(User user) {
        username = user.getUsername().trim().toLowerCase();
        authorities = (List<GrantedAuthority>) user.getAuthorities();
    }

}
