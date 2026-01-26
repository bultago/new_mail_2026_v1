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
	jQuery("#passQuestionSelect").selectbox({selectId:"passQuestionCode",selectFunc:"", height:55},
			"${userInfoVo.passQuestionCode}",passQuestionArray);
});
function init() {
	/*
	winResize();
	if(isMsie6){
		window.resizeTo(jQuery(window).width(),275);
	}
	*/
	if(parent)
		parent.resizeFindPass(500, 190);
}
function resetForm() {
	var f = document.searchPassForm;
	f.reset();
	<c:if  test="${domainType != 'asp'}">
	jQuery("#passQuestionSelect").selectboxSetIndex(0);
	</c:if>
}
function searchPassword() {
	var f = document.searchPassForm;

	var userId = f.userId.value;
	var passCode = f.passQuestionCode.value;
	var passAnswer = f.passAnswer.value;
	
	if(!checkInputLength("", f.userId, comMsg.register_008, 2, 64)) {
		return;
	}
	if(!checkInputValidate("", f.userId, "id")) {
		return;
	}

	if(!checkInputLength("", f.passAnswer, settingMsg.conf_userinfo_msg_03, 2, 128)) {
		return;
	}
	if(!checkInputValidate("", f.passAnswer, "onlyBack")) {
		return;
	}

	var param = {"userId":userId,"passCode":passCode,"passAnswer":passAnswer,"mailDomain":"${fn:escapeXml(mailDomain)}"};

	jQuery.post("/register/searchPasswordProcess.do", param, 
		function (data) {
			if (data.isExist) {
				alert(commonMsg.register_016);
				f.mailDomainSeq.value = data.mailDomainSeq;
				f.mailUserSeq.value = data.mailUserSeq;
				
				f.action = "/register/changePassword.do";
				f.method = "post";
				f.submit();
			}
			else {
				alert(commonMsg.register_017);
				return;
			}
		},"json");
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

<form name="searchPassForm" class="popupBody" onsubmit="return false;">
<input type="hidden" name="mailDomainSeq">
<input type="hidden" name="mailUserSeq">
<input type="hidden" name="mailDomain" value="${fn:escapeXml(mailDomain)}">
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
				<c:if  test="${domainType != 'asp'}">
				<tr>
				<th class="lbout"><tctl:msg key="register.020" bundle="common"/></th>
				<td>${fn:escapeXml(mailDomain)}</td>
				</tr>
				</c:if>
				<tr>
				<th class="lbout">* <tctl:msg key="register.021" bundle="common"/></th>
				<td><input type="text" name="userId" class="IP200"></td>
				</tr>
				<tr>
				<th class="lbout">* <tctl:msg key="conf.userinfo.basic.passquestion" bundle="setting"/></th>
				<td>
					<div id="passQuestionSelect"></div>
					<script type="text/javascript">
						passQuestionArray = [];
						<c:forEach var="passCode" items="${passCodeList}">
							passQuestionArray.push({index:"${passCode.codeName}",value:"${passCode.code}"});
						</c:forEach>
					</script>
				</td>
				</tr>
				<tr>
				<th class="lbout">* <tctl:msg key="conf.userinfo.basic.passanswer" bundle="setting"/></th>
				<td><input type="text" id="passAnswer" name="passAnswer" class="IP200"></td>
				</tr>				
			</table>
		</td>
		<td class="bg_right" style="display:none;"></td>
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