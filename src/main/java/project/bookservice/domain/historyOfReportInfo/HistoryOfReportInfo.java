package project.bookservice.domain.historyOfReportInfo;

import lombok.Data;

@Data
public class HistoryOfReportInfo {
    private Long id;
    private Long reportId;
    private String userId;
    private Boolean favorite;
    private Boolean collection;
}
