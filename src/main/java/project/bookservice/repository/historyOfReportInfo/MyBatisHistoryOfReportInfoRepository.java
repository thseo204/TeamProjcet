package project.bookservice.repository.historyOfReportInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisHistoryOfReportInfoRepository implements HistoryOfReportInfoRepository {

    private final HistoryOfReportInfoMapper historyOfReportInfoMapper;

    @Override
    public void save(HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm) {
        historyOfReportInfoMapper.save(historyOfReportInfoSaveForm);
    }

    @Override
    public List<HistoryOfReportInfo> findByUserId(String userId) {
        return historyOfReportInfoMapper.findByUserId(userId);
    }

    @Override
    public Boolean hasHistory(String userId, ReportInfo reportInfo) {
        return historyOfReportInfoMapper.hasHistory(userId, reportInfo);
    }

    @Override
    public void updateFavorite(Long reportId, HistoryOfReportInfo history) {
        historyOfReportInfoMapper.updateFavorite(reportId, history);
    }

    @Override
    public void updateCollection(Long reportId, HistoryOfReportInfo history) {
        historyOfReportInfoMapper.updateCollection(reportId, history);
    }
}
