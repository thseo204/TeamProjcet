package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookservice.domain.comment.Comment;
import project.bookservice.domain.member.Member;
import project.bookservice.service.comment.CommentService;
import project.bookservice.service.report.ReportInfoService;
import project.bookservice.web.SessionConst;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReportInfoService reportInfoService;

    @GetMapping("/deleteComment/{isbn}/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return "redirect:/book/{isbn}";
    }

    @PostMapping("/updateComment/{isbn}/{id}")
    public String updateComment(@ModelAttribute Comment comment) {
        commentService.update(comment);
        return "redirect:/book/{isbn}";
    }

    @PostMapping("/book/{isbn}")
    public String insertComment(@ModelAttribute Comment comment, @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model){
        Comment savedComment = commentService.save(comment);
        Integer reportCount = reportInfoService.countByWriterId(loginMember.getUserId());


        model.addAttribute("reportCount",reportCount);
        return "redirect:/book/{isbn}";
    }
}
