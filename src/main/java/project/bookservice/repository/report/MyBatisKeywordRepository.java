package project.bookservice.repository.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.report.Keyword;
import project.bookservice.web.validation.form.KeywordSaveForm;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisKeywordRepository implements KeywordRepository{

    private final KeywordMapper keywordMapper;
    @Override
    public void save(KeywordSaveForm form) {
        keywordMapper.save(form);
    }

    @Override
    public void update(KeywordSaveForm form) {
        keywordMapper.update(form);
    }

    @Override
    public void deleteReport(Long reportId) {
        keywordMapper.deleteReport(reportId);
    }

    @Override
    public List<Keyword> findAll() {
        return keywordMapper.findAll();
    }

    @Override
    public List<Keyword> findByKeyword(String keyword) {
        return keywordMapper.findByKeyword(keyword);
    }

    @Override
    public String[] findByLikeKeyword(String keyword) {
        return keywordMapper.findByLikeKeyword(keyword);
    }

    @Override
    public int countKeyword(String keyword) {
        return keywordMapper.countKeyword(keyword);
    }

}
