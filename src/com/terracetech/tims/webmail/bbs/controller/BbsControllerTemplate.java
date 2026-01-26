package com.terracetech.tims.webmail.bbs.controller;

/**
 * BBS Controller 템플릿
 * 
 * 공통 기능:
 * - BbsManager 사용
 * - 사용자 인증
 * - 권한 체크
 * - 리소스 관리
 */

import org.springframework.beans.factory.annotation.Autowired;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.util.SessionUtil;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.common.I18nResources;

// 템플릿 - 실제 사용 안함
public class BbsControllerTemplate {
    
    @Autowired
    protected BbsManager bbsManager;
    
    protected User getUser(jakarta.servlet.http.HttpServletRequest request) {
        return SessionUtil.getUser(request);
    }
    
    protected I18nResources getMessageResource(User user, String module) {
        return new I18nResources(user.get(User.LOCALE), module);
    }
}
