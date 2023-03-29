package project.bookservice.service.report;

import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;
import project.bookservice.web.validation.form.SignUpForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReportInfoService {

    ReportSaveForm save(ReportSaveForm reportSaveForm);
    void delete(Long id);

    void edit(ReportInfo reportInfo);
    ReportInfo findById(Long id);
    int existsReportInfo(String userId);
    List<ReportInfo> findByUserId(String userId);
    List<ReportInfo> findAll();
    void increaseOfFavoriteNum(ReportInfo reportInfo);
    void decreaseOfFavoriteNum(ReportInfo reportInfo);

    void increaseOfCollectionNum(ReportInfo reportInfo);
    void decreaseOfCollectionNum(ReportInfo reportInfo);
    List<ReportInfo> orderByFavoriteNum();
}
