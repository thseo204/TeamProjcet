package project.bookservice.web.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.web.validation.form.EditInformationForm;

@Slf4j
@Component
public class EditInformationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(EditInformationForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        EditInformationForm editInformationForm = (EditInformationForm) target;

        log.info("inputUserInfo {}=", editInformationForm);
        //비밀번호 일치 검증
        if(!editInformationForm.getUserPwd().equals(editInformationForm.getUserRePwd())){
            bindingResult.rejectValue("userPwd", "invalid.pwd", "비밀번호가 일치하지 않습니다.");
        }
    }
}
