package projects.user.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import projects.user.dto.registration.LoginResponseDto;
import projects.user.model.user.User;
import projects.user.security.exceptions.handlers.SecurityExceptionHandler;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.JwtToken;
import projects.user.security.model.token.JwtTokenFactory;
import projects.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler, SecurityExceptionHandler {
    private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;
    private final UserService userService;
    private final MessageSource messageSource;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserContext userContext = (UserContext) authentication.getPrincipal();

        Optional<User> user = userService.findByUsername(userContext.getUsername());
        if (user.isPresent()) {
            JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
            JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), new LoginResponseDto(
                    user.get(),
                    accessToken.getToken(),
                    refreshToken.getToken()
            ));

            clearAuthenticationAttributes(request);
        } else {
            handleException(
                    response,
                    new NoSuchElementException(messageSource.getMessage("user.not_found.username", new Object[]{userContext.getUsername()}, null)),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
