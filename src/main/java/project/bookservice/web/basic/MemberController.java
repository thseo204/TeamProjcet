package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBook;
import project.bookservice.service.member.BookmarkCollectionService;
import project.bookservice.service.member.BookmarkHistoryOfMemberService;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.join.JoinService;
import project.bookservice.service.member.ReportInfoHistoryOfMemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.service.starRating.StarRatingService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.SignUpFormValidator;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;
import project.bookservice.web.validation.form.SignUpForm;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JoinService joinService;
    private final SignUpFormValidator signUpFormValidator;
    private final ReportInfoService reportInfoService;
    private final BookmarkHistoryOfMemberService bookmarkHistoryOfMemberService;
    private final ReportInfoHistoryOfMemberService reportInfoHistoryOfMemberService;
    private final BookmarkCollectionService bookmarkCollectionService;
    private final StarRatingService starRatingService;

    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute Member member){
        return "/basic/joinForm";
    }

    @PostMapping("/joinForm")
    public String signUpSubmit(@Valid @ModelAttribute("member") SignUpForm signUpForm, BindingResult bindingResult) {
        log.info("signUpForm ={}", signUpForm);

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/joinForm";
        }

        //중복 검사(아이디, 이메일, 비빌버호 일치 검증)
        signUpFormValidator.validate(signUpForm, bindingResult);


        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/joinForm";
        }

        //회원 정보 등록
        memberService.save(signUpForm);

        return "/basic/joinSuccessForm"; // 회원가입 성공 시 축하페이지로 이동
    }

    @GetMapping("/joinSuccessForm")
    public String joinSuccessForm(){
        return "/basic/joinSuccessForm";
    }

    @GetMapping("/myBookmarkForm")
    public String myBookmarkForm(
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
                                     Member loginMember,
            Model model,
            RedirectAttributes redirectAttributes){

        boolean bookAndReport=true;
        redirectAttributes.addAttribute("bookAndReport", bookAndReport);

        return "redirect:/myBookmarkForm/{bookAndReport}";
    }

    @GetMapping("/myBookmarkForm/true/{collectionName}")
    public String myBookmarkFormPost(
            @PathVariable String collectionName,
            BookmarkCollectionSaveForm form,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required = false)
            Member loginMember,
            Model model,
            RedirectAttributes redirectAttributes){

//        String collectionName = (String)model.getAttribute("collectionName");
        BookmarkCollectionSaveForm saveForm = new BookmarkCollectionSaveForm();
        saveForm.setUserId(loginMember.getUserId());
        saveForm.setCollectionName(collectionName);
        log.info("form={}", form.getCollectionName());
        bookmarkCollectionService.save(saveForm);
        boolean bookAndReport=true;
        redirectAttributes.addAttribute("bookAndReport", bookAndReport);

        return "redirect:/myBookmarkForm/{bookAndReport}";
    }

    @GetMapping("/myBookmarkForm/{bookAndReport}")
    public String bookAndReportForm(@PathVariable boolean bookAndReport,
                                    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                                    Member loginMember, Model model,
                                    RedirectAttributes redirectAttributes) {
        int existsByHistory = 0;
        if (bookAndReport == true) {

            int existsByMyHistory = bookmarkCollectionService.existsCollection(loginMember.getUserId());
            if(existsByMyHistory > 0){
                List<BookmarkCollection> myCollectionList = bookmarkCollectionService.collectionList(loginMember.getUserId());
//                Optional<BookmarkHistoryOfMember> myCollectionList = myBookCollectionList.stream().distinct().filter(m -> m.getCollectionName() != null).findAny();
                for (BookmarkCollection bookmarkCollection : myCollectionList) {
                    log.info("bookmarkCollection={}", bookmarkCollection);
                }
                model.addAttribute("myCollectionList", myCollectionList);
            }
            existsByHistory = bookmarkHistoryOfMemberService.existsByHistory(loginMember.getUserId());
            if (existsByHistory > 0) {
                List<BookmarkHistoryOfMember> bookmarkList = bookmarkHistoryOfMemberService.findByUserIdDistinctIsbn(loginMember.getUserId());
//                bookmarkList.stream().distinct().collect(Collectors.toList());////


                model.addAttribute("bookmarkList", bookmarkList);
            }
            model.addAttribute("existsByMyHistory", existsByMyHistory);
            model.addAttribute("existsByHistory", existsByHistory);
            model.addAttribute("bookAndReport", true);
            return "detail/myBookmark";
//            return "redirect:/myBookmark/{bookAndReport}";

        } else {
            int existsByMyHistory = reportInfoService.existsReportInfo(loginMember.getUserId());
            if(existsByMyHistory > 0){
                List<ReportInfo> myReportInfoList = reportInfoService.findByUserId(loginMember.getUserId());
                model.addAttribute("myReportInfoList", myReportInfoList);
            }
            existsByHistory = reportInfoHistoryOfMemberService.existsByHistory(loginMember.getUserId());
            if (existsByHistory > 0) {
                List<ReportInfoHistoryOfMember> reportInfoList = reportInfoHistoryOfMemberService.findByUserId(loginMember.getUserId());
                model.addAttribute("reportInfoList", reportInfoList);
                for (ReportInfoHistoryOfMember reportInfoHistoryOfMember : reportInfoList) {
                    log.info("reportInfoHistoryOfMember={}", reportInfoHistoryOfMember);
                }

            }
            model.addAttribute("existsByMyHistory", existsByMyHistory);
            model.addAttribute("existsByHistory", existsByHistory);
            model.addAttribute("bookAndReport", false);
        }
        return "detail/myBookmark";
//        return "redirect:/myBookmark/{bookAndReport}";
    }

    @PostMapping("/myBookmarkForm/true/{collectionName}")
    public String addBookInCollection(@PathVariable String collectionName,
                                    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                                    Member loginMember, Model model,
                                    @RequestParam(value = "cb") List<Long> checkboxList,
                                    RedirectAttributes redirectAttributes) {
//        List<Long> checkboxList = (List<Long>) model.getAttribute("cb[]");
        log.info("collectionName={}", collectionName);
        Map<String, String> updateCollectionName = new HashMap<>();
        for (Long checkId : checkboxList) {
            log.info("checkId={}", checkId);
            updateCollectionName.put("checkId", checkId+"");
            updateCollectionName.put("collectionName", collectionName);
            bookmarkHistoryOfMemberService.updateCollectionName(updateCollectionName);
        }
        redirectAttributes.addAttribute("bookAndReport", true);
        return "redirect:/myBookmarkForm/{bookAndReport}";
    }

    @GetMapping("/addCollectionFormAtMyBookmark")
    public String addCollection(Model model, BookmarkCollectionSaveForm form) {
        BookmarkCollection bookmarkCollection = new BookmarkCollection();
        bookmarkCollection.setCollectionName(form.getCollectionName());
        log.info("form.getCollectionName={}", form.getCollectionName());

        model.addAttribute("bookmarkCollection", bookmarkCollection);
        return "detail/addCollectionAtMyBookmark";
    }

    @GetMapping("/collectionListForm/{collectionName}")
    public String collectionListForm(@PathVariable String collectionName,
                                     @SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember,
                                     Model model){
        // 회원이 가지고 있는 컬렉션 리스트 model 에 담기
        int existsByMyHistory = bookmarkCollectionService.existsCollection(loginMember.getUserId());
        if(existsByMyHistory > 0){
            List<BookmarkCollection> myCollectionList = bookmarkCollectionService.collectionList(loginMember.getUserId());
//                Optional<BookmarkHistoryOfMember> myCollectionList = myBookCollectionList.stream().distinct().filter(m -> m.getCollectionName() != null).findAny();
            for (BookmarkCollection bookmarkCollection : myCollectionList) {
                log.info("bookmarkCollection={}", bookmarkCollection);
            }
            model.addAttribute("myCollectionList", myCollectionList);
        }

        Map<String, String> selectToUserIdCollectionName = new HashMap<>();
        selectToUserIdCollectionName.put("userId", loginMember.getUserId());
        selectToUserIdCollectionName.put("collectionName", collectionName);
        int existsByHistory = bookmarkHistoryOfMemberService.existsByHistory(selectToUserIdCollectionName);
        if(existsByHistory == 0){
            model.addAttribute("existsByHistory", existsByHistory);
            return "detail/collectionList";
        }
        // 해당 유저의 collectionName에 있는 book 리스트 가져오기
        List<BookmarkHistoryOfMember> bookmarkHistoryOfMemberList = bookmarkHistoryOfMemberService.findByUserId(loginMember.getUserId());
        List<BookmarkHistoryOfMember> bookmarkList = bookmarkHistoryOfMemberList.stream()
                .filter(b -> b.getCollectionName().equals(collectionName))
                .collect(Collectors.toList());
        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("collectionName", collectionName);
        return "detail/collectionList";
    }

    @GetMapping("/editCollectionListForm/{collectionName}")
    public String editCollectionListForm(@PathVariable String collectionName,
                                     @SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember,
                                     Model model){

        // 해당 유저의 collectionName에 있는 book 리스트 가져오기
        List<BookmarkHistoryOfMember> bookmarkHistoryOfMemberList = bookmarkHistoryOfMemberService.findByUserId(loginMember.getUserId());
        List<BookmarkHistoryOfMember> bookmarkList = bookmarkHistoryOfMemberList.stream()
                .filter(b -> b.getCollectionName().equals(collectionName))
                .collect(Collectors.toList());
        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("collectionName", collectionName);
        return "detail/editCollectionList";
    }

    @PostMapping("/editCollectionListForm/{collectionName}")
    public String editCollectionList(@PathVariable String collectionName,
                                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember,
                                         Model model,
                                     @RequestParam(value = "cb") List<Long> checkboxList,
                                     RedirectAttributes redirectAttributes){
        // 컬렉션에서 삭제 하기 위해 컬렉션 레코드에 값 "-"로 변경
        Map<String, String> updateCollectionName = new HashMap<>();
        for (Long checkId : checkboxList) {
            log.info("editCheckId={}", checkId);
            updateCollectionName.put("checkId", checkId+"");
            updateCollectionName.put("collectionName", "-");
            bookmarkHistoryOfMemberService.updateCollectionName(updateCollectionName);
        }

        return "redirect:/editCollectionListForm/{collectionName}";
    }
}
