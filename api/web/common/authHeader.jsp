<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>

<%
	response.addHeader("Cache-Control","no-store");
	response.addHeader("Pragma", "no-cache");
	
	User user = UserAuthManager.getUser(request);
	
	if (user != null) {
		
		String authStatus = (String)session.getAttribute("authStatus");
		if(authStatus != null && "logout".equals(authStatus)){
			response.sendRedirect("/common/logout.do");
		}
	}
%>
