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
<title>Setting</title>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language="javascript">
function init(){	
	<%@ include file="settingCommonScript.jsp" %>		
	jQuery("#autoreply_menu").addClass("on");

	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	jQuery("#startTime").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#endTime").datepick({dateFormat:'yy-mm-dd'});

	jQuery("#modeSelect").selectbox({selectId:"mode",selectFunc:activeMode},
		"${replyVo.activeMode}",
		[{index:settingMsg.conf_filter_52,value:"on"},
		 {index:settingMsg.conf_filter_53,value:"off"}]);

	jQuery("#replyModeSelect").selectbox({selectId:"replyMode",selectFunc:changeMode},
			"${replyVo.autoReplyMode}",
			[{index:settingMsg.conf_autoreply_16,value:"REPLYALL"},
			 {index:settingMsg.conf_autoreply_18,value:"REPLYWHITE"}]);	

	activeMode();
	changeMode();

	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");
}

<%@ include file="settingFrameScript.jsp" %>

function saveAuto() {
	var f = document.replyForm;

	if (str_realLength(f.autoReplyText.value) > 2048) {
		alert(msgArgsReplace(comMsg.error_inputlength_over,[2048]));
        return;
    }

    if (f.mode.value == "on") {

		var startValue = jQuery("#startTime").val();
		var endValue = jQuery("#endTime").val();
    		
		if (trim(startValue) == "") {
			alert(settingMsg.conf_autoreply_29);
			return;
		}

		if (trim(endValue) == "") {
			alert(settingMsg.conf_autoreply_30);
			return;
		}
        
		 var startDate = startValue.split('-');
		 var endDate = endValue.split('-');
		
		 var start = new Date(startDate[0],startDate[1]-1, startDate[2]);
		 var end = new Date(endDate[0],endDate[1]-1,endDate[2]);
		 var today = new Date();
		 today.setHours(0,0,0,0);
	
		 if (today.getTime() > start.getTime()) {
			 alert(settingMsg.conf_autoreply_28);
			 return;
		 }
	
		 if (start.getTime() > end.getTime()) {
			 alert(settingMsg.conf_autoreply_8);
			 return;
		 }
	
		 if (today.getTime() > end.getTime()) {
			 alert(settingMsg.conf_autoreply_7);
			 return;
		 }
    }

	jQuery("#whiteList").removeAttr("disabled");
	jQuery("#replyModeSelect").selectboxEnable();
    
    for(var i = 0 ; i < f.whiteList.length; i++) {
		f.whiteList.options[i].selected = true;
    }

	f.action = "/setting/saveAutoReply.do";
	f.method = "post";
	f.submit();
}

function changeMode() {
	var replyTarget = jQuery("#replyMode").val();	
	if (replyTarget == "REPLYALL") {
		jQuery("#replyTarget").hide();
	} else {
		jQuery("#replyTarget").show();
	}	
	jQuery(window).trigger("resize");
	
}

function activeMode() {
	var mode = jQuery("#mode").val();

	if (mode == "on") {
		jQuery("#startTime").removeAttr("disabled");
		jQuery("#endTime").removeAttr("disabled");
		jQuery("#autoReplySubject").removeAttr("disabled");
		jQuery("#autoReplyText").removeAttr("disabled");
		jQuery("#whiteList").removeAttr("disabled");
		jQuery("#replyModeSelect").selectboxEnable();
		jQuery("#whiteAddress").removeAttr("disabled");
	}
	else {
		jQuery("#startTime").attr("disabled", true);
		jQuery("#endTime").attr("disabled", true);
		jQuery("#autoReplySubject").attr("disabled", true);
		jQuery("#autoReplyText").attr("disabled", true);
		jQuery("#whiteList").attr("disabled", true);
		jQuery("#replyModeSelect").selectboxDisable();
		jQuery("#whiteAddress").attr("disabled", true);
	}
}

function addAddress(inputObj, listObj, maxCount, myEmail) {
	var mode = jQuery("#mode").val();

	if (mode == "on") {
		addList(inputObj, listObj, maxCount, myEmail);
	}
}

function delAddress(listObj){
	var mode = jQuery("#mode").val();

	if (mode == "on") {
		deleteList(listObj);
	}
}

function searchAddress(inputObj, listObj) {
	var mode = jQuery("#mode").val();

	if (mode == "on") {
		searchList(inputObj, listObj);
	}
}

function selAllAddress(listObj, id) {
	var mode = jQuery("#mode").val();

	if (mode == "on") {
		selectAll(listObj, id);
	}
}

function resetAuto() {
	location.reload();
}

onloadRedy("init()");
</script>

</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">

<form name="replyForm">
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.autoreply" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.autoreply.9" bundle="setting"/></li>
				</ul>
			</div>			
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.autoreply.10" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><span><tctl:msg key="conf.autoreply.control" bundle="setting"/></span></th>
							<td>								
								<div id="modeSelect"></div>
							</td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.autoreply.13" bundle="setting"/></span></th>
							<td>
								<span><tctl:msg key="conf.autoreply.start.date" bundle="setting"/></span>
								<input type="text" id="startTime" name="startTime" class="IP100px" value='<c:if test="${!empty replyVo.autoReplyStartTime}">${fn:substring(replyVo.autoReplyStartTime,0,4)}-${fn:substring(replyVo.autoReplyStartTime,4,6)}-${fn:substring(replyVo.autoReplyStartTime,6,8)}</c:if>' readonly>	
								<span>~</span>
								<span><tctl:msg key="conf.autoreply.end.date" bundle="setting"/></span>
								<input type="text" id="endTime" name="endTime" class="IP100px" value='<c:if test="${!empty replyVo.autoReplyEndTime}">${fn:substring(replyVo.autoReplyEndTime,0,4)}-${fn:substring(replyVo.autoReplyEndTime,4,6)}-${fn:substring(replyVo.autoReplyEndTime,6,8)}</c:if>' readonly>	
							</td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.autoreply.31" bundle="setting"/></span></th>
							<td>
                                <input type="text"id="autoReplySubject" name="autoReplySubject" class="IP300" value='<c:if test="${!empty replyVo.autoReplySubject}">${replyVo.autoReplySubject}</c:if>'>
							</td>
						</tr>
						<tr class="last">
							<th><span><tctl:msg key="conf.autoreply.21" bundle="setting"/></span></th>
							<td>
								<textarea name="autoReplyText" id="autoReplyText" rows="10" style="width:95%;">${replyVo.autoReplyText}</textarea>
							</td>
						</tr>
					</table>
					
					<div class="title_bar"><span><tctl:msg key="conf.autoreply.14" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><span><tctl:msg key="conf.autoreply.14" bundle="setting"/></span></th>
							<td>								
								<div id="replyModeSelect"></div>
							</td>
						</tr>
					</table>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">					
						<tr id="replyTarget">
							<th><span><tctl:msg key="conf.autoreply.20" bundle="setting"/></span></th>
							<td>
								<table class="TB_cols2_content" cellpadding="0" cellspacing="0">
									<tr>
										<td class="TD_selectBox" style="padding-left:0px;">
											<input type="text" id="whiteAddress" name="whiteAddress" class="IP100"><br>
											<select id="whiteList" multiple="multiple" class="selectBox" name="whiteList">
												<c:forEach var="list" items="${whiteList}">
												<option value="${list.replyAddress}">${list.replyAddress}</option>
												</c:forEach>
											</select>
										</td>
										<td class="btnArea">
											<ul>
												<li>
													<a href="javascript:addAddress(document.replyForm.whiteAddress, document.replyForm.whiteList,'100','${email}')" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
													<a href="javascript:searchAddress(document.replyForm.whiteAddress, document.replyForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
												</li>
												<li><a href="javascript:selAllAddress(document.replyForm.whiteList,'b_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
												<li><a href="javascript:delAddress(document.replyForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
											</ul>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>									
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="saveAuto()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetAuto()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
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