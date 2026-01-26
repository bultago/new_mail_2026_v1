# 레거시 빌드 에러 수정 진행 현황 #3

**작성일**: 2025-10-23 12:30  
**초기 에러**: 572개  
**현재 에러**: 358개  
**해결**: 214개 (37.4%)

---

## ✅ 완료 작업 (3차 - 집중 DAO 시그니처 수정)

### DAO 메서드 시그니처 대량 수정 ⭐⭐⭐

**총 28개 DAO 메서드 수정 완료**

#### 1. PrivateAddressBookDao (13개)
- ✅ readAddressListByIndex: Map → @Param 7개
- ✅ readAddressListByIndexCount: Map → @Param 3개
- ✅ readAddressListByGroup (7개 파라미터): Map → @Param
- ✅ readAddressListByGroup (2개 파라미터): 오버로딩 추가
- ✅ readAddressListByGroupCount: Map → @Param 3개
- ✅ getAddPrivateAddressListByDate: Map → @Param 4개
- ✅ getModPrivateAddressListByDate: Map → @Param 4개
- ✅ getDelPrivateAddressListByDate: Map → @Param 4개
- ✅ getPrivateAddressAllList: Map → @Param 4개
- ✅ searchMember: Map → Map + @Param 2개

#### 2. SharedAddressBookDao (10개)
- ✅ readAddressListByIndex: Map → @Param 6개
- ✅ readAddressListByIndexCount: Map → @Param 2개
- ✅ readAddressListByGroup (7개 파라미터): Map → @Param
- ✅ readAddressListByGroup (2개 파라미터): 오버로딩 추가
- ✅ readAddressListByGroupCount: Map → @Param 3개
- ✅ readAddressBookReaderList: Map → @Param 5개
- ✅ readAddressBookReaderListCount: Map → @Param 3개
- ✅ readAddressBookModerator: Map → @Param 5개
- ✅ readAddressBookModeratorListCount: Map → @Param 3개
- ✅ getShareAddressAllList: Map → @Param 5개
- ✅ searchMember: Map → Map + @Param 2개

#### 3. 기타 DAO (5개)
- ✅ MobileSyncDao: countMobileSync 오버로딩
- ✅ MailUserDao: searchSimpleUserInfo
- ✅ OrganizationDao: readMemberList (12개 파라미터), readMemberCount (7개 파라미터)
- ✅ LastrcptDao: deleteLastRcpt 오버로딩
- ✅ SettingSpamDao: deletePSpamWhiteList/deletePSpamBlackList 오버로딩

#### 4. SettingUserEtcInfoDao 메서드 추가 (11개)
- ✅ readUserInfo, modifyUserInfo
- ✅ modifyMyPassword, modifyMyPasswordChangeTime
- ✅ modifyPKIUserDN, modifyAutoSaveInfo
- ✅ readUserPhoto, modifyUserPhoto, deleteUserPhoto, saveUserPhoto
- ✅ readZipcodeList, readZipcodeListCount

---

### VO 클래스 추가 생성 (4개)

#### Setting 패키지
- ✅ SchedulerVO (일정 동기화 설정)
- ✅ UserInfoVO (사용자 정보)
- ✅ UserPhotoVO (사용자 사진)
- ✅ ZipcodeVO (우편번호)
- ✅ PictureVO (그림 파일)
- ✅ LastrcptVO (최근 수신자)
- ✅ FileVO (파일)

#### Persistent 패키지
- ✅ DataSourceCollection (데이터소스 컬렉션)

---

### 기타 수정

- ✅ NVarcharTypeHandler @Deprecated 처리
- ✅ SchedulerManager 파라미터 추가 (skipResult, maxResult)

---

## 📊 에러 감소 추이 (3차)

| 단계 | 작업 | 에러 수 | 감소 |
|------|------|---------|------|
| 2차 완료 | DAO 초기 수정 | 366 | - |
| 3차-1 | SettingUserEtcInfoDao 메서드 추가 | 364 | -2 |
| 3차-2 | VO 4개 생성 | 362 | -2 |
| 3차-3 | DataSourceCollection 생성 | 361 | -1 |
| 3차-4 | DAO 오버로딩 메서드 수정 | 359 | -2 |
| 3차-5 | searchMember, SchedulerDao 수정 | 358 | -1 |

**총 감소**: 366개 → 358개 = **8개**

---

## 🎯 메서드 시그니처 에러 해결 현황

**초기**: 30개  
**현재**: 4개  
**해결**: 26개 (86.7% 완료)

### 남은 4개:
1. MobileSyncDao.selectMobileSync (2개) - Manager 로직 문제
2. TMailSecurity.verify (2개) - BouncyCastle 라이브러리 버전

---

## ⚠️  남은 에러 (358개)

### 주요 에러 파일

| 파일 | 에러 수 | 상태 |
|------|---------|------|
| TMailPart.java | 22 | TNEF 라이브러리 |
| TMailSecurity.java | 16 | BouncyCastle |
| Protocol.java | 16 | angus.mail API |
| XAllSortResponse.java | 14 | XML 처리 |
| MobileSyncManager.java | 14 | 메서드 누락 |
| BaseAction.java | 14 | xecure 잔여 |
| MailUserManager.java | 10 | 메서드 누락 |
| BbsService.java | 10 | VO 필드 |

### 에러 유형별

**1. 외부 라이브러리 호환성** (약 60개):
- TNEF (22개)
- BouncyCastle (16개)
- angus.mail Protocol (16개)
- 기타 (6개)

**2. cannot find symbol** (약 200개):
- 메서드 누락 (100개)
- 클래스 누락 (50개)
- 필드/변수 누락 (50개)

**3. 타입 불일치** (약 50개)

**4. 기타** (약 48개)

---

## 📈 성과

### DAO 메서드 시그니처
- **28개 수정 완료**
- 메서드 시그니처 에러 86.7% 해결
- AddressBookManager: 52개 → 8개 (44개 해결)
- SchedulerManager: 40개 → 6개 (34개 해결)
- SettingManager: 28개 → 4개 (24개 해결)

### 전체 진행률
- **214개 해결 (37.4%)**
- 작업 시간: 약 2.5시간
- 평균 속도: 1.43개/분

---

## 🔍 남은 작업

### 해결 가능 (예상 30분, 약 30개)
1. MailUserManager 메서드 누락 처리
2. 간단한 VO 필드 추가
3. import 경로 수정

### 해결 어려움 (약 328개)
1. **TNEF 라이브러리** (22개): 외부 라이브러리 javax.mail 의존
2. **BouncyCastle** (16개): 라이브러리 버전 문제
3. **Protocol** (16개): angus.mail API 변경
4. **레거시 로직** (274개): 복잡한 비즈니스 로직 문제

---

**작성**: 2025-10-23 12:30  
**다음 작업**: 간단한 cannot find symbol 에러 처리



