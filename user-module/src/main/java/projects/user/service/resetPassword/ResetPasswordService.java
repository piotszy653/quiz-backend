package projects.user.service.resetPassword;

import projects.user.dto.resetPassword.ForgotPasswordDto;
import projects.user.dto.resetPassword.ResetPasswordDto;
import projects.user.model.ResetPasswordToken;
import projects.user.model.user.User;
import projects.user.repository.ResetPasswordTokenRepository;
import projects.user.service.MailService;
import projects.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ResetPasswordService {

    final MessageSource messageSource;

    final UserService userService;

    final MailService mailService;

    final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Value("${reset-password-token.expirationTime}")
    private Long expirationTime;

    @Value("${frontend.url}")
    private String url;

    @Value("${endpoints.resetPassword}")
    private String endpoint;

    @Value("${mail.template.reset-password}")
    private String resetPasswordEmailPath;

    public void processForgotPassword(ForgotPasswordDto forgotPasswordDto) {
        String username = forgotPasswordDto.getUsername();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            ResetPasswordToken token = new ResetPasswordToken(UUID.randomUUID().toString(), expirationTime, user.get());
            resetPasswordTokenRepository.save(token);
            sendPasswordResetMail(
                    username,
                    messageSource.getMessage("email.reset.password.subject", null, LocaleContextHolder.getLocale()),
                    url + endpoint + "/" + token.getUuid()
            );
        }
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        ResetPasswordToken token = resetPasswordTokenRepository.findByUuid(resetPasswordDto.getUuid())
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("reset_password_token.not_found.uuid", new Object[]{resetPasswordDto.getUuid()}, null)));
        if (token.isExpired()) {
            throw new IllegalArgumentException(messageSource.getMessage("reset_password_token.expired", null, null));
        }
        User user = token.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordDto.getPassword()));
        resetPasswordTokenRepository.delete(token);
        userService.save(user);
    }

    private void sendPasswordResetMail(String receiver, String subject, String link) {
        Map<String, String> variables = new HashMap<>();
        variables.put("link", link);
        mailService.sendMailFromTemplate(receiver, subject, variables, resetPasswordEmailPath);
    }
}
