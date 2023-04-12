package project.bookservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
public class MailConfig {
    @Value("${spring.mail.username}")
    private String id;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host); // 네이버로그인 -> 메일 -> 환결설정 -> POP3/IMAP설정 내 SMTP 서버명
        javaMailSender.setUsername(id); // 네이버 아이디
        javaMailSender.setPassword(password); // 네이버 비밀번호

        javaMailSender.setPort(port); // 메일 인증 서버 SMTP 포트

        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp"); //프로토콜 설정
        properties.put("mail.smtp.auth", "true"); //smtp 인증
        properties.put("mail.smtp.starttls.enable", "true"); //smtp starttls 사용
        properties.setProperty("mail.debug", "true"); //디버그 사용
        properties.put("mail.smtp.tls.trust", "smtp.mailplug.co.kr"); //ssl 인증서버는 naver.com
        properties.put("mail.smtp.tls.enable", "true"); //ssl 사용
        return properties;
    }
}
