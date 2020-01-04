package projects.user.security.auth;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.RawAccessJwtToken;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private RawAccessJwtToken rawAccessToken;
    private UserContext userContext;
    private MessageSource messageSource;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken, MessageSource messageSource) {
        super(null);
        this.rawAccessToken = unsafeToken;
        this.setAuthenticated(false);
        this.messageSource = messageSource;
    }

    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities, MessageSource messageSource) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
        this.messageSource = messageSource;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(messageSource.getMessage("jwt_token.not_trusted", null, null));
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return rawAccessToken;
    }

    @Override
    public Object getPrincipal() {
        return userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawAccessToken = null;
    }
}
