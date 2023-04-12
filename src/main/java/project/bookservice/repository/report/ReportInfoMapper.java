package project.bookservice.repository.report;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportSaveForm;

import java.util.List;
import java.util.Optional;

@Mapper// 이 어노테이션을 붙여줘야 MyBatis에서 인식할 수 있음.
public interface ReportInfoMapper {
    // 실행할 SQL은 패키지 위치는 이 인터페이스와 동일한 경로로 만들어줘야함.
    void save(ReportSaveForm reportSaveForm);

    void delete(Long id);

    void edit(@Param("reportInfoParam") ReportInfo reportInfo);

    Optional<ReportInfo> findById(Long id);

    int existsReportInfo(String userId);

    List<ReportInfo> findByUserId(String userId);

    int existsReportInfoByIsbn(String isbn);

    List<ReportInfo> findByIsbn(String isbn);

    List<ReportInfo> findAll();

    void increaseOfFavoriteNum(@Param("reportInfoParam") ReportInfo reportInfo);

    void decreaseOfFavoriteNum(@Param("reportInfoParam") ReportInfo reportInfo);

    void increaseOfCollectionNum(@Param("reportInfoParam") ReportInfo reportInfo);

    void decreaseOfCollectionNum(@Param("reportInfoParam") ReportInfo reportInfo);

    List<ReportInfo> orderByFavoriteNum();

    Integer countByWriterId(String userId);
}
