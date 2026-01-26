<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
<title></title>
</head>
<body text="#000000" bgcolor="#FFFFFF">
<form method="post" action="<%=response.encodeUrl("/jm/mailread.do") %>">
<input type="hidden" name="folderName" value="${folderName}"/>
<input type="hidden" name="uid" value="${uid}"/>
<input type="hidden" name="keyWord" value="${keyWord}"/>
<input type="hidden" name="cpage" value="${contentsCurrentPage}"/>

<h2 align="center"><tctl:msg key="mail.read.title" bundle="jmail"/></h2>
<c:if test="${not empty contentsTotalPage}">
<p align="right"><tctl:msg key="mail.read.page" bundle="jmail"/> ${contentsCurrentPage}/${contentsTotalPage}</p>
</c:if>

<p><tctl:msg key="mail.read.subject" bundle="jmail"/> : ${fn:escapeXml(viewContents.subject)}</p>
<p><tctl:msg key="mail.read.from" bundle="jmail"/> : ${fn:escapeXml(viewContents.from.emailAddress)}</p>
<p>
<tctl:msg key="mail.read.date" bundle="jmail"/> : 
<c:if test="${userLocale == 'ko'}">
<fmt:setLocale value="ko"/>
</c:if>
<c:if test="${userLocale == 'en'}">
<fmt:setLocale value="en"/>
</c:if>
<c:if test="${userLocale == 'jp'}">
<fmt:setLocale value="ja"/>
</c:if>
<fmt:parseDate var="rdate" pattern="yyyyMMdd HH:mm:ss" value="${viewContents.date}"/>
<fmt:formatDate value="${rdate}" pattern="yyyy/MM/dd EEEE a h:mm:ss"/>
</p>
<p>
<tctl:msg key="mail.read.to" bundle="jmail"/> : 
<c:forEach var="iaddr" items="${viewContents.tos}" varStatus="idx">
<c:if test="${idx.index > 0}">,&nbsp;</c:if>${fn:escapeXml(iaddr.emailAddress)}									
</c:forEach>	
</p>
<c:if test="${not empty viewContents.ccs}">
<p>
<tctl:msg key="mail.read.cc" bundle="jmail"/> : 
<c:forEach var="iaddr" items="${viewContents.ccs}" varStatus="idx">
<c:if test="${idx.index > 0}">,&nbsp;</c:if>${fn:escapeXml(iaddr.emailAddress)}									
</c:forEach>	
</p>
</c:if>

<hr>
<p style="padding:5px;" style="word-wrap: break-word; word-break: break-all;">
${viewContents.textContents}
</p>

<c:if test="${not empty contentsTotalPage}">
<p>
<c:if test="${contentsCurrentPage != 1}">
<input type="submit" name="readContentPageUp" value="Up"/>
</c:if>
<c:if test="${contentsCurrentPage != contentsTotalPage}">
<input type="submit" name="readContentPageDown" value="Down"/>
</c:if>
</p>
</c:if>

<hr>
<c:if test="${viewContents.attachsCnt == 0}">
	<tctl:msg key="mail.noattach" />
</c:if>
<c:if test="${viewContents.attachsCnt > 0}">
<c:forEach var="fileData" items="${viewContents.attachs}" varStatus="loop">
	<a href="<%=response.encodeURL("/jm/attachdown.do")%>?uid=${viewContents.uid}&folderName=${viewContents.folderEncName}&depth=${fileData.depth}">${fileData.name}</a>
</c:forEach>
</c:if>
</form>

<c:if test="${!empty viewContents.preUid && viewContents.preUid > 0}">
4. <a accesskey="4" href="<%=response.encodeUrl("/jm/mailread.do")%>?folderName=${folderName}&keyWord=<tctl:encodeURL>${keyWord}</tctl:encodeURL>&uid=${viewContents.preUid}"><tctl:msg bundle="jmail" key="mail.listpre"/></a>
</c:if>
5. <a accesskey="5" href="<%=response.encodeUrl("/jm/prework.do")%>"><tctl:msg bundle="jmail" key="mail.list"/></a>
<c:if test="${!empty viewContents.nextUid && viewContents.nextUid > 0}">
6. <a accesskey="6" href="<%=response.encodeUrl("/jm/mailread.do")%>?folderName=${folderName}&keyWord=<tctl:encodeURL>${keyWord}</tctl:encodeURL>&uid=${viewContents.nextUid}"><tctl:msg bundle="jmail" key="mail.listnext"/></a>
</c:if>
<hr>
<form method="post" action="<%=response.encodeUrl("/jm/mailwork.do") %>">
<input type="hidden" name="folderName" value="${folderName}"/>
<input type="hidden" name="uid" value="${uid}"/>
<input type="submit" name="reply" value="<tctl:msg key="mail.reply" bundle="jmail"/>"/>
<input type="submit" name="replyall" value="<tctl:msg key="mail.replyall" bundle="jmail"/>"/>
<input type="submit" name="forward" value="<tctl:msg key="mail.forward" bundle="jmail"/>"/><br>

<input type="submit" name="deleteAction" value="<tctl:msg key="mail.delete" bundle="jmail"/>"/>
<input type="submit" name="moveSelect" value="<tctl:msg key="mail.move" bundle="jmail"/>"/>
</form>

<%@include file="/mobile/simple/common/bottommenu.jsp" %>
</body>
</html>