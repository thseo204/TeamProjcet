package project.bookservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.repository.member.MemberRepository;
import project.bookservice.service.member.BookmarkHistoryOfMemberService;
import project.bookservice.web.validation.form.SignUpForm;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookmarkHistoryOfMemberValidator implements Validator {

    private final BookmarkHistoryOfMemberService bookmarkHistoryOfMemberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BindingResult bindingResult = (BindingResult) errors;
        BookmarkHistoryOfMember bookmarkHistoryOfMember = (BookmarkHistoryOfMember) target;

        log.info("bookmarkHistoryOfMember {}=", bookmarkHistoryOfMember);

        if(bookmarkHistoryOfMember == null){
            bindingResult.rejectValue("isbn","invalid.bookmarkHistoryOfMember.isbn", "저장 내역이 없습니다.");
        }
    }
}
