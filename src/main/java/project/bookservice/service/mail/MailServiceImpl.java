package project.bookservice.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.StringValueExp;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@PropertySource("classpath:email.properties")
public class MailServiceImpl implements MailService{

    @Autowired
    JavaMailSender javaMailSender; // Bean 등록해둔 MailConfig를 emailSender 라는 이름으로 autowired

    private String ePw; //인증번호
    @Value("${spring.mail.username}")
    private String id;
    @Override
    public void sendMail(Member member) throws MessagingException {
        String subject="[Reading journal] Password";
        String body = "<h1>Password :" + member.getUserPwd() + "</h1>";
        body += "<p><a href='http://localhost:8000/loginForm'>Go login page</a></p>";
        EmailSender.sendEmail(member.getUserEmail(), subject, body);
    }

    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("mail to={}", to);
        log.info("mail ePw={}", ePw);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("독서노트 회원가입 이메일 인증"); //제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요 </h1>";
        msg += "<h1> 독서노트 회원가입을 위해 보낸 인증코드입니다. </h1>";
        msg += "<br/>";
        msg += "<p> 아래 코드를 회원가입 창으로 돌아가 입력해주세요. </p>";
        msg += "<br/>";
        msg += "<p> [독서노트]기록의 기억을 믿어보세요. </p>";
        msg += "<p> 감사합니다:-) </p>";
        msg += "<br/>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "CODE : <strong>" + ePw + "</strong></div><br/>"; //메일에 인증번호 넣기
        msg += "</div>";
        message.setText(msg,"utf-8", "html"); // 내용 charset 타입, subType
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress(id, "DokSeoNote"));

        return message;
    }

    // 랜덤 인증코드 전송
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for(int i = 0; i < 8; i++){ // 인증코드 8자리
            int index = random.nextInt(3); // 0~2 까지 랜덤, random 값에 따라 아래 switch 문 실행

            switch(index){
                case 0:
                    key.append((char) ((int) (random.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 -> (char) 98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (random.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    // 0~9
                    break;
            }

        }

        return key.toString();
    }

    //메일 발송
    @Override
    public String sendSimpleMessage(String to) throws Exception {

        ePw = createKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); //메일 발송
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.info("MailSendError", e);
            throw new IllegalArgumentException();
        }
        return ePw;// 메일로 보냈던 인증 코드를 서버로 반환
    }
}