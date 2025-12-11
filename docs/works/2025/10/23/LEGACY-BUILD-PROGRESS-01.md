# 레거시 빌드 에러 수정 진행 현황 #1

**작성일**: 2025-10-23 10:30  
**초기 에러**: 572개  
**현재 에러**: 366개  
**해결**: 206개 (36.0%)

---

## ✅ 완료 작업 (1차)

### 1. 패키지 정리 (47개 파일)
- ✅ samsung 패키지 삭제 (26개)
- ✅ mcnc 패키지 삭제 (19개)
- ✅ 중복 디렉토리 삭제 (terracet pricing)
- ✅ 파일명 변경: BbsContentVO → BoardContentVO

### 2. VO 클래스 생성 (7개)
- ✅ NoteSettingVO
- ✅ OrganizationVO
- ✅ AddressVO
- ✅ FolderVO (webfolder)
- ✅ SchedulerVO
- ✅ PKISignVO
- ✅ LetterVO import 수정

### 3. Import 수정
- ✅ PPSpamRuleVO → PSpamRuleVO (5개 파일)
- ✅ javax.crypto (jakarta.crypto → javax.crypto) (2개 파일)
- ✅ Quota (org.eclipse.angus → jakarta.mail)
- ✅ DWR import 주석 처리 (BeforeServiceAdvice)
- ✅ JAX-RPC import 주석 처리 (3개 Endpoint)

### 4. Annotation 추가 (4개)
- ✅ WebfolderManager (@Service/@Transactional)
- ✅ BigattachManager (@Service/@Transactional)
- ✅ SharedFolderHandler (@Service/@Transactional)
- ✅ LastrcptManager (@Service/@Transactional)

### 5. DAO 메서드 시그니처 수정 ⭐ (주요 작업)
**PrivateAddressBookDao (11개 메서드)**:
- ✅ readAddressListByIndex: Map → 7개 개별 파라미터
- ✅ readAddressListByIndexCount: Map → 3개 개별 파라미터
- ✅ readAddressListByGroup: Map → 7개 개별 파라미터
- ✅ readAddressListByGroupCount: Map → 3개 개별 파라미터
- ✅ getAddPrivateAddressListByDate: Map → 4개 개별 파라미터
- ✅ getModPrivateAddressListByDate: Map → 4개 개별 파라미터
- ✅ getDelPrivateAddressListByDate: Map → 4개 개별 파라미터
- ✅ getPrivateAddressAllList: Map → 4개 개별 파라미터
- (searchMember, searchMemberCount는 Map 유지 - 검색 조건)

**SharedAddressBookDao (8개 메서드)**:
- ✅ readAddressListByIndex: Map → 6개 개별 파라미터
- ✅ readAddressListByIndexCount: Map → 2개 개별 파라미터
- ✅ readAddressListByGroup: Map → 7개 개별 파라미터
- ✅ readAddressListByGroupCount: Map → 3개 개별 파라미터
- ✅ readAddressBookReaderListCount: Map → 3개 개별 파라미터
- ✅ readAddressBookReaderList: Map → 5개 개별 파라미터
- ✅ readAddressBookModeratorListCount: Map → 3개 개별 파라미터
- ✅ readAddressBookModerator: Map → 5개 개별 파라미터
- ✅ getShareAddressAllList: Map → 5개 개별 파라미터
- (searchMember, searchMemberCount는 Map 유지 - 검색 조건)

**결과**: AddressBookManager 52개 에러 → 31개로 감소 (21개 해결!)

---

## 📊 에러 감소 추이

| 단계 | 작업 | 에러 수 | 감소 |
|------|------|---------|------|
| 초기 | - | 572 | - |
| 1차 | 파일명/VO 수정 | 389 | -183 |
| 2차 | Annotation 추가 | 387 | -2 |
| 3차 | DAO 시그니처 수정 | 366 | -21 |

---

## ⚠️  남은 에러 (366개)

### 주요 에러 파일 (재분석 필요)

예상:
1. **AddressBookManager**: 52개 → 31개 (21개 감소)
2. **TMailPart**: 30개 (그대로)
3. **MobileSyncManager**: 18개 (그대로)
4. **TMailSecurity**: 14개 (그대로)
5. **BaseAction**: 14개 (xecure 관련)

---

## 🔍 다음 작업 계획

### 우선순위 1: AddressBookManager 남은 에러 확인
- 31개 남은 에러 유형 파악
- 추가 DAO 메서드 시그니처 수정 필요성 확인

### 우선순위 2: TMailPart (30개)
- 메일 파트 관련 에러 확인 및 수정

### 우선순위 3: BaseAction/xecure (14개)
- xecure.servlet import 주석 처리
- 관련 코드 주석 처리

### 우선순위 4: 기타 DAO 메서드 시그니처
- MailUserDao, SchedulerDao 등 확인

---

**작성**: 2025-10-23 10:30  
**다음 작업**: AddressBookManager 남은 에러 확인



