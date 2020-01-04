package projects.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties("mail")
public class MailSettings {
    private String host;
    private String username;
    private String password;
    private boolean auth;
    private boolean starttls;
    private Integer port;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setPassword(password);
        javaMailSender.setUsername(username);

        Properties prop = javaMailSender.getJavaMailProperties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", auth);
        prop.put("mail.smtp.starttls.enable", starttls);

        return javaMailSender;
    }
}
