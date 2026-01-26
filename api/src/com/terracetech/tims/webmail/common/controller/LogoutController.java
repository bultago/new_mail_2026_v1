package com.terracetech.tims.webmail.common.controller;

import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;

/**
 * Logout Controller
 * Struts2 LogoutAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 세션 정리
 *    - UserAuthManager.removeSessionTime(): 세션 타임아웃 정보 제거
 *    - session.removeAttribute("dormantUser"): 휴면 사용자 정보 제거
 * 
 * 2. 쿠키 삭제
 *    - 메인 쿠키 (cookie.name)
 *    - 로고 쿠키 (cookie.name + "Logo")
 * 
 * 3. 로그아웃 후 이동 경로 결정
 *    - UserAuthManager.afterLogoutPath(): 도메인별 로그아웃 페이지 조회
 * 
 * 4. 로그아웃 로그 기록
 *    - writeMailLog(): 로그아웃 이력 기록
 * 
 * 5. JSP에 전달할 속성 설정
 *    - cookieDomain, cookieNameObj: 클라이언트 쿠키 삭제용
 *    - timeout, stime: 세션 타임아웃 정보
 *    - logoutPath: 로그아웃 후 이동할 경로
 *    - language: 다국어 설정
 * 
 * 재사용 Manager:
 * - UserAuthManager: 세션/쿠키 관리, 로그아웃 경로 조회
 */
@Controller("logoutController")
public class LogoutController {
    
    @Autowired
    private UserAuthManager userAuthManager;
    
    /**
     * 로그아웃 처리 메인 메서드
     * URL: /common/logout.do, /logout.do
     * 
     * @return "success" → /common/logout.jsp
     */
    public String logout(HttpServletRequest request, HttpServletResponse response,
                        HttpSession session, Model model) throws Exception {
        
        // 1. 파라미터 추출
        String language = request.getParameter("language");
        String timeout = request.getParameter("timeout");
        String stime = request.getParameter("stime");
        
        language = (language == null) ? "ko" : language;
        timeout = (timeout != null) ? timeout : "off";
        
        // 2. 쿠키 삭제 목록 준비
        Set<String> cookieKeys = new HashSet<>(3);
        String mainCookie = EnvConstants.getBasicSetting("cookie.name");
        String logoCookie = EnvConstants.getBasicSetting("cookie.name") + "Logo";
        cookieKeys.add(mainCookie);
        cookieKeys.add(logoCookie);
        
        // 3. 세션 정리 (Heap에서 session clear를 위해)
        UserAuthManager.removeSessionTime(request, response);
        userAuthManager.deleteCookie(cookieKeys, request, response);
        
        // 4. 로그아웃 경로 조회
        User user = (User) session.getAttribute(User.class.getName());
        String logoutPath = "/";
        
        if (user != null) {
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            logoutPath = userAuthManager.afterLogoutPath(mailDomainSeq);
            
            // 5. 로그아웃 로그 기록
            LogManager.writeMailLogMsg(true, 
                org.slf4j.LoggerFactory.getLogger(LogoutController.class),
                user.get(User.EMAIL),
                request.getRemoteAddr(),
                "logout");
        }
        
        // 6. JSP에 전달할 속성 설정
        model.addAttribute("cookieDomain", EnvConstants.getBasicSetting("cookie.domain"));
        model.addAttribute("cookieNameObj", "{cookies:['" + mainCookie + "','" + logoCookie + "']}");
        model.addAttribute("timeout", timeout);
        model.addAttribute("stime", stime);
        model.addAttribute("logoutPath", logoutPath);
        model.addAttribute("language", language);
        
        // 7. 세션에서 휴면 사용자 정보 제거
        session.removeAttribute("dormantUser");
        
        return "logoutSuccess";
    }
}

