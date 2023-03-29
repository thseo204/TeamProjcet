package project.bookservice.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisBookmarkCollectionRepository implements BookmarkCollectionRepository {

    private final BookmarkCollectionMapper bookmarkCollectionMapper;

    @Override
    public void save(BookmarkCollectionSaveForm form) {
        bookmarkCollectionMapper.save(form);
    }

    @Override
    public int existsCollection(String userId) {
        return bookmarkCollectionMapper.existsCollection(userId);
    }

    @Override
    public List<BookmarkCollection> collectionList(String userId) {
        return bookmarkCollectionMapper.collectionList(userId);
    }
}
