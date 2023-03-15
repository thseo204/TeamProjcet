package project.bookservice.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import project.bookservice.domain.member.Member;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

	 Member save(Member member);
    SignUpForm save(SignUpForm signUpForm);

//    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Member> findById(String memberId);

    int existsByUserEmail(String email);
    int existsByUserId(String userId);
	
	 Optional<Member> findByUserId(String userId);
    List<Member> findAll();

}
