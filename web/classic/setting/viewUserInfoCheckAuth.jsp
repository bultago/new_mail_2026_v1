<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
response.setHeader("cache-control","no-cache"); 
response.setHeader("expires","0"); 
response.setHeader("pragma","no-cache");
%>
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language=javascript src="/js/ext-lib/common-aes.js"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
<script language = "javascript">
var loginParamterRSAUse = <%=loginParamterRSAUse %>;

function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery("#userinfo_menu").addClass("on");
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");	

	jQuery("#user_setting").val = USERNAME;
	jQuery("#userinfo_name").val = USERNAME;

}

<%@ include file="settingFrameScript.jsp" %>

function checkAuth() {
	var f = document.authForm;
	var prePasswd = jQuery("#prePasswd").val();

	if (trim(prePasswd) == "") {
		alert(settingMsg.conf_profile_17);
		jQuery("#prePasswd").focus();
		return;
	}
	jQuery('#passwd').val(prePasswd);
	
	f.action = "/setting/checkUserInfoAuth.do";
	
	if(loginParamterRSAUse){
	    try {
	    	var mailPassword = trim(prePasswd);
	    	f.prePasswd.value = "";
	        
	        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
	        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
	        var rsa = new RSAKey();
	        rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

	        // 비밀번호를 RSA로 암호화한다.
	        var securedPassword = rsa.encrypt(mailPassword);

	        // POST 폼에 값을 설정하고 발행(submit) 한다.
	        var securedLoginForm = document.getElementById("securedLoginForm");
	        securedLoginForm.action = "/setting/checkUserInfoAuth.do";
	        securedLoginForm.securedPassword.value = securedPassword;
	        <%if(isMsie && ExtPartConstants.isXecureWebUse()){%>
			XecureSubmit(f);
			<%} else {%>	
			securedLoginForm.submit();
			<%}%>
	    } catch(err) {
	        alert(err);
	    }
	}else{
		<%if(isMsie && ExtPartConstants.isXecureWebUse()){%>
		XecureSubmit(f);
		<%} else {%>	
		f.submit();
		<%}%>
	}
}

function resetAuth() {
	jQuery("#prePasswd").val("");
}

function start() {
	jQuery("#prePasswd").focus();
}

onloadRedy("init()");
</script>

</head>

<body onload="start()">
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="authForm" method="post" onsubmit="return false;">
	<input type="hidden" name="email" value="${email}"/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">					
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="menu_conf.modify" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
				
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.mmenu.9" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >			
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar"><span><tctl:msg key="menu_conf.modify" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th width="130px"><span><tctl:msg key="conf.userinfo.email" bundle="setting"/></span></th>
							<td>${email}</td>
						</tr>
						<tr>
							<th width="130px"><span><tctl:msg key="conf.userinfo.name" bundle="setting"/></span></th>
							<td><div id="userinfo_name" >${name}</div></td>
						</tr>
						<tr>
							<th width="130px"><span><tctl:msg key="conf.userinfo.pass" bundle="setting"/></span></th>
							<td>
								<input type="password" id="prePasswd" name="prePasswd" class="IP100px" onKeyPress="(keyEvent(event) == 13) ? checkAuth() : '';" autocomplete="off">
								<input type="hidden" id="passwd" name="passwd">
							</td>
						</tr>
					</table>
					<table collspan="0" cellpadding="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="checkAuth()"><span><tctl:msg key="conf.userinfo.checkauth" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetAuth()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						</td></tr>
					</table>
					</div>
					</div>	
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>		
</form>
</div>
<% if(loginParamterRSAUse){ %>
<%
String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
%>
<form id="securedLoginForm" name="securedLoginForm" action="" method="post" style="display: none;">
    <input type="hidden" name="securedPassword" id="securedPassword" value="" />
    <input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
	<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
	<input type="hidden" name="email" value="${email}"/>
</form>
<% } %>
<%@include file="/common/bottom.jsp"%>

</body>
</html>