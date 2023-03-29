package project.bookservice.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;
import project.bookservice.repository.member.MemberRepository;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public SignUpForm save(SignUpForm signUpForm) {
        return memberRepository.save(signUpForm);
    }

    @Override
    public void update(SignUpForm signUpForm) {
        memberRepository.update(signUpForm);
    }

    @Override
    public void editCharIcon(Member loginmember) {
        memberRepository.editCharIcon(loginmember);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public int existsByEmail(String email) {
        return memberRepository.existsByUserEmail(email);
    }

    @Override
    public int existsByUserId(String userId) {
        return memberRepository.existsByUserId(userId);
    }
//
//    @Override
//    public List<Item> findItems(ItemSearchCond cond) {
//        return itemRepository.findAll(cond);
//    }

}
