package projects.user.dto.registration;

import projects.user.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    User user;

    String token;

    String refreshToken;
}
