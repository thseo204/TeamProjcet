package project.bookservice.repository.historyOfReportInfo;

import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;

import java.util.List;

public interface HistoryOfReportInfoRepository {
    void save(HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm);

    List<HistoryOfReportInfo> findByUserId(String userId);

    // 로그인한 유저 아이디로 조회하여 true 인 좋아요, 저장 버튼 색칠
    Boolean hasHistory(String userId, ReportInfo reportInfo);

    void updateFavorite(Long reportId, HistoryOfReportInfo history);

    void updateCollection(Long reportId, HistoryOfReportInfo history);

}
