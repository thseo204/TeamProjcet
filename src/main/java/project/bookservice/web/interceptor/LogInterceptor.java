package project.bookservice.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        String uuid = UUID.randomUUID().toString(); // 요청 로그를 구분하기 위한 uuid 생성
        request.setAttribute(LOG_ID, uuid); // preHandle 에서 생성한 uuid 는 postHandle , afterCompletion 에서도 사용.
        // 싱글톤처럼 사용되기 때문에 멤버변수로 사용하면 위험. 따라서 request 에 담아두고 afterCompletion 에서 getAttribute(LOG_ID)로 찾아서 사용.

        //@RequestMapping : HandlerMethod -> 스프링을 사용하면 일반적으로 @Controller, @RequestMapping을 활용한 매핑을 사용. 이경우 핸들러 정보로 HandlerMethod 가 넘어온다.
        //정적 리소스 : ResourceHttpRequestHandler -> @Controller 가 아니라 /resources/static 과 같은 정적 리소스가 호출되는 경우 ResourceHttpRequestHandler 가 핸들러 정보로 넘어오기때문에 타입에 따라 처리가 필요함.
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // false 일 경우 진행 안함. true 는 정상호출로 다음 인터셉터나 컨트롤러가 호출됨
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle[{}]", modelAndView);
    }

    // 종료 로그는 postHandle 가 아니라 afterCompletion 에서 실행한 이유는 예외가 발생한 경우 postHandle 가 호출되지 않기때문
    // afterCompletion 는 예외가 발생해도 호출 되는 것을 보장함.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE[{}][{}]", logId, requestURI);
        if(ex != null){
            log.error("afterCompletion error!!", ex);
        }
    }
}
