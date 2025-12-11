package com.terracetech.tims.webmail.common.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LogoManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.vo.CommonLogoVO;
import com.terracetech.tims.webmail.common.vo.LogoVO;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.vo.MailDomainVO;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.BrowserUtil;
import com.terracetech.tims.webmail.util.CookieUtils;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * Welcome Controller
 * Struts2 WelcomeAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 로그인 전/후 분기 처리
 *    - user == null: 로그인 페이지 표시
 *    - user != null: 메인 페이지로 이동
 * 
 * 2. 모바일/PC 분기 처리
 *    - BrowserUtil.isMoblieBrowser(): User-Agent 분석
 *    - mailMode 세션/쿠키 확인
 * 
 * 3. 로그인 페이지 표시 (user == null)
 *    3-1. 도메인 설정 조회
 *        - single/multi 도메인 모드
 *        - domain_input_type (select/input)
 *    3-2. 로고 정보 조회
 *        - LogoManager.readCommonLogoList()
 *    3-3. RSA 암호화 키 생성 (선택적)
 *        - RSA 1024bit 키 쌍 생성
 *        - 세션에 PrivateKey 저장
 *        - 공개키 정보를 JSP에 전달
 *    3-4. ASP 서비스 처리
 *        - 도메인별 ASP 로그인 페이지
 *    3-5. 커스텀 로그인 페이지 처리
 *        - systemManager.getLoginPage()
 * 
 * 4. 메인 페이지 이동 (user != null)
 *    4-1. 사용자 설정 조회
 *        - UserEtcInfoVO: renderMode, afterLogin
 *    4-2. 메뉴 설정 조회
 *        - topMenuUse, leftMenuUse, autoForwardMenu
 *    4-3. afterLogin 설정에 따른 분기
 *        - "intro", "domain", "home", "mail", "manage" 등
 * 
 * 재사용 Manager:
 * - MailUserManager: 도메인 정보 조회
 * - SettingManager: 사용자 설정 조회
 * - SystemConfigManager: 시스템 설정 조회
 * - LogoManager: 로고 정보 조회
 */
@Controller("welcomeController")
public class WelcomeController {
    
    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    @Autowired
    private MailUserManager mailUserManager;
    
    @Autowired
    private SettingManager userSettingManager;
    
    @Autowired
    private SystemConfigManager systemManager;
    
    @Autowired
    private LogoManager logoManager;
    
    @Autowired
    private ServletContext servletContext;
    
    /**
     * Welcome 페이지 처리 메인 메서드
     * URL: /common/welcome.do
     * 
     * @return 로그인 전: "login", "mobileLogin", "aspLogin", "redirect", "forward"
     *         로그인 후: "intro", "domain", "home", "mail", "manage", "mobileIntro"
     *                   또는 "s" + 위의 값 (HTML 모드)
     */
    public String load(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        LogManager.printElapsedTime("welcome", "WelcomeController START");
        
        // 1. 파라미터 추출
        String forward = "main";
        String timeout = request.getParameter("timeout");
        String stime = request.getParameter("stime");
        String debugUse = request.getParameter("debug");
        boolean isDebugUse = "enable".equals(debugUse);
        timeout = (timeout != null) ? timeout : "off";
        
        String installLocale = EnvConstants.getBasicSetting("setup.state");
        
        // 2. 모바일/PC 분기 판단
        String agent = request.getHeader("user-agent");
        String mailMode = getMailMode(request, session);
        String mobileMailUse = systemManager.getMobileMailConfig();
        boolean isMobileMailUse = "enabled".equals(mobileMailUse);
        boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
        
        LogManager.printElapsedTime("welcome", "WelcomeController Cookie Check");
        
        // 3. 사용자 인증 확인
        User user = (User) session.getAttribute(User.class.getName());
        
        if (user == null) {
            // 3-1. 로그인 전 처리
            forward = handleNotLoggedIn(request, session, model, isMobileMailUse, isMobile, 
                                       mailMode, timeout, stime, installLocale);
        } else {
            // 3-2. 로그인 후 처리
            forward = handleLoggedIn(request, session, model, user, isMobileMailUse, 
                                    isMobile, mailMode, isDebugUse);
        }
        
        // 4. 공통 처리
        session.removeAttribute("dormantUser");
        log.debug("WelcomeController.forward: {}", forward);
        LogManager.printElapsedTime("welcome", "WelcomeController END");
        
        return forward;
    }
    
    /**
     * 로그인 전 처리
     */
    private String handleNotLoggedIn(HttpServletRequest request, HttpSession session,
                                     Model model, boolean isMobileMailUse, boolean isMobile,
                                     String mailMode, String timeout, String stime,
                                     String installLocale) throws Exception {
        
        LogManager.printElapsedTime("welcome", "WelcomeController.checkUser1");
        
        String defaultDomain = mailUserManager.readDefaultDomain();
        
        // 1. 모바일 로그인 페이지
        if (isMobileMailUse && 
            ("mobile".equalsIgnoreCase(mailMode) || 
             (isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
            
            Map<String, String> paramMap = createParamMap(request);
            paramMap.put("logoType", "mobile");
            Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);
            
            model.addAttribute("installLocale", installLocale);
            model.addAttribute("commonLogoList", commonLogoList);
            return "mobileLogin";
        }
        
        // 2. PC 로그인 페이지
        Map<String, String> loginConfigList = systemManager.getLoginConfig();
        String domainType = loginConfigList.get("domain_type");
        String domainInputType = loginConfigList.get("domain_input_type");
        
        int defaultDomainSeq = mailUserManager.searchMailDomainSeq(defaultDomain);
        String noticeContent = mailUserManager.getNoticeContent(defaultDomainSeq);
        
        log.debug("WelcomeController.domainType: {}", domainType);
        log.debug("WelcomeController.domainInputType: {}", domainInputType);
        
        // 2-1. 도메인 목록 설정
        Map<String, Integer> domainSeqMap = systemManager.getSsnNotUseDomainSeqMap();
        List<MailDomainVO> domainList = null;
        
        if ("single".equalsIgnoreCase(domainType)) {
            if (domainSeqMap != null && domainSeqMap.containsValue(defaultDomainSeq)) {
                model.addAttribute("ssnNotUse", true);
            }
            model.addAttribute("selectDomainName", defaultDomain);
        } else {
            if (!"input".equalsIgnoreCase(domainInputType)) {
                domainList = mailUserManager.readMailDomainList();
                if (domainSeqMap != null) {
                    int count = 0;
                    for (MailDomainVO vo : domainList) {
                        if (domainSeqMap.containsValue(vo.getMailDomainSeq())) {
                            count++;
                        }
                    }
                    if (count == domainList.size()) {
                        model.addAttribute("ssnNotUse", true);
                    }
                }
                model.addAttribute("domainList", domainList);
            }
        }
        
        // 2-2. 로고 정보 조회
        Map<String, String> paramMap = createParamMap(request);
        Map<String, CommonLogoVO> commonLogoList = logoManager.readCommonLogoList(paramMap);
        
        model.addAttribute("commonLogoList", commonLogoList);
        model.addAttribute("noticeContent", noticeContent);
        model.addAttribute("loginConfigList", loginConfigList);
        
        // 2-3. 커스텀 로그인 페이지 처리
        String loginPage = systemManager.getLoginPage(request.getServerName());
        String forward;
        
        if (StringUtils.isNotEmpty(loginPage)) {
            model.addAttribute("url", loginPage);
            forward = loginPage.startsWith("http") ? "redirect" : "forward";
        } 
        // 2-4. ASP 서비스 로그인 페이지
        else if (isAspLoginPage()) {
            forward = handleAspLogin(request, model, domainList, defaultDomain, 
                                    defaultDomainSeq, paramMap);
        } 
        // 2-5. 기본 로그인 페이지
        else {
            forward = "login";
        }
        
        // 2-6. 공통 속성 설정
        model.addAttribute("timeout", timeout);
        model.addAttribute("sessionTime", stime);
        model.addAttribute("mobileMailUse", isMobileMailUse);
        model.addAttribute("defaultDomain", defaultDomain);
        model.addAttribute("installLocale", installLocale);
        
        // 2-7. RSA 암호화 키 생성
        setupRsaEncryption(session, model);
        
        return forward;
    }
    
    /**
     * 로그인 후 처리
     */
    private String handleLoggedIn(HttpServletRequest request, HttpSession session,
                                  Model model, User user, boolean isMobileMailUse,
                                  boolean isMobile, String mailMode, boolean isDebugUse) 
                                  throws Exception {
        
        LogManager.printElapsedTime("welcome", "WelcomeController.checkUser2");
        
        // 1. 모바일 인트로
        if (isMobileMailUse && 
            ("mobile".equalsIgnoreCase(mailMode) || 
             (isMobile && !"pc".equalsIgnoreCase(mailMode)))) {
            return "mobileIntro";
        }
        
        // 2. PC 메인 페이지
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        Map<String, String> menuMap = systemManager.getTopAndLeftMenuUse(mailDomainSeq);
        
        // 2-1. 메뉴 설정
        session.setAttribute("autoForwardMenu", menuMap.get("autoForwardMenu"));
        
        Object ssoAuth = session.getAttribute("SSO_AUTH");
        if (ssoAuth != null && "T".equals((String) ssoAuth)) {
            session.setAttribute("topMenuUse", menuMap.get("ssotopmenu"));
            session.removeAttribute("SSO_AUTH");
        } else {
            session.setAttribute("topMenuUse", menuMap.get("nortopmenu"));
        }
        
        session.setAttribute("leftMenuUse", menuMap.get("leftmenu"));
        session.setAttribute("debugUse", isDebugUse ? "enable" : "disable");
        
        // 2-2. 사용자 설정 조회
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
        log.debug("WelcomeController.userSettingVo: {}", userSettingVo);
        
        if (userSettingVo == null) {
            return "intro";
        }
        
        // 2-3. afterLogin 설정에 따른 분기
        boolean isHtmlMode = "html".equals(userSettingVo.getRenderMode());
        String afterLogin = userSettingVo.getAfterLogin();
        log.debug("WelcomeController.afterLogin: {}", afterLogin);
        
        String forward;
        if (afterLogin != null) {
            forward = afterLogin.toLowerCase();
            if (StringUtils.isEmpty(forward)) {
                forward = "domain";
            }
        } else {
            forward = "intro";
        }
        
        // 2-4. HTML 모드 처리
        if (isHtmlMode) {
            forward = "s" + forward;
        }
        
        return forward;
    }
    
    /**
     * Mail 모드 가져오기 (모바일/PC)
     */
    private String getMailMode(HttpServletRequest request, HttpSession session) {
        boolean isMobile = BrowserUtil.isMoblieBrowser(request.getHeader("user-agent"));
        String mailMode = null;
        
        if (isMobile) {
            Object object = session.getAttribute("mailMode");
            if (object != null) {
                mailMode = (String) object;
            }
        } else {
            CookieUtils cookieUtil = new CookieUtils(request);
            mailMode = cookieUtil.getValue("TSMODE");
        }
        
        return mailMode;
    }
    
    /**
     * 파라미터 맵 생성
     */
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("attachPath", servletContext.getRealPath("/") + 
                    EnvConstants.getAttachSetting("attach.dir"));
        paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
        paramMap.put("localUrl", request.getScheme() + "://" + 
                    request.getServerName() + ":" + request.getServerPort());
        return paramMap;
    }
    
    /**
     * ASP 로그인 페이지 사용 여부
     */
    private boolean isAspLoginPage() {
        String asp = systemManager.getAspService("asp");
        String aspLoginPage = systemManager.getAspLoginPage("asp_login_page");
        return "enabled".equalsIgnoreCase(asp) && "on".equalsIgnoreCase(aspLoginPage);
    }
    
    /**
     * ASP 로그인 처리
     */
    private String handleAspLogin(HttpServletRequest request, Model model,
                                 List<MailDomainVO> domainList, String defaultDomain,
                                 int defaultDomainSeq, Map<String, String> paramMap) 
                                 throws Exception {
        
        String requestServerName = request.getServerName();
        String mailDomain = null;
        int mailDomainSeq = 0;
        
        // 1. 도메인 목록에서 매칭되는 도메인 찾기
        if (domainList != null) {
            for (MailDomainVO vo : domainList) {
                String domainUrl = vo.getUrlAddress();
                if (domainUrl != null && !domainUrl.trim().isEmpty()) {
                    if (requestServerName.equalsIgnoreCase(domainUrl)) {
                        mailDomain = vo.getMailDomain();
                        mailDomainSeq = vo.getMailDomainSeq();
                        break;
                    }
                }
            }
        }
        
        // 2. 도메인이 없으면 서버 이름에서 추출
        if (mailDomain == null) {
            mailDomain = extractDomainFromServerName(requestServerName, defaultDomain);
            mailDomainSeq = mailUserManager.searchMailDomainSeq(mailDomain);
        }
        
        // 3. 로고 정보 조회
        LogoVO logoVO = null;
        if (mailDomainSeq > 0) {
            logoVO = logoManager.getLogoInfo(mailDomainSeq, paramMap);
        } else {
            logoVO = logoManager.getLogoInfo(defaultDomainSeq, paramMap);
        }
        
        model.addAttribute("logoVO", logoVO);
        model.addAttribute("mailDomain", mailDomain);
        
        return "aspLogin";
    }
    
    /**
     * 서버 이름에서 도메인 추출
     */
    private String extractDomainFromServerName(String serverName, String defaultDomain) {
        if (serverName == null) {
            return defaultDomain;
        }
        
        int pointCount = StringUtils.getPointCount(serverName);
        
        switch (pointCount) {
            case 0:
                return defaultDomain;
            case 1:
                return serverName;
            case 2:
            case 3:
                return serverName.substring(serverName.indexOf(".") + 1);
            case 4:
                String domain = serverName.substring(serverName.indexOf(".") + 1);
                return domain.substring(domain.indexOf(".") + 1);
            case 5:
                domain = serverName.substring(serverName.indexOf(".") + 1);
                domain = domain.substring(domain.indexOf(".") + 1);
                return domain.substring(domain.indexOf(".") + 1);
            case 6:
                domain = serverName.substring(serverName.indexOf(".") + 1);
                domain = domain.substring(domain.indexOf(".") + 1);
                domain = domain.substring(domain.indexOf(".") + 1);
                return domain.substring(domain.indexOf(".") + 1);
            default:
                return defaultDomain;
        }
    }
    
    /**
     * RSA 암호화 설정
     */
    private void setupRsaEncryption(HttpSession session, Model model) throws Exception {
        boolean loginParamterRSAUse = "true".equalsIgnoreCase(
            EnvConstants.getMailSetting("login.rsa.encrypt.use"));
        
        model.addAttribute("loginParamterRSAUse", String.valueOf(loginParamterRSAUse));
        
        if (loginParamterRSAUse) {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            
            // 세션에 개인키 저장
            session.setAttribute("__rsaPrivateKey__", privateKey);
            
            // 공개키를 문자열로 변환하여 JSP에 전달
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(
                publicKey, RSAPublicKeySpec.class);
            
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
            
            model.addAttribute("publicKeyModulus", publicKeyModulus);
            model.addAttribute("publicKeyExponent", publicKeyExponent);
        }
    }
}

