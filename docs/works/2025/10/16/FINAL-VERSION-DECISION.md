# 최종 버전 결정

**결정일**: 2025-10-16  
**결정자**: 프로젝트 팀

---

## ✅ 최종 확정 버전

### 핵심 기술 스택

```
Java:              21 LTS (Eclipse Temurin)
Spring Framework:  6.1.13 (2024년 10월 최신)
Spring Boot:      3.3.5 (선택사항, 2024년 최신)
MyBatis:          3.5.16 (2024년 최신)
Tomcat:           10.1.30 (Jakarta EE 10)
Maven:            3.9.9 (현재 설치됨)
```

---

## Java 21 LTS (Eclipse Temurin) ⭐

### 선택 이유

**Eclipse Temurin (구 AdoptOpenJDK)**:
- Eclipse Foundation이 관리하는 공식 OpenJDK 배포판
- 100% 오픈소스, 무료
- 장기 지원 (LTS)
- 높은 신뢰성과 안정성
- 주요 기업들이 채택 (Red Hat, IBM, Google 등)

### Java 21 LTS 특징

**릴리스**: 2023년 9월  
**지원 기간**: 2031년까지 (8년!)  
**현재 최신 패치**: 21.0.5 (2024년 10월)

**주요 신기능**:
- Virtual Threads (Project Loom) - 경량 스레드
- Pattern Matching for switch
- Record Patterns
- Sequenced Collections
- String Templates (Preview)

**성능**:
- Java 8 대비 **40-50% 성능 향상**
- 메모리 사용량 개선
- GC 성능 향상 (G1GC, ZGC)

---

## 확정 버전 상세

### 1. Java 21 LTS (Eclipse Temurin)

**설치**:
```bash
# Ubuntu/Debian
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo apt-key add -
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | sudo tee /etc/apt/sources.list.d/adoptium.list

sudo apt-get update
sudo apt-get install temurin-21-jdk -y

# 확인
java -version
# openjdk version "21.0.5" 2024-10-15 LTS
# OpenJDK Runtime Environment Temurin-21.0.5+11 (build 21.0.5+11-LTS)
```

**Maven 설정**:
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <maven.compiler.release>21</maven.compiler.release>
</properties>
```

---

### 2. Spring Framework 6.1.13

**Maven 의존성**:
```xml
<properties>
    <spring.version>6.1.13</spring.version>
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
</dependencies>
```

**요구사항**:
- Java 17+ (Java 21 ✅)
- Jakarta EE 9+ (Jakarta EE 10 ✅)

---

### 3. MyBatis 3.5.16

**Maven 의존성**:
```xml
<properties>
    <mybatis.version>3.5.16</mybatis.version>
    <mybatis-spring.version>3.0.3</mybatis-spring.version>
</properties>

<dependencies>
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
</dependencies>
```

---

### 4. Jakarta EE 10

**Servlet API**:
```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

**JavaMail → Jakarta Mail**:
```xml
<dependency>
    <groupId>jakarta.mail</groupId>
    <artifactId>jakarta.mail-api</artifactId>
    <version>2.1.3</version>
</dependency>
<dependency>
    <groupId>org.eclipse.angus</groupId>
    <artifactId>angus-mail</artifactId>
    <version>2.0.3</version>
</dependency>
```

**Validation**:
```xml
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
```

---

### 5. Apache Tomcat 10.1.30

**Jakarta EE 10 지원**:
```bash
# Tomcat 10.1.x 다운로드
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.30/bin/apache-tomcat-10.1.30.tar.gz
tar -xzf apache-tomcat-10.1.30.tar.gz
sudo mv apache-tomcat-10.1.30 /opt/tomcat10
```

---

## Java 21 신기능 활용

### 1. Virtual Threads (Project Loom)

**기존 (Thread Pool)**:
```java
ExecutorService executor = Executors.newFixedThreadPool(100);
executor.submit(() -> processEmail());
```

**Java 21 (Virtual Threads)**:
```java
// 수백만 개의 경량 스레드 생성 가능!
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -> processEmail());
```

**효과**: 동시 처리 성능 **대폭 향상**, 메모리 효율 증가

---

### 2. Pattern Matching for switch

**기존**:
```java
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    System.out.println(i * 2);
}
```

**Java 21**:
```java
switch (obj) {
    case String s  -> System.out.println(s.length());
    case Integer i -> System.out.println(i * 2);
    case null      -> System.out.println("null");
    default        -> System.out.println("unknown");
}
```

---

### 3. Record Patterns

**DTO 클래스 간소화**:
```java
// 기존 (Verbose)
public class MailVO {
    private String mailId;
    private String subject;
    // getters, setters, equals, hashCode, toString...
}

// Java 21 (Records)
public record MailVO(String mailId, String subject, String sender) {
    // 자동으로 생성: getters, equals, hashCode, toString
}
```

---

### 4. Sequenced Collections

```java
// 첫 번째/마지막 요소 접근이 쉬워짐
List<String> list = new ArrayList<>();
String first = list.getFirst();  // Java 21
String last = list.getLast();    // Java 21
```

---

## 패키지 변경 자동화

### OpenRewrite 사용 (권장)

**Maven 설정**:
```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.38.1</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.JavaxMigrationToJakarta</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

**실행**:
```bash
# javax → jakarta 자동 변환
mvn rewrite:run

# 변환 결과: 563개 파일 자동 처리
```

**소요 시간**: 실행 10분 + 검증 2-3일

---

## 업데이트된 pom.xml (최종)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.terracetech.tims</groupId>
    <artifactId>tims7-webmail</artifactId>
    <version>7.5.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        
        <!-- Java 21 LTS (Eclipse Temurin) -->
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <maven.compiler.release>21</maven.compiler.release>
        
        <!-- Spring Framework 6.1.13 (2024-10 최신) -->
        <spring.version>6.1.13</spring.version>
        
        <!-- MyBatis 3.5.16 (2024 최신) -->
        <mybatis.version>3.5.16</mybatis.version>
        <mybatis-spring.version>3.0.3</mybatis-spring.version>
        
        <!-- Jakarta EE 10 -->
        <jakarta-servlet.version>6.0.0</jakarta-servlet.version>
        <jakarta-mail.version>2.1.3</jakarta-mail.version>
        <jakarta-validation.version>3.0.2</jakarta-validation.version>
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

        <!-- Jakarta Servlet (javax → jakarta) -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>${jakarta-servlet.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.1.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>3.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>3.0.1</version>
        </dependency>

        <!-- Jakarta Mail (javax.mail → jakarta.mail) -->
        <dependency>
            <groupId>jakarta.mail</groupId>
            <artifactId>jakarta.mail-api</artifactId>
            <version>${jakarta-mail.version}</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.angus</groupId>
            <artifactId>angus-mail</artifactId>
            <version>2.0.3</version>
        </dependency>

        <!-- Jakarta Validation -->
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>${jakarta-validation.version}</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>8.0.1.Final</version>
        </dependency>

        <!-- Jackson (JSON) -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.17.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.17.2</version>
        </dependency>

        <!-- Logging (SLF4J + Logback) -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.16</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.5.8</version>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.11.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>5.14.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>tims-webmail</finalName>
        <plugins>
            <!-- Maven Compiler Plugin (Java 21) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <release>21</release>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>

            <!-- Maven WAR Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <warSourceDirectory>web</warSourceDirectory>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

            <!-- Maven Surefire Plugin (Test) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.0</version>
            </plugin>

            <!-- OpenRewrite (javax → jakarta 자동 변환) -->
            <plugin>
                <groupId>org.openrewrite.maven</groupId>
                <artifactId>rewrite-maven-plugin</artifactId>
                <version>5.38.1</version>
                <configuration>
                    <activeRecipes>
                        <recipe>org.openrewrite.java.migrate.JavaxMigrationToJakarta</recipe>
                        <recipe>org.openrewrite.java.migrate.UpgradeToJava21</recipe>
                    </activeRecipes>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.openrewrite.recipe</groupId>
                        <artifactId>rewrite-migrate-java</artifactId>
                        <version>2.24.0</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Java 21 주요 기능 활용

### 1. Virtual Threads (대용량 메일 처리)

```java
// 메일 발송 시 Virtual Threads 활용
@Service
public class MailSender {
    
    public void sendBulkMails(List<MailVO> mails) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (MailVO mail : mails) {
                executor.submit(() -> sendMail(mail));
            }
        }
        // 수천 개 메일을 동시에 처리 가능!
    }
}
```

### 2. Record (VO 클래스 간소화)

```java
// API Response
public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    long timestamp
) {}

// 사용
var response = new ApiResponse<>(true, mailList, null, System.currentTimeMillis());
```

### 3. Pattern Matching

```java
// 타입별 처리 간소화
public String formatAddress(Object addr) {
    return switch (addr) {
        case String s -> s;
        case MailAddressBean bean -> bean.getEmail();
        case null -> "unknown";
        default -> addr.toString();
    };
}
```

---

## 마이그레이션 경로 (최종)

```
현재: Java 8 + Spring 2.5.6 + iBATIS 2.3.4
  ↓
Phase 2-1: Java 21 (Eclipse Temurin) 설치 (1개월)
  ↓
Phase 2-2: Spring 5.3.x 중간 단계 (1개월)
  ↓
Phase 2-3: Spring 6.1.x + jakarta 패키지 (1개월)
  - OpenRewrite로 javax → jakarta 자동 변환
  - 563개 파일 처리
  ↓
Phase 2-4: 검증 및 안정화 (1개월)
  ↓
Phase 3: MyBatis 3.5.16 (2-3개월)
  ↓
Phase 3.5: REST API (1-2개월)
  ↓
Phase 4: Spring MVC (3-4개월)
  ↓
Phase 5-6: 테스트 및 배포 (2-3개월)

목표: Java 21 + Spring 6.1.13 + MyBatis 3.5.16
```

---

## 성능 향상 예상

### Java 8 → Java 21

```
처리 속도:    40-50% 향상
메모리:      20-30% 절감
GC 성능:     50% 향상
동시 처리:   Virtual Threads로 10배+ 향상
```

### Spring 2.5 → Spring 6.1

```
응답 시간:   20-30% 개선
처리량:     30-40% 향상
메모리:     10-20% 절감
```

**총 예상 성능 향상**: **50-70%** 🚀

---

## 최종 결정 사항

### ✅ 확정 버전

```
Java:              21 LTS (Eclipse Temurin 21.0.5+)
Spring Framework:  6.1.13
MyBatis:          3.5.16
Jakarta EE:       10
Tomcat:           10.1.30
Maven:            3.9.9
```

### 추가 도구

```
OpenRewrite:      5.38.1 (javax → jakarta 자동 변환)
JUnit:           5.11.2
Mockito:         5.14.1
Jackson:         2.17.2
Logback:         1.5.8
```

---

## 총 프로젝트 기간

**최종 예상**: 13-18개월

```
Phase 0: 1주 (완료 ✅)
Phase 1: 2개월
Phase 2: 4개월 (Java 21 + Spring 6.1.x)
Phase 3: 2-3개월 (MyBatis)
Phase 3.5: 1-2개월 (REST API)
Phase 4: 3-4개월 (Spring MVC)
Phase 5: 1-2개월 (테스트)
Phase 6: 1개월 (배포)
```

---

## 승인 및 다음 단계

### ✅ 최종 승인 사항
- Java 21 LTS (Eclipse Temurin) 사용
- Spring Framework 6.1.13 (최신)
- MyBatis 3.5.16 (최신)
- 총 프로젝트 기간: 13-18개월

### 다음 단계
1. Phase 2 착수 준비
2. Java 21 (Eclipse Temurin) 설치
3. pom.xml 작성 시작

---

**최첨단 기술 스택으로 향후 7년간 안정적으로 운영!** 🚀

**승인**: ________________  
**날짜**: 2025-10-16

