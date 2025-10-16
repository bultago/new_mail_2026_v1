function loadToolBar(){

	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	  	 	         {type: "B" ,
	  	 	        Item : new MenuItem("writeBt",
	  	 			"item_bbs_write",
	  	 			bbsMsg.bbs_write, "B", writeContent,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("replyBt",
	  	 			"item_bbs_reply",
	  	 			bbsMsg.bbs_reply, "B", replyContent,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("modifyBt",
	  	 			"item_bbs_modify",
	  	 			bbsMsg.bbs_modify, "B", modifyContent,false)},
	  	 			{type: "B" ,
		  	 	        Item : new MenuItem("deleteBt",
		  	 			"item_bbs_del",
		  	 			bbsMsg.bbs_delete, "B", deleteContent,false)}
	  	 		];
	
	var basicGroup2 = [
		  	 	     {type: "B" ,
		  	 	        Item : new MenuItem("moveBt",
		  	 			"item_bbs_move",
		  	 			bbsMsg.bbs_move_content, "B", writeMail,false)},
	  	 			 {type: "B" ,
		  	 	        Item : new MenuItem("popupBt",
		  	 			"item_bbs_popup",
		  	 			bbsMsg.bbs_popup, "B", viewContentPopup,false)},
		  	 	     {type: "B" ,
		  	 	        Item : new MenuItem("printBt",
		  	 			"item_comm_print",
		  	 			bbsMsg.bbs_print, "B", viewContentPrint,false)}
		  	 	];
	
	var basicGroupSkin3 = [
	  		  	 			{type: "B" ,
		  		  	 	        Item : new MenuItem("replyBt",
		  		  	 			"item_bbs_reply",
		  		  	 			bbsMsg.bbs_reply, "B", replyContent,false)},
	  		  	 			{type: "B" ,
		  		  	 	        Item : new MenuItem("modifyBt",
		  		  	 			"item_bbs_modify",
		  		  	 			bbsMsg.bbs_modify, "B", modifyContent,false)},
	  		  	 			{type: "B" ,
	  			  	 	        Item : new MenuItem("deleteBt",
	  			  	 			"item_bbs_del",
	  			  	 			bbsMsg.bbs_delete, "B", deleteContent,false)},
	  			  	 		{type: "B" ,
  				  	 	        Item : new MenuItem("moveBt",
  				  	 			"item_bbs_move",
  				  	 			bbsMsg.bbs_move_content, "B", writeMail,false)}
	  		  	 		];
	
	var menuList;
	if(skin != "skin3"){
		menuList = [
	               {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
	            	groupItem : [basicGroup1, basicGroup2]}
	               ];
	}else{
		menuList = [
		            {name:mailMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroupSkin3]}
		               ];
	}
		
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	if(skin == "skin3"){
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		bbsPopupButtonEmpty();
		bbsPrintButtonEmpty();
		bbsPopButtonSet();
		bbsPrintButtonSet();
		jQuery("#pageNavi").css("padding-right","8px");		
	}
}

function bbsPopupButtonEmpty(){
	jQuery("#popup").empty();
}

function bbsPrintButtonEmpty(){
	jQuery("#print").empty();
}

function bbsPopButtonSet(){
	var popupTag = jQuery("<a href='#'></a>");
	popupTag.css("padding","0 5px");
	popupTag.click(function(e){viewContentPopup();});
	var imgPopupTag = jQuery("<img>");
	imgPopupTag.attr("src","/design/skin3/image/icon/ic_blank.gif");
	imgPopupTag.attr("title",comMsg.comn_new_window);
	popupTag.append(imgPopupTag);
	jQuery("#popup").append(popupTag);
}

function bbsPrintButtonSet(){
	var printTag = jQuery("<a href='#'></a>");
	printTag.css("padding","0 5px");
	printTag.click(function(e){viewContentPrint();});
	var imgPrintTag = jQuery("<img>");
	imgPrintTag.attr("src","/design/skin3/image/icon/ic_printer.gif");
	imgPrintTag.attr("title",comMsg.comn_print);
	printTag.append(imgPrintTag);
	jQuery("#print").append(printTag);
}

jQuery.fn.groupListDrow = function(flist,folderImg,linkFunc,type){
	var drowList = jQuery(this);
	
	var drowListWrap = jQuery("<ul>");
	
	for (var i = 0 ; i < flist.length ; i++) {
		var folderName = flist[i].name;
		var id = flist[i].id;
		
		var fItem = jQuery("<li></li>");
		fItem.addClass("flist_item");
		fItem.append(jQuery("<img>").attr("src",folderImg).css("margin-right","5px"));	
		fItem.append(jQuery("<span></span>").append(folderName));
		fItem.click(function(){linkFunc(jQuery(this),type);});
		fItem.attr("title",folderName);
		fItem.attr("fname",id);
		drowListWrap.append(fItem);
	}
	drowList.append(drowListWrap);
	return drowList;
};

var oldSubMenuID="";
function showSubMenuLayer(id,obj){	
	clearSubMenu();
	
	if(oldSubMenuID != id ){
		var subL = jQuery("<div></div>");
		subL.attr("id","funcSubLayer");
		subL.addClass("sub_item_canvas");
		subL.attr("pid",id);			
		subL.append(obj);
		subL.hover(function(){
					var wrap = jQuery(this);
					var id = wrap.attr("pid");					
					wrap.show();
				},function(){
					clearSubMenu();
				});
		jQuery("#"+id+"_sub").append(subL);		
		jQuery("#funcSubLayer").css("z-index","1");
		jQuery("#funcSubLayer").show("fast");
		oldSubMenuID = id;
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
	}	
}

function changeToolbarMode(tabId){
	menuBar.makeToolbar(tabId);	
}
/////////////////////////////////////////////////////////////////////////////////
function listPage() {
	var f = document.forms[0];
	f.method = "post";
	f.action = "/bbs/listContent.do";
	f.submit();
}

function writeContent() {
	var f = document.forms[0];
	f.isReply.value="false";
	f.isModify.value="false";
	f.orderNo.value=0;
	f.depth.value=0;
	f.parentId.value=0;
	f.contentId.value=0;
	f.method = "post";
	f.action = "/bbs/writeContent.do";
	f.submit();
}

function searchMine() {
	var f = document.forms[0];
	f.searchType.value = "7";
	f.currentPage.value = "1";
	f.action = "/bbs/listContent.do";
	f.method = "post";
	f.submit();
}

function searchContent() {
	var f = document.forms[0];
	
	var keywordObj = jQuery("#skword");
	
	if(!checkInputLength("jQuery", keywordObj, mailMsg.alert_search_nostr, 2, 64)) {
		return;
	}
	
	if (!checkInputText($("skword"), 2, 64, true)){
		return false;
	}
	
	if(!checkInputValidate("jQuery", keywordObj, "onlyBack")) {
		return;
	}
	
	f.searchType.value = f.inputSearchType.value;
	f.keyWord.value = keywordObj.val();
	f.action = "/bbs/listContent.do";
	f.method = "post";
	f.submit();
}