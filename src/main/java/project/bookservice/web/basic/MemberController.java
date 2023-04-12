package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.member.BookmarkCollection;
import project.bookservice.domain.member.BookmarkHistoryOfMember;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.member.ReportInfoHistoryOfMember;
import project.bookservice.domain.report.ReportInfo;
import project.bookservice.service.historyOfReportInfo.HistoryOfReportInfoService;
import project.bookservice.service.mail.MailService;
import project.bookservice.service.member.BookmarkCollectionService;
import project.bookservice.service.member.BookmarkHistoryOfMemberService;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.member.ReportInfoHistoryOfMemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.web.SessionConst;
import project.bookservice.web.validation.SignUpFormValidator;
import project.bookservice.web.validation.form.BookmarkCollectionSaveForm;
import project.bookservice.web.validation.form.HistoryOfReportInfoSaveForm;
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
    private final SignUpFormValidator signUpFormValidator;
    private final ReportInfoService reportInfoService;
    private final BookmarkHistoryOfMemberService bookmarkHistoryOfMemberService;
    private final ReportInfoHistoryOfMemberService reportInfoHistoryOfMemberService;
    private final BookmarkCollectionService bookmarkCollectionService;
    private final MailService mailService;
    private final HistoryOfReportInfoService historyOfReportInfoService;
    private String ePw; // 이메일 인증 코드
    private boolean availableId, availableEmail, emailCodeCheck;

    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute Member member, Model model){

        log.info("availableId={}", availableId);
        log.info("availableEmail={}",  availableEmail);
        log.info("emailCodeCheck={}", emailCodeCheck);
        model.addAttribute("availableId", availableId);
        model.addAttribute("availableEmail", availableEmail);
        model.addAttribute("emailCodeCheck", emailCodeCheck);

        return "/basic/joinForm";
    }


    @GetMapping("/joinForm/idCheck")
    public String idCheck(@Valid @ModelAttribute("member") Member member,
                          BindingResult bindingResult, Model model){

        log.info("member.getUserId()={}", member.getUserId());

        //아이디 중복 검사
        signUpFormValidator.idCheckValidate(member.getUserId(), bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            availableId = false; //아이디 중복검사 실패 시 false
            model.addAttribute("availableId", availableId);

            return "/basic/joinForm";
        }
        availableId = true; //아이디 중복검사 성공 시 true
        model.addAttribute("availableId", availableId);

//        return "redirect:/joinForm";
        return "/basic/joinForm";
    }

    //이메일 인증
    @GetMapping("joinForm/mail")
//    @ResponseBody
    String mailConfirm(@RequestParam("userEmail") String userEmail,
                       @RequestParam("userId") String userId,
                       @RequestParam("userPwd") String userPwd,
                       @RequestParam("userRePwd") String userRePwd,
                       @RequestParam("userPhone") String userPhone,
                       @RequestParam("userBirth") String userBirth,
//                       @RequestParam("userGender") String userGender,
                       @RequestParam("userName") String userName,
                       @ModelAttribute Member member,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) throws Exception {
        log.info("전달 받은 이메일 = {}", userEmail);
        // 이메일에 인증코드를 보내기 전 이메일 검증(중복 or null)
        member.setUserEmail(userEmail);
        signUpFormValidator.emailCheckValidate(member.getUserEmail(), bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);

            return "/basic/joinForm";
        }

        ePw = mailService.sendSimpleMessage(userEmail);
        log.info("인증코드={}", ePw);
        log.info("userId={}", userId);

        availableEmail = true; // 이메일 전송 성공 시
        model.addAttribute("availableEmail", availableEmail);

        redirectAttributes.addAttribute("userId", userId);
        redirectAttributes.addAttribute("userPwd", userPwd);
        redirectAttributes.addAttribute("userRePwd", userRePwd);
        redirectAttributes.addAttribute("userPhone", userPhone);
        redirectAttributes.addAttribute("userBirth", userBirth);
//        redirectAttributes.addAttribute("userGender", userGender);
        redirectAttributes.addAttribute("userName", userName);
        redirectAttributes.addAttribute("userEmail", userEmail);

        return "redirect:/joinForm";
    }

    @GetMapping("/joinForm/emailCode")
    public String emailCheck(@Valid @ModelAttribute("member") Member member,
                             BindingResult bindingResult,
                             @RequestParam("emailCode") String emailCode,
                             @RequestParam("userEmail") String userEmail,
                             @RequestParam("userId") String userId,
                             @RequestParam("userPwd") String userPwd,
                             @RequestParam("userRePwd") String userRePwd,
                             @RequestParam("userPhone") String userPhone,
                             @RequestParam("userBirth") String userBirth,
//                       @RequestParam("userGender") String userGender,
                             @RequestParam("userName") String userName,
                             Model model,
                             RedirectAttributes redirectAttributes){


        log.info("member.getEmailCode()={}", member.getEmailCode());
        model.addAttribute("member", member);
        redirectAttributes.addAttribute("userId", userId);
        redirectAttributes.addAttribute("userPwd", userPwd);
        redirectAttributes.addAttribute("userRePwd", userRePwd);
        redirectAttributes.addAttribute("userPhone", userPhone);
        redirectAttributes.addAttribute("userBirth", userBirth);
//        redirectAttributes.addAttribute("userGender", userGender);
        redirectAttributes.addAttribute("userName", userName);
        redirectAttributes.addAttribute("userEmail", userEmail);
        redirectAttributes.addAttribute("emailCode", emailCode);


        //이메일 인증코드 일치 검사
        signUpFormValidator.emailCodeCheckValidate(ePw, member.getEmailCode(), bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            emailCodeCheck = false; // 이메일 코드 불일치 시 false
            model.addAttribute("emailCodeCheck", emailCodeCheck);
            availableEmail = true; // 이메일 전송은 이미 성공 했으니
            model.addAttribute("availableEmail", availableEmail);
//            return "redirect:/joinForm";
            return "/basic/joinForm";
        }
        emailCodeCheck = true; // 이메일 코드 일치 시
        model.addAttribute("emailCodeCheck", emailCodeCheck);
//        redirectAttributes.addAttribute("userId", member.getUserId());

        return "redirect:/joinForm";
//        return "/basic/joinForm";
    }


    @PostMapping("/joinForm")
    public String signUpSubmit(@Valid @ModelAttribute("member") SignUpForm signUpForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        log.info("signUpForm ={}", signUpForm);
        availableEmail = true; // 이메일 전송 성공 시
        model.addAttribute("availableEmail", availableEmail);


        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);

            return "/basic/joinForm";
        }

        //이메일 중복 검사, 비빌버호 일치 검증
        signUpFormValidator.validate(signUpForm, bindingResult);

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "/basic/joinForm";
        }

        if(availableId == false || emailCodeCheck == false){
            bindingResult.reject("joinFalse", "아이디 중복확인 혹은 이메일 인증을 해주세요.");
        }
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "basic/joinForm";
        }

        //기본 아이콘 지급
        signUpForm.setUserCharIcon("/images/icons/F.png");

        //회원 정보 등록
        memberService.save(signUpForm);
        // 새 회원에 모든 레포트 히스토리 기본값으로 넣어주기!
        List<ReportInfo> reportInfoAllList = reportInfoService.findAll();
        for (ReportInfo reportInfo : reportInfoAllList) {
            HistoryOfReportInfoSaveForm form = new HistoryOfReportInfoSaveForm();
            form.setReportId(reportInfo.getId());
            form.setUserId(signUpForm.getUserId());
            historyOfReportInfoService.save(form);
        }
        availableId = false;
        availableEmail = false;
        emailCodeCheck = false;
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
                                    Member loginMember, Model model) {

        int existsByHistory = 0;
        if (bookAndReport == true) {

            int existsByMyHistory = bookmarkCollectionService.existsCollection(loginMember.getUserId());
            if(existsByMyHistory > 0){
                List<BookmarkCollection> myCollectionList = bookmarkCollectionService.collectionList(loginMember.getUserId());
                for (BookmarkCollection bookmarkCollection : myCollectionList) {
                    log.info("bookmarkCollection={}", bookmarkCollection);
                }
                model.addAttribute("myCollectionList", myCollectionList);
            }
            existsByHistory = bookmarkHistoryOfMemberService.existsByHistory(loginMember.getUserId());
            if (existsByHistory > 0) {
                List<BookmarkHistoryOfMember> bookmarkList = bookmarkHistoryOfMemberService.findByUserIdDistinctIsbn(loginMember.getUserId());

                model.addAttribute("bookmarkList", bookmarkList);
            }
            model.addAttribute("existsByMyHistory", existsByMyHistory);
            model.addAttribute("existsByHistory", existsByHistory);
            model.addAttribute("bookAndReport", true);
            return "detail/myBookmark";

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
    }

    @PostMapping("/myBookmarkForm/true/{collectionName}")
    public String addBookInCollection(@PathVariable String collectionName,
                                      @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                                      @RequestParam(value = "cb") List<Long> checkboxList,
                                      RedirectAttributes redirectAttributes) {

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

    @GetMapping("/agreementForm")
    public String agreementForm(){
        return "/basic/agreement";
    }
}

