<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/default/css/pwChange_style.css">
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript">
function changeUserAccount() {
	var f = document.loginForm;
	
	f.action = "/common/changeUserAccount.do";
	f.method = "post";
	f.submit();
}

</script>

</head>
<body>
<form name="loginForm">
	<div id="logo_pw"></div>
	<div class="pwChange_warp">
		<h3 class="title"><tctl:msg key="dormant.account.001" bundle="common"/></h3>
		<div class="pwChange_body">
			<div class="body">
				<dl class="title">
					<dt class="icon_dormant" />
					<dt class="subject"><tctl:msg key="dormant.account.002" bundle="common" arg0="${userName}"/></dt>
					
					<dd><c:if test="${manage eq 'on'}">- <tctl:msg key="dormant.account.003" bundle="common" arg0="${month}"/></c:if><c:if test="${manage ne 'on'}">&nbsp;</c:if></dd>
					
					<dd>- <tctl:msg key="dormant.account.004" bundle="common"/></dd>
				</dl>
				<div class="contents">
					<ul class="pwText" style="float:none;">
						<li><tctl:msg key="dormant.account.005" bundle="common"/></li>
						<li><tctl:msg key="dormant.account.006" bundle="common"/></li>
						<li><tctl:msg key="dormant.account.007" bundle="common"/></li>
					</ul>
				</div>	
				<c:if test="${manage eq 'on'}">
				<div class="warning">
					<p><tctl:msg key="dormant.account.008" bundle="common" arg0="${changeMonth}"/></p>
				</div>
				</c:if>
				<div class="downBtn">
					<a class="btn_style2" href="javascript:changeUserAccount()"><span><tctl:msg key="dormant.account.009" bundle="common"/></span></a>
					<a class="btn_style3" href="/common/logout.do"><span><tctl:msg key="dormant.account.010" bundle="common"/></span></a>
				</div>
			</div>
		</div>
		<p class="copyright">${copyright}</p>
	</div>
</form>

</body>
</html>