package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.comment.Comment;
import project.bookservice.domain.member.Member;

import project.bookservice.domain.starRating.StarRating;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.repository.starRating.StarRatingRepository;
import project.bookservice.service.CommentService;
import project.bookservice.service.StarRatingService;
import project.bookservice.web.SessionConst;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final MyBatisBookRepository bookRepository;

	 private final CommentService commentService;
     private final StarRatingService starRatingService;

    @GetMapping("/bookSearch")
    public String searchBook(Model model) throws ParseException {
        String bookTitle = "사피엔스";

        APIParser apiParser = new ApiSearchBook();
        ArrayList<Book> bookList= apiParser.jsonAndXmlParserToArr(bookTitle);

//        bookRepository.save(book);

        model.addAttribute("bookList", bookList);
        return "basic/book2";
    }

    @GetMapping("/main")
    public String BestSeller(Model model) throws ParseException {
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList();
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);
        model.addAttribute("bookList", bookList);


        return "basic/main";
    }

    @GetMapping("/book/{isbn}")
    public String bookInfo(@PathVariable String isbn, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model,StarRating starRating) {

        //로그인 세션에 정보가 없을경우 (비로그인 상태)
        if(loginMember == null){
            Book book = bookRepository.findByIsbn(isbn);
            List<Comment> comments = commentService.findComments(isbn);
            Double avgStarRating = starRatingService.findAvgStarRating(isbn);

            model.addAttribute("comments", comments);
            model.addAttribute("book",book);
            model.addAttribute("avgStarRating" , avgStarRating);
            return "detail/bookinfo";
        }
        //로그인 세션에 정보가 있을경우 (로그인 상태)
        starRating.setUserId(loginMember.getUserId()); //starRatingService 에 아이디 값을 넘겨주기 위해 세션 아이디값을 starRating 에 넣어준다

        Book book = bookRepository.findByIsbn(isbn);    //책 정보
        List<Comment> comments = commentService.findComments(isbn); //이 책에 등록된 댓글 정보
        Double avgStarRating = starRatingService.findAvgStarRating(isbn); // 이 책의 별점 평균 정보
        Boolean starId = starRatingService.findByUserId(starRating); // 로그인한 아이디가 별점 부여했는지 유무 체크


        log.info("starId={}", starId); //별점 유무
        log.info("userId={}", starRating.getUserId()); //로그인한 아이디
        log.info("isbn={}", starRating.getIsbn()); // 책 isbn

        model.addAttribute("comments", comments);
        model.addAttribute("book",book);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("avgStarRating" , avgStarRating);
        model.addAttribute("starId",starId);
        return "detail/bookinfo";
    }

    @GetMapping("/book/{isbn}/report")
    public String bookInfoReport(@PathVariable String isbn, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("book",book);
        model.addAttribute("loginMember", loginMember);

        return "detail/bookInfoReport";
    }
    @PostMapping("/book/{isbn}")
    public String insertComment(@ModelAttribute Comment comment){
        Comment savedComment = commentService.save(comment);
        return "redirect:/book/{isbn}";
    }


    @GetMapping("/starRating/{isbn}")
    public String starRating(@PathVariable String isbn, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){
        Book book = bookRepository.findByIsbn(isbn);


        model.addAttribute("book",book);
        model.addAttribute("loginMember", loginMember);

        return "detail/starRating";
    }

    @PostMapping("/starRating/{isbn}")
    public String insertStarRating(@ModelAttribute StarRating starRating){
        StarRating saveStarRating = starRatingService.saveStarRating(starRating);
        return "detail/thanks";
    }


    @GetMapping("/bookReportForm")
    public String bookReport(String isbn, Model model) {

        return "detail/bookReport";
    }


}
