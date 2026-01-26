package com.terracetech.tims.webmail.mail.controller;

import java.util.Date;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ExtPartConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.mail.ibean.MailWriteMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageWriteInfoBean;
import com.terracetech.tims.webmail.mail.manager.BigattachManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import com.terracetech.tims.common.I18nResources;

/**
 * Mail Write Controller
 * Struts2 WriteMessageAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 메일 작성 화면 준비
 *    - 새 메일 작성
 *    - 답장 (Reply)
 *    - 전달 (Forward)
 *    - 임시 보관함에서 불러오기
 * 
 * 2. 메일 작성 모드
 *    - normal: 일반 작성
 *    - popup: 팝업 작성
 *    - localpopup: 로컬 메일 작성
 * 
 * 3. 보안 메일 작성
 *    - secureWriteMode: normal/secure
 *    - 보안 모듈 (PKI) 설정
 * 
 * 4. 첨부 파일 설정
 *    - 일반 첨부 파일
 *    - 대용량 첨부 파일 (BigAttach)
 *    - 첨부 파일 용량 제한
 * 
 * 5. 사용자 설정 조회
 *    - 서명 (Signature)
 *    - 에디터 모드 (HTML/TEXT)
 *    - 보낸 메일함 저장 여부
 *    - 최근 수신자
 * 
 * 6. 시스템 설정 조회
 *    - 대량 발송 확인 설정
 *    - 문서 템플릿 사용 여부
 *    - 발송 확인 설정
 * 
 * 재사용 Manager:
 * - MailManager: 메일 작성 정보 조회
 * - MailUserManager: 사용자 설정 조회
 * - SettingManager: 사용자 상세 설정
 * - SignManager: 서명 조회
 * - BigattachManager: 대용량 첨부 관리
 * - BbsManager: BBS 메일 작성
 * - SystemConfigManager: 시스템 설정
 * - LastrcptManager: 최근 수신자
 */
@Controller("mailWriteController")
public class MailWriteController {
    
    @Autowired
    private MailUserManager mailUserManager;
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private SettingManager userSettingManager;
    
    @Autowired
    private BigattachManager bigAttachManager;
    
    @Autowired
    private BbsManager bbsManager;
    
    @Autowired
    private SignManager signManager;
    
    @Autowired
    private SystemConfigManager systemConfigManager;
    
    @Autowired
    private LastrcptManager rcptManager;
    
    @Autowired
    private ServletContext servletContext;
    
    // Request 파라미터 (URL에서 전달된 값)
    private String toReq;
    private String ccReq;
    private String bccReq;
    private String subjectReq;
    private String contentReq;
    
    /**
     * 메일 작성 화면 준비 메인 메서드
     * URL: /mail/write.do
     * 
     * @return "success" → /classic/mail/write.jsp
     *         "popupWrite" → 팝업 작성 페이지
     *         "subError" → 에러 페이지
     */
    public String write(HttpServletRequest request, HttpServletResponse response,
                       HttpSession session, Model model) throws Exception {
        
        // 1. 파라미터 추출
        String processMethod = request.getParameter("method");
        boolean isAjax = "ajax".equals(processMethod);
        
        boolean isError = false;
        boolean isMassConfirm = false;
        
        User user = (User) session.getAttribute(User.class.getName());
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        int groupSeq = Integer.parseInt(user.get(User.MAIL_GROUP_SEQ));
        
        String secureWriteMode = request.getParameter("secureWriteMode");
        secureWriteMode = (secureWriteMode != null) ? secureWriteMode : "normal";
        
        String wmode = request.getParameter("wmode");
        wmode = (wmode != null) ? wmode : "normal";
        
        String imgPath = servletContext.getRealPath("/") + 
                        EnvConstants.getAttachSetting("attach.dir");
        String secureMailModule = EnvConstants.getUtilSetting("secure.module");
        
        // 2. 브라우저 체크
        String agent = request.getParameter("agent");
        if (agent == null) {
            agent = request.getHeader("user-agent");
        }
        
        String browserinfo = "MSIE";
        boolean isMsieBrowser = true;
        String writeType = request.getParameter("wtype");
        writeType = StringEscapeUtils.escapeHtml(writeType);
        
        // 3. URL에서 전달된 파라미터 (외부 연동용)
        toReq = request.getParameter("to");
        ccReq = request.getParameter("cc");
        bccReq = request.getParameter("bcc");
        subjectReq = request.getParameter("subject");
        contentReq = request.getParameter("content");
        
        // 4. IMAP 연결
        TMailStoreFactory factory = new TMailStoreFactory();
        TMailStore store = null;
        
        if ("bbs".equals(writeType)) {
            Map<String, String> confMap = bbsManager.getBbsConnectionInfo(
                user.get(User.MAIL_DOMAIN));
            store = factory.connect(request.getRemoteAddr(), confMap);
            mailManager.setProcessResource(store, getMessageResource(request, "bbs"));
        } else {
            store = factory.connect(request.getRemoteAddr(), user);
            mailManager.setProcessResource(store, getMessageResource(request, "mail"));
        }
        
        // 5. 브라우저 판별
        if (agent.equals("mobile") || 
            agent.toUpperCase().indexOf("MSIE") == -1) {
            isMsieBrowser = false;
            browserinfo = "NON-MSIE";
        }
        
        JSONObject jObj = null;
        MailWriteMessageBean writeBean = null;
        
        try {
            // 6. 사용자 설정 조회
            Map<String, String> configMap = mailUserManager.readUserSetting(
                domainSeq, groupSeq, userSeq);
            
            // 7. 메일 작성 정보 설정
            MessageWriteInfoBean writeInfo = new MessageWriteInfoBean();
            writeInfo.setWriteType(writeType);
            writeInfo.setLocalMailWrite(wmode.equals("localpopup"));
            
            if (writeInfo.isLocalMailWrite()) {
                writeInfo.setUids(new String[]{request.getParameter("puids")});
            } else {
                writeInfo.setUids(request.getParameterValues("uids"));
            }
            
            writeInfo.setWuids(request.getParameter("wuid"));
            writeInfo.setFolderName(request.getParameter("folder"));
            writeInfo.setWebfolderType(request.getParameter("wfolderType"));
            writeInfo.setWebfolderShareSeq(request.getParameter("wfolderShareSeq"));
            writeInfo.setPlace(request.getParameter("place"));
            writeInfo.setReqTo(toReq);
            writeInfo.setReqCc(ccReq);
            writeInfo.setReqBcc(bccReq);
            writeInfo.setReqSubject(subjectReq);
            writeInfo.setReqContent(contentReq);
            writeInfo.setReturl(request.getParameter("returl"));
            
            writeInfo.setAttachLists(request.getParameterValues("attlists"));
            writeInfo.setAttatachUrls(request.getParameterValues("atturls"));
            writeInfo.setAttatachFilenames(request.getParameterValues("down_filename"));
            writeInfo.setAutoSaveMode(request.getParameter("autoSaveMode"));
            writeInfo.setForwardingMode(request.getParameter("fwmode"));
            
            writeInfo.setBuids(request.getParameter("buid"));
            writeInfo.setBmids(request.getParameter("bmid"));
            writeInfo.setBbsId(request.getParameter("bbsid"));
            writeInfo.setSignInside("inside".equals(getSignLocation(configMap)));
            writeInfo.setImgFilePath(imgPath);
            
            // 8. 사용자 상세 설정 조회
            UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
            
            String senderName = userSettingVo.getSenderName();
            String senderEmail = userSettingVo.getSenderEmail();
            
            // 9. 메일 작성 Bean 생성
            writeBean = mailManager.getWriteSettingBean(writeInfo, user);
            writeBean.setEditorMode(userSettingVo.getWriteMode());
            writeBean.setSaveSent(userSettingVo.getSaveSendBox());
            writeBean.setSenderName(senderName);
            writeBean.setSenderEmail(senderEmail);
            
            // 10. 첨부 파일 설정 조회
            Map<String, String> attachConfigMap = bigAttachManager.getAttachConfig(domainSeq);
            String bigattachQuotaUseage = bigAttachManager.getUsedBigAttachQuota(user);
            
            // 11. 시스템 설정 조회
            int maxRecipient = systemConfigManager.getMaxRecipient();
            if (maxRecipient > 0) {
                isMassConfirm = true;
            }
            
            // 12. Model에 데이터 추가
            model.addAttribute("writeBean", writeBean);
            model.addAttribute("secureWriteMode", secureWriteMode);
            model.addAttribute("secureMailModule", secureMailModule);
            model.addAttribute("browserinfo", browserinfo);
            model.addAttribute("isMsieBrowser", isMsieBrowser);
            
            // AJAX가 아닌 경우 추가 속성 설정
            if (!isAjax) {
                model.addAttribute("wmode", (writeType != null) ? writeType : "html");
                model.addAttribute("bigAttachDownCnt", 
                    attachConfigMap.get("bigattach_download"));
                model.addAttribute("bigAttachExpireday", 
                    attachConfigMap.get("bigattach_expireday"));
                model.addAttribute("maxAttachSize", 
                    attachConfigMap.get("attach_maxsize"));
                model.addAttribute("maxBigAttachSize", 
                    attachConfigMap.get("bigattach_maxsize"));
                model.addAttribute("maxBigAttachQuota", 
                    attachConfigMap.get("bigattach_maxsize"));
                model.addAttribute("useBigAttachDownCnt", 
                    attachConfigMap.get("bigattach_download_limited"));
                model.addAttribute("useBigAttachQuota", bigattachQuotaUseage);
                model.addAttribute("massRcptConfirm", Boolean.toString(isMassConfirm));
                model.addAttribute("systemTime", Long.toString(new Date().getTime()));
                model.addAttribute("sendCheckApply", 
                    "on".equalsIgnoreCase(configMap.get("send_check")));
                model.addAttribute("docTemplateApply", 
                    "on".equalsIgnoreCase(configMap.get("doc_template_use")));
            }
            
        } catch (Exception e) {
            LogManager.writeErr(this, e.getMessage(), e);
            isError = true;
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
            
            toReq = null;
            ccReq = null;
            bccReq = null;
            subjectReq = null;
            contentReq = null;
        }
        
        // 13. 응답 처리
        String forward = null;
        if (isAjax) {
            jObj = new JSONObject();
            if (isError) {
                jObj.put("jobResult", "error");
            } else {
                jObj.put("jobResult", "success");
            }
            ResponseUtil.processResponse(response, jObj);
            return null;
        } else {
            forward = "success";
            if (wmode.indexOf("popup") > -1) {
                forward = "popupWrite";
            }
            
            if (isError) {
                forward = "subError";
            }
        }
        
        return forward;
    }
    
    /**
     * 서명 위치 조회
     */
    private String getSignLocation(Map<String, String> configMap) {
        return configMap.get("sign_location");
    }
    
    /**
     * 다국어 리소스 가져오기
     */
    private I18nResources getMessageResource(HttpServletRequest request, String module) {
        return new I18nResources(module);
    }
}

