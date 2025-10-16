# Struts 2 → Spring MVC 마이그레이션 전략

## 문서 정보
- **작성일**: 2025-10-14
- **대상 시스템**: TIMS7 Webmail v7.4.6S
- **목표**: Struts 2 → Spring Framework 5.x + Spring MVC 전환

---

## 1. 마이그레이션 개요

### 1.1 마이그레이션 목표
```
현재: Struts 2.3.32 + Spring 2.5 + iBATIS 2.3.4
      ↓
목표: Spring MVC 5.x + Spring Framework 5.x + MyBatis 3.x
```

### 1.2 핵심 원칙
1. **단계적 접근**: 한 번에 하나의 프레임워크 전환
2. **JSP 유지**: 기존 JSP 뷰는 최대한 보존
3. **하위 호환성**: 점진적 마이그레이션으로 위험 최소화
4. **테스트 우선**: 각 단계마다 철저한 테스트
5. **롤백 가능**: 각 Phase 별 롤백 포인트 확보

### 1.3 예상 기간
- **전체 기간**: 6-12개월 (프로젝트 규모 고려)
- **Phase 1**: 2개월 (준비 및 환경 구축)
- **Phase 2**: 2-3개월 (Spring Framework 업그레이드)
- **Phase 3**: 2-3개월 (iBATIS → MyBatis)
- **Phase 4**: 3-4개월 (Struts 2 → Spring MVC)
- **Phase 5**: 1-2개월 (테스트 및 검증)

---

## 2. 전체 마이그레이션 로드맵

### 2.1 Phase 구조
```
Phase 0: 사전 준비
   ↓
Phase 1: 환경 구축 및 분석
   ↓
Phase 2: Spring Framework 5.x 업그레이드
   ↓
Phase 3: iBATIS → MyBatis 마이그레이션
   ↓
Phase 4: Struts 2 → Spring MVC 전환
   ↓
Phase 5: 테스트 및 검증
   ↓
Phase 6: 최적화 및 배포
```

### 2.2 의존성 관계
```
Struts 2 (제거 대상)
   ↓ 의존
Spring Framework 2.5 → 5.x (업그레이드)
   ↓ 의존
iBATIS 2.3.4 → MyBatis 3.x (교체)
   ↓
Database
```

**전환 순서**: Spring → MyBatis → Spring MVC (아래에서 위로)

---

## 3. Phase별 상세 전략

## Phase 0: 사전 준비 (착수 전)

### 3.1 백업 및 버전 관리
```bash
# 전체 프로젝트 백업
tar -czf tims7-webmail-backup-$(date +%Y%m%d).tar.gz .

# Git 저장소 초기화 (아직 없다면)
git init
git add .
git commit -m "Initial commit: Struts 2 baseline"
git tag v7.4.6S-struts2-baseline
```

### 3.2 브랜치 전략
```
main (프로덕션)
   ├── develop (개발 메인)
   ├── feature/spring-upgrade (Phase 2)
   ├── feature/mybatis-migration (Phase 3)
   └── feature/spring-mvc-conversion (Phase 4)
```

### 3.3 승인 사항
- [ ] 프로젝트 관리자 승인
- [ ] 테스트 환경 확보
- [ ] 개발 리소스 할당
- [ ] 예산 및 일정 승인

---

## Phase 1: 환경 구축 및 분석 (2개월)

### 4.1 개발 환경 구축
```
목표: 병렬 개발 환경 준비
```

#### 4.1.1 필요한 인프라
1. **개발 서버**
   - 기존 Struts 2 환경 (참조용)
   - 신규 Spring MVC 환경 (개발용)

2. **테스트 환경**
   - 통합 테스트 서버
   - 성능 테스트 환경
   - 사용자 승인 테스트(UAT) 환경

3. **데이터베이스**
   - 개발 DB (실 데이터 복사)
   - 테스트 DB
   - 성능 테스트 DB

#### 4.1.2 도구 준비
```
빌드 도구:
- Apache Ant (기존) 유지
- Maven/Gradle 도입 검토

IDE:
- Eclipse/IntelliJ IDEA 최신 버전
- Spring Tool Suite (STS)

테스트 도구:
- JUnit 5
- Mockito
- Spring Test
- Playwright (UI 테스트)

CI/CD:
- Jenkins / GitLab CI
- 자동화된 빌드 및 테스트
```

### 4.2 종속성 분석
```
목표: 모든 의존성 매핑 및 호환성 확인
```

#### 4.2.1 라이브러리 호환성 조사
| 현재 | 목표 | 호환성 | 비고 |
|------|------|--------|------|
| Struts 2.3.32 | Spring MVC 5.x | 제거 | 수동 전환 |
| Spring 2.5 | Spring 5.3.x | ⚠️ 주의 | 설정 변경 필요 |
| iBATIS 2.3.4 | MyBatis 3.5.x | ⚠️ 주의 | API 변경 |
| jQuery 1.3.2 | jQuery 3.x | ⚠️ 주의 | 플러그인 호환성 |
| Java 1.6/1.7 | Java 11/17 | ⚠️ 주의 | 버전 업그레이드 |

#### 4.2.2 타사 라이브러리 확인
```
보안 라이브러리:
- INISAFEMail → 최신 버전 확인
- Xecure7 → 호환성 확인
- INICrypto → 업데이트 여부

외부 통합:
- SSO 모듈 호환성
- 결제 모듈 (있다면)
- 외부 API 연동
```

### 4.3 코드 분석 및 메트릭 수집
```
목표: 마이그레이션 범위 정량화
```

#### 4.3.1 측정 항목
```bash
# Struts Action 클래스 수
find ./src -name "*Action.java" | wc -l

# Struts 태그 사용 JSP 수
grep -r "<s:" ./web --include="*.jsp" | wc -l

# Spring Bean 정의 수
grep -r "<bean" ./web/WEB-INF/classes/web-config/*.xml | wc -l

# iBATIS SQL 매핑 수
find ./web/WEB-INF/classes -name "*-sqlmap.xml" -exec grep -c "<select\|<insert\|<update\|<delete" {} + | awk '{s+=$1} END {print s}'
```

#### 4.3.2 복잡도 분석
- Cyclomatic Complexity 측정
- 코드 중복도 분석
- 의존성 그래프 생성

### 4.4 테스트 커버리지 확보
```
목표: 회귀 테스트 기반 마련
```

#### 4.4.1 기존 기능 테스트 작성
```java
// 주요 기능별 통합 테스트 작성
@Test
public void testMailSend() {
    // 메일 발송 기능 테스트
}

@Test
public void testMailList() {
    // 메일 목록 조회 테스트
}
```

#### 4.4.2 E2E 테스트 스크립트
```javascript
// Playwright를 사용한 UI 테스트
test('로그인 후 메일 발송', async ({ page }) => {
    await page.goto('/login');
    await page.fill('#userId', 'test@example.com');
    await page.fill('#password', 'password');
    await page.click('#loginButton');

    await page.click('#composeButton');
    await page.fill('#to', 'recipient@example.com');
    await page.fill('#subject', 'Test Mail');
    await page.click('#sendButton');

    await expect(page.locator('.success-message')).toBeVisible();
});
```

---

## Phase 2: Spring Framework 5.x 업그레이드 (2-3개월)

### 5.1 목표
```
Spring 2.5 → Spring 5.3.x 업그레이드
(Struts 2는 유지, 단순히 Spring만 업그레이드)
```

### 5.2 업그레이드 전략

#### 5.2.1 Spring 버전 선택
```xml
<!-- 권장: Spring Framework 5.3.x (LTS) -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.30</version>
</dependency>
```

**선택 이유**:
- Java 8+ 지원
- 안정적인 LTS 버전
- Struts 2와 호환 가능
- MyBatis 3.x 완벽 지원

#### 5.2.2 단계적 업그레이드
```
Step 1: 의존성 버전 변경
   ↓
Step 2: 설정 파일 업데이트
   ↓
Step 3: Deprecated API 교체
   ↓
Step 4: 컴파일 및 테스트
   ↓
Step 5: 통합 테스트
```

### 5.3 주요 변경 사항

#### 5.3.1 XML 네임스페이스 업데이트
```xml
<!-- 변경 전 (Spring 2.5) -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

<!-- 변경 후 (Spring 5.x) -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- Component Scan 추가 (선택사항) -->
    <context:component-scan base-package="com.terracetech.tims.webmail" />
</beans>
```

#### 5.3.2 의존성 주입 패턴 개선
```java
// 변경 전: Setter 주입 (XML 기반)
public class MailManager {
    private MailDao mailDao;

    public void setMailDao(MailDao mailDao) {
        this.mailDao = mailDao;
    }
}

// 변경 후: Constructor 주입 (권장)
@Service
public class MailManager {
    private final MailDao mailDao;

    @Autowired
    public MailManager(MailDao mailDao) {
        this.mailDao = mailDao;
    }
}
```

#### 5.3.3 트랜잭션 관리 업데이트
```xml
<!-- 변경 전 -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 변경 후 (동일하지만 annotation 지원 추가) -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:annotation-driven transaction-manager="transactionManager"/>
```

```java
// Java 코드에서 사용
@Transactional
public void sendMail(MailVO mail) {
    // 트랜잭션 처리
}
```

### 5.4 호환성 이슈 해결

#### 5.4.1 Struts 2 + Spring 5.x 통합
```xml
<!-- web.xml에서 Spring 5.x 설정 -->
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
        /WEB-INF/classes/web-config/spring-*.xml
    </param-value>
</context-param>
```

#### 5.4.2 Struts-Spring 플러그인 유지
```xml
<!-- struts.xml에서 Spring ObjectFactory 사용 -->
<struts>
    <constant name="struts.objectFactory" value="spring" />
    <constant name="struts.objectFactory.spring.autoWire" value="name" />
</struts>
```

### 5.5 테스트 계획
```
단위 테스트:
- Spring Bean 주입 테스트
- 트랜잭션 동작 테스트

통합 테스트:
- Struts Action + Spring Service 통합
- DAO + Spring DataSource 연동

회귀 테스트:
- 전체 기능 E2E 테스트
- 성능 테스트
```

---

## Phase 3: iBATIS → MyBatis 마이그레이션 (2-3개월)

### 6.1 목표
```
iBATIS 2.3.4 → MyBatis 3.5.x 전환
(Spring 5.x 환경에서 진행)
```

### 6.2 마이그레이션 전략

#### 6.2.1 MyBatis 버전 선택
```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.13</version>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.1.1</version>
</dependency>
```

#### 6.2.2 단계적 전환
```
Step 1: MyBatis 설정 추가
   ↓
Step 2: SQL 매핑 파일 변환 (모듈별)
   ↓
Step 3: DAO 인터페이스 생성
   ↓
Step 4: Spring 설정 업데이트
   ↓
Step 5: 테스트 및 검증
```

### 6.3 주요 변경 사항

#### 6.3.1 설정 파일 변환
```xml
<!-- 변경 전: iBATIS SqlMapConfig.xml -->
<sqlMapConfig>
    <settings useStatementNamespaces="true"/>
    <sqlMap resource="com/terracetech/tims/webmail/mail/sqlmap/Mail.xml"/>
</sqlMapConfig>

<!-- 변경 후: MyBatis mybatis-config.xml -->
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="cacheEnabled" value="true"/>
    </settings>
    <mappers>
        <mapper resource="com/terracetech/tims/webmail/mail/mapper/MailMapper.xml"/>
    </mappers>
</configuration>
```

#### 6.3.2 SQL 매핑 파일 변환
```xml
<!-- 변경 전: iBATIS -->
<sqlMap namespace="Mail">
    <select id="selectMailList" resultClass="com.terracetech.tims.webmail.mail.vo.MailVO">
        SELECT mail_id, subject, sender, reg_date
        FROM tb_mail
        WHERE user_id = #userId#
    </select>
</sqlMap>

<!-- 변경 후: MyBatis -->
<mapper namespace="com.terracetech.tims.webmail.mail.mapper.MailMapper">
    <select id="selectMailList" resultType="com.terracetech.tims.webmail.mail.vo.MailVO">
        SELECT mail_id, subject, sender, reg_date
        FROM tb_mail
        WHERE user_id = #{userId}
    </select>
</mapper>
```

**주요 차이점**:
- `<sqlMap>` → `<mapper>`
- `namespace` 속성값: 문자열 → 인터페이스 전체 경로
- 파라미터 표기: `#param#` → `#{param}`
- 결과 매핑: `resultClass` → `resultType`

#### 6.3.3 DAO 패턴 변경
```java
// 변경 전: iBATIS DAO 구현체
public class MailDao extends SqlMapClientDaoSupport {

    public List<MailVO> selectMailList(String userId) {
        return getSqlMapClientTemplate().queryForList("Mail.selectMailList", userId);
    }

    public int insertMail(MailVO mail) {
        return (Integer) getSqlMapClientTemplate().insert("Mail.insertMail", mail);
    }
}

// 변경 후: MyBatis Mapper 인터페이스
@Mapper
public interface MailMapper {

    List<MailVO> selectMailList(@Param("userId") String userId);

    int insertMail(MailVO mail);
}
```

**장점**:
- 구현체 불필요 (MyBatis가 자동 생성)
- 타입 안정성 향상
- 보일러플레이트 코드 제거

#### 6.3.4 Spring 설정 업데이트
```xml
<!-- 변경 전: iBATIS -->
<bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:SqlMapConfig.xml"/>
</bean>

<bean id="mailDao" class="com.terracetech.tims.webmail.mail.dao.MailDao">
    <property name="sqlMapClient" ref="sqlMapClient"/>
</bean>

<!-- 변경 후: MyBatis -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <property name="mapperLocations" value="classpath*:com/terracetech/tims/webmail/**/mapper/*.xml"/>
</bean>

<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.terracetech.tims.webmail.**.mapper"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
```

**MapperScannerConfigurer**: Mapper 인터페이스 자동 스캔 및 Bean 등록

### 6.4 모듈별 전환 순서
```
우선순위 1: 공통 모듈
- common/dao → common/mapper

우선순위 2: 독립적 모듈
- organization/dao → organization/mapper
- setting/dao → setting/mapper

우선순위 3: 의존성 있는 모듈
- mailuser/dao → mailuser/mapper
- scheduler/dao → scheduler/mapper

우선순위 4: 핵심 모듈 (신중하게)
- mail/dao → mail/mapper
```

### 6.5 테스트 전략
```java
// Mapper 단위 테스트
@SpringBootTest
@MybatisTest
class MailMapperTest {

    @Autowired
    private MailMapper mailMapper;

    @Test
    void testSelectMailList() {
        List<MailVO> mails = mailMapper.selectMailList("test@example.com");
        assertNotNull(mails);
    }
}
```

---

## Phase 4: Struts 2 → Spring MVC 전환 (3-4개월)

### 7.1 목표
```
Struts 2 제거 및 Spring MVC로 완전 전환
(JSP는 최대한 유지, Spring 태그로 변환)
```

### 7.2 전환 전략

#### 7.2.1 모듈별 순차 전환
```
1단계: 테스트/샘플 모듈 (리스크 낮음)
   - test/ 패키지 전환
   - 샘플 페이지 전환

2단계: 독립 모듈 (의존성 낮음)
   - organization/ 전환
   - setting/ 전환
   - webfolder/ 전환

3단계: 보조 모듈
   - scheduler/ (캘린더)
   - bbs/ (게시판)
   - addr/ (주소록)

4단계: 핵심 모듈 (신중하게)
   - mailuser/ (인증)
   - mail/ (메일 기능)
   - home/ (대시보드)
```

#### 7.2.2 Struts Action → Spring Controller 전환 패턴
```java
// ===== 변경 전: Struts 2 Action =====
public class MailListAction extends ActionSupport {

    private MailManager mailManager;
    private List<MailVO> mailList;
    private String userId;

    // Spring DI
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public String execute() throws Exception {
        this.mailList = mailManager.getMailList(userId);
        return SUCCESS;
    }

    // Getters/Setters for Struts
    public List<MailVO> getMailList() {
        return mailList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

// struts.xml 설정
<action name="mailList" class="mailListAction">
    <result name="success">/mail/mailList.jsp</result>
</action>


// ===== 변경 후: Spring MVC Controller =====
@Controller
@RequestMapping("/mail")
public class MailController {

    private final MailManager mailManager;

    @Autowired
    public MailController(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    @GetMapping("/list")
    public String mailList(
            @RequestParam("userId") String userId,
            Model model) {

        List<MailVO> mailList = mailManager.getMailList(userId);
        model.addAttribute("mailList", mailList);

        return "mail/mailList"; // ViewResolver가 JSP 경로 변환
    }
}
```

**주요 변경점**:
- `ActionSupport` → `@Controller`
- `execute()` → `@GetMapping/@PostMapping` 메서드
- 필드 + Getter/Setter → `@RequestParam` + `Model`
- `return SUCCESS` → `return "viewName"`

### 7.3 JSP 뷰 전환

#### 7.3.1 Struts 태그 → Spring 태그/JSTL
```jsp
<!-- ===== 변경 전: Struts 태그 ===== -->
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="mailSend" method="post">
    <s:textfield name="to" label="받는사람" required="true" />
    <s:textfield name="subject" label="제목" />
    <s:textarea name="content" label="내용" rows="10" />
    <s:submit value="전송" />
</s:form>

<s:if test="mailList != null">
    <s:iterator value="mailList" var="mail">
        <div>
            <s:property value="#mail.subject" />
            <s:property value="#mail.sender" />
        </div>
    </s:iterator>
</s:if>


<!-- ===== 변경 후: Spring Form 태그 + JSTL ===== -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form action="${pageContext.request.contextPath}/mail/send"
           method="post" modelAttribute="mailForm">
    <label>받는사람</label>
    <form:input path="to" required="true" />

    <label>제목</label>
    <form:input path="subject" />

    <label>내용</label>
    <form:textarea path="content" rows="10" />

    <button type="submit">전송</button>
</form:form>

<c:if test="${not empty mailList}">
    <c:forEach items="${mailList}" var="mail">
        <div>
            <c:out value="${mail.subject}" />
            <c:out value="${mail.sender}" />
        </div>
    </c:forEach>
</c:if>
```

**태그 변환 매핑**:
| Struts 태그 | Spring/JSTL 태그 |
|------------|------------------|
| `<s:form>` | `<form:form>` |
| `<s:textfield>` | `<form:input>` |
| `<s:textarea>` | `<form:textarea>` |
| `<s:select>` | `<form:select>` |
| `<s:checkbox>` | `<form:checkbox>` |
| `<s:if>` | `<c:if>` |
| `<s:iterator>` | `<c:forEach>` |
| `<s:property>` | `<c:out>` 또는 `${}` |
| `<s:url>` | `<c:url>` |

### 7.4 URL 매핑 전환

#### 7.4.1 Struts URL → Spring URL 매핑
```
변경 전 (Struts):
/mail/mailList.action
/mail/mailRead.action?mailId=123
/mail/mailSend.action

변경 후 (Spring MVC):
/mail/list
/mail/read?mailId=123
/mail/send
```

**주의사항**:
- 외부에서 사용 중인 URL은 유지하거나 리다이렉트 설정 필요
- 책갈피, 이메일 링크 등 고려

#### 7.4.2 URL 호환성 유지
```java
// 레거시 URL 리다이렉트
@Controller
public class LegacyUrlRedirectController {

    @GetMapping("/mail/mailList.action")
    public String redirectMailList() {
        return "redirect:/mail/list";
    }

    @GetMapping("/mail/mailRead.action")
    public String redirectMailRead(@RequestParam("mailId") String mailId) {
        return "redirect:/mail/read?mailId=" + mailId;
    }
}
```

### 7.5 인터셉터 전환

#### 7.5.1 Struts Interceptor → Spring Interceptor
```java
// 변경 전: Struts Interceptor
public class AuthInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();
        UserVO user = (UserVO) session.getAttribute("user");

        if (user == null) {
            return "login";
        }

        return invocation.invoke();
    }
}

// 변경 후: Spring Interceptor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        UserVO user = (UserVO) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
```

```java
// Spring MVC 설정에 인터셉터 등록
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/mail/**", "/setting/**")
                .excludePathPatterns("/login", "/logout");
    }
}
```

### 7.6 Spring MVC 설정

#### 7.6.1 web.xml 업데이트
```xml
<!-- Struts 필터 제거 -->
<!-- <filter>
    <filter-name>struts</filter-name>
    <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
</filter> -->

<!-- Spring MVC DispatcherServlet 추가 -->
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-mvc-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

#### 7.6.2 spring-mvc-config.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/mvc
           http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- Controller 스캔 -->
    <context:component-scan base-package="com.terracetech.tims.webmail.**.controller" />

    <!-- Annotation 기반 MVC 활성화 -->
    <mvc:annotation-driven />

    <!-- 정적 리소스 매핑 -->
    <mvc:resources mapping="/design/**" location="/design/" />
    <mvc:resources mapping="/js/**" location="/js/" />
    <mvc:resources mapping="/editor/**" location="/editor/" />

    <!-- JSP ViewResolver -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/" />
        <property name="suffix" value=".jsp" />
        <property name="order" value="1" />
    </bean>

    <!-- 인터셉터 등록 -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/mail/**"/>
            <mvc:mapping path="/setting/**"/>
            <mvc:exclude-mapping path="/login"/>
            <bean class="com.terracetech.tims.webmail.common.interceptor.AuthInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>

    <!-- 다국어 지원 -->
    <bean id="messageSource"
          class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basenames">
            <list>
                <value>messageResources/MailMessage</value>
                <value>messageResources/CommonMessage</value>
            </list>
        </property>
        <property name="defaultEncoding" value="UTF-8" />
    </bean>

    <bean id="localeResolver"
          class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
        <property name="defaultLocale" value="ko" />
    </bean>

    <mvc:interceptors>
        <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
            <property name="paramName" value="lang" />
        </bean>
    </mvc:interceptors>

    <!-- 파일 업로드 -->
    <bean id="multipartResolver"
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="104857600" /> <!-- 100MB -->
        <property name="maxInMemorySize" value="1048576" /> <!-- 1MB -->
        <property name="defaultEncoding" value="UTF-8" />
    </bean>

</beans>
```

### 7.7 예외 처리

#### 7.7.1 전역 예외 처리기
```java
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception", ex);

        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        return mav;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPassword(InvalidPasswordException ex) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("error", "비밀번호가 올바르지 않습니다.");
        return mav;
    }
}
```

### 7.8 폼 유효성 검증

#### 7.8.1 Validation 전환
```java
// 변경 전: Struts Validation
public class MailSendAction extends ActionSupport {

    @Override
    public void validate() {
        if (StringUtils.isEmpty(to)) {
            addFieldError("to", "받는사람을 입력하세요");
        }
        if (StringUtils.isEmpty(subject)) {
            addFieldError("subject", "제목을 입력하세요");
        }
    }
}

// 변경 후: Spring Validation
@Controller
public class MailController {

    @PostMapping("/send")
    public String sendMail(@Valid @ModelAttribute MailForm mailForm,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            return "mail/compose";
        }

        mailManager.sendMail(mailForm);
        return "redirect:/mail/list";
    }
}

// MailForm VO
public class MailForm {

    @NotEmpty(message = "받는사람을 입력하세요")
    private String to;

    @NotEmpty(message = "제목을 입력하세요")
    private String subject;

    private String content;

    // Getters/Setters
}
```

```jsp
<!-- JSP에서 에러 표시 -->
<form:form modelAttribute="mailForm">
    <form:input path="to" />
    <form:errors path="to" cssClass="error" />

    <form:input path="subject" />
    <form:errors path="subject" cssClass="error" />
</form:form>
```

---

## Phase 5: 테스트 및 검증 (1-2개월)

### 8.1 테스트 전략
```
Layer 1: 단위 테스트
   - Controller 테스트
   - Service/Manager 테스트
   - Mapper 테스트

Layer 2: 통합 테스트
   - Controller + Service 통합
   - Service + Mapper 통합

Layer 3: E2E 테스트
   - Playwright UI 자동화 테스트
   - 주요 사용자 시나리오 검증

Layer 4: 성능 테스트
   - 응답 시간 측정
   - 동시 사용자 부하 테스트

Layer 5: 보안 테스트
   - OWASP Top 10 검증
   - 인증/인가 테스트
```

### 8.2 회귀 테스트 체크리스트
```
✓ 로그인/로그아웃
✓ 메일 목록 조회
✓ 메일 읽기
✓ 메일 작성/발송
✓ 메일 삭제/이동
✓ 첨부파일 업로드/다운로드
✓ 주소록 관리
✓ 일정 관리
✓ 조직도 조회
✓ 사용자 설정
✓ 다국어 전환
✓ 모바일 화면
```

### 8.3 성능 벤치마크
```
기준선 (Struts 2):
- 로그인: < 500ms
- 메일 목록: < 1000ms
- 메일 읽기: < 800ms

목표 (Spring MVC):
- 로그인: < 400ms (20% 개선)
- 메일 목록: < 800ms (20% 개선)
- 메일 읽기: < 600ms (25% 개선)
```

---

## Phase 6: 최적화 및 배포 (1개월)

### 9.1 최적화 작업
```
1. SQL 쿼리 최적화
2. 캐싱 전략 적용 (Spring Cache)
3. 정적 리소스 최적화
4. DB 커넥션 풀 튜닝
5. JVM 힙 메모리 튜닝
```

### 9.2 배포 전략
```
Blue-Green 배포:
1. 신규 환경(Green) 구축
2. 트래픽 일부를 Green으로 전환
3. 모니터링 및 검증
4. 전체 트래픽 전환
5. 구 환경(Blue) 대기 (롤백용)
```

---

## 10. 위험 관리

### 10.1 주요 위험 요소
| 위험 | 영향도 | 발생 확률 | 대응 방안 |
|------|--------|-----------|-----------|
| 대규모 JSP 수정 | 높음 | 중간 | 자동화 스크립트, 단계적 전환 |
| 성능 저하 | 높음 | 낮음 | 성능 테스트, 튜닝 |
| 보안 이슈 | 높음 | 낮음 | 보안 검증, 침투 테스트 |
| 일정 지연 | 중간 | 높음 | 버퍼 기간 확보 |
| 데이터 손실 | 높음 | 매우 낮음 | 백업 전략, 롤백 계획 |

### 10.2 롤백 계획
```
각 Phase 별 롤백 포인트:
- Phase 2 완료 후: Spring 5.x 롤백 가능
- Phase 3 완료 후: MyBatis 롤백 가능
- Phase 4 완료 후: Spring MVC 롤백 가능

롤백 시나리오:
1. 데이터베이스 백업 복원
2. 애플리케이션 이전 버전 배포
3. 설정 파일 복원
4. 트래픽 전환
```

---

## 11. 마일스톤 및 체크포인트

### 11.1 Phase별 완료 기준
```
Phase 1 완료:
✓ 개발 환경 구축
✓ 테스트 커버리지 50% 이상
✓ 마이그레이션 계획 승인

Phase 2 완료:
✓ Spring 5.x 컴파일 성공
✓ 기존 기능 정상 동작
✓ 단위 테스트 통과

Phase 3 완료:
✓ MyBatis 전환 완료
✓ SQL 쿼리 정상 실행
✓ DAO 테스트 통과

Phase 4 완료:
✓ Struts 제거 완료
✓ 모든 URL 정상 동작
✓ E2E 테스트 통과

Phase 5 완료:
✓ 회귀 테스트 100% 통과
✓ 성능 목표 달성
✓ 보안 검증 완료

Phase 6 완료:
✓ 프로덕션 배포 완료
✓ 모니터링 정상
✓ 문서화 완료
```

---

## 12. 참고 자료

### 12.1 공식 문서
- Spring Framework 5.x Reference: https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/
- MyBatis 3 Documentation: https://mybatis.org/mybatis-3/
- Struts 2 to Spring MVC Migration Guide

### 12.2 도구 및 라이브러리
- Spring Tool Suite (STS)
- MyBatis Generator
- IntelliJ IDEA Spring Support

---

## 다음 문서
- `03-detailed-migration-guide.md` - 상세 마이그레이션 가이드
- `04-migration-checklist.md` - 단계별 체크리스트
