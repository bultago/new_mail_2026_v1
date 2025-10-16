# Struts 2 → Spring MVC 마이그레이션 문서

## 개요

이 디렉토리는 TIMS7 Webmail v7.4.6S 프로젝트의 Struts 2에서 Spring Framework 5.x + Spring MVC로의 마이그레이션을 위한 종합 문서를 포함합니다.

**작성일**: 2025-10-14
**목표**: Struts 2.3.32 + Spring 2.5 + iBATIS 2.3.4 → Spring MVC 5.x + Spring Framework 5.x + MyBatis 3.x

---

## 문서 목록

### 1. [현황 분석 보고서](./01-current-state-analysis.md)
**목적**: 현재 시스템의 상태를 상세히 분석하고 마이그레이션 대상을 파악

**주요 내용**:
- 프로젝트 규모 및 기술 스택
- 아키텍처 및 디자인 패턴 분석
- Struts 2, Spring, iBATIS 사용 현황
- JSP 및 다국어 지원 현황
- 보안 컴포넌트 및 필터 체인
- 마이그레이션 고려사항 및 위험 요소

**대상 독자**: 프로젝트 관리자, 아키텍트, 개발팀 전체

---

### 2. [마이그레이션 전략](./02-migration-strategy.md)
**목적**: 전체 마이그레이션 로드맵 및 Phase별 전략 수립

**주요 내용**:
- 마이그레이션 목표 및 핵심 원칙
- 6개 Phase 로드맵 (사전 준비 ~ 배포)
- Phase별 상세 전략:
  - **Phase 0**: 사전 준비 (백업, 환경, 승인)
  - **Phase 1**: 환경 구축 및 분석 (2개월)
  - **Phase 2**: Spring Framework 5.x 업그레이드 (2-3개월)
  - **Phase 3**: iBATIS → MyBatis 마이그레이션 (2-3개월)
  - **Phase 4**: Struts 2 → Spring MVC 전환 (3-4개월)
  - **Phase 5**: 테스트 및 검증 (1-2개월)
  - **Phase 6**: 최적화 및 배포 (1개월)
- 위험 관리 및 롤백 계획
- 마일스톤 및 체크포인트

**대상 독자**: 프로젝트 관리자, 아키텍트, 기술 리더

---

### 3. [상세 마이그레이션 가이드](./03-detailed-migration-guide.md)
**목적**: 실제 작업 수행을 위한 단계별 상세 가이드

**주요 내용**:
- 작업 전 필수 준비사항 (백업, 환경)
- **Phase 2 상세**: Spring 5.x 업그레이드
  - pom.xml 작성 (Maven 예시)
  - Spring 설정 파일 업데이트
  - Deprecated API 교체
  - 컴파일 에러 수정
- **Phase 3 상세**: MyBatis 마이그레이션
  - MyBatis 설정 (mybatis-config.xml)
  - SQL 매핑 파일 변환 (iBATIS → MyBatis)
  - DAO → Mapper 인터페이스 전환
  - 자동 변환 스크립트 (Python)
- **Phase 4 상세**: Spring MVC 전환
  - Controller 작성 패턴
  - JSP 뷰 전환 (Struts 태그 → Spring/JSTL)
  - URL 매핑 전환
  - Interceptor 전환
  - Spring MVC 설정
  - 예외 처리 및 파일 업로드
- 테스트 실행 방법
- 배포 절차 및 롤백 절차
- 트러블슈팅 가이드

**대상 독자**: 개발자 (실무 담당자)

---

### 4. [마이그레이션 체크리스트](./04-migration-checklist.md)
**목적**: Phase별 작업 항목 체크 및 진행 상황 추적

**주요 내용**:
- Phase 0~6 상세 체크리스트
- 각 Phase별 완료 기준
- 테스트 항목 체크리스트:
  - 회귀 테스트
  - 성능 테스트
  - 보안 테스트
  - UAT (사용자 승인 테스트)
- 위험 관리 체크리스트
- 롤백 체크리스트
- 최종 완료 체크리스트

**대상 독자**: 프로젝트 관리자, 개발팀, QA팀

---

### 5. [DWR → REST API 마이그레이션](./05-dwr-to-rest-api-migration.md) 🆕
**목적**: DWR (Direct Web Remoting)을 Spring REST API + 최신 JavaScript로 전환

**주요 내용**:
- DWR 현황 분석
  - DWR 사용 JSP: 약 20개
  - DWR-Spring 통합 설정
  - 주요 사용 사례 (무한 스크롤, 실시간 알림 등)
- DWR → REST API 전환 전략
  - Spring `@RestController` 패턴
  - 표준 API 응답 형식 (`ApiResponse<T>`)
  - RESTful URL 설계
- JavaScript 클라이언트 구현
  - Fetch API 활용 (최신 방식)
  - jQuery AJAX 활용 (레거시 호환)
  - 공통 API 유틸리티 작성
- 고급 기능 구현
  - 파일 업로드 (멀티파트)
  - 실시간 알림 (WebSocket)
  - 에러 처리 강화
- 점진적 전환 전략 (DWR와 병행 운영)
- 테스트 작성 (Controller, JavaScript)

**대상 독자**: 개발자 (AJAX 전환 담당)

**전환 시기**: Phase 3.5 (MyBatis 전환 후, Spring MVC 전환 전)

---

### 6. [마이그레이션 실행 작업 플랜](./06-migration-work-plan.md) 🆕
**목적**: 단계별 실행 가능한 작업 플랜 및 Todo 항목 관리

**주요 내용**:
- **전체 진행 상황 대시보드**
  - Phase별 진행률 시각화
  - 우선순위 작업 현황
  - 전체 작업 진행률 (0/328개)
- **Phase 0: 사전 준비** (13개 작업)
  - 백업 및 버전 관리 (3일)
  - 승인 및 계획 (3일)
- **Phase 1: 환경 구축 및 분석** (50개 작업, 2개월)
  - Week 1-2: 개발 환경 구축
  - Week 3-4: 코드 분석
  - Week 5-8: 테스트 커버리지 확보
- **Phase 2: Spring 5.x 업그레이드** (35개 작업, 2-3개월)
  - Week 1-2: 빌드 시스템 전환
  - Week 3-4: Spring 설정 업데이트
  - Week 5-6: Component Scan 전환
  - Week 7-8: Deprecated API 교체
  - Week 9-12: 테스트 및 코드 리뷰
- **Phase 3: MyBatis 마이그레이션** (28개 작업, 2-3개월)
  - Week 1-2: MyBatis 설정
  - Week 3-12: 모듈별 순차 전환
  - Week 13: iBATIS 제거
- **Phase 3.5: DWR → REST API** (40개 작업, 1-2개월)
  - Week 1-2: REST API 인프라
  - Week 3-8: 모듈별 API 구현
  - Week 9: WebSocket 실시간 알림
  - Week 10-12: DWR 제거 및 테스트
- **Phase 4: Spring MVC 전환** (62개 작업, 3-4개월)
  - Week 1-2: Spring MVC 설정
  - Week 3-18: 모듈별 순차 전환
  - Week 19-23: JSP 태그/Struts 제거
  - Week 24-26: 테스트 및 검증
- **Phase 5: 테스트 및 검증** (50개 작업, 1-2개월)
  - Week 1-2: 회귀 테스트
  - Week 3: 성능 테스트
  - Week 4-5: 보안 테스트
  - Week 6-7: UAT
  - Week 8: 문서화
- **Phase 6: 최적화 및 배포** (50개 작업, 1개월)
  - Week 1: 최적화 작업
  - Week 2: 모니터링 설정
  - Week 3: 배포 준비
  - Week 4: 프로덕션 배포 및 모니터링
- **작업 ID 체계**: [P{Phase}-{3자리숫자}] 형식
- **예상 총 기간**: 12-17개월
- **필요 리소스**: 개발자 3명, QA 2명, DBA 1명, DevOps 1명

**대상 독자**: 프로젝트 관리자, 개발팀 전체, QA팀

**사용 방법**:
1. 작업 시작 시 해당 작업 ID 체크
2. 작업 완료 시 `- [x]`로 변경
3. 이슈 발생 시 작업 ID와 함께 트래커에 등록
4. 주간 리뷰 시 진행률 계산

---

## 문서 읽기 순서

### 프로젝트 관리자 및 의사결정자
1. **현황 분석 보고서** → 현재 상태 파악
2. **마이그레이션 전략** → 전체 계획 및 일정 이해
3. **마이그레이션 체크리스트** → 진행 상황 추적

### 아키텍트 및 기술 리더
1. **현황 분석 보고서** → 기술 스택 및 아키텍처 이해
2. **마이그레이션 전략** → Phase별 전략 및 기술적 의사결정
3. **상세 마이그레이션 가이드** → 기술적 상세 검토
4. **마이그레이션 체크리스트** → 완료 기준 검토

### 개발자
1. **현황 분석 보고서** → 프로젝트 배경 이해
2. **상세 마이그레이션 가이드** → 실제 작업 수행
3. **마이그레이션 체크리스트** → 작업 항목 확인

---

## 핵심 마이그레이션 흐름

```
┌─────────────────────────────────────────────────┐
│  Phase 0: 사전 준비                              │
│  - 백업, 환경 구축, 승인                          │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 1: 환경 구축 및 분석 (2개월)              │
│  - 개발/테스트 환경, 코드 분석, 테스트 작성        │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 2: Spring 5.x 업그레이드 (2-3개월)        │
│  - Spring 2.5 → 5.3.x                           │
│  - Struts 2는 유지, Spring만 업그레이드          │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 3: iBATIS → MyBatis (2-3개월)            │
│  - iBATIS 2.3 → MyBatis 3.5.x                   │
│  - DAO → Mapper 인터페이스 전환                  │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 4: Struts 2 → Spring MVC (3-4개월)       │
│  - Action → Controller                          │
│  - Struts 태그 → Spring/JSTL 태그               │
│  - JSP 유지, Struts 완전 제거                    │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 5: 테스트 및 검증 (1-2개월)               │
│  - 회귀/성능/보안 테스트, UAT                     │
└──────────────────┬──────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────┐
│  Phase 6: 최적화 및 배포 (1개월)                 │
│  - 최적화, 모니터링, 프로덕션 배포                │
└─────────────────────────────────────────────────┘
```

---

## 주요 마이그레이션 패턴

### Struts Action → Spring Controller
```java
// Before (Struts 2)
public class MailListAction extends ActionSupport {
    public String execute() { ... }
}

// After (Spring MVC)
@Controller
@RequestMapping("/mail")
public class MailController {
    @GetMapping("/list")
    public String list(Model model) { ... }
}
```

### iBATIS DAO → MyBatis Mapper
```java
// Before (iBATIS)
public class MailDao extends SqlMapClientDaoSupport {
    public List<MailVO> selectMailList(String userId) {
        return getSqlMapClientTemplate().queryForList("Mail.selectMailList", userId);
    }
}

// After (MyBatis)
@Mapper
public interface MailMapper {
    List<MailVO> selectMailList(@Param("userId") String userId);
}
```

### JSP 태그 변환
```jsp
<!-- Before (Struts) -->
<s:form action="mailSend">
    <s:textfield name="subject" />
</s:form>

<!-- After (Spring) -->
<form:form action="${pageContext.request.contextPath}/mail/send" modelAttribute="mailForm">
    <form:input path="subject" />
</form:form>
```

---

## 예상 일정 및 리소스

### 전체 기간
- **6-12개월** (프로젝트 규모에 따라)
- Phase별 기간은 마이그레이션 전략 문서 참조

### 필요 리소스
- **개발자**: 3-5명 (풀타임)
- **아키텍트**: 1명
- **QA**: 2명
- **DevOps**: 1명 (파트타임)

### 주요 기술 스택
- Java 11+
- Spring Framework 5.3.x
- Spring MVC 5.3.x
- MyBatis 3.5.x
- Apache Tomcat 9.x
- Maven 3.6+ 또는 Gradle 7.x+

---

## 성공 기준

### 기능
- ✅ 기존 기능 100% 정상 동작
- ✅ 모든 테스트 통과
- ✅ 보안 취약점 0건

### 성능
- ✅ 로그인 < 400ms (20% 개선)
- ✅ 메일 목록 < 800ms (20% 개선)
- ✅ 메일 읽기 < 600ms (25% 개선)

### 품질
- ✅ 코드 커버리지 > 80%
- ✅ 기술 부채 50% 감소
- ✅ 유지보수성 향상

---

## 위험 요소 및 대응 방안

| 위험 | 영향도 | 대응 방안 |
|------|--------|-----------|
| 대규모 JSP 수정 | 높음 | 자동화 스크립트, 단계적 전환 |
| 성능 저하 | 높음 | 성능 테스트, 사전 튜닝 |
| 보안 이슈 | 높음 | 보안 검증, 침투 테스트 |
| 일정 지연 | 중간 | 버퍼 기간 확보 |
| 데이터 손실 | 높음 | 철저한 백업, 롤백 계획 |

---

## 연락처 및 지원

### 프로젝트 관련 문의
- 프로젝트 매니저: [이메일]
- 기술 리더: [이메일]

### 기술 지원
- Spring Framework: https://spring.io/support
- MyBatis: https://mybatis.org/mybatis-3/
- Apache Tomcat: https://tomcat.apache.org/

---

## 문서 업데이트 이력

| 날짜 | 버전 | 변경 내용 | 작성자 |
|------|------|-----------|--------|
| 2025-10-14 | 1.0.0 | 초기 문서 작성 (문서 1-4) | Claude Code |
| 2025-10-14 | 1.1.0 | DWR 마이그레이션 가이드 추가 (문서 5) | Claude Code |
| 2025-10-14 | 1.2.0 | 실행 작업 플랜 추가 (문서 6, 328개 Todo 항목) | Claude Code |

---

## 라이센스 및 저작권

이 문서는 TIMS7 Webmail 프로젝트의 내부 문서이며, 프로젝트 팀원에게만 공개됩니다.

**저작권 © 2025 TerraTech. All rights reserved.**
