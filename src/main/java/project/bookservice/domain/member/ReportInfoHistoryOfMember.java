package project.bookservice.domain.member;

import lombok.Data;

@Data
public class ReportInfoHistoryOfMember {
    private Long id;
    private String userId;
    private Long reportId;
    private String writerId;
    private String isbn;
    private String title;
    private String uploadFileName;
    private String storeFileName;
    private String content;
    private String hashTag;
}
