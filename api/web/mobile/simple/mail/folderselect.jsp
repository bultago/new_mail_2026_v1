<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
<title></title>
</head>
<body text="#000000" bgcolor="#FFFFFF">
<h3 align="center"><tctl:msg bundle="jmail" key="folder.title"/></h3>
<p style="padding-left:10px;">
<c:forEach var="dfolder" items="${defaultFolders}">
<c:if test="${action eq 'list'}">
<c:if test="${!(dfolder.fullName  eq 'Reserved')}">
	<a href="<%=response.encodeUrl("/jm/maillist.do")%>?folderName=${dfolder.fullName}">
	${dfolder.name}
	<c:if test="${!(dfolder.fullName  eq 'Sent') && dfolder.unseenCnt > 0}">
	(${dfolder.unseenCnt})
	</c:if>		
	</a><br>
</c:if>
</c:if>
<c:if test="${action eq 'move'}">
<c:if test="${!(dfolder.fullName  eq 'Reserved') && 
			!(dfolder.fullName  eq 'Sent')&&
			!(dfolder.fullName  eq 'Drafts')}">
	
	<a href="<%=response.encodeUrl("/jm/mailwork.do")%>?targetFolder=${dfolder.fullName}&uidStr=${fn:escapeXml(uids)}&moveAction=true&folderName=<tctl:encodeURL>${fromFolderName}</tctl:encodeURL>">
	${dfolder.name}
	<c:if test="${!(dfolder.fullName  eq 'Sent') && dfolder.unseenCnt > 0}">
	(${dfolder.unseenCnt})
	</c:if>		
	</a><br>
</c:if>
</c:if>
</c:forEach>
</p>
<br>
<p style="padding-left:10px;"><tctl:msg bundle="jmail" key="folder.personal"/></p>
<p style="padding-left:10px;">
<c:forEach var="ufolder" items="${userFolders}">
	<c:if test="${action eq 'list'}">
	<a href="<%=response.encodeUrl("/jm/maillist.do")%>?folderName=<tctl:encodeURL>${ufolder.fullName}</tctl:encodeURL>">
	</c:if>
	<c:if test="${action eq 'move'}">
	<a href="<%=response.encodeUrl("/jm/mailwork.do")%>?targetFolder=<tctl:encodeURL>${ufolder.fullName}</tctl:encodeURL>&uidStr=${fn:escapeXml(uids)}&moveAction=true&folderName=<tctl:encodeURL>${fromFolderName}</tctl:encodeURL>">
	</c:if>
		${ufolder.name}
		<c:if test="${ufolder.unseenCnt > 0}">
		(${ufolder.unseenCnt})
		</c:if>
	</a><br>
</c:forEach>
</p>

<hr align="center" size="1" width="95%" noshade style="border-color:#6F6FAF;">
<%@include file="/mobile/simple/common/bottommenu.jsp" %>
</body>
</html>