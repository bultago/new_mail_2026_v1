package com.terracetech.tims.webmail.mail.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.common.manager.SystemConfigManager;
import com.terracetech.tims.webmail.common.manager.VirusManager;
import com.terracetech.tims.webmail.common.vo.VirusCheckVO;
import com.terracetech.tims.webmail.exception.VirusCheckException;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.ibean.MailSecureInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.Image2Cid;
import com.terracetech.tims.webmail.mail.manager.LetterManager;
import com.terracetech.tims.webmail.mail.manager.SendMessageDirector;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mail.manager.builder.DraftMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.GeneralMimeMessageBuilder;
import com.terracetech.tims.webmail.mail.manager.builder.MimeMessageBuilder;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.mailuser.manager.SearchEmailManager;
import com.terracetech.tims.webmail.setting.manager.LastrcptManager;
import com.terracetech.tims.webmail.setting.manager.SignManager;
import com.terracetech.tims.webmail.setting.manager.VCardManager;
import com.terracetech.tims.webmail.util.HangulEmail;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;
import java.util.ArrayList;

/**
 * Mail Send Controller
 * Struts2 SendMessageAction을 Spring MVC로 전환
 * 
 * 기능 분석:
 * 1. 메일 발송
 *    - 일반 발송
 *    - 임시 보관
 *    - 예약 발송
 *    - 대량 발송 (Batch)
 *    - 개별 발송 (Each)
 * 
 * 2. 수신자 검증
 *    - 이메일 주소 형식 검증
 *    - 최대 수신자 수 체크
 *    - 수신자 유효성 검증
 * 
 * 3. 첨부 파일 처리
 *    - 일반 첨부
 *    - 대용량 첨부 (BigAttach)
 *    - 바이러스 검사
 * 
 * 4. 보안 메일
 *    - PKI 암호화
 *    - 서명
 * 
 * 5. 메일 저장
 *    - 보낸 메일함 저장
 *    - 임시 보관함 저장
 *    - 예약 메일함 저장
 * 
 * 6. 읽음 확인
 *    - MDN (Message Disposition Notification)
 * 
 * 7. 로그 기록
 *    - 발송 이력
 *    - 수신자 정보
 * 
 * 재사용 Manager:
 * - SendMessageDirector: 메일 발송 총괄
 * - MimeMessageBuilder: MIME 메시지 생성
 * - LastrcptManager: 최근 수신자 저장
 * - VCardManager: vCard 처리
 * - LetterManager: 편지지 처리
 * - SignManager: 서명 처리
 * - SearchEmailManager: 이메일 검색
 * - LadminManager: IMAP 프로토콜
 * - SystemConfigManager: 시스템 설정
 * - VirusManager: 바이러스 검사
 */
@Controller("mailSendController")
public class MailSendController {
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    @Autowired
    private LastrcptManager rcptManager;
    
    @Autowired
    private VCardManager vcardManager;
    
    @Autowired
    private LetterManager letterManager;
    
    @Autowired
    private SignManager signManager;
    
    @Autowired
    private SearchEmailManager searchEmailManager;
    
    @Autowired
    private LadminManager ladminManager;
    
    @Autowired
    private SystemConfigManager systemConfigManager;
    
    @Autowired
    private VirusManager virusManager;
    
    private MailSendResultBean sendResult = null;
    
    /**
     * 메일 발송 메인 메서드
     * URL: /mail/send.do
     * 
     * @return "success" → /classic/mail/write.jsp (재작성)
     *         "alertclose" → 알림 후 닫기
     */
    public String send(HttpServletRequest request, HttpServletResponse response,
                      HttpSession session, Model model) throws Exception {
        
        // 1. 사용자 정보 및 기본 설정
        User user = (User) session.getAttribute(User.class.getName());
        I18nResources msgResource = getMessageResource(request);
        I18nResources commonResource = getCommonResource(request);
        
        String remoteIP = request.getRemoteAddr();
        
        // 2. 발송 정보 파라미터 추출
        SenderInfoBean info = extractSenderInfo(request, user);
        
        // 3. 수신자 주소 파싱 및 검증
        List<TMailAddress> toList = parseAddresses(info.getTo());
        List<TMailAddress> ccList = parseAddresses(info.getCc());
        List<TMailAddress> bccList = parseAddresses(info.getBcc());
        
        int recipientCount = toList.size() + ccList.size() + bccList.size();
        
        // 4. 최대 수신자 수 체크
        int maxRecipient = systemConfigManager.getMaxRecipient();
        boolean flag = "true".equals(request.getParameter("flag"));
        boolean invalidMaxRecipient = false;
        String maxRecipientMessage = null;
        
        if (maxRecipient > 0 && maxRecipient < recipientCount && !flag) {
            maxRecipientMessage = msgResource.getMessage("mail.secure.maxrcpt", 
                new Object[]{maxRecipient});
            info.setSendType("draft");
            info.setSaveSent(false);
            info.setRecvNoti(false);
            invalidMaxRecipient = true;
        }
        
        // 5. IMAP 연결 및 발송 처리
        TMailStore store = null;
        TMailStoreFactory factory = new TMailStoreFactory();
        Protocol ladminProtocol = null;
        
        boolean detectVirus = false;
        String virusMsg = "";
        
        try {
            store = factory.connect(remoteIP, user);
            ladminProtocol = new Protocol(user.get(User.MAIL_HOST));
            ladminManager.setResource(ladminProtocol, msgResource);
            info.setLadminManager(ladminManager);
            
            // 6. 바이러스 검사
            if (StringUtils.isNotEmpty(info.getAttlist())) {
                String virusHost = EnvConstants.getVirusSetting("virus.server");
                String virusPortStr = EnvConstants.getVirusSetting("virus.server.port");
                int virusPort = StringUtils.isNotEmpty(virusPortStr) ? 
                              Integer.parseInt(virusPortStr) : 17777;
                
                VirusCheckVO checkVO = virusManager.checkVirus(
                    virusHost, virusPort, info.getAttlist(), commonResource);
                
                if (!checkVO.isSuccess()) {
                    if (!info.isSendDraft()) {
                        String content = info.getContent();
                        int start = content.indexOf("<div class='TerraceMsg'>");
                        if (start != -1) {
                            int end = content.lastIndexOf("</div>");
                            content = content.substring(start + 24, end);
                            info.setContent(content);
                        }
                    }
                    info.setSendType("draft");
                    info.setAttlist("");
                    info.setSaveSent(false);
                    info.setRecvNoti(false);
                    
                    detectVirus = true;
                    virusMsg = checkVO.getCheckResultMsg();
                }
            }
            
            // 7. MimeMessage 빌더 선택
            MimeMessageBuilder builder = null;
            if (info.isSendDraft()) {
                builder = new DraftMimeMessageBuilder();
            } else {
                builder = new GeneralMimeMessageBuilder();
            }
            
            // 8. 메일 발송 실행
            SendMessageDirector director = new SendMessageDirector(builder, info);
            sendResult = director.sendProcess(store, factory);
            
            // 9. 임시 보관함에서 삭제 (임시 보관에서 발송한 경우)
            String delMid = info.getDraftMid();
            if (delMid != null && delMid.length() > 0) {
                deleteDraftMessage(store, delMid);
            }
            
            // 10. 최근 수신자 저장
            saveLastRecipients(user, toList, ccList, bccList);
            
            // 11. 발송 로그 기록
            writeSendMailLog(info, user, invalidMaxRecipient);
            
        } catch (AddressException e) {
            LogManager.writeErr(this, "Invalid email address: " + e.getMessage(), e);
            model.addAttribute("errorMsg", msgResource.getMessage("mail.error.invalid.address"));
            return "alertclose";
        } catch (MessagingException e) {
            LogManager.writeErr(this, "Messaging error: " + e.getMessage(), e);
            model.addAttribute("errorMsg", msgResource.getMessage("mail.error.send"));
            return "alertclose";
        } catch (VirusCheckException e) {
            LogManager.writeErr(this, "Virus detected: " + e.getMessage(), e);
            model.addAttribute("errorMsg", msgResource.getMessage("mail.error.virus"));
            return "alertclose";
        } catch (Exception e) {
            LogManager.writeErr(this, "Send error: " + e.getMessage(), e);
            model.addAttribute("errorMsg", msgResource.getMessage("mail.error.send"));
            return "alertclose";
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
            if (ladminProtocol != null) {
                try {
                    ladminProtocol.logout();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        
        // 12. 응답 처리
        if (sendResult != null && !sendResult.isErrorOccur()) {
            model.addAttribute("sendResult", sendResult);
            model.addAttribute("sendType", sendResult.getSendResultType());
            
            if (detectVirus) {
                model.addAttribute("virusMsg", virusMsg);
            }
            
            if (invalidMaxRecipient) {
                model.addAttribute("maxRecipientMsg", maxRecipientMessage);
            }
            
            return "success";
        } else {
            String errorMsg = sendResult != null ? 
                            sendResult.getErrorMessage() : 
                            msgResource.getMessage("mail.error.send");
            model.addAttribute("errorMsg", errorMsg);
            return "alertclose";
        }
    }
    
    /**
     * 발송 정보 추출
     */
    private SenderInfoBean extractSenderInfo(HttpServletRequest request, User user) {
        SenderInfoBean info = new SenderInfoBean();
        
        info.setTo(request.getParameter("to"));
        info.setCc(request.getParameter("cc"));
        info.setBcc(request.getParameter("bcc"));
        info.setSubject(request.getParameter("subject"));
        info.setContent(request.getParameter("content"));
        info.setSendType(request.getParameter("sendType"));
        info.setAttlist(request.getParameter("attlist"));
        info.setDraftMid(request.getParameter("draftMid"));
        info.setSaveSent("true".equals(request.getParameter("saveSent")));
        info.setRecvNoti("true".equals(request.getParameter("recvNoti")));
        info.setSendDraft("draft".equals(request.getParameter("sendType")));
        
        // 예약 발송 정보
        String reservedDate = request.getParameter("reservedDate");
        if (StringUtils.isNotEmpty(reservedDate)) {
            info.setReservedDate(reservedDate);
        }
        
        return info;
    }
    
    /**
     * 이메일 주소 파싱
     */
    private List<TMailAddress> parseAddresses(String addresses) throws AddressException {
        if (StringUtils.isEmpty(addresses)) {
            return new ArrayList<TMailAddress>();
        }
        
        InternetAddress[] internetAddresses = InternetAddress.parse(addresses);
        List<TMailAddress> result = new ArrayList<TMailAddress>();
        
        for (InternetAddress addr : internetAddresses) {
            TMailAddress tAddr = new TMailAddress();
            tAddr.setAddress(addr.getAddress());
            tAddr.setPersonal(addr.getPersonal());
            result.add(tAddr);
        }
        
        return result;
    }
    
    /**
     * 임시 보관 메일 삭제
     */
    private void deleteDraftMessage(TMailStore store, String draftMid) {
        try {
            TMailFolder draftFolder = store.getFolder(FolderHandler.DRAFTS);
            draftFolder.open(true);
            
            TMailMessage[] messages = draftFolder.getMessages();
            for (TMailMessage msg : messages) {
                if (msg.getMessageID().equals(draftMid)) {
                    msg.setFlag(Flags.Flag.DELETED, true);
                    break;
                }
            }
            
            draftFolder.close(true);
        } catch (Exception e) {
            LogManager.writeErr(this, "Failed to delete draft: " + e.getMessage(), e);
        }
    }
    
    /**
     * 최근 수신자 저장
     */
    private void saveLastRecipients(User user, List<TMailAddress> toList,
                                    List<TMailAddress> ccList,
                                    List<TMailAddress> bccList) {
        try {
            int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            
            for (TMailAddress addr : toList) {
                rcptManager.saveLastRecipient(userSeq, addr.getAddress(), addr.getPersonal());
            }
            for (TMailAddress addr : ccList) {
                rcptManager.saveLastRecipient(userSeq, addr.getAddress(), addr.getPersonal());
            }
            for (TMailAddress addr : bccList) {
                rcptManager.saveLastRecipient(userSeq, addr.getAddress(), addr.getPersonal());
            }
        } catch (Exception e) {
            LogManager.writeErr(this, "Failed to save recipients: " + e.getMessage(), e);
        }
    }
    
    /**
     * 발송 로그 기록
     */
    private void writeSendMailLog(SenderInfoBean info, User user, boolean invalidMaxRecipient) {
        if (sendResult == null) {
            return;
        }
        
        String action = "";
        String folder = "";
        boolean isJobLog = false;
        
        if (!sendResult.isErrorOccur()) {
            if (info.isSaveSent()) {
                folder = "Sent";
            }
            
            String resultType = sendResult.getSendResultType();
            if ("normal".equals(resultType)) {
                action = "action_message_send";
                isJobLog = true;
            } else if ("reserved".equals(resultType)) {
                action = "action_message_reserved";
                folder = "Reserved";
                isJobLog = false;
            } else if ("draft".equals(resultType)) {
                action = "action_message_draft";
                folder = "Drafts";
                isJobLog = true;
            } else if ("batch".equals(resultType)) {
                action = "action_message_send";
                isJobLog = true;
            }
            
            LogManager.writeMailLog(isJobLog, action, folder,
                                   sendResult.getAllVaildAddress(),
                                   user.get(User.EMAIL),
                                   sendResult.getMailSize(),
                                   info.getSubject(),
                                   sendResult.getSaveMid());
        } else if (invalidMaxRecipient) {
            action = "action_reject_maxrcpt";
            folder = "Drafts";
            isJobLog = true;
            
            LogManager.writeMailLog(isJobLog, action, folder,
                                   sendResult.getAllVaildAddress(),
                                   user.get(User.EMAIL),
                                   sendResult.getMailSize(),
                                   info.getSubject(),
                                   sendResult.getSaveMid());
        }
    }
    
    /**
     * 다국어 리소스 가져오기
     */
    private I18nResources getMessageResource(HttpServletRequest request) {
        return new I18nResources("mail");
    }
    
    /**
     * 공통 리소스 가져오기
     */
    private I18nResources getCommonResource(HttpServletRequest request) {
        return new I18nResources("common");
    }
}

