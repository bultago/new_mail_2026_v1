<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>

<script language="javascript">
<%
    String agt = request.getHeader("USER-AGENT");
    agt = StringEscapeUtils.escapeHtml(agt);
%>
var agt = "<%=agt%>";
if((agt.indexOf("MSIE") != -1) && (agt.indexOf("Opera") == -1))
	agent = "ie";
else if(agt.indexOf('Gecko') != -1)
	agent = "gecko";
else if(agt.indexOf('Opera') != -1)
    agent = "opera";
else agent = "nav";

function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery(window).trigger("resize");
	
	jQuery("#spam_menu").addClass("on");

	var spamPolicy = getSpamPolicy();
	jQuery("#spamRuleTypeSelect").selectbox({selectId:"spamRuleType",selectFunc:checkSpamMode},
		spamPolicy,
		[{index:settingMsg.conf_spamrule_block_use,value:"1"},
		 {index:settingMsg.conf_spamrule_block_notuse,value:"2"},
		 {index:settingMsg.conf_spamrule_receive_all,value:"3"},
		 {index:settingMsg.conf_spamrule_block_all+", "+settingMsg.conf_spamrule_receive_white,value:"4"}]);	 
	
	jQuery("#policySelect").selectbox({selectId:"policy",selectFunc:""},
		"${rule.pspamPolicy}",
		[{index:settingMsg.conf_spamrule_25,value:"move Spam"},
		 {index:settingMsg.conf_spamrule_26,value:"move Trash"},
		 {index:settingMsg.conf_spamrule_27,value:"delete"}]);

	jQuery("#rulelevelSelect").selectbox({selectId:"rulelevel",selectFunc:""},
			"${rule.pspamRuleLevel}",
			[{index:settingMsg.conf_spamrule_21,value:"1"},
			 {index:settingMsg.conf_spamrule_22,value:"2"},
			 {index:settingMsg.conf_spamrule_23,value:"3"}]);
	 
	checkSpamMode();	 	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	
}

<%@ include file="settingFrameScript.jsp" %>

function saveSpamFilter(){
	var f = document.spamRuleForm;

	var ruleMode = jQuery("#spamRuleType").val();	
	
	if (ruleMode == 1) {
		f.applyAllowedlistOnly.value = "off";
		f.applyRuleLevel.value = "on";
		f.applyBlacklist.value = "on";
		f.applyWhitelist.value = "on";
	}
	else if (ruleMode == 2) {
		f.applyAllowedlistOnly.value = "off";
		f.applyRuleLevel.value = "off";
		f.applyBlacklist.value = "on";
		f.applyWhitelist.value = "on";
	}
	else if (ruleMode == 3) {
		f.applyAllowedlistOnly.value = "off";
		f.applyRuleLevel.value = "off";
		f.applyBlacklist.value = "off";
		f.applyWhitelist.value = "on";
	}
	else if (ruleMode == 4) {
		f.applyAllowedlistOnly.value = "on";
		f.applyRuleLevel.value = "off";
		f.applyBlacklist.value = "off";
		f.applyWhitelist.value = "on";
	}

	for (i = 0; i < f.blackList.length; i++) {
		f.blackList.options[i].selected = true;
	}

    for (i = 0; i < f.whiteList.length; i++) {
        f.whiteList.options[i].selected = true;
    }

    f.action = "/setting/saveSpamRule.do";
    f.method = "post";
    f.submit();
}

function checkSpamMode() {
	var ruleMode = jQuery("#spamRuleType").val();	
	if (ruleMode == 1) {
		jQuery("#autospamDiv").show();
		jQuery("#blackAndWhiteDiv").show();
		jQuery("#spamPolicyTr").show();
		jQuery("#blackListTr").show();
	}
	else if (ruleMode == 2) {
		jQuery("#autospamDiv").hide();
		jQuery("#blackAndWhiteDiv").show();
		jQuery("#spamPolicyTr").show();
		jQuery("#blackListTr").show();
	}
	else if (ruleMode == 3) {
		jQuery("#autospamDiv").hide();
		jQuery("#blackAndWhiteDiv").hide();
		jQuery("#spamPolicyTr").hide();
		jQuery("#blackListTr").show();
	}
	else if (ruleMode == 4) {
		jQuery("#autospamDiv").hide();
		jQuery("#blackAndWhiteDiv").show();
		jQuery("#spamPolicyTr").show();
		jQuery("#blackListTr").hide();
	}
}

function getSpamPolicy() {

	var f = document.spamRuleForm;
	var applyAllowedlistOnly = jQuery("#applyAllowedlistOnly").val();
	var applyRuleLevel = jQuery("#applyRuleLevel").val();
	var applyBlacklist = jQuery("#applyBlacklist").val();
	var applyWhitelist = jQuery("#applyWhitelist").val();
	
	if (applyAllowedlistOnly == "off" && applyRuleLevel == "on" &&
		applyBlacklist == "on" && applyWhitelist == "on") {		
		return 1;
	}
		
	else if (applyAllowedlistOnly == "off" && applyRuleLevel == "off" &&
		applyBlacklist == "on" && applyWhitelist == "on") {		
		return 2;
	}
		
	else if (applyAllowedlistOnly == "off" && applyRuleLevel == "off" &&
		applyBlacklist == "off" && applyWhitelist == "on") {		
		return 3;
	}
		
	else if (applyAllowedlistOnly == "on" && applyRuleLevel == "off" &&
		applyBlacklist == "off" && applyWhitelist == "on") {		
		return 4;
	}	
}

function resetRuleSetting() {
	location.reload();
}


function addRuleList(type){
	var inputObj,selectObj,compareObj,msg;
	var chk = false;
	if(type == "w"){
		inputObj = document.spamRuleForm.white_address;
		selectObj = document.spamRuleForm.whiteList;
		compareObj = document.spamRuleForm.blackList;
		msg = settingMsg.conf_spam_15;
	} else {
		inputObj = document.spamRuleForm.black_address;
		selectObj = document.spamRuleForm.blackList;
		compareObj = document.spamRuleForm.whiteList;
		msg = settingMsg.conf_spam_14;		
	}

	var options = compareObj.options;

	if(options && options.length > 0 ){
		for(var i = 0 ; i < options.length ; i++){
			if(inputObj.value == options[i].value){
				alert(msg);
				chk = true;				
				break;						
			}
		}	
	}
	if(!chk){
		addList(inputObj, selectObj,'1000','${email}');
	}
}

onloadRedy("init()");
</script>

</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="spamRuleForm">
<input type="hidden" id="applyAllowedlistOnly" name="applyAllowedlistOnly" value="${rule.applyAllowedlistOnly}">
<input type="hidden" id="applyRuleLevel" name="applyRuleLevel" value="${rule.applyRuleLevel}">
<input type="hidden" id="applyBlacklist" name="applyBlacklist" value="${rule.applyBlacklist}">
<input type="hidden" id="applyWhitelist" name="applyWhitelist" value="${rule.applyWhitelist}">

		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.spamrule.33" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.spamrule.title1" bundle="setting"/></li>
					<li><tctl:msg key="conf.spamrule.title2" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.spamrule.basic.title" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0" style="border:1px solid #E6E6E6">
						<tr>
							<th><span><tctl:msg key="conf.spamrule.basic.policy" bundle="setting"/></span></th>
							<td>								
								<div id="spamRuleTypeSelect"></div>
							</td>
						</tr>
						<tr id="spamPolicyTr">
							<th><span><tctl:msg key="conf.spamrule.24" bundle="setting"/></span></th>
							<td>								
								<div id="policySelect"></div>
							</td>
						</tr>
					</table>
					
					<div id="autospamDiv">
					<div class="title_bar"><span><tctl:msg key="conf.spamrule.block.title" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0" style="border:1px solid #E6E6E6">
						<tr class="last">
							<th><span><tctl:msg key="conf.spamrule.block.level" bundle="setting"/></span></th>
							<td>								
								<div id="rulelevelSelect"></div>
							</td>
						</tr>
					</table>
					</div>
					
					<div id="blackAndWhiteDiv">
					<div class="title_bar"><span><tctl:msg key="conf.spamrule.allow.block.control" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0" style="border:1px solid #E6E6E6">
						<tr>
							<th><tctl:msg key="conf.spamrule.46" bundle="setting"/></th>
							<td>
								<table class="TB_cols2_content" collspan="0" cellpadding="0" border="0">
									<tr>
									<td class="TD_selectBox">
										<input type="text" name="white_address" class="IP100">
										<select multiple="multiple" class="selectBox" name="whiteList">
											<c:forEach var="white" items="${rule.whiteList}">
											<option value="${fn:escapeXml(white.email)}">${fn:escapeXml(white.email)}</option>
											</c:forEach>
										</select>
									</td>
									<td class="btnArea">
										<ul>
											<li>
												<a href="javascript:addRuleList('w');" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
												<a href="javascript:searchList(document.spamRuleForm.white_address, document.spamRuleForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
											</li>
											<li><a href="javascript:selectAll(document.spamRuleForm.whiteList,'w_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
											<li><a href="javascript:deleteList(document.spamRuleForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
										</ul>
									</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr id="blackListTr">
							<th><tctl:msg key="conf.spamrule.49" bundle="setting"/></th>
							<td>
								<table class="TB_cols2_content" collspan="0" cellpadding="0" border="0">
									<tr>
									<td>
										<input type="text" name="black_address" class="IP100">
										<select multiple="multiple" class="selectBox" name="blackList">
											<c:forEach var="black" items="${rule.blackList}">
											<option value="${fn:escapeXml(black.email)}">${fn:escapeXml(black.email)}</option>
											</c:forEach>
										</select>											
									</td>
									<td class="btnArea">
										<ul>
											<li>
												<a href="javascript:addRuleList('b');" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
												<a href="javascript:searchList(document.spamRuleForm.black_address, document.spamRuleForm.blackList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
											</li>
											<li><a href="javascript:selectAll(document.spamRuleForm.blackList,'b_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
											<li><a href="javascript:deleteList(document.spamRuleForm.blackList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>										
										</ul>
									</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</div>
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="saveSpamFilter()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetRuleSetting()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
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

</body>
</html>