<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/common/css/login.css">
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" defer="defer">
var PKI_INFO = <tctl:pkiCheck print="true"/>;
function checkAuth () {
	var f = document.loginForm;
	var action = "/common/empnoLogin.do";
	var empno = jQuery.trim(f.empno.value);
	var password = jQuery.trim(f.pass.value);
	var domain = jQuery.trim(f.domain.value);

	if (trim(f.empno.value) == "") {
		alert(comMsg.error_login_empno);
		f.empno.focus();
		return;
	}

	if (trim(f.pass.value) == "") {
		alert(comMsg.error_login_password);
		f.pass.focus();
		return;
	}

	if(jQuery("#saveIdImg").attr("check") == "on"){
		setCookie("TSID",empno,365);
	} else {
		setCookie("TSID",empno,-1);
	}

	if(jQuery("#saveDomainImg").attr("check") == "on"){
		setCookie("TSDOMAIN",domain,365);
	} else {
		setCookie("TSDOMAIN",domain,-1);
	}

	if(jQuery("#secureImg").attr("check") == "on"){
		action = "https://"+this.location.host + action;
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
}

function checkCookieSetting(){
	var f = document.loginForm;
	var idFromCookie = getCookie("TSID");
	var domainFromCookie = getCookie("TSDOMAIN");
	if(idFromCookie != ""){
		f.empno.value = idFromCookie;
		jQuery("#saveIdImg").attr("check", "on");
		jQuery("#saveIdImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
	}
		
	if(domainFromCookie != ""){
		jQuery("#saveDomainImg").attr("check", "on");
		jQuery("#saveDomainImg").attr("src", "/design/default/image/icon/icon_login_on.gif");
		checkDomain(domainFromCookie);
				
		if(idFromCookie != ""){
			f.pass.focus();
		} else {
			f.empno.focus();
		}
	} else {
		if(idFromCookie != ""){
			f.domain.focus();
		} else {
			f.empno.focus();
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
	
	openSimplePopup(url,"500px","100px",true);
}

function searchId() {
	var url= "/register/searchUserIdWin.do";
	
	openSimplePopup(url,"500px","100px",true);
}

function searchPassword() {
	var url= "/register/searchPasswordWin.do";
	
	openSimplePopup(url,"600px","100px",true);
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
		<c:if test="${isExistloginTop}">
			<img src="${loginTopSrc}" width="210px" height="35px"/>
		</c:if>
		<c:if test="${!isExistloginTop}">
			<img src="/design/common/image/logo_tms.gif"/>
		</c:if>
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
		
		<div class="display" id="middleImg">
		<c:if test="${isExistloginMiddle}">
			<img src="${loginMiddleSrc}" width="760px"/>
		</c:if>
		<c:if test="${!isExistloginMiddle}">
			<img src="/design/common/image/login/display.jpg" width="760" height="260">
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
					<div class="title" style="text-align:center;color:#898989;padding-top:5px;height:22px;line-height:22px">
						<label onclick="changeLoginOption('secureImg')">
						<img id="secureImg" src="/design/default/image/icon/icon_login_off.gif">							
						<tctl:msg key="login.006" bundle="common"/>
						</label>
						&nbsp;
						<label onclick="changeLoginOption('saveIdImg')">
						<img id="saveIdImg" src="/design/default/image/icon/icon_login_off.gif">									
						<tctl:msg key="login.012" bundle="common"/>
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
									<td align="right"><input id="empno" name="empno" type="text" class="IP100px" style="width:110px" tabIndex="1"></td>
									<td style="white-space:nowrap;padding-left:10px">
										<c:if test="${loginConfigList['domain_type'] != 'single'}">
											<c:if test="${loginConfigList['domain_input_type'] != 'input'}">
												<select id="domain" name="domain" style="width:100px;" title="${domain.mailDomain}" tabIndex="3">
													<c:forEach var="domain" items="${domainList}">
														<option value="${domain.mailDomain}" title="${domain.mailDomain}">${domain.mailDomain}</option>
													</c:forEach>
												</select>
											</c:if>
											<c:if test="${loginConfigList['domain_input_type'] == 'input'}">
												<input id="domain" name="domain" type="text" class="IP100px" tabIndex="3">
											</c:if>
										</c:if>
										<c:if test="${loginConfigList['domain_type'] == 'single'}">
											<select id="domain" name="domain" style="width:100px;" title="${selectDomainName}" tabIndex="3">
												<option value="${selectDomainName}" title="${selectDomainName}">${selectDomainName}</option>
											</select>
										</c:if>
									</td>
								</tr>
								<tr>
									<td align="right"><input id="pass" name="pass" type="password" class="IP100px" style="width:110px" tabIndex="2" onKeyPress="(keyEvent(event) == 13) ? checkAuth() : '';" autocomplete="off"></td>
									<td style="padding-left:11px;">
										<a href="javascript:checkAuth()" class="btn_login" tabIndex="4"><span><tctl:msg key="comn.login" bundle="common"/></span></a>			
									</td>
								</tr>
							</tbody>
						</table>
					</div>

					<div class="optionArea">
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
					</div>
				
				</div>				
				
				
				
				<div class="s_help">
					<p class="title"><tctl:msg key="login.007" bundle="common"/></p>
					<p class="contents"></p>
					<div class="noticeCotent div_scroll">
						${noticeContent}
					</div>
				</div>
				
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
<%@include file="/common/xecureOcx.jsp" %>
<div id="loginNotice" title="" style="display:none">
    <iframe id="loginNoticeIframe" name="loginNoticeIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>
</body>
</html>