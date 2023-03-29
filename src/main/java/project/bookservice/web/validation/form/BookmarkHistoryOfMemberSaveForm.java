package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookmarkHistoryOfMemberSaveForm {
    private Long id;
    @NotBlank
    private String userId;
    @NotBlank
    private String isbn;
    // 디폴트 기본 컬렉션 명 정해주기? or collection 이름이 없을 때는 모든
    @NotBlank
    private String imageUrl;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String collectionName;
}
