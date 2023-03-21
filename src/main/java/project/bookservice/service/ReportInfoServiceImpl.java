package project.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.repository.report.ReportInfoRepository;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportInfoServiceImpl implements ReportInfoService {

    private final ReportInfoRepository reportInfoRepository;

    @Override
    public ReportSaveForm save(ReportSaveForm reportSaveForm) {
        return reportInfoRepository.save(reportSaveForm);
    }

    @Override
    public ReportInfo findById(Long id) {
//        reportInfoRepository.findById(id);
        return reportInfoRepository.findById(id);
    }

    @Override
    public List<ReportInfo> findAll() {
        return reportInfoRepository.findAll();
    }

//    private Optional<ReportInfo> getReportInfo(Long id) {
//
//        return reportInfoRepository.findById(id);
//    }
}
