package com.terracetech.tims.webmail.organization.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.organization.vo.MemberVO;

import jakarta.servlet.http.HttpSession;

/**
 * 조직도 REST API Controller
 * 
 * <p>DWR OrganizationService를 REST API로 전환하여 실제 OrganizationManager 호출</p>
 * 
 * <h3>제공 API 목록:</h3>
 * <ul>
 *   <li>GET /api/organization/tree - 조직도 트리 조회 (전체 계층 구조)</li>
 *   <li>GET /api/organization/dept/{orgCode} - 부서 상세 정보 조회</li>
 *   <li>GET /api/organization/dept/{orgCode}/members - 부서원 목록 조회</li>
 *   <li>GET /api/organization/dept/{orgCode}/children - 하위 부서 목록 조회</li>
 *   <li>GET /api/organization/dept/search - 부서 검색</li>
 *   <li>GET /api/organization/search - 사용자 검색</li>
 *   <li>GET /api/organization/user/{memberSeq} - 사용자 상세 정보 조회</li>
 * </ul>
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>조직도 계층 구조 조회 (트리 형태)</li>
 *   <li>부서별 인원 조회 (하위 부서 포함 옵션)</li>
 *   <li>사용자/부서 검색</li>
 *   <li>OrganizationManager를 통한 실제 데이터 처리</li>
 * </ul>
 * 
 * @author AI Assistant
 * @since 2025-10-21
 * @see OrganizationManager
 * @see OrganizationService (DWR 원본)
 */
@RestController
@RequestMapping("/api/organization")
public class OrganizationApiController {
    
    private static final Logger log = LoggerFactory.getLogger(OrganizationApiController.class);
    
    @Autowired
    private OrganizationManager organizationManager;
    
    /**
     * 조직도 트리 조회 API
     * 
     * <p>지정된 부서부터 하위 조직도 전체를 트리 구조로 조회합니다.</p>
     * 
     * <h4>처리 흐름:</h4>
     * <ol>
     *   <li>사용자 인증 확인</li>
     *   <li>orgCode가 없으면 루트 부서 코드 조회</li>
     *   <li>organizationManager.readOrganizationTree() 호출</li>
     *   <li>계층 구조 트리 반환</li>
     * </ol>
     * 
     * @param orgCode 조회 시작 부서 코드 (없으면 루트부터)
     * @param session HTTP 세션
     * @return 조직도 트리 (DeptVO, 하위 부서 재귀 포함)
     */
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<DeptVO>> getOrganizationTree(
            @RequestParam(required = false) String orgCode,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("조직도 트리 조회: 사용자={}, 부서코드={}", user.getMailUid(), orgCode);
            
            // orgCode가 없으면 루트 부서 코드 조회
            if (orgCode == null || orgCode.isEmpty()) {
                orgCode = organizationManager.readRootDept(domainSeq);
            }
            
            // 조직도 트리 조회 (재귀적으로 하위 부서 모두 포함)
            // - domainSeq: 메일 도메인 일련번호
            // - orgCode: 조회 시작 부서 코드
            DeptVO tree = organizationManager.readOrganizationTree(domainSeq, orgCode);
            
            return ResponseEntity.ok(ApiResponse.success("조직도 트리 조회 성공", tree));
            
        } catch (Exception e) {
            log.error("조직도 트리 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("TREE_ERROR", "조직도 트리 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 부서 상세 조회
     */
    @GetMapping("/dept/{orgCode}")
    public ResponseEntity<ApiResponse<DeptVO>> getDepartment(
            @PathVariable String orgCode,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("부서 상세 조회: 사용자={}, 부서코드={}", user.getMailUid(), orgCode);
            
            DeptVO dept = organizationManager.readDept(domainSeq, orgCode);
            
            if (dept == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("NOT_FOUND", "부서를 찾을 수 없습니다"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("부서 조회 성공", dept));
            
        } catch (Exception e) {
            log.error("부서 상세 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("DEPT_ERROR", "부서 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 부서원 목록 조회
     */
    @GetMapping("/dept/{orgCode}/members")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDepartmentMembers(
            @PathVariable String orgCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "false") boolean includeSubDept,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String codeLocale = user.getLocale();
            
            log.info("부서원 목록 조회: 사용자={}, 부서코드={}", user.getMailUid(), orgCode);
            
            // 부서 정보 조회
            DeptVO dept = organizationManager.readDept(domainSeq, orgCode);
            String orgFullCode = dept != null ? dept.getOrgFullCode() : orgCode;
            
            // 전체 인원수
            int totalCount = organizationManager.readMemberCount(
                codeLocale, orgCode, orgFullCode, includeSubDept, domainSeq, "", "");
            
            // 멤버 목록 조회
            MemberVO[] members = organizationManager.readMemberList(
                codeLocale, orgCode, orgFullCode, includeSubDept, domainSeq,
                page, pageSize, "", "", "", "");
            
            Map<String, Object> result = new HashMap<>();
            result.put("members", members);
            result.put("totalCount", totalCount);
            result.put("page", page);
            result.put("pageSize", pageSize);
            
            return ResponseEntity.ok(ApiResponse.success("부서원 목록 조회 성공", result));
            
        } catch (Exception e) {
            log.error("부서원 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MEMBERS_ERROR", "부서원 목록 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 사용자 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MemberVO>>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "name") String searchType,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String codeLocale = user.getLocale();
            String rootCode = organizationManager.readRootDept(domainSeq);
            
            log.info("사용자 검색: 사용자={}, 키워드={}", user.getMailUid(), keyword);
            
            // 루트부터 전체 검색 (하위 포함)
            DeptVO root = organizationManager.readDept(domainSeq, rootCode);
            String orgFullCode = root != null ? root.getOrgFullCode() : rootCode;
            
            MemberVO[] members = organizationManager.readMemberList(
                codeLocale, rootCode, orgFullCode, true, domainSeq,
                1, 100, searchType, keyword, "", "");
            
            List<MemberVO> memberList = new ArrayList<>();
            if (members != null) {
                for (MemberVO member : members) {
                    memberList.add(member);
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("사용자 검색 성공", memberList));
            
        } catch (Exception e) {
            log.error("사용자 검색 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SEARCH_ERROR", "사용자 검색 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 사용자 상세 조회
     */
    @GetMapping("/user/{memberSeq}")
    public ResponseEntity<ApiResponse<MemberVO>> getUser(
            @PathVariable int memberSeq,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String codeLocale = user.getLocale();
            
            log.info("사용자 상세 조회: 요청자={}, 대상ID={}", user.getMailUid(), memberSeq);
            
            MemberVO member = organizationManager.readMember(codeLocale, "", domainSeq, memberSeq);
            
            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("NOT_FOUND", "사용자를 찾을 수 없습니다"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("사용자 조회 성공", member));
            
        } catch (Exception e) {
            log.error("사용자 상세 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("USER_ERROR", "사용자 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 하위 부서 목록 조회
     */
    @GetMapping("/dept/{orgCode}/children")
    public ResponseEntity<ApiResponse<List<DeptVO>>> getDeptChildren(
            @PathVariable String orgCode,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("하위 부서 목록 조회: 사용자={}, 부서코드={}", user.getMailUid(), orgCode);
            
            List<DeptVO> children = organizationManager.readDeptChildList(domainSeq, orgCode);
            
            return ResponseEntity.ok(ApiResponse.success("하위 부서 조회 성공", children));
            
        } catch (Exception e) {
            log.error("하위 부서 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("CHILDREN_ERROR", "하위 부서 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 부서 검색
     */
    @GetMapping("/dept/search")
    public ResponseEntity<ApiResponse<List<DeptVO>>> searchDept(
            @RequestParam String keyword,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("부서 검색: 사용자={}, 키워드={}", user.getMailUid(), keyword);
            
            List<DeptVO> depts = organizationManager.findDept(domainSeq, keyword);
            
            return ResponseEntity.ok(ApiResponse.success("부서 검색 성공", depts));
            
        } catch (Exception e) {
            log.error("부서 검색 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("DEPT_SEARCH_ERROR", "부서 검색 실패: " + e.getMessage()));
        }
    }
}
