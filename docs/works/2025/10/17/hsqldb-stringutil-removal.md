# HSQLDB StringUtil 제거 및 내부 유틸리티로 대체

## 작업 개요
- **작업 ID**: P2-010 (일부)
- **작업 일시**: 2025-10-17 13:30
- **목표**: 외부 라이브러리 내부 API 사용 제거

## 배경

`org.hsqldb.lib.StringUtil`은 HSQLDB 데이터베이스의 **내부 유틸리티 클래스**입니다.

### 문제점
1. HSQLDB 내부 API를 애플리케이션 코드에서 직접 사용
2. HSQLDB 버전 업그레이드 시 호환성 문제 발생 가능
3. 잘못된 의존성 (데이터베이스 라이브러리를 유틸리티로 사용)

### 해결 방안
프로젝트 내부의 `com.terracetech.tims.webmail.util.StringUtils`로 대체

## 작업 내용

### 사용 현황 조사

총 5개 파일에서 사용:
1. DownloadAllAttachAction.java (Mail)
2. DownloadMessagesAction.java (Mail)
3. DownloadFileAction.java (WebFolder)
4. DownloadAllNoticeAttachAction.java (BBS)
5. DownloadAllAttachAction.java (BBS)

모두 `StringUtil.isEmpty()` 메서드 사용

### 변환 작업

#### 1. DownloadAllAttachAction.java (Mail)

**파일**: `src/com/terracetech/tims/webmail/mail/action/DownloadAllAttachAction.java`

```java
// Before
import org.hsqldb.lib.StringUtil;
String charset = StringUtil.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");

// After
import com.terracetech.tims.webmail.util.StringUtils;
String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
```

#### 2. DownloadMessagesAction.java

**파일**: `src/com/terracetech/tims/webmail/mail/action/DownloadMessagesAction.java`

```java
// Before
import org.hsqldb.lib.StringUtil;
String charset = StringUtil.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");

// After
import com.terracetech.tims.webmail.util.StringUtils;
String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
```

#### 3. DownloadFileAction.java

**파일**: `src/com/terracetech/tims/webmail/webfolder/action/DownloadFileAction.java`

```java
// Before
import org.hsqldb.lib.StringUtil;
String charset = StringUtil.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");

// After
import com.terracetech.tims.webmail.util.StringUtils;
String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
```

#### 4. DownloadAllNoticeAttachAction.java (BBS)

**파일**: `src/com/terracetech/tims/webmail/bbs/action/DownloadAllNoticeAttachAction.java`

```java
// Before
import org.hsqldb.lib.StringUtil;
String charset = StringUtil.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");

// After
import com.terracetech.tims.webmail.util.StringUtils;
String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
```

#### 5. DownloadAllAttachAction.java (BBS)

**파일**: `src/com/terracetech/tims/webmail/bbs/action/DownloadAllAttachAction.java`

```java
// Before
import org.hsqldb.lib.StringUtil;
String charset = StringUtil.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");

// After
import com.terracetech.tims.webmail.util.StringUtils;
String charset = StringUtils.isEmpty(EnvConstants.getMailSetting("default.zip.charset")) 
    ? "EUC-KR" : EnvConstants.getMailSetting("default.zip.charset");
```

## 작업 통계

- **총 변환 파일**: 5개
- **Import 변경**: 5개
- **메서드 호출 변경**: 5개
- **변환 패턴**: `org.hsqldb.lib.StringUtil` → `com.terracetech.tims.webmail.util.StringUtils`

### 모듈별 통계
| 모듈 | 파일 수 |
|------|---------|
| Mail | 2개 |
| BBS | 2개 |
| WebFolder | 1개 |

## 장점

1. **의존성 정리**: 불필요한 HSQLDB 라이브러리 의존성 제거
2. **유지보수성**: 내부 유틸리티 사용으로 일관성 확보
3. **안정성**: 데이터베이스 라이브러리 버전 변경에 영향 받지 않음
4. **설계 개선**: 적절한 레이어 분리

## 검증

### 컴파일 확인
```bash
mvn compile
```

### StringUtils.isEmpty() 메서드 확인
내부 StringUtils 클래스에 `isEmpty()` 메서드가 존재하는지 확인:
```java
public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
}
```

## 참고사항

### 유사한 사례
다른 데이터베이스나 라이브러리의 내부 유틸리티를 사용하는 경우도 동일하게 처리해야 합니다:
- `com.mysql.jdbc.StringUtils` (MySQL JDBC 내부)
- `oracle.sql.ARRAY` (Oracle JDBC 내부)
- 기타 드라이버 내부 클래스

### 권장 사항
- 서드파티 라이브러리의 **공개 API**만 사용
- 내부 패키지 (`.internal.`, `.impl.` 등)는 사용 금지
- 공통 유틸리티는 프로젝트 내부에 구현

