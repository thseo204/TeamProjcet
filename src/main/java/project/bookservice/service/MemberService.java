package project.bookservice.service;

import project.bookservice.domain.member.Member;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    SignUpForm save(SignUpForm signUpForm);

//    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Member> findById(String memberId);
    int existsByEmail(String email);

    int existsByUserId(String userId);


//    List<Member> findItems(ItemSearchCond itemSearch);
}