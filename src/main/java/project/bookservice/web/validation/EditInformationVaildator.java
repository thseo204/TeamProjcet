package project.bookservice.web.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.web.validation.form.SignUpForm;

@Slf4j
@Component
public class EditInformationVaildator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        SignUpForm signUpForm = (SignUpForm) target;

        log.info("inputUserInfo {}=", signUpForm);
        //비밀번호 일치 검증
        if(!signUpForm.getUserPwd().equals(signUpForm.getUserRePwd())){
            bindingResult.rejectValue("userPwd", "invalid.pwd", "비밀번호가 일치하지 않습니다.");
        }
    }
}
