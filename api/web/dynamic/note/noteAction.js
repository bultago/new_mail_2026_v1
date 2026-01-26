var NoteControl = Class.create({
	initialize: function(opt){
		this.opt = opt;
		this.listParam = {};
	},
	getListParam : function() {
		return this.listParam;
	},
	loadNoteList : function(param) {
		this.listParam = param;
		ActionLoader.postLoadAction(this.opt.listAction,param,printNoteList,"json");
		jQuery("#"+this.opt.mainLID).loadWorkMaskOnly(true);
	},
	reloadNoteList : function() {
		var param = this.listParam;
		this.loadNoteList(param);
	},
	movePage : function(page) {
		var param = this.listParam;
		param.page = page;
		this.loadNoteList(param);
	},
	loadSetting : function(param) {
		ActionBasicLoader.loadAction(this.opt.mainLID, this.opt.settingAction, param);
		jQuery("#"+this.opt.mainLID).loadWorkMaskOnly(true);
	},
	readNote : function(param) {
		var listParam = this.listParam;
		if (listParam.keyWord) {
			param.keyWord = listParam.keyWord;
		}
		ActionLoader.postLoadAction(this.opt.readAction,param,readNoteResult,"json");
	},
	sendNote : function(param) {
		ActionLoader.postLoadAction(this.opt.sendAction,param,sendNoteResult,"json");
	},
	noteMdnInfo : function(param) {
		ActionLoader.postLoadAction(this.opt.mdnInfoAction,param,makeMdnInfo,"json");
	},
	noteRecallMdn : function(param) {
		ActionLoader.postLoadAction(this.opt.recallMdnAction,param,recallMdnResult,"json");
	},
	moveSave : function(param) {
		ActionLoader.postLoadAction(this.opt.moveSaveAction,param,noteMoveResult,"json");
	},
	deleteNote : function(param) {
		ActionLoader.postLoadAction(this.opt.deleteAction,param,noteDeleteResult,"json");
	},
	rejectNote : function(param) {
		ActionLoader.postLoadAction(this.opt.rejectAction,param,noteRejectResult,"json");
	},
	allSelectNote : function(type) {
		var param = this.listParam;
		param.type = type;
		ActionLoader.postLoadAction(this.opt.allSelectAction,param,allSelectResult,"json");
	}
});

function reloadNoteList(){	
	noteControl.reloadNoteList();	
}

function goSetting() {
	jQuery("#siSearchBox").hide();
	jQuery("#noteMenubar").hide();
	var param = {};
	noteControl.loadSetting(param);
	jQuery(window).trigger("resize");
}

function goFolder(folderName, flag) {
	contentSplitter.setSplitter("n");
	var param = {};
	param.folderName = folderName;
	
	if(flag){
		param.flag = flag;
	}

	param.page = 1;
	noteControl.loadNoteList(param);
	
	resetSearch();
}

function unseenNote() {
	goFolder('Inbox', 'U');
}

function writeNote() {
	window.open("/dynamic/note/noteWrite.do","popupWriteNote","scrollbars=no,width=450,height=480");
}

function writeToNote(addr) {
	var f = document.noteForm;
	f.to.value = addr;
	
	window.open("about:blank","popupWriteNote","scrollbars=no,width=450,height=480");
	f.method = "post";
	f.action="/dynamic/note/noteWrite.do";
	f.target = "popupWriteNote";
	f.submit();
}

function readNote(folderName, uid) {
	if (jQuery("#"+folderName+"_"+uid).hasClass("TM_unseenLow")) {
		jQuery("#"+folderName+"_"+uid).removeClass("TM_unseenLow");
		jQuery("#"+folderName+"_"+uid).addClass("TM_mailLow");
		jQuery("#"+folderName+"_"+uid+"_readF").removeClass("noteFlagUNSE");
		jQuery("#"+folderName+"_"+uid+"_readF").addClass("noteFlagSE");
	}
	
	var f = document.noteForm;
	f.folderName.value = folderName;
	f.uid.value = uid;
	
	var listParam = noteControl.getListParam();
	if (listParam.keyWord) {
		f.keyWord.value = listParam.keyWord;
	}
	
	window.open("about:blank","popupReadNote","scrollbars=no,width=450,height=480");
	f.method = "post";
	f.action="/dynamic/note/noteRead.do";
	f.target = "popupReadNote";
	f.submit();
}

function readNoteResult(data) {
	if (data.isSuccess) {
		var messageData = data.msgContent;
		var toList = messageData.toList;
		if (toList && toList.length > 0) {
			var paddr = "";
			var personal = toList[0].personal;
			var addr = toList[0].address;
			if(personal != ""){
				paddr = "&quot;"+personal+"&quot; &lt;"+addr+"&gt;";
			} else {
				paddr = "&lt;"+addr+"&gt;";
			}
		}
		jQuery("#read_from").html(messageData.from);
		jQuery("#read_date").html(messageData.date);
		jQuery("#read_to").html(paddr);
		jQuery("#note_read_content").html(data.htmlContent);
		jQuery("#max_save_count").text(msgArgsReplace(comMsg.note_msg_041,[data.maxSaveCount]));
		jQuery("#max_save_date").text(msgArgsReplace(comMsg.note_msg_040,[data.maxSaveDate]));
		jQuery("#note_read_uid").val(messageData.uid);
		
		if (jQuery("#mdn_Sent_"+messageData.uid).attr("mdn") == "on") {
			jQuery("#read_note_recall").show();
		}
		
		if (data.preUid > 0) {
			jQuery("#pre_mail").html("<a href='javascript:;' onclick='readNote(\""+data.folderName+"\",\""+data.preUid+"\")'><img class='navi_img' src='/design/common/image/ic_paging_pre.gif'>"+comMsg.comn_prelist+"</a>");
		} else {
			jQuery("#pre_mail").html("");
		}
		
		if (data.preUid > 0 || data.nextUid > 0) {
			jQuery("#bar_mail").html("|");
		} else {
			jQuery("#bar_mail").html("");
		}
		
		if (data.nextUid > 0) {
			jQuery("#next_mail").html("<a href='javascript:;' onclick='readNote(\""+data.folderName+"\",\""+data.nextUid+"\")'>"+comMsg.comn_nextlist+"<img class='navi_img' src='/design/common/image/ic_paging_next.gif'></a>");
		} else {
			jQuery("#next_mail").html("");
		}

		if (jQuery("#noteReadDialog_p").css("display") != 'block') {
			openReadNotePopup();
		}
		
		if (currentFolderName == 'Inbox') {
			updateUnreadNote();
		}
		updateWorkReadCount(data.folderAliasName,data.messageCount,data.unseenCount);
	}
}

function checkPopupUnreadNote(folderAliasName, messageCount, unseenCount) {
	if (currentFolderName == 'Inbox') {
		updateUnreadNote();
	}
	updateWorkReadCount(folderAliasName,messageCount,unseenCount);
}

function openReadNotePopup() {
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 500,
			minWidth: 420,
			openFunc:function(){
				checkReadMenu();
			},
			closeFunc:function(){
				resetReadForm();
			}
		};
	jQuery("#noteReadDialog").jQpopup("open",popupOpt);
}

function sendNote() {
	var param = {};
	var to = trim(jQuery("#to").val());
	var content = trim(jQuery("#note_content").val());
	var toArray = to.split(/[,;]/g);
	var sendOk = true;
	var toArrayLength = toArray.length;
	var sendArray = new Array();
	
	for (var i=0; i<toArrayLength; i++) {
		if (trim(toArray[i]) != "") {
			sendArray[sendArray.length] = trim(toArray[i]);
		}
	}
	
	var sendArrayLength = sendArray.length;
	if (sendArrayLength > 10) {
		alert(msgArgsReplace(comMsg.note_msg_062,[10]));
		return;
	} else if (sendArrayLength == 0){
		alert(comMsg.note_msg_061);
		return;
	}
	
	var sendTo = "";
	for (var i=0; i<sendArrayLength; i++) {
		sendTo += sendArray[i] + ",";
	}
	
	if (content == "") {
		alert(comMsg.note_msg_064);
		jQuery("#note_content").focus();
		return;
	}
	
	param.to = sendTo;
	param.content = content;
	param.saveSentCheck = (jQuery("#saveSentCheck").attr("checked")) ? "on" : "off";
	noteControl.sendNote(param);
}

function printNoteList(data) {
	$(noteOption.mainLID).innerHtml = "";
	noteListControl.makeList(noteOption.mainLID, data);
	makeToolbarMenu(data);
	updateUnreadNote();
	
}

function sendNoteResult(data) {
	if (data.isSuccess) {
		if (data.msg != "") {
			alert(data.msg);
		}
	} 
	else {
		if (data.msg != "") {
			alert(data.msg);
		} else {
			alert(comMsg.note_msg_035);
		}
	}
	
	this.close();
}

function noteRejectResult(data) {
	if (data.isSuccess) {
		var msg = comMsg.note_msg_050;
		if (data.msg != "") {
			msg += "\n\n";
			msg += comMsg.note_msg_051;
			msg += "\n";
			msg += data.msg;
		}
		alert(msg);
	}
}

function checkMdnInfo(messageList) {
	if (messageList.length > 0) {
		var uids = new Array();
		for (var i=0; i<messageList.length; i++) {
			uids[i] = messageList[i].id;
		}
		var param = {};
		param.uids = uids;
		
		noteControl.noteMdnInfo(param);
	}
}

function makeMdnInfo(data) {
	if (data.isSuccess) {
		var mdnList = data.mdnList;
		var messageId;
		var uid;
		var code;
		var content = "";
		for (var i=0; i<mdnList.length; i++) {
			messageId = mdnList[i].messageId;
			uid = mdnList[i].uid;
			code = mdnList[i].code;

			if (code == "300") {
				content = mailMsg.mdn_msg_0 + " <a href='javascript:;' onclick='recallMsg(\""+messageId+"\",\""+uid+"\")'>"+ mailMsg.mail_mdn_recall+"</a>";
				jQuery("#mdn_Sent_"+uid).attr("mdn","on");
				jQuery("#mdn_Sent_"+uid).attr("msgId", messageId);
			} 
			else if (code == "1") {
				content = mailMsg.mail_mdn_recall;
			}
			else {
				content = mdnList[i].date;
			}
			jQuery("#mdn_Sent_"+uid).html(content);
			
		}
	} else {
		jQuery(".ReceiveClass").html(" ");
	}
}

function recallMsg(messageId, uid) {
	
	if (!confirm(comMsg.note_msg_036)) {
		return;
	}
	
	var email = jQuery("#chk_Sent_"+uid).attr("temail");
	var param = {};
	param.messageId = messageId;
	param.email = email;
	
	noteControl.noteRecallMdn(param);
}

function movePage(page) {
	noteControl.movePage(page);
}

function recallMdnResult(data) {
	if (data.isSuccess) {
		alert(comMsg.note_msg_037);
	} 
	else {
		alert(comMsg.note_msg_038);
	}
	if(opener && opener.reloadNoteList){
		opener.reloadNoteList();
		this.close();
	}
	else {
		reloadNoteList();
	}
}

function writeNoteToMail() {
	var to = jQuery("#to").val();
	var content = jQuery("#note_content").val();
	
	var f = document.mailForm;
	f.to.value = to;
	f.content.value = content;
	
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";
	f.target = "popupWrite";
	f.submit();
	
	this.close();
}

function noteMoveResult(data) {
	if (data.isSuccess) {
		alert(comMsg.note_msg_042);
	} else {
		alert(data.msg);
	}
	if(opener && opener.reloadNoteList){
		opener.reloadNoteList();
		this.close();
	}
	else {
		reloadNoteList();
	}
}

function noteDeleteResult() {
	if(opener && opener.reloadNoteList){
		opener.reloadNoteList();
		this.close();
	}
	else {
		reloadNoteList();
	}
}

function allSelectProcess(type) {
	noteControl.allSelectNote(type);
}

function allSelectResult(data) {
	if (!data.isSuccess) {
		if (data.msg != "") {
			alert(data.msg);
		}
	}
	reloadNoteList();
}