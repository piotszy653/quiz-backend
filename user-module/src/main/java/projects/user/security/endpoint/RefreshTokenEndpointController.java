package projects.user.security.endpoint;


import projects.user.model.user.User;
import projects.user.security.auth.jwt.extractor.TokenExtractor;
import projects.user.security.config.JwtSettings;
import projects.user.security.config.WebSecurityConfig;
import projects.user.security.exceptions.InvalidJwtTokenException;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.JwtToken;
import projects.user.security.model.token.JwtTokenFactory;
import projects.user.security.model.token.RawAccessJwtToken;
import projects.user.security.model.token.RefreshToken;
import projects.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RefreshTokenEndpointController {

    private final JwtTokenFactory tokenFactory;

    private final JwtSettings jwtSettings;

    private final UserService userService;

    @Qualifier("jwtHeaderTokenExtractor")
    private final TokenExtractor tokenExtractor;

    private final MessageSource messageSource;


    @PostMapping("/api/auth/token")
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request) {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload, messageSource);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(InvalidJwtTokenException::new);

        String subject = refreshToken.getSubject();
        User user = userService.findByUsername(subject)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.roles_required.username", new Object[]{subject}, null)));

        UserContext userContext = new UserContext(user);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
