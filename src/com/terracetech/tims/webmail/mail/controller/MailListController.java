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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.log.PerformanceLogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.UserEtcInfoVO;
import com.terracetech.tims.webmail.util.ResponseUtil;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * Mail List Controller
 * Struts2 ListMessageAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 메일 목록 조회
 *    - 일반 메일 목록
 *    - 공유 폴더 메일 목록
 *    - 태그별 메일 목록
 *    - 검색 결과 목록
 * 
 * 2. 메일 검색
 *    - 기본 검색 (keyWord)
 *    - 고급 검색 (발신자, 수신자, 기간, 플래그)
 * 
 * 3. 메일 정렬
 *    - sortBy: arrival, subject, from, size, etc.
 *    - sortDir: asc, desc
 * 
 * 4. 페이징 처리
 *    - PageManager를 통한 페이지 계산
 * 
 * 5. AJAX 응답 처리
 *    - JSON 형식으로 메일 목록 반환
 * 
 * 6. 성능 디버깅
 *    - 각 단계별 시간 측정 및 로깅
 * 
 * 재사용 Manager:
 * - MailManager: 메일 조회, 폴더 관리
 * - SettingManager: 사용자 설정 조회
 * - LadminManager: IMAP 프로토콜 직접 제어
 */
@Controller("mailListController")
public class MailListController {
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private SettingManager userSettingManager;
    
    @Autowired
    private LadminManager ladminManager;
    
    @Autowired
    private ServletContext servletContext;
    
    /**
     * 메일 목록 조회 메인 메서드
     * URL: /mail/list.do
     * 
     * @return "success" → /classic/mail/list.jsp
     *         "mailMailList" → /classic/mail/messageList.jsp
     *         "mailPortletList" → 포틀릿용 목록
     *         "subError" → 에러 페이지
     */
    public String list(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        
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
        Locale locale = new Locale(user.get(User.LOCALE));
        boolean isError = false;
        String userId = user.get(User.MAIL_UID);
        int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
        I18nResources msgResource = getMessageResource(request, "mail");
        
        // 4. 요청 파라미터 추출
        String viewMode = request.getParameter("vmode");
        viewMode = (viewMode != null) ? viewMode : "h";
        
        String sharedFlag = request.getParameter("sharedFlag");
        boolean isShared = "shared".equals(sharedFlag);
        String sharedUserSeq = request.getParameter("sharedUserSeq");
        String sharedFolderName = request.getParameter("sharedFolderName");
        
        String keyWord = request.getParameter("keyWord");
        String page = request.getParameter("page");
        String advancedSearch = request.getParameter("adv");
        String category = request.getParameter("category");
        String folderName = request.getParameter("folder");
        
        String portlet = request.getParameter("portlet");
        String portletPart = request.getParameter("part");
        
        String listType = request.getParameter("listType");
        String tagIdStr = request.getParameter("tagId");
        
        String flag = request.getParameter("flag");
        String fromAddr = request.getParameter("fromaddr");
        String toAddr = request.getParameter("toaddr");
        String fromDate = request.getParameter("sdate");
        String toDate = request.getParameter("edate");
        String sortBy = request.getParameter("sortBy");
        String sortDir = request.getParameter("sortDir");
        
        // 5. 스팸/화이트리스트 관리자 확인
        String spamAdmId = EnvConstants.getBasicSetting("spam.admin");
        String hamAdmId = EnvConstants.getBasicSetting("white.admin");
        boolean isRuleAdmin = spamAdmId.equals(userId) || hamAdmId.equals(userId);
        
        char flagType = (flag != null && flag.length() > 0) ? flag.charAt(0) : 'x';
        
        String pageBase = null;
        String folderEncodeName = null;
        
        page = (page == null) ? "1" : page;
        listType = (listType != null) ? listType : "mail";
        boolean isPortlet = "on".equals(portlet);
        boolean isUnseen = false;
        
        if (StringUtils.isEmpty(folderName)) {
            folderName = FolderHandler.INBOX;
        }
        
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "arrival";
        }
        
        if (StringUtils.isEmpty(sortDir)) {
            sortDir = "desc";
        }
        
        if (isShared) {
            folderEncodeName = sharedFolderName;
        } else {
            folderEncodeName = TMailUtility.IMAPFolderEncode(folderName);
        }
        
        // 6. 메일 저장소 연결 및 메일 목록 조회
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        Protocol ladminProtocol = null;
        
        String folderAliasName = null;
        int messageCount = 0;
        int unreadMessageCount = 0;
        String folderType = "normal";
        int total = 0;
        String folderFullName = "";
        keyWord = StringUtils.escapeImapQuote(keyWord);
        
        TMailMessage[] messages = null;
        MailSortMessageBean[] messageBeans = null;
        TMailFolder currentFolder = null;
        PageManager pageManager = null;
        PageManager pm = new PageManager();
        
        String forward = "success";
        JSONObject messageListinfo = new JSONObject();
        
        try {
            // 7. 사용자 설정 조회
            UserEtcInfoVO userSettingVO = userSettingManager.readUserEtcInfo(userSeq);
            
            // 8. IMAP 연결 및 메일 조회
            store = factory.getTMailStore(user, request);
            
            // 9. 폴더 정보 조회
            currentFolder = mailManager.openFolder(store, folderEncodeName, isShared);
            
            if (currentFolder == null) {
                isError = true;
                forward = "subError";
            } else {
                folderAliasName = currentFolder.getAliasName();
                messageCount = currentFolder.getMessageCount();
                unreadMessageCount = currentFolder.getUnreadMessageCount();
                folderFullName = currentFolder.getFullName();
                
                // 10. 메일 목록 조회 (정렬, 필터링, 검색)
                MessageSortInfoBean sortBean = new MessageSortInfoBean();
                sortBean.setSortBy(sortBy);
                sortBean.setSortDir(sortDir);
                sortBean.setFlagType(flagType);
                sortBean.setKeyWord(keyWord);
                sortBean.setFromAddr(fromAddr);
                sortBean.setToAddr(toAddr);
                sortBean.setFromDate(fromDate);
                sortBean.setToDate(toDate);
                sortBean.setAdvancedSearch(advancedSearch);
                
                // 11. 페이징 처리
                if (userSettingVO != null) {
                    pageBase = String.valueOf(userSettingVO.getPageLine());
                } else {
                    pageBase = "20";
                }
                
                pm.setPage(Integer.parseInt(page));
                pm.setPageBase(Integer.parseInt(pageBase));
                pm.setTotalRowCount(messageCount);
                pm.calcPage();
                
                pageManager = pm;
                
                // 12. 메일 목록 조회 실행
                messages = mailManager.getMessageList(store, currentFolder, 
                                                     pm, sortBean, isShared);
                
                // 13. 메일 목록을 Bean으로 변환
                messageBeans = mailManager.convertToMailSortMessageBeans(
                    messages, locale, userSettingVO);
                
                total = messages != null ? messages.length : 0;
                
                // 14. JSON 응답 준비 (AJAX인 경우)
                if (isAjax) {
                    messageListinfo.put("folderName", folderAliasName);
                    messageListinfo.put("messageCount", messageCount);
                    messageListinfo.put("unreadMessageCount", unreadMessageCount);
                    messageListinfo.put("total", total);
                    messageListinfo.put("page", page);
                    messageListinfo.put("pageBase", pageBase);
                    
                    JSONArray messageArray = new JSONArray();
                    if (messageBeans != null) {
                        for (MailSortMessageBean bean : messageBeans) {
                            JSONObject msgObj = new JSONObject();
                            msgObj.put("uid", bean.getUid());
                            msgObj.put("subject", StringEscapeUtils.escapeHtml(bean.getSubject()));
                            msgObj.put("from", StringEscapeUtils.escapeHtml(bean.getFrom()));
                            msgObj.put("date", bean.getDate());
                            msgObj.put("size", bean.getSize());
                            msgObj.put("hasAttachment", bean.isHasAttachment());
                            msgObj.put("isRead", bean.isRead());
                            messageArray.add(msgObj);
                        }
                    }
                    messageListinfo.put("messages", messageArray);
                }
            }
            
        } catch (Exception e) {
            LogManager.printElapsedTime("mailList", "ERROR: " + e.getMessage());
            isError = true;
            forward = "subError";
        } finally {
            if (store != null) {
                try {
                    store.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        
        // 15. 응답 처리
        if (isAjax) {
            // AJAX 응답
            ResponseUtil.sendJsonResponse(request.getServletContext(), 
                                        request, 
                                        messageListinfo.toJSONString());
            return null;
        } else {
            // JSP 페이지 응답
            if (!isError) {
                forward = "mailMailList";
                
                if (isPortlet) {
                    forward = "mailPortletList";
                    model.addAttribute("unseen", isUnseen);
                    model.addAttribute("folderName", folderAliasName);
                    model.addAttribute("messageCount", messageCount);
                    model.addAttribute("unreadMessageCount", unreadMessageCount);
                    model.addAttribute("currentPage", page);
                    model.addAttribute("pageBase", pageBase);
                    model.addAttribute("total", total);
                    model.addAttribute("part", portletPart);
                    model.addAttribute("viewMode", viewMode);
                }
                
                model.addAttribute("email", user.get(User.EMAIL));
                model.addAttribute("listType", listType);
                model.addAttribute("tagId", tagIdStr);
                model.addAttribute("folderType", folderType);
                model.addAttribute("folderFullName", folderFullName);
                model.addAttribute("page", page);
                model.addAttribute("advancedSearch", advancedSearch);
                model.addAttribute("category", category);
                model.addAttribute("fromaddr", fromAddr);
                model.addAttribute("toaddr", toAddr);
                model.addAttribute("sdate", fromDate);
                model.addAttribute("edate", toDate);
                model.addAttribute("keyWord", keyWord);
                model.addAttribute("flag", flag);
                model.addAttribute("sharedFlag", sharedFlag);
                model.addAttribute("sharedUserSeq", sharedUserSeq);
                model.addAttribute("sharedFolderName", sharedFolderName);
                model.addAttribute("mailInfo", messageListinfo.toString());
                
                // Model에 데이터 추가
                model.addAttribute("messages", messages);
                model.addAttribute("messageBeans", messageBeans);
                model.addAttribute("currentFolder", currentFolder);
                model.addAttribute("pageManager", pageManager);
                model.addAttribute("sortBy", sortBy);
                model.addAttribute("sortDir", sortDir);
            } else {
                forward = "subError";
            }
        }
        
        return forward;
    }
    
    /**
     * 다국어 리소스 가져오기
     */
    private I18nResources getMessageResource(HttpServletRequest request, String module) {
        // TODO: BaseAction의 getMessageResource() 로직 확인 필요
        return new I18nResources(module);
    }
}

