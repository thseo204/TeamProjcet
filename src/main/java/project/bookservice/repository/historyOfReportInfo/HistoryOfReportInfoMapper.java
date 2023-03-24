package project.bookservice.repository.historyOfReportInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.bookservice.domain.historyOfReportInfo.HistoryOfReportInfo;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;

import java.util.List;

@Mapper // 이 어노테이션을 붙여줘야 MyBatis에서 인식할 수 있음.
public interface HistoryOfReportInfoMapper {
    // 실행할 SQL은 패키지 위치는 이 인터페이스와 동일한 경로로 만들어줘야함.
    void save(HistoryOfReportInfoSaveForm historyOfReportInfoSaveForm);
    List<HistoryOfReportInfo> findByUserId(String userId);

    Boolean hasHistory(@Param("userId") String userId, @Param("reportInfoParam") ReportInfo reportInfo);
    void updateFavorite(@Param("reportId") Long id, @Param("historyParam") HistoryOfReportInfo history);

    void updateCollection(@Param("historyParam") HistoryOfReportInfo history);

}
