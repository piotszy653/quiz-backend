package projects.user.dto.resetPassword;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ForgotPasswordDto {

    @Email(message = "{username.mail_required}")
    @NotBlank(message = "{username.not_blank}")
    @Size(max = 255, message = "username.max:255")
    private String username;

    public ForgotPasswordDto(String username) {
        this.username = username.trim().toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username.trim().toLowerCase();
    }
}
