package com.terracetech.tims.webmail.mail.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.maxmind.geoip2.record.Country;
import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailPart;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.GeoIpManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.exception.MailNotFoundException;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.IPUtils;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * Mail Read Controller
 * Struts2 ReadMessageAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 메일 읽기
 *    - UID로 메일 조회
 *    - 공유 폴더 메일 조회
 *    - 팝업/인쇄 모드 지원
 * 
 * 2. 메일 파싱
 *    - HTML/TEXT 컨텐츠 추출
 *    - 첨부 파일 목록 추출
 *    - vCard 추출
 *    - 이미지 첨부 처리
 * 
 * 3. 이전/다음 메일 UID 조회
 *    - 목록에서 현재 메일의 위치 파악
 *    - 검색/정렬 조건 고려
 * 
 * 4. 메일 보안 기능
 *    - 메일 무결성 확인
 *    - 외부 이미지 차단 (hiddenImg)
 *    - 스팸 점수 표시
 * 
 * 5. GeoIP 조회
 *    - 발신자 IP 위치 정보
 * 
 * 6. 읽음 플래그 처리
 *    - 자동 읽음 표시
 * 
 * 7. AJAX 응답
 *    - JSON 형식으로 메일 정보 반환
 * 
 * 재사용 Manager:
 * - MailManager: 메일 조회, 파싱, 이전/다음 UID
 * - SettingManager: 사용자 설정 조회
 * - SystemConfigManager: 시스템 설정 (무결성)
 * - LadminManager: IMAP 프로토콜
 * - GeoIpManager: IP 위치 조회
 */
@Controller("mailReadController")
public class MailReadController {
    
    private static final Logger log = LoggerFactory.getLogger(MailReadController.class);
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private SettingManager userSettingManager;
    
    @Autowired
    private SystemConfigManager systemConfigManager;
    
    @Autowired
    private LadminManager ladminManager;
    
    @Autowired
    private GeoIpManager geoIpManager;
    
    @Autowired
    private ServletContext servletContext;
    
    /**
     * 메일 읽기 메인 메서드
     * URL: /mail/read.do
     * 
     * @return "success" → /classic/mail/view.jsp
     *         "popupRead" → 팝업 읽기 페이지
     *         "printRead" → 인쇄 페이지
     *         "alertclose" → 에러 알림 후 닫기
     */
    public String read(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        
        // 1. 파라미터 추출
        String processMethod = request.getParameter("method");
        boolean isAjax = "ajax".equals(processMethod);
        
        // 2. 성능 디버깅 설정
        Object wdebug = servletContext.getAttribute("PDEBUG");
        boolean isWDebug = "enable".equals(wdebug);
        StringBuffer debugStr = null;
        long dstime = 0;
        SimpleDateFormat dsdf = null;
        
        if (isWDebug && isAjax) {
            String agent = request.getHeader("user-agent").toUpperCase();
            dsdf = new SimpleDateFormat("mm:ss:SS");
            debugStr = new StringBuffer();
            Date dwbugDate = new Date();
            dstime = dwbugDate.getTime();
            debugStr.append("SERVER_START,");
            debugStr.append("NTIME,");
            debugStr.append(dsdf.format(dwbugDate));
        }
        
        // 3. 사용자 정보 및 기본 설정
        User user = (User) session.getAttribute(User.class.getName());
        I18nResources msgResource = getMessageResource(request);
        Locale locale = I18nConstants.getUserLocale(request);
        
        String userId = user.get(User.MAIL_UID);
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        
        // 4. 요청 파라미터 추출
        String sharedFlag = request.getParameter("sharedFlag");
        boolean isShared = "shared".equals(sharedFlag);
        String sharedUserSeq = request.getParameter("sharedUserSeq");
        String sharedFolderName = request.getParameter("sharedFolderName");
        
        String readType = request.getParameter("readType");
        String viewImg = request.getParameter("viewImg");
        String uid = request.getParameter("uid");
        String folder = request.getParameter("folder");
        String page = request.getParameter("page");
        
        String keyWord = request.getParameter("keyWord");
        String advancedSearch = request.getParameter("adv");
        String category = request.getParameter("category");
        String flag = request.getParameter("flag");
        String sortBy = request.getParameter("sortBy");
        String sortDir = request.getParameter("sortDir");
        
        String spamAdmId = EnvConstants.getBasicSetting("spam.admin");
        String hamAdmId = EnvConstants.getBasicSetting("white.admin");
        boolean isError = false;
        String errorMsg = null;
        
        boolean isRuleAdmin = spamAdmId.equals(userId) || hamAdmId.equals(userId);
        sharedFolderName = StringEscapeUtils.unescapeHtml(sharedFolderName);
        
        char flagType = (flag != null && flag.length() > 0) ? flag.charAt(0) : 'x';
        boolean isPopupRead = (readType != null && (readType.equals("pop") || readType.equals("print")));
        boolean isPrintRead = (readType != null && readType.equals("print"));
        
        if (StringUtils.isEmpty(folder)) {
            folder = FolderHandler.INBOX;
        }
        
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "arrival";
        }
        
        if (StringUtils.isEmpty(sortDir)) {
            sortDir = "desc";
        }
        
        if (isShared) {
            folder = sharedFolderName;
        } else {
            sharedFlag = "user";
        }
        
        // 5. 메일 저장소 연결 및 메일 읽기
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        TMailFolder ufolder = null;
        Protocol ladminProtocol = null;
        
        TMailMessage message = null;
        String htmlContent = null;
        TMailPart[] files = null;
        TMailPart[] vcards = null;
        String[] imageAttach = null;
        long preUid = -1L;
        long nextUid = -1L;
        
        String mesageIntegrity = null;
        boolean isHiddenImg = false;
        String folderType = null;
        float spamRateValue = 0;
        MailMessageBean messageBean = null;
        
        String forward = "success";
        
        try {
            // 6. UID, Folder 필수 검증
            if (uid == null) {
                throw new Exception("USER[" + userId + "] ReadMessage UID is null!");
            }
            if (folder == null) {
                throw new Exception("USER[" + userId + "] ReadMessage FolderName is null!");
            }
            
            // 7. IMAP 연결
            store = factory.connect(isShared, sharedUserSeq, request.getRemoteAddr(), user);
            
            // 8. 정렬 정보 설정
            MessageSortInfoBean sortBean = new MessageSortInfoBean();
            sortBean.setSortBy(sortBy);
            sortBean.setSortDir(sortDir);
            if (StringUtils.isNotEmpty(advancedSearch)) {
                sortBean.setAdSearchCategory(category);
                sortBean.setAdvanceMode(true);
                sortBean.setAdSearchPattern(keyWord);
            } else {
                sortBean.setPattern(keyWord);
            }
            sortBean.setSearchFlag(flagType);
            
            // 9. 폴더 열기 및 이전/다음 UID 조회
            mailManager.setProcessResource(store, getMessageResource(request));
            ufolder = store.getFolder(folder);
            ufolder.open(true);
            
            long[] neighborUID = mailManager.getNeighborUID(ufolder, Long.parseLong(uid), sortBean);
            preUid = neighborUID[0];
            nextUid = neighborUID[1];
            
            // 10. 사용자 설정 조회
            UserEtcInfoVO userSettingVo = userSettingManager.readUserEtcInfo(userSeq);
            
            mesageIntegrity = systemConfigManager.getMailIntegrityInfo();
            isHiddenImg = "on".equalsIgnoreCase(userSettingVo.getHiddenImg());
            
            // 11. 메일 조회
            message = mailManager.getMessage(ufolder, Long.parseLong(uid));
            
            if (message == null) {
                throw new MailNotFoundException("Message not found: UID=" + uid);
            }
            
            // 12. 메일 파싱
            MessageParserInfoBean parserBean = new MessageParserInfoBean();
            parserBean.setLocale(locale);
            parserBean.setViewImg(viewImg);
            parserBean.setHiddenImg(isHiddenImg ? "on" : "off");
            parserBean.setUserId(userId);
            parserBean.setFolderName(folder);
            
            messageBean = mailManager.parseMessage(message, parserBean);
            
            htmlContent = messageBean.getHtmlContent();
            files = messageBean.getAttachments();
            vcards = messageBean.getVcards();
            imageAttach = messageBean.getImageAttachments();
            
            // 13. 폴더 타입 확인
            folderType = mailManager.getFolderType(folder);
            
            // 14. 스팸 점수 조회
            if ("spam".equalsIgnoreCase(folderType)) {
                spamRateValue = mailManager.getSpamScore(message);
            }
            
            // 15. GeoIP 조회
            String senderIP = mailManager.getSenderIP(message);
            Country country = null;
            if (StringUtils.isNotEmpty(senderIP) && IPUtils.isPublicIP(senderIP)) {
                try {
                    country = geoIpManager.getCountry(senderIP);
                } catch (Exception e) {
                    log.debug("GeoIP lookup failed: {}", e.getMessage());
                }
            }
            
            // 16. 읽음 플래그 처리
            if (!message.isRead()) {
                mailManager.setReadFlag(message, true);
            }
            
            // 17. 결과 설정
            if (isPopupRead) {
                forward = isPrintRead ? "printRead" : "popupRead";
            }
            
        } catch (MailNotFoundException e) {
            log.error("Mail not found: {}", e.getMessage());
            isError = true;
            errorMsg = msgResource.getMessage("mail.error.notfound");
            forward = "alertclose";
        } catch (Exception e) {
            log.error("Error reading mail: {}", e.getMessage(), e);
            isError = true;
            errorMsg = msgResource.getMessage("mail.error.read");
            forward = "alertclose";
        } finally {
            if (ufolder != null) {
                try {
                    ufolder.close(false);
                } catch (Exception e) {
                    // ignore
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        
        // 18. 응답 처리
        if (isAjax) {
            // AJAX 응답
            JSONObject result = new JSONObject();
            if (!isError) {
                result.put("success", true);
                result.put("uid", uid);
                result.put("subject", message.getSubject());
                result.put("from", message.getFrom());
                result.put("date", message.getSentDate());
                result.put("htmlContent", htmlContent);
                result.put("preUid", preUid);
                result.put("nextUid", nextUid);
                
                // 첨부 파일 정보
                JSONArray attachArray = new JSONArray();
                if (files != null) {
                    for (TMailPart file : files) {
                        JSONObject fileObj = new JSONObject();
                        fileObj.put("filename", file.getFileName());
                        fileObj.put("size", file.getSize());
                        attachArray.add(fileObj);
                    }
                }
                result.put("attachments", attachArray);
            } else {
                result.put("success", false);
                result.put("error", errorMsg);
            }
            
            ResponseUtil.sendJsonResponse(servletContext, request, result.toJSONString());
            return null;
        } else {
            // JSP 페이지 응답
            if (!isError) {
                model.addAttribute("message", message);
                model.addAttribute("messageBean", messageBean);
                model.addAttribute("htmlContent", htmlContent);
                model.addAttribute("files", files);
                model.addAttribute("vcards", vcards);
                model.addAttribute("imageAttach", imageAttach);
                model.addAttribute("preUid", preUid);
                model.addAttribute("nextUid", nextUid);
                model.addAttribute("folderType", folderType);
                model.addAttribute("spamRateValue", spamRateValue);
                model.addAttribute("isHiddenImg", isHiddenImg);
                model.addAttribute("mesageIntegrity", mesageIntegrity);
                
                // 파라미터 유지
                model.addAttribute("folder", folder);
                model.addAttribute("page", page);
                model.addAttribute("keyWord", keyWord);
                model.addAttribute("advancedSearch", advancedSearch);
                model.addAttribute("category", category);
                model.addAttribute("flag", flag);
                model.addAttribute("sortBy", sortBy);
                model.addAttribute("sortDir", sortDir);
                model.addAttribute("sharedFlag", sharedFlag);
                model.addAttribute("sharedUserSeq", sharedUserSeq);
                model.addAttribute("sharedFolderName", sharedFolderName);
            } else {
                model.addAttribute("errorMsg", errorMsg);
            }
        }
        
        return forward;
    }
    
    /**
     * 다국어 리소스 가져오기
     */
    private I18nResources getMessageResource(HttpServletRequest request) {
        // TODO: BaseAction의 getMessageResource() 로직 확인 필요
        return new I18nResources("mail");
    }
}

