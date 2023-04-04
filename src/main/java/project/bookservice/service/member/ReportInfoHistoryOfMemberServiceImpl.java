package project.bookservice.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.repository.member.ReportInfoHistoryOfMemberRepository;
import project.bookservice.web.validation.form.ReportInfoHistoryOfMemberSaveForm;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportInfoHistoryOfMemberServiceImpl implements ReportInfoHistoryOfMemberService {

    private final ReportInfoHistoryOfMemberRepository reportInfoHistoryOfMemberRepository;

    @Override
    public void save(ReportInfoHistoryOfMemberSaveForm form) {
        reportInfoHistoryOfMemberRepository.save(form);
    }

    @Override
    public void delete(ReportInfoHistoryOfMember reportInfoHistoryOfMember) {
        reportInfoHistoryOfMemberRepository.delete(reportInfoHistoryOfMember);
    }

    @Override
    public void delete(Long reportId) {
        reportInfoHistoryOfMemberRepository.delete(reportId);
    }

    @Override
    public int existsByHistory(String userId) {
        return reportInfoHistoryOfMemberRepository.existsByHistory(userId);
    }

    @Override
    public List<ReportInfoHistoryOfMember> findAll() {
        return reportInfoHistoryOfMemberRepository.findAll();
    }

    @Override
    public List<ReportInfoHistoryOfMember> findByUserId(String userId) {
        return reportInfoHistoryOfMemberRepository.findByUserId(userId);
    }
}
