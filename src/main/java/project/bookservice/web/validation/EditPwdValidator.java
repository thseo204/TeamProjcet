package project.bookservice.web.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.web.validation.form.EditPwdForm;


@Slf4j
@Component
public class EditPwdValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(EditPwdForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        EditPwdForm editPwdForm = (EditPwdForm) target;

        log.info("inputUserInfo {}=", editPwdForm);
        //비밀번호 일치 검증
        if(!editPwdForm.getNewPwd().equals(editPwdForm.getReNewPwd())){
            bindingResult.rejectValue("newPwd", "invalid.pwd", "비밀번호가 일치하지 않습니다.");
        }
    }
}
