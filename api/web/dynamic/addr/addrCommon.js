var LayoutInfo = {mode:"privateAddrList"};
var mainSplitter, contentSplitter, mainTreeMap;
var leftMenuControl, addrControl, dndManager;
var menuBar,paneControl;
var idMap = new Hash();
var paneMode;

jQuery().ready(function(){
	setTopMenu('addr');	
//	PageMainLoadingManager.initLoadView();
//	
//	var jobStack = [];
//	jobStack.push("tree");
//	jobStack.push("list");
//	
//	PageMainLoadingManager.startLoad(jobStack);		
	
	var mainLayerPane = new LayerPane("a_mainBody","TM_m_mainBody");
	var menuLayerPane = new LayerPane("a_leftMenu","TM_m_leftMenu",220,220,350);
	var contentLayerPaneWapper = new LayerPane("a_contentBodyWapper","",300,100,200);
	
	mainSplitter = new SplitterManager(mainLayerPane,
			menuLayerPane,
			contentLayerPaneWapper,
			"sm","mainvsplitbar","hsplitbar");
	mainSplitter.setReferencePane(["a_contentBody","copyRight"]);
	mainSplitter.setSplitter("v",true);
	jQuery(window).autoResize(jQuery("#a_mainBody"), "#copyRight");
	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();
	
	
	var contentLayerPane = new LayerPane("a_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("a_contentMain","TM_m_contentMain",450,450,600);
	var previewLayerPane = new LayerPane("a_contentSub","TM_m_contentSub",200,200,300);
	
	contentSplitter = new SplitterManager(contentLayerPane,
											listLayerPane,
											previewLayerPane,
											"sc","vsplitbar","hsplitbar");	
	
	contentSplitter.setSplitter("h",true);
	paneMode = contentSplitter.getMode();
	contentSplitter.setSplitter("h",true);	
	
	jQuery(window).autoResize(jQuery("#a_contentBody"),"#copyRight");
	
	addrControl = new AddrControl(addrOption);
	leftMenuControl = new LeftMenuControl();
	
	//loadPrivateToolBar();
	HistoryManager.loadHistoryManager(historyCallBackFunc);
	
	initService();	
		
	setLeftMenuName();
	
	
	setWorkTitle("privateGroup");
	
	contentSplitterChange('h');	
	jQuery(window).autoResize(jQuery("#a_mainBody"), "#copyRight");
	jQuery.removeProcessBodyMask();
	
	jQuery("#searchTypeSelect").selectbox({selectId:"searchType",
		selectFunc:""},
		"",
		[{index:addrMsg.addr_table_header_001,value:"fname"},
		 {index:addrMsg.addr_table_header_002,value:"email"},
		 {index:addrMsg.addr_table_header_003,value:"mobile"},
		 {index:addrMsg.addr_table_header_004,value:"company"},
		 {index:addrMsg.addr_table_header_005,value:"tel"}]);
	setTimeout(function(){
		jQuery(window).trigger("resize");
	},1000);
});

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;
	}
	var topAreaSize = jQuery("#ml_btnFMain_s").outerHeight(true)+4;
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#a_leftMenu", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontent", 
		mainId:"#a_leftMenu", 
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

var LeftMenuControl = Class.create({
	initialize: function(){
		this.pfolderList = null;
		this.addressList;
	},
	
	getPrivateGroupList:function(){
		var dlist = [];
		dlist[0] =  {"id":"0", "name":addrMsg.addr_common_label_001};
		this.pfolderList.each(function(f){			
			dlist[dlist.length] =  {"id":f.id, "name":f.name};
		});	
		
		return dlist;
	},
	getAddressList:function() {
		return this.addressList;
	},
	getPrivateGroupList2:function(){
		var dlist = [];
		this.pfolderList.each(function(f){			
			dlist[dlist.length] =  {"id":f.id, "name":f.name};
		});	
		
		return dlist;
	},
	
	addPrivateGroup : function(){
		addGroup();
	},
	
	addSharedGroup : function(seq){
		jQuery('#bookSeq').val(seq)
		addGroup();
	},
	
	managePrivateGroup : function(){
		var _this = this;
		jQuery("#newPrivateGroupName").val("");
		leftMenuControl.loadPrivateAddressGroup();
		
		var managePopOpt = clone(popupOpt);
		managePopOpt.btnList = [{name:comMsg.comn_confirm,func:okPrivateGroupManagePressed}
								,{name:comMsg.comn_del,func:deletePrivateGroupPressed}];
		managePopOpt.minHeight = 200;
		managePopOpt.minWidth = 300;
		managePopOpt.openFunc = function() {
			var groupList = leftMenuControl.getAddressList();
			if (groupList && groupList.length > 0) {
				var groups = [];
				var currentGroup = jQuery('#groupSeq').val();
				for (var i=0; i<groupList.length; i++) {
					groups.push({index:escape_tag(groupList[i].name), value:groupList[i].id});
				}
				jQuery("#privateGroupFolderSelect").selectbox({selectId:"privateGroupFolder",
					selectFunc:"",height:60,width:180},currentGroup,groups);
			}
		};
		managePopOpt.closeFunc = function() {
			jQuery("#privateGroupFolderSelect").empty();
		};
		jQuery("#managePrivateGroupDialog").jQpopup("open",managePopOpt);
	},
	
	manageSharedGroup : function(seq){
		var _this = this;
		
		AddressBookService.getJSonSharedGroupList(seq,
				{callback:function(groupList){
					jQuery('#bookSeq').val(seq);
					jQuery('#newSharedGroupName').val('');

					var managePopOpt = clone(popupOpt);
					managePopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedGroupManagePressed}
											,{name:comMsg.comn_del,func:deleteSharedGroupPressed}];
					managePopOpt.minHeight = 200;
					managePopOpt.minWidth = 300;
					managePopOpt.openFunc = function() {
						if (groupList && groupList.length > 0) {
							var groups = [];
							var currentGroup = jQuery('#groupSeq').val();
						for (var i=0; i<groupList.length; i++) {
							groups.push({index:groupList[i].name, value:groupList[i].id});
						}
						
						jQuery("#sharedGroupFolderSelect").selectbox({selectId:"sharedGroupFolder",
							selectFunc:"",height:60},currentGroup,groups);
						}
					};
					managePopOpt.closeFunc = function() {
						jQuery("#sharedGroupFolderSelect").empty();
					};
					jQuery("#manageSharedGroupDialog").jQpopup("open",managePopOpt);
				}}
		);
	},
	
	addSharedAddressBook : function(){
		jQuery('#sharedAddrDialog').dialog('open');
	},
	
	loadPrivateAddressGroup : function(){
		var _this = this;
		AddressBookService.getJSonPrivateGroupList(
				{callback:function(addressList){		
					_this.printPrivateAddress(addressList);
					_this.addressList = addressList;
				}}
		);
	},
	
	loadSharedAddressGroup : function(book){
		var target = jQuery('#'+book).find("#sharedGroups");
		
		var _this = this;
		AddressBookService.getJSonSharedGroupList(book,
				{callback:function(addressList){		
					_this.printSharedAddressGroup(book, target, addressList);
				}}
		);
	},
	
	printPrivateAddress : function(addressList){
		this.pfolderList = addressList;
		jQuery('#privateGroups').empty();
		var groupName;
		var groupSeq;
		for (var i=0; i<addressList.length; i++) {
			groupName = htmlReplace(addressList[i].name);
			groupSeq = addressList[i].id;
			var str = leftMenuControl.getPrivateTreeNodeHtml(groupSeq, groupName);
			jQuery("<li><span class='group'>"+str+"</span></li>").appendTo("#privateGroups");
		}
		
		//PageMainLoadingManager.completeWork("tree");
	},
	
	loadSharedAddressBook : function(){
		addrControl.loadSharedBookList();
	},
	
	printSharedAddressBook : function(bookList){
		var _this = this;
		jQuery('#sharedTreeviewer').empty();
		
		addrControl.loadSharedAddress();
		
		jQuery('#sharedTreeviewer').treeview();
	},
	
	printSharedAddressGroup : function(bookSeq, obj, groupList){
		obj.empty();
		var groupName;
		var groupSeq;
		for (var i=0; i<groupList.length; i++) {
			groupName = groupList[i].name;
			groupSeq = groupList[i].id;
			var str = leftMenuControl.getSharedTreeNodeHtml(bookSeq, groupSeq, groupName);
			jQuery("<li>"+str+"</li>").appendTo(obj);
		}
	},
	
	showPrivateTreeViewer : function(){
		jQuery('#privateTreeviewerWrapper').show();
		jQuery('#sharedTreeviewerWrapper').hide();
		
		jQuery('#bookSeq').val('');
		
		loadPrivateToolBar();
		
		viewPrivateGroupMember('', 0);
	},
	
	showSharedTreeViewer : function(){
		jQuery('#privateTreeviewerWrapper').hide();
		jQuery('#sharedTreeviewerWrapper').show();	
		jQuery('#sharedTreeviewer').empty();
		jQuery('#sharedTreeviewer').hide();
		jQuery('#bookSeq').val('');
		
		AddressBookService.getJSonSharedAddressBookList( 
				{callback:function(bookList){
					if(bookList.length==0){
						
						AddressBookService.getJsonAuth(0, 
								{callback:function(auth){	
									if('Y' !=auth.creatorAuth){
										jQuery("#basic_0").hide();
									}
									viewSharedGroupMember(addrMsg.addr_tree_all_label,-1,0);
								}}
						);
					}else{
						var bookName;
						var bookSeq;
						for (var i=0; i<bookList.length; i++) {
							bookName = bookList[i].name;
							bookSeq = bookList[i].id;
							
							var str = leftMenuControl.getSharedBookListHtml(bookSeq, bookName);
							jQuery("#sharedTreeviewer").append(str);
						}
						jQuery('#sharedTreeviewer').treeview();
						viewSharedGroupMember(bookList[0].name,bookList[0].id, 0);
					}
				}}
		);
		
		jQuery('#sharedTreeviewer').show();		
	},
	
	getPrivateTreeNodeHtml : function(groupSeq, nodeName){
		var str = "";
		str += "<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>";
		str += "<tbody>";
		str += "<tr>";
		str += "<td class='treeNodeWrapperContents treeNodeHeight'>";
		str += "<a id='label_"+groupSeq+"' onclick='viewPrivateGroupMember(\""+nodeName+"\","+groupSeq+");' href='javascript:;'>"+nodeName+"</a>";
		str += "</td>";
		str += "</tr>";
		str += "</tbody>";
		str += "</table>";
		
		return str;
	},
	
	getSharedBookListHtml : function(bookSeq, bookName){
		var str = "";
		str += "<li class='closed'>";
		str += 	"<div class='folder'>";
		str += 		"<a id='book_"+bookSeq+"' href='javascript:;' onclick='viewSharedGroupMember(\""+bookName+"\","+bookSeq+", 0);'>"+bookName+"</a>";
		str += 	"</div>";
		str += 	"<ul id='"+bookSeq+"'>";
		str += 		"<a href='javascript:;' onclick='viewSharedGroupMember(\""+bookName+"\","+bookSeq+", 0);' class='myfolder_plus'>"+addrMsg.addr_tree_all_label+"</a>";
		str += 		"<li class='closed'>";
		str += 		"<div class='tree-toolbar'>";
		str += 		"<a onclick='leftMenuControl.addSharedGroup("+bookSeq+");' href='javascript:;'>"+addrMsg.addr_tree_add_label+"</a>";
		str += 		"<a onclick='leftMenuControl.manageSharedGroup("+bookSeq+");' href='javascript:;'>"+addrMsg.addr_tree_manage_label+"</a>";
		str += 		"</div>";
		str += 		"<div id='sharedBookGroup' class='folder' style='float:left;'><a onclick='leftMenuControl.loadSharedAddressGroup("+bookSeq+");' href='javascript:;'>"+addrMsg.addr_tree_groups_label+"</a></div>";
		str += 		"<div class='cls'></div>";
		str += 		"<ul id='sharedGroups'>";
		str += 		"</ul>";
		str += 		"</li>";
		str += 	"</ul>";
		str += "</li>";
		
		return str;
	},
	
	getSharedTreeNodeHtml : function(bookSeq, groupSeq, nodeName){
		var str = "";
		str += "<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>";
		str += "<tbody>";
		str += "<tr>";
		str += "<td class='treeNodeWrapperContents treeNodeHeight'>";
		str += "<a id='label_"+bookSeq+"_"+groupSeq+"' onclick='javascript:viewSharedGroupMember(\""+nodeName+"\","+ bookSeq+","+groupSeq+");' href='javascript:;'>"+nodeName+"</a>";
		str += "</td>";
		str += "</tr>";
		str += "</tbody>";
		str += "</table>";
		
		return str;
	},
	
	moveMember : function(){
	}
});

var AddrControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.param = null;
		this.listParam = {};
		this.popParam = null;
		this.addrParam = null;
	},
	loadAddressList : function(param){
		var addrbookSeq = jQuery('#bookSeq').val();
		
		if(param){
			this.param = param;			
		}
		
		if(Number(addrbookSeq) > 0){
			LayoutInfo.mode = "sharedAddrList";
			param.bookSeq = addrbookSeq;
			param.paneMode = paneMode;
		} else {
			LayoutInfo.mode = "privateAddrList";
		}		
		
		if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
			param.bookSeq = 0;
		
		if(!param.memberSeq || typeof(param.memberSeq) == 'undefined' || param.memberSeq == 'undefined')
			param.memberSeq = 0;
		
		ActionLoader.loadAction(this.opt.mainLID, 
				this.opt.listAction, param,true);
		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.listAction,param,LayoutInfo.mode);
	},
	loadAddressView : function(param){
		if(param){
			this.addrParam = param;			
		}
		if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
			param.bookSeq = 0;
		
		if(!param.memberSeq || typeof(param.memberSeq) == 'undefined' || param.memberSeq == 'undefined')
			param.memberSeq = 0;
		
		ActionLoader.loadAction(this.opt.subLID, this.opt.viewAction, param,false);
	},
	SharedAddressBook : function(param){
		
		ActionLoader.loadAction(this.opt.subLID, this.opt.viewAction, param,false);
	},
	loadReaderView:function(param){
		if(param){
			this.popParam = param;			
		}
		
		jQuery("#addReaderFrame").empty();
		jQuery("#addReaderFrame").load(addrOption.readerListAction+"?dummy="+makeRandom(), param,function(){
			jQuery("div#addReaderFrame table").css("width","100%");
		});
	},
	loadModeratorView:function(param){
		if(param){
			this.popParam = param;			
		}
		jQuery("#addModeratorFrame").empty();
		jQuery("#addModeratorFrame").load(addrOption.moderatorListAction, param,function(){
			jQuery("div#addModeratorFrame table").css("width","100%");			
		});
	},
	loadSharedBookList : function(param){		
		jQuery("#a_leftMenu").loadWorkMaskOnly(false,false);
		ActionNotMaskLoader.loadAction(this.opt.treeLID, this.opt.sharedListAction, param,
				function(){jQuery("#a_leftMenu").removeWorkMask();}
		);
	},
	movePage:function(page){
		var param = this.listParam;
		if(param == null){
			param = {};
		}
		param.page = page;
		this.listParam = param;
		this.loadAddressList(param); 
	}
	
});

function initService(){
	var param = {};
	param.paneMode = paneMode;
	
//	addrControl.loadAddressList(param);
	addrControl.loadAddressView(param);
	
	jQuery("#privateTreeviewer").treeview();
	
	leftMenuControl.loadPrivateAddressGroup();
//	leftMenuControl.loadSharedAddressBook();
	leftMenuControl.showPrivateTreeViewer();
}


function okReaderDelPressed(){
	
	var addrbookSeq = jQuery('#bookSeq').val();
	
	var data = [];
	if(jQuery("#readerList :checkbox").size()==1){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	if(checkedCnt($("readerForm").readerSeqs)==0){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	jQuery("#readerList :checkbox").each(function(i){
		   if (jQuery(this).attr("checked")) {
			   if(jQuery(this).val() != 'on'){
				   data[data.length] = jQuery(this).val();
				   AddressBookService.deleteAddressBookReader(addrbookSeq, data,
							{callback:function(){
							   var param = {"bookSeq" : addrbookSeq};
								jQuery("#addReaderFrame").load(addrOption.readerListAction, param);
							}}
					);   
			   }
			   
		   }
		});
}

function historyCallBackFunc(url, mode){
	var isLoad = true;
	if(LayoutInfo.mode != mode){
		if(mode == "privateAddrList"){
			leftMenuControl.showPrivateTreeViewer();
			isLoad = false;
		} else if(mode == "sharedAddrList"){
			leftMenuControl.showSharedTreeViewer();
			isLoad = false;
		}
	}
	return isLoad;
}

function okModeratorDelPressed(){
	
	var addrbookSeq = jQuery('#bookSeq').val();
	if(jQuery("#moderatorList :checkbox").size()==1){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	if(checkedCnt($("moderatorForm").moderatorSeqs)==0){
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	var data = [];
	jQuery("#moderatorList :checkbox").each(function(i){
		   if (jQuery(this).attr("checked")) {
			   if(jQuery(this).val() != 'on'){
				   data[data.length] = jQuery(this).val();
				   AddressBookService.deleteAddressBookModerator(addrbookSeq, data,
							{callback:function(){
							   var param = {"bookSeq" : addrbookSeq};
							   jQuery("#addModeratorFrame").load(addrOption.moderatorListAction, param);
							}}
					);   
			   }
			   
		   }
		});
}

function okReaderAddPressed(){
	var addrbookSeq = jQuery('#bookSeq').val();
	var userEmail = jQuery('#readerEmail').val();
	
	if(userEmail==''){
		alert(addrMsg.addr_info_msg_051);
		return;
	}
		
	
	AddressBookService.saveAddressBookReader(addrbookSeq,userEmail, 
			{callback:function(){		
				var param = {"bookSeq" : addrbookSeq};
				jQuery("#addReaderFrame").load(addrOption.readerListAction, param);
			},errorHandler:function(errorString, exception) {
				alert(addrMsg.addr_error_msg_10);
			}}
	);
}

function okReaderSearchPressed(){
	var addrbookSeq = jQuery('#bookSeq').val();
	var userEmail = jQuery('#readerEmail').val();
	
	if(userEmail==''){
//		alert(addrMsg.addr_info_msg_051);
//		return;
	}
	var bookSeq = jQuery('#bookSeq').val();
	
	var param = {"bookSeq" : bookSeq, "searchType" : "uid", "keyWord" : userEmail};
	addrControl.loadReaderView(param);
}

function okModeratorSearchPressed(){
	var addrbookSeq = jQuery('#bookSeq');
	var userEmail = jQuery('#moderatorEmail');
	
	if(!checkInputLength("jQuery", userEmail, addrMsg.addr_info_msg_051, 2, 255)) {
		return;
	}
	if(!checkInputValidate("jQuery", userEmail, "onlyBack")) {
		return;
	}
	
	var userEmailVal = jQuery.trim(userEmail.val());
	
	var bookSeq = jQuery('#bookSeq').val();
	
	var param = {"bookSeq" : bookSeq, "searchType" : "uid", "keyWord" : userEmailVal};
	addrControl.loadModeratorView(param);
}

function okModeratorAddPressed(){
	var addrbookSeq = jQuery('#bookSeq').val();
	var userEmail = jQuery('#moderatorEmail').val();
	
	if(userEmail==''){
		alert(addrMsg.addr_info_msg_051);
		return;
	}
	
	AddressBookService.saveAddressBookModerator(addrbookSeq,userEmail, 
			{callback:function(){		
				var param = {"bookSeq" : addrbookSeq};
				jQuery("#addModeratorFrame").load(addrOption.moderatorListAction, param);
			},errorHandler:function(errorString, exception) {
				alert(addrMsg.addr_error_msg_10);
			}}
	);
}

function okPrivateGroupAddPressed(){
	var name = jQuery.trim(jQuery('#pGroupName').val());
	if(name==''){
		alert(addrMsg.addr_info_msg_009);
		jQuery('#pGroupName').focus();
		return;
	}
	
	if(incNotAllowChar(name)){
		alert(addrMsg.addr_info_msg_010+"\n\n \\,/,*,?,,<,>|,',\",`");
		jQuery('#pGroupName').focus();
		return;
	}
	
	if(!checkInputText($("pGroupName"), 0, 32, false)){
		jQuery('#pGroupName').focus();
		return false;
	}
	
	AddressBookService.savePrivateGroup(jQuery('#pGroupName').val(), 
			{callback:function(){
				jQuery("#privateGroupDialog").jQpopup("close");
				alert(addrMsg.addr_info_msg_001);
				leftMenuControl.loadPrivateAddressGroup();
			}}
	);
}

function okSharedGroupAddPressed(){
	var name = jQuery.trim(jQuery('#sGroupName').val());
	
	if(name==''){
		alert(addrMsg.addr_info_msg_009);
		jQuery('#sGroupName').focus();
		return;
	}
	
	if(incNotAllowChar(name)){
		alert(addrMsg.addr_info_msg_010+"\n\n \\,/,*,?,,<,>|,',\",`");
		jQuery('#sGroupName').focus();
		return;
	}
	
	if(!checkInputText($("sGroupName"), 0, 32, false)){
		jQuery('#sGroupName').focus();
		return false;
	}
	
	AddressBookService.saveSharedGroup(jQuery('#bookSeq').val(), jQuery('#sGroupName').val(), 
			{callback:function(){
				jQuery("#sharedGroupDialog").jQpopup("close");
				alert(addrMsg.addr_info_msg_001);
				leftMenuControl.loadSharedAddressGroup(jQuery('#bookSeq').val());
			}}
	);
	
}


function deletePrivateGroupPressed(){
	
	
	if(jQuery('#privateGroupFolder').val()==null){
		alert(addrMsg.addr_info_msg_020);
		return;
	}
	
	if(!confirm(addrMsg.addr_info_msg_021))
		return;
	
	AddressBookService.deletePrivateGroup(jQuery('#privateGroupFolder').val(), 
			{callback:function(){
				alert(addrMsg.addr_info_msg_018);
				jQuery('#managePrivateGroupDialog').jQpopup('close');
				
				leftMenuControl.loadPrivateAddressGroup();
			}}
	);
}

function deleteSharedGroupPressed(){
	
	if(jQuery('#sharedGroupFolder').val()==null){
		alert(addrMsg.addr_info_msg_020);
		return;
	}
	
	if(!confirm(addrMsg.addr_info_msg_021))
		return;
	
	AddressBookService.deleteSharedGroup(
			jQuery('#bookSeq').val(),
			jQuery('#sharedGroupFolder').val(), 
			{callback:function(){		
				alert(addrMsg.addr_info_msg_018);
				jQuery("#manageSharedGroupDialog").jQpopup("close");
				
				leftMenuControl.loadSharedAddressGroup(jQuery('#bookSeq').val());
			}}
	);
}

function okPrivateGroupManagePressed(){
	
	
	if(jQuery('#privateGroupFolder').val()==null){
		alert(addrMsg.addr_info_msg_019);
		
		return;
	}
	var name = jQuery.trim(jQuery('#newPrivateGroupName').val());
	if(name==''){
		alert(addrMsg.addr_info_msg_009);
		jQuery('#newPrivateGroupName').focus();
		return;
	}
	
	if(incNotAllowChar(name)){
		alert(addrMsg.addr_info_msg_010+"\n\n \\,/,*,?,,<,>|,',\",`");
		jQuery('#newPrivateGroupName').focus();
		return;
	}
	
	if(!checkInputText($("newPrivateGroupName"), 0, 32, false)){
		jQuery('#newPrivateGroupName').focus();
		return false;
	}
	
	AddressBookService.updatePrivateGroup(
			jQuery('#privateGroupFolder').val(), 
			jQuery('#newPrivateGroupName').val(), 
			{callback:function(){
				alert(addrMsg.addr_info_msg_017);
				jQuery('#managePrivateGroupDialog').jQpopup('close');				
				leftMenuControl.loadPrivateAddressGroup();
			}}
	);
}

function okSharedGroupManagePressed(){
	
	if(jQuery('#sharedGroupFolder').val()==null){
		alert(addrMsg.addr_info_msg_019);
		return;
	}
	var name = jQuery.trim(jQuery('#newSharedGroupName').val());
	if(name==''){
		alert(addrMsg.addr_info_msg_009);
		jQuery('#newSharedGroupName').focus();
		return;
	}
	
	if(incNotAllowChar(name)){
		alert(addrMsg.addr_info_msg_010+"\n\n \\,/,*,?,,<,>|,',\",`");
		jQuery('#newSharedGroupName').focus();
		return;
	}
	
	if(!checkInputText($("newSharedGroupName"), 0, 32, false)){
		jQuery('#newSharedGroupName').focus();
		return false;
	}
	
	AddressBookService.updateSharedGroup(
			jQuery('#bookSeq').val(),
			jQuery('#sharedGroupFolder').val(), 
			jQuery('#newSharedGroupName').val(), 
			{callback:function(){		
				alert(addrMsg.addr_info_msg_017);
				leftMenuControl.loadSharedAddressGroup(jQuery('#bookSeq').val());
				jQuery("#manageSharedGroupDialog").jQpopup("close");
			}}
	);
}

function viewPrivateGroupMember(groupName, groupSeq){
	initEmail();
	jQuery(".group a").css("font-weight", "normal");
	jQuery("#label_"+groupSeq).css("font-weight", "bold");
	
	jQuery('#bookSeq').val('');
	jQuery('#groupSeq').val(groupSeq);
	
	//var param = addrControl.listParam;
	var param = null;
	if(param == null){
		param = {};
	}
	addrControl.listParam = param;
	param.groupSeq = groupSeq;
	param.bookSeq = '';
	param.paneMode = paneMode;
	
	
	addrControl.loadAddressList(param);
	
	if(groupName != '')
		setWorkTitle(groupName);
	else 
		setWorkTitle(addrMsg.addr_tree_all_label);
	
	if(groupName != '')
		jQuery("#addrGroupLabel").text(groupName);
	else
		jQuery("#addrGroupLabel").text(addrMsg.addr_tree_all_label);
}

function viewSharedGroupMember(groupName, bookSeq, groupSeq){
	initEmail();
	
	jQuery(".folder a").css("font-weight", "normal");
	jQuery(".group a").css("font-weight", "normal");
	jQuery(".treeNodeWrapperContents  a").css("font-weight", "normal");
	jQuery("#label_"+bookSeq+"_"+groupSeq).css("font-weight", "bold");
	jQuery("#book_"+bookSeq).css("font-weight", "bold");
	
	jQuery('#bookSeq').val(bookSeq);
	jQuery('#groupSeq').val(groupSeq);
	
	
	//var param = addrControl.listParam;
	var param = null;
	if(param == null){
		param = {};
	}
	addrControl.listParam = param;
	param.groupSeq = groupSeq;
	param.bookSeq = bookSeq;
	addrControl.loadAddressList(param);
	
	setWorkTitle(groupName);
	
	if(groupName != '')
		jQuery("#addrGroupLabel").text(groupName);
	else
		jQuery("#addrGroupLabel").text("");
}	

function srchByLeading(leadingPattern) {
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery("#leadingGroupSeq").val();
	
	var param = addrControl.listParam;
	if(param == null){
		param = {};
	}
	param.groupSeq = groupSeq;
	param.leadingPattern = leadingPattern;
	param.bookSeq = bookSeq;
	
	if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
		param.bookSeq = 0;
	
	addrControl.loadAddressList(param);
}

function okSharedAddressBookAddPressed(){
	var bookObj = jQuery('#sharedAddrDialog .addrBookName');
	var bookName = trim(bookObj.val());
	if(!checkInputLength("jQuery", bookObj, addrMsg.addr_info_msg_048, 2, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", bookObj, "onlyBack")) {
		return;
	}
	
	AddressBookService.saveSharedAddressBook(bookName,
			{callback:function(){		
				alert(addrMsg.addr_info_msg_001);
				jQuery("#sharedAddrDialog").jQpopup("close");
				leftMenuControl.loadSharedAddressBook();
			}}
	);
}

function okSharedAddressBookRenamePressed(){
	var bookObj = jQuery('#sharedAddrRenameDialog .addrBookName');
	var bookName = trim(bookObj.val());
	
	if(!checkInputLength("jQuery", bookObj, addrMsg.addr_info_msg_048, 2, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", bookObj, "onlyBack")) {
		return;
	}
	
	AddressBookService.updateSharedAddressBook(
			jQuery('#bookSeq').val(), 
			bookName,
			
			{callback:function(){
				alert(addrMsg.addr_info_msg_017);
				jQuery("#sharedAddrRenameDialog").jQpopup("close");
				jQuery("#addrGroupLabel").text(bookName);
				leftMenuControl.loadSharedAddressBook();
			}}
	);
}

var selectedId;
function viewAddress(bookSeq, id){
	selectedId = id;
	
	var param = {"bookSeq" : bookSeq,"memberSeq":id, "paneMode":paneMode};
	addrControl.loadAddressView(param);
}

function viewAddress2(bookSeq, id){
	selectedId = id;
	
	var param = {"bookSeq" : bookSeq,"memberSeq":id, "paneMode":paneMode};
	
	AddressBookService.getJsonAddressMember(bookSeq, id,
			{callback:function(member){
				jQuery("#vlastName").text(member.lastName + " ");
				jQuery("#vmiddleName").text(member.middleName + " ");
				jQuery("#vfirstName").text(member.firstName + " ");
				jQuery("#vnickName").text(member.nickName + " ");
				jQuery("#vmemberName").text(member.memberName + " ");
				jQuery("#vmobileNo").text(member.mobileNo + " ");
				jQuery("#vbirthDay").text(member.birthDay + " ");
				jQuery("#vanniversaryDay").text(member.anniversaryDay + " ");
				jQuery("#vmemberEmail").text(member.memberEmail + " ");
				
				jQuery("#vhomePostalCode").text(member.homePostalCode + " ");
				jQuery("#vhomeCountry").text(member.homeCountry + " ");
				jQuery("#vhomeState").text(member.homeState + " ");
				jQuery("#vhomeCity").text(member.homeCity + " ");
				jQuery("#vhomeStreet").text(member.homeStreet + " ");
				jQuery("#vhomeExtAddress").text(member.homeExtAddress + " ");
				jQuery("#vhomeTel").text(member.homeTel + " ");
				jQuery("#vhomeFax").text(member.homeFax + " ");
				jQuery("#vprivateHomepage").text(member.privateHomepage + " ");
				
				jQuery("#vcompanyName").text(member.companyName + " ");
				jQuery("#vdepartmentName").text(member.departmentName + " ");
				jQuery("#vtitleName").text(member.titleName + " ");
				jQuery("#vofficePostalCode").text(member.officePostalCode + " ");
				jQuery("#vofficeCountry").text(member.officeCountry + " ");
				jQuery("#vofficeState").text(member.officeState + " ");
				jQuery("#vofficeCity").text(member.officeCity + " ");
				jQuery("#vofficeStreet").text(member.officeStreet + " ");
				jQuery("#vofficeExtAddress").text(member.officeExtAddress + " ");
				jQuery("#vofficeTel").text(member.officeTel + " ");
				jQuery("#vofficeFax").text(member.officeFax + " ");
				jQuery("#vofficeHomepage").text(member.officeHomepage + " ");
			}}
	);
}

function modifyAddress(id){
	var bookSeq = jQuery('#bookSeq').val();
	
	AddressBookService.getJsonAddressMember(bookSeq, id,
			{callback:function(member){
				var memberPopOpt = clone(popupOpt);
				
				var bookSeq = jQuery('#bookSeq').val();
				if(bookSeq==''){
					memberPopOpt.btnList = [{name:comMsg.comn_confirm,func:okPrivateMemberAddPressed}];
				}else{
					memberPopOpt.btnList = [{name:comMsg.comn_confirm,func:okSharedMemberAddPressed}];
				}
				
				memberPopOpt.minHeight = 450;
				memberPopOpt.minWidth = 420;
				
				memberPopOpt.openFunc = function(){
					jQuery(".TB_addressAdd").css("width","100%");
					toggleTab2('basic_header', 'fragment-', 1, 2, 3);
					toggleTab2('add_header', 'fragment-', 1, 2, 3);
					setMemberData(member);
				};
				memberPopOpt.closeFunc = function(){
					jQuery(".TB_addressAdd").css("width","");
				};
				
				
				jQuery("#addAddrDialog").jQpopup("open",memberPopOpt);
			}}
	);
	
}
function setMemberData(member){
	jQuery('#addAddress #memberSeq').val(member.id);
	jQuery('#addAddress #firstName').val(member.firstName);
	jQuery('#addAddress #lastName').val(member.lastName);
	jQuery('#addAddress #middleName').val(member.middleName);
	jQuery('#addAddress #memberName').val(member.memberName);
	jQuery('#addAddress #nickName').val(member.nickName);
	jQuery('#addAddress #memberEmail').val(member.memberEmail);
	jQuery('#addAddress #birthDay').val(member.birthDay);
	jQuery('#addAddress #anniversaryDay').val(member.anniversaryDay);
	jQuery('#addAddress #mobileNo').val(member.mobileNo);
	jQuery('#addAddress #homeCountry').val(member.homeCountry);
	
	var homePostalCode = member.homePostalCode;
	if (homePostalCode.indexOf('-') != -1) {
		var homePostalData = homePostalCode.split('-');
		jQuery("#addAddress #homePost1").val(homePostalData[0]);
		jQuery("#addAddress #homePost2").val(homePostalData[1]);
	}
	
	jQuery('#addAddress #homeState').val(member.homeState);
	jQuery('#addAddress #homeCity').val(member.homeCity);
	jQuery('#addAddress #homeStreet').val(member.homeStreet);
	jQuery('#addAddress #homeExtAddress').val(member.homeExtAddress);
	jQuery('#addAddress #homeTel').val(member.homeTel);
	jQuery('#addAddress #homeFax').val(member.homeFax);
	jQuery('#addAddress #privateHomepage').val(member.privateHomepage);
	jQuery('#addAddress #companyName').val(member.companyName);
	jQuery('#addAddress #departmentName').val(member.departmentName);
	jQuery('#addAddress #titleName').val(member.titleName);
	jQuery('#addAddress #officeCountry').val(member.officeCountry);
	jQuery('#addAddress #officeState').val(member.officeState);
	jQuery('#addAddress #officeCity').val(member.officeCity);
	jQuery('#addAddress #officeStreet').val(member.officeStreet);
	jQuery('#addAddress #officeExtAddress').val(member.officeExtAddress);
	jQuery('#addAddress #officeTel').val(member.officeTel);
	jQuery('#addAddress #officeFax').val(member.officeFax);
	jQuery('#addAddress #officeHomepage').val(member.officeHomepage);
	
	var officePostalCode = member.officePostalCode;
	if (officePostalCode.indexOf('-') != -1) {
		var officePostalData = officePostalCode.split('-');
		jQuery("#addAddress #officePost1").val(officePostalData[0]);
		jQuery("#addAddress #officePost2").val(officePostalData[1]);
	}
}
function initSubMode(){
	if(paneMode != "n"){
		addrControl.initSubMessage();
	}
}

function isValidAddressInfo(data){
	
	if(!checkInputLength("", $("lastName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("lastName").focus();
		return false;
	}
	if(!checkInputValidate("", $("lastName"), "userName")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("lastName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("firstName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("firstName").focus();
		return false;
	}
	if(!checkInputValidate("", $("firstName"), "userName")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("firstName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("middleName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("middleName").focus();
		return false;
	}
	if(!checkInputValidate("", $("middleName"), "userName")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("middleName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("memberName"), addrMsg.addr_info_msg_006, 2, 255)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("memberName").focus();
		return false;
	}
	if(!checkInputValidate("", $("memberName"), "userName")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("memberName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("nickName"), "", 0, 255)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("nickName").focus();
		return false;
	}
	if(!checkInputValidate("", $("nickName"), "userName")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("nickName").focus();
		return false;
	}
	
	if (trim($("memberEmail").value) == "") {
		alert(addrMsg.addr_info_msg_002);
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("memberEmail").focus();
		return false;
	}
	
	if(!isEmail($("memberEmail").value)){
		alert(addrMsg.addr_info_msg_003);
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("memberEmail").focus();
		return false;
	}
	
	if(!checkInputLength("", $("birthDay"), "", 0, 16)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("birthDay").focus();
		return false;
	}
	if(!checkInputValidate("", $("birthDay"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("birthDay").focus();
		return false;
	}
	
	if(!checkInputLength("", $("anniversaryDay"), "", 0, 16)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("anniversaryDay").focus();
		return false;
	}
	if(!checkInputValidate("", $("anniversaryDay"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("anniversaryDay").focus();
		return false;
	}
	
	if(!checkInputLength("", $("mobileNo"), "", 0, 32)) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("mobileNo").focus();
		return false;
	}
	if(!checkInputValidate("", $("mobileNo"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 1, 2, 3);
		$("mobileNo").focus();
		return false;
	}
	
	if(!checkInputLength("", $("homeCountry"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeCountry").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeCountry"), "case4")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeCountry").focus();
		return false;
	}

	if(!checkInputLength("", $("homeState"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeState").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeState"), "case4")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeState").focus();
		return false;
	}
	
	if(!checkInputLength("", $("homeCity"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeCity").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeCity"), "case4")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeCity").focus();
		return false;
	}
	
	if(!checkInputLength("", $("homeStreet"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeStreet").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeStreet"), "case4")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeStreet").focus();
		return false;
	}

	if(!checkInputLength("", $("homeExtAddress"), "", 0, 128)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeExtAddress").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeExtAddress"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeExtAddress").focus();
		return false;
	}
	
	if(!checkInputLength("", $("homeTel"), "", 0, 32)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeTel").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeTel"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeTel").focus();
		return false;
	}
	
	if(!checkInputLength("", $("homeFax"), "", 0, 32)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeFax").focus();
		return false;
	}
	if(!checkInputValidate("", $("homeFax"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("homeFax").focus();
		return false;
	}
	
	if(!checkInputLength("", $("privateHomepage"), "", 0, 255)) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("privateHomepage").focus();
		return false;
	}
	if(!checkInputValidate("", $("privateHomepage"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 2, 1, 3);
		$("privateHomepage").focus();
		return false;
	}
	
	if(!checkInputLength("", $("companyName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("companyName").focus();
		return false;
	}
	if(!checkInputValidate("", $("companyName"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("companyName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("departmentName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("departmentName").focus();
		return false;
	}
	if(!checkInputValidate("", $("departmentName"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("departmentName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("titleName"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("titleName").focus();
		return false;
	}
	if(!checkInputValidate("", $("titleName"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("titleName").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeCountry"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeCountry").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeCountry"), "case4")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeCountry").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeState"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeState").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeState"), "case4")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeState").focus();
		return false;
	}

	if(!checkInputLength("", $("officeCity"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeCity").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeCity"), "case4")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeCity").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeStreet"), "", 0, 64)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeStreet").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeStreet"), "case4")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeStreet").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeExtAddress"), "", 0, 128)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeExtAddress").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeExtAddress"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeExtAddress").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeTel"), "", 0, 32)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeTel").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeTel"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeTel").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeFax"), "", 0, 32)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeFax").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeFax"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeFax").focus();
		return false;
	}
	
	if(!checkInputLength("", $("officeHomepage"), "", 0, 255)) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeHomepage").focus();
		return false;
	}
	if(!checkInputValidate("", $("officeHomepage"), "onlyBack")) {
		toggleTab2('add_header', 'fragment-', 3, 1, 2);
		$("officeHomepage").focus();
		return false;
	}
	
	if(data.homePostalCode != '-'){
		if(!isZipCode(data.homePostalCode)){
			alert(addrMsg.addr_info_msg_038);
			toggleTab2('add_header', 'fragment-', 2, 1, 3);
			jQuery('#addAddress #homePost1').focus();
			return false;
		}
	}
	
	return true;
}

function okPrivateMemberAddPressed(){
	
	
	var groupSeq = jQuery('#groupSeq').val();
	var data = getMemberData();
	
	if(!isValidAddressInfo(data))
		return;
	
	if(data.memberSeq == ""){
		AddressBookService.checkPrivateDupEmail(data.memberEmail,data.memberName,
				{callback:function(msg){
					if(msg.isSuccess){
						if(parseInt(msg.dupCnt) > 0){
							var groupPopOpt = clone(popupOpt);
							jQuery("#dupEmailAddress").empty();
							jQuery("#dupEmail").empty();
							jQuery("#dupName").empty();
							jQuery("#dupQuestion").empty();
							jQuery("#dupEmailAddress").append(addrMsg.addr_add_type_12);
							jQuery("#dupEmail").append(data.memberEmail);
							jQuery("#dupName").append(data.memberName);
							if(parseInt(msg.dupCnt) == 1){
								jQuery("#dupQuestion").append(addrMsg.addr_add_type_10);								
								groupPopOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("overWrite",data,groupSeq);}},
								                       {name:addrMsg.addr_add_type_07,func:function(){addrWrite("addWrite",data,groupSeq);}},
								                       {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",data,groupSeq);}}
								                       ];
							}else{
								jQuery("#dupQuestion").append(msgArgsReplace(addrMsg.addr_add_type_11,[msg.dupCnt]));
								groupPopOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("addWrite",data,groupSeq);}},
								                       {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",data,groupSeq);}}
								                       ];
							}
							
							groupPopOpt.hideCloseBtn = true;
							groupPopOpt.minHeight = 100;
							groupPopOpt.minWidth = 470;
							groupPopOpt.openFunc = function(){};
							groupPopOpt.closeFunc = function(){};
							jQuery("#addAddrConfirm").jQpopup("open",groupPopOpt);	
							jQuery("#addAddrConfirm_px.btn_X").hide();
							
						}else{
							AddressBookService.savePrivateAddressMember(data, groupSeq, 
									{callback:function(msg){
										viewPrivateGroupMember('', groupSeq);
										jQuery("#addAddrDialog").jQpopup("close");
										
										if(data.memberSeq == null || data.memberSeq==""){
											alert(addrMsg.addr_info_msg_001);	
										}else{
											alert(addrMsg.addr_info_msg_017);
											viewAddress2(jQuery('#bookSeq').val(), data.memberSeq);
										}
										
									}}
							);						
						}
					}
				}}
		);
	}else{
		AddressBookService.savePrivateAddressMember(data, groupSeq, 
				{callback:function(msg){	
					viewPrivateGroupMember('', groupSeq);
					jQuery("#addAddrDialog").jQpopup("close");				
					
					if(data.memberSeq == null || data.memberSeq==""){
						alert(addrMsg.addr_info_msg_001);	
					}else{
						alert(addrMsg.addr_info_msg_017);
						viewAddress2(jQuery('#bookSeq').val(), data.memberSeq);
					}
					
				}}
		);
	}	
}
function addrWrite(type,data,groupSeq){
	if(type == "overWrite" || type == "addWrite"){
		AddressBookService.saveDupPrivateAddressMember(type,data,groupSeq,
			{callback:function(msg){
				viewPrivateGroupMember('', groupSeq);
				jQuery("#addAddrDialog").jQpopup("close");				
				jQuery("#addAddrConfirm").jQpopup("close");
				if(type == "overWrite"){
					alert(addrMsg.addr_add_type_13);
				}else{
					alert(addrMsg.addr_add_type_14);
				}
			}}
		);	
	}else if(type == "noWrite"){
		viewPrivateGroupMember('', groupSeq);
		jQuery("#addAddrDialog").jQpopup("close");
		jQuery("#addAddrConfirm").jQpopup("close");
	}
}

function getMemberData(){
	var bookSeq = jQuery('#bookSeq').val();
	var memberSeq = jQuery('#addAddress #memberSeq').val();
	var firstName = jQuery('#addAddress #firstName').val();
	var lastName = jQuery('#addAddress #lastName').val();
	var middleName = jQuery('#addAddress #middleName').val();
	var memberName = jQuery('#addAddress #memberName').val();
	var nickName = jQuery('#addAddress #nickName').val();
	var memberEmail = jQuery('#addAddress #memberEmail').val();
	var birthDay = jQuery('#addAddress #birthDay').val();
	var anniversaryDay = jQuery('#addAddress #anniversaryDay').val();
	var mobileNo = jQuery('#addAddress #mobileNo').val();
	
	var homeCountry = jQuery('#addAddress #homeCountry').val();
	var homePostalCode = jQuery('#addAddress #homePost1').val() + "-"+ jQuery('#addAddress #homePost2').val();
		homePostalCode = (jQuery.trim(homePostalCode) == "-") ? "" : homePostalCode;
	var homeState = jQuery('#addAddress #homeState').val();
	var homeCity = jQuery('#addAddress #homeCity').val();
	var homeStreet = jQuery('#addAddress #homeStreet').val();
	var homeExtAddress = jQuery('#addAddress #homeExtAddress').val();
	var homeTel = jQuery('#addAddress #homeTel').val();
	var homeFax = jQuery('#addAddress #homeFax').val();
	var privateHomepage = jQuery('#addAddress #privateHomepage').val();
	
	var companyName = jQuery('#addAddress #companyName').val();
	var departmentName = jQuery('#addAddress #departmentName').val();
	var titleName = jQuery('#addAddress #titleName').val();
	var officePostalCode = jQuery('#addAddress #officePost1').val() + "-"+ jQuery('#addAddress #officePost2').val();
		officePostalCode = (jQuery.trim(officePostalCode) == "-") ? "" : officePostalCode;
	var officeCountry = jQuery('#addAddress #officeCountry').val();
	var officeState = jQuery('#addAddress #officeState').val();
	var officeCity = jQuery('#addAddress #officeCity').val();
	var officeStreet = jQuery('#addAddress #officeStreet').val();
	var officeExtAddress = jQuery('#addAddress #officeExtAddress').val();
	var officeTel = jQuery('#addAddress #officeTel').val();
	var officeFax = jQuery('#addAddress #officeFax').val();
	var officeHomepage = jQuery('#addAddress #officeHomepage').val();
	
	var data = {
			memberSeq:memberSeq,
			addrbookSeq:bookSeq,
			firstName:escape_tag(jQuery.trim(firstName)),
			lastName:escape_tag(jQuery.trim(lastName)),
			middleName:escape_tag(jQuery.trim(middleName)),
			memberName:escape_tag(jQuery.trim(memberName)),
			nickName:escape_tag(nickName),
			memberEmail:escape_tag(jQuery.trim(memberEmail)),
			birthDay:escape_tag(birthDay),
			mobileNo:escape_tag(mobileNo),
			anniversaryDay:escape_tag(anniversaryDay),
			
			homeCountry:escape_tag(homeCountry),
			homePostalCode:escape_tag(homePostalCode),
			homeState:escape_tag(homeState),
			homeCity:escape_tag(homeCity),
			homeStreet:escape_tag(homeStreet),
			homeExtAddress:escape_tag(homeExtAddress),
			homeTel:escape_tag(homeTel),
			homeFax:escape_tag(homeFax),
			privateHomepage:escape_tag(privateHomepage),
			
			companyName:escape_tag(companyName),
			departmentName:escape_tag(departmentName),
			titleName:escape_tag(titleName),
			officeCountry:escape_tag(officeCountry),
			officePostalCode:escape_tag(officePostalCode),
			officeState:escape_tag(officeState),
			officeCity:escape_tag(officeCity),
			officeStreet:escape_tag(officeStreet),
			officeExtAddress:escape_tag(officeExtAddress),
			officeTel:escape_tag(officeTel),
			officeFax:escape_tag(officeFax),
			officeHomepage:escape_tag(officeHomepage)
			};
	
	return data;
}

function okSharedMemberAddPressed(){
	
	
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery('#groupSeq').val();
	var data = getMemberData();
	
	if(!isValidAddressInfo(data))
		return;
	
	AddressBookService.saveSharedAddressMember(data, groupSeq, 
			{callback:function(){	
				jQuery("#addAddrDialog").jQpopup("close");
				
				if(data.memberSeq == null || data.memberSeq==""){
					alert(addrMsg.addr_info_msg_001);	
				}else{
					alert(addrMsg.addr_info_msg_017);
					viewAddress2(jQuery('#bookSeq').val(), data.memberSeq);
				}
				
				viewSharedGroupMember('', bookSeq, groupSeq);
				
			}}
	);
	
}

function rememberMe2(id, name, email){
	jQuery("#memberSeq_all").attr("checked",false);
	
	if (jQuery("#memberSeq_"+id).attr("checked")){
		jQuery("#memberSeq_"+id).parent().parent().addClass("TM_checkLow");
		idMap.set("#memberSeq_"+id, id);
	}else{
		jQuery("#memberSeq_"+id).parent().parent().removeClass("TM_checkLow");
	}
}

function initEmail(){
	idMap = new Hash();
	$("emails").value = '';
}

function rememberMe(id, name, email) {
	jQuery("#memberSeq_all").attr("checked",false);
	
	var checked = jQuery("#memberSeq_"+id).attr("checked");
	if (checked){
		jQuery("#memberSeq_"+id).parent().parent().addClass("TM_checkLow");
		idMap.set("memberSeq_"+id, id);
	}else{
		jQuery("#memberSeq_"+id).parent().parent().removeClass("TM_checkLow");
		idMap.unset("memberSeq_"+id);
	}

	saveMail(name, email, checked);
}

function saveMail(name, email, checked){
	var str = "";
	   
    var fullEmail = "\"" + name + "\""+ "<"+ email+ ">";
    
	var emails = $("emails").value;
	arr = emails.split(",");
	
	for (var i = 0; i < arr.length; i++) {
		if ((arr[i] == fullEmail) && (!checked)){
            for (var i = 0; i < arr.length; i++) {
                if ((arr[i].length > 0) && (arr[i] != fullEmail)) {
                    str += arr[i] + ",";
                }
            }
            
            $("emails").value = str;

            return;
        }
    }
	if (checked)
		arr.push(fullEmail);

    for (var i = 0; i < arr.length; i++) {
    	if (arr[i].length > 0) {
    		str += arr[i] + ",";
        }
    }
    $("emails").value = str;
}

function checkAll(){
	if (jQuery("#memberSeq_all").attr("checked")){
		idMap = new Hash();
		
		jQuery(".address_list :checkbox").each(function(i){
			jQuery(this).attr("checked",true)
			if(jQuery(this).attr("id") != "memberSeq_all"){
				idMap.set(jQuery(this).attr("id"), jQuery(this).attr("value"));
				
				jQuery(this).parent().parent().addClass("TM_checkLow");
				
				var name = jQuery("#" + jQuery(this).attr("id") + "_name").val();
				var email = jQuery("#" + jQuery(this).attr("id") + "_email").val();
				
				saveMail(name, email, true);
			}
		});
	}else{
		jQuery(".address_list :checkbox").each(function(i){
			jQuery(this).attr("checked",false)
			if(jQuery(this).attr("id") != "memberSeq"){
				
				if(jQuery(this).attr("value") != 'on'){
					idMap.unset(jQuery(this).attr("id"));	
				}
				
				jQuery(this).parent().parent().removeClass("TM_checkLow");
				
				var name = jQuery("#" + jQuery(this).attr("id") + "_name").val();
				var email = jQuery("#" + jQuery(this).attr("id") + "_email").val();
				
				saveMail(name, email, false);
			}
		});
	}
}

function setWorkTitle(folderName){
	var workTitle = "<span class='TM_work_title'>"+folderName+"</span>&nbsp&nbsp";

	jQuery("#workTitle").html(workTitle);
	
	jQuery("#selectedGroupName").val(folderName);
}

function uploadfile() {	
	var f = $("uploadForm");
	
	if(f.theFile.value == "") {
		alert(msgArgsReplace(comMsg.error_nofile,["csv"]));
		return;
	}
	
	if(!isCsvFile(f.theFile.value)){
		alert(msgArgsReplace(comMsg.error_nofileext,["csv"]));
		return;
	}
	
	jQuery("#workHiddenFrame").makeUploadFrame("up_hidden_frame");    
    f.target= "up_hidden_frame";
	f.action = "/addr/uploadAddr.do";
	f.method = "post";	
	f.theBookSeq.value = jQuery('#bookSeq').val();
	f.theGroupSeq.value = jQuery('#toGroup').val();
	f.theVendor.value = jQuery('#vendor').val();
	f.theEncoding.value = jQuery('#encoding').val();
	f.addrAddType.value = jQuery('#dupAddrType').val();
	f.submit();
}

function uploadFailed(){
	alert(comMsg.error_fileupload);
	
	jQuery("#importAddressDialog").jQpopup("close");
}

function licenseFailed(){
	alert(addrMsg.addr_error_msg_01);
	
	jQuery("#importAddressDialog").jQpopup("close");
}

function uploadResult(){
	alert(addrMsg.addr_info_msg_028);
	var f = $("uploadForm");
	if(jQuery('#bookSeq').val() !=""){
		viewSharedGroupMember('', jQuery('#bookSeq').val(), jQuery('#groupSeq').val());
	}else{
		viewPrivateGroupMember('', f.theGroupSeq.value);
	}
	
	jQuery("#importAddressDialog").jQpopup("close");
}

function addlist(name, size, path, uid){
	
}

function searchAddress() {
	var searchType = jQuery('#searchType').val();
	var keyWord = jQuery('#skeyWord').val();
	
	if(jQuery.trim(keyWord) == ""){
		alert(mailMsg.alert_search_nostr);
		$('skeyWord').focus();
		return;
	}
	
	if(searchType=='fname'){
	
		if(keyWord != ''){
			if(!checkInputText($("skeyWord"), 2, 64, true)){
				return false;
			}	
		}
		
	}
	
	if(searchType=='email'){
		if(keyWord != ''){
			if(!checkInputValidate("prototype", $("skeyWord"), "case3")){
				return false;
			}	
		}
		
	}
	
	if(searchType=='mobile'){
		
		if(keyWord != ''){
			if(!checkInputText($("skeyWord"), 0, 18, true)){
				return false;
			}	
			if(!isPhone($("skeyWord").value)){
//				alert(addrMsg.addr_info_msg_039);
//				return false;
			}
				
		}
		
	}
	
	if(searchType=='company'){
		
		if(keyWord != ''){
			if(!checkInputText($("skeyWord"), 4, 64, true)){
				return false;
			}	
		}
		
	}
	
	if(searchType=='tel'){
		
		if(keyWord != ''){
			if(!checkInputText($("skeyWord"), 0, 18, true)){
				return false;
			}	
			if(!isPhone($("skeyWord").value)){
//				alert(addrMsg.addr_info_msg_039);
//				return false;
			}
				
		}
		
	}
	
	
//	contentSplitter.setSplitter("n");
	
	var param = addrControl.listParam;
	if(param == null){
		param = {};
	}
	param.page = 1;
	param.searchType = searchType;
	param.leadingPattern = '';
	param.keyWord = keyWord;
	param.bookSeq = jQuery('#bookSeq').val();
	
	if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
		param.bookSeq = 0;
	
	addrControl.loadAddressList(param);
}

function sortAddress(sortBy, sortDir){
	
	var param = addrControl.listParam;
	if(param == null){
		param = {};
	}
	param.page = 1;
	param.sortBy = sortBy;
	param.sortDir = sortDir;
	param.groupSeq = jQuery('#groupSeq').val();
	
	addrControl.loadAddressList(param);
}

function checkName() {
	var firstName = jQuery('#addAddress #firstName').val();
	var lastName = jQuery('#addAddress #lastName').val();
	var middleName = jQuery('#addAddress #middleName').val();
	
	lastName = lastName + " ";
	
	if (middleName != "") {
		middleName = middleName + " ";
	}
	
	if(jQuery('#installLocale').val()=='jp'){
	}
	if(jQuery('#installLocale').val()=='ko'){
		jQuery("#addAddress #memberName").val(lastName+middleName+firstName);
	}
}

function moveReaderPage(page){
	var param = addrControl.popParam;
	if(param == null){
		param = {};
		var bookSeq = jQuery('#bookSeq').val();
		param = {"bookSeq" : bookSeq};
	}
	param.page = page;
	addrControl.loadReaderView(param);
}

function moveModeratorPage(page){
	var param = addrControl.popParam;
	if(param == null){
		param = {};
		var bookSeq = jQuery('#bookSeq').val();
		param = {"bookSeq" : bookSeq};
	}
	param.page = page;
	addrControl.loadModeratorView(param);
}

function checkall2(self, others) {
	
	if (jQuery("#"+self).attr("checked"))
	{
		jQuery("."+others+" :checkbox").each(function(i){
			jQuery(this).attr("checked",true)
		});
	}else{
		jQuery("."+others+" :checkbox").each(function(i){
			jQuery(this).attr("checked",false)
		});
	}
}

function htmlReplace(str){
	str = replaceAll(str, "<", "&lt");
	str = replaceAll(str, ">", "&gt");
	str = replaceAll(str, "\"", "&#034");
	str = replaceAll(str, "\'", "&#039");
	return str;
}