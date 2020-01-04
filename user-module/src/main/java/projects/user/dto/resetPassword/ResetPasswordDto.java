package projects.user.dto.resetPassword;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ResetPasswordDto {

    @NotBlank(message = "{password.not_blank}")
    @Size(min = 5, message = "password.min.5")
    private String password;

    @NotBlank(message = "{imageUuid.not_blank}")
    private String uuid;
}
