package com.terracetech.tims.webmail.mail.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.api.ApiException;
import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.manager.LetterManager;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;

/**
 * 메일 공통 기능 REST API Controller
 * 
 * <p>편지지, 자동 저장, 주소 검색 등 공통 기능을 제공합니다.</p>
 * 
 * <h3>주요 API 목록</h3>
 * <ul>
 *   <li>GET /mail/common/letter/list - 편지지 목록 조회</li>
 *   <li>POST /mail/common/autosave - 자동 저장 설정 업데이트</li>
 *   <li>POST /mail/common/search/address - 주소 키워드 검색</li>
 *   <li>POST /mail/common/search/account - 계정 DN 검색</li>
 * </ul>
 * 
 * @author Phase 3.5 Migration
 * @since 2025-10-21
 */
@RestController
@RequestMapping("/mail/common")
public class MailCommonApiController {
    
    private static final Logger log = LoggerFactory.getLogger(MailCommonApiController.class);
    
    @Autowired
    private LetterManager letterManager;
    
    @Autowired
    private SettingManager settingManager;
    
    @Autowired
    private MailManager mailManager;
    
    @Autowired
    private MailUserManager mailUserManager;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private ServletContext context;
    
    /**
     * 편지지 목록 조회 API
     * 
     * <p>사용 가능한 편지지 목록을 페이징하여 조회합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailCommonService.getLetterList(page)</pre>
     * 
     * @param page 페이지 번호
     * @param session HTTP 세션
     * @return 편지지 목록 및 페이징 정보
     */
    @GetMapping("/letter/list")
    public ResponseEntity<ApiResponse<JSONObject>> getLetterList(
            @RequestParam(defaultValue = "1") int page,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("편지지 목록 조회: 사용자={}, 페이지={}", user.getMailUid(), page);
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            JSONObject jObj = new JSONObject();
            
            int letterCount = 6;
            int startPos = (page - 1) * letterCount;
            int totalCount = letterManager.getTotalCount(domainSeq);
            
            // 페이징 정보 설정
            if (startPos < totalCount && totalCount > (startPos + letterCount)) {
                jObj.put("next", page + 1);
            } else {
                jObj.put("next", -1);
            }
            
            if (startPos < totalCount && startPos > 0) {
                jObj.put("pre", page - 1);
            } else {
                jObj.put("pre", -1);
            }
            
            // 편지지 경로 설정
            String strLocalhost = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String letterPath = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
            String letterUrl = strLocalhost + "/design/common/image/attaches/";
            
            jObj.put("page", page);
            jObj.put("list", letterManager.getLetterJsonList(domainSeq, startPos, letterCount, letterPath, letterUrl));
            
            return ResponseEntity.ok(ApiResponse.success("편지지 목록 조회 성공", jObj));
            
        } catch (Exception e) {
            log.error("편지지 목록 조회 실패: page={}, error={}", page, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LETTER_LIST_ERROR", "편지지 목록 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 자동 저장 설정 업데이트 API
     * 
     * <p>메일 작성 시 자동 저장 기능 설정을 업데이트합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailCommonService.updateAutoSaveInfo(mode, term)</pre>
     * 
     * @param requestBody 요청 데이터 (mode, term)
     * @param session HTTP 세션
     * @return 설정 정보
     */
    @PostMapping("/autosave")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateAutoSaveInfo(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            String mode = (String) requestBody.get("mode");
            int term = (Integer) requestBody.get("term");
            
            log.info("자동 저장 설정 업데이트: 사용자={}, mode={}, term={}", 
                user.getMailUid(), mode, term);
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            settingManager.setAutoSaveInfo(mailUserSeq, term, mode);
            
            Map<String, Object> result = new HashMap<>();
            result.put("term", term);
            result.put("result", "success");
            
            return ResponseEntity.ok(ApiResponse.success("자동 저장 설정 업데이트 성공", result));
            
        } catch (Exception e) {
            log.error("자동 저장 설정 업데이트 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("AUTOSAVE_UPDATE_ERROR", "자동 저장 설정 업데이트 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 주소 키워드 검색 API
     * 
     * <p>입력한 키워드로 주소록 및 조직도에서 주소를 검색합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailCommonService.searchAddressByKeyowrd(keywords, isNotOrgSearch)</pre>
     * 
     * @param requestBody 요청 데이터 (keywords, isNotOrgSearch)
     * @param session HTTP 세션
     * @return 검색 결과
     */
    @PostMapping("/search/address")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchAddressByKeyword(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            @SuppressWarnings("unchecked")
            List<String> keywordList = (List<String>) requestBody.get("keywords");
            String[] keywords = keywordList.toArray(new String[0]);
            boolean isNotOrgSearch = (Boolean) requestBody.getOrDefault("isNotOrgSearch", false);
            
            log.info("주소 키워드 검색: 사용자={}, 키워드 수={}", user.getMailUid(), keywords.length);
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String locale = user.get(User.LOCALE);
            
            MailAddressBean[] addrs = mailManager.getUserMailAddressList(
                mailUserSeq, mailDomainSeq, locale, 15, keywords, true, isNotOrgSearch);
            
            List<Map<String, String>> addressList = new ArrayList<>();
            for (MailAddressBean addr : addrs) {
                Map<String, String> addrMap = new HashMap<>();
                addrMap.put("address", addr.getAddress());
                addrMap.put("name", addr.getName());
                addrMap.put("mailuid", addr.getMailUid());
                addressList.add(addrMap);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("total", addressList.size());
            result.put("list", addressList);
            
            return ResponseEntity.ok(ApiResponse.success("주소 검색 성공", result));
            
        } catch (Exception e) {
            log.error("주소 키워드 검색 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("ADDRESS_SEARCH_ERROR", "주소 검색 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 계정 DN 검색 API
     * 
     * <p>이메일 주소로 계정의 DN(Distinguished Name)을 검색합니다.</p>
     * 
     * <h4>원본 DWR 메서드:</h4>
     * <pre>MailCommonService.searchAccountDN(emails)</pre>
     * 
     * @param requestBody 요청 데이터 (emails)
     * @param session HTTP 세션
     * @return DN 검색 결과
     */
    @PostMapping("/search/account")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchAccountDN(
            @RequestBody Map<String, Object> requestBody,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            @SuppressWarnings("unchecked")
            List<String> emailList = (List<String>) requestBody.get("emails");
            String[] emails = emailList.toArray(new String[0]);
            
            log.info("계정 DN 검색: 사용자={}, 이메일 수={}", user.getMailUid(), emails.length);
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            List<Map<String, String>> accountList = new ArrayList<>();
            for (String email : emails) {
                Map<String, String> account = mailUserManager.searchAccountDN(domainSeq, email);
                if (account != null) {
                    accountList.add(account);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("total", accountList.size());
            result.put("list", accountList);
            
            return ResponseEntity.ok(ApiResponse.success("계정 DN 검색 성공", result));
            
        } catch (Exception e) {
            log.error("계정 DN 검색 실패: error={}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("ACCOUNT_SEARCH_ERROR", "계정 DN 검색 실패: " + e.getMessage()));
        }
    }
}

