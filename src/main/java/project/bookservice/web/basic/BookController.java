package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.repository.BookRepository;
import project.bookservice.openapi.ApiExamSearchBook;
import project.bookservice.openapi.GetAPIData;

import java.util.ArrayList;


@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;


    @GetMapping("/main")
    public String searchBook(Model model) throws ParseException {
        String bookTitle = "사피엔스";
//        JSONObject object = searchByBookTitle(bookTitle);
        ApiExamSearchBook apiExamSearchBook = new ApiExamSearchBook(bookTitle);
        JSONObject object = apiExamSearchBook.getObject();


        String title = (String)object.get("title");
        String description = (String)object.get("description");
        String image = (String)object.get("image");
//        System.out.println("title = " + title);
//        System.out.println("description = " + description);
        System.out.println("image = " + image);

        Book book = new Book(title, description, image);
        bookRepository.save(book);

        model.addAttribute("book", book);
        return "basic/main";
    }

    @GetMapping("/book")
    public String Bestsellser(Model model) {
        GetAPIData gad = new GetAPIData();
        gad.getApiData();
        ArrayList<Book> b = gad.book;

        for(int i = 0 ; i<b.size(); i++){
            String title = b.get(i).getTitle();
            String description = b.get(i).getDescription();
            String image = b.get(i).getImage();

            Book book = new Book(title, description, image);
            bookRepository.save(book);

            model.addAttribute("book", book);
        }

        return "basic/book2";
    }

}
