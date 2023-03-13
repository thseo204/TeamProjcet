package project.bookservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.bookservice.domain.repository.MemberMapper;
import project.bookservice.domain.repository.MemberRepositoryImpl;
import project.bookservice.service.MemberService;
import project.bookservice.service.MemberServiceImpl;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final MemberMapper memberMapper;

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepositoryImpl memberRepository(){
        return new MemberRepositoryImpl(memberMapper);
    }
}
