<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
</head>
<body text="#000000" bgcolor="#FFFFFF">
<h3 align="center"><tctl:msg bundle="jmail" key="mail.read.title"/></h3>
<p>
	<form method="post" action="<%=response.encodeUrl("/jm/maillist.do")%>">
	&nbsp;&gt;
	<select name="folderName">
		<c:forEach var="dfolder" items="${defaultFolders}">
		<c:if test="${!(dfolder.fullName  eq 'Reserved')}">
		<option value="${dfolder.fullName}" <c:if test="${dfolder.fullName eq listInfoVo.folderName}">selected</c:if>>
			${dfolder.name}
			<c:if test="${!(dfolder.fullName  eq 'Sent') && dfolder.unseenCnt > 0}">
			(${dfolder.unseenCnt})
			</c:if>
		</option>
		</c:if>
		</c:forEach>
		<c:forEach var="ufolder" items="${userFolders}">
		<c:if test="${ufolder.fullName eq listInfoVo.folderName}">
			<option value="${ufolder.fullName}" selected>${ufolder.name}</option>
			<c:if test="${ufolder.unseenCnt > 0}">
			(${ufolder.unseenCnt})
			</c:if>
		</c:if>
		</c:forEach>
		<option value="etc"><tctl:msg bundle="jmail" key="mail.folder.etc"/></option>
	</select>
	<input type="submit" name="sub_act_select" value="<tctl:msg bundle="jmail" key="mail.select"/>"/>
	</form>
</p>

<form method="post" action="<%=response.encodeUrl("/jm/mailwork.do")%>">
<c:forEach items="${listInfoVo.listContents}" var="msg" varStatus="idx">
<p>              
<input type="checkbox" value="${msg.id}" name="uid">
 
<c:choose>
	<c:when test="${fn:containsIgnoreCase(msg.flag,'S')}">[<tctl:msg bundle="jmail" key="mail.flag.seen"/>]</c:when>
	<c:otherwise>[<tctl:msg bundle="jmail" key="mail.flag.unseen"/>]</c:otherwise>
</c:choose>
<%--
<c:if test="${fn:containsIgnoreCase(msg.flag,'T')}"><img src="/design/default/image/icon/ic_mail_file.gif"/></c:if>
--%>
<a href="<%=response.encodeUrl("/jm/mailread.do")%>?folderName=${listInfoVo.folderName}&uid=${msg.id}">
	<c:if test="${not empty msg.subject}">
	<c:if test="${fn:length(msg.subject) > 13}">
		${fn:substring(msg.subject,0,11)}...
	</c:if>
	<c:if test="${fn:length(msg.subject) <= 13}">
		${msg.subject}
	</c:if>
	</c:if>
	<c:if test="${empty msg.subject}">
		<tctl:msg key="mail.nomessage"/>
	</c:if>
</a><br>
<c:if test="${not empty msg.email}">
<c:if test="${fn:length(msg.email) > 20}">
	${fn:substring(msg.email,0,18)}...
</c:if>
<c:if test="${fn:length(msg.email) <= 20}">
	${msg.email}
</c:if>
</c:if>
<br>              
<c:if test="${!empty msg.date}">	
	<fmt:parseDate var="parseDate" value="${msg.date}" pattern="yyyyMMdd HH:mm:ss" parseLocale="en_US"/>	
	<c:if test="${fn:contains(msg.date, thisDate)}">		
		${fn:substring(msg.date,9,17)}
	</c:if>
	<c:if test="${!fn:contains(msg.date, thisDate) && fn:contains(msg.date, fn:substring(thisDate,0,4))}">
		${fn:substring(msg.date,4,6)}/${fn:substring(msg.date,6,8)}
	</c:if>
	<c:if test="${!fn:contains(msg.date, fn:substring(thisDate,0,4))}">
		${fn:substring(msg.date,3,5)}/${fn:substring(msg.date,4,6)}/${fn:substring(msg.date,6,8)}
	</c:if>
</c:if>
</p>
</c:forEach>
<input type="hidden" name="folderName" value="${listInfoVo.folderName}">
<input type="submit" name="seenAction" value="<tctl:msg bundle="jmail" key="mail.flag.btn.seen"/>"/>
<input type="submit" name="unseenAction" value="<tctl:msg bundle="jmail" key="mail.flag.btn.unseen"/>"/><br>
<input type="submit" name="deleteAction" value="<tctl:msg bundle="jmail" key="mail.delete"/>"/>
<input type="submit" name="moveSelect" value="<tctl:msg bundle="jmail" key="mail.move"/>"/>
</form>

<c:if test="${!pm.firstPage}">
4. <a accesskey="4" href="<%=response.encodeUrl("/jm/maillist.do")%>?folderName=${listInfoVo.folderName}&keyWord=<tctl:encodeURL>${keyWord}</tctl:encodeURL>&page=${pm.prevPage}"><tctl:msg bundle="jmail" key="mail.listpre"/></a>
</c:if>
<c:if test="${!pm.lastPage}">
6. <a accesskey="6" href="<%=response.encodeUrl("/jm/maillist.do")%>?folderName=${listInfoVo.folderName}&keyWord=<tctl:encodeURL>${keyWord}</tctl:encodeURL>&page=${pm.nextPage}"><tctl:msg bundle="jmail" key="mail.listnext"/></a>
</c:if>
<br>
<form method="post" action="<%=response.encodeUrl("/jm/maillist.do")%>">
<input type="hidden" name="folderName" value="${listInfoVo.folderName}"></input> 
<input type="text" name="keyWord" istyle="2" style="width:60%;" autocomplete="off" value="${keyWord}">
<input type="submit" value="<tctl:msg bundle="jmail" key="mail.search"/>"/> 
</form>

<%@include file="/mobile/simple/common/bottommenu.jsp" %>
</body>
</html>