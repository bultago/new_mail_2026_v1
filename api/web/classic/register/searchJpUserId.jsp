<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/design/default/css/popup_style.css" />

<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>

</head>

<script language="javascript">
jQuery().ready(function(){
	<c:if test="${domainType == 'single' || domainInputType != 'input'}">
	jQuery("#mailDomainSelect").selectbox({selectId:"mailDomain",selectFunc:"", height:100},
			"",domainArray);
	</c:if>
});
function init() {
	winResize();
	if(isMsie6){
		window.resizeTo(jQuery(window).width(),262);
	}
	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	jQuery("#birthday").datepick({dateFormat:'yymmdd', yearRange:'-100:+0'});
	jQuery("#datepick-div").css("z-index","10001");
}

function resetForm() {
	var f = document.searchIdForm;
	f.reset();
}

function searchId() {
	var f = document.searchIdForm;

	var domain = trim(f.mailDomain.value);
	var userName = f.userName.value;
	var postal1 = trim(f.homePostCode1.value);
	var postal2 = trim(f.homePostCode2.value);
	var postalCode = postal1+"-"+postal2;
	var birthday = trim(f.birthday.value);

	if (!isDomain(domain)) {
		alert(comMsg.error_domain);
		f.mailDomain.select();
		return;
	}
	
	if(!checkInputLength("", f.userName, comMsg.register_001, 2, 64)) {
		return;
	}
	if(!checkInputValidate("", f.userName, "userName")) {
		return;
	}
	
	if(!checkInputLength("", f.homePostCode1, comMsg.register_034, 1, 10)) {
		return;
	}
	if(!checkInputLength("", f.homePostCode2, comMsg.register_034, 1, 10)) {
		return;
	}

	if (birthday == "") {
		alert(comMsg.register_035);
		return;
	}

	var param = {"checkType":"jp", "postalCode":postalCode, "userName":userName, "birthday":birthday, "mailDomain":domain};
	var action = "/register/searchUserId.do";
	
	jQuery.post(action, param, 
		function (result) {
		var chk = result.isSuccess;
		if (!chk) {
			if (result.msg == "empty") {
				alert(comMsg.error_domain_info);
			} else if (result.msg == "fail") {
				alert(comMsg.register_015);
			} else {
				alert(comMsg.error_msg_001);
			}
		}
		else {
			alert(msgArgsReplace(comMsg.register_014,[userName, result.msg]));
		}
	}, "json");
}
</script>

<body onload="init()" class="popupBody">

<form name="searchIdForm" onsubmit="return false;">
<input type="hidden" name="postalCode">
	<div class="popup_style1" >
		<div class="popup_style1_title">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="register.030" bundle="common"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>
		
		<table class="TB_popup_style1_body">
			<tbody>
			<tr>
			<td class="bg_left"/>
			<td class="popup_content">
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
					<tr>
					<th class="lbout">* <tctl:msg key="conf.userinfo.name" bundle="setting"/></th>
					<td><input type="text" name="userName" class="IP200"></td>
					</tr>
					<tr>
						<th class="lbout">* <tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
						<td>
							<input type="text" id="home_post_code1" name="homePostCode1" class="IP50" maxlength="4"> - 
							<input type="text" id="home_post_code2" name="homePostCode2" class="IP50" maxlength="4">
						</td>
					</tr>
					<tr>
						<th class="lbout">* <tctl:msg key="conf.userinfo.basic.birthday" bundle="setting"/></th>
						<td>
							<input type="text" id="birthday" name="birthday" class="IP100px" readonly="readonly">
						</td>
					</tr>		
				</table>
		
				<div class="btnArea">
					<a class="btn_style2" href="#" onclick="searchId()"><span><tctl:msg key="register.030" bundle="common"/></span></a>
					<a class="btn_style3" href="#" onclick="resetForm()"><span><tctl:msg key="register.023" bundle="common"/></span></a>
				</div>
				</td>
				<td class="bg_right"></td>
				</tr>
			</tbody>
		</table>

		<div class="popup_style1_down">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>