# Phase 4 재작업 프롬프트 - Spring MVC 전환

## 전체 재작업 프롬프트

```
Phase 4의 Struts 2 → Spring MVC 전환 작업을 재수행해줘.

작업 범위:
1. Spring MVC 설정 (DispatcherServlet, ViewResolver 등)
2. 모듈별 Controller 작성 (test → 독립 → 보조 → 핵심)
3. JSP 뷰 전환 (Struts 태그 → Spring/JSTL)
4. Validation 전환
5. 예외 처리 및 URL 호환성
6. Struts 완전 제거
7. 전체 테스트 및 검증

참조: docs/plans/phase-4/

목표: Struts 완전 제거, 성능 20-25% 개선
```

## 모듈별 전환 재실행

```
{모듈} 모듈의 Struts → Spring MVC 전환을 재수행해줘.
1. Action → Controller 전환
2. JSP 태그 변환
3. URL 매핑 업데이트
4. 테스트

모듈: organization / setting / mail / mailuser 등
참조: docs/plans/phase-4/{모듈}-module.md
```

## JSP 태그 변환만 재실행

```
모든 JSP 파일의 Struts 태그를 Spring/JSTL 태그로 변환해줘. (P4-039 ~ P4-041)

변환 매핑:
- <s:form> → <form:form>
- <s:textfield> → <form:input>
- <s:if> → <c:if>
- <s:iterator> → <c:forEach>

301개 JSP 파일 대상
```

