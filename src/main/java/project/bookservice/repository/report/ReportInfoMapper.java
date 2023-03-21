package project.bookservice.repository.report;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.ReportForm;
import project.bookservice.web.validation.form.ReportSaveForm;
import project.bookservice.web.validation.form.SignUpForm;

import java.util.List;
import java.util.Optional;
@Mapper// 이 어노테이션을 붙여줘야 MyBatis에서 인식할 수 있음.
public interface ReportInfoMapper {
    // 실행할 SQL은 패키지 위치는 이 인터페이스와 동일한 경로로 만들어줘야함.
    void save(ReportSaveForm reportSaveForm);

//    void update(@Param("id")Long id, @Param("updateParam")ItemUpdateDto updatePara);

    Optional<ReportInfo> findById(Long id);
//
//    // Sign-up email exists check
//    int findByEmail(String email);
//
//    // Sign-up email userId check
//    int findByUserId(String userId);
//
    List<ReportInfo> findAll();

}