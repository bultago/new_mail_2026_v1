<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@include file="/common/authHeader.jsp"%>
<html>
<head>
<script language="javascript">

function srchByLeading(leadingPattern) {
	var f = document.forms[0];

	f.leadingPattern.value = leadingPattern;

	f.submit();
}

</script>
</head>
<body>
<form name="contentListForm" method="post" action="/addr/pAddrlist.do">
<input type="hidden" name="leadingPattern" value="all">

<table width="100%">
<tr>
	<td>
		<table border="0" width="100%" cellspacing="1">
			<tr>
				<td align="right">
					<a href="/addr/pAddrAdd.do">Add Contact</a>
				</td>
			</tr>
			<tr>
				<td align="right">
					<a href="javascript:srchByLeading('all')" class=TM_SORT_SEL>전체</a>&nbsp;|
					<a href="javascript:srchByLeading('ㄱ')" class=>
					ㄱ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㄴ')" class=>
					ㄴ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㄷ')" class=>
					ㄷ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㄹ')" class=>
					ㄹ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅁ')" class=>
					ㅁ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅂ')" class=>
					ㅂ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅅ')" class=>
					ㅅ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅇ')" class=>
					ㅇ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅈ')" class=>
					ㅈ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅊ')" class=>
					ㅊ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅋ')" class=>
					ㅋ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅌ')" class=>
					ㅌ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅍ')" class=>
					ㅍ</a>&nbsp;|
					<a href="javascript:srchByLeading('ㅎ')" class=>
					ㅎ</a>&nbsp;|
					<a href="javascript:srchByLeading('a-z')" class=>a-z</a>&nbsp;|
					<a href="javascript:srchByLeading('0-9')" class=>0-9</a>
				</td>
			</tr>
		</table>
		<table border="1" width="100%" cellspacing="1">
				<tr>
					<th>번호</td>
					<th>이름</td>
					<th>메일주소</td>
					<th>회사</td>
					<th>연락처</td>
				</tr>
			<c:forEach var="member" items="${members}" varStatus="loop">
				<tr>
					<td align="center">${loop.count}</td>					
					<td>${member.memberName}</td>
					<td>${member.memberEmail}</td>
					<td>${member.companyName}</td>
					<td>${member.mobileNo}</td>
				</tr>	
			</c:forEach>
		</table>
	</td>
</form>
</body>