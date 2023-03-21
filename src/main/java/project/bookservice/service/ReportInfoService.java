package project.bookservice.service;

import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;
import project.bookservice.web.validation.form.SignUpForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReportInfoService {

    ReportSaveForm save(ReportSaveForm reportSaveForm);
    ReportInfo findById(Long id);

    List<ReportInfo> findAll();
}
