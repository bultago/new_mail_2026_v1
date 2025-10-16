<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="com.terracetech.tims.webmail.common.ExtPartConstants"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>

<%
	response.addHeader("Cache-Control","no-store");
	response.addHeader("Pragma", "no-cache");
	String agent = request.getHeader("user-agent");
	
	boolean isMsie = (agent != null && agent.toUpperCase().indexOf("MSIE") > 0)?true : false;
	User user = UserAuthManager.getUser(request);
	String locale = "";
	String skin = "default";
	String cssLocation = "/design/default";
	boolean activeXMake = false;
	boolean activeXUse = false;
	
	if (user != null) {
		skin = (String)request.getAttribute("skin");
		cssLocation = "/design/"+skin;
		locale = user.get(User.LOCALE);
		activeXMake = "on".equals(user.get(User.USE_ACTIVE_X));
		activeXUse = "T".equals(user.get(User.ACTIVE_X));
		
		request.setAttribute("mailDomainSeq", user.get(User.MAIL_DOMAIN_SEQ));
		request.setAttribute("mailUserSeq", user.get(User.MAIL_USER_SEQ));
		request.setAttribute("mailUid", user.get(User.MAIL_UID));
		request.setAttribute("userName", user.get(User.USER_NAME));
		request.setAttribute("email", user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN));
	}
	else {
		Locale loc = (Locale)request.getLocale(); 
		locale = loc.getLanguage();
		if(locale.indexOf("en") > -1){
			loc = new Locale("en");
			locale = "en";
		}
	}
	
	if(!"ko".equals(locale) && !"en".equals(locale) && !"jp".equals(locale)) {
        locale = "en";
    }
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="/design/common/css/font_<%=locale%>.css"/>
<link rel="stylesheet" type="text/css" href="/design/common/css/common.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/layout.css"/>

<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.core.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.tabs.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.resizable.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.dialog.css" />

<script type="text/javascript" src="/dwr/engine.js"> </script>
<script type="text/javascript" src="/dwr/util.js"> </script>

<script type="text/javascript" src="/js/core-lib/prototype.js"></script>
<script type="text/javascript" src="/js/core-lib/jquery-1.3.2.js"></script>
<script type="text/javascript" src="/js/common-lib/common.js"></script>
<script type="text/javascript" src="/js/common-lib/common-base64.js"></script>
<script type="text/javascript" src="/js/common-lib/common-editor.js"></script>
<script type="text/javascript" src="/js/common-lib/validate.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ocx/ocx_load.js"></script>
<script type="text/javascript" src="/js/ext-lib/jcookie.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.history.js"></script>
<script type="text/javascript" src="/js/ext-lib/noclick.js"></script>

<script type="text/javascript" src="/i18n?bundle=common&var=comMsg&locale=<%=locale%>"></script>


<tctl:extModuleCheck moduleName="ckkey" msie="true">
<script type="text/javascript" src="<%=ExtPartConstants.getCkKeyProUrl()%>"></script>
</tctl:extModuleCheck>
<tctl:extModuleCheck moduleName="ckweb" msie="true">
<script type="text/javascript" src="<%=ExtPartConstants.getCkWebProUrl()%>"></script>
</tctl:extModuleCheck>
<tctl:extModuleCheck moduleName="xecureWeb" msie="true">
<script type="text/javascript" src="/XecureObject/xecureweb.js"></script>
</tctl:extModuleCheck>

<script type="text/javascript" >
var hostInfo = this.location.protocol + "//" + this.location.host;
var myhostInfo = this.location.protocol + "//127.0.0.1"; //tmp
var LOCALE = "<%=locale%>";
var activeXMake = <%=activeXMake%>;
var activeXUse = <%=activeXUse%>;

var isDwrLoad = false;
dwr.engine.setPreHook(function() {
	//if(!PageMainLoadingManager.isWork){
		isDwrLoad = true;	
		jQuery("#mainLoaddingMessage").show();
			
		var msgFrame = jQuery("#mainLoadMessage");		
		var bHeight = jQuery(window).height();		
		var bWidth = jQuery(window).width();
		
		msgFrame.css("top",(bHeight/2 - msgFrame.height()/2)+"px");
		msgFrame.css("left",(bWidth/2 - msgFrame.width()/2)+"px");		
		msgFrame.show();
	//}				
});
dwr.engine.setPostHook(function() {
	jQuery("#mainLoaddingMessage").hide();
	setTimeout(function(){isDwrLoad = false;},100);
});


function errorHandler (errorString, exception) {
	var msg = "";
	var buf = "<b>" + exception.javaClassName + "</b><br>";
    var st = exception.stackTrace;

    if (st && st.length > 0) {
        for (var i = 0; i < 5 && i < st.length; i++) {
            buf += st[i].fileName + ":" + st[i].lineNumber + " " +
                st[i].methodName + "()<br>";
        }
    }
    
    if(errorString && errorString != "" && errorString.length > 0){       
    	msg = errorString;
    } else {
    	msg = comMsg.error_default;
    } 
    alert(msg);    
}

dwr.engine.setErrorHandler(errorHandler);
</script>


