<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.terracetech.tims.common.I18nConstants" %>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<%
session.setAttribute(I18nConstants.LOCALE_KEY, request.getLocale());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<SCRIPT type="text/javascript" src="/i18n?bundle=mail&var=mailMsg"></SCRIPT>
</head>
<body>

<h1>WelcomePage!!!</h1>
<br>
path : ${info.path} <br>
path : ${info.locale} <br>
path : ${info.encoding} <br>

<tctl:msg key="mail.001"/>

<SCRIPT type="text/javascript">
	alert(mailMsg.mail_001);
</SCRIPT>

</body>
</html>