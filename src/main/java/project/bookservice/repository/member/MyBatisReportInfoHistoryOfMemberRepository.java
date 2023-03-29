package project.bookservice.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisReportInfoHistoryOfMemberRepository implements ReportInfoHistoryOfMemberRepository {

    private final ReportInfoHistoryOfMemberMapper reportInfoHistoryOfMemberMapper;
    @Override
    public void save(ReportInfoHistoryOfMemberSaveForm form) {
        reportInfoHistoryOfMemberMapper.save(form);
    }

    @Override
    public void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember) {
        reportInfoHistoryOfMemberMapper.delete(reportInfoHistoryOfMember);
    }

    @Override
    public int existsByHistory(String userId) {
        return reportInfoHistoryOfMemberMapper.existsByHistory(userId);
    }

    @Override
    public List<ReportInfoHistoryOfMember> findAll() {
        return reportInfoHistoryOfMemberMapper.findAll();
    }

    @Override
    public List<ReportInfoHistoryOfMember> findByUserId(String userId) {
        return reportInfoHistoryOfMemberMapper.findByUserId(userId);
    }
}
