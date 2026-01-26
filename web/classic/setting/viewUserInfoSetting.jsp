<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="xecure.crypto.VidVerifier"%>
<%@page import="xecure.servlet.XecureConfig"%>
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
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language=javascript src="/js/ext-lib/common-aes.js"></script>
<% if(loginParamterRSAUse){ %>
<%-- script 태그에서 가져오는 자바스크립트 파일의 순서에 주의해야한다! 순서가 틀릴경우 자바스크립트 오류가 발생한다. --%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<% } %>
<script language = "javascript">
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

function init(){		
	<%@ include file="settingCommonScript.jsp" %>
	jQuery("#userinfo_menu").addClass("on");
	jQuery("#passQuestionSelect").selectbox({selectId:"passQuestionCode",selectFunc:""},
			"${userInfoVo.passQuestionCode}",passQuestionArray);

	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	var date = new Date();
	var end = date.getFullYear();
	date.setFullYear(end - 100);
	var start = date.getFullYear();
	jQuery("#birthday").datepick({dateFormat:'yymmdd', yearRange:start+':'+end});
	jQuery("#datepick-div").css("z-index","10001");
	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");

	readPicture();	
}

<%@ include file="settingFrameScript.jsp" %>

function toggleMenu(obj, id) {
	jQuery("#"+id).toggle();
	jQuery(obj).toggleClass("open");
	jQuery(window).trigger("resize");	
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
				
	jQuery("#userName").val(lastName+middleName+firstName);
}

function checkPwd () {

	var pwd1 = jQuery("#preMailPassword").val();
	var pwd2 = jQuery("#preMailPasswordConfirm").val();
	var uSeq = ${userInfoVo.mailUserSeq};
    var userName = jQuery("#userName").val();
    var userId = "";
    var param = {};
    
	if (pwd1 != pwd2) {
		alert(settingMsg.conf_userinfo_msg_08);
		jQuery("#preMailPasswordConfirm").focus();
		return;
	}
		
	if (trim(pwd1) == "") {
        saveUserInfo();
    } else {
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
                            
									jQuery("#preMailPassword").val('');
									jQuery("#preMailPasswordConfirm").val('');
									jQuery("#mailPassword").val('');
									jQuery("#mailPasswordConfirm").val('');
										
									jQuery("#preMailPassword").focus();
									return false;
								}else{
                                	saveUserInfo();
								}
							},"json");
                    }
                , "json");
                
            } catch(err) {
                alert(err);
            }
        }else{
			param = {"password":pwd1.val(), "passwordConfirm":pwd2.val(), "uSeq":uSeq,"uid":UserId, "name":userName};
            setParamPAID(param);
            jQuery.post("/common/passwordCheck.do", param, 
                function (res) {
                    if(res.msg != 'success'){
                        alert(res.msg);
            
						jQuery("#preMailPassword").val('');
						jQuery("#preMailPasswordConfirm").val('');
						jQuery("#mailPassword").val('');
						jQuery("#mailPasswordConfirm").val('');
    
						jQuery("#preMailPassword").focus();
                        return false;
                    }else{
                        saveUserInfo();
                    }
                },"json");
        }
    }
	return true;
}

function saveUserInfo() {
	var f = document.userInfoForm;

	if(!checkInputLength("", f.lastName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.lastName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.firstName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.firstName, "userName")) {
		return;
	}
	
	if(!checkInputLength("", f.middleName, "", 0, 64)) {
		return;
	}
	if(!checkInputValidate("", f.middleName, "userName")) {
		return;
	}

	if(!checkInputLength("", f.userName, settingMsg.conf_userinfo_msg_01, 2, 64)) {
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

	<c:if test='${showPasswordInput ne "off"}'>
	if(!checkInputLength("", f.passAnswer, "", 0, 255)) {
		return;
	}
	if(!checkInputValidate("", f.passAnswer, "case5")) {
		return;
	}
	</c:if>

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
	
	var pwd1 = jQuery.trim(jQuery("#preMailPassword").val());
	if(pwd1 != ""){
		if(loginParamterRSAUse){
		    try {
		    	var mailPassword = trim(jQuery("#preMailPassword").val());
		        
		        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
		        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
		        var rsa = new RSAKey();
		        rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);
	
		        // 비밀번호를 RSA로 암호화한다.
		        var securedPassword = rsa.encrypt(mailPassword);
		        jQuery("#mailPassword").val(securedPassword);
		    } catch(err) {
		        alert(err);
		    }
		}
	}
	jQuery("#preMailPassword").val("");	
	jQuery("#preMailPasswordConfirm").val("");		
	
	f.action = "/setting/modifyUserInfo.do";
	f.method = "post";

	if(isMsie && PKI_INFO.useage == "enable"){
		if(PKI_INFO.vender == "SOFTFORUM"){			
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

function start() {
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

function resetUserInfo() {
	var f = document.userInfoForm;
	f.reset();

	jQuery("#passQuestionSelect").selectboxSetValue("${userInfoVo.passQuestionCode}");
}


function uploadPicturePopup() {

	resetPictureForm();
	
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 200,
			minWidth:575,
			btnList : [{name:settingMsg.common_save,func:savePicture}],	
			openFunc:function(){},
			closeFunc:function(){
				resetPictureForm();
			}
		};	
	jQuery("#picture_pop").jQpopup("open",popupOpt);	
}

function resetPictureForm() {
	jQuery("#picture_area").empty();
	var f = document.pictureForm;
	f.theFile.value = "";
	f.picturePath.value = "";
	f.pictureName.value = "";
}

function uploadPicture() {
	var f = document.pictureForm;

	var picture = f.theFile.value;
	
	if (!isJpgNGifFile(picture)) {
		alert(settingMsg.conf_userinfo_msg_10);
		f.theFile.value = "";
		return;
	}
	
	jQuery("#workHiddenFrame").makeUploadFrame("up_hidden_frame");    
    f.target= "up_hidden_frame";	
	f.method = "post";
	f.action = "/setting/uploadPicture.do";
	f.submit();
}

function setPicture(msg, url, src, name) {

	if (msg == 'size') {
		alert(msgArgsReplace(settingMsg.conf_userinfo_msg_11,['50KB']));
		return;
	} 
	else if (msg == 'name') {
		alert(settingMsg.conf_sign_8);
		return;
	} 
	else if (msg == 'error') {
		alert(comMsg.error_fileupload);
		return;
	}

	jQuery("#picture_area").empty();
	var img = jQuery("<img>");
	img.css("width", "120px");
	img.css("height", "145px"); 
	img.attr("src", url);
	jQuery("#picture_area").append(img);

	var f = document.pictureForm;
	f.picturePath.value = src;
	f.pictureName.value = name;
}

function savePicture() {

	var f = document.pictureForm;
	var picturePath = f.picturePath.value;
	var pictureName = f.pictureName.value;
	
	var url = "/setting/savePicture.do";
	var param = {"picturePath":picturePath, "pictureName":pictureName};
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isSuccess) {
				alert(settingMsg.conf_userinfo_msg_15);
				jQuery("#picture_pop").jQpopup("close");
				readPicture();
				return;
			}
			else {
				if (data.msg == 'empty') {
					alert(settingMsg.conf_userinfo_msg_16);
				}
				else if (data.msg = 'error') {
					alert(comMsg.error_default);
				}
				return;
			}
		});
}

function deletePicture() {

	if (!confirm(settingMsg.conf_userinfo_msg_12)) {
		return;
	}
	
	var url = "/setting/deletePicture.do";
	var param = {};
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isSuccess) {
				alert(settingMsg.conf_userinfo_msg_13);
				readPicture();
				return;
			}
			else {
				if (data.msg = 'empty') {
					alert(settingMsg.conf_userinfo_msg_14);
				}
				else if (data.msg = 'error') {
					alert(comMsg.error_default);
				}
				return;
			}
		});
}

function readPicture() {
	var url = "/setting/viewPicture.do";
	var param = {};
	param.userSeq = '${userInfoVo.mailUserSeq}';
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isExist) {
				jQuery("#myPicture").empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", data.pictureSrc);
				jQuery("#myPicture").append(img);
				return;
			}
			else {
				if (data.msg = 'empty') {
					jQuery("#myPicture").empty();
				}
				else if (data.msg = 'error') {
					alert(comMsg.error_default);
				}
				return;
			}
		});
}

function end() {
	setCookie("PSID","",-1);
}

onloadRedy("init()");
</script>
</head>

<body onload="start()" onunload="end()">
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>
<div id="s_mainBody">

<form name="userInfoForm" id="userInfoForm">
<tctl:pkiCheck msie="true" loginMode="PKI">
<input type="hidden" name="ssn" value="${userInfoVo.ssn}"/>
</tctl:pkiCheck>
<input type="hidden" id="homePostalCode" name="homePostalCode" value="${userInfoVo.homePostalCode}">
<input type="hidden" id="officePostalCode" name="officePostalCode" value="${userInfoVo.officePostalCode}">
<input type="hidden" id="classCode" name="classCode" value="${userInfoVo.classCode}">
<script type="text/javascript">makePAID();</script>

<% if(loginParamterRSAUse){ %>
<%
String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
%>
<input type="hidden" name="securedPassword" id="securedPassword" value="" />
<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
<% } %>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="menu_conf.modify" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>	
				
	
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.userinfo.title" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
										<div class="title_bar"><a href="javascript:;" onclick="toggleMenu(this,'basic_info')" class="close open"><span><tctl:msg key="conf.userinfo.basic.menu" bundle="setting"/></span></a></div>
					<table id="basic_info" class="TB_cols2" cellpadding="0" cellspacing="0" style="height:220px;">
						<colgroup span="3">
							<col width="145px"></col>
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<td rowspan="8" style="padding:10px 10px 10px 10px;background:#f7f7f7;" align="center">
								<div id="myPicture" style="border:1px solid #CDCDCD;background:#ffffff;width:120px;height:145px">
								</div>
								<div class="TM_s_btnArea">
									<a class="btn_style3" href="#" onclick="uploadPicturePopup()"><span><tctl:msg key="conf.userinfo.basic.picture.upload" bundle="setting"/></span></a>
									<a class="btn_style3" href="#" onclick="deletePicture()"><span><tctl:msg key="conf.userinfo.basic.picture.delete" bundle="setting"/></span></a>
								</div>
							</td>	
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.nameinfo" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.basic.lastname" bundle="setting"/> <input type="text" id="lastName" name="lastName" class="IP50" onkeyup="checkName()" value="${userInfoVo.lastName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.firstname" bundle="setting"/> <input type="text" id="firstName" name="firstName" class="IP50" onkeyup="checkName()" value="${userInfoVo.firstName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.middlename" bundle="setting"/> <input type="text" id="middleName" name="middleName" class="IP50" onkeyup="checkName()" value="${userInfoVo.middleName}">&nbsp;&nbsp;
								<tctl:msg key="conf.userinfo.basic.displayname" bundle="setting"/> <input type="text" id="userName" name="userName" class="IP100px" value="${userInfoVo.userName}">
							</td>
						</tr>
						<tr style="<c:if test='${empty userInfoVo.ssn}'>display:none</c:if>">
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="register.006" bundle="common"/></th>
							<td>
								<c:set var="ssnLength" value="${fn:length(userInfoVo.ssn)}"/>
								<c:if test="${ssnLength > 6}">
									${fn:substring(userInfoVo.ssn,0,7)}<c:forEach begin="8" end="${ssnLength}">*</c:forEach>
								</c:if>
							</td>
						</tr>
						<tr>
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="register.007" bundle="common"/></th>
							<td>
								${userInfoVo.empno}
							</td>
						</tr>
						<tr>
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.mobile" bundle="setting"/></th>
							<td>
								<input type="text" id="mobileNo" name="mobileNo" class="IP200" value="${userInfoVo.mobileNo}">
							</td>
						</tr>
						<tr style="<c:if test='${showPasswordInput eq "off"}'>display:none</c:if>">
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.password" bundle="setting"/></th>
							<td>
								<input type="password" id="preMailPassword" name="preMailPassword" class="IP200" autocomplete="off"><br />
								<input type="hidden" id="mailPassword" name="mailPassword" class="IP200">
								<p style="color:#ff0000">
                                    ${pwInfoMsg }
                                </p>
							</td>
						</tr>
						<tr style="<c:if test='${showPasswordInput eq "off"}'>display:none</c:if>">
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.passconfirm" bundle="setting"/></th>
							<td>
								<input type="password" id="preMailPasswordConfirm" name="preMailPasswordConfirm" class="IP200" autocomplete="off">
								<input type="hidden" id="mailPasswordConfirm" name="mailPasswordConfirm" class="IP200">
							</td>
						</tr>
						<tr style="<c:if test='${showPasswordInput eq "off"}'>display:none</c:if>">
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.passquestion" bundle="setting"/></th>
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
						<tr style="<c:if test='${showPasswordInput eq "off"}'>display:none</c:if>">
							<th style="border-left:1px solid #E6E6E6;"><tctl:msg key="conf.userinfo.basic.passanswer" bundle="setting"/></th>
							<td>
								<input type="text" id="passAnswer" name="passAnswer" class="IP200" value="${userInfoVo.passAnswer}">
							</td>
						</tr>
					</table>

					<div class="title_bar"><a href="javascript:;" onclick="toggleMenu(this,'home_info')" class="close"><span><tctl:msg key="conf.userinfo.private.menu" bundle="setting"/></span></a></div>
					<table id="home_info" class="TB_cols2" cellpadding="0" cellspacing="0" style="display:none">
						<colgroup span="2">
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><tctl:msg key="conf.userinfo.basic.birthday" bundle="setting"/></th>
							<td>
								<input type="text" id="birthday" name="birthday" class="IP100px" value="${userInfoVo.birthday}" readonly="readonly">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
							<td>
								<input type="text" id="home_post_code1" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>> - 
								<input type="text" id="home_post_code2" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>>
								<c:if test="${installLocale ne 'jp'}">
									<a class="btn_basic" href="javascript:;" onclick="viewSearchZipcode('home')"><span><tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/></span></a>
								</c:if>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.country" bundle="setting"/>  <input type="text" id="homeCountry" name="homeCountry" class="IP50" value="${userInfoVo.homeCountry}">
								<tctl:msg key="conf.userinfo.state" bundle="setting"/>  <input type="text" id="homeState" name="homeState" class="IP50" value="${userInfoVo.homeState}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.city" bundle="setting"/>  <input type="text" id="homeCity" name="homeCity" class="IP100px" value="${userInfoVo.homeCity}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.street" bundle="setting"/>  <input type="text" id="homeStreet" name="homeStreet" class="IP200" value="${userInfoVo.homeStreet}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.extaddress" bundle="setting"/></th>
							<td>
								<input type="text" id="homeExtAddress" name="homeExtAddress" class="IP300" value="${userInfoVo.homeExtAddress}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.telephone" bundle="setting"/></th>
							<td>
								<input type="text" id="homeTel" name="homeTel" class="IP200" value="${userInfoVo.homeTel}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.home" bundle="setting"/> <tctl:msg key="conf.userinfo.fax" bundle="setting"/></th>
							<td>
								<input type="text" id="homeFax" name="homeFax" class="IP200" value="${userInfoVo.homeFax}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.private.homepage" bundle="setting"/></th>
							<td>
								<input type="text" id="privateHomepage" name="privateHomepage" class="IP200" value="${userInfoVo.privateHomepage}">
							</td>
						</tr>
					</table>
				
					<div class="title_bar"><a href="javascript:;" onclick="toggleMenu(this,'company_info')" class="close"><span><tctl:msg key="conf.userinfo.company.menu" bundle="setting"/></span></a></div>
					<table id="company_info" class="TB_cols2" cellpadding="0" cellspacing="0" style="display:none">
						<colgroup span="2">
							<col width="130px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><tctl:msg key="conf.userinfo.companyname" bundle="setting"/></th>
							<td>
								<input type="text" id="companyName" name="companyName" class="IP200" value="${userInfoVo.companyName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.departmentname" bundle="setting"/></th>
							<td>
								<input type="text" id="departmentName" name="departmentName" class="IP200" value="${userInfoVo.departmentName}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
							<td>
								<input type="text" id="company_post_code1" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>> - 
								<input type="text" id="company_post_code2" class="IP50" <c:if test="${installLocale eq 'jp'}">maxlength="4"</c:if><c:if test="${installLocale ne 'jp'}"> readonly</c:if>>
								<c:if test="${installLocale ne 'jp'}">
									<a class="btn_basic" href="javascript:;" onclick="viewSearchZipcode('company')"><span><tctl:msg key="conf.userinfo.postcode.search" bundle="setting"/></span></a>
								</c:if>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
							<td>
								<tctl:msg key="conf.userinfo.country" bundle="setting"/>  <input type="text" id="officeCountry" name="officeCountry" class="IP50" value="${userInfoVo.officeCountry}">
								<tctl:msg key="conf.userinfo.state" bundle="setting"/> <input type="text" id="officeState" name="officeState" class="IP50" value="${userInfoVo.officeState}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.city" bundle="setting"/>  <input type="text" id="officeCity" name="officeCity" class="IP100px" value="${userInfoVo.officeCity}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
								<tctl:msg key="conf.userinfo.street" bundle="setting"/>  <input type="text" id="officeStreet" name="officeStreet" class="IP200" value="${userInfoVo.officeStreet}" <c:if test="${installLocale ne 'jp'}">readonly</c:if>>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.extaddress" bundle="setting"/></th>
							<td>
								<input type="text" id="officeExtAddress" name="officeExtAddress" class="IP300" value="${userInfoVo.officeExtAddress}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.telephone" bundle="setting"/></th>
							<td>
								<input type="text" id="officeTel" name="officeTel" class="IP200" value="${userInfoVo.officeTel}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.company" bundle="setting"/> <tctl:msg key="conf.userinfo.fax" bundle="setting"/></th>
							<td>
								<input type="text" id="officeFax" name="officeFax" class="IP200" value="${userInfoVo.officeFax}">
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.userinfo.companyhomepage" bundle="setting"/></th>
							<td>
								<input type="text" id="officeHomepage" name="officeHomepage" class="IP200" value="${userInfoVo.officeHomepage}">
							</td>
						</tr>
					</table>					
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="checkPwd()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetUserInfo()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						</td></tr>
					</table>
					</div>
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

<div id="picture_pop" title="<tctl:msg key="conf.userinfo.basic.picture.upload" bundle="setting"/>" style="display:none">
	<form name="pictureForm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="picturePath" />
		<input type="hidden" name="pictureName" />
		<input type="hidden" name="maxPictureSize" value="51200" />
		<table cellpadding="0" cellspacing="0">
			<col width="135px"></col>
			<col></col>			
			<tr>
				<td style="padding:10px 10px 10px 10px" align="center" valign="top">
					<div id="picture_area" style="border:1px solid #CDCDCD;width:120px;height:145px">
					</div>
				</td>						
				<td style="padding:10px 0px 10px 0px" valign="top">
					<div style="border:1px solid #CDCDCD;width:400px;height:80px">
						<p style="padding:10px 5px 5px 5px"><b>* <tctl:msg key="conf.userinfo.msg.09" bundle="setting" arg0="120" arg1="145"/></b></p>
						<p style="padding:5px 5px 5px 5px"><b>* <tctl:msg key="conf.userinfo.msg.10" bundle="setting"/></b></p>
						<p style="padding:5px 5px 10px 5px"><b>* <tctl:msg key="conf.userinfo.msg.11" bundle="setting" arg0="50KB"/></b></p>
					</div>
					
					<input type="file" name="theFile" onChange="uploadPicture()" class="TM_attFile" />
				</td>
			</tr>
		</table>
		<div id="workHiddenFrame"></div>
	</form>
</div>

<%@include file="/common/bottom.jsp"%>
</body>
</html>