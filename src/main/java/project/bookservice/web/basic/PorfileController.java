package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bookservice.domain.member.Member;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.EditInformationVaildator;
import project.bookservice.web.validation.form.SignUpForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class PorfileController {

    private final EditInformationVaildator editInformationVaildator;
    private final ReportInfoService reportInfoService;
    private final MemberService memberService;

    @GetMapping("/profile")
    public String profile(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){

        Integer reportCount = reportInfoService.countByWriterId(loginMember.getUserId());

        log.info("loginID={}",loginMember);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("reportCount",reportCount);
        return "basic/profile2";
    }

    @GetMapping("/profile2")
    public String profile2(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){

        Integer reportCount = reportInfoService.countByWriterId(loginMember.getUserId());

        log.info("loginID={}",loginMember);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("reportCount",reportCount);
        return "basic/profile";
    }

    @GetMapping("/editInformation")
    public String editInformation(@SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){

        log.info("loginID={}",loginMember);
        model.addAttribute("loginMember", loginMember);
        return "/basic/editInformation";
    }

    @PostMapping("/editInformation")
    public String updateInformation(@Valid @ModelAttribute("loginMember") SignUpForm signUpForm, BindingResult bindingResult,
                                    HttpServletRequest request){
        log.info("signUpForm ={}", signUpForm);

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/editInformation";
        }

        // 비빌버호 일치 검증
        editInformationVaildator.validate(signUpForm, bindingResult);


        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/editInformation";
        }

        //회원 정보 등록
        memberService.update(signUpForm);

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
    public String pwdCheckPost(String userPwd,  @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) {

        if(!Objects.equals(userPwd, loginMember.getUserPwd())){
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "detail/pwdCheck";
        }

        log.info("로그인아이디={}",loginMember);


        return "redirect:/editInformation";
    }
}
