package project.bookservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.bookservice.domain.book.Book;
import project.bookservice.domain.member.Member;
import project.bookservice.openapi.APIParser;
import project.bookservice.openapi.ApiSearchBookList;
import project.bookservice.repository.member.MemberRepository;
import project.bookservice.web.argumentresolver.Login;
import project.bookservice.web.session.SessionManager;

import java.util.ArrayList;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,
            required = false) Member loginMember, Model model) throws ParseException {
        log.info("loginMember {}", loginMember);
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            String bookTitle = "Bestseller";
            APIParser apiParser = new ApiSearchBookList();
            ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);
            model.addAttribute("bookList", bookList);
            return "basic/main";
        }
        //세션이 유지되면 로그인으로 이동
        String bookTitle = "Bestseller";
        APIParser apiParser = new ApiSearchBookList();
        ArrayList<Book> bookList = apiParser.jsonAndXmlParserToArr(bookTitle);
        model.addAttribute("bookList", bookList);
        model.addAttribute("member", loginMember);
        return "basic/loginmain";
    }
}