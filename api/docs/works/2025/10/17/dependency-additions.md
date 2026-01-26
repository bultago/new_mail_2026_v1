# 의존성 추가 작업

## 작업 개요
- **작업 ID**: P2-009 (계속)
- **작업 일시**: 2025-10-17 13:00 ~ 14:30
- **목표**: 누락된 의존성 추가

## 추가된 의존성 목록

### 1. EhCache 2.10.9.2 (캐시 라이브러리)
```xml
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>2.10.9.2</version>
</dependency>
```
**용도**: 애플리케이션 레벨 캐싱

### 2. Apache Commons Lang 2.6 (레거시)
```xml
<dependency>
    <groupId>commons-lang</groupId>
    <artifactId>commons-lang</artifactId>
    <version>2.6</version>
</dependency>
```
**용도**: 문자열, 배열 등 유틸리티 (레거시 코드 지원)
**참고**: Commons Lang 3.x도 함께 사용 중

### 3. Apache HttpClient 3.1 (레거시, deprecated)
```xml
<dependency>
    <groupId>commons-httpclient</groupId>
    <artifactId>commons-httpclient</artifactId>
    <version>3.1</version>
</dependency>
```
**용도**: HTTP 클라이언트 (레거시)
**참고**: HttpClient 4.x도 함께 추가 (향후 마이그레이션 준비)

### 4. Apache HttpClient 4.5.14 (최신)
```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.14</version>
</dependency>
```
**용도**: HTTP 클라이언트 (현대적 방식)

### 5. MaxMind GeoIP2 4.2.0
```xml
<dependency>
    <groupId>com.maxmind.geoip2</groupId>
    <artifactId>geoip2</artifactId>
    <version>4.2.0</version>
</dependency>
```
**용도**: IP 기반 지리적 위치 정보 조회

### 6. DWR 3.0.2-RELEASE (Direct Web Remoting)
```xml
<dependency>
    <groupId>org.directwebremoting</groupId>
    <artifactId>dwr</artifactId>
    <version>3.0.2-RELEASE</version>
</dependency>
```
**용도**: AJAX 프레임워크 (레거시)
**참고**: Phase 3.5에서 REST API로 대체 예정

### 7. Apache Commons Compress 1.27.1
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-compress</artifactId>
    <version>1.27.1</version>
</dependency>
```
**용도**: ZIP, TAR 등 압축 파일 처리

### 8. Apache Commons Configuration 1.10
```xml
<dependency>
    <groupId>commons-configuration</groupId>
    <artifactId>commons-configuration</artifactId>
    <version>1.10</version>
</dependency>
```
**용도**: 설정 파일 읽기/쓰기

### 9. Apache Commons DBCP 1.4
```xml
<dependency>
    <groupId>commons-dbcp</groupId>
    <artifactId>commons-dbcp</artifactId>
    <version>1.4</version>
</dependency>
```
**용도**: 데이터베이스 커넥션 풀
**참고**: HikariCP도 함께 사용 중

### 10. JTidy r938
```xml
<dependency>
    <groupId>net.sf.jtidy</groupId>
    <artifactId>jtidy</artifactId>
    <version>r938</version>
</dependency>
```
**용도**: HTML 정리 및 검증

## 의존성 카테고리

### 캐싱
- EhCache 2.10.9.2

### HTTP 통신
- Commons HttpClient 3.1 (레거시)
- HttpClient 4.5.14 (최신)

### 유틸리티
- Commons Lang 2.6 (레거시)
- Commons Compress 1.27.1
- Commons Configuration 1.10
- JTidy r938

### 데이터베이스
- Commons DBCP 1.4

### 웹 기술
- DWR 3.0.2-RELEASE
- GeoIP2 4.2.0

## 버전 선택 기준

### 레거시 라이브러리
기존 코드와의 호환성을 위해 구버전 사용:
- Commons Lang 2.6
- Commons HttpClient 3.1
- Commons DBCP 1.4
- Commons Configuration 1.10

### 최신 라이브러리
향후 마이그레이션을 위해 최신 버전 병행 사용:
- HttpClient 4.5.14
- Commons Compress 1.27.1

## 의존성 충돌 이슈

### 발생한 문제
일부 의존성 추가 후 컴파일 에러 증가:
- 469개 → 489개 (20개 증가)

### 원인
1. 라이브러리 간 전이적 의존성 충돌
2. 레거시 라이브러리와 최신 라이브러리의 충돌
3. Jakarta EE vs Java EE 충돌

### 해결 방안 (Phase 4에서)
1. Struts2 제거 → 많은 충돌 자동 해결
2. 사용하지 않는 라이브러리 제거
3. `<exclusions>` 태그로 전이적 의존성 제외
4. Maven Dependency Plugin으로 충돌 분석

## 여전히 누락된 의존성

### PKI/보안 라이브러리
- xecure.servlet (SoftForum)
- com.epki (KERIS)
- com.initech (Initech)

→ 외부 상용 라이브러리, Phase 4 이후 정리 예정

### 웹서비스
- org.apache.axis (SOAP 웹서비스, deprecated)
- org.springframework.remoting.jaxrpc (Spring에서 제거됨)

→ Phase 3.5 또는 Phase 4에서 REST API로 대체

### 기타
- org.kxml2.io (XML 파서)
- org.xmlpull.v1 (XML Pull Parser)
- org.mortbay (Jetty 관련)
- Alfresco JLAN (sun.security.jca 접근 문제)

→ 사용하지 않는 경우 주석 처리 예정

## 다음 단계

### Phase 4에서 처리할 사항
1. **Struts2 제거** → 많은 의존성 충돌 자동 해결
2. **사용하지 않는 코드 정리** → PKI, Axis 등
3. **의존성 최적화** → 중복 제거, 버전 통일
4. **Maven Dependency 분석** → 충돌 해결

### 문서화
- [ ] 의존성 트리 생성
- [ ] 충돌 분석 보고서
- [ ] 라이센스 검토

## 참고 명령어

### 의존성 트리 확인
```bash
mvn dependency:tree
```

### 의존성 분석
```bash
mvn dependency:analyze
```

### 충돌 확인
```bash
mvn dependency:tree -Dverbose
```

## 총 추가 의존성 통계

- **총 추가**: 10개 라이브러리
- **캐싱**: 1개
- **HTTP**: 2개
- **유틸리티**: 4개
- **데이터베이스**: 1개
- **웹**: 2개

