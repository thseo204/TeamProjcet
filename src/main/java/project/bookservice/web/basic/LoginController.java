package project.bookservice.web.basic;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.login.LoginForm;
import project.bookservice.service.login.LoginService;
import project.bookservice.domain.member.Member;
import project.bookservice.service.mail.MailService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.SignUpFormValidator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MailService mailService;
    private String ePw; // 이메일 인증 코드
    private boolean availableId, availableEmail, emailCodeCheck;
    private final SignUpFormValidator signUpFormValidator;

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
            HttpServletRequest request,
            Model model){

        Member loginMember = new Member();


        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        model.addAttribute("loginMember",loginMember);

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
    public String findId(@ModelAttribute("loginForm") LoginForm form, Model model){
        model.addAttribute("availableId", availableId);
        model.addAttribute("availableEmail", availableEmail);
        model.addAttribute("emailCodeCheck", emailCodeCheck);
        return "basic/findId";
    }


    @GetMapping("/findPwdByEmail")
    public String findPwdByEmail(String userId,Model model) throws MessagingException {


        // Retrieve the member from the database using the user ID
        Member member = loginService.findId(userId);
        log.info("ID?= {}", member);
        if(member == null){
            model.addAttribute("message1", "존재하지 않는 아이디 입니다");
            return "basic/findId";
        }
      //  mailService.sendMail(member);
        model.addAttribute("member",member);
        return "detail/emailAuthentication";
    }

    @GetMapping("/findIdByEmail")
    public String findIdByEmail(String userEmail,Model model) throws MessagingException {
        Member member = loginService.findIdByEmail(userEmail);
        log.info("ID?= {}", member);
        if(member == null){
            model.addAttribute("message2", "가입된 아이디가 없습니다");
            return "basic/findId";
        }

        model.addAttribute("member",member);
        return "detail/findIdByEmailAuthentication";
    }

    @GetMapping("/emailAuthentication")
    public String mailConfirm(@RequestParam("userEmail") String userEmail,
                              @RequestParam("userId") String userId,
                              Model model,
                              RedirectAttributes redirectAttributes) throws Exception {
        log.info("전달 받은 이메일 = {}", userEmail);
        ePw = mailService.sendSimpleMessage(userEmail);
        log.info("인증코드={}", ePw);

        Member member = loginService.findId(userId);


        availableEmail = true; // 이메일 전송 성공 시
        model.addAttribute("availableEmail", availableEmail);
        model.addAttribute("member",member);

        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("userId", userId);

        return "detail/emailAuthentication";
    }

    @GetMapping("/emailAuthentication/emailCode")
    public String emailCheck(@Valid @ModelAttribute("member") Member member,
                             BindingResult bindingResult,
                             @RequestParam("emailCode") String emailCode,
                             @RequestParam("userEmail") String userEmail,
                             @RequestParam("userId") String userId,
                             @RequestParam("userPwd") String userPwd,
                             Model model,
                             RedirectAttributes redirectAttributes) throws MessagingException {


        log.info("member.getEmailCode()={}", member.getEmailCode());

        redirectAttributes.addAttribute("userId", userId);
        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("emailCode", emailCode);
        redirectAttributes.addAttribute("userPwd", userPwd);

        //이메일 인증코드 일치 검사
        signUpFormValidator.emailCodeCheckValidate(ePw, member.getEmailCode(), bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            emailCodeCheck = false; // 이메일 코드 불일치 시 false
            model.addAttribute("emailCodeCheck", emailCodeCheck);
            availableEmail = true; // 이메일 전송은 이미 성공 했으니
            model.addAttribute("availableEmail", availableEmail);
            return "/detail/emailAuthentication";
        }

        mailService.sendMail(member);

        return "/detail/emailComplete";
    }

    @GetMapping("/findIdByEmailAuthentication")
    public String mailConfirm2(@RequestParam("userEmail") String userEmail,
                              Model model,
                              RedirectAttributes redirectAttributes) throws Exception {
        log.info("전달 받은 이메일 = {}", userEmail);
        ePw = mailService.sendSimpleMessage(userEmail);
        log.info("인증코드={}", ePw);

        Member member = loginService.findIdByEmail(userEmail);


        availableEmail = true; // 이메일 전송 성공 시
        model.addAttribute("availableEmail", availableEmail);
        model.addAttribute("member",member);

        redirectAttributes.addAttribute("userEmail", userEmail);

        return "detail/findIdByEmailAuthentication";
    }

    @GetMapping("/findIdByEmailAuthentication/emailCode")
    public String emailCheck2(@Valid @ModelAttribute("member") Member member,
                             BindingResult bindingResult,
                             @RequestParam("emailCode") String emailCode,
                             @RequestParam("userEmail") String userEmail,
                             Model model,
                             RedirectAttributes redirectAttributes) throws MessagingException {

        Member member2 = loginService.findIdByEmail(userEmail);
        member.setUserId(member2.getUserId());
        log.info("member.getEmailCode()={}", member.getEmailCode());

        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("emailCode", emailCode);


        //이메일 인증코드 일치 검사
        signUpFormValidator.emailCodeCheckValidate(ePw, member.getEmailCode(), bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            emailCodeCheck = false; // 이메일 코드 불일치 시 false
            model.addAttribute("emailCodeCheck", emailCodeCheck);
            availableEmail = true; // 이메일 전송은 이미 성공 했으니
            model.addAttribute("availableEmail", availableEmail);
            return "/detail/findIdByEmailAuthentication";
        }

        model.addAttribute(member);

        return "/detail/findIdComplete";
    }



    @GetMapping("/newPassword")
    public String newPassword(){
        return "/basic/NewPassword";
    }



}
