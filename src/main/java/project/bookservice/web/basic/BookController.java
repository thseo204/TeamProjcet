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
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.service.CommentService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final MyBatisBookRepository bookRepository;

	 private final CommentService commentService;

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
		 List<Comment> comments = commentService.findComments(isbn);

        model.addAttribute("comments", comments);
        model.addAttribute("book",book);
        return "detail/bookinfo";
    }

    @GetMapping("/book/{isbn}/report")
    public String bookInfoReport(@PathVariable String isbn, Model model){
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("book",book);

        return "detail/bookInfoReport";
    }
    @PostMapping("/book/{isbn}")
    public String insertComment(@ModelAttribute Comment comment , RedirectAttributes redirectAttributes){
        Comment savedComment = commentService.save(comment);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/book/{isbn}";
    }

  
}