package project.bookservice.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.repository.member.BookmarkCollectionRepository;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkCollectionServiceImpl implements BookmarkCollectionService {

    private final BookmarkCollectionRepository bookmarkCollectionRepository;

    @Override
    public void save(BookmarkCollectionSaveForm form) {
        bookmarkCollectionRepository.save(form);
    }

    @Override
    public int existsCollection(String userId) {
        return bookmarkCollectionRepository.existsCollection(userId);
    }

    @Override
    public List<BookmarkCollection> collectionList(String userId) {
        return bookmarkCollectionRepository.collectionList(userId);
    }
}
