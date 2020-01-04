package projects.user.security.model.token;

import projects.user.security.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;

@Slf4j
public class RawAccessJwtToken implements JwtToken {

    private String token;

    private MessageSource messageSource;

    public RawAccessJwtToken(String token, MessageSource messageSource) {
        this.token = token;
        this.messageSource = messageSource;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error(messageSource.getMessage("jwt_token.invalid", null, null), ex);
            throw new BadCredentialsException(messageSource.getMessage("jwt_token.invalid", null, null));
        } catch (ExpiredJwtException expiredEx) {
            log.info(messageSource.getMessage("jwt_token.expired", null, null));
            throw new JwtExpiredTokenException(messageSource.getMessage("jwt_token.expired", null, null));
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
