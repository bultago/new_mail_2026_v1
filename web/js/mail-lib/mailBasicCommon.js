var treeOpt = {

	 	//Style Class Define
	 	nodeClass : "tnode",
	 	childNodeClass : "tchild-node",
	 	closefolderClass : "tcfolder",
	 	sharedfolderClass : "tsfolder",
	 	openfolderClass : "tofolder",
	 	closeChildClass : "tcChild",
	 	openChildClass : "toChild",
	 	emptyChildClass : "emChild",
	 	nodeLinkClass : "tcnodelink",
	 	nodeConfirmClass : "tcconfirmIcon",
	 	nodeCancelClass : "tccancelIcon",	
	 	blankImgSrc : "/design/common/image/blank.gif",

	 	//menuUse [true / false]
	 	nodeMenuUse : true,	
	 	
	 	//link function
	 	nodelink: nodeLink,
	 	
	 	addFolderFunc:addFolder,

	 	deleteFolderFunc:deleteFolder,
	 	
	 	modifyFolderFunc:modifyFolder,
	 	
	 	//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		outClass : "tcmenuOutClass",
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [	 			 		
	 			 		{name:mailMsg.menu_add,link:addChildNode},
	 			 		{name:mailMsg.menu_modfy,link:modifyNode},
	 			 		{name:mailMsg.menu_remove,link:removeNode},
	 			 		{name:mailMsg.menu_empty,link:cleanFolder},
	 			 		{name:mailMsg.menu_shared,link:viewSharedFolderSetting}
	 			 	]
	 	}	

	 };

var dndOption = {			
		helperID:"dragHelper",
		helperUseClass:"TM_dhelper",
		helperUnuseClass:"TM_dhelper_unuse",
		helperRefID:"msg_subject_",
		dropOverClass:"TM_dropareahover"
};

var mailOption = {
		mainLID : "m_contentMain"
};


var tagOption = {

		type : "tag",
		wrapClass : "TM_tag_tree_wrapper",
		drowFrame : "m_userTag",
		folderClass : "tagimg_base",
		linkFunc:listTagMessage,

		//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
						{name:mailMsg.menu_modfy,link:modifyTag},					
						{name:mailMsg.menu_remove,link:deleteTag}
	 			 	]
	 	}	
};

var searchFolderOption = {

		type : "sfolder",
		wrapClass : "TM_tree_wrapper",
		drowFrame : "m_userSearchFolder",
		folderClass : "tcfolder",
		linkFunc:listSearchFolder,
		//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [	 			 		
	 			 		{name:mailMsg.menu_modfy,link:modifySearchFolder},					
						{name:mailMsg.menu_remove,link:deleteSearchFolder}
	 			 	]
	 	}	
};

var sharedFolderOption = {

		type : "shfolder",
		wrapClass : "TM_tree_wrapper",
		drowFrame : "m_userSharringFolder",
		folderClass : "tcfolder",
		linkFunc:sharedFolderLink			
};

var flagOpt = {
	onFlagedImg : "/design/default/image/icon/ic_flag_on.gif",
	offFlagedImg : "/design/default/image/icon/ic_flag_off.gif",
	seenImg : "/design/default/image/icon/ic_mail_seen.gif",
	forwardImg : "/design/default/image/icon/ic_mail_fw.gif",
	replyImg : "/design/default/image/icon/ic_mail_reply.gif",
	unseenImg : "/design/default/image/icon/ic_mail_unseen.gif",
	attrImg : "/design/default/image/icon/ic_attach_file.gif",
	flagFunc : switchFlagFlaged	
};

var popupOpt = {
	closeName:comMsg.comn_close,
	btnClass:"btn_style3"			
};


var MailControl = Class.create({	
	initialize: function(){				
		this.currentFolder = "Inbox";
		this.sharedParam = {};
		this.searchParam = {};
		this.listParam = {};
	},
	setSharedParam:function(sharedParam) {
		this.sharedParam = sharedParam;
	},
	getMailListParam:function(){
		return this.listParam;
	},
	setSearchParam:function(searchParam) {
		this.searchParam = searchParam;
	},
	setCurrentFolder:function(folder){
		this.currentFolder = folder;
	},
	setListParam:function(param){
		this.listParam = param;
	},
	getCurrentFolder:function(){
		return this.currentFolder;
	},
	getViewMode:function(){		
		return this.viewMode;
	},
	movePage:function(param){				
		if(LayoutInfo.mode == "list"){
			this.loadMessageList(param);
		} else if(LayoutInfo.mode == "mdnlist"){
			this.loadMDNList(param);
		} else if(LayoutInfo.mode == "mdnread"){			
			this.viewMDNContent(param);
		}
	},	
	loadMessageList : function(param){
		setTimeout(function(){
			document.location = "/mail/listMessage.do?"+jQuery.param(param);
		},1);
	},
	reloadMessageList : function(){
		var _this = this;
		setTimeout(function(){
			if(LayoutInfo.mode == "list"){
				document.location.reload();
			} else {
				_this.loadMessageList(_this.getMailListParam());
			}
		},1);
	},	
	readMessage:function(param){
		setTimeout(function(){
			document.location = "/mail/readMessage.do?"+jQuery.param(param);			
		},1);
	},
	writeMessage:function(paramObj){
		setTimeout(function(){
			document.location = "/mail/writeMessage.do?"+jQuery.param(paramObj);
		},1);
	},
	sendMessage:function(param){
		var form = makeForm("writeSendForm","/mail/sendMessage.do","post",param);
		form.submit();
	},
	moveMessage:function(uids,fromFolders,toFolder){
		var _this = this;
		dwr.engine.setAsync(true);		
		MailMessageService.moveMessage(uids,fromFolders,toFolder, function(){	
			if(LayoutInfo.mode == "list"){
				document.location.reload();
			} else {
				_this.loadMessageList(_this.getMailListParam());
			} 			
		});
		
	},
	copyMessage:function(uids,fromFolders,toFolder){
		var _this = this;
		dwr.engine.setAsync(true);
		var param = {};
		param = this.getSharedFolderParam(param);
		
		MailMessageService.copyMessage(param.sharedFlag,
				param.sharedUserSeq,
				param.sharedFolderName,
				uids,fromFolders,toFolder, function(){			
				if(LayoutInfo.mode == "list"){
					document.location.reload();
				} else {
					_this.loadMessageList(_this.getMailListParam());
				} 
			});
	},
	goMessageList:function(){
		var param = {};		
		if(this.viewMode == "list"){
			param.folder = this.currentFolder;
			param.page = 1;			
		} else if(this.viewMode == "read"){
			param = this.listParam;			
		}		
		if(this.listMode == "mail"){
			this.loadMessageList(param);
		} else if(this.listMode == "mdnlist"){
			this.loadMDNList(param);
		}
	},
	searchMessage:function(param){		
		if(param){
			param = this.getSharedFolderParam(param);
		}		
		this.loadMessageList(param);
	},
	deleteMessages:function(uids, folders){
		var _this = this;
		dwr.engine.setAsync(true);
		MailMessageService.deleteMessage(uids,folders,{
			callback:function(){
				if(LayoutInfo.mode == "list"){
					document.location.reload();
				} else {
					_this.loadMessageList(_this.getMailListParam());
				}
			},
			async:true});
	},
	cleanMessages:function(uids, folders){
		var _this = this;
		dwr.engine.setAsync(true);
		MailMessageService.cleanMessage(uids,folders,{
			callback:function(){			
				if(LayoutInfo.mode == "list"){
					_this.reloadMessageList();
				} else {
					_this.loadMessageList(_this.getMailListParam());
				}
			},
			async:true});
	},
	switchMessagesFlags:function(uids, folders, flagType, used){
		
		dwr.engine.setAsync(true);
		
		var param = {};
		param = this.getSharedFolderParam(param);				
		MailMessageService.switchMessagesFlags(
				param.sharedFlag,
				param.sharedUserSeq,
				param.sharedFolderName,
				uids,folders,flagType, used,{
			callback:function(result){
				if(LayoutInfo.mode == "list"){					
					document.location.reload();
				}
			},
			async:true});
		
	},
	sortMessage:function(param,sortBy,sortDir){
		
		param.page = 1;
		param.sortBy = sortBy;
		param.sortDir = sortDir;		
		this.loadMessageList(param); 
	},
	removeAttachFile:function(uid, folder, part){
		var _this = this;
		var param = this.listParam;
		dwr.engine.setAsync(true);
		MailMessageService.removeAttachFile(uid,folder,part,function(nuid){
			param.uid = nuid;
			_this.readMessage(param);
		});
	},
	downLoadMessages:function(uids, folder){
		var param = {"folder":folder, "uids":uids};
		if(this.sharedFlag){
			param.sharedFlag = "shared";
			param.sharedUserSeq = this.listParam.sharedUserSeq;
			param.sharedFolderName = this.listParam.sharedFolderName;
		}
		jQuery("#tempForm").remove();
		var action = "/mail/downloadMessages.do";
		var target = "hidden_frame";
		var tempForm = jQuery("<form name='tempForm' id='tempForm' method='post' target='"+target+"' action='"+action+"'></form>");
		jQuery("body").append(tempForm);
		jQuery("#tempForm").append("<input type='hidden' name='folder' value='"+folder+"'>");
		jQuery("#tempForm").append("<input type='hidden' name='uids' value='"+uids+"'>");
		if(this.sharedFlag){
			jQuery("#tempForm").append("<input type='hidden' name='sharedFlag' value='"+"shared"+"'>");
			jQuery("#tempForm").append("<input type='hidden' name='sharedUserSeq' value='"+sharedUserSeq+"'>");
			jQuery("#tempForm").append("<input type='hidden' name='sharedFolderName' value='"+sharedFolderName+"'>");
		}
		jQuery("#tempForm").submit();
	},
	loadMDNList:function(param){
		setTimeout(function(){
			document.location = "/mail/listMDNResponses.do?"+jQuery.param(param);
		},1);
	},
	reloadMDNList : function(){
		var param = this.listParam;
		setTimeout(function(){
			document.location = "/mail/listMDNResponses.do?"+jQuery.param(param);
		},1);
	},
	viewMDNContent:function(param){
		setTimeout(function(){
			document.location = "/mail/viewMDNResponses.do?"+jQuery.param(param);
		},1);
	},
	recallMessage:function(param){
		setTimeout(function(){
			document.location = "/mail/recallMessage.do?"+jQuery.param(param);
		},1);
	},
	getWriteEmailAddressList:function(isNotOrgSearch){
		var addrList =  this.userAddrList;
		if(!addrList){
			var _this = this;
			dwr.engine.setAsync(true);
			MailMessageService.getMailAdressList(isNotOrgSearch,function(listObj){				
				_this.setUserAddrList(listObj.addrs);
				addrList = listObj.addrs;
								
			});
		}				
		return addrList;
	},
	setUserAddrList:function(list){
		this.userAddrList = list;
	},
	getSharedFolderParam:function(param) {
		if(this.sharedParam.isShared){
			param.sharedFlag = "shared";
			param.sharedUserSeq = this.sharedParam.sharedUserSeq;
			param.sharedFolderName = this.sharedParam.sharedFolderName;
		} else {
			param.sharedFlag = "user";
			param.sharedUserSeq = "0";
			param.sharedFolderName = "";
		}
		return param;
	},
	getSearchParam:function(param) {
		if(this.searchParam.keyWord != ""){
			if(LayoutInfo.mode != "mdnlist" && LayoutInfo.mode != "mdnread"){
				param.keyWord = this.searchParam.keyWord;
			} else {
				param.pattern = this.searchParam.keyWord;
			}
			if (this.searchParam.adv == "on") {
				param.adv = "on";
				param.fromaddr = this.searchParam.fromaddr;
				param.toaddr = this.searchParam.toaddr;
				param.category = this.searchParam.category;
				param.sdate = this.searchParam.sdate;
				param.edate = this.searchParam.edate;	
			}
		}
		return param;
	}
});

function mainInitFunc(){		
	folderControl = new FolderControl();
	mailControl = new MailControl();	
	tagControl = new TagControl(tagOption);	
	searchFolderControl = new SearchFolderControl(searchFolderOption);	
	dndManager = new DnDManager(dndOption);
}

function reloadListPage(){
	mailControl.reloadMessageList();
}

function reloadMDNListPage(){
	mailControl.reloadMDNList();
}

function sortMessage(sortBy, sortDir){
	
	var param = {};
	param.sortBy = sortBy;
	param.sortDir = sortDir;
	param.folder = jQuery("#folderName").val();
	param.page = jQuery("#page").val();
	param.flag = jQuery("#flag").val();
	
	param = mailControl.getSearchParam(param);
	param = mailControl.getSharedFolderParam(param);
	
	mailControl.loadMessageList(param);
}

function movePage(page){
	
	var param = {};

	if((LayoutInfo.mode != "mdnlist") && (LayoutInfo.mode != "mdnread")){
		param = mailControl.getMailListParam();
		param.folder = jQuery("#folderName").val();
		param.flag = jQuery("#flag").val();		
		param = mailControl.getSharedFolderParam(param);
	} else if(LayoutInfo.mode == "mdnread"){
		param.uid = jQuery("#uid").val();
		param.mdnlistpage = jQuery("#mdnlistpage").val();
		param.mdnlistpattern = jQuery("#mdnlistpattern").val();
	}
	
	param = mailControl.getSearchParam(param);
	param.page = page;
	param.sortBy = jQuery("#sortBy").val();
	param.sortDir = jQuery("#sortDir").val();
	mailControl.movePage(param);
}

function listPage(){
	mailControl.loadMessageList(mailControl.getMailListParam());
}

function goWriteSecureMail(){
	goWrite("expressE");
}

function goWrite(secureWriteMode){
	secureWriteMode = (secureWriteMode)?secureWriteMode:"normal";	
	if(isPopupWrite){
		try{
		if(popWriteWin && 
			popWriteWin.isWriteModify() && 
			!confirm(mailMsg.confirm_escapewrite))return;
		} catch(e){}
	} else if(!checkEscapeWriteMode()){return;}
	jQuery("#mailSearchCancel").hide();
	
	var dfolderUnseen = jQuery("#mf_Drafts_newCnt").data("unseen");	
	var isGoDrafts = false;	
	if(isWriteNoti && Number(dfolderUnseen) > 0){
		if(confirm(mailMsg.confirm_draftswrite)){
			isGoDrafts = true;
		}
	}
	
	if(isGoDrafts){
		goFolder("Drafts");	
	} else {
		var paramObj = {wtype:"send",secureWriteMode:secureWriteMode};
		if(!isPopupWrite){
			mailControl.writeMessage(paramObj);			
		}else {
			popupWriteLoad(paramObj);
		}
	}
}

function goWriteLoad(paramObj){
	if(!isPopupWrite){
		document.location = "/mail/writeMessage.do?"+jQuery.param(paramObj);
	} else {
		popupWriteLoad(paramObj);
	}
}

function goFolder(folderName,flag){
	/*
	if(LayoutInfo.mode == "write" && ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
	setStoreScrollHeight(0);
	if(!checkEscapeWriteMode()){return;}	
	
	var param = {"folder":folderName};
	if(flag){
		param.flag = flag;
	}
	mailControl.loadMessageList(param);
}

function goMailHome(){
	this.location = "/mail/shome.do?dummy="+makeRandom();
}

function searchMessage(addr){
	var folderName = mailControl.getCurrentFolder();
	var keyWord;
	var checkLength = 3;
	if(addr){
		keyWord = addr;
	} else {		
		keyWord = jQuery.trim(jQuery("#skword").val());		
		if(jQuery.trim(keyWord) == ""){
			alert(mailMsg.alert_search_nostr);
			$('skword').focus();
			return;
		}
		
		if(validateHan(keyWord)){
			checkLength = 4;
		}		
		
		if (!checkInputText($("skword"), checkLength, 255, true)){
			return false;
		}
		
		if(!checkInputSearch($("skword"),checkLength,255,true)){
			return;
		}
	}
	
	if(LayoutInfo.mode != "mdnlist" && LayoutInfo.mode != "mdnread"){
		var param = {"folder":folderName, "keyWord":keyWord};
		mailControl.searchMessage(param);
	} else {
		var param = {"pattern" :keyWord};
		if(LayoutInfo.mode == "mdnlist"){
			mailControl.loadMDNList(param);
		} else if(LayoutInfo.mode == "mdnread"){
			param.uid = $("uid").value;
			param.mdnlistpage = jQuery("#mdnlistpage").val();
			param.mdnlistpattern = jQuery("#mdnlistpattern").val();
			mailControl.viewMDNContent(param);
		}		
	}
}

function adSearchMessage(){	
	var folderName = $('adSfolder').value;
	var keyWord = jQuery.trim($('sdkeyWord').value);
	var from = jQuery.trim($('adFrom').value);
	var to = jQuery.trim($('adTo').value);
	var condition = $('adCondition').value;
	var sdate = $('adStartDate').value;
	var edate = $('adEndDate').value;
	var checkLength = 3;
	
	if(validateHan(from)){checkLength = 4;}
	if(from != "" && !checkInputSearch($("adFrom"),checkLength,255,true)){return;}
	if(validateHan(to)){checkLength = 4;}
	if(to != "" && !checkInputSearch($("adTo"),checkLength,255,true)){return;}
	checkLength = 3;
	if(sdate != "" || edate !== ""){
		if(!checkDatePeriod(sdate,edate)){
			return;
		}	
		sdate += "-00-00-00";
		edate += "-23-59-59";
	}
	
	if(from == "" && 
			to == "" &&
			sdate == "" &&
			edate == "" &&
			jQuery.trim(keyWord) == ""){
		alert(mailMsg.alert_search_nostr);
		$('sdkeyWord').focus();
		return;
	}
	
	if(jQuery.trim(keyWord) != ""){
		
		if(validateHan(keyWord)){
			checkLength = 4;
		}			
	
		if(!checkInputSearch($("sdkeyWord"),checkLength,255,true)){
			return;
		}
	}
	
	$("skword").value = "";
	
	var searchFolderFlagObj = $('adsearchForm').searchFolderFlag;
	var searchFolderFlag = "";
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].checked) {
			searchFolderFlag = searchFolderFlagObj[i].value;
			break;
		}
	}
	
	var param = {};
	param.adv = "on";
	param.listType = "mail";
	param.folder = folderName;
	param.keyWord = keyWord;
	param.fromaddr = from;
	param.toaddr = to;
	param.category = condition;
	param.sdate = sdate;
	param.edate = edate;
	param.flag = searchFolderFlag;

	mailControl.searchMessage(param);
}

function cancelSearch(){
	var folderName = mailControl.getCurrentFolder();	
	if(LayoutInfo.mode != "mdnlist" && LayoutInfo.mode != "mdnread"){
		folderName = (folderName == "all")?"Inbox":folderName;						
		goFolder(folderName);
	} else {
		var param = {};
		if(LayoutInfo.mode == "mdnlist"){
			mailControl.loadMDNList(param);
		} else if(LayoutInfo.mode == "mdnread"){
			param.uid = $("uid").value;
			param.mdnlistpage = jQuery("#mdnlistpage").val();
			param.mdnlistpattern = jQuery("#mdnlistpattern").val();
			mailControl.viewMDNContent(param);
		}		
	}	
}

function setSearchStatus(keyWord, advancedSearch) {
	if (keyWord != "") jQuery("#mailSearchCancel").show();
	if (advancedSearch != "on") jQuery("#skword").val(keyWord);
}

function goMDNList(){	
	var param = {page : 1};		
	mailControl.loadMDNList(param);	
}
function viewQuickList(type,isAllFolder,isMyFolder){
	
	if(!checkEscapeWriteMode()){
		return;
	}
	
	var param = {};
	
	var folderName = "";
	
	if(isAllFolder){
		folderName = "all";
	} else {
		folderName = mailControl.getCurrentFolder();
	}
	
	param.folder = folderName;
	param.listType = "mail";
	param.page = "1";
	
	if(type == "flaged"){
		param.flag = "F";
	} else if(type == "unseen"){
		param.flag = "U";
	} else if(type == "seen"){			
		param.flag = "S";
	} else if(type == "myself"){
		param.flag = "L";
	} else if(type == "attached"){
		param.flag = "T";
	} else if(type == "reply"){
		param.flag = "A";
	} else  if(type == "today"){
		param.adv = "on";
		param.sdate = "TODAYS";
		param.edate = "TODAYE";
	} else  if(type == "yesterday"){
		param.adv = "on";
		param.sdate = "YESTERDAYS";
		param.edate = "YESTERDAYE";
	}	
	
	if(!isMyFolder){
		param = mailControl.getSharedFolderParam(param);
	}
	
	mailControl.loadMessageList(param);
}

function readMessage(folderName,uid){
	if(isMsie)
		setStoreScrollHeight(jQuery("html").scrollTop());
	else
		setStoreScrollHeight(jQuery("body").scrollTop());
	
	var param = {};
	param = mailControl.getMailListParam();
	param.folder = folderName;
	param.uid = uid;

	mailControl.readMessage(param);
}

function reloadMailPage(){	
	mailControl.reloadMessageList();		
}

function goSharedFolder(folderName,userSeq){
	/*
	if(LayoutInfo.mode == "write" && ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
	
	if(!checkEscapeWriteMode()){return;}
	jQuery("#mailSearchCancel").hide();
	
	var param = {"folder" : folderName, 
			"sharedFlag":"shared", 
			"sharedUserSeq":userSeq,
			"sharedFolderName":folderName};
	
	jQuery("#skword").val("");
	
	mailControl.loadMessageList(param);
}

function resizeLeftMenuSize(){
	if(IS_LMENU_USE){
		//jQuery("#leftSideMenu").css("position","relative");
	}
}

function readHomeMessage(folderName,uid){
	var param = {"folder" : folderName, "uid":uid};
	mailControl.readMessage(param);	
}

function addrGoWrite(addr){
	var uaddr = decodeURIComponent(addr);	
	var param = {wtype:"send",to:uaddr};
	goWriteLoad(param);		
}

function chgWorkMode(){return true;}