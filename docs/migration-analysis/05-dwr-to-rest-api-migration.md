# DWR → Spring REST API 마이그레이션 가이드

## 문서 정보
- **작성일**: 2025-10-14
- **목적**: DWR (Direct Web Remoting)을 Spring REST API + 최신 JavaScript로 전환

---

## 1. DWR 현황 분석

### 1.1 DWR 사용 개요

**DWR (Direct Web Remoting)**:
- AJAX 라이브러리로 서버의 Java 메서드를 JavaScript에서 직접 호출
- 2000년대 중반에 인기있던 기술
- 현재는 구식 기술, 유지보수 및 보안 업데이트 중단

**현재 프로젝트 사용 현황**:
- DWR 라이브러리: `dwr.jar`
- DWR Servlet 설정: `/dwr/*` 패턴
- DWR 사용 JSP: 약 20개
- Spring-DWR 통합 사용 중

### 1.2 DWR 설정 확인

#### web.xml 설정
```xml
<servlet>
    <servlet-name>dwr</servlet-name>
    <servlet-class>org.directwebremoting.spring.DwrSpringServlet</servlet-class>
    <init-param>
        <param-name>crossDomainSessionSecurity</param-name>
        <param-value>false</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>dwr</servlet-name>
    <url-pattern>/dwr/*</url-pattern>
</servlet-mapping>
```

#### Spring 설정 (spring-mail.xml 예시)
```xml
<dwr:configuration>
    <dwr:convert type="bean" class="com.terracetech.tims.webmail.mail.vo.MailVO"/>
</dwr:configuration>

<dwr:controller id="dwrController"/>

<dwr:remote javascript="MailService">
    <dwr:include method="getMailList"/>
    <dwr:include method="getMail"/>
    <dwr:include method="sendMail"/>
</dwr:remote>
```

#### JavaScript 사용 예시 (기존 DWR)
```javascript
// DWR 엔진 로드
<script src="/dwr/interface/MailService.js"></script>
<script src="/dwr/engine.js"></script>

// 서버 메서드 호출
MailService.getMailList(userId, function(mailList) {
    // 콜백 함수
    displayMailList(mailList);
});

MailService.sendMail(mailData, {
    callback: function(result) {
        if (result.success) {
            alert('메일이 전송되었습니다.');
        }
    },
    errorHandler: function(message, exception) {
        alert('오류: ' + message);
    }
});
```

### 1.3 DWR의 주요 사용 사례

1. **메일 목록 동적 로딩** (무한 스크롤)
2. **메일 읽음 상태 업데이트** (AJAX)
3. **폴더 이동/삭제** (실시간 반영)
4. **주소록 검색** (자동완성)
5. **첨부파일 진행률 표시**
6. **실시간 알림** (폴링 방식)

---

## 2. DWR 전환 전략

### 2.1 전환 목표

```
기존: DWR (구식 AJAX 라이브러리)
  ↓
목표: Spring REST API + Fetch API (또는 Axios)
```

**장점**:
- 표준 HTTP/REST API 사용
- 최신 JavaScript (ES6+) 활용
- 더 나은 디버깅 및 테스트
- 보안 강화 (CSRF, CORS 등)
- 유지보수 용이성

### 2.2 전환 방식

#### 방식 1: Spring MVC `@ResponseBody` (권장)
- Spring MVC Controller에서 JSON 응답
- 간단하고 직관적
- 기존 Spring 구조와 잘 통합

#### 방식 2: Spring REST Controller (`@RestController`)
- RESTful API 전용 Controller
- 더 명확한 REST 의미론
- API 버전 관리 용이

### 2.3 전환 순서

```
Phase 1: REST API 설계
   ↓
Phase 2: Spring REST Controller 작성
   ↓
Phase 3: JavaScript 클라이언트 코드 작성
   ↓
Phase 4: DWR와 병행 운영 (점진적 전환)
   ↓
Phase 5: DWR 완전 제거
```

---

## 3. Spring REST API 구현

### 3.1 Spring 설정

#### Jackson 의존성 추가 (JSON 변환)
```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.15.2</version>
</dependency>
```

#### Spring MVC 설정
```xml
<!-- spring-mvc-config.xml -->
<mvc:annotation-driven>
    <mvc:message-converters>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
            <property name="objectMapper" ref="objectMapper"/>
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>

<bean id="objectMapper" class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
    <property name="featuresToDisable">
        <array>
            <util:constant static-field="com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS"/>
        </array>
    </property>
</bean>
```

### 3.2 REST Controller 작성

#### 예시 1: 메일 API Controller
```java
package com.terracetech.tims.webmail.mail.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/mail")
public class MailApiController {

    private final MailManager mailManager;

    @Autowired
    public MailApiController(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    /**
     * 메일 목록 조회
     * 기존 DWR: MailService.getMailList(userId, callback)
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MailVO>>> getMailList(
            @SessionAttribute("user") UserVO user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            List<MailVO> mailList = mailManager.getMailList(user.getUserId(), page, size);
            return ResponseEntity.ok(ApiResponse.success(mailList));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("메일 목록 조회 실패", e.getMessage()));
        }
    }

    /**
     * 메일 상세 조회
     * 기존 DWR: MailService.getMail(mailId, callback)
     */
    @GetMapping("/{mailId}")
    public ResponseEntity<ApiResponse<MailVO>> getMail(@PathVariable String mailId) {

        try {
            MailVO mail = mailManager.getMail(mailId);
            if (mail == null) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("메일을 찾을 수 없습니다.", null));
            }
            return ResponseEntity.ok(ApiResponse.success(mail));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("메일 조회 실패", e.getMessage()));
        }
    }

    /**
     * 메일 전송
     * 기존 DWR: MailService.sendMail(mailData, callback)
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<SendResult>> sendMail(
            @Valid @RequestBody MailSendRequest request,
            @SessionAttribute("user") UserVO user) {

        try {
            SendResult result = mailManager.sendMail(request, user);
            return ResponseEntity.ok(ApiResponse.success(result));

        } catch (ValidationException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("유효성 검사 실패", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("메일 전송 실패", e.getMessage()));
        }
    }

    /**
     * 메일 읽음 상태 업데이트
     * 기존 DWR: MailService.markAsRead(mailId, callback)
     */
    @PatchMapping("/{mailId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String mailId) {

        try {
            mailManager.markAsRead(mailId);
            return ResponseEntity.ok(ApiResponse.success(null));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("상태 업데이트 실패", e.getMessage()));
        }
    }

    /**
     * 메일 삭제
     * 기존 DWR: MailService.deleteMails(mailIds, callback)
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<DeleteResult>> deleteMails(
            @RequestParam String[] mailIds) {

        try {
            DeleteResult result = mailManager.deleteMails(mailIds);
            return ResponseEntity.ok(ApiResponse.success(result));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("메일 삭제 실패", e.getMessage()));
        }
    }

    /**
     * 메일 이동
     * 기존 DWR: MailService.moveMails(mailIds, targetFolder, callback)
     */
    @PatchMapping("/move")
    public ResponseEntity<ApiResponse<MoveResult>> moveMails(
            @RequestBody MoveMailRequest request) {

        try {
            MoveResult result = mailManager.moveMails(
                    request.getMailIds(),
                    request.getTargetFolder()
            );
            return ResponseEntity.ok(ApiResponse.success(result));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("메일 이동 실패", e.getMessage()));
        }
    }
}
```

### 3.3 표준 API 응답 형식

#### ApiResponse 클래스
```java
package com.terracetech.tims.webmail.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private String errorDetail;
    private long timestamp;

    private ApiResponse(boolean success, T data, String message, String errorDetail) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorDetail = errorDetail;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorDetail) {
        return new ApiResponse<>(false, null, message, errorDetail);
    }

    // Getters/Setters
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public String getErrorDetail() { return errorDetail; }
    public long getTimestamp() { return timestamp; }
}
```

#### 응답 JSON 예시
```json
// 성공 응답
{
  "success": true,
  "data": [
    {
      "mailId": "123",
      "subject": "테스트 메일",
      "sender": "test@example.com",
      "regDate": "2025-10-14T15:30:00"
    }
  ],
  "timestamp": 1697276400000
}

// 에러 응답
{
  "success": false,
  "message": "메일을 찾을 수 없습니다.",
  "errorDetail": "MailNotFoundException: Mail ID 123 not found",
  "timestamp": 1697276400000
}
```

### 3.4 Request DTO 클래스
```java
// 메일 전송 요청
public class MailSendRequest {

    @NotEmpty(message = "받는사람을 입력하세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String to;

    private String cc;
    private String bcc;

    @NotEmpty(message = "제목을 입력하세요")
    private String subject;

    private String content;
    private List<String> attachmentIds;

    // Getters/Setters
}

// 메일 이동 요청
public class MoveMailRequest {

    @NotEmpty(message = "메일 ID가 필요합니다")
    private String[] mailIds;

    @NotEmpty(message = "대상 폴더가 필요합니다")
    private String targetFolder;

    // Getters/Setters
}
```

---

## 4. JavaScript 클라이언트 구현

### 4.1 Fetch API 활용 (최신 방식)

#### 공통 API 유틸리티
```javascript
// /web/js/api-utils.js
const API = {
    baseURL: '/api',

    /**
     * GET 요청
     */
    async get(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = `${this.baseURL}${url}${queryString ? '?' + queryString : ''}`;

        try {
            const response = await fetch(fullUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'same-origin' // 쿠키 포함
            });

            return await this._handleResponse(response);

        } catch (error) {
            console.error('API GET 오류:', error);
            throw error;
        }
    },

    /**
     * POST 요청
     */
    async post(url, data = {}) {
        try {
            const response = await fetch(`${this.baseURL}${url}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'same-origin',
                body: JSON.stringify(data)
            });

            return await this._handleResponse(response);

        } catch (error) {
            console.error('API POST 오류:', error);
            throw error;
        }
    },

    /**
     * PATCH 요청
     */
    async patch(url, data = {}) {
        try {
            const response = await fetch(`${this.baseURL}${url}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'same-origin',
                body: JSON.stringify(data)
            });

            return await this._handleResponse(response);

        } catch (error) {
            console.error('API PATCH 오류:', error);
            throw error;
        }
    },

    /**
     * DELETE 요청
     */
    async delete(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = `${this.baseURL}${url}${queryString ? '?' + queryString : ''}`;

        try {
            const response = await fetch(fullUrl, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'same-origin'
            });

            return await this._handleResponse(response);

        } catch (error) {
            console.error('API DELETE 오류:', error);
            throw error;
        }
    },

    /**
     * 응답 처리
     */
    async _handleResponse(response) {
        const contentType = response.headers.get('content-type');

        if (!response.ok) {
            if (contentType && contentType.includes('application/json')) {
                const errorData = await response.json();
                throw new Error(errorData.message || '요청 실패');
            }
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }

        if (contentType && contentType.includes('application/json')) {
            const result = await response.json();

            if (!result.success) {
                throw new Error(result.message || '작업 실패');
            }

            return result.data;
        }

        return null;
    }
};

// 메일 API 래퍼
const MailAPI = {
    /**
     * 메일 목록 조회
     */
    async getMailList(page = 1, size = 20) {
        return await API.get('/mail/list', { page, size });
    },

    /**
     * 메일 상세 조회
     */
    async getMail(mailId) {
        return await API.get(`/mail/${mailId}`);
    },

    /**
     * 메일 전송
     */
    async sendMail(mailData) {
        return await API.post('/mail/send', mailData);
    },

    /**
     * 메일 읽음 처리
     */
    async markAsRead(mailId) {
        return await API.patch(`/mail/${mailId}/read`);
    },

    /**
     * 메일 삭제
     */
    async deleteMails(mailIds) {
        return await API.delete('/mail', { mailIds });
    },

    /**
     * 메일 이동
     */
    async moveMails(mailIds, targetFolder) {
        return await API.patch('/mail/move', { mailIds, targetFolder });
    }
};
```

### 4.2 사용 예시

#### 메일 목록 로딩
```javascript
// ===== 기존 DWR 방식 =====
MailService.getMailList(userId, function(mailList) {
    displayMailList(mailList);
});


// ===== 신규 REST API 방식 =====
async function loadMailList() {
    try {
        const mailList = await MailAPI.getMailList(1, 20);
        displayMailList(mailList);

    } catch (error) {
        console.error('메일 목록 로딩 실패:', error);
        alert('메일 목록을 불러올 수 없습니다.');
    }
}

// 또는 Promise 방식
MailAPI.getMailList(1, 20)
    .then(mailList => displayMailList(mailList))
    .catch(error => {
        console.error('메일 목록 로딩 실패:', error);
        alert('메일 목록을 불러올 수 없습니다.');
    });
```

#### 메일 전송
```javascript
// ===== 기존 DWR 방식 =====
MailService.sendMail(mailData, {
    callback: function(result) {
        alert('메일이 전송되었습니다.');
        location.href = '/mail/list';
    },
    errorHandler: function(message) {
        alert('오류: ' + message);
    }
});


// ===== 신규 REST API 방식 =====
async function sendMail() {
    const mailData = {
        to: document.getElementById('to').value,
        subject: document.getElementById('subject').value,
        content: document.getElementById('content').value
    };

    try {
        const result = await MailAPI.sendMail(mailData);
        alert('메일이 전송되었습니다.');
        location.href = '/mail/list';

    } catch (error) {
        alert('오류: ' + error.message);
    }
}
```

#### 메일 읽음 처리 (AJAX)
```javascript
// ===== 기존 DWR 방식 =====
MailService.markAsRead(mailId, function(success) {
    if (success) {
        document.getElementById('mail-' + mailId).classList.add('read');
    }
});


// ===== 신규 REST API 방식 =====
async function markMailAsRead(mailId) {
    try {
        await MailAPI.markAsRead(mailId);
        document.getElementById('mail-' + mailId).classList.add('read');

    } catch (error) {
        console.error('읽음 처리 실패:', error);
    }
}
```

### 4.3 jQuery AJAX 활용 (레거시 호환)

기존 jQuery 1.3.2를 사용 중이므로 jQuery AJAX도 활용 가능:

```javascript
// jQuery AJAX 래퍼
const MailAPI_jQuery = {
    getMailList: function(page, size, callback) {
        $.ajax({
            url: '/api/mail/list',
            type: 'GET',
            data: { page: page, size: size },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    callback(null, response.data);
                } else {
                    callback(response.message);
                }
            },
            error: function(xhr, status, error) {
                callback('요청 실패: ' + error);
            }
        });
    },

    sendMail: function(mailData, callback) {
        $.ajax({
            url: '/api/mail/send',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(mailData),
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    callback(null, response.data);
                } else {
                    callback(response.message);
                }
            },
            error: function(xhr, status, error) {
                callback('요청 실패: ' + error);
            }
        });
    }
};

// 사용 예시
MailAPI_jQuery.getMailList(1, 20, function(error, mailList) {
    if (error) {
        alert('오류: ' + error);
        return;
    }
    displayMailList(mailList);
});
```

---

## 5. 점진적 전환 전략

### 5.1 DWR와 REST API 병행 운영

**1단계**: REST API 추가 (DWR 유지)
```
/dwr/*           → DWR Servlet (기존)
/api/*           → Spring REST API (신규)
```

**2단계**: 모듈별 순차 전환
```
1. 메일 목록 API → REST로 전환
2. 메일 읽기 API → REST로 전환
3. 메일 전송 API → REST로 전환
...
```

**3단계**: DWR 제거
```
- web.xml에서 DWR Servlet 제거
- Spring 설정에서 DWR 설정 제거
- dwr.jar 의존성 제거
```

### 5.2 변환 체크리스트

#### 모듈별 전환 순서
- [ ] **공통 모듈**: 설정 조회, 사용자 정보
- [ ] **메일 모듈**: 목록, 읽기, 전송, 삭제
- [ ] **주소록 모듈**: 검색, 자동완성
- [ ] **일정 모듈**: 일정 조회, 등록
- [ ] **조직도 모듈**: 트리 조회
- [ ] **설정 모듈**: 폴더 관리

#### 각 API별 작업
- [ ] REST Controller 작성
- [ ] Request/Response DTO 작성
- [ ] JavaScript 클라이언트 코드 작성
- [ ] 기존 DWR 코드와 병행 테스트
- [ ] DWR 코드 제거
- [ ] 통합 테스트

---

## 6. 고급 기능 구현

### 6.1 파일 업로드 (멀티파트)

```java
@PostMapping("/upload")
public ResponseEntity<ApiResponse<UploadResult>> uploadAttachment(
        @RequestParam("file") MultipartFile file) {

    try {
        String savedPath = attachmentService.save(file);

        UploadResult result = new UploadResult();
        result.setOriginalName(file.getOriginalFilename());
        result.setSavedPath(savedPath);
        result.setFileSize(file.getSize());

        return ResponseEntity.ok(ApiResponse.success(result));

    } catch (Exception e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.error("파일 업로드 실패", e.getMessage()));
    }
}
```

```javascript
// FormData를 사용한 파일 업로드
async function uploadFile(fileInput) {
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    const response = await fetch('/api/mail/upload', {
        method: 'POST',
        body: formData, // Content-Type 자동 설정
        credentials: 'same-origin'
    });

    const result = await response.json();
    if (result.success) {
        console.log('업로드 성공:', result.data);
    }
}
```

### 6.2 실시간 알림 (Long Polling → WebSocket)

기존 DWR의 Reverse AJAX (폴링) 대체:

```java
// WebSocket 설정
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mailNotificationHandler(), "/ws/mail-notification")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler mailNotificationHandler() {
        return new MailNotificationHandler();
    }
}

// WebSocket 핸들러
public class MailNotificationHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 새 메일 알림 전송
    }

    public void sendMailNotification(String userId, MailVO newMail) {
        // 해당 사용자 세션에 알림 전송
    }
}
```

```javascript
// JavaScript WebSocket 클라이언트
const socket = new WebSocket('ws://localhost:8080/ws/mail-notification');

socket.onmessage = function(event) {
    const notification = JSON.parse(event.data);
    displayNewMailNotification(notification);
};

socket.onopen = function() {
    console.log('WebSocket 연결됨');
};

socket.onerror = function(error) {
    console.error('WebSocket 오류:', error);
};
```

### 6.3 에러 처리 강화

```java
@ControllerAdvice
@RestController
public class RestApiExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("유효성 검사 실패", ex.getMessage()));
    }

    @ExceptionHandler(MailNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMailNotFoundException(MailNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error("메일을 찾을 수 없습니다.", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(500)
                .body(ApiResponse.error("서버 오류가 발생했습니다.", ex.getMessage()));
    }
}
```

---

## 7. 테스트

### 7.1 REST Controller 테스트
```java
@SpringJUnitConfig
@WebAppConfiguration
class MailApiControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testGetMailList() throws Exception {
        mockMvc.perform(get("/api/mail/list")
                        .param("page", "1")
                        .param("size", "20")
                        .sessionAttr("user", createTestUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSendMail() throws Exception {
        MailSendRequest request = new MailSendRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");

        mockMvc.perform(post("/api/mail/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .sessionAttr("user", createTestUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
```

### 7.2 JavaScript 테스트 (Jest)
```javascript
// __tests__/api-utils.test.js
describe('MailAPI', () => {
    beforeEach(() => {
        fetch.resetMocks();
    });

    test('getMailList should return mail list', async () => {
        const mockData = [
            { mailId: '1', subject: 'Test Mail' }
        ];

        fetch.mockResponseOnce(JSON.stringify({
            success: true,
            data: mockData
        }));

        const result = await MailAPI.getMailList(1, 20);
        expect(result).toEqual(mockData);
        expect(fetch).toHaveBeenCalledWith('/api/mail/list?page=1&size=20', expect.any(Object));
    });

    test('sendMail should handle error', async () => {
        fetch.mockResponseOnce(JSON.stringify({
            success: false,
            message: 'Validation error'
        }), { status: 400 });

        await expect(MailAPI.sendMail({})).rejects.toThrow('Validation error');
    });
});
```

---

## 8. 마이그레이션 완료 기준

### 8.1 완료 체크리스트
- [ ] 모든 DWR 메서드 → REST API 전환 완료
- [ ] 모든 JSP에서 DWR 스크립트 제거
- [ ] JavaScript 클라이언트 코드 작성 완료
- [ ] 단위 테스트 작성 및 통과
- [ ] 통합 테스트 통과
- [ ] E2E 테스트 통과
- [ ] 성능 테스트 (DWR과 동등 이상)
- [ ] 보안 테스트 (CSRF, CORS 등)
- [ ] web.xml에서 DWR Servlet 제거
- [ ] Spring 설정에서 DWR 설정 제거
- [ ] dwr.jar 의존성 제거
- [ ] 문서 업데이트

### 8.2 검증 항목
- 모든 AJAX 기능 정상 동작
- 에러 처리 정상 동작
- 세션 관리 정상 동작
- 파일 업로드/다운로드 정상 동작
- 실시간 알림 정상 동작 (WebSocket)

---

## 9. 참고 자료

### 9.1 관련 문서
- Spring MVC REST 공식 문서: https://spring.io/guides/gs/rest-service/
- Fetch API MDN: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
- WebSocket API: https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API

### 9.2 도구
- Postman: REST API 테스트
- Chrome DevTools: 네트워크 디버깅
- Jest: JavaScript 단위 테스트

---

## 다음 단계

이 문서를 `02-migration-strategy.md`의 Phase 4에 통합하거나, 별도 Phase로 관리할 수 있습니다.

**권장 순서**:
1. Phase 2: Spring 5.x 업그레이드
2. Phase 3: iBATIS → MyBatis
3. **Phase 3.5: DWR → REST API** (신규)
4. Phase 4: Struts 2 → Spring MVC
