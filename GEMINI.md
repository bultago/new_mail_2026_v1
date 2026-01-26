# Agent Rules (에이전트 규칙)

## 1. Language Policy (언어 정책)
- **All Responses in Korean**: 모든 응답, 설명, 대화는 반드시 **한국어**로 작성해야 합니다.
- **Exception**: 코드, 변수명, 파일 경로, 로그 메시지와 같은 기술적인 용어는 영어를 유지합니다.

## 2. Project Rules
- `PROJECT_RULES.md` 파일의 내용을 준수합니다.

## 3. Tool Usage Policy (도구 사용 정책)
- **Browser Testing Fallback**: 내부 브라우저 도구 사용 시 **429 Too Many Requests** 에러가 발생하면, 즉시 **Playwright MCP**를 사용하여 로컬 환경에서 테스트를 수행해야 합니다.

## 4. Backend Modernization Policy (백엔드 현대화 정책)
- **Reference**: `new_mail_2026_v1/plan/` 디렉토리의 문서들을 반드시 따릅니다.
- **TDD Mandatory**: 모든 서비스와 도메인 로직은 테스트 코드를 먼저 작성해야 합니다.
- **DDD Applied**: 비즈니스 로직은 Service나 Controller가 아닌 **Domain Entity**에 위치해야 합니다.
- **Task Management**: 남은 작업은 `plan/*.md` 문서와 GitHub Issue로 관리됩니다.
