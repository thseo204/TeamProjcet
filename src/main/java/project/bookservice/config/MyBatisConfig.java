package project.bookservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import project.bookservice.repository.comment.CommentMapper;
import project.bookservice.repository.comment.CommentRepository;
import project.bookservice.repository.comment.MybatisCommentRepository;
import project.bookservice.repository.member.MemberMapper;


import project.bookservice.repository.member.MyBatisMemberRepository;
import project.bookservice.repository.report.MyBatisReportRepository;
import project.bookservice.repository.report.ReportInfoMapper;
import project.bookservice.repository.report.ReportInfoRepository;
import project.bookservice.repository.starRating.MybatisStarRatingRepository;
import project.bookservice.repository.starRating.StarRatingMapper;
import project.bookservice.repository.starRating.StarRatingRepository;

import project.bookservice.service.*;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;

    private final ReportInfoMapper reportInfoMapper;
	private final StarRatingMapper starRatingMapper;

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MyBatisMemberRepository memberRepository(){
        return new MyBatisMemberRepository(memberMapper);
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceV1(commentRepository());
    }

    @Bean
    public CommentRepository commentRepository(){
        return new MybatisCommentRepository(commentMapper);
    }

    @Bean
    public ReportInfoService reportInfoService(){
        return new ReportInfoServiceImpl(reportInfoRepository());
    }
    @Bean
    public ReportInfoRepository reportInfoRepository(){
        return new MyBatisReportRepository(reportInfoMapper);
    }
	
	   @Bean
    public StarRatingService starRatingService(){
        return new StarRatingServiceImpl(starRatingRepository());
    }

    @Bean
    public StarRatingRepository starRatingRepository(){
        return new MybatisStarRatingRepository(starRatingMapper);
    }


}
