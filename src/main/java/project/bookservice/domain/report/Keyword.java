package project.bookservice.domain.report;

import lombok.Data;

@Data
public class Keyword {
    private Long id;
    private String keyword;
    private String isbn;
    private Long reportId;
}
