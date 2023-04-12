package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.comment.Comment;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.domain.member.Member;

import project.bookservice.domain.report.ReportInfo;
import project.bookservice.domain.starRating.StarRating;
import project.bookservice.repository.book.MyBatisBookRepository;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.openapi.ApiSearchBookList;

import project.bookservice.service.comment.CommentService;
import project.bookservice.service.member.BookmarkCollectionService;
import project.bookservice.service.member.BookmarkHistoryOfMemberService;
import project.bookservice.service.report.KeywordService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;
import project.bookservice.web.validation.form.BookmarkHistoryOfMemberSaveForm;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final MyBatisBookRepository bookRepository;

	 private final CommentService commentService;
     private final StarRatingService starRatingService;
    private final BookmarkHistoryOfMemberService bookmarkHistoryOfMemberService;
    private final BookmarkCollectionService bookmarkCollectionService;
	 private final KeywordService keywordService;
    private final ReportInfoService reportInfoService;

    @GetMapping("/BookList")
    public String BookList(String title,Book book,Model model) throws ParseException {
        log.info("title ={}", title);
        String[] keywordArr = keywordService.findByLikeKeyword(title);
        if(keywordArr.length != 0){
            model.addAttribute("keywordArr", keywordArr);
        }

            return "basic/searchBookList";
    }

    @GetMapping("/NoSearchWord")
    public String MoSearchWord(String title,Book book,Model model) throws ParseException {

        log.info("title ={}", title);

        //검색어가 아무겄또 없을경우 베스트셀러 정보 가져오기
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);

        //랜덤한 숫자 생성
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;

        Book randomBook = bookList.get(randomNumber-1);
        model.addAttribute("randomBook", randomBook);
        model.addAttribute("randomNumber", randomNumber);
        return "basic/searchBookList";
    }

    @GetMapping("/searchBookList")
    public String searchBook(String title,Book book,Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) throws ParseException {

        log.info("title ={}", title);

        if (title.isBlank()) {
            title = null;
            return "redirect:/NoSearchWord";

        }else {
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(title);
			String[] keywordArr = keywordService.findByLikeKeyword(title);

            // 전체 데이터 개수
            int total = bookList.size();

            if(total == 0){
                return "redirect:/NoSearchWord";
            }
            // 총 페이지 수 계산
            int totalPages = (int) Math.ceil((double) total / size);


            // 현재 페이지의 데이터 추출
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            List<Book> currentPageList = bookList.subList(start, end);

            // 모델에 데이터 담기
            model.addAttribute("bookList", currentPageList);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
			model.addAttribute("keywordArr", keywordArr);
            return "basic/searchBookList";
        }
    }

    @GetMapping("/book/{isbn}")
    public String book(Model model,
                       RedirectAttributes redirectAttributes){

        boolean bookAndReport=true;
        redirectAttributes.addAttribute("bookAndReport", bookAndReport);
        return "redirect:/book/{isbn}/bookInfo/{bookAndReport}";
    }

    @GetMapping("/book/{isbn}/bookInfo/{bookAndReport}")
    public String bookInfo(@PathVariable String isbn,@PathVariable boolean bookAndReport, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model,StarRating starRating) throws ParseException {

        //bookAndReport 가 true 일때 -> 책상세정보 페이지
        if(bookAndReport == true){
            //로그인 세션에 정보가 없을경우 (비로그인 상태)
            if(loginMember == null){
                APIParser apiParser = new ApiSearchBook(starRatingService);
                ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
                Book book = bookList.get(0);
                List<Comment> comments = commentService.findComments(isbn);
                log.info("avgStarRating={}",bookList.get(0).getAvgStarRating());
                log.info("comments={}",comments);
                model.addAttribute("comments", comments);
                model.addAttribute("book",book);

                return "detail/bookInfo";
            }
            //로그인 세션에 정보가 있을경우 (로그인 상태)
            starRating.setUserId(loginMember.getUserId()); //starRatingService 에 아이디 값을 넘겨주기 위해 세션 아이디값을 starRating 에 넣어준다

            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn); //책정보
            Book book = bookList.get(0);
            List<Comment> comments = commentService.findComments(isbn); //이 책에 등록된 댓글 정보
            Boolean starId = starRatingService.findByUserId(starRating); // 로그인한 아이디가 별점 부여했는지 유무 체크


            // 로그인한 아이디의 컬렉션 이름 리스트 가져오기
            int existsByMyHistory = bookmarkCollectionService.existsCollection(loginMember.getUserId());
            if(existsByMyHistory > 0){
                List<BookmarkCollection> myCollectionList = bookmarkCollectionService.collectionList(loginMember.getUserId());

                for (BookmarkCollection bookmarkCollection : myCollectionList) {
                    log.info("bookmarkCollection={}", bookmarkCollection);
                }
                model.addAttribute("myCollectionList", myCollectionList);
            }
            model.addAttribute("existsByMyHistory", existsByMyHistory);

            log.info("starId={}", starId); //별점 유무
            log.info("userId={}", starRating.getUserId()); //로그인한 아이디
            log.info("isbn={}", starRating.getIsbn()); // 책 isbn

            model.addAttribute("comments", comments);
            model.addAttribute("book",book);
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("starId",starId);
            return "detail/bookInfo";
        } else{   //bookAndReport 가 false 일때 -> 책 독후감 페이지
            if(loginMember == null){
                APIParser apiParser = new ApiSearchBook(starRatingService);
                ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
                Book book = bookList.get(0);

                int existsByBookHistory = reportInfoService.existsReportInfoByIsbn(isbn);

                if(existsByBookHistory > 0){
                    List<ReportInfo> bookReportInfoList = reportInfoService.findByIsbn(isbn);
                    model.addAttribute("bookReportInfoList", bookReportInfoList);
                    log.info("bookReportInfoList ={}",bookReportInfoList);
                }


                log.info("avgStarRating={}",bookList.get(0).getAvgStarRating());
                model.addAttribute("existsByBookHistory", existsByBookHistory);
                model.addAttribute("book",book);

                return "detail/bookInfo";
            }

            //로그인 세션에 정보가 있을경우 (로그인 상태)
            starRating.setUserId(loginMember.getUserId()); //starRatingService 에 아이디 값을 넘겨주기 위해 세션 아이디값을 starRating 에 넣어준다

            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn); //책정보
            Book book = bookList.get(0);

            int existsByBookHistory = reportInfoService.existsReportInfoByIsbn(isbn);

            if(existsByBookHistory>0){
                List<ReportInfo> bookReportInfoList = reportInfoService.findByIsbn(isbn);
                model.addAttribute("bookReportInfoList", bookReportInfoList);
                log.info("bookReportInfoList ={}",bookReportInfoList);
            }

            Boolean starId = starRatingService.findByUserId(starRating); // 로그인한 아이디가 별점 부여했는지 유무 체크

            // 로그인한 아이디의 컬렉션 이름 리스트 가져오기
            int existsByMyHistory = bookmarkCollectionService.existsCollection(loginMember.getUserId());
            if(existsByMyHistory > 0){
                List<BookmarkCollection> myCollectionList = bookmarkCollectionService.collectionList(loginMember.getUserId());

                for (BookmarkCollection bookmarkCollection : myCollectionList) {
                    log.info("bookmarkCollection={}", bookmarkCollection);
                }
                model.addAttribute("myCollectionList", myCollectionList);
            }
            model.addAttribute("existsByMyHistory", existsByMyHistory);

            log.info("starId={}", starId); //별점 유무
            log.info("userId={}", starRating.getUserId()); //로그인한 아이디
            log.info("isbn={}", starRating.getIsbn()); // 책 isbn

            model.addAttribute("book",book);
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("existsByBookHistory", existsByBookHistory);
            model.addAttribute("starId",starId);
            return "detail/bookInfo";

        }

    }

    @GetMapping("/book/{isbn}/report")
    public String bookInfoReport(@PathVariable String isbn, Model model,StarRating starRating, @SessionAttribute(name= SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember) throws ParseException {

        //로그인 세션에 정보가 없을경우 (비로그인 상태)
        if(loginMember == null){
            APIParser apiParser = new ApiSearchBook(starRatingService);
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
            Book book = bookList.get(0);
            model.addAttribute("book",book);

            return "detail/bookInfoReport";
        }

        //로그인 세션에 정보가 있을경우 (로그인 상태)
        starRating.setUserId(loginMember.getUserId());
        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
        Book book = bookList.get(0);
        Boolean starId = starRatingService.findByUserId(starRating); // 로그인한 아이디가 별점 부여했는지 유무 체크
        model.addAttribute("book",book);
        model.addAttribute("starId",starId);

        return "detail/bookInfoReport";
    }



    @GetMapping("/starRating/{isbn}")
    public String starRating(@PathVariable String isbn, Model model){
        Book book = bookRepository.findByIsbn(isbn);

        model.addAttribute("book",book);

        return "detail/starRating";
    }

    @PostMapping("/starRating/{isbn}")
    public String insertStarRating(@ModelAttribute StarRating starRating){
        StarRating saveStarRating = starRatingService.saveStarRating(starRating);
        return "detail/thanks";
    }


    @GetMapping("/bookReportForm")
    public String bookReport(String isbn, Model model) {

        return "detail/bookReport";
    }

    /**
     * bookInfo collection 저장 눌렀을 때
     * 해당 컬렉션에 동일한 도서가 존재하는지 체크
     */
    @ResponseBody
    @GetMapping("/book/{isbn}/checkCollection/{collectionName}")
    public String bookCollectionClick(@PathVariable String isbn,
                                      @PathVariable String collectionName,
                                      Model model, StarRating starRating,
                                      RedirectAttributes redirectAttributes,
                                      @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member loginMember) throws ParseException {

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
        Book book = bookList.get(0);
        Map<String, String> existsBookInCollection = new HashMap<>();
        existsBookInCollection.put("userId", loginMember.getUserId());
        existsBookInCollection.put("collectionName", collectionName);
        existsBookInCollection.put("isbn", isbn);
        int exists = bookmarkHistoryOfMemberService.existsByIsbn(existsBookInCollection);

        if(exists > 0){ // 해당 컬렉션에 해당 도서가 있으면 컬렉션에 중복추가 안하고 돌아가기
            String message = "<script>alert('해당 컬렉션에 이미 저장되어 있는 도서입니다.');location.href='/book/" + isbn + "';</script>";
            return message;
        }

        // 해당 컬렉션에 해당 도서가 없으면 컬렉션에 추가
        return "<script>alert('저장되었습니다.');location.href='/book/" +
                isbn + "}/addCollection/" + collectionName + "';</script>";

    }

    /**
     * book collection 저장 눌렀을 때
     * 해당 컬렉션에 바로 저장하기
     */
    @GetMapping("/book/{isbn}/addCollection/{collectionName}")
    public String addBookInCollection(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                      Model model,
                                      @PathVariable String isbn,
                                      @PathVariable String collectionName,
                                      RedirectAttributes redirectAttributes) throws ParseException {

        APIParser apiParser = new ApiSearchBook(starRatingService);
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(isbn);
        Book book = bookList.get(0);

        BookmarkHistoryOfMemberSaveForm form = new BookmarkHistoryOfMemberSaveForm();
        form.setUserId(loginMember.getUserId());
        form.setIsbn(book.getIsbn());
        form.setImageUrl(book.getImageUrl());
        form.setTitle(book.getTitle());
        form.setAuthor(book.getAuthor());
        form.setCollectionName(collectionName);
        bookmarkHistoryOfMemberService.save(form);
        log.info("saveForm={}", form);
        return "redirect:/book/{isbn}";
    }

    @GetMapping("/addCollectionFormAtBookInfo/{isbn}")
    public String addCollection(Model model, BookmarkCollectionSaveForm form,
                                @PathVariable String isbn
                                ) {
        BookmarkCollection bookmarkCollection = new BookmarkCollection();
        bookmarkCollection.setCollectionName(form.getCollectionName());
        log.info("form.getCollectionName={}", form.getCollectionName());

        model.addAttribute("isbn", isbn);
        model.addAttribute("bookmarkCollection", bookmarkCollection);
        return "detail/addCollectionAtBookInfo";
    }
    @GetMapping("/book/{isbn}/{collectionName}")
    public String myBookmarkFormPost(
            @PathVariable String collectionName,
            @PathVariable String isbn,
            BookmarkCollectionSaveForm form,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember,
            Model model,
            RedirectAttributes redirectAttributes){

        BookmarkCollectionSaveForm saveForm = new BookmarkCollectionSaveForm();
        saveForm.setUserId(loginMember.getUserId());
        saveForm.setCollectionName(collectionName);
        log.info("form={}", form.getCollectionName());
        bookmarkCollectionService.save(saveForm);

        redirectAttributes.addAttribute("isbn", isbn);

        return "redirect:/book/{isbn}";
    }
}
