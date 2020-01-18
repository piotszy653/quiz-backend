package projects.user.dto.registration;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationDto {

    @NotBlank(message = "{username.not_blank}")
    @Email(message = "{username.mail_required}")
    @Size(max = 255, message = "username.max:255")
    @Column(unique = true)
    private final String username;

    @Size(max = 255, message = "name.max:255")
    private final String name;

    @NotBlank(message = "{password.not_blank}")
    @Size(min = 5, message = "password.min:5")
    private final String password;

    private final boolean regulationsAccepted;

    public RegistrationDto(String username, String name, String password, boolean regulationsAccepted) {
        this.username = username != null ? username.trim().toLowerCase() : null;
        this.name = name;
        this.password = password;
        this.regulationsAccepted = regulationsAccepted;
    }
}
