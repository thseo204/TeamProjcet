package project.bookservice.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.bookservice.domain.report.Keyword;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.repository.report.KeywordRepository;
import project.bookservice.web.validation.form.KeywordSaveForm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Override
    public void save(KeywordSaveForm form) {
        keywordRepository.save(form);
    }

    @Override
    public void update(KeywordSaveForm form) {
        keywordRepository.update(form);
    }

    @Override
    public void deleteReport(Long reportId) {
        keywordRepository.deleteReport(reportId);
    }

    @Override
    public List<Keyword> findAll() {
        return keywordRepository.findAll();
    }

    @Override
    public List<Keyword> findByKeyword(String keyword) {
        return keywordRepository.findByKeyword(keyword);
    }

    @Override
    public String[] findByLikeKeyword(String keyword) {
        return keywordRepository.findByLikeKeyword(keyword);
    }

    @Override
    public int countKeyword(String keyword) {
        return keywordRepository.countKeyword(keyword);
    }

    @Override
    public void deleteKeywords(ReportInfo reportInfo) {
        keywordRepository.deleteKeywords(reportInfo);
    }
}
