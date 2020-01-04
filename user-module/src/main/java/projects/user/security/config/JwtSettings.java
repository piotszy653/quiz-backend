package projects.user.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties("jwt")
public class JwtSettings {

    private Integer tokenExpirationTime;

    private String tokenIssuer;

    private String tokenSigningKey;

    private Integer refreshTokenExpTime;

}
