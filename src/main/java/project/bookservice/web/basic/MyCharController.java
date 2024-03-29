package project.bookservice.web.basic;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.bookservice.domain.comment.Comment;
import project.bookservice.domain.member.Member;
import project.bookservice.service.comment.CommentService;
import project.bookservice.service.member.MemberService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.web.SessionConst;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyCharController {
    private final ReportInfoService reportInfoService;
    private final MemberService memberService;
    private final CommentService commentService;
    @GetMapping("/myChar")
    public String myChar(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){

        Integer reportCount = reportInfoService.countByWriterId(loginMember.getUserId());

        model.addAttribute("member",loginMember);
        model.addAttribute("reportCount",reportCount);

        return "basic/myChar";
    }

    @GetMapping("/editCharIcon/{writerID}")
    public String editCharIcon(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, @RequestParam(name = "imagePath") String imagePath){



        loginMember.setUserCharIcon(imagePath);
        
        memberService.editCharIcon(loginMember);

        
        Comment comment = new Comment(loginMember.getUserId(),loginMember.getUserCharIcon());

        
        commentService.updateCharIcon(comment);

        log.info("comment={}",comment);

        log.info("editCharIcon = {}", loginMember);

        return "basic/editIconSuccessForm";
    }

}
