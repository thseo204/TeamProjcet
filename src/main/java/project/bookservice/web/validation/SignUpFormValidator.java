package project.bookservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.repository.member.MemberRepository;
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
        if(signUpForm.getUserEmail().isEmpty()){
            bindingResult.rejectValue("userEmail","invalid.email.null", "");
        }
        //비밀번호 일치 검증
        if(!signUpForm.getUserPwd().equals(signUpForm.getUserRePwd())){
            bindingResult.rejectValue("userPwd", "invalid.pwd", "비밀번호가 일치하지 않습니다.");
        }
        if(signUpForm.getUserPwd().isEmpty() || signUpForm.getUserRePwd().isEmpty()){
            bindingResult.rejectValue("userPwd", "invalid.pwd.null", "");
        }
    }

    public void idCheckValidate(Object target, Errors errors){
        BindingResult bindingResult = (BindingResult) errors;
        String userId = (String) target;
        log.info("idCheckValidate {}=", userId);
        // 아이디 중복 검증
        if(memberRepository.existsByUserId(userId) == 1){
            bindingResult.rejectValue("userId", "invalid.id", "이미 사용중인 아이디 입니다.");
        }
        if(userId.isEmpty()){
            bindingResult.rejectValue("userId", "invalid.id.null", "아이디를 입력해주세요.");
        }
    }

    public void emailCheckValidate(Object target, Errors errors){
        BindingResult bindingResult = (BindingResult) errors;
        String userEmail = (String) target;
        log.info("emailCheckValidate {}=", userEmail);
        // 아이디 중복 검증
        if(memberRepository.existsByUserEmail(userEmail) == 1){
            bindingResult.rejectValue("userEmail", "invalid.email", "이미 사용중인 이메일 입니다.");
        }
        if(userEmail.isEmpty()){
            bindingResult.rejectValue("userEmail", "invalid.email.null", "이메일을 입력해주세요.");
        }
    }


    public void emailCodeCheckValidate(String ePw, Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        String emailCode = (String) target;
        log.info("idCheckValidate ={}", emailCode);
        // 아이디 중복 검증
        if(!ePw.equals(emailCode)){
            bindingResult.rejectValue("emailCode", "invalid.ePw", "인증번호가 일치하지 않습니다.");
        }
        if(emailCode.isEmpty()){
            bindingResult.rejectValue("emailCode", "invalid.ePw.null", "인증번호를 입력해주세요.");
        }
    }
}
