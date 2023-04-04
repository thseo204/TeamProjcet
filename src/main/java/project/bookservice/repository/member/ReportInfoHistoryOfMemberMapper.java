package project.bookservice.repository.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;

@Mapper// 이 어노테이션을 붙여줘야 MyBatis에서 인식할 수 있음.
public interface ReportInfoHistoryOfMemberMapper {
    // 실행할 SQL은 패키지 위치는 이 인터페이스와 동일한 경로로 만들어줘야함.
    void save(ReportInfoHistoryOfMemberSaveForm form);
    void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember);
    void deleteAll(Long reportId); // 작성자가 게시글 삭제했을 경우 모든 유저의 독후기록 저장에서 삭제
    int existsByHistory(String userId); // List로 불러오기 전 히스토리가 있는지 체크하기 위한 쿼리
    List<ReportInfoHistoryOfMember> findAll();

    List<ReportInfoHistoryOfMember> findByUserId(String userId);


}
