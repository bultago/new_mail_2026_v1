var MailControl = Class.create({	
	initialize: function(opt){		
		this.opt = opt;
		this.currentFolder = "Inbox";
		this.sharedFlag = false;
		this.listParam = {};
		this.mdnListPage = 0;
		this.listMode = "mail";
		this.readParam = {};		
		this.viewMode = null;
		this.userAddrList = null;		
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
	movePage:function(page){
		var param =	this.listParam;
		
		if(this.listMode == "mdnread"){
			// 201612 TCUSTOM-2537 수신확인 목록 페이지 번호를 저장 
			if(this.mdnListPage == 0 || this.mdnListPage == "undefined"){
				this.mdnListPage =  param.page;
			}
			param = this.readParam;
		} else if(this.listMode == "mdnlist"){
			this.listParam = param;
			// 201612 TCUSTOM-2537 수신확인 목록 페이지 번호를 초기화 - 2
			if(this.mdnListPage != 0){
				this.mdnListPage = 0;
			}
		} else {
			this.listParam = param;
		}
		
		if(param == null){
			param = {};
		}
		
		param.page = page;		
		this.listParam = param;
		if(this.listMode == "mail"){
			this.loadMessageList(param);
		} else if(this.listMode == "mdnlist"){
			this.loadMDNList(param);
		} else if(this.listMode == "mdnread"){			
			this.viewMDNContent(param);
		}
		LayoutInfo.mode = "list";
	},	
	loadMessageList : function(param){		
		jQuery("#"+this.opt.mainLID).empty();
		
		if(param){
			param.vmode = paneMode;			
			this.listParam = param;
			if(param.folder && param.folder != ""){
				this.currentFolder = param.folder;
			}			
			if(param.sharedFlag && param.sharedFlag == "shared"){
				this.sharedFlag = true;
			} else {
				this.sharedFlag = false;
			}
		} else {
			param = {};			
		}
		
		this.listParam = param;
		param.method = "ajax";		
		this.initSubMessage();
		this.listMode = "mail";
		this.viewMode = "list";
		if(LayoutInfo.mode == "write" ||
			LayoutInfo.mode == "mdnlist"||
			LayoutInfo.mode == "mdnread"){
			$(this.opt.mainLID).innerHTML = "<div style='height:300px'></div>";
		}
		jQuery("#"+this.opt.mainLID).loadWorkMaskOnly(true);
		debugInfo("LIST_START",true);
		ActionLoader.postLoadAction(this.opt.listAction,param,printMessageList,"json");
		
		LayoutInfo.mode = "list";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.listAction,param,"list");		
		
	},
	reloadMessageList : function(){
		if(this.listMode == "mail"){
			this.loadMessageList(this.listParam);			
		} else if(this.listMode == "mdnlist"){
			this.loadMDNList(this.listParam);
		} else if(this.listMode == "mdnread"){
			this.viewMDNContent(this.readParam);
		}
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
		this.viewMode = "read";
		this.readParam.method = "ajax";		
		jQuery("#"+this.opt.mainLID).loadWorkMaskOnly(true);
		removePreview();
		debugInfo("READ_START",true);
		ActionLoader.postLoadAction(this.opt.readAction,this.readParam,printMessageRead,"json");		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.readAction,this.readParam,"read");
		
		LayoutInfo.mode = "read";
	},
	readSubMessage:function(param){		
		if(param){
			this.readParam = clone(this.listParam);
			this.readParam.folder = param.folder;
			this.readParam.uid = param.uid;
			if(param.viewImg){
				this.readParam.viewImg = param.viewImg;
			}
			
			if(this.sharedFlag){
				param.sharedFlag = "shared";
				param.sharedUserSeq = this.listParam.sharedUserSeq;
				param.sharedFolderName = this.listParam.sharedFolderName;
			}
		}
		this.viewMode = "list";		
		param.method = "ajax";
		jQuery("#"+this.opt.subLID).loadWorkMaskOnly(true);
		removePreview();
		debugInfo("READ_START",true);
		ActionLoader.postLoadAction(this.opt.listAction,this.listParam,printMessageList,"json");
		ActionLoader.postLoadAction(this.opt.readAction,param,printMessageRead,"json");
		LayoutInfo.mode = "list";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.readAction,this.readParam,"readSub");
	},
	writeMessage:function(param){
		/*if(param){
			this.param = param;			
		}*/
		debugInfo("WRITE_START",true);
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.writeAction,
				param,true);
				//function(){PageMainLoadingManager.completeWork("write");});
		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.writeAction,param,"write");
		
		LayoutInfo.mode = "write";
	},
	sendMessage:function(param){
		/*if(param){
			this.param = param;			
		}*/
		ActionNotMaskLoader.loadAction(this.opt.mainLID,
				this.opt.sendAction,
				param,"");
		
		LayoutInfo.mode = "send";
	},
	moveMessage:function(uids,fromFolders,toFolder){
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.moveMessage()
		MailAPI.moveMessages(uids, fromFolders[0], toFolder)
			.then(function() {
				folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
			})
			.catch(function(error) {
				console.error('메일 이동 실패:', error);
				alert('메일 이동에 실패했습니다: ' + error.message);
			});
	},
	copyMessage:function(uids,fromFolders,toFolder){
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.copyMessage()
		MailAPI.copyMessages(uids, fromFolders[0], toFolder)
			.then(function() {
				folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
			})
			.catch(function(error) {
				console.error('메일 복사 실패:', error);
				alert('메일 복사에 실패했습니다: ' + error.message);
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
	initSubMessage:function(){
		ReadSubMessageChecker.resetUid();
		var subHeight = 95.5;
		if(paneMode == "h")
			subHeight = 91;
		else if(paneMode == "v")
			subHeight = 95.5;
		
		//var content = "<div id='nomsg_content' style='text-align: center;height:"+nomsgContentHeight+"px;' class='TM_mail_content'>"+
		var content = "<div id='nomsg_content' style='text-align: center;height:"+subHeight+"%;' class='TM_mail_content'>"+
					"<div class='TM_mail_nomsg_textWrapper'>"+
					"<div class='TM_mail_nomsg_text'>"+mailMsg.mail_nomessage+"</div></div></div>";
	
		jQuery("#"+this.opt.subLID).empty();
		jQuery("#"+this.opt.subLID).append(content);		
	},
	searchMessage:function(param){		
		if(param){
			if(this.sharedFlag){
				param.sharedFlag = "shared";
				param.sharedUserSeq = this.listParam.sharedUserSeq;
				param.sharedFolderName = this.listParam.sharedFolderName;
				
			}
			this.listParam = param;
		}	
		
		this.initSubMessage();
		this.listMode = "mail";		
		this.loadMessageList(param);
		LayoutInfo.mode = "list";
	},
	deleteMessages:function(uids, folders){
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.deleteMessage()
		MailAPI.deleteMessages(uids, folders[0])
			.then(function() {
				folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
			})
			.catch(function(error) {
				console.error('메일 삭제 실패:', error);
				alert('메일 삭제에 실패했습니다: ' + error.message);
			});
	},
	cleanMessages:function(uids, folders){
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.cleanMessage()
		MailAPI.deleteMessages(uids, folders[0])
			.then(function() {
				folderControl.updateFolderCountInfo(mailControl.reloadMessageList());
			})
			.catch(function(error) {
				console.error('메일 완전 삭제 실패:', error);
				alert('메일 완전 삭제에 실패했습니다: ' + error.message);
			});
	},
	switchMessagesFlags:function(uids, folders, flagType, used){
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.switchMessagesFlags()
		MailAPI.setFlags(uids, folders[0], flagType, used)
			.then(function(result) {
				if(LayoutInfo.mode == "list"){
					changeFlagView(used, flagType, uids);
				}
			})
			.catch(function(error) {
				console.error('플래그 변경 실패:', error);
				alert('플래그 변경에 실패했습니다: ' + error.message);
			});
	},
	sortMessage:function(sortBy,sortDir){
		var param = this.listParam;
		if(param == null){
			param = {};
		}
		param.page = 1;
		param.sortBy = sortBy;
		param.sortDir = sortDir;
		this.listParam = param;
		this.listMode = "mail";
		this.loadMessageList(param); 
	},
	removeAttachFile:function(uid, folder, part){
		var _this = this;
		var vmode = this.viewMode;
		var param = this.readParam;
		
		// DWR → REST API 전환 (2025-10-21)
		// 원본: MailMessageService.removeAttachFile()
		MailAPI.removeAttachFile(uid, folder, part, '/tmp')
			.then(function(result) {
				param.uid = result.newUid;
				if(vmode == "read"){
					_this.readMessage(param);
				} else if(vmode == "list"){
					_this.readSubMessage(param);
				}
			})
			.catch(function(error) {
				console.error('첨부파일 제거 실패:', error);
				alert('첨부파일 제거에 실패했습니다: ' + error.message);
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
		if(param){
			this.listParam = param;
			this.currentFolder = "Sent";									
		}
		this.listMode = "mdnlist";
		this.viewMode = "list";
				
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.mdnAction,
				param,true);
		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.mdnAction,param,"mdnlist");
		
		LayoutInfo.mode = "mdnlist";
	},
	reloadMDNList : function(){
		// 201612 TCUSTOM-2537 저장된 수신확인 목록 페이지 번호를 수신확인 목록 페이지에 적용 
		if(this.mdnListPage != 0){
			this.listParam.page = this.mdnListPage;
			this.mdnListPage = 0; //저장된 수신확인 목록 페이지 번호 초기화 - 1
		}
		
		this.loadMDNList(this.listParam);		
	},
	viewMDNContent:function(param){
		if(param){
			this.readParam = param;
			this.currentFolder = "Sent";									
		}
		this.listMode = "mdnread";
		this.viewMode = "read";
				
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.mdnViewAction,
				param,true);
		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.mdnViewAction,param,"mdnread");
		LayoutInfo.mode = "mdnread";
	},
	recallMessage:function(param){
				
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.recallAction,
				param,true);
	},
	getWriteEmailAddressList:function(isNotOrgSearch){
		var addrList =  this.userAddrList;
		if(!addrList){
			var _this = this;
			
			// DWR → REST API 전환 (2025-10-21)
			// 원본: MailMessageService.getMailAdressList()
			MailAPI.getMailAddressList(isNotOrgSearch)
				.then(function(listObj) {
					_this.setUserAddrList(listObj.addrs);
					addrList = listObj.addrs;
				})
				.catch(function(error) {
					console.error('메일 주소 목록 조회 실패:', error);
				});
		}				
		return addrList;
	},
	setUserAddrList:function(list){
		this.userAddrList = list;
	},
	setSharedParam:function(sharedParam){
		this.sharedFlag = sharedParam.sharedFlag;
		this.listParam.sharedUserSeq = sharedParam.sharedUserSeq;
		this.listParam.sharedFolderName = sharedParam.sharedFolderName;
	},
	getMailListParam:function(){
		return this.listParam;
	},
	loadError:function(isMain){
		var layerId = (isMain)?this.opt.mainLID:this.opt.subLID;		
		ActionBasicLoader.loadAction(layerId,
				this.opt.errorPage,
				{},true);
	}
});

jQuery().ready(function(){
	if(CURRENT_PAGE_NAME == "NORMAL"){		
		//PageMainLoadingManager.initLoadView();
		setTopMenu('mail');
		var mainLayerPane = new LayerPane("m_mainBody","TM_m_mainBody");	
		var menuLayerPane = new LayerPane("m_leftMenu","TM_m_leftMenu",220,180,350);
		var contentLayerPaneWapper = new LayerPane("m_contentBodyWapper","",300,100,500);
		
		
		mainSplitter = new SplitterManager(mainLayerPane,
											menuLayerPane,
											contentLayerPaneWapper,
											"sm","mainvsplitbar","hsplitbar");	
		mainSplitter.setReferencePane(["m_contentBody","copyRight"]);	
		mainSplitter.setSplitter("v",true);
		jQuery(window).autoResize(jQuery("#m_mainBody"),"#copyRight");		
		if(IS_LMENU_USE){loadSideMenu();}
		resizeLeftMenuSize();
		
		mailListControl = new MailListControl();
		mailReadControl = new MailReadControl();
		mailRealtionListControl = new MailRealtionListControl();
		folderControl = new FolderControl();
		mailControl = new MailControl(mailOption);	
		tagControl = new TagControl(tagOption);	
		searchFolderControl = new SearchFolderControl(searchFolderOption);	
		dndManager = new DnDManager(dndOption);
		HistoryManager.loadHistoryManager(historyCallBackFunc);
		chgServiceView(workMailParam.vmode, workMailParam);
		jQuery.removeProcessBodyMask();			
	}
});


function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;
	}
	
	var topAreaSize = jQuery("#ml_btnFMain").outerHeight(true)+4;	
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;	
	extHeight = (IS_USE_EXPRESS_E)?extHeight+((isMsie6)?28:26):extHeight;	
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#m_leftMenu", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontent", 
		mainId:"#m_leftMenu", 
		sideObjId:["#copyRight"],
		wrapperMode:false,
		isNoneWidthChk:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:false});
	
	inResizefunc(jQuery(window),jQuery("#leftMenuRcontentWrapper"),true);
	outResizefunc(jQuery(window),jQuery("#leftMenuRcontent"),true);			
	
	jQuery(window).trigger("resize");
}

function chgWorkMode(){
	var isChange;	
	if(workMailParam.vmode != "mwork"){		
		isChange = chgServiceView("mwork");
		workMailParam.vmode = "mwork";
	} else {
		isChange = true;
		var mode = jQuery.cookie("PM_L");		
		mode = (mode)?mode:"n";
		paneMode = mode;
		contentSplitter.setSplitter(mode);		
	}
	
	return isChange;
}

function chgServiceView(mode,paramObj){	
	var jobStack = [];
	jobStack.push("tag");
	jobStack.push("sfolder");	
	
	if(mode == "mhome" || mode == "intro"){		
		
//		jobStack.push("folder");	
//		jobStack.push("shfolder");	
//		jobStack.push("portletA");	
//		jobStack.push("portletB");	
//		jobStack.push("portletC");	
//		jobStack.push("portletD");	
//		
//		PageMainLoadingManager.startLoad(jobStack);		
		
		jQuery("#m_homeBody").show();
		jQuery("#m_contentMain").hide();
		jQuery("#m_contentSub").hide();
		jQuery("#mailMenubar").hide();
		var contentLayerPane = new LayerPane("m_contentBody","TM_m_contentBody");
		var listLayerPane = new LayerPane("m_homeBody","TM_m_contentHomeMain",300,0,0);
		var previewLayerPane = new LayerPane("m_contentSub","TM_m_contentSub",400,0,0);		
		
		contentSplitter = new SplitterManager(contentLayerPane,
				listLayerPane,
				previewLayerPane,
				"sc","vsplitbar","hsplitbar");
		contentSplitter.setSplitter("n",false);		
		jQuery(window).autoResize(jQuery("#m_contentBody"),"#copyRight");
		contentSplitter.setSplitter("n",false);		
		
		//tagControl.updateTagInfo();
		//searchFolderControl.updateFolderInfo();		
		//folderControl.updateFolderInfo();
		folderInitLoad();
		paneMode = "n";
			
		ActionNotMaskLoader.loadAction("m_homeBody","/mail/home.do?dummy="+makeRandom());
		HistoryManager.historyManagerPush("m_homeBody","/mail/home.do?dummy="+makeRandom(),{},"home");
		
		jQuery("#siSearchBox").hide();

		LayoutInfo.mode = "home";	
		
		jQuery(window).trigger("resize");
				
	} else if(mode == "mwork"){
		
		jQuery("#m_homeBody").hide();
		jQuery("#m_homeBody").empty();
		jQuery("#m_contentMain").show();
		jQuery("#m_contentSub").show();
		jQuery("#mailMenubar").show();
		var contentLayerPane = new LayerPane("m_contentBody","TM_m_contentBody");
		var listLayerPane = new LayerPane("m_contentMain","TM_m_contentMain",300,0,0);
		var previewLayerPane = new LayerPane("m_contentSub","TM_m_contentSub",400,0,0);		
		
		contentSplitter = new SplitterManager(contentLayerPane,
				listLayerPane,
				previewLayerPane,
				"sc","vsplitbar","hsplitbar");
		
		var mode = jQuery.cookie("PM_L");		
		mode = (mode)?mode:"n";
		contentSplitter.setSplitter(mode);		
		jQuery(window).autoResize(jQuery("#m_contentBody"),"#copyRight");		
		paneMode = contentSplitter.getMode();		
		initSubMode();			
		contentSplitter.setSplitter(mode);
		
		
//		jobStack.push("folder");		
//		if(paramObj){
//			if(paramObj.workName == "list"){				
//				jobStack.push("list");	
//			}else if(paramObj.workName == "golist"){				
//				jobStack.push("list");	
//			}else if(paramObj.workName == "write"){				
//				jobStack.push("write");	
//			}else if(paramObj.workName == "manage"){				
//				jobStack.push("manage");	
//			}
//		}
//		
//		//PageMainLoadingManager.startLoad(jobStack);
		
		//tagControl.updateTagInfo();
		//searchFolderControl.updateFolderInfo();
		if(paramObj){
			if(paramObj.workName == "list"){
				folderInitLoad(function(){goFolder("Inbox")});				
			}else if(paramObj.workName == "golist"){				
				folderInitLoad(function(){goFolder(paramObj.folder)});
			}else if(paramObj.workName == "golistparam"){				
				folderInitLoad(function(){goFolderParam(paramObj)});
			}else if(paramObj.workName == "write"){				
				folderInitLoad(function(){goWriteLoad(paramObj)});
			}else if(paramObj.workName == "manage"){				
				goFolderManage();
			}else if(paramObj.workName == "unseen"){				
				folderInitLoad(function(){
					setTimeout(function(){
						viewQuickList('unseen',true,true);
					}, 500);
				});
			}else if(paramObj.workName == "sent"){				
				folderInitLoad(function(){
					setTimeout(function(){
						goFolder("Sent");
					}, 500);
				});
			}
		}				
	}
	
	workMailParam.vmode = mode;
	jQuery(window).trigger("resize");
	return true;
	
}
function goMailHome(){
	LayoutInfo.mode = "home";
	workMailParam.vmode = "mailhome";
	jQuery("#m_homeBody").show();
	jQuery("#m_contentMain").hide().empty();
	jQuery("#m_contentSub").hide().empty();
	jQuery("#mailMenubar").hide();
	jQuery("#menuBarContent").empty();
	jQuery("#menuBarTab").empty();
	contentSplitter.setSplitter("n",false);		
	ActionNotMaskLoader.loadAction("m_homeBody","/mail/home.do?dummy="+makeRandom());
	HistoryManager.historyManagerPush("m_homeBody","/mail/home.do?dummy="+makeRandom(),{},"home");
}

function goFolder(folderName,flag){
	/*
	if(LayoutInfo.mode == "write" && ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
	
	if(!checkEscapeWriteMode()){return;}	
	setStoreScrollHeight(0);
	storeLinkId("link_folder_"+folderName);
	hiddenAdsearchBox();
	jQuery("#mailSearchCancel").hide();
	
	var param = {"folder" : folderName};
	if(flag){
		param.flag = flag;
	}
	param.sharedFlag = "user";	
	$("skword").value="";
	if(chgWorkMode()){
		if(folderName != "Drafts"){
			var mode = jQuery.cookie("PM_L");		
			mode = (mode)?mode:"n";
			contentSplitter.setSplitter(mode);
			paneMode = contentSplitter.getMode();
			setTimeout(function(){
				mailControl.loadMessageList(param);
			}, 200);
		} else {			
			contentSplitter.setNormalSplitter();
			paneMode = "n";
			setTimeout(function(){
				mailControl.loadMessageList(param);
			}, 200);
			
		}
	}
}

function goFolderParam(param){
	/*
	if(LayoutInfo.mode == "write" && ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
	
	storeLinkId("link_folder_"+param.folder);	
	if(chgWorkMode()){		
		jQuery("#mailSearchCancel").hide();
		setTimeout(function(){
			mailControl.loadMessageList(param);		
			contentSplitter.setReloadSplitter();
		}, 200);
	}	
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
	
	$("skword").value="";
	if(chgWorkMode()){		
		setTimeout(function(){
			mailControl.loadMessageList(param);		
		}, 200);
	}
	
	storeLinkId("link_shfolder_"+folderName);
}

function goConfirmFolder(folderName){
	if(confirm(comMsg.comn_newmail_003)){
		goFolder(folderName);
	}
}

function movePage(page){	
	mailControl.movePage(page);
}

function listPage(){
	mailControl.goMessageList();
}

function reloadListPage(){	
	mailControl.reloadMessageList();	
}

function reloadMailPage(){	
	folderControl.updateFolderCountInfo(mailControl.reloadMessageList());		
}

function sortMessage(sortBy, sortDir){
	mailControl.sortMessage(sortBy, sortDir);
}

function goWriteSecureMail(){
	goWrite("expressE");
}

function goWrite(secureWriteMode){
	/*
	if(ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
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
		if(!isPopupWrite && chgWorkMode()){
			storeLinkId("");
			contentSplitter.setNormalSplitter();				
			mailControl.writeMessage(paramObj);
		}else {
			popupWriteLoad(paramObj);
		}
	}
}

function goWriteLoad(paramObj){
	if(!checkEscapeWriteMode()){return;}
	
	if(CURRENT_PAGE_NAME == "NORMAL"){	
		jQuery("#mailSearchCancel").hide();
		if(!isPopupWrite && chgWorkMode()){
			storeLinkId("");
			contentSplitter.setNormalSplitter();		
			mailControl.writeMessage(paramObj);
		} else {
			popupWriteLoad(paramObj);
		}
	} else {
		mailControl.writeMessage(paramObj);
	}
}

function printMessageList(data,textStatus){	
	debugInfo("LIST_RECEIVE_DATA");
	if(textStatus == "success") mailListControl.makeList(mailOption.mainLID,data);
	else mailControl.loadError(true);	
}

function printMessageRead(data,textStatus){
	debugInfo("READ_RECEIVE_DATA");
	if(textStatus == "success"){
		if(data.readResult == "success"){
			if(paneMode == "n") mailReadControl.makeRead(mailOption.mainLID,data);	
			else mailReadControl.makeRead(mailOption.subLID,data);
		} else {
			if(paneMode == "n") mailControl.loadError(true);	
			else mailControl.loadError(false);
		}
	} else {
		if(paneMode == "n") mailControl.loadError(true);	
		else mailControl.loadError(false);
	}
}

function goMDNList(){
	/*
	if(LayoutInfo.mode == "write" && ActionLoader.isloadAction){
		alert(comMsg.comn_loadding_action);
		return;
	}
	*/
	
	if(!checkEscapeWriteMode()){return;}
	jQuery("#mailSearchCancel").hide();
	if(chgWorkMode()){
		storeLinkId("");
		contentSplitter.setNormalSplitter();
		paneMode ="n";
		var param = {page : 1};
		setTimeout(function(){
			mailControl.loadMDNList(param);		
		}, 200);
	}
}

function readMessage(folderName,uid){
	setStoreScrollHeight(jQuery("#m_msgListWrapper").scrollTop());
	var param = {"folder" : folderName, "uid":uid};	
	
	if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
		mailControl.readMessage(param);
	} else {		
		if(paneMode == "n"){
			mailControl.readMessage(param);	
		} else {
			if(currentFolderType != "all" && $("chk_"+ReadSubMessageChecker.currentUid)){
				ReadSubMessageChecker.resetPre(folderName+"_"+uid);
				if(ReadSubMessageChecker.currentUid != uid){
					changeFlagView(true,"S",[folderName+"_"+uid]);
				}
			}
			mailControl.readSubMessage(param);
		}
	}
}

function readHomeMessage(folderName,uid){
	var param = {"folder" : folderName, "uid":uid};
	jQuery("#m_homeBody").hide();	
	jQuery("#m_contentSub").hide();
	jQuery("#mailMenubar").show();
	jQuery("#m_contentMain").show();
	
	setTimeout(function(){
		if(chgWorkMode()){
			loadListToolBar();
			var mode = jQuery.cookie("PM_L");
                	mode = (mode)?mode:"n";
			if(mode != 'n'){
				goFolder(folderName);
			}
			mailControl.readMessage(param);
		}
	},100);
	
}

function initSubMode(){
	if(paneMode != "n"){
		mailControl.initSubMessage();
	}
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
	if(LayoutInfo.mode != "mdnlist" &&
			LayoutInfo.mode != "mdnread"){
		var param = {"folder" : folderName,"keyWord":keyWord};
		if(chgWorkMode()){		
			mailControl.searchMessage(param);			
		}
	} else {
		var param = {"pattern" :keyWord};
		if(LayoutInfo.mode == "mdnlist"){
			mailControl.loadMDNList(param);
		} else if(LayoutInfo.mode == "mdnread"){
			param.uid = $("uid").value;
			mailControl.viewMDNContent(param);
		}		
	}
	
	jQuery("#mailSearchCancel").show();
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
	param.keyWord = keyWord;
	param.folder = folderName;
	param.fromaddr = from;
	param.toaddr = to;
	param.category = condition;
	param.sdate = sdate;
	param.edate = edate;
	param.flag = searchFolderFlag;
	
	if(chgWorkMode()){		
		mailControl.loadMessageList(param);			
	}
	
	hiddenAdsearchBox();
	$("skword").value = "";	
	jQuery("#mailSearchCancel").show();
}

function cancelSearch(){
	var folderName = mailControl.getCurrentFolder();	
	if(LayoutInfo.mode != "mdnlist" &&
			LayoutInfo.mode != "mdnread"){
		folderName = (folderName == "all")?"Inbox":folderName;						
		goFolder(folderName);
	} else {
		var param = {};
		if(LayoutInfo.mode == "mdnlist"){
			mailControl.loadMDNList(param);
		} else if(LayoutInfo.mode == "mdnread"){
			param.uid = $("uid").value;
			mailControl.viewMDNContent(param);
		}		
	}	
	jQuery("#mailSearchCancel").hide();
}

function viewQuickList(type,isAllFolder, isMyFolder){
	
	if(!checkEscapeWriteMode()){
		return;
	}
	if(chgWorkMode()){
		var param = {};
		if(isAllFolder){
			param.folder = "all";
		} else {
			param.folder = mailControl.getCurrentFolder();
		}
		param.page = 1;
		
		if(!isMyFolder){
			param = mailControl.getSharedFolderParam(param);
		}
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
		
		mailControl.loadMessageList(param);
	}
}

function addrGoWrite(addr){
	var uaddr = unescape_tag(addr);	
	var param = {wtype:"send",to:uaddr};
	goWriteLoad(param);		
}