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
import project.bookservice.repository.book.MybatisBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.service.CommentService;

import java.util.ArrayList;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final MybatisBookRepository bookRepository;

	 private final CommentService commentService;

    @GetMapping("/star")
    public String starpage(){
        return "basic/star";
    }
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
    public String bookInfo(@PathVariable String isbn, Model model) {

        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("book",book);
        return "detail/bookinfo";
    }


    @PostMapping("/book/{isbn}")
    public String insertComment(@ModelAttribute Comment comment , RedirectAttributes redirectAttributes){
        Comment savedComment = commentService.save(comment);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/book/{isbn}";
    }

    @GetMapping("/bookReportForm")
    public String bookReport(String isbn, Model model) {

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
    public String writeReportAddTitle(Model model, Book book) {

        return "detail/writeReport";
    }

    /**
     * 서브 검색창에서 도서 검색 후 선택한 도서 정보 독후기록 페이지로 넘기
     */
    @GetMapping("/writeReportForm/{isbn}")
    public String writeReportAddTitle(
            @PathVariable String isbn,
            Model model,
            Book book) {

        log.info("isbn={}", isbn);

        book = bookRepository.findByIsbn(isbn);
        log.info("book={}", book);
        log.info("book.title={}", book.getTitle());

        model.addAttribute("book", book);

        return "detail/writeReport";
    }


}
