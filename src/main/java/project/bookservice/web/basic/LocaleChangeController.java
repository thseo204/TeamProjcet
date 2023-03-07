package project.bookservice.web.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LocaleChangeController {
    private LocaleResolver localeResolver;

    @RequestMapping("/changeLanguage")
    public String change(@RequestParam("lang") String language, HttpServletRequest request, HttpServletResponse response){
        Locale locale = new Locale(language);
        localeResolver.setLocale(request, response, locale);
        return "redirect:/main";
    }

    public void setLocaleResolver(LocaleResolver localeResolver){
        this.localeResolver = localeResolver;
    }
}
