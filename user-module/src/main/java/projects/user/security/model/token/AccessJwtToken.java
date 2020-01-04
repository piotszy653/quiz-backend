package projects.user.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class AccessJwtToken implements JwtToken {
    @JsonIgnore
    private final String rawToken;
    @JsonIgnore
    private Claims claims;

    @Override
    public String getToken() {
        return this.rawToken;
    }
}
