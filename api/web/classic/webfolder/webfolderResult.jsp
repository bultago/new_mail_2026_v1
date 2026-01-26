<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/common-lib/common-base64.js"></script>
<script type="text/javascript">
function init() {
	var msg = "${msg}";
	if (msg != "") {
		alert("${msg}");
	}
	var url = "/webfolder/listFolders.do?";
	url += "path="+Base64.encode("${path}");
	url += "&userSeq=${userSeq}";
	url += "&type=${type}";
	url += "&nodeNum=" + encodeURI("${nodeNum}");
	url += "&sroot="+Base64.encode("${sroot}");

	this.location = url;
}
</script>
</head>
<body onload="init()"></body>
</html>