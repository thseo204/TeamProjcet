package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.domain.report.UploadFile;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.repository.report.FileStore;
import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoService;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController implements Serializable {

    private final HistoryOfReportInfoService historyOfReportInfoService;
    private final MyBatisBookRepository bookRepository;
    private final ReportInfoService reportInfoService;
    private final MemberService memberService;
    private final FileStore fileStore;
    private final StarRatingService starRatingService;
    @Value("${file.dir2}")
    private String fileDir;

    @GetMapping("/bookReportForm/{id}")
    public String bookReport(
            @PathVariable Long id,
            Model model, RedirectAttributes redirectAttributes,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
                    required = false) Member loginMember
//            @Valid @ModelAttribute("member") SignUpForm signUpForm,
//            String isbn,
//            BindingResult bindingResult
    ) throws ParseException {

        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}", reportInfo.toString());
        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(reportInfo.getIsbn()); //책정보
        Book book = booklist.get(0);
        log.info("book={}", book);

        model.addAttribute("book", book);
        model.addAttribute("reportInfo", reportInfo);

        // 로그인상태 확인
        if(loginMember == null){
            HistoryOfReportInfo history = new HistoryOfReportInfo();
            history.setCollection(false);
            history.setFavorite(false);
            model.addAttribute("history",history);

            return "detail/bookReport";
        }

        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        Optional<HistoryOfReportInfo> historyOptional = historyList.stream()
                .filter(b -> b.getReportId() == id)
                .findAny();

        HistoryOfReportInfo history = null;
        if(historyOptional.isPresent()){
            history = historyOptional.get();

        } else if(historyOptional.isEmpty()){
            history = new HistoryOfReportInfo();
            history.setCollection(false);
            history.setFavorite(false);
        }
        model.addAttribute("history",history);

        return "detail/bookReport";
    }


    /**
     * 좋아요 버튼 클릭시 실행 로직
     * @param id -> 레포트의 아이디
     * @param favorite -> 파라매터로 넘어온 좋아요 값의 상태 true/false
     * @param loginMember -> 세션에 로그인 정보 확인
     * @return -> 레포트인포 기본 폼으로 리다이렉트
     */
    @GetMapping("/bookReportForm/{id}/{favorite}")
    public String favoriteClick(
            @PathVariable Long id,
            @PathVariable boolean favorite,
            Model model,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) throws ParseException {


        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}", reportInfo);

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(reportInfo.getIsbn()); //책정보
        Book book = booklist.get(0);
        log.info("book={}", book);

        Boolean hasHistory = historyOfReportInfoService.hasHistory(loginMember.getUserId(), reportInfo);
        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        Optional<HistoryOfReportInfo> historyOptional = historyList.stream()
                .filter(b -> b.getReportId() == id)
                .findAny();

        if(hasHistory == true && historyOptional.isPresent()){//  해당 히스토리가 있으면 업데이트 쿼리 실행
            HistoryOfReportInfo history = historyOptional.get();

            log.info("hasHistory={}", history);

            if(favorite == false){ // 좋아요 안눌린 상태였을때
                reportInfoService.increaseOfFavoriteNum(reportInfo); // 좋아요 수 1 증가 db 반영
            } else { // 좋아요 눌린 상태였을 때
                reportInfoService.decreaseOfFavoriteNum(reportInfo); // 좋아요 수 1 감소 db 반영
            }
            historyOfReportInfoService.updateFavorite(id, history); // 좋아요 눌린 상태 db 반영
            //false -> true 로 true -> false
            model.addAttribute("history", history);
            model.addAttribute("book", book);
            model.addAttribute("reportInfo", reportInfo);

            return "redirect:/bookReportForm/{id}";

        } else{ //해당 히스토리가 없으면 insert 쿼리 실행
            // 새로운 히스토리 db에 생성
            HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm = new HistoryOfReportInfoSaveForm();
            historyOfReportInfoSaveForm.setUserId(loginMember.getUserId());
            historyOfReportInfoSaveForm.setReportId(reportInfo.getId());
            historyOfReportInfoService.save(historyOfReportInfoSaveForm);

            // 히스토리가 없다는 것은 좋아요를 누르지 않은 상태이기때문에 감소 불가. 증가만 가능.
            reportInfoService.increaseOfFavoriteNum(reportInfo);
            // 방금 저장한 히스토리 꺼내와서 모델 속성에 반영
            List<HistoryOfReportInfo> newHistoryList = historyOfReportInfoService.findByUserId(loginMember.getUserId());
            HistoryOfReportInfo history = newHistoryList.get(0);
            historyOfReportInfoSaveForm.setUserId(loginMember.getUserId());
            history.setReportId(reportInfo.getId());
            history.setFavorite(false);
            history.setCollection(false);

            historyOfReportInfoService.updateFavorite(id, history); // 좋아요 눌린 상태 db 반영 false 를 true로

            log.info("hasNotHistory={}", history);
            model.addAttribute("history", history);
            model.addAttribute("book", book);
            model.addAttribute("reportInfo", reportInfo);

            return "redirect:/bookReportForm/{id}";
        }
    }


    /**
     * 서브 검색창 내에서 검색 값이 있을 경우 리스트 출력
     */
    @GetMapping("/searchBookInReportForm")
    public String searchBookList(String title, Book book, Model model) throws ParseException {

        log.info("title={}", title);
        if (title == null) {
            // 타이틀이 널값일 경우 에러문구 띄워주기
            return "detail/searchBookInReport";

        } else {
            //"title 로 검색된 검색 명으로 api 에서 리스트 출력 하여 bookList 에 담기
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(title);

            model.addAttribute("bookList", bookList);

            return "detail/searchBookInReport"; // 검색페이지에 검색된 리스트 보여주기
        }
    }

    @GetMapping("/writeReportForm")
    public String writeReportAddTitle(@ModelAttribute("reportInfo") ReportInfo reportInfo,
                                      @ModelAttribute Book book){

        return "detail/writeReport";
    }

    /**
     * 서브 검색창에서 도서 검색 후 선택한 도서 정보 독후기록 페이지로 넘기
     */
    @GetMapping("/writeReportForm/{isbn}")
    public String writeReportAddTitle(
            @PathVariable String isbn,
            Model model,
            @ModelAttribute("book") Book book,
            @ModelAttribute("reportInfo") ReportForm form) {

        log.info("isbn={}", isbn);

        book = bookRepository.findByIsbn(isbn);
        log.info("book={}", book.toString());

        model.addAttribute("book", book);
        model.addAttribute("reportInfo", form);

        return "detail/writeReport";
    }

    @PostMapping("writeReportForm/{isbn}")
    public String saveReport(
            @PathVariable String isbn,
            @ModelAttribute("reportInfo") ReportForm form,
            @ModelAttribute("book") Book book,
            RedirectAttributes redirectAttributes,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        log.info("loginMember.getUserId()={}", loginMember.getUserId());

        //데이터베이스에 report 저장
        ReportSaveForm reportInfo = new ReportSaveForm();
        reportInfo.setIsbn(isbn);
        reportInfo.setWriterId(loginMember.getUserId());

        reportInfo.setUploadFileName(attachFile.getUploadFileName());
        reportInfo.setStoreFileName(attachFile.getStoreFileName());
        log.info("form.getUploadFileName={}", attachFile.getUploadFileName());
        log.info("form.getUploadFileName={}", attachFile.getStoreFileName());

        reportInfo.setContent(form.getContent());
        reportInfo.setDisclosure(form.getDisclosure());
        reportInfo.setHashTag(form.getHashTag());

        log.info("reportForm ={}", reportInfo);
        reportInfoService.save(reportInfo);

        // 새로운 히스토리 각 member 별 db 생성
        List<Member> memberList = memberService.findAll();
        for (Member member : memberList) {

            HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm = new HistoryOfReportInfoSaveForm();
            historyOfReportInfoSaveForm.setUserId(member.getUserId());
            historyOfReportInfoSaveForm.setReportId(reportInfo.getId());
            historyOfReportInfoService.save(historyOfReportInfoSaveForm);
        }

        redirectAttributes.addAttribute("id", reportInfo.getId());

        return "redirect:/bookReportForm/{id}";

//        return "detail/bookReport";
    }


    @ResponseBody
    @GetMapping("/image/{fileName}")
    public Resource showImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    @GetMapping("/feedListForm")
    public String feedList(Model model,
                           @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
                           Member loginMember){

        // 리포트 리스트 받아와서 feedListForm에 뿌리기!!
        List<ReportInfo> reportInfoList = reportInfoService.findAll();
        model.addAttribute("reportInfoList", reportInfoList);
        if(loginMember == null){

            return "detail/feedList";
        }

        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        model.addAttribute("historyList", historyList);

        return "detail/feedList";
    }

    /**
     * 좋아요 버튼 클릭시 실행 로직
     * @param id -> 레포트의 아이디
     * @param favorite -> 파라매터로 넘어온 좋아요 값의 상태 true/false
     * @param loginMember -> 세션에 로그인 정보 확인
     * @return -> 레포트인포 기본 폼으로 리다이렉트
     */
    @GetMapping("/feedListForm/{id}/{favorite}")
    public String favoriteClickInFeed(
            @PathVariable Long id,
            @PathVariable boolean favorite,
            Model model,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember) throws ParseException {


        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}", reportInfo);

        //Book book = bookRepository.findByIsbnInAPI(reportInfo.getIsbn());
        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(reportInfo.getIsbn()); //책정보
        Book book = booklist.get(0);

        log.info("book={}", book);

        Boolean hasHistory = historyOfReportInfoService.hasHistory(loginMember.getUserId(), reportInfo);
        List<HistoryOfReportInfo> historyList = historyOfReportInfoService.findByUserId(loginMember.getUserId());

        Optional<HistoryOfReportInfo> historyOptional = historyList.stream()
                .filter(b -> b.getReportId() == id)
                .findAny();

            HistoryOfReportInfo history = historyOptional.get();

            log.info("hasHistory={}", history);

            if(favorite == false){ // 좋아요 안눌린 상태였을때
                reportInfoService.increaseOfFavoriteNum(reportInfo); // 좋아요 수 1 증가 db 반영
            } else { // 좋아요 눌린 상태였을 때
                reportInfoService.decreaseOfFavoriteNum(reportInfo); // 좋아요 수 1 감소 db 반영
            }
            historyOfReportInfoService.updateFavorite(id, history); // 좋아요 눌린 상태 db 반영
            //false -> true 로 true -> false
            model.addAttribute("history", history);
            model.addAttribute("book", book);
            model.addAttribute("reportInfo", reportInfo);

            return "redirect:/feedListForm";

    }

    @GetMapping("/deleteReportInfoForm/{id}")
    public String deleteReportInfo(@PathVariable Long id){
        reportInfoService.delete(id);

        return "/basic/deleteReportSuccessForm"; // 해당게시물이 삭제되었습니다. 페이지로 이동.
    }
    @GetMapping("/editReportInfoForm/{reportId}/{isbn}")
    public String editReportInfoForm(@PathVariable Long reportId, @PathVariable String isbn, Model model) throws ParseException {

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(isbn); //책정보
        Book book = booklist.get(0);
        ReportInfo reportInfo = reportInfoService.findById(reportId);

        model.addAttribute("book", book);
        model.addAttribute("reportInfo", reportInfo);
        return "detail/editReport"; // 수정하지않고 취소 눌렀을 경우
    }
    @PostMapping("/editReportInfoForm/{reportId}/{isbn}")
    public String editReportInfo(@PathVariable Long reportId,
                                 @PathVariable String isbn,
                                 @ModelAttribute("reportInfo") ReportForm form,
                                 Model model,
                                 RedirectAttributes redirectAttributes) throws ParseException {

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> booklist = apiParser.jsonAndXmlParserToArr(isbn); //책정보
        Book book = booklist.get(0);
        ReportInfo reportInfo = reportInfoService.findById(reportId);
        reportInfo.setContent(form.getContent());
        reportInfo.setHashTag(form.getHashTag());
        reportInfo.setDisclosure(form.getDisclosure());
        reportInfoService.edit(reportInfo);

        model.addAttribute("book", book);
        redirectAttributes.addAttribute("id", reportId);
        return "redirect:/bookReportForm/{id}"; // 게시글 수정 후 수정완료하였을 경우
    }
}
