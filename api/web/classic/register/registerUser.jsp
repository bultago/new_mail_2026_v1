<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="xecure.crypto.VidVerifier"%>
<%@page import="xecure.servlet.XecureConfig"%>
<%@ page import="com.terracetech.tims.webmail.util.CryptoSession" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/design/default/css/setting_style.css" />
<link rel="stylesheet" type="text/css" href="/design/default/css/popup_style.css" />

<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
</head>

<script language="javascript">
var PKI_INFO = <tctl:pkiCheck print="true"/>;
var loginParamterRSAUse = <%=loginParamterRSAUse %>;
<%
if(isMsie && 
		ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender() &&
		ExtPartConstants.isPkiLoginUse()){
VidVerifier vid = new VidVerifier(new XecureConfig());
out.println(vid.ServerCertWriteScript());
}
%>
jQuery().ready(function(){
	jQuery("#passQuestionSelect").selectbox({selectId:"passQuestionCode",selectFunc:""},
			"${userInfoVo.passQuestionCode}",passQuestionArray);
	
	if(parent)
		parent.resizeRegisterUser(650, 580);
});

function init() {
	/*
	winResize();
	var width = jQuery("body").innerWidth();
	if (width < 650) {
		window.document.body.scroll = "auto";		
		window.resizeBy(150,0);
	}

	if(!jQuery.browser.msie){		
		jQuery(window).resize(function(){
				jQuery("#wrapper").css("width",jQuery(this).width()+"px");
				jQuery("#wrapper").css("height",jQuery(this).height()+"px");
		});	
	}
	*/

	var postalCode = "${fn:escapeXml(postalCode)}";
	if (postalCode != "") {
		var postalData = postalCode.split("-");
		if (postalData && postalData.length == 2) {
			jQuery("#home_post_code1").val(postalData[0]);
			jQuery("#home_post_code2").val(postalData[1]);
		}
	}
}
function resetForm() {
	var f = document.registerForm;
	f.reset();
	jQuery("#passQuestionSelect").selectboxSetIndex(0);
	if("${installLocale}" == "jp") {
		var postalCode = "${fn:escapeXml(postalCode)}";
		if (postalCode != "") {
			var postalData = postalCode.split("-");
			if (postalData && postalData.length == 2) {
				jQuery("#home_post_code1").val(postalData[0]);
				jQuery("#home_post_code2").val(postalData[1]);
			}
		}
	}
}

function userIdDupCheck() {

	var f = document.registerForm;

	var userIdObj = jQuery("#userId");
	var userId = trim(userIdObj.val());

	if(!checkInputLength("jQuery", userIdObj, comMsg.register_008, 1, 64)) {
		return;
	}
	if(!checkInputValidate("jQuery", userIdObj, "id")) {
		return;
	}
	
	var param = {"mailDomainSeq":"${mailDomainSeq}", "userId":userId};
	setParamPAID(param);
	jQuery.getJSON("/register/userIdDupCheck.do", param, 
		function (data) {
			if (!data.isExist) {
				alert(comMsg.register_009);
				jQuery("#userId").val(userId);
				f.mailUid.value = userId;
				f.userIdCheck.value = "true";
				
			}
			else {
				alert(comMsg.register_010);
				f.mailUid.value = "";
				f.userIdCheck.value = "";
			}
		});
	
}

function checkName() {
	var lastName = jQuery("#lastName").val();
	var middleName = jQuery("#middleName").val();
	var firstName = jQuery("#firstName").val();

	lastName = lastName + " ";
	
	if (middleName != "") {
		middleName = middleName + " ";
	}
				
	jQuery("#userName").val(lastName+middleName+firstName);
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

function checkPwd () {
	var f = $("registerForm");
	var pwd1 = f.mailPassword.value;
	var pwd2 = f.mailPasswordConfirm.value;

	var uSeq = "";
	var userId = jQuery("#userId").val();
	var userName = jQuery("#userName").val();
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
                                    saveRegister();
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
					saveRegister();
				}
			},"json");
    }
	return true;
}

function saveRegister() {
	var f = document.registerForm;

	if(!checkInputLength("", f.userId, comMsg.register_008, 1, 64)) {
		return;
	}
	if(!checkInputValidate("", f.userId, "id")) {
		return;
	}

	if (trim(f.userIdCheck.value) != "true") {
		alert(comMsg.register_011);
		return;
	}

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

	if(!checkInputLength("", f.userName, comMsg.register_001, 2, 64)) {
		return;
	}
	if(!checkInputValidate("", f.userName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.mobileNo, "", 0, 32)) {
		return;
	}
	if(!checkInputValidate("", f.mobileNo, "onlyBack")) {
		return;
	}
	
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
	
	if (trim(f.passAnswer.value) == "") {
		alert(settingMsg.conf_userinfo_msg_03);
		f.passAnswer.focus();
		return;
	}

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

	var hpcode1 = jQuery.trim(jQuery("#home_post_code1").val());
	var hpcode2 = jQuery.trim(jQuery("#home_post_code2").val());

	if ((hpcode1 != '') || (hpcode2 != '')) {
		jQuery("#homePostalCode").val(hpcode1+"-"+hpcode2);
	} else {
		jQuery("#homePostalCode").val("");
	}

	if(loginParamterRSAUse){
        var rsaPublicKeyModulus = "";
        var rsaPublicKeyExponent = "";  
        try {
            jQuery.post(
                 "/common/checkRsaPrivateKey.jsp", 
                 "",
                function (data) {
                    if(data.existPrivateKey == "true"){
                        rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
                        rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
                    }else{
                        rsaPublicKeyModulus = data.rsaPublicKeyModulus;
                        rsaPublicKeyExponent = data.rsaPublicKeyExponent;
                    }
                    
                    var rsa = new RSAKey();
                    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);

                    // 비밀번호를 RSA로 암호화한다.
                    var securedPassword = rsa.encrypt(f.mailPassword.value);
                    jQuery("#securedPassword").val(securedPassword);
                    f.mailPassword.value = "";
                    
                    if(f.ssn) {
	        			f.ssn.value = rsa.encrypt(f.ssn.value);
			        }
                    
                    f.action = "/register/saveUserInfo.do";
                    f.method = "post";
                    
					if(isMsie && PKI_INFO.useage == "enable"){
						if(PKI_INFO.vender == "SOFTFORUM"){
							if(PKI_INFO.mode == "EPKI"){
								f.pkiSignText.value = Sign_with_vid_web(0, "REGISTTEMPPKI"+makeRandom(), s, replaceAll(f.ssn.value,"-","")); 
								f.pkiVidText.value = send_vid_info();
							}
							XecureSubmit(f);
						} else if(PKI_INFO.vender == "INITECH_V7"){
							f.submit();
						} else {
							f.submit();
						}	
					} else {
						f.submit();
					}
                }
            , "json");
            
        } catch(err) {
            alert(err);
        }
    } else {
		f.action = "/register/saveUserInfo.do";
		f.method = "post";
		if(isMsie && PKI_INFO.useage == "enable"){
			if(PKI_INFO.vender == "SOFTFORUM"){
				if(PKI_INFO.mode == "EPKI"){
					f.pkiSignText.value = Sign_with_vid_web(0, "REGISTTEMPPKI"+makeRandom(), s, replaceAll(f.ssn.value,"-","")); 
					f.pkiVidText.value = send_vid_info();
				}
				XecureSubmit(f);
			} else if(PKI_INFO.vender == "INITECH_V7"){
				f.submit();
			} else {
				f.submit();
			}	
		} else {
			f.submit();
		}
    }
	
}

function putUserId(userId) {
	jQuery("#userId").val(userId);
}

function closeWin(){
	if(parent){
		parent.modalPopupForRegisterUserClose();
		parent.jQpopupClear();
	}else
		window.close();	
}
</script>

<body onload="init()" class="popupBody" style="background:none;">

<div id="wrapper" style="overflow:auto;">
<form name="registerForm" id="registerForm" onsubmit="return false;">
<input type="hidden" id="homePostalCode" name="homePostalCode">
<input type="hidden" name="ssn" value="${fn:escapeXml(ssn)}">
<input type="hidden" id="mailDomain" name="mailDomain" value="${fn:escapeXml(mailDomain)}">
<input type="hidden" name="empNo" value="${fn:escapeXml(empno)}">
<input type="hidden" name="birthday" value="${fn:escapeXml(birthday)}">
<input type="hidden" name="empNo" value="${fn:escapeXml(empno)}">
<input type="hidden" name="mailUid">
<input type="hidden" name="userIdCheck">
<input type="hidden" name="mailDomainSeq" value="${mailDomainSeq}">
<input type="hidden" name="pkiSignText"/>
<input type="hidden" name="pkiVidText"/>
<script type="text/javascript">makePAID();</script>
<input type="hidden" name="closeJQpopupFunction" id="closeJQpopupFunction" value="parent.modalPopupForRegisterUserClose(); parent.jQpopupClear();" />
<% if(loginParamterRSAUse){ %>
<%
	String publicKeyModulus = (String) request.getAttribute(CryptoSession.PUBLIC_KEY_MODULUS_NAME_PREFIX + "register");
	String publicKeyExponent = (String) request.getAttribute(CryptoSession.PUBLIC_KEY_EXPONENT_NAME_PREFIX + "register"); %>
	<input type="hidden" id="securedPassword" name="securedPassword" value="" />
	<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
	<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
<% } %>
<div class="popup_style1" style="border:0px;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="register.027" bundle="common"/></span>
				<span>* <tctl:msg key="register.require" bundle="common"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>
		
		<table class="TB_popup_style1_body TM_tableList">
			<tbody>
			<tr>
			<td class="bg_left" style="display:none;" />
			<td class="popup_content" style="padding-bottom:0px;">
				<table width="100%" cellpadding="0" cellspacing="0" class="jq_innerTable">
					<col width="140px"></col>
					<col></col>
					<tr>
						<th class="lbout">* <tctl:msg key="conf.userinfo.id" bundle="setting"/></th>
						<td>
							<input type="text" id="userId" name="userId" class="IP100px" style="ime-mode:inactive;">@${fn:escapeXml(mailDomain)}
							<a class="btn_style4" href="javascript:;;" onclick="userIdDupCheck()"><span><tctl:msg key="register.024" bundle="common"/></span></a>
						</td>
					</tr>
					<tr>
						<th class="lbout">* <tctl:msg key="conf.userinfo.basic.nameinfo" bundle="setting"/></th>
						<td>
							<tctl:msg key="conf.userinfo.basic.lastname" bundle="setting"/> <input type="text" id="lastName" name="lastName" class="IP50" <c:if test="${installLocale ne 'jp'}">onkeyup="checkName()"</c:if> <c:if test="${installLocale eq 'jp'}">readonly</c:if>>&nbsp;&nbsp;
							<tctl:msg key="conf.userinfo.basic.firstname" bundle="setting"/> <input type="text" id="firstName" name="firstName" class="IP50" <c:if test="${installLocale ne 'jp'}">onkeyup="checkName()"</c:if> <c:if test="${installLocale eq 'jp'}">readonly</c:if>>&nbsp;&nbsp;
							<tctl:msg key="conf.userinfo.basic.middlename" bundle="setting"/> <input type="text" id="middleName" name="middleName" class="IP50" <c:if test="${installLocale ne 'jp'}">onkeyup="checkName()"</c:if> <c:if test="${installLocale eq 'jp'}">readonly</c:if>>&nbsp;&nbsp;
							<tctl:msg key="conf.userinfo.basic.displayname" bundle="setting"/> <input type="text" id="userName" name="userName" class="IP100px" value="${userName}" <c:if test="${installLocale eq 'jp'}">readonly</c:if>>
						</td>
					</tr>
					<c:if test='${!empty ssn}'>
					<tr>
						<th class="lbout"><tctl:msg key="register.006" bundle="common"/></th>
						<td>
							<c:set var="ssnLength" value="${fn:length(ssn)}"/>
							<c:if test="${ssnLength > 6}">
								${fn:substring(ssn,0,7)}<c:forEach begin="8" end="${ssnLength}">*</c:forEach>
							</c:if>&nbsp;
						</td>
					</tr>
					</c:if>
					<c:if test='${!empty birthday}'>
					<tr>
						<th class="lbout"><tctl:msg key="conf.userinfo.basic.birthday" bundle="setting"/></th>
						<td>
							${fn:escapeXml(fn:substring(birthday,0,4))}<tctl:msg key="scheduler.year" bundle="scheduler"/>
							${fn:escapeXml(fn:substring(birthday,4,6))}<tctl:msg key="scheduler.month" bundle="scheduler"/>
							${fn:escapeXml(fn:substring(birthday,6,8))}<tctl:msg key="scheduler.day" bundle="scheduler"/>
						</td>
					</tr>
					</c:if>					
					<tr>
					<th class="lbout"><tctl:msg key="register.007" bundle="common"/></th>
					<td>${fn:escapeXml(empno)}&nbsp;</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.basic.mobile" bundle="setting"/></th>
					<td><input type="text" id="mobileNo" name="mobileNo" class="IP200"></td>
					</tr>
					<tr>
					<th class="lbout">* <tctl:msg key="conf.userinfo.basic.password" bundle="setting"/></th>
					<td><input type="password" id="mailPassword" name="mailPassword" class="IP200" autocomplete="off"></td>
					</tr>
					<tr>
					<th class="lbout">* <tctl:msg key="conf.userinfo.basic.passconfirm" bundle="setting"/></th>
					<td><input type="password" id="mailPasswordConfirm" name="mailPasswordConfirm" class="IP200" autocomplete="off"></td>
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
						<td>
							<input type="text" id="passAnswer" name="passAnswer" class="IP200">
						</td>
					</tr>					
				</table>
				<div class="title_bar" style="border-right:1px solid #B4CCF1;border-left:1px solid #B4CCF1;"><span><tctl:msg key="conf.userinfo.private.menu" bundle="setting"/></span></div>
				<table width="100%" cellpadding="0" cellspacing="0" class="jq_innerTable">
					<col width="140px"></col>
					<col></col>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
					<td>
					<input type="text" id="home_post_code1" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if> readonly> - 
					<input type="text" id="home_post_code2" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if> readonly>
					<c:if test="${installLocale ne 'jp'}">
						<a class="btn_basic" href="#" onclick="viewSearchZipcode('home')"><span><tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/></span></a>
					</c:if>
					</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
					<td>
					<tctl:msg key="conf.userinfo.country" bundle="setting"/>  <input type="text" id="homeCountry" name="homeCountry" class="IP50">
					<tctl:msg key="conf.userinfo.state" bundle="setting"/>  <input type="text" id="homeState" name="homeState" class="IP50" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
					<tctl:msg key="conf.userinfo.city" bundle="setting"/>  <input type="text" id="homeCity" name="homeCity" class="IP100px" <c:if test="${installLocale ne 'jp'}">readonly</c:if>><br>
					<tctl:msg key="conf.userinfo.street" bundle="setting"/>  <input type="text" id="homeStreet" name="homeStreet" class="IP200" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
					</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.extaddress" bundle="setting"/></th>
					<td>
					<input type="text" id="homeExtAddress" name="homeExtAddress" class="IP300">
					</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.telephone" bundle="setting"/></th>
					<td>
					<input type="text" id="homeTel" name="homeTel" class="IP200">
					</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.fax" bundle="setting"/></th>
					<td>
					<input type="text" id="homeFax" name="homeFax" class="IP200">
					</td>
					</tr>
					<tr>
					<th class="lbout"><tctl:msg key="conf.userinfo.private.homepage" bundle="setting"/></th>
					<td>
					<input type="text" id="privateHomepage" name="privateHomepage" class="IP200">
					</td>
					</tr>
				</table>
			</td>
			<td class="bg_right" style="display:none;"></td>
			</tr>
			</tbody>
		</table>
		<div class="btnArea">
			<a class="btn_style2" href="#" onclick="checkPwd()"><span><tctl:msg key="register.025" bundle="common"/></span></a>
			<a class="btn_style3" href="#" onclick="closeWin();"><span><tctl:msg key="register.023" bundle="common"/></span></a>
		</div>
		<div class="popup_style1_down" style="display:none;">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>
</form>
</div>

<div id="search_zipcode_pop" title="<tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/>">
	<div id="search_zipcode_div" ></div>
</div>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>