package project.bookservice.repository.book;

import org.springframework.stereotype.Repository;
import project.bookservice.domain.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MybatisBookRepository implements BookRepository{
    private static final Map<Long, Book> store = new ConcurrentHashMap<>(); // 실무에서는 ConcurrentHashMap 사용!
    private static long sequence = 0L; // 실무에서는 어터믹?

    public Book save(Book book){
        book.setId(++sequence);
        store.put(book.getId(), book);
        return book;
    }

    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    public static Book findById(Long id){
        return store.get(id);
    }

    public void clearStore() {
        store.clear();
        sequence = 0L;
    }

}
