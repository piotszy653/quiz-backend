package projects.user.data;

import projects.user.dto.resetPassword.ForgotPasswordDto;
import projects.user.dto.resetPassword.ResetPasswordDto;
import projects.user.model.ResetPasswordToken;
import projects.user.model.user.User;

import java.util.UUID;

public interface IResetPasswordData {

    default Long getDefaultExpirationTime() {
        return 900000L;
    }

    default ForgotPasswordDto getDefaultForgotPasswordDto() {
        return new ForgotPasswordDto("mail@mail.com");
    }

    default ResetPasswordDto getDefaultResetPasswordDto(String uuid) {
        return new ResetPasswordDto("password", uuid);
    }

    default ResetPasswordToken getDefaultResetPasswordToken(String uuid, User user) {
        return new ResetPasswordToken(uuid, getDefaultExpirationTime(), user);
    }

    default String getUuid() {
        return UUID.randomUUID().toString();
    }
}
