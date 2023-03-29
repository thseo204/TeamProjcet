package project.bookservice.service.mail;

import project.bookservice.domain.member.Member;

import javax.mail.MessagingException;

public interface MailService {

    public void sendMail(Member member) throws MessagingException;
}
