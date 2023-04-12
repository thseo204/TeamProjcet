package project.bookservice.repository.report;

import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.util.List;

public interface ReportInfoRepository {
    ReportSaveForm save(ReportSaveForm reportSaveForm);

    void delete(Long id);

    void edit(ReportInfo reportInfo);

    ReportInfo findById(Long id);

    int existsReportInfo(String userId);

    List<ReportInfo> findByUserId(String userId);

    int existsReportInfoByIsbn(String isbn);

    List<ReportInfo> findByIsbn(String isbn);

    List<ReportInfo> findAll();

    void increaseOfFavoriteNum(ReportInfo reportInfo);

    void decreaseOfFavoriteNum(ReportInfo reportInfo);

    void increaseOfCollectionNum(ReportInfo reportInfo);

    void decreaseOfCollectionNum(ReportInfo reportInfo);

    List<ReportInfo> orderByFavoriteNum();

    Integer countByWriterId(String userId);
}
