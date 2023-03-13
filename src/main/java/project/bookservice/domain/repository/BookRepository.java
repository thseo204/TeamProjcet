package project.bookservice.domain.repository;

import project.bookservice.domain.book.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(String isdn);
    List<Book> findAll(String book);
}
