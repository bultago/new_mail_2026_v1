<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<html>
	<head>
		<title>sendResult</title>
	</head>
	<body>
		<table cellpadding="0" cellspacing="0" width="600" align="center" border="1">
			<tr>
			<td align="center">
				<c:if test="${!sendResult.errorOccur}">
					Send Success
				</c:if>
				<c:if test="${sendResult.errorOccur}">
					Send Fail
				</c:if>
			</td>
			</tr>
			<tr><td>Send Email Addresses</td></tr>
			<tr><td>
				${fn:escapeXml(sendResult.sendAddrs)}
			</td></tr>
			<c:if test="${not empty sendResult.invalidAddrs}">
			<tr><td>Invalid Email Addresses</td></tr>
			<tr><td>
				${fn:escapeXml(sendResult.invalidAddrs)}
			</td></tr>
			</c:if>			
		</table>
		<input type="hidden" value="${sendResult.messageId}"/>	
	</body>
</html>