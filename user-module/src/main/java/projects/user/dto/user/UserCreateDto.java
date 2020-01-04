package projects.user.dto.user;

import lombok.Data;
import projects.user.utils.validator.groupAllowed.GroupAllowed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserCreateDto {

    @NotBlank(message = "{username.not_blank}")
    @Email(message = "{username.mail_required}")
    @Size(max = 255, message = "username.max:255")
    private final String username;

    @NotBlank(message = "{password.not_blank}")
    @Size(min = 5, message = "password.min.5")
    private final String password;

    private final boolean enabled;

    @NotNull(message = "{role_group.not_null}")
    @GroupAllowed
    private final String roleGroup;

    public UserCreateDto(String username, String password, boolean enabled, String roleGroup) {
        this.username = username != null ? username.trim().toLowerCase() : null;
        this.password = password;
        this.enabled = enabled;
        this.roleGroup = roleGroup;
    }
}

