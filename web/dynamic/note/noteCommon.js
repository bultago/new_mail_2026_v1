var mainSplitter, noteControl, menuBar, noteListControl, pagingInfo;

function makeToolbarMenu(data) {
	var totalCnt = data.messageCount;		
	var unreadCnt = data.unseenCount;
	var folderName = data.folderName;
	var page = data.page;
	var pageBase = data.pageBase;
	var totalMessage = data.total;
	var folderAliasName = data.folderAliasName;

	var pagingInfo = {"total":totalMessage,"pageBase":pageBase};
	
	currentFolderName = folderName;
	
	loadListToolBar();
	/*menuBar.setPageNavi("p",
			{total:totalMessage,
			base:pageBase,
			page:page,
			isListSet:true,
			isLineCntSet:true,
			pagebase:pageBase,
			changeAfter:reloadNoteList});*/
	
	menuBar.setPageNaviBottom("p",
			{total:totalMessage,
			base:pageBase,
			page:page,
			isListSet:true,
			isLineCntSet:true,
			pagebase:pageBase,
		changeAfter:reloadNoteList});
		
	setCurrentPage(page);
	
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
	$("m_msgListWrapper").scrollTop = 0;
	inResizefunc(jQuery(window),jQuery("#m_msgListWrapper"),true);
	outResizefunc(jQuery(window),jQuery("#m_msgListContent"),true);
	menuBar.toggleSideItem(false);
	
	updateWorkReadCount(folderAliasName,totalCnt,unreadCnt);
}

function updateUnreadNote() {
	var param = {};
	jQuery.post("/dynamic/note/noteInfo.do", param, function(data) {
		if (data && data.unSeenCount > 0) {
			jQuery("#mf_Inbox_newCnt").html("(<a href='#' onclick='unseenNote()' class='TM_unseenLink'>"+data.unSeenCount+"</a>)");
		} else {
			var data = {};
			data.unSeenCount = 0;
			jQuery("#mf_Inbox_newCnt").html("");
		}
		var count = "("+data.unSeenCount+")";
		jQuery("#unseen_note_count").text(count);
		jQuery("#unseen_note_count_left").text(count);
	}, "json");
}

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;
	}
	
	var topAreaSize = jQuery("#ml_btnFMain_s").outerHeight(true)+4;	
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;	
	
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

function closeWriteNote() {
	jQuery("#noteWriteDialog").jQpopup("close");
}

function closeReadNote() {
	jQuery("#noteReadDialog").jQpopup("close");
}

function resetWriteForm() {
	jQuery("#myselfCheck").attr("checked", false);
	jQuery("#to").val("");
	jQuery("#saveSentCheck").attr("checked", true);
	jQuery("#note_content").val("");
	checkMaxLength();
}

function resetReadForm() {
	jQuery("#note_read_uid").val("");
	jQuery("#note_read_content").val("");
	jQuery("#read_from").text("");
	jQuery("#read_date").text("");
	jQuery("#read_to").text("");
	jQuery("#pre_mail").empty();
	jQuery("#next_mail").empty();
	jQuery("#bar_mail").empty();
	
	jQuery("#read_note_reply").hide();
	jQuery("#read_note_recall").hide();
	jQuery("#read_note_save").hide();
	jQuery("#read_note_delete").hide();
	jQuery("#read_note_reject").hide();
}

function checkReadMenu(folderName, mdnCode) {
	if (folderName == 'Inbox') {
		jQuery("#read_note_reply").show();
		jQuery("#read_note_save").show();
		jQuery("#read_note_delete").show();
		jQuery("#read_note_reject").show();
	} else if (folderName == 'Sent') {
		jQuery("#read_note_save").show();
		jQuery("#read_note_delete").show();
		if (mdnCode == "300") {
			jQuery("#read_note_recall").show();
		}
	} else {
		jQuery("#read_note_reply").show();
		jQuery("#read_note_delete").show();
	}
}

function updateWorkReadCount(folderName,totalCnt,unreadCnt){
	var workTitle = "<span class='TM_work_title' title='"+folderName+"'>"+folderName+"</span>";
		workTitle = workTitle + "&nbsp;" + msgArgsReplace(comMsg.note_msg_020,[totalCnt,unreadCnt]);
	setWorkTitle(workTitle);
}

function setWorkTitle(msg) {
	jQuery("#workTitle").html(msg);
}

function emulEvent(el) {
	el = document.getElementById(el);

	if (typeof el.onpropertychange != 'undefined') return;

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

function insertMySelf(obj) {
	jQuery("#to").focus();
	var toValue = jQuery("#to").val();
	
	if (obj.checked) {
		var setValue;
		if (toValue.indexOf(USEREMAIL) < 0) {
			if(toValue != ""){				
				if(toValue.substring(toValue.length-1,toValue.length) == ',')setValue = toValue + USEREMAIL;
				else setValue = toValue + "," + USEREMAIL + ",";
			} else {
				setValue = USEREMAIL + ",";
			}			
			jQuery("#to").val(setValue);
		}
	}
	else {
		var addr_array = getEmailArray(toValue);
		var pstr = "";
		
		if(toValue != ""){
			for(var i = 0; i < addr_array.length; i++) {
	        	var address = addr_array[i];
				if (address.indexOf(USEREMAIL) < 0 && address != "") {
					pstr += address + ",";
				}
			}
		}
		jQuery("#to").val(pstr);
	}
}

function checkMaxLength() {
	var data = jQuery("#note_content").val();
	var dataLength = data.length;
	if (dataLength > 400) {
		alert(msgArgsReplace(comMsg.note_msg_030,[400]));
		jQuery("#note_content").val(data.substring(0,400));
		return false;
	} else {
		jQuery("#word_count").text(dataLength);
		return true;
	}
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
	}
	
	toggleAllCheckMessage(bool);
}

function checkMessage(chkObj){
	var id = chkObj.id;		
	var bool = chkObj.checked;		
			
	var trObj = chkObj;
	while (trObj.tagName != "TR") {
        trObj = trObj.parentNode;
    }
	
	if(bool){			
		jQuery(trObj).addClass("TM_checkLow");			
	} else {
		jQuery(trObj).removeClass("TM_checkLow");	
	}					
}

function toggleAllCheckMessage(bool){
	
	if(Number(pagingInfo.total) <= Number(pagingInfo.pageBase)){
		return;
	}
	var esize = jQuery("#m_msgListWrapper").data("extSize");
	if(bool){
		var colspanCnt = 5;
		
		if(currentFolderName == "Sent"){
			colspanCnt = 6;
		}		
		
		var allContentHtml = "<tr id='allSelectTr' style='height:55px;display:none;'><td colspan='"+colspanCnt+"' class='TM_allcheckLow' id='allSelectTd' style='padding:5px;height:40px;'>"		
		+"<div id='allSelectUncheck'>"
	    + comMsg.note_msg_052+"<br/>"
	    + "<a href='javascript:;' onclick='onSelectSearchAllMessage()'>"
	    + msgArgsReplace(comMsg.note_msg_053,[pagingInfo.total])
	    + "</a>"
	    +"</div>"
	    +"<div id='allSelectCheck'  style='display:none'>"
	    + comMsg.note_msg_054 +"<br/>"
	    + "<a href='javascript:;' onclick='offSelectSearchAllMessage()'>"
	    + mailMsg.allselect_004
	    + "</a>&nbsp;("+comMsg.note_msg_055+")"
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

function getCheckedListUids() {
	var f = document.listForm;
	var uids = new Array();
	if (f.msgId.length) {
		var count = 0;
        for (var i = 0; i < f.msgId.length; i++) {
            if (f.msgId[i].checked) {
            	uids[count++] = f.msgId[i].value;
            }
        }
    }
    else {
        if(f.msgId.checked) {
        	uids[0] = f.msgId.value;
        }
    }
	
	return uids;
}