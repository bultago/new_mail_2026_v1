# API & Controller Migration Plan (Complete & Structured)

This document maps **ALL Struts Actions** (348+) found in `struts-*.xml`.
Everything is categorized and described in Korean.

> **Status Legend**
> - 🟢 **View Ready**: Handled by View Controller.
> - 🟡 **REST Ready**: Handled by REST Controller.
> - 🔴 **Legacy**: Not yet migrated.

## 1. Mail Service (`struts-mail.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **발송 관리** | `writeMessage` | 메일 쓰기 | 🟢 | `MailWriteController` |
| | `sendMessage` | 메일 발송 | 🟢 | `MailSendController` |
| | `writePreview` | 미리보기 | 🔴 | - |
| | `recallMessage` | 메일 회수 | 🔴 | - |
| | `changeSentMessageFlag` | 보낸메일함 저장 설정 | 🔴 | - |
| | `attachCheck` | 첨부파일 검사 | 🔴 | - |
| **읽기/조회** | `listMessage` | 메일 목록 | 🟢 | `MailListController` |
| | `readMessage` | 메일 읽기 | 🟢 | `MailReadController` |
| | `readSimpleMessage` | 간단 보기 | 🔴 | - |
| | `readNestedMessage` | 중첩 메일 읽기 | 🔴 | - |
| | `viewMailFromIp` | 발송 IP 조회 | 🔴 | - |
| | `listRelationMessage` | 관련 메일 목록 | 🔴 | - |
| | `allSelectMessageProcess` | 전체 선택 처리 | 🔴 | - |
| **폴더/관리** | `viewFolderManage` | 폴더 관리 화면 | 🟢 | `FolderController` |
| | `changeUserFolderAging` | 폴더 보관기간 설정 | 🔴 | - |
| | `statusFolderBackup` | 폴더 백업 상태 | 🔴 | - |
| | `startFolderBackup` | 폴더 백업 시작 | 🔴 | - |
| | `downloadFolderBackup` | 백업 다운로드 | 🔴 | - |
| | `deleteFolderBackup` | 백업 삭제 | 🔴 | - |
| **첨부/기타** | `listBigAttach` | 대용량 첨부 목록 | 🔴 | - |
| | `deleteBigAttach` | 대용량 첨부 삭제 | 🔴 | - |
| | `reportSWRule` | 스팸 규칙 신고 | 🔴 | - |
| | `reportNcscInfo` | NCSC 신고 | 🔴 | - |
| | `localMailbox` | 로컬 메일함 | 🔴 | - |
| | `listMDNResponses` | 수신확인 목록 | 🔴 | - |
| | `viewMDNResponses` | 수신확인 상세 | 🔴 | - |
| | `sendMDNResponses` | 수신확인 발송 | 🔴 | - |
| **Test/Dev** | `listMessageTest` | 목록 테스트 | 🔴 | - |
| | `readMessageTest` | 읽기 테스트 | 🔴 | - |
| | `readUserSearchTest` | 검색 테스트 | 🔴 | - |
| | `searchEmailByName` | 이름 검색 | 🔴 | - |

## 2. Mobile Service (`struts-mobile.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller (Implemented) |
| :--- | :--- | :--- | :--- | :--- |
| **홈/인증** | `home` | 모바일 홈 | 🟢 | `MobileHomeController` |
| | `login` | 모바일 로그인 | 🟢 | `LoginAction` (jmobile) |
| | `logout` | 로그아웃 | 🟢 | `LogoutAction` (jmobile) |
| | `sso` | SSO 로그인 | 🟢 | `SsoAction` (hybrid) |
| | `welcome` | 웰컴 페이지 | 🟢 | `WelcomeAction` (jmobile) |
| | `changeMailMode` | 메일 모드 변경 | 🔴 | - |
| **메일 (JMobile)** | `mailList` | 메일 목록 | 🟢 | `MailListAction` (jmobile) |
| | `mailRead` | 메일 읽기 | 🟢 | `MailReadAction` (jmobile) |
| | `mailWrite` | 메일 쓰기 | 🟢 | `MailWriteAction` (jmobile) |
| | `mailSend` | 메일 발송 | 🟢 | `MailSendAction` (jmobile) |
| | `folderList` | 폴더 목록 | 🟢 | `MobileFolderController` |
| | `mailMdnList` | 수신확인 목록 | 🔴 | - |
| | `mailMdnRead` | 수신확인 상세 | 🔴 | - |
| | `mailMdnRecall` | 수신확인 회수 | 🔴 | - |
| | `mailWork` | 메일 작업(이동/삭제) | 🟢 | `MailWorkAction` (jmobile) |
| | `mailMdnWork` | 수신확인 작업 | 🔴 | - |
| **일정** | `monthCalendar` | 월간 일정 | 🟢 | `MobileCalendarController` |
| | `weekCalendar` | 주간 일정 | 🟢 | `MobileCalendarController` |
| | `viewCalendar` | 일정 상세 | 🟢 | `MobileCalendarController` |
| | `writeCalendar` | 일정 쓰기 | 🟢 | `MobileCalendarController` |
| | `saveCalendar` | 일정 저장 | 🟢 | `MobileCalendarController` |
| | `modifyCalendar` | 일정 수정 | 🔴 | - |
| | `deleteCalendar` | 일정 삭제 | 🔴 | - |
| | `modifyCalendarQuestion` | 일정 수정 질문 | 🔴 | - |
| | `deleteCalendarQuestion` | 일정 삭제 질문 | 🔴 | - |
| | `assetCalendar` | 자산 예약 | 🔴 | - |
| | `writeAssetCalendar` | 자산 예약 쓰기 | 🔴 | - |
| **주소록** | `privateAddrList` | 개인 주소록 목록 | 🟢 | `MobileAddrController` |
| | `publicAddrList` | 공용 주소록 목록 | 🟢 `MobileAddrController` |
| | `readAddrView` | 주소 상세 보기 | 🟢 | `MobileAddrController` |
| | `writeAddrView` | 주소 등록 화면 | 🟢 | `MobileAddrController` |
| | `addAddrSave` | 주소 저장 | 🟢 | `MobileAddrController` |
| | `updateAddrSave` | 주소 수정 저장 | 🟢 | `MobileAddrController` |
| | `deleteAddr` | 주소 삭제 | 🟢 | `MobileAddrController` |
| | `moveAddr` | 주소 이동 | 🟢 | `MobileAddrController` |
| | `writePrivateAddrList` | 개인 주소 쓰기 선택 | 🟢 | `MobileAddrController` |
| | `writePublicAddrList` | 공용 주소 쓰기 선택 | 🟢 | `MobileAddrController` |
| **게시판** | `bbsList` | 게시판 목록 | 🟢 | `MobileBBSController` |
| | `bbsContentList` | 게시물 목록 | 🟢 | `MobileBBSController` |
| | `bbsContentView` | 게시물 읽기 | 🟢 | `MobileBBSController` |
| | `bbsContentWrite` | 게시물 쓰기 | 🟢 | `MobileBBSController` |
| | `bbsContentSave` | 게시물 저장 | 🟢 | `MobileBBSController` |
| | `bbsContentUpdate` | 게시물 업데이트 | 🟢 | `MobileBBSController` |
| | `bbsContentModify` | 게시물 수정 | 🟢 | `MobileBBSController` |
| | `bbsContentDelete` | 게시물 삭제 | 🟢 | `MobileBBSController` |
| | `bbsContentViewReply` | 답글 보기 | 🟢 | `MobileBBSController` |
| | `bbsContentSaveReply` | 답글 저장 | 🟢 | `MobileBBSController` |
| | `bbsContentDeleteReply` | 답글 삭제 | 🟢 | `MobileBBSController` |
| **Mobile API (Hybrid)** | `mailHomeService` | 홈 데이터 API | 🟢 | `MailHomeServiceAction` |
| | `mailHomeScheduleService` | 홈 일정 API | 🟡 | `MobileApiService` |
| | `mailBoxListService` | 메일함 API | 🟢 | `MailBoxListServiceAction` |
| | `mailListService` | 메일 목록 API | 🟢 | `MailListServiceAction` |
| | `mailReadService` | 메일 본문 API | 🟢 | `MailReadServiceAction` |
| | `mailDownloadService` | 다운로드 API | 🟢 | `MailDownloadServiceAction` |
| | `mailWriteService` | 쓰기 폼 API | 🟢 | `MailWriteServiceAction` |
| | `mailWorkService` | 메일 작업 API | 🟢 | `MailWorkServiceAction` |
| | `mailSignListService` | 서명 목록 API | 🟢 | `MailSignListServiceAction` |
| | `mailSendService` | 메일 발송 API | 🟢 | `MailSendServiceAction` |
| | `mailSendCheckService` | 발송 체크 API | 🟢 | `MailSendCheckServiceAction` |
| | `mailSearchEmailService` | 이메일 검색 API | 🟢 | `MailSearchEmailServiceAction` |
| | `mailSearchAddrOrgService` | 주소/조직 검색 API| 🟡 | `MobileApiService` |
| | `mailAttachService` | 첨부파일 API | 🟢 | `MailAttachServiceAction` |
| | `mdnListService` | 수신확인 목록 API | 🟢 | `MailMdnServiceAction` |
| | `mdnViewService` | 수신확인 상세 API | 🟢 | `MailMdnServiceAction` |
| | `mdnRecallService` | 수신확인 회수 API | 🟢 | `MailMdnServiceAction` |
| | `mdnSendMailService` | 수신확인 메일 API | 🟢 | `MailMdnServiceAction` |
| | `addrMemberListService` | 멤버 목록 API | 🟢 | `AddrMemberListServiceAction` |
| | `addrMemberViewService` | 멤버 상세 API | 🟢 | `AddrMemberViewServiceAction` |
| | `addrMemberSaveService` | 멤버 저장 API | 🟢 | `AddrMemberSaveServiceAction` |
| | `addrMemberDeleteService` | 멤버 삭제 API | 🟢 | `AddrMemberDeleteServiceAction` |
| | `addrMemberMoveService` | 멤버 이동 API | 🟢 | `AddrMemberMoveServiceAction` |
| | `addrBookListService` | 주소록 목록 API | 🟡 | `MobileApiService` |
| | `addrGroupListService` | 주소록 그룹 API | 🟢 | `AddrBookGroupServiceAction` |

## 3. Webfolder (`struts-webfolder.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **파일 관리** | `listFolders` | 폴더 목록 | 🟢 | `WebfolderListController` |
| | `folderTree` | 폴더 트리 | 🟢 | `WebfolderTreeController` |
| | `uploadFiles` | 파일 업로드 | 🟢 | `WebfolderUploadController` |
| | `webfolder` | 웹하드 메인 | 🔴 | - |
| | `downloadFile` | 파일 다운로드 | 🔴 | - |
| | `writeAttachFile` | 첨부파일 쓰기 | 🔴 | - |
| | `webfolderPopup` | 웹하드 팝업 | 🔴 | - |
| **폴더 작업** | `deleteFolders` | 폴더 삭제 | 🟢 | `WebfolderWorkController` |
| | `createFolder` | 폴더 생성 | 🟢 | `WebfolderWorkController` |
| | `renameFolder` | 폴더명 변경 | 🟢 | `WebfolderWorkController` |
| | `copyAndMoveFolders` | 복사 및 이동 | 🟢 | `WebfolderWorkController` |
| | `listFolderData` | 폴더 데이터(Raw) | 🔴 | - |
| **공유 관리** | `searchShareFolder` | 공유 폴더 검색 | 🔴 | - |
| | `addShareFolder` | 공유 추가 | 🔴 | - |
| | `shareFolder` | 공유 설정 | 🔴 | - |
| | `searchUser` | 공유 유저 검색 | 🔴 | - |
| | `makeShareFolder` | 공유 폴더 생성 | 🔴 | - |
| | `modifyShareFolder` | 공유 수정 | 🔴 | - |
| | `deleteShareFolder` | 공유 삭제 | 🔴 | - |

## 4. Note (`struts-note.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **쪽지 기능** | `noteList` | 쪽지 목록 | 🟢 | `NoteListController` |
| | `noteRead` | 쪽지 읽기 | 🟢 | `NoteReadController` |
| | `noteWrite` | 쪽지 쓰기 | 🟢 | `NoteWriteController` |
| | `noteSend` | 쪽지 발송 | 🟢 | `NoteSendController` |
| | `noteSave` | 쪽지 보관 | 🟢 | `NoteWorkController` |
| | `noteDelete` | 쪽지 삭제 | 🟢 | `NoteWorkController` |
| | `moveSave` | 쪽지 이동 | 🟢 | `NoteWorkController` |
| | `noteReject` | 수신 거부 | 🟢 | `NoteWorkController` |
| | `noteSetting` | 쪽지 설정 | 🟢 | `NoteSettingController` |
| | `noteSaveSetting` | 설정 저장 | 🟢 | `NoteSettingController` |
| | `noteCommon` | 쪽지 공통 | 🔴 | - |
| | `noteInfo` | 쪽지 정보 | 🔴 | - |
| | `searchUserList` | 유저 검색 | 🔴 | - |
| | `noteAllSelect` | 전체 선택 | 🔴 | - |
| | `noteMdnInfo` | 수신 확인 정보 | 🔴 | - |
| | `noteRecallMdn` | 수신 확인 회수 | 🔴 | - |

## 5. Setting (`struts-setting.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **스팸/필터** | `viewSpamRule` | 스팸 규칙 조회 | 🟢 | `SpamRuleController` |
| | `saveSpamRule` | 스팸 규칙 저장 | 🟢 | `SpamRuleController` |
| | `viewFilter` | 필터 조회 | 🟢 | `FilterController` |
| | `saveFilter` | 필터 저장 | 🟢 | `FilterController` |
| | `modifyFilter` | 필터 수정 | 🟢 | `FilterController` |
| | `deleteFilter` | 필터 삭제 | 🟢 | `FilterController` |
| | `saveMailFilter` | 메일 필터 적용 | 🟢 | `FilterController` |
| | `saveFilterApply` | 필터 적용 실행 | 🟢 | `FilterController` |
| **자동응답/전달** | `viewAutoReply` | 자동 응답 조회 | 🟢 | `AutoReplyController` |
| | `saveAutoReply` | 자동 응답 저장 | 🟢 | `AutoReplyController` |
| | `viewForward` | 자동 전달 조회 | 🟢 | `ForwardController` |
| | `saveForward` | 자동 전달 저장 | 🟢 | `ForwardController` |
| | `deleteDefineForward` | 전달 설정 삭제 | 🟢 | `ForwardController` |
| **외부메일/서명** | `viewExtMail` | 외부메일 조회 | 🟢 | `ExtMailController` |
| | `viewExtMailPopup` | 외부메일 팝업 | 🟢 | `ExtMailController` |
| | `viewSelectedExtMail` | 선택 외부메일 | 🟢 | `ExtMailController` |
| | `saveExtMail` | 외부메일 저장 | 🟢 | `ExtMailController` |
| | `deleteExtMail` | 외부메일 삭제 | 🟢 | `ExtMailController` |
| | `modifyExtMail` | 외부메일 수정 | 🟢 | `ExtMailController` |
| | `viewSign` | 서명 조회 | 🟢 | `SignController` |
| | `jsonSignList` | 서명 목록 JSON | 🟢 | `SignController` |
| | `writeSignData` | 서명 작성 | 🟢 | `SignController` |
| | `uploadSignImage` | 서명 이미지 업로드 | 🟢 | `SignController` |
| | `saveSignData` | 서명 저장 | 🟢 | `SignController` |
| | `modifySignData` | 서명 수정 | 🟢 | `SignController` |
| | `updateSignData` | 서명 업데이트 | 🟢 | `SignController` |
| | `deleteSignData` | 서명 삭제 | 🟢 | `SignController` |
| | `modifySign` | 서명 설정 수정 | 🟢 | `SignController` |
| **사용자/환경** | `viewSetting` | 설정 조회 | 🟢 | `UserSettingController` |
| | `modifySetting` | 설정 수정 | 🟢 | `UserSettingController` |
| | `checkUserInfoAuth` | 정보수정 인증 | 🟢 | `UserInfoController` |
| | `viewUserInfoAuth` | 인증 화면 | 🟢 | `UserInfoController` |
| | `viewUserInfo` | 사용자 정보 조회 | 🟢 | `UserInfoController` |
| | `modifyUserInfo` | 사용자 정보 수정 | 🟢 | `UserInfoController` |
| | `updateUserInfo` | 정보 업데이트 | 🟢 | `UserInfoController` |
| | `viewZipcode` | 우편번호 조회 | 🟢 | `ZipcodeController` |
| | `viewVcardInfo` | VCard 조회 | 🟢 | `VcardController` |
| | `modifyVcardInfo` | VCard 수정 | 🟢 | `VcardController` |
| | `viewLayout` | 레이아웃 조회 | 🟢 | `LayoutController` |
| | `saveLayout` | 레이아웃 저장 | 🟢 | `LayoutController` |
| | `passwordChange` | 비밀번호 변경 | 🟢 | `PwdChangeController` |
| **기타** | `viewSchedulerSetting` | 일정 설정 조회 | 🟢 | `SchedSettingController` |
| | `saveSchedulerSetting` | 일정 설정 저장 | 🟢 | `SchedSettingController` |
| | `getJsonSchedulerShare` | 일정 공유 JSON | 🟢 | `SchedSettingController` |
| | `deleteSchedulerSetting` | 일정 설정 삭제 | 🟢 | `SchedSettingController` |
| | `uploadPicture` | 사진 업로드 | 🟢 | `PictureController` |
| | `savePicture` | 사진 저장 | 🟢 | `PictureController` |
| | `viewPicture` | 사진 보기 | 🟢 | `PictureController` |
| | `deletePicture` | 사진 삭제 | 🟢 | `PictureController` |
| | `viewLastrcpt` | 최근 수신자 | 🟢 | `LastRcptController` |
| | `jsonLastrcpt` | 최근 수신자 JSON | 🟢 | `LastRcptController` |
| | `deleteLastrcpt` | 최근 수신자 삭제 | 🟢 | `LastRcptController` |
| | `viewPKIUpdate` | PKI 인증서 조회 | 🟢 | `PKIController` |
| | `updatePKISign` | PKI 인증서 갱신 | 🟢 | `PKIController` |
| | `deleteCache` | 캐시 삭제 | 🟢 | `CacheController` |

## 6. Authentication (`struts-login.xml` + `register`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **등록/찾기** | `checkRegister` | 가입 자격 확인 | 🟢 | `RegisterUserController` |
| | `registerUser` | 가입 처리 | 🟢 | `RegisterUserController` |
| | `userIdDupCheck` | ID 중복 확인 | 🟢 | `UserIdCheckController` |
| | `registerUserWin` | 가입 팝업 | 🔴 | - |
| | `checkDomain` | 도메인 체크 | 🔴 | - |
| | `saveUserInfo` | 유저 정보 저장 | 🔴 | - |
| | `searchUserId` | ID 찾기 | 🟢 | `SearchUserController` |
| | `searchPassword` | 비밀번호 찾기 | 🟢 | `SearchUserController` |
| | `searchPasswordProcess` | 비번 찾기 처리 | 🟢 | `SearchUserController` |
| | `searchUserIdWin` | ID 찾기 팝업 | 🔴 | - |
| | `searchPasswordWin` | 비번 찾기 팝업 | 🔴 | - |
| | `changePassword` | 비밀번호 변경 | 🟢 | `PwdChangeController` |
| | `changePasswordProcess` | 비번 변경 처리 | 🟢 | `PwdChangeController` |

## 7. Portlet (`struts-portlet.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **포틀릿** | `mailView` | 메일 포틀릿 | 🟢 | `PortletMailController` |
| | `noticeView` | 공지사항 포틀릿 | 🟢 | `PortletNoticeController` |
| | `calendarView` | 일정 포틀릿 | 🟢 | `PortletCalendarController` |
| | `quotaView` | 용량 포틀릿 | 🟢 | `PortletQuotaController` |
| | `todaySchedule` | 오늘의 일정 | 🟢 | `PortletScheduleController` |
| | `clockView` | 시계 | 🟢 | `PortletClockController` |

## 8. Organization (`struts-organization.xml`)
| Feature Group | Action Name | Description (Korean) | Status | New Controller |
| :--- | :--- | :--- | :--- | :--- |
| **조직도** | `orgTree` | 조직도 트리 | 🟢 | `OrgTreeController` |
| | `memberList` | 멤버 목록 | 🟢 | `OrgListController` |
| | `orgJsonTree` | 조직도 Tree JSON | 🟢 | `OrgTreeController` |
| | `orgJsonlist` | 멤버 목록 JSON | 🟢 | `OrgListController` |
| | `orgCommon` | 조직도 공통 | 🔴 | - |
| | `orgPopupTree` | 조직도 팝업 트리 | 🔴 | - |
| | `orgPopupList` | 조직도 팝업 목록 | 🔴 | - |
