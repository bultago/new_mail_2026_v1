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
		action = "https://"+this.location.host + action;
	}
	
	f.action = action;
	f.method = "post";
	f.submit();
}
	

</script>

</head>
<body onload="init()">
<form name="loginForm">
	<div class="loginContents">
		<h1><a href="#"><img src="/design/common/image/logo_tms.gif"></a></h1>
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
		
		<div class="display"><img src="/design/common/image/login/display.jpg" width="760" height="260"></div>
		
		<div class="body_bar"></div>
		
		<div class="login_body">			
			<div class="news loginBody">
				<div class="title" style="text-align:center;color:#898989;padding-top:5px;height:22px;line-height:22px">
					<label onclick="changeLoginOption('secureImg')">
					<img id="secureImg" src="/design/default/image/icon/icon_login_off.gif">							
					<tctl:msg key="login.006" bundle="common"/>
					</label>
					&nbsp;
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
			
				
				<div class="optionArea">
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
</body>
</html>