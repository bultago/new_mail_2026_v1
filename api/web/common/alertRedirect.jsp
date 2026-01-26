<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@include file="/common/authHeader.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	alert("${fn:escapeXml(msg)}");
	var url = "${url}";
	var paramUrl = "${param.url}";
	this.location = (url != "")?url:paramUrl;
</script>
</head>
<body></body>
</html>