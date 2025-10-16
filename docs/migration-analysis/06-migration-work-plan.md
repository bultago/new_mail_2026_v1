# 마이그레이션 실행 작업 플랜 (TODO 기반)

## 문서 정보
- **작성일**: 2025-10-14
- **용도**: 단계별 실행 가능한 작업 플랜 및 Todo 항목 관리
- **업데이트**: 작업 진행에 따라 체크 상태 업데이트 필요

---

## 📋 전체 진행 상황 대시보드

### Phase별 진행률
```
Phase 0: 사전 준비              [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 1: 환경 구축 및 분석      [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 2: Spring 5.x 업그레이드  [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 3: MyBatis 마이그레이션   [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 3.5: DWR → REST API      [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 4: Spring MVC 전환       [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 5: 테스트 및 검증         [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
Phase 6: 최적화 및 배포         [ 0%] ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜

전체 진행률: 0/380 작업 (0%)
```

### 우선순위 작업
```
🔴 긴급 (Critical): 0개
🟡 높음 (High): 0개
🟢 중간 (Medium): 0개
⚪ 낮음 (Low): 0개
```

---

## Phase 0: 사전 준비 (착수 전)

**예상 기간**: 1주
**담당자**: [TBD]
**우선순위**: 🔴 긴급

### 1. 백업 및 버전 관리 (2일)

#### 1.1 소스코드 백업
- [ ] **[P0-001]** 전체 소스코드 tar.gz 백업 생성
  ```bash
  # 명령어
  tar -czf tims7-webmail-backup-$(date +%Y%m%d-%H%M%S).tar.gz \
    --exclude='*.log' --exclude='build/*' --exclude='target/*' .

  # 검증
  tar -tzf tims7-webmail-backup-*.tar.gz | wc -l
  ```
  - **담당**: DevOps
  - **예상시간**: 1시간
  - **완료조건**: 백업 파일 생성 및 무결성 확인

- [ ] **[P0-002]** 데이터베이스 덤프 백업 생성
  ```bash
  # PostgreSQL 예시
  pg_dump -U postgres -h localhost webmail_db > db_backup_$(date +%Y%m%d).sql

  # MySQL 예시
  mysqldump -u root -p webmail_db > db_backup_$(date +%Y%m%d).sql
  ```
  - **담당**: DBA
  - **예상시간**: 2시간
  - **완료조건**: SQL 덤프 파일 생성 및 테스트 복원 성공

- [ ] **[P0-003]** 설정 파일 별도 백업
  ```bash
  mkdir -p backup/config
  cp -r web/WEB-INF/classes/web-config/ backup/config/
  cp web/WEB-INF/web.xml backup/config/
  cp build.xml backup/config/
  ```
  - **예상시간**: 30분

#### 1.2 Git 저장소 초기화
- [ ] **[P0-004]** Git 저장소 초기화 (기존 없는 경우)
  ```bash
  git init
  git config user.name "TIMS7 Team"
  git config user.email "tims7@example.com"
  ```
  - **예상시간**: 15분

- [ ] **[P0-005]** .gitignore 파일 작성
  ```bash
  cat > .gitignore << 'EOF'
  # Build
  build/
  target/
  dist/

  # IDE
  .idea/
  .vscode/
  *.iml

  # Logs
  *.log
  logs/

  # OS
  .DS_Store
  Thumbs.db
  EOF
  ```
  - **예상시간**: 15분

- [ ] **[P0-006]** Baseline 커밋 및 태그 생성
  ```bash
  git add .
  git commit -m "Initial commit: Struts 2 baseline before migration"
  git tag -a v7.4.6S-struts2-baseline -m "Baseline before Spring MVC migration"
  ```
  - **예상시간**: 30분

#### 1.3 브랜치 전략 수립
- [ ] **[P0-007]** 브랜치 전략 문서화 및 브랜치 생성
  ```bash
  # 메인 브랜치
  git branch develop

  # Feature 브랜치
  git branch feature/spring-upgrade
  git branch feature/mybatis-migration
  git branch feature/dwr-to-rest
  git branch feature/spring-mvc-conversion
  ```
  - **예상시간**: 1시간

### 2. 승인 및 계획 (3일)

- [ ] **[P0-008]** 프로젝트 관리자 승인 획득
  - **문서**: 마이그레이션 제안서 작성
  - **예상시간**: 4시간
  - **완료조건**: 승인 서명 획득

- [ ] **[P0-009]** 이해관계자 동의 획득
  - **대상**: 개발팀, 운영팀, QA팀, 경영진
  - **예상시간**: 2일 (미팅 및 피드백)

- [ ] **[P0-010]** 일정 및 예산 승인
  - **문서**: 상세 일정표 및 예산 산출서
  - **예상시간**: 4시간

- [ ] **[P0-011]** 리소스 (인력) 할당
  - **필요인력**:
    - 개발자 3명 (풀타임)
    - QA 2명 (풀타임)
    - DBA 1명 (파트타임)
    - DevOps 1명 (파트타임)
  - **예상시간**: 2일

- [ ] **[P0-012]** 위험 관리 계획 수립
  - **문서**: 위험 관리 매트릭스 작성
  - **예상시간**: 4시간

- [ ] **[P0-013]** 커뮤니케이션 계획 수립
  - **내용**: 주간 회의, 일일 스탠드업, 월간 리뷰
  - **예상시간**: 2시간

### Phase 0 완료 기준
- ✅ 모든 백업 완료 (소스, DB, 설정)
- ✅ Git 저장소 초기화 및 Baseline 태그 생성
- ✅ 모든 승인 획득
- ✅ 리소스 할당 완료

---

## Phase 1: 환경 구축 및 분석 (2개월)

**예상 기간**: 2개월
**담당자**: 전체 팀
**우선순위**: 🔴 긴급

### 주차별 계획

#### Week 1-2: 개발 환경 구축

##### 1.1 인프라 준비
- [ ] **[P1-001]** 개발 서버 확보 (기존 Struts 2 참조용)
  - **스펙**: 8GB RAM, 4 Core, 100GB Disk
  - **OS**: CentOS 7 / Ubuntu 20.04
  - **예상시간**: 1일

- [ ] **[P1-002]** 신규 개발 서버 확보 (Spring MVC 개발용)
  - **스펙**: 16GB RAM, 8 Core, 200GB Disk
  - **예상시간**: 1일

- [ ] **[P1-003]** 테스트 환경 구축
  - 통합 테스트 서버
  - 성능 테스트 서버
  - UAT 서버
  - **예상시간**: 2일

- [ ] **[P1-004]** 데이터베이스 환경 준비
  - [ ] 개발 DB (실 데이터 복사)
  - [ ] 테스트 DB
  - [ ] 성능 테스트 DB
  - **예상시간**: 1일

##### 1.2 도구 설치
- [ ] **[P1-005]** Java JDK 11 설치 (모든 서버)
  ```bash
  # OpenJDK 11 설치
  sudo apt-get install openjdk-11-jdk

  # 확인
  java -version
  ```
  - **예상시간**: 2시간

- [ ] **[P1-006]** Apache Tomcat 9.x 설치
  ```bash
  wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz
  tar -xzf apache-tomcat-9.0.80.tar.gz
  sudo mv apache-tomcat-9.0.80 /opt/tomcat9
  ```
  - **예상시간**: 2시간

- [ ] **[P1-007]** Maven 3.8+ 설치
  ```bash
  wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz
  tar -xzf apache-maven-3.8.8-bin.tar.gz
  sudo mv apache-maven-3.8.8 /opt/maven

  # PATH 설정
  export PATH=/opt/maven/bin:$PATH
  ```
  - **예상시간**: 1시간

- [ ] **[P1-008]** IDE 설치 및 설정
  - [ ] IntelliJ IDEA Ultimate (또는 Eclipse)
  - [ ] Spring Tool Suite 플러그인
  - [ ] Lombok 플러그인
  - [ ] Git 플러그인
  - **예상시간**: 4시간

##### 1.3 CI/CD 파이프라인 구축
- [ ] **[P1-009]** Jenkins 설치 및 설정
  ```bash
  docker run -d -p 8080:8080 -p 50000:50000 \
    -v jenkins_home:/var/jenkins_home \
    --name jenkins jenkins/jenkins:lts
  ```
  - **예상시간**: 4시간

- [ ] **[P1-010]** Jenkins Job 생성
  - [ ] 빌드 Job
  - [ ] 테스트 Job
  - [ ] 배포 Job
  - **예상시간**: 1일

- [ ] **[P1-011]** SonarQube 설치 및 설정
  ```bash
  docker run -d --name sonarqube \
    -p 9000:9000 \
    sonarqube:lts
  ```
  - **예상시간**: 4시간

- [ ] **[P1-012]** 정적 분석 도구 설정
  - [ ] PMD
  - [ ] FindBugs / SpotBugs
  - [ ] Checkstyle
  - **예상시간**: 4시간

##### 1.4 테스트 도구 설정
- [ ] **[P1-013]** JUnit 5 + Mockito 설정
  ```xml
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.10.0</version>
      <scope>test</scope>
  </dependency>
  ```
  - **예상시간**: 2시간

- [ ] **[P1-014]** Playwright 설치 및 설정
  ```bash
  npm init playwright@latest
  ```
  - **예상시간**: 4시간

- [ ] **[P1-015]** 샘플 E2E 테스트 작성
  - **예상시간**: 1일

#### Week 3-4: 코드 분석

##### 2.1 프로젝트 메트릭 수집
- [ ] **[P1-016]** Struts Action 클래스 개수 파악
  ```bash
  find ./src -name "*Action.java" | wc -l
  find ./src -name "*Action.java" > struts-actions-list.txt
  ```
  - **예상결과**: ~200개
  - **예상시간**: 1시간

- [ ] **[P1-017]** JSP 파일 개수 및 Struts 태그 사용 분석
  ```bash
  find ./web -name "*.jsp" | wc -l
  grep -r "<s:" ./web --include="*.jsp" | wc -l
  grep -r "<s:" ./web --include="*.jsp" > struts-tags-usage.txt
  ```
  - **예상결과**: ~301개 JSP
  - **예상시간**: 2시간

- [ ] **[P1-018]** iBATIS SQL 매핑 개수 파악
  ```bash
  find ./web/WEB-INF/classes -name "*-sqlmap.xml" | wc -l
  find ./web/WEB-INF/classes -name "*-sqlmap.xml" -exec grep -c "<select\|<insert\|<update\|<delete" {} + | awk '{s+=$1} END {print s}'
  ```
  - **예상결과**: ~500개 SQL
  - **예상시간**: 1시간

- [ ] **[P1-019]** Spring Bean 정의 개수 파악
  ```bash
  grep -r "<bean" ./web/WEB-INF/classes/web-config/*.xml | wc -l
  ```
  - **예상시간**: 30분

- [ ] **[P1-020]** DWR 사용 현황 분석
  ```bash
  grep -r "dwr/interface" ./web --include="*.jsp" | wc -l
  find ./web -name "*.jsp" -exec grep -l "dwr/interface" {} + > dwr-jsps-list.txt
  ```
  - **예상결과**: ~20개 JSP
  - **예상시간**: 1시간

##### 2.2 복잡도 분석
- [ ] **[P1-021]** SonarQube 분석 실행
  ```bash
  mvn sonar:sonar \
    -Dsonar.projectKey=tims7-webmail \
    -Dsonar.host.url=http://localhost:9000
  ```
  - **예상시간**: 2시간

- [ ] **[P1-022]** Cyclomatic Complexity 상위 20개 클래스 식별
  - **예상시간**: 2시간

- [ ] **[P1-023]** 코드 중복도 분석 및 리팩토링 대상 선정
  - **예상시간**: 4시간

- [ ] **[P1-024]** 의존성 그래프 생성
  - **도구**: jdeps, IntelliJ Dependency Analyzer
  - **예상시간**: 4시간

##### 2.3 호환성 조사
- [ ] **[P1-025]** Spring 2.5 → 5.x 호환성 검증
  - **문서**: Breaking Changes 목록 작성
  - **예상시간**: 1일

- [ ] **[P1-026]** iBATIS 2.3 → MyBatis 3.x API 차이 분석
  - **문서**: API 변환 매핑 테이블 작성
  - **예상시간**: 1일

- [ ] **[P1-027]** Struts 2 → Spring MVC 전환 패턴 연구
  - **문서**: 전환 패턴 문서 작성
  - **예상시간**: 2일

- [ ] **[P1-028]** 보안 라이브러리 호환성 확인
  - [ ] INISAFEMail 최신 버전 확인
  - [ ] Xecure7 호환성 확인
  - [ ] INICrypto 업데이트 여부
  - **예상시간**: 1일

- [ ] **[P1-029]** SSO 모듈 호환성 확인
  - **담당**: 인증팀 협업
  - **예상시간**: 2일

- [ ] **[P1-030]** 외부 통합 모듈 호환성 확인
  - [ ] 결제 모듈 (있는 경우)
  - [ ] 외부 API 연동
  - **예상시간**: 1일

#### Week 5-8: 테스트 커버리지 확보

##### 3.1 통합 테스트 작성 (목표: 50%)
- [ ] **[P1-031]** 로그인/로그아웃 테스트
  ```java
  @Test
  public void testLogin() {
      UserVO user = userAuthManager.login("test@example.com", "password");
      assertNotNull(user);
  }
  ```
  - **예상시간**: 1일

- [ ] **[P1-032]** 메일 목록 조회 테스트
  - **예상시간**: 1일

- [ ] **[P1-033]** 메일 읽기 테스트
  - **예상시간**: 1일

- [ ] **[P1-034]** 메일 작성/발송 테스트
  - **예상시간**: 2일

- [ ] **[P1-035]** 메일 삭제/이동 테스트
  - **예상시간**: 1일

- [ ] **[P1-036]** 첨부파일 업로드/다운로드 테스트
  - **예상시간**: 2일

- [ ] **[P1-037]** 주소록 관리 테스트
  - **예상시간**: 1일

- [ ] **[P1-038]** 일정 관리 테스트
  - **예상시간**: 2일

- [ ] **[P1-039]** 조직도 조회 테스트
  - **예상시간**: 1일

- [ ] **[P1-040]** 사용자 설정 테스트
  - **예상시간**: 1일

##### 3.2 E2E 테스트 작성
- [ ] **[P1-041]** Playwright 로그인 시나리오
  ```javascript
  test('사용자 로그인', async ({ page }) => {
      await page.goto('/login');
      await page.fill('#userId', 'test@example.com');
      await page.fill('#password', 'password');
      await page.click('#loginButton');
      await expect(page).toHaveURL('/mail/list');
  });
  ```
  - **예상시간**: 1일

- [ ] **[P1-042]** 메일 발송 E2E 시나리오
  - **예상시간**: 2일

- [ ] **[P1-043]** 메일 읽기 및 답장 시나리오
  - **예상시간**: 2일

- [ ] **[P1-044]** 첨부파일 업로드 시나리오
  - **예상시간**: 1일

- [ ] **[P1-045]** 주소록 검색 시나리오
  - **예상시간**: 1일

##### 3.3 성능 벤치마크 기준선 측정
- [ ] **[P1-046]** JMeter 테스트 플랜 작성
  - **예상시간**: 2일

- [ ] **[P1-047]** 로그인 응답 시간 측정
  - **목표**: Baseline < 500ms
  - **예상시간**: 4시간

- [ ] **[P1-048]** 메일 목록 응답 시간 측정
  - **목표**: Baseline < 1000ms
  - **예상시간**: 4시간

- [ ] **[P1-049]** 메일 읽기 응답 시간 측정
  - **목표**: Baseline < 800ms
  - **예상시간**: 4시간

- [ ] **[P1-050]** 동시 사용자 부하 테스트 (100/500/1000명)
  - **예상시간**: 1일

### Phase 1 완료 기준
- ✅ 개발 환경 100% 구축
- ✅ CI/CD 파이프라인 가동
- ✅ 코드 분석 완료 및 문서화
- ✅ 테스트 커버리지 50% 이상
- ✅ 성능 기준선 데이터 확보
- ✅ Phase 2 착수 승인

**완료 작업**: 0/50 (0%)

---

## Phase 2: Spring Framework 5.x 업그레이드 (2-3개월)

**예상 기간**: 2-3개월
**담당자**: Backend Team
**우선순위**: 🟡 높음

### 주차별 계획

#### Week 1-2: 빌드 시스템 전환

##### 1.1 Maven 프로젝트 구조 생성
- [ ] **[P2-001]** pom.xml 생성 (부모 POM)
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                               http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>com.terracetech.tims</groupId>
      <artifactId>tims7-webmail</artifactId>
      <version>7.4.7-SNAPSHOT</version>
      <packaging>war</packaging>

      <properties>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <java.version>11</java.version>
          <spring.version>5.3.30</spring.version>
      </properties>
  </project>
  ```
  - **예상시간**: 4시간

- [ ] **[P2-002]** Spring Framework 5.3.30 의존성 추가
  ```xml
  <dependencies>
      <!-- Spring Core -->
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
  </dependencies>
  ```
  - **예상시간**: 2시간

- [ ] **[P2-003]** 기존 라이브러리 의존성 추가
  - [ ] Struts 2.3.37 (임시 유지)
  - [ ] iBATIS 2.3.4 (임시 유지)
  - [ ] JavaMail, Servlet API, JSTL
  - **예상시간**: 4시간

- [ ] **[P2-004]** Maven 빌드 플러그인 설정
  ```xml
  <build>
      <plugins>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <version>3.11.0</version>
              <configuration>
                  <source>11</source>
                  <target>11</target>
              </configuration>
          </plugin>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-war-plugin</artifactId>
              <version>3.4.0</version>
          </plugin>
      </plugins>
  </build>
  ```
  - **예상시간**: 2시간

- [ ] **[P2-005]** Maven 빌드 테스트
  ```bash
  mvn clean compile
  mvn package
  ```
  - **예상시간**: 2시간

#### Week 3-4: Spring 설정 업데이트

##### 2.1 Spring XML 네임스페이스 업데이트 (11개 파일)
- [ ] **[P2-006]** `spring-common.xml` 업데이트
  - **변경**: 네임스페이스 2.5 → 5.x
  - **예상시간**: 1시간

- [ ] **[P2-007]** `spring-mail.xml` 업데이트
  - **예상시간**: 2시간

- [ ] **[P2-008]** `spring-addr.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-009]** `spring-calendar.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-010]** `spring-setting.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-011]** `spring-bbs.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-012]** `spring-webfolder.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-013]** `spring-organization.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-014]** `spring-mobile.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-015]** `spring-jmobile.xml` 업데이트
  - **예상시간**: 1시간

- [ ] **[P2-016]** `spring-note.xml` 업데이트
  - **예상시간**: 1시간

##### 2.2 트랜잭션 관리 업데이트
- [ ] **[P2-017]** `<tx:annotation-driven>` 설정 추가
  ```xml
  <tx:annotation-driven transaction-manager="transactionManager"/>
  ```
  - **예상시간**: 1시간

- [ ] **[P2-018]** Manager 클래스에 `@Transactional` 어노테이션 추가 (우선순위 높은 10개)
  - [ ] MailManager
  - [ ] LetterManager
  - [ ] MailUserManager
  - [ ] AddressManager
  - [ ] SchedulerManager
  - **예상시간**: 1일

#### Week 5-6: Component Scan 및 어노테이션 전환 (선택사항)

- [ ] **[P2-019]** `<context:component-scan>` 설정 추가
  ```xml
  <context:component-scan base-package="com.terracetech.tims.webmail" />
  ```
  - **예상시간**: 1시간

- [ ] **[P2-020]** `@Service` 어노테이션 추가 (Manager 클래스 20개)
  - **예상시간**: 2일

- [ ] **[P2-021]** `@Repository` 어노테이션 추가 (DAO 클래스 30개)
  - **예상시간**: 2일

- [ ] **[P2-022]** Constructor 주입으로 변경 (우선순위 높은 20개 클래스)
  ```java
  @Service
  public class MailManager {
      private final MailDao mailDao;

      @Autowired
      public MailManager(MailDao mailDao) {
          this.mailDao = mailDao;
      }
  }
  ```
  - **예상시간**: 3일

#### Week 7-8: Deprecated API 교체 및 컴파일

- [ ] **[P2-023]** `SimpleDateFormat` → `DateTimeFormatter` 교체
  - **스크립트**: 일괄 교체 스크립트 작성
  - **예상시간**: 2일

- [ ] **[P2-024]** 제네릭 타입 경고 수정
  - **예상시간**: 2일

- [ ] **[P2-025]** Raw 타입 사용 제거
  - **예상시간**: 1일

- [ ] **[P2-026]** 모든 컴파일 경고 해결
  - **예상시간**: 2일

- [ ] **[P2-027]** Spring 5.x 컴파일 성공
  ```bash
  mvn clean compile
  ```
  - **예상시간**: 반복 작업

#### Week 9-10: 테스트 및 검증

- [ ] **[P2-028]** 단위 테스트 실행 및 수정
  ```bash
  mvn test
  ```
  - **예상시간**: 3일

- [ ] **[P2-029]** 통합 테스트 실행 및 수정
  - **예상시간**: 3일

- [ ] **[P2-030]** E2E 테스트 실행 (Struts 2 환경)
  - **예상시간**: 2일

- [ ] **[P2-031]** 성능 테스트 (기준선과 비교)
  - **목표**: ±5% 이내
  - **예상시간**: 1일

- [ ] **[P2-032]** 메모리 누수 테스트
  - **도구**: VisualVM, JProfiler
  - **예상시간**: 1일

#### Week 11-12: 코드 리뷰 및 마무리

- [ ] **[P2-033]** 코드 리뷰 실시
  - **참여자**: 전체 개발팀
  - **예상시간**: 1주

- [ ] **[P2-034]** 피드백 반영 및 수정
  - **예상시간**: 1주

- [ ] **[P2-035]** Phase 2 완료 승인 요청
  - **문서**: Phase 2 완료 보고서 작성
  - **예상시간**: 1일

### Phase 2 완료 기준
- ✅ Maven 빌드 성공
- ✅ Spring 5.x 컴파일 성공
- ✅ 모든 테스트 통과
- ✅ 기능 100% 정상 동작
- ✅ 성능 저하 없음 (±5% 이내)
- ✅ 코드 리뷰 완료

**완료 작업**: 0/35 (0%)

---

## Phase 3: iBATIS → MyBatis 마이그레이션 (2-3개월)

**예상 기간**: 2-3개월
**담당자**: Backend Team + DBA
**우선순위**: 🟡 높음

### 주차별 계획

#### Week 1-2: MyBatis 설정

- [ ] **[P3-001]** MyBatis 3.5.13 의존성 추가
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
  - **예상시간**: 1시간

- [ ] **[P3-002]** `mybatis-config.xml` 생성
  ```xml
  <configuration>
      <settings>
          <setting name="mapUnderscoreToCamelCase" value="true"/>
          <setting name="cacheEnabled" value="true"/>
      </settings>
  </configuration>
  ```
  - **예상시간**: 2시간

- [ ] **[P3-003]** Spring 설정에 `SqlSessionFactory` Bean 추가
  ```xml
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>
      <property name="configLocation" value="classpath:mybatis-config.xml"/>
      <property name="mapperLocations" value="classpath*:com/terracetech/tims/webmail/**/mapper/*.xml"/>
  </bean>
  ```
  - **예상시간**: 2시간

- [ ] **[P3-004]** `MapperScannerConfigurer` 설정
  ```xml
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
      <property name="basePackage" value="com.terracetech.tims.webmail.**.mapper"/>
      <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
  </bean>
  ```
  - **예상시간**: 1시간

- [ ] **[P3-005]** iBATIS → MyBatis SQL 매핑 변환 스크립트 작성
  ```python
  # convert_ibatis_to_mybatis.py
  # 자동 변환 스크립트
  ```
  - **예상시간**: 2일

#### Week 3-4: 공통 모듈 전환

- [ ] **[P3-006]** `common` DAO → Mapper 전환
  - [ ] SQL 매핑 XML 변환
  - [ ] Mapper 인터페이스 생성
  - [ ] 단위 테스트 작성
  - **예상시간**: 3일

- [ ] **[P3-007]** `common` 모듈 Manager 클래스 업데이트
  - DAO 의존성 → Mapper 의존성
  - **예상시간**: 1일

- [ ] **[P3-008]** `common` 모듈 테스트 실행
  - **예상시간**: 1일

#### Week 5-6: 독립 모듈 전환

- [ ] **[P3-009]** `organization` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-010]** `setting` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-011]** `webfolder` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-012]** 독립 모듈 통합 테스트
  - **예상시간**: 2일

#### Week 7-8: 보조 모듈 전환

- [ ] **[P3-013]** `scheduler` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-014]** `bbs` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-015]** `addr` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-016]** 보조 모듈 통합 테스트
  - **예상시간**: 2일

#### Week 9-12: 핵심 모듈 전환 (신중하게)

- [ ] **[P3-017]** `mailuser` DAO → Mapper 전환
  - [ ] UserAuthDao → UserAuthMapper
  - [ ] MailUserDao → MailUserMapper
  - **예상시간**: 1주

- [ ] **[P3-018]** `mail` DAO → Mapper 전환 (가장 중요)
  - [ ] LetterDao → LetterMapper
  - [ ] BigAttachDao → BigAttachMapper
  - [ ] CacheEmailDao → CacheEmailMapper
  - **예상시간**: 2주

- [ ] **[P3-019]** `home` DAO → Mapper 전환
  - **예상시간**: 3일

- [ ] **[P3-020]** 핵심 모듈 통합 테스트
  - **예상시간**: 1주

#### Week 13: iBATIS 제거 및 최종 검증

- [ ] **[P3-021]** iBATIS 라이브러리 제거 (pom.xml)
  - **예상시간**: 1시간

- [ ] **[P3-022]** iBATIS 관련 import 제거
  - **예상시간**: 2시간

- [ ] **[P3-023]** 전체 컴파일 및 패키징
  ```bash
  mvn clean package
  ```
  - **예상시간**: 1시간

- [ ] **[P3-024]** 모든 단위 테스트 실행
  - **예상시간**: 1일

- [ ] **[P3-025]** 모든 통합 테스트 실행
  - **예상시간**: 1일

- [ ] **[P3-026]** 전체 E2E 테스트 실행
  - **예상시간**: 2일

- [ ] **[P3-027]** SQL 쿼리 성능 비교
  - **목표**: 성능 저하 없음 또는 개선
  - **예상시간**: 1일

- [ ] **[P3-028]** 코드 리뷰 및 승인
  - **예상시간**: 1주

### Phase 3 완료 기준
- ✅ 모든 DAO → Mapper 전환 완료
- ✅ iBATIS 라이브러리 제거
- ✅ 모든 테스트 통과
- ✅ 기능 100% 정상 동작
- ✅ 성능 개선 또는 유지
- ✅ Phase 3.5 착수 승인

**완료 작업**: 0/28 (0%)

---

## Phase 3.5: DWR → REST API 마이그레이션 (1-2개월)

**예상 기간**: 1-2개월
**담당자**: Frontend Team + Backend Team
**우선순위**: 🟢 중간

### 주차별 계획

#### Week 1-2: REST API 인프라 구축

- [ ] **[P3.5-001]** Jackson 2.15.2 의존성 추가
  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.15.2</version>
  </dependency>
  ```
  - **예상시간**: 1시간

- [ ] **[P3.5-002]** Spring MVC JSON 변환기 설정
  - **예상시간**: 2시간

- [ ] **[P3.5-003]** `ApiResponse<T>` 클래스 작성
  ```java
  public class ApiResponse<T> {
      private boolean success;
      private T data;
      private String message;
      // ...
  }
  ```
  - **예상시간**: 2시간

- [ ] **[P3.5-004]** REST API 예외 처리기 작성 (`@ControllerAdvice`)
  - **예상시간**: 4시간

- [ ] **[P3.5-005]** JavaScript 공통 API 유틸리티 작성 (`api-utils.js`)
  - **예상시간**: 1일

#### Week 3-4: 메일 모듈 REST API 구현

- [ ] **[P3.5-006]** `MailApiController` 생성
  - **예상시간**: 1일

- [ ] **[P3.5-007]** 메일 목록 조회 API (`GET /api/mail/list`)
  - **예상시간**: 4시간

- [ ] **[P3.5-008]** 메일 상세 조회 API (`GET /api/mail/{mailId}`)
  - **예상시간**: 4시간

- [ ] **[P3.5-009]** 메일 전송 API (`POST /api/mail/send`)
  - **예상시간**: 1일

- [ ] **[P3.5-010]** 메일 읽음 처리 API (`PATCH /api/mail/{mailId}/read`)
  - **예상시간**: 2시간

- [ ] **[P3.5-011]** 메일 삭제 API (`DELETE /api/mail`)
  - **예상시간**: 4시간

- [ ] **[P3.5-012]** 메일 이동 API (`PATCH /api/mail/move`)
  - **예상시간**: 4시간

- [ ] **[P3.5-013]** 첨부파일 업로드 API (`POST /api/mail/upload`)
  - **예상시간**: 1일

#### Week 5-6: JavaScript 클라이언트 구현

- [ ] **[P3.5-014]** `MailAPI` 래퍼 작성 (Fetch API 기반)
  - **예상시간**: 1일

- [ ] **[P3.5-015]** 메일 목록 로딩 JavaScript 전환
  - **예상시간**: 4시간

- [ ] **[P3.5-016]** 메일 전송 JavaScript 전환
  - **예상시간**: 4시간

- [ ] **[P3.5-017]** 메일 읽음 처리 JavaScript 전환
  - **예상시간**: 2시간

- [ ] **[P3.5-018]** 파일 업로드 JavaScript 전환 (FormData)
  - **예상시간**: 4시간

- [ ] **[P3.5-019]** jQuery AJAX 래퍼 작성 (레거시 호환)
  - **예상시간**: 4시간

#### Week 7-8: 기타 모듈 REST API 구현

- [ ] **[P3.5-020]** 주소록 API 구현
  - [ ] 검색 API
  - [ ] 자동완성 API
  - **예상시간**: 3일

- [ ] **[P3.5-021]** 일정 API 구현
  - [ ] 일정 조회 API
  - [ ] 일정 등록 API
  - **예상시간**: 3일

- [ ] **[P3.5-022]** 조직도 API 구현
  - [ ] 트리 조회 API
  - **예상시간**: 2일

- [ ] **[P3.5-023]** 설정 API 구현
  - [ ] 폴더 관리 API
  - **예상시간**: 2일

- [ ] **[P3.5-024]** 각 모듈 JavaScript 클라이언트 구현
  - **예상시간**: 1주

#### Week 9: WebSocket 실시간 알림 (선택)

- [ ] **[P3.5-025]** Spring WebSocket 설정
  ```java
  @Configuration
  @EnableWebSocket
  public class WebSocketConfig implements WebSocketConfigurer {
      // ...
  }
  ```
  - **예상시간**: 1일

- [ ] **[P3.5-026]** 메일 알림 WebSocket 핸들러 구현
  - **예상시간**: 2일

- [ ] **[P3.5-027]** JavaScript WebSocket 클라이언트 구현
  - **예상시간**: 1일

- [ ] **[P3.5-028]** WebSocket 통합 테스트
  - **예상시간**: 1일

#### Week 10: DWR 제거

- [ ] **[P3.5-029]** 모든 JSP에서 DWR 스크립트 제거
  ```bash
  find ./web -name "*.jsp" -exec sed -i '/<script.*dwr\/interface/d' {} +
  find ./web -name "*.jsp" -exec sed -i '/<script.*dwr\/engine/d' {} +
  ```
  - **예상시간**: 1일

- [ ] **[P3.5-030]** web.xml에서 DWR Servlet 제거
  - **예상시간**: 30분

- [ ] **[P3.5-031]** Spring 설정에서 DWR 설정 제거
  - **예상시간**: 1시간

- [ ] **[P3.5-032]** dwr.jar 의존성 제거 (pom.xml)
  - **예상시간**: 30분

- [ ] **[P3.5-033]** DWR 관련 import 제거
  - **예상시간**: 1시간

- [ ] **[P3.5-034]** 전체 컴파일 및 테스트
  - **예상시간**: 1일

#### Week 11-12: 테스트 및 검증

- [ ] **[P3.5-035]** REST API 단위 테스트 (MockMvc)
  - **예상시간**: 3일

- [ ] **[P3.5-036]** JavaScript 테스트 (Jest)
  - **예상시간**: 2일

- [ ] **[P3.5-037]** E2E 테스트 업데이트 (Playwright)
  - **예상시간**: 3일

- [ ] **[P3.5-038]** 성능 테스트 (DWR 대비)
  - **목표**: 동등 이상
  - **예상시간**: 1일

- [ ] **[P3.5-039]** 보안 테스트 (CSRF, CORS)
  - **예상시간**: 1일

- [ ] **[P3.5-040]** 코드 리뷰 및 승인
  - **예상시간**: 1주

### Phase 3.5 완료 기준
- ✅ 모든 DWR → REST API 전환 완료
- ✅ DWR 완전 제거
- ✅ JavaScript 클라이언트 작성 완료
- ✅ 모든 테스트 통과
- ✅ 성능 목표 달성
- ✅ Phase 4 착수 승인

**완료 작업**: 0/40 (0%)

---

## Phase 4: Struts 2 → Spring MVC 전환 (3-4개월)

**예상 기간**: 3-4개월
**담당자**: 전체 개발팀
**우선순위**: 🔴 긴급

### 주차별 계획

#### Week 1-2: Spring MVC 설정

- [ ] **[P4-001]** `spring-mvc-config.xml` 작성
  - **예상시간**: 1일

- [ ] **[P4-002]** web.xml에 `DispatcherServlet` 설정
  ```xml
  <servlet>
      <servlet-name>dispatcher</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
      <servlet-name>dispatcher</servlet-name>
      <url-pattern>/</url-pattern>
  </servlet-mapping>
  ```
  - **예상시간**: 2시간

- [ ] **[P4-003]** ViewResolver 설정 (JSP)
  - **예상시간**: 1시간

- [ ] **[P4-004]** Resource Handler 설정 (정적 리소스)
  - **예상시간**: 2시간

- [ ] **[P4-005]** Multipart Resolver 설정 (파일 업로드)
  - **예상시간**: 2시간

- [ ] **[P4-006]** MessageSource 설정 (다국어)
  - **예상시간**: 2시간

- [ ] **[P4-007]** LocaleResolver 설정
  - **예상시간**: 1시간

- [ ] **[P4-008]** Interceptor 설정 (AuthInterceptor)
  ```java
  @Component
  public class AuthInterceptor implements HandlerInterceptor {
      // ...
  }
  ```
  - **예상시간**: 4시간

#### Week 3-4: 테스트/샘플 모듈 전환 (1단계)

- [ ] **[P4-009]** `test` 패키지 Controller 작성
  - **예상시간**: 2일

- [ ] **[P4-010]** `test` 패키지 JSP 뷰 전환
  - **예상시간**: 1일

- [ ] **[P4-011]** `test` 모듈 테스트 실행
  - **예상시간**: 1일

#### Week 5-8: 독립 모듈 전환 (2단계)

- [ ] **[P4-012]** `organization` Controller 작성
  - [ ] ViewOrganizationTreeController
  - [ ] ViewOrganizationMemberController
  - [ ] OrganizationCommonController
  - **예상시간**: 1주

- [ ] **[P4-013]** `organization` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-014]** `organization` 테스트 실행
  - **예상시간**: 2일

- [ ] **[P4-015]** `setting` Controller 작성
  - **예상시간**: 1주

- [ ] **[P4-016]** `setting` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-017]** `setting` 테스트 실행
  - **예상시간**: 2일

- [ ] **[P4-018]** `webfolder` Controller 작성
  - **예상시간**: 1주

- [ ] **[P4-019]** `webfolder` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-020]** `webfolder` 테스트 실행
  - **예상시간**: 2일

#### Week 9-12: 보조 모듈 전환 (3단계)

- [ ] **[P4-021]** `scheduler` Controller 작성
  - **예상시간**: 1.5주

- [ ] **[P4-022]** `scheduler` JSP 전환
  - **예상시간**: 1.5주

- [ ] **[P4-023]** `scheduler` 테스트 실행
  - **예상시간**: 2일

- [ ] **[P4-024]** `bbs` Controller 작성
  - **예상시간**: 1주

- [ ] **[P4-025]** `bbs` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-026]** `bbs` 테스트 실행
  - **예상시간**: 2일

- [ ] **[P4-027]** `addr` Controller 작성
  - **예상시간**: 1주

- [ ] **[P4-028]** `addr` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-029]** `addr` 테스트 실행
  - **예상시간**: 2일

#### Week 13-18: 핵심 모듈 전환 (4단계 - 신중하게)

##### `mailuser` 모듈 (인증)
- [ ] **[P4-030]** `mailuser` Controller 작성
  - [ ] LoginController
  - [ ] LogoutController
  - [ ] UserAuthController
  - **예상시간**: 2주

- [ ] **[P4-031]** `mailuser` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-032]** `mailuser` 테스트 실행
  - **예상시간**: 3일

##### `mail` 모듈 (핵심)
- [ ] **[P4-033]** `mail` Controller 작성
  - [ ] MailListController
  - [ ] MailReadController
  - [ ] MailWriteController
  - [ ] MailSendController
  - [ ] MailWorkController
  - **예상시간**: 3주

- [ ] **[P4-034]** `mail` JSP 전환 (대량)
  - **예상시간**: 2주

- [ ] **[P4-035]** `mail` 테스트 실행
  - **예상시간**: 1주

##### `home` 모듈
- [ ] **[P4-036]** `home` Controller 작성
  - **예상시간**: 1주

- [ ] **[P4-037]** `home` JSP 전환
  - **예상시간**: 1주

- [ ] **[P4-038]** `home` 테스트 실행
  - **예상시간**: 2일

#### Week 19-20: JSP 태그 자동 변환

- [ ] **[P4-039]** JSP 태그 변환 스크립트 작성
  ```python
  # convert_struts_to_spring_tags.py
  # <s:form> → <form:form>
  # <s:textfield> → <form:input>
  # <s:if> → <c:if>
  # ...
  ```
  - **예상시간**: 3일

- [ ] **[P4-040]** 변환 스크립트 실행 및 검증
  - **예상시간**: 2일

- [ ] **[P4-041]** 수동 수정 필요한 JSP 식별 및 수정
  - **예상시간**: 1주

#### Week 21: Validation 전환

- [ ] **[P4-042]** Struts Validation → Spring Validation 전환
  - **예상시간**: 3일

- [ ] **[P4-043]** Validation 어노테이션 추가
  - `@NotEmpty`, `@Email`, `@Size` 등
  - **예상시간**: 2일

- [ ] **[P4-044]** Custom Validator 작성 (필요시)
  - **예상시간**: 1일

- [ ] **[P4-045]** 에러 메시지 properties 업데이트
  - **예상시간**: 1일

#### Week 22: 예외 처리 및 URL 호환성

- [ ] **[P4-046]** 전역 예외 처리기 작성 (`@ControllerAdvice`)
  ```java
  @ControllerAdvice
  public class GlobalExceptionHandler {
      // ...
  }
  ```
  - **예상시간**: 1일

- [ ] **[P4-047]** 에러 페이지 작성 (404, 500)
  - **예상시간**: 1일

- [ ] **[P4-048]** 레거시 URL 리다이렉트 Controller 작성
  ```java
  @GetMapping("/mail/mailList.action")
  public String redirectMailList() {
      return "redirect:/mail/list";
  }
  ```
  - **예상시간**: 2일

- [ ] **[P4-049]** 외부 링크 영향 분석 및 문서화
  - **예상시간**: 1일

#### Week 23: Struts 제거

- [ ] **[P4-050]** web.xml에서 Struts 필터 제거
  - **예상시간**: 30분

- [ ] **[P4-051]** struts.xml 파일 제거
  - **예상시간**: 30분

- [ ] **[P4-052]** struts-*.xml 파일 제거
  - **예상시간**: 30분

- [ ] **[P4-053]** Struts 라이브러리 제거 (pom.xml)
  - **예상시간**: 30min

- [ ] **[P4-054]** Struts 관련 import 제거
  - **예상시간**: 2시간

- [ ] **[P4-055]** 전체 컴파일 및 패키징
  ```bash
  mvn clean package
  ```
  - **예상시간**: 1시간

#### Week 24-26: 테스트 및 검증

- [ ] **[P4-056]** 모든 Controller 단위 테스트 (MockMvc)
  - **예상시간**: 2주

- [ ] **[P4-057]** 통합 테스트 실행 및 수정
  - **예상시간**: 1주

- [ ] **[P4-058]** E2E 테스트 전체 실행
  - **예상시간**: 1주

- [ ] **[P4-059]** 회귀 테스트 100% 실행
  - **예상시간**: 1주

- [ ] **[P4-060]** 성능 테스트 (목표 달성 확인)
  - **목표**: 20-25% 개선
  - **예상시간**: 3일

- [ ] **[P4-061]** 보안 검증 (OWASP Top 10)
  - **예상시간**: 3일

- [ ] **[P4-062]** 코드 리뷰 및 승인
  - **예상시간**: 1주

### Phase 4 완료 기준
- ✅ Struts 완전 제거
- ✅ 모든 URL 정상 동작
- ✅ 모든 테스트 통과
- ✅ 기능 100% 정상 동작
- ✅ 성능 목표 달성 (20-25% 개선)
- ✅ 보안 검증 완료
- ✅ Phase 5 착수 승인

**완료 작업**: 0/62 (0%)

---

## Phase 5: 테스트 및 검증 (1-2개월)

**예상 기간**: 1-2개월
**담당자**: QA Team + 전체 개발팀
**우선순위**: 🔴 긴급

### 주차별 계획

#### Week 1-2: 회귀 테스트

- [ ] **[P5-001]** 로그인/로그아웃 테스트
  - **예상시간**: 1일

- [ ] **[P5-002]** 메일 목록 조회 테스트
  - **예상시간**: 1일

- [ ] **[P5-003]** 메일 읽기 테스트
  - **예상시간**: 1일

- [ ] **[P5-004]** 메일 작성/발송 테스트
  - **예상시간**: 2일

- [ ] **[P5-005]** 메일 삭제/이동 테스트
  - **예상시간**: 1일

- [ ] **[P5-006]** 메일 검색 테스트
  - **예상시간**: 1일

- [ ] **[P5-007]** 첨부파일 업로드/다운로드 테스트
  - **예상시간**: 2일

- [ ] **[P5-008]** 대용량 첨부 테스트
  - **예상시간**: 1일

- [ ] **[P5-009]** 주소록 관리 테스트
  - **예상시간**: 1일

- [ ] **[P5-010]** 일정 관리 테스트
  - **예상시간**: 2일

- [ ] **[P5-011]** 조직도 조회 테스트
  - **예상시간**: 1일

- [ ] **[P5-012]** 사용자 설정 테스트
  - **예상시간**: 1일

- [ ] **[P5-013]** 다국어 전환 테스트 (한/일/영)
  - **예상시간**: 1일

- [ ] **[P5-014]** 모바일 화면 테스트
  - **예상시간**: 2일

#### Week 3: 성능 테스트

- [ ] **[P5-015]** JMeter 테스트 플랜 업데이트
  - **예상시간**: 1일

- [ ] **[P5-016]** 로그인 응답 시간 측정
  - **목표**: < 400ms (20% 개선)
  - **예상시간**: 4시간

- [ ] **[P5-017]** 메일 목록 응답 시간 측정
  - **목표**: < 800ms (20% 개선)
  - **예상시간**: 4시간

- [ ] **[P5-018]** 메일 읽기 응답 시간 측정
  - **목표**: < 600ms (25% 개선)
  - **예상시간**: 4시간

- [ ] **[P5-019]** 동시 사용자 부하 테스트
  - 100명, 500명, 1000명
  - **예상시간**: 1일

- [ ] **[P5-020]** 메모리 사용량 측정 및 비교
  - **예상시간**: 4시간

- [ ] **[P5-021]** CPU 사용률 측정 및 비교
  - **예상시간**: 4시간

- [ ] **[P5-022]** 데이터베이스 쿼리 성능 측정
  - **예상시간**: 1일

- [ ] **[P5-023]** 성능 목표 달성 확인 및 보고서 작성
  - **예상시간**: 1일

#### Week 4-5: 보안 테스트

- [ ] **[P5-024]** SQL Injection 테스트
  - **예상시간**: 1일

- [ ] **[P5-025]** XSS (Cross-Site Scripting) 테스트
  - **예상시간**: 1일

- [ ] **[P5-026]** CSRF (Cross-Site Request Forgery) 테스트
  - **예상시간**: 1일

- [ ] **[P5-027]** 인증 우회 시도 테스트
  - **예상시간**: 1일

- [ ] **[P5-028]** 권한 상승 시도 테스트
  - **예상시간**: 1일

- [ ] **[P5-029]** 민감 정보 노출 테스트
  - **예상시간**: 1일

- [ ] **[P5-030]** OWASP ZAP 스캔 실행
  - **예상시간**: 1일

- [ ] **[P5-031]** Burp Suite 스캔 실행
  - **예상시간**: 1일

- [ ] **[P5-032]** 침투 테스트 (외부 업체)
  - **예상시간**: 1주

- [ ] **[P5-033]** 보안 취약점 보고서 작성
  - **예상시간**: 2일

- [ ] **[P5-034]** 보안 이슈 수정
  - **예상시간**: 1주

- [ ] **[P5-035]** 보안 재테스트
  - **예상시간**: 3일

#### Week 6-7: UAT (사용자 승인 테스트)

- [ ] **[P5-036]** UAT 환경 준비
  - **예상시간**: 2일

- [ ] **[P5-037]** UAT 테스트 시나리오 작성
  - **예상시간**: 2일

- [ ] **[P5-038]** 사용자 교육 (파워 유저)
  - **예상시간**: 1일

- [ ] **[P5-039]** UAT 실행 (2주)
  - **예상시간**: 2주

- [ ] **[P5-040]** 피드백 수집 및 이슈 트래킹
  - **예상시간**: 지속

- [ ] **[P5-041]** 이슈 수정
  - **예상시간**: 1주

- [ ] **[P5-042]** UAT 재테스트
  - **예상시간**: 3일

- [ ] **[P5-043]** 최종 승인 획득
  - **예상시간**: 1일

#### Week 8: 문서화

- [ ] **[P5-044]** API 문서 작성 (Swagger/OpenAPI)
  - **예상시간**: 3일

- [ ] **[P5-045]** 사용자 매뉴얼 업데이트
  - **예상시간**: 2일

- [ ] **[P5-046]** 관리자 가이드 업데이트
  - **예상시간**: 2일

- [ ] **[P5-047]** 배포 가이드 작성
  - **예상시간**: 1일

- [ ] **[P5-048]** 운영 매뉴얼 작성
  - **예상시간**: 2일

- [ ] **[P5-049]** 트러블슈팅 가이드 작성
  - **예상시간**: 1일

- [ ] **[P5-050]** Phase 5 완료 보고서 작성
  - **예상시간**: 1일

### Phase 5 완료 기준
- ✅ 회귀 테스트 100% 통과
- ✅ 성능 목표 100% 달성
- ✅ 보안 검증 완료 (취약점 0건)
- ✅ UAT 최종 승인
- ✅ 모든 문서 완성
- ✅ Phase 6 착수 승인

**완료 작업**: 0/50 (0%)

---

## Phase 6: 최적화 및 배포 (1개월)

**예상 기간**: 1개월
**담당자**: DevOps Team + 전체 개발팀
**우선순위**: 🟡 높음

### 주차별 계획

#### Week 1: 최적화 작업

##### SQL 쿼리 최적화
- [ ] **[P6-001]** Slow Query 식별 및 로깅
  ```sql
  -- MySQL Slow Query Log 활성화
  SET GLOBAL slow_query_log = 'ON';
  SET GLOBAL long_query_time = 2;
  ```
  - **예상시간**: 1일

- [ ] **[P6-002]** 인덱스 추가/수정
  - **예상시간**: 2일

- [ ] **[P6-003]** 쿼리 리팩토링
  - **예상시간**: 2일

##### Spring Cache 적용
- [ ] **[P6-004]** 캐시 전략 수립
  - **예상시간**: 1일

- [ ] **[P6-005]** `@Cacheable` 어노테이션 적용 (주요 조회 메서드)
  ```java
  @Cacheable(value = "mailList", key = "#userId")
  public List<MailVO> getMailList(String userId) {
      // ...
  }
  ```
  - **예상시간**: 2일

- [ ] **[P6-006]** 캐시 무효화 전략 구현 (`@CacheEvict`)
  - **예상시간**: 1일

##### 정적 리소스 최적화
- [ ] **[P6-007]** JavaScript 압축 및 병합
  - **도구**: UglifyJS, Webpack
  - **예상시간**: 1일

- [ ] **[P6-008]** CSS 압축 및 병합
  - **도구**: CSSNano
  - **예상시간**: 1일

- [ ] **[P6-009]** 이미지 최적화
  - **도구**: ImageOptim, TinyPNG
  - **예상시간**: 1일

- [ ] **[P6-010]** CDN 적용 검토 및 설정
  - **예상시간**: 2일

##### DB 커넥션 풀 튜닝
- [ ] **[P6-011]** 최적 풀 크기 결정
  - **공식**: connections = ((core_count * 2) + effective_spindle_count)
  - **예상시간**: 1일

- [ ] **[P6-012]** 타임아웃 설정 조정
  - **예상시간**: 4시간

- [ ] **[P6-013]** 커넥션 풀 모니터링 설정
  - **예상시간**: 4시간

##### JVM 튜닝
- [ ] **[P6-014]** 힙 메모리 크기 조정
  ```bash
  -Xms2g -Xmx4g
  ```
  - **예상시간**: 1일

- [ ] **[P6-015]** GC 알고리즘 선택 (G1GC 권장)
  ```bash
  -XX:+UseG1GC -XX:MaxGCPauseMillis=200
  ```
  - **예상시간**: 1일

- [ ] **[P6-016]** GC 로깅 설정
  ```bash
  -Xlog:gc*:file=gc.log:time,uptime:filecount=10,filesize=10M
  ```
  - **예상시간**: 4시간

#### Week 2: 모니터링 설정

- [ ] **[P6-017]** APM 도구 설치 (New Relic / DataDog / Pinpoint)
  - **예상시간**: 1일

- [ ] **[P6-018]** ELK Stack 설치 (로그 수집)
  - Elasticsearch + Logstash + Kibana
  - **예상시간**: 2일

- [ ] **[P6-019]** Prometheus + Grafana 설치 (메트릭 수집)
  - **예상시간**: 2일

- [ ] **[P6-020]** 알림 규칙 설정
  - 응답 시간 > 1초
  - 에러율 > 1%
  - CPU > 80%
  - 메모리 > 85%
  - **예상시간**: 1일

- [ ] **[P6-021]** 대시보드 구성
  - **예상시간**: 1일

#### Week 3: 배포 준비

- [ ] **[P6-022]** 배포 체크리스트 작성
  - **예상시간**: 4시간

- [ ] **[P6-023]** 롤백 계획 수립 및 테스트
  - **예상시간**: 1일

- [ ] **[P6-024]** Blue-Green 배포 환경 구성
  - **예상시간**: 2일

- [ ] **[P6-025]** 배포 자동화 스크립트 작성
  ```bash
  #!/bin/bash
  # deploy.sh
  # Blue-Green 배포 스크립트
  ```
  - **예상시간**: 2일

- [ ] **[P6-026]** 배포 시나리오 검증 (Dry-run)
  - **예상시간**: 1일

- [ ] **[P6-027]** 긴급 연락망 구성
  - **예상시간**: 2시간

#### Week 4: 프로덕션 배포

##### 배포 Day -1
- [ ] **[P6-028]** 최종 백업 (소스, DB, 설정)
  - **예상시간**: 2시간

- [ ] **[P6-029]** 배포 팀 최종 브리핑
  - **예상시간**: 1시간

##### 배포 Day 0 (오전 2:00 시작)
- [ ] **[P6-030]** Green 환경에 신규 버전 배포
  - **예상시간**: 1시간

- [ ] **[P6-031]** 스모크 테스트 실행
  - **예상시간**: 30분

- [ ] **[P6-032]** 트래픽 10% 전환
  - **예상시간**: 30분

- [ ] **[P6-033]** 모니터링 및 검증 (1시간)
  - **예상시간**: 1시간

- [ ] **[P6-034]** 트래픽 50% 전환
  - **예상시간**: 30분

- [ ] **[P6-035]** 모니터링 및 검증 (2시간)
  - **예상시간**: 2시간

- [ ] **[P6-036]** 트래픽 100% 전환
  - **예상시간**: 30분

- [ ] **[P6-037]** Blue 환경 대기 (롤백 준비)
  - **예상시간**: 지속

##### 배포 Day 0 (오전 9:00 ~ 오후 6:00)
- [ ] **[P6-038]** 24시간 집중 모니터링
  - **담당**: DevOps + 개발팀 전원 대기
  - **예상시간**: 1일

- [ ] **[P6-039]** 이슈 발생 시 즉시 대응
  - **예상시간**: 필요시

##### 배포 Day +1
- [ ] **[P6-040]** 헬스 체크 정상 확인
  - **예상시간**: 1시간

- [ ] **[P6-041]** 주요 기능 동작 확인
  - **예상시간**: 2시간

- [ ] **[P6-042]** 성능 지표 모니터링
  - **예상시간**: 지속

- [ ] **[P6-043]** 에러 로그 확인
  - **예상시간**: 2시간

- [ ] **[P6-044]** 사용자 피드백 수집
  - **예상시간**: 지속

##### 배포 Day +7
- [ ] **[P6-045]** 1주일 안정성 확인
  - **예상시간**: 1일

- [ ] **[P6-046]** Blue 환경 정리 또는 유지 결정
  - **예상시간**: 2시간

- [ ] **[P6-047]** 배포 완료 보고서 작성
  - **예상시간**: 4시간

- [ ] **[P6-048]** 프로젝트 종료 보고서 작성
  - **예상시간**: 1일

- [ ] **[P6-049]** 경험 공유 세션 개최
  - **예상시간**: 2시간

- [ ] **[P6-050]** 프로젝트 아카이빙
  - **예상시간**: 4시간

### Phase 6 완료 기준
- ✅ 프로덕션 배포 성공
- ✅ 24시간 안정적 운영
- ✅ 성능 목표 달성 확인
- ✅ 사용자 만족도 확인
- ✅ 모니터링 정상 동작
- ✅ 프로젝트 종료 승인

**완료 작업**: 0/50 (0%)

---

## 📊 전체 작업 요약

### Phase별 작업 수
```
Phase 0: 13개 작업
Phase 1: 50개 작업
Phase 2: 35개 작업
Phase 3: 28개 작업
Phase 3.5: 40개 작업
Phase 4: 62개 작업
Phase 5: 50개 작업
Phase 6: 50개 작업

총 작업 수: 328개
```

### 예상 총 기간
```
Phase 0: 1주
Phase 1: 2개월
Phase 2: 2-3개월
Phase 3: 2-3개월
Phase 3.5: 1-2개월
Phase 4: 3-4개월
Phase 5: 1-2개월
Phase 6: 1개월

총 기간: 12-17개월 (1년 ~ 1년 5개월)
```

### 필요 리소스
```
개발자: 3명 (풀타임)
QA: 2명 (풀타임)
DBA: 1명 (파트타임)
DevOps: 1명 (파트타임)

총 인력: 7명
총 인월: 약 84-119 인월
```

---

## 🎯 다음 단계

1. **Phase 0 착수**: 백업 및 승인 절차 시작
2. **주간 진행 회의**: 매주 월요일 오전 10시
3. **이슈 트래킹**: Jira / GitHub Issues 사용
4. **문서 업데이트**: 주간 단위로 진행률 업데이트

---

## 📝 사용 방법

1. **작업 시작 시**: 해당 작업 ID를 체크
2. **작업 완료 시**: `- [x]`로 변경
3. **이슈 발생 시**: 작업 ID와 함께 이슈 트래커에 등록
4. **주간 리뷰**: 완료된 작업 수 집계 및 진행률 계산

---

## 참고 문서
- `01-current-state-analysis.md` - 현황 분석
- `02-migration-strategy.md` - 마이그레이션 전략
- `03-detailed-migration-guide.md` - 상세 가이드
- `04-migration-checklist.md` - 체크리스트
- `05-dwr-to-rest-api-migration.md` - DWR 마이그레이션
