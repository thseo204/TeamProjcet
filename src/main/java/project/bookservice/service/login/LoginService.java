package project.bookservice.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;
import project.bookservice.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String userId, String userPwd){
        return memberRepository.findByLoginId(userId)
                .filter(m->m.getUserPwd().equals(userPwd))
                .orElse(null);
    }

    public Member findId(String userId){
        return memberRepository.findId(userId);
    }

    public Member findIdByEmail(String userEmail){
        return memberRepository.findIdByEmail(userEmail);
    }
}
