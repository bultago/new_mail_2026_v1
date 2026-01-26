<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<form name="noteSettingForm">
<div class="explanation" style="margin:0px;">
	<ul>
		<li><tctl:msg key="note.msg.007" bundle="common"/></li>
	</ul>
</div>
<div id="main_wrapper">
	<div>
	<div class="title_bar"><span><tctl:msg key="note.msg.008" bundle="common"/></span></div>
	<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0" style="border:1px solid #E6E6E6">
		<tr>
			<th><span><tctl:msg key="note.msg.009" bundle="common"/></span></th>
			<td>								
				<div id="policyTypeSelect"></div>
			</td>
		</tr>
	</table>
	
	<div id="blackAndWhiteDiv">
	<div class="title_bar"><span><tctl:msg key="conf.spamrule.allow.block.control" bundle="setting"/></span></div>
	<table class="TB_cols2" cellpadding="0" cellspacing="0" border="0" style="border:1px solid #E6E6E6">
		<tr id="whiteListTr">
			<th><tctl:msg key="conf.spamrule.46" bundle="setting"/></th>
			<td>
				<table class="TB_cols2_content" cellspacing="0" cellpadding="0" border="0">
					<tr>
					<td class="TD_selectBox">
						<input type="text" id="white_address" name="white_address" class="IP100" onKeyPress="(keyEvent(event) == 13) ? addUser() : '';">
						<select multiple="multiple" class="selectBox" id="whiteList" name="whiteList">
							<c:if test="${noteResult.policyType == 'whiteOnly'}">
							<c:forEach var="noteCondResult" items="${noteCondResultList}">
							<option value="${fn:escapeXml(noteCondResult.condTarget)}">${fn:escapeXml(noteCondResult.mailUid)}(${fn:escapeXml(noteCondResult.userName)})</option>
							</c:forEach>
							</c:if>
						</select>
					</td>
					<td class="btnArea">
						<ul>
							<li>
								<a href="javascript:;" onclick="addUser()" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
								<a href="javascript:searchList(document.noteSettingForm.white_address, document.noteSettingForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
							</li>
							<li><a href="javascript:selectAll(document.noteSettingForm.whiteList,'w_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
							<li><a href="javascript:deleteList(document.noteSettingForm.whiteList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
						</ul>
					</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="blackListTr">
			<th><tctl:msg key="conf.spamrule.49" bundle="setting"/></th>
			<td>
				<table class="TB_cols2_content" cellspacing="0" cellpadding="0" border="0">
					<tr>
					<td>
						<input type="text" id="black_address" name="black_address" class="IP100" onKeyPress="(keyEvent(event) == 13) ? addUser() : '';">
						<select multiple="multiple" class="selectBox" id="blackList" name="blackList">
							<c:if test="${noteResult.policyType == 'blackOnly'}">
							<c:forEach var="noteCondResult" items="${noteCondResultList}">
							<option value="${fn:escapeXml(noteCondResult.condTarget)}">${fn:escapeXml(noteCondResult.mailUid)}(${fn:escapeXml(noteCondResult.userName)})</option>
							</c:forEach>
							</c:if>
						</select>											
					</td>
					<td class="btnArea">
						<ul>
							<li>
								<a href="javascript:;" onclick="addUser('black')" class="btn_style4"><span><tctl:msg key="common.9" bundle="setting"/></span></a>
								<a href="javascript:searchList(document.noteSettingForm.black_address, document.noteSettingForm.blackList)" class="btn_style4"><span><tctl:msg key="common.10" bundle="setting"/></span></a>
							</li>
							<li><a href="javascript:selectAll(document.noteSettingForm.blackList,'b_select')" class="btn_style4"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
							<li><a href="javascript:deleteList(document.noteSettingForm.blackList)" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>										
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
		<a class="btn_style2" href="#" onclick="saveNotePolicy()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
		<a class="btn_style3" href="#" onclick="resetNotePolicy()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
		</td></tr>
	</table>					
	</div>
</div>
</form>
<script type="text/javascript">
jQuery("#policyTypeSelect").selectbox({selectId:"policyType",selectFunc:checkPolicyType},
		"${noteResult.policyType}",
		[{index:comMsg.note_msg_013, value:"blackOnly"},{index:comMsg.note_msg_012, value:"whiteOnly"},
		 {index:comMsg.note_msg_010, value:"allowAll"},{index:comMsg.note_msg_011, value:"rejectAll"}
		]);

function checkPolicyType() {
	var policyType = jQuery("#policyType").val();
	if (policyType == "allowAll" || policyType == "rejectAll") {
		jQuery("#blackAndWhiteDiv").hide();
	} 
	else if (policyType == "whiteOnly") {
		jQuery("#blackListTr").hide();
		jQuery("#whiteListTr").show();
		jQuery("#blackAndWhiteDiv").show();
	} 
	else if (policyType == "blackOnly") {
		jQuery("#whiteListTr").hide();
		jQuery("#blackListTr").show();
		jQuery("#blackAndWhiteDiv").show();
	}
}

function setSettingTitle() {
	var title = '<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>';
		title += '<span class="TM_work_title_sub"> | <tctl:msg key="note.msg.014" bundle="common"/></span>';

	setWorkTitle(title);
}

function saveNotePolicy() {
	var param = {};
	var pType = jQuery("#policyType").val();
	param.policyType = pType;

	if (pType != "allowAll" && pType != "rejectAll") {
		var selectOption;
		if (pType == "whiteOnly") {
			selectOption = jQuery("#whiteList option");
		} 
		else if (pType == "blackOnly") {
			selectOption = jQuery("#blackList option");
		}

		if (selectOption.length > 0) {
			var userseqs = new Array();
			for ( var i=0; i < selectOption.length; i++) {
				userseqs[i] = jQuery(selectOption[i]).attr("value");
			}
			param.condTarget = userseqs;
		}
	}

	jQuery.post("/dynamic/note/noteSaveSetting.do", param, function(data) {
		if (data.isSuccess) {
			alert(comMsg.note_msg_015);
		} 
		else {
			alert(comMsg.note_msg_016)
		}
	},"json");
}

function resetNotePolicy() {
	goSetting();
}

function addUser() {
	var searchObj;
	if (jQuery("#policyType").val() == 'whiteOnly') {
		searchObj = jQuery("#white_address");
	} else {
		searchObj = jQuery("#black_address");
	}

	if(!checkInputLength("jQuery", searchObj, comMsg.register_008, 2, 64)) {
		return;
	}
	if(!checkInputValidate("jQuery", searchObj, "id")) {
		return;
	}

	var searchId = jQuery.trim(searchObj.val());

	if (USERID == searchId) {
		alert(comMsg.note_msg_046);
		return;
	}
	
	var param = {"searchId":searchId, "equal":"true"};
	
	jQuery.post("/common/searchMailUserJson.do", param, function(data) {
		var userList = data.userList;
		if (!userList || userList.length < 1) {
			alert(comMsg.register_015);
			return;
		} else {
			var select;		
			if (jQuery("#policyType").val() == 'whiteOnly') {
				select = jQuery("#whiteList");
				jQuery("#white_address").val("");
			} else {
				select = jQuery("#blackList");
				jQuery("#black_address").val("");
			}
			if(userList && userList.length > 0){

				if ((checkUserCount() + userList.length) > 1000) {
					alert(msgArgsReplace(comMsg.note_msg_056,[1000]));
					return;
				}
				
				for ( var i = 0; i < userList.length; i++) {
					if(!checkUserList(userList[i].mailUserSeq)){
						select.append(
								jQuery("<option value='"+userList[i].mailUserSeq+"'>"+
										userList[i].mailUid+"("+userList[i].userName+")</option>"));
					}
				}
			}
		}
	},"json");
}

function checkUserList(userSeq){
	var selectOption;
	if (jQuery("#policyType").val() == 'whiteOnly') {
		selectOption = jQuery("#whiteList option");
	} else {
		selectOption = jQuery("#blackList option");
	}
	
	var isExist = false;
	for ( var i = 0; i < selectOption.length; i++) {
		if(userSeq == jQuery(selectOption[i]).attr("value")){
			isExist = true;
			break;
		}
	}
	return isExist;
}

function checkUserCount(){
	var selectOption;
	if (jQuery("#policyType").val() == 'whiteOnly') {
		selectOption = jQuery("#whiteList option");
	} else {
		selectOption = jQuery("#blackList option");
	}
	return selectOption.length;
}

checkPolicyType();
setSettingTitle();
</script>