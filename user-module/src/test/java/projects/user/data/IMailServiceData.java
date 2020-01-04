package projects.user.data;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

public interface IMailServiceData {

    default String getDefaultReceiver() {
        return "default@receiver.com";
    }

    default String getDefaultSubject() {
        return "default subject";
    }

    default String getDefaultTemplate() {
        return "default template";
    }

    default Map<String, String> getDefaultVariables() {
        return new HashMap<>();
    }

    default MimeMessage getDefaultMimeMessage() {
        JavaMailSender javaMailSender = new JavaMailSenderImpl();
        return javaMailSender.createMimeMessage();
    }
}
