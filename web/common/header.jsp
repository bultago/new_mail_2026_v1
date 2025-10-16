<%@page import="com.terracetech.tims.webmail.util.SessionUtil"%>
<%@ page import="com.terracetech.tims.common.I18nConstants"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.User"%>
<%@ page import="com.terracetech.tims.webmail.mailuser.manager.UserAuthManager"%>
<%@ page import="com.terracetech.tims.webmail.common.EnvConstants"%>
<%@ page import="com.terracetech.tims.webmail.common.ExtPartConstants"%>
<%@ page import="com.terracetech.tims.webmail.util.StringUtils"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>

<%!
static boolean isMsie(String agent) {
	if(agent == null) return false;
	if(agent.indexOf("Trident") != -1 || agent.indexOf("MSIE") != -1){
		return true;
	}else {
		return false;
	}
}
%>
<%
	response.addHeader("Cache-Control","no-store");
	response.addHeader("Pragma", "no-cache");
	String agent = request.getHeader("user-agent");
	Object pdebug = getServletContext().getAttribute("PDEBUG");
	String sessionID = request.getSession().getId();
	boolean isMsie = isMsie(agent);
	boolean isMinify = "true".equalsIgnoreCase(EnvConstants.getMailSetting("js.minify"));
	boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
	String version = request.getSession().getServletContext().getInitParameter("version");
	User user = UserAuthManager.getUser(request);
	String locale = "";
	String USER_ID = "";
	String USER_EMAIL = "";
	String USER_PAGEBASE = "";
	String USER_NAME = "";
	String STIME_OUT = "0";	
	String skin = "default";
	String cssLocation = "/design/default";
	String tmenuUse = "enable";
	String lmenuUse = "disable";
	String debugUse = "disable";
	String menuStatus = "{}";
	String renderMode = "ajax";
	boolean activeXMake = false;
	boolean activeXUse = false;
	boolean isPDebug = false;
	boolean noteUse = false;
	
	if (user != null) {
		skin = (String)request.getAttribute("skin");
		skin = (StringUtils.isEmpty(skin)) ? "default" : skin;
		menuStatus = (String)request.getAttribute("menuStatus");		
		cssLocation = "/design/"+skin;
		locale = user.get(User.LOCALE);
		session.setAttribute(I18nConstants.LOCALE_KEY, new Locale(locale));
		
		//XXS 취약점 보완
		String authStatus = (String)session.getAttribute("authStatus");
		
		if(authStatus != null && "logout".equals(authStatus)){
			response.sendRedirect("/common/logout.do?language="+locale);
		}
		
		Object val = session.getAttribute("topMenuUse");
		tmenuUse = (val != null)?(String)val:"enable";
		val = session.getAttribute("leftMenuUse");
		lmenuUse = (val != null)?(String)val:"disable";
		val = session.getAttribute("debugUse");
		debugUse  = (val != null)?(String)val:"disable";
		
		if(pdebug != null)isPDebug = "enable".equals((String)pdebug);		
		
		USER_ID = user.get(User.MAIL_UID);
		USER_EMAIL = user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN);
		USER_PAGEBASE = user.get(User.PAGE_LINE_CNT);		
		USER_NAME = user.get(User.USER_NAME);
		STIME_OUT = user.get(User.SESSION_CHECK_TIME);
		activeXMake = "on".equals(user.get(User.USE_ACTIVE_X));
		activeXUse = "T".equals(user.get(User.ACTIVE_X));
		renderMode = (user.get(User.RENDER_MODE) == null || "".equals(user.get(User.RENDER_MODE))) ? renderMode : user.get(User.RENDER_MODE);
		noteUse = "T".equals(user.get(User.NOTE_USE));
		
		request.setAttribute("mailDomainSeq", user.get(User.MAIL_DOMAIN_SEQ));
		request.setAttribute("mailUserSeq", user.get(User.MAIL_USER_SEQ));
		request.setAttribute("mailUid", user.get(User.MAIL_UID));
		request.setAttribute("userName", user.get(User.USER_NAME));
		request.setAttribute("email", user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN));
		
		request.setAttribute("noteUse", noteUse);
		
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
		else if(locale.indexOf("ja") > -1){
			loc = new Locale("jp");
			locale = "jp";
		}
		
		session.setAttribute(I18nConstants.LOCALE_KEY, loc);		
	}
	
	if(!"ko".equals(locale) && !"en".equals(locale) && !"jp".equals(locale)) {
        locale = "en";
    }
%>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Version Info : <%=version%> -->
<title>${title}</title>

<link rel="stylesheet" type="text/css" href="/design/common/css/font_<%=locale%>.css"/>
<link rel="stylesheet" type="text/css" href="/design/common/css/common.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/layout.css"/>

<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.core.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.tabs.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.resizable.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/style1/ui.dialog.css" />

<script type="text/javascript" src="/dwr/engine.js"> </script>
<script type="text/javascript" src="/dwr/util.js"> </script>

<%if (isMinify) {%>
	<script type="text/javascript" src="/js/core-lib/core-all.min.js"></script>
	<script type="text/javascript" src="/js/common-lib/common-header-all.min.js"></script>
	<script type="text/javascript" src="/js/ext-lib/ext-header-all.min.js"></script>
<%} else {%>
	<script type="text/javascript" src="/js/core-lib/prototype.js"></script>
	<script type="text/javascript" src="/js/core-lib/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="/js/common-lib/common.js"></script>
	<script type="text/javascript" src="/js/common-lib/common-base64.js"></script>
	<script type="text/javascript" src="/js/common-lib/common-editor.js"></script>
	<script type="text/javascript" src="/js/common-lib/validate.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jcookie.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.history.js"></script>	
	<script type="text/javascript" src="/js/ext-lib/noclick.js"></script>
<%}%>
<script type="text/javascript" src="/js/ext-lib/jquery.jqplugin.1.0.2.min.js"></script>
<script type="text/javascript" src="/js/ocx/ocx_load.js"></script>

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
<tctl:extModuleCheck moduleName="keris" msie="true">
<script type="text/javascript" src="/KerisEpki/EPKICommon.js"></script>
</tctl:extModuleCheck>

<script type="text/javascript" >
var LMENU_USE = "<%=lmenuUse%>";
var TMENU_USE = "<%=tmenuUse%>";
var IS_TMENU_USE = (TMENU_USE == "disable")?false:true;
var IS_LMENU_USE = (LMENU_USE == "disable")?false:true;
var hostInfo = this.location.protocol + "//" + this.location.host;
var myhostInfo = this.location.protocol + "//127.0.0.1"; //tmp
var USEREMAIL = "<%=USER_EMAIL%>";
var USERID = "<%=USER_ID%>";
var USERNAME ="<%=USER_NAME%>";
var LOCALE = "<%=locale%>";
var USER_PAGEBASE = "<%=USER_PAGEBASE%>";
var CURRENT_PAGE_NAME = "NORMAL";
var STIME_OUT = <%=STIME_OUT%>;
var MENU_STATUS = <%=menuStatus%>;
var notIntervalCheckLoad = true;
var DEBUGMODE = "<%=debugUse%>";
var activeXMake = <%=activeXMake%>;
var activeXUse = <%=activeXUse%>;
var PDEBUGLOGGING = <%=isPDebug%>;
var RENDERMODE = "<%=renderMode%>";
var noteUse = <%=noteUse%>;
var skin = "<%=skin%>";
var PAID = "<%=sessionID%>";
function makePAID() {document.write('<input type="hidden" id="paid" name="paid" value="'+PAID+'"/>');}
function setParamPAID(param) {if (!param) param = {};var paid = jQuery("#paid").val();param.paid = paid;}
function setTitleBar(title){
	document.title=title;
}
		
try{
if(IS_TMENU_USE)top.setTitleBar('${title}');
}catch(e){}

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

    if(errorString && errorString != "" && errorString.length > 0){       
    	msg = errorString;
    } else {
    	msg = comMsg.error_default;
    } 
    alert(msg);    
}

dwr.engine.setErrorHandler(errorHandler);

function viewHelp() {
	var url = "/help/"+LOCALE+"/index.htm";
	var option = "width=770,height=700,status=yes,scrollbars=no,resizable=no";
	window.open(url, "help", option);
}

var alrimFolderlist = []; 
var existAlrimFolderlist = false;
function loadFolderInfo(){
	var param = {};
	jQuery.post("/mail/folderInfo.do", param, newMailAlrim, "json");	
}

function loadNoteCheck() {
	var param = {};
	jQuery.post("/dynamic/note/noteInfo.do", param, noteCheckResult, "json");
}
function sessionHealthCheck() {
    var param = {};
    jQuery.post("/common/sessionHealthCheck.do", param, sessionHealthCheckResult, "json");
}
function sessionHealthCheckResult(data) {
    var isDuplicateCheckOn = data.isDuplicateCheckOn;
    var isUserLogin = data.isUserLogin;
    if(!isDuplicateCheckOn){
    	clearInterval(sessionHealthCheckInterval);
    	return ;
    }
    
    if (isUserLogin) {
    	clearInterval(sessionHealthCheckInterval);
    	alert(comMsg.auth_fail_userexistout);
    	document.location = "/common/logout.do?language=<%=locale%>";
    	
    	
    }
}
var orgNoteCount = 0;
var isNoteFirstLoad = true;
function noteCheckResult(data) {
	var count = "("+data.unSeenCount+")";
	jQuery("#unseen_note_count").text(count);
	jQuery("#unseen_note_count_left").text(count);

	if (orgNoteCount < data.unSeenCount) {
		if (isNoteFirstLoad) {
			orgNoteCount = data.unSeenCount;
		} else {
			viewNewNoteAlrim();
		}
	}

	var interval = Number(data.alrimInteval) * 60 * 1000;
	if(interval > 0){
		setTimeout("loadNoteCheck()",interval + 10000);
	}
	isNoteFirstLoad = false;
}

function viewNewNoteAlrim() {
	var contents = "<a href='/dynamic/note/noteCommon.do' class='new_message'>"+comMsg.note_msg_065+"</a>";
	showAlrimMessage("noticeSystemMessage",contents);
}

var alWriteMode = false;
var alMailMode = false;
function viewNewMailAlrim(folder){
	var contents = "";
	if(alMailMode){
		if(alWriteMode){
			contents = "<a href='javascript:;'"+
			"class='new_message' "+
			"onclick='closeAlrim(\"noticeSystemMessage\");goConfirmFolder(\""+folder.encName+"\")'"+
			">"+comMsg.comn_newmail_001+"</a>";
		} else {
			contents = "<a href='javascript:;'"+
			"class='new_message' "+
			"onclick='closeAlrim(\"noticeSystemMessage\");goFolder(\""+folder.encName+"\")'"+
			">"+comMsg.comn_newmail_001+"</a>";			
		}
		
	} else {
		contents = "<a href='/dynamic/mail/mailCommon.do?"+
					"workName=list&folder="+
					encodeURI(folder.encName)+					
					"' class='new_message'>"+comMsg.comn_newmail_001+"</a>";
	}

	showAlrimMessage("noticeSystemMessage",contents);		
}

function closeAlrim(layerId){
	jQuery("#"+layerId).hide();	
}
function viewQuotaOverAlrim(quota){	
	var contents = "";
	if(alMailMode){
		if(alWriteMode){
			contents = "<a href='javascript:;'"+
			"class='quotaover_message' "+
			"onclick='closeAlrim(\"noticeSystemMessage\");goConfirmFolder(\"Inbox\")'"+
			">"+msgArgsReplace(comMsg.comn_quotaover_001,[quota])+"</a>";
		} else {
			contents = "<a href='javascript:;'"+
			"onclick='closeAlrim(\"noticeSystemMessage\");goFolder(\"Inbox\")'"+
			"class='quotaover_message' "+
			">"+msgArgsReplace(comMsg.comn_quotaover_001,[quota])+"</a>";			
		}
		
	} else {
		contents = "<a href='/dynamic/mail/mailCommon.do?"+
					"workName=list&folder="+
					encodeURI("Inbox")+
					"' class='quotaover_message'>"+msgArgsReplace(comMsg.comn_quotaover_001,[quota])+"</a>";
	}
	
	showAlrimMessage("noticeSystemMessage",contents);
}

function newMailAlrim(data, textStatus){
	if(textStatus == "success"){
		var interval;
		if(Number(data.quotaInfo) > 90){
			alrimFolderlist = data.folderInfo;
			viewQuotaOverAlrim(data.quotaInfo);
			interval = 30 * 1000;
		} else {		
			if(!existAlrimFolderlist){
				makeFolderInfoList(alrimFolderlist,data.folderInfo);
				existAlrimFolderlist = true;
			} else { 
				checkNewMailAlrim(data.folderInfo);
			}

			interval = Number(data.alrimInteval) * 60 * 1000;	
		}
					
		if(interval > 0){
			setTimeout("loadFolderInfo()",interval);
		}		
	}
}

function checkNewMailAlrim(folderList){
	var mailListArray = [];
	var keyList = [];
	makeFolderInfoList(mailListArray,folderList,keyList);

	for (var i = 0 ; i < keyList.length ; i++) {
		var nfolderName = keyList[i];		
		var compareFolder = alrimFolderlist[nfolderName];		
		if(compareFolder){									
			var currentFolder = mailListArray[nfolderName];			
			if((compareFolder.fullName != "Drafts" &&
					compareFolder.fullName != "Sent" &&
					compareFolder.fullName != "Reserved" &&
					compareFolder.fullName != "Spam" && 
					compareFolder.fullName != "Trash") &&
					Number(currentFolder.unseenCnt) > Number(compareFolder.unseenCnt)){
				viewNewMailAlrim(currentFolder);
				isCheck = true;
				break;
			} 
		}			
	}	
	//alrimFolderlist = folderList;	
}

function makeFolderInfoList(arrayList,folderList,keyList){
	var folder;
	for ( var i = 0; i < folderList.length; i++) {
		folder = folderList[i];		
		if(folder.child && folder.child.length > 0){
			arrayList[folder.fullName] = folder;
			if(keyList)keyList.push(folder.fullName);
			makeFolderInfoList(arrayList,folder.child);			
		} else {
			arrayList[folder.fullName] = folder;
			if(keyList)keyList.push(folder.fullName);
		}	
	}
}


function showAlrimMessage(layerId,contents){
	jQuery("#systemNoticeContent").html(contents);
	jQuery("#"+layerId).slideDown("slow");
	setTimeout("hideAlrimMessage('"+layerId+"')",10000);

	jQuery("#noticeCloseBtn").click(function(){closeAlrim(layerId);});
}

function hideAlrimMessage(layerId){
	jQuery("#"+layerId).slideUp("slow");
}

function setAlWriteMode(val){
	alWriteMode = val;
}
function setAlMailMode(val){
	alMailMode = val;
}

function resultCheckSessionTime(data, textStatus){	
	if(textStatus == "success"){
		if(data.isOverSession){
			document.location = "/common/logout.do?timeout=on&stime="+STIME_OUT+"&language=<%=locale%>";
		}
	}
	
	if(STIME_OUT != 0 ){
		setTimeout("checkSessionTimeout()",STIME_OUT * 60 * 1000);
	}
}
function checkSessionTimeout(){
	if(STIME_OUT != 0 && !alWriteMode){		
		jQuery.post("/common/checkSessionTimeout.do", {}, resultCheckSessionTime, "json");
	}
}

function topLink(id, link, target){
	if(alWriteMode && alMailMode){
		if(isWriteModify()){
			if(!confirm(mailMsg.confirm_escapewrite)){
				return;			
			} else {
				alWriteMode = false;
			}
		}
	}
	
	if(id == "mail" && alMailMode){
		if(LayoutInfo.mode == "manage"){
			escapeManageMode();			
		}
		goFolder('Inbox'); 
		
	} else {
		if(target != "new"){
			if (target == 'top') {
                top.location.href = link;
	        } else if (target == 'self') {
	            self.location.href = link;
	        } else {
	            this.location.href = link;
	        }
		} else {
			var nwin = window.open("","","");
			nwin.location.href = link;
		}		
	}
}

function toogleSideMenu(event){
	var eventTarget = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);

	if(eventTarget.attr("id") != "sideHelpBtn"){	
		var content = jQuery("#leftSideMenuContent");
		var smenu = jQuery("#leftSideMenu");
		var smenuBox = jQuery("#leftSideMenuBox");
		var smenuBtn = jQuery("#leftSideMenuBtn");
		if(smenu.attr("viewmenu") == "show"){					
			smenu.animate({height: "25px",bottom:"0px"}, 500,function(){jQuery("#leftSideMenuBox").empty();resizeLeftMenuSize();setCookie("SME","F",365);});		
			smenu.attr("viewmenu","hide");
			smenu.css({bottom:"0px"});
			smenuBtn.removeClass("TM_sideMenuDown");
			smenuBtn.addClass("TM_sideMenuUp");
			smenuBox.empty();
			smenuBox.hide();							
		} else if(smenu.attr("viewmenu") == "hide"){
			smenuBox.show();		
			smenu.show();		
			smenuBox.html(content.html());
			smenu.css({bottom:"10px"});
			smenu.animate({height: "207px",bottom:"0px"}, 500, function(){resizeLeftMenuSize();setCookie("SME","T",365);});		
			smenu.attr("viewmenu","show");
			smenuBtn.removeClass("TM_sideMenuUp");
			smenuBtn.addClass("TM_sideMenuDown");
		}
	}
}

function loadSideMenu(){
	var content = jQuery("#leftSideMenuContent");
	var smenu = jQuery("#leftSideMenu");
	var smenuBox = jQuery("#leftSideMenuBox");
	var smenuBtn = jQuery("#leftSideMenuBtn");
	var isSideMenu = getCookie("SME");
	smenu.css({bottom:"0px"});
	if(!isSideMenu || isSideMenu == "T"){
		smenuBox.html(content.html());
		smenu.css({height: "207px"});
		smenu.attr("viewmenu","show");		
		smenuBtn.removeClass("TM_sideMenuUp");
		smenuBtn.addClass("TM_sideMenuDown");				
	} else if(isSideMenu == "F"){
		smenuBox.empty();
		smenu.css({height: "25px"});
		smenu.attr("viewmenu","hide");
		smenuBtn.removeClass("TM_sideMenuDown");
		smenuBtn.addClass("TM_sideMenuUp");
	}
}
var sessionHealthCheckInterval;
jQuery().ready(function(){
<%if (user != null) {%>
	if(!opener && notIntervalCheckLoad){
		loadFolderInfo();
		checkSessionTimeout();
		if (noteUse) {
			loadNoteCheck();
		}
		//PageMainLoadingManager.clearLoad();
	}
    sessionHealthCheckInterval = setInterval('sessionHealthCheck()',5000);
    
<%}%>

if(PDEBUGLOGGING){
	jQuery("body").append("<div id='pdebuglogging'>");
}
});
</script>