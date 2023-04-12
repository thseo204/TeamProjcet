package project.bookservice.domain.member;

import lombok.Data;

@Data
public class BookmarkCollection {
    private Long id;
    private String user_id;
    private String date;
    private String collectionName;
}
