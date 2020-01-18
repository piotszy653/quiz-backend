package projects.user.unit.service;

import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import projects.user.data.IRegistrationData;
import projects.user.data.IRoleGroupData;
import projects.user.data.IUserData;
import projects.user.dto.registration.LoginResponseDto;
import projects.user.dto.registration.RegistrationDto;
import projects.user.model.user.User;
import projects.user.model.user.UserProfile;
import projects.user.security.model.UserContext;
import projects.user.security.model.token.AccessJwtToken;
import projects.user.security.model.token.JwtTokenFactory;
import projects.user.service.MailService;
import projects.user.service.RegistrationService;
import projects.user.service.RoleGroupService;
import projects.user.service.UserService;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest implements IRegistrationData, IUserData, IRoleGroupData {

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    UserService userService;

    @Mock
    RoleGroupService roleGroupService;

    @Mock
    MailService mailService;

    @Mock
    MessageSource messageSource;

    @Mock
    JwtTokenFactory tokenFactory;



    private UserProfile defaultProfile = new UserProfile(null);

    @Test
    public void register() {

        //given
        RegistrationDto registrationDto = getDefaultRegistrationDto(true);
        when(roleGroupService.getByName(getUserRoleGroup().getName())).thenReturn(getUserRoleGroup());
        when(userService.findByUsername(getDefaultUsername())).thenReturn(Optional.empty());
        when(messageSource.getMessage("email.registration.subject", null, LocaleContextHolder.getLocale())).thenReturn("");
        when(tokenFactory.createAccessJwtToken(any(UserContext.class))).thenReturn(new AccessJwtToken("", new DefaultClaims()));
        when(tokenFactory.createRefreshToken(any(UserContext.class))).thenReturn(new AccessJwtToken("", new DefaultClaims()));

        //when
        LoginResponseDto registrationResponseDto = registrationService.register(registrationDto);

        //then
        User returnedUser = registrationResponseDto.getUser();
        verify(mailService, times(1)).sendMailFromTemplate(any(String.class), any(String.class), any(), any());
        assertNotNull(returnedUser);
        assertEquals(registrationDto.getUsername(), returnedUser.getUsername());
        assertNotNull(registrationResponseDto.getToken());
        assertNotNull(registrationResponseDto.getRefreshToken());
        assertTrue(returnedUser.isEnabled());
        assertEquals("USER", returnedUser.getRoleGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerWithExistingMail() {
        //given
        when(userService.findByUsername(getDefaultUsername())).thenReturn(Optional.of(getDefaultUser(defaultProfile, getDefaultRoleGroup())));

        //when
        registrationService.register(getDefaultRegistrationDto(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerWithoutAcceptingRegulations() {
        //given
        RegistrationDto registrationDto = getDefaultRegistrationDto(false);
        //when
        registrationService.register(registrationDto);
    }
}
