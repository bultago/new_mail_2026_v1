# Java 및 Spring Framework 버전 업그레이드 권장 사항

**분석일**: 2025-10-16  
**목적**: 최신 안정 버전으로 업그레이드

---

## 현재 버전

### 확인된 버전
```
Java:              OpenJDK 1.8.0_462 (Java 8)
Spring Framework:  2.5.6 (2007년 릴리스)
iBATIS:           2.3.4.726 (2010년 중단)
Struts:           2.3.32 (2016년 릴리스)
```

---

## 최신 버전 조사 (2025년 10월 기준)

### Java LTS 버전

| 버전 | 릴리스 | 지원 종료 | 상태 | 권장 |
|------|--------|-----------|------|------|
| Java 8 | 2014년 | 2030년 (Extended) | ✅ 현재 사용 중 | ⚠️ 구버전 |
| Java 11 | 2018년 | 2026년 | ✅ LTS | 🟡 권장 (안정) |
| Java 17 | 2021년 | 2029년 | ✅ LTS | 🟢 강력 권장 |
| Java 21 | 2023년 | 2031년 | ✅ LTS (최신) | 🟢 최신 LTS |

### Spring Framework 버전

| 버전 | 릴리스 | 지원 종료 | Java 요구 | 권장 |
|------|--------|-----------|-----------|------|
| 2.5.x | 2007년 | 종료 | Java 5+ | ❌ 현재 (레거시) |
| 5.3.x | 2020년 | 2024년 말 | Java 8+ | ⚠️ 지원 종료 임박 |
| 6.0.x | 2022년 | 2025년 8월 | Java 17+ | 🟡 권장 (안정) |
| 6.1.x | 2023년 | 진행 중 | Java 17+ | 🟢 강력 권장 |
| 6.2.x | 2024년 | 진행 중 | Java 17+ | 🟢 최신 안정 |

### MyBatis 버전

| 버전 | 릴리스 | 상태 | 권장 |
|------|--------|------|------|
| 3.5.13 | 2023년 | 안정 | 🟢 권장 |
| 3.5.16 | 2024년 | 최신 | 🟢 최신 |

---

## 권장 버전 (2가지 옵션)

### 옵션 1: 안정적 접근 (권장) ⭐

```
Java:              17 LTS (2021년, 2029년까지 지원)
Spring Framework:  6.1.x (Spring Boot 3.2.x 호환)
MyBatis:          3.5.16 (최신 안정)
Spring Boot:      3.2.x (선택사항)
```

**장점**:
- ✅ 장기 지원 보장 (Java 17: 2029년까지)
- ✅ 현대적 기능 활용 (Records, Pattern Matching 등)
- ✅ 성능 개선 (Java 17은 Java 8 대비 30% 향상)
- ✅ Spring 6.x의 새로운 기능 (HTTP Interface, AOT 등)

**단점**:
- ⚠️ Java 8 → 17 점프가 큼 (9년 차이)
- ⚠️ Spring 2.5 → 6.x 점프가 큼 (변경사항 많음)

**마이그레이션 난이도**: 🟡 중간-높음

---

### 옵션 2: 보수적 접근

```
Java:              11 LTS (2018년, 2026년까지 지원)
Spring Framework:  6.0.x (Java 11 호환 가능하지만 17 권장)
MyBatis:          3.5.16
```

**장점**:
- ✅ Java 8 → 11 업그레이드는 비교적 쉬움
- ✅ 변경사항 적음

**단점**:
- ❌ Java 11 지원이 2026년 종료 (2년 후)
- ❌ Spring 6.x는 Java 17 권장
- ❌ 빠른 재업그레이드 필요

**마이그레이션 난이도**: 🟢 낮음

---

## 최종 권장: 옵션 1 (Java 17 + Spring 6.1.x) ⭐

### 권장 버전

```xml
<properties>
    <java.version>17</java.version>
    <spring.version>6.1.13</spring.version>
    <spring-boot.version>3.2.10</spring-boot.version>
    <mybatis.version>3.5.16</mybatis.version>
    <mybatis-spring.version>3.0.3</mybatis-spring.version>
</properties>
```

### 이유

1. **장기 지원**
   - Java 17: 2029년까지 지원 (7년 이상)
   - Spring 6.x: 2026년 이후까지 지원

2. **성능 향상**
   - Java 17: Java 8 대비 약 30% 성능 향상
   - Spring 6.x: 최적화된 성능

3. **현대적 기능**
   - Java 17: Records, Text Blocks, Pattern Matching
   - Spring 6.x: HTTP Interface, Observability

4. **보안**
   - 최신 보안 패치 적용
   - 취약점 대응 신속

---

## 마이그레이션 전략 업데이트

### Phase 2: Spring 2.5.6 → 6.1.x + Java 8 → 17

#### 단계 1: Java 17 설치
```bash
# Ubuntu
sudo apt-get install openjdk-17-jdk -y

# 버전 확인
java -version
# openjdk version "17.0.x"
```

#### 단계 2: pom.xml 작성
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    
    <!-- Spring Framework 6.1.x -->
    <spring.version>6.1.13</spring.version>
    
    <!-- MyBatis -->
    <mybatis.version>3.5.16</mybatis.version>
    <mybatis-spring.version>3.0.3</mybatis-spring.version>
</properties>

<dependencies>
    <!-- Spring Framework 6.x -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
        <version>${spring.version}</version>
    </dependency>
    
    <!-- MyBatis -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>${mybatis.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>${mybatis-spring.version}</version>
    </dependency>
    
    <!-- Servlet API (Jakarta EE) -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

#### 단계 3: Jakarta EE 마이그레이션

**중요!** Spring 6.x는 Jakarta EE를 사용합니다.

```
javax.servlet   → jakarta.servlet
javax.mail      → jakarta.mail
javax.validation → jakarta.validation
```

**영향**:
- import 문 일괄 변경 필요
- 563개 Java 파일 검토

---

## Spring Boot 도입 검토 (선택사항)

### Spring Boot 3.2.x 장점

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.10</version>
</parent>
```

**장점**:
- ✅ 자동 설정 (AutoConfiguration)
- ✅ 임베디드 Tomcat (배포 간소화)
- ✅ 의존성 관리 자동화
- ✅ Actuator (모니터링)
- ✅ 개발 생산성 향상

**고려사항**:
- 기존 WAR 배포 방식에서 변경
- 학습 곡선

---

## 최종 권장 버전 (업데이트)

### 프로덕션 권장 ⭐

```
Java:              17 LTS (OpenJDK 17.0.12)
Spring Framework:  6.1.13 (2024년 10월 최신)
MyBatis:          3.5.16
Tomcat:           10.1.x (Jakarta EE 10 지원)
Servlet API:      Jakarta Servlet 6.0
```

### 컴파일 설정

```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
<maven.compiler.release>17</maven.compiler.release>
```

---

## 주요 변경사항

### 1. Java 8 → 17 변경사항

**새로운 기능 활용 가능**:
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching for instanceof (Java 16+)
- Sealed Classes (Java 17)

**제거된 기능**:
- Nashorn JavaScript 엔진 (제거됨)
- Applet API (제거됨)

### 2. Spring 2.5 → 6.1 주요 변경

**Breaking Changes**:
- javax.* → jakarta.* 패키지 변경
- Spring 5.x 이후 많은 API 변경
- Java 17 필수

**새로운 기능**:
- HTTP Interface (REST Client)
- Observability (메트릭, 트레이싱)
- Native Image 지원 (GraalVM)
- AOT (Ahead-of-Time) 컴파일

---

## 마이그레이션 난이도 재평가

### 기존 계획 (Spring 5.3.x)
```
Java 8 → Java 11
Spring 2.5 → Spring 5.3.x
난이도: 🟡 중간
```

### 업데이트 계획 (Spring 6.1.x)
```
Java 8 → Java 17 (큰 점프!)
Spring 2.5 → Spring 6.1.x (큰 점프!)
jakarta.* 패키지 변경 (563개 파일)
난이도: 🔴 높음
```

---

## 단계적 업그레이드 전략 (수정)

### 전략 A: 직접 점프 (빠르지만 위험)

```
현재: Java 8 + Spring 2.5
  ↓ (한 번에)
목표: Java 17 + Spring 6.1.x
```

**기간**: Phase 2에서 3-4개월  
**위험도**: 🔴 높음

### 전략 B: 단계적 업그레이드 (안전) ⭐ 권장

```
Step 1: Java 8 → Java 17 (Phase 2-1, 1개월)
  ↓
Step 2: Spring 2.5 → Spring 5.3.x (Phase 2-2, 1개월)
  ↓ (iBATIS → MyBatis, Phase 3)
Step 3: Spring 5.3.x → Spring 6.1.x (Phase 2-3, 1개월)
  ↓
Step 4: javax.* → jakarta.* (Phase 2-4, 1개월)
```

**기간**: Phase 2에서 4개월  
**위험도**: 🟡 중간

---

## 최종 권장 사항

### 🎯 권장 버전 (최신 안정)

```xml
<!-- pom.xml -->
<properties>
    <!-- Java 17 LTS -->
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    
    <!-- Spring Framework 6.1.13 (2024년 10월 최신) -->
    <spring.version>6.1.13</spring.version>
    
    <!-- MyBatis 3.5.16 (2024년 최신) -->
    <mybatis.version>3.5.16</mybatis.version>
    <mybatis-spring.version>3.0.3</mybatis-spring.version>
    
    <!-- Jakarta EE 10 -->
    <jakarta-servlet.version>6.0.0</jakarta-servlet.version>
    <jakarta-mail.version>2.1.3</jakarta-mail.version>
    
    <!-- Tomcat 10.1.x -->
    <tomcat.version>10.1.30</tomcat.version>
</properties>

<dependencies>
    <!-- Spring Framework 6.1.x -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    
    <!-- MyBatis -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>${mybatis.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>${mybatis-spring.version}</version>
    </dependency>
    
    <!-- Jakarta Servlet (javax → jakarta 변경) -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>${jakarta-servlet.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 추가 필요 작업

### javax → jakarta 패키지 변경

**영향 범위**: 563개 Java 파일

```java
// 변경 전
import javax.servlet.*;
import javax.mail.*;

// 변경 후
import jakarta.servlet.*;
import jakarta.mail.*;
```

**자동 변환 도구**:
- OpenRewrite (권장)
- IntelliJ IDEA Migration Assistant
- 정규식 일괄 치환

---

## 업데이트된 Phase 2 작업

### Phase 2-1: Java 17 업그레이드 (1개월)
- Java 17 설치
- 컴파일 설정 변경
- 신규 문법 활용 검토

### Phase 2-2: Spring 5.3.x 중간 단계 (1개월)
- Spring 2.5 → 5.3.x
- Struts 2와 호환성 유지
- 테스트

### Phase 2-3: Spring 6.1.x 업그레이드 (1개월)
- Spring 5.3 → 6.1.x
- jakarta.* 패키지 변경
- 테스트

### Phase 2-4: 최종 검증 (1개월)
- 전체 테스트
- 성능 벤치마크
- 보안 검증

**Phase 2 총 기간**: 2-3개월 → **4개월로 연장**

---

## 예상 일정 업데이트

```
기존 계획:
- Phase 2: 2-3개월 (Spring 5.3.x)

업데이트:
- Phase 2: 4개월 (Java 17 + Spring 6.1.x)
  ├─ Java 17 업그레이드: 1개월
  ├─ Spring 5.3.x: 1개월
  ├─ Spring 6.1.x + jakarta: 1개월
  └─ 검증: 1개월

전체 프로젝트: 12-17개월 → 13-18개월 (1개월 증가)
```

---

## 비용 대비 효과

### 추가 비용
- 개발 시간: +1개월
- 학습 시간: Java 17, Spring 6.x

### 장기 이익
- 7년 이상 장기 지원 (Java 17)
- 30% 성능 향상
- 최신 보안 패치
- 개발 생산성 향상 (Records, Text Blocks 등)
- 미래 기술 대응 (GraalVM, Virtual Threads 등)

**ROI**: 초기 투자 대비 장기 이익 **매우 높음** ✅

---

## 결론 및 권장사항

### 🎯 최종 권장

**Java 17 + Spring Framework 6.1.13 + MyBatis 3.5.16**

### 이유
1. 장기 지원 보장 (2029년까지)
2. 최고의 성능과 보안
3. 현대적 개발 경험
4. 한 번의 큰 업그레이드로 장기간 안정 운영

### 추가 작업
- Phase 2에 1개월 추가 (총 4개월)
- jakarta.* 패키지 변경 작업 추가

---

**승인 요청**: Java 17 + Spring 6.1.x로 목표 버전 업데이트

**작성자**: 아키텍트 + Backend 리더  
**작성일**: 2025-10-16

