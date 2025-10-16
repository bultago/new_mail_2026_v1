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
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>

</head>

<script language="javascript">
function init(){	
	<%@ include file="settingCommonScript.jsp" %>	
	jQuery("#filter_menu").addClass("on");
	jQuery("#filterApplySelect").selectbox({selectId:"filterApply",
		selectFunc:""},
		"${filterVo.apply}",
		[{index:settingMsg.conf_filter_52,value:"on"},
		 {index:settingMsg.conf_filter_53,value:"off"}]);

	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);	 		
	jQuery(window).trigger("resize");
}

<%@ include file="settingFrameScript.jsp" %>

function saveFilter(mode){
	var f = document.filterForm;

	if (mode == 'save') {
		var filterLength = objCnt(f.rules);
		if (filterLength >= 1000) {
			alert(msgArgsReplace(settingMsg.conf_forward_15,[1000]));
			return;
		}
	}
	
	if(trim(f.sender.value) == "" && trim(f.receiver.value) == "" && trim(f.subject.value) == "") {
		alert(settingMsg.conf_filter_1);
		f.sender.focus();
		return;
	}

	if(!checkInputLength("", f.sender, "", 0, 255)) {
		return;
	}
	if (f.sender.value != "" && !isEmailCheck(f.sender.value) ) {
        alert(settingMsg.conf_filter_44);
        f.sender.focus();
        return;
    }

	if(!checkInputLength("", f.receiver, "", 0, 255)) {
		return;
	}

	if (f.receiver.value != "" && !isEmailCheck(f.receiver.value)) {
        alert(settingMsg.conf_filter_44);
        f.receiver.focus();
        return;
	}

    if(!checkInputLength("", f.subject, "", 0, 255)) {
		return;
	}
	if(!checkInputValidate("", f.subject, "onlyBack")) {
		return;
	}

	if(!f.mbox.checked){
		if (f.policy.value == "") {
			alert(settingMsg.conf_alert_mailbox_select);
			return;
		}
    }
    else {
    	var newbox = trim(f.inputBoxName.value);

    	if(!checkInputLength("", f.inputBoxName, settingMsg.conf_filter_37, 2, 32)) {
    		return;
    	}
    	if(!checkInputValidate("", f.inputBoxName, "folderName")) {
    		return;
    	}
    	
        if (trim(f.parentFolder.value) == "") {
        	f.boxName.value = newbox;
        }else {
        	 f.boxName.value = f.parentFolder.value + "." + newbox;
        } 
    }

	if (mode == 'modify') {
		f.action = "/setting/modifyFilter.do";
	}
	else {
		f.action = "/setting/saveFilter.do";
	}
	f.method = "post";
    f.submit();
}
function isEmailCheck(email){
    if(email.indexOf("@")> -1 && !isEmail(email) && !isMailDomain(email) && !isMailSubDomain(email)){
        return false;
    }
    return true ;
}

function saveFilterApply() {
	var f = document.filterForm;
	f.action = "/setting/saveFilterApply.do";
	f.method = "post";
	f.submit();
}

function deleteFilter(){
	var f = document.filterForm;

	if (checkedCnt(f.rules) == 0) {
		alert(settingMsg.conf_filter_36);
		return;
	}

	if (!confirm(settingMsg.conf_filter_3)){
        return;
    }
	
	f.action = "/setting/deleteFilter.do";
	f.method = "post";
    f.submit();
}

function deleteFilter1(index) {
	var f = document.filterForm;

	if(f.rules.length > 1){
		for ( var i = 0; i < f.rules.length; i++) {
			f.rules[i].checked = false;
		}
	}

	if (!confirm(settingMsg.conf_filter_3)){
        return;
    }
	
	f.rule.value = index;	
	f.action = "/setting/deleteFilter.do";
	f.method = "post";
	f.submit();
}

function modifyFilter() {
	var f = document.filterForm;

	if (checkedCnt(f.rules) == 0) {
		alert(settingMsg.conf_filter_36);
		return;
	}

	if (checkedCnt(f.rules) > 1) {
		alert(settingMsg.conf_alert_modify_overselect);
		return;
	}

	var data;
	if (f.rules.length) {
		for (i=0; i<f.rules.length; i++) {
			if (f.rules[i].checked) {
				data = f.rules[i].value;
				break;
			}
		}
	} else {
		data = f.rules.value;
	}
	var datas = data.split('-|-');

	modifyForm(datas[0], datas[1], datas[2], datas[4], datas[5]);
}

function modifyForm(index, sender, receiver, count, target) {

	var f = document.filterForm;
	
	jQuery("#sender").val(sender);
	jQuery("#receiver").val(receiver);
	
	if(document.getElementsByName("subjectFilterValue").length > 1){
		jQuery("#subject").val(document.getElementsByName("subjectFilterValue")[parseInt(count)].value);	
	}else{
		jQuery("#subject").val(document.getElementsByName("subjectFilterValue")[0].value);
	}

	jQuery("#saveButton").hide();
	jQuery("#modifyButton").show();

	f.policy.value = "move "+target;
	f.condSeq.value = index;

	<c:forEach var = "defaultFolder" items="${defaultFolderBeans}">
		setFolderName('${defaultFolder.fullName}', '${defaultFolder.name}', target);
	</c:forEach>

	<c:forEach var = "userFolder" items="${userFolderBeans}">
		setFolderName('${userFolder.fullName}', '${userFolder.name}', target);
	</c:forEach>
}

function modifyCancel(msg) {
	var f = document.filterForm;
	
	jQuery("#sender").val("");
	jQuery("#receiver").val("");
	jQuery("#subject").val("");

	f.mbox.checked = false;
	checkBox();
	
	jQuery("#saveButton").show();
	jQuery("#modifyButton").hide();

	f.policy.value = "";
	f.condSeq.value = "";

	jQuery("#parentFolder_default").text(msg);
}

function folderList(target) {
	var listItem = "";
	if (target != 'new') { 
		<c:forEach var = "defaultFolder" items="${defaultFolderBeans}">
			listItem += setFolderList("${defaultFolder.fullName}","${defaultFolder.name}", target);			
		</c:forEach>
	}	
	<c:forEach var = "userFolder" items="${userFolderBeans}">
		listItem +=setFolderList("${userFolder.fullName}",escape_tag("${userFolder.name}"), target);
	</c:forEach>

	document.write(listItem);
}

function checkBox(){
	var f = document.filterForm;
	if(f.mbox.checked){
		jQuery("#newPfBoxWrapper").show();
		f.inputBoxName.disabled = false;
		f.inputBoxName.focus();
		jQuery("#currentPfBoxWrapper").hide();		
	}else if(!f.mbox.checked){
        f.inputBoxName.disabled = true;
        jQuery("#newPfBoxWrapper").hide();
        jQuery("#currentPfBoxWrapper").show();
	}
}

function openMailBox(id) {
	var options = {};
	jQuery("#"+id).show("blind",options,500,callback);
}
function closeMailBox(id) {
	var options = {};
	jQuery("#"+id).hide("blind",options,500,callback);
}

onloadRedy("init()");
</script>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="filterForm">
<input type="hidden" name="policy" id="policy">
<input type="hidden" name="condSeq">
<input type="hidden" name="parentFolder" id="parentFolder">
<input type="hidden" name="rule">
<input type="hidden" id="boxName" name="boxName"/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.filter" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.filter.4" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.filter.51" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><tctl:msg key="conf.filter.contol" bundle="setting"/></th>
							<td>
								<div style="float:left;white-space:nowrap;padding-right:5px;">	
									<div id="filterApplySelect"></div>
								</div>
								<div style="float:left;white-space:nowrap;">
									<a href="#" class="btn_style4" onclick="saveFilterApply()"><span><tctl:msg key="common.7" bundle="setting"/></span></a>
								</div>	
							</td>
						</tr>
					</table>
					<div class="title_bar"><span><tctl:msg key="conf.filter.5" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><tctl:msg key="conf.filter.26" bundle="setting"/></th>
							<td>
								<table class="TB_cols2_content" cellpadding="0" cellspacing="0">
									<tr>
										<td class="filterPre"><tctl:msg key="conf.filter.48" bundle="setting"/></td>
										<td><input type="text" class="IP100" maxlength="255" id="sender" name="sender" /></td>
										<td width="140px"><tctl:msg key="conf.filter.23" bundle="setting"/></td>
									</tr>
									<tr>
										<td class="filterPre"><tctl:msg key="conf.filter.49" bundle="setting"/></td>
										<td><input type="text" class="IP100" maxlength="255" id="receiver" name="receiver" /></td>
										<td width="140px"><tctl:msg key="conf.filter.23" bundle="setting"/></td>
									</tr>
									<tr>
										<td class="filterPre"><tctl:msg key="conf.filter.50" bundle="setting"/></td>
										<td><input type="text" class="IP100" maxlength="255" id="subject" name="subject" /></td>
										<td width="140px"><tctl:msg key="conf.filter.30" bundle="setting"/></td>
									</tr>
									<tr id="currentPfBoxWrapper">
										<td class="filterPre" style="padding-top: 10px"><tctl:msg key="conf.filter.14" bundle="setting"/></td>
										<td style="padding-top: 10px" nowrap>
											<a href="javascript:" id="parentFolder_default" onclick="viweStatus='pfBox'; menuLayerOpen('pfBox')" onMouseOut="viweStatus='out'; menuLayerOut('pfBox')" class="selectMailBoxA"> ::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> ::::: </a>
											<div id="pfBox" onMouseOver="viweStatus='pfBox'; menuLayerOpen('pfBox')" onMouseOut="viweStatus='out'; menuLayerOut('pfBox')" class="gutter_layer div_scroll selectMailBox">
												<ul class="list_st_02" style="padding:0px 0px 0px 5px; margin:0px; list-style-image:none">
													<li onmouseover='yellowline(this)' onmouseout='grayline(this)' style="background:none">
														<a href="javascript:selPFolder('::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::','', true)">::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::</a>
													</li>
													<script type="text/javascript">
														folderList();
													</script>
												</ul>
											</div>
											<div style="position:relative;">
												<div style="position:absolute;left:185px;top:-15px">
													<tctl:msg key="conf.filter.34" bundle="setting"/>
												</div>
											</div>
										</td>
										<td width="140px" style="padding-top: 10px"> </td>
									</tr>
									<tr>
										<td class="filterPre" style="padding-top: 5px" valign="top">
											<input type="checkbox" name="mbox" class="TM_ST_NONE" onClick="checkBox()"> <tctl:msg key="conf.filter.31" bundle="setting"/>
										</td>
										<td style="padding-top: 5px">
											<div id="newPfBoxWrapper" style="display:none;white-space:nowrap;" nowrap>
												<a href="javascript:" id="parentFolder_new" onclick="viweStatus='newPfBox'; menuLayerOpen('newPfBox')" onMouseOut="viweStatus='out'; menuLayerOut('newPfBox')" class="selectMailBoxA"> ::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> ::::: </a>
												<div id="newPfBox" onMouseOver="viweStatus='newPfBox'; menuLayerOpen('newPfBox')" onMouseOut="viweStatus='out'; menuLayerOut('newPfBox')" class="gutter_layer div_scroll selectMailBox">
													<ul class="list_st_02" style="padding:0px 0px 0px 5px; margin:0px; list-style-image:none">
														<li onmouseover='yellowline(this)' onmouseout='grayline(this)' style="background:none">
															<a href="javascript:selPFolder('::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> :::::','', true, 'new')">::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> :::::</a>
														</li>
														<script type="text/javascript">
															folderList('new');
														</script>
													</ul>
												</div>																														
												<input type="text" class="IP100" maxlength="64" name="inputBoxName" disabled/>
												<tctl:msg key="conf.filter.34" bundle="setting"/>
											</div>																																											
										</td>
										<td valign="top">				
										</td>
									</tr>									
									<tr>
										<td colspan="3">
											<div class="btnArea">
												<a id="saveButton" class="btn_style2" href="#" onclick="saveFilter('save')"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
												<a id="modifyButton" class="btn_style2" href="#" onclick="saveFilter('modify')" style="display:none"><span><tctl:msg key="common.modify" bundle="setting"/></span></a>
												<a class="btn_style3" href="#" onclick="modifyCancel('::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::')"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<div class="title_bar"><span><tctl:msg key="conf.filter.8" bundle="setting"/></span></div>
					<div class="sub_toolbar">
						<a href="#" onclick="modifyFilter()" class="btn_style4"><span><tctl:msg key="common.24" bundle="setting"/></span></a>
						<a href="#" onclick="deleteFilter()" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a>
					</div>
					<table id="settingListTable" cellpadding="0" cellspacing="0">
						<colgroup span="4">
							<col width="30px"></col>
							<col></col>
							<col width="120px"></col>
							<col width="120px"></col>
						</colgroup>
						<tr>
							<th><input type="checkbox" onclick="checkAll(this,filterForm.rules)"/></th>
							<th><tctl:msg key="conf.filter.21" bundle="setting"/></th>
							<th><tctl:msg key="conf.filter.32" bundle="setting"/></th>
							<th><tctl:msg key="common.24" bundle="setting"/> | <tctl:msg key="common.11" bundle="setting"/></th>
						</tr>
						<c:if test="${empty filters}">
						<tr>
							<td colspan="4" align="center"><tctl:msg key="conf.filter.list.empty" bundle="setting"/></td>
						</tr>
						</c:if>
						<c:forEach var="filter" items="${filters}" varStatus="loop">
						<c:set var="tmpTarget" value="${fn:escapeXml(filter.policy)}"/>
						<c:set var="index" value="${fn:indexOf(tmpTarget, ' ')}"/>
						<c:set var="target" value="${fn:substring(tmpTarget,index+1,-1)}"/>
						<input type="hidden" name="subjectFilterValue" value='${fn:escapeXml(filter.subject)}'/>
						<tr>
							<td align="center">
								<input type="checkbox" name="rules" value="${filter.index}-|-${fn:escapeXml(filter.sender)}-|-${fn:escapeXml(filter.receiver)}-|-${fn:escapeXml(filter.subject)}-|-${loop.count-1}-|-${fn:escapeXml(target)}">
							</td>
							<td class="subject">
								<c:if test="${!empty filter.sender && filter.sender != ''}" var = "sender">
							  	<p><tctl:msg key="conf.filter.48" bundle="setting"/> <font color="blue">${fn:escapeXml(filter.sender)}</font> <tctl:msg key="conf.filter.30" bundle="setting"/> </p>
							  	</c:if>
								<c:if test="${!empty filter.receiver && filter.receiver != ''}" var = "receiver">
							  	<p><tctl:msg key="conf.filter.49" bundle="setting"/> <font color="blue">${fn:escapeXml(filter.receiver)}</font> <tctl:msg key="conf.filter.30" bundle="setting"/></p>
							  	</c:if>  
								<c:if test="${!empty filter.subject && filter.subject != ''}" var = "subject">
							  	</p><tctl:msg key="conf.filter.50" bundle="setting"/> <font color="blue">${fn:escapeXml(filter.subject)}</font> <tctl:msg key="conf.filter.30" bundle="setting"/></p>
							  	</c:if>  
							</td>
							<td>
							    <c:choose>	
								     <c:when test="${target == 'Inbox'}">
								     <tctl:msg key="conf.filter.39" bundle="setting"/>
								     </c:when>
								     <c:when test="${target == 'Sent'}">
								     <tctl:msg key="conf.filter.38" bundle="setting"/>
								     </c:when>
								     <c:when test="${target == 'Trash'}">
								     <tctl:msg key="conf.filter.41" bundle="setting"/>
								     </c:when>
								     <c:when test="${target == 'Spam'}">
								     <tctl:msg key="conf.filter.42" bundle="setting"/>
								     </c:when>
								     <c:when test="${target == 'Reserved'}">
								     <tctl:msg key="conf.filter.43" bundle="setting"/>
								     </c:when>
								     <c:when test="${target == 'Drafts'}">
								     <tctl:msg key="conf.filter.40" bundle="setting"/>
								     </c:when>
								     <c:otherwise>
								     ${fn:replace(target,".","/")}
								     </c:otherwise>
							     </c:choose>
							</td>
							<td>
								<a href="#" onclick = "modifyForm('${filter.index}', '${fn:escapeXml(filter.sender)}', '${fn:escapeXml(filter.receiver)}', ${loop.index}, '${fn:escapeXml(target)}')"><tctl:msg key="common.24" bundle="setting"/></a> | 
						  		<a href="#" onclick="deleteFilter1('${filter.index}')"><tctl:msg key="common.11" bundle="setting"/></a>
							</td>
						</tr>
						</c:forEach>
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