package project.bookservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.repository.member.MemberRepository;

import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.session.SessionManager;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
	 private final StarRatingService starRatingService;

    private final HistoryOfReportInfoService historyOfReportInfoService;
    private final ReportInfoService reportInfoService;


    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) throws ParseException {
        log.info("loginMember {}", loginMember);
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);

        // // 오더바이 좋아요순으로 리밋 4 받아와서 메인 feedList에 뿌리기!!
        List<ReportInfo> reportInfoList = reportInfoService.orderByFavoriteNum();
        if(loginMember != null){
            List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());
            model.addAttribute("historyList", historyList);
        }
        model.addAttribute("reportInfoList", reportInfoList);
        model.addAttribute("bookList", bookList);
//        model.addAttribute("loginMember", loginMember);
        return "basic/main";
    }
}