<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/design/default/css/popup_style.css" />

<script type="text/javascript" src="/i18n?bundle=common&var=commonMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
</head>

<script language="javascript">
jQuery().ready(function(){
	<c:if test="${domainType == 'single' || domainInputType != 'input'}">
	jQuery("#mailDomainSelect").selectbox({selectId:"mailDomain",selectFunc:"", height:55},
			"",domainArray);
	</c:if>
});
function init() {
	/*
	winResize();
	if(isMsie6){
		window.resizeTo(jQuery(window).width(),185);
	}
	*/
	if(parent)
		parent.setFindPassPopupTitle("<tctl:msg key="register.029" bundle="common"/>");
	
	jQuery(":text:first").focus();
}
function resetForm() {
	var f = document.searchPassForm;
	f.reset();
	jQuery("#mailDomainSelect").selectboxSetIndex(0);
}
function searchPassword() {
	var f = document.searchPassForm;

	var domain = trim(f.mailDomain.value);

	if (!isDomain(domain)) {
		alert(comMsg.error_domain);
		f.mailDomain.select();
		return;
	}
	
	f.action = "/register/searchPassword.do";
	f.submit();
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

<form name="searchPassForm" class="popupBody" method="post" onsubmit="return false;">
<div class="popup_style1" style="border:0px;">
	<div class="popup_style1_title" style="display:none;">
		<div class="popup_style1_title_left">
			<span class="SP_title"><tctl:msg key="register.029" bundle="common"/></span>
		</div>
		<div class="popup_style1_title_right">
			<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
		</div>
	</div>
	
	<table class="TB_popup_style1_body">
		<tbody>
			<tr>
			<td class="bg_left" style="display:none;" />
			<td class="popup_content" style="padding-bottom:0px;">
				<table width="100%" cellpadding="0" cellspacing="0" class="jq_innerTable">
					<col width="200px"></col>
					<col></col>
					<tr>
					<th class="lbout">* <tctl:msg key="register.020" bundle="common"/></th>
					<td>
						<c:if test="${domainType != 'single'}">
							<c:if test="${domainInputType != 'input'}">
								<div id="mailDomainSelect"></div>
								<script type="text/javascript">
									domainArray = [];
									<c:forEach var="domain" items="${domainList}">
										domainArray.push({index:"${domain.mailDomain}",value:"${domain.mailDomain}"});
									</c:forEach>
								</script>
							</c:if>
							<c:if test="${domainInputType == 'input'}">
								<input id="mailDomain" name="mailDomain" type="text" class="IP200">
							</c:if>
						</c:if>
						<c:if test="${domainType == 'single'}">
							<div id="mailDomainSelect"></div>
							<script type="text/javascript">
								domainArray = [];
								domainArray.push({index:"${selectDomainName}",value:"${selectDomainName}"});
							</script>
						</c:if>
					</td>
					</tr>					
				</table>				
			</td>
			<td class="bg_right" style="display:none;" />
			</tr>
		</tbody>
	</table>
	<div class="btnArea">
		<a class="btn_style2" href="#" onclick="searchPassword()"><span><tctl:msg key="register.029" bundle="common"/></span></a>
		<%--<a class="btn_style3" href="#" onclick="resetForm()"><span><tctl:msg key="register.023" bundle="common"/></span></a>--%>
		<a class="btn_style3" href="#" onclick="closeWin()"><span><tctl:msg key="comn.close" bundle="common"/></span></a>
	</div>
	<div class="popup_style1_down" style="display:none;">
		<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
	</div>
</div>
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>