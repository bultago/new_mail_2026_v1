<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${mail.subject}</title>
</head>
<body>
	<table border="1" width="100%">
	<tr>
		<th>
			folder
		</th>
		<td>
			${mail.folderName}
		</td>
	</tr>
	<tr>
		<th>
			sentDate
		</th>
		<td>
			${mail.date}
		</td>
	</tr>
	<tr>
		<th>
			from
		</th>
		<td>
			${mail.from.personal} (${mail.from.address})
		</td>
	</tr>
	<tr>
		<th>
			to
		</th>
		<td>
			<c:forEach var="to" items="${mail.tos}" varStatus="loop">
				${to.personal} (${to.address}),
			</c:forEach>
		</td>
	</tr>
	<tr>
		<th>
			cc
		</th>
		<td>
			<c:forEach var="cc" items="${mail.ccs}" varStatus="loop">
				${cc.personal} (${cc.address}),
			</c:forEach>
		</td>
	</tr>
	<tr>
		<th>
			contents
		</th>
		<td>
			${mail.contents}
		</td>
	</tr>
	</table>
</body>
</html>