package com.terracetech.tims.webmail.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.terracetech.tims.webmail.mailuser.User;

/**
 * Spring MVC 인증 인터셉터
 * Struts2 BaseAction의 인증 로직을 대체
 */
public class AuthInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        HttpSession session = request.getSession(false);
        
        // 세션이 없거나 로그인 정보가 없는 경우
        if (session == null || session.getAttribute(User.class.getName()) == null) {
            logger.debug("인증되지 않은 요청: {}", request.getRequestURI());
            
            // AJAX 요청인 경우
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"Session expired\"}");
                return false;
            }
            
            // 일반 요청인 경우 로그인 페이지로 리다이렉트
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/login.do");
            return false;
        }
        
        // 인증 성공
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 요청 처리 후 로직 (필요시 구현)
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, 
            Exception ex) throws Exception {
        // 완료 후 로직 (필요시 구현)
    }
}

