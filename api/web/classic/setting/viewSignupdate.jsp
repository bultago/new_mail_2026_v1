<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="xecure.crypto.VidVerifier"%>
<%@page import="xecure.servlet.XecureConfig"%>
<%@page import="com.epki.api.EpkiApi"%>
<%@page import="com.epki.conf.ServerConf"%>
<%@page import="com.epki.cert.X509Certificate"%>
<%@page import="com.epki.exception.EpkiException"%>
<%@page import="com.epki.util.Base64"%>
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
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language = "javascript">
var PKI_INFO = <tctl:pkiCheck print="true"/>;
<%
String strServerCert = "";
if(isMsie && 
		ExtPartConstants.VENDER_SOFTFORUM == ExtPartConstants.getPKIVender() &&
		ExtPartConstants.isPkiLoginUse()){
VidVerifier vid = new VidVerifier(new XecureConfig());
out.println(vid.ServerCertWriteScript());
} else if(isMsie && 
		ExtPartConstants.VENDER_KERIS == ExtPartConstants.getPKIVender() &&
		ExtPartConstants.isPkiLoginUse()){
	EpkiApi.initApp();
		
	try
	{
		byte[] bsserverCert = null;	
		ServerConf conf = new ServerConf();
		X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
		bsserverCert = cert.getCert();		
		Base64 encoder = new Base64();
		strServerCert = encoder.encode(bsserverCert);
	}
	catch (EpkiException e) 
	{
		out.println("Session Request Error!<BR>");
		out.println(e.toString());
		return;
	}
	catch (Exception e) 
	{
		out.println("Session Request Error!<BR>");
		out.println(e.toString());
		return;
	}
}
%>

function checkFieldNext(id,nid,num) {	
	var writeSize = $(id).value.length;
	if (writeSize >= num) {
		$(nid).focus();
	}
}

function checkCompanySsnlength(nextID,num) {
	var f = document.pkiSignForm;
	var writeSize = f.ssn1.value.length;
	if (writeSize >= 6) {
		f.ssn2.focus();
	}
}

function updatePKISign(){
	var ssn = "";
	var mode = jQuery("#pkiUserType").val();
	var f = document.pkiSignForm;
	if(mode == "user"){

		var ssn1 = f.ssn1.value;
		var ssn2 = f.ssn2.value;
	
		if (trim(ssn1) == "") {
			alert(comMsg.register_002);
			f.ssn1.focus();
			return;
		}
	
		if (trim(ssn2) == "") {
			alert(comMsg.register_002);
			f.ssn2.focus();
			return;
		}
	
		ssn = ssn1 + "-" +ssn2;
		var ssnCheck = false;		
		if (isSsn(ssn)) {
			ssnCheck = true;
		}
		if (!ssnCheck) {
			alert(comMsg.register_003);
			f.ssn1.focus();
			return;
		}
		ssn = ssn1+ssn2;
	} else {
		var cssn1 = f.companySsn1.value;
		var cssn2 = f.companySsn2.value;
		var cssn3 = f.companySsn3.value;
		cssn1 = trim(cssn1);
		cssn2 = trim(cssn2);
		cssn3 = trim(cssn3);
		
		if (cssn1 == "") {
			alert(settingMsg.conf_signupdate_007);
			f.companySsn1.focus();
			return;
		}

		if (!isNumber(cssn1)) {
			alert(settingMsg.conf_signupdate_008);
			f.companySsn1.focus();
			return;
		}

		if (cssn2 == "") {
			alert(settingMsg.conf_signupdate_007);
			f.companySsn2.focus();
			return;
		}

		if (!isNumber(cssn2)) {
			alert(settingMsg.conf_signupdate_008);
			f.companySsn2.focus();
			return;
		}

		if (cssn3 == "") {
			alert(settingMsg.conf_signupdate_007);
			f.companySsn3.focus();
			return;
		}

		if (!isNumber(cssn3)) {
			alert(settingMsg.conf_signupdate_008);
			f.companySsn3.focus();
			return;
		}
		
		
		ssn = cssn1 + cssn2 + cssn3;
	}
	f.ssn.value = ssn;
	f.action = "/setting/updatePKISign.do";
	f.method = "post";
	
	if(PKI_INFO.vender == "SOFTFORUM"){
		var isError = false;
		try{
			if(PKI_INFO.mode == "EPKI"){
				f.pkiSignText.value = Sign_with_vid_web(0, "REGISTTEMPPKI"+makeRandom(), s, ssn); 
				f.pkiVidText.value = send_vid_info();
			}
		} catch(e){
			isError = true;
		}

		if(!isError)XecureSubmit(f);
	} else if(PKI_INFO.vender == "INITECH_V7"){
		f.submit();
	} else if(PKI_INFO.vender == "KERIS"){
		var isError = false;
		var pkiSignText = "";
        try{    
			pkiSignText = RequestVerifyVID("<%=strServerCert%>", ssn);
        } catch(e){
			isError = true;
        }

        f.pkiSignText.value = pkiSignText;

		if(!isError && f.pkiSignText.value != ""){
			f.submit();
		} else {
			ECTErrorInfo();
		}
	}
	
}
function resetPKISign(){
	$("pkiSignForm").reset();
	jQuery("#pkiUserTypeSelect").empty();
	jQuery("#pkiUserTypeSelect").selectbox({selectId:"pkiUserType",selectFunc:chgSignType},
			"user",[{index:settingMsg.conf_signupdate_002,value:"user"},
				 {index:settingMsg.conf_signupdate_003,value:"company"}]);
	chgSignType();
	
}
function chgSignType(selectValue){
	var mode = jQuery("#pkiUserType").val();
	if(mode == "user"){
		jQuery("#userSsn").show();
		jQuery("#companySsn").hide();	
	} else {
		jQuery("#userSsn").hide();
		jQuery("#companySsn").show();
	}
			
}
function init(){		
	<%@ include file="settingCommonScript.jsp" %>
	jQuery("#pkiUserTypeSelect").selectbox({selectId:"pkiUserType",selectFunc:chgSignType},
			"user",[{index:settingMsg.conf_signupdate_002,value:"user"},
				 {index:settingMsg.conf_signupdate_003,value:"company"}]);
	chgSignType();
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);			
}

<%@ include file="settingFrameScript.jsp" %>

onloadRedy("init()");
</script>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>
<div id="s_mainBody">

<form name="pkiSignForm" id="pkiSignForm">
<input type="hidden" name="ssn"/>
<input type="hidden" name="pkiSignText"/>
<input type="hidden" name="pkiVidText"/>

		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.signupdate.menu" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>	
				
	
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.signupdate.title" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
					<div>
					
					<div class="title_bar"><span><tctl:msg key="conf.signupdate.001" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><span><tctl:msg key="conf.signupdate.005" bundle="setting"/></span></th>
							<td>								
								<div id="pkiUserTypeSelect"></div>
							</td>
						</tr>
						<tr id="userSsn">
							<th><span><tctl:msg key="register.006" bundle="common"/></span></th>
							<td>								
								<input type="text" name="ssn1" id="ssn1" class="IP50" maxlength="6" onkeyup="checkFieldNext('ssn1','ssn2',6)"> - 
								<input type="password" name="ssn2" id="ssn2" class="IP50" style="width:70px" maxlength="7" autocomplete="off">
							</td>
						</tr>
						<tr id="companySsn">
							<th><span><tctl:msg key="conf.signupdate.004" bundle="setting"/></span></th>
							<td>								
								<input type="text" id="companySsn1" maxlength="3" name="companySsn1" onkeyup="checkFieldNext('companySsn1','companySsn2',3)" class="IP50" style="width:30px">
								-
								<input type="text" id="companySsn2" maxlength="2" name="companySsn2" onkeyup="checkFieldNext('companySsn2','companySsn3',2)" class="IP50" style="width:20px">
								-
								<input type="password" id="companySsn3" maxlength="5" name="companySsn3" class="IP100px" autocomplete="off">								
							</td>
						</tr>						
					</table>
					
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="updatePKISign()"><span><tctl:msg key="conf.signupdate.006" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetPKISign()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
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

<%@include file="/common/bottom.jsp"%>
