# Phase 2 재작업 프롬프트 - Spring 5.x 업그레이드

## 전체 재작업 프롬프트

```
Phase 2의 Spring 5.x 업그레이드 작업을 재수행해줘.

작업 범위:
1. pom.xml 생성 및 Spring 5.3.30 의존성 추가
2. 모든 Spring XML 설정 파일 네임스페이스 업데이트 (11개)
3. 트랜잭션 관리 어노테이션 설정
4. Component Scan 및 어노테이션 기반 DI
5. Deprecated API 교체
6. 컴파일 및 테스트

참조: docs/plans/phase-2/

목표: Spring 5.x 컴파일 성공, 모든 테스트 통과, 성능 저하 없음
```

## Spring 설정 업데이트만

```
Spring XML 설정 파일 11개의 네임스페이스를 2.5에서 5.x로 업데이트해줘. (P2-006 ~ P2-016)
파일 위치: web/WEB-INF/classes/web-config/spring-*.xml
```

