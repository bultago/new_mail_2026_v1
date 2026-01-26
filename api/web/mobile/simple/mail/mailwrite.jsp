<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
<title></title>
</head>
<body text="#000000" bgcolor="#FFFFFF">
<h3 align="center"><tctl:msg key="mail.write.title" bundle="jmail"/></h3>
<p></p>
<form method="post" action="<%=response.encodeUrl("/jm/mailsend.do")%>">
<p>
<tctl:msg key="mail.read.to" bundle="jmail"/> :
<input type="text" name="to" istyle="3" style="width:90%" value="${fn:escapeXml(writeContent.to)}"><br>
</p>

<p>
<tctl:msg key="mail.read.cc" bundle="jmail"/> :
<input type="text" name="cc" istyle="3" style="width:90%" value="${fn:escapeXml(writeContent.cc)}"><br>
</p>

<p>
<tctl:msg key="mail.read.bcc" bundle="jmail"/> :
<input type="text" name="bcc" istyle="3" style="width:90%" value="${fn:escapeXml(writeContent.bcc)}"><br>
</p>

<p>
<tctl:msg key="mail.read.subject" bundle="jmail"/> :
<input type="text" name="subject" istyle="3" style="width:90%" value="${fn:escapeXml(writeContent.subject)}"><br>
</p>

<p>
<tctl:msg key="mail.write.text" bundle="jmail"/> :
<textarea rows="5" name="content" style="width:100%">${writeContent.textNormalContent}</textarea>
</p>

<input type="submit" name="send" value="<tctl:msg key="mail.write.send" bundle="jmail"/>"/>
<input type="submit" name="sendDraft" value="<tctl:msg key="mail.write.senddraft" bundle="jmail"/>"/>

</form>

<%@include file="/mobile/simple/common/bottommenu.jsp" %>
</body>
</html>