<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>

<script language="javascript">
function init(){	
	<%@ include file="settingCommonScript.jsp" %>
	
	jQuery("#forward_menu").addClass("on");

	jQuery("#forwardModeSelect").selectbox({selectId:"forwardMode",selectFunc:""},
		"${info.forwardingMode}",
		[{index:settingMsg.conf_forward_13,value:"none"},
		 {index:settingMsg.conf_forward_8,value:"forwarding"},
		 {index:settingMsg.conf_forward_9,value:"forwardingonly"}]);

	jQuery("#defineTypeSelect").selectbox({selectId:"defineType",selectFunc:""},
			"${fn:escapeXml(defineType)}",
			[{index:settingMsg.conf_forward_27,value:"mail"},
			 {index:settingMsg.conf_forward_28,value:"domain"}]);	
	 	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");
}

<%@ include file="settingFrameScript.jsp" %>

function saveForward(){
	var f = document.forwardForm;

	if (f.forwardingAddress.length == 0) {
		f.forwardMode.selectedIndex = 0;
	}
	
	for (var i = 0; i < f.forwardingAddress.length; i++) {
		f.forwardingAddress.options[i].selected = true;
	}

	for (var i = 0; i < f.defineForwardingAddress.length; i++) {
		f.defineForwardingAddress.options[i].selected = true;
	}

	if(f.defineForwardingAddress.length > 0){
		if(f.defineType.value == 'mail'){
			if(!isMail(f.defineValue.value)){
				alert(comMsg.common_form_004);
				f.defineValue.focus();
				return;
			}
		}
		else if(f.defineType.value=='domain'){
			if(!isDomain(f.defineValue.value)){
				alert(comMsg.error_domain);
				f.defineValue.focus();
				return;
			}
		}
	}

	f.action="/setting/saveForward.do";
	f.method="post"; 
    f.submit();
}

function addForwardList(){
	addList(document.forwardForm.email, document.forwardForm.forwardingAddress,'${maxForwarding}','${email}','true');
}

function addDefineForwardList(){
	addList(document.forwardForm.defineForwardingAddressText, document.forwardForm.defineForwardingAddress,'${maxForwarding}','${define}','true');
}

function resetForward() {
	location.reload();
}

function deleteDefineForward(seq){
	var f = document.forwardForm;
	if(seq){
		if(f.defineCheck.length > 1){
			f.defineCheck[seq].checked = true;
		}
		else{
			f.defineCheck.checked = true;	
		}
	}
	
	if (checkedCnt(f.defineCheck) == 0) {
		alert(settingMsg.conf_forward_29);
		return;
	}
	if (!confirm(settingMsg.conf_filter_3)){
        return;
    }
	f.action="/setting/deleteDefineForward.do";
	f.method="post"; 
    f.submit();
}

function updateDefineForward(seq){	
	var f = document.forwardForm;
	if(seq){
		if(f.defineCheck.length > 1){
			f.defineCheck[seq].checked = true;
		}
		else{
			f.defineCheck.checked = true;	
		}
	}
	var checkCount = checkedCnt(f.defineCheck);
	if (checkCount == 0) {
		alert(settingMsg.conf_forward_29);
		return;
	}
	else if(checkCount > 1){
		alert(settingMsg.conf_forward_30);
		return;
	}	

	f.actionType.value="update";
	f.action="/setting/viewForward.do";
	f.method="post"; 
    f.submit();
}
onloadRedy("init()");
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
	<form name="forwardForm" onsubmit="return false;">
		<input type="hidden" name="actionType" value="${fn:escapeXml(actionType)}"/>
		<input type="hidden" name="defineForwardingSeq" value="${fn:escapeXml(defineForwardingSeq)}"/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.forward" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
				<div class="explanation">
					<ul>
						<li><tctl:msg key="conf.forward.6" bundle="setting"/></li>
					</ul>
				</div>
				<div id="s_contentBody" >
					<div id="s_contentMain">
						<div id="main_wrapper" class="smain_content_wrapper">					
							<div>
								<div class="title_bar"><span><tctl:msg key="conf.forward.title" bundle="setting"/> (<tctl:msg key="conf.forward.15" bundle="setting" arg0="${fn:escapeXml(maxForwarding)}"/>)</span></div>
								<table class="TB_cols2" cellpadding="0" cellspacing="0">
									<tr>
										<th><span><tctl:msg key="conf.forward.7" bundle="setting"/></span></th>
										<td>								
											<div id="forwardModeSelect"></div>
										</td>
									</tr>
									<tr>
										<th><span><tctl:msg key="conf.forward.17" bundle="setting"/></span></th>
										<td>
											<table class="TB_cols2_content" style="table-layout:fixed;">
												<colgroup span="2">
													<col></col>
													<col width="120px"></col>
												</colgroup>					
												<tr>
													<td colspan="2"><tctl:msg key="conf.forward.24" bundle="setting"/></td>
												</tr>
												<tr>
													<td class="TD_selectBox">
														<input type="text" name="email" class="IP100" onKeyPress="(keyEvent(event) == 13) ? addForwardList() : '';"/>
														<select multiple="multiple" class="selectBox" name="forwardingAddress">
															<c:forEach var="email" items="${info.forwardingAddress}">
															<option value="${fn:escapeXml(email)}">${fn:escapeXml(email)}</option>
															</c:forEach>
														</select>
													</td>
													<td class="btnArea">
														<ul>
															<li>
																<a href="javascript:addList(document.forwardForm.email, document.forwardForm.forwardingAddress,'${fn:escapeXml(maxForwarding)}','${fn:escapeXml(email)}','true')" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
																<a href="javascript:searchList(document.forwardForm.email, document.forwardForm.forwardingAddress)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
															</li>
															<li><a href="javascript:selectAll(document.forwardForm.forwardingAddress,'w_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
															<li><a href="javascript:deleteList(document.forwardForm.forwardingAddress)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
														</ul>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr class="last">
										<th><span><tctl:msg key="conf.forward.18" bundle="setting"/></span></th>
										<td>
											<table class="TB_cols2_content" style="table-layout:fixed;">
												<colgroup span="2">
													<col></col>
													<col width="120px"></col>
												</colgroup>									
												<tr>
													<td colspan="2">
														<div style="float:left;"><div id="defineTypeSelect"></div></div>
														<div style="float:left;padding-left:5px;"><input type="text" name="defineValue"  class="IP100px" width="140px" maxlength="64" value="${fn:escapeXml(defineValue)}" style="margin:0px;"></div>
                                                        <div style="float:left;padding-left:5px;padding-top:3px;"><tctl:msg key="conf.forward.26" bundle="setting"/></div>      													
													</td>
												</tr>
												<tr>
													<td class="TD_selectBox">
														<input type="text" name="defineForwardingAddressText" class="IP100" onKeyPress="(keyEvent(event) == 13) ? addDefineForwardList() : '';"/>
														<select multiple="multiple" class="selectBox" name="defineForwardingAddress">
															<c:forEach var="define" items="${defineForwardInfo.forwarding_address_list}">
																<option value="${fn:escapeXml(define)}">${fn:escapeXml(define)}</option>
															</c:forEach>
														</select>
													</td>
													<td class="btnArea">
														<ul>
															<li>
																<a href="javascript:addList(document.forwardForm.defineForwardingAddressText, document.forwardForm.defineForwardingAddress,'${fn:escapeXml(maxForwarding)}','${fn:escapeXml(email)}','true')" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
																<a href="javascript:searchList(document.forwardForm.defineForwardingAddressText, document.forwardForm.defineForwardingAddress)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
															</li>
															<li><a href="javascript:selectAll(document.forwardForm.defineForwardingAddress,'w_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
															<li><a href="javascript:deleteList(document.forwardForm.defineForwardingAddress)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
														</ul>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>					
								<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
									<tr>
										<td align="center" class="TM_s_btnArea">
											<c:if test="${actionType eq 'update'}">
												<a class="btn_style2" href="#" onclick="saveForward()"><span><tctl:msg key="common.modify" bundle="setting"/></span></a>
											</c:if>
											<c:if test="${actionType ne 'update'}">
												<a class="btn_style2" href="#" onclick="saveForward()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
											</c:if>
											<a class="btn_style3" href="#" onclick="resetForward()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
										</td>
									</tr>
								</table>
								<div class="title_bar"><span><tctl:msg key="conf.forward.19" bundle="setting"/></span></div>	
								<table class="TB_cols2" cellpadding="0" cellspacing="0">
									<tr>
										<td colspan="2">
											<ul>
												<li>
													<a href="javascript:updateDefineForward();" class="btn_style4"><span><tctl:msg key="common.24" bundle="setting"/></span></a>
													<a href="javascript:deleteDefineForward();" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a>
												</li>
											</ul>
										</td>
									</tr>
								</table>
								<div>	
									<table id="settingListTable" cellpadding="0" cellspacing="0">
										<colgroup span="4">
											<col width="30px"></col>
											<col></col>
											<col width="120px"></col>
											<col width="120px"></col>
										</colgroup>
										<tr>
											<th><input type="checkbox" name="allDefineCheck" id="allCheck" onclick="checkAll(this,forwardForm.defineCheck)"></th>
											<th><tctl:msg key="conf.forward.20" bundle="setting"/></th>
											<th><tctl:msg key="conf.forward.21" bundle="setting"/></th>
											<th><tctl:msg key="common.24" bundle="setting"/> | <tctl:msg key="common.11" bundle="setting"/></th>
										</tr>
										<c:forEach var="defineForward" items="${defineForwardingList}" varStatus="status">
											<tr>
												<td><input type="checkbox" name="defineCheck" id="defineCheck" value="${fn:escapeXml(defineForward.define_forwarding_seq)}"></td>
												<td>													
													<c:if test='${fn:escapeXml(defineForward.from_address) ne null}'>													
														${fn:escapeXml(defineForward.from_address)}
													</c:if>
													<c:if test='${fn:escapeXml(defineForward.from_domain) ne null}'>
														${fn:escapeXml(defineForward.from_domain)}
													</c:if>
												</td>
												<td>
													<c:forEach var="address" items="${defineForward.forwarding_address_list}">
														${fn:escapeXml(address)}<br>
													</c:forEach>
												</td>
												<td>
													<a href="javascript:updateDefineForward('${status.count-1}');"><tctl:msg key="common.24" bundle="setting"/></a>|
													<a href="javascript:deleteDefineForward('${status.count-1}');"><tctl:msg key="common.11" bundle="setting"/></a>
												</td>										
											</tr>	
										</c:forEach>								
									</table>
								</div>
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