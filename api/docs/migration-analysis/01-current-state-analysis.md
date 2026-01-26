# 프로젝트 현황 분석 보고서

## 문서 정보
- **작성일**: 2025-10-14
- **프로젝트**: TIMS7 Webmail v7.4.6S
- **목적**: Struts 2에서 Spring MVC로 마이그레이션을 위한 현황 분석

---

## 1. 프로젝트 개요

### 1.1 기본 정보
- **프로젝트명**: TIMS7 Webmail
- **버전**: 7.4.6S
- **유형**: 엔터프라이즈 웹메일 시스템
- **아키텍처**: Struts 2 + Spring Framework 2.5 + iBATIS 2.3.4

### 1.2 규모 분석
- **Java 소스 파일**: 약 563개
- **JSP 파일**: 약 301개
- **패키지 구조**: `com.terracetech.tims.webmail`
- **빌드 시스템**: Apache Ant

---

## 2. 현재 기술 스택

### 2.1 프레임워크 버전
```
핵심 프레임워크:
├── Struts 2.3.32 (웹 MVC 프레임워크)
├── Spring Framework 2.5 (IoC, DI, 트랜잭션 관리)
├── iBATIS 2.3.4.726 (ORM/데이터 액세스)
└── DWR (Direct Web Remoting - AJAX)

서블릿 컨테이너:
└── Apache Tomcat 4+ 호환

JavaScript 라이브러리:
├── jQuery 1.3.2
└── Prototype.js
```

### 2.2 주요 라이브러리
- `struts2-core-2.3.32.jar`
- `struts2-spring-plugin-2.3.32.jar`
- `spring.jar` (2.5)
- `spring-webmvc.jar`
- `ibatis-2.3.4.726.jar`
- 보안: INISAFEMail, Xecure7, INICrypto
- 기타: Commons 라이브러리들, AspectJ, CGLIB

### 2.3 설정 파일 구조
```
web/WEB-INF/
├── web.xml (서블릿 설정, 필터 체인)
├── classes/
│   └── web-config/
│       ├── struts-*.xml (Struts 설정)
│       ├── spring-*.xml (Spring Bean 설정)
│       └── *-sqlmap.xml (iBATIS SQL 매핑)
└── lib/ (의존 라이브러리)
```

---

## 3. 아키텍처 분석

### 3.1 레이어 구조
```
┌─────────────────────────────────────┐
│   Web Layer (JSP + Struts Actions)  │
├─────────────────────────────────────┤
│   Business Layer (Manager)          │
├─────────────────────────────────────┤
│   Data Access Layer (DAO + iBATIS)  │
├─────────────────────────────────────┤
│   Database                          │
└─────────────────────────────────────┘
```

### 3.2 모듈 구조
```
com.terracetech.tims.webmail/
├── mail/                    # 메일 핵심 기능
│   ├── action/             # Struts2 액션 (약 50개)
│   ├── manager/            # 비즈니스 로직
│   │   └── send/          # 전송 핸들러 (Strategy 패턴)
│   ├── dao/               # 데이터 액세스
│   └── vo/                # Value Objects
│
├── mailuser/              # 사용자 관리
│   ├── manager/
│   └── sso/              # SSO 통합
│
├── scheduler/             # 일정/캘린더
├── organization/          # 조직도
├── webfolder/            # 웹 폴더
├── setting/              # 설정
├── home/                 # 홈/대시보드
└── common/               # 공통 유틸리티
```

### 3.3 디자인 패턴
1. **Strategy Pattern**: 메일 전송 핸들러
   - `SendMessageDirector` (조정자)
   - `NormalSendHandler`, `BatchSendHandler`, `ReservedSendHandler`, `EachSendHandler`

2. **Layered Architecture**: Action → Manager → DAO → DB

3. **Dependency Injection**: Spring `byName` autowiring

4. **Front Controller**: Struts2 필터 체인

---

## 4. Struts 2 사용 현황

### 4.1 Struts Action 클래스
- **위치**: 각 모듈의 `action` 패키지
- **명명 규칙**: `*Action.java`
- **추정 개수**: 100개 이상

**주요 액션 예시**:
```
mail/action/
├── MailListAction.java
├── MailReadAction.java
├── MailWriteAction.java
├── MailSendAction.java
└── MailWorkAction.java

organization/action/
├── ViewOrganizationTreeAction.java
├── ViewOrganizationMemberAction.java
└── OrganizationCommonAction.java
```

### 4.2 Struts 설정 파일
```
web/WEB-INF/classes/web-config/
├── struts-mobile.xml
├── struts-portlet.xml
├── struts-setting.xml
├── struts-bbs.xml
├── struts-addr.xml
├── struts-calendar.xml
├── struts-mail.xml
├── struts-webfolder.xml
└── struts-organization.xml
```

### 4.3 Struts 필터 설정
```xml
<filter>
    <filter-name>struts</filter-name>
    <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>struts</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

---

## 5. Spring Framework 사용 현황

### 5.1 Spring 버전 및 모듈
- **Core**: Spring 2.5
- **사용 모듈**: Core, Context, Web, WebMVC, ORM, AOP, TX

### 5.2 Spring 설정 파일
```
web/WEB-INF/classes/web-config/
├── spring-common.xml      # 공통 Bean
├── spring-login.xml       # 인증
├── spring-mail.xml        # 메일 Bean
├── spring-addr.xml        # 주소록
├── spring-calendar.xml    # 캘린더
├── spring-setting.xml     # 설정
├── spring-bbs.xml         # 게시판
├── spring-webfolder.xml   # 웹폴더
├── spring-organization.xml # 조직도
├── spring-mobile.xml      # 모바일
├── spring-jmobile.xml     # J모바일
└── spring-note.xml        # 노트
```

### 5.3 Spring Bean 정의 패턴
```xml
<bean id="mailManager"
      class="com.terracetech.tims.webmail.mail.manager.MailManager"
      p:mailDao-ref="mailDao" />

<bean id="mailDao"
      class="com.terracetech.tims.webmail.mail.dao.MailDao"
      p:sqlMapClient-ref="sqlMapClient" />
```

- **의존성 주입**: `default-autowire="byName"`
- **프로퍼티 설정**: `p:` namespace 사용

### 5.4 Spring-Struts 통합
- `struts2-spring-plugin-2.3.32.jar` 사용
- Struts Action들이 Spring Bean으로 관리됨
- Spring ApplicationContext에서 의존성 주입

---

## 6. iBATIS 사용 현황

### 6.1 데이터 액세스 패턴
```
DAO 클래스 → SqlMapClient → SQL Mapping XML → Database
```

### 6.2 주요 DAO 클래스
```
mail/dao/
├── BigAttachDao.java      # 대용량 첨부
├── CacheEmailDao.java     # 이메일 캐싱
├── LetterDao.java         # 메일 메시지
├── FolderAgingDao.java    # 폴더 에이징
└── SharedFolderDao.java   # 공유 폴더
```

### 6.3 iBATIS 설정
```xml
<bean id="sqlMapClient"
      class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="configLocation" value="..." />
</bean>
```

---

## 7. JSP 사용 현황

### 7.1 JSP 파일 분포
- **총 개수**: 약 301개
- **위치**: `web/` 디렉토리 하위
- **주요 디렉토리**:
  ```
  web/
  ├── classic/      # 클래식 UI 템플릿
  ├── mobile/       # 모바일 인터페이스
  ├── hybrid/       # 하이브리드 UI
  ├── design/       # CSS, 이미지
  ├── js/          # JavaScript
  └── *.jsp        # 개별 JSP 페이지
  ```

### 7.2 JSP 기술 스택
- **JSTL**: JSP Standard Tag Library
- **Struts Tags**: `<s:*>` 태그 라이브러리
- **Custom Tags**: 프로젝트 자체 태그
- **EL**: Expression Language

### 7.3 JSP 패턴
```jsp
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="mailSend" method="post">
    <s:textfield name="subject" />
    <s:submit value="전송" />
</s:form>
```

---

## 8. 다국어 지원

### 8.1 메시지 리소스
```
web/WEB-INF/classes/messageResources/
├── *_ko.properties    # 한국어
├── *_jp.properties    # 일본어
└── *.properties       # 영어 (기본)
```

### 8.2 메시지 카테고리
- `MailMessage` - 메일 UI 문자열
- `CommonMessage` - 공통 문자열
- `SchedulerMessage` - 일정 문자열
- `SettingMessage` - 설정 UI
- `AddrApplicationResources` - 주소록
- `BbsMessage` - 게시판
- `WebfolderMessage` - 웹폴더
- `JMailMobile` - 모바일

---

## 9. 보안 컴포넌트

### 9.1 보안 라이브러리
- `INISAFEMail_v1.4.0.jar` - 이메일 보안
- `Xecure7.jar` - 보안 프레임워크
- `INICrypto_v3.2.1_signed.jar` - 암호화
- `bcprov-jdk15-141.jar` - Bouncy Castle 암호화

### 9.2 보안 패키지
```
com.terracetech.secure/
├── policy/
│   └── AllowPolicy.java
└── rule/
    └── TextRule.java
```

### 9.3 웹 보안 필터
```xml
<filter>
    <filter-name>WebmailAccessFilter</filter-name>
    <filter-class>com.terracetech.tims.webmail.common.WebmailAccessFilter</filter-class>
</filter>
```

---

## 10. 필터 체인 분석

### 10.1 현재 필터 실행 순서
```
1. WebmailAccessFilter        # 접근 제어
2. StrutsPrepareAndExecuteFilter  # Struts2 처리
3. ResponseHeaderFilter        # 정적 리소스 캐시
4. ResponseHeaderDwrFilter     # DWR/JS 캐시
```

### 10.2 URL 패턴 매핑
```
/* → WebmailAccessFilter, Struts
/design/* → ResponseHeaderFilter
/js/* → ResponseHeaderDwrFilter
/dwr/* → ResponseHeaderDwrFilter
/editor/* → ResponseHeaderFilter
```

---

## 11. 빌드 시스템

### 11.1 Apache Ant 타겟
```
ant compile      # 컴파일
ant clean        # 클린
ant dist         # 배포 패키지 생성
ant js.minify    # JavaScript 최적화
```

### 11.2 빌드 프로세스
```
1. js.minify     # JS 병합 및 압축 (YUI Compressor)
2. prepare       # 빌드 디렉토리 생성
3. compile       # Java 컴파일
4. 리소스 복사   # Properties, XML 복사
```

---

## 12. 마이그레이션 고려사항

### 12.1 주요 이슈
1. **Struts 2 → Spring MVC 전환**
   - Action 클래스 → Controller 변환 (100개 이상)
   - Struts 태그 → Spring 태그 변환 (301개 JSP)
   - URL 매핑 재구성
   - 인터셉터 → Spring Interceptor 변환

2. **Spring 2.5 → Spring 5.x 업그레이드**
   - XML 설정 현대화 (Java Config 고려)
   - 의존성 주입 패턴 변경
   - 트랜잭션 관리 업데이트
   - Bean 라이프사이클 변경사항 대응

3. **iBATIS 2.3 → MyBatis 3.x 마이그레이션**
   - SQL 매핑 XML 변환
   - DAO 인터페이스 패턴 변경
   - 네임스페이스 변경 (com.ibatis → org.apache.ibatis)
   - API 변경사항 대응

4. **보안 업데이트**
   - 최신 보안 프레임워크 적용
   - Spring Security 통합 고려
   - 레거시 암호화 라이브러리 업데이트

5. **JavaScript 현대화**
   - jQuery 1.3.2 → 최신 버전
   - 모던 JavaScript 패턴 적용

### 12.2 위험 요소
- **규모**: 563개 Java 파일, 301개 JSP
- **복잡도**: 다중 모듈, 다국어 지원
- **테스트**: 기존 테스트 코드 부재 가능성
- **의존성**: 레거시 라이브러리 의존도 높음
- **호환성**: SSO, 보안 모듈 호환성

### 12.3 장점
- Spring Framework는 이미 사용 중 (Spring 2.5)
- 모듈화된 구조 (기능별 패키지 분리)
- 명확한 레이어 분리
- 풍부한 문서화 (properties 파일)

---

## 13. 권장 마이그레이션 접근법

### 13.1 단계적 마이그레이션 (추천)
```
Phase 1: 준비 단계
- 현재 시스템 완전 백업
- 테스트 환경 구축
- 의존성 버전 호환성 조사

Phase 2: Spring Framework 업그레이드
- Spring 2.5 → Spring 5.x
- 설정 파일 업데이트
- Bean 정의 현대화

Phase 3: iBATIS → MyBatis 전환
- SQL 매핑 파일 변환
- DAO 인터페이스 업데이트
- 통합 테스트

Phase 4: Struts 2 → Spring MVC 전환
- 모듈별 순차 전환
- Controller 클래스 작성
- URL 매핑 재구성
- JSP 태그 변환

Phase 5: 테스트 및 검증
- 기능 테스트
- 성능 테스트
- 보안 검증

Phase 6: 최적화 및 배포
- 성능 최적화
- 모니터링 설정
- 프로덕션 배포
```

### 13.2 Big Bang 방식 (비추천)
- 한 번에 모든 프레임워크 교체
- **위험도 높음**
- **테스트 부담 큼**
- **롤백 어려움**

---

## 14. 다음 단계

1. ✅ **현황 분석 완료** (본 문서)
2. ⏳ **마이그레이션 전략 수립**
   - Spring Framework 5.x 업그레이드 계획
   - Spring MVC 전환 전략
   - MyBatis 마이그레이션 계획
3. ⏳ **상세 마이그레이션 가이드 작성**
4. ⏳ **체크리스트 및 테스트 계획**

---

## 참고 자료
- Apache Struts 2.3.32 Documentation
- Spring Framework 2.5/5.x Migration Guide
- iBATIS 2.x → MyBatis 3.x Migration Guide
- JSP 2.x Specification
