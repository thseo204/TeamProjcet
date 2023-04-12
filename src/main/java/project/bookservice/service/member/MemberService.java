package project.bookservice.service.member;

import project.bookservice.domain.member.Member;
import project.bookservice.web.validation.form.EditEmailForm;
import project.bookservice.web.validation.form.EditInformationForm;
import project.bookservice.web.validation.form.EditPwdForm;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    SignUpForm save(SignUpForm signUpForm);

    void update(EditInformationForm editInformationForm);

    void editPwd(EditPwdForm editPwdForm);

    void editEmail(EditEmailForm editEmailForm);

    void editCharIcon(Member loginmember);

    Optional<Member> findById(String memberId);

    List<Member> findAll();

    int existsByEmail(String email);

    int existsByUserId(String userId);
}
