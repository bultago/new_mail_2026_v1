<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ page import="com.terracetech.tims.webmail.util.BrowserUtil"%>
<%

String agent = request.getHeader("user-agent");
boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
%>
<html>
<script type="text/javascript">

var cookieDomain = "${cookieDomain}";

function cookie_delete(name){
	
	if (document.cookie.indexOf(name) < 0) {
		return 0;
	}

    var expireTime = new Date();
    expireTime.setTime(expireTime.getTime() - expireTime.getTime());

    if (cookieDomain != ""){
    	var pstr = name + "=" + null +
        	("; expires=" + expireTime.toGMTString()) +
        	("; path=" + "/") +
        	("; domain=" + cookieDomain + ";")
        	;
    } else {
    	var pstr = name + "=" + null +
        	("; expires=" + expireTime.toGMTString()) +
        	("; path=" + "/" + ";")
        	;
    }

    document.cookie = pstr;
}

function start() {
	var isMobile = <%=isMobile%>;
	var hostInfo = this.location.protocol + "//" + this.location.host;
	var cookieObj = ${cookieNameObj};
	var cookieNames = cookieObj.cookies;
	for(var i = 0 ; i < cookieNames.length ; i++){		
		cookie_delete(cookieNames[i]);
	}
	var logoutPath = "${logoutPath}";
	var path = "";
	if (logoutPath != "") {
		if (logoutPath.indexOf('http://') != -1) {
			path = logoutPath;
		} else {
			path = hostInfo+logoutPath;
		}
	} else {
		if(!isMobile){
			path = hostInfo +"/common/welcome.do?timeout=${fn:escapeXml(timeout)}&stime=${fn:escapeXml(stime)}&language=${language}";
		} else {
			path = hostInfo +"/mobile/common/welcome.do";
		}
	}	
	this.location = path;
}

</script>
<body onLoad='start()'>
</body>
</html>
<% session.invalidate(); %>