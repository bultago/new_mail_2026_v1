<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/mobile/simple/common/header.jsp" %>
</head>
<body text="#000000" bgcolor="#FFFFFF">

<h3 align="center"><font color="black"> <font color="blue" class="font-stretch: extra-expanded">T</font>errace <font color="blue" class="font-stretch: extra-expanded">M</font>ail <font color="blue" class="font-stretch: extra-expanded">S</font>uite</font></h3>
&nbsp;&gt; <tctl:msg bundle="jmail" key="login.loc"/> <br/>
<p align="center"><tctl:msg bundle="jmail" key="login.title"/><br></p>

<form name="login" method="POST" action="<%=response.encodeURL("/jm/login.do")%>">  
  <input type="hidden" name="language" value="<%=locale%>">
  <input type="hidden" name="charset" value="UTF-8">
  <input type="hidden" name="mobile_pagetype" value="html">
	&nbsp;<tctl:msg bundle="jmail" key="login.email"/>:<br>
	&nbsp;<input type="text" name="email" istyle="2" maxlength="30" style="ime-mode: disabled;width:80%;" autocomplete="off" value="">		
	<br>
	&nbsp;<tctl:msg bundle="jmail" key="login.pass"/> :<br>
	&nbsp;<input type="password" name="pass" maxlength="50" style="ime-mode: disabled;width:80%" autocomplete="off" value=""><br>
    <p align="center">
      <input type="submit" name="login" value="<tctl:msg bundle="jmail" key="login.btn"/>">
    </p>
</form>
<p align="center">
	<%if(locale.equals("jp")){%>
	<a href="/jm/welcome.do?loginLocale=en">English</a>
	<%}else if(locale.equals("en")){%>
	<a href="/jm/welcome.do?loginLocale=jp">Japanese</a>
	<%}%>
</p>

<p align="center">
  <hr align="center" size="1" width="95%" noshade style="border-color:#6F6FAF;">
  <div align="center"><font color="#7F7F7F">(C) 2010 DAOU Tech. Inc.</font></div>
</p>


</body>
</html>