package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.login.LoginForm;
import project.bookservice.domain.naverUser.NaverUser;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.service.login.LoginService;
import project.bookservice.domain.member.Member;
import project.bookservice.repository.member.MemberRepository;
import project.bookservice.service.mail.MailService;
import project.bookservice.web.SessionConst;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MailService mailService;
    private final MemberRepository memberRepository;

    @GetMapping("/loginForm")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "basic/loginForm";
    }

    /**
     * 로그인 이후 redirect 처리
     */
    @PostMapping("/loginForm")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "basic/loginForm";
        }
        Member loginMember = loginService.login(form.getUserId(),
                form.getUserPwd());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "basic/loginForm";
        }
        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //redirectURL 적용
        return "redirect:" + redirectURL;
    }

    @GetMapping("/callback/naver")
    public String loginWithNaver(
            @RequestParam(defaultValue = "/") String redirectURL,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname,
            HttpServletRequest request,
            Model model){

        Member loginMember = new Member();
        loginMember.setUserName(nickname);
        loginMember.setUserId(email);

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        model.addAttribute("loginMember",loginMember);
        log.info("nickname={}",nickname);
        log.info("loginmem={}",loginMember);

        // 리디렉션 처리하기
        return "basic/test/callback";
    }





    @GetMapping("/logout")
    public String logout(HttpServletRequest request,@RequestParam(defaultValue = "/") String redirectURL) {

        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:"+redirectURL;
    }

    @GetMapping("/findId")
    public String findId(@ModelAttribute("loginForm") LoginForm form){

        return "basic/findId";
    }


    @PostMapping("/findIdByEmail")
    public String findIdByEmail(String userId,Model model) throws MessagingException {


        // Retrieve the member from the database using the user ID
        Member member = loginService.findId(userId);
        log.info("ID?= {}", member);
        if(member == null){
            model.addAttribute("message", "존재하지 않는 아이디 입니다");
            return "basic/findId";
        }
        mailService.sendMail(member);
        model.addAttribute("member",member);
        return "detail/emailComplete";
    }




}
