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
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
</head>

<script language="javascript">
function init(){	
	<%@ include file="settingCommonScript.jsp" %>	
	jQuery("#extmail_menu").addClass("on");

	<c:if test="${installLocale != 'jp'}">
	jQuery("#pop3ListSelect").selectbox({selectId:"pop3List",selectFunc:setPOP3Server},
			"",
			 [{index:settingMsg.conf_pop_11,value:""},
              {index:settingMsg.conf_pop_user_input,value:"userInput"},
             {index:settingMsg.conf_pop_38,value:"pop.naver.com|ssl"},
             {index:settingMsg.conf_pop_53,value:"pop.daum.net|ssl"},
             {index:settingMsg.conf_pop_13,value:"pop3.nate.com|ssl"},
             {index:settingMsg.conf_pop_12,value:"pop.gmail.com|ssl"},
             //{index:settingMsg.conf_pop_14,value:"pop.paran.com|ssl"},
             //{index:settingMsg.conf_pop_15,value:"pop.dreamwiz.com|ssl"},
             {index:settingMsg.conf_pop_16,value:"pop.mail.yahoo.com|ssl"},
             {index:settingMsg.conf_pop_17,value:"pop3.live.com|ssl"},
             {index:settingMsg.conf_pop_18,value:"pop.chol.com"},
             {index:settingMsg.conf_pop_19,value:"kornet.net"},
             {index:settingMsg.conf_pop_20,value:"mail.hanafos.com"},
             //{index:settingMsg.conf_pop_55,value:"pop.korea.com"},
             {index:settingMsg.conf_pop_56,value:"mail.unitel.co.kr"},
             {index:settingMsg.conf_pop_57,value:"email.kebi.com"}]);
	</c:if>
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");
	
}

<%@ include file="settingFrameScript.jsp" %>

function saveExtMail(type){
	var f = document.extMailForm;

	if(type != 'modify') {
		if (f.rules && f.rules.length && f.rules.length >= 7) {
			alert(settingMsg.conf_pop_8);
			return;
		}
	}

	if(!checkInputLength("", f.pop3Host, settingMsg.conf_pop_1 , 2, 255)) {
		return;
	}
	if (!validateIP(f.pop3Host.value) && !isDomain(f.pop3Host.value)) {
		alert(settingMsg.conf_pop_37);
		f.pop3Host.focus();
		return;
	} 
	
	if(trim(f.pop3Port.value) == ""){
        alert(settingMsg.conf_pop_40);
        f.pop3Port.focus();
        return;
    }
	else {
		if (!isPort(f.pop3Port.value)) {
			alert(settingMsg.conf_pop_43);
			return;
		}
	}

	if(trim(f.pop3Id.value) == ""){
        alert(settingMsg.conf_pop_2);
        f.pop3Id.focus();
        return;
    }

	if((!isLocal(f.pop3Id.value)) && (!isEmail(f.pop3Id.value))){
		alert(settingMsg.conf_pop_52);
        f.pop3Id.focus();
		return;
	}

	if(trim(f.pop3Pw.value) == ""){
        alert(settingMsg.conf_pop_6);
        f.pop3Pw.focus();
        return;
    }

	if(f.mbox[0].checked){
		if (f.policy.value == "") {
			alert(settingMsg.conf_alert_mailbox_select);
			return;
		}
    }
    else {
    	var newbox = trim(f.inputBoxName.value);

    	if(newbox == ""){
    		alert(settingMsg.conf_filter_37);
            f.inputBoxName.value = "";
    		f.inputBoxName.focus();
    		return;
    	}

    	if(!isFolderName(newbox)) {
            alert(settingMsg.conf_alert_mailbox_invalidname+"\n\n"
            		+"%,&,*,.,/,\\,',`,\"");
            f.inputBoxName.value = "";
            f.inputBoxName.focus();
            return;
        }
    	
        if (trim(f.parentFolder.value) == "") {
        	f.boxName.value = newbox;
        } 
        else {
        	f.boxName.value = f.parentFolder.value + "." + newbox;
        } 
    }

	jQuery("#pop3Id").attr("disabled", false);
	jQuery("#pop3Host").attr("disabled", false);

    if(type == 'modify') {
    	f.action="/setting/modifyExtMail.do";
    }
    else {
    	f.action="/setting/saveExtMail.do";
    }
	f.method="post";
    f.submit();
}

function deleteExtMail(){
	var f = document.extMailForm;

	if (checkedCnt(f.rules) == 0) {
		alert(settingMsg.conf_alert_delete_noselect);
		return;
	}

	if (!confirm(settingMsg.conf_filter_3)){
        return;
    }
	
	f.action = "/setting/deleteExtMail.do";
	f.method="post";
    f.submit();
}

function deleteExtMail1(host, id) {
	var f = document.extMailForm;

	if (!confirm(settingMsg.conf_filter_3)){
        return;
    }
	
	f.rule.value = host + "|" + id;
	f.action = "/setting/deleteExtMail.do";
	f.method = "post";
	f.submit();
	
}

function modifyExtMail() {
	var f = document.extMailForm;

	if (checkedCnt(f.rules) == 0) {
		alert(settingMsg.conf_alert_modify_noselect);
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
	var datas = data.split('|');

	modifyForm(datas[0], datas[1], datas[2], datas[3], datas[4], datas[5]);
}

function modifyForm(pop3Host, pop3Id, pop3Port, pop3Del, target, useSsl) {

	var f = document.extMailForm;
	
	jQuery("#pop3Id").val(pop3Id);
	jQuery("#pop3Id").attr("disabled", true);
	jQuery("#pop3Host").val(pop3Host);
	jQuery("#pop3Host").attr("disabled", true);
	jQuery("#pop3Port").val(pop3Port);
	jQuery("#pop3Port").attr("disabled", false);
	jQuery("#pop3Pw").attr("disabled", false);
	if (useSsl == '1') {
		jQuery("#sslCheck").attr("checked",true);
	} 
	else {
		jQuery("#sslCheck").attr("checked",false);
	}
	jQuery("#sslCheck").attr("disabled", false);

	jQuery("#pop3ListSelect").selectboxDisable();

	if (pop3Del == '1') {
		f.pop3del.checked = true;
	}

	jQuery("#modifyMessageTr").show();

	jQuery("#saveButton").css("display","none");
	jQuery("#modifyButton").css("display","")

	f.policy.value = "move "+target;

	<c:forEach var = "defaultFolder" items="${defaultFolderBeans}">
		setFolderName('${defaultFolder.fullName}', '${defaultFolder.name}', target);
	</c:forEach>

	<c:forEach var = "userFolder" items="${userFolderBeans}">
		setFolderName('${userFolder.fullName}', '${userFolder.name}', target);
	</c:forEach>
	
	jQuery("input:radio[name=mbox]:first").attr("checked",true);
    checkBox();
    document.extMailForm.inputBoxName.value="";
    jQuery("#parentFolder_new").text(('::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> :::::'));
}

function modifyCancel(msg) {
	var f = document.extMailForm;
	
	jQuery("#pop3Id").val("");
	jQuery("#pop3Id").attr("disabled", true);
	jQuery("#pop3Host").val("");
	jQuery("#pop3Host").attr("disabled", true);
	jQuery("#pop3Port").val("");
	jQuery("#pop3Port").attr("disabled", true);
	jQuery("#pop3Pw").val("");
	jQuery("#pop3Pw").attr("disabled", true);
	jQuery("#sslCheck").attr("checked",false);
	jQuery("#sslCheck").attr("disabled", true);

	jQuery("#pop3ListSelect").selectboxEnable();
	
	f.pop3del.checked = false;

	jQuery("#modifyMessageTr").hide();

	f.mbox[0].checked = true;
	checkBox();
		
	jQuery("#saveButton").css("display","");
	jQuery("#modifyButton").css("display","none");

	f.policy.value = "";

	jQuery("#parentFolder_default").text(msg);
	jQuery("#pop3ListSelect").selectboxSetValue("");
}

function folderList(target) {
	var listItem = "";
	if (target != 'new') { 
		<c:forEach var = "defaultFolder" items="${defaultFolderBeans}">
			listItem += setFolderList("${defaultFolder.fullName}","${defaultFolder.name}", target);
		</c:forEach>
	}
	<c:forEach var = "userFolder" items="${userFolderBeans}">
		listItem += setFolderList("${userFolder.fullName}",escape_tag("${userFolder.name}"), target);
	</c:forEach>

	document.write(listItem);
}

function setPOP3Server(){
	var pop3value = jQuery("#pop3List").val();
	jQuery("form input").attr("disabled",pop3value == "");
	if(pop3value.indexOf("|")>-1){
		pop3value=pop3value.replace("|ssl","");
		jQuery("#pop3Port").val("995");
		jQuery("#sslCheck").attr("checked",true);
	}else{
		jQuery("#pop3Port").val("110");
		jQuery("#sslCheck").attr("checked",false);
	}
	
	jQuery("#pop3Host").val(pop3value=="userInput"?"":pop3value);
	
}

function checkBox(){
	var f = document.extMailForm;
	if(f.mbox[1].checked){
		jQuery("#newPfBoxWrapper").show();
		f.inputBoxName.disabled = false;
		f.inputBoxName.focus();
	}else {
		jQuery("#newPfBoxWrapper").hide();
        f.inputBoxName.disabled = true;
	}
}

function checkPop3Delete(obj){
	if(obj.checked){
		alert(settingMsg.conf_pop_28);
	}
}
function checkSsl(){
	if(jQuery("#sslCheck:checked").val()=="on"){
		jQuery("#pop3Port").val("995");
	}else{
		jQuery("#pop3Port").val("110");
	}
}
onloadRedy("init()");
</script>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="extMailForm">
<input type="hidden" name="policy" id="policy">
<input type="hidden" name="rule">
<input type="hidden" name="parentFolder" id="parentFolder">
<input type="hidden" id="boxName" name="boxName"/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.pop" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.pop.41" bundle="setting"/></li>
					<li><tctl:msg key="conf.pop.8" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.pop.9" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><span><tctl:msg key="conf.pop.10" bundle="setting"/></span></th>
							<td>
								<div style="float: left"><input type="text" id="pop3Host" name="pop3Host" class="IP300"  disabled="disabled"></div>
								<div style="float: left;margin-left:5px;"><div id="pop3ListSelect"></div></div>
		                 		<div class="cls"></div>
							</td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.pop.42" bundle="setting"/></span></th>
							<td>
								<input type="text" id="pop3Port" name="pop3Port" class="IP300" disabled="disabled">
								<!-- ssl사용하기-->
								<input type="checkbox" id="sslCheck" name="sslCheck" onclick="checkSsl();"  disabled="disabled" /> <tctl:msg key="conf.pop.table.header.ssl" bundle="setting"/>
							</td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.pop.21" bundle="setting"/></span></th>
							<td>
								<input type="text" id="pop3Id" name="pop3Id" class="IP300"  disabled="disabled">
							</td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.pop.22" bundle="setting"/></span></th>
							<td>
								<input type="password" id="pop3Pw" name="pop3Pw" class="IP300"  disabled="disabled">
							</td>
						</tr>
						<tr id="modifyMessageTr" style="display:none">
							<td colspan="2" style="padding:5px"><font color="blue">* <tctl:msg key="conf.pop.50" bundle="setting"/></font></td>
						</tr>
						<tr>
							<th><span><tctl:msg key="conf.pop.24" bundle="setting"/></span></th>
							<td>
								<input type="checkbox" id="pop3del" name="pop3del" onClick="checkPop3Delete(this)"> <tctl:msg key="conf.pop.26" bundle="setting"/>
							</td>
						</tr>
						<tr>
							<th rowspan="2"><span><tctl:msg key="conf.pop.30" bundle="setting"/></span></th>
							<td>
								<table class="TB_cols2_content">
									<tr>
										<td width="130px">
											<label>
											<input type="radio" name="mbox" id="mbox1" value="off" class="TM_ST_NONE" onclick="checkBox()" checked><tctl:msg key="pop.mailbox.already" bundle="setting"/>
											</label>
										</td>
										<td>
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
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table class="TB_cols2_content">
									<tr>
										<td width="130px" rowspan="2">
											<label>
											<input type="radio" name="mbox" id="mbox1" value="on" class="TM_ST_NONE" onclick="checkBox()"><tctl:msg key="conf.filter.31" bundle="setting"/>
											</label>
										</td>
										<td>
											<div id="newPfBoxWrapper" style="display:none">
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
											</div>
											<input type="text" maxlength="64" name="inputBoxName" class="IP100px" style="width:177px" disabled>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<div class="btnArea" style="margin-bottom:10px">
						<a id="saveButton" class="btn_style2" href="#" onclick="saveExtMail('save')"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a id="modifyButton" class="btn_style2" href="#" onclick="saveExtMail('modify')" style="display:none"><span><tctl:msg key="common.modify" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="modifyCancel('::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::')"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
					</div>	
					
					<div class="title_bar"><span><tctl:msg key="conf.pop.29" bundle="setting"/></span></div>
					<div class="sub_toolbar">
						<a href="#" onclick="modifyExtMail()" class="btn_style4"><span><tctl:msg key="common.24" bundle="setting"/></span></a>
						<a href="#" onclick="deleteExtMail()" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a>
					</div>
					<table id="settingListTable" cellpadding="0" cellspacing="0">
						<colgroup span="8">
							<col width="30px"></col>
							<col></col>
							<col width="80px"></col>
							<col width="120px"></col>
							<col></col>
							<col width="120px"></col>
							<col width="100px"></col>
							<col width="100px"></col>
						</colgroup>
						<tr>
							<th><input type="checkbox" onclick="checkAll(this,extMailForm.rules)"/></th>
							<th><tctl:msg key="conf.pop.31" bundle="setting"/></th>
							<th><tctl:msg key="conf.pop.39" bundle="setting"/></th>
							<th><tctl:msg key="conf.pop.table.header.ssl" bundle="setting"/></th>
							<th><tctl:msg key="conf.pop.32" bundle="setting"/></th>
							<th><tctl:msg key="conf.pop.30" bundle="setting"/></th>
							<th><tctl:msg key="conf.pop.24" bundle="setting"/></th>
							<th><tctl:msg key="common.24" bundle="setting"/> | <tctl:msg key="common.11" bundle="setting"/></th>
						</tr>
						<c:if test="${empty pop3List}">
						<tr>
							<td colspan="8" align="center"><tctl:msg key="conf.pop.list.empty" bundle="setting"/></td>
						</tr>
						</c:if>
						<c:forEach var="pop" items="${pop3List}">
						<c:set var="tmpTarget" value="${fn:escapeXml(pop.pop3Boxname)}"/>
						<c:set var="index" value="${fn:indexOf(tmpTarget, ' ')}"/>
						<c:set var="target" value="${fn:substring(tmpTarget,index+1,-1)}"/>
						<tr>
							<td align="center">
								<input type="checkbox" name="rules" value="${fn:escapeXml(pop.pop3Host)}|${fn:escapeXml(pop.pop3Id)}|${fn:escapeXml(pop.pop3Port)}|${fn:escapeXml(pop.pop3Del)}|${fn:escapeXml(target)}|${fn:escapeXml(pop.usedSsl)}">
							</td>
							<td>
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${fn:escapeXml(pop.pop3Host)}">
											<div class='TM_HiddenTextDivCenter'>${fn:escapeXml(pop.pop3Host)}</div>
										</td>
									</tr>
								</table>
							</td>
							<td>${fn:escapeXml(pop.pop3Port)}</td>
							<td>
								<c:if test="${pop.usedSsl eq '1'}">
									<tctl:msg key="conf.pop.ssl.use" bundle="setting"/>
								</c:if>
								<c:if test="${pop.usedSsl ne '1'}">
								<tctl:msg key="conf.pop.ssl.none.use" bundle="setting"/>
								</c:if>
							</td>	
							<td>
                            <table class='TM_HiddenTextTable'>
                                    <tr>
                                        <td style="border:0;" title="${fn:escapeXml(pop.pop3Id)}">
                                            <div class='TM_HiddenTextDivCenter'>${fn:escapeXml(pop.pop3Id)}</div>
                                        </td>
                                    </tr>
                                </table>
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
								<c:if test="${pop.pop3Del == '1'}">O</c:if>
								<c:if test="${pop.pop3Del != '1'}">X</c:if>
							</td>
							<td>
								<a href="#" onclick = "modifyForm('${fn:escapeXml(pop.pop3Host)}', '${fn:escapeXml(pop.pop3Id)}', '${fn:escapeXml(pop.pop3Port)}', '${fn:escapeXml(pop.pop3Del)}', '${fn:escapeXml(target)}','${fn:escapeXml(pop.usedSsl)}')"><tctl:msg key="common.24" bundle="setting"/></a> | 
						  		<a href="#" onclick="deleteExtMail1('${fn:escapeXml(pop.pop3Host)}','${fn:escapeXml(pop.pop3Id)}')"><tctl:msg key="common.11" bundle="setting"/></a>
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