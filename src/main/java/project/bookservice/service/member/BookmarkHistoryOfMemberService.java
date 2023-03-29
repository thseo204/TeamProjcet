package project.bookservice.service.member;

import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.web.validation.form.BookmarkHistoryOfMemberSaveForm;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookmarkHistoryOfMemberService {

    void save(BookmarkHistoryOfMemberSaveForm form);

    List<BookmarkHistoryOfMember> findAll();

    int existsByHistory(String userId);
    int existsByHistory(Map<String, String> selectToUserIdCollectionName);
    int existsByIsbn(Map<String, String> selectToUserIdCollectionNameIsbn);
    List<BookmarkHistoryOfMember> findByUserId(String userId);
    List<BookmarkHistoryOfMember> findByUserIdDistinctIsbn(String userId);

    List<BookmarkHistoryOfMember> findByCollectionName(String collectionName);

    int existsCollectionName(String userId);

    void updateCollectionName(Map<String, String> updateCollectionName);
}
