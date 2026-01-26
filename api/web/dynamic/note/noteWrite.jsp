<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/note_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail_write_style.css">

<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>

<script type="text/javascript" src="noteOption.js"></script>
<script type="text/javascript" src="noteCommon.js"></script>
<script type="text/javascript" src="noteAction.js"></script>

<script type="text/javascript">
function init() {
	noteControl = new NoteControl(noteOption);
	jQuery("#to").autocomplete("/dynamic/note/searchUserList.do");
	setTimeout(function(){		
		new emulEvent('to');
	},1000);
	jQuery("#to").focus();
	setTitleBar(comMsg.note_msg_005);
}

function scrollControl() {
	var toObj = document.getElementById("to");
	toObj.scrollTop = toObj.scrollHeight;
}
</script>
</head>
<body onload="init()" style="background:#FFFFFF">
	<form name="noteWriteForm" id="noteWriteForm">	
	<div>
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="table-layout:fixed;">
			<col width="120px;"></col>
			<col width="300px;"></col>
			<tr>
				<td style="padding:5px 5px 5px 10px;width:120px;">
					<a href="javascript:;" class="writeNoteSend" onclick="sendNote()"><tctl:msg key="menu.send"/></a>
				</td>
				<td style="text-align:right;padding:5px 10px 5px 5px;">
					<a href="javascript:;" class="writeNoteMail" onclick="writeNoteToMail()"><tctl:msg key="note.msg.026" bundle="common"/></a>
				</td>
			</tr>
			<tr>
				<td style="padding:5px 5px 5px 10px;">
					<tctl:msg key="mail.to"/>
					<input type="checkbox" id="myselfCheck" name="myselfCheck" onclick="insertMySelf(this)"/> 
					<tctl:msg key="mail.myself"/>
				</td>
				<td style="padding:5px 10px 5px 5px;">
					<textarea id="to" name="to" onfocus="" onpropertychange="scrollControl()" class="TM_rcpt_input" style="height:30px;overflow:auto">${fn:escapeXml(to)}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding:5px 10px;">
					<input type="checkbox" id="saveSentCheck" name="saveSentCheck" checked="checked"/> 
					<tctl:msg key="note.msg.027" bundle="common"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;padding:10px;">
					<textarea id="note_content" class="noteContent" onkeyup="checkMaxLength()"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="noteWordCountTd" style="padding:5px 10px;">
					<span id="word_count">0</span><span> / 400 <tctl:msg key="note.msg.031" bundle="common"/></span>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding:10px;border-bottom:0px;">
					<div class="noteDescWrap">
						<p style="padding-bottom:5px;line-height:150%;font-size:11px;">
							<img align="absmiddle" src="/design/common/image/ic_import.gif">
							<tctl:msg key="note.msg.028" bundle="common" arg0="10"/>
						</p>
						<p style="line-height:150%;font-size:11px;">
							<img align="absmiddle" src="/design/common/image/ic_import.gif">
							<tctl:msg key="note.msg.029" bundle="common"/>
						</p>
					</div>
				</td>
			</tr>
		</table>
	</div>
	</form>
	<form name="mailForm" onsubmit="false">
		<input type="hidden" name="wmode" value="popup">
		<input type="hidden" name="wtype" value="send">
		<input type="hidden" name="to">
		<input type="hidden" name="content">
	</form>
</body>
</html>