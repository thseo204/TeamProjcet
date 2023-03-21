package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bookservice.domain.member.Member;
import project.bookservice.service.MemberService;
import project.bookservice.service.join.JoinService;
import project.bookservice.web.validation.SignUpFormValidator;
import project.bookservice.web.validation.form.SignUpForm;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JoinService joinService;
    private final SignUpFormValidator signUpFormValidator;

    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute Member member){
        return "/basic/joinForm";
    }



    @PostMapping("/joinForm")
    public String signUpSubmit(@Valid @ModelAttribute("member") SignUpForm signUpForm, BindingResult bindingResult) {
        log.info("signUpForm ={}", signUpForm);

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/joinForm";
        }

        //중복 검사(아이디, 이메일, 비빌버호 일치 검증)
        signUpFormValidator.validate(signUpForm, bindingResult);


        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/joinForm";
        }

        //회원 정보 등록
        memberService.save(signUpForm);

        return "/basic/joinSuccessForm"; // 회원가입 성공 시 축하페이지로 이동
    }

    @GetMapping("/joinSuccessForm")
    public String joinSuccessForm(){
        return "/basic/joinSuccessForm";
    }
}
