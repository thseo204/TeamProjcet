package project.bookservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.repository.member.MemberRepository;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.SignUpForm;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportSaveFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        ReportForm form = (ReportForm) target;

        log.info("inputReportSaveForm {}=", form);

        // 이미지 파일 업로드 검증
        if(form.getAttachFile().isEmpty()){
            bindingResult.rejectValue("attachFile", "invalid.reportInfo.attachFile.null", "이미지를 선택해주세요");
        }
        //내용 입력 검증
        if(form.getContent().isEmpty()){
            bindingResult.rejectValue("content", "invalid.reportInfo.content.null", "내용을 입력해주세요.");
        }
        // 공개여부 선택 검
        if(form.getDisclosure() == null){
            bindingResult.rejectValue("disclosure", "invalid.reportInfo.disclosure.null", "공개여부를 선택해주세요.");
        }
    }
}
