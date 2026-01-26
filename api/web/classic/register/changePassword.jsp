<%@page import="com.terracetech.tims.webmail.util.CryptoSession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/design/default/css/popup_style.css" />

<script type="text/javascript" src="/i18n?bundle=common&var=commonMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
</head>

<script language="javascript">
var loginParamterRSAUse = <%=loginParamterRSAUse %>;

function init() {
	/*
	winResize();
	if(isMsie6){
		window.resizeTo(jQuery(window).width(),275);
	}
	*/
}
function resetForm() {
	var f = document.changePassForm;
	f.reset();
}

function checkPwd () {

	var pwd1 = jQuery("#mailPassword").val();
	var pwd2 = jQuery("#mailPasswordConfirm").val();
	var uSeq = jQuery("#mailUserSeq").val();
	var userId = "";
	var userName = "";
	var param = {};
	
	if (trim(pwd1) == "") {
        saveUserInfo();
    } else {
	    //TCUSTOM-2657
	    if(loginParamterRSAUse){
	        var rsaPublicKeyModulus = "";
	        var rsaPublicKeyExponent = "";  
	        try {
	            jQuery.post("/common/checkRsaPrivateKeyToPwchange.jsp", "",
	                function (data) {
                        rsaPublicKeyModulus = data.rsaPublicKeyModulus;
                        rsaPublicKeyExponent = data.rsaPublicKeyExponent;
	                    
	                    var rsa = new RSAKey();
	                    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);
	
	                    // 사용자ID와 비밀번호를 RSA로 암호화한다.
	                    var securedPwd1 = rsa.encrypt(pwd1);
	                    var securedPwd2 = rsa.encrypt(pwd2);
	                    
	                    param = {"password":securedPwd1, "passwordConfirm":securedPwd2, "uSeq":uSeq,"uid":userId, "name":userName};
	                    setParamPAID(param);
	                    jQuery.post("/common/passwordCheck.do", param, 
	                            function (res) {
	                                if(res.msg != 'success'){
	                                    alert(res.msg);
	                        
	                                    jQuery("#mailPassword").val('');
	                                    jQuery("#mailPasswordConfirm").val('');
	                
	                                    jQuery("#mailPassword").focus();
	                                    return false;
	                                }else{
	                                	changePass();
	                                }
	                            },"json");
	                }
	            , "json");
	            
	        } catch(err) {
	            alert(err);
	        }
	    }else{
	        param = {"password":pwd1, "passwordConfirm":pwd2, "uSeq":uSeq,"uid":userId, "name":userName};
	        setParamPAID(param);
	        jQuery.post("/common/passwordCheck.do", param, 
	            function (res) {
	                if(res.msg != 'success'){
	                    alert(res.msg);
	        
	                    jQuery("#mailPassword").val('');
	                    jQuery("#mailPasswordConfirm").val('');
	
	                    jQuery("#mailPassword").focus();
	                    return false;
	                }else{
	                	changePass();
	                }
	            },"json");
	    }
    }
}

function changePass() {
	var f = document.changePassForm;

	if (trim(f.mailPassword.value) == "") {
		alert(settingMsg.conf_userinfo_msg_07);
		f.mailPassword.focus();
		return;
	}
	
	if (trim(f.mailPasswordConfirm.value) == "") {
		alert(settingMsg.conf_userinfo_msg_04);
		f.mailPasswordConfirm.focus();
		return;
	}

	if (f.mailPassword.value != f.mailPasswordConfirm.value) {
		alert(settingMsg.conf_userinfo_msg_08);
		f.mailPasswordConfirm.focus();
		return;
	}

	if(loginParamterRSAUse){
	    try {
	    	var mailPassword = trim(f.mailPassword.value);
	    	f.mailPassword.value = "";
	        f.mailPasswordConfirm.value = "";
	        
	        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
	        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
	        var rsa = new RSAKey();
	        rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

	        // 비밀번호를 RSA로 암호화한다.
	        var securedPassword = rsa.encrypt(mailPassword);

	        // POST 폼에 값을 설정하고 발행(submit) 한다.
	        var securedLoginForm = document.getElementById("securedLoginForm");
	        securedLoginForm.action = "/register/changePasswordProcess.do";
	        securedLoginForm.securedPassword.value = securedPassword;
	        securedLoginForm.submit();
	    } catch(err) {
	        alert(err);
	    }
	}else{
		f.action = "/register/changePasswordProcess.do";
		f.method = "post";
		f.submit();
	}
}

function closeWin(){
	if(parent){
		parent.modalPopupFindPassClose();
		parent.jQpopupClear();
	}else
		window.close();	
}
</script>

<body onload="init()" class="popupBody" style="background:none;">

<form name="changePassForm" onsubmit="return false;">
<input type="hidden" name="mailDomainSeq" value="${mailDomainSeq}">
<input type="hidden" id="mailUserSeq" name="mailUserSeq" value="${mailUserSeq}">
<script type="text/javascript">makePAID();</script>
<input type="hidden" name="closeJQpopupFunction" id="closeJQpopupFunction" value="parent.modalPopupFindPassClose(); parent.jQpopupClear();" />
<div class="popup_style1" style="border:0px;">
	<div class="popup_style1_title" style="display:none;">
		<div class="popup_style1_title_left">
			<span class="SP_title"><tctl:msg key="register.022" bundle="common"/></span>
		</div>
		<div class="popup_style1_title_right">
			<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
		</div>
	</div>
	
	<table class="TB_popup_style1_body">
		<tbody>
			<tr>
			<td class="bg_left" style="display:none;" ></td>
			<td class="popup_content" style="padding-bottom:0px;">
				<table width="100%" cellpadding="0" cellspacing="0" class="jq_innerTable">
					<col width="200px"></col>
					<col></col>
					<tr>
					<th class="lbout"><tctl:msg key="register.020" bundle="common"/></th>
					<td>${mailDomain}</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="register.021" bundle="common"/></th>
					<td>${userId}</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.basic.password" bundle="setting"/></th>
					<td><input type="password" id="mailPassword" name="mailPassword" class="IP200" autocomplete="off"></td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.basic.passconfirm" bundle="setting"/></th>
					<td><input type="password" id="mailPasswordConfirm" name="mailPasswordConfirm" class="IP200" autocomplete="off"></td>
					</tr>
				</table>
			</td>
			<td class="bg_right" style="display:none;"></td>
			</tr>
		</tbody>
	</table>
	<div class="btnArea">
		<a class="btn_style2" href="#" onclick="checkPwd()"><span><tctl:msg key="register.022" bundle="common"/></span></a>
		<%--<a class="btn_style3" href="#" onclick="resetForm()"><span><tctl:msg key="register.023" bundle="common"/></span></a>--%>
		<a class="btn_style3" href="#" onclick="closeWin()"><span><tctl:msg key="comn.close" bundle="common"/></span></a>
	</div>
	
	<div class="popup_style1_down" style="display:none;">
		<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
	</div>
</div>
</form>
<% if(loginParamterRSAUse){ %>
<%
String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
%>
<form id="securedLoginForm" name="securedLoginForm" action="" method="post" style="display: none;">
    <input type="hidden" name="securedPassword" id="securedPassword" value="" />
    <input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
	<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
	<input type="hidden" name="mailDomainSeq" value="${mailDomainSeq}">
	<input type="hidden" id="mailUserSeq" name="mailUserSeq" value="${mailUserSeq}">
	<input type="hidden" name="closeJQpopupFunction" id="closeJQpopupFunction" value="parent.modalPopupFindPassClose(); parent.jQpopupClear();" />
</form>
<% } %>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>