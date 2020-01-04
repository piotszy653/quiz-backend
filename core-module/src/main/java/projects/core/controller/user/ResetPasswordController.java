package projects.core.controller.user;

import projects.user.dto.resetPassword.ForgotPasswordDto;
import projects.user.dto.resetPassword.ResetPasswordDto;
import projects.user.service.resetPassword.ResetPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

    ResetPasswordService resetPasswordService;

    @PostMapping
    public void forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        resetPasswordService.processForgotPassword(forgotPasswordDto);
    }

    @PutMapping
    public void resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        resetPasswordService.resetPassword(resetPasswordDto);
    }
}
