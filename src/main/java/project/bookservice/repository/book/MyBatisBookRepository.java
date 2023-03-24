package project.bookservice.repository.book;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.book.Book;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.service.starRating.StarRatingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class MyBatisBookRepository implements BookRepository{
    private static final Map<String, Book> store = new ConcurrentHashMap<>(); // 실무에서는 ConcurrentHashMap 사용!
    private static long sequence = 0L; // 실무에서는 어터믹?

    private StarRatingService starRatingService;

    public Book save(Book book){
        book.setId(++sequence);
        store.put(book.getIsbn(), book);
        return book;
    }

    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    public Book findByIsbn(String isbn){
        return store.get(isbn);
    }

    @Override
    public Book findByIsbnInAPI(String isbn) throws ParseException {
        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookArrayList = apiParser.jsonAndXmlParserToArr(isbn);
        return bookArrayList.get(0);
    }

    public void clearStore() {
        store.clear();
        sequence = 0L;
    }

}
