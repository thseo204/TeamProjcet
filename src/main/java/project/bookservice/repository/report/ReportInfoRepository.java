package project.bookservice.repository.report;

import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReportInfoRepository {
    ReportSaveForm save(ReportSaveForm reportSaveForm);
    ReportInfo findById(Long id);
//    Optional<ReportInfo> findById(Long id);
//    Optional<ReportInfo> findByUserId(String userId, String isbn);
    List<ReportInfo> findAll();
}
