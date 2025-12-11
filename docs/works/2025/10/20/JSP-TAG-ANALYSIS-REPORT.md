# JSP 태그 분석 보고서

**작성일**: 2025-10-20 14:30  
**작업자**: System  
**Phase**: 4 - Spring MVC 전환

---

## 🎯 작업 목표

Struts2 태그 사용 현황을 분석하고 Spring MVC 태그로의 전환 계획을 수립합니다.

---

## 📊 JSP 파일 현황

### 전체 통계
```
총 JSP 파일: 301개
Struts2 태그 사용 JSP: 63개 (약 21%)
Struts2 미사용 JSP: 238개 (약 79%)
```

### Struts2 태그 사용 위치

**Classic 디렉토리** (8개):
1. `/web/classic/welcome.jsp`
2. `/web/classic/mail/intro.jsp`
3. `/web/classic/mail/messageTestList.jsp`
4. `/web/classic/mail/messageTestRead.jsp`
5. `/web/classic/addrbook/list.jsp`
6. `/web/classic/addrbook/write.jsp`
7. `/web/classic/addrbook/uploadResult.jsp`
8. `/web/classic/webfolder/selectFolderList.jsp`

**기타 디렉토리** (55개):
- `/web/editor/` (1개)
- `/web/securemail/` (1개)
- `/web/portlet/` (3개)
- 기타 모듈 (50개)

---

## 🔍 Struts2 태그 사용 패턴 분석

### 1. 사용된 Struts2 태그

**확인된 태그**:
```jsp
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:property value="..."/>  (3건)
```

### 2. 주요 사용 사례

#### 예제 1: welcome.jsp
```jsp
<%@ taglib prefix="s"  uri="/struts-tags"%>

<s:property value="info.path"/> 
<s:property value="info.locale"/> 
<s:property value="info.encoding"/>
```

**변환 후**:
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

${info.path}
${info.locale}
${info.encoding}
```

#### 예제 2: intro.jsp
```jsp
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>

<tctl:msg key="intro.tooltip"/>
```

**분석**: 이미 JSTL과 커스텀 태그를 주로 사용하고 있으며, Struts2 태그 선언만 있고 실제 사용은 거의 없음.

---

## ✅ 긍정적 발견 사항

### 1. 이미 JSTL 기반 구현
대부분의 JSP가 이미 JSTL과 EL(Expression Language)을 사용하고 있습니다:

```jsp
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<c:forEach var="member" items="${members}" varStatus="loop">
    <td>${member.memberName}</td>
    <td>${member.memberEmail}</td>
</c:forEach>
```

### 2. 커스텀 태그 라이브러리 사용
프로젝트 전용 태그 라이브러리가 잘 구축되어 있습니다:

```jsp
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>

<tctl:msg key="intro.tooltip"/>
<tctl:msg key="intro.mail.read.title"/>
```

### 3. Struts2 의존도 매우 낮음
- **Struts2 태그 실제 사용**: 3건 (`<s:property>`)
- **대부분**: taglib 선언만 있고 실제 사용 없음

---

## 📋 변환 작업 계획

### Phase 1: Struts2 태그 제거 (1일)

**작업 대상**: 8개 JSP 파일 (Classic 디렉토리)

#### 작업 1: `<s:property>` 태그 변환
```jsp
<!-- 변환 전 -->
<s:property value="info.path"/>

<!-- 변환 후 -->
${info.path}
```

#### 작업 2: 미사용 taglib 선언 제거
```jsp
<!-- 제거 -->
<%@ taglib prefix="s" uri="/struts-tags"%>
```

### Phase 2: 기타 디렉토리 변환 (2일)

**작업 대상**: 55개 JSP 파일

- `/web/editor/` (1개)
- `/web/securemail/` (1개)
- `/web/portlet/` (3개)
- 기타 모듈 (50개)

### Phase 3: 검증 및 테스트 (1일)

- JSP 컴파일 오류 확인
- 런타임 테스트
- 화면 정상 동작 확인

---

## 🚀 즉시 실행 가능한 작업

### 1. Classic 디렉토리 JSP 변환 (8개)

**우선순위 1** (3개):
1. `welcome.jsp` - `<s:property>` 3건 → EL 변환
2. `mail/intro.jsp` - taglib 선언만 제거
3. `mail/messageTestList.jsp` - taglib 선언만 제거

**우선순위 2** (5개):
4. `mail/messageTestRead.jsp`
5. `addrbook/list.jsp`
6. `addrbook/write.jsp`
7. `addrbook/uploadResult.jsp`
8. `webfolder/selectFolderList.jsp`

### 2. 변환 스크립트

```bash
# Struts2 taglib 선언 제거
find web/classic -name "*.jsp" -exec sed -i 's/<%@ taglib prefix="s"  uri="\/struts-tags"%>//g' {} \;

# <s:property> 태그를 EL로 변환
find web/classic -name "*.jsp" -exec sed -i 's/<s:property value="\([^"]*\)"\s*\/>/\${\1}/g' {} \;
```

---

## 📊 예상 작업 시간

| 작업 | 파일 수 | 예상 시간 | 난이도 |
|------|--------|----------|--------|
| Classic JSP 변환 | 8개 | 2시간 | 쉬움 |
| 기타 JSP 변환 | 55개 | 4시간 | 쉬움 |
| 검증 및 테스트 | 전체 | 2시간 | 중간 |
| **총계** | **63개** | **8시간 (1일)** | **쉬움** |

---

## ✅ 결론

### 긍정적 요소
1. ✅ **Struts2 태그 의존도 매우 낮음**
   - 실제 사용: 3건만 확인
   - 대부분: 선언만 있고 미사용

2. ✅ **이미 JSTL 기반 구현**
   - `<c:forEach>`, `<c:if>` 등 표준 태그 사용
   - EL(Expression Language) 적극 활용

3. ✅ **커스텀 태그 라이브러리 완비**
   - `<tctl:msg>` 등 프로젝트 전용 태그
   - i18n 처리 완료

4. ✅ **변환 작업 매우 단순**
   - `<s:property>` → EL 변환
   - taglib 선언 제거
   - 스크립트로 자동화 가능

### 작업 계획
1. **Phase 1** (2시간): Classic 디렉토리 8개 파일 변환
2. **Phase 2** (4시간): 기타 55개 파일 변환
3. **Phase 3** (2시간): 검증 및 테스트

**총 소요 시간**: 8시간 (1일)

---

## 🎯 다음 단계

### 즉시 시작 가능
1. `welcome.jsp` 파일 변환 (가장 단순)
2. `mail/intro.jsp` 파일 변환
3. 나머지 Classic JSP 파일 변환

### 검증 방법
1. JSP 컴파일 오류 확인
2. 브라우저 테스트
3. 각 화면 정상 동작 확인

---

**작업 시작 준비 완료** ✅

