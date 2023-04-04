package project.bookservice.service.member;

import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;

public interface ReportInfoHistoryOfMemberService {

    void save(ReportInfoHistoryOfMemberSaveForm form);
    void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember);
    void delete(Long reportId);
    int existsByHistory(String userId);
    List<ReportInfoHistoryOfMember> findAll();

    List<ReportInfoHistoryOfMember> findByUserId(String userId);
}
