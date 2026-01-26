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
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language = "javascript">
function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery("#setting_menu").addClass("on");

	jQuery.removeProcessBodyMask();

	var homePostalCode = jQuery("#homePostalCode").val();
	var officePostalCode = jQuery("#officePostalCode").val();

	if (homePostalCode.indexOf('-') != -1) {
		var homePostalData = homePostalCode.split('-');
		jQuery("#home_post_code1").val(homePostalData[0]);
		jQuery("#home_post_code2").val(homePostalData[1]);
	}

	if (officePostalCode.indexOf('-') != -1) {
		var officePostalData = officePostalCode.split('-');
		jQuery("#company_post_code1").val(officePostalData[0]);
		jQuery("#company_post_code2").val(officePostalData[1]);
	}
}

<%@ include file="settingFrameScript.jsp" %>

function toggleMenu(id) {
	jQuery("#"+id).toggle();
}

function viewSearchZipcode(type) {
	var param = {"type":type};
	var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3",
		minHeight: 520,
		minWidth:450,
		openFunc:function(){
			jQuery("#search_zipcode_div").load("/setting/viewZipcode.do", param);	
		},
		closeFunc:function(){
			jQuery("#search_zipcode_div").empty();
		}
	};	
	jQuery("#search_zipcode_pop").jQpopup("open",popupOpt);
}

function selectZipcode(type, zipcode, sido, gugun, dong, bunji) {

	var zipcodeParts = zipcode.split("-");
	var postCode1 = zipcodeParts[0];
	var postCode2 = zipcodeParts[1];
	
	if (type == "home") {
		jQuery("#homePostalCode").val(zipcode);
		jQuery("#home_post_code1").val(postCode1);
		jQuery("#home_post_code2").val(postCode2);
		jQuery("#homeState").val(sido);
		jQuery("#homeCity").val(gugun);
		jQuery("#homeStreet").val(dong);	
	}
	else {
		jQuery("#officePostalCode").val(zipcode);
		jQuery("#company_post_code1").val(postCode1);
		jQuery("#company_post_code2").val(postCode2);
		jQuery("#officeState").val(sido);
		jQuery("#officeCity").val(gugun);
		jQuery("#officeStreet").val(dong);	
	}

	jQuery("#search_zipcode_pop").jQpopup("close");
}

function searchZipcode() {
	var dongObj = jQuery("#dong");
	var dong = dongObj.val();
	var type = jQuery("#searchType").val();

	if(!checkInputLength("jQuery", dongObj, settingMsg.conf_alert_search_empty, 2, 64)) {
		return;
	}
	if(!checkInputValidate("jQuery", dongObj, "onlyBack")) {
		return;
	}
	
	var param = {"type":type, "dong":dong};
	jQuery("#search_zipcode_div").load("/setting/viewZipcode.do", param);
}

function moveto_page(currentPage) {
	var dongObj = jQuery("#prekeyword");
	var dong = dongObj.val();
	var type = jQuery("#searchType").val();

	var param = {"type":type, "dong":dong, "currentPage":currentPage};
	jQuery("#search_zipcode_div").load("/setting/viewZipcode.do", param);
}

function checkName() {
	var lastName = jQuery("#lastName").val();
	var middleName = jQuery("#middleName").val();
	var firstName = jQuery("#firstName").val();

	lastName = lastName + " ";
	
	if (middleName != "") {
		middleName = middleName + " ";
	}
				
	jQuery("#memberName").val(lastName+middleName+firstName);
}

function saveVcardInfo() {
	var f = document.vcardForm;

	if(!checkInputLength("", f.lastName, comMsg.register_001, 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.lastName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.firstName, comMsg.register_001, 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.firstName, "userName")) {
		return;
	}
	
	if(!checkInputLength("", f.middleName, comMsg.register_001, 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.middleName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.memberName, comMsg.register_001, 2, 255)) {
		return;
	}
	if(!checkInputValidate("", f.memberName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.nickName, comMsg.register_001, 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.nickName, "userName")) {
		return;
	}

	if (trim(f.memberEmail.value) != "") {
		if(!isEmail(f.memberEmail.value)){
			alert(settingMsg.common_2);
			return;
		}
	}

	if(!checkInputLength("", f.mobileNo, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.mobileNo, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.description, "", 0, 512)) {
		return;
	}
	/*if(!checkInputValidate("", f.description, "onlyBack")) {
		return;
	}*/
	
	if(!checkInputLength("", f.homeCountry, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.homeCountry, "case4")) {
		return;
	}

	<c:if test="${installLocale eq 'jp'}">
		if(!checkInputLength("", f.homeState, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.homeState, "case4")) {
			return;
		}
		if(!checkInputLength("", f.homeCity, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.homeCity, "case4")) {
			return;
		}
		if(!checkInputLength("", f.homeStreet, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.homeStreet, "case4")) {
			return;
		}
	</c:if>
	if(!checkInputLength("", f.homeExtAddress, "", 0, 128)) {
		return;
	}
	if(!checkInputValidate("", f.homeExtAddress, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.homeTel, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.homeTel, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.homeFax, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.homeFax, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.privateHomepage, "", 0, 255)) {
		return;
	}
	if(!checkInputValidate("", f.privateHomepage, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.companyName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.companyName, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.departmentName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.departmentName, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.titleName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.titleName, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.officeCountry, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.officeCountry, "case4")) {
		return;
	}

	<c:if test="${installLocale eq 'jp'}">
		if(!checkInputLength("", f.officeState, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.officeState, "case4")) {
			return;
		}
		if(!checkInputLength("", f.officeCity, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.officeCity, "case4")) {
			return;
		}
		if(!checkInputLength("", f.officeStreet, "", 0, 64)) {
			return;
		}
		if(!checkInputValidate("", f.officeStreet, "case4")) {
			return;
		}
	</c:if>
	
	if(!checkInputLength("", f.officeExtAddress, "", 0, 128)) {
		return;
	}
	if(!checkInputValidate("", f.officeExtAddress, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.officeTel, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.officeTel, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.officeFax, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.officeFax, "onlyBack")) {
		return;
	}

	if(!checkInputLength("", f.officeHomepage, "", 0, 255)) {
		return;
	}
	if(!checkInputValidate("", f.officeHomepage, "onlyBack")) {
		return;
	}

	var hpcode1 = jQuery.trim(jQuery("#home_post_code1").val());
	var hpcode2 = jQuery.trim(jQuery("#home_post_code2").val());

	if ((hpcode1 != '') || (hpcode2 != '')) {
		jQuery("#homePostalCode").val(hpcode1+"-"+hpcode2);
	} else {
		jQuery("#homePostalCode").val("");
	}

	var cpcode1 = jQuery.trim(jQuery("#company_post_code1").val());
	var cpcode2 = jQuery.trim(jQuery("#company_post_code2").val());
	
	if ((cpcode1 != '') || (cpcode2 != '')) {
		jQuery("#officePostalCode").val(cpcode1+"-"+cpcode2);
	} else {
		jQuery("#officePostalCode").val("");
	}

	f.action = "/setting/modifyVcardInfo.do";
	f.method = "post";
	f.submit();
}

function gotoUserSetting() {
	this.location = "/setting/viewSetting.do";
}

onloadRedy("init()");
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="vcardForm">
<input type="hidden" id="homePostalCode" name="homePostalCode" value="${vcardVo.homePostalCode}">
<input type="hidden" id="officePostalCode" name="officePostalCode" value="${vcardVo.officePostalCode}">
<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.sign.13" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
				
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.vcard.title" bundle="setting"/></li>
				</ul>
			</div>
	
			<div style="margin-top: 5px;"></div>

			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div class="title_bar"><span><a href="javascript:;;" onclick="toggleMenu('basic_info')"><tctl:msg key="conf.userinfo.basic.menu" bundle="setting"/></a></span></div>
					<table id="basic_info" class="TB_cols2" cellpadding="0" cellspacing="0">
						<colgroup span="2">
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><tctl:msg key="conf.userinfo.basic.nameinfo" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.basic.lastname" bundle="setting"/> <input type="text" id="lastName" name="lastName" class="IP50" onkeyup="checkName()" value="${vcardVo.lastName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.firstname" bundle="setting"/> <input type="text" id="firstName" name="firstName" class="IP50" onkeyup="checkName()" value="${vcardVo.firstName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.middlename" bundle="setting"/> <input type="text" id="middleName" name="middleName" class="IP50" onkeyup="checkName()" value="${vcardVo.middleName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.displayname" bundle="setting"/> <input type="text" id="memberName" name="memberName" class="IP100px" value="${vcardVo.memberName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.basic.nickname" bundle="setting"/></th>
							<td>
								<input type="text" id="nickName" name="nickName" class="IP200" value="${vcardVo.nickName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.email" bundle="setting"/></th>
							<td>
								<input type="text" id="memberEmail" name="memberEmail" class="IP200" value="${vcardVo.memberEmail}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.basic.mobile" bundle="setting"/></th>
							<td>
								<input type="text" id="mobileNo" name="mobileNo" class="IP200" value="${vcardVo.mobileNo}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.basic.memo" bundle="setting"/></th>
							<td>
								<textarea name="description" id="description" rows="10" style="width:58%;">${vcardVo.description}</textarea>
							</td>
						</tr>
					</table>

					<div class="title_bar"><span><a href="javascript:;;" onclick="toggleMenu('home_info')"><tctl:msg key="conf.userinfo.private.menu" bundle="setting"/></a></span></div>
					<table id="home_info" class="TB_cols2" cellpadding="0" cellspacing="0" style="display:none">
						<colgroup span="2">
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
							<td>
								<input type="text" id="home_post_code1" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>> - 
								<input type="text" id="home_post_code2" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>>
								<c:if test="${installLocale ne 'jp'}">
									<a class="btn_basic" href="javascript:;;" onclick="viewSearchZipcode('home')"><span><tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/></span></a>
								</c:if>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.country" bundle="setting"/> <input type="text" id="homeCountry" name="homeCountry" class="IP50" value="${vcardVo.homeCountry}">
								<tctl:msg key="conf.userinfo.state" bundle="setting"/>  <input type="text" id="homeState" name="homeState" class="IP50" value="${vcardVo.homeState}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.city" bundle="setting"/>  <input type="text" id="homeCity" name="homeCity" class="IP50" value="${vcardVo.homeCity}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.street" bundle="setting"/>  <input type="text" id="homeStreet" name="homeStreet" class="IP200" value="${vcardVo.homeStreet}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.extaddress" bundle="setting"/></th>
							<td>
								<input type="text" id="homeExtAddress" name="homeExtAddress" class="IP300" value="${vcardVo.homeExtAddress}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.telephone" bundle="setting"/></th>
							<td>
								<input type="text" id="homeTel" name="homeTel" class="IP200" value="${vcardVo.homeTel}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.fax" bundle="setting"/></th>
							<td>
								<input type="text" id="homeFax" name="homeFax" class="IP200" value="${vcardVo.homeFax}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.private.homepage" bundle="setting"/></th>
							<td>
								<input type="text" id="privateHomepage" name="privateHomepage" class="IP200" value="${vcardVo.privateHomepage}">
							</td>
						</tr>
					</table>
				
					<div class="title_bar"><span><a href="javascript:;;" onclick="toggleMenu('company_info')"><tctl:msg key="conf.userinfo.company.menu" bundle="setting"/></a></span></div>
					<table id="company_info" class="TB_cols2" cellpadding="0" cellspacing="0" style="display:none">
						<colgroup span="2">
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><tctl:msg key="conf.userinfo.companyname" bundle="setting"/></th>
							<td>
								<input type="text" id="companyName" name="companyName" class="IP200" value="${vcardVo.companyName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.departmentname" bundle="setting"/></th>
							<td>
								<input type="text" id="departmentName" name="departmentName" class="IP200" value="${vcardVo.departmentName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.codeclass" bundle="setting"/></th>
							<td>
								<input type="text" id="titleName" name="titleName" class="IP200" value="${vcardVo.titleName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
							<td>
								<input type="text" id="company_post_code1" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>> - 
								<input type="text" id="company_post_code2" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>>
								<c:if test="${installLocale ne 'jp'}">
									<a class="btn_basic" href="javascript:;;" onclick="viewSearchZipcode('company')"><span><tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/></span></a>
								</c:if>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.country" bundle="setting"/>  <input type="text" id="officeCountry" name="officeCountry" class="IP50" value="${vcardVo.officeCountry}">
								<tctl:msg key="conf.userinfo.state" bundle="setting"/>  <input type="text" id="officeState" name="officeState" class="IP50" value="${vcardVo.officeState}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.city" bundle="setting"/>  <input type="text" id="officeCity" name="officeCity" class="IP50" value="${vcardVo.officeCity}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.street" bundle="setting"/>  <input type="text" id="officeStreet" name="officeStreet" class="IP200" value="${vcardVo.officeStreet}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.extaddress" bundle="setting"/></th>
							<td>
								<input type="text" id="officeExtAddress" name="officeExtAddress" class="IP300" value="${vcardVo.officeExtAddress}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.telephone" bundle="setting"/></th>
							<td>
								<input type="text" id="officeTel" name="officeTel" class="IP200" value="${vcardVo.officeTel}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.fax" bundle="setting"/></th>
							<td>
								<input type="text" id="officeFax" name="officeFax" class="IP200" value="${vcardVo.officeFax}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.companyhomepage" bundle="setting"/></th>
							<td>
								<input type="text" id="officeHomepage" name="officeHomepage" class="IP200" value="${vcardVo.officeHomepage}">
							</td>
						</tr>
					</table>
					<div class="btnArea" style="margin-bottom:10px">
						<a class="btn_style2" href="#" onclick="saveVcardInfo()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="gotoUserSetting()"><span><tctl:msg key="common.return" bundle="setting"/></span></a>
					</div>
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>		
</form>
</div>
<div id="search_zipcode_pop" title="<tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/>">
	<div id="search_zipcode_div" ></div>
</div>

<%@include file="/common/bottom.jsp"%>
</body>
</html>