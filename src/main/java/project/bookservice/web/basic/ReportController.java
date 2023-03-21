package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.login.LoginForm;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.domain.report.UploadFile;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.repository.report.FileStore;
import project.bookservice.repository.report.MyBatisReportRepository;
import project.bookservice.repository.report.ReportInfoRepository;
import project.bookservice.service.ReportInfoService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
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
    private final MyBatisBookRepository bookRepository;
    private final ReportInfoService reportInfoService;
    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/bookReportForm/{id}")
    public String bookReport(
            @PathVariable Long id,
            String isbn, Model model) throws ParseException {
        ReportInfo reportInfo = reportInfoService.findById(id);
        log.info("reportInfo={}",reportInfo.toString());
        Book book = bookRepository.findByIsbnInAPI(reportInfo.getIsbn());
        log.info("book={}",book.toString());
        model.addAttribute("book", book);
        model.addAttribute("reportInfo", reportInfo);
        return "detail/bookReport";
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
            APIParser apiParser = new ApiSearchBook();
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(title);

            model.addAttribute("bookList", bookList);

            return "detail/searchBookInReport"; // 검색페이지에 검색된 리스트 보여주기
        }
    }

    @GetMapping("/writeReportForm")
    public String writeReportAddTitle(@ModelAttribute("reportInfo") ReportInfo reportInfo, @ModelAttribute Book book,@SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) {
            model.addAttribute("loginMember", loginMember);
//        model.addAttribute("reportInfo", reportInfo);
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

//        log.info("book.title={}", book.getTitle());

        model.addAttribute("book", book);
        model.addAttribute("reportInfo", form);
//        redirectAttributes.addAttribute("bookTitle", book.getTitle());

        return "detail/writeReport";
    }

    @PostMapping("writeReportForm/{isbn}")
    public String saveReport(
            @PathVariable String isbn,
            @ModelAttribute("reportInfo") ReportForm form,
            @ModelAttribute("book") Book book,
            RedirectAttributes redirectAttributes,
            HttpSession session) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());


//        log.info("sessionUserId={}", session.getAttribute("userId"));
        log.info("SessionConst.LOGIN_MEMBER={}", session.getAttribute(SessionConst.LOGIN_MEMBER));
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("member.getUserId()={}", member.getUserId());
//        log.info("form={}", form.toString());

        //데이터베이스에 저장
        ReportSaveForm reportInfo = new ReportSaveForm();
        reportInfo.setIsbn(isbn);
        reportInfo.setWriterId(member.getUserId());

        reportInfo.setUploadFileName(attachFile.getUploadFileName());
        reportInfo.setStoreFileName(attachFile.getStoreFileName());
        log.info("form.getUploadFileName={}", attachFile.getUploadFileName());
        log.info("form.getUploadFileName={}", attachFile.getStoreFileName());

        reportInfo.setContent(form.getContent());
        reportInfo.setDisclosure(form.getDisclosure());
        reportInfo.setHashTag(form.getHashTag());

        log.info("reportForm ={}", reportInfo);
        reportInfoService.save(reportInfo);
        redirectAttributes.addAttribute("id", reportInfo.getId());

        return "redirect:/bookReportForm/{id}";

//        return "detail/bookReport";
    }

////    @GetMapping("/bookReportForm/{id}")
//    public String bookReportForm(@PathVariable Long id,
//                                 Model model) {
//
//        ReportInfo reportInfo = reportInfoService.findById(id);
//        Book book = bookRepository.findByIsbn(reportInfo.getIsbn());
//
//
//        log.info("book={}", book);
//        log.info("reportInfo.isbn={}", reportInfo.getIsbn());
//
//        model.addAttribute("reportInfo", reportInfo);
//        model.addAttribute("book", book);
//
//        return "detail/bookReport";
//
////        return new UrlResource("file:" + fileStore.getFullPath(fileName));
//    }

    @ResponseBody
    @GetMapping("/image/{fileName}")
    public Resource showImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    @GetMapping("/feedListForm")
    public String feedList(){
        List<ReportInfo> reportInfoList = reportInfoService.findAll();
        // 리포트 리스트 받아와서 feedListForm에 뿌리기!!
        return "detail/feedList";
    }


}
