package project.bookservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.repository.MemberRepository;
import project.bookservice.web.validation.form.SignUpForm;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        SignUpForm signUpForm = (SignUpForm) target;

        log.info("inputUserInfo {}=", signUpForm);
        // 이메일 중복 검증
        if(memberRepository.existsByUserEmail(signUpForm.getUserEmail()) == 1){
            bindingResult.rejectValue("userEmail","invalid.email", "이미 사용중인 이메일 입니다.");
        }
        // 아이디 중복 검증
        if(memberRepository.existsByUserId(signUpForm.getUserId()) == 1){
            bindingResult.rejectValue("userId", "invalid.id", "이미 사용중인 아이디 입니다.");
        }
        //비밀번호 일치 검증
        if(!signUpForm.getUserPwd().equals(signUpForm.getUserRePwd())){
            bindingResult.rejectValue("userPwd", "invalid.pwd", "비밀번호가 일치하지 않습니다.");
        }
    }
}
