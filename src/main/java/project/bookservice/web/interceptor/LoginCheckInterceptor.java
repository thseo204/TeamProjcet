package project.bookservice.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.bookservice.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 로그인 인증은 컨트롤러 호출 전에만 호출되면 됨. 따라서 preHandle 만 구현
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/error-page/404");
            return  false;
        }
        return true;
    }
}
