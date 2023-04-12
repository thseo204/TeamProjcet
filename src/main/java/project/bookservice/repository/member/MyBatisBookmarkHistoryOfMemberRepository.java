package project.bookservice.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.web.validation.form.BookmarkHistoryOfMemberSaveForm;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisBookmarkHistoryOfMemberRepository implements BookmarkHistoryOfMemberRepository {

    private final BookmarkHistoryOfMemberMapper bookmarkHistoryOfMemberMapper;

    @Override
    public void save(BookmarkHistoryOfMemberSaveForm form) {
        bookmarkHistoryOfMemberMapper.save(form);
    }

    @Override
    public List<BookmarkHistoryOfMember> findAll() {
        return bookmarkHistoryOfMemberMapper.findAll();
    }

    @Override
    public int existsByHistory(String userId) {
        return bookmarkHistoryOfMemberMapper.existsByHistory(userId);
    }

    @Override
    public int existsByHistory(Map<String, String> selectToUserIdCollectionName) {
        return bookmarkHistoryOfMemberMapper.existsByHistoryOfCollection(selectToUserIdCollectionName);
    }

    @Override
    public int existsByIsbn(Map<String, String> selectToUserIdCollectionNameIsbn) {
        return bookmarkHistoryOfMemberMapper.existsByIsbn(selectToUserIdCollectionNameIsbn);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByUserId(String userId) {
        return bookmarkHistoryOfMemberMapper.findByUserId(userId);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByUserIdDistinctIsbn(String userId) {
        return bookmarkHistoryOfMemberMapper.findByUserIdDistinctIsbn(userId);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByCollectionName(String collectionName) {
        return bookmarkHistoryOfMemberMapper.findByCollectionName(collectionName);
    }

    @Override
    public int existsCollectionName(String userId) {
        return bookmarkHistoryOfMemberMapper.existsCollectionName(userId);
    }

    @Override
    public void updateCollectionName(Map<String, String> updateCollectionName) {
        bookmarkHistoryOfMemberMapper.updateCollectionName(updateCollectionName);
    }
}
