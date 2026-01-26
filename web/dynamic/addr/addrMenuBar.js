var currentToolbarMode;

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
		this.sideMenu.append(positionH);
		this.sideMenu.append(positionV);
	},
	getItem: function(){
		return this.sideMenu;
	},
	setIcon:function(mode){
		switch (mode) {			
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



function loadPrivateToolBar(){

	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	  	 	         {type: "B" ,
	  	 	        Item : new MenuItem("addMember",
	  	 			"item_addr_addmember",
	  	 			addrMsg.addr_btn_add_member, "B", addMember,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("modiMember",
	  	 	        "item_addr_modmember",
	  	 			addrMsg.addr_btn_modi_member, "B", modiMember,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("delMember",
	  	 			"item_addr_delmember",
	  	 			addrMsg.addr_btn_del_member, "B", deleteMember,false)}
	  	 		];
	
	var basicGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("addGroup",
  	 	        		"item_addr_addgroup",
		  	 			addrMsg.addr_btn_add_group, "B", addGroup,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("moveMember",
		  	 			"item_addr_movemember",
		  	 			addrMsg.addr_btn_move_member, "B", moveMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("copyMember",
			  	 			"item_addr_copymember",
			  	 			addrMsg.addr_btn_copy_member, "B", copyMember,true)}
		  	 		];
	
	var basicGroup3 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("sendMail",
  	 	        		"item_addr_sendmail",
		  	 			addrMsg.addr_btn_send, "B", sendMail,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("sendMailAll",
		  	 			"item_addr_sendallmail",
		  	 			addrMsg.addr_btn_sendall, "B", sendMailToAll,false)}
		  	 		];
	
	var extraGroup1 = [
 	                   {type: "B" ,
 	                	Item : new MenuItem("print",
 	                	"item_comm_print",
 	                	addrMsg.addr_btn_print, "B", printAddress,false)}
 	 	   	 	 		];
	
	var extraGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("export",
		  	 			"item_addr_export",
		  	 			addrMsg.addr_btn_export, "B", exportAddress,true)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("import",
		  	 			"item_addr_import",
		  	 			addrMsg.addr_btn_import, "B", importAddress,false)}
		  	 		];
	
	var skin3Group = [
		  	 	        {type: "B" ,
			  	 	        Item : new MenuItem("addMember",
			  	 			"item_addr_addmember",
			  	 			addrMsg.addr_btn_add_member, "B", addMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("modiMember",
			  	 	        "item_addr_modmember",
			  	 			addrMsg.addr_btn_modi_member, "B", modiMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("delMember",
			  	 			"item_addr_delmember",
			  	 			addrMsg.addr_btn_del_member, "B", deleteMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("sendMail",
	  	 	        		"item_addr_sendmail",
			  	 			addrMsg.addr_btn_send, "B", sendMail,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("sendMailAll",
			  	 			"item_addr_sendallmail",
			  	 			addrMsg.addr_btn_sendall, "B", sendMailToAll,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("moveMember",
			  	 			"item_addr_movemember",
			  	 			mailMsg.menu_move, "BSM", moveMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("copyMember",
			  	 			"item_addr_copymember",
			  	 			mailMsg.menu_copy, "BSM", copyMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("option",
			  	 			"item_addr_option",
			  	 			mailMsg.menu_option_option, "BM", privateAddrShowOptionList,true)}
		  	 		];
	
		if(skin != "skin3"){
			var menuList = [
				               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
				            	groupItem : [basicGroup1, basicGroup2, basicGroup3]},
				               {name:addrMsg.addr_btn_tab_extra,id:"extra",initOn:true,linkFunc:changeToolbarMode,
				            		groupItem : [extraGroup1, extraGroup2]}
				               ];
		}else{
			var menuList = [
				               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
				            	groupItem : [skin3Group]}
				               ];
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
	menuBar.makeToolbar("basic");
	paneControl.setIcon(paneMode);	
	
	jQuery("#addrTypeLabel").text(addrMsg.addr_tree_tab1_title);
	currentToolbarMode = "basic";
	if(skin=="skin3"){
		emptyAddressPrint();
		addressPrintButtonSet();
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		jQuery("#pageNavi").css("padding-right","8px");
		jQuery("#menuBarTab").css("width","600px");
	}
}
function privateAddrShowOptionList(target){
	var id = jQuery(target).attr("sid");
	var opt = {size : "100", 
			list : [{name:addrMsg.addr_btn_export,linkFunc:addrOptionListFunc,param:"export"},
			        {name:addrMsg.addr_btn_import,linkFunc:addrOptionListFunc,param:"import"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:mailMsg.menu_option_preview,linkFunc:addrOptionListFunc,param:"preview"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:addrOptionListFunc,param:"right"},			        
			        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:addrOptionListFunc,param:"down"}			        
			]};	
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());
}

function sharedAddrShowOptionList(target){
	
	var writeAuth = jQuery('#writeAuth').val();
	var readAuth = jQuery('#readAuth').val();
	var creatorAuth = jQuery('#creatorAuth').val();
	var id = jQuery(target).attr("sid");
	var createOpt = {size : "100", 
			list : [{name:addrMsg.addr_btn_add_book,linkFunc:addrOptionListFunc,param:"addBook"},
			        {name:addrMsg.addr_btn_rename_book,linkFunc:addrOptionListFunc,param:"renameBook"},
			        {name:addrMsg.addr_btn_del_book,linkFunc:addrOptionListFunc,param:"delShareBook"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:addrMsg.addr_btn_manage_reader+" "+addrMsg.addr_tree_add_label,linkFunc:addrOptionListFunc,param:"addrReader"},
			        {name:addrMsg.addr_btn_manage_moderator+" "+addrMsg.addr_tree_add_label,linkFunc:addrOptionListFunc,param:"addrModerator"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:addrMsg.addr_btn_export,linkFunc:addrOptionListFunc,param:"export"},
			        {name:addrMsg.addr_btn_import,linkFunc:addrOptionListFunc,param:"import"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:mailMsg.menu_option_preview,linkFunc:addrOptionListFunc,param:"preview"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:addrOptionListFunc,param:"right"},			        
			        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:addrOptionListFunc,param:"down"}			        
			]};	
	
	var readOpt = {size : "100", 
			list : [{name:mailMsg.menu_option_preview,linkFunc:addrOptionListFunc,param:"preview"},	
			        {name:"",linkFunc:"",param:"split"},
			        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:addrOptionListFunc,param:"right"},			        
			        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:addrOptionListFunc,param:"down"}			        
			]};
	
	var writeOpt = {size : "100", 
			list : [{name:addrMsg.addr_btn_manage_reader+" "+addrMsg.addr_tree_add_label,linkFunc:addrOptionListFunc,param:"addrReader"},
			        {name:"11",linkFunc:"",param:"split"},
			        {name:addrMsg.addr_btn_export,linkFunc:addrOptionListFunc,param:"export"},
			        {name:addrMsg.addr_btn_import,linkFunc:addrOptionListFunc,param:"import"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:mailMsg.menu_option_preview,linkFunc:addrOptionListFunc,param:"preview"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:"<img src='/design/skin3/image/right.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_right,linkFunc:addrOptionListFunc,param:"right"},			        
			        {name:"<img src='/design/skin3/image/down.gif' class='optionImg'/>&nbsp;"+mailMsg.menu_option_down,linkFunc:addrOptionListFunc,param:"down"}			        
			]};
	var slist;
	if(creatorAuth == "Y"){
		slist = new SubToolbarList(createOpt);
	}else if(writeAuth == "Y"){
		slist = new SubToolbarList(writeOpt);
	}else{
		slist = new SubToolbarList(readOpt);
	}
	showSubMenuLayer(id,slist.getList());
}


function addrOptionListFunc(target){
	
	var type = jQuery(target).attr("param");
	
	if(type == "addBook"){
		addSharedBook();	
	}else if(type == "renameBook"){
		renameSharedBook();
	}else if(type == "delShareBook"){
		delSharedBook();
	}else if(type == "addrReader"){
		manageReader();
	}else if(type == "addrModerator"){
		manageModerator();
	}else if(type == "export"){
		exportAddress();
	}else if(type == "import"){
		importAddress();
	}else if(type == "right"){
		contentSplitterChange('v')
	}else if(type == "down"){
		contentSplitterChange('h');
	}
	clearSubMenu();
}
function emptyAddressPrint(){
	jQuery("#print").empty();
}
function addressPrintButtonSet(){
	var printTag = jQuery("<a href='#'></a>");
	printTag.css("padding","0 5px");
	printTag.click(function(e){printAddress();});
	var imgPrintTag = jQuery("<img>");
	imgPrintTag.attr("src","/design/skin3/image/icon/ic_printer.gif");
	imgPrintTag.attr("title",comMsg.comn_print);
	printTag.append(imgPrintTag);
	jQuery("#print").append(printTag);
}
function loadSharedToolBar(){

	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	  	 	         {type: "B" ,
	  	 	        Item : new MenuItem("addMember",
	  	 			"item_addr_addmember",
	  	 			addrMsg.addr_btn_add_member, "B", addMember,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("modiMember",
	  	 			"item_addr_modmember",
	  	 			addrMsg.addr_btn_modi_member, "B", modiMember,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("delMember",
	  	 			"item_addr_delmember",
	  	 			addrMsg.addr_btn_del_member, "B", deleteMember,false)}
	  	 		];
	
	var basicGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("addGroup",
		  	 			"item_addr_addgroup",
		  	 			addrMsg.addr_btn_add_group, "B", addGroup,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("moveMember",
		  	 			"item_addr_movemember",
		  	 			addrMsg.addr_btn_move_member, "B", moveSharedMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("copyMember",
			  	 			"item_addr_copymember",
			  	 			addrMsg.addr_btn_copy_member, "B", copySharedMember,true)}
		  	 		];
	
	var basicGroup3 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("sendMail",
		  	 			"item_addr_sendmail",
		  	 			addrMsg.addr_btn_send, "B", sendMail,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("sendMailAll",
		  	 			"item_addr_sendallmail",
		  	 			addrMsg.addr_btn_sendall, "B", sendMailToAll,false)}
		  	 		];
	
	var extraGroup1 = [
 	                   {type: "B" ,
 	                	Item : new MenuItem("print",
 	                	"item_comm_print",
 	                	addrMsg.addr_btn_print, "B", printAddress,false)}
 	 	   	 	 		];
	
	var extraGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("export",
		  	 			"item_addr_export",
		  	 			addrMsg.addr_btn_export, "B", exportAddress,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("import",
		  	 			"item_addr_import",
		  	 			addrMsg.addr_btn_import, "B", importAddress,false)}
		  	 		];
	
	var extraGroup3 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("addAddressBook",
		  	 			"item_addr_addaddr",
		  	 			addrMsg.addr_btn_add_book, "B", addSharedBook,false)},
		  	 			
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("reNameAddressBook",
			  	 			"item_addr_modaddr",
			  	 			addrMsg.addr_btn_rename_book, "B", renameSharedBook,false)},
		  	 			
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("deleteAddressBook",
		  	 			"item_addr_deladdr",
		  	 			addrMsg.addr_btn_del_book, "B", delSharedBook,false)},
		  	 			
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("addReader",
		  	 			"item_addr_reader",
		  	 			addrMsg.addr_btn_manage_reader, "B", manageReader,false)},
		  	 			
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("addModerator",
		  	 			"item_addr_moderator",
		  	 			addrMsg.addr_btn_manage_moderator, "B", manageModerator,false)}
		  	 		];
	
	var skin3Group = [
		  	 	        {type: "B" ,
			  	 	        Item : new MenuItem("addMember",
			  	 			"item_addr_addmember",
			  	 			addrMsg.addr_btn_add_member, "B", addMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("modiMember",
			  	 	        "item_addr_modmember",
			  	 			addrMsg.addr_btn_modi_member, "B", modiMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("delMember",
			  	 			"item_addr_delmember",
			  	 			addrMsg.addr_btn_del_member, "B", deleteMember,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("sendMail",
	  	 	        		"item_addr_sendmail",
			  	 			addrMsg.addr_btn_send, "B", sendMail,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("sendMailAll",
			  	 			"item_addr_sendallmail",
			  	 			addrMsg.addr_btn_sendall, "B", sendMailToAll,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("moveMember",
			  	 			"item_addr_movemember",
			  	 			mailMsg.menu_move, "BSM", moveSharedMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("copyMember",
			  	 			"item_addr_copymember",
			  	 			mailMsg.menu_copy, "BSM", copySharedMember,true)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("option",
			  	 			"item_addr_option",
			  	 			mailMsg.menu_option_option, "BM", sharedAddrShowOptionList,true)}
		  	 		];
	if(skin != "skin3"){
		var menuList = [
		               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1, basicGroup2, basicGroup3]},
		               {name:addrMsg.addr_btn_tab_extra,id:"extra",initOn:true,linkFunc:changeToolbarMode,
		            		groupItem : [extraGroup1, extraGroup2,extraGroup3]}
		               ];
	}else{
		var menuList = [
			               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
			            	groupItem : [skin3Group]}			               
			               ];
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
	menuBar.makeToolbar("basic");
	paneControl.setIcon(paneMode);	
	
	jQuery("#addrTypeLabel").text(addrMsg.addr_tree_tab2_title);
	
	currentToolbarMode = "basic";
	if(skin == "skin3"){
		setMenuBarStatusSkin3();		
	}
}

function loadSimpleSharedToolBar(){
	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var extraGroup3 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("addAddressBook",
		  	 			"item_addr_addaddr",
		  	 			addrMsg.addr_btn_add_book, "B", addSharedBook,false)}
		  	 		];
	
	var menuList = [
		               {name:addrMsg.addr_btn_tab_extra,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            		groupItem : [extraGroup3]}
		               ];
	
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
	menuBar.makeToolbar("basic");
	paneControl.setIcon(paneMode);	
	
	jQuery("#addrTypeLabel").text(addrMsg.addr_tree_tab2_title);
	
	currentToolbarMode = "basic";
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
var toolbarSubMenuTimeOut;
function showSubMenuLayer(id,obj){	
	clearSubMenu();
	
	if(oldSubMenuID != id ){
		var subL = jQuery("<div></div>");
		subL.attr("id","funcSubLayer");
		subL.addClass("sub_item_canvas");
		subL.attr("pid",id);	
		if(skin == "skin3"){
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
		jQuery("#funcSubLayer").css("z-index","1");
		jQuery("#funcSubLayer").show("fast");
		oldSubMenuID = id;
		toolbarSubMenuTimeOut = setTimeout("clearSubMenu()",1000);
	} else {
		oldSubMenuID = "";
	}	
	
}

function toggleToolBarItem(id,isOver){
	
}

function clearSubMenu(){
	var oldL = jQuery("#funcSubLayer");
	if($("funcSubLayer")){		
		oldL.hide();		
		oldL.remove();	
		oldSubMenuID = "";		
	}	
}

function addMember(){
	var param;
	jQuery('#addAddress #memberSeq').val('');
	jQuery('#addAddress #memberSeq').val('');
	jQuery('#addAddress #firstName').val('');
	jQuery('#addAddress #lastName').val('');
	jQuery('#addAddress #middleName').val('');
	jQuery('#addAddress #memberName').val('');
	jQuery('#addAddress #nickName').val('');
	jQuery('#addAddress #memberEmail').val('');
	jQuery('#addAddress #birthDay').val('');
	jQuery('#addAddress #anniversaryDay').val('');
	jQuery('#addAddress #mobileNo').val('');
	jQuery('#addAddress #homePost1').val('');
	jQuery('#addAddress #homePost2').val('');
	jQuery('#addAddress #homeCountry').val('');
	jQuery('#addAddress #homeState').val('');
	jQuery('#addAddress #homeCity').val('');
	jQuery('#addAddress #homeStreet').val('');
	jQuery('#addAddress #homeExtAddress').val('');
	jQuery('#addAddress #homeTel').val('');
	jQuery('#addAddress #homeFax').val('');
	jQuery('#addAddress #privateHomepage').val('');
	jQuery('#addAddress #companyName').val('');
	jQuery('#addAddress #departmentName').val('');
	jQuery('#addAddress #titleName').val('');
	jQuery('#addAddress #officePost1').val('');
	jQuery('#addAddress #officePost2').val('');
	jQuery('#addAddress #officeCountry').val('');
	jQuery('#addAddress #officeState').val('');
	jQuery('#addAddress #officeCity').val('');
	jQuery('#addAddress #officeStreet').val('');
	jQuery('#addAddress #officeExtAddress').val('');
	jQuery('#addAddress #officeTel').val('');
	jQuery('#addAddress #officeFax').val('');
	jQuery('#addAddress #officeHomepage').val('');
	
	var groupPopOpt = clone(popupOpt);
	
	var bookSeq = jQuery('#bookSeq').val();
	if(bookSeq==''){
		groupPopOpt.btnList = [{name:comMsg.comn_confirm,func:okPrivateMemberAddPressed}];
	}else{
		groupPopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedMemberAddPressed}];
	}
	
	groupPopOpt.minHeight = 450;
	groupPopOpt.minWidth = 420;
	groupPopOpt.openFunc = function(){
		jQuery(".TB_addressAdd").css("width","100%");
		toggleTab2('basic_header', 'fragment-', 1, 2, 3);
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
	};
	groupPopOpt.closeFunc = function(){
		jQuery(".TB_addressAdd").css("width","");
	};
	
	jQuery("#addAddrDialog").jQpopup("open",groupPopOpt);
}

function modiMember(){
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery('#groupSeq').val();
	
	var ids = [];
	idMap.each(function(val) {
		ids[ids.length] =val.value;
	});
	
	if(ids.length ==0){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	if(ids.length >1){
		alert(addrMsg.addr_info_msg_015);
		return;
	}
	
	modifyAddress(ids[0]);
}

function deleteMember(){
	
	
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery('#groupSeq').val();
	
	var ids = [];
	idMap.each(function(val) {
		ids[ids.length] =val.value;
	});
	
	if(ids.length ==0){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	if(!confirm(addrMsg.addr_info_msg_043))
		return;
	
	AddressBookService.deleteMember(bookSeq, groupSeq, ids,
			{callback:function(){	
				var param = addrControl.listParam;
				if(param == null){
					param = {};
				}
				addrControl.loadAddressList(param);
			}}
	);
	
	initEmail();
}

function moveMember(target){
	var id = jQuery(target).attr("sid");
	
	var userFList = leftMenuControl.getPrivateGroupList();
	
	if(idMap.size()==0)
	{
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	var flist = jQuery("<div></div>").addClass("flist_drow")
	.groupListDrow(userFList,"/design/default/image/icon/icon_plus.gif",copyMoveMsg, "move");
	
	showSubMenuLayer(id,flist);
}

function moveSharedMember(target){
	var id = jQuery(target).attr("sid");
	
	var userFList = leftMenuControl.getPrivateGroupList();
	
	if(idMap.size()==0)
	{
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	var bookSeq = jQuery('#bookSeq').val();
	var _this = this;
	AddressBookService.getJSonSharedGroupList(bookSeq,
			{callback:function(groupList){
				var dlist = [];
				dlist[0] =  {"id":"0", "name":addrMsg.addr_common_label_001};
				groupList.each(function(f){			
					dlist[dlist.length] =  {"id":f.id, "name":f.name};
				});	
				
				var flist = jQuery("<div></div>").addClass("flist_drow")
				.groupListDrow(dlist,"/design/default/image/icon/icon_plus.gif",copyMoveMsg, "move");
				
				showSubMenuLayer(id,flist);
			}}
	);
	
	
}

function copyMoveMsg(target,type){	
	var toFolderSeq = jQuery(target).attr("fname");
	
	var ids = [];
	idMap.each(function(val) {
		ids[ids.length] =val.value;
	}); 
	var bookSeq = jQuery('#bookSeq').val();
	if(type == "copy"){
		AddressBookService.copyMember(bookSeq, ids,toFolderSeq,
				{callback:function(){	
					var param = addrControl.listParam;
					if(param == null){
						param = {};
					}
					idMap = new Hash();
					addrControl.loadAddressList(param);
				}}
		);
	} else if(type == "move"){
		var groupSeq = jQuery('#groupSeq').val();
		
		AddressBookService.moveMember(bookSeq, ids, groupSeq, toFolderSeq,
				{callback:function(){	
					var param = addrControl.listParam;
					if(param == null){
						param = {};
					}
					idMap = new Hash();
					addrControl.loadAddressList(param);
				}}
		);
	}
	clearSubMenu();
}

function copyMember(target){
	var id = jQuery(target).attr("sid");
	
	if(idMap.size()==0)
	{
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	var userFList = leftMenuControl.getPrivateGroupList2();
	
	var flist = jQuery("<div></div>").addClass("flist_drow")
	.groupListDrow(userFList,"/design/default/image/icon/icon_plus.gif",copyMoveMsg, "copy");
	
	showSubMenuLayer(id,flist);
}

function copySharedMember(target){
	var id = jQuery(target).attr("sid");
	
	if(idMap.size()==0)
	{
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	var bookSeq = jQuery('#bookSeq').val();
	var _this = this;
	AddressBookService.getJSonSharedGroupList(bookSeq,
			{callback:function(groupList){
				var dlist = [];
				dlist[0] =  {"id":"0", "name":addrMsg.addr_common_label_001};
				groupList.each(function(f){			
					dlist[dlist.length] =  {"id":f.id, "name":f.name};
				});	
				
				var flist = jQuery("<div></div>").addClass("flist_drow")
				.groupListDrow(dlist,"/design/default/image/icon/icon_plus.gif",copyMoveMsg, "copy");
				
				showSubMenuLayer(id,flist);
			}}
	);
	
	
}

function exportAddress(target){
	
	var importPopOpt = clone(popupOpt);
	importPopOpt.btnList = [{name:comMsg.comn_confirm,func:doExportAddress}];
	importPopOpt.minHeight = 120;
	importPopOpt.minWidth = 300;
	importPopOpt.openFunc = function() {
		jQuery("#theVendorSelect").selectbox({selectId:"theVendor",
			selectFunc:""},
			"",
			[{index:"TMS",value:"1"},
			 {index:"Outlook Express",value:"2"},
			 {index:"Outlook",value:"3"}]);
		
		jQuery("#theEncodingSelect").selectbox({selectId:"theEncoding",
			selectFunc:""},
			"",
			[{index:"----",value:""},
			 {index:"EUC-KR",value:"euc-kr"},
			 {index:"SHIFT-JIS",value:"shift-jis"},
			 {index:"UTF-8",value:"utf-8"}]);
		
	};
	importPopOpt.closeFunc = function() {
		jQuery("#theVendorSelect").empty();
		jQuery("#theEncodingSelect").empty();
	};
	jQuery("#exportAddressDialog").jQpopup("open",importPopOpt);
}

function doExportAddress(){
	
	var f = $("downloadForm");
	jQuery("#workHiddenFrame").makeUploadFrame("down_hidden_frame");    
    f.target= "down_hidden_frame";    
	f.theBookSeq.value = jQuery('#bookSeq').val();
	f.theGroupSeq.value = jQuery('#groupSeq').val();
	
	f.action = "/addr/downloadAddr.do";
	f.method = "post";	
	f.submit();
	
	jQuery("#exportAddressDialog").jQpopup("close");
}

function importAddress(){
	var f = $("uploadForm");
	f.theFile.value = '';

	var importPopOpt = clone(popupOpt);
	importPopOpt.btnList = [{name:comMsg.comn_confirm,func:uploadfile}];
	importPopOpt.minHeight = 270;
	importPopOpt.minWidth = 360;
	importPopOpt.openFunc = function() {
		jQuery("#vendorSelect").selectbox({selectId:"vendor",
			selectFunc:""},
			"",
			[{index:"TMS",value:"1"},
			 {index:"Outlook Express",value:"2"},
			 {index:"Outlook",value:"3"},
			 {index:"Thunderbird",value:"4"}]);
		
		jQuery("#encodingSelect").selectbox({selectId:"encoding",
			selectFunc:""},
			"",
			[{index:"----",value:""},
			 {index:"EUC-KR",value:"euc-kr"},
			 {index:"SHIFT-JIS",value:"shift-jis"},
			 {index:"UTF-8",value:"utf-8"}]);
		
		if(jQuery('#bookSeq').val() !=""){
			AddressBookService.getJSonSharedGroupList(jQuery('#bookSeq').val(),
				{callback:function(groupList){		
					printGroup(groupList);
				}}
			);
		}else{
			var groupList = leftMenuControl.getPrivateGroupList();
			printGroup(groupList);
		}
		jQuery("#dupAddressSelect").selectbox({selectId:"dupAddrType",
			selectFunc:""},
			"",
			[{index:addrMsg.addr_add_type_02,value:"addrAddDup"},
			 {index:addrMsg.addr_add_type_01,value:"addrOverWrite"},
			 {index:addrMsg.addr_add_type_03,value:"noAddAddr"}]);
		
	};
	importPopOpt.closeFunc = function() {
		jQuery("#vendorSelect").empty();
		jQuery("#encodingSelect").empty();
		jQuery("#toGroupSelect").empty();
		jQuery("#dupAddressSelect").empty();
	};
	
	jQuery("#importAddressDialog").jQpopup("open",importPopOpt);
}

function printGroup(groupList){
	var groups = [];
	var currentGroup = jQuery('#groupSeq').val();
	for (var i=0; i<groupList.length; i++) {
		groups.push({index:groupList[i].name, value:groupList[i].id});
	}
	jQuery("#toGroupSelect").selectbox({selectId:"toGroup",
	selectFunc:"",height:60},currentGroup,groups);
}

function addGroup(){
	var bookSeq = jQuery('#bookSeq').val();
	
	var groupPopOpt = clone(popupOpt);
	if(bookSeq==''){
		jQuery('#pGroupName').val('');
		
		groupPopOpt.btnList = [{name:comMsg.comn_confirm,func:okPrivateGroupAddPressed}];
		groupPopOpt.minHeight = 100;
		groupPopOpt.minWidth = 300;
		
		jQuery("#privateGroupDialog").jQpopup("open",groupPopOpt);
	}else{
		jQuery('#sGroupName').val('');
		
		groupPopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedGroupAddPressed}];
		groupPopOpt.minHeight = 100;
		groupPopOpt.minWidth = 300;
		
		jQuery("#sharedGroupDialog").jQpopup("open",groupPopOpt);
	}
}

function sendMail(){
	var emails = $("emails").value;
	if(emails==""){
		alert(addrMsg.addr_info_msg_030);
		return;
	}
	
	var f = $("mailForm");
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");
	f.to.value = emails;
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";
	f.target = "popupWrite";
	f.submit();	
}

function sendMailToAll(){
	var bookSeq = jQuery('#bookSeq').val();
	
	var f = $("mailForm");		
	if(jQuery("#groupSeq").val() == '0'){
		alert(addrMsg.addr_info_msg_045);
		return;
	}
	if(bookSeq==''){
		f.to.value = "$"+jQuery("#selectedGroupName").val();
	}else{
		f.to.value = "&" + bookSeq + "."+jQuery("#selectedGroupName").val();
	}
	
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";
	f.target = "popupWrite";
	f.submit();		
}

function sendSingleMail(name, email){
	var fullEmail = "\"" + name + "\""+ "<"+ email+ ">";
	
	var f = $("mailForm");
	
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");
	f.to.value = fullEmail;
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";	
	f.target = "popupWrite";
	f.submit();
}

function printAddress(){
	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width=540,height=640");
	var param = addrControl.listParam;
	
	var f = $("printForm");
	f.target = "popupRead";
	f.action = "/dynamic/addr/listAddress.do";
	f.method = "post";
	f.bookSeq.value = jQuery('#bookSeq').val();
	f.groupSeq.value = param.groupSeq;
	if(param.leadingPattern)
		f.leadingPattern.value = param.leadingPattern;
	if(param.searchType)
		f.searchType.value = param.searchType;
	if(param.keyWord)
		f.keyWord.value = param.keyWord;
	
	f.method = "post";
	f.submit();
}

function movePage(page){
	addrControl.movePage(page);
}

function reloadListPage(){
	addrControl.movePage(currentPage);
}

function contentSplitterChange(mode){
	contentSplitter.setSplitter(mode, false, true);
	paneMode = mode;
	paneControl.setIcon(paneMode);
	
	viewAddress(selectedId);
	var param = addrControl.listParam;
	param.paneMode = paneMode;
	addrControl.loadAddressList(param);
}

function changeToolbarMode(tabId){
	menuBar.makeToolbar(tabId);
	currentToolbarMode = tabId;
	setMenuBarStatus();
}

function addSharedBook(){
	var bookPopOpt = clone(popupOpt);
	bookPopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedAddressBookAddPressed}];
	bookPopOpt.minHeight = 100;
	bookPopOpt.minWidth = 300;
	
	jQuery("#sharedAddrAddInput").val('');
	jQuery("#sharedAddrDialog").jQpopup("open",bookPopOpt);
}

function renameSharedBook(){
	var bookPopOpt = clone(popupOpt);
	bookPopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedAddressBookRenamePressed}];
	bookPopOpt.minHeight = 100;
	bookPopOpt.minWidth = 300;
	
	jQuery("#sharedAddrRenameInput").val(jQuery("#addrGroupLabel").text());
	jQuery("#sharedAddrRenameDialog").jQpopup("open",bookPopOpt);
}

function delSharedBook(){
	var bookSeq = jQuery('#bookSeq').val();
	
	if(!confirm(addrMsg.addr_info_msg_037))
		return;
	
	
	
	AddressBookService.deleteSharedAddressBook(bookSeq, 
			{callback:function(){		
				addrControl.loadSharedBookList();
			}}
	);
}

function manageSharedBook(){
	
}

function readReaderList(){
	addrControl.popParam = {};
	var bookSeq = jQuery('#bookSeq').val();
	var param = {"bookSeq" : bookSeq};
	
	addrControl.loadReaderView(param);
	
}

function disposeAddReaderDialog(){
	jQuery("#addReaderFrame").empty();	
}
function manageReader(){
	
	var readerPopOpt = clone(popupOpt);
	
	readerPopOpt.btnList = [];
	readerPopOpt.minHeight = 350;
	readerPopOpt.minWidth = 400;
	readerPopOpt.openFunc = readReaderList;
	readerPopOpt.closeFunc = disposeAddReaderDialog;
	jQuery("#addReaderDialog").jQpopup("open",readerPopOpt);
	
	
}

function readModeratorList(){
	addrControl.popParam = {};
	var bookSeq = jQuery('#bookSeq').val();
	var param = {"bookSeq" : bookSeq};
	
	addrControl.loadModeratorView(param);	
}

function disposeAddModeratorDialog(){
	jQuery("#addModeratorFrame").empty();	
}

function manageModerator(){
	var moderatorPopOpt = clone(popupOpt);
	
	moderatorPopOpt.btnList = [];
	moderatorPopOpt.minHeight = 350;
	moderatorPopOpt.minWidth = 400;
	moderatorPopOpt.openFunc = readModeratorList;
	moderatorPopOpt.closeFunc = disposeAddModeratorDialog;
	jQuery("#addModeratorDialog").jQpopup("open",moderatorPopOpt);
}
function setMenuBarStatusSkin3(){
	menuBar.initToolBarSkin3();
	var writeAuth = jQuery('#writeAuth').val();
	var readAuth = jQuery('#readAuth').val();
	var creatorAuth = jQuery('#creatorAuth').val();
	
	if(writeAuth!='Y'){
		menuBar.hideMenu("addMember");
		menuBar.hideMenu("modiMember");
		menuBar.hideMenu("delMember");
		menuBar.hideMenu("export");
		menuBar.hideMenu("import");		
	}	
}
function setMenuBarStatus(){
	menuBar.initToolBar();
	
	var writeAuth = jQuery('#writeAuth').val();
	var readAuth = jQuery('#readAuth').val();
	var creatorAuth = jQuery('#creatorAuth').val();
	
	if(currentToolbarMode == "basic"){
		if(writeAuth!='Y'){
			jQuery("#basic_0").hide();
			jQuery("#basic_1").hide();
		}
	}else{
		if(writeAuth!='Y'){
			jQuery("#extra_1").hide();
		}
		
		if(creatorAuth!='Y'){
			jQuery("#extra_2").hide();
		}
		
		if(writeAuth!='Y'){
			menuBar.hideMenu("addReader");
			menuBar.hideMenu("addModerator");
		}
	}
}

function toggleTab2(header, body, idx1, idx2, idx3) {
	if(jQuery("#"+body + idx1).css("display") =='none'){
		jQuery("#"+body + idx1).css("display", "block");
		jQuery("#"+body + idx2).css("display", "none");
		jQuery("#"+body + idx3).css("display", "none");
	}
	jQuery("#"+header + idx1).removeClass("btn_tabmenu2_on");
	jQuery("#"+header + idx2).removeClass("btn_tabmenu2_on");
	jQuery("#"+header + idx3).removeClass("btn_tabmenu2_on");
	
	jQuery("#"+header + idx1).removeClass("btn_tabmenu2_off");
	jQuery("#"+header + idx2).removeClass("btn_tabmenu2_off");
	jQuery("#"+header + idx3).removeClass("btn_tabmenu2_off");
	
	jQuery("#"+header + idx1).addClass("btn_tabmenu2_on");
	jQuery("#"+header + idx2).addClass("btn_tabmenu2_off");
	jQuery("#"+header + idx3).addClass("btn_tabmenu2_off");
}