# Phase 3.5 재작업 프롬프트 - DWR → REST API

## 전체 재작업 프롬프트

```
Phase 3.5의 DWR → REST API 전환 작업을 재수행해줘.

작업 범위:
1. REST API 인프라 (Jackson, ApiResponse, 예외 처리)
2. 메일 API 구현 (목록/읽기/전송/삭제/이동/첨부)
3. JavaScript 클라이언트 (Fetch API, jQuery)
4. 기타 모듈 API (주소록/일정/조직도/설정)
5. WebSocket 실시간 알림 (선택)
6. DWR 완전 제거

참조: docs/plans/phase-3.5/
참조: docs/migration-analysis/05-dwr-to-rest-api-migration.md

목표: DWR 완전 제거, 모든 AJAX 기능 REST API로 전환
```

## API 구현만 재실행

```
{모듈} 모듈의 REST API를 재구현해줘.
1. RestController 작성
2. Request/Response DTO
3. JavaScript 클라이언트 코드
4. 테스트

모듈: mail / addr / scheduler / organization
```

