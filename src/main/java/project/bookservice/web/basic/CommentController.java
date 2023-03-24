package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookservice.domain.comment.Comment;
import project.bookservice.service.comment.CommentService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

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
    public String insertComment(@ModelAttribute Comment comment){
        Comment savedComment = commentService.save(comment);
        return "redirect:/book/{isbn}";
    }
}
