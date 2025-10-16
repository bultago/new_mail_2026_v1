# Phase 3 재작업 프롬프트 - MyBatis 마이그레이션

## 전체 재작업 프롬프트

```
Phase 3의 MyBatis 마이그레이션 작업을 재수행해줘.

작업 범위:
1. MyBatis 3.5.13 설정
2. iBATIS → MyBatis SQL 매핑 변환 스크립트 작성
3. 모듈별 DAO → Mapper 전환 (common, 독립, 보조, 핵심 모듈)
4. Manager 클래스 업데이트
5. iBATIS 완전 제거
6. 전체 테스트 및 성능 비교

참조: docs/plans/phase-3/

목표: 모든 DAO → Mapper 전환 완료, 성능 개선 또는 유지
```

## 모듈별 전환 재실행

```
{모듈명} 모듈의 DAO → Mapper 전환을 재수행해줘.
1. SQL 매핑 XML 변환
2. Mapper 인터페이스 생성
3. Manager 클래스 업데이트
4. 테스트 실행

모듈: common / organization / setting / mail 등
```

