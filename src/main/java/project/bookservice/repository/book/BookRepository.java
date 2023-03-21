package project.bookservice.repository.book;

import org.json.simple.parser.ParseException;
import project.bookservice.domain.book.Book;

import java.util.ArrayList;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    void clearStore();

    Book findByIsbn(String isbn);

    Book findByIsbnInAPI(String isbn) throws ParseException;
//    Optional<Book> findByIsbn(String isbn);

//    List<Book> findAll(String book);
}
