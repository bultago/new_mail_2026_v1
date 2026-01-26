# 코드 분석 보고서

**분석일**: 2025-10-16  
**프로젝트**: TIMS7 Webmail v7.4.6S  
**목적**: 마이그레이션 범위 및 규모 파악

---

## 분석 결과 요약

### 1. Struts Actions (258개)
**분석 ID**: P1-016  
**파일 위치**: `docs/analysis/struts-actions-list.txt`

#### 모듈별 분포
```
webmail/setting:      47개  (가장 많음)
webmail/mail:         33개  (핵심 모듈)
webmail/webfolder:    18개
webmail/common:       14개
webmail/bbs:          14개
hybrid/mail:          13개
webmail/register:     12개
webmail/addrbook:     12개
webmail/scheduler:    11개
webmail/note:         11개
webmail/mailuser:     10개  (인증)
mobile/bbs:            9개
mobile/calendar:       7개
mobile/mail:           6개
hybrid/addr:           6개
webmail/organization:  5개
mobile/common:         5개
jmobile/mail:          5개
webmail/test:          4개
webmail/home:          4개
jmobile/common:        4개
mobile/addrbook:       3개
hybrid/common:         3개
service/aync:          2개

총 258개
```

**마이그레이션 영향**:
- Phase 4에서 258개 Action을 Controller로 전환 필요
- 모듈별 순차 전환 전략 적용 필요

---

### 2. JSP 파일 (301개)
**분석 ID**: P1-017

#### Struts 태그 사용
- JSP 파일 총 개수: **301개**
- Struts 태그 사용 라인: **3개만** (매우 적음!)
- 주요 태그: `<s:property>` 3회

**마이그레이션 영향**:
- ✅ **매우 긍정적!** Struts 태그 사용이 거의 없음
- JSP 전환 작업이 예상보다 훨씬 간단할 것
- JSTL과 EL 중심으로 이미 작성되어 있을 가능성 높음

---

### 3. iBATIS SQL 매핑 (82개 파일, ~2,412개 쿼리)
**분석 ID**: P1-018  
**파일 위치**: `web/WEB-INF/classes/db-config/`

#### 데이터베이스별 지원
```
MySQL:        16개 파일
PostgreSQL:   16개 파일
Oracle:       16개 파일
Derby:        16개 파일
공통:         18개 파일

총 82개 파일
```

#### 모듈별 SQL 매핑
- addrbook: 113 statements
- setting: 75 statements
- bbs: 58 statements
- scheduler: 53 statements
- mail: 다수
- 기타 모듈

**추정 총 SQL statements**: ~2,412개

**마이그레이션 영향**:
- Phase 3에서 iBATIS → MyBatis 전환 필요
- 4개 DB 지원으로 작업량 증가
- SQL 매핑 XML 자동 변환 스크립트 필요

---

### 4. Spring Bean (357개)
**분석 ID**: P1-019

#### 모듈별 Bean 분포
```
spring-mail.xml:         62개 (가장 많음)
spring-setting.xml:      69개
spring-mobile.xml:       55개
spring-login.xml:        41개
spring-webfolder.xml:    21개
spring-addr.xml:         20개
spring-bbs.xml:          19개
spring-calendar.xml:     18개
spring-common.xml:       18개
spring-note.xml:         15개
spring-jmobile.xml:      10개
spring-organization.xml:  9개

총 357개
```

**마이그레이션 영향**:
- Phase 2에서 Spring 2.5 → 5.x 업그레이드
- XML Bean 정의는 유지 가능 (선택적으로 어노테이션으로 전환)

---

### 5. DWR 사용 현황 (20개 JSP)
**분석 ID**: P1-020

#### DWR 사용 JSP 위치
```
classic/mail/:           7개 (주요 메일 기능)
dynamic/:               11개 (동적 컨텐츠)
common/:                 2개 (헤더)

총 20개
```

**DWR 서비스**:
- AddressBookService (주소록)
- MailService (메일)
- SchedulerService (일정)
- OrganizationService (조직도)

**마이그레이션 영향**:
- Phase 3.5에서 DWR → REST API 전환
- 20개 JSP에서 DWR 스크립트 제거 필요
- JavaScript 클라이언트 재작성 필요

---

## 종합 분석

### 마이그레이션 규모
```
코드:
- Struts Actions:     258개 → Spring Controllers
- JSP 파일:           301개 (Struts 태그 사용 거의 없음!)
- SQL 매핑:            82개 → MyBatis Mapper
- SQL 쿼리:        ~2,412개
- Spring Bean:        357개 (XML 설정)
- DWR 사용 JSP:        20개 → REST API

총 작업 규모:         328개 TODO 항목
```

### 난이도 평가

| 항목 | 난이도 | 이유 |
|------|--------|------|
| JSP 전환 | 🟢 낮음 | Struts 태그 사용 거의 없음 |
| Action 전환 | 🟡 중간 | 258개이지만 패턴 일정 |
| SQL 매핑 전환 | 🔴 높음 | 82개 파일, 2,412개 쿼리, 4개 DB 지원 |
| DWR 전환 | 🟡 중간 | 20개 JSP, REST API 패턴 적용 |

### 긍정적 요소
1. ✅ Struts 태그 사용이 거의 없어 JSP 전환이 쉬움
2. ✅ Spring Framework는 이미 사용 중
3. ✅ 모듈화가 잘 되어 있음
4. ✅ 명확한 레이어 분리 (Action-Manager-DAO)

### 주의 요소
1. ⚠️ SQL 매핑 파일이 많고 4개 DB 지원
2. ⚠️ Action 클래스 258개 전환 필요
3. ⚠️ DWR 의존성 20개 JSP

---

## 다음 작업

### 즉시 진행 가능
- [P1-021] SonarQube 분석 실행
- [P1-022] 복잡도 분석
- [P1-023] 코드 중복도 분석

### 준비 필요
- Phase 2: Maven pom.xml 작성 (P2-001)
- Phase 3: MyBatis 변환 스크립트 작성 (P3-005)
- Phase 4: Controller 전환 패턴 수립

---

**분석 완료일**: 2025-10-16 16:12  
**분석 시간**: 약 10분  
**상태**: ✅ 완료

