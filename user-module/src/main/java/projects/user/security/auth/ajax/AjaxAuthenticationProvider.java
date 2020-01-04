package projects.user.security.auth.ajax;

import projects.user.model.user.User;
import projects.user.security.model.UserContext;
import projects.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final MessageSource messageSource;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, messageSource.getMessage("authentication_data.required", null, null));

        String username = ((String) authentication.getPrincipal()).trim().toLowerCase();
        String password = (String) authentication.getCredentials();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.not_found.username", new Object[]{username}, null)));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(messageSource.getMessage("login_data.invalid", null, null));
        }

        if (!user.isEnabled()) {
            throw new DisabledException(messageSource.getMessage("user.activation_required.id", new Object[]{Long.toString(user.getId())}, null));
        }

        if (user.getRoleGroup().getRoles() == null) {
            throw new InsufficientAuthenticationException(messageSource.getMessage("user.roles_required.id", new Object[]{Long.toString(user.getId())}, null));
        }

        List<GrantedAuthority> authorities = user.getRoleGroup().getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        UserContext userContext = new UserContext(user.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
