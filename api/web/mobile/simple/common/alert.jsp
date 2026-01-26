<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
<title></title>
</head>
<body text="#000000" bgcolor="#FFFFFF">


<h2 align="center">
<c:if test="${not empty alerttitle}">${alerttitle}</c:if>
<c:if test="${empty alerttitle}"><tctl:msg key="error.title" bundle="jmail"/></c:if>
</h2>
<p align="center">
${alertmsg}
<c:if test="${empty alertmsg}"><tctl:msg key="error.systemerror" bundle="jmail"/></c:if>
</p>
<br/>
<br/>
<p align="center">
<a accesskey="5" href="<%=response.encodeUrl("/jm/prework.do") %>"><tctl:msg key="menu.back" bundle="jmail"/></a>
<a href="<%=response.encodeURL("/jm/welcome.do")%>"><tctl:msg key="menu.gomenu" bundle="jmail"/></a>
</p>
</body>
</html>