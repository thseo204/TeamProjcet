package project.bookservice.service.member;

import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;

import java.util.List;


public interface BookmarkCollectionService {
    void save(BookmarkCollectionSaveForm form);
    int existsCollection(String userId);
    List<BookmarkCollection> collectionList(String userId);
}
