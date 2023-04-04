package project.bookservice.repository.member;

import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;

public interface ReportInfoHistoryOfMemberRepository {
    void save(ReportInfoHistoryOfMemberSaveForm form);
    void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember);
    void delete(Long reportId); // 작성자가 게시글 삭제했을 경우 모든 유저의 독후기록 저장에서 삭제

    int existsByHistory(String userId);
    List<ReportInfoHistoryOfMember> findAll();
    List<ReportInfoHistoryOfMember> findByUserId(String userId);

}
