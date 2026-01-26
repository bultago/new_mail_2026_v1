var currentFolder = "none";
var currentFolderViewName = "none";
var currentFolderType;
var currentToolbarMode;
var registAddrList;
var chkMsgCnt = 0;
var chkMsgHash = new Hash();

function checkItem(){	
	if(chkMsgCnt == 0){
		alert(comMsg.comn_error_001);
		return false;
	}
	
	return true;
}


/*****Delete Function********/
function deleteMsg(){	
	if(!checkItem())return;
	
	var folderName = mailControl.getCurrentFolder();	
	if(folderName == "Trash" && !confirm(mailMsg.confirm_trashdelete)){
		return;
	}
	
	if(folderName == "Reserved" && !confirm(mailMsg.confirm_reserveddelete)){
		return;
	}

	if(!confirm(mailMsg.confirm_delete)){
		return;
	}

	var msginfo = getMessgesInfo();
	var allCheck = false;
	if($("allSearchSelectCheck")){		
		var allSCheck = $("allSearchSelectCheck").value;
		if(allSCheck == "on"){
			allCheck = true;
		}
	}
	
	if(allCheck){
		allSelectAction("delete","");
	}else {
		menuBar.disableToolBarItem("del");
		mailControl.deleteMessages(msginfo.uid,msginfo.folders);
	}
}

function cleanMsg(){
	if(!checkItem())return;
	var allCheck = false;
	if($("allSearchSelectCheck")){		
		var allSCheck = $("allSearchSelectCheck").value;
		if(allSCheck == "on"){
			allCheck = true;
		}
	}
	
	var msginfo = getMessgesInfo();	
	var mailCount = (allCheck)?pagingInfo.total:msginfo.uid.length;
	
	if(!confirm(msgArgsReplace(mailMsg.confirm_clean,[mailCount]))){
		return;
	}
	
	if(allCheck){
		allSelectAction("clean","");
	} else {				
		menuBar.disableToolBarItem("adel");
		mailControl.cleanMessages(msginfo.uid,msginfo.folders);
	}
}

/*****Reply Function********/

function replyWrite(){
	if(!checkItem())return;
	
	if(chkMsgCnt > 1){
		alert(mailMsg.error_replyone);
		return;
	}
	
	var workList = chkMsgHash.values();
	var msgInfo = getListProcessParams(workList);
	
	var params = {};
	params.folder = msgInfo.fnames[0];
	params.uids = msgInfo.uids;
	params.wtype = "reply";
	
	goWriteLoad(params);	
	
}

function replyWriteAll(){
	if(!checkItem())return;
	
	if(chkMsgCnt > 1){
		alert(mailMsg.error_replyone);
		return;
	}
	
	var workList = chkMsgHash.values();
	var msgInfo = getListProcessParams(workList);
	
	var params = {};
	params.folder = msgInfo.fnames[0];
	params.uids = msgInfo.uids;
	params.wtype = "replyall";
	
	goWriteLoad(params);
	
}


function reWrite(){
	if(!checkItem())return;
	
	if(chkMsgCnt > 1){
		alert(mailMsg.error_rewrite);
		return;
	}
	
	var workList = chkMsgHash.values();
	var msgInfo = getListProcessParams(workList);
	
	var params = {};
	params.folder = msgInfo.fnames[0];
	params.uids = msgInfo.uids;
	params.wtype = "rewrite";
	
	goWriteLoad(params);	
	
}

/*****Copy Move Function********/
function showFolderList(target){	
	if(!checkItem())return;
	var id = jQuery(target).attr("sid");
	var type;
	if(id.indexOf("copy") > -1){
		type = "copy";
	} else if(id.indexOf("move") > -1){
		type = "move";
	}
	
	var userFList = folderControl.getFolderList();
	var flist = jQuery("<div></div>").addClass("flist_drow")
	.folderListDrow(userFList,"/design/common/image/icon/ic_cfolder.gif",copyMoveMsg,type);
	
	showSubMenuLayer(id,flist);
}

function copyMoveMsg(target,type){	
	var toFolderName = jQuery(target).attr("fname");	
	var workList = chkMsgHash.values();	
	var params = getListProcessParams(workList);
	
	var allCheck = false;
	if($("allSearchSelectCheck")){		
		var allSCheck = $("allSearchSelectCheck").value;
		if(allSCheck == "on"){
			allCheck = true;
		}
	}
	if(allCheck){
		if(type == "copy"){
			allSelectAction("copy",toFolderName);
		} else if(type == "move"){
			allSelectAction("move",toFolderName);
		}		
	} else {	
		if(type == "copy"){
			mailControl.copyMessage(params.uids,params.fnames,toFolderName);
		} else if(type == "move"){
			mailControl.moveMessage(params.uids,params.fnames,toFolderName);
		}
	}
	clearSubMenu();
}

/*****Quick View Function********/
function showOptionList(target){
	
	var id = jQuery(target).attr("sid");
	var opt;
	if(currentFolderType == "normal"){
		if(RENDERMODE == "ajax" && CURRENT_PAGE_NAME!="MAILREADPOPUP"){
			opt = {size : "100",
					list : [{name:mailMsg.menu_upload,linkFunc:goOptionListFunc,param:"upload"},
					        {name:mailMsg.menu_option_save,linkFunc:goOptionListFunc,param:"save"},
					        {name:"",linkFunc:"",param:"split"},
					        {name:"<img src='/design/skin3/image/normal.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_normal,linkFunc:goOptionListFunc,param:"normal"},
					        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:goOptionListFunc,param:"right"},
					        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:goOptionListFunc,param:"down"}
					        ]};
		}else{
			opt = {size : "100",
					list : [{name:mailMsg.menu_option_save,linkFunc:goOptionListFunc,param:"save"}					        
					        ]};
		}
	}else{
		if(RENDERMODE == "ajax"  && CURRENT_PAGE_NAME!="MAILREADPOPUP"){
			opt = {size : "100", 
				list : [{name:mailMsg.menu_option_save,linkFunc:goOptionListFunc,param:"save"},
				        {name:"",linkFunc:"",param:"split"},
				        {name:"<img src='/design/skin3/image/normal.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_normal,linkFunc:goOptionListFunc,param:"normal"},
				        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:goOptionListFunc,param:"right"},
				        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:goOptionListFunc,param:"down"}			        
				]};
		}else{
			opt = {size : "100", 
					list : [{name:mailMsg.menu_option_save,linkFunc:goOptionListFunc,param:"save"}
					]};
		}
	}
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());
}
function showQuickList(target){
	var id = jQuery(target).attr("sid");
	var opt = {size : "110", 
			list : [{name:mailMsg.menu_quick_flag,linkFunc:goQuickList,param:"flaged"},
			        {name:mailMsg.menu_quick_unread,linkFunc:goQuickList,param:"unseen"},
			        {name:mailMsg.menu_quick_read,linkFunc:goQuickList,param:"seen"},
			        {name:mailMsg.menu_quick_myself,linkFunc:goQuickList,param:"myself"},
			        {name:mailMsg.menu_quick_reply,linkFunc:goQuickList,param:"reply"},
			        {name:mailMsg.menu_quick_attach,linkFunc:goQuickList,param:"attached"},
			        {name:mailMsg.menu_quick_today,linkFunc:goQuickList,param:"today"},
			        {name:mailMsg.menu_quick_yesterday,linkFunc:goQuickList,param:"yesterday"}
			]};
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());
}

function goQuickList(target){
	var type = jQuery(target).attr("param");
	viewQuickList(type,false);
	clearSubMenu();
}

function goOptionListFunc(target){
	
	var type = jQuery(target).attr("param");
	
	if(type == "upload"){
		uploadMsg();
	}else if(type == "save"){
		saveMsg();
	}else if(type == "normal"){
		contentSplitterChange('n');
	}else if(type == "right"){
		contentSplitterChange('v');
	}else if(type == "down"){
		contentSplitterChange('h');
	}
	
	clearSubMenu();
}

/*****Foward Function********/
function showForwardList(target){
	if(!checkItem())return;
	var id = jQuery(target).attr("sid");
	var opt = {size : "120", 
			list : [{name:mailMsg.menu_forward_parsed,linkFunc:forwardMessage,param:"parsed"},
			        {name:mailMsg.menu_forward_attach,linkFunc:forwardMessage,param:"attached"}]};
	
	if(chkMsgCnt > 1){
		opt.list = [{name:mailMsg.menu_forward_attach,linkFunc:forwardMessage,param:"attached"}];
	}
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());
}


function forwardMessage(target){
	var fwtype = jQuery(target).attr("param");
	
	if(!checkItem())return;	
	
	var workList = chkMsgHash.values();
	var msgInfo = getListProcessParams(workList);
	
	var params = {};
	params.folder = msgInfo.fnames[0];
	params.uids = msgInfo.uids;
	params.fwmode = fwtype;
	params.wtype = "forward";
	
	goWriteLoad(params);		
	
	clearSubMenu();
}


/*****Read Unread Function********/
function showSeenFlag(target){
	var id = jQuery(target).attr("sid");
	var opt = {size : "80", 
			list : [{name:mailMsg.menu_flag_read,linkFunc:changeSeenFlag,param:"seen"},
			        {name:mailMsg.menu_flag_unread,linkFunc:changeSeenFlag,param:"unseen"}]};
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());
}

function changeSeenFlag(target){	
	var type = jQuery(target).attr("param");
	
	if(!checkItem())return;	
	
	var isSeen = false;
	if(type == "seen"){
		isSeen = true;
	} else if(type == "unseen"){
		isSeen = false;
	}
	
	var msgInfo = getMessgesInfo();	
	switchFlaged(msgInfo, "S", isSeen);	
	if($("allSelectTr")){	
		toggleAllCheckMessage();
	}
	
	clearSubMenu();
}

/***** TagAdd Function********/
function showTagAdd(target){
	if(!checkItem())return;    
	
	var id = jQuery(target).attr("sid");
	var tagMap = tagControl.getTagMap();
	var tagSelect = jQuery("<div></div>").addClass("flist_drow")
						.tagListDrow(tagMap,addMessageTagging,removeMessageTagging);
	
	
	showSubMenuLayer(id,tagSelect);
}

function addMessageTagging(target){
	var tagId = jQuery(target).attr("tid");	
	var workList = chkMsgHash.values();	
	var params = getListProcessParams(workList);
	
	if($("allSelectTr")){	
		toggleAllCheckMessage();
	}
	
	tagControl.taggingMessage("true",params.uids,params.fnames,tagId);	
	
	clearSubMenu();
}

function removeMessageTagging(target){
	var tagId = jQuery(target).attr("tid");	
	var workList = chkMsgHash.values();	
	var params = getListProcessParams(workList);
	
	if($("allSelectTr")){	
		toggleAllCheckMessage();
	}
	
	tagControl.taggingMessage("false",params.uids,params.fnames,tagId);	
	
	clearSubMenu();
}


/*****MailSave Function********/
function saveMsg(){
	if(!checkItem())return;
	var workList = chkMsgHash.values();	
	var params = getListProcessParams(workList);	
	mailControl.downLoadMessages(params.uids,params.fnames);	
}

function printMsg(){
	if(!checkItem())return;	
	
	var popupReadForm = document.popupReadForm;
	var msgInfo = getMessgesInfo();
	var uids = msgInfo.uid;
	var folders = msgInfo.folders;
	if(chkMsgCnt > 1){
		alert(mailMsg.error_printone);
		return;
	}
	
	popupReadForm.uid.value = uids[0];
	popupReadForm.folder.value = folders[0];
	popupReadForm.readType.value = "print";
	
	var param = {};
	param = mailControl.getSharedFolderParam(param);	
	popupReadForm.sharedFlag.value = param.sharedFlag;
	popupReadForm.sharedUserSeq.value = param.sharedUserSeq;
	popupReadForm.sharedFolderName.value = param.sharedFolderName;	
	
	var popWin = window.open("about:blank","popupPrint","scrollbars=yes,width=700,height=640");
	popupReadForm.method = "post";
	popupReadForm.action="/dynamic/mail/readMessage.do";
	popupReadForm.target = "popupPrint";
	popupReadForm.submit();
	
}

/*****Mail upload Function********/
function uploadMsg(){
	msgUploadInit();
	jQuery("#m_messageUploadForm").makeModal("bodyModalMask");	
}


/*****View change Function********/
function changeToolbarMode(tabId){
	menuBar.makeToolbar(tabId);
	currentToolbarMode = tabId;
	if(skin != "skin3"){
		setMailMenuBarStatus();
	}
}

/*****Rule Function********/
var ruleAddType;
function spamRuleAdd(){	
	if(!checkItem())return;	
	
	var popOpt = clone(popupOpt);
	popOpt.minHeight = 180;
	popOpt.minWidth = 400;
	popOpt.top = 150;
	popOpt.openFunc = function() {
		jQuery("#reportSpamNcscTable").hide();
		jQuery("#reportSpamNcscLabel").text("");
		jQuery("#reportSpamNcsc").attr("checked", false);
		
		jQuery.post("/dynamic/mail/reportNcscInfo.do", null, function(data) {
			if (data.reportNcsc) {
				jQuery("#reportSpamNcscTable").show();
				jQuery("#reportSpamNcscLabel").text(msgArgsReplace(mailMsg.reportspam_ncsc_address,[data.reportNcscAddress]));
			}
		}, "json");
	};
	popOpt.closeFunc = closeRuleModal;
	popOpt.btnList = [{name:comMsg.comn_confirm,func:registRule}];	
	
	jQuery("#spamRuleRegist").jQpopup("open",popOpt);		
	ruleAddType = "black";
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#addspam").attr("checked", false);
		jQuery("#add_spam_area").hide();
	}
}

function whiteRuleAdd(){
	if(!checkItem())return;
	var popOpt = clone(popupOpt);
	popOpt.minHeight = 150;
	popOpt.minWidth = 400;
	popOpt.top = 150;
	popOpt.closeFunc = closeRuleModal;
	popOpt.btnList = [{name:comMsg.comn_confirm,func:registRule}];
	
	jQuery("#whiteRuleRegist").jQpopup("open",popOpt);	
	ruleAddType = "white";
}

function closeRuleModal(){
	if(ruleAddType == "black"){
		jQuery("#spamRuleRegist").jQpopup("close");
	} else if(ruleAddType == "white"){
		jQuery("#whiteRuleRegist").jQpopup("close");
	}	
	ruleAddType = "";	
}

var registRuleChecking = false;
function registRule(){
	if (registRuleChecking) {
		return;
	}
	var msgInfo = getMessgesInfo();
	var uids = msgInfo.uid;
	var checkIds = msgInfo.checkIds;
	var folderName = msgInfo.folders[0];
	var femails = [];
	var moveBox,addlist;
	
	var reportNcsc = "false";
	var isMyselfCheck = false;
	
	if(ruleAddType == "black"){
		addlist = ($("addspam").checked)?"true":"false";
		moveBox = ($("movetrash").checked)?"true":"false";
		reportNcsc = ($("reportSpamNcsc").checked)?"true":"false";
	} else {
		addlist = ($("addwhite").checked)?"true":"false";
		moveBox = ($("moveinbox").checked)?"true":"false";
	}
	
	var email;
	if(addlist == "true"){
		for ( var i = 0; i < checkIds.length; i++) {
			email = get_email(jQuery("#chk_"+checkIds[i]).attr("femail"));		
			if(email == USEREMAIL){
				isMyselfCheck = true;
				break;			
			} else {
				femails[femails.length] = email;
			}
		}
		
		if(isMyselfCheck){
			alert(settingMsg.conf_forward_14);
			return;
		}	
	}
	
	var param = {};
	param.rtype = ruleAddType;
	param.folderName = folderName;
	param.uids = uids;
	param.femails = femails;
	param.movebox = moveBox;
	param.addlist = addlist;
	param.reportNcsc = reportNcsc;
	param.adminRegist = "regist";
	
	registRuleChecking = true;
	closeRuleModal();
	jQuery("body").loadWorkMask(true, null, true);
	jQuery.post("/dynamic/mail/reportSWRule.do", param, registRuleResult, "json");	
}

function registRuleResult(data, textStatus){	
	if(textStatus == "success"){
		if(data.result == "success"){
			if(data.adminRegist == "error"){
				alert(mailMsg.bayesian_adminerror);
			} else {
				if(data.type == "black"){
					var msg = "";
					if (data.reportToNcsc) {
						if (data.reportToNcsc == "true") {
							msg += mailMsg.reportspam_ncsc_message_success + "\n\n";
						} else {
							msg += mailMsg.reportspam_ncsc_message_fail + "\n\n";
						}
					}
					msg += mailMsg.bayesian_reportspam;
					alert(msg);
				} else {
					alert(mailMsg.bayesian_reportwhite);
				}
			}
		} else {
			alert(mailMsg.report_error);
		}
	} else {
		alert(mailMsg.report_error);
	}
	registRuleChecking = false;
	jQuery("body").removeWorkMask();
	folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
	if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
		window.close();
	}
}


function registReject(){
	if(!checkItem())return;
	
	if(!confirm(mailMsg.confirm_denymsg)){
		return;
	}
	var rejectEmail = [];	
	var checkIds = getMessgesInfo().checkIds;
	
	var email;
	var isMyselfCheck = false;
	for ( var i = 0; i < checkIds.length; i++) {		
		email = get_email(jQuery("#chk_"+checkIds[i]).attr("femail"));		
		if(email == USEREMAIL){
			isMyselfCheck = true;
			break;			
		} else {
			rejectEmail[rejectEmail.length] = email;
		}		
	}	
	
	if(isMyselfCheck){
		alert(settingMsg.conf_forward_14);
		return;
	}	
	
	var param = {};
	param.rtype = "black";
	param.addlist = "true";
	param.femails = rejectEmail;
	param.adminRegist = "none";
	
	jQuery.post("/dynamic/mail/reportSWRule.do", param, registRejectAllowResult, "json");
	
}

function registAllow(){
	
	if(!checkItem())return;
	
	if(!confirm(mailMsg.confirm_allowmsg)){
		return;
	}
	var allowEmail = [];	
	var checkIds = getMessgesInfo().checkIds;	
	
	for ( var i = 0; i < checkIds.length; i++) {
		allowEmail[allowEmail.length] = get_email(jQuery("#chk_"+checkIds[i]).attr("femail"));
	}
	
	var param = {};
	param.rtype = "white";
	param.addlist = "true";
	param.femails = allowEmail;
	param.adminRegist = "none";
	
	jQuery.post("/dynamic/mail/reportSWRule.do", param, registRejectAllowResult, "json");
}

function registRejectAllowResult(data, textStatus){	
	if(textStatus == "success"){
		if(data.result == "success"){
			if(data.type == "black"){
				if(!confirm(mailMsg.confirm_denyok)){					
					return;
				}
			} else {				
				if(!confirm(mailMsg.confirm_allowok)){					
					return;
				}
			}
			moveRejectMsg(data.type);
		} else {			
			alert(mailMsg.error_report_rejectallow);
		}
	} else {		
		alert(mailMsg.error_report_rejectallow);
	}
	
	closeRuleModal();
}

function moveRejectMsg(type){	
	
	var moveEmail = [];
	var msgInfo = getMessgesInfo();
	var uids = msgInfo.uid;
	var folderName = msgInfo.folders[0];
	
	var param = {};
	param.rtype = type;
	param.movebox = "true";
	param.uids = uids;
	param.folderName = folderName;
	
	jQuery.post("/dynamic/mail/reportSWRule.do", param, moveRejectAllowReult, "json");
	
}

function moveRejectAllowReult(data, textStatus){	
	if(textStatus == "success"){
		if(data.result == "success"){
			folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
			if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
				window.close();
			}
		}
	}
}

function autoRuleAdd(){
	if(!checkItem())return;
	
	var checkIds = getMessgesInfo().checkIds;
	var subjects = getMessgesInfo().subjects;
	if(checkIds.length > 1){
		if(!confirm(mailMsg.list_filter03)){
			return;
		}
	}
	
	jQuery("#parentFolder_default").text(" ::::: "+settingMsg.conf_filter_savefolder+":::::");
	jQuery("#parentFolder_new").text(" ::::: "+settingMsg.conf_filter_parentfolder+":::::");
	
	
	var popOpt = clone(popupOpt);
	popOpt.minHeight = 220;
	popOpt.minWidth = 600;
	popOpt.top = 150;	
	popOpt.btnList = [{name:comMsg.comn_confirm,func:saveAutoRule}];
	popOpt.openFunc = function(){
		jQuery("#autoRuleForm").find("input[type='checkbox']").removeAttr("checked");
		jQuery("#boxName").attr("value","");
		toggleAddNewBox();
	};
	
	jQuery("#autoRuleRegist").jQpopup("open",popOpt);	
	
	makeFolderList("ruleSaveBox","normal");
	makeFolderList("ruleSaveNewBox","new");	
	
	var checkId = checkIds[0];	
	$("ruleSender").value = get_email(jQuery("#chk_"+checkId).attr("femail"));
	$("ruleReceiver").value = get_email(jQuery("#chk_"+checkId).attr("temail"));	
	$("ruleSubject").value = subjects[0];	
}

function saveAutoRule(data, textStatus){
	
	var f = document.autoRuleForm;
	
	if(!f.ruleSenderCheck.checked && 
		!f.ruleReceiverCheck.checked &&
		!f.ruleSubjectCheck.checked){
		alert(comMsg.comn_error_001);
		return;
	}
	
	if(trim(f.ruleSender.value) == "" && 
			trim(f.ruleReceiver.value) == "" && 
			trim(f.ruleSubject.value) == "") {
		alert(settingMsg.conf_filter_1);
		f.ruleSender.focus();
		return;
	}	

	if (f.ruleSenderCheck.checked &&
			(f.ruleSender.value) != "" && 
			!isEmail(f.ruleSender.value) && 
			!isMailDomain(f.ruleSender.value)) {
	    alert(settingMsg.conf_filter_44);
	    f.ruleSender.focus();
	    return;
	}
	
	if(f.ruleSenderCheck.checked &&
    		(f.ruleSender.value) != "" &&
    		!checkInputLength("normal",f.ruleSender,settingMsg.conf_filter_1,0,255)){
    	return;
	}	

    if (f.ruleReceiverCheck.checked &&
    		(f.ruleReceiver.value) != "" && 
    		!isEmail(f.ruleReceiver.value) && 
    		!isMailDomain(f.ruleReceiver.value)) {
    	alert(settingMsg.conf_filter_44);
        f.ruleReceiver.focus();
        return;
    }
    
    if(f.ruleReceiverCheck.checked &&
    		(f.ruleReceiver.value) != "" &&
    		!checkInputLength("normal",f.ruleReceiver,settingMsg.conf_filter_1,0,255)){
    	return;
    }
    
       
    if(f.ruleSubjectCheck.checked &&
    		subject != "" ){
    	var subject = f.ruleSubject.value;
    	var checkLength = 3;
    	if(validateHan(subject)){checkLength = 4;}
    	if(!checkInputLength("normal",f.ruleSubject,settingMsg.conf_filter_1,checkLength,255)){return;}
    }

	if(!f.mbox.checked){
		if (f.policy.value == "") {
			alert(settingMsg.conf_alert_mailbox_select);
			return;
		}
    }
    else {
    	var newbox = trim(f.boxName.value);

    	if(newbox == ""){
    		alert(settingMsg.conf_filter_37);
            f.boxName.value = "";
    		f.boxName.focus();
    		return;
    	}

    	if(!checkFolderName("folder",f.boxName)) {            
            return;
        }
    	
        if (trim(f.parentFolder.value) != "") {			
			f.boxName.value = f.parentFolder.value + "." + newbox;
        } else {
        	f.boxName.value = newbox;
        }
        
    }
	
	var param = $("autoRuleForm").serialize(true);	
	if(!f.ruleSenderCheck.checked){
		param.ruleSender = "";
	}	
	if(!f.ruleReceiverCheck.checked){
		param.ruleReceiver = "";
	}	
	if(!f.ruleSubjectCheck.checked){
		param.ruleSubject = "";
	}
	
	
	jQuery.post("/setting/saveMailFilter.do", param, saveAutoRuleResult, "json");	
}


function saveAutoRuleResult(data, textStatus){
	var isSuccess = false;
	if(textStatus == "success"){
		if(data.result == "success"){
			isSuccess = true;			
		}			
	}
	
	if(isSuccess){		
		if(confirm(mailMsg.list_filter01)){
			jQuery("#autoRuleRegist_p").hide();
			sortFilterList();
		} else {
			jQuery("#autoRuleRegist").jQpopup("close");			
		}
		folderControl.updateFolderInfo();
	} else if(data.result == "exist"){
		alert(settingMsg.conf_filter_exist);
		jQuery("#autoRuleRegist").jQpopup("close");
	} else {
		alert(mailMsg.filter_error);
		jQuery("#autoRuleRegist").jQpopup("close");
	}
}

function sortFilterList(){
	var f = document.autoRuleForm;	
	
	var toFolder = f.policy.value;
	if(f.mbox.checked){
		toFolder =  f.boxName.value;
	} else {
		toFolder = jQuery.trim(replaceAll(toFolder,"move",""));
	}
	
	var param = $("autoRuleForm").serialize(true);
	
	var param = $("autoRuleForm").serialize(true);	
	if(!f.ruleSenderCheck.checked){
		param.ruleSender = "";
	}	
	if(!f.ruleReceiverCheck.checked){
		param.ruleReceiver = "";
	}	
	if(!f.ruleSubjectCheck.checked){
		param.ruleSubject = "";
	}
	
	param.fromFolder = mailControl.getCurrentFolder();
	param.toFolder = toFolder;
	jQuery.post("/mail/sortFilterMessage.do", param, sortFilterResult, "json");
}

function sortFilterResult(data, textStatus){
	var isSuccess = false;
	if(textStatus == "success"){
		if(data.result == "success"){
			isSuccess = true;			
		}
	}
	jQuery("#autoRuleRegist").jQpopup("close");
	folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
}

function toggleAddNewBox(){
	var isCheck = $("mbox").checked
	if(isCheck){		
		jQuery("#newSaveRuleFolder").show();
		$("boxName").disabled = false;
		$("boxName").focus();
		jQuery("#currentSaveRuleFolder").hide();		
	}else if(!isCheck){		
		jQuery("#newSaveRuleFolder").hide();	
		jQuery("#currentSaveRuleFolder").show();
				
	}
}

function makeFolderList(id, type){
	var folderList;	
	var drowObj = jQuery("#"+id);	
	jQuery("ul#"+id +" li:not(:first)").remove();	
	
	if (type != 'new') {		
		folderList = folderControl.getFolderList();
	} else {
		folderList = folderControl.getFolderList("ufolder");
	}	
	
	var item;
	for (var i = 0; i < folderList.length; i++) {
		if(folderList[i].fname != "Sent" &&
			folderList[i].fname != "Reserved" &&
			folderList[i].fname != "Drafts" &&
			folderList[i].fname != "Quotaviolate"){				
			item = setFolderList(folderList[i].fname,folderList[i].name, type);
			if(item != ""){
				drowObj.append(jQuery(item));
			}
		}
	}
}


/**** LOCAL MAIL FUNC *****/
function moveLocalMail(){
	if(!checkItem())return;
	
	var infos = getMessgesInfo();
	var uids = infos.uid;
	var folders = infos.folders;
	var subjects = infos.subjects;
	var mailSizes = infos.sizes;	
	
	sendOcxLocalMail(USEREMAIL,hostInfo,folders,uids,subjects,mailSizes);
}




/*****SubLayer Function********/
var oldSubMenuID="";
var toolbarSubMenuTimeOut;
function showSubMenuLayer(id,obj){	
	clearSubMenu();
	
	if(oldSubMenuID != id ){		
		var subL = jQuery("<div></div>");
		subL.attr("id","funcSubLayer");
		subL.addClass("sub_item_canvas");
		subL.attr("pid",id);			
		if(id == "option"){
			if(LOCALE=="ko"){
				subL.css("left","-75px");
			}else{
				subL.css("left","-55px");
			}
		}
		if(skin == "skin3"){
			if(id=="forward"){
				subL.css("top","20px");
		//	}else if(id=="option" || id=="quick"){
		//		subL.css("top","14px");
			}else{
				subL.css("top","14px");
			}
		} else if(skin == "skin2"){
			if(RENDERMODE == "ajax") {
				subL.css("left",(jQuery("#"+id+"_sub").offset().left - 244) + "px");
				subL.css("top",(jQuery("#"+id+"_sub").offset().top - 60)+ "px");
			}
		}
		
		subL.append(obj);
		subL.hover(function(){
					var wrap = jQuery(this);
					var id = wrap.attr("pid");					
					wrap.show();
					toggleToolBarItem(id,true);
					clearTimeout(toolbarSubMenuTimeOut);
				},function(){
					clearSubMenu();
				});
		
		
		if(RENDERMODE == "ajax") {
			if(skin == "skin2"){
				jQuery("#m_mainContent").append(subL);
			} else {
				jQuery("#"+id+"_sub").append(subL);
			}
		} else {
			jQuery("#"+id+"_sub").append(subL);
		}
		jQuery("#funcSubLayer").css("z-index","1");
		jQuery("#funcSubLayer").show("fast");	
		oldSubMenuID = id;		
		toolbarSubMenuTimeOut = setTimeout("clearSubMenu()",3000);
	} else {
		oldSubMenuID = "";
	}	
	
}

function toggleToolBarItem(id,isOver){
	var overCss = [];
	if(jQuery("#"+id+"_item_link").parent().hasClass("menu_basic_unit")){
		overCss[0] = "micon_body_bover";
		overCss[1] = "micon_right_bover";
	} else {
		overCss[0] = "micon_body_sover";
		overCss[1] = "micon_right_sover";
	}
	if(isOver){
		jQuery("#"+id+"_item_link span.micon_body").addClass(overCss[0]);
		jQuery("#"+id+"_item_link span.micon_right").addClass(overCss[1]);
	} else {
		jQuery("#"+id+"_item_link span.micon_body").removeClass(overCss[0]);
		jQuery("#"+id+"_item_link span.micon_right").removeClass(overCss[1]);
	}	
}

function clearSubMenu(){	
	var oldL = jQuery("#funcSubLayer");
	if($("funcSubLayer")){		
		oldL.hide();		
		oldL.remove();	
		oldSubMenuID = "";		
		toggleToolBarItem(oldL.attr("pid"),false);
	}	
}

function contentSplitterChange(mode){
	contentSplitter.setSplitter(mode);
	paneMode = mode;
	paneControl.setIcon(paneMode);
	mailControl.reloadMessageList();
	initSubMode();
	jQuery.cookie("PM_L", mode,{path:"/"});
}

var PaneControl = Class.create({
	initialize : function(opt){
		this.opt = opt;
		this.positionNID = null;
		this.positionVID = null;
		this.positionHID = null;
		this.blankImgSrc = "/design/common/image/blank.gif";
		this.sideMenu = null;
		this.makeFunctionBtn();				
	},
	makeFunctionBtn:function(mode){		
		var css = {"margin-top":"10px","margin-bottom":"5px","cursor":"pointer"};
		
		var positionN =  jQuery("<img onclick=\"contentSplitterChange('n')\">");
		positionN.attr("src",this.blankImgSrc);
		positionN.addClass(this.opt.posNOff);
		positionN.css(css);		
		positionN.attr("align","absmiddle");
		positionN.attr("id","pn");		
		this.positionNID = "#pn"; 

		var positionV = jQuery("<img onclick=\"contentSplitterChange('v')\">");
		positionV.attr("src",this.blankImgSrc);
		positionV.addClass(this.opt.posVOff);
		positionV.css(css);		
		positionV.attr("align","absmiddle");
		positionV.attr("id","pv");		
		this.positionVID = "#pv"; 

		var positionH = jQuery("<img onclick=\"contentSplitterChange('h')\">");
		positionH.attr("src",this.blankImgSrc);
		positionH.addClass(this.opt.posNOff);
		positionH.css(css);		
		positionH.attr("align","absmiddle");
		positionH.attr("id","ph");		
		this.positionHID = "#ph";
		
		this.sideMenu = jQuery("<span></span>");
		this.sideMenu.append(positionN);		
		this.sideMenu.append(positionH);
		this.sideMenu.append(positionV);
		
		this.sideMenu.append(jQuery("<br/>"));
		this.sideMenu.append(
				jQuery("<a href='javascript:;' onclick='reloadListPage()' class='reloadItem'>"+mailMsg.menu_newmail+"</a>"));
	},
	getItem: function(){
		return this.sideMenu;
	},
	setIcon:function(mode){
		switch (mode) {
			case "n":
				jQuery(this.positionNID).removeClass(this.opt.posNOff);
				jQuery(this.positionNID).addClass(this.opt.posNOn);
				jQuery(this.positionVID).removeClass(this.opt.posVOn);
				jQuery(this.positionVID).addClass(this.opt.posVOff);
				jQuery(this.positionHID).removeClass(this.opt.posHOn);
				jQuery(this.positionHID).addClass(this.opt.posHOff);								
				break;
			case "v":
				jQuery(this.positionNID).removeClass(this.opt.posNOn);
				jQuery(this.positionNID).addClass(this.opt.posNOff);
				jQuery(this.positionVID).removeClass(this.opt.posVOff);
				jQuery(this.positionVID).addClass(this.opt.posVOn);
				jQuery(this.positionHID).removeClass(this.opt.posHOn);
				jQuery(this.positionHID).addClass(this.opt.posHOff);								
				break;
			case "h":
				jQuery(this.positionNID).removeClass(this.opt.posNOn);
				jQuery(this.positionNID).addClass(this.opt.posNOff);
				jQuery(this.positionVID).removeClass(this.opt.posVOn);
				jQuery(this.positionVID).addClass(this.opt.posVOff);
				jQuery(this.positionHID).removeClass(this.opt.posHOff);
				jQuery(this.positionHID).addClass(this.opt.posHOn);				
				break;		
		}		
	}
	
});

function loadListToolBar(){
	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationMenuID : "toolbarSide",
			navigationBottomID : "pageBottomNavi"
	};
	if(skin != "skin3"){
		var basicGroup1 = [
		 	         {type: "B" ,
		 	        Item : new MenuItem("del",
		 			"item_mail_del",
		 			mailMsg.menu_delete, "B", deleteMsg,false)},
		 			{type: "B" ,
		 	        Item : new MenuItem("adel",
		 	        "item_mail_delc",
		 			mailMsg.menu_deleteforever, "B", cleanMsg,false)}
		 		];
		 	
	 	var basicGroup2 = [
	 	 	         {type: "B" ,
	 	 	        Item : new MenuItem("reply",
	 	 			"item_mail_reply",
	 	 			mailMsg.menu_reply, "B", replyWrite,false)},
	 	 			{type: "S" ,
	 	 	        Item : [
	 	 	        new MenuItem("areply",
	 	 	        		"item_mail_areply",
	 	 	        		mailMsg.menu_replyall, "S", replyWriteAll,false),
	 	 			new MenuItem("forward",
	 	 					"item_mail_forward",
	 	 		 			mailMsg.menu_forward, "S", showForwardList,true)
	 	 	        ]}	 		 			
	 	 		];
	 	
	 	var basicGroup3 = [
	 	 	         {type: "B" ,
	 	 	        Item : new MenuItem("move",
	 	 	        "item_mail_move",
	 	 			mailMsg.menu_move, "B", showFolderList,true)},
	 	 			{type: "B" ,
	 	 	        Item : new MenuItem("copy",
	 	 	        "item_mail_copy", 	 			
	 	 			mailMsg.menu_copy, "B", showFolderList,true)}
	 	 		];
	 	
	 	var basicGroup4 = [	 	       
							{type: "B" ,
							 	Item : new MenuItem("print",
				 	  	 		"item_comm_print",
				 	  	 		mailMsg.menu_print, "B", printMsg,false)}
	 	                   ];
	 	
	 	var basicGroup5 = [	 	       
	 	 			{type: "S" ,
	 	 	        Item : [
	 	 	        new MenuItem("spam", 	 	        		
	 	 	        		"item_mail_spam",
	 	 	        		mailMsg.menu_spam, "S", spamRuleAdd,false),
	 	 			new MenuItem("normal", 	 		 			
	 	 		 			"item_mail_normal",
	 	 		 			mailMsg.menu_white, "S", whiteRuleAdd,false),
	 	 		 	new MenuItem("reject", 	 		 			
	 	 		 			"item_mail_srule",
	 	 		 			mailMsg.mail_receivreject, "S", registReject, false),
	 	 		 	new MenuItem("allow", 	 	 	 			
	 	 	 	 			"item_mail_srule",
	 	 	 	 			mailMsg.mail_receivallow, "S", registAllow, false)
	 	 	        ]}	 		 			
	 	 		];
	 	
	 	var basicGroup6 = [	 	       
	 	   	 			{type: "B" ,
	 	   	 	        Item : new MenuItem("quick", 	   	 		 			
	 	   	 		 			"item_mail_quick",
	 	   	 		 			mailMsg.menu_quick, "B", showQuickList,true)
	 	   	 	        }	 		 			
	 	   	 		];
	 	
	 	var extraGroup1 = [
							{type: "B" ,
							 	Item : new MenuItem("seen", 	                	
							 	"item_mail_rflag",
							 	mailMsg.menu_flag, "B", showSeenFlag,true)},
	 	                	{type: "B" ,
	 	 	   	 	 	        Item : new MenuItem("tag",
	 	 	   	 	 			"item_mail_tag",
	 	 	   	 	 			mailMsg.menu_tag, "B", showTagAdd,true)}
	 	 	   	 	 		];
	 	                	
	   	var extraGroup2 = [
	 	   	 	         {type: "B" ,
	 	   	 	 	        Item : new MenuItem("rule",
	 	   	 	 			"item_mail_rule",
	 	   	 	 		mailMsg.menu_rule, "B", autoRuleAdd,false)}
	 	   	 	 		];
	 	
	 	var extraGroup3 = [
				 	  	 	{type: "S" ,
							        Item : [
							        new MenuItem("save",
							        		"item_mail_save",
							        	mailMsg.menu_save, "S", saveMsg,false),
									new MenuItem("upload",
								 			"item_mail_upload",
								 		mailMsg.menu_upload, "S", uploadMsg,false)
							        ]}
	 	 	   	 	 		];
	 	
	 	var extraGroup4 = [
	 	                   {type: "B" ,
	 	                	   Item : new MenuItem("local",
	 	                			   "item_mail_local",
	 	                			   mailMsg.menu_localmail, "B", moveLocalMail,false)}
	 	                   ];
	 	
	 	var extraGroup5 = [
	 	                   {type: "B" ,
	 	                	   Item : new MenuItem("rewrite",
	 	                			   "item_mail_rewrite",
	 	                			   mailMsg.menu_rewrite, "B", reWrite,false)}
	 	                   ];
	 	
		var menuList = [
		               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1,basicGroup2,basicGroup3,basicGroup4,basicGroup5,basicGroup6]},
		               {name:mailMsg.menu_extra,id:"extra",initOn:true,linkFunc:changeToolbarMode,
		            		groupItem : [extraGroup1,extraGroup2,extraGroup3,extraGroup4,extraGroup5]}
		               ];
	
	}else{
			var basicGroup1 = [
						{type: "B" ,
						   Item : new MenuItem("rewrite",
						   "item_mail_rewrite",
						   mailMsg.menu_rewrite, "B", reWrite,false)},
		  	 	         {type: "B" ,
			 	 	        Item : new MenuItem("reply",
			 	 			"item_mail_reply",
			 	 			mailMsg.menu_reply, "B", replyWrite,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("areply",
			  	 	        "item_mail_areply",
			  	 	     mailMsg.menu_replyall, "B", replyWriteAll,false)},
			  	 	     {type: "B" ,
				  	 	        Item : new MenuItem("forward",
				  	 	        "item_mail_forward",
				  	 	     mailMsg.menu_forward, "B", showForwardList,true)},
			  	 	     {type: "B" ,
				 	        Item : new MenuItem("del",
				 			"item_mail_del",
				 			mailMsg.menu_delete, "B", deleteMsg,false)},
				 		{type: "B" ,
				 	        Item : new MenuItem("adel",
				 	        "item_mail_delc",
				 			mailMsg.menu_deleteforever, "B", cleanMsg,false)},
			 			{type: "B" ,
				 	        Item : new MenuItem("spam",
				 	        "item_mail_spam",
				 	       mailMsg.menu_spam, "B", spamRuleAdd,false)},
				 	    {type: "B" ,
				 	        Item : new MenuItem("normal",
				 	        "item_mail_normal",
				 	       mailMsg.menu_white, "B", whiteRuleAdd,false)},
				 	    {type: "B" ,
					 	    Item : new MenuItem("reject",
					 	    "item_mail_srule",
					 	   mailMsg.mail_receivreject, "B", registReject,false)},
					 	{type: "B" ,
					 	    Item : new MenuItem("allow",
					 	    "item_mail_srule",
					 	   mailMsg.mail_receivallow, "B", registAllow,false)},
					 	{type: "B" ,
						    Item : new MenuItem("rule",
						    "item_mail_rule",
						    mailMsg.menu_rule, "B", autoRuleAdd,false)}
		  	 			
		  	 		];
			
	     	var extraGroup1 = [
		   	   	 	         {type: "B" ,
							 	Item : new MenuItem("seen", 	                	
							 	"item_mail_rflag",
							 	mailMsg.menu_flag, "BSM", showSeenFlag,true)},
						 	{type: "B" ,
	 	 	   	 	 	        Item : new MenuItem("tag",
	 	 	   	 	 			"item_mail_tag",
		 	 	   	 	 		mailMsg.menu_tag, "BSM", showTagAdd,true)},
		 	 	   	 	 	{type: "B" ,
	 	 		 	 	        Item : new MenuItem("move",
	 	 		 	 	        "item_mail_move",
	 	 		 	 			mailMsg.menu_move, "BSM", showFolderList,true)},
 	 		 	 			{type: "B" ,
	 	 		 	 	        Item : new MenuItem("copy",
	 	 		 	 	        "item_mail_copy", 	 			
	 	 		 	 			mailMsg.menu_copy, "BSM", showFolderList,true)},
	 	 		 	 		{type: "B" ,
	 	 		 	   	 	    Item : new MenuItem("quick", 	   	 		 			
	 	 		 	   	 		"item_mail_quick",
	 	 		 	   	 		mailMsg.menu_quick, "BM", showQuickList,true)},
 		 	   	 		 	{type: "B" ,
 		 	 		 	   	 	        Item : new MenuItem("option", 	   	 		 			
 		 	 		 	   	 		 			"item_mail_quick",
 		 	 		 	   	 		 			mailMsg.menu_option_option, "OM", showOptionList,true)}
	 	 		 	   	 	];
		   	
		  	var menuList = [
		  	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		  	            	groupItem : [basicGroup1]},
		  	               {name:mailMsg.menu_extra,id:"extra",initOn:true,linkFunc:changeToolbarMode,
		  	            		groupItem : [extraGroup1]}
		  	               ];
		  	jQuery(".mail_body_menu").css("display","");
		  	//jQuery(".mail_body_tabmenu").css("border-bottom","0px");
		  	printReloadLocalMailEmpty();
		  	printButtonSet();
		  	if(RENDERMODE == "ajax")reloadButtonSet();
		  	localMailButtonSet();
		  	
		  	
	}
	
	var paneOpt = {
			posNOn : "item_layout1_on",
			posNOff : "item_layout1_off",
			posHOn : "item_layout2_on",
			posHOff : "item_layout2_off",
			posVOn : "item_layout3_on",
			posVOff : "item_layout3_off"
			
	};
	
	paneControl = new PaneControl(paneOpt);	
	menuBar = new  MailMenuBar(menuBarOpt,menuList,paneControl.getItem());	
	changeToolbarMode("basic");
	currentToolbarMode = "basic";
	paneControl.setIcon(paneMode);
	setAlWriteMode(false);
	jQuery("#siSearchBox").show();
	jQuery("#adSearchBoxBtn").show();	
}

function printReloadLocalMailEmpty(){
	jQuery("#reload").empty();
	jQuery("#localMail").empty();
	jQuery("#print").empty();
	jQuery("#autoSaveArea").empty();
}
function printButtonSet(){
	var printTag = jQuery("<a href='#'></a>");
	printTag.css("padding","0 5px");
	printTag.click(function(e){printMsg();});
	var imgPrintTag = jQuery("<img>");
	imgPrintTag.attr("src","/design/skin3/image/icon/ic_printer.gif");	
	imgPrintTag.attr("title",comMsg.comn_print);
	printTag.append(imgPrintTag);
	jQuery("#print").append(printTag);
}
function reloadButtonSet(){
	var reloadTag = jQuery("<a href='#'></a>");
	reloadTag.css("padding","0 5px");
	reloadTag.click(function(e){reloadListPage();});
	var imgReloadTag = jQuery("<img>");
	imgReloadTag.attr("src","/design/skin3/image/icon/ic_refresh.gif");
	imgReloadTag.attr("title",comMsg.comn_refresh);
	reloadTag.append(imgReloadTag);
	jQuery("#reload").append(reloadTag);
}
function localMailButtonSet(){
	if(jQuery.browser.msie && isLocalMail){
		var localMailTag = jQuery("<a href='#'></a>");
		localMailTag.css("padding","0 5px");
		localMailTag.click(function(e){moveLocalMail();});
		var imgLocalMailTag = jQuery("<img>");
		imgLocalMailTag.attr("src","/design/skin3/image/icon/ic_public.gif");
		imgLocalMailTag.attr("title",mailMsg.mail_localmail);
		localMailTag.append(imgLocalMailTag);
		jQuery("#localMail").append(localMailTag);		
	}
}

function setMailMenuBarStatusSkin3(){
	menuBar.initToolBarSkin3();
	if(currentFolderType == "all"){
		
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		
		menuBar.hideMenu("allow");
		menuBar.hideSkin3MenuBar("allow");
		
	}else if(currentFolderType == "shared"){
		
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("reply");
		menuBar.hideSkin3MenuBar("reply");
		
		menuBar.hideMenu("areply");
		menuBar.hideSkin3MenuBar("areply");
		
		menuBar.hideMenu("forward");
		menuBar.hideSkin3MenuBar("forward");
		
		menuBar.hideMenu("del");
		menuBar.hideSkin3MenuBar("del");
		
		menuBar.hideMenu("adel");
		menuBar.hideSkin3MenuBar("adel");
		
		menuBar.hideMenu("spam");
		menuBar.hideSkin3MenuBar("spam");
		
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		
		menuBar.hideMenu("reject");
		menuBar.hideSkin3MenuBar("reject");
		
		menuBar.hideMenu("allow");
		menuBar.hideSkin3MenuBar("allow");
		
		menuBar.hideMenu("rule");
		menuBar.hideSkin3MenuBar("rule");
		
		menuBar.hideMenu("move");
		menuBar.hideSkin3MenuBar("move");
		
		menuBar.hideMenu("seen");
		menuBar.hideSkin3MenuBar("seen");
		
		menuBar.hideMenu("tag");
		menuBar.hideSkin3MenuBar("tag");
		
	}else if(currentFolderType == "inbox"){
		
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		
		menuBar.hideMenu("allow"); 
		menuBar.hideSkin3MenuBar("allow");
		
		
	}else if(currentFolderType == "sent"){
		menuBar.hideMenu("spam");
		menuBar.hideSkin3MenuBar("spam");
		
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		
		menuBar.hideMenu("reject");
		menuBar.hideSkin3MenuBar("reject");
		
		menuBar.hideMenu("allow");
		menuBar.hideSkin3MenuBar("allow");
		
		menuBar.hideMenu("rule");
		menuBar.hideSkin3MenuBar("rule");
		
		menuBar.hideSkin3MenuBar("adel");
	}else if(currentFolderType == "draft" || currentFolderType == "reserved"){
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("reply");
		menuBar.hideSkin3MenuBar("reply");
		
		menuBar.hideMenu("areply");
		menuBar.hideSkin3MenuBar("areply");
		
		menuBar.hideMenu("forward");
		menuBar.hideSkin3MenuBar("forward");
		
		menuBar.hideMenu("spam");
		menuBar.hideSkin3MenuBar("spam");
		
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		
		menuBar.hideMenu("reject");
		menuBar.hideSkin3MenuBar("reject");
		
		menuBar.hideMenu("allow");
		menuBar.hideSkin3MenuBar("allow");
		
		menuBar.hideMenu("rule");
		menuBar.hideSkin3MenuBar("rule");
		
		menuBar.hideSkin3MenuBar("adel");
		
		if(currentFolderType == "reserved"){
			menuBar.hideMenu("move");
			menuBar.hideSkin3MenuBar("move");
			
			menuBar.hideMenu("copy");
			menuBar.hideSkin3MenuBar("copy");
		}
	}else if(currentFolderType == "quotaviolate"){
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("reply");
		menuBar.hideSkin3MenuBar("reply");
		
		menuBar.hideMenu("areply");
		menuBar.hideSkin3MenuBar("areply");
		
		menuBar.hideMenu("forward");
		menuBar.hideSkin3MenuBar("forward");
		
		menuBar.hideMenu("spam");
		menuBar.hideSkin3MenuBar("spam");
		menuBar.hideMenu("reject");
		menuBar.hideSkin3MenuBar("reject");
		
		menuBar.hideMenu("seen");
		menuBar.hideSkin3MenuBar("seen");
		
		menuBar.hideMenu("tag");
		menuBar.hideSkin3MenuBar("tag");
		menuBar.hideMenu("rule");
		menuBar.hideSkin3MenuBar("rule");
		menuBar.hideMenu("copy");
		menuBar.hideSkin3MenuBar("copy");
	} else if(currentFolderType == "spam" || currentFolderType == "trash"){
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		
		menuBar.hideMenu("spam");
		menuBar.hideSkin3MenuBar("spam");
		
		menuBar.hideMenu("reject");
		menuBar.hideSkin3MenuBar("reject");
		if(currentFolderType == "trash"){
			menuBar.hideMenu("adel");
			menuBar.hideSkin3MenuBar("adel");
		}
	}else{
		menuBar.hideMenu("rewrite");
		menuBar.hideSkin3MenuBar("rewrite");
		menuBar.hideMenu("normal");
		menuBar.hideSkin3MenuBar("normal");
		menuBar.hideMenu("allow");
		menuBar.hideSkin3MenuBar("allow");
	}
}

function setMailMenuBarStatus(){
	menuBar.initToolBar();	
	
	if(!jQuery.browser.msie || !isLocalMail){
		jQuery("#extra_3").hide();
//		jQuery("#extra_2").hide();
	}
	
	if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
		if(currentToolbarMode == "basic"){
//			jQuery("#basic_4").hide();
			jQuery("#basic_5").hide();
		} else if(currentToolbarMode == "extra"){
			menuBar.disableToolBarItem("upload");
		}
		
	}
	if(currentFolderType == "all"){
		if(currentToolbarMode == "basic"){
//			jQuery("#basic_3").hide();
			jQuery("#basic_4").hide();
		} else if(currentToolbarMode == "extra"){
//			jQuery("#extra_2").hide();	
			jQuery("#extra_3").hide();
			jQuery("#extra_4").hide();
			menuBar.disableToolBarItem("save");
			menuBar.disableToolBarItem("upload");			
		}
	} else if(currentFolderType == "shared"){
		if(currentToolbarMode == "basic"){
			jQuery("#basic_0").hide();
			jQuery("#basic_1").hide();
			menuBar.disableToolBarItem("move");
			jQuery("#basic_3").hide();			
			jQuery("#basic_4").hide();			
		} else if(currentToolbarMode == "extra"){
			jQuery("#extra_0").hide();
			jQuery("#extra_1").hide();
			jQuery("#extra_2").hide();
			jQuery("#extra_4").hide();
			menuBar.disableToolBarItem("upload");
			jQuery("#extra_3").hide();	
		}
	}else if(currentFolderType == "inbox"){ 
		if(currentToolbarMode == "basic"){		
			menuBar.hideMenu("normal");
			menuBar.hideMenu("allow");
		} else if(currentToolbarMode == "extra"){							
			menuBar.disableToolBarItem("upload");
			jQuery("#extra_4").hide();
		}
	}else if(currentFolderType == "sent" || currentFolderType == "draft"){
		if(currentFolderType == "draft"){
			jQuery("#toolbarSide").hide();
		}
		if(currentToolbarMode == "basic"){
			if(currentFolderType == "draft"){
				jQuery("#basic_1").hide();
			}
//			jQuery("#basic_3").hide();
			jQuery("#basic_4").hide();
		} else if(currentToolbarMode == "extra"){
			jQuery("#extra_1").hide();
			jQuery("#extra_3").hide();	
//			jQuery("#extra_2").hide();	
			menuBar.disableToolBarItem("upload");
			if(currentFolderType == "draft"){
				menuBar.disableToolBarItem("tag");
				jQuery("#extra_4").hide();
			}
		}		
	} else if(currentFolderType == "reserved"){
		if(currentToolbarMode == "basic"){
			jQuery("#basic_1").hide();
			jQuery("#basic_2").hide();
//			jQuery("#basic_3").hide();			
			jQuery("#basic_4").hide();			
		} else if(currentToolbarMode == "extra"){
			jQuery("#extra_1").hide();
			jQuery("#extra_3").hide();	
//			jQuery("#extra_2").hide();
			jQuery("#extra_4").hide();
			menuBar.disableToolBarItem("tag");
			menuBar.disableToolBarItem("upload");
		}
	} else if(currentFolderType == "quotaviolate"){
		if(currentToolbarMode == "basic"){
			jQuery("#basic_1").hide();			
//			jQuery("#basic_3").hide();
			jQuery("#basic_4").hide();
			menuBar.disableToolBarItem("copy");
		} else if(currentToolbarMode == "extra"){
			jQuery("#extra_0").hide();
			jQuery("#extra_1").hide();
			jQuery("#extra_4").hide();
			menuBar.disableToolBarItem("upload");
		}		
	} else if(currentFolderType == "spam"){
		if(currentToolbarMode == "basic"){			
			menuBar.hideMenu("spam");
			menuBar.hideMenu("reject");
		} else if(currentToolbarMode == "extra"){
			menuBar.disableToolBarItem("upload");
			jQuery("#extra_4").hide();
		}
	} else if(currentFolderType == "trash"){
		if(currentToolbarMode == "basic"){
			menuBar.disableToolBarItem("adel");
			menuBar.hideMenu("spam");
			menuBar.hideMenu("reject");
		} else if(currentToolbarMode == "extra"){
			menuBar.disableToolBarItem("upload");
			jQuery("#extra_4").hide();
		}
	} else {		
	
		if(currentToolbarMode == "basic"){		
			menuBar.hideMenu("normal");
			menuBar.hideMenu("allow");
		}else if(currentToolbarMode == "extra"){
			jQuery("#extra_4").hide();
		}
	}
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#extra_1").hide();
		jQuery("#reject_item_link").hide();
	}
	
}


function loadWriteToolBar(writeSettingInfo){
	if(CURRENT_PAGE_NAME != "MAILWRITEPOPUP")jQuery("body").unbind("keydown",scrollDownEvent);
	var menuBarOpt = {
			mode : "write",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var receiveNotiChk,saveSentChk,autoSaveMode,autoSaveTerm,resent,onesend,reservation,secure,savesend;
	
	if(skin != "skin3"){
		
		var writeGroup1 = [
		   	 	         {type: "B" ,
		   	 	        Item : new MenuItem("send",
		   	 			"item_mail_send",
		   	 			mailMsg.menu_send, "B", sendNormalMessage,false)},
		   	 			{type: "S" ,
		   	 	        Item : [
		   	 	        new MenuItem("priview",
		   	 	        		"item_mail_preview",
		   	 	        		mailMsg.menu_preview, "S", writePreview,false),
		   	 			new MenuItem("draft",
		   	 		 			"item_mail_save",
		   	 		 			mailMsg.menu_draft, "S", sendDraftMessage,false)
		   	 	        ]}	 		 			
		   	 		];
		
		receiveNotiChk = (writeSettingInfo.receiveNoti == "on")?"checked":"";
		saveSentChk = (writeSettingInfo.saveSent == "on")?"checked":"";	
		autoSaveMode = writeSettingInfo.autoSaveMode;
		autoSaveTerm = writeSettingInfo.autoSaveTerm;
		
		
		resent = "<div style='float:left;'><input type='checkbox' name='receivenoti' id='receivenoti' "+receiveNotiChk+"></div>"+
					"<div class='menu_label'><label for='receivenoti'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_receivenoti+"</span></label></div><div class='fclear'></div>";
		onesend = "<div style='float:left;'><input type='checkbox' name='onesend' id='onesend'></div>"+
					"<div class='menu_label'><label for='onesend'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_onesend+"</span></label></div><div class='fclear'></div>";
		reservation =
					"<div style='float:left;*padding-top:2px'><input type='checkbox' name='reservation' id='reservation' onclick='toggleReservDate()'></div>"+
					"<label for='reservation'><div class='menu_label' style='*padding-top:2px;'>"+
					"<span class='menu_text jpf'>"+mailMsg.menu_reservation+"</span></div></label>" +
					"<div style='float:left;position:relative'>"+				
					"<div id='reservDateSet' style='display:none;width:320px;position:absolute;top:-5px; left:3px;white-space:nowrap;'>"+
					"<div style='float:left; white-space:nowrap; border:#C6C6C6 solid 1px;padding:2px;background:#f8f8f8;'>" +
					"<div style='float:left;'><input type='text' class='IP100px' style='width:70px;height:15px;padding-top:5px;*padding-top:0px;padding-left:5px;font-size:11px;' id='resevDate' readonly='readonly'/></div>"+
					"<div style='float:left;padding-left:3px;'><div id='resevHourSelect'></div></div>"+
					"<div style='float:left;padding-left:3px;'><div id='resevMinSelect'></div></div>"+
					"<div style='float:left;padding-left:3px;'><div id='resevInfo' style='margin-top:2px'/></div>"+
					"<div id='detail' style='display:none; position:absolute'>"+
					"<div id=pop style='padding:2px;top:27px;left:-229px;position:absolute;border:1px solid #C6C6C6;background-color:#FDFBF4;color:#495C79;padding:5;line-height:14px;font-size:11px'>"+
					mailMsg.reserved_alert+"</div></div>"+
					"</div></div></div><div class='fclear'></div>"				
					;
		
		secure = "<div style='float:left'><input type='checkbox' name='securemail' id='securemail' onclick='toggleSecureInput()'></div>"+
					"<label for='securemail'> "+
					"<div class='menu_label'><span class='menu_text jpf'>"+mailMsg.menu_secure+"</span></div></label>"+
					"<div style='float:left;position:relative'>" +
					"" +
					"<span id='secureMailSet' style='display:none; width:200px;position:absolute;top:-5px; left:3px; white-space:nowrap;border:#C6C6C6 solid 1px;padding:2px;background:#f8f8f8;' class='menu_text jpf'>"+
					mailMsg.mail_secure_form_004+" "+
					"<input type='password' class='IP50' style='width:50px' name='smailPassword' id='smailPassword' /> "+
					mailMsg.mail_secure_hint+" "+
					"<input type='text' class='IP50' style='width:50px' name='smailHint' id='smailHint' />"+				
					"</span></div>"			
					;
		
		if(IS_USE_EXPRESS_E || writeSettingInfo.securemail == "disabled"){
			secure = "<div style='display:none'></div>";
		}
		
		
		savesend = "<div style='float:left;'><input type='checkbox' name='savesent' id='savesent' "+saveSentChk+"></div>"+
					"<div class='menu_label'><label for='savesent'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_sentsave+"</span></label></div>" +
					"<div class='fclear'></div>";
		
		var writeGroup2 = [
		                   	{type: "T" ,
		                   	Item : [new TextItem("recent",resent),
		                   	        new TextItem("onesend",onesend),
		                   	        new TextItem("savesend",savesend)]}
		                ];
		
		var writeGroup3 = [
		                   	{type: "T" ,
		                   	Item : [new TextItem("reservation",reservation),
		                   	        new TextItem("secure",secure)]}
		                ];
	
		if(IS_XPRESS_MAIL_WRITE){
			writeGroup3 = null;
		}
	
		var menuList = [
			               {name:mailMsg.menu_write,id:"write",initOn:true,linkFunc:changeToolbarMode,
			            	groupItem : [writeGroup1,writeGroup2,writeGroup3]}
			               ];
	
	}else{
		
		receiveNotiChk = (writeSettingInfo.receiveNoti == "on")?"checked":"";
		saveSentChk = (writeSettingInfo.saveSent == "on")?"checked":"";	
		autoSaveMode = writeSettingInfo.autoSaveMode;
		autoSaveTerm = writeSettingInfo.autoSaveTerm;
		
		
		resent = "<div style='float:left;'><input type='checkbox' name='receivenoti' id='receivenoti' "+receiveNotiChk+"></div>"+
					"<div class='menu_label'><label for='receivenoti'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_receivenoti+"</span></label></div>";
		onesend = "<div style='float:left;'><input type='checkbox' name='onesend' id='onesend'></div>"+
					"<div class='menu_label'><label for='onesend'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_onesend+"</span></label></div>";
				
		reservation = "<div style='float:left;padding-top:5px;*padding-top:0px'><input type='checkbox' name='reservation' id='reservation' onclick='checkReservSecure()'></div>"+
						"<label for='reservation'><div class='menu_label' style='padding-top:5px;'>"+
						"<span class='menu_text jpf'>"+mailMsg.menu_reservation+"</span></div></label>" +
						"<div style='float:left'><input type='text' class='IP100px' style='width:70px;height:15px;*padding-top:0px;padding-left:5px;font-size:11px;' id='resevDate' readonly='readonly'/></div>"+
						"<div style='float:left'><div id='resevHourSelect'></div></div>"+
						"<div style='float:left'><div id='resevMinSelect'></div></div>"+
						"<div style='float:left'><div id='resevInfo' style='margin-top:2px'></div></div>"+
						"<div id='detail' style='display:none; position:absolute'>"+
						"<div id=pop style='width:320px;padding:2px;top:20px;left:-309px;position:absolute;border:1px solid #C6C6C6;background-color:#FDFBF4;color:#495C79;padding:5;line-height:14px;font-size:11px'>"+
						mailMsg.reserved_alert+"</div></div>";
		
		secure = "<div style='float:left;padding-top:5px;padding-left:20px;*padding-top:0px'><input type='checkbox' name='securemail' id='securemail' onclick='checkReservSecure()'></div>"+
				 "<label for='securemail'> "+
				 "<div class='menu_label' style='padding-top:5px;padding-left:5px'><span class='menu_text jpf'>"+mailMsg.menu_secure+"</span></div></label>"+
				 "<span id='smailform'>"+mailMsg.mail_secure_form_004+"<input type='password' class='IP50' style='width:50px' name='smailPassword' id='smailPassword'/></span>"+
				 "<span id='smailhint' style='padding-left:5px'>"+mailMsg.mail_secure_hint+"<input type='text' class='IP50' style='width:50px' name='smailHint' id='smailHint' /></span>";
		
		if(IS_USE_EXPRESS_E || writeSettingInfo.securemail == "disabled"){
			secure = "<div style='display:none'></div>";
		}
		
		
		savesend = "<div style='float:left;'><input type='checkbox' name='savesent' id='savesent' "+saveSentChk+"></div>"+
					"<div class='menu_label'><label for='savesent'> "+
					"<span class='menu_text jpf'>"+mailMsg.menu_sentsave+"</span></label></div>";
					
		
		var writeGroup1 = [
			   	 	         {type: "B" ,
			   	 	        Item : new MenuItem("send",
			   	 			"item_mail_send",
			   	 			mailMsg.menu_send, "B", sendNormalMessage,false)},
			   	 			{type: "B" , 
				   	 	        Item : new MenuItem("priview",
				   	 			"item_mail_preview",
				   	 			mailMsg.menu_preview, "B", writePreview,false)},
				   	 		{type: "B" , 
					   	 	        Item : new MenuItem("draft",
					   	 			"item_mail_save",
					   	 			mailMsg.menu_draft, "B", sendDraftMessage,false)},
				   	 		{type: "T" ,
				                   	Item : [new TextItem("recent",resent),
				                   	        new TextItem("onesend",onesend),
				                   	        new TextItem("savesend",savesend)]}
			   	 		];
		var menuList = [
		  	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		  	            	groupItem : [writeGroup1]}
		  	               ];
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		printReloadLocalMailEmpty();
		jQuery("#pageNavi").empty();

		if(IS_XPRESS_MAIL_WRITE || writeSettingInfo.securemail == "disabled"){
			jQuery("#reservSecureTabBySkin3").hide();
		}else{
			jQuery("#reservSecureTabBySkin3").append(reservation).append(secure);
			settingReservDate();
			settingToggleSecureInput();
		}
		
		
	}
	var autoSaveItem = getAutoSaveItem();
	if(IS_XPRESS_MAIL_WRITE){
		autoSaveItem = null;
	}
	menuBar = new  MailMenuBar(menuBarOpt,menuList,autoSaveItem);
	menuBar.makeToolbar("write");
	setAlWriteMode(true);
	
	if(IS_XPRESS_MAIL_WRITE){
		if(skin != "skin3"){
			menuBar.disableToolBarItem("draft");
		}else{
			menuBar.hideMenu("draft");
		}
	}
	
	if(!IS_XPRESS_MAIL_WRITE){
		var checkTerm = (autoSaveMode == "on")?Number(autoSaveTerm):0;		
		preAutoSaveTerm = checkTerm;
		makeAutoSaveSelect(checkTerm);
	}
}
function settingReservDate(){
	var isReservChk = $("reservation").checked;
	var isSecureChk = ($("securemail"))?$("securemail").checked:false;
	var dateLayer = jQuery("#reservDateSet");
		
	if(LOCALE == "jp"){
		dateLayer.css("left","25px");
	}
				
	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	$("resevDate").value = "";
	jQuery("#resevDate").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#resevDate").focus(function(){jQuery(this).val("")});
	$("resevDate").value = reserveTimeSet.todayDate;
	jQuery("#resevHourSelect").empty();
	jQuery("#resevMinSelect").empty();
	jQuery("#resevHourSelect").timeSelectList("resevHour",1,23,comMsg.comn_hour,reserveTimeSet.thour);			
	jQuery("#resevMinSelect").timeSelectList("resevMin",10,50,comMsg.comn_min,reserveTimeSet.tmin);
	jQuery("#resevInfo").empty();
	jQuery("#resevInfo").append(jQuery("<img>").attr("src","/design/default/image/icon/ic_r_notice.gif").hover(resevInfoOver,resevInfoOver));
	
	jQuery("#resevDate").hide();
	jQuery("#resevHourSelect").hide();
	jQuery("#resevMinSelect").hide();
	jQuery("#resevInfo").hide();
	
}

function checkReservSecure(){
	var isReservChk = $("reservation").checked;
	var isSecureChk = ($("securemail"))?$("securemail").checked:false;
	
	if(isReservChk && isSecureChk){
		alert(mailMsg.write_alert004);
		$("reservation").checked = false;
		$("securemail").checked = false;
		isReservChk = false;
		isSecureChk = false;
	} 

	if(isReservChk){
		jQuery("#resevDate").show();
		jQuery("#resevHourSelect").show();
		jQuery("#resevMinSelect").show();
		jQuery("#resevInfo").show();
	}else{
		jQuery("#resevDate").hide();
		jQuery("#resevHourSelect").hide();
		jQuery("#resevMinSelect").hide();
		jQuery("#resevInfo").hide();
	}
	
	if(isSecureChk){
		jQuery("#smailform").show();
		jQuery("#smailhint").show();
	}else{
		jQuery("#smailform").hide();
		jQuery("#smailhint").hide();
	}
	
	
}

function settingToggleSecureInput(){
	var isReservChk = $("reservation").checked;
	var isSecureChk = ($("securemail"))?$("securemail").checked:false;
	var secureInputLayer = jQuery("#secureMailSet");
			
	if(isSecureChk){
		if($("securemail").checked){
			$("smailPassword").value = "";
			$("smailHint").value = "";
		}
	}else{
		$("smailform").hide();
		$("smailhint").hide();
	}
	
}
function toggleReservDate(){
	var isReservChk = $("reservation").checked;
	var isSecureChk = ($("securemail"))?$("securemail").checked:false;
	var dateLayer = jQuery("#reservDateSet");
	
	if(isReservChk && isSecureChk){
		alert(mailMsg.write_alert004);
		$("reservation").checked = false;
	} else {	
		if(isReservChk){
			if(LOCALE == "jp"){
				dateLayer.css("left","25px");
			}
			dateLayer.show();			
			jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
			$("resevDate").value = "";
			jQuery("#resevDate").datepick({dateFormat:'yy-mm-dd'});
			jQuery("#resevDate").focus(function(){jQuery(this).val("")});
			$("resevDate").value = reserveTimeSet.todayDate;
			jQuery("#resevHourSelect").empty();
			jQuery("#resevMinSelect").empty();
			jQuery("#resevHourSelect").timeSelectList("resevHour",1,23,comMsg.comn_hour,reserveTimeSet.thour);			
			jQuery("#resevMinSelect").timeSelectList("resevMin",10,50,comMsg.comn_min,reserveTimeSet.tmin+10);
			jQuery("#resevInfo").empty();
			jQuery("#resevInfo").append(jQuery("<img>").attr("src","/design/default/image/icon/ic_r_notice.gif").hover(resevInfoOver,resevInfoOver));			
		} else {
			dateLayer.hide();
		}
	}
}
function resevInfoOver(){
		
	displayDetail = document.getElementById("detail");
	if(displayDetail.style.display=="inline"){
		displayDetail.style.display = "none"
	}else{
		displayDetail.style.display = "inline"
	}
}

function toggleSecureInput(){
	var isReservChk = $("reservation").checked;
	var isSecureChk = ($("securemail"))?$("securemail").checked:false;
	var secureInputLayer = jQuery("#secureMailSet");
	
	if(isReservChk && isSecureChk){
		alert(mailMsg.write_alert004);
		$("securemail").checked = false;
	} else {	
		if(isSecureChk){
			hugeMailCheck();			
			if($("securemail").checked){
				$("smailPassword").value = "";
				$("smailHint").value = "";
				secureInputLayer.show();
			}
		} 
	}
}

function chkSecureMail(chk) {
    var passwordObj = $('smailPassword');
    var passwordhintObj = $('smailHint');
    var secureCheck = $('securemail');

	if (chk) {
		passwordObj.disabled 	= false;
		passwordhintObj.disabled	= false;
		secureCheck.disabled = false;		
	} else {
		passwordObj.disabled 	= true;
		passwordhintObj.disabled	= true;
		secureCheck.disabled = true;
		passwordObj.value = "";
		passwordhintObj.value = "";
		secureCheck.checked = false;
	}
	
	toggleSecureInput();
}

function chkReservedMail(chk) {
	var reservCheck = $('reservation');
    var resevDate = $('resevDate');
    var resevHour = $('resevHour');
    var resevMin = $('resevMin');
	if (chk) {
		resevDate.disabled 	= false;
		resevHour.disabled	= false;
		resevMin.disabled = false;
		reservCheck.disabled = false;
	} else {
		resevDate.disabled 	= true;
		resevHour.disabled	= true;
		resevMin.disabled = true;
		reservCheck.disabled = true;
		reservCheck.checked = false;
	}
	
	toggleReservDate();
}

function loadFolderMangerToolBar(){
	var menuBarOpt = {
			mode : "folder",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var folderGroup1 = [
	   	 	         {type: "B" ,
	   	 	        Item : new MenuItem("send",
	   	 			"/design/default/image/icon/ic_at_import.gif",
	   	 			mailMsg.menu_endmsgt, "B", endFolderManage,false)}	   	 				 		 			
	   	 		];
	
	var backupPane = "<div id='backup_info' class='TM_backupInfo'></div>";
	var folderGroup2 = [
		   	 	         {type: "B" ,
		   	 	        Item : new TextItem("backup",backupPane)}	   	 				 		 			
		   	 		];	
	
	var menuList = [
		               {name:mailMsg.menu_basic,id:"folder",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [folderGroup1,folderGroup2]}
		               ];
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("folder");
	setAlWriteMode(false);
	jQuery("#adSearchBoxBtn").show();
	
}

function loadMDNListToolBar(){
	
	var menuBarOpt = {
			mode : "mdnlist",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1;
	if(skin != "skin3"){
		basicGroup1 = [
		 	         {type: "B" ,
		 	        Item : new MenuItem("del",
		 			"item_mail_del",
		 			mailMsg.menu_delete, "B", deleteMsg,false)},
		 			{type: "B" ,
		 	        Item : new MenuItem("adel",
		 			"item_mail_delc",
		 			mailMsg.menu_deleteforever, "B", cleanMsg,false)}
		 		]; 	
	}else{
		basicGroup1 = [
				 	         {type: "B" ,
				 	        Item : new MenuItem("del",
				 			"item_mail_del",
				 			mailMsg.menu_delete, "B", deleteMsg,false)},
				 			{type: "B" ,
				 	        Item : new MenuItem("adel",
				 			"item_mail_delc",
				 			mailMsg.menu_deleteforever, "B", cleanMsg,false)}
				 		]; 	
	}
	
	var menuList = [
		               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1]}
		               ];
		
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	setAlWriteMode(false);
	jQuery("#adSearchBoxBtn").hide();
	
	if(skin == "skin3"){
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		jQuery("#autoSaveArea").hide();
	}
	
}

function loadMDNViewToolBar(){
	
	var menuBarOpt = {
			mode : "mdnview",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	var basicGroup1;
	if(skin != "skin3"){
	  basicGroup1 = [
	 	         {type: "B" ,
	 	        Item : new MenuItem("recall",
	 			"item_mail_recall",
	 			mailMsg.mail_mdn_recall, "B", recallMsg,false)},
	 			 {type: "B" ,
		 	        Item : new MenuItem("golist",
		 			"item_addr_import",
		 			comMsg.comn_list, "B", goMdnList, false)}
	 			
	 		]; 	
	
	}else{
		basicGroup1 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("recall",
		  	 			"item_mail_recall",
		  	 			mailMsg.mail_mdn_recall, "B", recallMsg,false)},
		  	 			 {type: "B" ,
		  		 	        Item : new MenuItem("golist",
		  		 			"item_addr_import",
		  		 			comMsg.comn_list, "B", goMdnList, false)}
		  	 			
		  	 		]; 	
	}
	var menuList = [
	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
	            	groupItem : [basicGroup1]}
	               ];	
		
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	setAlWriteMode(false);
	jQuery("#adSearchBoxBtn").hide();
	jQuery("#siSearchBox").show();
}

function goMdnList(){
	mailControl.reloadMDNList();
}

function loadSendToolBar(){
	
	var menuBarOpt = {
			mode : "sendview",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	 	         {type: "B" ,
	 	        Item : new MenuItem("address",
	 			"item_mail_addaddr",
	 			mailMsg.mail_addradd, "B", viewAddSendAddr,false)}
	 		]; 	
 	
	var menuList = [
	               {name:mailMsg.mail_send_menu,id:"basic",initOn:true,linkFunc:changeToolbarMode,
	            	groupItem : [basicGroup1]}
	               ];	
		
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	setAlWriteMode(false);
	jQuery("#siSearchBox").show();
	if(skin == "skin3"){
		jQuery("#autoSaveArea").hide();
	}
}

function viewAddSendAddr(){
	setSendAddressList();
	viewAddAddrModal();
}

function setAddressList(list){
	registAddrList = list;
}
function viewAddAddrModal(){	
	var popOpt = clone(popupOpt);
	popOpt.btnList = [{name:comMsg.comn_confirm,func:addAddrList}];
	popOpt.minHeight = 300;
	popOpt.minWidth = 500;
	popOpt.top = 150;
	popOpt.openFunc = function(){		
		jQuery("div#addAddrListContents table").css("width","100%");		
	};
	popOpt.closeFunc = function(){
		jQuery("div#addAddrListContents table").css("width","");
	};
	
	jQuery("#addAddrPop").jQpopup("open",popOpt);	
	jQuery("table#addAddrTable tr").remove();
	
	var addtable = jQuery("#addAddrTable");
	
	var addrList = registAddrList;	
	for ( var i = 0; i < addrList.length; i++) {
		if(jQuery.trim(addrList[i]) != ""){		
			var name = get_name(addrList[i]);
			var email = get_email(addrList[i]);		
			var content = "<tr>"
				+"<td style='text-align:center'><input type='checkbox' name='addAddrEmail' value='"+email+"'/></td>"
				+"<td><input type='text'  name='addAddrName' class='IP100' value='"+name+"'/></td>"
				+"<td>"+email+"</td>"
				+"<td><div id='groupSelect"+i+"'></div></td>"
				+"</tr>";	
		addtable.append(jQuery(content));
		}
	}	
	jQuery("input[name=addAddrName]").addClass("IP100");	
	document.addAddrForm.allchk.checked = false;
	
	var groupList2 = [];
	AddressBookService.getJSonPrivateGroupList(
			{callback:function(addressList){
				groupList2.push({index:addrMsg.addr_common_label_001,value:"0"});
				for(var i=0 ; i<addressList.length; i++){
					groupList2.push({index:addressList[i].name,value:addressList[i].id});
				}
				
				for(var k = 0; k < addrList.length; k++){
					jQuery('#groupSelect'+k).selectbox({selectId:"groupSeqNm"+k,selectFunc:"",width:100,height:50},"",groupList2);
				}
				
				//jQuery("#groupSelect_selectMain").css("width","224px");
				
			}}
	);
	
}
var memberStateArray = [];
var memberDecideArray = [];

function addAddrList(){	
	var f = document.addAddrForm;
	var emails = f.addAddrEmail;
	var names = f.addAddrName;
	var memberArray = [];
	
	var groupSeqs;
	var isChecked = false;
	var addrList = [];
	var addrAddType;
	
	if(!checkedCnt(emails)){
		alert(comMsg.comn_error_001);
		return;
	}	
	if(emails.length > 0){
		
			
			for(var i=0 ; i < emails.length; i++){
				if(emails[i].checked){
					if(jQuery.trim(names[i].value) == ""){					
						alert(settingMsg.conf_userinfo_msg_01);
						names[i].focus();
						return;
					}
					if(checkInputName(names[i],2,64,true)){
						groupSeqs = jQuery("#groupSeqNm"+i);
						memberArray.push({memberName:names[i].value,memberEmail:emails[i].value,groupSeq:groupSeqs.val()});						
					}else{
						return;
					}
				}
			}
			
			AddressBookService.checkDupEmail(memberArray,
					{callback:function(data){
						for(var k=0 ; k<data.size(); k++){
							memberStateArray.push({memberName:data[k].memberName,
												  memberEmail:data[k].memberEmail,
												  groupSeq:data[k].groupSeq,
												  dupCnt:data[k].dupCnt});								
						}
						
						decideAddType();
					}}
			);
		
	} else {
		groupSeqs = jQuery("#groupSeqNm0");
		if(emails.checked){
			if(jQuery.trim(names.value) == ""){
				alert(settingMsg.conf_userinfo_msg_01);
				names.focus();
				return;
			}
			
			if(checkInputName(names,2,64,true)){				
				memberArray.push({memberName:names.value,memberEmail:emails.value,groupSeq:groupSeqs.val()});
				AddressBookService.checkDupEmail(memberArray,
						{callback:function(data){
							for(var k=0 ; k<data.size(); k++){
								memberStateArray.push({memberName:data[k].memberName,
													  memberEmail:data[k].memberEmail,
													  groupSeq:data[k].groupSeq,
													  dupCnt:data[k].dupCnt});								
							}
							decideAddType();
						}}
				);
			}else{
				return;
			}
			}
	}
		
}
function decideAddType(){
	var addrObj = memberStateArray.pop();
	var popOpt = clone(popupOpt);
		
	if(!addrObj){
		if(parseInt(memberDecideArray.length) == 0){
			return;
		}
		copyMailMember(memberDecideArray);
		return;
	}
	
	if(parseInt(addrObj.dupCnt) > 0){
		
		jQuery("#dupEmailAddress").empty();
		jQuery("#dupEmail").empty();
		jQuery("#dupName").empty();
		jQuery("#dupQuestion").empty();
		jQuery("#dupEmailAddress").append(addrMsg.addr_add_type_12);
		jQuery("#dupEmail").append(addrObj.memberEmail);
		jQuery("#dupName").append(addrObj.memberName);
		popOpt.hideCloseBtn = true;
		popOpt.minHeight = 100;
		popOpt.minWidth = 470;
		popOpt.openFunc = function(){};
		popOpt.closeFunc = function(){};
				
		if(parseInt(addrObj.dupCnt) == 1){
			jQuery("#dupQuestion").append(addrMsg.addr_add_type_10);								
			popOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("overWrite",addrObj);}},
								   {name:addrMsg.addr_add_type_07,func:function(){addrWrite("addWrite",addrObj);}},
								   {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",addrObj);}}
								   ];			
		}
		
		if(parseInt(addrObj.dupCnt) > 1){
			jQuery("#dupQuestion").append(msgArgsReplace(addrMsg.addr_add_type_11,[addrObj.dupCnt]));
			popOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("addWrite",addrObj);}},
			                       {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",addrObj);}}
			                       ];
		}		
		
		if(parseInt(memberStateArray.length) > 0){
			popOpt.btnList.push({name:addrMsg.addr_add_type_09,func:function(){addrWrite("cancle",addrObj);}});
			popOpt.minWidth = 550;
		}
		
		jQuery("#addAddrConfirm").jQpopup("open",popOpt);
		jQuery("#addAddrConfirm_px.btn_X").hide();
		
	}else{
		memberDecideArray.push({memberName:addrObj.memberName,memberEmail:addrObj.memberEmail,groupSeq:addrObj.groupSeq,addrAddType:"addWrite"});
		setTimeout('decideAddType()', 100);
	}
}
function closeAddAddrConfirm(){
	jQuery("#dupEmailAddress").empty();
	jQuery("#dupEmail").empty();
	jQuery("#dupName").empty();
	jQuery("#dupQuestion").empty();		
	jQuery("#addAddrConfirm").jQpopup("close");

}
function addrWrite(type,data){
	
	if(type == "overWrite"){
		closeAddAddrConfirm();
		memberDecideArray.push({memberName:data.memberName,memberEmail:data.memberEmail,groupSeq:data.groupSeq,addrAddType:type});
		alert(addrMsg.addr_add_type_13);
		setTimeout('decideAddType()', 100);
	}else if(type == "addWrite"){
		 closeAddAddrConfirm();
		memberDecideArray.push({memberName:data.memberName,memberEmail:data.memberEmail,groupSeq:data.groupSeq,addrAddType:type});
		alert(addrMsg.addr_add_type_14);
		setTimeout('decideAddType()', 100);
	}else if(type == "noWrite"){
		 closeAddAddrConfirm();
		setTimeout('decideAddType()', 100);
		
	}else if(type == "cancle"){
		if(confirm(addrMsg.addr_add_type_17)){
			closeAddAddrConfirm();
			alert(addrMsg.addr_add_type_16);
			copyMailMemberCancle(memberDecideArray);
		}else{
				
		}
	}	
}
function copyMailMemberCancle(arrayObj){
	AddressBookService.copyMailMember(arrayObj,{
		callback:function(data){
			if(data.isSuccess){
				memberStateArray = [];
				memberDecideArray = [];
			}else{
				alert(addrMsg.addr_error_msg_01);
				memberStateArray = [];
				memberDecideArray = [];
			}
		}
	});
	jQuery("#addAddrPop").jQpopup("close");
	jQuery("#addAddrPop_p").hide();
}

function copyMailMember(arrayObj){
	
	AddressBookService.copyMailMember(arrayObj,{
		callback:function(data){
			if(data.isSuccess){
				alert(addrMsg.addr_add_type_15);
				memberStateArray = [];
				memberDecideArray = [];
			}else{
				alert(addrMsg.addr_error_msg_01);
				memberStateArray = [];
				memberDecideArray = [];
			}
		}
	});	
	jQuery("#addAddrPop").jQpopup("close");
	jQuery("#addAddrPop_p").hide();
}

function addAddrResult(data, textStatus){
	if(textStatus == "success"){
		if(data.result == "success"){
			alert(mailMsg.alert_addr_add);
		} else {
			if(data.errMsg){
				alert(data.errMsg);
			} else {
				alert(mailMsg.error_addr_add);
			}
		}
	} else {
		alert(mailMsg.error_addr_add);
	}
	jQuery("#addAddrPop").jQpopup("close");
}

function getMessgesInfo(){
	if(LayoutInfo.mode == "list" || LayoutInfo.mode == "mdnlist"){
		return getListMessgesInfo();
	} else if(LayoutInfo.mode == "read"){
		return getReadMessgesInfo();
	}
}