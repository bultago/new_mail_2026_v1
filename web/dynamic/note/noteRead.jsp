<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/note_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail_write_style.css">

<script type="text/javascript" src="noteOption.js"></script>
<script type="text/javascript" src="noteCommon.js"></script>
<script type="text/javascript" src="noteAction.js"></script>

<script type="text/javascript">
function init() {
	noteControl = new NoteControl(noteOption);

	var mdnCode = jQuery("#note_code").val();
	checkReadMenu("${folderName}", mdnCode);
	
	var paddr = "";
	<c:forEach var="iaddr" items="${message.to}" varStatus="idx">					
		<c:if test="${not empty iaddr.personal}">
			paddr = "&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;";
		</c:if>
		<c:if test="${empty iaddr.personal}">
			paddr = "${iaddr.address}";
		</c:if>
	</c:forEach>
	jQuery("#read_to").html(paddr);

	setTitleBar(comMsg.note_msg_039);

	if (opener && opener.checkPopupUnreadNote) {
		opener.checkPopupUnreadNote("${folderAliasName}","${messageCount}","${unseenCount}");
	}
}

function readPreNextNote(folderName, uid) {
	var f = document.noteForm;
	f.folderName.value = folderName;
	f.uid.value = uid;

	f.method = "post";
	f.action="/dynamic/note/noteRead.do";
	f.submit();
}

function readNoteReply() {
	writeToNote('${message.fromString}');
}

function readNoteSave() {
	var uid = jQuery("#note_uid").val();
	var uids = [];
	uids[0] = uid;
	var param = {};
	param.uids = uids;
	param.folderName = "${folderName}";
	noteControl.moveSave(param);
}

function readNoteDelete() {
	if (!confirm(comMsg.note_msg_045)) {
		return;
	}
	var uid = jQuery("#note_uid").val();
	var uids = [];
	uids[0] = uid;
	var param = {};
	param.uids = uids;
	param.folderName = "${folderName}";
	noteControl.deleteNote(param);
}

function readNoteReject() {
	
	var isMyselfCheck = false;
	var email;
	var femails = [];
	
	email = get_email(jQuery("#note_escape_from").val());
	if(email == USEREMAIL){
		isMyselfCheck = true;		
	} else {
		femails[0] = email;
	}

	if(isMyselfCheck){
		alert(comMsg.note_msg_049);
		return;
	}
	
	var param = {};
	param.emails = femails;
	
	noteControl.rejectNote(param);
}

function readNoteRecall() {
	var msgId = jQuery("#note_msgid").val();
	
	if (!confirm(comMsg.note_msg_036)) {
		return;
	}
	
	var email = jQuery("#note_escape_to").val();
	var param = {};
	param.messageId = msgId;
	param.email = email;
	
	noteControl.noteRecallMdn(param);
}
</script>
</head>
<body style="background:#FFFFFF">
	<form name="noteReadForm" id="noteReadForm">
		<input type="hidden" id="note_uid" name="noteUid" value="${message.uid}"/>
		<input type="hidden" id="note_code" name="noteCode" value="${mdnCode}"/>
		<input type="hidden" id="note_msgid" name="noteMsgId" value="${messageId}"/>
		<input type="hidden" id="note_escape_from" name="noteEscapeFrom" value="${escapeFrom}"/>
		<input type="hidden" id="note_escape_to" name="noteEscapeTo" value="${escapeTo}"/>
		<div>
			<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="table-layout:fixed;">
				<col width="290px"></col>
				<col width="130px"></col>
				<tr>
					<td style="padding:5px 5px 5px 10px;">
						<a id="read_note_reply" href="javascript:;" onclick="readNoteReply()" class="readNoteReply" style="display:none"><tctl:msg key="menu.reply"/></a> 
						<a id="read_note_recall" href="javascript:;" onclick="readNoteRecall()" class="readNoteRecall" style="display:none"><tctl:msg key="mail.mdn.recall"/></a>
						<a id="read_note_save" href="javascript:;" onclick="readNoteSave()" class="readNoteSave" style="display:none"><tctl:msg key="note.msg.021" bundle="common"/></a>
						<a id="read_note_delete" href="javascript:;" onclick="readNoteDelete()" class="readNoteDelete" style="display:none"><tctl:msg key="menu.remove"/></a>
					</td>
					<td style="text-align:right;padding:5px 10px 5px 5px;">
						<a id="read_note_reject" href="javascript:;" onclick="readNoteReject()" class="readNoteReject" style="display:none"><tctl:msg key="mail.receivreject"/></a>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding:5px 10px;"><tctl:msg key="mail.from"/> : ${fn:escapeXml(message.fromString)}</td>
				</tr>
				<tr>
					<td colspan="2" style="padding:5px 10px;"><tctl:msg key="mail.receivedate"/> : ${fn:escapeXml(message.sentDateForRead)}</td>
				</tr>
				<tr>
					<td colspan="2" style="padding:5px 10px;"><tctl:msg key="mail.to"/> : <span id="read_to"></span></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center;padding:10px;">
						<table cellpadding="0" cellspacing="0" style="width:100%;border:0;table-layout:fixed;">
							<tr>
								<td style="height:200px;border:0px;"><div id="note_read_content" style="width: 100%;height: 100%;overflow: auto;">$${fn:escapeXml(htmlContent)}</div></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="notePreNext" style="padding:5px 10px;">
						<c:if test="${preUid > 0}"><a href='#' onclick='readPreNextNote("${folderName}","${preUid}")'><img class='navi_img' src='/design/common/image/ic_paging_pre.gif'><tctl:msg key="comn.prelist" bundle="common"/></a></c:if>
						<c:if test="${preUid > 0 || nextUid > 0}"> | </c:if>
						<c:if test="${nextUid > 0}"><a href='#' onclick='readPreNextNote("${folderName}","${nextUid}")'><tctl:msg key="comn.nextlist" bundle="common"/><img class='navi_img' src='/design/common/image/ic_paging_next.gif'></a></c:if>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding:10px;border-bottom:0px;">
						<div class="noteDescWrap">
							<p style="padding-bottom:5px;line-height:150%;font-size:11px;">
								<img align="absmiddle" src="/design/common/image/ic_import.gif">
								<tctl:msg key="note.msg.040" bundle="common" arg0="${maxSaveDate}"/>
							</p>
							<p style="line-height:150%;font-size:11px;">
								<img align="absmiddle" src="/design/common/image/ic_import.gif">
								<tctl:msg key="note.msg.041" bundle="common" arg0="${maxSaveCount}"/>
							</p>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<form name="noteForm" onsubmit="false">
		<input type="hidden" name="to"/>
		<input type="hidden" name="keyWord" value="${keyWord}"/>
		<input type="hidden" name="folderName"/>
		<input type="hidden" name="uid"/>
		<input type="hidden" name="flag" value="${flag}"/>
	</form>
</body>

<script type="text/javascript">init();</script>
</html>