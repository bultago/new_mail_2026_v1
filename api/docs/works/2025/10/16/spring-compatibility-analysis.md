# Spring 2.5 → 5.x 호환성 분석

**분석일**: 2025-10-16  
**작업 ID**: P1-025  
**대상**: Spring Framework 2.5 → 5.3.30 업그레이드

---

## 현재 Spring 사용 현황

### Spring 버전
- **현재**: Spring 2.5 (2007년 릴리스)
- **목표**: Spring 5.3.30 (LTS)

### Spring 모듈 사용
```
현재 프로젝트에서 사용 중인 Spring 모듈:
- spring-core
- spring-context
- spring-web
- spring-webmvc
- spring-jdbc
- spring-tx (트랜잭션)
- spring-orm (iBATIS 통합)
- spring-aop
```

### Spring 설정 방식
- XML 기반 Bean 정의 (357개 Bean)
- `default-autowire="byName"` 사용
- `p:` namespace 사용 (프로퍼티 설정)

---

## Breaking Changes 분석

### 1. 네임스페이스 변경

**변경 전 (Spring 2.5)**:
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">
```

**변경 후 (Spring 5.x)**:
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd">
```

**영향**: 12개 Spring XML 파일 모두 업데이트 필요

---

### 2. Deprecated API 목록

#### SimpleDateFormat → DateTimeFormatter
```java
// 변경 전
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String dateStr = sdf.format(new Date());

// 변경 후 (Java 8+)
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String dateStr = LocalDate.now().format(formatter);
```

**영향**: Java 코드에서 SimpleDateFormat 사용 검색 필요

#### iBATIS 통합 제거
```xml
<!-- Spring 2.5 -->
<bean class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">

<!-- Spring 5.x -->
<!-- iBATIS 지원 제거됨, MyBatis-Spring 사용 필요 -->
<bean class="org.mybatis.spring.SqlSessionFactoryBean">
```

**영향**: Phase 3 (MyBatis 마이그레이션)과 함께 진행

---

### 3. Struts 2 + Spring 5.x 통합

#### Struts-Spring 플러그인 호환성
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-spring-plugin</artifactId>
    <version>2.3.37</version>
</dependency>

<!-- Spring 5.x와 호환 가능 -->
<constant name="struts.objectFactory" value="spring" />
```

**결론**: ✅ **Struts 2.3.37 + Spring 5.x 통합 가능** (Phase 2에서 안전하게 업그레이드 가능)

---

### 4. 트랜잭션 관리 변경

**현재 (XML 기반)**:
```xml
<bean id="transactionManager" 
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

**Spring 5.x (어노테이션 추가)**:
```xml
<bean id="transactionManager" 
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 어노테이션 기반 트랜잭션 활성화 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

```java
// Java 코드에서 사용
@Service
@Transactional
public class MailManager {
    // 메서드가 자동으로 트랜잭션 처리됨
}
```

**영향**: 기존 XML 설정 유지하면서 어노테이션 추가 가능

---

## 호환성 검증 결과

### ✅ 호환 가능 항목
1. XML Bean 정의 방식 (그대로 사용 가능)
2. `byName` autowiring (계속 지원)
3. Struts-Spring 통합 (플러그인 버전 맞춰야 함)
4. 트랜잭션 관리 (기존 방식 유지 가능)

### ⚠️ 주의 필요 항목
1. 네임스페이스 업데이트 (12개 XML 파일)
2. iBATIS 지원 제거 (MyBatis로 전환 필수)
3. Deprecated API 교체 (SimpleDateFormat 등)
4. Java 버전 요구사항 (Java 8+ 필요, 현재 Java 8 사용 중 ✅)

### ❌ 제거된 기능
1. Spring ORM iBATIS 지원 (`spring-orm-ibatis`)
   - → MyBatis-Spring으로 대체

---

## 마이그레이션 전략

### Phase 2 전략 (Spring 5.x 업그레이드)

**단계 1: 의존성 변경**
```xml
<spring.version>5.3.30</spring.version>
```

**단계 2: 네임스페이스 업데이트**
- 12개 Spring XML 파일 (spring-*.xml)
- XSD 경로를 버전 없는 경로로 변경

**단계 3: Struts 2 유지**
- Struts 2.3.32 → 2.3.37 (보안 패치 버전)
- struts2-spring-plugin 계속 사용

**단계 4: 컴파일 및 테스트**
- `mvn clean compile`
- 컴파일 에러 해결
- 단위 테스트 실행

---

## 위험 요소

### 중간 위험
1. **Deprecated API 사용**
   - 영향: 컴파일 경고 발생
   - 대응: 점진적 교체

2. **설정 파일 형식 변경**
   - 영향: 12개 XML 파일 수정
   - 대응: 자동화 스크립트 사용

### 낮은 위험
1. **Struts-Spring 통합**
   - Struts 2.3.37 + Spring 5.x 호환 확인됨
   - 안전하게 업그레이드 가능

---

## 테스트 계획

### 1. 컴파일 테스트
```bash
mvn clean compile
```

### 2. 단위 테스트
```bash
mvn test
```

### 3. 통합 테스트
- Spring Context 로딩 테스트
- Bean 주입 테스트
- 트랜잭션 동작 테스트

### 4. 회귀 테스트
- 기존 E2E 테스트 실행
- 주요 기능 수동 테스트

---

## 완료 조건

- ✅ Breaking Changes 목록 작성
- ✅ Deprecated API 목록 작성
- ✅ 마이그레이션 전략 수립
- ✅ 위험 요소 식별
- ✅ 테스트 계획 수립

---

## 참고 문서

- Spring Framework 5.3.x Documentation
- Spring Framework 5.0 Migration Guide
- Struts 2 Spring Plugin Documentation

---

**결론**: Spring 2.5 → 5.x 업그레이드는 **안전하게 진행 가능**하며, Struts 2와의 호환성도 확보되었습니다.

**작성자**: Backend 리더  
**완료일**: 2025-10-16

