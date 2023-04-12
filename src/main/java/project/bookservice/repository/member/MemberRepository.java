package project.bookservice.repository.member;

import project.bookservice.domain.member.Member;
import project.bookservice.web.validation.form.EditEmailForm;
import project.bookservice.web.validation.form.EditInformationForm;
import project.bookservice.web.validation.form.EditPwdForm;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    SignUpForm save(SignUpForm signUpForm);

    void update(EditInformationForm editInformationForm);

    void editCharIcon(Member loginmember);

    List<Member> findAll();

    Optional<Member> findById(String memberId);

    int existsByUserEmail(String email);

    int existsByUserId(String userId);


    Optional<Member> findByLoginId(String userId);

    Member findId(String userId);

    Member findIdByEmail(String userEmail);

    void editPwdByRandom(Member member);

    void editPwd(EditPwdForm editPwdForm);

    void editEmail(EditEmailForm editEmailForm);
}
