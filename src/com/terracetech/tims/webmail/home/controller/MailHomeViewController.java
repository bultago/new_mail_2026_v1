package com.terracetech.tims.webmail.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.home.manager.MailHomeManager;
import com.terracetech.tims.webmail.home.vo.MailHomePortletVO;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * Mail Home View Controller
 * Struts2 MailHomeViewAction을 Spring MVC로 전환
 */
@Controller("mailHomeViewController")
public class MailHomeViewController {
    
    @Autowired
    private MailHomeManager manager;
    
    public String view(HttpSession session, HttpServletRequest request, Model model) throws Exception {
        
        // 세션에서 User 객체 가져오기
        User user = (User) session.getAttribute(User.class.getName());
        if (user == null) {
            return "redirect:/login.do";
        }
        
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        String forward = "home";
        String afterLogin = manager.readAfterLogin(userSeq);
        
        // 로그인 시간 정보 설정
        model.addAttribute("loginTimeInfo", user.get(User.WEBMAIL_LOGIN_TIME));
        
        if (afterLogin == null) {
            return "intro";
        }
        
        if (afterLogin.indexOf("intro") > -1) {
            return "intro";
        }
        
        // 포틀릿 레이아웃 조회
        Map<String, MailHomePortletVO> customMap = new HashMap<>();
        List<MailHomePortletVO> list = manager.readLayoutPortlet(userSeq);
        for (int i = 0; i < list.size(); i++) {
            MailHomePortletVO vo = list.get(i);
            customMap.put("portlet" + vo.getLocation(), vo);
        }
        
        if (customMap.size() == 0) {
            forward = "intro";
        } else {
            forward = "home";
        }
        
        model.addAttribute("customMap", customMap);
        
        return forward;
    }
}

