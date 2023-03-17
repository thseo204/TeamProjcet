package project.bookservice.repository.report;

import project.bookservice.domain.book.Book;
import project.bookservice.domain.report.ReportInfo;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    ReportInfo save(ReportInfo reportInfo);
//    Optional<ReportInfo> findByUserId(String userId, String isbn);
//    List<ReportInfo> findByBookTitle(String bookTitle);
}
