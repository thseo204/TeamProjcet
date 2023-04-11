package project.bookservice.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class EmailSender {

    public static void sendEmail(String to, String subject, String body) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("[EmailId]", "[password]");
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("nanovia100@naver.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body, "text/html");

        Transport.send(message);
    }
}




