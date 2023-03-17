package project.bookservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import project.bookservice.repository.comment.CommentMapper;
import project.bookservice.repository.comment.CommentRepository;
import project.bookservice.repository.comment.MybatisCommentRepository;
import project.bookservice.repository.member.MemberMapper;

import project.bookservice.repository.member.MybatisMemberRepository;

import project.bookservice.service.CommentService;
import project.bookservice.service.CommentServiceV1;
import project.bookservice.service.MemberService;
import project.bookservice.service.MemberServiceImpl;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MybatisMemberRepository memberRepository(){
        return new MybatisMemberRepository(memberMapper);
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceV1(commentRepository());
    }

    @Bean
    public CommentRepository commentRepository(){
        return new MybatisCommentRepository(commentMapper);
    }




}
