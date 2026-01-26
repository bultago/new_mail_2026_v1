# TMailMessage 관련 클래스 복사 작업 보고서

**작업일**: 2025-10-17  
**작업 시간**: 09:00 - 09:30 (30분)  
**작업 ID**: Phase 2 추가 작업  
**상태**: ✅ 완료

---

## 작업 배경

### 문제 상황
컴파일 중 다음 오류 발견:
```
[ERROR] symbol: class TMailFolder
[ERROR] symbol: class TMailAddress
[ERROR] symbol: class TMailUtility
... (기타 TMailXxx 클래스들)
```

### 원인 분석
- 현재 프로젝트에 `TMailMessage.java`와 `TMailPart.java` 2개만 존재
- 나머지 관련 클래스들이 누락됨
- 원본 위치: `/mnt/d/workspace/tms7_pkg/tms7_46_ko/lib/src/com/terracetech/tims/mail/`

---

## 작업 내용

### 1. 소스 파일 탐색

**원본 프로젝트 구조 확인**:
```bash
find /mnt/d/workspace/tms7_pkg/tms7_46_ko/lib/src/com/terracetech/tims/mail -name "*.java"
```

**발견된 파일**: 35개
- 메인 패키지: 16개
- search 서브패키지: 3개
- sort 서브패키지: 11개
- tag 서브패키지: 3개
- TMailMessage.java (중복)
- TMailPart.java (중복)

---

### 2. 중복 파일 확인

**현재 프로젝트에 존재하는 파일**:
```bash
find src/com/terracetech/tims/mail -name "*.java" | sort
```

**결과**:
- TMailMessage.java ✓ (덮어쓰지 않음)
- TMailPart.java ✓ (덮어쓰지 않음)

---

### 3. 디렉토리 생성

```bash
mkdir -p src/com/terracetech/tims/mail/{search,sort,tag}
```

---

### 4. 파일 복사 (중복 제외)

**명령어**:
```bash
# 메인 클래스 복사 (16개)
cd /mnt/d/workspace/tms7_pkg/tms7_46_ko/lib/src/com/terracetech/tims/mail
cp -n AttachTerm.java MessageComparator.java MyselfSearchTerm.java \
      TMailAddress.java TmailCertificate.java TMailConfirm.java \
      TMailFolder.java TMailMDNHistory.java TMailMDNResponse.java \
      TMailMimeUtility.java TMailSecurity.java TMailSession.java \
      TMailStore.java TMailTnefAttach.java TMailUtility.java \
      TMailXCommand.java \
      /opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/src/com/terracetech/tims/mail/

# search 패키지 복사 (3개)
cd search
cp -n SearchRequest.java TMailSearchQuery.java XSearchCommand.java \
      /opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/src/com/terracetech/tims/mail/search/

# sort 패키지 복사 (11개)
cd ../sort
cp -n SortMessage.java SortRequest.java TIMSBODY.java TIMSBODYSTRUCTURE.java \
      TIMSENVELOPE.java TIMSINTERNALDATE.java TIMSRFC822DATA.java \
      TIMSRFC822SIZE.java TIMSUID.java XAllSortCommand.java XAllSortResponse.java \
      /opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/src/com/terracetech/tims/mail/sort/

# tag 패키지 복사 (3개)
cd ../tag
cp -n TagRequest.java TMailTag.java XTagCommand.java \
      /opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/src/com/terracetech/tims/mail/tag/
```

**옵션 설명**:
- `-n`: 기존 파일이 있으면 덮어쓰지 않음 (no-clobber)

---

### 5. javax → jakarta 변환

복사된 파일 중 16개 파일에 javax import가 있어 jakarta로 변환:

**변환된 파일**:
1. AttachTerm.java
2. MessageComparator.java
3. MyselfSearchTerm.java
4. TMailAddress.java
5. TMailConfirm.java
6. TMailFolder.java
7. TMailMDNResponse.java
8. TMailSecurity.java
9. TMailSession.java
10. TMailStore.java
11. TMailUtility.java
12. TMailXCommand.java
13. sort/SortMessage.java
14. sort/TIMSBODYSTRUCTURE.java
15. sort/TIMSENVELOPE.java
16. sort/TIMSINTERNALDATE.java

**변환 내용**:
```java
javax.mail.*          → jakarta.mail.*
javax.mail.internet.* → jakarta.mail.internet.*
javax.mail.search.*   → jakarta.mail.search.*
```

**검증**:
```bash
# javax import 확인 (0개여야 함)
grep -r "^import javax\." src/com/terracetech/tims/mail --include="*.java" | wc -l
# 결과: 0
```

---

## 작업 결과

### 복사된 파일 통계
```
기존 파일: 2개 (TMailMessage.java, TMailPart.java)
복사 파일: 33개
총 파일: 35개
```

### 패키지 구조
```
src/com/terracetech/tims/mail/
├── [메인 클래스 18개]
│   ├── AttachTerm.java
│   ├── MessageComparator.java
│   ├── MyselfSearchTerm.java
│   ├── TMailAddress.java
│   ├── TmailCertificate.java
│   ├── TMailConfirm.java
│   ├── TMailFolder.java
│   ├── TMailMDNHistory.java
│   ├── TMailMDNResponse.java
│   ├── TMailMessage.java (기존)
│   ├── TMailMimeUtility.java
│   ├── TMailPart.java (기존)
│   ├── TMailSecurity.java
│   ├── TMailSession.java
│   ├── TMailStore.java
│   ├── TMailTnefAttach.java
│   ├── TMailUtility.java
│   └── TMailXCommand.java
│
├── search/ (3개)
│   ├── SearchRequest.java
│   ├── TMailSearchQuery.java
│   └── XSearchCommand.java
│
├── sort/ (11개)
│   ├── SortMessage.java
│   ├── SortRequest.java
│   ├── TIMSBODY.java
│   ├── TIMSBODYSTRUCTURE.java
│   ├── TIMSENVELOPE.java
│   ├── TIMSINTERNALDATE.java
│   ├── TIMSRFC822DATA.java
│   ├── TIMSRFC822SIZE.java
│   ├── TIMSUID.java
│   ├── XAllSortCommand.java
│   └── XAllSortResponse.java
│
└── tag/ (3개)
    ├── TagRequest.java
    ├── TMailTag.java
    └── XTagCommand.java
```

---

## 주의사항 준수

### 사용자 요구사항 준수
✅ **"중복되는 클래스들은 덮어쓰지마"**
- `-n` (no-clobber) 옵션 사용
- TMailMessage.java, TMailPart.java 보호됨

✅ **원본 파일 보존**
- 원본 프로젝트 파일 수정 없음
- 복사만 수행

✅ **수동 작업**
- javax → jakarta 변환도 search_replace 도구로 수동 진행
- 스크립트 사용하지 않음

---

## 발견 사항

### com.sun.mail 의존성
복사된 일부 파일에서 `com.sun.mail` 패키지 사용:
- TMailFolder.java
- TMailStore.java
- TMailUtility.java
- sort/SortMessage.java

**조치**:
- pom.xml에 이미 angus-mail 의존성 추가되어 있음
- Jakarta Mail API 구현체로 제공

---

## 다음 단계

### 즉시 필요
1. iBATIS → MyBatis DAO 변환 시작
2. 컴파일 테스트 실행
3. 에러 확인 및 추가 조치

### 예상 효과
- TMailMessage 관련 컴파일 에러 해결
- Mail 모듈 정상 동작 기반 마련

---

## 교훈

### 잘된 점
- 중복 파일 보호 성공
- 체계적인 패키지 구조 유지
- javax → jakarta 변환 즉시 적용

### 개선 사항
- 초기 분석 시 의존성 파일 확인 필요
- 외부 프로젝트 파일 목록 사전 확보

---

**작업 완료**: TMailMessage 관련 33개 클래스 복사 및 jakarta 변환 완료

**다음 작업**: [P2-008] iBATIS → MyBatis DAO 변환

