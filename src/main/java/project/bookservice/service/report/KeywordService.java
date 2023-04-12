package project.bookservice.service.report;

import project.bookservice.domain.report.Keyword;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.web.validation.form.KeywordSaveForm;

import java.util.List;

public interface KeywordService {
    void save(KeywordSaveForm form);

    void update(KeywordSaveForm form);

    void deleteReport(Long reportId);

    List<Keyword> findAll();

    List<Keyword> findByKeyword(String keyword);

    String[] findByLikeKeyword(String keyword);

    int countKeyword(String keyword);

    void deleteKeywords(ReportInfo reportInfo);
}
