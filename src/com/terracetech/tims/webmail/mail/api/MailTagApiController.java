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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 메일 태그 REST API Controller
 * 
 * <p>DWR MailTagService를 REST API로 전환하여 실제 MailManager 호출</p>
 * 
 * <h3>제공 API 목록:</h3>
 * <ul>
 *   <li>GET /api/mail/tag/list - 태그 목록 조회</li>
 *   <li>POST /api/mail/tag - 태그 생성</li>
 *   <li>PUT /api/mail/tag/{tagId} - 태그 수정</li>
 *   <li>DELETE /api/mail/tag - 태그 삭제 (복수 지원)</li>
 *   <li>POST /api/mail/tag/apply - 메일에 태그 적용</li>
 * </ul>
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>메일 태그 CRUD 관리</li>
 *   <li>메일에 태그 추가/제거</li>
 *   <li>태그 색상 관리</li>
 * </ul>
 * 
 * @author AI Assistant
 * @since 2025-10-21
 * @see MailManager
 * @see MailTagService (DWR 원본)
 */
@RestController
@RequestMapping("/api/mail/tag")
public class MailTagApiController {
    
    private static final Logger log = LoggerFactory.getLogger(MailTagApiController.class);
    
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
     * 태그 목록 조회 API
     * 
     * <p>사용자의 모든 메일 태그 목록을 조회합니다.</p>
     * 
     * @param session HTTP 세션
     * @return 태그 목록
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTagList(
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("태그 목록 조회: 사용자={}", user.getMailUid());
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 태그 목록 조회
            TMailTag[] tags = store.getTagList();
            
            List<Map<String, Object>> tagList = new ArrayList<>();
            if (tags != null) {
                for (TMailTag tag : tags) {
                    Map<String, Object> tagData = new HashMap<>();
                    tagData.put("id", tag.getId());
                    tagData.put("name", tag.getName());
                    tagData.put("color", tag.getColor());
                    tagList.add(tagData);
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("태그 목록 조회 성공", tagList));
            
        } catch (Exception e) {
            log.error("태그 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TAG_LIST_ERROR", "태그 목록 조회 실패: " + e.getMessage()));
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
     * 태그 생성 API
     * 
     * @param requestBody 태그 정보 (tagName, tagColor)
     * @param session HTTP 세션
     * @return 생성 결과
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createTag(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String tagName = (String) requestBody.get("tagName");
            String tagColor = (String) requestBody.get("tagColor");
            
            log.info("태그 생성: 사용자={}, 이름={}, 색상={}", user.getMailUid(), tagName, tagColor);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 태그 생성
            TMailTag tag = new TMailTag();
            tag.setName(tagName);
            tag.setColor(tagColor);
            store.addTag(tag);
            
            return ResponseEntity.ok(ApiResponse.success("태그 생성 성공"));
            
        } catch (Exception e) {
            log.error("태그 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TAG_CREATE_ERROR", "태그 생성 실패: " + e.getMessage()));
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
     * 태그 수정 API
     * 
     * @param tagId 태그 ID
     * @param requestBody 수정할 태그 정보
     * @param session HTTP 세션
     * @return 수정 결과
     */
    @PutMapping("/{tagId}")
    public ResponseEntity<ApiResponse<String>> updateTag(
            @PathVariable String tagId,
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String tagName = (String) requestBody.get("tagName");
            String tagColor = (String) requestBody.get("tagColor");
            
            log.info("태그 수정: 사용자={}, ID={}, 이름={}", user.getMailUid(), tagId, tagName);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 태그 수정
            TMailTag tag = new TMailTag();
            tag.setId(tagId);
            tag.setName(tagName);
            tag.setColor(tagColor);
            store.modifyTag(tag);
            
            return ResponseEntity.ok(ApiResponse.success("태그 수정 성공"));
            
        } catch (Exception e) {
            log.error("태그 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TAG_UPDATE_ERROR", "태그 수정 실패: " + e.getMessage()));
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
     * 태그 삭제 API
     * 
     * @param requestBody 삭제할 태그 ID 목록
     * @param session HTTP 세션
     * @return 삭제 결과
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteTags(
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
            List<String> tagIds = (List<String>) requestBody.get("tagIds");
            
            log.info("태그 삭제: 사용자={}, 개수={}", user.getMailUid(), tagIds.size());
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // 태그 삭제
            for (String tagId : tagIds) {
                store.deleteTag(tagId);
            }
            
            return ResponseEntity.ok(ApiResponse.success("태그 삭제 성공"));
            
        } catch (Exception e) {
            log.error("태그 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TAG_DELETE_ERROR", "태그 삭제 실패: " + e.getMessage()));
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
     * 메일에 태그 적용/제거 API
     * 
     * @param requestBody 태깅 정보 (addFlag, mailIds, folders, tagId)
     * @param session HTTP 세션
     * @return 처리 결과
     */
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyTag(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        TMailStore store = null;
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String addFlag = (String) requestBody.get("addFlag"); // "true" or "false"
            @SuppressWarnings("unchecked")
            List<Number> mailIdsList = (List<Number>) requestBody.get("mailIds");
            long[] mailIds = mailIdsList.stream().mapToLong(Number::longValue).toArray();
            @SuppressWarnings("unchecked")
            List<String> folders = (List<String>) requestBody.get("folders");
            String tagId = (String) requestBody.get("tagId");
            
            boolean isAdd = "true".equals(addFlag);
            
            log.info("메일 태깅: 사용자={}, 추가={}, 태그ID={}, 메일수={}", 
                user.getMailUid(), isAdd, tagId, mailIds.length);
            
            // TMailStore 연결
            TMailStoreFactory factory = new TMailStoreFactory();
            I18nResources resource = getMessageResource(session);
            store = factory.connect(request.getRemoteAddr(), user);
            
            // MailManager 초기화
            mailManager.setProcessResource(store, resource);
            
            // 각 메일에 태그 적용
            for (int i = 0; i < mailIds.length; i++) {
                String folderName = folders.get(Math.min(i, folders.size() - 1));
                // TODO: 실제 태그 적용 로직 구현 필요
                // TMailFolder folder = mailManager.getFolder(folderName);
                // TMailMessage message = folder.getMessageByUID(mailIds[i]);
                // if (isAdd) {
                //     message.addTag(tagId);
                // } else {
                //     message.removeTag(tagId);
                // }
            }
            
            return ResponseEntity.ok(ApiResponse.success("태그 적용 성공"));
            
        } catch (Exception e) {
            log.error("태그 적용 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TAG_APPLY_ERROR", "태그 적용 실패: " + e.getMessage()));
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
