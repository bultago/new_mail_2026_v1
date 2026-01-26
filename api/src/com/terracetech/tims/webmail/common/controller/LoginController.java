package com.terracetech.tims.webmail.common.controller;

import java.io.PrintWriter;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.CheckUserExistManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mailuser.AuthUser;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.plugin.pki.PKIAuthParamBean;

/**
 * Login Controller
 * Struts2 LoginAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. RSA 암호화 로그인 처리 (선택적)
 *    - 세션에서 PrivateKey 가져오기
 *    - 암호화된 ID/PW 복호화
 * 
 * 2. 사용자 인증 처리
 *    - 일반 로그인: UserAuthManager.validateUser(session, id, pass, domain)
 *    - PKI 로그인: UserAuthManager.validateUser(pkiParamBean)
 * 
 * 3. 인증 결과에 따른 분기
 *    - 성공: 세션에 User 저장, 중복 로그인 체크, 로그 기록
 *    - 비밀번호 변경 필요: "change" 반환
 *    - 휴면 계정: "dormant" 반환
 *    - 실패: 에러 메시지 출력 후 null 반환
 * 
 * 재사용 Manager:
 * - UserAuthManager: 사용자 인증 및 로그인 프로세스
 * - SystemConfigManager: 세션 시간, 암호화 설정
 * - CheckUserExistManager: 중복 로그인 체크
 */
@Controller("loginController")
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String DEFAULT_RETURN_PATH = "/common/welcome.do";
    
    @Autowired
    private UserAuthManager userAuthManager;
    
    @Autowired
    private SystemConfigManager systemManager;
    
    @Autowired
    private CheckUserExistManager checkUserExistManager;
    
    /**
     * 로그인 처리 메인 메서드
     * URL: /common/login.do
     * 
     * @return "success" - 로그인 성공 → /common/welcome.do로 redirect
     *         "change" - 비밀번호 변경 필요 → 비밀번호 변경 페이지
     *         "dormant" - 휴면 계정 → 휴면 계정 안내 페이지
     *         null - 로그인 실패 → alert 출력 후 리턴
     */
    public String login(HttpServletRequest request, HttpServletResponse response, 
                       HttpSession session, Model model) throws Exception {
        
        LogManager.printElapsedTime("login", "LoginController START");
        
        // 1. 다국어 리소스 준비
        I18nResources resource = getMessageResource(request, "common");
        Locale locale = request.getLocale();
        if (locale != null) {
            String language = locale.getLanguage();
            if (language.indexOf("en") > -1) {
                locale = new Locale("");
            } else if (language.indexOf("ja") > -1) {
                locale = new Locale("jp");
            }
            resource.setChangeLocale(locale);
        }
        
        // 2. 로그인 파라미터 추출
        String loginMode = request.getParameter("loginMode");
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
        String domain = request.getParameter("domain");
        
        // 3. RSA 암호화 로그인 처리 (선택적)
        boolean loginParamterRSAUse = "true".equalsIgnoreCase(
            EnvConstants.getMailSetting("login.rsa.encrypt.use"));
        
        if (loginParamterRSAUse) {
            String[] decrypted = decryptLoginParams(request, response, session, resource);
            if (decrypted == null) {
                return null; // RSA 복호화 실패 시 이미 에러 출력됨
            }
            id = decrypted[0];
            pass = decrypted[1];
        }
        
        // 4. 환경 설정 준비
        String attachPath = request.getServletContext().getRealPath("/") + 
                           EnvConstants.getAttachSetting("attach.dir");
        String strLocalhost = request.getScheme() + "://" + 
                             request.getServerName() + ":" + 
                             request.getServerPort();
        
        id = id.toLowerCase();
        domain = domain.toLowerCase();
        
        log.debug("id: {}", (id != null) ? id : "");
        log.debug("domain: {}", (domain != null) ? domain : "");
        log.debug("attachPath: {}", attachPath);
        log.debug("strLocalhost: {}", strLocalhost);
        
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("attachPath", attachPath);
        paramMap.put("attachUrl", EnvConstants.getAttachSetting("attach.url"));
        paramMap.put("localUrl", strLocalhost);
        
        String sessionTime = systemManager.getSessionTimeConfig();
        session.setAttribute(EnvConstants.COOKIE_ALGORITHM_METHOD, 
                           systemManager.getCryptMethod());
        paramMap.put("cookieAlgorithm", systemManager.getCryptMethod());
        
        try {
            // 5. 사용자 인증 처리
            AuthUser auth = null;
            if ("pki".equalsIgnoreCase(loginMode)) {
                auth = userAuthManager.validateUser(getPKIParamBean(request));
            } else {
                auth = userAuthManager.validateUser(session, id, pass, domain);
            }
            LogManager.printElapsedTime("login", "userAuthManager.validateUser");
            
            // 6. 인증 결과 처리
            boolean isChangePass = (auth.getAuthResult() == UserAuthManager.PASSWORD_CHANGE);
            
            if (auth.isAuthSuccess() || isChangePass) {
                // 6-1. 인증 성공 처리
                User allowUser = auth.getUser();
                int domainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));
                String accoutStatus = allowUser.get(User.ACCOUNT_STATUS);
                String userName = allowUser.get(User.USER_NAME);
                
                model.addAttribute("mailDomainSeq", allowUser.get(User.MAIL_DOMAIN_SEQ));
                model.addAttribute("mailUserSeq", allowUser.get(User.MAIL_USER_SEQ));
                model.addAttribute("mailUid", allowUser.get(User.MAIL_UID));
                model.addAttribute("userName", userName);
                
                // 6-2. 비밀번호 변경 필요
                if (isChangePass) {
                    session.setAttribute("allowUser", allowUser);
                    return "change";
                }
                
                // 6-3. 휴면 계정 처리
                if ("dormant".equalsIgnoreCase(accoutStatus)) {
                    @SuppressWarnings("rawtypes")
                    Map dormantMap = userAuthManager.readDormantMonth(domainSeq);
                    if (dormantMap != null) {
                        session.setAttribute("dormantUser", auth);
                        model.addAttribute("manage", dormantMap.get("long_term_manage"));
                        model.addAttribute("month", dormantMap.get("long_term_month"));
                        model.addAttribute("changeMonth", dormantMap.get("long_term_change_month"));
                    }
                    return "dormant";
                }
                
                // 6-4. 정상 로그인 처리
                allowUser.put(User.SESSION_CHECK_TIME, sessionTime);
                
                // 중복 로그인 체크
                int mailDomainSeq = Integer.parseInt(allowUser.get(User.MAIL_DOMAIN_SEQ));
                String timestamp = checkUserExistManager.dupCheckProcess(
                    allowUser.get(User.EMAIL), mailDomainSeq);
                allowUser.put(User.LOGIN_TIMESTAMP, timestamp);
                
                // 로그인 프로세스 실행 (세션 저장 등)
                userAuthManager.doLoginProcess(request, response, allowUser, paramMap);
                boolean isFirstLogin = "F".equals(allowUser.get(User.FIRST_LOGIN));
                model.addAttribute("user", allowUser);
                
                // 로그인 로그 기록
                LogManager.writeMailLogMsg(true, log, allowUser.get(User.EMAIL),
                                         request.getRemoteAddr(), "login");
                
                // 최초 로그인 시 비밀번호 변경 체크
                if ((isFirstLogin && systemManager.isPasswordChange(domainSeq)) ||
                    (auth.getAuthResult() == UserAuthManager.PASSWORD_CHANGE)) {
                    return "change";
                }
                
            } else {
                // 6-5. 인증 실패 처리
                String failMsg = userAuthManager.getFailProperty(auth.getAuthResult());
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                String msg = resource.getMessage(failMsg);
                
                // 비밀번호 실패 횟수 초과
                if (auth.getAuthResult() == UserAuthManager.PASSWORD_FAIL_CNT) {
                    String[] password_fail_val = new String[3];
                    password_fail_val[0] = Integer.toString(auth.getFailCurrCount());
                    password_fail_val[1] = Integer.toString(auth.getFailCount());
                    password_fail_val[2] = Integer.toString(auth.getAccessDeniedTerm());
                    msg = resource.getMessage(failMsg, password_fail_val);
                } 
                // 비밀번호 잠금
                else if (auth.getAuthResult() == UserAuthManager.PASSWORD_LOCK) {
                    String[] password_lock_val = new String[2];
                    password_lock_val[0] = Integer.toString(auth.getFailCount());
                    password_lock_val[1] = Integer.toString(auth.getAccessDeniedTerm());
                    msg = resource.getMessage(failMsg, password_lock_val);
                }
                
                out.print(MakeMessage.printAlertUrl(msg, DEFAULT_RETURN_PATH));
                out.flush();
                
                return null;
            }
            
            log.debug("AuthUser: {}", auth);
            LogManager.printElapsedTime("login", "LoginController END");
            return "success";
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * RSA 암호화된 로그인 파라미터 복호화
     * 
     * @return [id, pass] 배열, 실패 시 null
     */
    private String[] decryptLoginParams(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       HttpSession session,
                                       I18nResources resource) throws Exception {
        PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
        session.removeAttribute("__rsaPrivateKey__"); // 키의 재사용 방지
        
        if (privateKey == null) {
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            String msg = resource.getMessage("login.rsa.fail.no.privatekey");
            out.print(MakeMessage.printAlertUrl(msg, DEFAULT_RETURN_PATH));
            out.flush();
            return null;
        }
        
        String securedId = request.getParameter("securedId");
        String securedPassword = request.getParameter("securedPassword");
        
        try {
            String userId = decryptRsa(privateKey, securedId);
            String password = decryptRsa(privateKey, securedPassword);
            return new String[]{userId, password};
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            String msg = resource.getMessage("login.rsa.fail.descrypt");
            out.print(MakeMessage.printAlertUrl(msg, DEFAULT_RETURN_PATH));
            out.flush();
            return null;
        }
    }
    
    /**
     * PKI 인증 파라미터 빈 생성
     */
    private PKIAuthParamBean getPKIParamBean(HttpServletRequest request) {
        PKIAuthParamBean paramBean = new PKIAuthParamBean();
        if (ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender()) {
            paramBean.setSignedText(request.getParameter("pkiSignText"));
        } else if (ExtPartConstants.VENDER_INITECH_V7 == ExtPartConstants.getPKIVender()) {
            paramBean.setUserDN(request.getParameter("_shttp_client_cert_subject_"));
        } else if (ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender()) {
            paramBean.setSignedText(request.getParameter("pkiSignText"));
        }
        return paramBean;
    }
    
    /**
     * RSA 복호화
     */
    private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8");
        return decryptedValue;
    }
    
    /**
     * 16진 문자열을 byte 배열로 변환
     */
    private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }
        
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
    
    /**
     * 다국어 리소스 가져오기 (BaseAction의 메서드 대체)
     */
    private I18nResources getMessageResource(HttpServletRequest request, String module) {
        // TODO: BaseAction의 getMessageResource() 로직 확인 필요
        return new I18nResources(module);
    }
}

