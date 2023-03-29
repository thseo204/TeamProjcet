package project.bookservice.repository.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisReportRepository implements ReportInfoRepository {
    // 매퍼에 위임하는 코드

    private final ReportInfoMapper reportInfoMapper;
    @Override
    public ReportSaveForm save(ReportSaveForm reportSaveForm) {
        log.info("reportInfo info={}", reportSaveForm);

        reportInfoMapper.save(reportSaveForm);
        return reportSaveForm;
    }

    @Override
    public void delete(Long id) {
        reportInfoMapper.delete(id);
    }

    @Override
    public void edit(ReportInfo reportInfo) {
        reportInfoMapper.edit(reportInfo);
    }

    @Override
    public ReportInfo findById(Long id) {
        log.info("reportInfo findById={}", id);

        Optional<ReportInfo> optionalReportInfo = reportInfoMapper.findById(id);
        ReportInfo reportInfo = null;
        if(optionalReportInfo.isPresent()){
            reportInfo = optionalReportInfo.get();
        }
        return reportInfo;
    }

    @Override
    public int existsReportInfo(String userId) {
        return reportInfoMapper.existsReportInfo(userId);
    }

    @Override
    public List<ReportInfo> findByUserId(String userId) {
        return reportInfoMapper.findByUserId(userId);
    }

    @Override
    public List<ReportInfo> findAll() {
        return reportInfoMapper.findAll();
    }
    /**
     * 좋아요 수 증감
     */
    @Override
    public void increaseOfFavoriteNum(ReportInfo reportInfo) {
        reportInfoMapper.increaseOfFavoriteNum(reportInfo);
    }

    @Override
    public void decreaseOfFavoriteNum(ReportInfo reportInfo) {
        reportInfoMapper.decreaseOfFavoriteNum(reportInfo);
    }
    /**
     * 컬렉션 저장 수 증감
     */
    @Override
    public void increaseOfCollectionNum(ReportInfo reportInfo) {
        reportInfoMapper.increaseOfCollectionNum(reportInfo);
    }

    @Override
    public void decreaseOfCollectionNum(ReportInfo reportInfo) {
        reportInfoMapper.decreaseOfCollectionNum(reportInfo);
    }

    @Override
    public List<ReportInfo> orderByFavoriteNum() {
        return reportInfoMapper.orderByFavoriteNum();
    }
    @Override
    public Integer countByWriterId(String userId) {
        return reportInfoMapper.countByWriterId(userId);
    }
}
