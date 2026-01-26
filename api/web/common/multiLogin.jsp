<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="/design/default/css/login.css">
<title>Tims7 Webmail</title>
<script type="text/javascript" defer="defer">

function gotoLogin (domainName) {
	var f = document.multiLoginForm;
	f.selectDomainName.value = domainName;
	f.action = "/common/welcome.do";
	f.method = "post";
	f.submit();
}

function searchDomain() {
	var f = document.multiLoginForm;
	f.action = "/common/welcome.do";
	f.method = "post";
	f.submit();
}

function init(){}
</script>

</head>
<body onload="init()">
<form name="multiLoginForm">
<input type="hidden" name="selectDomainName">
<input type="hidden" name="currentPage" value="${current}">
	<div class="loginContents">
		<h1><a href="#"><img src="/design/default/image/login/logo.gif"></a></h1>
		<div class="login_title">
			<h2><tctl:msg key="login.001" bundle="common"/></h2>
			<p class="language">
				<span style="color:white"><tctl:msg key="register.020" bundle="common"/> <tctl:msg key="comn.search" bundle="common"/></span>
				<input type="text" name="keyword" class="IP200" value="${keyword}">
				<input type="text" name="_tmp" style="width:0px;display:none">
				<input type="button" value='<tctl:msg key="comn.search" bundle="common"/>' onclick="searchDomain()">
			</p>
		</div>
		<div class="display"><img src="/design/default/image/login/display.jpg" width="760" height="260"></div>

		<div class="body_bar"></div>

		<div class="login_body">			
			<div class="news" style="width:761px;height:100%;border-left:1px solid #D9D9D9;">
				<div>
					<table class="TB_multiTable">
						<tbody>
							<tr>
								<th width="50%"><tctl:msg key="register.020" bundle="common"/></th>
								<th width="50%" class="last"><tctl:msg key="comn.domain.name" bundle="common"/></th>
							</tr>
							<c:forEach var="domain" items="${domainList}" varStatus="loop">
							<tr <c:if test="${loop.last}">class='last'</c:if>>
								<td class="domain">
									<a href="#" onclick="gotoLogin('${domain.mailDomain}')">${domain.mailDomain}</a>
								</td>
								<td class="last">
									${domain.mailDomainName}
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<br>
			<table align="center" style="margin-top:10px;width:100%">
				<tr>
				<c:forEach var="page" items="${pm.pages}">
					<td align="center">
						<c:if test="${page == current}"><font color="blue">${page}</font></c:if>
						<c:if test="${page != current}">${page}</c:if>
					</td>
				</c:forEach>
				</tr>
			</table>
		</div>		
	</div>
</form>
</body>
</html>