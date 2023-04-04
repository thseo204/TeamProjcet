package project.bookservice.repository.report;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.report.Keyword;
import project.bookservice.web.validation.form.KeywordSaveForm;

import java.util.List;

@Mapper
public interface KeywordMapper {
    void save(KeywordSaveForm form);
    void update(KeywordSaveForm form);
    void deleteReport(Long reportId);
    List<Keyword> findAll();

    List<Keyword> findByKeyword(String keyword);

    String[] findByLikeKeyword(String keyword);

    int countKeyword(String keyword);
}
