package project.bookservice.repository.book;

import project.bookservice.domain.book.Book;

import java.util.ArrayList;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    void clearStore();

    Book findByIsbn(String isbn);

}
