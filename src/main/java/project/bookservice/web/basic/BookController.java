package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project.bookservice.domain.book.Book;
import project.bookservice.repository.JpaBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;

import java.util.ArrayList;


@Controller
@RequiredArgsConstructor
public class BookController {
    private final JpaBookRepository bookRepository;


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

    @GetMapping("/book/{bookId}")
    public String bookInfo(String isbn, Model model) {
//        Book book = JpaBookRepository.findById(isbn);
//        model.addAttribute("book",book);
        return "basic/bookinfo";
    }

    @GetMapping("/bookReportForm")
    public String bookReport(String isbn, Model model) {

        return "basic/bookReport";
    }
}
