package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.bookservice.domain.book.Book;
import project.bookservice.domain.comment.Comment;
import project.bookservice.domain.member.Member;

import project.bookservice.domain.starRating.StarRating;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;

import project.bookservice.service.comment.CommentService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.SessionConst;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final MyBatisBookRepository bookRepository;

	 private final CommentService commentService;
     private final StarRatingService starRatingService;



    @GetMapping("/searchBookList")
    public String searchBook(String title,Book book,Model model) throws ParseException {


        if (title == null) {
            return "basic/searchBookList";

        }else {
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(title);
            model.addAttribute("bookList", bookList);
            return "basic/searchBookList";
        }
    }

    @GetMapping("/main")
    public String BestSeller(Model model) throws ParseException {
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);
        model.addAttribute("bookList", bookList);


        return "basic/main";
    }


    @GetMapping("/book/{isbn}")
    public String bookInfo(@PathVariable String isbn, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model,StarRating starRating) throws ParseException {

        //로그인 세션에 정보가 없을경우 (비로그인 상태)
        if(loginMember == null){
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
            Book book = bookList.get(0);
            List<Comment> comments = commentService.findComments(isbn);
            log.info("avgStarRating={}",bookList.get(0).getAvgStarRating());
            model.addAttribute("comments", comments);
            model.addAttribute("book",book);

            return "detail/bookinfo";
        }
        //로그인 세션에 정보가 있을경우 (로그인 상태)
        starRating.setUserId(loginMember.getUserId()); //starRatingService 에 아이디 값을 넘겨주기 위해 세션 아이디값을 starRating 에 넣어준다

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn); //책정보
        Book book = bookList.get(0);
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
    public String bookInfoReport(@PathVariable String isbn, Model model,StarRating starRating, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember) throws ParseException {

        //로그인 세션에 정보가 없을경우 (비로그인 상태)
        if(loginMember == null){
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
            Book book = bookList.get(0);
            model.addAttribute("book",book);

            return "detail/bookinfo";
        }

        //로그인 세션에 정보가 있을경우 (로그인 상태)
        starRating.setUserId(loginMember.getUserId());
        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
        Book book = bookList.get(0);
        Boolean starId = starRatingService.findByUserId(starRating); // 로그인한 아이디가 별점 부여했는지 유무 체크
        model.addAttribute("book",book);
        model.addAttribute("starId",starId);

        return "detail/bookInfoReport";
    }



    @GetMapping("/starRating/{isbn}")
    public String starRating(@PathVariable String isbn, Model model){
        Book book = bookRepository.findByIsbn(isbn);

        model.addAttribute("book",book);

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
