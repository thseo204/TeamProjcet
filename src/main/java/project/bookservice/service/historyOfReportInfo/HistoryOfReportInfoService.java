package project.bookservice.service.historyOfReportInfo;

import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;

import java.util.List;

public interface HistoryOfReportInfoService {
    void save(HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm);

    List<HistoryOfReportInfo> findByUserId(String userId);

    Boolean hasHistory(String userId, ReportInfo reportInfo);

    void updateFavorite(Long reportId, HistoryOfReportInfo history);

    void updateCollection(Long reportId, HistoryOfReportInfo history);

}
