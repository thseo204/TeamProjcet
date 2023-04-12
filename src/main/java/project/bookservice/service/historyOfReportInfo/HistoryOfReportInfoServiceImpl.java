package project.bookservice.service.historyOfReportInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.repository.historyOfReportInfo.HistoryOfReportInfoRepository;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryOfReportInfoServiceImpl implements HistoryOfReportInfoService {

    private final HistoryOfReportInfoRepository historyOfReportInfoRepository;

    @Override
    public void save(HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm) {
        historyOfReportInfoRepository.save(historyOfReportInfoSaveForm);
    }

    // 로그인한 유저가 좋아요, 저장 누른 게시글에 대한 정보 조회해와서 누른 글에는 색칠된 아이콘으로 표시
    @Override
    public List<HistoryOfReportInfo> findByUserId(String userId) {
        return historyOfReportInfoRepository.findByUserId(userId);
    }

    @Override
    public Boolean hasHistory(String userId, ReportInfo reportInfo) {
        return historyOfReportInfoRepository.hasHistory(userId, reportInfo);
    }

    @Override
    public void updateFavorite(Long reportId, HistoryOfReportInfo history) {
        historyOfReportInfoRepository.updateFavorite(reportId, history);
    }

    @Override
    public void updateCollection(Long reportId, HistoryOfReportInfo history) {
        historyOfReportInfoRepository.updateCollection(reportId, history);
    }
}
