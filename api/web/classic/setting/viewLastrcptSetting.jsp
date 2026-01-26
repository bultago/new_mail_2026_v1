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

<%
	int maxRcptCount = com.terracetech.tims.webmail.util.StringUtils.isEmpty(com.terracetech.tims.webmail.common.EnvConstants.getMailSetting("mail.write.last.rcpt.count")) 
			? 100 : Integer.parseInt(com.terracetech.tims.webmail.common.EnvConstants.getMailSetting("mail.write.last.rcpt.count"));
    String agt = request.getHeader("USER-AGENT");
    agt = StringEscapeUtils.escapeHtml(agt);
%>
<script language="javascript">

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
	
	jQuery("#lastrcpt_menu").addClass("on");
	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
}

<%@ include file="settingFrameScript.jsp" %>

function saveLastrcpt(){
	var f = document.lastrcptForm;
	
	f.deleteList.value = array;
	
	for (i = 0; i < f.lastrcptList.length; i++) {
        f.lastrcptList.options[i].selected = true;
    }
    f.action = "/setting/deleteLastrcpt.do";
    f.method = "post";
    f.submit();
}


function resetLastrcptSetting() {
	location.reload();
}

function resetLastrcptAll() {
	var confirmStr = "<tctl:msg key="conf.lastrcpt.total.del.confirm" bundle="setting"/>";
	if(confirm(confirmStr)){
		var f = document.lastrcptForm;
		f.all.value = "true";
		f.action = "/setting/deleteLastrcpt.do";
	    f.method = "post";
	    f.submit();
	}
}

var array = "";

function deleteLastrcptList(selObj) {
	move = new Array();
	var count=0;
	
	
	for(var i=0;i<selObj.options.length;i++){
		if(selObj.options[i].selected){			
			array += selObj.options[i].value + ",";
			move[count] = selObj.options[i].text;
			 count++;
			 
			 
		}
	}
	
	if (count == 0) {
		alert(comMsg.common_form_007);
		return;
	}
	else if (count > 0) {
		if(!confirm(comMsg.common_form_008+"\n"+settingMsg.conf_lastrcpt_del_add_desc)) {
			return;
		}
	}
	
	for(i=0;i<move.length;i++){
		for(j=0;j<selObj.options.length;j++){
			if(selObj.options[j].text==move[i]) {
				if(getBrowserType() == "ie" || getBrowserType() == "opera")
				selObj.options.remove(j);
				else if(getBrowserType() == "nav" || getBrowserType() == "gecko")
				selObj.options[j] = null;
			}
		}
	}	
}
function searchLastRctpList(txtObj, selObj) {
	var address = txtObj.value.toLowerCase();
	
	clearSelObj(selObj);

	var isPattern = false;
	
	if (trim(txtObj.value) == "") {
        alert(comMsg.common_form_006);
        return;
    }

	for (var i = 0; i < selObj.length; i++) {
		
		if (selObj.options[i].text.indexOf(address) >= 0) {			
			selObj.options[i].selected = true;
			isPattern = true;
		}
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


<form name="aaa">
<input type="hidden" id="aaa" name="aaa" value="aaa" />
<input type="hidden" name="bbb" value="bbb" />
<input type="hidden" name="ccc" value="ccc" />
<input type="hidden" name="ddd" value="ddd" />
</form>


<form name="lastrcptForm">
<input type="hidden" name="deleteList" value=""/>
<input type="hidden" name="all" value=""/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.lastrcpt.menu" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.lastrcpt.title" bundle="setting"/></li>
					<li><b><tctl:msg key="conf.lastrcpt.title.desc.1" bundle="setting"/><%=maxRcptCount %><tctl:msg key="conf.lastrcpt.title.desc.2" bundle="setting"/></b></li>
					<li><b><tctl:msg key="conf.lastrcpt.title.desc.3" bundle="setting"/></b></li>					
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
					<div>					
					<div id="blackAndWhiteDiv">
					<div class="title_bar"><span><tctl:msg key="conf.lastrcpt.address" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th style="width:160px;"><tctl:msg key="conf.lastrcpt.list" bundle="setting"/></th>
							<td>
								<table class="TB_cols2_content" collspan="0" cellpadding="0" border="0">
									<tr>
									<td>
										<input type="text" name="search_address" class="IP100">
									</td>
									<td class="btnArea">
										<a href="javascript:searchLastRctpList(document.lastrcptForm.search_address, document.lastrcptForm.lastrcptList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
									</td>
									</tr>
									<tr>
									<td class="TD_selectBox">										
										<select multiple="multiple" class="selectBox" name="lastrcptList">
											<c:forEach var="lastRcpts" items="${lastRcpts}">
											<option value="${fn:escapeXml(lastRcpts.rcptSeq)}">${fn:escapeXml(lastRcpts.address)}</option>
											</c:forEach>
										</select>
									</td>
									<td class="btnArea">
										<ul>											
											<li><a href="javascript:selectAll(document.lastrcptForm.lastrcptList,'w_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
											<li><a href="javascript:deleteLastrcptList(document.lastrcptForm.lastrcptList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
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
						<a class="btn_style2" href="#" onclick="saveLastrcpt()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetLastrcptSetting()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						<a class="btn_style2" href="#" onclick="resetLastrcptAll()"><span><tctl:msg key="conf.lastrcpt.total.del.button" bundle="setting"/></span></a>
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