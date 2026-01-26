package com.terracetech.tims.webmail.mail.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailMessage;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.api.ApiException;
import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 메일 REST API Controller
 * 
 * <p>DWR MailMessageService를 REST API로 전환하여 실제 MailManager를 호출</p>
 * 
 * <h3>제공 API 목록:</h3>
 * <ul>
 *   <li>GET /api/mail/list - 메일 목록 조회 (페이징 지원)</li>
 *   <li>GET /api/mail/{mailId} - 메일 상세 조회</li>
 *   <li>PATCH /api/mail/{mailId}/read - 메일 읽음 처리</li>
 *   <li>DELETE /api/mail - 메일 삭제 (복수 메일 지원)</li>
 *   <li>PATCH /api/mail/move - 메일 이동 (폴더 간 이동)</li>
 *   <li>PATCH /api/mail/flags - 메일 플래그 변경 (중요, 별표 등)</li>
 * </ul>
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>TMailStore를 통한 IMAP 서버 연결 및 메일 처리</li>
 *   <li>자동 리소스 해제 (finally 블록에서 store 종료)</li>
 *   <li>세션 기반 사용자 인증</li>
 *   <li>다국어 지원 (I18nResources)</li>
 * </ul>
 * 
 * <h3>사용 예시:</h3>
 * <pre>
 * // JavaScript에서 호출 예시
 * fetch('/api/mail/list?folderName=INBOX&page=1&pageSize=20')
 *   .then(response => response.json())
 *   .then(data => console.log(data));
 * </pre>
 * 
 * @author AI Assistant
 * @since 2025-10-21
 * @see MailManager
 * @see TMailStore
 * @see MailMessageService (DWR 원본)
 */
@RestController
@RequestMapping("/api/mail")
public class MailApiController {
    
    private static final Logger log = LoggerFactory.getLogger(MailApiController.class);
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * I18nResources 가져오기
     * 
     * <p>ServletContext에서 다국어 리소스를 가져옵니다.</p>
     * <p>DWR BaseService.getMessageResource() 메서드와 동일한 방식</p>
     * 
     * @param session HTTP 세션
     * @return I18nResources 다국어 메시지 리소스
     */
    private I18nResources getMessageResource(HttpSession session) {
        return (I18nResources) session.getServletContext().getAttribute("i18nResource");
    }
    
    /**
     * 메일 목록 조회 API
     * 
     * <p>지정된 폴더의 메일 목록을 페이징하여 조회합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailMessageService.getMessageList()</pre>
     * 
     * <h4>처리 흐름:</h4>
     * <ol>
     *   <li>세션에서 사용자 정보 확인</li>
     *   <li>TMailStoreFactory로 IMAP 서버 연결</li>
     *   <li>MailManager 초기화 (store, i18n resource 설정)</li>
     *   <li>지정된 폴더에서 메일 조회</li>
     *   <li>페이징 계산 및 적용</li>
     *   <li>메일 데이터 변환 (UID, subject, from, date, size, flags)</li>
     *   <li>TMailStore 자동 종료 (finally)</li>
     * </ol>
     * 
     * <h4>반환 데이터 구조:</h4>
     * <pre>
     * {
     *   "folderName": "INBOX",
     *   "page": 1,
     *   "pageSize": 20,
     *   "totalCount": 150,
     *   "mailList": [
     *     {
     *       "uid": 12345,
     *       "subject": "메일 제목",
     *       "from": "sender@example.com",
     *       "sentDate": "2025-10-21T10:30:00",
     *       "size": 5120,
     *       "flags": {...}
     *     },
     *     ...
     *   ]
     * }
     * </pre>
     * 
     * @param folderName 조회할 메일 폴더명 (기본값: INBOX)
     * @param page 페이지 번호 (1부터 시작, 기본값: 1)
     * @param pageSize 페이지당 메일 개수 (기본값: 20)
     * @param session HTTP 세션 (사용자 인증용)
     * @return ResponseEntity<ApiResponse<Map<String, Object>>> 메일 목록 및 페이징 정보
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMailList(
            @RequestParam(defaultValue = "INBOX") String folderName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            // 1. 사용자 인증 확인
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("메일 목록 조회: 사용자={}, 폴더={}, 페이지={}", user.getMailUid(), folderName, page);
            
            // 2. TMailStore 연결 (IMAP 서버)
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            
            try {
                store = factory.connect(request.getRemoteAddr(), user);
            } catch (Exception e) {
                log.error("IMAP 연결 실패: {}", e.getMessage(), e);
                throw ApiException.internalError(resource != null ? 
                    resource.getMessage("error.imapconn") : "IMAP 연결 실패");
            }
            
            // 3. MailManager 초기화 (TMailStore와 I18n 리소스 설정)
            mailManager.setProcessResource(store, resource);
            
            // 4. 메일 폴더 가져오기 및 전체 메일 개수 확인
            TMailFolder folder = mailManager.getFolder(folderName);
            int totalCount = folder.getMessageCount();
            
            // 5. 페이징 계산 (start, end 인덱스)
            int start = (page - 1) * pageSize + 1;
            int end = Math.min(start + pageSize - 1, totalCount);
            
            List<Map<String, Object>> mailList = new ArrayList<>();
            
            // 6. 메일이 있으면 조회 및 데이터 변환
            if (totalCount > 0 && start <= totalCount) {
                TMailMessage[] messages = folder.getMessages(start, end);
                
                // 7. 각 메일 데이터를 Map으로 변환
                for (TMailMessage message : messages) {
                    Map<String, Object> mailData = new HashMap<>();
                    mailData.put("uid", folder.getUID(message));           // 메일 고유 ID
                    mailData.put("subject", message.getSubject());         // 제목
                    mailData.put("from", message.getFrom());               // 발신자
                    mailData.put("sentDate", message.getSentDate());       // 발송일시
                    mailData.put("size", message.getSize());               // 크기
                    mailData.put("flags", message.getFlags());             // 플래그 (읽음, 중요 등)
                    mailList.add(mailData);
                }
            }
            
            // 8. 결과 데이터 구성
            Map<String, Object> result = new HashMap<>();
            result.put("folderName", folderName);
            result.put("page", page);
            result.put("pageSize", pageSize);
            result.put("totalCount", totalCount);
            result.put("mailList", mailList);
            
            return ResponseEntity.ok(ApiResponse.success("메일 목록 조회 성공", result));
            
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("메일 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MAIL_LIST_ERROR", "메일 목록 조회 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * 메일 상세 조회
     * 
     * DWR: MailMessageService.getMessageDetail()
     * 
     * @param mailId 메일 UID
     * @param session HTTP 세션
     * @return 메일 상세 정보
     */
    @GetMapping("/{mailId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMailDetail(
            @PathVariable long mailId,
            @RequestParam String folderName,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("메일 상세 조회: 사용자={}, 메일UID={}", user.getMailUid(), mailId);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 메일 조회
            TMailMessage message = mailManager.getMessage(folderName, mailId);
            
            Map<String, Object> mailData = new HashMap<>();
            mailData.put("uid", mailId);
            mailData.put("subject", message.getSubject());
            mailData.put("from", message.getFrom());
            mailData.put("to", message.getRecipients(jakarta.mail.Message.RecipientType.TO));
            mailData.put("cc", message.getRecipients(jakarta.mail.Message.RecipientType.CC));
            mailData.put("sentDate", message.getSentDate());
            mailData.put("content", message.getContent());
            mailData.put("size", message.getSize());
            mailData.put("flags", message.getFlags());
            
            return ResponseEntity.ok(ApiResponse.success("메일 조회 성공", mailData));
            
        } catch (Exception e) {
            log.error("메일 상세 조회 실패: mailId={}, error={}", mailId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MAIL_DETAIL_ERROR", "메일 조회 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * 메일 읽음 처리
     * 
     * DWR: MailMessageService.setMessageRead()
     * 
     * @param mailId 메일 UID
     * @param request 요청 데이터
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @PatchMapping("/{mailId}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(
            @PathVariable long mailId,
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String folderName = (String) requestBody.get("folderName");
            Boolean read = (Boolean) requestBody.getOrDefault("read", true);
            
            log.info("메일 읽음 처리: 사용자={}, 메일UID={}, 읽음={}", user.getMailUid(), mailId, read);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 플래그 변경
            String flagType = "SEEN";
            mailManager.switchMessagesFlags(new long[]{mailId}, folderName, flagType, read);
            
            return ResponseEntity.ok(ApiResponse.success("메일 읽음 처리 성공"));
            
        } catch (Exception e) {
            log.error("메일 읽음 처리 실패: mailId={}, error={}", mailId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MAIL_READ_ERROR", "읽음 처리 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * 메일 삭제
     * 
     * DWR: MailMessageService.deleteMessages()
     * 
     * @param requestBody 요청 데이터 (mailIds, folderName)
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteMessages(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            @SuppressWarnings("unchecked")
            List<Number> mailIdsList = (List<Number>) requestBody.get("mailIds");
            long[] mailIds = mailIdsList.stream().mapToLong(Number::longValue).toArray();
            String folderName = (String) requestBody.get("folderName");
            
            log.info("메일 삭제: 사용자={}, 폴더={}, 개수={}", user.getMailUid(), folderName, mailIds.length);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 메일 삭제
            mailManager.deleteMessage(mailIds, folderName);
            
            return ResponseEntity.ok(ApiResponse.success("메일 삭제 성공"));
            
        } catch (Exception e) {
            log.error("메일 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MAIL_DELETE_ERROR", "메일 삭제 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * 메일 이동
     * 
     * DWR: MailMessageService.moveMessages()
     * 
     * @param requestBody 요청 데이터 (mailIds, fromFolder, toFolder)
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @PatchMapping("/move")
    public ResponseEntity<ApiResponse<String>> moveMessages(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            @SuppressWarnings("unchecked")
            List<Number> mailIdsList = (List<Number>) requestBody.get("mailIds");
            long[] mailIds = mailIdsList.stream().mapToLong(Number::longValue).toArray();
            String fromFolder = (String) requestBody.get("fromFolder");
            String toFolder = (String) requestBody.get("toFolder");
            
            log.info("메일 이동: 사용자={}, {}→{}, 개수={}", user.getMailUid(), fromFolder, toFolder, mailIds.length);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 메일 이동 (deleteMessage 후 다른 폴더로 copy)
            TMailFolder fromFld = mailManager.getFolder(fromFolder);
            TMailFolder toFld = mailManager.getFolder(toFolder);
            
            fromFld.open(true);
            toFld.open(true);
            
            TMailMessage[] messages = fromFld.getMessagesByUID(mailIds);
            fromFld.copyMessages(messages, toFld);
            mailManager.deleteMessage(mailIds, fromFolder);
            
            return ResponseEntity.ok(ApiResponse.success("메일 이동 성공"));
            
        } catch (Exception e) {
            log.error("메일 이동 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MAIL_MOVE_ERROR", "메일 이동 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * 메일 플래그 변경
     * 
     * DWR: MailMessageService.switchMessagesFlags()
     * 
     * @param requestBody 요청 데이터
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @PatchMapping("/flags")
    public ResponseEntity<ApiResponse<String>> switchFlags(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            @SuppressWarnings("unchecked")
            List<Number> mailIdsList = (List<Number>) requestBody.get("mailIds");
            long[] mailIds = mailIdsList.stream().mapToLong(Number::longValue).toArray();
            String folderName = (String) requestBody.get("folderName");
            String flagType = (String) requestBody.get("flagType");
            Boolean flagValue = (Boolean) requestBody.get("flagValue");
            
            log.info("메일 플래그 변경: 사용자={}, 플래그={}, 값={}", user.getMailUid(), flagType, flagValue);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 플래그 변경
            mailManager.switchMessagesFlags(mailIds, folderName, flagType, flagValue);
            
            return ResponseEntity.ok(ApiResponse.success("플래그 변경 성공"));
            
        } catch (Exception e) {
            log.error("플래그 변경 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FLAG_ERROR", "플래그 변경 실패: " + e.getMessage()));
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (Exception e) {
                    log.warn("Store 종료 실패: {}", e.getMessage());
                }
            }
        }
    }
}
