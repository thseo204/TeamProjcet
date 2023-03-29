package project.bookservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import project.bookservice.repository.comment.CommentMapper;
import project.bookservice.repository.comment.CommentRepository;
import project.bookservice.repository.comment.MybatisCommentRepository;
import project.bookservice.repository.historyOfReportInfo.HistoryOfReportInfoMapper;
import project.bookservice.repository.historyOfReportInfo.HistoryOfReportInfoRepository;
import project.bookservice.repository.historyOfReportInfo.MyBatisHistoryOfReportInfoRepository;
import project.bookservice.repository.member.*;

import project.bookservice.repository.report.MyBatisReportRepository;
import project.bookservice.repository.report.ReportInfoMapper;
import project.bookservice.repository.report.ReportInfoRepository;
import project.bookservice.service.comment.CommentService;
import project.bookservice.service.comment.CommentServiceImpl;
import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoService;
import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoServiceImpl;
import project.bookservice.service.member.*;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.repository.starRating.MybatisStarRatingRepository;
import project.bookservice.repository.starRating.StarRatingMapper;
import project.bookservice.repository.starRating.StarRatingRepository;
import project.bookservice.service.report.ReportInfoServiceImpl;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.service.starRating.StarRatingServiceImpl;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;

    private final ReportInfoMapper reportInfoMapper;
		private final StarRatingMapper starRatingMapper;
    private final HistoryOfReportInfoMapper historyOfReportInfoMapper;
    private final ReportInfoHistoryOfMemberMapper reportInfoHistoryOfMemberMapper;
    private final BookmarkHistoryOfMemberMapper bookmarkHistoryOfMemberMapper;
    private final BookmarkCollectionMapper bookmarkCollectionMapper;

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
        return new CommentServiceImpl(commentRepository());
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
    public HistoryOfReportInfoService historyOfReportInfoService(){
        return new HistoryOfReportInfoServiceImpl(historyOfReportInfoRepository());
    }
    @Bean
    public HistoryOfReportInfoRepository historyOfReportInfoRepository(){
        return new MyBatisHistoryOfReportInfoRepository(historyOfReportInfoMapper);
    }
	   @Bean
    public StarRatingService starRatingService(){
        return new StarRatingServiceImpl(starRatingRepository());
    }

    @Bean
    public StarRatingRepository starRatingRepository() {
        return new MybatisStarRatingRepository(starRatingMapper);
    }

    @Bean
    public ReportInfoHistoryOfMemberRepository reportInfoHistoryOfMemberRepository(){
        return new MyBatisReportInfoHistoryOfMemberRepository(reportInfoHistoryOfMemberMapper);
    }
    @Bean
    public ReportInfoHistoryOfMemberService reportInfoHistoryOfMemberService(){
        return new ReportInfoHistoryOfMemberServiceImpl(reportInfoHistoryOfMemberRepository());
    }
    @Bean
    public BookmarkHistoryOfMemberRepository bookmarkHistoryOfMemberRepository(){
        return new MyBatisBookmarkHistoryOfMemberRepository(bookmarkHistoryOfMemberMapper);
    }
    @Bean
    public BookmarkHistoryOfMemberService bookmarkHistoryOfMemberService(){
        return new BookmarkHistoryOfMemberServiceImpl(bookmarkHistoryOfMemberRepository());
    }
    @Bean
    public BookmarkCollectionRepository bookmarkCollectionRepository(){
        return new MyBatisBookmarkCollectionRepository(bookmarkCollectionMapper);
    }
    @Bean
    public BookmarkCollectionService bookmarkCollectionService(){
        return new BookmarkCollectionServiceImpl(bookmarkCollectionRepository());
    }
}
