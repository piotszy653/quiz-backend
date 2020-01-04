package projects.user.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @NonNull
    private JavaMailSender javaMailSender;

    @NonNull
    private ITemplateEngine templateEngine;

    @Value("${mail.template.logo}")
    private String logoSource;

    public void sendMailFromTemplate(String receiver, String subject, Map<String, String> variables, String template) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(buildMailFromTemplate(template, variables), true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            log.error(e.toString());
        }
    }

    private String buildMailFromTemplate(String template, Map<String, String> variables) {
        Context context = new Context();
        insertLogo(variables);
        variables.forEach(context::setVariable);
        return templateEngine.process(template, context);
    }

    private void insertLogo(Map<String, String> variables) {
        variables.put("logo", logoSource);
    }
}
