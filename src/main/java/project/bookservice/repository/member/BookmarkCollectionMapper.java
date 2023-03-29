package project.bookservice.repository.member;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;

import java.util.List;

@Mapper
public interface BookmarkCollectionMapper {
    void save(BookmarkCollectionSaveForm form);
    int existsCollection(String userId);
    List<BookmarkCollection> collectionList(String userId);
}
