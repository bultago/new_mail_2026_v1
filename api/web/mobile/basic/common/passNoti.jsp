<%@page import="com.terracetech.tims.webmail.common.EnvConstants"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="jp" xml:lang="jp">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>		
		<% 
		  boolean loginParamterRSAUse = "true".equalsIgnoreCase(EnvConstants.getMailSetting("login.rsa.encrypt.use"));
		  String sessionID = request.getSession().getId();
		%>
		<% if(loginParamterRSAUse){ %>
		<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
		<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
		<script type="text/javascript" src="/js/rsa/rsa.js"></script>
		<script type="text/javascript" src="/js/rsa/prng4.js"></script>
		<script type="text/javascript" src="/js/rsa/rng.js"></script>
		<% } %>
	</head>
<%
String language = (String) request.getAttribute("language");
if(language == null) language = "ko";
session.setAttribute(I18nConstants.LOCALE_KEY, new Locale(language));
%>
	<script type="text/javascript">
	var PAID = "<%=sessionID%>";
	function makePAID() {document.write('<input type="hidden" id="paid" name="paid" value="'+PAID+'"/>');}
	function setParamPAID(param) {if (!param) param = {};var paid = jQuery("#paid").val();param.paid = paid;}
	var loginParamterRSAUse = <%=loginParamterRSAUse%>
	function changeUserAccount() {
		var f = document.loginForm;
		
		f.action = "/common/changeUserAccount.do";
		f.method = "post";
		f.submit();
	}
	</script>
	<script type="text/javascript" defer="defer">
	function checkAuth () {
		var f = document.loginForm;
		var newPassword = jQuery.trim(f.newPassword.value);
		var newConfirmPassword = jQuery.trim(f.newConfirmPassword.value);
		var mobile = jQuery.trim(f.mobile.value);
		
	
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
	
		var param = {};
		
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
                        
                        param = {"password":securedPwd1, "passwordConfirm":securedPwd2, "uSeq":"${userSeq}","uid":"${userId}", "name":"${userName}", "mailDomainSeq":"${domainSeq}", "mailUserSeq":"${userSeq}", "language":"${language}", "mobile":mobile};
                        setParamPAID(param);
                        jQuery.post("/common/passwordCheck.do", param, 
	                        function (res) {
	                            if(res.msg != 'success'){
	                                alert(res.msg);
	                    
	                                jQuery("#newPassword").val('');
	                                jQuery("#newConfirmPassword").val('');
	            
	                                jQuery("#newPassword").focus();
	                                return false;
	                            }else{
	                            	param = {"password":newPassword, "passwordConfirm":newConfirmPassword, "uSeq":"${userSeq}","uid":"${userId}", "name":"${userName}", "mailDomainSeq":"${domainSeq}", "mailUserSeq":"${userSeq}", "language":"${language}", "mobile":mobile};
	                            	pwChange(param);
	                            }
	                        },"json");
                    }
                , "json");
                
            } catch(err) {
                alert(err);
            }
		} else {
			param = {"password":newPassword, "passwordConfirm":newConfirmPassword, "uSeq":"${userSeq}","uid":"${userId}", "name":"${userName}", "mailDomainSeq":"${domainSeq}", "mailUserSeq":"${userSeq}", "language":"${language}", "mobile":mobile};
            jQuery.post("/common/passwordCheck.do", param, 
				function (res) {
					if(res.msg != 'success'){
						alert(res.msg);
	
						f.newPassword.value = "";
						f.newConfirmPassword.value = "";
	
						f.newPassword.focus();
					}else{
						pwChange(param);
					}
				},"json");
		}
	}
	function pwChange(param) {
		jQuery.post("/mobile/basic/common/pwChangeResult.jsp", param, 
				function (res) {
					if(res.status != 'success'){
						alert(res.msg);

						f.newPassword.value = "";
						f.newConfirmPassword.value = "";

						f.newPassword.focus();
					}else{
						alert(res.msg);
						goLogout();
					}
				},"json");
	}
	function goMobileHome(){
		location.href = "/mobile/common/welcome.do";
	}
	function goLogout(){
		excuteAction("logout");
	}
	</script>
	<body>
		<c:if test="${notiType == 'change'}">
			<div class="header">
				<h1>Terrace Mail Suite</h1>			
				<div class="hh"><h2><tctl:msg key="register.022" bundle="common"/></h2></div>
			</div>		
			<div class="container">
				<div class="alert_title_box_wrap">
					<div class="alert_icon"></div>
					<div class="alert_title_box">
						<h4 style="color:#006ECF"><tctl:msg key="password.info.002" bundle="common" arg0="${userName}"/></h4>
						<p class="scrip" style="margin-bottom:3px;margin-top:3px;"><span><tctl:msg key="password.info.008" bundle="common"/></span></p>
						<form name="loginForm" id="loginForm" method="post">
						<input type="hidden" name="userSeq" value="${userSeq}" />
						<input type="hidden" name="mobile" value="true" />
						<input type="hidden" name="domainSeq" value="${domainSeq}" />
						<table style="table-layout:fixed;width:100%;margin-top:10px;">
							<tr>
								<td style="height:26px;line-height:26px;width:180px;text-align:right;"><tctl:msg key="password.info.006" bundle="common"/></td>
								<td style="height:26px;"><span style="margin-left:10px;"><input type="password" name="newPassword" style="width:150px;" autocomplete="off" /></span></td>
							</tr>
							<tr>
								<td style="height:26px;line-height:26px;width:180px;text-align:right;"><tctl:msg key="password.info.007" bundle="common"/></td>
								<td style="height:26px;"><span style="margin-left:10px;"><input type="password" name="newConfirmPassword" style="width:150px;" autocomplete="off" /></span></td>
							</tr>
						</table>
						</form>
					</div>
				</div>
				<div class="login" style="padding-top:10px;">
					<div class="lc">
					<a href="javascript:checkAuth()" class="btn2"><span><tctl:msg key="password.info.001" bundle="common"/></span></a>
					<a href="/mobile/common/home.do" class="btn2" style="margin-left:10px;"><span><tctl:msg key="password.info.011" bundle="common"/></span></a>
					</div>
				</div>
			</div>
		</c:if>
		<c:if test="${notiType == 'dormant'}">
			<form name="loginForm">
			<div class="header">
				<h1>Terrace Mail Suite</h1>			
				<div class="hh"><h2><tctl:msg key="dormant.account.dormancy" bundle="common"/></h2></div>
			</div>		
			<div class="container">
				<div class="alert_title_box_wrap">
					<div class="alert_icon"></div>
					<div class="alert_title_box">
					<h4 style="color:#006ECF">
						<li><tctl:msg key="dormant.account.005" bundle="common"/></li>
						<li><tctl:msg key="dormant.account.006" bundle="common"/></li>
						<li><tctl:msg key="dormant.account.007" bundle="common"/></li>
					</h4>
					</div>
				</div>
				<div class="login">
					<div class="lc">
						<a href="javascript:changeUserAccount()" class="btn2"><span><tctl:msg key="dormant.account.009" bundle="common"/></span></a>
						<a href="/common/welcome.do" class="btn2"><span><tctl:msg key="dormant.account.010" bundle="common"/></span></a>
					</div>
				</div>
			</div>
			</form>
		</c:if>
	</body>
</html>