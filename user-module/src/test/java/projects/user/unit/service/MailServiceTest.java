package projects.user.unit.service;

import projects.user.data.IMailServiceData;
import projects.user.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest implements IMailServiceData {

    @InjectMocks
    MailService mailService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    ITemplateEngine templateEngine;


    @Test
    public void shouldSendMailFromTemplate() {
        //given
        when(javaMailSender.createMimeMessage()).thenReturn(getDefaultMimeMessage());
        when(templateEngine.process(eq(getDefaultTemplate()), any(Context.class))).thenReturn("mail");

        //when
        mailService.sendMailFromTemplate(getDefaultReceiver(), getDefaultSubject(), getDefaultVariables(), getDefaultTemplate());

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}
