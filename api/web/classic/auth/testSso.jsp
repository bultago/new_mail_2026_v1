<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
String target = request.getParameter("target");
String portStr = request.getParameter("port");

String empno = "654321";

//TODO 해당 서버에 테스트 sso 연동 확인 success 메세지로 확인
String returnValue = "success";

UserAuthManager userManager = TerraceBeans.getBean();






%>
</body>
</html>