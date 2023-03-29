package project.bookservice.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;

import javax.mail.MessagingException;


@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendMail(Member member) throws MessagingException {
        String subject="[Reading journal] Password";
        String body = "<h1>Password :" + member.getUserPwd() + "<h1>";
        body += "<p><a href='http://localhost:8000/loginForm'>Go login page</a></p>";
        EmailSender.sendEmail(member.getUserEmail(), subject, body);
    }
}