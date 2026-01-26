<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.terracetech.tims.webmail.util.BrowserUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd"> 
<html>
<head>
<title></title>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon"/>
<script type="text/javascript">
function setTitleBar(title){
	document.title=title;
}
</script>
</head>
<%
String agent = request.getHeader("user-agent");
boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
boolean isJMobile = BrowserUtil.isJPMobile(agent);
%>
<% if (isMobile && !isJMobile) {%>
<META HTTP-EQUIV=refresh CONTENT='0; URL=/common/welcome.do'>
<%} else if (isJMobile) {
response.sendRedirect("/jm/welcome.do");
} else {%>
<frameset rows='*' cols='*' border='0'>
<frame src='/common/welcome.do' scrolling='auto'>
</frameset>
<%} %>
</html>

