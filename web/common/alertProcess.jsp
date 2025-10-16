<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	alert("${errMsg}");
	var action = "${actionType}";
	if(action == "close"){
		window.close();
	}
	
</script>
</head>
<body></body>
</html>