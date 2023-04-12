package project.bookservice.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.repository.member.BookmarkHistoryOfMemberRepository;
import project.bookservice.web.validation.form.BookmarkHistoryOfMemberSaveForm;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkHistoryOfMemberServiceImpl implements BookmarkHistoryOfMemberService {

    private final BookmarkHistoryOfMemberRepository bookmarkHistoryOfMemberRepository;

    @Override
    public void save(BookmarkHistoryOfMemberSaveForm form) {
        bookmarkHistoryOfMemberRepository.save(form);
    }

    @Override
    public List<BookmarkHistoryOfMember> findAll() {
        return bookmarkHistoryOfMemberRepository.findAll();
    }

    @Override
    public int existsByHistory(String userId) {
        return bookmarkHistoryOfMemberRepository.existsByHistory(userId);
    }

    @Override
    public int existsByHistory(Map<String, String> selectToUserIdCollectionName) {
        return bookmarkHistoryOfMemberRepository.existsByHistory(selectToUserIdCollectionName);
    }

    @Override
    public int existsByIsbn(Map<String, String> selectToUserIdCollectionNameIsbn) {
        return bookmarkHistoryOfMemberRepository.existsByIsbn(selectToUserIdCollectionNameIsbn);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByUserId(String userId) {
        return bookmarkHistoryOfMemberRepository.findByUserId(userId);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByUserIdDistinctIsbn(String userId) {
        return bookmarkHistoryOfMemberRepository.findByUserIdDistinctIsbn(userId);
    }

    @Override
    public List<BookmarkHistoryOfMember> findByCollectionName(String collectionName) {
        return bookmarkHistoryOfMemberRepository.findByCollectionName(collectionName);
    }

    @Override
    public int existsCollectionName(String userId) {
        return bookmarkHistoryOfMemberRepository.existsCollectionName(userId);
    }

    @Override
    public void updateCollectionName(Map<String, String> updateCollectionName) {
        bookmarkHistoryOfMemberRepository.updateCollectionName(updateCollectionName);
    }
}
