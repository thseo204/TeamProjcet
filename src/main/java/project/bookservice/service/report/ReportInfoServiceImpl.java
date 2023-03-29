package project.bookservice.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.repository.report.ReportInfoRepository;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportInfoServiceImpl implements ReportInfoService {

    private final ReportInfoRepository reportInfoRepository;

    @Override
    public ReportSaveForm save(ReportSaveForm reportSaveForm) {
        return reportInfoRepository.save(reportSaveForm);
    }

    @Override
    public void delete(Long id) {
        reportInfoRepository.delete(id);
    }

    @Override
    public void edit(ReportInfo reportInfo) {
        reportInfoRepository.edit(reportInfo);
    }

    @Override
    public ReportInfo findById(Long id) {
//        reportInfoRepository.findById(id);
        return reportInfoRepository.findById(id);
    }

    @Override
    public int existsReportInfo(String userId) {
        return reportInfoRepository.existsReportInfo(userId);
    }

    @Override
    public List<ReportInfo> findByUserId(String userId) {
        return reportInfoRepository.findByUserId(userId);
    }

    @Override
    public List<ReportInfo> findAll() {
        return reportInfoRepository.findAll();
    }

    /**
     * 좋아요 수 증감
     */
    @Override
    public void increaseOfFavoriteNum(ReportInfo reportInfo) {
        reportInfoRepository.increaseOfFavoriteNum(reportInfo);
    }

    @Override
    public void decreaseOfFavoriteNum(ReportInfo reportInfo) {
        reportInfoRepository.decreaseOfFavoriteNum(reportInfo);
    }

    /**
     * 컬렉션 저장 수 증감
     */
    @Override
    public void increaseOfCollectionNum(ReportInfo reportInfo) {
        reportInfoRepository.increaseOfCollectionNum(reportInfo);
    }

    @Override
    public void decreaseOfCollectionNum(ReportInfo reportInfo) {
        reportInfoRepository.decreaseOfCollectionNum(reportInfo);
    }

    @Override
    public List<ReportInfo> orderByFavoriteNum() {
        return reportInfoRepository.orderByFavoriteNum();
    }
    @Override
    public Integer countByWriterId(String userId) {
        return reportInfoRepository.countByWriterId(userId);
    }
}
