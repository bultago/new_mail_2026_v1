package com.terracetech.tims.webmail.scheduler.api;

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

import com.terracetech.tims.webmail.common.api.ApiResponse;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

import jakarta.servlet.http.HttpSession;

/**
 * 일정 REST API Controller
 * 
 * <p>DWR SchedulerService를 REST API로 전환하여 실제 SchedulerManager 호출</p>
 * 
 * <h3>제공 API 목록:</h3>
 * <ul>
 *   <li>GET /api/schedule/list - 기간별 일정 목록 조회</li>
 *   <li>GET /api/schedule/month - 월별 일정 조회</li>
 *   <li>GET /api/schedule/week - 주간 일정 조회</li>
 *   <li>GET /api/schedule/day - 일별 일정 조회</li>
 *   <li>GET /api/schedule/{scheduleId} - 일정 상세 조회</li>
 *   <li>POST /api/schedule - 일정 등록</li>
 *   <li>PUT /api/schedule/{scheduleId} - 일정 수정</li>
 *   <li>DELETE /api/schedule/{scheduleId} - 일정 삭제</li>
 * </ul>
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>월/주/일별 일정 조회 (SchedulerManager 직접 호출)</li>
 *   <li>일정 CRUD 처리</li>
 *   <li>반복 일정 지원</li>
 *   <li>공유 일정 처리</li>
 * </ul>
 * 
 * @author AI Assistant
 * @since 2025-10-21
 * @see SchedulerManager
 * @see SchedulerService (DWR 원본)
 */
@RestController
@RequestMapping("/api/schedule")
public class SchedulerApiController {
    
    private static final Logger log = LoggerFactory.getLogger(SchedulerApiController.class);
    
    @Autowired
    private SchedulerManager schedulerManager;
    
    /**
     * 일정 목록 조회 API
     * 
     * <p>지정된 기간의 일정 목록을 조회합니다.</p>
     * 
     * <h4>처리 흐름:</h4>
     * <ol>
     *   <li>사용자 인증 확인</li>
     *   <li>startDate에서 년/월/일 파싱</li>
     *   <li>schedulerManager.getMonthScheduleList() 호출</li>
     *   <li>일정 목록 반환</li>
     * </ol>
     * 
     * @param startDate 시작일 (YYYY-MM-DD 형식)
     * @param endDate 종료일 (YYYY-MM-DD 형식)
     * @param session HTTP 세션
     * @return 일정 목록 및 개수
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getScheduleList(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String email = user.getMailUid();
            
            log.info("일정 목록 조회: 사용자={}, 기간={} ~ {}", email, startDate, endDate);
            
            // startDate 파싱 (YYYY-MM-DD → year, month, day)
            String[] parts = startDate.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            // 월별 일정 조회
            // - year, month: 조회 대상 년월
            // - mailDomainSeq, mailUserSeq: 사용자 식별
            // - email: 사용자 이메일 주소
            List<SchedulerDataVO> schedules = schedulerManager.getMonthScheduleList(
                year, month, mailDomainSeq, mailUserSeq, email);
            
            Map<String, Object> result = new HashMap<>();
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("schedules", schedules);
            result.put("totalCount", schedules.size());
            
            return ResponseEntity.ok(ApiResponse.success("일정 목록 조회 성공", result));
            
        } catch (Exception e) {
            log.error("일정 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LIST_ERROR", "일정 목록 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 일정 상세 조회
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<SchedulerDataVO>> getSchedule(
            @PathVariable int scheduleId,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String orgLocale = user.getLocale();
            
            log.info("일정 상세 조회: 사용자={}, 일정ID={}", user.getMailUid(), scheduleId);
            
            SchedulerDataVO schedule = schedulerManager.getSchedule(
                mailDomainSeq, scheduleId, orgLocale, "");
            
            if (schedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("NOT_FOUND", "일정을 찾을 수 없습니다"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("일정 조회 성공", schedule));
            
        } catch (Exception e) {
            log.error("일정 상세 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("DETAIL_ERROR", "일정 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 일정 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createSchedule(
            @RequestBody SchedulerDataVO schedule,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("일정 등록: 사용자={}, 제목={}", user.getMailUid(), schedule.getSubject());
            
            schedule.setMailUserSeq(mailUserSeq);
            schedule.setMailDomainSeq(mailDomainSeq);
            
            Map<String, Object> result = schedulerManager.saveSchedule(schedule);
            
            return ResponseEntity.ok(ApiResponse.success("일정 등록 성공", result));
            
        } catch (Exception e) {
            log.error("일정 등록 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("CREATE_ERROR", "일정 등록 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 일정 수정
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateSchedule(
            @PathVariable int scheduleId,
            @RequestBody SchedulerDataVO schedule,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            
            log.info("일정 수정: 사용자={}, 일정ID={}", user.getMailUid(), scheduleId);
            
            schedule.setSchedulerId(scheduleId);
            schedule.setMailUserSeq(mailUserSeq);
            schedule.setMailDomainSeq(mailDomainSeq);
            
            Map<String, Object> result = schedulerManager.modifySchedule(schedule);
            
            return ResponseEntity.ok(ApiResponse.success("일정 수정 성공", result));
            
        } catch (Exception e) {
            log.error("일정 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("UPDATE_ERROR", "일정 수정 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 일정 삭제
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<String>> deleteSchedule(
            @PathVariable int scheduleId,
            @RequestParam(defaultValue = "N") String sendMail,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            log.info("일정 삭제: 사용자={}, 일정ID={}", user.getMailUid(), scheduleId);
            
            String result = schedulerManager.deleteSchedule(scheduleId, sendMail);
            
            return ResponseEntity.ok(ApiResponse.success("일정 삭제 성공: " + result));
            
        } catch (Exception e) {
            log.error("일정 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("DELETE_ERROR", "일정 삭제 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 월별 일정 조회
     */
    @GetMapping("/month")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlySchedule(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String email = user.getMailUid();
            
            log.info("월별 일정 조회: 사용자={}, {}년 {}월", email, year, month);
            
            List<SchedulerDataVO> schedules = schedulerManager.getMonthScheduleList(
                year, month, mailDomainSeq, mailUserSeq, email);
            
            Map<String, Object> result = new HashMap<>();
            result.put("year", year);
            result.put("month", month);
            result.put("schedules", schedules);
            result.put("totalCount", schedules.size());
            
            return ResponseEntity.ok(ApiResponse.success("월별 일정 조회 성공", result));
            
        } catch (Exception e) {
            log.error("월별 일정 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("MONTHLY_ERROR", "월별 일정 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 주간 일정 조회
     */
    @GetMapping("/week")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWeeklySchedule(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String email = user.getMailUid();
            
            log.info("주간 일정 조회: 사용자={}, {}년 {}월 {}일", email, year, month, day);
            
            List<SchedulerDataVO> schedules = schedulerManager.getWeekScheduleList(
                year, month, day, mailDomainSeq, mailUserSeq, email, "", "");
            
            Map<String, Object> result = new HashMap<>();
            result.put("year", year);
            result.put("month", month);
            result.put("day", day);
            result.put("schedules", schedules);
            result.put("totalCount", schedules.size());
            
            return ResponseEntity.ok(ApiResponse.success("주간 일정 조회 성공", result));
            
        } catch (Exception e) {
            log.error("주간 일정 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("WEEKLY_ERROR", "주간 일정 조회 실패: " + e.getMessage()));
        }
    }
    
    /**
     * 일별 일정 조회
     */
    @GetMapping("/day")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailySchedule(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute(User.USER_KEY);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("UNAUTHORIZED", "로그인이 필요합니다"));
            }
            
            int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
            int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
            String email = user.getMailUid();
            
            log.info("일별 일정 조회: 사용자={}, {}년 {}월 {}일", email, year, month, day);
            
            List<SchedulerDataVO> schedules = schedulerManager.getDayScheduleList(
                year, month, day, mailDomainSeq, mailUserSeq, email, "", "");
            
            Map<String, Object> result = new HashMap<>();
            result.put("year", year);
            result.put("month", month);
            result.put("day", day);
            result.put("schedules", schedules);
            result.put("totalCount", schedules.size());
            
            return ResponseEntity.ok(ApiResponse.success("일별 일정 조회 성공", result));
            
        } catch (Exception e) {
            log.error("일별 일정 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("DAILY_ERROR", "일별 일정 조회 실패: " + e.getMessage()));
        }
    }
}
