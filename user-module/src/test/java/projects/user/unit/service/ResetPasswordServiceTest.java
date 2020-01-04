package projects.user.unit.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import projects.user.data.IResetPasswordData;
import projects.user.data.IRoleGroupData;
import projects.user.data.IUserData;
import projects.user.dto.resetPassword.ForgotPasswordDto;
import projects.user.model.ResetPasswordToken;
import projects.user.model.user.User;
import projects.user.repository.ResetPasswordTokenRepository;
import projects.user.service.MailService;
import projects.user.service.UserService;
import projects.user.service.resetPassword.ResetPasswordService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordServiceTest implements IResetPasswordData, IUserData, IRoleGroupData {

    @InjectMocks
    ResetPasswordService resetPasswordService;

    @Mock
    UserService userService;

    @Mock
    MailService mailService;

    @Mock
    ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Mock
    MessageSource messageSource;

    @Test
    public void processForgotPasswordWithCorrectUsername() {

        //given
        ReflectionTestUtils.setField(
                resetPasswordService,
                "expirationTime",
                getDefaultExpirationTime()
        );
        ForgotPasswordDto forgotPasswordDto = getDefaultForgotPasswordDto();
        User user = getDefaultUser(getDefaultRoleGroup());
        when(userService.findByUsername(forgotPasswordDto.getUsername())).thenReturn(Optional.ofNullable(user));
        when(messageSource.getMessage("email.reset.password.subject", null, LocaleContextHolder.getLocale())).thenReturn("");

        //when
        resetPasswordService.processForgotPassword(getDefaultForgotPasswordDto());

        //then
        verify(resetPasswordTokenRepository, times(1)).save(any(ResetPasswordToken.class));
        verify(mailService, times(1)).sendMailFromTemplate(any(String.class), any(String.class), any(), any());

    }

    @Test
    public void processForgotPasswordWithIncorrectUsername() {
        //given
        ReflectionTestUtils.setField(
                resetPasswordService,
                "expirationTime",
                getDefaultExpirationTime()
        );
        ForgotPasswordDto forgotPasswordDto = getDefaultForgotPasswordDto();
        when(userService.findByUsername(forgotPasswordDto.getUsername())).thenReturn(Optional.empty());

        //when
        resetPasswordService.processForgotPassword(getDefaultForgotPasswordDto());

        //then
        verify(resetPasswordTokenRepository, times(0)).save(any(ResetPasswordToken.class));
        verify(mailService, times(0)).sendMailFromTemplate(any(String.class), any(String.class), any(), any());
    }

    @Test
    public void resetPasswordWithCorrectUuid() {

        //given
        String uuid = getUuid();
        User user = getDefaultUser(getDefaultRoleGroup());
        ResetPasswordToken token = getDefaultResetPasswordToken(uuid, getDefaultUser(getDefaultRoleGroup()));
        user.setPassword(getDefaultResetPasswordDto(uuid).getPassword());
        when(resetPasswordTokenRepository.findByUuid(uuid)).thenReturn(Optional.ofNullable(token));


        //when
        resetPasswordService.resetPassword(getDefaultResetPasswordDto(uuid));

        //then
        verify(resetPasswordTokenRepository, times(1)).delete(token);
        verify(userService, times(1)).save(any(User.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void resetPasswordWithIncorrectUuid() {
        resetPasswordService.resetPassword(getDefaultResetPasswordDto(getUuid()));
    }
}
