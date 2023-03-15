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
        return memberRepository.findByUserId(userId)
                .filter(m->m.getUserPwd().equals(userPwd))
                .orElse(null);

    }
}