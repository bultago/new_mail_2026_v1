<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<style type="text/css">
			ul li {padding:3px;}
		</style>
	</head>
	<script type="text/javascript">
	function init(){
		if(checkOS() == "android"){
			eval("window.TMSMobile.getAuthKey('setAuthKey','','','')");
		}else{
			window.location = "tmsmobile://getAuthKey?setAuthKey&uid&folder&part";
		}
	}
	
	function setAuthKey(r){
		authKey = r.authKey;
	}
	
	function changeUserAccount() {
		var url = "/hybrid/changeUserAccount.do";
		var paramArray = [];
		paramArray.push({name:"authKey", value:authKey});
		executeUrl(url, paramArray);
	}
	</script>
	
	<body onload="init()">
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
						<ul>
							<li><tctl:msg key="dormant.account.005" bundle="common"/></li>
							<li><tctl:msg key="dormant.account.006" bundle="common"/></li>
							<li><tctl:msg key="dormant.account.007" bundle="common"/></li>
						</ul>
					</h4>
					</div>
				</div>
				<div class="login">
					<div class="lc">
						<a href="javascript:changeUserAccount()" class="btn2"><span><tctl:msg key="dormant.account.009" bundle="common"/></span></a>
					</div>
				</div>
			</div>
		</form>
	</body>
</html>