package project.bookservice.web.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.repository.BookRepository;
import project.bookservice.openapi.ApiExamSearchBook;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    ApiExamSearchBook apiExamSearchBook = new ApiExamSearchBook();


    @GetMapping("/book")
    public String searchBook(Model model){
        String responseBody = apiExamSearchBook.ApiExamSearchBook("사피엔스");
        ObjectMapper mapper = new ObjectMapper();
        try{
            Map<String, String> map = mapper.readValue(responseBody, Map.class);
            System.out.println("map = " + map);
            Book book = new Book(map.get("title"), map.get("description"));
            bookRepository.save(book);
//            System.out.println("title = " + map.get("title"));
//            System.out.println("description = " + map.get("description"));
//            System.out.println(map.get(0));
            model.addAttribute("book", book);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "/basic/book";
    }

    @GetMapping("/book2")
    public String searchBook2(Model model) throws ParseException {
        String responseBody = apiExamSearchBook.ApiExamSearchBook("사피엔스");
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(responseBody);
        JSONArray itemArr = (JSONArray) jsonObject.get("items");

        JSONObject object = (JSONObject) itemArr.get(0);
        String jsonString = jsonObject.toJSONString();
//        System.out.println("jsonString = " + jsonString);
        String title = (String)object.get("title");
        String description = (String)object.get("description");
//        System.out.println("title = " + title);
//        System.out.println("description = " + description);

        Book book = new Book(title, description);
        bookRepository.save(book);

        model.addAttribute("book", book);
        return "/basic/book";
    }
}
