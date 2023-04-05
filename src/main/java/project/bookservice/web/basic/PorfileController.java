package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.member.Member;
import project.bookservice.service.login.LoginService;
import project.bookservice.service.mail.MailService;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.EditInformationValidator;
import project.bookservice.web.validation.EditPwdValidator;
import project.bookservice.web.validation.SignUpFormValidator;
import project.bookservice.web.validation.form.EditEmailForm;
import project.bookservice.web.validation.form.EditInformationForm;
import project.bookservice.web.validation.form.EditPwdForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class PorfileController {

    private final SignUpFormValidator signUpFormValidator;
    private final LoginService loginService;
    private final MailService mailService;
    private final EditInformationValidator editInformationValidator;
    private final EditPwdValidator editPwdValidator;
    private final ReportInfoService reportInfoService;
    private final MemberService memberService;
    private boolean availableEmail, emailCodeCheck;
    private String ePw; // 이메일 인증 코드

    @GetMapping("/profile")
    public String profile(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) {

        Integer reportCount = reportInfoService.countByWriterId(loginMember.getUserId());

        log.info("loginID={}", loginMember);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("reportCount", reportCount);
        return "basic/profile2";
    }


    @GetMapping("/editInformation")
    public String editInformation(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) {

        log.info("loginID={}", loginMember);
        model.addAttribute("loginMember", loginMember);
        return "/basic/editInformation";
    }

    @PostMapping("/editInformation")
    public String updateInformation(@Valid @ModelAttribute("loginMember") EditInformationForm editInformationForm, BindingResult bindingResult,
                                    HttpServletRequest request) {
        log.info("signUpForm ={}", editInformationForm);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/basic/editInformation";
        }

        // 비빌버호 일치 검증
        editInformationValidator.validate(editInformationForm, bindingResult);


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/basic/editInformation";
        }

        //회원 정보 등록
        memberService.update(editInformationForm);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "basic/editSuccessForm";
    }

    @GetMapping("/pwdCheck")
    public String pwdCheck() {
        return "detail/pwdCheck";
    }

    @PostMapping("/pwdCheck")
    public String pwdCheckPost(String userPwd, @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) {

        if (!Objects.equals(userPwd, loginMember.getUserPwd())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "detail/pwdCheck";
        }

        log.info("로그인아이디={}", loginMember);


        return "redirect:/editInformation";
    }

    @GetMapping("/editPwd")
    public String editPwd(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model, EditPwdForm editPwdForm) {

        editPwdForm.setUserId(loginMember.getUserId());
        editPwdForm.setUserPwd(loginMember.getUserPwd());

        log.info("editPwdForm = {} ", editPwdForm);

        model.addAttribute("editPwdForm", editPwdForm);
        return "basic/editPwd";
    }

    @PostMapping("/editPwd")
    public String editPwdPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              @Valid @ModelAttribute("editPwdForm") EditPwdForm editPwdForm, BindingResult bindingResult,
                              HttpServletRequest request, Model model) {
        editPwdForm.setUserId(loginMember.getUserId());
        log.info("editPwdForm ={}", editPwdForm);

        if (!Objects.equals(editPwdForm.getUserPwd(), loginMember.getUserPwd())) {
            model.addAttribute("message", "기존 비밀번호가 일치하지 않습니다");
            return "/basic/editPwd";
        }


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/basic/editPwd";
        }


        // 비빌버호 일치 검증
        editPwdValidator.validate(editPwdForm, bindingResult);


        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/basic/editPwd";
        }

        //회원 정보 등록
        memberService.editPwd(editPwdForm);

        log.info("editPwdForm={}", editPwdForm);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "basic/editSuccessForm";
    }

    @GetMapping("/editEmail")
    public String editEmail(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model, EditEmailForm editEmailForm) {

        editEmailForm.setUserEmail(loginMember.getUserEmail());
        log.info("editEmailForm = {} ", editEmailForm);
        model.addAttribute("editEmailForm", editEmailForm);
        model.addAttribute("availableEmail", availableEmail);
        model.addAttribute("emailCodeCheck", emailCodeCheck);
        return "basic/editEmail";
    }

    @GetMapping("/editEmail/mail")
    public String editEmailMail(@RequestParam("userEmail") String userEmail,
                                @RequestParam("newEmail") String newEmail,
                                Model model,
                                RedirectAttributes redirectAttributes) throws Exception {

        log.info("전달 받은 이메일 = {}", newEmail);
        ePw = mailService.sendSimpleMessage(newEmail);
        log.info("인증코드={}", ePw);

        availableEmail = true; // 이메일 전송 성공 시
        model.addAttribute("availableEmail", availableEmail);

        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("newEmail", newEmail);
        return "redirect:/editEmail";
    }

    @GetMapping("/editEmail/emailCode")
    public String editEmailEmailCode(@Valid @ModelAttribute("editEmailForm") EditEmailForm editEmailForm,
                                     BindingResult bindingResult,
                                     @RequestParam("emailCode") String emailCode,
                                     @RequestParam("userEmail") String userEmail,
                                     @RequestParam("newEmail") String newEmail,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        log.info("member.getEmailCode()={}", editEmailForm.getEmailCode());
        model.addAttribute("editEmailForm", editEmailForm);

        redirectAttributes.addAttribute("emailCode", emailCode);
//        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("newEmail", newEmail);

        signUpFormValidator.emailCodeCheckValidate(ePw, editEmailForm.getEmailCode(), bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            emailCodeCheck = false; // 이메일 코드 불일치 시 false
            model.addAttribute("emailCodeCheck", emailCodeCheck);
            availableEmail = true; // 이메일 전송은 이미 성공 했으니
            model.addAttribute("availableEmail", availableEmail);
//            return "redirect:/joinForm";
            return "/basic/editEmail";
        }
        emailCodeCheck = true; // 이메일 코드 일치 시
        model.addAttribute("emailCodeCheck", emailCodeCheck);

        editEmailForm.setNewEmail(newEmail);
       log.info("editEmailForm = {}" , editEmailForm);
        return "redirect:/editEmail";
    }

    @PostMapping("/editEmail")
    public String editEmailPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @Valid @ModelAttribute("editEmailForm") EditEmailForm editEmailForm,
                                BindingResult bindingResult,
                                String newEmail,
                                HttpServletRequest request, Model model) throws Exception {
        int index = newEmail.indexOf(",");
        if (index != -1) {
            newEmail = newEmail.substring(0, index);
        }
        editEmailForm.setNewEmail(newEmail);
        log.info("newEmail = {}", newEmail);
        log.info("emailCodeCheck = {}", emailCodeCheck);
            availableEmail = true; // 이메일 전송 성공 시

        model.addAttribute("availableEmail", availableEmail);
//        editEmailForm.setNewEmail(newEmail);
//        editEmailForm.setUserEmail(loginMember.getUserEmail());

        log.info("editEmailForm ={}", editEmailForm);
        log.info("loginMember ={}", loginMember);

        if (!Objects.equals(editEmailForm.getUserEmail(), loginMember.getUserEmail())) {
            log.info("editEmailForm = {}" , editEmailForm.getUserEmail());
            log.info("loginMember = {}" , loginMember.getUserEmail());
            model.addAttribute("message", "기존 이메일이 일치하지 않습니다");
            return "basic/editEmail";
        }

        log.info("editEmailForm = {} ", editEmailForm);

        if (emailCodeCheck == false) {

            bindingResult.reject("joinFalse", "이메일 인증을 해주세요.");
            return "basic/editEmail";
        }

        editEmailForm.setUserId(loginMember.getUserId());
        log.info("editEmailForm= {}" , editEmailForm);
        memberService.editEmail(editEmailForm);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        availableEmail = false;
        emailCodeCheck = false;
        return "basic/editSuccessForm";
    }

}
