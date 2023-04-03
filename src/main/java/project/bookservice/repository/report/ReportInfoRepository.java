package project.bookservice.repository.report;

import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReportInfoRepository {
    ReportSaveForm save(ReportSaveForm reportSaveForm);
    void delete(Long id);

    void edit(ReportInfo reportInfo);
    ReportInfo findById(Long id);
//    Optional<ReportInfo> findById(Long id);
//    Optional<ReportInfo> findByUserId(String userId, String isbn);
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
