package projects.user.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;
    

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

}
