package projects.user.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import projects.user.security.auth.ajax.AjaxAuthenticationProvider;
import projects.user.security.auth.ajax.AjaxLoginProcessingFilter;
import projects.user.security.auth.jwt.JwtAuthenticationProvider;
import projects.user.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import projects.user.security.auth.jwt.SkipPathRequestMatcher;
import projects.user.security.auth.jwt.extractor.TokenExtractor;
import projects.user.security.exceptions.handlers.RestAccessDeniedHandler;
import projects.user.security.exceptions.handlers.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_HEADER_NAME = "Authentication";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String API_ROOT_URL = "/api/**";

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    private final RestAccessDeniedHandler accessDeniedHandler;

    private final AuthenticationSuccessHandler successHandler;

    private final AuthenticationFailureHandler failureHandler;

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final TokenExtractor tokenExtractor;

    private final MessageSource messageSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper;

    private AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(SecurityEndpoints.AUTHENTICATION_URL.value, successHandler, failureHandler, objectMapper, messageSource);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    private JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher, messageSource);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(
                        SecurityEndpoints.AUTHENTICATION_URL.value,
                        SecurityEndpoints.REFRESH_TOKEN_URL.value,
                        SecurityEndpoints.REGISTRATION_URL.value,
                        SecurityEndpoints.RESET_PASSWORD_URL.value,
                        SecurityEndpoints.ACTUATOR_URL.value,
                        SecurityEndpoints.FETCH_FILE_URL.value)
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(
                        Stream.of(SecurityEndpoints.values())
                                .map((endpoint) -> endpoint.value)
                                .collect(Collectors.toList())),
                        UsernamePasswordAuthenticationFilter.class);

        //for enabling h2-console
        http.headers().frameOptions().disable();
    }
}
