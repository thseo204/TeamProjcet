package project.bookservice.web.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class JoinController {

    @GetMapping("/joinForm")
    public String loginForm(){
        return "/basic/joinForm";
    }
}
