package project.bookservice.repository.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisReportRepository implements ReportInfoRepository, Serializable {
    // 매퍼에 위임하는 코드

    private final ReportInfoMapper reportInfoMapper;
    @Override
    public ReportSaveForm save(ReportSaveForm reportSaveForm) {
        log.info("reportInfo info={}", reportSaveForm);

        reportInfoMapper.save(reportSaveForm);
        return reportSaveForm;
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
    public List<ReportInfo> findAll() {
        return reportInfoMapper.findAll();
    }

    //추후 isbn 과 userId 로 리스트 출력하는 메서드 생성하기!!
//    @Override
//    public Optional<Member> findById(String userId) {
//        log.info("userId info={}", userId);
//
//        return reportInfoMapper.findById(userId);
//    }
}
