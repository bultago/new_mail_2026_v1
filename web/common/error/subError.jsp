<%@page import="java.util.Date"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%
DateFormat timeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
String strDate = timeFormatter.format(new Date());
String exceptionMsg = null;

Object exception = ActionContext.getContext().getValueStack().findValue("exception");
if (exception instanceof RuntimeException){
	System.out.print("[JSP EXCEPTION] subError ### ");
    System.out.println(strDate);
    RuntimeException e = (RuntimeException)exception;
    System.out.println(e.getMessage());
    e.printStackTrace();
    exceptionMsg = e.getMessage();
}
%>
<div  style='text-align: center;height:97%;' class='TM_mail_content'>
	<div class="TM_errorContentWrapper">
		<div class="TM_errorContentMain">				
			<tctl:msg key="error.msg.001" bundle="common"/>
		</div>
		<div class="TM_errorContentSub">
			<tctl:msg key="error.msg.002" bundle="common"/><br>
			<tctl:msg key="error.msg.003" bundle="common"/>
		</div>
	</div>
</div>
<div style="display:none">
<%= strDate %>
<%= exceptionMsg %>
</div>