<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="com.terracetech.tims.hybrid.common.manager.HybridAuthManager"%>
<%@ page import="com.terracetech.tims.webmail.util.StringUtils" %>
<%@ page import="com.terracetech.tims.webmail.util.BrowserUtil"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>

<%
	response.addHeader("Cache-Control","no-store");
	response.addHeader("Pragma", "no-cache");
	String agent = request.getHeader("user-agent");
	agent = agent.toLowerCase();
	
	String version = request.getSession().getServletContext().getInitParameter("version");

	String authKey = HybridAuthManager.checkMobileSso(request);
    User user = null;
    if (StringUtils.isNotEmpty(authKey)) {
    	user = HybridAuthManager.getUser(authKey);
        if (user == null) {
        	user = UserAuthManager.getUser(request);
        }
    } else {
    	user = UserAuthManager.getUser(request);                
    }
    
	String locale = "";
	String STIME_OUT = "0";	
	String skin = "default";
	String cssLocation = "/design/mobile/default";
	String userName = null;
	String userEmail = null;
	
	boolean isMobile = BrowserUtil.isMoblieBrowser(agent);
	if (user != null) {
		userName = user.get(User.USER_NAME);
		userEmail = user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN);
		skin = "default";//(String)request.getAttribute("mobileSkin");
		cssLocation = "/design/mobile/"+skin;
		locale = user.get(User.LOCALE);
		session.setAttribute(I18nConstants.LOCALE_KEY, new Locale(locale));
		
		request.setAttribute("mailUid", user.get(User.MAIL_UID));
		request.setAttribute("userName", userName);
		request.setAttribute("email", userEmail);
		
		Map<String,String> logoInfoMap = UserAuthManager.getLogoInfo(request);		
		if (logoInfoMap != null) {
			request.setAttribute("title", logoInfoMap.get("title"));
			request.setAttribute("copyright", logoInfoMap.get("copyright").replaceAll("!","&"));			
			request.setAttribute("logoUrl", logoInfoMap.get("logoUrl"));
		}		
	}
	else {
		Locale loc = (Locale)request.getLocale(); 
		locale = loc.getLanguage();
		if(locale.indexOf("en") > -1){
			loc = new Locale("en");
			locale = "en";
		}
		session.setAttribute(I18nConstants.LOCALE_KEY, loc);		
	}
	
	if(!"ko".equals(locale) && !"en".equals(locale) && !"ja".equals(locale) && !"jp".equals(locale) && !"cn".equals(locale)) {
        locale = "en";
    }
	
	if("ja".equals(locale)){
		locale = "jp";
	}
	
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%if (BrowserUtil.isMobileOpera(agent)) {
	if(agent.indexOf("9.5") > -1){%>
<meta name="viewport" content="initial-scale=0.75, maximum-scale=0.75, minimum-scale=0.75, user-scalable=no" />
	<%}	else if(agent.indexOf("9.8") > -1){%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<%}%>	
<%}	else {%>
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<%}%>
<meta name="format-detection" content="telephone=no" />
<title>${title}</title>

<link rel="stylesheet" type="text/css" href="/design/common/css/font_<%=locale%>.css"/>
<link rel="stylesheet" type="text/css" href="/design/mobile/css/common.css" />
<link rel="stylesheet" type="text/css" href="/design/mobile/css/layout_blue.css" />
<link rel="stylesheet" type="text/css" href="/design/mobile/css/skin_blue.css" />
<link href="/design/mobile/image/apple.png" rel="apple-touch-icon-precomposed">

<script type="text/javascript" src="/js/core-lib/jquery-1.3.2.js"></script>
<script type="text/javascript" src="/i18n?bundle=common&var=comMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/common-lib/common.js"></script>
<script type="text/javascript" src="/js/common-lib/validate.js"></script>
<script type="text/javascript" src="/hybrid/common/hybrid.js"></script>

<script type="text/javascript" >
var hostInfo = this.location.protocol + "//" + this.location.host;
var myhostInfo = this.location.protocol + "//127.0.0.1"; //tmp
var LOCALE = "<%=locale%>";
var pageCategory = "";
var authKey = "";
function excuteAction(type){	
	var location = "";
	if(type == "logout"){
		location = "/common/logout.do";
	} else if(type == "home"){
		location ="/hybrid/common/home.do";
	} else if(type == "mail"){
		location ="/hybrid/mail/mailList.do";
	} else if(type == "addr"){
		location ="/hybrid/addr/privateAddrList.do";
	} else if(type == "calendar"){
		location ="/hybrid/calendar/monthCalendar.do";
	} else if(type == "bbs"){
		location ="/hybrid/bbs/bbsList.do";
	} 	
	if(pageCategory == "write"){
		var f = document.writeForm;	
		if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') ||	trim(f.subject.value) != "" || trim(f.content.value) != ""){					
				if(!confirm('<tctl:msg key="confirm.escapewrite"/>')){
					return;
				}
		}
	} 
	
	if(pageCategory == "writeCalendar"){ 
		var titleObj = jQuery("#title");
		var locationObj = jQuery("#location");
		var contentObj = jQuery("#content");
		if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" ){
			if(!confirm('<tctl:msg key="confirm.calendarEscapewrite"/>')){
				return;
			}
		}
	}
	
	if(pageCategory == "writeAsset"){ 
		var contectObj = jQuery("#contect");
		var titleObj = jQuery("#title");
		var locationObj = jQuery("#location");
		var contentObj = jQuery("#content");
		if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" || contectObj.val() != ""){
			if(!confirm('<tctl:msg key="confirm.calendarEscapewrite"/>')){
				return;
			}
		}
	}
	
	if(pageCategory == "writeBBS"){
		var titleObj = jQuery("#subject");
		var contentObj = jQuery("#contentText");
		if(titleObj.val() != "" || contentObj.val() != "" ){
			if(!confirm('<tctl:msg key="confirm.BBSEscapewrite"/>')){
				return;
			}
		}
	}
	
	if(pageCategory == "addAddrForm"){
		var name = jQuery("#mName");
		var email = jQuery("#mEmail");
		if(name.val() != "" || email.val() != "" ){
			if(!confirm('<tctl:msg key="confirm.addressEscapewrite"/>')){
				return;
			}
		}
		
	}
	
	document.location = location;
}

function chageMailMode(mailMode) {
	if(pageCategory == "write"){
		var f = document.writeForm;	
		if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){					
				if(!confirm('<tctl:msg key="confirm.escapewrite"/>')){
					return;
				}
		}
	} 
	this.location = "/hybrid/common/changeMailMode.do?mailMode="+mailMode;
}

function setTitleBar(title){
	document.title=title;
}

jQuery().ready(function(){
    setTimeout(function() { window.scrollTo(0, 1) }, 100);
    sessionHealthCheck();
});
function sessionHealthCheck() {
    var param = {};
    jQuery.post("/common/sessionHealthCheck.do", param, sessionHealthCheckResult, "json");
}
function sessionHealthCheckResult(data) {
    var isDuplicateCheckOn = data.isDuplicateCheckOn;
    var isUserLogin = data.isUserLogin;
    if(!isDuplicateCheckOn){
             return ;
    }
    
    if (isUserLogin) {
    	alert(comMsg.auth_fail_userexistout);
        document.location = "/common/logout.do";
        
    }
}
</script>