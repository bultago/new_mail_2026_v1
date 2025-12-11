<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.jmobile.common.manager.UserAuthManager"%>
<%@ page import="com.terracetech.tims.webmail.util.StringUtils"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Locale"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>
<%
	User user = UserAuthManager.getUser(request);
	String locale = "";
	
	if (user != null) {
		locale = user.get(User.LOCALE);		
	} else {
		locale = (String)request.getAttribute("loginLocale");
		locale = StringUtils.isEmpty(locale) ? "jp" : locale;
	}
	locale = StringEscapeUtils.escapeHtml(locale);	
	session.setAttribute(I18nConstants.LOCALE_KEY, new Locale(locale));
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="format-detection" content="telephone=no">
<title>Terrace Mail Suite</title>
