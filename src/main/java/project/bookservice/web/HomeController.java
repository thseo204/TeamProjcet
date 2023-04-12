package project.bookservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.domain.report.Keyword;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;

import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoService;
import project.bookservice.service.member.ReportInfoHistoryOfMemberService;
import project.bookservice.service.report.KeywordService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StarRatingService starRatingService;
    private final HistoryOfReportInfoService historyOfReportInfoService;
    private final ReportInfoHistoryOfMemberService reportInfoHistoryOfMemberService;
    private final ReportInfoService reportInfoService;
    private final KeywordService keywordService;


    @GetMapping("/main/{listViewStat}")
    public String homeLoginV3ArgumentResolver(@PathVariable boolean listViewStat,
                                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                                      required = false) Member loginMember, Model model) throws ParseException {
        log.info("loginMember {}", loginMember);
        APIParser apiParser = new ApiSearchBookList(starRatingService);

        ArrayList<Book> bookList = null;

        if (listViewStat == true) {
            // BlogBestList 블로거 베스트 셀러 버튼 눌리면 이 리스트 노출
            bookList = apiParser.jsonAndXmlParserToArr("BlogBest");
            model.addAttribute("listViewStat", true);
        } else {
            // ItemNewSpecialList 주목할 만한 신간 리스트 버튼 눌리면 이 리스트 노출
            bookList = apiParser.jsonAndXmlParserToArr("ItemNewSpecial");
            model.addAttribute("listViewStat", false);
        }

        // BestsellerList 하단에 박스형식으로 10개 노출
        ArrayList<Book> bestsellerList = apiParser.jsonAndXmlParserToArr("Bestseller");
        // ItemNewAllList 신간 전체 리스트
        ArrayList<Book> ItemNewAllList = apiParser.jsonAndXmlParserToArr("ItemNewAll");


        // // 오더바이 좋아요순으로 리밋 4 받아와서 메인 feedList에 뿌리기!!
        List<ReportInfo> reportInfoList = reportInfoService.orderByFavoriteNum();
        List<Keyword> keywordList = keywordService.findAll();
        model.addAttribute("keywordList", keywordList);
        if (loginMember != null) {
            List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());
            model.addAttribute("historyList", historyList);
        }
        model.addAttribute("reportInfoList", reportInfoList);
        model.addAttribute("bestsellerList", bestsellerList);
        model.addAttribute("ItemNewAllList", ItemNewAllList);
        model.addAttribute("bookList", bookList);

        return "basic/main";
    }

    @GetMapping("/")
    public String myBookmarkForm(RedirectAttributes redirectAttributes) {

        boolean listViewStat = true;
        redirectAttributes.addAttribute("listViewStat", listViewStat);

        return "redirect:/main/{listViewStat}";
    }

    @GetMapping("/mainFeedListForm/{id}/favorite/{favorite}")
    public String favoriteClickInFeed(
            @PathVariable Long id,
            @PathVariable boolean favorite,
            Model model,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) throws ParseException {

        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}", reportInfo);

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(reportInfo.getIsbn()); //책정보
        Book book = booklist.get(0);

        log.info("book={}", book);

        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        Optional<HistoryOfReportInfo> historyOptional = historyList.stream()
                .filter(b -> b.getReportId() == id)
                .findAny();

        HistoryOfReportInfo history = historyOptional.get();

        log.info("hasHistory={}", history);

        if (favorite == false) { // 좋아요 안눌린 상태였을때
            reportInfoService.increaseOfFavoriteNum(reportInfo); // 좋아요 수 1 증가 db 반영
        } else { // 좋아요 눌린 상태였을 때
            reportInfoService.decreaseOfFavoriteNum(reportInfo); // 좋아요 수 1 감소 db 반영
        }
        historyOfReportInfoService.updateFavorite(id, history); // 좋아요 눌린 상태 db 반영

        return "redirect:/";
    }

    /**
     * 컬렉션에 저장 클릭했을 때
     */
    @GetMapping("/mainFeedListForm/{id}/collection/{collection}")
    public String collectionClickInFeed(
            @PathVariable Long id,
            @PathVariable boolean collection,
            Model model,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) throws ParseException {


        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}", reportInfo);

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(reportInfo.getIsbn()); //책정보
        Book book = booklist.get(0);

        log.info("book={}", book);

        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        Optional<HistoryOfReportInfo> historyOptional = historyList.stream()
                .filter(b -> b.getReportId() == id)
                .findAny();

        HistoryOfReportInfo history = historyOptional.get();

        log.info("hasHistory={}", history);
        log.info("reportInfo={}", reportInfo);
        if (collection == false) { // 컬렉션 안눌린 상태였을때
            reportInfoService.increaseOfCollectionNum(reportInfo); // 컬렉션 저장 수 1 증가 db 반영
            ReportInfoHistoryOfMemberSaveForm form = new ReportInfoHistoryOfMemberSaveForm();
            form.setUserId(loginMember.getUserId());
            form.setReportId(reportInfo.getId());
            form.setWriterId(reportInfo.getWriterId());
            form.setIsbn(reportInfo.getIsbn());
            form.setTitle(reportInfo.getTitle());
            form.setUploadFileName(reportInfo.getUploadFileName());
            form.setStoreFileName(reportInfo.getStoreFileName());
            form.setContent(reportInfo.getContent());
            form.setHashTag(reportInfo.getHashTag());
            reportInfoHistoryOfMemberService.save(form);
        } else { // 좋아요 눌린 상태였을 때
            reportInfoService.decreaseOfCollectionNum(reportInfo); // 컬렉션 저장 수 1 감소 db 반영
            List<ReportInfoHistoryOfMember> reportInfoHistoryOfMembers = reportInfoHistoryOfMemberService.findByUserId(loginMember.getUserId());
            Optional<ReportInfoHistoryOfMember> reportInfoHistoryOfMemberOptional = reportInfoHistoryOfMembers.stream().filter(r -> r.getReportId() == id).findAny();
            ReportInfoHistoryOfMember reportInfoHistoryOfMember = reportInfoHistoryOfMemberOptional.get();
            reportInfoHistoryOfMemberService.delete(reportInfoHistoryOfMember);
        }

        historyOfReportInfoService.updateCollection(id, history); // 좋아요 눌린 상태 db 반영

        return "redirect:/";
    }
}
