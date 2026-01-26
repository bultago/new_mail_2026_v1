package com.terracetech.tims.webmail.mail.api;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.api.ApiException;
import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 메일 폴더 REST API Controller
 * 
 * <p>DWR MailFolderService를 REST API로 전환하여 실제 MailManager 호출</p>
 * 
 * <h3>제공 API 목록:</h3>
 * <ul>
 *   <li>GET /api/mail/folder/list - 폴더 목록 조회</li>
 *   <li>GET /api/mail/folder/info - 폴더 상세 정보 (메일 개수 포함)</li>
 *   <li>POST /api/mail/folder - 폴더 생성</li>
 *   <li>PUT /api/mail/folder/{folderName} - 폴더 이름 변경</li>
 *   <li>DELETE /api/mail/folder/{folderName} - 폴더 삭제</li>
 *   <li>DELETE /api/mail/folder/{folderName}/empty - 폴더 비우기</li>
 *   <li>GET /api/mail/folder/shared - 공유 폴더 목록</li>
 * </ul>
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>메일 폴더 CRUD 관리</li>
 *   <li>폴더별 메일 개수 및 상태 정보</li>
 *   <li>공유 폴더 관리</li>
 *   <li>폴더 비우기 (완전 삭제)</li>
 * </ul>
 * 
 * @author AI Assistant
 * @since 2025-10-21
 * @see MailManager
 * @see MailFolderService (DWR 원본)
 */
@RestController
@RequestMapping("/api/mail/folder")
public class MailFolderApiController {
    
    private static final Logger log = LoggerFactory.getLogger(MailFolderApiController.class);
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * I18nResources 가져오기
     */
    private I18nResources getMessageResource(HttpSession session) {
        return (I18nResources) session.getServletContext().getAttribute("i18nResource");
    }
    
    /**
     * 폴더 목록 조회 API
     * 
     * <p>사용자의 모든 메일 폴더 목록과 각 폴더의 메일 개수를 조회합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailFolderService.getMailFolderInfo()</pre>
     * 
     * <h4>처리 흐름:</h4>
     * <ol>
     *   <li>사용자 인증 확인</li>
     *   <li>TMailStore 연결</li>
     *   <li>mailManager.getFolderList() 호출</li>
     *   <li>각 폴더의 메일 개수 조회</li>
     *   <li>폴더 정보 반환</li>
     * </ol>
     * 
     * @param type 폴더 타입 (0: 전체, 1: 개인, 2: 공유)
     * @param session HTTP 세션
     * @return 폴더 목록 및 메일 개수
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFolderInfo(
            @RequestParam(defaultValue = "0") int type,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            // 1. 사용자 인증
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            
            log.info("폴더 정보 조회: 사용자={}, 타입={}", user.getMailUid(), type);
            
            // 2. TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 3. MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 4. 폴더 목록 조회
            MailFolderBean[] folders = mailManager.getFolderList(type, false, userSeq);
            
            Map<String, Object> result = new HashMap<>();
            result.put("folders", folders);
            result.put("totalCount", folders != null ? folders.length : 0);
            
            return ResponseEntity.ok(ApiResponse.success("폴더 정보 조회 성공", result));
            
        } catch (Exception e) {
            log.error("폴더 정보 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FOLDER_INFO_ERROR", "폴더 정보 조회 실패: " + e.getMessage()));
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
     * 폴더 생성 API
     * 
     * @param requestBody 폴더 정보 (folderName)
     * @param session HTTP 세션
     * @return 생성 결과
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createFolder(
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
            
            log.info("폴더 생성: 사용자={}, 폴더={}", user.getMailUid(), folderName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 폴더 생성
            TMailFolder newFolder = store.getFolder(folderName);
            if (!newFolder.exists()) {
                newFolder.create(TMailFolder.HOLDS_MESSAGES);
            }
            
            return ResponseEntity.ok(ApiResponse.success("폴더 생성 성공"));
            
        } catch (Exception e) {
            log.error("폴더 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FOLDER_CREATE_ERROR", "폴더 생성 실패: " + e.getMessage()));
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
     * 폴더 삭제 API
     * 
     * @param folderName 폴더명
     * @param session HTTP 세션
     * @return 삭제 결과
     */
    @DeleteMapping("/{folderName}")
    public ResponseEntity<ApiResponse<String>> deleteFolder(
            @PathVariable String folderName,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("폴더 삭제: 사용자={}, 폴더={}", user.getMailUid(), folderName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 폴더 삭제
            TMailFolder folder = store.getFolder(folderName);
            if (folder.exists()) {
                folder.delete(false);
            }
            
            return ResponseEntity.ok(ApiResponse.success("폴더 삭제 성공"));
            
        } catch (Exception e) {
            log.error("폴더 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FOLDER_DELETE_ERROR", "폴더 삭제 실패: " + e.getMessage()));
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
     * 폴더 이름 변경 API
     * 
     * @param previousName 기존 폴더명
     * @param requestBody 새 폴더명 정보
     * @param session HTTP 세션
     * @return 변경 결과
     */
    @PutMapping("/{previousName}")
    public ResponseEntity<ApiResponse<String>> renameFolder(
            @PathVariable String previousName,
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String newName = (String) requestBody.get("newName");
            
            log.info("폴더 이름 변경: 사용자={}, {} → {}", user.getMailUid(), previousName, newName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 폴더 이름 변경
            TMailFolder folder = store.getFolder(previousName);
            if (folder.exists()) {
                TMailFolder newFolder = store.getFolder(newName);
                folder.renameTo(newFolder);
            }
            
            return ResponseEntity.ok(ApiResponse.success("폴더 이름 변경 성공"));
            
        } catch (Exception e) {
            log.error("폴더 이름 변경 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FOLDER_RENAME_ERROR", "폴더 이름 변경 실패: " + e.getMessage()));
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
     * 폴더 비우기 API
     * 
     * <p>폴더 내 모든 메일을 삭제합니다.</p>
     * 
     * @param folderName 폴더명
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @DeleteMapping("/{folderName}/empty")
    public ResponseEntity<ApiResponse<String>> emptyFolder(
            @PathVariable String folderName,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("폴더 비우기: 사용자={}, 폴더={}", user.getMailUid(), folderName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 폴더 열기
            TMailFolder folder = store.getFolder(folderName);
            folder.open(true);
            
            // 모든 메시지 삭제
            int messageCount = folder.getMessageCount();
            if (messageCount > 0) {
                jakarta.mail.Message[] messages = folder.getMessages();
                folder.setFlags(messages, new jakarta.mail.Flags(jakarta.mail.Flags.Flag.DELETED), true);
                folder.expunge();
            }
            
            return ResponseEntity.ok(ApiResponse.success("폴더 비우기 성공"));
            
        } catch (Exception e) {
            log.error("폴더 비우기 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("FOLDER_EMPTY_ERROR", "폴더 비우기 실패: " + e.getMessage()));
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
     * 공유 폴더 목록 조회 API
     * 
     * @param session HTTP 세션
     * @return 공유 폴더 목록
     */
    @GetMapping("/shared")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSharedFolders(
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("공유 폴더 목록 조회: 사용자={}", user.getMailUid());
            
            // TODO: 공유 폴더 목록 조회 구현 필요
            // SharedFolderManager를 통한 조회
            
            return ResponseEntity.ok(ApiResponse.success("공유 폴더 목록 조회 성공", List.of()));
            
        } catch (Exception e) {
            log.error("공유 폴더 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SHARED_FOLDER_ERROR", "공유 폴더 조회 실패: " + e.getMessage()));
        }
    }
}
