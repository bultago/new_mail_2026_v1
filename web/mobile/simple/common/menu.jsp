<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
<title></title>
</head>
<body text="#000000" bgcolor="#FFFFFF">
<h3 align="center"><font color="black"> <font color="blue" class="font-stretch: extra-expanded">T</font>errace <font color="blue" class="font-stretch: extra-expanded">M</font>ail <font color="blue" class="font-stretch: extra-expanded">S</font>uite</font></h3>
&nbsp;&gt; <tctl:msg bundle="jmail" key="menu.title"/> <br/>
<%String helpURL = "/help/"+locale+"/jmobile.htm";%>
<p>1.&nbsp;<a accesskey="1" href="<%=response.encodeUrl("/jm/maillist.do")%>"><tctl:msg bundle="jmail" key="menu.list"/></a></p>
<p>2.&nbsp;<a accesskey="2" href="<%=response.encodeUrl("/jm/mailwrite.do")%>"><tctl:msg bundle="jmail" key="menu.write"/></a></p>
<p>3.&nbsp;<a accesskey="3" href="<%=response.encodeUrl(helpURL)%>"><tctl:msg bundle="jmail" key="menu.help"/></a></p>
<p>4.&nbsp;<a accesskey="4" href="<%=response.encodeUrl("/jm/logout.do") %>"><tctl:msg bundle="jmail" key="menu.logout"/></a></p>

</body>
</html>