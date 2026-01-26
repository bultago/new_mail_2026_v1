package com.terracetech.tims.webmail.mail.api;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONArray;
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
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.api.ApiException;
import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

/**
 * 메일 검색 폴더 REST API Controller
 * 
 * <p>메일 검색 폴더 생성, 수정, 삭제, 조회 기능을 제공합니다.</p>
 * 
 * <h3>주요 API 목록</h3>
 * <ul>
 *   <li>GET /mail/search-folder/list - 검색 폴더 목록 조회</li>
 *   <li>POST /mail/search-folder - 검색 폴더 추가</li>
 *   <li>PUT /mail/search-folder/{folderId} - 검색 폴더 수정</li>
 *   <li>DELETE /mail/search-folder - 검색 폴더 삭제</li>
 * </ul>
 * 
 * @author Phase 3.5 Migration
 * @since 2025-10-21
 */
@RestController
@RequestMapping("/mail/search-folder")
public class MailSearchFolderApiController {
    
    private static final Logger log = LoggerFactory.getLogger(MailSearchFolderApiController.class);
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * I18n 리소스 조회
     */
    private I18nResources getMessageResource(HttpSession session) {
        return (I18nResources) session.getAttribute("i18nResource");
    }
    
    /**
     * 검색 폴더 목록 조회 API
     * 
     * <p>사용자의 모든 검색 폴더 목록을 조회합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailSearchFolderService.getFolderList()</pre>
     * 
     * @param session HTTP 세션
     * @return 검색 폴더 목록
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<JSONArray>> getFolderList(HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("검색 폴더 목록 조회: 사용자={}", user.getMailUid());
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 검색 폴더 목록 조회
            JSONArray list = mailManager.getJsonSearchFolders(null);
            
            return ResponseEntity.ok(ApiResponse.success("검색 폴더 목록 조회 성공", list));
            
        } catch (Exception e) {
            log.error("검색 폴더 목록 조회 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SEARCH_FOLDER_LIST_ERROR", "검색 폴더 목록 조회 실패: " + e.getMessage()));
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
     * 검색 폴더 추가 API
     * 
     * <p>새로운 검색 폴더를 생성합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailSearchFolderService.addSearchFolder(folderName, query)</pre>
     * 
     * @param requestBody 요청 데이터 (folderName, query)
     * @param session HTTP 세션
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addSearchFolder(
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
            String query = (String) requestBody.get("query");
            
            log.info("검색 폴더 추가: 사용자={}, 폴더명={}", user.getMailUid(), folderName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 검색 폴더 추가
            mailManager.addSearchFolder(folderName, query);
            
            return ResponseEntity.ok(ApiResponse.success("검색 폴더 추가 성공", null));
            
        } catch (Exception e) {
            log.error("검색 폴더 추가 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SEARCH_FOLDER_ADD_ERROR", "검색 폴더 추가 실패: " + e.getMessage()));
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
     * 검색 폴더 수정 API
     * 
     * <p>기존 검색 폴더를 수정합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailSearchFolderService.modifySearchFolder(oldId, folderName, query)</pre>
     * 
     * @param folderId 폴더 ID
     * @param requestBody 요청 데이터 (folderName, query)
     * @param session HTTP 세션
     * @return 성공 메시지
     */
    @PutMapping("/{folderId}")
    public ResponseEntity<ApiResponse<Void>> modifySearchFolder(
            @PathVariable String folderId,
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
            String query = (String) requestBody.get("query");
            
            log.info("검색 폴더 수정: 사용자={}, 폴더ID={}, 폴더명={}", 
                user.getMailUid(), folderId, folderName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 검색 폴더 수정
            mailManager.modifySearchFolder(folderId, folderName, query);
            
            return ResponseEntity.ok(ApiResponse.success("검색 폴더 수정 성공", null));
            
        } catch (Exception e) {
            log.error("검색 폴더 수정 실패: folderId={}, error={}", folderId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SEARCH_FOLDER_MODIFY_ERROR", "검색 폴더 수정 실패: " + e.getMessage()));
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
     * 검색 폴더 삭제 API
     * 
     * <p>선택한 검색 폴더들을 삭제합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailSearchFolderService.deleteSearchFolder(folderIds)</pre>
     * 
     * @param requestBody 요청 데이터 (folderIds)
     * @param session HTTP 세션
     * @return 성공 메시지
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteSearchFolder(
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
            java.util.List<String> folderIdList = (java.util.List<String>) requestBody.get("folderIds");
            String[] folderIds = folderIdList.toArray(new String[0]);
            
            log.info("검색 폴더 삭제: 사용자={}, 개수={}", user.getMailUid(), folderIds.length);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 검색 폴더 삭제
            mailManager.deleteSearchFolder(folderIds);
            
            return ResponseEntity.ok(ApiResponse.success("검색 폴더 삭제 성공", null));
            
        } catch (Exception e) {
            log.error("검색 폴더 삭제 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SEARCH_FOLDER_DELETE_ERROR", "검색 폴더 삭제 실패: " + e.getMessage()));
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

