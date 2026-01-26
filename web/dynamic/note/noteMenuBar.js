var currentFolderName;

function checkItem(){
	var f = document.listForm;
	if(checkedCnt(f.msgId) == 0){
		alert(comMsg.comn_error_001);
		return false;
	}
	return true;
}

function checkCount(){
	var f = document.listForm;
	return checkedCnt(f.msgId);
}

/*****Quick View Function********/
function showQuickList(target){
	var id = jQuery(target).attr("sid");
	var opt = {size : "100", 
			list : [{name:comMsg.note_msg_022,linkFunc:goQuickList,param:"seen"},
			        {name:comMsg.note_msg_023,linkFunc:goQuickList,param:"unseen"},
			        {name:comMsg.note_msg_024,linkFunc:goQuickList,param:"myself"}
			]};
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());	
}

function goQuickList(target){
	var type = jQuery(target).attr("param");	
	viewQuickList(type);
	clearSubMenu();
}

function viewQuickList(type) {
	
	var param = {};
	var flag = "";
	if(type == "unseen"){
		flag = "U";
	} else if(type == "seen"){			
		flag = "S";
	} else if(type == "myself"){
		flag = "L";
	}
	
	goFolder(currentFolderName, flag);
}

/*****Delete Function********/
function deleteNote(){	
	if(!checkItem())return;
	
	if (!confirm(comMsg.note_msg_045)) {
		return;
	}
	
	var allCheck = false;
	if($("allSearchSelectCheck")){		
		var allSCheck = $("allSearchSelectCheck").value;
		if(allSCheck == "on"){
			allCheck = true;
		}
	}
	
	if (allCheck) {
		allSelectProcess("delete");
	}	
	else {
		var uids = getCheckedListUids();
		var param = {};
		param.uids = uids;
		param.folderName = currentFolderName;
		noteControl.deleteNote(param);
	}
}

function readNoteDelete() {
	if (!confirm(comMsg.note_msg_045)) {
		return;
	}
	var uid = jQuery("#note_read_uid").val();
	var uids = [];
	uids[0] = uid;
	var param = {};
	param.uids = uids;
	param.folderName = currentFolderName;
	noteControl.deleteNote(param);
	
	closeReadNote();
}

/*****Reply Function********/

function replyWrite(){
	if(!checkItem())return;
	
	if(checkCount() > 1){
		alert(comMsg.note_msg_047);
		return;
	}
	
	var uids = getCheckedListUids();
	var uid = uids[0];
	
	email = jQuery("#chk_"+currentFolderName+"_"+uid).attr("femail");
	
	writeToNote(email);
}

function readNoteReply() {
	var uid = jQuery("#note_read_uid").val();
	email = jQuery("#chk_"+currentFolderName+"_"+uid).attr("femail");
	
	writeToNote(email);
	closeReadNote();
}

function readNoteRecall() {
	var uid = jQuery("#note_read_uid").val();
	var msgId = jQuery("#mdn_Sent_"+uid).attr("msgId");
	
	recallMsg(msgId, uid);
	closeReadNote();
}

function moveSave(){	
	if(!checkItem())return;
	
	var allCheck = false;
	if($("allSearchSelectCheck")){		
		var allSCheck = $("allSearchSelectCheck").value;
		if(allSCheck == "on"){
			allCheck = true;
		}
	}
	
	if (allCheck) {
		allSelectProcess("save");
	}	
	else {
		var uids = getCheckedListUids();
		var param = {};
		param.uids = uids;
		param.folderName = currentFolderName;
		noteControl.moveSave(param);
	}
}

function readNoteSave() {
	var uid = jQuery("#note_read_uid").val();
	var uids = [];
	uids[0] = uid;
	var param = {};
	param.uids = uids;
	param.folderName = currentFolderName;
	noteControl.moveSave(param);
	closeReadNote();
}

function rejectNote() {
	if(!checkItem())return;
	
	var uids = getCheckedListUids();
	var isMyselfCheck = false;
	var email;
	var femails = [];
	for ( var i = 0; i < uids.length; i++) {
		email = get_email(jQuery("#chk_"+currentFolderName+"_"+uids[i]).attr("femail"));
		if(email == USEREMAIL){
			isMyselfCheck = true;
			break;			
		} else {
			femails[femails.length] = email;
		}
	}

	if(isMyselfCheck){
		alert(comMsg.note_msg_049);
		return;
	}
	
	var param = {};
	param.emails = femails;
	
	noteControl.rejectNote(param);
}

function readNoteReject() {
	var uid = jQuery("#note_read_uid").val();
	
	var isMyselfCheck = false;
	var email;
	var femails = [];
	
	email = get_email(jQuery("#chk_"+currentFolderName+"_"+uid).attr("femail"));
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
	
	closeReadNote();
}

function searchMessage(){
	var checkLength = 3;
	
	var keyWord = jQuery.trim(jQuery("#skword").val());		
	if(jQuery.trim(keyWord) == ""){
		alert(mailMsg.alert_search_nostr);
		$('skword').focus();
		return;
	}
		
	if(validateHan(keyWord)){
		checkLength = 4;
	}
	
	if(!checkInputLength("", $("skword"), mailMsg.alert_search_nostr, checkLength, 255)) {
		return;
	}
	
	if(!checkInputValidate("", $("skword"), "onlyBack")) {
		return;
	}
	
	var param = {};
	param.folderName = currentFolderName;
	param.keyWord = keyWord;
		
	noteControl.loadNoteList(param);
	
	jQuery("#noteSearchCancel").show();
}

function cancelSearch() {			
	goFolder(currentFolderName);
	resetSearch();
}

function resetSearch() {
	jQuery("#skword").val("");
	jQuery("#noteSearchCancel").hide();
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
		if(id == "quick"){
			subL.css("top","19px");
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
		jQuery("#"+id+"_sub").append(subL);		
		jQuery("#funcSubLayer").css("z-index","10");
		jQuery("#funcSubLayer").show("fast");	
		oldSubMenuID = id;		
		toolbarSubMenuTimeOut = setTimeout("clearSubMenu()",3000);
	} else {
		oldSubMenuID = "";
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

function loadListToolBar(){
	
	jQuery("#siSearchBox").show();
	jQuery("#noteMenubar").show();
	
	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	 	         {type: "B" ,
	 	        Item : new MenuItem("del",
	 			"item_note_del",
	 			mailMsg.menu_delete, "B", deleteNote,false)}
	 		];
	
	var basicGroup2 = [
	   	 	         {type: "B" ,
	   	 	        Item : new MenuItem("move",
	   	 	        "item_note_save",
	   	 			comMsg.note_msg_021, "B", moveSave,true)}
	   	 		];
	
 	var basicGroup3 = [
 	 	         {type: "B" ,
 	 	        Item : new MenuItem("reply",
 	 			"item_note_reply",
 	 			mailMsg.menu_reply, "B", replyWrite,false)}
 	 		];
 	
 	var basicGroup4 = [	 	       
 	 			{type: "B" ,
 	   	 	        Item : new MenuItem("reject", 	   	 		 			
 	   	 		 			"item_note_reject",
 	   	 		 			mailMsg.mail_receivreject, "B", rejectNote,true)
 	   	 	    }	 			
 	 		];
 	
 	var basicGroup5 = [	 	       
 	   	 			{type: "B" ,
 	   	 	        Item : new MenuItem("quick", 	   	 		 			
 	   	 		 			"item_note_search_quick",
 	   	 		 			mailMsg.menu_quick, "B", showQuickList,true)
 	   	 	        }	 		 			
 	   	 		];
 	
 	var basicGroupSkin3 = [
					{type: "B" ,
				      Item : new MenuItem("reply",
						"item_note_reply",
						mailMsg.menu_reply, "B", replyWrite,false)},
					{type: "B" ,
		   	 	        Item : new MenuItem("move",
		   	 	        "item_note_save",
		   	 			comMsg.note_msg_021, "B", moveSave,false)},
		   	 		{type: "B" ,
	   		 	        Item : new MenuItem("del",
	   		 			"item_note_del",
	   		 			mailMsg.menu_delete, "B", deleteNote,false)},
		   		 	{type: "B" ,
   	 	   	 	        Item : new MenuItem("reject", 	   	 		 			
   	 		 			"item_note_reject",
   	 		 			mailMsg.mail_receivreject, "B", rejectNote,false)},
   	 		 		{type: "B" ,
   	  	   	 	        Item : new MenuItem("quick", 	   	 		 			
   	 		 			"item_note_search_quick",
   	 		 			mailMsg.menu_quick, "BM", showQuickList,true)}
 	             ];
 	
 	var menuList;
 	
 	if(skin != "skin3"){
 	
 		menuList = [
	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:"",
	            	groupItem : [basicGroup1,basicGroup2,basicGroup3,basicGroup4,basicGroup5]}
	               ];
 	}else{
 		menuList = [
 	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:"",
 	            	groupItem : [basicGroupSkin3]}
 	               ];
 	}

	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	if(skin != "skin3"){
		setMailMenuBarStatus();
	}
	else{
		setMailMenuBarStatusSkin3();
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		jQuery("#pageNavi").css("padding-right","8px");
	}
}

function setMailMenuBarStatus(){
	menuBar.initToolBar();
	if(currentFolderName == "Sent"){
		jQuery("#basic_2").hide();
		jQuery("#basic_3").hide();
	}else if(currentFolderName == "Save"){
		jQuery("#basic_1").hide();
		jQuery("#basic_2").hide();
		jQuery("#basic_3").hide();
	}
}
function setMailMenuBarStatusSkin3(){
	menuBar.initToolBarSkin3();
	if(currentFolderName == "Sent"){
		menuBar.hideMenu("reply");
		menuBar.hideMenu("reject");
	}else if(currentFolderName == "Save"){		
		menuBar.hideMenu("move");
		menuBar.hideMenu("reject");
		menuBar.hideMenu("reply");
	}
}