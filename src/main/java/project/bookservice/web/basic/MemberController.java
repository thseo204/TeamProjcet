package project.bookservice.web.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.bookservice.domain.member.Member;
import project.bookservice.service.MemberService;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/joinForm")
    public String joinForm(){
        return "/basic/joinForm";
    }


    @PostMapping("/joinForm")
    public String joinMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        Member savedMember = memberService.save(member);
        redirectAttributes.addAttribute("memberId", savedMember.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/main/{memberId}";
//        return "redirect:/items/{itemId}";
    }
}
