package projects.user.security.auth.jwt;

import projects.user.model.user.User;
import projects.user.repository.user.UserRepository;
import projects.user.security.auth.JwtAuthenticationToken;
import projects.user.security.config.JwtSettings;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtSettings jwtSettings;

    private final UserRepository userRepository;
    private final MessageSource messageSource;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        String username = jwsClaims.getBody().get("sub", String.class);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("jwt_token.invalid", null, null)));
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();

        UserContext context = new UserContext(subject, authorities);

        return new JwtAuthenticationToken(context, context.getAuthorities(), messageSource);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
