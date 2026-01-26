# Phase 3.5: DWR 제거 전략 분석 보고서

**분석일**: 2025-10-21  
**분석자**: AI Assistant

---

## 🔍 DWR 사용 현황

### 현재 상태
- DWR Service 클래스: 11개
- DWR 사용 JSP: 20개
- Spring DWR 설정: 9개
- DWR 의존성: dwr-3.0.2-RELEASE

### 주요 사용 파일
```
web/classic/mail/messageList.jsp
web/classic/mail/messageWrite.jsp
web/classic/mail/messageRead.jsp
web/dynamic/addr/addrFrame.jsp
web/dynamic/scheduler/schedulerFrame.jsp
web/dynamic/org/orgFrame.jsp
... (총 20개 JSP)
```

---

## 📊 두 가지 방법 비교

### 방법 1: DWR 제거와 동시에 JavaScript 전환

**장점**:
- ✅ 작업 누락 위험 없음
- ✅ 바로 동작하는 코드 완성
- ✅ 테스트를 바로 진행 가능
- ✅ 롤백이 쉬움 (작업 중단 시)

**단점**:
- ❌ 시간이 오래 걸림 (JSP 20개 × 평균 30분 = 10시간)
- ❌ 한 번에 많은 파일 수정

**작업 흐름**:
```
JSP 1개 작업:
1. DWR 스크립트 태그 제거
2. DWR 함수 호출 찾기
3. REST API 호출로 교체
4. 테스트
→ 다음 JSP로 이동
```

---

### 방법 2: DWR 제거 후 추적하여 JavaScript 전환

**장점**:
- ✅ 단계적 작업 가능
- ✅ DWR 제거를 먼저 완료하여 의존성 정리
- ✅ 주석으로 TODO 표시하여 추적 용이

**단점**:
- ❌ **작업 누락 위험 매우 높음** ⚠️
- ❌ DWR 제거 후 일부 기능 동작 안 함
- ❌ TODO 주석이 많아짐
- ❌ 나중에 찾기 어려울 수 있음

**작업 흐름**:
```
1단계: DWR 제거
- DWR 스크립트 제거
- DWR 호출 부분을 주석으로 표시
  // TODO: REST API로 교체 필요 - MailMessageService.getList()

2단계: JavaScript 전환 (나중에)
- TODO 주석 검색
- REST API로 교체
- 테스트
```

---

## 🎯 권장 방법: **방법 1 (동시 작업)** ✅

### 이유:

**1. 작업 누락 방지** ⚠️ (가장 중요)
- 방법 2는 TODO 주석을 나중에 찾아야 함
- JSP가 20개나 되므로 일부 누락 가능성 높음
- 사용자가 우려한 것과 정확히 일치

**2. 품질 보증**
- 각 JSP를 완전히 전환 후 바로 테스트
- 동작 확인 후 다음으로 진행

**3. 실제 구현 가능**
- 이미 REST API Controller가 준비됨
- mail-api.js 래퍼도 있음
- 바로 교체 가능

---

## 📋 추천 작업 방식 (방법 1 개선)

### 단계별 작업 흐름:

**Step 1**: JSP 파일별 DWR 사용 분석
```bash
# 각 JSP의 DWR 호출 패턴 파악
grep "MailMessageService\|AddressBookService" web/classic/mail/messageList.jsp
```

**Step 2**: JSP 파일 하나씩 전환
```
JSP 작업 순서:
1. DWR 스크립트 태그 제거
2. DWR 함수 호출을 REST API로 교체
3. 동일 JSP 내에서 완전히 전환
4. 브라우저 테스트
5. 다음 JSP로 이동
```

**Step 3**: 모든 JSP 완료 후 DWR 제거
```
1. Spring XML에서 DWR 설정 제거
2. web.xml에서 DWR Servlet 제거
3. pom.xml에서 DWR 의존성 제거
4. DWR Service 클래스 제거
```

---

## ⏱️ 예상 소요 시간

### 방법 1 (권장):
- JSP 전환: 20개 × 30분 = **10시간**
- DWR 완전 제거: **1시간**
- **총 11시간** (약 1.5일)

### 방법 2:
- DWR 제거 + TODO 표시: **2시간**
- 나중에 TODO 찾아서 전환: **12시간** (찾는 시간 포함)
- **총 14시간** (약 2일) + 누락 위험

---

## ✅ 최종 권장 사항

**방법 1을 권장합니다!**

### 작업 순서:
1. **우선순위 높은 JSP 5개부터 시작**
   - web/classic/mail/messageList.jsp (메일 목록)
   - web/classic/mail/messageRead.jsp (메일 읽기)
   - web/dynamic/addr/addrFrame.jsp (주소록)
   - web/dynamic/scheduler/schedulerFrame.jsp (일정)
   - web/dynamic/org/orgFrame.jsp (조직도)

2. **각 JSP 완전 전환 후 테스트**

3. **나머지 15개 JSP 순차 전환**

4. **최종 DWR 완전 제거**

---

**시작하시겠습니까?**
