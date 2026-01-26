<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>${mailbox.folderName}</title>
	</head>
	<body>
		<table border="1" width="100%">
			<tr>
				<th>msgUid</th>
				<th>subject</th>
				<th>personName</th>
				<th>personEmail</th>
				<th>sentDate</th>
				<th>size</th>
			</tr>
			<c:forEach var="message" items="${mailbox.listContents}" varStatus="loop">
			<tr>
				<td>${message.id}</td>
				<td>${message.subject}</td>
				<td>${message.name}</td>
				<td>${message.email}</td>
				<td>${message.date}</td>
				<td>${message.size}</td>
			</tr>
			</c:forEach>
		</table>
	</body>
</html>