package project.bookservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.bookservice.web.interceptor.LogInterceptor;
import project.bookservice.web.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // 인터셉터 등록
                .order(1) // 인터넷터의 호출순서 지정. 낮을수록 먼저 호출됨.
                .addPathPatterns("/**") // 인터셉터를 적용할 URL 패턴 지정
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/assets/**", "/image/**","/images/**"); // 인터셉터에서 제외할 패턴을 지정

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                  "/", "/main", "/main/**", "/joinForm/**", "/findId/**", "/findPwdByEmail/**", "/findIdByEmail/**","/searchBookList/**","/BookList/**", "/loginForm",
                        "/book/**", "/bookReportForm/*", "/feedListForm", "/assets/**", "/image/**", "/callback/naver/**",
                        "/searchKeyword/*", "/error" ,"/agreementForm", "/joinSuccessForm", "/callback/**", "/error-page/**",
                        "/NoSearchWord/**","/logout","/emailAuthentication/**","/findIdByEmailAuthentication/**"
                );
    }
}
