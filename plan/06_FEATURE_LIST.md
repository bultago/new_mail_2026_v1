# Feature Implementation Status (Complete & Structured)
This document maps **ALL Spring Beans** (175+) found in `spring-*.xml`.
Every single bean is categorized and described in Korean.
> **Status Legend**
> - 🟢 **View Ready**: Handled by View Controller / Implemented.
> - 🔴 **Legacy**: Logic in `Manager` (High Refactoring Priority).

## 1. Mail Service (`spring-mail.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **발송 관리** | `writeMessageAction` | 메일 쓰기 화면 | 🟢 `MailWriteController` |
| | `sendMessageAction` | 메일 발송 처리 | 🟢 `MailSendController` |
| | `writePreviewAction` | 메일 미리보기 | 🔴 Legacy |
| | `writeForLocalMessage` | 로컬 메일 쓰기 | 🔴 Legacy |
| **읽기/조회** | `listMessageAction` | 메일 목록 조회 | 🟢 `MailListController` |
| | `readMessageAction` | 메일 본문 읽기 | 🟢 `MailReadController` |
| | `readNestedMessageAction` | 중첩(전달) 메일 읽기 | 🔴 Legacy |
| | `viewMessageSourceAction` | 메일 원문 보기 | 🔴 Legacy |
| | `viewLetterImgAction` | 편지지 이미지 보기 | 🔴 Legacy |
| **첨부파일** | `downloadAttachAction` | 첨부파일 다운로드 | 🟢 `DownloadAttachController` |
| | `downloadAllAttachAction` | 전체 첨부 다운로드 | 🔴 Legacy |
| | `downloadBigAttachAction` | 대용량 첨부 다운 | 🔴 Legacy |
| | `bigAttachManageAction` | 대용량 첨부 관리 | 🔴 Legacy |
| | `attachCheckAction` | 첨부파일 바이러스 검사 | 🔴 Legacy |
| **수신확인** | `receiveMDNResponse` | MDN(수신확인) 응답 처리 | 🔴 Legacy |
| | `listMDNResponsesAction` | 수신확인 목록 조회 | 🔴 Legacy |
| | `workMDNResponsesAction` | 수신확인 작업(회수 등) | 🔴 Legacy |
| **검색/필터** | `searchEmailByNameAction` | 이름으로 메일 검색 | 🔴 Legacy |
| | `readUserSearchTestAction` | 사용자 검색 테스트 | 🔴 Legacy |
| | `sortFilterMessageAction` | 메일 정렬 및 필터링 | 🔴 Legacy |
| | `listRelationMessage` | 관련 메일 목록 | 🔴 Legacy |
| **폴더/기타** | `folderInfoAction` | 폴더 정보 조회 | 🟢 `FolderController` |
| | `folderManageAction` | 폴더 관리(생성/삭제) | 🔴 Legacy |
| | `quotaViewAction` | 메일 용량 조회 | 🟢 `QuotaController` |
| | `reportSWRuleAction` | 스팸 신고 처리 | 🔴 Legacy |
| | `receivePopMessageAction` | 외부메일 가져오기 | 🔴 Legacy |
| | `LocalMailManageAction` | 로컬 메일함 관리 | 🔴 Legacy |
| | `mailPortletAction` | 메일 포틀릿 액션 | 🔴 Legacy |
| **Core Components** | `mailManager` | 메일 비즈니스 로직 | 🔴 Legacy |
| | `mailServiceManager` | 메일 서비스 매니저 | 🔴 Legacy |
| | `storeHandler` | 메일 저장 핸들러 | 🔴 Legacy |
| | `folderHandler` | 폴더 처리 핸들러 | 🔴 Legacy |
| | `messageHandler` | 메시지 처리 핸들러 | 🔴 Legacy |
| | `jsonHandler` | JSON 처리 핸들러 | 🔴 Legacy |
| | `commandHandler` | 커맨드 처리 핸들러 | 🔴 Legacy |
| | `sharedFolderHandler` | 공유폴더 핸들러 | 🔴 Legacy |
| **Internal DAOs** | `bigAttachDao` | 대용량 첨부 DAO | 🔴 Legacy |
| | `cacheEmailDao` | 이메일 캐시 DAO | 🔴 Legacy |
| | `letterDao` | 편지지 DAO | 🔴 Legacy |
| | `agingDao` | 메일 보관기간 DAO | 🔴 Legacy |
| | `sharedFolderDao` | 공유폴더 DAO | 🔴 Legacy |
| **Internal Managers** | `bigattachManager` | 대용량 파일 매니저 | 🔴 Legacy |
| | `pop3Manager` | POP3 매니저 | 🔴 Legacy |
| | `letterManager` | 편지지 매니저 | 🔴 Legacy |
| | `searchEmailManager` | 이메일 검색 매니저 | 🔴 Legacy |
| | `garnetMailManager` | Garnet 메일 연동 | 🔴 Legacy |
| **Internal Services** | `mailFolderService` | 메일 폴더 서비스 | 🔴 Legacy |
| | `mailMessageService` | 메일 메시지 서비스 | 🔴 Legacy |
| | `mailTagService` | 메일 태그 서비스 | 🔴 Legacy |
| | `mailSearchFolderService` | 검색 폴더 서비스 | 🔴 Legacy |
| | `mailCommonService` | 메일 공통 서비스 | 🔴 Legacy |
| | `mailWebService` | 메일 웹 서비스 | 🔴 Legacy |

## 2. Address Book (`spring-addr.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **주소록 관리** | `importAddrAction` | 주소록 가져오기(Import) | 🟢 `AddrImportController` |
| | `exportAddrAction` | 주소록 내보내기(Export) | 🔴 Legacy |
| | `privateAddressAddAction` | 개인 주소 추가 | 🟢 `AddrWriteController` |
| | `privateAddressSaveAction` | 개인 주소 저장 | 🟢 `AddrWriteController` |
| | `saveAddressAction` | 공용/개인 주소 저장 | 🟢 `AddrWriteController` |
| **목록/조회** | `sharedAddressBookListAction`| 공유 주소록 목록 | 🟢 `AddrSharedController` |
| | `viewAddressMemberAction` | 주소록 멤버 조회 | 🟢 `AddrListController` |
| | `viewAddressMemberListAction`| 멤버 목록 상세 | 🟢 `AddrListController` |
| | `viewReaderListAction` | 읽기 권한자 목록 | 🔴 Legacy |
| | `viewModeratorListAction` | 관리자 목록 | 🔴 Legacy |
| | `addrPopupAction` | 주소록 팝업 | 🔴 Legacy |
| | `addressCommonAction` | 주소록 공통 기능 | 🟢 `AddrCommonController` |
| | `viewAddressListTestAction` | 주소록 목록 테스트 | 🔴 Legacy |
| **Core Components** | `addressBookManager` | 주소록 매니저 | 🔴 Legacy |
| | `addressBookService` | 주소록 서비스 | 🔴 Legacy |
| | `contactService` | 연락처 서비스 | 🔴 Legacy |
| | `contactWebService` | 주소록 웹 서비스 | 🔴 Legacy |
| **Internal DAOs** | `privateAddressBookDao` | 개인 주소록 DAO | 🔴 Legacy |
| | `sharedAddressBookDao` | 공용 주소록 DAO | 🔴 Legacy |

## 3. Scheduler (`spring-calendar.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **일정 조회** | `monthSchedulerAction` | 월간 일정 보기 | 🟢 `MonthSchedulerController` |
| | `weekSchedulerAction` | 주간 일정 보기 | 🟢 `WeekSchedulerController` |
| | `daySchedulerAction` | 일간 일정 보기 | 🟢 `DaySchedulerController` |
| | `progressSchedulerAction` | 일정 진행률 보기 | 🔴 Legacy |
| **일정 관리** | `writeCalendarAction` | 일정 등록/수정 화면 | 🟢 `SchedWriteController` |
| | `viewCalendarAction` | 일정 상세 보기 | 🟢 `SchedViewController` |
| **Outlook 연동** | `schedulerOutlookBaseAction` | 아웃룩 연동 기본 | 🟢 `SchedOutlookController` |
| | `schedulerOutlookUpdateAction`| 아웃룩 업데이트 | 🟢 `SchedOutlookController` |
| | `schedulerOutlookReceiveAction`| 아웃룩 데이터 수신 | 🟢 `SchedOutlookController` |
| | `schedulerOutlookLoginAction` | 아웃룩 로그인 | 🟢 `SchedOutlookController` |
| | `schedulerOutlookSsoAction` | 아웃룩 SSO | 🟢 `SchedOutlookController` |
| **Core Components** | `schedulerManager` | 일정 매니저 | 🔴 Legacy |
| | `schedulerService` | 일정 서비스 | 🔴 Legacy |
| | `schedulerAssetService` | 자산 예약 서비스 | 🔴 Legacy |
| | `schedulerShareService` | 일정 공유 서비스 | 🔴 Legacy |
| | `calendarService` | 캘린더 서비스 | 🔴 Legacy |
| | `schedulerDao` | 일정 DAO | 🔴 Legacy |

## 4. Webfolder (`spring-webfolder.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **파일 관리** | `listFoldersAction` | 폴더 목록 조회 | 🟢 `WebfolderListController` |
| | `folderTreeAction` | 폴더 트리 조회 | 🟢 `WebfolderTreeController` |
| | `folderMainAction` | 웹하드 메인 | 🔴 Legacy |
| | `uploadFilesAction` | 파일 업로드 | 🟢 `WebfolderUploadController` |
| | `downloadFileAction` | 파일 다운로드 | 🔴 Legacy |
| | `writeAttachFileAction` | 첨부파일 쓰기 | 🔴 Legacy |
| **폴더 작업** | `deleteFoldersAction` | 폴더 삭제 | 🟢 `WebfolderWorkController` |
| | `createFolderAction` | 폴더 생성 | 🟢 `WebfolderWorkController` |
| | `renameFolderAction` | 폴더명 변경 | 🟢 `WebfolderWorkController` |
| | `copyAndMoveFoldersAction` | 복사 및 이동 | 🟢 `WebfolderWorkController` |
| | `listFolderDataAction` | 폴더 데이터 조회 | 🔴 Legacy |
| **공유 폴더** | `searchShareFolderAction` | 공유 폴더 검색 | 🔴 Legacy |
| | `addShareFolderAction` | 공유 폴더 추가 | 🔴 Legacy |
| | `shareFolderAction` | 폴더 공유 설정 | 🔴 Legacy |
| | `searchUserAction` | 공유용 사용자 검색 | 🔴 Legacy |
| | `makeShareFolderAction` | 공유 폴더 만들기 | 🔴 Legacy |
| | `deleteShareFolderAction` | 공유 폴더 삭제 | 🔴 Legacy |
| | `webfolderPopupAction` | 웹하드 팝업 | 🔴 Legacy |
| **Core Components** | `webfolderManager` | 웹하드 매니저 | 🔴 Legacy |
| | `webfolderDao` | 웹하드 DAO | 🔴 Legacy |

## 5. Note (`spring-note.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **쪽지 기능** | `noteWriteAction` | 쪽지 쓰기 | 🟢 `NoteWriteController` |
| | `noteSendAction` | 쪽지 발송 | 🟢 `NoteSendController` |
| | `noteReadAction` | 쪽지 읽기 | 🟢 `NoteReadController` |
| | `noteListAction` | 쪽지 목록 | 🟢 `NoteListController` |
| | `noteMdnAction` | 수신 확인 | 🔴 Legacy |
| | `noteWorkAction` | 쪽지 작업(이동/삭제) | 🟢 `NoteWorkController` |
| | `noteAllSelectAction` | 전체 선택 | 🔴 Legacy |
| | `noteInfoAction` | 쪽지 정보 | 🔴 Legacy |
| **관리/설정** | `noteSettingAction` | 쪽지 환경설정 | 🟢 `NoteSettingController` |
| | `searchUserListAction` | 사용자 검색 | 🔴 Legacy |

## 6. BBS (`spring-bbs.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **게시판** | `listContentAction` | 게시물 목록 | 🟢 `BBSListController` |
| | `writeContentAction` | 게시물 쓰기 | 🟢 `BBSWriteController` |
| | `viewContentAction` | 게시물 읽기 | 🟢 `BBSViewController` |
| | `viewContentReplyAction` | 답글 보기 | 🟢 `BBSViewController` |
| | `saveContentReplyAction` | 답글 저장 | 🟢 `BBSWriteController` |
| | `downloadBbsAttachAction` | 게시판 첨부 다운로드 | 🔴 Legacy |
| **공지사항** | `listNoticeContentAction` | 공지사항 목록 | 🟢 `NoticeListController` |
| | `viewNoticeContentAction` | 공지사항 보기 | 🟢 `NoticeViewController` |
| | `downloadNoticeAttachAction` | 공지 첨부 다운로드 | 🔴 Legacy |
| **Core Components** | `bbsManager` | 게시판 매니저 | 🔴 Legacy |
| | `bbsService` | 게시판 서비스 | 🔴 Legacy |
| **Internal DAOs** | `boardDao` | 게시판 DAO | 🔴 Legacy |
| | `boardContentDao` | 게시글 DAO | 🔴 Legacy |

## 7. Authentication (`spring-login.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **로그인** | `loginAction` | 일반 로그인 | 🟢 `LoginController` |
| | `empnoLoginAction` | 사번 로그인 | 🟢 `LoginController` |
| | `ssoAction` | SSO 로그인 | 🟢 `SsoController` |
| | `cookieSso` | 쿠키 기반 SSO | 🟢 `SsoController` |
| | `paramSso` | 파라미터 기반 SSO | 🟢 `SsoController` |
| | `testSsoAction` | SSO 테스트 | 🔴 Legacy |
| | `logoutAction` | 로그아웃 | 🟢 `LogoutController` |
| | `welcomeAction` | 웰컴 페이지 | 🔴 Legacy |
| | `checkSessionTimeoutAction` | 세션 타임아웃 체크 | 🟢 `SessionController` |
| **회원가입** | `registerUserAction` | 회원가입 처리 | 🟢 `RegisterUserController` |
| | `checkRegisterAction` | 가입 자격 확인 | 🟢 `RegisterUserController` |
| | `userIdDupCheckAction` | ID 중복 확인 | 🟢 `UserIdCheckController` |
| | `registerUserWinAction` | 가입 팝업 | 🔴 Legacy |
| | `checkDomainAction` | 도메인 확인 | 🔴 Legacy |
| **계정 관리** | `searchUserIdAction` | ID 찾기 | 🟢 `SearchUserController` |
| | `searchUserIdWinAction` | ID 찾기 팝업 | 🔴 Legacy |
| | `searchPasswordAction` | 비밀번호 찾기 | 🟢 `SearchUserController` |
| | `searchPasswordProcessAction`| 비밀번호 찾기 처리 | 🟢 `SearchUserController` |
| | `changePasswordAction` | 비밀번호 변경 | 🟢 `PwdChangeController` |
| | `changePasswordProcessAction`| 비밀번호 변경 처리 | 🟢 `PwdChangeController` |
| | `saveUserInfoAction` | 사용자 정보 저장 | 🔴 Legacy |
| **이미지/기타** | `viewImageAction` | 이미지 보기 | 🔴 Legacy |
| | `uploadAttachImageAction` | 첨부 이미지 업로드 | 🔴 Legacy |
| | `uploadCkAttachImageAction` | CKEditor 이미지 업로드 | 🔴 Legacy |
| | `uploadSmartAttachImageAction`| 스마트에디터 업로드 | 🔴 Legacy |
| **Core Components** | `mailUserManager` | 사용자 매니저 | 🔴 Legacy |
| | `userAuthManager` | 인증 매니저 | 🟢 Implemented (Mobile) |
| | `ssoManager` | SSO 매니저 | 🔴 Legacy |
| | `logManager` | 로그 매니저 | 🟢 Implemented (Mobile) |
| | `logoManager` | 로고 매니저 | 🔴 Legacy |
| | `ladminManager` | LAdmin 매니저 | 🔴 Legacy |
| | `systemConfigManager` | 시스템 설정 매니저 | 🔴 Legacy |
| **Internal DAOs** | `userInfoDao` | 사용자 정보 DAO | 🔴 Legacy |
| | `mailUserDao` | 메일 유저 DAO | 🔴 Legacy |
| | `mailDomainDao` | 메일 도메인 DAO | 🔴 Legacy |
| | `systemConfigDao` | 시스템 설정 DAO | 🔴 Legacy |

## 8. Setting (`spring-setting.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **스팸/필터** | `viewSpamRuleAction` | 스팸 규칙 조회 | 🟢 `SpamRuleController` |
| | `saveSpamRuleAction` | 스팸 규칙 저장 | 🟢 `SpamRuleController` |
| | `viewFilterAction` | 메일 필터 조회 | 🟢 `FilterController` |
| | `saveFilterAction` | 필터 저장 | 🟢 `FilterController` |
| | `modifyFilterAction` | 필터 수정 | 🟢 `FilterController` |
| | `deleteFilterAction` | 필터 삭제 | 🟢 `FilterController` |
| **자동응답/전달** | `viewAutoReplyAction` | 자동 응답 조회 | 🟢 `AutoReplyController` |
| | `saveAutoReplyAction` | 자동 응답 저장 | 🟢 `AutoReplyController` |
| | `viewForwardAction` | 자동 전달 조회 | 🟢 `ForwardController` |
| | `saveForwardAction` | 자동 전달 저장 | 🟢 `ForwardController` |
| | `deleteDefineForwardAction` | 전달 설정 삭제 | 🟢 `ForwardController` |
| **외부메일/서명** | `viewExtMailAction` | 외부메일 조회 | 🟢 `ExtMailController` |
| | `saveExtMailAction` | 외부메일 저장 | 🟢 `ExtMailController` |
| | `deleteExtMailAction` | 외부메일 삭제 | 🟢 `ExtMailController` |
| | `modifyExtMailAction` | 외부메일 수정 | 🟢 `ExtMailController` |
| | `viewSelectedExtMailAction` | 선택된 외부메일 조회 | 🟢 `ExtMailController` |
| | `viewSignAction` | 서명 조회 | 🟢 `SignController` |
| | `writeSignDataAction` | 서명 작성 | 🟢 `SignController` |
| | `uploadSignImageAction` | 서명 이미지 업로드 | 🟢 `SignController` |
| | `saveSignDataAction` | 서명 저장 | 🟢 `SignController` |
| | `modifySignDataAction` | 서명 데이터 수정 | 🟢 `SignController` |
| | `deleteSignDataAction` | 서명 삭제 | 🟢 `SignController` |
| | `modifySignAction` | 서명 설정 수정 | 🟢 `SignController` |
| | `updateSignDataAction` | 서명 업데이트 | 🟢 `SignController` |
| **사용자/환경** | `viewUserSettingAction` | 사용자 환경 조회 | 🟢 `UserSettingController` |
| | `modifyUserSettingAction` | 사용자 환경 수정 | 🟢 `UserSettingController` |
| | `passwordChangeActionAction` | 비밀번호 변경 | 🟢 `PwdChangeController` |
| | `checkUserInfoAuthAction` | 정보 수정 인증 | 🟢 `UserInfoController` |
| | `viewUserInfoAuthAction` | 인증 화면 | 🟢 `UserInfoController` |
| | `viewUserInfoAction` | 사용자 정보 조회 | 🟢 `UserInfoController` |
| | `modifyUserInfoAction` | 사용자 정보 수정 | 🟢 `UserInfoController` |
| | `updateUserInfoAction` | 사용자 정보 업데이트 | 🟢 `UserInfoController` |
| | `viewZipcodeAction` | 우편번호 조회 | 🟢 `ZipcodeController` |
| | `viewVcardAction` | VCard(명함) 조회 | 🟢 `VcardController` |
| | `modifyVcardAction` | VCard 수정 | 🟢 `VcardController` |
| **일정/기타** | `viewSchedulerAction` | 일정 설정 조회 | 🟢 `SchedSettingController` |
| | `saveSchedulerAction` | 일정 설정 저장 | 🟢 `SchedSettingController` |
| | `viewSchedulerShareJsonAction` | 일정 공유 설정(JSON) | 🟢 `SchedSettingController` |
| | `deleteSchedulerAction` | 일정 설정 삭제 | 🟢 `SchedSettingController` |
| | `uploadPictureAction` | 사진 업로드 | 🟢 `PictureController` |
| | `savePictureAction` | 사진 저장 | 🟢 `PictureController` |
| | `viewPictureAction` | 사진 보기 | 🟢 `PictureController` |
| | `deletePictureAction` | 사진 삭제 | 🟢 `PictureController` |
| | `viewLastrcptAction` | 최근 수신자 목록 | 🟢 `LastRcptController` |
| | `deleteLastrcptAction` | 최근 수신자 삭제 | 🟢 `LastRcptController` |
| | `viewPKISignUpdateAction` | PKI 인증서 조회 | 🟢 `PKIController` |
| | `updatePKISignAction` | PKI 인증서 갱신 | 🟢 `PKIController` |
| | `cacheDeleteAction` | 캐시 메모리 삭제 | 🟢 `CacheController` |
| **Core Components** | `lastrcptManager` | 목록 매니저 | 🔴 Legacy |
| | `vcardManager` | VCard 매니저 | 🔴 Legacy |
| | `homeManager` | 홈 매니저 | 🔴 Legacy |
| | `settingManager` | 설정 매니저 | 🔴 Legacy |
| | `signManager` | 서명 매니저 | 🔴 Legacy |
| | `secureManager` | 보안 매니저 | 🔴 Legacy |
| **Internal DAOs** | `settingFilterDao` | 필터 DAO | 🔴 Legacy |
| | `sgnImageDao` | 서명 이미지 DAO | 🔴 Legacy |
| | `vcardDao` | VCard DAO | 🔴 Legacy |
| | `settingUserEtcInfoDao` | 유저 설정 DAO | 🔴 Legacy |
| | `settingSpamDao` | 스팸 DAO | 🔴 Legacy |
| | `settingForwardDao` | 전달 DAO | 🔴 Legacy |
| | `settingAutoReplyDao` | 자동응답 DAO | 🔴 Legacy |
| | `lastrcptDao` | 최근수신자 DAO | 🔴 Legacy |
| | `settingPop3Dao` | POP3 설정 DAO | 🔴 Legacy |
| | `mailHomePortletDao` | 홈페이지 포틀릿 DAO | 🔴 Legacy |
| | `attachSettingDao` | 첨부파일 설정 DAO | 🔴 Legacy |
| | `settingSecureDao` | 보안 설정 DAO | 🔴 Legacy |

## 9. Organization (`spring-organization.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **조직도** | `viewOrgTreeAction` | 조직도 트리 | 🟢 `OrgTreeController` |
| | `viewMemberListAction` | 멤버 목록 | 🟢 `OrgListController` |
| | `viewOrgListJsonAction` | 멤버 목록(JSON) | 🟢 `OrgListController` |
| | `viewOrgTreeJsonAction` | 조직도 트리(JSON) | 🟢 `OrgTreeController` |
| **Core Components** | `orgDao` | 조직도 DAO | 🔴 Legacy |
| | `orgManager` | 조직도 매니저 | 🔴 Legacy |
| | `orgService` | 조직도 서비스 | 🔴 Legacy |

## 10. Mobile (`spring-mobile.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **모바일** | `mobileMailListAction` | 모바일 메일 목록 | 🟢 `MailListAction` |
| | `mobileMailReadAction` | 모바일 메일 읽기 | 🟢 `MailReadAction` |
| | `mobileMailWriteAction` | 모바일 메일 쓰기 | 🟢 `MailWriteAction` |
| | `monthCalendarAction` | 모바일 월간 일정 | 🟢 `MobileCalendarController` |
| | `weekCalendarAction` | 모바일 주간 일정 | 🟢 `MobileCalendarController` |
| | `dayCalendarAction` | 모바일 일간 일정 | 🟢 `MobileCalendarController` |
| | `mobileAddrListAction` | 모바일 주소록 목록 | 🟢 `MobileAddrController` |
| | `mobileAddrViewAction` | 모바일 주소록 상세 | 🟢 `MobileAddrController` |
| | `bbsListAction` | 모바일 게시판 목록 | 🟢 `MobileBBSController` |
| | `bbsContentListAction` | 모바일 게시물 목록 | 🟢 `MobileBBSController` |
| **Sync/Core** | `mobileSyncManager` | 모바일 동기화 매니저 | 🟢 Implemented (Mobile) |
| **Internal DAOs** | `mobileSyncDao` | 모바일 동기화 DAO | 🟢 Implemented (Mobile) |

## 11. Common (`spring-common.xml`)
| Feature Group | Bean ID | Description (Korean) | Status |
| :--- | :--- | :--- | :--- |
| **공통** | `docTemplateAction` | 문서 서식 관리 | 🟢 `DocTemplateController` |
| | `emptyMappingAction` | 빈 매핑 (Dummy) | 🔴 Legacy |
| **Core Components** | `virusManager` | 바이러스 매니저 | 🔴 Legacy |
| | `geoIpManager` | 접속 국가 매니저 | 🔴 Legacy |
| | `cacheManager` | 캐시 매니저 | 🔴 Legacy |
| | `checkUserExistManager` | 사용자 존재여부 체크 | 🔴 Legacy |
| | `docTemplateManager` | 서식 매니저 | 🔴 Legacy |
| **Configuration** | `baseConfiguration` | 기본 설정 | 🟢 Implemented (Config) |
| | `sqlMapClient` | MyBatis Client | 🔴 Legacy |
| | `transactionManager` | 트랜잭션 매니저 | 🔴 Legacy |
| | `txAdvisor` | 트랜잭션 Advisor | 🔴 Legacy |
| | `serviceAdvice` | 서비스 Advice | 🔴 Legacy |
| | `serviceAdvisor` | 서비스 Advisor | 🔴 Legacy |
| **Internal DAOs** | `docTemplateDao` | 문서 서식 DAO | 🔴 Legacy |
