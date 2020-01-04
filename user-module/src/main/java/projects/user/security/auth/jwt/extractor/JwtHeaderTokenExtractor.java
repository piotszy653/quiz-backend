package projects.user.security.auth.jwt.extractor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtHeaderTokenExtractor implements TokenExtractor {
    private static String HEADER_PREFIX = "Bearer ";
    private final MessageSource messageSource;

    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException(messageSource.getMessage("authorization_header.required", null, null));
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException(messageSource.getMessage("authorization_header.invalid", null, null));
        }

        return header.substring(HEADER_PREFIX.length());
    }
}
