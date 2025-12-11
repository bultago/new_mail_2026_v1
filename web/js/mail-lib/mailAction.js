/*########################## LIST Script ################################*/
var isAllFolder = false;
var pagingInfo;
var oEditors = [];

function checkMessage(chkObj){
	var id = chkObj.id;		
	var bool = chkObj.checked;		
			
	var trObj = chkObj;
	while (trObj.tagName != "TR") {
        trObj = trObj.parentNode;
    }
	
	if(bool){			
		jQuery(trObj).addClass("TM_checkLow");			
		chkMsgHash.set(id,trObj.id);
		chkMsgCnt++;
	} else {
		if(chkMsgCnt > 0){
			jQuery(trObj).removeClass("TM_checkLow");
			chkMsgHash.unset(id);
			chkMsgCnt--;
		}			
	}					
}

function getListMessgesInfo(){
	var msgIds = [];
	var folders = [];
	var subjects = [];
	var sizes = [];
	var checkIds = [];
	
	var dfolder;
	var chkList = chkMsgHash.values();	
	var chkValues,fid,uid,folder,size;
	for ( var i = 0; i < chkList.length; i++) {	   
		chkValues = MsgIdUtil.getID(chkList[i]);
		uid = chkValues.uid;
		fid = chkValues.fid;
		folder = chkValues.folder;
		msgIds.push(uid);
		subjects.push(jQuery.trim(jQuery("#msg_subject_"+fid+"_"+uid).text()));
		size = (jQuery("#chk_"+fid+"_"+uid).attr("mailsize"))?jQuery("#chk_"+fid+"_"+uid).attr("mailsize"):0;
		sizes.push(size);
		checkIds.push(fid+"_"+uid);
		if(isAllFolder){
			folders.push(folder);
		} else {
			dfolder = folder;
		}
	}

	if(!isAllFolder){
		folders.push(dfolder);
	}
	
	return {"uid":msgIds,"folders":folders,"subjects":subjects,"sizes":sizes,"checkIds":checkIds};
} 

function allCheckMessage(chkObj,bool){
	var chkList;
	if(chkObj){
		if(chkObj.length > 1){
			chkList = chkObj;
		} else {
			chkList = [chkObj];
		}
	
		for ( var i = 0; i < chkList.length; i++) {
			if(chkList[i].checked != bool){
				chkList[i].checked = bool;
				checkMessage(chkList[i],bool);	
			}				
		}
		if(LayoutInfo.mode == "list"){
			toggleAllCheckMessage(bool);
		}		
	}
}


function toggleAllCheckMessage(bool){
	
	if(Number(pagingInfo.total) <= Number(pagingInfo.pageBase)){
		return;
	}
	var esize = jQuery("#m_msgListWrapper").data("extSize");
	if(bool){
		var colspanCnt = 7;
		
		if(paneMode == "v"){			
			colspanCnt = 2;
		} else if(currentFolderType == "all"){
			colspanCnt = 8;
		} else if(currentFolderType == "sent" ||
				currentFolderType == "draft" ||
				currentFolderType == "reserved"){
			colspanCnt = 7;
		}		
		
		var allContentHtml = "<tr id='allSelectTr' style='height:55px;display:none;'><td colspan='"+colspanCnt+"' class='TM_allcheckLow' id='allSelectTd' style='padding:5px;height:40px;'>"		
		+"<div id='allSelectUncheck'>"
	    + mailMsg.allselect_001+"<br/>"
	    + "<a href='javascript:;' onclick='onSelectSearchAllMessage()'>"
	    + msgArgsReplace(mailMsg.allselect_002,[pagingInfo.total])
	    + "</a>"
	    +"</div>"
	    +"<div id='allSelectCheck'  style='display:none'>"
	    + mailMsg.allselect_003 +"<br/>"
	    + "<a href='javascript:;' onclick='offSelectSearchAllMessage()'>"
	    + mailMsg.allselect_004
	    + "</a>&nbsp;("+mailMsg.allselect_005+")"
	    +"</div>"	    
	    +"</td></tr>";
	    
	    var allContent = jQuery(allContentHtml);		
		jQuery("table#msgListHeader tr:eq(0)").after(allContent);		
		esize = esize + 55;
		jQuery("#allSelectTr").show();
	} else {
		esize = esize - 55;		
		jQuery("#allSelectTr").remove();
		$("allSearchSelectCheck").value = "off";
	}	
	if(isMsie6){esize +=2;}
	jQuery("#m_msgListWrapper").data("extSize",esize);
	jQuery(window).trigger("resize");
}

function onSelectSearchAllMessage(){
	$("allSearchSelectCheck").value = "on";
	jQuery("#allSelectUncheck").hide();
	jQuery("#allSelectCheck").show();
}

function offSelectSearchAllMessage(){
	$("allSearchSelectCheck").value = "off";
	jQuery("#allSelectUncheck").show();
	jQuery("#allSelectCheck").hide();
}

function changeFlagView(isUsed, flagType, mlowIDs){						
	var tr,readIcon,flagedIcon,chkbox,fname;
	for ( var i = 0; i < mlowIDs.length; i++) {
		fname = getFolderNameEscape(mlowIDs[i]);
		if($(fname) && flagType == "S"){
			$(fname).removeClassName("TM_unseenLow");
			$(fname).removeClassName("TM_checkLow");
			$(fname).removeClassName("TM_mailLow");
			
			if(isUsed){													
				readIcon = jQuery("#"+fname+"_readF");
				readIcon.removeClass("flagUNSE");
				readIcon.addClass("flagSE");
				$(fname).addClassName("TM_mailLow");
			} else {									
				readIcon = jQuery("#"+fname+"_readF");
				readIcon.removeClass("flagSE");
				readIcon.addClass("flagUNSE");
				$(fname).addClassName("TM_unseenLow");
			}
			
			var msgInfo = MsgIdUtil.getID(fname);
			$("chk_"+msgInfo.fid+"_"+msgInfo.uid).checked = false;
			checkMessage($("chk_"+msgInfo.fid+"_"+msgInfo.uid));				
		}

		if(flagType == "F"){
			if(isUsed){
				flagedIcon = jQuery("#"+fname+"_flagedF");
				flagedIcon.attr("src",flagOpt.onFlagedImg);
				flagedIcon.removeClass("flagOFF");
				flagedIcon.addClass("flagON");
				flagedIcon.attr("flaged","false");
			} else {
				flagedIcon = jQuery("#"+fname+"_flagedF");
				flagedIcon.removeClass("flagON");
				flagedIcon.addClass("flagOFF");
				flagedIcon.attr("flaged","true");
			}				
		}
	}

	if(flagType == "S"){
		$("allChk").checked = false;
		folderControl.updateFolderCountInfo();
	}				
}

/*****Tag Remove Function********/
function showTagMenu(tag,event){
	if(currentFolderType != "shared"){
		var id = jQuery(tag).attr("id");	
		var deleteMenu = jQuery("<div nowrap></div>");
		deleteMenu.addClass("sub_menu_wrapper");
		deleteMenu.css({"white-space":"nowrap","width":"40px","pading":"5px","text-align":"center"});
		
		var funcLink = jQuery("<a href='javascript:;' "+
				"onclick='removeEachMessageTagging(\""+id+"\")'>"+
				comMsg.comn_del+"</a>");
		deleteMenu.append(funcLink);	
		
		showTagLayer(id,deleteMenu,event);
	}
}

function removeEachMessageTagging(id){	
	var idx = id.lastIndexOf("_");
	var msgInfo = id.substr(0,idx);	
	msgInfo = msgInfo.substr(2,idx);		
	
	var params = MsgIdUtil.getID(msgInfo);	
	var tagId = id.substr(idx+1, id.length);	
	tagControl.taggingMessage("false",[params.uid],[params.folder],tagId);
	clearSubTagMenu();
}

var oldSubToolID="";
var onTagMenuTimeout;
function showTagLayer(id,obj, event){
	clearSubTagMenu();
	
	var eventTarget = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);	
	var left = eventTarget.offset().left;
	var top = eventTarget.offset().top;	
	
	if(oldSubToolID != id ){		
		var subL = jQuery("<div></div>");
		subL.attr("id","funcSubToolLayer");
		subL.attr("pid",id);
		subL.addClass("sub_m_canvas");	
		subL.append(obj);
		subL.hover(function(){
					var wrap = jQuery(this);										
					wrap.show();
					clearTimeout(onTagMenuTimeout);
				},function(){
					clearSubTagMenu();
				});
		jQuery("body").append(subL.css({"top":top+10,"left":left+10}));
		jQuery("#funcSubToolLayer").css("z-index","3");		
		jQuery("#funcSubToolLayer").show();	
		oldSubMenuID = id;
		onTagMenuTimeout = setTimeout("clearSubTagMenu()",1000);
	} else {
		oldSubMenuID = "";
	}	
}

function clearSubTagMenu(){
	var oldL = jQuery("#funcSubToolLayer");
	if($("funcSubToolLayer")){		
		oldL.hide();		
		oldL.remove();	
		oldSubMenuID = "";
	}
	clearTimeout(onTagMenuTimeout);
}

var addrSubMenuTimeOut;
var addAddrItem;
function onListSubMenu(){
	jQuery("#listSubMenu").show();
	clearTimeout(addrSubMenuTimeOut);
}

function outListSubMenu(){
	jQuery("#listSubMenu").remove();		
}

function makeAddrSubMenu(lid, evt){
	var addrValue = jQuery(evt).attr("addr");
	var menuWrapper = jQuery("#"+lid);
	menuWrapper.css("position","absolute");
	menuWrapper.css("margin-top","5px");			
	
	if(jQuery("#listSubMenu")){
		jQuery("#listSubMenu").remove();
	}		
	var menuWL = jQuery("<div>");
	menuWL.attr("id","listSubMenu");
	menuWL.css("position","relative");		
	
	var opt = {blankImgSrc : "/design/common/image/blank.gif",width:100};
	var linkList =[
		{name:mailMsg.mail_write,link:addrGoWrite},
		{name:mailMsg.menu_addaddr,link:addrAddToBook},
		{name:mailMsg.menu_searchmail,link:addrSearchList}];
	
	if(!MENU_STATUS.addr || MENU_STATUS.addr != "on") {
		linkList = [
		{name:mailMsg.mail_write,link:addrGoWrite},
		{name:mailMsg.menu_searchmail,link:addrSearchList}];
	}
	menuWL.makeSubMenu(opt,linkList,addrValue);
	menuWrapper.append(menuWL);
	menuWrapper.hover(onListSubMenu,outListSubMenu);
	addrSubMenuTimeOut = setTimeout("outListSubMenu()",1000);				
}

function addrAddToBook(addr){	
	addAddrItem = unescape_tag(addr);	
	setAddressList([addAddrItem]);
	viewAddAddrModal();
}

function addrSearchList(addr){
	searchAddr = unescape_tag(addr);	
	var email = get_email(searchAddr);
	searchMessage(email);
}

function sortMessageSelect(){
	var sortItem = $("sortMsgSelect").value;
	if(sortItem != ""){
		sortMessage(sortItem, "desc");
	}		
}


function makeDrag(id){
	dndManager.applyDrag(id);
}
function loadMessageListPage(listInfo){
	debugInfo("LIST_FUNC_START");
	chkMsgCnt = 0;	
	isAllFolder = false;
	chkMsgHash = new Hash();
	
	currentFolderType = listInfo.folderType;
	flagOpt.folderType = currentFolderType;
	
	var totalCnt = listInfo.messageCount;		
	var unreadCnt = listInfo.unreadMessageCount;
	var folderName = listInfo.folderName;
	currentFolderViewName = folderName;
	currentFolder = listInfo.folderFullName;
	
	var currentPage = listInfo.currentPage;
	var pageBase = listInfo.pageBase;
	var totalMessage = listInfo.total;
				
	setTimeout(function(){
		
		var folderList = getDropFolderList();		
		//mailListControl.updatePreview();		
		//jQuery("table#m_messageList td.TM_list_flag").printFlag(flagOpt);
		if(currentFolderType != "shared"){
			mailListControl.updateTag(showTagMenu);
		}
		if(currentFolderType != "reserved" &&  currentFolderType != "shared"){
			dndManager.applyDrop(folderList,"table#m_messageList tr",dropFunc);
		} else {
			dndManager.destroyDrop(folderList);
		}
		
		debugInfo("LIST_END");
		if(PDEBUGLOGGING){
			loggingData("MAIL_LIST");
		}
	},50);
	
	
	pagingInfo = {"total":totalMessage,"pageBase":pageBase};
	
	if(currentFolderType == "all"){
		isAllFolder = true;							
	}
	
	if(!menuBar || menuBar.getMode() != "list"){
		loadListToolBar();
	}	
	loadListToolBar();
	/*menuBar.setPageNavi("p",
			{total:totalMessage,
			base:pageBase,
			page:currentPage,
			isListSet:true,
			isLineCntSet:true,
			pagebase:pageBase,
			changeAfter:reloadListPage});*/
	
	menuBar.setPageNaviBottom("p",
			{total:totalMessage,
		base:pageBase,
		page:currentPage,
		isListSet:true,
		isLineCntSet:true,
		pagebase:pageBase,
		changeAfter:reloadListPage});
		
	menuBar.toggleSideItem(true);
	
	if(skin != "skin3"){
		setMailMenuBarStatus();
	}else{
		setMailMenuBarStatusSkin3();
	}
	
	folderControl.updateFolderCountInfo();
	
	currentFolderName = folderName;	
	updateWorkReadCount(folderName,totalCnt,unreadCnt);
	
	
	var currentFolderEnc = getFolderNameEscape(currentFolder);
	storeLinkId("link_folder_"+currentFolderEnc);
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#m_msgListWrapper", 
		mainId:"#m_contentMain", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:58});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#m_msgListContent", 
		mainId:"#m_contentMain", 
		sideObjId:["#copyRight"],
		wrapperMode:false,
		isNoneWidthChk:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:29});
	
	/*
	listScrollTop = 0;
	$("m_msgListWrapper").scrollTop = listScrollTop;
	*/
	inResizefunc(jQuery(window),jQuery("#m_msgListWrapper"),true);
	outResizefunc(jQuery(window),jQuery("#m_msgListContent"),true);	
	
	$("m_contentMain").style.overflow = "auto";
	
	settingMailListEvent();
	
	jQuery("#siSearchBox").show();
	$("skword").focus();
	//jQuery("body").bind("keydown",scrollDownEvent);
	listInfo = null;
	debugInfo("LIST_FUNC_END");
	if(isMsie6)jQuery(window).trigger("resize");
	
	setCurrentPage(currentPage);
	
	
	if(isMsie)
		jQuery("#m_msgListWrapper").scrollTop(getStoreScrollHeight());
	else{
		setTimeout(function(){
			jQuery("#m_msgListWrapper").scrollTop(getStoreScrollHeight());
		},50);
	}
}

function loadHtmlMessageListPage(listInfo, tagArray, sharedParam, searchParam){
	//debugInfo("LIST_FUNC_START");
	chkMsgCnt = 0;	
	isAllFolder = false;
	chkMsgHash = new Hash();
	
	currentFolderType = listInfo.folderType;
	flagOpt.folderType = currentFolderType;
	
	var totalCnt = listInfo.messageCount;		
	var unreadCnt = listInfo.unreadMessageCount;
	var folderName = listInfo.folderName;
	currentFolderViewName = folderName;
	currentFolder = listInfo.folderFullName;
	mailControl.setCurrentFolder(currentFolder);
	mailControl.setSharedParam(sharedParam);
	mailControl.setSearchParam(searchParam);
	
	var currentPage = listInfo.currentPage;
	var pageBase = listInfo.pageBase;
	var totalMessage = listInfo.total;

	pagingInfo = {"total":totalMessage,"pageBase":pageBase};
	
	if(currentFolderType == "all"){
		isAllFolder = true;							
	}

	/*menuBar.setPageNavi("p",
			{total:totalMessage,
			base:pageBase,
			page:currentPage,
			isListSet:true,
			isLineCntSet:true,
			pagebase:pageBase,
			changeAfter:reloadListPage});*/
	
	menuBar.setPageNaviBottom("p",
			{total:totalMessage,
			base:pageBase,
			page:currentPage,
			isListSet:true,
			isLineCntSet:true,
			pagebase:pageBase,
			changeAfter:reloadListPage});
		
	menuBar.toggleSideItem(false);
		
	if(skin != "skin3"){
		setMailMenuBarStatus();
	}else{
		setMailMenuBarStatusSkin3();
	}
	
	currentFolderName = folderName;
	updateWorkReadCount(folderName,totalCnt,unreadCnt);
	
	jQuery("#siSearchBox").show();
	$("skword").focus();
	listInfo = null;
	//debugInfo("LIST_FUNC_END");
	
	setCurrentPage(currentPage);
}

var listScrollTop = 0;
function scrollDownEvent(event){	
	var KEY = {
		UP: 38,
		DOWN: 40		
	};
	var pane = $("m_msgListWrapper"); 
	if(pane){
		var top = pane.scrollTop;	
		switch(event.keyCode) {
			case KEY.UP:
				listScrollTop = (top > 0)?top-25:0;
				break;
			case KEY.DOWN:
				listScrollTop = top+25;
				break;
		}
		
		pane.scrollTop = listScrollTop;
	} else {
		jQuery("body").unbind("keydown",scrollDownEvent);
	}
}

/*########################## READ Script ################################*/
var isMDNSend = 'false'; 
var readMsgData;

function getMessageText(){
	return $("messageText").value;
}

var contentFrameWidth;
function resizeTextFrame(height,width){
	//
	$("messageContentFrame").style.height=height+"px";
	jQuery("#messageContentFrame").css({"overflow-x":"hidden","overflow-y":"hidden"});
	contentFrameWidth = width;	
	var func;
	if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
		func = function(){
			var wrapper = jQuery("#readPopupFrame");
			var parentWrapper = jQuery("#readPopupFrame").parent();
			if(parentWrapper.width() < contentFrameWidth){
				wrapper.css("width",(contentFrameWidth+30)+ "px");
			} else {			
				wrapper.css("width",(isMsie)?"":"100%");			
			}
		};
		jQuery(window).bind("resize",func);
	} else {
		func = function(){
			var wrapper = jQuery("#readWrapper");
			var parentWrapper = jQuery("#readWrapper").parent();
			if(parentWrapper.width() < contentFrameWidth){
				wrapper.css("width",(contentFrameWidth+30)+ "px");
			} else {			
				wrapper.css("width",(isMsie)?"":"100%");			
			}
		};
		jQuery(window).bind("resize",func);
	}
	func();	
}

function resizeHtmlTextFrame(height, width){
	jQuery("#messageContentFrame").height(height+25+"px");
	jQuery("#messageContentFrame").css({"overflow-x":"hidden","overflow-y":"hidden"});
	
	contentFrameWidth = width;	
	var func = function(){
		var parentWrapper = jQuery("#messageReadWrap");
		var wrapper = jQuery("#messageReadContent");
		if(parentWrapper.width() < contentFrameWidth){
			wrapper.css("width",(contentFrameWidth+5)+ "px");
		} else {			
			wrapper.css("width","100%");			
		}
	};
	jQuery(window).bind("resize",func);
	func();	
}

function toggleHeaderInfo(setValue){
	var toggleBtn = $("btnRcptInfo");
	var infoLayer = jQuery("#rmsg_info");
	var settingValue;

	if(setValue){
		settingValue = setValue;
	} else {
		settingValue = infoLayer.css("display");		
	}

	if(settingValue == "none"){
		infoLayer.show();
		toggleBtn.className = "openRcptBtn";
		setCookie("TSHIF","T",365);
	} else {
		infoLayer.hide();
		toggleBtn.className = "closeRcptBtn";
		setCookie("TSHIF","T",-1);
	}
}

function toggleEmailAddress(type){
	var moreAddr = jQuery("#moreAddr"+type);
	var moreBtn = jQuery("#moreAddr"+type+"Btn");
	if(moreAddr.css("display") == "none"){
		moreAddr.show();
		moreBtn.html(comMsg.comn_close);
	} else {
		moreAddr.hide();
		moreBtn.html(comMsg.comn_more);
	}
}

function makeRcptAddrMore(type,lid,addrList){
	var addrStr = new StringBuffer();
	var listSize = addrList.length;
	var isOverAddr = false;
	var maxSize = listSize;
	
	if(listSize > 5){
		isOverAddr = true;
		maxSize = 5;
	}
	
	for ( var i = 0; i < maxSize; i++) {		
		addrStr.append(addrList[i]);		
	}
	
	if(isOverAddr){
		addrStr.append("<span id='moreAddr"+type+"' style='display:none'>");
		for ( var i = 5; i < listSize; i++) {
			addrStr.append(addrList[i]);	
		}
		addrStr.append("</span>");
		addrStr.append("<span class='smoreBtn' id='moreAddr"+type+"Btn' onclick='toggleEmailAddress(\""+type+"\")'>");
		addrStr.append(comMsg.comn_more);
		addrStr.append("</span>");
	}
	
	
	$(lid).innerHTML = addrStr.toString();
}

function toggleAttachInfo(setValue){	
	var infoLayer = jQuery("#attachList");
	var settingValue;

	if(setValue){
		settingValue = setValue;
	} else {
		settingValue = infoLayer.css("display");		
	}

	if(settingValue == "none"){
		infoLayer.show();
		setCookie("TSAIF","T",365);
	}else {
		infoLayer.hide();
		setCookie("TSAIF","T",-1);
	}
		
}

function downloadAllAttach(uid,folder){
	var part = jQuery("#attSaveAllBtn").attr("allpart");	
	if(jQuery.trim(part) == ""){
		alert(mailMsg.alert_download_nofile);
		return;
	}	
	part = part.substr(0, part.length-1);	
	var param = {"folder":folder, "uid":uid, "part":part};
	param = mailControl.getSharedFolderParam(param);	
	$("reqFrame").src = "/mail/downloadAllAttach.do?"+jQuery.param(param);			
}

function downLoadAttach(uid, folder, part){
	var param = {"folder":folder, "uid":uid, "part":part,"type":"normal"};
	param = mailControl.getSharedFolderParam(param);
	$("reqFrame").src = "/mail/downloadAttach.do?"+jQuery.param(param);	
}

function downLoadTnefAttach(uid, folder, part, tnefKey){
	var param = {"folder":folder, "uid":uid, "part":part,"type":"tnef","tnefKey":tnefKey};
	param = mailControl.getSharedFolderParam(param);
	$("reqFrame").src = "/mail/downloadAttach.do?"+jQuery.param(param);
}

function deleteAttachFile(uid, folder, part){
	if(!confirm(mailMsg.confirm_delete)){
		return;
	}
	mailControl.removeAttachFile(uid, folder, part);
}

function readNestedMessage(uid, folder, part){
	var popupReadForm = document.popupReadForm;
	popupReadForm.uid.value = uid;
	popupReadForm.folder.value = folder;	
	
	var param = {};
	param = mailControl.getSharedFolderParam(param);	
	popupReadForm.sharedFlag.value = param.sharedFlag;
	popupReadForm.sharedUserSeq.value = param.sharedUserSeq;
	popupReadForm.sharedFolderName.value = param.sharedFolderName;
	popupReadForm.part.value = part;
	
	var wname = "popupRead"+makeRandom();
	var popWin = window.open("about:blank",wname,"resizable=yes,scrollbars=yes,width=800,height=640");
	popupReadForm.method = "post";
	popupReadForm.action="/dynamic/mail/readNestedMessage.do";
	popupReadForm.target = wname;
	popupReadForm.submit();		
}

function viewSource(folder,uid){	

	var sourcePopOpt = clone(popupOpt);
	sourcePopOpt.minHeight = 640;
	sourcePopOpt.minWidth = 560;
	sourcePopOpt.btnList = [];
	
	sourcePopOpt.openFunc = function(){
		var param = {"folder":folder, "uid":uid};
		param = mailControl.getSharedFolderParam(param);
		$("sourceFrame").src = "/mail/viewMessageSource.do?"+jQuery.param(param);
		jQuery("#sourceFrame").css("height",(sourcePopOpt.minHeight - 70)+"px");
	};
	
	sourcePopOpt.closeFunc = function(){
		$("sourceFrame").src = "about:blank";
	};
	jQuery("#m_sourceView").jQpopup("open",sourcePopOpt);

}

function viewMailFromIp(folderName, uid) {
	var popOpt = clone(popupOpt);
	popOpt.minHeight = 250;
	popOpt.minWidth = 350;
	popOpt.openFunc = function() {
		jQuery("#geoIpLoadingbar").show().loadbar();
		var param = mailControl.getMailListParam();
		param.folder = folderName;
		param.uid = uid;
		jQuery.post("/dynamic/mail/viewMailFromIp.do", param, function(data) {
			jQuery("#geoIpLoadingbar").hide();
			var $geoIptable = jQuery("#geoIpListTable");
			if (data && data.length > 0) {
				for (var i=0; i<data.length; i++) {
					$geoIptable.append(jQuery("<tr><td class='centerText'>"+(i+1)+"</td><td class='centerText'>"+data[i].ip+"</td><td class='centerText'>"+data[i].country+"</td></tr>"));
				}		
			} else {
				$geoIptable.append(jQuery("<tr><td colspan='3' class='centerText'>"+mailMsg.mail_geoip_ip_none+"</td></tr>"));
			}
		}, "json");
	};
	popOpt.closeFunc = function() {
		jQuery("#geoIpListTable tr").remove();
	};
	jQuery("#geoIpViewer").jQpopup("open",popOpt);
}
 
function viewRelationMsg(folder,uid, sortDesc){	
	var viewPane = jQuery("#relationMsgPane");
	var isOpenRelation = viewPane.data("openRelation");
	isOpenRelation = (isOpenRelation)?isOpenRelation:false;
	if(!isOpenRelation){
		viewPane.show();
		sortDesc = (sortDesc)?sortDesc:"desc";		
		var param = {"folder":folder,"uid":uid,"sortDir":sortDesc};
		jQuery("#relationMsgPane").data("param",param);
		jQuery.post("/dynamic/mail/listRelationMessage.do", param, function(data, textStatus){
			$("relationMsgPane").innerHTML = mailRealtionListControl.makeList(data);
			jQuery("#relationMsgPane").data("openRelation",true);
		}, "json");
		
	} else {
		viewPane.hide();
		$("relationMsgPane").innerHTML = "";
		jQuery("#relationMsgPane").data("openRelation",false);
	}	
}
function moveRelationList(page,sortDesc){
	var param = jQuery("#relationMsgPane").data("param");
	param.page = page;
	if(sortDesc){
		param.sortDir = sortDesc;
	}
	jQuery.post("/dynamic/mail/listRelationMessage.do", param, function(data, textStatus){
		$("relationMsgPane").innerHTML = mailRealtionListControl.makeList(data);		
	}, "json");
}




function addAddr(type){	
	var addrList = [];
	var f = document.readMessageForm;	
	var addAddrForm;
	
	if(type == "from"){		
		addAddrForm = jQuery("input[name=fromAddAddr]");		
	} else if(type == "to"){
		addAddrForm = jQuery("input[name=toAddAddr]");
	} else if(type == "cc"){
		addAddrForm = jQuery("input[name=ccAddAddr]");
	} else if(type == "bcc"){
		addAddrForm = jQuery("input[name=bccAddAddr]");
	}	
	
	for ( var i = 0; i < addAddrForm.length; i++) {		
		addrList[i] = addAddrForm[i].value;				
	}

	setAddressList(addrList);	
	viewAddAddrModal();
}

function readViewImg(folder,uid){
	var param = {};
	param.folder = folder;
	param.uid = uid;
	param.viewImg = "on";

	param = mailControl.getSharedFolderParam(param);
	
	if(paneMode == "n"){		
		mailControl.readMessage(param);
	} else {
		mailControl.readSubMessage(param);		
	}
}

function confirmIntegrity(folder,uid){
	jQuery("#integrityMsg").text(mailMsg.mail_integrity_check);
	jQuery("#integrityBtn").hide();
	var resultFunc = updateIntegrity;
	
	// DWR → REST API 전환 (2025-10-21)
	// 원본: MailMessageService.getMessageIntegrity()
	MailAPI.getMessageIntegrity(folder, uid)
		.then(function(resultObj) {
			resultFunc(resultObj);
		})
		.catch(function(error) {
			console.error('메일 무결성 검사 실패:', error);
			updateIntegrity({result: "error", integrity: "error"});
		});
}

function updateIntegrity(resultObj){
	var isError = false;
	var msgPane = jQuery("#integrityMsg"); 
	if(resultObj.result == "success"){
		if(resultObj.integrity == "match"){
			msgPane.text(mailMsg.mail_integrity_match);
		} else if(resultObj.integrity == "no-match"){
			msgPane.text(mailMsg.mail_integrity_nomatch);
		} else if(resultObj.integrity == "no"){
			msgPane.text(mailMsg.mail_integrity_no);
		} else if(resultObj.integrity == "error"){
			isError = true;
		}
	} else {
		isError = true;
	}

	if(isError){
		msgPane.text(mailMsg.mail_integrity_error);
	}
	jQuery("#integrityBtn").show();		
}

var ReadSubMessageChecker = {
	currentUid:"",
	resetPre:function(uid){
		uid = getFolderNameEscape(uid);		
		if(uid != ReadSubMessageChecker.currentUid &&				
				ReadSubMessageChecker.currentUid != "" ){
			if($("chk_"+ReadSubMessageChecker.currentUid).checked){
				jQuery("#chk_"+ReadSubMessageChecker.currentUid).trigger("click");
			}
		}
	},
	setCurrent:function(uid){
		uid = getFolderNameEscape(uid);		
		if(uid != ReadSubMessageChecker.currentUid){		
			jQuery("#chk_"+uid).trigger("click");
			ReadSubMessageChecker.currentUid = uid;
		}
	},
	resetUid:function(){
		ReadSubMessageChecker.currentUid = "";
	}
};


function loadMessageReadPage(){
	if(CURRENT_PAGE_NAME == "MAILREADPOPUP"){
		
		setTitleBar(jQuery("#msg_subject_"+readMsgData.uid).text());
		if(isPopupload){
			return;
		}
		isAllFolder = false;
		chkMsgHash = new Hash();
		
		loadListToolBar();
		if(skin == "skin3"){
			setMailMenuBarStatusSkin3();
		}
		//menuBar.toggleSideItem(false);
		/*
		menuBar.setPageNavi("n",{preUid:readMsgData.preUid,
							nextUid:readMsgData.nextUid,
							isListSet:false,							
							folderName:readMsgData.folderEncName});
		*/
		menuBar.toggleSideItem(true);
		menuBar.setPageMenuNavi("n",{preUid:readMsgData.preUid,
			nextUid:readMsgData.nextUid,
			isListSet:false,							
			folderName:readMsgData.folderEncName});
		chkMsgCnt = 1;
		chkMsgHash.set(readMsgData.fid,readMsgData.fid);
		folderControl = new FolderPopupControl();		
		mailControl = new MailPopupControl(mailOption);
		tagControl = new TagPopupControl();
		mailRealtionListControl = new MailRealtionListControl();
		
		dwr.engine.setAsync(true);
		tagControl.updateTagListInfo();
		folderControl.updateFolderListInfo();

		var sharedParam = {};
		var isShared = (readMsgData.sharedFlag== "shared")?true:false;
		sharedParam.sharedFlag = isShared;
		sharedParam.sharedUserSeq = readMsgData.sharedUserSeq;
		sharedParam.sharedFolderName = readMsgData.sharedFolderName;		
		mailControl.setSharedParam(sharedParam);
		//var listParam = (opener && opener.getMailControlListParam)?opener.getMailControlListParam():{};
		var listParam = {};
		try{
			listParam = (opener && opener.getMailControlListParam)?opener.getMailControlListParam():{};
		}catch(e){}
		mailControl.setMailListParam(listParam);
		
		LayoutInfo.mode="read";
		paneMode = "n";
		HistoryManager.loadHistoryManager(historyCallBackFunc);		
		isPopupload = true;
	} else {
		debugInfo("READ_FUNC_START");
		removePreview();
		currentFolderViewName = getSFolderName(readMsgData.folderFullName);
		currentFolder = readMsgData.folderFullName;		
		folderControl.updateFolderCountInfo();		
		
		
		if(skin == "skin3"){
			loadListToolBar();
			setMailMenuBarStatusSkin3();
		}
		
		if(paneMode == "n"){
			if(!menuBar || menuBar.getMode() != "list"){
				loadListToolBar();
			}
			//menuBar.toggleSideItem(false);
			/*
			menuBar.setPageNavi("n",{preUid:readMsgData.preUid,
								nextUid:readMsgData.nextUid,
								isListSet:true,							
								folderName:readMsgData.folderEncName});			
			*/
			menuBar.toggleSideItem(true);
			menuBar.setPageMenuNavi("n",{preUid:readMsgData.preUid,
				nextUid:readMsgData.nextUid,
				isListSet:true,							
				folderName:readMsgData.folderEncName});
			chkMsgCnt = 1;
			chkMsgHash = new Hash();
			chkMsgHash.set(readMsgData.fid,readMsgData.fid);
			$("m_contentMain").scrollTop = 0;
			jQuery("#m_contentMain").show();
			$("m_contentMain").style.overflow = "auto";
		} else {
			if(!chkMsgHash){
				chkMsgHash = new Hash();
			}			
			$("m_contentSub").scrollTop = 0;
			$("m_contentSub").style.overflow = "auto";			
			ReadSubMessageChecker.setCurrent(readMsgData.fid);
			jQuery("#"+readMsgData.fid).removeClass("TM_unseenLow");
			jQuery("#"+readMsgData.fid).addClass("TM_mailLow");
			LayoutInfo.mode = "list";
		}
		//jQuery("body").bind("keydown",scrollDownEvent);		
		jQuery("#siSearchBox").show();
		debugInfo("READ_FUNC_END");
		debugInfo("READ_END");
		if(PDEBUGLOGGING){
			loggingData("MAIL_READ");
		}
	}
	
	if(isMDNSend == 'true'){
		if(confirm(mailMsg.mail_mdn_response_confirm)){
			var param = {"folder":readMsgData.folderEncName, "uid":readMsgData.uid};
			param = mailControl.getSharedFolderParam(param);
			$("reqFrame").src = "/dynamic/mail/sendMDNResponses.do?"+jQuery.param(param);			
		}
	}

	var aInfoDisplay = getCookie("TSAIF");
	var hInfoDisplay = getCookie("TSHIF");
	var attFileCnt = readMsgData.filesLength;
	if(attFileCnt > 0 && aInfoDisplay == "T"){
		toggleAttachInfo("none");
	}	
	if(hInfoDisplay == "T"){
		toggleHeaderInfo("none");
	}
	if(isMsie){jQuery(window).trigger("resize");}
}

function loadHtmlMessageReadPage(){		
	
	//debugInfo("READ_FUNC_START");
	currentFolderViewName = getSFolderName(readMsgData.folderFullName);
	currentFolder = readMsgData.folderFullName;		
	folderControl.updateFolderCountInfo();	
	mailControl.setCurrentFolder(currentFolder);
	
	if(skin == "skin3"){
		setMailMenuBarStatusSkin3();
	}
	/*
	menuBar.toggleSideItem(false);
	menuBar.setPageNavi("n",{preUid:readMsgData.preUid,
						nextUid:readMsgData.nextUid,
						isListSet:true,							
						folderName:readMsgData.folderEncName});			
	*/
	menuBar.toggleSideItem(true);
	menuBar.setPageMenuNavi("n",{preUid:readMsgData.preUid,
						nextUid:readMsgData.nextUid,
						isListSet:true,							
						folderName:readMsgData.folderEncName});
	chkMsgCnt = 1;
	chkMsgHash = new Hash();
	chkMsgHash.set(readMsgData.fid,readMsgData.fid);

	jQuery("#siSearchBox").show();
	/*debugInfo("READ_FUNC_END");
	debugInfo("READ_END");
	if(PDEBUGLOGGING){
		loggingData("MAIL_READ");
	}*/

	if(isMDNSend == 'true'){
		if(confirm(mailMsg.mail_mdn_response_confirm)){
			var param = {"folder":readMsgData.folderEncName, "uid":readMsgData.uid};
			param = mailControl.getSharedFolderParam(param);
			$("reqFrame").src = "/dynamic/mail/sendMDNResponses.do?"+jQuery.param(param);			
		}
	}

	var aInfoDisplay = getCookie("TSAIF");
	var hInfoDisplay = getCookie("TSHIF");
	var attFileCnt = readMsgData.filesLength;
	if(attFileCnt > 0 && aInfoDisplay == "T"){
		toggleAttachInfo("none");
	}	
	if(hInfoDisplay == "T"){
		toggleHeaderInfo("none");
	}
}

function getReadMessgesInfo(){
	var msgIds = [readMsgData.uid];
	var folders = [readMsgData.folderFullName];
	var subjects = [jQuery.trim(jQuery("#msg_subject_"+readMsgData.uid).text())];
	var sizes = [readMsgData.size];
	var checkIds = [readMsgData.uid];
	return {"uid":msgIds,"folders":folders,"subjects":subjects,"sizes":sizes,"checkIds":checkIds};	
}

function registBayesianRuleResult(data, textStatus){
	if(textStatus == "success"){
		if(data.result == "success"){
			alert(mailMsg.bayesian_submitmsg);
			readMessage(data.folderName,data.uid);			
		} else {
			alert(mailMsg.bayesian_submitmsg_error);
		}
	} else {
		alert(mailMsg.bayesian_submitmsg_error);
	}	
}

function registBayesianRuleMessage(ruleType, folderName, uid){
	var param = {};
	param.ruleType = ruleType;
	param.folderName = folderName;
	param.uid = uid;

	jQuery.post("/mail/registBayesianRule.do", param, registBayesianRuleResult, "json");	
}


/*########################## WRITE Script ################################*/
var isOcxUpload = false;
var isOcxUploadDownModule = false;
var letterWidth = "600px";
var isLetter = false;
var isDocTemplate = false;
var letterIdx = -1;
var isAutoSave = false;
var sendMailType;
var isSendWork = false;
var secureMailModule = "tms";
var userDN = "";
var secureMailMakeMode = "";
var IS_XPRESS_MAIL_WRITE = false;
var EXPRESS_MAX_SEND = 0;
var isSendInfoCheckUse = false;

function sendNormalMessage(){
	if(isAutoSave){
		alert(mailMsg.write_alert008);
		return;
	}
	clearTimeout(autoSaveTimeTerm);
	isSendWork = true;
	
	if(isRcptModeNoneAC){
		sendMailType = "normal";
		searchRcptAddr();
	} else {
		sendMessage("normal");
	}		
}

function sendDraftMessage(){
	if(isAutoSave){
		alert(mailMsg.write_alert008);
		return;
	}	
	clearTimeout(autoSaveTimeTerm);
	isSendWork = true;
	
	if(isRcptModeNoneAC){
		sendMailType = "draft";
		searchRcptAddr();
	} else {
		sendMessage("draft");
	}
}

function sendMessage(type) {
	
	var param = {};
	var f = document.writeForm;	
	
	if(!isRcptModeNormal)makeRcptForm();
	var toObj = $("to");
	var ccObj = $("cc");
	var bccObj = $("bcc");
	if(isRcptModeNoneAC){
		toObj = $("toTempVal");
		ccObj = $("ccTempVal");
		bccObj = $("bccTempVal");
	}	
	
	var massFilePath = $("massFilePath").value;
	var subject = $("subject").value;
	var to = toObj.value;	
	var cc = ccObj.value;
	var bcc = bccObj.value;	
	
	if (!(type != "draft" && massFilePath != "")  &&
			type == "normal" && jQuery.trim(to) == "") {
		if(isRcptModeNormal)alert(mailMsg.error_norecipient);
		else alert(mailMsg.error_norecipient2);
		if(isRcptModeNormal)$("to").focus();
		return;
	}
	
	if(!(type != "draft" && massFilePath != "") &&
			!checkEmailInvalidAddress(to)) {
		if(isRcptModeNormal)$("to").focus();
		return;
	}

	if(!(type != "draft" && massFilePath != "") &&
			!checkEmailInvalidAddress(cc)) {
		if(isRcptModeNormal)$("cc").focus();
		return;
	}

	if(!(type != "draft" && massFilePath != "") &&
			!checkEmailInvalidAddress(bcc)) {
		if(isRcptModeNormal)$("bcc").focus();
		return;
	}
	
	if (jQuery.trim(subject) == "") {
		alert(mailMsg.alert_nosubject);
		$("subject").focus();
		return;
	}
	
	if(!checkInputText($("subject"),1,255,false)){
		return;
	}
	
	param.subject = subject;	
	param.to = to;	
	param.cc = cc;
	param.bcc = bcc;
	param.sendType = type;
	param.sendFlag = $("sendFlag").value;
	param.wmode = $("editorMode").value;
	param.uids = $("uid").value;
	param.folder = $("folder").value;
	param.senderName = $("senderName").value;
	param.senderEmail = $("senderEmail").value;
	param.encharset = $("encharset").value;
	param.draftMid = $("draftMid").value;
	param.trashMid = $("trashMid").value;
	param.sendViewMode = $("sendViewMode").value;
	
	
	if(type != "draft" && massFilePath != ""){
		param.massRcpt = $("massFilePath").value;
		param.massMode = "on";
		$("onesend").checked = true;
		if(to == ""){
			param.to = "";
		}
		if(cc == ""){
			param.cc = "";
		}
		if(bcc == ""){
			param.bcc = "";
		}
	} else {
		param.massMode = "off";
	}
	
	if(type != "draft" && 
			massFilePath == "" &&
			isSendInfoCheckUse) checkSendInfoValue(param);
	else sendMessageParamProcess(param);
	
}
function sendMessageParamProcess(param){
	var type = param.sendType;
	var contentValue = getMessageContent(type);
	var quotaInfo = folderControl.getQuotaInfo();
	var massFilePath = $("massFilePath").value;
	
	if(Number(quotaInfo.percent) >= 100){
		if(!confirm(mailMsg.mail_send_quotaover_confirm)){
			return;
		} else {
			$("receivenoti").checked = false;
			$("reservation").checked = false;
			if($("securemail")){
				$("securemail").checked = false;
			}
			$("savesent").checked = false;			
		}
		if(type == "draft"){
			alert(mailMsg.mail_send_quotaover_draft);
			return;
		}		
	}	
		
	if(type != "draft" && $("onesend").checked){
		param.onesend = "on";
	}
	
	if(type != "draft" && $("receivenoti").checked && editorMode == 'html'){
		param.receivenoti = "on";		
	}
	
	if(type != "draft" && $("priority").checked){
		param.priority = 2;
	}
	
	if(type != "draft" && $("signAttach").checked){
		var signSeq = $("signList").value;
		if(parseInt(signSeq) > 0){
			param.attachsign = "on";
			param.signseq = signSeq;
		}		
	}
	
	if(type != "draft" && $("vcardAttach").checked){
		param.attachvcard = "on";
	}
	
	if(type != "draft" && $("savesent").checked){
		param.savesent = "on";
	}
	
	if(!IS_XPRESS_MAIL_WRITE){
		param.letterMode = $("letterMode").value;
		param.letterSeq = $("letterSeq").value;
		if(type != "draft" && $("reservation").checked){
			if(jQuery("#resevDate").val() == ""){
				alert(mailMsg.alert_emptyreserved);
				return;
			}
			var maxReservedDay = jQuery("#resevDate").data("maxReservedDay");
			var reservDate = jQuery("#resevDate").val().split('-');		
			
			if(!checkReserveTime(reservDate[0],reservDate[1],reservDate[2],
					jQuery("#resevHour").val(),jQuery("#resevMin").val(),maxReservedDay)){
				return;
			}
			
			param.reservation = "on";
			param.reservYear = reservDate[0];
			param.reservMonth = reservDate[1];
			param.reservDay = reservDate[2];
			param.reservHour = jQuery("#resevHour").val();
			param.reservMin = jQuery("#resevMin").val();
			param.sendType = "reserved";
		}
		
		if(type != "draft" && 
				$("securemail") && 
				$("securemail").checked){
			var smailPassword = $("smailPassword").value;
			var smailHint = $("smailHint").value;
			smailPassword = jQuery.trim(smailPassword);
			smailHint = jQuery.trim(smailHint);
			if(smailPassword == ""){
				alert(mailMsg.error_secure_pass_null);
				$("smailPassword").select();
				return;			
			}
			
			if(!isAlphabetNumber(smailPassword)){
				alert(mailMsg.error_secure_pass_invalid);
				$("smailPassword").select();
				return;			
			}
			
			if(smailHint == ""){
				alert(mailMsg.error_secure_hint_null);
				$("smailPassword").select();
				return;			
			}
			
			param.secure = "on";
			param.securePass = smailPassword;
			param.secureHint = smailHint;		
		} else if(type == "draft" && 
				$("securemail") && 
				$("securemail").checked){
			alert(mailMsg.error_secure_draft);		
			return;		
		}
		
		var sendConfirmMsg = "";
		if(type != "draft" && 
				$("securemail") && 
				$("securemail").checked &&
				secureMailModule == "tms"){
			sendConfirmMsg = mailMsg.mail_secure_confirm;		
		} else {
			if(type == "draft"){
				sendConfirmMsg = mailMsg.mail_send_drafts_confirm;
			} else {
				if($("receivenoti").checked && !$("savesent").checked){
					sendConfirmMsg = mailMsg.confirm_savesent + "\n" +mailMsg.mail_send_confirm;
				} else {
					sendConfirmMsg = mailMsg.mail_send_confirm;
				}
			}		
		}
		
		if(!confirm(sendConfirmMsg)){
			return;
		}
		
		
		if (type == "draft") {
			var hugeSize;
			
			hugeSize = basicAttachUploadControl.getAttachSize("hugeUse");
			if (hugeSize >0) {				
				if (!confirm(mailMsg.confirm_draftbigattach)) {
					return;
				}
			}
			
			var hugeFileList = basicAttachUploadControl.getFileList("huge");
			for (var i = 0; i < hugeFileList.length; i++) {
				basicAttachUploadControl.deleteAttachList(hugeFileList[i].id);
			}				
		
			
			param.letterMode = "off";
			
		}
		
		param.content =  contentValue;
		requestSendAction(param);
		
		
	} else {		
		if(secureMailMakeMode == "PKI"){
			var sendDnList = null;
			var toEmails = getOnlyEmailArray(param.to);
			var ccEmails = getOnlyEmailArray(param.cc);
			var bccEmails = getOnlyEmailArray(param.bcc);		
			var totalEmails = jQuery.merge(toEmails,ccEmails);
			totalEmails = jQuery.merge(totalEmails,bccEmails);
			
			if(EXPRESS_MAX_SEND < totalEmails.length){			
				alert(msgArgsReplace(mailMsg.mail_secure_maxrcpt,[EXPRESS_MAX_SEND]));
				return;
			}
			MailCommonService.searchAccountDN(totalEmails,function(res){
				if(res.result == "success"){					
					sendDnList = res.dnList;
					requestExpressSendAction(sendDnList,param);
				}
			});	
		}
	}	
}

function requestExpressSendAction(sendDnList, param){
	if(!sendDnList){
		alert(mailMsg.mail_secure_notdn);
		return;
	}
	
	if(param.savesent == "on"){
		var isCheck = false;
		for (var i = 0; i < sendDnList.length ; i++){
			if(sendDnList[i] == userDN)isCheck = true;					
		} 
		if(!isCheck)sendDnList.push(userDN);
	}
	var secureSendInfo = {"type":secureMailMakeMode,
			"sendDnList":sendDnList,				
			"from":param.senderEmail,
			"to":param.to,
			"cc":param.cc,
			"bcc":param.bcc,
			"subject":param.subject,
			"title":mailMsg.menu_secure,
			"contents":getMessageContent("secure")};
	var contents = createSecureMail(secureSendInfo);
	
	if(!contents){
		return;
	}	
	param.content = contents;
	
	var sendConfirmMsg = mailMsg.mail_send_confirm;
	if(!confirm(sendConfirmMsg)){
		return;
	}
	
	param.secure = "on";
	jQuery("#"+mailOption.mainLID).loadWorkMaskOnly(true);
	mailControl.sendMessage(param);
}

function requestSendAction(param){
	
	if(uploadAttachFilesError){
		return;
	}
	
	jQuery("#"+mailOption.mainLID).loadWorkMaskOnly(true);
	
	var isHugeFileInfoCheck = false;
	var actionParam = {};
	var hfileIdx = [];
	var hfileName = [];
	var hfilePath = [];
	var hfileSize = [];
	
	param.attachList = getAttachString();	
	
	var hugeFiles = basicAttachUploadControl.getFileList("huge");
	var uid;
	if (hugeFiles){
		for (var i = 0; i < hugeFiles.length; i++) {			
			uid = hugeFiles[i].uid;		
			if((!uid || uid == "" || uid =="0")){
				isHugeFileInfoCheck = true;
				hfileIdx.push(hugeFiles[i].id);				
				hfileName.push(hugeFiles[i].name);
				hfilePath.push(hugeFiles[i].path);
				hfileSize.push(hugeFiles[i].size);
			}
		}
		}
		
	
	
	if(!isHugeFileInfoCheck){		
		var bigAttachInfo = makeBigAttachContent();
		param.bigattachContents = bigAttachInfo.html;
		param.content =  param.content;
		param.bigAttachMode = bigAttachInfo.mode;
		param.bigAttachLinks = bigAttachInfo.links;
		
		mailControl.sendMessage(param);
		basicAttachUploadControl.destroy();
		
	} else {
		actionParam.hidx = hfileIdx;
		actionParam.hfileName = hfileName;
		actionParam.hfilePath = hfilePath;
		actionParam.hfileSize = hfileSize;
		actionParam.regdate = today.getTime();
		
		jQuery.post("/file/updateBigAttachInfo.do",actionParam,function(data, textStatus){				
			sendForBigAttach(data, textStatus, param);
		},"json");
	}
}

function sendForBigAttach(data, textStatus, param){	
	if(textStatus == "success"){
		var hfileInfos = data.hugeInfoList;
		var fid;	
		var hugeFile;			
		for ( var i = 0; i < hfileInfos.length; i++) {				
			hugeFile = basicAttachUploadControl.getAttachFileInfo("huge",hfileInfos[i].seq);
			hugeFile.uid = hfileInfos[i].uid;
			basicAttachUploadControl.setAttachFileInfo("huge",hfileInfos[i].seq,hugeFile);
		}		
	}
	
	var bigAttachInfo = makeBigAttachContent();
	param.bigattachContents = bigAttachInfo.html;
	param.bigAttachMode = bigAttachInfo.mode;
	param.bigAttachLinks = bigAttachInfo.links;
	
	mailControl.sendMessage(param);
}

function writePreview(){
	var param = {};
	var writeForm = document.writeForm;
	var previewForm = document.previewForm;
	
	var contentValue = getMessageContent("preview");
	
	if(!isRcptModeNormal)makeRcptForm();
	
	previewForm.subject.value = writeForm.subject.value;	
	previewForm.to.value = writeForm.to.value;
	previewForm.cc.value = writeForm.cc.value;
	previewForm.bcc.value = writeForm.bcc.value;	
	previewForm.senderName.value = writeForm.senderName.value;
	previewForm.senderEmail.value = writeForm.senderEmail.value;
	previewForm.content.value = contentValue;
	previewForm.wmode.value = $("editorMode").value;
	
	if($("signAttach").checked){
		var signSeq = $("signList").value;
		if(parseInt(signSeq) > 0){
			previewForm.attachsign.value = "on";
			previewForm.signseq.value = signSeq;
		}		
	} else {
		previewForm.attachsign.value = "off";		
	}
	
	//var priviewWin = window.open("about:blank","previewPopup","scrollbars=yes,width=740,height=640");
	previewForm.method = "post";
	previewForm.action="/dynamic/mail/writePreview.do";
	previewForm.target = "writePreviewIframe";
	//previewForm.submit();
	modalPopupForWritePreview("", 860, 660, previewForm);
}

function modalPopupForWritePreview(src, widthParam, heightParam, form){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = heightParam ? heightParam : 660;
	var width = widthParam ? widthParam : 860;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.height = height;
	popOpt.width = width;
	jQuery("#writePreview").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			//jQuery("#writePreviewIframe").attr("src",src);
			form.submit();
			jQuery("#writePreviewIframe").css("height",(height-20)+"px");
			jQuery("#writePreviewIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#writePreview_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#writePreviewIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#writePreview").jQpopup("open",popOpt);
}

function modalPopupWritePreviewClose(){
	jQuery("#writePreview").jQpopup("close");
}

function setWritePreviewTitle(title){
	jQuery("#writePreview_pht").html(title);
}

function modalPopupForWebfolder(src, widthParam, heightParam) {
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var width = widthParam ? widthParam : 330;
	var height = heightParam ? heightParam : 330;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	jQuery("#webfolder").css({"height":height+"px","width":width+"px"});
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#webfolderIframe").attr("src",src);
			jQuery("#webfolderIframe").css("height",(height-10)+"px");
			jQuery("#webfolderIframe").css("width",(width-8)+"px");
		},100);
		jQuery("#webfolder_jqbtn").hide();
	};
	popOpt.beforeCloseFunc = function(){
		jQuery("#webfolderIframe").attr("src","/common/zero.html");
		jQpopupClear();
	};
	
	jQuery("#webfolder").jQpopup("open",popOpt);
}

function modalPopupForWebfolderClose(){
	jQuery("#webfolder").jQpopup("close");
}

function setWebfolderPopupTitle(title){
	jQuery("#webfolder_pht").html(title);
}

var signInfo = {"signList":[],"defaultSign":""};
function getSignInfo(){
	jQuery.post("/setting/jsonSignList.do", {}, function (result) {
		var signArray = [];
		var defaultSign = "";
		if (result.isSuccess) {
			var signData = result.signList;			
			if (signData.length > 0) {
				for (var i=0; i<signData.length; i++) {
					signArray.push({index:signData[i].signName,value:signData[i].signSeq});                    
					if(signData[i].isDefault)defaultSign = signData[i].signSeq;
				}
			}			
			signInfo.signList = signArray;
			signInfo.defaultSign = defaultSign;
		
		}
		checkSign();
	},"json");	
}

function checkSign(){
	jQuery("#signListSelect").empty();
	var signArray = [];
	var defaultSign = "";
	if(signInfo.signList.length > 0){
		signArray = signInfo.signList;
		defaultSign = signInfo.defaultSign;
	} else {
		signArray.push({index:mailMsg.mail_nosign,value:"-1"});	
	}			

	jQuery("#signListSelect").selectbox({selectId:"signList",selectFunc:"",width:105,textMaxLength:6},
			defaultSign,signArray);

	
	if($("signAttach") && $("signAttach").checked){
		jQuery("#signListPane").show();
	} else {
		jQuery("#signListPane").hide();
	}	
}

function getLastRcptList(){
	var lastRcptArray = [];
	
	jQuery.post("/setting/jsonLastrcpt.do", {}, function (result) {
				
		lastRcptArray.push({index:mailMsg.mail_lastrcpt,value:"-1"});
		
		if (result.isSuccess) {
			var rcptData = result.rcptList;			
			if (rcptData.length > 0) {
				for (var i=0; i<rcptData.length; i++) {
					lastRcptArray.push({index:rcptData[i].email,value:unescape_tag(rcptData[i].email)});
				}
			}
		}
		jQuery("#lastrcptSelect").selectbox({selectId:"lastrcpt",selectFunc:applyLastrcpts,width:170},
			"-1",lastRcptArray);
	
	},"json");
	
}

/*
function GetHtmlMessage(type)
{		
    var oEditor = CKEDITOR.instances.ckeditor;
    var defaultStyle='';
   
    if(type!='ck')
    {
    	defaultStyle=GetStyle();
    }
    
	return defaultStyle+oEditor.getData();
}
*/
function GetHtmlMessage(type)
{	
	var editorData;
    var defaultStyle;
    try{
	    editorData = document.getElementById("smarteditor").value;
	    oEditors = oEditors.getById["smarteditor"].exec("UPDATE_CONTENTS_FIELD", []);
    
	    if(type!='ck')
	    {
	    	defaultStyle=GetStyle();
	    }
    }catch(e){}
    
	return defaultStyle+editorData;
}

function getMessageContent(type){
	var contentValue;
	var signLocation = $("signLocation").value;
	var isSignAdd = false;
	var searchText,spos,contentStr;
	
	if(editorMode=='html') {
		contentValue = GetMessage(type);
	} else {
		if (isMsie) {
			contentValue = window.textContentFrame.getContent();
		} else {
			contentValue = document.getElementById("textContentFrame").contentWindow.getContent();
		}
	}	
	
	if (type != "secure" && type != 'draft') {
		if(signLocation == "inside"){
			if(editorMode=='html') {		
				searchText = "<signpos\></signpos>";
				spos = contentValue.toLowerCase().indexOf(searchText)
				if(spos > -1){
					contentStr = contentValue.substring(0,spos);
					contentStr = contentStr + "<br>{tims_sign_pos}<br>";
					contentValue = contentStr + contentValue.substring(spos,contentValue.length);
					isSignAdd = true;
				}
			} else {
				searchText = "--- Original Message ---";
				spos = contentValue.indexOf(searchText);
				if(spos > -1){
					contentStr = contentValue.substring(0,spos);
					contentStr = contentStr + "\n{tims_sign_pos}\n\n";
					contentValue = contentStr + contentValue.substring(spos,contentValue.length);
					isSignAdd = true;
				}
			}		
		}
		
		if(!isSignAdd){		
			if(editorMode=='html') {
				contentValue = contentValue+"<br>{tims_sign_pos}<br>";
			} else {
				contentValue = contentValue+"\n{tims_sign_pos}\n\n";
			}		
			
		}
	}
	
	return contentValue;
}



function GetMessage(type) {
	try{
	oEditors.getById["smarteditor"].exec("UPDATE_CONTENTS_FIELD", []);
    var tmpHtmlContents = document.getElementById("smarteditor").value;   
    
    var replaceExp1 = new RegExp(unescape("%uFEFF") , "g");
    tmpHtmlContents = tmpHtmlContents.replace(replaceExp1, ''); 
    
    var replaceExp2 = new RegExp(unescape("%u200B") , "g");
    tmpHtmlContents = tmpHtmlContents.replace(replaceExp2, '&nbsp;');     
    
    tmpHtmlContents = tmpHtmlContents.replace(/^[\s\u00a0\u3000]+|[\s\u00a0\u3000]+$/g, '');
    
	if (type == 'draft') {
		return tmpHtmlContents;
	}
	
	if (type == 'ascheck') {
		return tmpHtmlContents;
	}
	
	if (type == 'secure') {
		return tmpHtmlContents;		
	}
	
	if (!isLetter) {	// Without Letter Image
		return GetStyle() + "<div class='TerraceMsg'>"+	tmpHtmlContents +"</div>";
	} else {			// With Letter Image
		var headerPath = "{tims_letter_paper_header}";
		var bodyPath = "{tims_letter_paper_body}";
		var tailPath = "{tims_letter_paper_tail}";
		if(type == "preview"){					
			var letterObj = letterControl.getLetterObj(letterIdx);			
			headerPath = letterObj.header;
			bodyPath = letterObj.body;
			tailPath = letterObj.tail;	 
		} 
		var pstr = ""
			+ GetStyle()
			+ "<table width='" + letterWidth + "' cellspacing='0' cellpadding='0'>"
			+ "<tr><td><img src='"+headerPath+"' style='display:box;display:flex;-ms-display:flexbox;'/></td></tr>"
			+ "<tr>"
			+ "<td class='TerraceMsg' style='padding:10px 50px 0px 50px;' background='"+bodyPath+"'>"
			+ document.getElementById("smarteditor").value
			+ "</td></tr>"
			+ "<tr><td><img src='"+tailPath+"'></td></tr>"
			+ "</table>";
	}
	}catch(e){}
	return pstr;
}

function GetStyle() {
	var font = (LOCALE != "jp")?"Dotum, Arial, Verdana, Sans-Serif":"MS PGothic,Osaka, Sans-serif";
	var pstr = "<style type='text/css'>"
		+ ".TerraceMsg { font-size: 12px; font-family:"+font+
		";}"
		+ ".Bold { font-weight: bold; }"
		+ "</style>";
	return pstr;
}

function contentDataCheck(){
	var f =document.contentForm;
	var contentTemp =f.contentTemp.value;
	if(contentTemp != '')	{
	     //f.content_text.value="";
	     setTimeout('initTextContentData()', 1000);	   	
	}
}
function initTextContentData(){
	var f =document.contentForm;
	if(f.contentTemp){
		var textContent = f.contentTemp.value;
		if (isMsie) {
			window.textContentFrame.setContent(textContent);
		} else {
			document.getElementById("textContentFrame").contentWindow.setContent(textContent);
		}
	}
}

function insertMySelf(obj) {
	if(isRcptModeNormal){
		insertMySelfNormal(obj);
	} else {
		insertMySelfSearch(obj);
	}
	
}

function insertMySelfNormal(obj) {
	
	var f = document.writeForm;	
	
	if (obj.checked) {
		var toValue = f.to.value;
		var setValue;
		if (toValue.indexOf(USEREMAIL) < 0) {
			if(toValue != ""){				
				if(toValue.substring(toValue.length-1,toValue.length) == ',')setValue = toValue + USEREMAIL;
				else setValue = toValue + "," + USEREMAIL + ",";
			} else {
				setValue = USEREMAIL + ",";
			}			
			f.to.focus();
			f.to.value = setValue;
		}
	}
	else {
		var addr_array = getEmailArray(f.to.value);
		var pstr = "";
		
		if(f.to.value != ""){
			for(var i = 0; i < addr_array.length; i++) {
	        	var address = addr_array[i];
				if (address.indexOf(USEREMAIL) < 0) {
					pstr += address + ",";
				}
			}
		}
		f.to.value = pstr;
		f.to.focus();
	}
}

function setRcptSearchList(type,str){
	if(str != ""){
		var listVal = str.split(",");
		for ( var i = 0; i < listVal.length; i++) {
			if(jQuery.trim(listVal[i]) != "")addRcptSearchList(type,listVal[i]);		
		}
		listVal = null;
	}
}

function addRcptSearchList(type,addr){
	var addrSelect = $("rcptSearchList");
	var val = type + "|" + addr;	
	addrSelect.options[addrSelect.length] = new Option(getRcptTypeIndex(type)+" : "+addr, val);
}

function deleteRcptSearchList(){
	deleteList(document.writeForm.rcptSearchList);	
}

function getRcptTypeIndex(type){
	if(type == 'to')
		return mailMsg.mail_rcptto;
	else if(type == 'cc')
		return mailMsg.mail_cc;
	else if(type == 'bcc')
		return mailMsg.mail_bcc;
	
	
}
function insertMySelfSearch(obj) {
	var f = document.writeForm;
	var addrSelect = f.rcptSearchList;
	var options = addrSelect.options;
	var val;
	if (obj.checked) {
		var isExist = false;
		var rcptType = $("rcptCategory").value;		
		for ( var i = 0; i < options.length; i++) {
			val = options[i].value;
			if(val.indexOf(USEREMAIL) > -1){
				isExist = true;
			}
		}
		
		if(!isExist){
			val = rcptType + "|<"+USEREMAIL+">";
			addrSelect.options[addrSelect.length] = new Option(getRcptTypeIndex(rcptType)+" : <"+USEREMAIL+">",val);
		}
	} else {
		for ( var i = 0; i < options.length; i++) {
			val = options[i].value;
			if(val.indexOf(USEREMAIL) > -1){
				if(isMsie)addrSelect.options.remove(i);
				else addrSelect.options[i] = null;
			}
		}
	}
}

function insertAddr(type,addrList){
	if(isRcptModeNormal){
		insertAddrNormal(type,addrList);
	} else {
		insertAddrSearch(type,addrList);
	}	
}

function insertAddrNormal(type,addrList){
	if(type == "bcc"){
		toggleRcpt(true);
	}	
	var addSelect = $(type);
	var addrs = jQuery.trim(addSelect.value);
	var addr_array = getEmailArray(addrs);
	var pstr = "";
	var cnt = 0;
	for ( var i = 0; i < addrList.length; i++) {
		var isExist = false;
		for(var j = 0; j < addr_array.length; j++) {
	    	var address = addr_array[j];	    	
			if (address.indexOf(addrList[i]) > -1) {
				isExist = true;
				break;
			}
		}
		
		if(!isExist){
			
			if(cnt > 0){
				pstr += ",";
			}
			pstr += addrList[i];
			cnt++;
			
		}
	}
	if(addrs != ""){
		if(addrs.substring(addrs.length-1,addrs.length) == ',')addrs = addrs + pstr;
		else addrs = addrs + "," + pstr;
	} else {
		addrs = pstr;
	}

	addSelect.value = addrs;
}

function insertAddrSearch(type,addrList){
	if(!isRcptModeNoneAC){
		var email;
		for ( var i = 0; i < addrList.length; i++) {
			email = get_email(addrList[i]);
			if(!checkRcptSearchList(type,email)){
				addRcptSearchList(type,addrList[i]);
			}
		}
	} else {		
		if(type == "to"){
			var to = $("toTempVal").value;
			to += (to != "")?",":"";
			to += addrList.join(",");
			$("toTempVal").value = to;
		} else if(type == "cc"){
			var cc = $("ccTempVal").value;
			cc += (cc != "")?",":"";
			cc += addrList.join(",");
			$("ccTempVal").value = cc;			
		} else if(type == "bcc"){			
			var bcc = $("bccTempVal").value;
			bcc += (bcc != "")?",":"";
			bcc += addrList.join(",");
			$("bccTempVal").value = bcc;			
		}
	}
}

function checkRcptSearchList(type,email){
	var addrSelect = $("rcptSearchList");
	var isExist = false;
	var val;	
	for ( var i = 0; i < addrSelect.length; i++) {
		val = addrSelect.options[i].value;		
		if(type == getRcptListValue(val).type && val.indexOf(email) > -1){
			isExist = true;
			break;
		}
	}
	return isExist;
}

function getRcptListValue(val){
	var vobj = {type:"",val:""};
	var idx;
	if(val != ""){
		idx = val.indexOf("|");
		vobj.type = val.substring(0,idx);
		vobj.val = val.substring(idx+1);
		
	}
	idx = null;
	return vobj;
}

function makeRcptForm(){
	var to = "";
	var cc = "";
	var bcc = "";
	var addrSelect = $("rcptSearchList");
	var vobj;
	for ( var i = 0; i < addrSelect.length; i++) {
		vobj = getRcptListValue(addrSelect.options[i].value);
		switch (vobj.type) {
		case "to":
			to = to + vobj.val +",";
			break;
			
		case "cc":
			cc = cc + vobj.val +",";
			break;
			
		case "bcc":			
			bcc = bcc + vobj.val +",";
			break;
		}		
	}
	$("to").value = to;
	$("cc").value = cc;
	$("bcc").value = bcc;
}


var SearchByNameInfo ={
	searchAddrValueList:[],
	emptyListFlag:false,
	emptyKeywordList:[],
	rcptMultiResultType:"normal",
	rcptType:""
	
	
};


function searchRcptAddr(){
	$("toTempVal").value = "";
	$("ccTempVal").value = "";
	$("bccTempVal").value = "";
	
	var to = $("to").value;
	var cc = $("cc").value;
	var bcc = $("bcc").value;	
	SearchByNameInfo.searchAddrValueList.push({type:"bcc",list:bcc});
	SearchByNameInfo.searchAddrValueList.push({type:"cc",list:cc});
	SearchByNameInfo.searchAddrValueList.push({type:"to",list:to});	
	queryByRcptName();
}

function queryByRcptName(){
	SearchByNameInfo.emptyListFlag = false;
	SearchByNameInfo.emptyKeywordList = [];
	SearchByNameInfo.rcptType = "";
	
	var addrObj = SearchByNameInfo.searchAddrValueList.pop();	
	if(addrObj){
		var addrListVal = addrObj.list;
		var rcptType = addrObj.type;
		SearchByNameInfo.rcptType = rcptType;
		if(!addrListVal || jQuery.trim(addrListVal) == ""){
			queryByRcptName();
			return;
		}
		
		var searchKeywordList = [];
		var normalEmailList = [];
		var searchNameList = [];
		var checkLength = 3;		
		
		var isSplit1 = (addrListVal.indexOf(",") > -1);
		var isSplit2 = (addrListVal.indexOf(";") > -1);
		
		if(isSplit1 || isSplit2){
			if(isSplit1){			
				searchKeywordList = addrListVal.split(",");
			} else if(isSplit2){
				searchKeywordList = addrListVal.split(";");
			}
		} else {
			searchKeywordList.push(addrListVal);
		}		
		
		for ( var i = 0; i < searchKeywordList.length; i++) {
			var keyword = jQuery.trim(searchKeywordList[i]);
			if(keyword == "" || keyword.length == 0){
				continue;
			}
			
			if(validateHan(keyword)){
				checkLength = 4;
			}
			
			if(!checkInputSearchAddr(keyword,checkLength,255,true)){
				break;
				return;
			}
			var emailValue = get_email(keyword);
			if(isEmail(emailValue) || 
					emailValue.indexOf("#") == 0 ||
					emailValue.indexOf("$") == 0 ||
					emailValue.indexOf("&") == 0){
				if(keyword.indexOf("<") > -1){
					normalEmailList.push(keyword);
				} else {
					normalEmailList.push("<"+keyword+">");
				}
			} else {
				searchNameList.push(keyword);
			}		
		}
		insertAddrSearch(rcptType,normalEmailList);
		if(searchNameList.length > 0){
			var isNotOrgSearch = IS_XPRESS_MAIL_WRITE;
			MailCommonService.searchAddressByKeyowrd(searchNameList,IS_XPRESS_MAIL_WRITE,
					function(searchResult){			
						resultQueryByRcptName(rcptType,searchResult);			
			});	
		} else {
			queryByRcptName();
			return;
		}
	} else {
		sendMessage(sendMailType);
	}
}

function resultQueryByRcptName(rcptType,sresult){
	if(sresult.result == "success"){
		var slist = sresult.single;
		var mlist = sresult.multi;
		var elist = sresult.empty;
		
		var sEmailList = [];
		for ( var i = 0; i < slist.length; i++) {
			sEmailList.push(slist[i].emailValue);
		}
		insertAddrSearch(rcptType,sEmailList);
		if(elist.length > 0){
			SearchByNameInfo.emptyListFlag = true;
			SearchByNameInfo.emptyKeywordList = elist;
		}
		
		if(mlist.length > 0){
			confirmSearchAddrModal(sresult.multiType,mlist);
		} else if(SearchByNameInfo.emptyListFlag){
			confirmSearchAddrModal("empty",elist);
		} else {
			queryByRcptName();
		}
	} else if(sresult.result == "empty"){		
		confirmSearchAddrModal("empty",sresult.empty);
	} else if(sresult.result == "error"){
		alert(mailMsg.alert_addr_search_error);
		return;
	}	
}

function searchRcptAddress(){
	var keyword = $("searchRcptAddr").value;
	var searchKeywordList = [];
	var normalEmailList = [];
	var searchNameList = [];
	var checkLength = 2;
	var rcptType = $("rcptCategory").value;	
	if(keyword == ""){
		alert(mailMsg.alert_addr_search_nostr);
		$('searchRcptAddr').focus();
		return;
	}
	var isSplit1 = (keyword.indexOf(",") > -1);
	var isSplit2 = (keyword.indexOf(";") > -1);
	
	if(isSplit1 || isSplit2){
		if(isSplit1){			
			searchKeywordList = keyword.split(",");
		} else if(isSplit2){
			searchKeywordList = keyword.split(";");
		}
	} else {
		searchKeywordList.push(keyword);
	}
	
	for ( var i = 0; i < searchKeywordList.length; i++) {
		var keyword = jQuery.trim(searchKeywordList[i]);
		if(validateHan(keyword)){
			checkLength = 4;
		}
		
		if(!checkInputSearchAddr(keyword,checkLength,255,true)){
			break;
			return;
		}
		
		if(isEmail(keyword)){
			normalEmailList.push("<"+keyword+">");
		} else {
			searchNameList.push(keyword);
		}		
	}
	if(searchNameList.length > 0){
		MailCommonService.searchAddressByKeyowrd(searchNameList,IS_XPRESS_MAIL_WRITE,function(searchResult){			
			searchRcptResult(searchResult);			
		});	
	}
	if(normalEmailList.length > 0){
		insertAddrSearch(rcptType,normalEmailList);
	}
	
	$("searchRcptAddr").value = "";
	$("searchRcptAddr").blur();
}

function searchRcptResult(sresult){
	if(sresult.result == "success"){
		var slist = sresult.single;
		var mlist = sresult.multi;
		var rcptType = $("rcptCategory").value;	
		var sEmailList = [];		
		for ( var i = 0; i < slist.length; i++) {
			sEmailList.push(slist[i].emailValue);
		}
		insertAddrSearch(rcptType,sEmailList);
		if(mlist.length > 0){
			confirmSearchAddrModal(sresult.multiType,mlist);
		}		
	} else if(sresult.result == "empty"){
		alert(mailMsg.alert_addr_search_noresult);
	} else if(sresult.result == "error"){
		alert(mailMsg.alert_addr_search_error);
	}
}

function confirmSearchAddrModal(type,mlist){
	var popupId = "addRcptNormalAddrPop";
	var contentPaneId = "addRcptNormalAddrListContents";
	var ctableId = "addRcptNormalAddrTable";
	var allCheckId = "addRcptNormalAddrAllchk";
	SearchByNameInfo.rcptMultiResultType = "normal";
	if(type == "org"){
		popupId = "addRcptOrgAddrPop";
		contentPaneId = "addRcptOrgAddrListContents";
		ctableId = "addRcptOrgAddrTable";
		SearchByNameInfo.rcptMultiResultType = "org";
		allCheckId = "addRcptOrgAddrAllchk";
	} else if(type == "empty"){
		popupId = "addRcptEmptyAddrPop";
		contentPaneId = "addRcptEmptyAddrListContents";
		ctableId = "addRcptEmptyAddrTable";
		SearchByNameInfo.rcptMultiResultType = "empty";
		allCheckId = "addRcptEmptyAddrAllchk";
	}
	
	var popOpt = clone(popupOpt);
	popOpt.btnList = [{name:comMsg.comn_confirm,func:addSearchRcptAddr}];
	popOpt.minHeight = 300;
	popOpt.minWidth = 500;
	popOpt.top = 150;
	popOpt.openFunc = function(){		
		jQuery("div#"+contentPaneId+" table").css("width","100%");		
	};
	popOpt.closeFunc = function(){
		$(allCheckId).checked = false;
		jQuery("div#"+contentPaneId+" table").css("width","");
	};
	
	jQuery("#"+popupId).jQpopup("open",popOpt);	
	jQuery("table#"+ctableId+" tr").remove();	
	
	var addtable = jQuery("#"+ctableId);	
	for ( var i = 0; i < mlist.length; i++) {
		var content;
		if(type == "org"){
			content = "<tr>"
				 +"<td style='text-align:center'><input type='checkbox' name='addRcptAddrEmail' value=\'"+escape_tag(mlist[i].emailValue)+"\'/></td>"
				+"<td>"+mlist[i].name+"</td>"
				+"<td>"+((mlist[i].title)?mlist[i].title:"")+"</td>"
				+"<td>"+((mlist[i].dept)?mlist[i].dept:"")+"</td>"
				+"<td>"+mlist[i].email+"</td>"
				+"</tr>";
		} else if(type == "empty"){								
			content = "<tr>"
				 +"<td style='text-align:center'><input type='checkbox' name='addRcptAddrEmail' value=\'"+escape_tag(mlist[i].emailValue)+"\'/></td>"
				+"<td>"+mlist[i]+"</td>"
				+"<td style='padding-right:5px'><input type='text'  name='rcptemailaddr' class='IP100'/></td>"
				+"</tr>";			
		} else if(type == "normal"){
			content = "<tr>"
				 +"<td style='text-align:center'><input type='checkbox' name='addRcptAddrEmail' value=\'"+escape_tag(mlist[i].emailValue)+"\'/></td>"
				+"<td>"+mlist[i].name+"</td>"
				+"<td>"+mlist[i].email+"</td>"
				+"</tr>";
		}
		addtable.append(jQuery(content));		
	}
}

function addSearchRcptAddr(){
	var checkObj;
	var rcptType = (!isRcptModeNoneAC)?$("rcptCategory").value:SearchByNameInfo.rcptType;
	var popupId = "addRcptNormalAddrPop";
	if(SearchByNameInfo.rcptMultiResultType == "normal"){
		checkObj = document.addRcptNormalAddrForm.addRcptAddrEmail;
	} else if(SearchByNameInfo.rcptMultiResultType == "org"){
		checkObj = document.addRcptOrgAddrForm.addRcptAddrEmail;
		popupId = "addRcptOrgAddrPop";
	} else if(SearchByNameInfo.rcptMultiResultType == "empty"){
		checkObj = document.addRcptEmptyAddrForm.addRcptAddrEmail;
		popupId = "addRcptEmptyAddrPop";
	}
	
	var addAddrList = [];
	if(SearchByNameInfo.rcptMultiResultType == "normal" ||
			SearchByNameInfo.rcptMultiResultType == "org"){		
		if(checkObj.length > 1){
			for ( var i = 0; i < checkObj.length; i++) {
				if(checkObj[i].checked)addAddrList.push(checkObj[i].value);
			}
		} else {
			if(checkObj.checked)addAddrList.push(checkObj.value);
		}
		
		if(addAddrList.length == 0){
			alert(comMsg.comn_error_001);
			return;
		}
	
	} else if(SearchByNameInfo.rcptMultiResultType == "empty"){
		var inputs = document.addRcptEmptyAddrForm.rcptemailaddr;
		var emailVal;
		var isNotCheck = false;
		if(checkObj.length > 1){			
			for ( var i = 0; i < checkObj.length; i++) {
				emailVal = inputs[i].value;
				if(checkObj[i].checked){
					if(isEmail(emailVal)){
						addAddrList.push("\""+checkObj[i].value+"\" <"+emailVal+">");
					} else {
						alert(mailMsg.alert_invalidaddress+"\n\n"
								+emailVal);					
						return;
					}
				} else {
					if(isEmail(emailVal)){
						isNotCheck = true;
					}
				}
			}
		} else {
			emailVal = inputs.value;
			if(checkObj.checked){
				if(isEmail(emailVal)){
					addAddrList.push("\""+checkObj.value+"\" <"+emailVal+">");
				} else {
					alert(mailMsg.alert_invalidaddress+"\n\n"
							+emailVal);
					return;
				}
			} else {
				if(isEmail(emailVal)){
					isNotCheck = true;
				}
			}
		}
		
		if(isNotCheck && !confirm(mailMsg.confirm_notcheckrcpt)){
			return;
		}
	}		
	
	insertAddrSearch(rcptType,addAddrList);
	jQuery("#"+popupId).jQpopup("close");	
	if(isRcptModeNoneAC){		
		if(SearchByNameInfo.rcptMultiResultType != "empty" &&
				SearchByNameInfo.emptyListFlag){
			setTimeout(function(){
				confirmSearchAddrModal("empty",SearchByNameInfo.emptyKeywordList);
			},100);
		} else {
			setTimeout(function(){
				queryByRcptName();
			},100);
		}
	}	
}


function checkSendInfoValue(param){	
	var toObj = $("to");
	var ccObj = $("cc");
	var bccObj = $("bcc");
	if(isRcptModeNoneAC){
		toObj = $("toTempVal");
		ccObj = $("ccTempVal");
		bccObj = $("bccTempVal");
	}
	
	var toArray = getEmailArray(toObj.value);
	var ccArray = (ccObj.value != "")?getEmailArray(ccObj.value):[];
	var bccArray = (bccObj.value != "")?getEmailArray(bccObj.value):[];
	var attachArray = [];
	
	var hfileList = basicAttachUploadControl.getFileList("huge");
	var nfileList = basicAttachUploadControl.getFileList("normal");
	jQuery.merge(hfileList,nfileList);
	for(var i = 0; i < hfileList.length; i++) {
		$(hfileList[i].id).checked = false;
		attachArray.push({name:hfileList[i].name,id:hfileList[i].id});
	}
		
	
	var popOpt = clone(popupOpt);
	popOpt.btnList = [{name:mailMsg.menu_send,func:checkSendMessage},
	                  {name:comMsg.comn_setting,func:settingWriteInfo}];
	popOpt.minHeight = 350;
	popOpt.minWidth = 500;
	popOpt.top = 150;
	popOpt.openFunc = function(){		
		jQuery("div#checkSendInfoPop table").css("width","100%");		
	};
	popOpt.closeFunc = function(){
		jQuery("div#checkSendInfoPop table").css("width","");
		jQuery("#ccRcptCheckBox").hide();
		jQuery("#bccRcptCheckBox").hide();
		jQuery("#attachCheckBox").hide();				
	};
	
	jQuery("#checkSendInfoPop").jQpopup("open",popOpt);
	jQuery("#checkSendInfoPop").data("sendParam",param);
	
	jQuery("#subjectCheckL").empty();
	jQuery("#toRcptCheckL").empty();
	jQuery("#ccRcptCheckL").empty();
	jQuery("#bccRcptCheckL").empty();
	jQuery("#attachCheckL").empty();
	
	if(isMsie6 || isMsie7){
		jQuery("div#checkSendInfoPop table").css("width","");
	}
	
	jQuery("#subjectCheckL").html(escape_tag(param.subject));
	jQuery("#toRcptCheckL").html(makeCheckRcptHtml("to",toArray));	
	
	if(ccArray.length > 0){
		jQuery("#ccRcptCheckBox").show();
		jQuery("#ccRcptCheckL").html(makeCheckRcptHtml("cc",ccArray));
	}
	
	if(bccArray.length > 0){
		jQuery("#bccRcptCheckBox").show();
		jQuery("#bccRcptCheckL").html(makeCheckRcptHtml("bcc",bccArray));
	}
	
	if(attachArray.length > 0){
		jQuery("#attachCheckBox").show();
		jQuery("#attachCheckL").html(makeCheckRcptHtml("att",attachArray));
	}
	
}

function makeCheckRcptHtml(type,checkList){
	var content = "";
	var borderStyle = "";
	var bgStyle = "";
	var localDomain = USEREMAIL.substring(USEREMAIL.indexOf("@")+1,USEREMAIL.length);
	var contentStr = "";
	
	for ( var i = 0; i < checkList.length; i++) {		
		borderStyle = (i == (checkList.length-1))?"":"border-bottom:#E6E6E6 solid 1px;";
		
		if(type == "to" || type == "cc" || type == "bcc"){
			contentStr = checkList[i];
			bgStyle = (contentStr.indexOf(localDomain) < 0)?"background:#FFF880;":"";
		} else {
			contentStr = checkList[i].name;
		}
		content += "<div style='height:15px;padding:5px;"+borderStyle+bgStyle+"'>" +				
				"<input type='checkbox' name='"+type+"SendInfoCheck' " +
						"value='"+((type == "att")?checkList[i].id:i)+
						"' align='absmiddle'/>&nbsp;" +
				"<span id='"+type+"InfoText"+i+"'>" + escape_tag(contentStr)+"</span></div>";
	}
	return content;
}

function settingWriteInfo(){
	var toVal, ccVal, bccVal;
	toVal = getCheckSendInfoValue("to");
	ccVal = getCheckSendInfoValue("cc");
	bccVal = getCheckSendInfoValue("bcc");
	
	if(!isRcptModeNormal){
		jQuery("#rcptSearchList").empty();				
		var tempList;	
		if(toVal != ""){
			tempList = toVal.split(",");
			for ( var i = 0; i < tempList.length; i++) {
				addRcptSearchList("to",unescape_tag(tempList[i]));
			}
		}
		
		if(ccVal != ""){
			tempList = ccVal.split(",");
			for ( var i = 0; i < tempList.length; i++) {
				addRcptSearchList("cc",unescape_tag(tempList[i]));
			}
		}
		
		if(bccVal != ""){
			tempList = bccVal.split(",");
			for ( var i = 0; i < tempList.length; i++) {
				addRcptSearchList("bcc",unescape_tag(tempList[i]));
			}
		}		
	} else {
		$("to").value = unescape_tag(toVal);
		$("cc").value = unescape_tag(ccVal);
		$("bcc").value = unescape_tag(bccVal);		
	}
	
	settingWriteInfoAttach();
	$("reply_me").checked = false;
	jQuery("#checkSendInfoPop").jQpopup("close");	
}

function checkSendMessage(){
	var toVal, ccVal, bccVal;
	toVal = getCheckSendInfoValue("to");
	ccVal = getCheckSendInfoValue("cc");
	bccVal = getCheckSendInfoValue("bcc");
	var param = jQuery("#checkSendInfoPop").data("sendParam");
	
	if(toVal == ""){
		alert(mailMsg.error_norecipient);
		return;
	}
	
	param.to = unescape_tag(toVal);
	param.cc = unescape_tag(ccVal);
	param.bcc = unescape_tag(bccVal);
	
	settingWriteInfo();	
	sendMessageParamProcess(param);	
}


function settingWriteInfoAttach(){
	
	var attachChkObj = document.checkSendInfoForm.attSendInfoCheck;
	if(attachChkObj){
		var removeAttachList = [];
		
		if(attachChkObj.length > 1){
			for ( var i = 0; i < attachChkObj.length; i++) {
				if(!attachChkObj[i].checked)removeAttachList.push(attachChkObj[i].value);				
			}
		} else {
			if(!attachChkObj.checked)removeAttachList.push(attachChkObj.value);			
		}		
		
		for ( var i = 0; i < removeAttachList.length; i++) {
			$(removeAttachList[i]).checked = true;
		}			
		deletefile();
		
	}
}


function getCheckSendInfoValue(type){
	var lid;
	var checkInputBox;
	var returnVal = "";
	if(type == "to"){
		lid = "toRcptCheckL";
	} else if(type == "cc"){
		lid = "ccRcptCheckL";
	} else if(type == "bcc"){
		lid = "bccRcptCheckL";
	} else if(type == "att"){
		lid = "attachCheckL";
	}
	
	checkInputBox = jQuery("#"+lid+" input:checked");
	var index;
	if(checkInputBox){
		for ( var i = 0; i < checkInputBox.length; i++) {
			if(i > 0 )returnVal += ",";
			if(type != "att"){
				index = jQuery(checkInputBox[i]).val();				
				returnVal += jQuery("#"+type+"InfoText"+index).text();
			} else {
				returnVal += checkInputBox[i].val();
			}
		}
	}
	
	return returnVal;
}

var currentFocusRcpt = "to";
function focusRcpt(type){	
	currentFocusRcpt = type;	
}

function applyLastrcpts(value) {
	if(value < 0){
		return;
	}
	
	value = replaceAll(value,"&lt;","<");
	value = replaceAll(value,"&gt;",">");
	value = replaceAll(value,"&#034;","\"");
	
	if(isRcptModeNormal){
		var txt = document.writeForm.elements[currentFocusRcpt];
		var rcptVal = txt.value;
	    if (rcptVal == "") {txt.value = value;}
		else {							
			if(rcptVal.substring(rcptVal.length-1,rcptVal.length) == ',')txt.value = rcptVal + value;
			else txt.value = rcptVal + "," + value + ",";
		}
	    txt.focus();
	} else {		
		var rcptType = $("rcptCategory").value;
		var addrSelect = $("rcptSearchList");
		var val = rcptType+"|"+value;
		addrSelect.options[addrSelect.length] = new Option(getRcptTypeIndex(rcptType)+" : "+value,val);
	}
	
	
	jQuery("#lastrcptSelect").selectboxSetValue("-1");
}

function expandRcpt(obj,temp) {	
	var bisIE = true;
	if( navigator.userAgent.indexOf("MSIE") == -1 ) bisIE = false
	
	temp = document.getElementById(temp);	
	if(!temp)return;	
	
	if ( obj.clientWidth > 2 ) {  
		temp.style.width = (obj.clientWidth - 2 ) + "px";	// revision value = 2
	} else {
		temp.style.width = obj.clientWidth + "px";
	}

	if (temp.innerText) temp.innerText = obj.value;
	else {		
		if(bisIE){
			temp.innerText = obj.value;
		} else {
			var tval =  obj.value.replace(/([,;])([\w])/g,'$1 $2');
			jQuery(temp).text(tval.length?tval:'mail'); // dummy text
		} 
	}
	
	var size = temp.offsetHeight;
	var objHeight = Math.max(17,temp.offsetHeight);
	
	if(objHeight > 80){
		objHeight = 80;
		obj.style.overflow = "auto";
	}
	if(obj.value)
		obj.style.height = (objHeight-2)+'px';
	else
		obj.style.height = 18+'px';
	
	if(objHeight == 80){
		obj.scrollTop = size;
	}
	
}


function emulEvent(el) {
	el = document.getElementById(el);

	if (el == null || el.onpropertychange == null || typeof el.onpropertychange == 'undefined' || typeof el == 'undefined') return;

	el._last_value = el.value;
	el._onpropertychange = new Function('this._last_value=this.value;'+el.getAttribute('onpropertychange'));

	var f = function() {
		if (el._last_value != el.value) {
			el._onpropertychange();
			el._last_value = el.value;
		}
		setTimeout(f, 10);
	};

	f();
}


function getEmailArray(str) {
    var addr_array = str.split(/\s*[,;\r\n]\s*/);
	var new_array = [];

	var j = 0;

    for(var i = 0; i < addr_array.length; i++) {
        var address = addr_array[i];

        // "kim, doyoung" <doyoung@terracetech.com>
        if(address.charAt(0) == '"'
            && address.indexOf("\"", 1) < 0) {

            if(addr_array[i+1] != null) {
                addr_array[i+1] = address + "," + addr_array[i+1];
                continue;
            }
        }
        if(trim(address) != ""){
        	new_array.push(address);
        }
		
	}

	return new_array;
}

function getWriteType() {
	return jQuery("#editorMode").val();
}

function getWriteMode() {
	var writeMode = "normal";
	var f = document.writeForm;
	if (f.sendFlag.value == "forwardAttached" || f.sendFlag.value == "forward") {
		writeMode = "forward";
	}
	return writeMode;
}

var editorMode;
function chgEditorMod(mode,isinit){
	var htmlLayer 	= jQuery("#modeHtmlWrapper");
	var textLayer 	= jQuery("#modeTextWrapper");
	
	if(!isinit && editorMode == mode){
		return;
	}
		
    var confirmMeg = mailMsg.write_alert002;
    if(editorMode == "html"){
    	confirmMeg = mailMsg.write_alert002;
    	confirmMeg +="\n"+mailMsg.write_alert003;    	
    } 
    
    if(!isinit){
    	if(!confirm(confirmMeg)) {    	
    		if (editorMode == "text") {	    
    			jQuery("#editorModeSelect").selectboxSetValue("text");
    		} else if (editorMode == "html") {
    			jQuery("#editorModeSelect").selectboxSetValue("html");
    		}
    		return;
    	}
    } else {    	
    	if (mode == "text") {	    
    		jQuery("#editorModeSelect").selectboxSetValue("text");
		} else if (mode == "html") {
			jQuery("#editorModeSelect").selectboxSetValue("html");
		}
    }
    
    if(mode == "text"){    	
    	htmlLayer.hide();
    	textLayer.show();
    	if($("htmlMoreFuncPane"))$("htmlMoreFuncPane").hide();
    	if(letterControl.chkOpen()){
    		toggleLetterPane();
    	}
    	if(docTemplateControl.chkOpen()){
    		toggleTemplatePane();
    	}
    	
    	if(!isinit){
    		if (isMsie) {
    			window.textContentFrame.setFrameFocus();
    		} else {
    			document.getElementById("textContentFrame").contentWindow.setFrameFocus();
    		}
    	}
    	
   		$("receivenoti").disabled = true;
    } else {    	
    	textLayer.hide();
    	htmlLayer.show();
    	if($("htmlMoreFuncPane"))$("htmlMoreFuncPane").show();
    	
    	
		$("receivenoti").disabled = false;
    }
    
    editorMode = mode;
}

function toggleLetterPane(){	
	if(docTemplateControl.chkOpen()){
		toggleTemplatePane();
	}
	if(letterControl.chkOpen()){
		jQuery("#letterBtn").removeClass("openRcptBtn");
		jQuery("#letterBtn").addClass("closeRcptBtn");		
		letterControl.hidePane();
	} else {		
		jQuery("#letterBtn").removeClass("closeRcptBtn");
		jQuery("#letterBtn").addClass("openRcptBtn");
		letterControl.getViewLetterPagerList(1);
	}	
}

function goLetterPage(page){
	letterControl.getViewLetterPagerList(page);
}
var letterInfo = {};
function setLetterPaper(idx){
	if(isDocTemplate){
		alert(comMsg.doctemplate_text_alert3);
		return;
	}	
	
	
	var letterObj = letterControl.getLetterObj(idx);

	var header = letterObj.header;
	var body = letterObj.body;
	var tail = letterObj.tail;

	var topImg = jQuery("<img></img>");
	topImg.attr("id","letter_top_img");
	topImg.attr("src",header);
	topImg.css("width","600px");
    
    var bottomImg = jQuery("<img></img>");
    bottomImg.attr("id","letter_bot_img");
    bottomImg.attr("src",tail);
    bottomImg.css("width","600px");
   
	var editorWrapFrame = jQuery("#modeHtmlWrapper iframe:eq(0)");
	var editorWrapContent = editorWrapFrame.contents();
	var editorFrame = editorWrapContent.find("#se2_iframe");
	var editorContent = editorFrame.contents();
	editorWrapContent.find("#paperTopRow").html(topImg).show();
	editorWrapContent.find("#paperBottomRow").html(bottomImg).show();
	editorWrapContent.find("textarea.se2_input_htmlsrc").css({"height":"300px","width":"600px"});
			
	letterInfo.frameHeight = editorWrapFrame.css("height");
	editorWrapFrame.css("height","600px");
	
	var editorContainerObj = editorWrapContent.find("div.husky_seditor_editing_area_container");
	letterInfo.editorHeight = editorContainerObj.css("height");
	editorContainerObj.css("height","300px");
	
	editorFrame.css({
		"background":"url('"+body+"') repeat-y scroll left top transparent",
		"width":"630px","height":"300px"
	});
	editorContent.find("body").css({
		"background":"url('"+body+"') repeat-y scroll left top transparent",
		"height":"270px",
		"margin":"10px 25px"
	});
	
	setTimeout(function() {
		oEditors.getById["smarteditor"].exec("MSG_EDITING_AREA_RESIZE_STARTED");
		oEditors.getById["smarteditor"].exec("RESIZE_EDITING_AREA_BY", [0, 0]);
		oEditors.getById["smarteditor"].exec("MSG_EDITING_AREA_RESIZE_ENDED");
		oEditors.getById["smarteditor"].exec("FOCUS");
	},500);
    
	
	resizeEditorFrame(730);
	

	$("letterMode").value = "on";
	$("letterSeq").value = letterObj.letterSeq;
	isLetter = true;
	letterIdx = idx;
	
	jQuery("#letterBtn").removeClass("openRcptBtn");
	jQuery("#letterBtn").addClass("closeRcptBtn");		
	letterControl.hidePane();
	
	jQuery(window).trigger("resize");

}

function clearLetterPaper(){
	jQuery("#modeHtml").css({"width":""});
	
	
	var editorWrapFrame = jQuery("#modeHtmlWrapper iframe:eq(0)");
	var editorWrapContent = editorWrapFrame.contents();
	var editorFrame = editorWrapContent.find("#se2_iframe");
	var editorContent = editorFrame.contents();
	
	editorWrapFrame.css("height",letterInfo.frameHeight);
	editorWrapFrame.css("width","100%");
	editorWrapContent.find("#paperTopRow").hide().empty();
	editorWrapContent.find("#paperBottomRow").hide().empty();
	editorWrapContent.find("textarea.se2_input_htmlsrc").css("width","");
	editorWrapContent.find("div.husky_seditor_editing_area_container").css({"width":"100%","height":letterInfo.editorHeight});
	
	editorFrame.css({"width":"100%","height":"100%","background":"none"});
	editorContent.find("body").css({
		"background":"none",
		"height":letterInfo.editorHeight,
		"margin":""
	});
	letterInfo = {};
	
	oEditors.getById["smarteditor"].exec("FOCUS");
	resizeEditorFrame(400);
	$("letterMode").value = "off";
	$("letterSeq").value = "";
	isLetter = false;
	letterIdx = -1;
}

function toggleTemplatePane(){
	if(letterControl.chkOpen()){
		toggleLetterPane();
	}
	if(docTemplateControl.chkOpen()){
		jQuery("#templateBtn").removeClass("openRcptBtn");
		jQuery("#templateBtn").addClass("closeRcptBtn");		
		docTemplateControl.hidePane();
	} else {		
		jQuery("#templateBtn").removeClass("closeRcptBtn");
		jQuery("#templateBtn").addClass("openRcptBtn");
		docTemplateControl.viewDocTemplateList(1);
	}	
}

function goDocTemplatePage(page){
	docTemplateControl.viewDocTemplateList(page);
}

function setTemplate(seq){	
	if(isLetter){
		alert(comMsg.doctemplate_text_alert3);
		return;
	}
	var content = GetMessage("ascheck");
	if(content == '<br />'){
		content = "";
	}
	
	if(jQuery.trim(content) != ""){
		if(!confirm(comMsg.doctemplate_text_alert2)){
			return;
		}
	}
	docTemplateControl.getTemplateContents(seq,function(contents){
		oEditors.getById["smarteditor"].setIR(contents);
		isDocTemplate = true;		
	});	
}

function clearTemplate(){
	oEditors.getById["smarteditor"].setIR("");
	isDocTemplate = false;
}

var attachFileListLength = 1;
function fnAttachAdd(){
	if (attachFileListLength < 10) {
		attachFileListLength++;
		jQuery("#sattachArea").append(jQuery("<input type='file' name='attachFile"+attachFileListLength+"'  id='attachFile"+attachFileListLength+"' class='TM_attFile'/>"));	
	}
}

function fnAttachDel(){
	if (attachFileListLength > 1) {
		attachFileListLength--;
		jQuery("#sattachArea").find("input:last").remove();
	}
}

/*********** UPLOAD MASS RCPT FUNCTION **************/

var DocTemplateControl = Class.create({
	initialize: function(paneId){
		this.isOpen = false;
		this.paneId = paneId;
	},
	viewDocTemplateList:function(page){
		var _this = this;
		var param = {"page":page};
		jQuery.post("/common/listDocTemplate.do", param, function(data, textStatus){
			if(textStatus == "success"){
				if(data.result == "success"){
					_this.makeDocTemplateList(data);
				} else {
					alert(comMsg.doctemplate_error);
				}
			} else {
				alert(comMsg.doctemplate_error);
				return;
			}
		}, "json");
			
	},
	makeDocTemplateList:function(templateInfo){
		var list = templateInfo.list;
		var pre = templateInfo.pre;
		var next = templateInfo.next;
		jQuery("#"+this.paneId).empty();
		
		var cancelPane = jQuery("<a href='javascript:;' onclick='clearTemplate();' class='btn_basic'><span>"+
				comMsg.doctemplate_cancel+"</span></a>").css({"margin-bottom":"10px"});
		var pagingPane = jQuery("<div>").css({"margin-top":"10px"});
		var mainPane = jQuery("<div>").attr("id", "templateMenu").addClass("TM_letterWrapper").css("width","150px");		
		
		var closeBtn = jQuery("<a class='btn_X' href='javascript:;' onclick='toggleTemplatePane()'></a>");
		closeBtn.css({"position":"absolute","right":"-5px","top":"-5px"});
		mainPane.append(jQuery("<div>").css({"text-align":"center","pading":"5px","position":"relative"}).append(cancelPane).append(closeBtn));		
		
		if(list){			
			for ( var i = 0; i < list.length; i++) {				
				var link = jQuery("<div onclick='setTemplate("+list[i].seq+")'>" +
						"<img src='/design/common/image/icon/icon_dot.gif' align=absmiddle> "+
						"<a href='javascript:;'>"+list[i].name+"</a></div>");
				link.attr("title",list[i].name);
				link.addClass("TM_templateList")
				.hover(function(){jQuery(this).addClass("item_body_over");},
						function(){jQuery(this).removeClass("item_body_over");});
				mainPane.append(link);				
			}
		}		
		
		if(parseInt(pre) > 0){
			pagingPane.append(
					jQuery("<a href='javascript:;' class='TM_letterPagePre'>"+comMsg.comn_prelist+"</a> &nbsp;")
					.click(function(e){goDocTemplatePage(pre);}));
		}
		
		if(parseInt(next) > 0){
			pagingPane.append(
					jQuery("<a href='javascript:;' class='TM_letterPageNext'>"+comMsg.comn_nextlist+"</a>")
					.click(function(e){goDocTemplatePage(next);}));
		}
		
		mainPane.append(pagingPane);		
		jQuery("#"+this.paneId).append(mainPane);
		this.isOpen = true;		
	},
	getTemplateContents:function(templateSeq,afterFunc){
		var param = {"templateSeq":templateSeq};
		jQuery.post("/common/loadDocTemplate.do", param, function(data, textStatus){
			if(textStatus == "success"){
				if(data.result == "success"){
					afterFunc(data.template);
				} else {
					alert(comMsg.doctemplate_error);
				}
			} else {
				alert(comMsg.doctemplate_error);
				return;
			}
		}, "json");
				
	},
	hidePane:function(){
		jQuery("#templateMenu").hide();
		this.isOpen = false;
	},
	chkOpen:function(){
		return this.isOpen;
	}
	
});

var LetterControl = Class.create({	
	initialize: function(paneId){
		this.isOpen = false; 
		this.letterList = null;
		this.paneId = paneId;
	},
	getViewLetterPagerList:function(page){
		var _this = this;		
		dwr.engine.setAsync(true);		
		
		MailCommonService.getLetterList(page,function(letterInfo){			
			_this.makeLetterList(letterInfo);			
		});	
	},
	makeLetterList:function(letterInfo){
		var list = letterInfo.list;
		var pre = letterInfo.pre;
		var next = letterInfo.next;
		jQuery("#"+this.paneId).empty();
		
		var cancelPane = jQuery("<a href='javascript:;' onclick='clearLetterPaper()' class='btn_basic'><span>"+
				comMsg.comn_cancel+"</span></a>").css({"margin-bottom":"10px"});
		var closeBtn = jQuery("<a class='btn_X' href='javascript:;' onclick='toggleLetterPane()'></a>");
		closeBtn.css({"position":"absolute","right":"-5px","top":"-5px"});
		var pagingPane = jQuery("<div>").css({"margin-top":"10px"});
		var mainPane = jQuery("<div>").attr("id", "letterMenu").addClass("TM_letterWrapper");		
		
		mainPane.append(jQuery("<div>").css({"text-align":"center","pading":"5px","position":"relative"}).append(cancelPane).append(closeBtn));
		
		var cnt = 1;
		for ( var i = 0; i < 6; i++) {
			var link,item;
			var idx = i;
			if(list[i]){
				link = jQuery("<a href='javascript:;' onclick='setLetterPaper("+i+")'></a>");
				item = jQuery("<img>").attr("src",list[i].thumbnail).addClass("TM_letterItem");
				
			} else {
				link = jQuery("<a href='javascript:;'></a>");
				item = jQuery("<img>").attr("src","/design/common/image/blank.gif").addClass("TM_letterItem");				
			}
			
			mainPane.append(link.append(item));
			if(cnt> 1 && (cnt%2 == 0)){
				mainPane.append(jQuery("<br>"));
			}
			cnt++;
		}
		
		if(parseInt(pre) > 0){
			pagingPane.append(
					jQuery("<a href='javascript:;' class='TM_letterPagePre'>"+comMsg.comn_prelist+"</a> &nbsp;")
					.click(function(e){goLetterPage(pre);}));
		}
		
		if(parseInt(next) > 0){
			pagingPane.append(
					jQuery("<a href='javascript:;' class='TM_letterPageNext'>"+comMsg.comn_nextlist+"</a>")
					.click(function(e){goLetterPage(next);}));
		}
		
		mainPane.append(pagingPane);		
		jQuery("#"+this.paneId).append(mainPane);
		this.isOpen = true;
		this.letterList = list;
	},
	setList:function(list){
		this.letterList = list;
	},
	hidePane:function(){
		jQuery("#letterMenu").hide();
		this.isOpen = false;
	},
	chkOpen:function(){
		return this.isOpen;
	},
	getLetterObj:function(idx){		
		return this.letterList[idx];
	}
	
});

function resizeEditorFrame(h) {
	jQuery("#editor___Frame").css("height",h + "px");
	jQuery("#modeHtmlWrapper").css("height",h + "px");
}


/*********** UPLOAD MASS RCPT FUNCTION **************/
function uploadMassUserFile(){
		
	var f = document.massRcptUploadForm;
	jQuery("#upload_rcpt_Frame").makeUploadFrame("up_rcpt_frame");    
	var file = f.massRcptFile.value;
	if(file == "") {
		return;
	}
	
	if(!isConfirmFile(file,"txt")) {
		alert(msgArgsReplace(comMsg.error_nofileext,["txt"]));
		f.reset();
		return;
	}
	
	f.uploadType.value = "basic";
	f.attachtype.value = "massrcpt";
	f.action = "/file/uploadAttachFile.do";
	f.method = "post";
	f.target= "up_rcpt_frame";	
	f.submit();	
}

function addRcptFile(name,size,path){
		
	var f = document.massRcptUploadForm;
	
	f.massFilePath.value = path;
	
	jQuery("#massRcptFile").hide();
	jQuery("#massRcptFileInfo").show();
	jQuery("#massFileName").html(name);
	jQuery("#massFileSize").html(printSize(size));
}

function deleteMassRcptFile(){
	var f = document.massRcptUploadForm;
	jQuery("#upload_rcpt_Frame").makeUploadFrame("up_rcpt_frame");
	
	f.method = "get";	
	f.action = "/file/deleteAttachFile.do";	
	f.target= "up_rcpt_frame";	
	f.submit();	
}

function clearMassRcpt(){
	var f = document.massRcptUploadForm;
	
	f.reset();
	f.massFilePath.value = "";
	
	jQuery("#massRcptFile").show();
	jQuery("#massRcptFileInfo").hide();
	jQuery("#massFileName").empty();
	jQuery("#massFileSize").empty();
}


/*********** AUTO SAVE FUNCTION **************/
var isLayerOn = false;
var preAutoSaveTerm;
var isAutoSaveStart = false;
function getAutoSaveItem(){
	var item;
	if(skin != "skin3"){
		item = "<div style='margin-top:10px;text-align:left;color:#495C79'>"
		+"<img class='menu_icon_img itemSSize item_mail_autosave' src='/design/common/image/blank.gif' align='absmiddle'/>&nbsp;"
		+ mailMsg.autosave_title	
		+"<div id='autoSaveTermSelect'></div>";
	}else{
		item= "<div id='autoSaveTermSelect' style='float:right'></div><div style='float:right;padding:3px 5px 0 0'>"+mailMsg.autosave_title+"</div>";
	}
	
	return jQuery(item);
}

function makeAutoSaveSelect(checkTerm){
	jQuery("#autoSaveTermSelect").empty();
	jQuery("#autoSaveTermSelect").selectbox({selectId:"autoSaveTerm",selectFunc:chgAutoSaveTerm},
			checkTerm,[{index:mailMsg.autosave_option_nosave,value:0},
			            {index:mailMsg.autosave_title+"(30"+mailMsg.autosave_option_sec+")",value:30},
			            {index:mailMsg.autosave_title+"(1"+mailMsg.autosave_option_min+")",value:60},
			            {index:mailMsg.autosave_title+"(3"+mailMsg.autosave_option_min+")",value:180}]);

	
}

function chgAutoSaveTerm(val){
	clearTimeout(autoSaveTimeTerm);
	isAutoSaveStart = false;
	MailCommonService.updateAutoSaveInfo("on",val,function(data){		
		if(data.result != "success"){			
			makeAutoSaveSelect(preAutoSaveTerm);			
			processAutoSaveMessage("saveErrorTerm");			
		} else {			
			preAutoSaveTerm = data.term;			
			runAutoSaveProcess();
			processAutoSaveMessage("saveTerm");
		}		
	});
}

var autoSaveTimeTerm,checkSaveTimeTerm;
function runAutoSaveProcess (isSetting) {
	clearTimeout(autoSaveTimeTerm);
	if($("autoSaveTerm")){
		var quotaInfo = folderControl.getQuotaInfo();
		
		var selectedCheckTime = $("autoSaveTerm").value;		
		var checkTime = selectedCheckTime * 1000;		
		if (checkTime > 0) {
			if(quotaInfo){
				if(Number(quotaInfo.percent) >= 100){
					processAutoSaveMessage("quotaover");
				} else {				
					if(isAutoSaveStart){
						doAutoSave();
					}
					isAutoSaveStart = true;
				}
				autoSaveTimeTerm = setTimeout('runAutoSaveProcess()', checkTime);
			}
		} else {
			isAutoSaveStart = false;
			clearTimeout(autoSaveTimeTerm);
		}
		
	}
}


function processAutoSaveMessage(type,data){	
	clearTimeout(checkSaveTimeTerm);
	var isShow = true;
	var tmpMessage = "" ;

	if(type == "saveMessage"){		
		$("draftMid").value = data.msgId;
		tmpMessage = mailMsg.autosave_message_savesuccess + " ( " + data.updateTime +" )";		
	} else if(type == "saveTerm"){		
		var selectedValue = $('autoSaveTerm').value;
		tmpMessage = mailMsg.autosave_message_termchange;
		if (selectedValue > 0) {
			if (selectedValue < 60)
				tmpMessage += " (" + selectedValue + mailMsg.autosave_option_sec+")"
			else
				tmpMessage += " (" + (selectedValue / 60) + mailMsg.autosave_option_min +")";
		} else {			
			tmpMessage += " ("+mailMsg.autosave_option_nosave+")";
		}		
	}  else if(type == "hideMsg"){
		isShow = false;
	} else if(type == "saveErrorTerm"){
		tmpMessage = mailMsg.autosave_term_error;		
	} else if(type == "saveErrMessage"){
		tmpMessage = mailMsg.autosave_message_error;		
	} else if(type == "quotaover"){
		tmpMessage = mailMsg.mail_send_quotaover_draft	
	}
	
	processMessageViewer(isShow,tmpMessage);
	isAutoSave = false;
}

function isWriteModify() {
	var contentChanged = false;
	if(!$("editorMode")) return false;
	var mode = $("editorMode").value;
	var content = "";
	try{
		if (mode == "html"){
			content = GetMessage("ascheck");
		} else {
			if (isMsie) {
				content = window.textContentFrame.getContent();
    		} else {
    			content = document.getElementById("textContentFrame").contentWindow.getContent();
    		}
			
			content = jQuery.trim(content);
			
		}
		if(content == '<br />'){
			content = "";
		}
		
		if( jQuery.trim(content) != ""){
			contentChanged = true;
			return contentChanged;
		}
		
		if(isRcptModeNormal){
			var keys = new Array("to", "cc", "bcc", "subject");
			for (var i = 0; i < keys.length; i++) {
				var value = $(keys[i]).value;
				if ( value && jQuery.trim(value) != "") {
					contentChanged = true;
					break;
				}
			}
		} else {
			var addrSelect = $("rcptSearchList");
			if(addrSelect.length > 0){
				contentChanged = true;
			}
		} 
	} finally{
		content = null;
	}
	return contentChanged;
}

function doAutoSave(){
	var param = {};	
	try{
		if(isSendWork){return;}	
		if(!isWriteModify()){return;}	
		isAutoSave = true;
		processMessageViewer(true,mailMsg.autosave_message_start);
		
		if(!isRcptModeNormal)makeRcptForm();
		
		var f = document.writeForm;
		var param = {};	
		var editorMode = $("editorMode").value;
		var contentValue;
		if(editorMode=='html') {
			contentValue = GetMessage("draft");
		} else {
			if (isMsie) {
				contentValue = window.textContentFrame.getContent();
    		} else {
    			contentValue = document.getElementById("textContentFrame").contentWindow.getContent();
    		}
			
		}	
		 
		
		param.subject = f.subject.value;	
		param.to = f.to.value;
		param.cc = f.cc.value;
		param.bcc = f.bcc.value;	
		param.sendType = "autosave";
		param.draftMid = $("draftMid").value;	
		
		var flag = f.sendFlag.value
		if(flag.indexOf("forward") > -1){
			flag = "forwardAttached";
		}
		
		param.draft =  contentValue;
		param.sendFlag = flag;
		param.wmode = $("editorMode").value;
		param.uids = f.uid.value;
		param.folder = f.folder.value;	
		param.encharset = $("encharset").value;
		param.massMode = "off";
		param.content =  contentValue;
		
		checkSaveTimeTerm = setTimeout(function(){			
			if(isAutoSave)processAutoSaveMessage("saveErrMessage");
		},10000);
		
		jQuery.post( "/dynamic/mail/sendMessage.do", param, resultSutoSave, "json");
	} finally {
		param = null;
	}
}

function resultSutoSave(data,status){	
	if(status == "success"){
		processAutoSaveMessage("saveMessage",data);
	} else {
		processAutoSaveMessage("saveErrMessage");
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


function checkEmailInvalidAddress(str) {
    var addr_array = getEmailArray(str);

    for(var i = 0; i < addr_array.length; i++) {       
        
        var address = addr_array[i];        
		address = jQuery.trim(address);
		
        var email = get_email(address);		
        // Address Group Check
        // Public Address Group Check
        // Organization 
        if(email.charAt(0) == '$'
            || email.charAt(0) == '&'
            || email.charAt(0) == '+'
            || email.charAt(0) == '#') {
            continue;
        }
       
        var isDomain = false;
        
		if(address == "") {
			continue;
		}else if(isEmail(email)) {			
			continue;
		}		
		else {
			alert(mailMsg.alert_invalidaddress+"\n\n"
				+address);
			return false;
		}
    }

    return true;
}

function checkReserveTime(Y1,M1,d1,h1,m1,maxDay) {	
	var Y= reserveTimeSet.tyear;
	var M= reserveTimeSet.tmonth;
	var d= reserveTimeSet.tday
	var h= reserveTimeSet.thour;
	var m= reserveTimeSet.tmin;	
	
	var date  = new Date(Y,M-1,d,h,m);
	var dateB = new Date(Y1,M1-1,d1,h1,m1);
	dateB.setFullYear(Y1, M1-1, d1);
	dateB.setHours(h1);
	dateB.setMinutes(m1);

	var minTimeStamp = date.getTime()+(30*60*1000);
	var maxTimeStamp = date.getTime() + (maxDay*24*60*60*1000);
	var maxDate = new Date(maxTimeStamp);
	
	var selTimeStamp = dateB.getTime();
	
	if(selTimeStamp < minTimeStamp){
		alert(mailMsg.alert_minreserved);
		return false;
	}
	if (selTimeStamp > maxTimeStamp) {
		alert(msgArgsReplace(mailMsg.alert_maxreserved,[maxDay]));
		return false;
	}

	return true;
}

function settingMailListEvent() {
	
	var folderType = mailListControl.getFolderType();
	var viewMode = mailListControl.getViewMode();
	var isExceptFolder = (folderType == 'sent' || folderType == 'draft' || folderType == 'reserved')?true:false;
	
	var $evtParent = jQuery("#m_messageList");
	
	$evtParent.bind('click', function(e) {
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();

		if (nodeName == "input") {
			if (target.name == "msgId") {
				checkMessage(target);
			}
		} else if (nodeName == "span") {
			var type = $target.attr("type");
			if (type == "flag") {
				var mid = $target.attr("mid");
				switchFlagFlaged([mid],'F',mid+"_flagedF");
			}
		} else if (nodeName == "td") {
			/*
			if ($target.hasClass("TM_list_subject")) {
				var folderName = $target.attr("fname");
				var uid = $target.attr("uid");
				var idx = $target.attr("idx");
				removePreview();
				readMessage(folderName,uid,idx);
			}
			*/
		} else if (nodeName == "img") {
			var $parent = $target.parent();
			var type = $parent.attr("type");
			if (type == "popup") {
				var folderName = $parent.attr("fname");
				var uid = $parent.attr("uid");
				var idx = $parent.attr("idx");
				readPopMessage(folderName,uid,idx);
			}
		} else if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "addr") {
				var idx = $target.attr("idx");
				var idxName = "from_"+idx;
				if (isExceptFolder) {
					idxName = "to_"+idx;
				}
				makeAddrSubMenu(idxName, target);
			}
			else if (type == "subject") {
				var $parent = $target.parent();
				if (viewMode == "v") {
					$parent = $parent.parent("td.TM_list_subject");
				}
				var folderName = $parent.attr("fname");
				var uid = $parent.attr("uid");
				var idx = $parent.attr("idx");
				removePreview();
				
				if (folderType == "draft") {
					writeDraftMessage(folderName,uid,idx);
				} else {
					readMessage(folderName,uid,idx);
				}
			}
		} else if (nodeName == "div") {
			if ($target.is("div.TM_mlist_s_wrapper")) {
				var $parent = $target.parent();
				var folderName = $parent.attr("fname");
				var uid = $parent.attr("uid");
				var idx = $parent.attr("idx");
				removePreview();
				readMessage(folderName,uid,idx);
			}
		}
	});
	
	$evtParent.bind('mouseover', function(e) {
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();
		var dragId;
		if (nodeName == "td") {
			dragId = $target.closest('tr').attr("id");
		} 
		else {
			dragId = $target.parent("td.TM_list_subject").closest('tr').attr("id");
		}
		if (dragId) {
			makeDrag(dragId);
		}
		
		if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "subject") {
				var mtr_id = $target.attr("mtr_id");
				var privIdx = $target.attr("privIdx");
				overPriview("msg_subject_"+mtr_id,privIdx);
			}
		}
	});
	
	$evtParent.bind('mouseout', function(e) {
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();
		
		if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "subject") {
				outPriview();
			}
		}
	});
	

}