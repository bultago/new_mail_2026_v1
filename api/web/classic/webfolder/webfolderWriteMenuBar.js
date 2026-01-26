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
	  	 	        Item : new MenuItem("attachFile",
	  	 			"item_webfolder_wmail",
	  	 			webfolderMsg.folder_file_attach, "B", attachFile,false)}
	  	 		];
	
	var menuList = [
		               {name:comMsg.menu_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1]}
		           ];
	
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");
}

function attachFile(){
	window.fraContent.addAttachFile();
}

function changeToolbarMode(tabId){
	menuBar.makeToolbar(tabId);
	window.fraContent.checkMenubar();
}
function listPage() {}