# Phase 0 재작업 프롬프트 - 백업 및 Git 설정

## 재작업 시나리오

Phase 0 작업을 처음부터 다시 시작하거나, 특정 작업만 재실행해야 할 때 사용하는 프롬프트입니다.

---

## 전체 재작업 프롬프트

```
Phase 0의 백업 및 Git 설정 작업을 다시 수행해줘.

작업 범위:
1. 전체 소스코드 tar.gz 백업 생성
2. 데이터베이스 덤프 백업
3. 설정 파일 별도 백업
4. Git 저장소 초기화
5. .gitignore 파일 작성
6. Baseline 커밋 및 태그 생성
7. 브랜치 전략 수립 및 브랜치 생성

참조 문서: docs/plans/phase-0/backup-and-version-control.md
참조 문서: docs/plans/phase-0/approval-and-planning.md

백업 위치: /opt/
프로젝트 경로: /opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747

각 작업의 완료 조건을 확인하고, 작업 로그에 결과를 기록해줘.
```

---

## 개별 작업 재실행 프롬프트

### 소스코드 백업만 재실행
```
[P0-001] 작업을 다시 수행해줘.
전체 소스코드를 tar.gz 형식으로 백업하고, 무결성을 확인해줘.
백업 파일명: tims7-webmail-backup-$(date +%Y%m%d-%H%M%S).tar.gz
제외 항목: *.log, build/*, target/*
```

### Git 설정만 재실행
```
[P0-004] ~ [P0-007] 작업을 다시 수행해줘.
1. Git 저장소 초기화
2. .gitignore 파일 작성
3. Baseline 커밋 및 v7.4.6S-struts2-baseline 태그 생성
4. 브랜치 생성 (develop, feature/* 브랜치들)
```

---

## 검증 프롬프트

```
Phase 0 작업이 정상적으로 완료되었는지 검증해줘.

확인 항목:
1. 백업 파일 존재 및 크기 확인
2. Git 저장소 초기화 확인 (.git 디렉토리)
3. Baseline 태그 확인 (git tag -l)
4. 브랜치 확인 (git branch -a)

각 항목의 검증 결과를 보고해줘.
```

---

## 롤백 프롬프트

```
Phase 0 작업을 롤백하고 이전 상태로 되돌려줘.

롤백 항목:
1. Git 저장소 삭제 (.git 디렉토리)
2. 생성된 백업 파일 삭제 (확인 후)
3. 브랜치 정보 초기화

주의: 백업 파일은 삭제 전 사용자 승인 필요
```

