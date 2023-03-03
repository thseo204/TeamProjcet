package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project.bookservice.domain.book.Book;
import project.bookservice.domain.repository.BookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;

import java.util.ArrayList;


@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;


    @GetMapping("/main")
    public String searchBook(Model model) throws ParseException {
        String bookTitle = "사피엔스";

        APIParser apiParser = new ApiSearchBook();
        ArrayList<Book> bookList= apiParser.jsonAndXmlParserToArr(bookTitle);

//        bookRepository.save(book);

        model.addAttribute("bookList", bookList);
        return "basic/main";
    }

    @GetMapping("/book")
    public String BestSeller(Model model) throws ParseException {
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList();
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);

        model.addAttribute("bookList", bookList);

        return "basic/book2";
    }

}
