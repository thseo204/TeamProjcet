package project.bookservice.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.book.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
//@Transactional
//@RequiredArgsConstructor
//public class JpaBookRepository implements BookRepository{
public class JpaBookRepository {
    private static final Map<String, Book> store = new ConcurrentHashMap<>(); // 실무에서는 ConcurrentHashMap 사용!
    private static long sequence = 0L; // 실무에서는 어터믹?

//    private final EntityManager em;
//    private final SpringDataJpaItemRepository repository;


    public JpaBookRepository() {
    }

    public Book save(Book book){
        book.setId(++sequence);
        store.put(book.getIsbn(), book);
        return book;
    }

//    @Override
//    public Optional<Book> findById(String isbn) {
//        Book book = em.find(Book.class, isbn);
//        return Optional.ofNullable(book);
//    }

//    @Override
    public List<Book> findAll(String book) {
        String jpql = "select i from Item i";



        return null;
    }

}
