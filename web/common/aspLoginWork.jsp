<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/common/css/loginCyber.css">
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" defer="defer">
var PKI_INFO = <tctl:pkiCheck print="true"/>;
function checkAuth () {
	var f = document.loginForm;
	var action = "/common/login.do";
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

	if(jQuery("#secureImg").attr("check") == "on"){
		setCookie("TSSECURE","on",365);
		action = "https://"+this.location.host + action;
		f.target = "_parent";	
	} else {
		setCookie("TSSECURE","on",-1);
	}

	f.loginMode.value = "normal";		
	f.action = action;
	f.method = "post";
	f.submit();
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
	var timeout = '${timeout}';	
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

	displayIdPass();
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
		//checkDomain(domainFromCookie);
				
		if(idFromCookie != ""){
			f.pass.focus();
		} else {
			f.id.focus();
		}
	} else {
		if(idFromCookie != ""){
			//f.domain.focus();
		} else {
			f.id.focus();
		}
	}

	if(secureCookie == "on") {
		jQuery("#secureImg").attr("check", "on");
		jQuery("#secureImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
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

function openTimeoutNoticeModal() {
	var timeout = '${sessionTime}';
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

function registerPopup() {
	var url= "/register/registerUserWin.do";
	
	openSimplePopup(url,"500px","200px",true);
}

function searchId() {
	var url= "/register/searchUserIdWin.do?domain="+jQuery("#domain").val();
	
	openSimplePopup(url,"500px","180px",true);
}

function searchPassword() {
	var url= "/register/searchPassword.do";
	
	openSimplePopup(url,"600px","80px",true);
}

function getNoticeBbs() {
	var url = "/bbs/listNoticeContent.do"
	var param = {bbsIndex:0};
	jQuery("#noticeBbs").load(url, param);
}

function getFaqBbs() {
	var url = "/bbs/listNoticeContent.do"
	var param = {bbsIndex:1};
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
	var src = "/bbs/listNoticeContentPopup.do?jQpopup=Y&bbsIndex="+bbsIndex;
	modalPopupForNotice(src);
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
	modalPopupForNotice(src);
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
	parent.document.location = "/mobile/common/changeMailMode.do?mailMode=mobile";	
}

function displayIdPass(){
	var margin = 4;
	
	if(!jQuery.browser.msie)
		margin = 3
		
	var idTop =  jQuery("#id").offset().top + margin;
	var idLeft =  jQuery("#id").offset().left + margin;
	var passTop =  jQuery("#pass").offset().top + margin;
	var passLeft =  jQuery("#pass").offset().left + margin;
	
	
	if(jQuery("#id").val() != "" || jQuery("#id").val().length >= 1){
		jQuery("#idLayer").hide();
	}else{
		jQuery("#idLayer").css("z-index","1000");
		jQuery("#idLayer").css("top", idTop +"px");
		jQuery("#idLayer").css("left", idLeft +"px");
		jQuery("#idLayer").show();
	}
	if(jQuery("#pass").val() != "" || jQuery("#pass").val().length >= 1){
		jQuery("#passLayer").hide();
	}else{
		jQuery("#passLayer").css("z-index","1000");
		jQuery("#passLayer").css("top", passTop +"px");
		jQuery("#passLayer").css("left", passLeft +"px");
		jQuery("#passLayer").show();
	}
			
}

function onClickIdPass(id){
	
	if(jQuery("#"+id).val() != "" || jQuery("#"+id).val().length >= 1){
		jQuery("#"+id).show();
	}else{
		if(id=="idLayer")
			document.loginForm.id.focus();
		else if(id=="passLayer")
			document.loginForm.pass.focus();
		
		jQuery("#"+id).hide();
	}
}

function mouseOutObj(obj){
	jQuery("#"+obj+"Layer").show();
}
function mouseUpObj(obj){
	if(obj == "id"){
		jQuery("#"+obj+"Layer").hide();
		if(jQuery("#pass").val() == ""){
			jQuery("#passLayer").show();
		}else{
			jQuery("#passLayer").hide();
		}
	}else if(obj == "pass"){
		jQuery("#"+obj+"Layer").hide();
		if(jQuery("#id").val() == ""){
			jQuery("#idLayer").show();
		}else{
			jQuery("#idLayer").hide();
		}
	}
}

var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};

function modalPopupForNotice(src){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = 540;
	var width = 740;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#loginNoticeIframe").attr("src",src);
			jQuery("#loginNoticeIframe").css("height",(height-20)+"px");
			jQuery("#loginNoticeIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#loginNotice_jqbtn").hide();
	};
	popOpt.closeFunc = function(){
		jQuery("#loginNoticeIframe").attr("src","/common/zero.html");
	};
	
	jQuery("#loginNotice").jQpopup("open",popOpt);
}

function modalPopupForNoticeClose(){
	jQuery("#loginNotice").jQpopup("close");
}
</script>

</head>
<body onload="init()">
<form name="loginForm">
<input type="hidden" name="loginMode"/>
<input type="hidden" name="pkiSignText"/>
	<div class="loginContents">
		<div id="loginTopImg">
	 
		<c:if test="${!empty logoVO.logoImgUrl}">
			<img src="${logoVO.logoImgUrl}" width="210px" height="35px"/>
		</c:if>
				
		<c:if test="${empty logoVO.logoImgUrl}">
			<img src="/design/common/image/logo_tms.gif"/>
		</c:if>
		 
		 <!-- 	
		 <c:if test="${!empty logoUrl}">
			<img src="${logoUrl}" width="210px" height="35px"/>
		</c:if>
				
		<c:if test="${empty logoUrl}">
			<img src="/design/common/image/logo_tms.gif"/>
		</c:if>
		 -->	
		
		<!-- 
		<div class="logo">
			<div class="logo_area">
				<a href="/common/welcome.do">
				<c:if test="${empty logoUrl}">
				<img src="/design/common/image/logo_tms.gif" height="33"/>
				</c:if>						
				<c:if test="${!empty logoUrl}">
				<img src="${logoUrl}" height="33px"/>						 
				</c:if>
				</a>
			</div>
		</div>
		 -->
				
		</div>
		<div class="login_title">
			<h2><tctl:msg key="login.001" bundle="common"/></h2>
			
			<p class="language">
				<select name="language">
					<option value=""><tctl:msg key="login.008" bundle="common"/></option>
					<option value="ko"><tctl:msg key="conf.profile.23" bundle="setting"/></option>
					<option value="en"><tctl:msg key="conf.profile.24" bundle="setting"/></option>
					<option value="jp"><tctl:msg key="conf.profile.language.japanese" bundle="setting"/></option>
				</select>
			</p>
		</div>
		
		<tctl:pkiCheck msie="true" loginMode="PKI">
		<div style="height:18px;margin-top:3px;">
			<div style="position: absolute;">
				<div id="pkiLBtn" class="optionLoginOn" onclick="toogleLoginMode('pki')"><tctl:msg key="login.010" bundle="common"/></div>
				<div id="normalLBtn" class="optionLoginOff" style="margin-left:2px" onclick="toogleLoginMode('normal')"><tctl:msg key="login.011" bundle="common"/></div>
			</div>
		</div>
		</tctl:pkiCheck>
		
		<div class="login_body" style="border:1px solid #D9D9D9;">			
			<div style="border:1px solid #e8e8e8;margin:50px 50px;">
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
					<div style="padding:20px 12px 10px 12px">
						<table style="width:100%">
							<tbody>
								<tr>
									<td align="right">
										<input id="id" name="id" type="text" class="IP100px" style="width:215px;ime-mode:inactive;" onblur="javascript:displayIdPass();" onclick="javascript:mouseUpObj('id');" onkeyup="javascript:displayIdPass();" tabIndex="1">
									</td>
									<td style="padding-left:11px;">
										<input id="domain" name="domain" type="hidden" class="IP100px" value="${mailDomain}">
										<label onclick="changeLoginOption('saveIdImg')">
										<img id="saveIdImg" src="/design/default/image/icon/icon_login_off.gif">									
										<tctl:msg key="login.002" bundle="common"/>
										</label>
									</td>
								</tr>
								<tr>
									<td align="right">
										<input id="pass" name="pass" type="password" class="IP100px" style="width:215px" tabIndex="2" onblur="javascript:displayIdPass();" onclick="javascript:mouseUpObj('pass');" onkeyup="javascript:displayIdPass();" onKeyPress="(keyEvent(event) == 13) ? checkAuth() : '';"  autocomplete="off">
									</td>
									<td style="padding-left:11px;">
										<a href="javascript:checkAuth()" class="btn_login" tabIndex="3"><span><tctl:msg key="comn.login" bundle="common"/></span></a>			
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
						<c:if test="${loginConfigList['search_id_option'] == 'on' && !ssnNotUse}">
							<a href="javascript:searchId()"><tctl:msg key="login.004" bundle="common"/></a>
							<span>|</span>
						</c:if>
						<c:if test="${loginConfigList['search_password_option'] == 'on'}">
							<a href="javascript:searchPassword()"><tctl:msg key="login.005" bundle="common"/></a>
						</c:if>
						<br/>						
					</div>
				</div>				
				
			</div>
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

<div id="idLayer" style="display:none;position:absolute;color:#CCCCCC;font-style:oblique;" onclick="onClickIdPass('idLayer');"><tctl:msg key="register.021" bundle="common"/></div>
<div id="passLayer" style="display:none;position:absolute;color:#CCCCCC;font-style:oblique;" onclick="onClickIdPass('passLayer');"><tctl:msg key="conf.pop.22" bundle="setting"/></div>
 
<div id="timeOutNotice"  title="<tctl:msg key="auth.timeout.title" bundle="common"/>" style="display: none">	
	<div class="TM_modalMessage">			
		<div class="TM_timeout_message">				
			<div class="TM_alert_title"><tctl:msg key="auth.timeout.001" bundle="common"/></div>
			<div id="timeoutMsg"></div>
		</div>
	</div>		
</div>
<%@include file="/common/xecureOcx.jsp" %>
<div id="loginNotice" title="" style="display:none">
    <iframe id="loginNoticeIframe" name="loginNoticeIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
</body>
</html>