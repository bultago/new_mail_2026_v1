var fmgntControl;
var isBackup = false;
var isBackupComplete = false;
var backupInterval;
var folderAgaingVal = [];

function loadManage(backupStatus){
	
	jQuery(window).autoResize(jQuery("#m_contentBody"),"#copyRight");	
	jQuery(".TM_sfolder_query").printSearchQuery();
	
	var dfolderMOpt = {
		overClass : "tcmenuOverClass",
 		iconClass : "tcmenuIcon",
 		blankImgSrc : "/design/common/image/blank.gif",
 		subOverClass : "tcsubmenu",

 		// menuLink
 		menuLink : [
 			 		{name:mailMsg.menu_empty,link:cleanFolderService},
 			 		{name:mailMsg.menu_backup,link:backupFolderService}
 			 	]
		 
	};

	var ufolderMOpt = {
			overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
						{name:mailMsg.menu_add,link:addSubFolderLayer},
						{name:mailMsg.menu_modfy,link:modfySubFolderLayer},						
	 			 		{name:mailMsg.menu_empty,link:cleanFolderService},
	 			 		{name:mailMsg.menu_remove,link:deleteFolderService},
	 			 		{name:mailMsg.menu_shared,link:viewSharedFolderSetting},
	 			 		{name:mailMsg.menu_upload,link:uploadMessageService},
	 			 		{name:mailMsg.menu_backup,link:backupFolderService}
	 			 	]			 
		};

	var sfolderMOpt = {
			overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
						{name:mailMsg.menu_modfy,link:modifySFolder},						
						{name:mailMsg.menu_remove,link:deleteSearchFolderService}
	 			 	]			 
		};

	var tagMOpt = {
			overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
	 		            {name:mailMsg.menu_modfy,link:modifyMTag},
						{name:mailMsg.menu_remove,link:deleteTagService}
	 			 	]
	};
	
	jQuery(".TM_fm_dfolder_wrap").folderManageMenu(dfolderMOpt);
	if(jQuery(".TM_fm_ufolder_wrap")){
		jQuery(".TM_fm_ufolder_wrap").folderManageMenu(ufolderMOpt);	
	}	
	if(jQuery(".TM_fm_sfolder_wrap")){
		jQuery(".TM_fm_sfolder_wrap").folderManageMenu(sfolderMOpt);
	}
	if(jQuery(".TM_fm_tag_wrap")){
		jQuery(".TM_fm_tag_wrap").folderManageMenu(tagMOpt);		
	}	
	
	
	jQuery("#sfolderTargetSelect").empty();	
	jQuery("#sfolderTargetSelect").folderSelectList("sfolderTarget",ufolderList,false,'s',checkSearchAllFolderMsg);
	
	
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
	
	jQuery("#sfolderConditionSelect").empty();
	jQuery("#sfolderConditionSelect").selectbox({selectId:"sfolderCondition",selectFunc:"",width:150},
			"",
			searchCondition);
	
	fmgntControl = new FolderManagerControl();	
	checkBackup(backupStatus);
	processBackupFolder(backupStatus);			
}

var BtnMaker = {};
BtnMaker.getBtn = function(name,className){
	var btn = jQuery("<a href='javascript:;'></a>");
	btn.addClass(className);
	btn.append(jQuery("<span></span>").append(name));		
	return btn;
	
}

var FolderManagerControl = Class.create({	
	initialize: function(){
		this.tagMap = null;
		this.sfolderMap = null;
	},
	emptyFolder:function(folderName){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailFolderService.emptyFolder()
		MailFolderAPI.emptyFolder(folderName)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('폴더 비우기 실패:', error);
				alert('폴더 비우기에 실패했습니다: ' + error.message);
			});
	},
	addFolder:function(folderName){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailFolderService.addFolder()
		MailFolderAPI.addFolder(folderName)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('폴더 추가 실패:', error);
				if(error.message && error.message.includes('exist')){
					alert(mailMsg.alert_folder_samename);
				} else {
					alert('폴더 추가에 실패했습니다: ' + error.message);
				}
			});
	},
	deleteFolder:function(folderName){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailFolderService.deleteFolder()
		MailFolderAPI.deleteFolder(folderName)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('폴더 삭제 실패:', error);
				alert('폴더 삭제에 실패했습니다: ' + error.message);
			});
	},
	modifyFolder:function(previousName, parentName, newName){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailFolderService.modifyFolder()
		MailFolderAPI.modifyFolder(previousName, parentName, newName)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('폴더 수정 실패:', error);
				alert('폴더 수정에 실패했습니다: ' + error.message);
			});
	},
	reloadView:function(){
		var dummary = Math.floor(Math.random() * 10000) + 1;
		document.location = "/dynamic/mail/viewFolderManage.do";
	},
	addSFolder:function(folderName,folderQuery){
		var _this = this;
		MailSearchFolderService.addSearchFolder(folderName,folderQuery,function(){
			_this.reloadView();
		});
	},
	modifySFolder:function(oldId,folderName,folderQuery){
		var _this = this;
		MailSearchFolderService.modifySearchFolder(oldId,folderName,folderQuery,function(){
			_this.reloadView();
		});
	},
	deleteSFolder:function(folderIds){
		var _this = this;
		MailSearchFolderService.deleteSearchFolder(folderIds,function(){
			_this.reloadView();
		});
	},
	addTag:function(tagName,tagColor){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailTagService.addTag()
		MailTagAPI.addTag(tagName, tagColor)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('태그 추가 실패:', error);
				alert('태그 추가에 실패했습니다: ' + error.message);
			});
	},
	modifyTag:function(oldId,tagName,tagColor){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailTagService.modifyTag()
		MailTagAPI.modifyTag(oldId, tagName, tagColor)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('태그 수정 실패:', error);
				alert('태그 수정에 실패했습니다: ' + error.message);
			});
	},
	deleteTag:function(tagIds){
		var _this = this;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailTagService.deleteTag()
		MailTagAPI.deleteTag(tagIds)
			.then(function() {
				_this.reloadView();
			})
			.catch(function(error) {
				console.error('태그 삭제 실패:', error);
				alert('태그 삭제에 실패했습니다: ' + error.message);
			});
	},
	getTagMap:function(){
		if(!this.tagMap){
			var map = new Hash();			
			if(jQuery(".TM_fm_tag_wrap")){
				var fList = jQuery(".TM_fm_tag_wrap");	
				jQuery.each(fList,function(){
					var el = jQuery(this);
					map.set(el.attr("fname"),{id : el.attr("sid"), color: el.attr("tcolor")});		
				});
			}
			this.tagMap = map;
		}
		
		return this.tagMap;
	},
	getSFolderMap:function(){
		if(!this.sfolderMap){
			var map = new Hash();			
			if(jQuery(".TM_fm_sfolder_wrap")){
				var fList = jQuery(".TM_fm_sfolder_wrap");	
				jQuery.each(fList,function(){
					var el = jQuery(this);
					map.set(el.attr("fname"),{id : el.attr("sid"),
						name : el.attr("fname")});		
				});
			}
			this.sfolderMap = map;
		}		
		return this.sfolderMap;
	}
});

function viewSharedFolder(folderName,userSeq){
	viewFolder(folderName, "sharedFlag=shared&sharedUserSeq="+
			userSeq+"&sharedFolderName="+folderName);
}

function cleanFolderService(data){
	if(confirm(mailMsg.confirm_delete)){	
		var fname = data.fname;
		fmgntControl.emptyFolder(fname);		
	}
}

function deleteFolderService(data){
	if(confirm(mailMsg.confirm_delete_folder)){
		var fname = data.fname;
		fmgntControl.deleteFolder(fname);		
	}
}

function addMainFolderService(){
	var fname = jQuery.trim(jQuery("#ufolderMain").val());
	
	if(!checkFolderName('folder',$("ufolderMain"))){
		return;
	}
	
	fmgntControl.addFolder(fname);
}

function addSubFolderService(parentFolder){
	var fname = jQuery.trim(jQuery("#addUfolderName").val());
	
	if(!checkFolderName('folder',$("addUfolderName"))){
		return;
	}
	
	fname = parentFolder+"."+fname;
	fmgntControl.addFolder(fname);
}

function modfySubFolderService(oldFolder){
	var fname = jQuery.trim(jQuery("#modfyUfolderName").val());
	
	if(!checkFolderName('folder',$("modfyUfolderName"))){
		return;
	}
	
	var idx = oldFolder.lastIndexOf(".");
	var parentName = "";
	if(idx > -1){
		parentName = oldFolder.substr(0,idx);
	}
	fmgntControl.modifyFolder(oldFolder,parentName,fname);
}

function addSubFolderLayer(data){
	
	var fname = data.fname;
	var idx = data.idx;
	var depth = Number(data.depth);
	if(depth == 2){
		alert(mailMsg.alert_addfolder_depth);
		return;
	}	
	removeWorkFolder();

	var eventTr = jQuery("#ufolder_tr_"+idx);	

	var table = jQuery("<tr id='ufolder_tr_add'></tr>");
	var tableTd = jQuery("<td colspan='3'></td>");
	
	var wrap = jQuery("<div></div>");
	wrap.addClass("TM_fm_ufolder_wrap");
	if(depth == 0){
		wrap.addClass("ufolder_level1");
	} else if(depth == 1){
		wrap.addClass("ufolder_level2");
	}
	wrap.append(jQuery("<div></div>").addClass("tcfolder"));
	wrap.append(jQuery("<input type='text' id='addUfolderName'/>").addClass("basicInput").css("width","200px"));

	var addBtn = BtnMaker.getBtn(mailMsg.menu_add,"btn_basic");
	var cancelBtn = BtnMaker.getBtn(comMsg.comn_cancel,"btn_basic");
	addBtn.click(function(e){addSubFolderService(fname)});
	cancelBtn.click(function(e){jQuery("#ufolder_tr_add").remove();});
	
	wrap.append("&nbsp;&nbsp;");
	wrap.append(addBtn.addClass("btn_align"));
	wrap.append(cancelBtn.addClass("btn_align"));
	
	tableTd.append(wrap);
	table.append(tableTd);	
	eventTr.after(table);
	
}

var preWorkIdx;
function removeWorkFolder(){
	
	if($("ufolder_tr_add")){
		jQuery("#ufolder_tr_add").remove();
	}

	if($("ufolder_modfy")){
		jQuery("#ufolder_modfy").parent().removeClass("ufolder_work");		
		jQuery("#ufolder_modfy").remove();		
		jQuery("#ufolder_link_"+preWorkIdx).show();
	}
	
}

function modfySubFolderLayer(data){
	
	var fname = data.fname;
	var idx = data.idx;
	 
	removeWorkFolder();
	preWorkIdx = idx;
	
	var linkObj = jQuery("#ufolder_link_"+idx);
	var wrap = linkObj.parent();
	var modfyLayer = jQuery("<span id='ufolder_modfy'></span>");
	linkObj.hide();
	wrap.addClass("ufolder_work");
	
	var modfyBtn = BtnMaker.getBtn(mailMsg.menu_modfy,"btn_basic");
	var cancelBtn = BtnMaker.getBtn(comMsg.comn_cancel,"btn_basic");
	modfyBtn.click(function(e){modfySubFolderService(fname)});
	cancelBtn.click(function(e){
		 	jQuery("#ufolder_link_"+idx).show();
			jQuery("#ufolder_modfy").remove();
			wrap.removeClass("ufolder_work");		
	});
	
	modfyLayer.append(
		jQuery("<input type='text' id='modfyUfolderName' value='"+linkObj.text()+"'/>").
		addClass("basicInput").css("width","200px"));
	modfyLayer.append("&nbsp;&nbsp;");
	modfyLayer.append(modfyBtn);
	modfyLayer.append(cancelBtn);
	
	wrap.append(modfyLayer);
}

var basicMsgUploadListeners = {
		swfuploadLoaded: function(event){},
		fileQueued: function(event, file){			
			var size = file.size;
			var quota = basicMsgUploadControl.getAttachQuotaInfo();
			if((size > quota.normalMax) || 
					(size+quota.normalUse > quota.normalMax)){				
				alert(mailMsg.error_upload_quota);
				setTimeout(function(){
					closeUploadModal();
				},1000);
				return;
			}
			
			basicMsgUploadControl.addUploadQueueFile(file);
		},
		fileQueueError: function(event, file, errorCode, message){
			if(errorCode == "-120"){
				alert(mailMsg.ocx_virtxt_failquest);
			} else {
				alert(comMsg.error_fileupload + "["+errorCode+"]");
			}
		},
		fileDialogStart: function(event){},
		fileDialogComplete: function(event, numFilesSelected, numFilesQueued){
			if(numFilesSelected < 100){
				uploadMailMessage();
			} else {
				alert(msgArgsReplace(mailMsg.error_upload_mail,["100"]));
				closeUploadModal();
			}			
		},
		uploadStart: function(event, file){
			$("uploadLoadingBox").scrollTop = file.index * 40;
		},
		uploadProgress: function(event, file, bytesLoaded){			
			var fileSize = file.size;
			var fileUploadPercent = Math.ceil(bytesLoaded / fileSize * 100); 
			$(file.id+"GraphBar").style.width = fileUploadPercent+"%";			
			$(file.id+"Status").innerHTML = fileUploadPercent+"%";
		},
		uploadSuccess: function(event, file, serverData){			
			var data = eval("("+serverData+")");			
			if(data.result != "OK"){
				alert(mailMsg.error_upload);
				setTimeout(function(){
					closeUploadModal();
				},1000);
				return;
			}
		},
		uploadComplete: function(event, file){
			$(file.id+"Status").innerHTML = comMsg.comn_upload_complete;
			if(!basicMsgUploadControl.isNextUploadQueue(file.id)){				
				eval(uploadAfterFunc);
				alert(mailMsg.alert_messageupload);
				setTimeout(function(){
					closeUploadModal();
				},1000);
			}
		},
		uploadError: function(event, file, errorCode, message){
			if(errorCode != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED){
				jQuery("#attachUploadProgress").jQpopup("close");
				alert(comMsg.error_fileupload);
				setTimeout(function(){
					closeUploadModal();
				},1000);
				return;
			}
		}
	};

var basicMsgUploadOpt = {
		controlType:"normal",
		btnId:"basicMsgUploadControl",
		btnCid:"basicMsgUploadBtn",
		formName:"theFile",
		param:{"uploadType":"flash"},
		url:"/mail/uploadMessage.do",
		maxFileSize:1024 * 1024 * 100,
		fileType:"*.eml",		
		btnImage:"/design/common/image/btn/bg_msgupload_btn_"+LOCALE+".gif",
		btnWidth:130,
		btnHeight:19,			
		debug:false,		
		handler:basicMsgUploadListeners};

var basicMsgUploadControl = new UploadBasicControl(basicMsgUploadOpt);

function startUploadMsg(folderName){	
	var html,file;
	var fileLists = basicMsgUploadControl.getUploadQueueFile();
	jQuery("#basicMsgUploadControl").swfupload("addPostParam","folder",folderName);
	if(fileLists.length > 0){		
		jQuery("table#attachFileUploadTable  tr").remove();
		for ( var i = 0; i < fileLists.length; i++) {
			file = fileLists[i];
			html = "<tr><td style='height:40px' valign='top'>"+
			"<p style='width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap' title='"+escape_tag(file.name)+"'> <img src='/design/common/image/icon/ic_att_"+getFileTypeImage(file.name)+".gif'/>&nbsp;"+escape_tag(file.name)+"</p>"+
			"<div id='"+file.id+"GBox' style='position:relative'><div id='"+file.id+"GraphBar' class='TM_upload_graphBar'></div>" +
			"<div class='TM_upload_capacity'><span class='TM_quotaUsage'>"+printSize(file.size)+"</span></div></div>"+
			"</td><td id='"+file.id+"Status' style='padding-top:5px;text-align:center'>"+comMsg.comn_upload_ready+"</td></tr>";			
			jQuery("#attachFileUploadTable").append(html);
		}
	
		var popOpt = clone(popupOpt);
		popOpt.btnList = [];
		popOpt.hideCloseBtn = true;		
		popOpt.minHeight = 450;
		popOpt.minWidth = 400;
		popOpt.top = 150;
		popOpt.openFunc = function(){		
			jQuery("div#attachUploadProgress table").css("width","100%");		
			basicMsgUploadControl.startUpload();		
		};
		popOpt.notResize = true;
		popOpt.closeFunc = function(){
			jQuery("div#attachUploadProgress table").css("width","");						
		};
		
		jQuery("#attachUploadProgress").jQpopup("open",popOpt);
	} else {
		closeUploadModal();
	}
}

function uploadMessageService(data){
	var fname = data.fname;
	upfolderName = fname;
	uploadType = "fmanager";
	
	if(isMsie6){
		jQuery("#m_messageUploadForm").makeModal("bodyModalMask");
		setTimeout(function(){msgUploadInit();}, 1000);
	} else {
		msgUploadInit();
		jQuery("#m_messageUploadForm").makeModal("bodyModalMask");
	}
}

function closeUploadModal(){
	try{
		basicMsgUploadControl.cancelUpload();
		jQuery("#attachUploadProgress").jQpopup("close");
		jQuery("#m_messageUploadForm").removeModal("bodyModalMask");
	}catch(e){}
}

function backupFolderService(data){	
	if(isBackup || isBackupComplete){
		alert(mailMsg.alert_backup_process);
		return;
	}
	startBackupFolder(data);
}


function viewSearchFolder(evt){
	var query = jQuery(evt).attr("query");
	var querys = query.split("/%");

	viewFolder(querys[0],"adv=on&keyWord="+querys[2]+"&category="+querys[1]+
			"&fromaddr="+querys[3]+"&toaddr="+querys[4]+"&flag="+querys[5]);	
}

function addMainSearchFolderService(){
	var checkLength = 3;
	var pattern = jQuery("#sfolderValue").val();
	var from = $('sfolderFrom').value;
	var to = $('sfolderTo').value;	
	
	if(!checkFolderName("sfolder", $("sfolderName"))){return;}	
	if(validateHan(from)){checkLength = 4;}
	if(from != "" && !checkInputSearch($("sfolderFrom"),checkLength,255,true)){return;}
	if(validateHan(to)){checkLength = 4;}
	if(to != "" && !checkInputSearch($("sfolderTo"),checkLength,255,true)){return;}	
	
	checkLength = 3;
	if(validateHan(pattern)){checkLength = 4;}
	if(pattern != "" && !checkInputSearch($("sfolderValue"),checkLength,255,true)){return;}
	
	if(from == "" && 
			to == "" &&			
			pattern == ""){
		alert(mailMsg.alert_search_nostr);
		$('sfolderValue').focus();
		return;
	}
	
	var searchFolderFlagObj = document.forwardForm.searchFolderFlag;
	var searchFolderFlag = "";
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].checked) {
			searchFolderFlag = searchFolderFlagObj[i].value;
			break;
		}
	}
	
	var sfolderName,sfolderTarget,sfolderCond,sfolderValue;
	sfolderName = jQuery.trim($("sfolderName").value);
	sfolderTarget =	$("sfolderTarget").value;
	sfolderCond = $("sfolderCondition").value;
	sfolderValue = $("sfolderValue").value;	
	
	var sfolderMap = fmgntControl.getSFolderMap();
	var existFolderName = sfolderMap.get(sfolderName);
	
	if(existFolderName){
		alert(mailMsg.alert_folder_samename);
		$("sfolderName").select();
		return;
	}
	
	var query = sfolderTarget+"/%"+sfolderCond+"/%"+sfolderValue+"/%"+from+"/%"+to+"/%"+searchFolderFlag;	

	fmgntControl.addSFolder(sfolderName,query);	
}

function getSFolderName(folder){
	var fname;
	if(folder == "Inbox"){
		fname = mailMsg.folder_inbox;
	} else if (folder == "Sent"){
		fname = mailMsg.folder_sent;
	} else if (folder == "Draft"){
		fname = mailMsg.folder_draft;
	} else if (folder == "Reserved"){
		fname = mailMsg.folder_reserved;
	} else if (folder == "Spam"){
		fname = mailMsg.folder_spam;
	} else if (folder == "Trash"){
		fname = mailMsg.folder_trash;
	} else if (folder == "all"){
		fname = mailMsg.folder_all;
	}else {
		folder = replaceAll(folder,".","/");
		fname = folder;
	}

	return fname;
}

function getSContdMsg(cond){			
	
	var condStr;
	if(cond == "s"){
		condStr = mailMsg.search_subject;
	} else if (cond == "b"){
		condStr = mailMsg.search_body;
	} else if (cond == "af"){
		condStr = mailMsg.search_attname;
	} else if (cond == "ab"){
		condStr = mailMsg.search_attcontent;
	} else if (cond == "sb"){
		condStr = mailMsg.search_sbody;
	} else if (cond == "saf"){
		condStr = mailMsg.search_sattname;
	} else if (cond == "sab"){
		condStr = mailMsg.search_sattcontent;
	}

	return condStr;
}

function getMailFlagMsg(cond) {
	var condStr = "";
	if(cond == "F"){
		condStr = mailMsg.menu_quick_flag;
	} else if (cond == "T"){
		condStr = mailMsg.menu_quick_attach;
	} else if (cond == "S"){
		condStr = mailMsg.menu_quick_read;
	} else if (cond == "U"){
		condStr = mailMsg.menu_quick_unread;
	}

	return condStr;
}

jQuery.fn.printSearchQuery = function(){
	var qItems = jQuery(this);
	
	jQuery.each(qItems,function(){
		var queryLayer = jQuery(this);
		var query = queryLayer.html();
		var queries = query.split("/%");
		queryLayer.empty();
		var extCond = "";
		var mainCond = "";
		var mainCondText = "";
		
		if(queries[2]){
			mainCond = getSContdMsg(queries[1]);
			mainCondText = escape_tag(queries[2]);
		}
		if(queries[3]){
			if(mainCond != ""){
				extCond += msgArgsReplace(mailMsg.search_queryfrom,[escape_tag(queries[3])]);
			} else {
				mainCond = mailMsg.mail_from;
				mainCondText = escape_tag(queries[3]);
			}
		}
		if(queries[4]){
			if(mainCond != ""){
				extCond += msgArgsReplace(mailMsg.search_queryto,[escape_tag(queries[4])]);
			} else {
				mainCond = mailMsg.mail_to;
				mainCondText = escape_tag(queries[4]);
			}
		}
		if(queries[5]) {
			var argMsg = getMailFlagMsg(queries[5]);
			if(mainCond != "" && argMsg != ""){
				extCond += msgArgsReplace(mailMsg.search_queryflag,[argMsg]);
			} else {
				mainCond = "";
				mainCondText = argMsg;
			}
			
		}
		queryLayer.html(msgArgsReplace(mailMsg.search_query,
			[getSFolderName(queries[0]),mainCond,mainCondText,extCond]));			
		
	});
	
};


function deleteSearchFolderService(data){
	if(confirm(mailMsg.confirm_delete)){
		var ids = [data.sid];	
		fmgntControl.deleteSFolder(ids);
	}
}

function modifySFolder(data){
	var id = data.sid;
	var query = data.sqval;
	var fname = data.fname;	
	
	var searchPopOpt = clone(popupOpt);
	searchPopOpt.btnList = [{name:comMsg.comn_modfy,func:saveSearchFolderService}];
	searchPopOpt.minHeight = 250;
	searchPopOpt.minWidth = 600;
	searchPopOpt.openFunc = function(){
		setSearchFolderForm	(fname,id,query);
	};	
	jQuery("#m_searchFolderForm").jQpopup("open",searchPopOpt);	
}

function addSFolder(){
	
	var searchPopOpt = clone(popupOpt);
	searchPopOpt.btnList = [{name:comMsg.comn_add,func:saveSearchFolderService}];
	searchPopOpt.minHeight = 250;
	searchPopOpt.minWidth = 600;	
	searchPopOpt.openFunc = function(){
		setSearchFolderForm	();
	};	
	jQuery("#m_searchFolderForm").jQpopup("open",searchPopOpt);	
	
}

function saveSearchFolderService(){	
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
		$('sfolderValue').focus();
		return;
	}
	
	var searchFolderFlagObj = $("searchFolderForm").searchFolderFlag;
	var searchFolderFlag = "";
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].checked) {
			searchFolderFlag = searchFolderFlagObj[i].value;
			break;
		}
	}
	
	var folderName = jQuery.trim(jQuery("#searchFolderName").val());
	var sFolder = jQuery("#s2folder").val();
	var cond = jQuery("#searchFolderCondition").val();	
	var query = sFolder+"/%"+cond+"/%"+pattern+"/%"+from+"/%"+to+"/%"+searchFolderFlag;
	
	var oldId = jQuery("#oldSFolderId").val();
	var sfolderMap = fmgntControl.getSFolderMap();
	var existFolderName = sfolderMap.get(folderName);
	
	if(oldId != ""){
		fmgntControl.modifySFolder(oldId,folderName,query);
	} else {
		if(existFolderName){
			alert(mailMsg.alert_folder_samename);
			$("searchFolderName").select();
			return;
		}
		fmgntControl.addSFolder(folderName,query);
	}
	
	jQuery("#m_searchFolderForm").jQpopup("close");
}

function deleteTagService(data){
	if(confirm(mailMsg.confirm_delete)){
		var ids = [data.sid];	
		fmgntControl.deleteTag(ids);
	}
}

function modifyMTag(data){	
	var tagPopOpt = clone(popupOpt);
	tagPopOpt.btnList = [{name:comMsg.comn_modfy,func:saveTagService}];
	tagPopOpt.minHeight = 100;
	tagPopOpt.minWidth = 250;
	tagPopOpt.closeFunc = function(){
		jQuery("#_seven_color_selecter").remove();
	};
	
	jQuery("#m_tagForm").jQpopup("open",tagPopOpt);		
	setTagForm(data.fname,data.tcolor,data.sid);
	jQuery("#tagColor").SevenColorPicker();
}


function addMTag(){
	var tagPopOpt = clone(popupOpt);
	tagPopOpt.btnList = [{name:comMsg.comn_add,func:saveTagService}];
	tagPopOpt.minHeight = 100;
	tagPopOpt.minWidth = 250;
	tagPopOpt.closeFunc = function(){
		jQuery("#_seven_color_selecter").remove();
	};
	
	jQuery("#m_tagForm").jQpopup("open",tagPopOpt);	
	setTagForm("","#ffffff",-1);
	jQuery("#tagColor").SevenColorPicker();
}

function saveTagService(){	
	if(!checkFolderName('tag',$("tagName"))){
		return;
	}
	
	var tagName = jQuery.trim(jQuery("#tagName").val());
	var tagColor = jQuery("#tagColor").val();	
	var oldId = jQuery("#oldTagId").val();
	
	var tagMap = fmgntControl.getTagMap();
	var checkTag = tagMap.get(tagName);
	
	if(parseInt(oldId) > 0){
		fmgntControl.modifyTag(oldId,tagName,tagColor);		
	} else {
		if(checkTag){
			alert(mailMsg.alert_tag_samename);
			$("tagName").select();
			return;
		}
		fmgntControl.addTag(tagName,tagColor);
	}
	
	jQuery("#m_tagForm").jQpopup("close");		
}

function viewTagMessage(tagId){
	viewFolder("all","listType=tag&tagId="+tagId);
}





jQuery.fn.printBackupFolderInfo = function(info){
	var pane = jQuery(this);
	
	pane.empty();
	
	if(info.start){
		pane.append(jQuery("<span>"+mailMsg.mail_backup_start+"</span>"));
	} else if(info.error){
		pane.append(jQuery("<span>"+mailMsg.mail_backup_error+"</span>"));
	}else if(info.backupComplete){		
		var folderInfo = "<div><span>'"+escape_tag(info.folderName)+"' </span>"+mailMsg.mail_backup_complete+"</div>";
		var funcBtn = "<a class='btn_basic' onclick='downloadFolderBackup();' href='javascript:;'>	<span>"+mailMsg.mail_backup_down+"</span></a>"+
						"<a class='btn_basic' onclick='deleteFolderBackup()' href='javascript:;'><span>"+mailMsg.mail_backup_delete+"</span></a>";

		pane.append(jQuery(folderInfo));
		pane.append(jQuery(funcBtn));
		
	}else if(info.backupLoad){
		var folderInfo = "<div><span class='TM_backupFolderName'>'"+escape_tag(info.folderName)+
							"'</span> "+mailMsg.mail_backup_process+" </div>";
		var progressBar = "<div style='width:400px;margin:5px auto 0 auto'><div class='TM_graphBox' style='width:400px'>"+
								"<div class='TM_graphBar' style='width:"+info.percent+"%'></div>"+										
								"<div class='TM_capacity'>"+info.percent+"%</div>"+
								"&nbsp;</div></div>";

		pane.append(jQuery(folderInfo));
		pane.append(jQuery(progressBar));		
	} else {
		pane.append(jQuery("<div style='padding-top:10px;'>"+mailMsg.mail_backup_nobackup+"</div>"));
	}
	
};

function checkBackup(backupStatus){
	if(backupStatus.backupLoad){
		isBackup = true;
	} else {
		isBackup = false;
	}	
}

var backupFolderFullName = "";
function startBackupFolder(data){
	var param = {};
	var info = {};
	info.start = true;
	param.folderName = data.fname;
	param.fullName = data.fullName;
	backupFolderFullName = data.fullName;
	processBackupFolder(info);	
	jQuery.post("/dynamic/mail/startFolderBackup.do", param, resultBackupProcess, "json");	
}

function resultBackupProcess(data, textStatus){	
	if(textStatus == "success"){
		if(data.type == "start"){
			if(data.result == "success"){
				isBackup = true;
				processBackupFolder({start:true});
			} else {
				processBackupFolder({error:true});
			}
		}
		
		if(data.type == "status"){
			if(data.result != "success"){
				isBackup = false;
			} else if(data.backupComplete){
				isBackup = false;
				isBackupComplete = true;	
			}
			if(backupFolderFullName){
				data.folderName = backupFolderFullName;
			} 
			processBackupFolder(data);
		}	
		
		if(data.type == "delete"){
			if(data.result == "success"){
				alert(mailMsg.alert_backup_delete_success);				
			} else {
				alert(mailMsg.alert_backup_delete_error);
			}
			
			isBackup = false;
			isBackupComplete = false;
			processBackupFolder({});
		}	
	} else {
		isBackup = false;
		processBackupFolder({error:true});
	}
}

function statusBackupFolder(){
	jQuery.post("/dynamic/mail/statusFolderBackup.do", {}, resultBackupProcess, "json");	
}

function downloadFolderBackup(){	
	$("reqFrame").src = "/dynamic/mail/downloadFolderBackup.do";
	isBackup = false;
	isBackupComplete = false;
	processBackupFolder({});
}

function deleteFolderBackup(){	
	jQuery.post("/dynamic/mail/deleteFolderBackup.do", {}, resultBackupProcess, "json");
}

function processBackupFolder(backupStatus){
	jQuery("#backup_info").printBackupFolderInfo(backupStatus);
	if(isBackup){
		backupInterval = setTimeout("statusBackupFolder()",3000);
	}	
}

function changeAging(idx,folderName, aging){	
	var param = {};
	param.selectIdx = idx;
	param.folderName = folderName;
	param.newAgingDay = aging;
	param.preAgingDay = jQuery("#againg_"+idx+"_select").attr("aging");	
	
	jQuery.post("/dynamic/mail/changeUserFolderAging.do", param, resultAgingUpdate, "json");
}

function resultAgingUpdate(data, textStatus){
	var isJobSuccess = false;
	if(textStatus == "success"){
		if(data.result == "success"){
			isJobSuccess = true;
		}	
	}
	
	if(isJobSuccess){
		alert(mailMsg.alert_folder_aging_change);
		 jQuery("#againg_"+data.selectIdx).attr("aging",data.agingDay);
	} else {	
		alert(mailMsg.alert_folder_aging_error);
		var preAgingValue = jQuery("#againg_"+data.selectIdx).attr("aging");		
		var options = $("againg_"+data.selectIdx).options;
		for ( var i = 0; i < options.length; i++) {
			if(options[i].value == preAgingValue){
				options[i].selected = true;
			}
		}
	}

}


function checkFolderName(type, inputObj){
	var folderName = inputObj.value;
	var fname = jQuery.trim(folderName);
	var isVaild = true;
	if(fname == ""){
		if(type == "folder"){
			alert(mailMsg.error_folder_insertfolder);
		} else if(type == "sfolder"){
			alert(mailMsg.error_sfolder_insertfolder);
		} else if(type == "tag"){
			alert(mailMsg.error_tag_insertfolder);
		}		
		
		inputObj.focus();
		isVaild = false;
	}
	
	var len = str_realLength(fname);
	if(isVaild && (len < 2 || len > 32)){
		alert(msgArgsReplace(comMsg.error_inputlength,[2,32]));
		inputObj.focus();
		isVaild = false;
	}
	
	if(isVaild && type == "folder" && isDefaultBoxCheck(fname)){
		alert(mailMsg.alert_systemfolder);
		inputObj.focus();
		isVaild = false;
	}
	
	if(isVaild && !isFolderName(fname)){
		var msg;
		if(type == "folder"){
			msg = mailMsg.error_folder_invalidname;
		} else if(type == "sfolder"){
			msg = mailMsg.error_sfolder_invalidname;
		} else if(type == "tag"){
			msg = mailMsg.error_tag_invalidname;
		}
		alert(msg+"\n\n"
			+"%,&,*,.,/,\\,',`,\"");
		inputObj.focus();
		isVaild = false;
	}
	
	return isVaild;
}


//*** SHARED FOLDER FUNC ***//
function viewSharedFolderSetting(nodeData){
	document.sharedFolderForm.reset();
	$("readerSearchStr").value="";
	
	var popOpt = clone(popupOpt);
	popOpt.btnList = [{name:comMsg.comn_confirm,func:saveSharedFolderSetting}];
	popOpt.minHeight = 270;
	popOpt.minWidth = 550;
	popOpt.openFunc = function(){
		jQuery("#sharedFolderPop select").show();
		jQuery("#sharedSearchItemSelect").empty();
		jQuery("#sharedSearchItemSelect").selectbox({selectId:"sharedSearchItem",
			selectFunc:""},
			"",
			[{index:comMsg.register_021,value:"id"},
			 {index:comMsg.register_032,value:"name"}]);
		setSharedFolderInfo(nodeData.shared,nodeData.sharedUid,nodeData.fullName);
	};
	
	jQuery("#sharedFolderPop").jQpopup("open",popOpt);	
}


function setSharedReaderList(readerList){
	var select = jQuery("#settingReaderList");
	if(readerList && readerList.length > 0){
		for ( var i = 0; i < readerList.length; i++) {						
			select.append(
				jQuery("<option value='"+readerList[i].readerUserSeq+"'>"+
						readerList[i].readerId+"("+readerList[i].readerName+")</option>"));
		}
	}
}

function setSharedSearchUserList(searchList){
	var select = jQuery("#searchReaderList");	
	if(searchList && searchList.length > 0){
		select.empty();
		for ( var i = 0; i < searchList.length; i++) {
			if(!checkSharedUserList(searchList[i].mailUserSeq)){
				select.append(
						jQuery("<option value='"+searchList[i].mailUserSeq+"'>"+
							searchList[i].mailUid+"("+searchList[i].userName+")</option>"));
			}
		}
	}
}

function checkSharedUserList(userSeq){
	var selectOption = jQuery("#searchReaderList option");
	var isExist = false;
	for ( var i = 0; i < selectOption.length; i++) {
		if(userSeq == jQuery(selectOption[i]).attr("value")){
			isExist = true;
			break;
		}
	}
	
	return isExist;
}

function setSharedFolderInfo(isSharedFolder, sharedUid, folderName){
	jQuery("#settingReaderList").empty();
	jQuery("#searchReaderList").empty();
	
	if(isSharedFolder){
		$("shreadUseEnabled").checked=true;		
		var settingreaderFunc = setSharedReaderList;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailFolderService.getSharringReaderList()
		MailFolderAPI.getSharringReaderList(sharedUid)
			.then(function(readerList) {
				settingreaderFunc(readerList);
			})
			.catch(function(error) {
				console.error('공유 폴더 권한자 목록 조회 실패:', error);
			});
	} else {
		$("shreadUseDisabled").checked=true;		
	}
	$("oldSharedFolderUid").value=sharedUid;
	$("sharedfolderName").value = folderName;	
	
	toggleSharedFolderSetting();
}

function toggleSharedFolderSetting(){	
	if($("shreadUseEnabled").checked){		
		jQuery("#sharedFolderForm").find("input").not("[type=radio]").removeAttr("disabled");
		jQuery("#sharedSearchItemSelect").selectboxEnable();
	} else {
		jQuery("#sharedFolderForm").find("input").not("[type=radio]").attr("disabled","true");
		jQuery("#sharedSearchItemSelect").selectboxDisable();
	}
}

function closeSharedFolder(){	
	jQuery("#sharedFolderPop").jQpopup("close");	
}
function settingSearchSharedUser(data, textStatus){
	if(textStatus == "success"){
		var list = data.userList;		
		if(!list || list.length ==0){
			alert(mailMsg.alert_search_nouser);
		} else {
			setSharedSearchUserList(list);
		}
	}
}

function searchSharedUser(){
	if(!$("shreadUseEnabled").checked){
		return;
	}
	var searchItem = $("sharedSearchItem").value;
	var searchStrInput = $("readerSearchStr");
	var searchStr = jQuery.trim(searchStrInput.value);
	var searchName = ""; 
	var searchId = "";
	
	if(searchStr == ""){		
		alert(mailMsg.alert_search_nostr);
		searchStrInput.focus();
		return;
	}
	
	if(incNotAllowChar(searchStr)){		
		alert(mailMsg.error_search_invalidname+"\n\n"+
				"\\,/,:,*,?,<,>,|,.,',\",`");		
		searchStrInput.select();
		return;
	}
	
	if(searchItem == "name"){
		searchName = searchStr;
		searchId = "";
	} else if(searchItem == "id"){
		searchId = searchStr;
		searchName = "";
	}
	var param = {"searchName":searchName,"searchId":searchId};
	
	jQuery.post("/common/searchMailUserJson.do",param,settingSearchSharedUser,"json");
	$("readerSearchStr").value="";
	
}

function addSharedUser(){
	if(!$("shreadUseEnabled").checked){
		return;
	}
	var selectedIndex = $("searchReaderList").selectedIndex;
	var searchListOptions = $("searchReaderList").options;
	var readerListOptions = $("settingReaderList").options;
	if(selectedIndex < 0){
		alert(comMsg.error_noselect);
		return;
	}
	
	var uid, text, suid;	
	for (var i = 0; i < searchListOptions.length ; i++) {
		if (searchListOptions[i].selected) {
			uid = searchListOptions[i].value;
			text = searchListOptions[i].text;
			var isExist = false;
			for ( var j = 0; j < readerListOptions.length; j++) {
				suid = readerListOptions[j].value;
				if(uid == suid){
					isExist = true;
					break;
				}
			}	
			
			if(!isExist){
				jQuery("#settingReaderList").append(jQuery("<option value='"+uid+"'>"+text+"</option>"))
			}
			searchListOptions[i] = null;
			i--;
		}
	}
}

function removeSharedUser(){
	if(!$("shreadUseEnabled").checked){
		return;
	}
	var readerListOptions = $("settingReaderList").options;
	
	for (var i = 0; i < readerListOptions.length;) {
		if (readerListOptions[i].selected) {
			if(getBrowserType() == "ie" || getBrowserType() == "opera")
				readerListOptions.remove(i);
			else if(getBrowserType() == "nav" || getBrowserType() == "gecko")
				readerListOptions[i] = null;	
		} else {
			i++;
		}
	}
}

function saveSharedFolderSetting(){
	var folderUid,folderName;
	var oldFolderUid = Number($("oldSharedFolderUid").value);
	var isSaved = false;
	var readerListOptions = $("settingReaderList").options;
	var sharedUids = [];
	
	if($("shreadUseEnabled").checked){		
		if(readerListOptions.length == 0){
			alert(mailMsg.alert_noshareduser);		
			return;
		}
		
		folderUid = oldFolderUid;
		folderName = $("sharedfolderName").value;
		
		for (var i = 0; i < readerListOptions.length ; i++) {
			sharedUids[sharedUids.length] = readerListOptions[i].value;
		}
		
		isSaved = true;
	} else {
		if(oldFolderUid != 0){
			folderUid = oldFolderUid;
		} else {
			folderUid = 0;
		}	
		folderName = "";		
	}
	
	if(folderUid != ""){
		folderUid = parseInt(folderUid);
	} else {
		folderUid = 0;
	}
	
	// DWR → REST API 전환 (2025-10-21)
	// 원본: MailFolderService.setSharringReaderList()
	MailFolderAPI.setSharringReaderList(isSaved, folderUid, folderName, sharedUids)
		.then(function(data) {
			sharedFolderSettingResult(data);
		})
		.catch(function(error) {
			console.error('공유 폴더 권한자 설정 실패:', error);
			alert('공유 폴더 권한자 설정에 실패했습니다: ' + error.message);
		});
	
	jQuery("#sharedFolderPop_p").hide();
}

function sharedFolderSettingResult(data){	
	if(data.result == "success"){
		alert(mailMsg.alert_shared_save);
	} else {
		alert(mailMsg.alert_shared_error);
	}
	closeSharedFolder();
	if(LayoutInfo.mode == "manage"){
		fmgntControl.reloadView();
	}else {
		folderControl.updateFolderInfo();
	}
}

function isDefaultBoxCheck(mbox) {
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

function msgUploadInit(type){	
	var simpleFileObj      = jQuery("#simpleFileInit"); //201802 HTML5 Uploader
	var basicMsgUploadControlObj = jQuery("#basicMsgUploadControl"); 
	basicMsgUploadControlObj.hide();
	simpleFileObj.show();
	
	isOcxMsgUploadMode = false;
}


var uploadAfterFunc;
var upfolderName = "";
function uploadMailMessage(){
	if(upfolderName == "" || upfolderName.length ==0){
		upfolderName = mailControl.getCurrentFolder();
	}
	
	if(LayoutInfo.mode == "list"){
		uploadAfterFunc = "folderControl.updateFolderCountInfo(function(){mailControl.reloadMessageList();jQuery('#contentLoadMask').hide();});";
	} 
	else if(LayoutInfo.mode == "read"){
		uploadAfterFunc = "folderControl.updateFolderCountInfo(function(){mailControl.reloadMessageList();jQuery('#contentLoadMask').hide();});";
	}
	else if(LayoutInfo.mode == "manage"){
		uploadAfterFunc = "fmgntControl.reloadView();";
	}		
	startUploadMsg(upfolderName);	
	upfolderName = "";
}

//201802 HTML5 Uploader
function uploadSimpleEmlFile() {
	var f = document.messageUploadForm;
	if(f.theFile.value == "") {
		return;
	}
	
	var nMaxSize = 1024 * 1024 * 100; //100MB		
	var nFileSize = f.theFile.size;
	var strFilePath = f.theFile.value;
	
	if(nFileSize > nMaxSize) {
		alert(mailMsg.ocx_upalert_size);		
	}
	
	var regExtFilter = /\.(eml)$/i;
	if(strFilePath.match(regExtFilter) == null){
		alert(mailMsg.ocx_virtxt_failquest);
		f.theFile.value = "";
		return;
	}
	
	var listForm = document.listForm;
	if(upfolderName){
		f.folder.value = upfolderName;
	} else {
		if(currentFolder){
			f.folder.value = currentFolder;
		} else {
			f.folder.value = listForm.folderName.value;
		}
	}
	
	jQuery("#up_file_eml_basic_frame").remove();    	
	jQuery("#upload_eml_att_frame").makeUploadFrame("up_file_eml_basic_frame");    
	
	f.maxAttachFileSize.value = nMaxSize;
	f.uploadType.value = "basic";
	f.action = "/mail/uploadMessage.do";
	f.method = "post";
	f.target= "up_file_eml_basic_frame";
	f.submit();
	var fileObj = jQuery("#simpleEmlFile");
	fileObj.replaceWith(fileObj = fileObj.clone( true ));
}

//*** SHARED FOLDER FUNC END ***//