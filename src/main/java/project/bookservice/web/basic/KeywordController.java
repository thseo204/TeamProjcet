package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.report.Keyword;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.service.report.KeywordService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.service.starRating.StarRatingServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;
    private final StarRatingService starRatingService;
    private final ReportInfoService reportInfoService;

    @GetMapping("/searchKeyword/{keyword}")
    public String keywordSearch(@PathVariable String keyword,
                                Model model) throws ParseException {


        List<Keyword> keywordList = keywordService.findByKeyword(keyword);

        List<Book> bookList = new ArrayList<>();
        List<ReportInfo> reportInfoList = new ArrayList<>();
        APIParser apiParser = new ApiSearchBook(starRatingService);
        for (Keyword keywords : keywordList) {
            log.info("keywords.getIsbn()={}", keywords.getIsbn());
            ArrayList<Book> book = apiParser.jsonAndXmlParserToArr(keywords.getIsbn());
            bookList.add(book.get(0));

            ReportInfo reportInfo = reportInfoService.findById(keywords.getReportId());
            reportInfoList.add(reportInfo);
        }


        model.addAttribute("keyword", keyword);
        model.addAttribute("keywordList", keywordList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("reportInfoList", reportInfoList);
        return "/detail/searchKeyword";
    }

    @GetMapping("/searchKeyword")
    public String keywordSearchForm(@RequestParam String keyword,
                                    RedirectAttributes redirectAttributes){
//
//        if(keyword== null){
//            //키워드 검색어를 입력하지 않은 경우 DB에 있는 키워드 중 랜덤으로 검색하여 보여주기
//            List<Keyword> keywordList = keywordService.findAll();
//            Collections.shuffle(keywordList);
//            Keyword randomKeyword = keywordList.get(0);
//            log.info("randomKeyword={}" , randomKeyword);
//            redirectAttributes.addAttribute("keyword", randomKeyword);
//            return "redirect:/searchKeyword/{keyword}";
//        }
        log.info("keyword={}" , keyword);
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/searchKeyword/{keyword}";
    }

}