<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.terracetech.tims.webmail.util.CryptoSession" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<script type="text/javascript" src="/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/rsa/rng.js"></script>
<link rel="stylesheet" type="text/css" href="/design/default/css/popup_style.css" />

</head>

<script language="javascript">
jQuery().ready(function(){
	<c:if  test="${domainType != 'asp'}">
	<c:if test="${domainType == 'single' || domainInputType != 'input'}">
	jQuery("#mailDomainSelect").selectbox({selectId:"mailDomain",selectFunc:checkUseSsn, height:100, width:300}, "", domainArray);
	</c:if>
	</c:if>
});
var domainType = "${domainType}";
var loginParamterRSAUse = <%=loginParamterRSAUse %>;
var useSsn = true;
function init() {
	var selectDomain = jQuery("#mailDomain").val();
	
	<c:forEach var="notUseSsn" items="${domainInfoMap}">
		if("${notUseSsn.key}" == selectDomain) {
			jQuery("#use_ssn_tr").hide();
			jQuery("#use_empno_tr").show();
			useSsn = false;
		}
	</c:forEach>

	if (useSsn) {
		jQuery("#use_empno_tr").hide();
		jQuery("#use_ssn_tr").show();
	} else {
		jQuery("#use_ssn_tr").hide();
		jQuery("#use_empno_tr").show();
	}
	/*
	winResize();
	
	if(isMsie6){
		window.resizeTo(jQuery(window).width(),275);
	}
	*/
	if(parent)
		parent.setRegisterUserPopupTitle("<tctl:msg key="register.027" bundle="common"/>");
	
	jQuery(":text:first").focus();
}

function checkUseSsn() {
	var selectDomain = jQuery("#mailDomain").val();
	useSsn = true;
	<c:forEach var="notUseSsn" items="${domainInfoMap}">
		if("${notUseSsn.key}" == selectDomain) {
			jQuery("#use_ssn_tr").hide();
			jQuery("#use_empno_tr").show();
			useSsn = false;
		}
	</c:forEach>

	if (useSsn) {
		jQuery("#use_empno_tr").hide();
		jQuery("#use_ssn_tr").show();
		/*
		if(isMsie6){
			window.resizeTo(jQuery(window).width(),275);
		} else {
			winResize();
		}
		*/
	} else {
		jQuery("#use_ssn_tr").hide();
		jQuery("#use_empno_tr").show();
		/*
		winResize();
		if(isMsie6){
			window.resizeTo(jQuery(window).width(),275);
		}
		*/
	}
}

function resetForm() {
	var f = document.registerCheckForm;
	f.reset();
	if(domainType!="asp")jQuery("#mailDomainSelect").selectboxSetIndex(0);
}

function checkssnlength() {
	var f = document.registerCheckForm;
	var writeSize = f.ssn1.value.length;
	if (writeSize >= 6) {
		f.ssn2.focus();
	}
}
function checkRegister(rsaPublicKeyModulus, rsaPublicKeyExponent) {
	var f = document.registerCheckForm;

	var domain = trim(f.mailDomain.value);
	var userName = f.userName.value;

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

	if (trim(userName) == "") {
		alert(comMsg.register_001);
		f.userName.focus();
		return;
	}

	if (useSsn) {
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
	
		var ssn = ssn1 + "-" + ssn2;
		var ssnCheck = false;
		if (f.fgnCheck.checked) {
			if(isFgnSsn(ssn1 + "" + ssn2)) {
				ssnCheck = true;
			}
		}else {
			if (isSsn(ssn)) {
				ssnCheck = true;
			}
		}
	
		if (!ssnCheck) {
			alert(comMsg.register_003);
			f.ssn1.focus();
			return;
		}
		
		if(loginParamterRSAUse){
			var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
	        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
	        
	        var rsa = new RSAKey();
	        rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);
	        ssn = rsa.encrypt(ssn); 
		}

		var param = {"ssn":ssn, "userName":userName, "domain":domain};
		var action = "/register/checkRegister.do";
		<tctl:extModuleCheck moduleName="xecureWeb" msie="true">
		var paramResult = XecureEncParamObject(action,jQuery.param(param));
		param = paramResult.param;
		action = paramResult.action;
		</tctl:extModuleCheck>
	
		jQuery.post(action, param, 
			function (result) {
			var chk = result.isSuccess;
			if (!chk) {
				if (result.msg == "empty") {
					alert(comMsg.error_domain_info);
				} else if (result.msg == "dup") {
					alert(comMsg.register_033);
				} else {
					alert(comMsg.error_msg_001);
				}
				return;
			}
			else {
				alert(comMsg.register_004);
				f.ssn.value = ssn;
				f.ssn1.value = "";
				f.ssn2.value = "";
				f.action = "/register/registerUser.do";
				f.method = "post";
				f.submit();
			}
		}, "json");
	} else {
		
		var empno = f.empno.value;
		
		if(!checkInputLength("", f.empno, comMsg.register_001, 0, 20)) {
			return;
		}
		
		if(!checkInputValidate("", f.empno, "empNo")) {
			return;
		}
		
		if (trim(empno) == "") {
			alert(comMsg.error_login_empno);
			f.empno.focus();
			return;
		}
		
		
		var param = {"empno":empno, "userName":userName, "domain":domain};
		
		var action = "/register/checkRegister.do";
		<tctl:extModuleCheck moduleName="xecureWeb" msie="true">
		var paramResult = XecureEncParamObject(action,jQuery.param(param));
		param = paramResult.param;
		action = paramResult.action;
		</tctl:extModuleCheck>
	
		jQuery.post(action, param, 
			function (result) {
			var chk = result.isSuccess;
			if (!chk) {
				if (result.msg == "empty") {
					alert(comMsg.error_domain_info);
				} else if (result.msg == "dup") {
					alert(comMsg.register_033);
				} else {
					alert(comMsg.error_msg_001);
				}
				return;
			}
			else {
				alert(comMsg.register_004);
				f.action = "/register/registerUser.do";
				f.method = "post";
				f.ssn1.value = "";
				f.ssn2.value = "";
				f.submit();
			}
		}, "json");
	}
}

function pause(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;

    while (true) {
         now = new Date();
         if (now.getTime() > exitTime)
             return;
    }
}

function winResize(minHeight) {		
	var Dwidth = 0;
	var Dheight = 0;
	
    var divEl = document.createElement("div");
    divEl.style.position = "absolute";
    divEl.style.left = "0px";
    divEl.style.top = "0px";
    divEl.style.width = "100%";
    divEl.style.height = "100%";
    document.body.appendChild(divEl);
        
    if (jQuery.browser.msie){
    	Dwidth = 0;
    	Dheight = jQuery("body").innerHeight();    	
    }else{
    	Dwidth = document.body.clientWidth;
    	Dheight = document.body.clientHeight;
    }
    
    var pheight = Dheight-divEl.offsetHeight;
    if(minHeight && minHeight < pheight){
    	pheight = 0;
    }   
    
    pause(1);
    
  	if (jQuery.browser.msie) {  		  		
  		window.resizeBy(Dwidth,pheight);
  	} else {  		
  		if (Dwidth > divEl.offsetWidth)
  			Dwidth = divEl.offsetWidth;
  		
  		if (pheight < 0 || pheight > 500)   			
  			pheight = 112;
  		
  		window.resizeBy(Dwidth-divEl.offsetWidth,pheight);  		
  	}
    document.body.removeChild(divEl); 
}

function closeWin(){
	if(parent){
		parent.modalPopupForRegisterUserClose(); 
		parent.jQpopupClear();
	}	
	else
		window.close();	
}
</script>

<body onload="init()" class="popupBody" style="background:none;">
<%
	if(loginParamterRSAUse) {
		String publicKeyModulus = (String) request.getAttribute(CryptoSession.PUBLIC_KEY_MODULUS_NAME_PREFIX + "register");
		String publicKeyExponent = (String) request.getAttribute(CryptoSession.PUBLIC_KEY_EXPONENT_NAME_PREFIX + "register"); %>
	<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
	<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
<% } %>
<form name="registerCheckForm" onsubmit="return false;">
<input type="hidden" name="ssn">
<c:if  test="${domainType == 'asp'}">
<input type="hidden" name="mailDomain"value="${selectDomainName}">
</c:if>
	<div class="popup_style1" style="border:0px;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="register.027" bundle="common"/></span>
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
					</c:if>
					<tr>
					<th class="lbout">* <tctl:msg key="conf.userinfo.name" bundle="setting"/></th>
					<td><input type="text" name="userName" class="IP200"></td>
					</tr>
					<tr id="use_ssn_tr" style="display:none">
					<th class="lbout">* <tctl:msg key="register.006" bundle="common"/></th>
					<td>
						<input type="text" name="ssn1" class="IP50" maxlength="6" onkeyup="checkssnlength()"> - 
						<input type="password" name="ssn2" class="IP50" style="width:70px" maxlength="7" autocomplete="off">
						<label><input type="checkbox" name="fgnCheck" style="border:0"><tctl:msg key="register.031" bundle="common"/></label>
					</td>
					</tr>
					<tr id="use_empno_tr" style="display:none">
					<th class="lbout">* <tctl:msg key="register.007" bundle="common"/></th>
					<td><input type="text" name="empno" class="IP200"></td>
					</tr>			
				</table>
				</td>
				<td class="bg_right" style="display:none;" ></td>
				</tr>
			</tbody>
		</table>
		<div class="btnArea">
			<a class="btn_style2" href="#" onclick="checkRegister()"><span><tctl:msg key="register.028" bundle="common"/></span></a>
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