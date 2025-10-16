var LayoutInfo = {mode:""};
var mainSplitter, contentSplitter, mainTreeMap;
var folderControl, mailControl, mailListControl, dndManager,tagControl, searchFolderControl,mailReadControl,mailRealtionListControl;
var menuBar,paneControl; 
var paneMode;
var popWriteWin;

var FolderControl = Class.create({	
	initialize: function(){
		this.ufolderList = null;
		this.dfolderList = null;
		this.quotaInfo = null;
	},	
	getQuotaInfo:function(){
		return this.quotaInfo;
	},
	getFolderList:function(type){
		var dlist = [];
		this.dfolderList.each(function(f){			
			dlist[dlist.length] =  {"id":f.id,
					"depth": f.depth,
					"fname":f.fullName,					
					"name":f.name,
					"ename":f.encName};
		});
		if(type){
			if(type == "dfolder"){
				return dlist;
			} else if(type == "ufolder"){
				return this.ufolderList;
			}			
		} else {
			return jQuery.merge(dlist,this.ufolderList);
		}
	},
	updateFolderInfo: function(exeFunction){
		var _this = this;
		
		dwr.engine.setAsync(true);
		jQuery("#m_userFolderTree").loadbar("m_leftMenu");		
		MailFolderService.getMailFolderInfo(function(leftMenuInfo){	
			_this.updateQuota(leftMenuInfo.quotaInfo);
			_this.updateDefaultFolder(leftMenuInfo.defaultFolders);
			_this.updateUserTree(leftMenuInfo.userFolders);
			_this.getSharedFolderList();			
			
			if(exeFunction){
				exeFunction();
			}
			jQuery("#m_leftMenu").css("overflow","auto");
			
			//PageMainLoadingManager.completeWork("folder");
			
		});	
		
	},
	updateFolderCountInfo: function(exeFunction){
		var _this = this;
		jQuery.post("/mail/folderInfo.do", {info:true}, function(data, textStatus){
				if(textStatus == "success"){
					_this.updateFolderCount(data);
				}
				if(exeFunction){
					exeFunction();
				}
		}, "json");
	},
	updateFolderCount:function(finfo){
		var folderList = finfo.folderInfo;
		var newMailCnt;
		var linkObj;
		var _this = this;		
		jQuery.each(folderList,function(){			
			var folder = this;
			
			
				
			if(folder.fullName != "Sent"){				
				var fname = folder.fullName;				
				fname = getFolderNameEscape(fname);								
				
				newMailCnt = jQuery("#mf_"+fname+"_newCnt");				
				newMailCnt.data("unseen",folder.unseenCnt);
				newMailCnt.empty();
				if(folder.unseenCnt > 0){
					linkObj = jQuery("<a href='#'></a>");
					linkObj.append(folder.unseenCnt);
					linkObj.addClass("TM_unseenLink");
					linkObj.click(function(e){
						goFolder(folder.fullName,"U");						
					});
					
					newMailCnt.append("(");
					newMailCnt.append(linkObj);
					newMailCnt.append(")");
				}
				if(folder.child && folder.child.length > 0){
					_this.updateFolderCount({folderInfo:folder.child});
				}
			}
			
			if((LayoutInfo.mode == "list" || LayoutInfo.mode == "read") 
					&& currentFolder == folder.fullName){
				updateWorkReadCount(currentFolderViewName,
						folder.totalCnt,folder.unseenCnt);
			}
		});	
		folderList = linkObj = null;
	},
	updateFolderListInfo: function(exeFunction){
		var _this = this;
		MailFolderService.getMailFolderInfo(function(leftMenuInfo){
			_this.dfolderList = leftMenuInfo.defaultFolders;
			_this.parseUserFolderNode(leftMenuInfo.userFolders);
			
			if(exeFunction){
				exeFunction();
			}
		});	
		
	},	
	parseUserFolderNode:function(list,ufolders){		
		for(var i = 0 ; i < list.length ; i++){
			ufolders[ufolders.length] = {	
					"dropid" : "#tfwrap_m_userFolderTree_" +list[i].id,
					"id":list[i].id,
					"depth": list[i].depth,
					"fname":list[i].folderName,
					"name":list[i].name,
					"ename":list[i].encName};
			
			if(list[i].child){		
				this.parseUserFolderNode(list[i].child,ufolders);
			}		
		}
	},
	updateQuota : function(quotaInfo){
		var quotaPersent = quotaInfo.percent;
		jQuery("#ml_quotaUseAge").html(quotaInfo.usage);
		jQuery("#ml_quotaTotal").html(quotaInfo.limit);		
		jQuery("#ml_quotaGraphBar").css("width",((Number(quotaPersent) > 100)?100:quotaPersent)+"%");
		this.quotaInfo = quotaInfo;
	},
	updateDefaultFolder : function(folderList){
		this.dfolderList = folderList;
		var isOuotaOverExist = false;
		this.dfolderMap = {};		
		if(folderList && folderList.length > 0){			
			folderList.each(function(folder) {
				if(folder.fullName == "Quotaviolate"){
					isOuotaOverExist = true;
				}				
				var unseenArea = jQuery("#mf_"+folder.fullName+"_newCnt");
				unseenArea.empty();
				
				jQuery("#mf_"+folder.fullName+"_newCnt").data("unseen",folder.unseenCnt);								
				
				if(folder.fullName != "Sent" && folder.unseenCnt > 0){					
					var unseenLink = jQuery("<a href='javascript:;'></a>");
					unseenLink.append(folder.unseenCnt);
					unseenLink.addClass("TM_unseenLink");
					unseenLink.click(function(e){
						goFolder(folder.fullName,"U");						
					});					
					unseenArea.append("(");
					unseenArea.append(unseenLink);
					unseenArea.append(")");
					
				}	
				
				if((LayoutInfo.mode == "list" || LayoutInfo.mode == "read") 
						&& currentFolder == folder.fullName){
					updateWorkReadCount(currentFolderViewName,
							folder.totalCnt,folder.unseenCnt);
				}
			});		
		}
		if(isOuotaOverExist){
			jQuery("#df_Quotaviolate").show();
		} else {
			jQuery("#df_Quotaviolate").hide();
		}
	},
	updateUserTree:function(folders){
		if(folders && folders.length > 0){			
			folders.each(function(folder) {
				if((LayoutInfo.mode == "list" || LayoutInfo.mode == "read") 
						&& currentFolder == folder.fullName){
					updateWorkReadCount(currentFolderViewName,
							folder.totalCnt,folder.unseenCnt);
				}
			});
		}		
		
		jQuery("#m_userFolderTree").empty();
		var mainTree = new DFTree("m_userFolderTree",treeOpt,folders);
		mainTree.creatTree();
		this.ufolderList = mainTree.getNodeIDList();
		mainTreeMap = {"m_userFolderTree":mainTree};		
		updateUserFolderDnD();
		
		setToggleTreeMenu("m_userFolderTree");
	},
	getDropFolderList : function(){
		var dlist;
		
		if(this.ufolderList){
			dlist = [];
			dlist[0] = "#df_Inbox";
			dlist[1] = "#df_Sent";
			dlist[2] = "#df_Drafts";
			dlist[3] = "#df_Reserved";
			dlist[4] = "#df_Spam";
			dlist[5] = "#df_Trash";
			this.ufolderList.each(function(f){			
				dlist[dlist.length] = f.dropid;
			});
		} else {
			var _this = this;
			var ufolders = [];
			MailFolderService.getMailFolderInfo(function(leftMenuInfo){				
				_this.parseUserFolderNode(leftMenuInfo.userFolders,ufolders);
			});
			
			ufolders.each(function(f){			
				dlist[dlist.length] = f.dropid;
			});
			this.ufolderList = ufolders;
		}
		
		return dlist;
	},
	emptyFolder:function(folderName,exeFunction){		
		var _this = this;
		
		dwr.engine.setAsync(true);
		MailFolderService.emptyFolder(folderName,function(result){
			if(result == "success"){
				_this.updateFolderCountInfo();
			}
			
			if(exeFunction){
				eval(exeFunction);
			}			
		});	
	},
	addFolder:function(folderName){
		var _this = this;
		
		dwr.engine.setAsync(true);
		MailFolderService.addFolder(folderName,function(result){
			if(result == "success"){
				_this.updateFolderInfo();
			} else if(result == "exist"){
				alert(mailMsg.alert_folder_samename);
			}				
		});
	},
	deleteFolder:function(folderName,currentEqual){		
		var _this = this;
		
		dwr.engine.setAsync(true);
		MailFolderService.deleteFolder(folderName,function(){
			_this.updateFolderInfo();
			if(currentEqual){
				goFolder("Inbox");
			}
		});
	},
	modifyFolder:function(previousName, parentName, newName){
		var _this = this;
		
		dwr.engine.setAsync(true);
		MailFolderService.modifyFolder(previousName, parentName, newName,function(){
			_this.updateFolderInfo();				
		});
	},
	getSharedFolderList:function(){
		var _this = this;
		jQuery("#m_userSharringFolder").loadbar("m_leftMenu");
		MailFolderService.getSharringFolderList(function(folderList){
			_this.updateSharedFolderInfo(folderList);
			jQuery("#m_leftMenu").css("overflow","auto");
		});
	},
	updateSharedFolderInfo:function(folderList){
		jQuery("#m_userSharringFolder").empty();
		jQuery("#m_userSharringFolder").DSharedFolderTree(folderList,sharedFolderOption);		
		setToggleTreeMenu("m_userSharringFolder");		
		//PageMainLoadingManager.completeWork("shfolder");
	}
		
});

var FolderPopupControl = Class.create({	
	initialize: function(){
		this.ufolderList = null;
		this.dfolderList = null;
		this.quotaInfo = null;
	},	
	getQuotaInfo:function(){
		return this.quotaInfo;
	},
	getFolderList:function(type){
		var dlist = [];
		this.dfolderList.each(function(f){			
			dlist[dlist.length] =  {"id":f.id,
					"depth": f.depth,
					"fname":f.fullName,					
					"name":f.name,
					"ename":f.encName};
		});
		if(type){
			if(type == "dfolder"){
				return dlist;
			} else if(type == "ufolder"){				
				return this.ufolderList;
			}			
		} else {
			return jQuery.merge(dlist,this.ufolderList);
		}
	},
	updateFolderInfo: function(){
		if(opener.reloadMailPage){
			opener.reloadMailPage();
		}		
	},
	updateFolderCountInfo: function(exeFunction){
		this.updateFolderListInfo(exeFunction);
	},
	updateFolderListInfo: function(exeFunction){
		var _this = this;
		
		MailFolderService.getMailFolderInfo(function(leftMenuInfo){
			_this.dfolderList = leftMenuInfo.defaultFolders;
			_this.ufolderList = [];
			_this.parseUserFolderNode(leftMenuInfo.userFolders);
			_this.quotaInfo = leftMenuInfo.quotaInfo;
			
			if(exeFunction){
				exeFunction();
			}
		});	
		
	},
	parseUserFolderNode:function(list){		
		for(var i = 0 ; i < list.length ; i++){
			this.ufolderList[this.ufolderList.length] = {				
					"id":list[i].id,
					"depth": list[i].depth,
					"fname":list[i].fullName,
					"name":list[i].name,
					"ename":list[i].encName};
			
			if(list[i].child){		
				this.parseUserFolderNode(list[i].child);
			}
		}
	}		
});

function getCurrentFolderName(){
	return mailControl.getCurrentFolder;	
}

var MailPopupControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.currentFolder = "Inbox";
		this.sharedFlag = false;
		this.listParam = {};
		this.listMode = "mail";
		this.readParam = {};		
		this.viewMode = "read";
		this.userAddrList = null;		
	},
	setCurrentFolder:function(folder){
		this.currentFolder = folder;
	},
	getCurrentFolder:function(){
		return this.currentFolder;
	},
	getSharedFolderParam:function(param){
		if(this.sharedFlag){
			param.sharedFlag = "shared";
			param.sharedUserSeq = this.listParam.sharedUserSeq;
			param.sharedFolderName = this.listParam.sharedFolderName;
		} else {
			param.sharedFlag = "user";
			param.sharedUserSeq = "0";
			param.sharedFolderName = "";
		}
		return param;
	},
	getViewMode:function(){		
		return this.viewMode;
	},		
	readMessage:function(param){		
		if(param){
			this.readParam = clone(this.listParam);			
			this.readParam.folder = param.folder;
			this.readParam.uid = param.uid;
			if(param.viewImg){
				this.readParam.viewImg = param.viewImg;
			}			
			this.currentFolder = param.folder;
			
			if(this.sharedFlag){
				this.readParam.sharedFlag = "shared";
				this.readParam.sharedUserSeq = this.listParam.sharedUserSeq;
				this.readParam.sharedFolderName = this.listParam.sharedFolderName;
			}
		}
		this.readParam.method = "normal";
		this.viewMode = "read";		
		jQuery.makeBodyMask("popupMask");		
		ActionNotMaskLoader.loadAction(this.opt.mainLID,
				this.opt.readAction,
				this.readParam,function(){jQuery.removeBodyMask("popupMask");});
		LayoutInfo.mode = "read";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.readAction,this.readParam,"read");
	},
	reloadMessageList:function(){
		if(opener && opener.reloadMailPage){
			opener.reloadMailPage();
		}
		window.close();
	},
	writeMessage:function(param){
		try{
			if(opener && opener.goWriteLoad){
				opener.goWriteLoad(param);
				window.close();
			} else {
				document.location = "/dynamic/mail/writeMessage.do?wmode=popup&"+jQuery.param(param);
			}
		}catch(e){
			document.location = "/dynamic/mail/writeMessage.do?wmode=popup&"+jQuery.param(param);
		}
		
	},
	sendMessage:function(param){
		/*if(param){
			this.param = param;			
		}*/		
		jQuery.makeBodyMask("popupMask");
		jQuery("object").hide();
		ActionNotMaskLoader.loadAction(this.opt.mainLID,
				this.opt.sendAction,
				param,function(){
				jQuery.removeBodyMask("popupMask");
					jQuery("object").show();
				});
		
		LayoutInfo.mode = "send";
	},
	moveMessage:function(uids,fromFolders,toFolder){
		
		dwr.engine.setAsync(true);
		
		MailMessageService.moveMessage(uids,fromFolders,toFolder, function(){
			if(opener && opener.reloadMailPage){
				opener.reloadMailPage();
			}
			window.close();
		});
		
	},
	copyMessage:function(uids,fromFolders,toFolder){
		
		dwr.engine.setAsync(true);
		var param = {};
		param = this.getSharedFolderParam(param);
		
		MailMessageService.copyMessage(param.sharedFlag,
				param.sharedUserSeq,
				param.sharedFolderName,
				uids,fromFolders,toFolder, function(){			
				if(opener && opener.reloadMailPage){
					opener.reloadMailPage();
				}			
			});
	},
	deleteMessages:function(uids, folders){
		
		dwr.engine.setAsync(true);
		MailMessageService.deleteMessage(uids,folders,{
			callback:function(){
				if(opener && opener.reloadMailPage){
					opener.reloadMailPage();
				}
				window.close();				
			},
			async:true});
	},
	cleanMessages:function(uids, folders){
		
		dwr.engine.setAsync(true);
		MailMessageService.cleanMessage(uids,folders,{
			callback:function(){			
				if(opener && opener.reloadMailPage){
					opener.reloadMailPage();
				}
				window.close();		
			},
			async:true});
	},
	switchMessagesFlags:function(uids, folders, flagType, used){
		var param = {};
		param = this.getSharedFolderParam(param);				
		MailMessageService.switchMessagesFlags(
				param.sharedFlag,
				param.sharedUserSeq,
				param.sharedFolderName,
				uids,folders,flagType, used,{
			callback:function(result){	
				if(opener && opener.reloadMailPage){
					opener.reloadMailPage();
				}
			},
			async:true});
		
	},
	removeAttachFile:function(uid, folder, part){
		var _this = this;
		var vmode = this.viewMode;
		var param = this.readParam;
		dwr.engine.setAsync(true);
		MailMessageService.removeAttachFile(uid,folder,part,function(nuid){
			param.uid = nuid;			
			if(opener && opener.reloadMailPage){
				opener.reloadMailPage();
			}
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
	setSharedParam:function(sharedParam){
		this.sharedFlag = sharedParam.sharedFlag;
		this.listParam.sharedUserSeq = sharedParam.sharedUserSeq;
		this.listParam.sharedFolderName = sharedParam.sharedFolderName;
	},
	setMailListParam:function(param){
		this.listParam = param;
		this.readParam = param;
	},
	getMailListParam:function(){
		return this.listParam;
	}		
});

var TagControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;		
		this.param = null;		
		this.tagMap = new Hash();
		this.tagIdList = null;
	},
	updateTagInfo:function(){
		var _this = this;
		jQuery("#"+this.opt.drowFrame).loadbar("m_leftMenu");		
		MailTagService.getTagList(function(tagList){
			_this.updateTagList(tagList);
			jQuery("#m_leftMenu").css("overflow","auto");
		});
	},
	updateTagListInfo:function(){
		var _this = this;
		MailTagService.getTagList(function(tagList){
			_this.makeTagMap(tagList);
		});
	},
	updateTagList:function(tagList){		
		this.tagIdList = tagList;
		jQuery("#"+this.opt.drowFrame).empty();
		jQuery("#"+this.opt.drowFrame).DTagTree(tagList,this.opt);
		this.makeTagMap(tagList);
		//PageMainLoadingManager.completeWork("tag");		
		setToggleTreeMenu(this.opt.drowFrame);		
	},
	addTag:function(tagName,tagColor){
		var _this = this;
		MailTagService.addTag(tagName,tagColor,function(){
			_this.updateTagInfo();
		});
	},
	modifyTag:function(oldId,tagName,tagColor,func){
		var _this = this;
		MailTagService.modifyTag(oldId,tagName,tagColor,function(){
			_this.updateTagInfo();
			if(func){
				func();
			}
		});
	},
	deleteTag:function(tagIds){
		var _this = this;
		MailTagService.deleteTag(tagIds,function(){
			_this.updateTagInfo();
			mailControl.reloadMessageList();
		});
	},
	makeTagMap:function(tagList){
		this.tagIdList = tagList;
		var tempMap = new Hash();
		for ( var i = 0; i < tagList.length; i++) {
			tempMap.set(tagList[i].name,{id : tagList[i].id, color: tagList[i].color});			
		}
		this.tagMap = tempMap;
	},
	getTagMap:function(){
		return this.tagMap;
	},
	getDropTagList:function(){
		var drowTagList = [];
		for ( var i = 0; i < this.tagIdList.length; i++) {
			drowTagList.push("#tnwrap_"+this.tagIdList[i].id);			
		}		
		return drowTagList;		
	},
	getTagInfo:function(name){
		var _this = this;
		var info = this.tagMap.get(name);
		if(!info){
			MailTagService.getTagList(function(tagList){
				_this.makeTagMap(tagList);
				info = this.tagMap.get(name);
			});
		}		
		return info;
	},
	taggingMessage:function(isAdd,uids,folders,tagID){
		var _this = this;
		MailTagService.taggingMessage(isAdd,uids,folders,tagID, function(){
			mailControl.reloadMessageList();
		});
	}
	
});

var TagPopupControl = Class.create({	
	initialize: function(){		
		this.tagMap = new Hash();
	},	
	updateTagListInfo:function(){
		var _this = this;
		MailTagService.getTagList(function(tagList){
			_this.makeTagMap(tagList);
		});
	},	
	makeTagMap:function(tagList){
		var tempMap = new Hash();
		for ( var i = 0; i < tagList.length; i++) {
			tempMap.set(tagList[i].name,{id : tagList[i].id, color: tagList[i].color});			
		}
		this.tagMap = tempMap;
	},
	getTagMap:function(){
		return this.tagMap;
	},
	getTagInfo:function(name){
		return this.tagMap.get(name);
	},
	taggingMessage:function(isAdd,uids,folders,tagID){
		var _this = this;
		MailTagService.taggingMessage(isAdd,uids,folders,tagID, function(){
			if(opener.reloadMailPage){
				opener.reloadMailPage();
			}
		});
	}
	
});

function deleteTag(tagData){
	if(confirm(mailMsg.confirm_delete)){
		var ids = [tagData.id];	
		tagControl.deleteTag(ids);
	}
}

function modifyTag(tagData){
	
	var tagPopOpt = clone(popupOpt);
	tagPopOpt.btnList = [{name:comMsg.comn_modfy,func:saveTag}];
	tagPopOpt.minHeight = 100;
	tagPopOpt.minWidth = 250;
	tagPopOpt.closeFunc = function(){
		jQuery("#_seven_color_selecter").remove();
	};
	
	jQuery("#m_tagForm").jQpopup("open",tagPopOpt);		
	setTagForm(tagData.tagName,tagData.tagColor,tagData.id);
	jQuery("#tagColor").SevenColorPicker();
	
}

function setTagForm(name,color,oldId){
	$("tagName").value = name;
	$("tagColor").value = color;	
	$("oldTagId").value = oldId;	
}

function addTag(){
	var tagPopOpt = clone(popupOpt);
	tagPopOpt.btnList = [{name:comMsg.comn_add,func:saveTag}];
	tagPopOpt.minHeight = 100;
	tagPopOpt.minWidth = 250;
	tagPopOpt.closeFunc = function(){
		jQuery("#_seven_color_selecter").remove();
	};
	
	jQuery("#m_tagForm").jQpopup("open",tagPopOpt);	
	setTagForm("","#ffffff",-1);
	jQuery("#tagColor").SevenColorPicker();
	
}

function saveTag(){
	
	if(!checkFolderName("tag", $("tagName"))){
		return;
	}
	
	var tagName = jQuery.trim(jQuery("#tagName").val());
	var tagColor = jQuery("#tagColor").val();	
	var oldId = jQuery("#oldTagId").val();
	
	var tagMap = tagControl.getTagMap();
	var checkTag = tagMap.get(tagName);
	
	if(parseInt(oldId) > 0){
		tagControl.modifyTag(oldId,tagName,tagColor,function(){mailControl.reloadMessageList();});
	} else {
		if(checkTag){
			alert(mailMsg.alert_tag_samename);
			$("tagName").select();
			return;
		}
		tagControl.addTag(tagName,tagColor);
	}
	
	jQuery("#m_tagForm").jQpopup("close");	
}

function listTagMessage(nodeData){	
	var param = {
		folder : "all",
		listType : "tag",
		tagId : nodeData.id		
	};	
	if(chgWorkMode()){		
		mailControl.loadMessageList(param);
	}
	
	storeLinkId("link_tag_"+nodeData.id);
}

var SearchFolderControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.folderMap;
	},
	updateFolderInfo:function(){
		var _this = this;
		jQuery("#"+this.opt.drowFrame).loadbar("m_leftMenu");
		MailSearchFolderService.getFolderList(function(folderList){
			_this.updateFolderList(folderList);
			jQuery("#m_leftMenu").css("overflow","auto");
		});
	},
	updateFolderList:function(folderList){
		jQuery("#"+this.opt.drowFrame).empty();
		jQuery("#"+this.opt.drowFrame).DSearchFolderTree(folderList,this.opt);
		setToggleTreeMenu(this.opt.drowFrame);
		this.makeSFolderMap(folderList);
		//PageMainLoadingManager.completeWork("sfolder");
	},
	addFolder:function(folderName,folderQuery){
		var _this = this;
		MailSearchFolderService.addSearchFolder(folderName,folderQuery,function(){
			_this.updateFolderInfo();
		});
	},
	modifyFolder:function(oldId,folderName,folderQuery){
		var _this = this;
		MailSearchFolderService.modifySearchFolder(oldId,folderName,folderQuery,function(){
			_this.updateFolderInfo();
		});
	},
	deleteFolder:function(folderIds){
		var _this = this;
		MailSearchFolderService.deleteSearchFolder(folderIds,function(){
			_this.updateFolderInfo();
		});
	},
	makeSFolderMap:function(folderList){
		var tempMap = new Hash();
		for ( var i = 0; i < folderList.length; i++) {
			tempMap.set(folderList[i].name,{id : folderList[i].id,						
				name : folderList[i].name,
				encname : folderList[i].encname,
				userid : folderList[i].userid});
		}
		this.folderMap = tempMap;
	},
	getSFolderMap:function(){
		return this.folderMap;
	}
});


function deleteSearchFolder(nodeData){
	if(confirm(mailMsg.confirm_delete)){
		var ids = [nodeData.id];	
		searchFolderControl.deleteFolder(ids);
	}
}

function modifySearchFolder(nodeData){
	
	var query = nodeData.query;	
	var searchPopOpt = clone(popupOpt);
	searchPopOpt.btnList = [{name:comMsg.comn_confirm,func:saveSearchFolder}];
	searchPopOpt.minHeight = 250;
	searchPopOpt.minWidth = 550;
	searchPopOpt.openFunc = function(){
		setSearchFolderForm	(nodeData.name,nodeData.id,query);
	};	
	
	jQuery("#m_searchFolderForm").jQpopup("open",searchPopOpt);	
	
}

function addSearchFolder(){
	var searchPopOpt = clone(popupOpt);
	searchPopOpt.btnList = [{name:comMsg.comn_add,func:saveSearchFolder}];
	searchPopOpt.minHeight = 250;
	searchPopOpt.minWidth = 600;
	searchPopOpt.openFunc = function(){
		setSearchFolderForm	();
	};
	jQuery("#m_searchFolderForm").jQpopup("open",searchPopOpt);	
}

function saveSearchFolder(){
	
	var checkLength = 3;
	var pattern = jQuery("#searchFolderPattern").val();
	var from = $('searchFolderFrom').value;
	var to = $('searchFolderTo').value;
	
	
	if(!checkFolderName("sfolder", $("searchFolderName"))){return;}	
	if(validateHan(from)){checkLength = 4;}
	if(from != "" && !checkInputSearch($("searchFolderFrom"),checkLength,255,true)){return;}
	if(validateHan(to)){checkLength = 4;}
	if(to != "" && !checkInputSearch($("searchFolderTo"),checkLength,255,true)){return;}
	
	checkLength = 3;
	if(validateHan(pattern)){checkLength = 4;}
	if(pattern != "" && !checkInputSearch($("searchFolderPattern"),checkLength,255,true)){return;}
	
	if(from == "" && 
			to == "" &&			
			pattern == ""){
		alert(mailMsg.alert_search_nostr);
		$('searchFolderPattern').focus();
		return;
	}
	
	var folderName = jQuery.trim(jQuery("#searchFolderName").val());
	var sFolder = jQuery("#s2folder").val();
	var cond = jQuery("#searchFolderCondition").val();	
	var sfolderMap = searchFolderControl.getSFolderMap();
	var existFolderName = sfolderMap.get(folderName);
	var searchFolderFlagObj = $('searchFolderForm').searchFolderFlag;
	var searchFolderFlag = "";
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].checked) {
			searchFolderFlag = searchFolderFlagObj[i].value;
			break;
		}
	}
	var query = sFolder+"/%"+cond+"/%"+pattern+"/%"+from+"/%"+to+"/%"+searchFolderFlag;
	
	var oldId = jQuery("#oldSFolderId").val();
	
	if(oldId != ""){
		searchFolderControl.modifyFolder(oldId,jQuery.trim(folderName),query);
	} else {		
		if(existFolderName){
			alert(mailMsg.alert_folder_samename);
			$("searchFolderName").select();
			return;
		}
		searchFolderControl.addFolder(jQuery.trim(folderName),query);
	}
	
	jQuery("#m_searchFolderForm").jQpopup("close");	
}

function setSearchFolderForm(folderName,id, querys){
	if(!querys){querys = "";}
	var values = querys.split("/%");
	var sfolder = values[0];	
	var queryCondition = values[1];
	var pattern = values[2];
	var from = values[3];
	var to = values[4];
	var flag = values[5];
	
	if(folderName){
		$("searchFolderName").value = folderName;
	} else {
		$("searchFolderName").value = "";
	}
	var userFList = folderControl.getFolderList();
	jQuery("#s2folderSelect").empty();	
	jQuery("#s2folderSelect").folderSelectList("s2folder",userFList,sfolder,'s',checkSearchAllFolderMsg);
	
	
	var searchCondition = [];
	searchCondition.push({index:mailMsg.search_subject,value:"s"});
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_body,value:"b"});
	}
	searchCondition.push({index:mailMsg.search_attname,value:"af"});
	if(mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_attcontent,value:"ab"});
	}	
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_sbody,value:"sb"});
	}	
	searchCondition.push({index:mailMsg.search_sattname,value:"saf"});
	if(mailSearchConfig.bodySearch == "on" && mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_sattcontent,value:"sab"});
	}
	
	jQuery("#searchFolderConditionSelect").empty();
	jQuery("#searchFolderConditionSelect").selectbox({selectId:"searchFolderCondition",selectFunc:"",width:150},
			queryCondition,searchCondition);
	
	if(!from){from = "";}
	$("searchFolderFrom").value = from;
	
	if(!to){to = "";}
	$("searchFolderTo").value = to;
	
	if(!pattern){pattern = "";}	
	$("searchFolderPattern").value = pattern;
	
	if(!id){id = "";} 
	$("oldSFolderId").value = id;
	
	if (!flag){flag = "";}
	var searchFolderFlagObj = $('searchFolderForm').searchFolderFlag;
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].value == flag) {
			searchFolderFlagObj[i].checked = true;
			break;
		}
	}
	
	var stype = sfolder;
	if (querys == "") stype = "all";
	checkSearchAllFolderMsg(stype);
}

function checkSearchAllFolderMsg(val) {
	var searchId = "sAllfolderDesc";
	if (jQuery("#adSearchBox").css("display") == "none" || jQuery("#m_searchFolderForm").css("display") != "none") {
		searchId = "s2AllfolderDesc";
	}
	if (val == "all") {
		var searchAllfolderMsg = settingMsg.conf_profile_search_folder;
		if (!isSearchAllFolder) {
			searchAllfolderMsg = settingMsg.conf_profile_search_folder_exc;
		}
		jQuery("#"+searchId).text(searchAllfolderMsg);
	} else {
		jQuery("#"+searchId).text("");
	}
}


function listSearchFolder(nodeData){
	if(!checkEscapeWriteMode()){return;}
	var query = nodeData.query;	
	var querys = query.split("/%");	
	
	var param = {
		folder : querys[0],
		adv : "on",
		keyWord : querys[2],
		category : querys[1],
		fromaddr : querys[3],
		toaddr : querys[4],
		flag : querys[5]
	};
	if(chgWorkMode()){
		mailControl.loadMessageList(param);
	}
	
	storeLinkId("link_sfolder_"+nodeData.id);
}


function historyCallBackFunc(url, mode, param){
	var isLoad = true;
	var isCheck = true;	
	if(LayoutInfo.mode != mode){
		if(LayoutInfo.mode == "manage"){
			escapeManageMode();
		}		
		
		if(LayoutInfo.mode == "write"){
			if(!confirm(mailMsg.confirm_escapewrite)){
				isCheck = false;
				isLoad = false;				
			} else {
				alWriteMode = false;				
			}
		}			
		if(mode == "home"){			
			chgServiceView("mhome");			
			isLoad = false;
		}	
	} else {
		if(LayoutInfo.mode == "write"){
			isLoad = false;
		}
	}
	
	if(isCheck && mode == "list"){
		var mode = jQuery.cookie("PM_L");		
		mode = (mode)?mode:"n";
		contentSplitter.setSplitter(mode);
		paneMode = contentSplitter.getMode();
		mailControl.loadMessageList(param);
		isLoad = false;
	}
	
	if(isCheck && mode == "read"){		
		mailControl.readMessage(param);
		isLoad = false;
	}
	
	return isLoad;
}

var MsgIdUtil = {};
MsgIdUtil.getID = function(elementId){
	var fname = elementId.substr(0,elementId.lastIndexOf("_"));
	elementId = jQuery.trim(getFolderNameUnescape(elementId));	
	var idx = elementId.lastIndexOf("_");		
	var idInfo = {
			"folder" : elementId.substr(0,idx),
			"uid": elementId.substr(idx+1,elementId.length),
			"fid": fname
			};
	return idInfo;
}

function folderInitLoad(afterFunc){
	jQuery("#m_userFolderTree").loadbar("m_leftMenu");
	jQuery("#m_userTag").loadbar("m_leftMenu");
	jQuery("#m_userSearchFolder").loadbar("m_leftMenu");
	jQuery("#m_userSharringFolder").loadbar("m_leftMenu");
	
	MailFolderService.getMailFolderAllInfo(function(leftMenuInfo){
		tagControl.updateTagList(leftMenuInfo.userTags);
		folderControl.updateQuota(leftMenuInfo.quotaInfo);
		folderControl.updateDefaultFolder(leftMenuInfo.defaultFolders);
		folderControl.updateUserTree(leftMenuInfo.userFolders);
		folderControl.updateSharedFolderInfo(leftMenuInfo.userSharedFolderList);
		searchFolderControl.updateFolderList(leftMenuInfo.userSearchFolders);
					
		if(afterFunc)afterFunc();
		
		jQuery("#m_leftMenu").css("overflow","auto");
	});
}


function popupWriteLoad(paramObj){
	var windowWidth  = screen.availWidth ;
	var windowHeight  = screen.availHeight ;

	var popupHeight = 960;
	var popupWidth = 1044;
	
	var top = windowHeight/2 - popupHeight/2;
	var left = windowWidth/2 - popupWidth/2;
	
	if(!isMsie) popupHeight = 966;
	popWriteWin = window.open("about:blank","writePop","resizable=yes,scrollbars=yes,status=yes,top="+top+",left="+left+",width="+popupWidth+",height="+popupHeight);	
	var popupWriteForm = document.popupWriteForm;
	if(!paramObj)paramObj = {};
	paramObj.wmode = "popup";
	makeParamElement(popupWriteForm,paramObj);
	
	popupWriteForm.method = "post";
	popupWriteForm.action="/dynamic/mail/writeMessage.do";
	popupWriteForm.target = "writePop";
	popupWriteForm.submit();	
}

function readPopMessage(folderName,uid){	
	if(LayoutInfo.mode == "list"){
		changeFlagView(true,"S",[folderName+"_"+uid]);
	}
	
	var popupReadForm = document.popupReadForm;
	popupReadForm.uid.value = uid;
	popupReadForm.folder.value = folderName;
	popupReadForm.readType.value = "pop";
	
	var param = {};
	param = mailControl.getSharedFolderParam(param);	
	popupReadForm.sharedFlag.value = param.sharedFlag;
	popupReadForm.sharedUserSeq.value = param.sharedUserSeq;
	popupReadForm.sharedFolderName.value = param.sharedFolderName;	
	
	var wname = "popupRead"+makeRandom();
	var popWin = window.open("about:blank",wname,"resizable=yes,scrollbars=yes,width=800,height=640");
	popupReadForm.method = "post";
	popupReadForm.action="/dynamic/mail/readMessage.do";
	popupReadForm.target = wname;
	popupReadForm.submit();		
}

function setWorkTitle(val){	
	$("workTitle").innerHTML = val;
}

function emptyFolder(folderName){
	var func = null;
	
	if(folderName == "Trash"){
		if(!confirm(mailMsg.confirm_emptytrash)){
			return;
		}
	}
	
	if(folderName == "Spam"){
		if(!confirm(mailMsg.confirm_emptyspam)){
			return;
		}
	}
	
	if(folderName == mailControl.getCurrentFolder()){		
		func = "mailControl.reloadMessageList()";
	}
	
	
	folderControl.emptyFolder(folderName,func);
	
}

function allSelectAction(funcType,targetFolder){
	var param = mailControl.getMailListParam();
	param.funcType = funcType;
	param.targetFolder = targetFolder;	
	jQuery.post("/mail/allSelectMessageProcess.do",param, allSelectActionResult, "json");
	
	jQuery.makeBodyMask("mainBodyMaskWrapper");	
}
function allSelectActionResult(data, textStatus){
	if(textStatus == "success"){
		if(data.result == "success"){
			reloadMailPage();
		} else {
			alert(mailMsg.allselect_action_error);
		}
	} else {
		alert(mailMsg.allselect_action_error);
	}
	
	jQuery.removeBodyMask("mainBodyMaskWrapper");
}

var dropFunc = function dropMsgFolder(isAllCheck,dropType,uids, fromFolders, toFolder){
	if(isAllCheck){
		if(!dropActive){
			dropActive = true;			
			if(dropType == "folder")
				allSelectAction("move",toFolder);									
		}		
	}else if((uids && uids.length > 0) &&
		(fromFolders && fromFolders.length > 0)){
		if(!dropActive){
			dropActive = true;
			if(dropType == "folder"){
				mailControl.moveMessage(uids, fromFolders, toFolder);
			} else if(dropType == "tag"){
				tagControl.taggingMessage("true",uids,fromFolders,toFolder);
			}
		}
	}
}

function toggleMenu(id){
	var menu = jQuery("#"+id);	
	if(menu.css("display") != "none"){
		setDisplayLeftTree("N",id);
		jQuery.cookie("LM_"+id , "N",{path:"/"});
	} else {		
		setDisplayLeftTree("V",id);
		jQuery.cookie("LM_"+id , "V",{path:"/"});		
	}	
}

function setDisplayLeftTree(mode,id){
	var menu = jQuery("#"+id);
	var menuIcon = jQuery("#"+id+"_view");
	
	if(mode == "V"){
		menu.show();
		menuIcon.attr("src","/design/common/image/btn_menu_mius.gif");
	} else {
		menu.hide();
		menuIcon.attr("src","/design/common/image/btn_menu_plus.gif");		
	}	
	
}

function setToggleTreeMenu(id){
	var viewValue = jQuery.cookie("LM_"+id);
	if(viewValue){
		setDisplayLeftTree(viewValue,id);
	}
}

function showAdsearchBox(){
	var box = jQuery("#adSearchBox");
	var userFList = folderControl.getFolderList();
	jQuery("#adsearchForm *").clearForm();
	box.slideDown();	
	
	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	
	jQuery("#adStartDate").datepick({dateFormat:'yy-mm-dd'});	
	jQuery("#adEndDate").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#datepick-div").css("z-index", "110");
	jQuery("#datepick-div").hide();	
	
	var searchCondition = [];
	searchCondition.push({index:mailMsg.search_subject,value:"s"});
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_body,value:"b"});
	}
	searchCondition.push({index:mailMsg.search_attname,value:"af"});
	if(mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_attcontent,value:"ab"});
	}	
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_sbody,value:"sb"});
	}	
	searchCondition.push({index:mailMsg.search_sattname,value:"saf"});
	if(mailSearchConfig.bodySearch == "on" && mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_sattcontent,value:"sab"});
	}
	
	jQuery("#adConditionSelect").empty();
	jQuery("#adConditionSelect").selectbox({selectId:"adCondition",selectFunc:"",width:150},
			"",searchCondition);
	
	jQuery("#adSfolderSelect").empty();
	jQuery("#adSfolderSelect").folderSelectList("adSfolder",userFList,false,'s',checkSearchAllFolderMsg);

	
	jQuery("div#pageNavi").hide();	
	jQuery("#m_contentBodyWapper").bind("resize",resizeHiddenAdsearchBox);
	jQuery("#m_mainContent").bind("click",resizeHiddenAdsearchBox);
	
	checkSearchAllFolderMsg("all");
	$("adsearchForm").searchFolderFlag[0].checked = true;
}

function resizeHiddenAdsearchBox(){
	hiddenAdsearchBox();
}
function hiddenAdsearchBox(){
	var box = jQuery("#adSearchBox");
	if(jQuery.browser.msie){
		jQuery("#adSearchBoxCover").remove();
	}
	box.slideUp();
	jQuery("#m_contentBodyWapper").unbind("resize",resizeHiddenAdsearchBox);
	jQuery("#m_mainContent").unbind("click",resizeHiddenAdsearchBox);
	jQuery("div#pageNavi").show();	
}

//*** MAIL FOLDER FUNC ***//
function nodeLink(nodeData){
	 goFolder(nodeData.fullName);
}

function sharedFolderLink(nodeData){
	goSharedFolder(nodeData.encname,nodeData.userid);
}
function removeNode(nodeData){
	if(confirm(mailMsg.confirm_delete_folder)){
		var treeObj = eval("mainTreeMap."+nodeData.treeId);	
		treeObj.removeNode(nodeData.id,nodeData.fullName);
	}
}

function deleteFolder(folderName){
	var deleteEqual = (folderName == mailControl.getCurrentFolder())?true:false;	
	folderControl.deleteFolder(folderName,deleteEqual);	
}

function addChildNode(nodeData){	
	if(nodeData.depth < 2){
		var treeObj = eval("mainTreeMap."+nodeData.treeId); 
		treeObj.addNode(nodeData.id,nodeData.fullName);
	} else {
		alert(mailMsg.alert_addfolder_depth);
	}
}

function addFolder(folderName){	
	folderControl.addFolder(jQuery.trim(folderName));
}

function modifyNode(nodeData){
	var treeObj = eval("mainTreeMap."+nodeData.treeId);	
	treeObj.modifyNode(nodeData.id,nodeData.fullName);
}

function modifyFolder(previousName, parentName, newName){	
	folderControl.modifyFolder(previousName, parentName, jQuery.trim(newName));	
}

function cleanFolder(nodeData){	
	if(confirm(mailMsg.confirm_delete)){	
		emptyFolder(nodeData.fullName);
	}
}

//*** MAIL FOLDER FUNC END ***//



/*
 * FOLDER TREE FUNC 
 */

function addNode(id){
	var treeObj = eval("mainTreeMap."+id); 
	treeObj.addNode();
}

function updateUserFolderDnD(){
	if(currentFolderType != "shared"){
		dndManager.reloadApplyDnD(jQuery.merge(getDropFolderList(), 
				tagControl.getDropTagList()));
	}	
}
function getDropFolderList(){
	var list = folderControl.getDropFolderList();		
	if(!list){		
		list = getDropFolderList();
	}
	list = jQuery.merge(list, 
			tagControl.getDropTagList());
	
	return list;	
}



function switchFlaged(msgInfo, flagType, used){	
	mailControl.switchMessagesFlags(msgInfo.uid, msgInfo.folders, flagType, used);
}

function switchFlagFlaged(mid,flagType, flagID){	 
	var msgInfo = getListProcessParams(mid);
	mailControl.switchMessagesFlags(msgInfo.uids,msgInfo.fnames,flagType, jQuery("#"+flagID).attr("flaged"));
}

function getListProcessParams(lowIDs){
	var uids = [];
	var fnames = [];		

	for ( var i = 0; i < lowIDs.length; i++) {
		var finfo = MsgIdUtil.getID(lowIDs[i]);
		uids[uids.length] = finfo.uid;
		if (isAllFolder){				
			fnames[fnames.length] = finfo.folder;
		} else {
			fnames[0] = finfo.folder;
		}
	}
	
	return {"uids":uids,"fnames":fnames};
}

function goFolderManage(){
	document.location = "/dynamic/mail/viewFolderManage.do";
}

/*function escapeManageMode(){
	$("m_contentMain").scrollTop = 0;
	jQuery("#m_leftMenu").removeMask();
	folderControl.updateFolderInfo();
	searchFolderControl.updateFolderInfo();
	tagControl.updateTagInfo();
	clearTimeout(backupInterval);
	clearInterval(hideLeftScroll);
}*/

function endFolderManage(){
	escapeManageMode();	
	setTimeout(function(){goFolder("Inbox");},500);
}


function pop3Win() {
	/*
	var url = "/setting/viewExtMailPopup.do";
	openSimplePopup(url,"700px","400px",true);
	*/
	var src = "/setting/viewExtMailPopup.do";
	modalPopupForExtPop3(src);
}

function modalPopupForExtPop3(src) {
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = 330;
	var width = 700;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	jQuery("#extpop3").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#extpop3Iframe").attr("src",src);
			jQuery("#extpop3Iframe").css("height",(height-10)+"px");
			jQuery("#extpop3Iframe").css("width",(width-8)+"px");
		},100);
		jQuery("#extpop3_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#extpop3Iframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#extpop3").jQpopup("open",popOpt);
}

function modalPopupForExtPop3Close(){
	jQuery("#extpop3").jQpopup("close");
}

function setExtPop3PopupTitle(title){
	jQuery("#extpop3_pht").html(title);
}

function jQpopupClear(){
	setTimeout(function(){
		//jQuery(":text:first").focus();
		jQuery("#skword").focus();
	},100);
	
}


function processMessageViewer(isShow, msg) {	
	var msgContent =  jQuery("#processMessageContent");	
	if (isShow) {
		msgContent.show();		
		msgContent.html(msg);		
		setTimeout("processMessageViewer(false)",3000);
	} else {
		msgContent.hide();
		msgContent.empty();
	}	
}


function isDefaultBox(mbox) {
	var mboxUpper = mbox.toUpperCase();
	if(mboxUpper == "INBOX"
		|| mboxUpper == "SENT"
		|| mboxUpper == "DRAFTS"
		|| mboxUpper == "TRASH"
		|| mboxUpper == "SPAM"
		|| mboxUpper == "RESERVED"
		|| mboxUpper == "QUOTAVIOLATE"
		|| mbox == mailMsg.folder_inbox 
		|| mbox == mailMsg.folder_sent
		|| mbox == mailMsg.folder_drafts
		|| mbox == mailMsg.folder_reserved
		|| mbox == mailMsg.folder_spam
		|| mbox == mailMsg.folder_trash
		|| mbox == mailMsg.folder_quotaviolate
		) {
		return true;
	}
	return false;
}


function getSharedWorkParam(param){
	return mailControl.getSharedFolderParam(param);	
}

function getMailControlListParam(){
	return mailControl.getMailListParam();
}

function updateWorkReadCount(folderName,totalCnt,unreadCnt){
	var workTitle = "<span class='TM_work_title' title='"+folderName+"'>"+escape_tag(ellipsisString(folderName,20))+"</span>";
	if(currentFolderType != "all"){		
		workTitle = workTitle +"&nbsp;"+msgArgsReplace(mailMsg.folder_info,[totalCnt,unreadCnt]);
	}

	setWorkTitle(workTitle);
}

function checkEscapeWriteMode(){	
	if(alWriteMode){
		if(isWriteModify()){
			if(!confirm(mailMsg.confirm_escapewrite)){			
				return false;			
			} else {
				alWriteMode = false;
			}
		}
		escapeAutocomplete();
		if(basicAttachUploadControl)basicAttachUploadControl.destroy();
	}
	return true;
}

function writeDraftMessage(folderName,uid){
	goWriteLoad({wtype:"drafts",folder:folderName,uids:[uid]});
}

function writeMessage(to){
	goWriteLoad({wtype:"send",to:to});	
}

function markSentSeenFlag(msgId) {
	var param = {"messageId":msgId};
	var paid = jQuery("#paid").val();
	param.paid = paid;
	jQuery.post("/mail/changeSentMessageFlag.do",param);
}