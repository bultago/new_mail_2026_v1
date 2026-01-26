<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/common/css/login.css">
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
<%
String newSessionId = request.getSession().getId();
String language = request.getParameter("language");
if(language == null){
	language = (String)request.getSession().getAttribute("language");
	if(language == null){
		language = "ko";
	}
}
session.setAttribute(I18nConstants.LOCALE_KEY, new Locale(language));
%>
<script type="text/javascript" defer="defer">
var loginParamterRSAUse = <%=loginParamterRSAUse %>;
var PKI_INFO = <tctl:pkiCheck print="true"/>;
var WEB_PORT = "<%=EnvConstants.getBasicSetting("web.port")%>";
var SSL_CERT_USE = <%=EnvConstants.getBasicSetting("ssl.cert.force.rediret.use") == null ? "false" : EnvConstants.getBasicSetting("ssl.cert.force.rediret.use") %>;


function checkAuth () {
	var action = "/common/login.do";
	var f = document.loginForm;
	var id = jQuery.trim(f.id.value);
	var password = jQuery.trim(f.pass.value);
	var domain = jQuery.trim(f.domain.value);
	
	if (trim(f.id.value) == "") {
		alert(comMsg.error_login_id);
		f.id.focus();
		return;
	}

	if (trim(f.pass.value) == "") {
		alert(comMsg.error_login_password);
		f.pass.focus();
		return;
	}

	if(jQuery("#saveIdImg").attr("check") == "on"){
		setCookie("TSID",id,365);
	} else {
		setCookie("TSID",id,-1);
	}

	if(jQuery("#saveDomainImg").attr("check") == "on"){
		setCookie("TSDOMAIN",domain,365);
	} else {
		setCookie("TSDOMAIN",domain,-1);
	}
	
	if(loginParamterRSAUse){
		var rsaPublicKeyModulus = "";
		var rsaPublicKeyExponent = "";	
	    try {
	    	jQuery.post(
				 "/common/checkRsaPrivateKey.jsp", 
				 "",
				function (data) {
					f.id.value = "";
			        f.pass.value = "";
					if(data.existPrivateKey == "true"){
						rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
						rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
					}else{
						rsaPublicKeyModulus = data.rsaPublicKeyModulus;
						rsaPublicKeyExponent = data.rsaPublicKeyExponent;
					}
					
					var rsa = new RSAKey();
			        rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

			        // 사용자ID와 비밀번호를 RSA로 암호화한다.
			        var securedId = rsa.encrypt(id);
			        var securedPassword = rsa.encrypt(password);

			        // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
			        var securedLoginForm = document.getElementById("securedLoginForm");
			        securedLoginForm.action = action;
			        securedLoginForm.securedId.value = securedId;
			        securedLoginForm.securedPassword.value = securedPassword;
			        securedLoginForm.domain.value = domain;
			        securedLoginForm.pkiSignText.value = f.pkiSignText.value;
			        securedLoginForm.language.value = f.language.value;
			        securedLoginForm.submit();
				}
			, "json");
	        
	    } catch(err) {
	        alert(err);
	    }
	}else{
		f.loginMode.value = "normal";		
		f.action = action;
		f.method = "post";
		f.submit();
	}
}

function checkPkiLogin(){
	var f = document.loginForm;
	var signText = "";

	if(isMsie && PKI_INFO.vender == "SOFTFORUM"){
		if(PKI_INFO.mode == "EPKI"){		
			signText = Sign_with_option(0, "LOGINTEMPPKI"+makeRandom());
		}
		if(signText != ""){
			f.pkiSignText.value = signText;
			f.loginMode.value = "pki";		
			f.action = "/common/login.do";
			f.method = "post";
			XecureSubmit(f);
		}
	} else if(isMsie && PKI_INFO.vender == "INITECH_V7"){
		f.loginMode.value = "pki";
		f.action = "/common/loginPki.do";
		f.method = "post";
		f.submit();
	}  else if(isMsie && PKI_INFO.vender == "KERIS"){
		signText = Sign(1, "", "LOGINTEMPPKI"+makeRandom());
		if(signText != ""){
			f.pkiSignText.value = signText;
			f.loginMode.value = "pki";		
			f.action = "/common/login.do";
			f.method = "post";	
			f.submit();		
		} else {
			ECTErrorInfo();
		}
	}	
	
}
	
function init(){
	var timeout = '${fn:escapeXml(timeout)}';
	if(timeout == "on"){
		openTimeoutNoticeModal();
	}

	<c:if test="${loginConfigList['announcements_option'] == 'on'}">
		getNoticeBbs();
	</c:if>
	<c:if test="${loginConfigList['faq_option'] == 'on'}">
		getFaqBbs();
	</c:if>
	if(!PKI_INFO.loginPKI){
		checkCookieSetting();
	}

	<c:if test="${loginConfigList['secure_access_display'] == 'off'}">
		jQuery("#optionArea").css({"text-align":"left","padding-left":"39px"});
	</c:if>
	<c:if test="${loginConfigList['secure_access_display'] != 'off'}">
		jQuery("#optionArea").css("text-align","center");
	</c:if>
	
	
	if(jQuery("#id").val() == ""){
		jQuery("#id").focus();
	}else{
		jQuery("#pass").focus();
	}
	
}

function checkCookieSetting(){
	var f = document.loginForm;
	var idFromCookie = getCookie("TSID");
	var domainFromCookie = getCookie("TSDOMAIN");
	var secureCookie = getCookie("TSSECURE");
	if(idFromCookie != ""){
		f.id.value = idFromCookie;
		jQuery("#saveIdImg").attr("check", "on");
		jQuery("#saveIdImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
	}
		
	if(domainFromCookie != ""){
		jQuery("#saveDomainImg").attr("check", "on");
		jQuery("#saveDomainImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
		checkDomain(domainFromCookie);
				
		if(idFromCookie != ""){
			if(f.pass) f.pass.focus();
		} else {
			if(f.id) f.id.focus();
		}
	} else {
		if(idFromCookie != ""){
			if(f.domain) f.domain.focus();
		} else {
			if(f.id) f.id.focus();
		}
	}

	var protocol = this.location.protocol;
	if(SSL_CERT_USE){
		if (protocol == "http:") {
			top.location = "https://"+this.location.hostname;
		}
		jQuery("#secureImgLabel").hide();
	}else{
		if(secureCookie == "on") {
			jQuery("#secureImg").attr("check", "on");
			jQuery("#secureImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
			
			if (protocol == "http:") {
				top.location = "https://"+this.location.hostname;
			}
		} else {
			if (protocol == "https:") {
				top.location = "http://"+this.location.hostname+":"+WEB_PORT;
			}
		}
	}
}

function checkDomain(domainName) {
	var f = document.loginForm;

	<c:if test="${loginConfigList['domain_type'] != 'single'}">
		<c:if test="${loginConfigList['domain_input_type'] != 'input'}">
			if (f.domain.length) {
				for (i=0; i<f.domain.length; i++) {
					if (domainName == f.domain.options[i].value) {
						f.domain.options[i].selected = true;
						break;
					}
				}
			}
		</c:if>
		<c:if test="${loginConfigList['domain_input_type'] == 'input'}">
			f.domain.value = domainName;
		</c:if>
	</c:if>
}

function outlookPopup() {
	var url = "/common/outlook_ko.html";
	var winName ="pop";
	var features = "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,directories=no, width=750,height=680,top=1,left=1";

	popUpWin=window.open(url,winName,features);
	popUpWin.focus();
}

/*
function openTimeoutNoticeModal() {
	var timeout = '${fn:escapeXml(sessionTime)}';
	var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3",
		minWidth:450,
		minHeight:200,
		openFunc:function(){
			jQuery("#timeoutMsg").html(msgArgsReplace(comMsg.auth_timeout_002,[timeout]));
		}
	};

	jQuery("#timeOutNotice").jQpopup("open",popupOpt);
		
}
*/
function openTimeoutNoticeModal() {
	var timeout = '${fn:escapeXml(sessionTime)}';
	<% if("en".equals(locale)) { %>
	alert(msgArgsReplace("<%="Because mail services are not used for {0} mins, \\nmail services will be logged out automatically for the protection of personal information."%>",[timeout]));
	<% } else if("jp".equals(locale)) { %>
	alert(msgArgsReplace("<%="\u30e1\u30fc\u30eb\u3092 {0}\u5206\u9593\u4f7f\u7528\u3057\u306a\u3044\u3068\u3001\\n\u30a2\u30ab\u30a6\u30f3\u30c8\u60c5\u5831\u3092\u4fdd\u8b77\u3059\u308b\u305f\u3081\u306b\u81ea\u52d5\u3067 \u30ed\u30b0\u30a2\u30a6\u30c8 \u3057\u307e\u3059\u3002"%>",[timeout]));
	<% } else { %>
	alert(msgArgsReplace("<%="\uba54\uc77c \uc11c\ube44\uc2a4\ub97c \uc77c\uc815\uc2dc\uac04({0}\ubd84)\ub3d9\uc548 \uc0ac\uc6a9\ud558\uc9c0 \uc54a\uc544\\n\uac1c\uc778\uc815\ubcf4\ubcf4\ud638\ub97c \uc704\ud574 \uba54\uc77c \uc11c\ube44\uc2a4\ub97c \uc790\ub3d9\uc73c\ub85c \ub85c\uadf8\uc544\uc6c3 \ud569\ub2c8\ub2e4."%>",[timeout]));
	<% } %>
	location.href = "/";
}

function registerPopup() {
	var url= "/register/registerUserWin.do";
	
	//openSimplePopup(url,"500px","100px",true);
	modalPopupForRegisterUser(url, 500, 160);
}

function searchId() {
	var url= "/register/searchUserIdWin.do";
	
	//openSimplePopup(url,"500px","100px",true);
	modalPopupForFindId(url, 500, 160);
}

function searchPassword() {
	var url= "/register/searchPasswordWin.do";
	
	//openSimplePopup(url,"600px","100px",true);
	modalPopupForFindPass(url, 500, 110);
}

function getNoticeBbs() {
	var url = "/bbs/listNoticeContent.do"
	var param = {bbsIndex:0};
	setParamPAID(param);
	jQuery("#noticeBbs").load(url, param);
}

function getFaqBbs() {
	var url = "/bbs/listNoticeContent.do"
	var param = {bbsIndex:1};
	setParamPAID(param);
	jQuery("#faqBbs").load(url, param);
}

function listContentPopup(bbsIndex) {
	/*
	var f = document.contentListForm;
	f.bbsIndex.value = bbsIndex;
	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width=740,height=640");
	var oldTarget = f.target;
	f.target = "popupRead";
	f.action = "/bbs/listNoticeContentPopup.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
	*/
	var src = "/bbs/listNoticeContentPopup.do?jQpopup=Y&bbsIndex="+bbsIndex+"&paid="+PAID;
	modalPopupForNoticeList(src);
}

function viewContentPopup(bbsId, contentId, bbsIndex) {
	/*
	var f = document.contentListForm;
	f.bbsId.value = bbsId;
	f.contentId.value = contentId;
	f.bbsIndex.value = bbsIndex;
	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width=740,height=640");
	var oldTarget = f.target;
	f.target = "popupRead";
	f.action = "/bbs/viewNoticeContentPopup.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
	*/
	var src = "/bbs/viewNoticeContentPopup.do?jQpopup=Y&bbsId="+bbsId+"&contentId="+contentId+"&bbsIndex="+bbsIndex;
	modalPopupForNoticeContent(src);
}

function setNoticeListPopupTitle(title){
	jQuery("#noticeList_pht").html(title);
}

function setNoticeContentPopupTitle(title){
	jQuery("#noticeContent_pht").html(title);
}

function setRegisterUserPopupTitle(title){
	jQuery("#registerUser_pht").html(title);
}

function setFindIdPopupTitle(title){
	jQuery("#findId_pht").html(title);
}

function setFindPassPopupTitle(title){
	jQuery("#findPass_pht").html(title);
}

function changeLoginOption(id) {
	var check = jQuery("#"+id).attr("check");
	if (check == "on") {
		jQuery("#"+id).attr("check", "off");
		jQuery("#"+id).attr("src", "/design/default/image/icon/icon_login_off.gif");		
	} else {
		jQuery("#"+id).attr("check", "on");
		jQuery("#"+id).attr("src", "/design/default/image/icon/icon_login_on.gif");
	}
}

function changeSecureMode() {
	var check = jQuery("#secureImg").attr("check");
	if (check == "on") {
		setCookie("TSSECURE","on",-1);
		top.location = "http://"+this.location.hostname+":"+WEB_PORT;
	} else {
		setCookie("TSSECURE","on",365);
		top.location = "https://"+this.location.hostname;
	}
}

function toogleLoginMode(mode){
	if(mode == "pki"){
		jQuery("#pkiLBtn").removeClass("optionLoginOff");
		jQuery("#pkiLBtn").addClass("optionLoginOn");
		jQuery("#normalLBtn").removeClass("optionLoginOn");
		jQuery("#normalLBtn").addClass("optionLoginOff");
		jQuery("#normalLogin").hide();
		jQuery("#pkiLogin").show();
	} else if(mode == "normal"){
		jQuery("#normalLBtn").removeClass("optionLoginOff");
		jQuery("#normalLBtn").addClass("optionLoginOn");
		jQuery("#pkiLBtn").removeClass("optionLoginOn");
		jQuery("#pkiLBtn").addClass("optionLoginOff");
		jQuery("#normalLogin").show();
		jQuery("#pkiLogin").hide();
		checkCookieSetting();
	}	
}

function goSimpleWebmail(){
	parent.document.location = "/mobile/common/changeMailMode.do?mailMode=mobile&language="+language;	
}

var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};

function modalPopupForNoticeList(src){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = 540;
	var width = 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	jQuery("#noticeList").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#noticeListIframe").attr("src",src);
			jQuery("#noticeListIframe").css("height",(height-20)+"px");
			jQuery("#noticeListIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#noticeList_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#noticeListIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#noticeList").jQpopup("open",popOpt);
}

function modalPopupForNoticeListClose(){
	jQuery("#noticeList").jQpopup("close");
}

function modalPopupForNoticeContent(src){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = 540;
	var width = 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	jQuery("#noticeContent").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#noticeContentIframe").attr("src",src);
			jQuery("#noticeContentIframe").css("height",(height-20)+"px");
			jQuery("#noticeContentIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#noticeContent_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#noticeContentIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#noticeContent").jQpopup("open",popOpt);
}

function modalPopupForNoticeContentClose(){
	jQuery("#noticeContent").jQpopup("close");
}

function modalPopupForRegisterUser(src, widthParam, heightParam){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = heightParam ? heightParam : 540;
	var width = widthParam ? widthParam : 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#registerUser").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#registerUserIframe").attr("src",src);
			jQuery("#registerUserIframe").css("height",(height-20)+"px");
			jQuery("#registerUserIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#registerUser_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#registerUserIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#registerUser").jQpopup("open",popOpt);
}

function resizeRegisterUser(width, height){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#registerUser").css({"height":height+"px","width":width+"px"});
	jQuery("#registerUserIframe").css("height",(height-20)+"px");
	jQuery("#registerUserIframe").css("width",(width-10)+"px");
	jQuery("#registerUser").jQpopup("resize",popOpt);
}

function modalPopupForRegisterUserClose(){
	jQuery("#registerUser").jQpopup("close");
}

function modalPopupForFindId(src, widthParam, heightParam){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = heightParam ? heightParam : 540;
	var width = widthParam ? widthParam : 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#findId").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#findIdIframe").attr("src",src);
			jQuery("#findIdIframe").css("height",(height-20)+"px");
			jQuery("#findIdIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#findId_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#findIdIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#findId").jQpopup("open",popOpt);
}

function modalPopupFindIdClose(){
	jQuery("#findId").jQpopup("close");
}

function modalPopupForFindPass(src, widthParam, heightParam){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = heightParam ? heightParam : 540;
	var width = widthParam ? widthParam : 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#findPass").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#findPassIframe").attr("src",src);
			jQuery("#findPassIframe").css("height",(height-20)+"px");
			jQuery("#findPassIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#findPass_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#findPassIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#findPass").jQpopup("open",popOpt);
}

function resizeFindPass(width, height){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#findPass").css({"height":height+"px","width":width+"px"});
	jQuery("#findPassIframe").css("height",(height-20)+"px");
	jQuery("#findPassIframe").css("width",(width-10)+"px");
	jQuery("#findPass").jQpopup("resize",popOpt);
}

function modalPopupFindPassClose(){
	jQuery("#findPass").jQpopup("close");
}

function jQpopupClear(){
	setTimeout(function(){
		jQuery(":text:first").focus();
	},100);
	
}
var language = "<%=language %>"
function changeLanguage(selectObject){
	if(language != selectObject.value)
		location.href = "/common/welcome.do?language="+selectObject.value;
}
</script>

</head>
<body onload="init()">
<form name="loginForm">
<input type="hidden" name="loginMode"/>
<input type="hidden" name="pkiSignText"/>
<script type="text/javascript">makePAID();</script>

	<div class="loginContents">
		<div id="loginTopImg">
		<c:if test="${!empty commonLogoList['loginTop']}">
			<img src="${commonLogoList['loginTop'].logoImgUrl}" width="210px" height="35px"/>
		</c:if>
		<c:if test="${empty commonLogoList['loginTop']}">
			<img src="/design/common/image/logo_tms.gif"/>
		</c:if>
		</div>
		<div class="login_title">
			<h2><tctl:msg key="login.001" bundle="common"/></h2>
			<c:if test="${mobileMailUse}">
			<div style="float:left;padding:6px 0px 0px 8px;"><a class="btn_style7" href="javascript:goSimpleWebmail();"><span style='padding-top:1px;'><tctl:msg key="comn.simplemail" bundle="common"/></span></a></div>
			</c:if>
			
			<p class="language">
				<span style="line-height:20px;color:#fff;font-size:12px;"><tctl:msg key="login.008" bundle="common"/> : </span>
				<select name="language" onchange="changeLanguage(this);">
					<%--<option value=""><tctl:msg key="login.008" bundle="common"/></option>--%>
					<option value="ko" <% if("ko".equals(language)){ out.println("selected"); } %>><tctl:msg key="conf.profile.23" bundle="setting"/></option>
					<option value="en" <% if("en".equals(language)){ out.println("selected"); } %>><tctl:msg key="conf.profile.24" bundle="setting"/></option>
					<option value="jp" <% if("jp".equals(language)){ out.println("selected"); } %>><tctl:msg key="conf.profile.language.japanese" bundle="setting"/></option>
				</select>
			</p>
		</div>
		
		<div class="display" id="middleImg">
			<c:if test="${!empty commonLogoList['loginMiddle']}">
				<img src="${commonLogoList['loginMiddle'].logoImgUrl}" width="760px"/>
			</c:if>
			<c:if test="${empty commonLogoList['loginMiddle']}">
				<c:if test="${installLocale == 'jp'}">
					<img src="/design/common/image/login/display_jp.jpg" width="760" height="260">
				</c:if>
				<c:if test="${installLocale != 'jp'}">
					<img src="/design/common/image/login/display.jpg" width="760" height="260">
				</c:if>
			</c:if>
		</div>
		
		<tctl:pkiCheck msie="true" loginMode="PKI">
		<div style="height:18px;margin-top:3px;">
			<div style="position: absolute;">
				<div id="pkiLBtn" class="optionLoginOn" onclick="toogleLoginMode('pki')"><tctl:msg key="login.010" bundle="common"/></div>
				<div id="normalLBtn" class="optionLoginOff" style="margin-left:2px" onclick="toogleLoginMode('normal')"><tctl:msg key="login.011" bundle="common"/></div>
			</div>
		</div>
		</tctl:pkiCheck>
		
		<div class="body_bar"></div>
		
		<div class="login_body">			
			<div class="news loginBody">
				<tctl:pkiCheck msie="true" loginMode="PKI">
				<div id="pkiLogin" class="pkiLoginPane">
					<div class="pkiLoginBtn">
						<img src="/design/common/image/icon/ic_key.gif" width="32" height="32" style="position: absolute;top:5px; left:60px; z-index: 100"/>
						<a href="#" onclick="checkPkiLogin();" class="btn_login_pki"><span><tctl:msg key="login.010" bundle="common"/></span></a>
					</div>					
					<div class="optionArea">
						<c:if test="${loginConfigList['signup_option'] == 'on'}">
							<span>|</span>
							<a href="javascript:registerPopup()"><tctl:msg key="login.003" bundle="common"/></a>
							<span>|</span>
						</c:if>
					</div>
				</div>
				</tctl:pkiCheck>
				<div id="normalLogin" <tctl:pkiCheck msie="true" loginMode="PKI">style="display:none"</tctl:pkiCheck>>
					<div class="title" id="optionArea" style="color:#898989;padding-top:5px;height:22px;line-height:22px">						
						<c:if test="${loginConfigList['secure_access_display'] != 'off'}">
							<label id="secureImgLabel" onclick="changeSecureMode()">
							<img id="secureImg" src="/design/default/image/icon/icon_login_off.gif">							
							<tctl:msg key="login.006" bundle="common"/>
							</label>
							&nbsp;
						</c:if>
						<label onclick="changeLoginOption('saveIdImg')">
						<img id="saveIdImg" src="/design/default/image/icon/icon_login_off.gif">									
						<tctl:msg key="login.002" bundle="common"/>
						</label>
						&nbsp;	
						<label onclick="changeLoginOption('saveDomainImg')">
						<img id="saveDomainImg" src="/design/default/image/icon/icon_login_off.gif">
						<tctl:msg key="login.009" bundle="common"/>
						</label>
					</div>
				
					<div style="padding:12px 12px 10px 12px">
						<table style="width:100%">
							<tbody>
								<tr>
									<td align="right"><input id="id" name="id" type="text" class="IP100px" style="width:110px;ime-mode:inactive;" tabIndex="1"></td>
									<td style="white-space:nowrap;">
										<c:if test="${loginConfigList['domain_type'] != 'single'}">
											<c:if test="${loginConfigList['domain_input_type'] != 'input'}">
												@<select id="domain" name="domain" style="width:100px;" title="${domain.mailDomain}" tabIndex="2">
													<c:forEach var="domain" items="${domainList}">
														<option value="${domain.mailDomain}" title="${domain.mailDomain}" <c:if test="${domain.mailDomain == defaultDomain}">selected</c:if>>${domain.mailDomain}</option>
													</c:forEach>
												</select>
											</c:if>
											<c:if test="${loginConfigList['domain_input_type'] == 'input'}">
												@<input id="domain" name="domain" type="text" class="IP100px" tabIndex="2">
											</c:if>
										</c:if>
										<c:if test="${loginConfigList['domain_type'] == 'single'}">
											@<select id="domain" name="domain" style="width:100px;" title="${selectDomainName}" tabIndex="3">
												<option value="${selectDomainName}" title="${selectDomainName}">${selectDomainName}</option>
											</select>
										</c:if>
									</td>
								</tr>
								<tr>
									<td align="right"><input id="pass" name="pass" type="password" class="IP100px" style="width:110px" tabIndex="3" onKeyPress="(keyEvent(event) == 13) ? checkAuth() : '';" autocomplete="off"></td>
									<td style="padding-left:11px;">
										<a href="javascript:checkAuth()" class="btn_login" tabIndex="4"><span><tctl:msg key="comn.login" bundle="common"/></span></a>			
									</td>
								</tr>
							</tbody>
						</table>
					</div>

					<div class="optionArea" <c:if test="${loginConfigList['simple_info'] == 'off'}">style="border-bottom-width:0px;"</c:if>>
						<c:if test="${loginConfigList['signup_option'] == 'on'}">
							<a href="javascript:registerPopup()"><tctl:msg key="login.003" bundle="common"/></a>
							<span>|</span>
						</c:if>
						<c:if test="${loginConfigList['search_id_option'] == 'on'}">
							<a href="javascript:searchId()"><tctl:msg key="login.004" bundle="common"/></a>
							<span>|</span>
						</c:if>
						<c:if test="${loginConfigList['search_password_option'] == 'on'}">
							<a href="javascript:searchPassword()"><tctl:msg key="login.005" bundle="common"/></a>
						</c:if>
						<br/>						
					</div>
				</div>				
				
				<c:if test="${loginConfigList['simple_info'] != 'off'}">
				<div class="s_help">
					<p class="title"><tctl:msg key="login.007" bundle="common"/></p>
					<p class="contents"></p>
					<div class="noticeCotent div_scroll">
						${noticeContent}
					</div>
				</div>
				</c:if>
				
			</div>
			
			
			<div id="noticeBbs" class="news"></div>
			
			
			<div id="faqBbs" class="news"></div>
			
		</div>
		<div class="copyright">
			
		</div>
	</div>
</form>
<form name="contentListForm">
	<input type="hidden" name="bbsId">
	<input type="hidden" name="contentId">
	<input type="hidden" name="parentId">
	<input type="hidden" name="orderNo">
	<input type="hidden" name="bbsIndex">
	<input type="hidden" name="viewType" value="list">
</form>

<div id="timeOutNotice"  title="<tctl:msg key="auth.timeout.title" bundle="common"/>" style="display: none">	
	<div class="TM_modalMessage">			
		<div class="TM_timeout_message">				
			<div class="TM_alert_title"><tctl:msg key="auth.timeout.001" bundle="common"/></div>
			<div id="timeoutMsg"></div>
		</div>
	</div>		
</div>

<% if(loginParamterRSAUse){ %>
<%
String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
%>
<form id="securedLoginForm" name="securedLoginForm" action="" method="post" style="display: none;">
    <input type="hidden" name="securedId" id="securedId" value="" />
    <input type="hidden" name="securedPassword" id="securedPassword" value="" />
    <input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
	<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
	<input type="hidden" name="loginMode" id="loginMode" value="normal" />
	<input type="hidden" name="domain" id="domain" value="" />
	<input type="hidden" name="pkiSignText" id="pkiSignText" value="" />
	<input type="hidden" name="language">
</form>
<% } %>

<%@include file="/common/xecureOcx.jsp" %>
<div id="noticeList" title="" style="display:none">
    <iframe id="noticeListIframe" name="noticeListIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
<div id="noticeContent" title="" style="display:none">
    <iframe id="noticeContentIframe" name="noticeContentIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
<div id="registerUser" title="" style="display:none">
    <iframe id="registerUserIframe" name="registUserIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
<div id="findId" title="" style="display:none">
    <iframe id="findIdIframe" name="findIdIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
<div id="findPass" title="" style="display:none">
    <iframe id="findPassIframe" name="findPassIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
</body>
</html>