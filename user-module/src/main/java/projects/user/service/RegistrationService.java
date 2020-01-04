package projects.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.user.dto.registration.LoginResponseDto;
import projects.user.dto.registration.RegistrationDto;
import projects.user.model.user.User;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.JwtTokenFactory;

import javax.validation.Valid;
import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final UserService userService;

    private final MailService mailService;

    private final RoleGroupService roleGroupService;

    private final JwtTokenFactory tokenFactory;

    private final MessageSource messageSource;

    @Value("${mail.template.registration}")
    private String registrationEmailPath;


    public LoginResponseDto register(@Valid RegistrationDto registrationDto) {
        if (!registrationDto.isRegulationsAccepted())
            throw new IllegalArgumentException(messageSource.getMessage("regulations_accepted.required_true", null, null));

        userService.findByUsername(registrationDto.getUsername())
                .ifPresent(user -> {
                    throw new IllegalArgumentException(messageSource.getMessage("user.exists.username", new Object[]{user.getUsername()}, null));
                });

        User user = new User(
                registrationDto.getUsername(),
                new BCryptPasswordEncoder().encode(registrationDto.getPassword()),
                roleGroupService.getByName("USER"));
        user.setEnabled(true);
        userService.save(user);
        sendRegistrationMail(registrationDto.getUsername(), (messageSource.getMessage("email.registration.subject", null, LocaleContextHolder.getLocale())));

        UserContext userContext = new UserContext(user);
        return new LoginResponseDto(
                user,
                tokenFactory.createAccessJwtToken(userContext).getToken(),
                tokenFactory.createRefreshToken(userContext).getToken()
        );
    }

    private void sendRegistrationMail(String receiver, String subject) {
        mailService.sendMailFromTemplate(receiver, subject, new HashMap<>(), registrationEmailPath);
    }
}