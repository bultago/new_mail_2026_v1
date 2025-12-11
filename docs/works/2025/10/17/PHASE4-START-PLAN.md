# Phase 4 시작 계획

**작업 시작**: 2025-10-17  
**Phase**: 4 - Struts2 → Spring MVC 전환  
**예상 기간**: 2-3주

---

## 🎯 목표

1. **Struts2 완전 제거**
2. **Spring MVC 전환**
3. **javax/jakarta 충돌 해결**
4. **컴파일 성공**

---

## 📋 현재 상태 분석

### 기존 구조
```
Struts2 Filter (web.xml)
  ↓
struts.xml
  ↓
Action 클래스들
  ↓
JSP 페이지
```

### 기존 Spring 설정
- **Spring 2.5** 사용 중
- Service/DAO 레이어만 Spring 관리
- Controller(Action) 레이어는 Struts2
- 설정 파일: `web-config/spring-*.xml` (모듈별)

### 문제점
1. **Struts2가 javax.servlet 사용** → Jakarta Servlet과 충돌
2. Action 클래스가 Struts2 의존성
3. struts.xml 설정 파일

---

## 🚀 Phase 4 작업 단계

### Step 1: Spring MVC 설정 (1-2일)

#### [P4-001] Spring MVC 설정 파일 생성
**파일**: `web/WEB-INF/spring-mvc-config.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-6.1.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-6.1.xsd
           http://www.springframework.org/schema/mvc
           http://www.springframework.org/schema/mvc/spring-mvc-6.1.xsd">

    <!-- Component Scan -->
    <context:component-scan base-package="com.terracetech.tims.webmail.*.controller" />
    
    <!-- Annotation Driven -->
    <mvc:annotation-driven />
    
    <!-- ViewResolver -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/" />
        <property name="suffix" value=".jsp" />
    </bean>
    
    <!-- Static Resources -->
    <mvc:resources mapping="/resources/**" location="/resources/" />
    <mvc:resources mapping="/js/**" location="/js/" />
    <mvc:resources mapping="/css/**" location="/css/" />
    <mvc:resources mapping="/images/**" location="/images/" />
    
    <!-- Multipart Resolver -->
    <bean id="multipartResolver"
          class="org.springframework.web.multipart.support.StandardServletMultipartResolver" />
    
    <!-- Interceptors -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/resources/**"/>
            <mvc:exclude-mapping path="/js/**"/>
            <mvc:exclude-mapping path="/css/**"/>
            <mvc:exclude-mapping path="/images/**"/>
            <bean class="com.terracetech.tims.webmail.common.interceptor.AuthInterceptor" />
        </mvc:interceptor>
    </mvc:interceptors>
    
</beans>
```

#### [P4-002] web.xml 수정
**작업**: DispatcherServlet 추가, Struts2 Filter 제거

```xml
<!-- 기존 Struts2 Filter 제거 -->
<!-- <filter>
    <filter-name>struts</filter-name>
    <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
</filter> -->

<!-- Spring DispatcherServlet 추가 -->
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-mvc-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <multipart-config>
        <max-file-size>52428800</max-file-size>
        <max-request-size>52428800</max-request-size>
        <file-size-threshold>0</file-size-threshold>
    </multipart-config>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

---

### Step 2: 샘플 Controller 작성 (1일)

#### [P4-003] 간단한 Action → Controller 변환

**예시: WelcomeAction → WelcomeController**

```java
// Before: Struts2 Action
public class WelcomeAction extends BaseAction {
    public String execute() {
        return SUCCESS;
    }
}

// After: Spring MVC Controller
@Controller
@RequestMapping("/welcome")
public class WelcomeController {
    
    @GetMapping
    public String welcome(Model model, HttpSession session) {
        return "welcome";
    }
}
```

#### [P4-004] 테스트 및 검증
- 컴파일 확인
- 서버 구동 확인
- 페이지 로딩 확인

---

### Step 3: 모듈별 체계적 변환 (2주)

#### 우선순위
1. **Common/Login** (가장 기본) - 3일
2. **Home** (홈 화면) - 2일
3. **Mail** (핵심 기능) - 5일
4. **Setting** (설정) - 2일
5. **Address** (주소록) - 2일
6. **나머지 모듈** - 3일

#### 변환 패턴

**1. Controller 변환**
```java
// Struts2 Annotation 제거
// @Namespace("/mail")
// @Results({ ... })

// Spring MVC Annotation 추가
@Controller
@RequestMapping("/mail")

// Method 변환
// public String execute() { return SUCCESS; }
@GetMapping("/list")
public String list(Model model) { return "mail/list"; }
```

**2. Request Parameter 처리**
```java
// Before
private String userId;
public void setUserId(String userId) { this.userId = userId; }

// After
@GetMapping
public String method(@RequestParam String userId) { ... }
```

**3. Session 처리**
```java
// Before
HttpSession session = ServletActionContext.getRequest().getSession();

// After
@GetMapping
public String method(HttpSession session) { ... }
```

**4. Model 데이터 전달**
```java
// Before
request.setAttribute("data", data);

// After
model.addAttribute("data", data);
```

---

### Step 4: JSP 수정 (1주)

#### Struts2 태그 → Spring/JSTL 태그

```jsp
<!-- Before: Struts2 -->
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:property value="userName" />
<s:form action="login">
    <s:textfield name="userId" />
</s:form>

<!-- After: Spring/JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
${userName}
<form:form action="/login" modelAttribute="loginForm">
    <form:input path="userId" />
</form:form>
```

---

### Step 5: Struts2 제거 (1일)

#### [P4-005] pom.xml에서 Struts2 의존성 제거
```xml
<!-- 제거할 의존성 -->
<!-- <dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-core</artifactId>
</dependency> -->
```

#### [P4-006] struts.xml 삭제
#### [P4-007] BaseAction 등 Struts2 관련 클래스 정리

---

## 📊 예상 작업량

| 단계 | 작업 | 예상 시간 | 파일 수 |
|------|------|-----------|---------|
| Step 1 | Spring MVC 설정 | 1-2일 | 2개 |
| Step 2 | 샘플 Controller | 1일 | 3개 |
| Step 3 | 모듈별 변환 | 2주 | 약 150개 |
| Step 4 | JSP 수정 | 1주 | 약 200개 |
| Step 5 | Struts2 제거 | 1일 | 5개 |
| **합계** | **3-4주** | **약 360개** |

---

## ⚠️ 주의사항

### 1. 점진적 접근
- 한 번에 모든 Action을 변환하지 말 것
- 모듈별로 하나씩 완료 후 다음 진행
- 각 모듈 변환 후 테스트 필수

### 2. 호환성 유지
- 기존 URL 구조 최대한 유지
- 사용자 영향 최소화

### 3. 테스트
- 각 Controller 단위 테스트
- 통합 테스트
- UI 테스트

---

## 🎯 현재 시작 단계

### 즉시 시작 가능한 작업

1. **[P4-001] Spring MVC 설정 파일 생성** ← 지금 시작
2. **[P4-002] web.xml 수정**
3. **[P4-003] 샘플 Controller 작성**

---

## 📝 진행 방식

### Option A: 단계별 승인 (권장)
- 각 Step별로 작업 후 검토
- 문제 발생 시 빠른 롤백 가능
- 안정적

### Option B: 연속 진행
- 한 번에 여러 Step 진행
- 빠른 진행
- 리스크 높음

---

## 🤔 사용자 결정 필요

다음 중 선택해주세요:

**A) Step 1만 먼저 진행** (Spring MVC 설정 + 샘플 Controller)
- 안전하게 설정부터 검증
- 예상 시간: 2-3시간
- 컴파일 확인 후 다음 단계

**B) Step 1-2 연속 진행** (설정 + 샘플 + 테스트)
- 동작하는 Controller까지 완성
- 예상 시간: 4-5시간
- 실제 페이지 동작 확인

**C) 전체 계획만 검토** (오늘은 문서만)
- Phase 4 계획 상세화
- 내일부터 본격 시작

---

**어떤 방식으로 진행할까요?**

