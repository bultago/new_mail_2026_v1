# Phase 2 진행 상황 보고서

날짜: 2025-10-16  
작성 시간: 17:40

## 완료한 작업

### [P2-001] pom.xml 생성 ✅
- Java 21 (Eclipse Temurin)
- Spring 6.1.13
- MyBatis 3.5.16
- Jakarta EE 10 의존성
- OpenRewrite 플러그인

### [P2-002] Java 21 설치 ✅
- Eclipse Temurin OpenJDK 21.0.8 설치
- Maven 3.9.9 (Java 21 사용)

### [P2-003] 인코딩 문제 해결 ✅
다음 6개 파일을 EUC-KR → UTF-8로 변환:
- CalendarClientAddCommand.java
- CalendarServerDelCommand.java
- CalendarServerModCommand.java
- ContactsClientAddCommand.java
- ContactsServerAddCommand.java
- CalendarData.java

### [P2-004] 라이브러리 파일 정리 ✅
소스 코드에서 제거 (백업 완료):
- `src/com/sun/mail/` (3개 파일) → angus-mail 라이브러리로 대체
- `src/com/terracetech/secure/` → terrace_secure.jar로 대체

백업 위치: `.backup/`


## 현재 상황

### 컴파일 에러 분석

#### 1. javax → jakarta 패키지 변환 필요
- **영향받는 파일**: 194개
- **변환 필요 import**: 605개

주요 변환:
```
javax.servlet.* → jakarta.servlet.*
javax.servlet.http.* → jakarta.servlet.http.*
javax.mail.* → jakarta.mail.*
javax.activation.* → jakarta.activation.*
```

#### 2. Log4j → SLF4J 변경 필요
```
org.apache.log4j.Logger → org.slf4j.Logger
```

#### 3. iBATIS → MyBatis 변경 필요
```
org.springframework.orm.ibatis.support.SqlMapClientDaoSupport
→ org.mybatis.spring.support.SqlSessionDaoSupport
```


## 해결 방안

### 방안 1: search_replace 도구로 수동 변경
- 장점: 각 파일을 확인하며 변경 가능
- 단점: 194개 파일 × 평균 3회 변경 = 약 600회 작업 (시간 매우 오래 걸림)

### 방안 2: 일괄 변환 스크립트 사용 (권장)
- 변환 대상:
  ```bash
  import javax.servlet → import jakarta.servlet
  import javax.mail → import jakarta.mail
  import javax.activation → import jakarta.activation
  ```
- 백업 자동 생성
- 변환 후 git diff로 검증 가능

### 방안 3: 우선순위별 점진적 변환
1. 핵심 공통 클래스만 먼저 변환 (10~20개)
2. 컴파일 가능한 상태 확인
3. 나머지 단계적 변환


## 권장 사항

**방안 2 (일괄 변환)**를 권장합니다.

이유:
1. 194개 파일을 수동으로 변환하는 것은 비현실적
2. 패키지 이름 변경은 기계적인 작업
3. Git으로 변경사항 추적 가능
4. 롤백 가능

변환 스크립트:
```bash
#!/bin/bash
# javax-to-jakarta.sh

BACKUP_DIR=".backup/javax-to-jakarta-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$BACKUP_DIR"

# 백업
find src -name "*.java" -exec cp --parents {} "$BACKUP_DIR/" \;

# 변환
find src -name "*.java" -type f -exec perl -pi -e 's/import javax\.servlet/import jakarta.servlet/g' {} +
find src -name "*.java" -type f -exec perl -pi -e 's/import javax\.mail/import jakarta.mail/g' {} +
find src -name "*.java" -type f -exec perl -pi -e 's/import javax\.activation/import jakarta.activation/g' {} +

echo "변환 완료. 백업: $BACKUP_DIR"
echo "변경사항 확인: git diff src/"
```


## 다음 단계

1. 사용자 승인 대기
2. javax → jakarta 변환 실행
3. log4j → SLF4J 변환
4. iBATIS → MyBatis 변경
5. 재컴파일
6. 에러 수정
7. 컴파일 성공 확인

