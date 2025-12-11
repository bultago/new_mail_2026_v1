# 레거시 빌드 에러 수정 진행 현황 #2

**작성일**: 2025-10-23 11:00  
**초기 에러**: 572개  
**현재 에러**: 366개  
**해결**: 206개 (36.0%)

---

## ✅ 완료 작업 (2차)

### 1. DAO 메서드 시그니처 수정 ⭐⭐ (주요 작업)

**PrivateAddressBookDao (11개 메서드)**:
- ✅ readAddressListByIndex: Map → @Param 7개
- ✅ readAddressListByIndexCount: Map → @Param 3개
- ✅ readAddressListByGroup: Map → @Param 7개
- ✅ readAddressListByGroupCount: Map → @Param 3개
- ✅ getAddPrivateAddressListByDate: Map → @Param 4개
- ✅ getModPrivateAddressListByDate: Map → @Param 4개
- ✅ getDelPrivateAddressListByDate: Map → @Param 4개
- ✅ getPrivateAddressAllList: Map → @Param 4개

**SharedAddressBookDao (8개 메서드)**:
- ✅ readAddressListByIndex: Map → @Param 6개
- ✅ readAddressListByIndexCount: Map → @Param 2개
- ✅ readAddressListByGroup: Map → @Param 7개
- ✅ readAddressListByGroupCount: Map → @Param 3개
- ✅ readAddressBookReaderListCount: Map → @Param 3개
- ✅ readAddressBookReaderList: Map → @Param 5개
- ✅ readAddressBookModeratorListCount: Map → @Param 3개
- ✅ readAddressBookModerator: Map → @Param 5개
- ✅ getShareAddressAllList: Map → @Param 5개

**결과**: AddressBookManager 52개 → 8개 (44개 해결!)

---

### 2. SchedulerVO 완성 ⭐ (주요 작업)

**문제**: 초기 생성한 SchedulerVO가 일정(schedule) VO였으나, 실제로는 월력(calendar) VO였음

**해결**: 25개 필드 추가
- 현재 날짜: thisdayStr, thisYear, thisMonth, thisDay, thisDayOfWeek
- 이전/다음 월: prevYear, prevMonth, prevDay, nextYear, nextMonth, nextDay
- 월력 정보: firstYear, firstMonth, firstDay, firstdayOfMonth, lastYear, lastMonth, lastDay, maxdayOfMonth
- 오늘: todayStr, todayYear, todayMonth, todayDay
- 리스트: monthDayList, weekDayList
- 기타: lunar

**결과**: SchedulerManager 40개 → 6개 (34개 해결!)

---

### 3. Mail 패키지 import 수정

**TMailPart.java**:
- ✅ javax.mail.Header → jakarta.mail.Header (10곳)
- ✅ javax.mail.Session → jakarta.mail.Session
- ✅ javax.mail.Message.RecipientType → jakarta.mail.Message.RecipientType

---

### 4. 유틸리티 클래스 생성

**CharsetUtility**:
- ✅ convertByteToStr 메서드 구현
- InputStream을 지정 charset으로 String 변환

---

## 📊 에러 감소 추이 (2차)

| 단계 | 작업 | 에러 수 | 감소 |
|------|------|---------|------|
| 초기 | - | 572 | - |
| 1차 완료 | 파일명/VO/Annotation | 387 | -185 |
| 2차-1 | DAO 시그니처 수정 | 366 | -21 |
| 2차-2 | SchedulerVO 수정 (일시적 증가) | 370 | +4 |
| 2차-3 | SchedulerVO 완성 | 367 | -3 |
| 2차-4 | TMailPart, CharsetUtility | 366 | -1 |

**실제 해결**: 387개 → 366개 = **21개 감소**
- DAO 시그니처: 44개 해결 (AddressBookManager)
- SchedulerVO: 34개 해결 (SchedulerManager)
- 기타: 57개 증가 (새로 발견된 에러)

---

## ⚠️  남은 에러 (366개)

### 주요 에러 파일 (현재)

| 파일 | 에러 수 | 주요 원인 |
|------|---------|-----------|
| TMailPart.java | 26 | Mail 관련 |
| MobileSyncManager.java | 18 | Mobile 동기화 |
| Protocol.java | 16 | 프로토콜 (새로 발견) |
| TMailSecurity.java | 16 | 보안 관련 |
| XAllSortResponse.java | 14 | XML 응답 |
| BaseAction.java | 14 | xecure 관련 |
| MailUserManager.java | 12 | 메서드 시그니처 |
| HybridAuthManager.java | 8 | 인증 |
| AddressBookManager.java | 8 | 잔여 에러 |
| SchedulerManager.java | 6 | 잔여 에러 |

### 에러 유형

**1. cannot find symbol** (약 200개):
- VO 클래스/메서드 누락
- import 경로 문제

**2. method cannot be applied** (약 50개):
- DAO 메서드 시그니처 불일치

**3. package does not exist** (약 30개):
- xecure.servlet, com.initech 등

**4. incompatible types** (약 40개):
- 타입 불일치

**5. 기타** (약 46개):
- duplicate class, no interface expected 등

---

## 🔍 다음 작업 계획

### 우선순위 1: TMailPart 남은 에러 (26개)
- Mail 관련 심볼 확인
- 추가 import/클래스 필요성 확인

### 우선순위 2: Protocol.java (16개)
- 새로 발견된 에러
- 빠른 확인 및 수정

### 우선순위 3: BaseAction/xecure (14개)
- xecure.servlet 주석 처리
- 관련 코드 주석 처리

### 우선순위 4: MailUserManager (12개)
- DAO 메서드 시그니처 확인

---

## 📈 작업 통계

**총 작업 시간**: 약 60분  
**해결 속도**: 약 3.4개/분  
**남은 예상 시간**: 약 107분 (1시간 47분)

**주요 성과**:
- DAO 메서드 시그니처 19개 수정 → 44개 에러 해결
- SchedulerVO 완성 (25개 필드) → 34개 에러 해결
- 총 206개 에러 해결 (36.0%)

---

**작성**: 2025-10-23 11:00  
**다음 작업**: TMailPart 남은 에러 확인



