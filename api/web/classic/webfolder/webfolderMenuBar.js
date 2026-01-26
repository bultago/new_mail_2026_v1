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
	  	 	        Item : new MenuItem("createFolder",
	  	 			"item_webfolder_add",
	  	 			webfolderMsg.folder_create, "B", createFolderMenu,false)},
	  	 			{type: "B" ,
	  	 	        Item : new MenuItem("renameFolder",
	  	 			"item_webfolder_rename",
	  	 			webfolderMsg.folder_rename, "B", renameFolderMenu,false)},
	  	 			{type: "B" ,
		  	 	        Item : new MenuItem("deleteFolderAndFile",
		  	 			"item_webfolder_del",
		  	 			webfolderMsg.folder_delete, "B", deleteFolderAndFile,false)}
	  	 		];
	
	var basicGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("searchCopyTarget",
		  	 			"item_webfolder_copy",
		  	 			webfolderMsg.folder_copy, "B", searchCopyTarget,false)},
		  	 			{type: "B" ,
		  	 	        Item : new MenuItem("searchMoveTarget",
		  	 			"item_webfolder_move",
		  	 			webfolderMsg.folder_move, "B", searchMoveTarget,true)},
		  	 			{type: "B" ,
			  	 	    Item : new MenuItem("downloadFiles",
			  	 		"item_webfolder_download",
			  	 		webfolderMsg.folder_filedownload, "B", downFiles,true)}
		  	 		];
	
	var basicGroup3 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("writeMail",
		  	 			"item_webfolder_wmail",
		  	 			webfolderMsg.folder_writeMail, "B", writeMail,false)}
		  	 		];
	
	var extraGroup1 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("currentShare",
		  	 			"item_webfolder_currentshare",
		  	 			webfolderMsg.folder_share, "B", openShareFolder,false)}
		  	 		];
	
	var extraGroup2 = [
		  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("viewShare",
		  	 			"item_webfolder_viewshare",
		  	 			webfolderMsg.share_folder_info, "B", viewShareFolder,false)},
		  	 			 {type: "B" ,
			  	 	        Item : new MenuItem("deleteShare",
			  	 			"item_webfolder_delshare",
			  	 			webfolderMsg.folder_cancel, "B", deleteShareFolder,false)}
		  	 		];
	
	var basicGroupSkin3 = [
	                       
						 {type: "B" ,
			  	 	        Item : new MenuItem("createFolder",
			  	 			"item_webfolder_add",
			  	 			webfolderMsg.folder_create, "B", createFolderMenu,false)},
			  	 		{type: "B" ,
			  	 	        Item : new MenuItem("renameFolder",
			  	 			"item_webfolder_rename",
			  	 			webfolderMsg.folder_rename, "B", renameFolderMenu,false)},
			  	 		{type: "B" ,
			  	 	        Item : new MenuItem("deleteFolderAndFile",
			  	 			"item_webfolder_del",
			  	 			webfolderMsg.folder_delete, "B", deleteFolderAndFile,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("searchMoveTarget",
			  	 			"item_webfolder_move",
			  	 			webfolderMsg.folder_move, "B", searchMoveTarget,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("searchCopyTarget",
			  	 			"item_webfolder_copy",
			  	 			webfolderMsg.folder_copy, "B", searchCopyTarget,false)},
		  	 			{type: "B" ,
			  	 	        Item : new MenuItem("writeMail",
			  	 			"item_webfolder_wmail",
			  	 			webfolderMsg.folder_writeMail, "B", writeMail,false)},
		  	 			{type: "B" ,
				  	 	    Item : new MenuItem("downloadFiles",
				  	 		"item_webfolder_download",
				  	 		webfolderMsg.folder_filedownload, "B", downFiles,false)},
			  	 		{type: "B" ,
				  	 	    Item : new MenuItem("shareOption",
				  	 		"item_webfolder_shareOption",
				  	 		webfolderMsg.webfolder_share, "BM", showShareOptionList,true)}
	                  ];
	
	var menuList;
	
	if(skin != "skin3"){
		menuList = [
		               {name:comMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1, basicGroup2, basicGroup3]},
		               {name:comMsg.menu_extra,id:"extra",initOn:true,linkFunc:changeToolbarMode,
		            		groupItem : [extraGroup1, extraGroup2]}
		               ];
	}else{
		menuList = [
		               {name:comMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroupSkin3]}               
		               ];
	}
	
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
	
	if(skin == "skin3"){
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
	}
}

function showShareOptionList(target){
	var id = jQuery(target).attr("sid");
	var opt = {size : "100", 
			list : [{name:webfolderMsg.folder_share,linkFunc:OptionListFunc,param:"folderShare"},
			        {name:"",linkFunc:"",param:"split"},
			        {name:webfolderMsg.share_folder_info,linkFunc:OptionListFunc,param:"shareInfo"},			        
			        {name:webfolderMsg.folder_cancel,linkFunc:OptionListFunc,param:"folderCancel"}			        
			]};	
	var slist = new SubToolbarList(opt);
	showSubMenuLayer(id,slist.getList());	
}
function OptionListFunc(target){
	
var type = jQuery(target).attr("param");
	
	if(type == "folderShare"){
		openShareFolder();
	}else if(type == "shareInfo"){
		viewShareFolder();
	}else if(type == "folderCancel"){
		deleteShareFolder();
	}
	clearSubMenu();
	
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
		if(id="shareOption"){
			subL.css("top","19px");
		}
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
	window.fraContent.checkMenubar();
}

function createFolderMenu() {
	createFolderWin();
}
function renameFolderMenu() {
	window.fraContent.renameFolderWin();
}
function deleteFolderAndFile() {
	window.fraContent.remove();
}
function searchCopyTarget() {
	window.fraContent.search('copy');
}
function searchMoveTarget() {
	window.fraContent.search('move');
}
function writeMail() {
	window.fraContent.writeMail();
}
function openShareFolder() {
	window.fraContent.shareFolderDialog();
}
function viewShareFolder() {
	window.fraContent.shareFolderDialog('view');
}
function deleteShareFolder() {
	window.fraContent.deleteShareFolder();
}
function listPage() {
	
}
function downFiles() {
	window.fraContent.downloadFiles();
}