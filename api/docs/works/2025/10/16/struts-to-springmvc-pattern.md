# Struts 2 → Spring MVC 전환 패턴

**분석일**: 2025-10-16  
**작업 ID**: P1-027  
**목적**: Action → Controller 전환 패턴 수립

---

## 현재 Struts 2 사용 패턴

### 대표 Action 클래스 분석

**예시**: `webmail/mail/action/MailListAction.java`

```java
public class MailListAction extends ActionSupport {
    
    // Spring DI (byName)
    private MailManager mailManager;
    
    // Action 필드 (파라미터 바인딩)
    private String userId;
    private int page;
    
    // 결과 데이터
    private List<MailVO> mailList;
    
    // Spring DI Setter
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    // 파라미터 Setter
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    // Execute 메서드
    @Override
    public String execute() throws Exception {
        // 비즈니스 로직 호출
        this.mailList = mailManager.getMailList(userId, page);
        return SUCCESS;
    }
    
    // 결과 Getter
    public List<MailVO> getMailList() {
        return mailList;
    }
}
```

**struts.xml 설정**:
```xml
<action name="mailList" class="mailListAction">
    <result name="success">/mail/mailList.jsp</result>
</action>
```

---

## Spring MVC 전환 패턴

### 기본 Controller 패턴

```java
@Controller
@RequestMapping("/mail")
public class MailController {
    
    // Spring DI (Constructor 주입 권장)
    private final MailManager mailManager;
    
    @Autowired
    public MailController(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    @GetMapping("/list")
    public String mailList(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(defaultValue = "1") int page,
            @SessionAttribute("user") UserVO currentUser,
            Model model) {
        
        // userId가 없으면 세션에서 가져오기
        String targetUserId = (userId != null) ? userId : currentUser.getUserId();
        
        // 비즈니스 로직 호출
        List<MailVO> mailList = mailManager.getMailList(targetUserId, page);
        
        // Model에 데이터 추가
        model.addAttribute("mailList", mailList);
        model.addAttribute("currentPage", page);
        
        // 뷰 이름 반환
        return "mail/mailList"; // → /WEB-INF/jsp/mail/mailList.jsp
    }
}
```

---

## 전환 매핑 테이블

### 1. 클래스 구조

| Struts 2 | Spring MVC | 비고 |
|----------|------------|------|
| `extends ActionSupport` | `@Controller` | 어노테이션 기반 |
| `execute()` 메서드 | `@GetMapping/@PostMapping` 메서드 | HTTP 메서드별 매핑 |
| 필드 + Getter/Setter | `@RequestParam` + `Model` | 파라미터 명시적 |
| `return SUCCESS` | `return "viewName"` | 뷰 이름 문자열 |

### 2. 의존성 주입

| Struts 2 | Spring MVC |
|----------|------------|
| Setter 주입 (XML 설정) | Constructor 주입 (권장) |
| `public void setXxx(Xxx xxx)` | `@Autowired public MailController(Xxx xxx)` |

### 3. 파라미터 바인딩

| Struts 2 | Spring MVC |
|----------|------------|
| 필드 + Setter | `@RequestParam` |
| 자동 바인딩 | 명시적 선언 |

### 4. 세션 접근

| Struts 2 | Spring MVC |
|----------|------------|
| `ActionContext.getContext().getSession()` | `@SessionAttribute` |
| 직접 접근 | 어노테이션 기반 |

### 5. 결과 전달

| Struts 2 | Spring MVC |
|----------|------------|
| 필드 + Getter | `Model.addAttribute()` |
| JSP에서 Action 필드 접근 | JSP에서 Model 속성 접근 |

---

## Action → Controller 전환 예시

### 예시 1: 간단한 조회 (MailListAction)

**Struts Action**:
```java
public class MailListAction extends ActionSupport {
    private MailManager mailManager;
    private List<MailVO> mailList;
    
    public String execute() {
        mailList = mailManager.getMailList();
        return SUCCESS;
    }
}
```

**Spring Controller**:
```java
@Controller
@RequestMapping("/mail")
public class MailController {
    
    private final MailManager mailManager;
    
    @Autowired
    public MailController(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    @GetMapping("/list")
    public String list(Model model) {
        List<MailVO> mailList = mailManager.getMailList();
        model.addAttribute("mailList", mailList);
        return "mail/mailList";
    }
}
```

---

### 예시 2: 폼 제출 (MailSendAction)

**Struts Action**:
```java
public class MailSendAction extends ActionSupport {
    private MailManager mailManager;
    private String to;
    private String subject;
    private String content;
    
    @Override
    public void validate() {
        if (StringUtils.isEmpty(to)) {
            addFieldError("to", "받는사람을 입력하세요");
        }
    }
    
    public String execute() {
        MailVO mail = new MailVO();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setContent(content);
        
        mailManager.sendMail(mail);
        return SUCCESS;
    }
}
```

**Spring Controller**:
```java
@Controller
@RequestMapping("/mail")
public class MailController {
    
    private final MailManager mailManager;
    
    @Autowired
    public MailController(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    @PostMapping("/send")
    public String send(
            @Valid @ModelAttribute MailForm mailForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        // Validation 에러 확인
        if (bindingResult.hasErrors()) {
            return "mail/compose";
        }
        
        // 비즈니스 로직
        mailManager.sendMail(mailForm);
        
        // Flash 메시지
        redirectAttributes.addFlashAttribute("message", "메일이 전송되었습니다.");
        
        // 리다이렉트
        return "redirect:/mail/list";
    }
}

// Form 객체
public class MailForm {
    @NotEmpty(message = "받는사람을 입력하세요")
    @Email
    private String to;
    
    @NotEmpty(message = "제목을 입력하세요")
    private String subject;
    
    private String content;
    
    // Getters/Setters
}
```

---

### 예시 3: AJAX 응답 (MailWorkAction)

**Struts Action** (DWR 사용):
```java
// DWR 서비스 클래스
public class MailService {
    private MailManager mailManager;
    
    public boolean markAsRead(String mailId) {
        return mailManager.markAsRead(mailId);
    }
}
```

**Spring Controller** (REST API):
```java
@RestController
@RequestMapping("/api/mail")
public class MailApiController {
    
    private final MailManager mailManager;
    
    @Autowired
    public MailApiController(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    @PatchMapping("/{mailId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String mailId) {
        try {
            mailManager.markAsRead(mailId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("처리 실패", e.getMessage()));
        }
    }
}
```

---

## Interceptor 전환

### Struts Interceptor
```java
public class AuthInterceptor extends AbstractInterceptor {
    @Override
    public String intercept(ActionInvocation invocation) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        if (session.getAttribute("user") == null) {
            return "login";
        }
        return invocation.invoke();
    }
}
```

### Spring Interceptor
```java
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
```

---

## URL 매핑 전환

### Struts URL → Spring URL

| Struts 2 | Spring MVC | 비고 |
|----------|------------|------|
| `/mail/mailList.action` | `/mail/list` | .action 제거 |
| `/mail/mailRead.action?mailId=123` | `/mail/read/123` | RESTful |
| `/mail/mailSend.action` | `/mail/send` | 동일 유지 가능 |

**레거시 URL 호환**:
```java
@Controller
public class LegacyUrlController {
    
    @GetMapping("/mail/mailList.action")
    public String redirectMailList() {
        return "redirect:/mail/list";
    }
}
```

---

## Validation 전환

### Struts Validation → Spring Validation

**Struts**:
```java
public void validate() {
    if (StringUtils.isEmpty(to)) {
        addFieldError("to", "받는사람을 입력하세요");
    }
}
```

**Spring**:
```java
// Form 객체에 어노테이션
public class MailForm {
    @NotEmpty(message = "받는사람을 입력하세요")
    private String to;
}

// Controller
@PostMapping("/send")
public String send(@Valid @ModelAttribute MailForm form,
                  BindingResult result) {
    if (result.hasErrors()) {
        return "mail/compose";
    }
    // ...
}
```

---

## 모듈별 전환 우선순위

### 1단계: 테스트 모듈 (4개 Actions)
- 리스크 낮음
- 학습 목적
- 패턴 검증

### 2단계: 독립 모듈
- organization (5개)
- setting (47개)
- webfolder (18개)

### 3단계: 보조 모듈
- scheduler (11개)
- bbs (14개)
- addrbook (12개)

### 4단계: 핵심 모듈
- mailuser (10개) - 인증
- mail (33개) - 핵심
- home (4개)

---

## 자동화 가능 항목

### 1. 클래스 구조 변환
- `extends ActionSupport` 제거
- `@Controller` 추가
- `@RequestMapping` 추가

### 2. 메서드 시그니처
- `execute()` → `@GetMapping/@PostMapping`
- 파라미터 → `@RequestParam` 추가
- `Model` 파라미터 추가

### 3. import 문
- Struts import 제거
- Spring import 추가

**도구**: IntelliJ IDEA Refactoring, 정규식 치환

---

## 예상 작업량

### 258개 Action → Controller 전환

```
간단한 Action (조회만):    100개 × 2시간 = 200시간
중간 Action (CRUD):        100개 × 4시간 = 400시간
복잡한 Action (다중 기능):  58개 × 8시간 = 464시간

총 예상: 1,064시간 (약 133일, 5.5개월)

병렬 작업 (개발자 3명):
1,064시간 / 3명 = 355시간/명 (약 44일, 2개월)
```

**Phase 4 예상 기간**: 3-4개월 (적절함)

---

## 전환 체크리스트 (Action별)

```markdown
- [ ] Action 클래스 분석
- [ ] Controller 클래스 생성
- [ ] URL 매핑 정의
- [ ] 파라미터 바인딩
- [ ] 비즈니스 로직 호출
- [ ] Model 데이터 설정
- [ ] Validation 전환
- [ ] 예외 처리
- [ ] 테스트 작성
- [ ] URL 호환성 확인
```

---

## 완료 조건

- ✅ Action → Controller 전환 패턴 정의
- ✅ 전환 매핑 테이블 작성
- ✅ 코드 예시 작성 (간단/중간/복잡)
- ✅ Interceptor 전환 패턴 정의
- ✅ Validation 전환 패턴 정의
- ✅ 모듈별 우선순위 수립
- ✅ 예상 작업량 산정

---

**결론**: Struts 2 → Spring MVC 전환은 **명확한 패턴**이 있어 체계적으로 진행 가능. 258개 Action 전환에 약 2-3개월 소요 예상 (개발자 3명 기준).

**작성자**: Backend 리더  
**완료일**: 2025-10-16

