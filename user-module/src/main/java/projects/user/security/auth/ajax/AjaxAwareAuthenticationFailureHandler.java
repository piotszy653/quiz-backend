package projects.user.security.auth.ajax;

import projects.user.security.exceptions.handlers.SecurityExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler, SecurityExceptionHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException {

            handleException(response, e, HttpStatus.UNAUTHORIZED);
    }
}
