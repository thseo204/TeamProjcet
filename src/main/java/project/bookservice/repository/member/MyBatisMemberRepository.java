package project.bookservice.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.login.LoginForm;
import project.bookservice.domain.member.Member;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {
    // 대부분 매퍼에 위임하는 코드임
    private final MemberMapper memberMapper;

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public SignUpForm save(SignUpForm signUpForm) {
        log.info("member info={}", signUpForm);

        memberMapper.save(signUpForm);
        return signUpForm;
    }
	
    @Override
    public List<Member> findAll() {
        return memberMapper.findAll();
    }

//    @Override
//    public void update(Long itemId, ItemUpdateDto updateParam) {
//        itemMapper.update(itemId, updateParam);
//    }

    @Override
    public Optional<Member> findById(String userId) {
        log.info("userId info={}", userId);

        return memberMapper.findById(userId);
    }

    @Override
    public int existsByUserEmail(String email) {
        log.info("existsByEmail info={}", email);
        return memberMapper.findByEmail(email);
    }

    @Override
    public int existsByUserId(String userId) {
        log.info("existsByUserId info={}", userId);
//        boolean byUserId = memberMapper.findByUserId(userId);
        return memberMapper.findByUserId(userId);
    }

    @Override
    public Optional<Member> findByLoginId(String userId) {
        log.info("loginId info={}", userId);

        return memberMapper.findByLoginId(userId);
    }
}
