# 마이그레이션 체크리스트

## 문서 정보
- **작성일**: 2025-10-14
- **용도**: 각 Phase별 작업 항목 체크 및 진행 상황 추적

---

## Phase 0: 사전 준비

### 백업 및 버전 관리
- [ ] 전체 소스코드 tar.gz 백업 완료
- [ ] 데이터베이스 덤프 백업 완료
- [ ] 설정 파일 별도 백업 완료
- [ ] Git 저장소 초기화 (기존 저장소 없는 경우)
- [ ] Baseline 태그 생성 (`v7.4.6S-struts2-baseline`)
- [ ] 브랜치 전략 수립 및 문서화

### 환경 준비
- [ ] 개발 서버 확보 (기존 + 신규)
- [ ] 테스트 환경 구축 (통합/성능/UAT)
- [ ] 데이터베이스 환경 준비 (개발/테스트/성능)
- [ ] Java JDK 11+ 설치
- [ ] Apache Tomcat 9.x 설치
- [ ] Maven 3.6+ 또는 Gradle 7.x+ 설치
- [ ] IDE 설치 및 플러그인 설정

### 승인 및 계획
- [ ] 프로젝트 관리자 승인
- [ ] 이해관계자 동의
- [ ] 일정 및 예산 승인
- [ ] 리소스 (인력) 할당
- [ ] 위험 관리 계획 수립
- [ ] 커뮤니케이션 계획 수립

---

## Phase 1: 환경 구축 및 분석 (2개월)

### 개발 환경 구축
- [ ] CI/CD 파이프라인 구축 (Jenkins/GitLab CI)
- [ ] 코드 품질 도구 설정 (SonarQube)
- [ ] 정적 분석 도구 설정 (PMD, FindBugs)
- [ ] 테스트 자동화 프레임워크 설정 (JUnit 5, Mockito)
- [ ] E2E 테스트 도구 설정 (Playwright)

### 코드 분석
- [ ] Struts Action 클래스 개수 파악
- [ ] JSP 파일 개수 및 Struts 태그 사용 현황 파악
- [ ] iBATIS SQL 매핑 개수 파악
- [ ] Spring Bean 정의 개수 파악
- [ ] 외부 라이브러리 의존성 목록 작성
- [ ] 복잡도 메트릭 수집 (Cyclomatic Complexity)
- [ ] 코드 중복도 분석

### 호환성 조사
- [ ] Spring 2.5 → 5.x 호환성 검증
- [ ] iBATIS 2.3 → MyBatis 3.x API 차이 분석
- [ ] Struts 2 → Spring MVC 전환 패턴 연구
- [ ] 보안 라이브러리 (INISAFEMail, Xecure7) 호환성 확인
- [ ] SSO 모듈 호환성 확인
- [ ] 타사 통합 모듈 호환성 확인

### 테스트 커버리지
- [ ] 주요 기능별 통합 테스트 작성 (목표: 50%+)
  - [ ] 로그인/로그아웃
  - [ ] 메일 목록 조회
  - [ ] 메일 읽기
  - [ ] 메일 작성/발송
  - [ ] 메일 삭제/이동
  - [ ] 첨부파일 업로드/다운로드
  - [ ] 주소록 관리
  - [ ] 일정 관리
  - [ ] 조직도 조회
  - [ ] 사용자 설정

- [ ] E2E 테스트 스크립트 작성 (주요 시나리오)
- [ ] 성능 벤치마크 기준선 측정
  - [ ] 로그인 응답 시간
  - [ ] 메일 목록 응답 시간
  - [ ] 메일 읽기 응답 시간

### Phase 1 완료 기준
- [ ] 개발 환경 100% 구축
- [ ] 테스트 커버리지 50% 이상 달성
- [ ] 호환성 조사 완료 및 문서화
- [ ] 마이그레이션 계획 최종 승인

---

## Phase 2: Spring Framework 5.x 업그레이드 (2-3개월)

### 빌드 시스템 전환
- [ ] pom.xml 생성 (Maven) 또는 build.gradle (Gradle)
- [ ] 의존성 정의 (Spring 5.3.x, Struts 2.3.37)
- [ ] 빌드 스크립트 작성
- [ ] WAR 패키징 설정
- [ ] Maven/Gradle 빌드 성공 확인

### Spring 설정 업데이트
- [ ] 모든 Spring XML 네임스페이스 업데이트 (2.5 → 5.x)
- [ ] `spring-common.xml` 업데이트
- [ ] `spring-mail.xml` 업데이트
- [ ] `spring-addr.xml` 업데이트
- [ ] `spring-calendar.xml` 업데이트
- [ ] `spring-setting.xml` 업데이트
- [ ] `spring-bbs.xml` 업데이트
- [ ] `spring-webfolder.xml` 업데이트
- [ ] `spring-organization.xml` 업데이트
- [ ] `spring-mobile.xml`, `spring-jmobile.xml` 업데이트
- [ ] `spring-note.xml` 업데이트

### Component Scan 추가 (선택사항)
- [ ] `<context:component-scan>` 설정 추가
- [ ] `@Service` 어노테이션 추가 (Manager 클래스)
- [ ] `@Repository` 어노테이션 추가 (DAO 클래스)
- [ ] XML Bean 정의 제거 (어노테이션으로 대체)

### 트랜잭션 관리 업데이트
- [ ] `<tx:annotation-driven>` 설정 추가
- [ ] `@Transactional` 어노테이션 적용 (Service 메서드)
- [ ] 트랜잭션 동작 테스트

### Deprecated API 교체
- [ ] `SimpleDateFormat` → `DateTimeFormatter` 교체
- [ ] 제네릭 타입 경고 수정
- [ ] Raw 타입 사용 제거
- [ ] 컴파일 경고 모두 해결

### 테스트 및 검증
- [ ] Spring 5.x 컴파일 성공
- [ ] 단위 테스트 실행 및 통과
- [ ] 통합 테스트 실행 및 통과
- [ ] E2E 테스트 실행 및 통과 (Struts 2 환경)
- [ ] 성능 테스트 (기준선과 비교)
- [ ] 메모리 누수 테스트

### Phase 2 완료 기준
- [ ] 모든 컴파일 에러 해결
- [ ] 모든 테스트 통과
- [ ] 기존 기능 100% 정상 동작
- [ ] 성능 저하 없음 (±5% 이내)
- [ ] 코드 리뷰 완료 및 승인

---

## Phase 3: iBATIS → MyBatis 마이그레이션 (2-3개월)

### MyBatis 설정
- [ ] `mybatis-config.xml` 생성
- [ ] `SqlSessionFactory` Bean 설정
- [ ] `MapperScannerConfigurer` 설정
- [ ] TypeAlias 설정
- [ ] Settings 설정 (iBATIS 호환 모드)

### SQL 매핑 파일 변환 (모듈별)
#### 공통 모듈
- [ ] `common` DAO → Mapper 전환
- [ ] SQL 매핑 XML 변환
- [ ] Mapper 인터페이스 생성
- [ ] 단위 테스트 작성 및 통과

#### 독립 모듈
- [ ] `organization` DAO → Mapper 전환
- [ ] `setting` DAO → Mapper 전환
- [ ] `webfolder` DAO → Mapper 전환

#### 보조 모듈
- [ ] `scheduler` DAO → Mapper 전환
- [ ] `bbs` DAO → Mapper 전환
- [ ] `addr` DAO → Mapper 전환

#### 핵심 모듈
- [ ] `mailuser` DAO → Mapper 전환
- [ ] `mail` DAO → Mapper 전환
- [ ] `home` DAO → Mapper 전환

### Manager 클래스 업데이트
- [ ] DAO 의존성 → Mapper 의존성으로 변경
- [ ] 모든 Manager 클래스 업데이트
- [ ] Constructor 주입으로 변경 (권장)

### 테스트 및 검증
- [ ] 모든 Mapper 단위 테스트 작성 및 통과
- [ ] SQL 쿼리 정상 실행 확인
- [ ] 트랜잭션 동작 확인
- [ ] 통합 테스트 실행 및 통과
- [ ] E2E 테스트 실행 및 통과
- [ ] 성능 테스트 (SQL 실행 시간 비교)

### Phase 3 완료 기준
- [ ] 모든 DAO → Mapper 전환 완료
- [ ] iBATIS 라이브러리 제거
- [ ] 모든 테스트 통과
- [ ] 기능 정상 동작 (100%)
- [ ] 성능 저하 없음 또는 개선
- [ ] 코드 리뷰 완료 및 승인

---

## Phase 4: Struts 2 → Spring MVC 전환 (3-4개월)

### Spring MVC 설정
- [ ] `spring-mvc-config.xml` 작성
- [ ] DispatcherServlet 설정 (web.xml)
- [ ] ViewResolver 설정 (JSP)
- [ ] Resource Handler 설정 (정적 리소스)
- [ ] Multipart Resolver 설정 (파일 업로드)
- [ ] MessageSource 설정 (다국어)
- [ ] LocaleResolver 설정
- [ ] Interceptor 설정

### Controller 작성 (모듈별 순차 전환)
#### 1단계: 테스트/샘플 모듈
- [ ] `test` 패키지 Controller 작성
- [ ] JSP 뷰 전환 (Struts → Spring 태그)
- [ ] URL 매핑 업데이트
- [ ] 테스트 실행

#### 2단계: 독립 모듈
- [ ] `organization` Controller 작성
  - [ ] ViewOrganizationTreeController
  - [ ] ViewOrganizationMemberController
  - [ ] OrganizationCommonController
- [ ] `organization` JSP 전환
- [ ] 테스트 실행

- [ ] `setting` Controller 작성
- [ ] `setting` JSP 전환
- [ ] 테스트 실행

- [ ] `webfolder` Controller 작성
- [ ] `webfolder` JSP 전환
- [ ] 테스트 실행

#### 3단계: 보조 모듈
- [ ] `scheduler` Controller 작성
- [ ] `scheduler` JSP 전환
- [ ] 테스트 실행

- [ ] `bbs` Controller 작성
- [ ] `bbs` JSP 전환
- [ ] 테스트 실행

- [ ] `addr` Controller 작성
- [ ] `addr` JSP 전환
- [ ] 테스트 실행

#### 4단계: 핵심 모듈 (신중하게)
- [ ] `mailuser` Controller 작성 (인증)
  - [ ] LoginController
  - [ ] LogoutController
  - [ ] UserAuthController
- [ ] `mailuser` JSP 전환
- [ ] 테스트 실행

- [ ] `mail` Controller 작성
  - [ ] MailListController
  - [ ] MailReadController
  - [ ] MailWriteController
  - [ ] MailSendController
  - [ ] MailWorkController
- [ ] `mail` JSP 전환
- [ ] 테스트 실행

- [ ] `home` Controller 작성
- [ ] `home` JSP 전환
- [ ] 테스트 실행

### JSP 뷰 전환
- [ ] Struts 태그 → Spring/JSTL 태그 변환 도구 개발 (스크립트)
- [ ] 각 JSP 파일 변환 및 검증
- [ ] `<s:form>` → `<form:form>` 변환
- [ ] `<s:textfield>` → `<form:input>` 변환
- [ ] `<s:if>` → `<c:if>` 변환
- [ ] `<s:iterator>` → `<c:forEach>` 변환
- [ ] `<s:property>` → `<c:out>` 또는 EL 변환

### Validation 전환
- [ ] Struts Validation → Spring Validation 변환
- [ ] Validation 어노테이션 추가 (`@NotEmpty`, `@Email` 등)
- [ ] Custom Validator 작성 (필요시)
- [ ] 에러 메시지 properties 파일 업데이트

### Interceptor 전환
- [ ] Struts Interceptor → Spring Interceptor 변환
- [ ] AuthInterceptor 작성
- [ ] LoggingInterceptor 작성 (필요시)
- [ ] Interceptor 등록 및 URL 패턴 설정

### 예외 처리
- [ ] `@ControllerAdvice` 작성
- [ ] 전역 예외 처리기 구현
- [ ] 에러 페이지 작성 (404, 500)
- [ ] 사용자 정의 예외 처리

### URL 호환성
- [ ] 레거시 URL 리다이렉트 Controller 작성
- [ ] `/mail/mailList.action` → `/mail/list` 리다이렉트
- [ ] 모든 레거시 URL 매핑 확인
- [ ] 외부 링크 영향 분석

### Struts 제거
- [ ] web.xml에서 Struts 필터 제거
- [ ] struts.xml 파일 제거
- [ ] struts-*.xml 파일 제거
- [ ] Struts 라이브러리 제거 (pom.xml/build.gradle)
- [ ] Struts 관련 import 제거

### 테스트 및 검증
- [ ] 모든 Controller 단위 테스트 작성 및 통과
- [ ] MockMvc 테스트 작성
- [ ] 통합 테스트 실행 및 통과
- [ ] E2E 테스트 전체 실행 및 통과
- [ ] 회귀 테스트 100% 통과
- [ ] 성능 테스트 (목표 대비 확인)

### Phase 4 완료 기준
- [ ] Struts 완전 제거
- [ ] 모든 URL 정상 동작
- [ ] 모든 테스트 통과
- [ ] 기능 100% 정상 동작
- [ ] 성능 목표 달성 (20-25% 개선)
- [ ] 보안 검증 완료
- [ ] 코드 리뷰 완료 및 승인

---

## Phase 5: 테스트 및 검증 (1-2개월)

### 회귀 테스트
- [ ] 로그인/로그아웃 테스트
- [ ] 메일 목록 조회 테스트
- [ ] 메일 읽기 테스트
- [ ] 메일 작성/발송 테스트
- [ ] 메일 삭제/이동 테스트
- [ ] 메일 검색 테스트
- [ ] 첨부파일 업로드 테스트
- [ ] 첨부파일 다운로드 테스트
- [ ] 대용량 첨부 테스트
- [ ] 주소록 관리 테스트
- [ ] 일정 관리 테스트
- [ ] 조직도 조회 테스트
- [ ] 사용자 설정 테스트
- [ ] 다국어 전환 테스트 (한국어, 일본어, 영어)
- [ ] 모바일 화면 테스트

### 성능 테스트
- [ ] 로그인 응답 시간 측정 (목표: < 400ms)
- [ ] 메일 목록 응답 시간 측정 (목표: < 800ms)
- [ ] 메일 읽기 응답 시간 측정 (목표: < 600ms)
- [ ] 동시 사용자 부하 테스트 (100명, 500명, 1000명)
- [ ] 메모리 사용량 측정
- [ ] CPU 사용률 측정
- [ ] 데이터베이스 쿼리 성능 측정
- [ ] 성능 목표 달성 확인

### 보안 테스트
- [ ] OWASP Top 10 검증
  - [ ] SQL Injection 테스트
  - [ ] XSS (Cross-Site Scripting) 테스트
  - [ ] CSRF (Cross-Site Request Forgery) 테스트
  - [ ] 인증 우회 시도 테스트
  - [ ] 권한 상승 시도 테스트
  - [ ] 민감 정보 노출 테스트
- [ ] 보안 스캔 도구 실행 (OWASP ZAP, Burp Suite)
- [ ] 침투 테스트 (Penetration Testing)
- [ ] 보안 취약점 보고서 작성
- [ ] 보안 이슈 수정 및 재테스트

### 사용자 승인 테스트 (UAT)
- [ ] UAT 환경 준비
- [ ] 테스트 시나리오 작성
- [ ] 사용자 교육
- [ ] UAT 실행
- [ ] 피드백 수집
- [ ] 이슈 수정
- [ ] 재테스트
- [ ] 최종 승인 획득

### 문서화
- [ ] API 문서 작성 (Swagger/OpenAPI)
- [ ] 사용자 매뉴얼 업데이트
- [ ] 관리자 가이드 업데이트
- [ ] 배포 가이드 작성
- [ ] 운영 매뉴얼 작성
- [ ] 트러블슈팅 가이드 작성

### Phase 5 완료 기준
- [ ] 회귀 테스트 100% 통과
- [ ] 성능 목표 100% 달성
- [ ] 보안 검증 완료 (취약점 0건)
- [ ] UAT 최종 승인
- [ ] 모든 문서 완성

---

## Phase 6: 최적화 및 배포 (1개월)

### 최적화 작업
- [ ] SQL 쿼리 최적화
  - [ ] Slow Query 식별
  - [ ] 인덱스 추가/수정
  - [ ] 쿼리 리팩토링
- [ ] Spring Cache 적용
  - [ ] 캐시 전략 수립
  - [ ] `@Cacheable` 어노테이션 적용
  - [ ] 캐시 무효화 전략 구현
- [ ] 정적 리소스 최적화
  - [ ] JavaScript 압축 및 병합
  - [ ] CSS 압축 및 병합
  - [ ] 이미지 최적화
  - [ ] CDN 적용 검토
- [ ] DB 커넥션 풀 튜닝
  - [ ] 최적 풀 크기 결정
  - [ ] 타임아웃 설정
  - [ ] 모니터링 설정
- [ ] JVM 튜닝
  - [ ] 힙 메모리 크기 조정
  - [ ] GC 알고리즘 선택
  - [ ] GC 로깅 설정

### 모니터링 설정
- [ ] APM 도구 설치 (New Relic, DataDog, Pinpoint)
- [ ] 로그 수집 시스템 설정 (ELK Stack)
- [ ] 메트릭 수집 설정 (Prometheus, Grafana)
- [ ] 알림 규칙 설정
- [ ] 대시보드 구성

### 배포 준비
- [ ] 배포 체크리스트 작성
- [ ] 롤백 계획 수립 및 테스트
- [ ] Blue-Green 배포 환경 구성
- [ ] 배포 자동화 스크립트 작성
- [ ] 배포 시나리오 검증
- [ ] 긴급 연락망 구성

### 프로덕션 배포
- [ ] 최종 백업 (소스, DB, 설정)
- [ ] Green 환경에 신규 버전 배포
- [ ] 스모크 테스트 실행
- [ ] 트래픽 10% 전환
- [ ] 모니터링 및 검증 (1시간)
- [ ] 트래픽 50% 전환
- [ ] 모니터링 및 검증 (2시간)
- [ ] 트래픽 100% 전환
- [ ] Blue 환경 대기 (롤백 준비)
- [ ] 24시간 모니터링
- [ ] 이슈 없음 확인
- [ ] Blue 환경 정리 또는 유지

### 배포 후 검증
- [ ] 헬스 체크 정상
- [ ] 주요 기능 동작 확인
- [ ] 성능 지표 모니터링
- [ ] 에러 로그 확인
- [ ] 사용자 피드백 수집
- [ ] 이슈 트래킹 및 해결

### Phase 6 완료 기준
- [ ] 프로덕션 배포 성공
- [ ] 24시간 안정적 운영
- [ ] 성능 목표 달성 확인
- [ ] 사용자 만족도 확인
- [ ] 모니터링 정상 동작
- [ ] 프로젝트 종료 보고서 작성

---

## 위험 관리 체크리스트

### 높은 위험 항목
- [ ] 대규모 JSP 수정 - 자동화 도구 개발 및 단계적 전환
- [ ] 성능 저하 - 성능 테스트 및 사전 튜닝
- [ ] 보안 이슈 - 보안 검증 및 침투 테스트
- [ ] 데이터 손실 - 철저한 백업 및 롤백 계획

### 중간 위험 항목
- [ ] 일정 지연 - 버퍼 기간 확보 및 리소스 추가 투입
- [ ] 리소스 부족 - 사전 리소스 확보 및 외부 지원
- [ ] 기술 부채 누적 - 코드 리뷰 및 리팩토링

### 낮은 위험 항목
- [ ] 문서 부족 - 단계별 문서화
- [ ] 커뮤니케이션 문제 - 정기 회의 및 보고

---

## 롤백 체크리스트

### 애플리케이션 롤백
- [ ] Tomcat 중지
- [ ] 현재 버전 제거
- [ ] 백업 버전 복원
- [ ] 설정 파일 복원
- [ ] Tomcat 시작
- [ ] 헬스 체크
- [ ] 기능 확인

### 데이터베이스 롤백
- [ ] 현재 DB 백업 (롤백 전)
- [ ] 백업 SQL 확인
- [ ] DB 복원 실행
- [ ] 데이터 무결성 확인
- [ ] 애플리케이션 연동 확인

### 롤백 완료 확인
- [ ] 시스템 정상 동작
- [ ] 사용자 접속 가능
- [ ] 주요 기능 동작 확인
- [ ] 롤백 사유 분석
- [ ] 재발 방지 대책 수립

---

## 최종 완료 체크리스트

- [ ] 모든 Phase 완료
- [ ] 모든 테스트 통과
- [ ] 프로덕션 배포 완료
- [ ] 안정적 운영 (1개월)
- [ ] 사용자 만족도 목표 달성
- [ ] 성능 목표 달성
- [ ] 보안 검증 완료
- [ ] 문서화 완료
- [ ] 프로젝트 종료 보고서 작성
- [ ] 경험 공유 세션 개최
- [ ] 프로젝트 아카이빙

---

## 참고사항

### 체크리스트 사용법
1. 각 Phase 시작 전 해당 섹션 검토
2. 작업 진행하면서 항목 체크
3. 주간/월간 진행 상황 리뷰
4. 완료 기준 충족 확인 후 다음 Phase 진행

### 이슈 트래킹
- 체크되지 않은 항목은 이유 문서화
- 블로킹 이슈는 즉시 에스컬레이션
- 지연 항목은 일정 재조정

### 승인 프로세스
- 각 Phase 완료 시 관리자 승인 필요
- 완료 기준 미충족 시 Phase 반복
- 중요 변경사항은 사전 승인 필요
