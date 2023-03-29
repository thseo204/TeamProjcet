package project.bookservice.repository.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.web.validation.form.BookmarkHistoryOfMemberSaveForm;

import java.util.List;
import java.util.Map;

@Mapper// 이 어노테이션을 붙여줘야 MyBatis에서 인식할 수 있음.
public interface BookmarkHistoryOfMemberMapper {
    // 실행할 SQL은 패키지 위치는 이 인터페이스와 동일한 경로로 만들어줘야함.
    void save(BookmarkHistoryOfMemberSaveForm form);

    List<BookmarkHistoryOfMember> findAll();

    int existsByHistory(String userId); // List로 불러오기 전 히스토리가 있는지 체크하기 위한 쿼리
    int existsByHistoryOfCollection(Map<String, String> selectToUserIdCollectionName); // 해당 유저의 컬렉션에 List로 불러오기 전 히스토리가 있는지 체크하기 위한 쿼리
    int existsByIsbn(Map<String, String> selectToUserIdCollectionNameIsbn);
    List<BookmarkHistoryOfMember> findByUserId(String userId);

    List<BookmarkHistoryOfMember> findByUserIdDistinctIsbn(String userId);

    List<BookmarkHistoryOfMember> findByCollectionName(String collectionName);

    int existsCollectionName(String userId);

    void updateCollectionName(Map<String, String> updateCollectionName);
}
