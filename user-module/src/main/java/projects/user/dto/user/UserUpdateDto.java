package projects.user.dto.user;

import lombok.Data;
import projects.user.utils.validator.groupAllowed.GroupAllowed;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserUpdateDto {

    @Email(message = "{username.mail_required}")
    @Size(max = 255, message = "username.max:255")
    private final String username;

    @Size(max = 255, message = "name.max:255")
    private final String name;

    @Size(min = 5, message = "password.min.5")
    private final String password;

    private final Boolean enabled;

    @GroupAllowed
    private final String roleGroup;

    public UserUpdateDto(String username, String name, String password, Boolean enabled, String roleGroup) {
        this.username = username != null ? username.trim().toLowerCase() : null;
        this.name = name;
        this.password = password;
        this.enabled = enabled;
        this.roleGroup = roleGroup;
    }
}

