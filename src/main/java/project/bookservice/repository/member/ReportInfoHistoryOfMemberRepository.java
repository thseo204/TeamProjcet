package project.bookservice.repository.member;

import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;

public interface ReportInfoHistoryOfMemberRepository {
    void save(ReportInfoHistoryOfMemberSaveForm form);
    void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember);
    int existsByHistory(String userId);
    List<ReportInfoHistoryOfMember> findAll();
    List<ReportInfoHistoryOfMember> findByUserId(String userId);

}
