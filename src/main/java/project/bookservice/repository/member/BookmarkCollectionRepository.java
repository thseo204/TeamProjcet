package project.bookservice.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;

import java.util.List;


public interface BookmarkCollectionRepository {
    void save(BookmarkCollectionSaveForm form);
    int existsCollection(String userId);
    List<BookmarkCollection> collectionList(String userId);
}
