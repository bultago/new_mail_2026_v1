# 레거시 컴파일 에러 수정 계획

**작성일**: 2025-10-21 24:15  
**총 에러**: 572개  
**예상 소요 시간**: 약 3-4시간

---

## 📊 에러 유형별 분류

### 1. 인코딩 에러 (200개) - 우선순위: 최상

**영향받은 파일**: 3개
- CalendarSerializer.java
- WbxmlException.java
- IMailServiceManager.java

**원인**: EUC-KR 인코딩 파일을 UTF-8로 컴파일

**해결 방법**:
- 파일 인코딩을 UTF-8로 변환
- 또는 pom.xml에서 source encoding을 EUC-KR로 설정

**예상 시간**: 30분

---

### 2. 패키지 누락 에러 (18개) - 우선순위: 상

**누락된 패키지** (6개):
1. `com.sun.mail.imap` - JavaMail 관련
2. `com.terracetech.tims.config` - 설정 클래스
3. `javax.mail` - JavaMail API
4. `org.kxml2.io` - KXML 라이브러리
5. `org.kxml2.wap` - KXML WAP
6. `org.xmlpull.v1` - XML Pull Parser

**해결 방법**:
- pom.xml에 누락된 의존성 추가
- 또는 해당 클래스 사용 안 하는 경우 제거

**예상 시간**: 1시간

---

### 3. 심볼 누락 에러 (176개) - 우선순위: 중

**누락된 주요 클래스** (19개):
1. `AddressVO` - 주소 VO
2. `AddressbookManager` - 주소록 매니저 (오타?)
3. `BbsContentVO`, `BbsVO` - 게시판 VO
4. `ConfigurationLoader` - 설정 로더
5. `DataSourceCollection` - 데이터소스 컬렉션
6. `IAttachSettingDao` - 첨부파일 설정 DAO
7. `ILastrcptDao` - 최근 수신자 DAO
8. `IMailHomePortletDao` - 메일 홈 포틀릿 DAO
9. `IOrganizationDao` - 조직도 DAO
10. `ISettingFilterDao`, `ISettingPop3Dao`, `ISettingUserEtcInfoDao` - 설정 DAO들
11. `MailVO` - 메일 VO
12. `Quota` - JavaMail Quota 클래스
13. `SessionUtil` - 세션 유틸
14. `Transactional` - Spring 어노테이션 (import 문제)

**원인**:
- Phase 3에서 DAO 삭제 시 일부 참조가 남음
- VO 클래스 누락
- import 문제

**해결 방법**:
- 삭제된 DAO 클래스 복구 또는 참조 제거
- VO 클래스 확인 및 복구
- import 문 수정

**예상 시간**: 2시간

---

### 4. 기타 에러 (178개) - 우선순위: 하

**내용**: 위 3가지 유형에 포함되지 않는 에러들

**예상 시간**: 30분

---

## 🎯 수정 계획

### Step 1: 인코딩 에러 수정 (30분)

**작업**:
1. 3개 파일 인코딩 확인
2. UTF-8로 변환 또는 pom.xml 설정 변경
3. 재컴파일 확인

**예상 감소**: 200개 에러

---

### Step 2: 패키지 누락 해결 (1시간)

**작업**:
1. pom.xml에 누락된 의존성 추가:
   ```xml
   <!-- JavaMail -->
   <dependency>
       <groupId>com.sun.mail</groupId>
       <artifactId>jakarta.mail</artifactId>
   </dependency>
   
   <!-- KXML -->
   <dependency>
       <groupId>net.sf.kxml</groupId>
       <artifactId>kxml2</artifactId>
   </dependency>
   
   <!-- XML Pull Parser -->
   <dependency>
       <groupId>xmlpull</groupId>
       <artifactId>xmlpull</artifactId>
   </dependency>
   ```

2. config 패키지 클래스 확인 및 복구

**예상 감소**: 18개 에러

---

### Step 3: 심볼 누락 해결 (2시간)

**작업**:

**3-1. DAO 인터페이스 누락 확인 및 복구**:
- IAttachSettingDao
- ILastrcptDao
- IMailHomePortletDao
- IOrganizationDao
- ISettingFilterDao
- ISettingPop3Dao
- ISettingUserEtcInfoDao

**해결**:
- Phase 3에서 삭제된 DAO가 있는지 백업에서 확인
- 참조하는 곳이 있으면 복구, 없으면 참조 제거

**3-2. VO 클래스 누락 확인**:
- AddressVO
- BbsContentVO, BbsVO
- MailVO

**해결**:
- 클래스 파일 존재 여부 확인
- 패키지 import 문제인지 확인

**3-3. Import 문제 수정**:
- `Transactional` → `org.springframework.transaction.annotation.Transactional`
- `Quota` → `com.sun.mail.imap.protocol.Quota` 또는 `javax.mail.Quota`

**예상 감소**: 176개 에러

---

### Step 4: 기타 에러 수정 (30분)

**작업**:
- 남은 에러 개별 확인 및 수정

**예상 감소**: 178개 에러

---

## 📅 작업 일정

### 총 예상 시간: 4시간

| 단계 | 작업 | 소요 시간 | 에러 감소 |
|------|------|----------|----------|
| 1 | 인코딩 에러 수정 | 30분 | -200개 |
| 2 | 패키지 누락 해결 | 1시간 | -18개 |
| 3 | 심볼 누락 해결 | 2시간 | -176개 |
| 4 | 기타 에러 수정 | 30분 | -178개 |
| **합계** | | **4시간** | **-572개** |

---

## 🎯 수정 순서

1. **인코딩 에러** (가장 많은 에러 유발) → 우선 수정
2. **패키지 누락** (의존성 추가) → 빠른 해결 가능
3. **심볼 누락** (개별 확인 필요) → 시간 소요
4. **기타 에러** (잔여 처리)

---

## ✅ 시작 준비

작업을 시작하시겠습니까?

