package project.bookservice.domain.member;

import lombok.Data;

@Data
public class BookmarkHistoryOfMember {
    private Long id;
    private String userId;
    private String isbn;
    private String imageUrl;
    private String title;
    private String author;
    private String collectionName;
}
