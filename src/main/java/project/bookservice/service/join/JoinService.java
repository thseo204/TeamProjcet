package project.bookservice.service.join;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;
import project.bookservice.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;

    /**
     * @return null 이면 회원가입 가능한 아이디
     */
    public Member idCheck(String memberId) {
        return memberRepository.findById(memberId)
                .filter(m -> m.getUserId().equals(memberId))
                .orElse(null);
    }
}
