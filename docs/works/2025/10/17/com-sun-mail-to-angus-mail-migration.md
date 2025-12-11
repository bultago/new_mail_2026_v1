# com.sun.mail → org.eclipse.angus.mail 패키지 변환

## 작업 개요
- **작업 ID**: P2-010 (일부)
- **작업 일시**: 2025-10-17
- **목표**: JavaMail 내부 API(com.sun.mail) → Angus Mail(org.eclipse.angus.mail) 변환

## 작업 내용

### 1. 인코딩 문제 해결 (8개 파일)
복사한 TMailMessage 관련 클래스들이 EUC-KR 인코딩으로 되어있어 UTF-8로 변환:
- `src/com/terracetech/tims/mail/search/SearchRequest.java`
- `src/com/terracetech/tims/mail/search/TMailSearchQuery.java`
- `src/com/terracetech/tims/mail/search/XSearchCommand.java`
- `src/com/terracetech/tims/mail/sort/SortRequest.java`
- `src/com/terracetech/tims/mail/sort/XAllSortCommand.java`
- `src/com/terracetech/tims/mail/tag/TMailTag.java`
- `src/com/terracetech/tims/mail/tag/TagRequest.java`
- `src/com/terracetech/tims/mail/tag/XTagCommand.java`

```bash
iconv -f EUC-KR -t UTF-8 <파일명> > <파일명>.utf8 && mv <파일명>.utf8 <파일명>
```

### 2. com.sun.mail → org.eclipse.angus.mail 패키지 변환 (37개 파일)

#### 2.1 TMailMessage 관련 클래스 (20개)
- TMailFolder.java
- TMailMessage.java
- TMailPart.java
- TMailUtility.java
- TMailStore.java
- TMailSecurity.java
- TMailMimeUtility.java
- TMailXCommand.java
- sort/SortMessage.java
- sort/SortRequest.java
- sort/TIMSBODY.java
- sort/TIMSRFC822DATA.java
- sort/TIMSRFC822SIZE.java
- sort/TIMSUID.java
- sort/XAllSortResponse.java
- sort/TIMSBODYSTRUCTURE.java
- sort/TIMSENVELOPE.java
- sort/TIMSINTERNALDATE.java
- search/XSearchCommand.java
- sort/XAllSortCommand.java
- tag/XTagCommand.java

#### 2.2 Webmail 패키지 (16개)
- webmail/mail/manager/FolderHandler.java
- webmail/mail/manager/TMailStoreFactory.java
- webmail/mail/manager/Pop3Manager.java
- webmail/mail/manager/write/WriteHandler.java
- webmail/mail/manager/send/NormalSendHandler.java
- webmail/mail/manager/send/EachSendHandler.java
- webmail/mail/manager/send/SendHandler.java
- webmail/bbs/manager/BbsMessageUtil.java
- webmail/mail/action/ReadNestedMessageAction.java
- webmail/mail/action/ReadSecureMessageAction.java
- webmail/mail/action/ReceivePopMessageAction.java
- webmail/mail/action/DownloadAttachAction.java
- webmail/util/StringReplaceUtil.java
- webmail/mail/action/DownloadAllAttachAction.java
- webmail/webfolder/action/DownloadFileAction.java
- webmail/common/manager/VirusCheckManager.java
- webmail/util/Twofish_Algorithm.java
- webmail/util/StringReplacer.java
- webmail/util/StringUtils.java

#### 2.3 기타 (1개)
- webmail/common/ladmin/Protocol.java

### 3. 변환 패턴

| 기존 패키지 | 새 패키지 |
|------------|----------|
| `com.sun.mail.iap.*` | `org.eclipse.angus.mail.iap.*` |
| `com.sun.mail.imap.*` | `org.eclipse.angus.mail.imap.*` |
| `com.sun.mail.imap.protocol.*` | `org.eclipse.angus.mail.imap.protocol.*` |
| `com.sun.mail.pop3.*` | `org.eclipse.angus.mail.pop3.*` |
| `com.sun.mail.smtp.*` | `org.eclipse.angus.mail.smtp.*` |
| `com.sun.mail.util.*` | `org.eclipse.angus.mail.util.*` |

### 4. LogManagerBean 복사 및 변환 (1개 파일)
외부 프로젝트에서 복사:
- `src/com/terracetech/tims/logging/LogManagerBean.java`
- Log4j → SLF4J 변환 적용

### 5. 의존성 추가 (pom.xml)
- `net.sf.ehcache:ehcache:2.10.9.2` (캐시 라이브러리)

## 컴파일 에러 변화

| 단계 | 에러 개수 | 변화량 |
|------|----------|-------|
| 인코딩 변환 전 | 544 | - |
| 인코딩 변환 후 | 509 | -35 |
| LogManagerBean 추가 후 | 465 | -44 |
| 현재 | 465 | - |

## 남은 문제

### 1. 누락된 의존성 패키지 (약 465개 에러)
- `org.apache.commons.httpclient` (HttpClient 3.x, deprecated)
- `org.directwebremoting` (DWR - Direct Web Remoting)
- `com.maxmind.geoip2` (GeoIP 라이브러리)
- `org.apache.commons.lang` (Apache Commons Lang 2.x)
- `org.hsqldb.lib` (HSQLDB 라이브러리)

### 2. 다음 단계
1. 누락된 의존성 추가
2. Deprecated 라이브러리 대체 방안 검토
3. 컴파일 성공 확인

## 참고 사항
- Angus Mail은 Jakarta Mail의 공식 구현체로, Eclipse Foundation에서 관리
- com.sun.mail은 JavaMail의 내부 구현 API로, 공식적으로 사용이 권장되지 않음
- 하지만 TIMS7 프로젝트는 IMAP 프로토콜 레벨 API를 직접 사용하는 고급 기능이 있어 완전 대체 불가
- Angus Mail도 동일한 패키지 구조를 가지고 있어 패키지명만 변경하면 동작

