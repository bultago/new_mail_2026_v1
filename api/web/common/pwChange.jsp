<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/default/css/pwChange_style.css">
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
<script type="text/javascript" defer="defer">
<%
User allowUser = (User) session.getAttribute("allowUser");
System.out.println("allowUser : "+allowUser);
%>
var loginParamterRSAUse = <%=loginParamterRSAUse %>;

function checkAuth () {
	var paid = "<%=sessionID%>";
	var f = document.loginForm;
	var newPassword = jQuery.trim(f.newPassword.value);
	var newConfirmPassword = jQuery.trim(f.newConfirmPassword.value);
	var param = {};

	if(newPassword == ""){
		alert(comMsg.error_login_password);
		f.newPassword.focus();
		return;
	}

	if(newConfirmPassword == ""){
		alert(comMsg.error_login_password);
		f.newConfirmPassword.focus();
		return;
	}
	
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
                    var securedPwd1 = rsa.encrypt(newPassword);
                    var securedPwd2 = rsa.encrypt(newConfirmPassword);
                    
                    param = {"password":securedPwd1, "passwordConfirm":securedPwd2,  "uSeq":"${mailUserSeq}","uid":"${userId}", "name":"${userName}"};
                    setParamPAID(param);
                    jQuery.post("/common/passwordCheck.do", param, 
                       function (res) {
                           if(res.msg != 'success'){
                               alert(res.msg);
                   
                               jQuery("#newPassword").val('');
                               jQuery("#newConfirmPassword").val('');
           
                               jQuery("#newPassword").focus();
                               return false;
                           } else {
                           	f.action = "/setting/passwordChange.do";
                               f.method = "post";
                               f.submit();
                           }
                       },"json");
                }
            , "json");
            
        } catch(err) {
            alert(err);
        }
    } else {
    	param = {"password":newPassword, "passwordConfirm":newConfirmPassword, "uSeq":"${mailUserSeq}","uid":"${userId}", "name":"${userName}"};
        setParamPAID(param);
        jQuery.post("/common/passwordCheck.do", param, 
			function (res) {
			    if(res.msg != 'success'){
			        alert(res.msg);
			
			        f.newPassword.value = "";
			        f.newConfirmPassword.value = "";
			
			        f.newPassword.focus();
			    } else {
			        f.action = "/setting/passwordChange.do";
			        f.method = "post";
			        f.submit();
			    }
			},"json");
    }	
}

function pwChangeNext(){
	var url = "/common/pwChangeNext.jsp";
	var f = document.loginForm;
	f.method = "post";
	f.action = url;
	f.submit();
}
</script>

</head>
<body>
<form name="loginForm">
<input type="hidden" name="userSeq" value="${mailUserSeq}">
<input type="hidden" name="domainSeq" value="${mailDomainSeq}">
<script type="text/javascript">makePAID();</script>

	<div id="logo_pw"></div>
	<div class="pwChange_warp">
		<h3 class="title"><tctl:msg key="password.info.001" bundle="common"/></h3>
		<div class="pwChange_body">
			<div class="body">
				<dl class="title">
					<dt class="icon_mail" />
					<dt class="subject"><tctl:msg key="password.info.002" bundle="common" arg0="${userName}"/></dt>
					<dd>- <tctl:msg key="password.info.003" bundle="common"/></dd>
					<dd>- <tctl:msg key="password.info.004" bundle="common"/></dd>
				</dl>
				<div class="contents">
					<ul class="pwText">
						<li><tctl:msg key="password.info.006" bundle="common"/></li>
						<li><tctl:msg key="password.info.007" bundle="common"/></li>
					</ul>
					<ul class="pwForm">
						<li><input type="password" name="newPassword" autocomplete="off"/> * <tctl:msg key="password.info.008" bundle="common"/></li>
						<li><input type="password" name="newConfirmPassword" autocomplete="off"/></li>
					</ul>
				</div>	
				<div class="warning">
					<p><tctl:msg key="password.info.009" bundle="common"/></p>
				</div>
				<div class="downBtn">
					<a class="btn_style2" href="javascript:checkAuth()"><span><tctl:msg key="password.info.010" bundle="common"/></span></a>
					<a class="btn_style3" href="javascript:pwChangeNext()"><span><tctl:msg key="password.info.011" bundle="common"/></span></a>
				</div>
			</div>
		</div>
		<p class="copyright">${copyright}</p>
	</div>
</form>
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
</body>
</html>