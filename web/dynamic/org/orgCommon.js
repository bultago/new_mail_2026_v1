var LayoutInfo = {mode:"orgList"};
var mainSplitter, contentSplitter, mainTreeMap;
var leftMenuControl, orgControl, dndManager;
var menuBar,paneControl;
var idMap = new Hash();
var deptMap = new Hash();
var firstPageLoad = true;

jQuery().ready(function(){
	setTopMenu('organization');
//	PageMainLoadingManager.initLoadView();
//	var jobStack = [];
//	jobStack.push("tree");
//	jobStack.push("list");
//	
//	PageMainLoadingManager.startLoad(jobStack);
	HistoryManager.loadHistoryManager(historyCallBackFunc);
	
	var mainLayerPane = new LayerPane("o_mainBody","TM_m_mainBody");
	var menuLayerPane = new LayerPane("o_leftMenu","TM_m_leftMenu",220,100,350);
	var contentLayerPaneWapper = new LayerPane("o_contentBodyWapper","",300,100,500);
	
	var contentLayerPane = new LayerPane("o_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("o_contentMain","TM_m_contentMain",100,0,0);
	var previewLayerPane = new LayerPane("o_contentSub","TM_m_contentSub",100,0,0);

	mainSplitter = new SplitterManager(mainLayerPane,
										menuLayerPane,
										contentLayerPaneWapper,
										"sm","mainvsplitbar","hsplitbar");
	mainSplitter.setReferencePane(["o_contentBody","copyRight"]);
	mainSplitter.setSplitter("v",true);	
	jQuery(window).autoResize(jQuery("#o_mainBody"), "#copyRight");
	
	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();
	
	contentSplitter = new SplitterManager(contentLayerPane,
											listLayerPane,
											previewLayerPane,
											"sc","vsplitbar","hsplitbar");
	
	contentSplitter.setSplitter("h",false,true);	
	paneMode = contentSplitter.getMode();
	contentSplitter.setSplitter("h",true);	
	jQuery(window).autoResize(jQuery("#o_contentBody"),"#copyRight");	
	
	orgControl = new OrgControl(orgOption);
	
	initService();
	initDialog();
	loadBasicToolBar();
	contentSplitter.setSplitter("h",false,true);
	jQuery.removeProcessBodyMask();
	
	jQuery("#searchTypeSelect").selectbox({selectId:"searchType",
		selectFunc:""},
		"",
		[{index:addrMsg.addr_table_header_001,value:"fname"},
		 {index:addrMsg.addr_table_header_002,value:"email"},
		 {index:addrMsg.addr_table_header_003,value:"mobile"},
		 {index:addrMsg.addr_table_header_011,value:"title"},
		 {index:addrMsg.addr_table_header_012,value:"class"}]);
	
});

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;		
	}
	var topAreaSize = 4;
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#o_leftMenu", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontent", 
		mainId:"#o_leftMenu", 
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

var OrgControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.param = null;
		this.listParam = {};
	},
	loadOrgTree : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionNotMaskLoader.loadAction(this.opt.treeLID, this.opt.treeAction, param,"");
				//function(){PageMainLoadingManager.completeWork("tree");});		
	},loadMember : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionLoader.loadAction(this.opt.mainLID, this.opt.listAction, param,true);
		
		
	},
	loadMemberList : function(param){
		if(param){
			this.param = param;			
		}
		
		var isMessage = (firstPageLoad)?false:true;
		ActionLoader.loadAction(this.opt.mainLID, this.opt.listAction, param,isMessage);
		firstPageLoad = false;
				//function(){PageMainLoadingManager.completeWork("list");});
		
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.listAction,param,LayoutInfo.mode);
	},
	loadMailTo : function(param){
		if(param){
			this.param = param;			
		}
		
		jQuery("#"+this.opt.subLID).html("<textarea id='emails' style='width: 99%; font-size: 9pt;height:85px' cols='10' rows='5'/>");
	},
	movePage:function(page){
		var param = this.listParam;
		if(param == null){
			param = {};
		}
		param.page = page;
		this.listParam = param;
		this.loadMemberList(param); 
	}
});

function loadBasicToolBar(){

	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("SendMail",
		  	 			"item_addr_sendmail",
		  	 			addrMsg.org_btn_send, "B", sendMail,false)},
	  	 			{type: "B" ,
		  	 	        Item : new MenuItem("copyMember",
		  	 			"item_addr_copymember",
		  	 			addrMsg.addr_btn_copy_member, "B", copyMember,true)}
	  	 		];
	
	var basicGroupSkin3 = [
					{type: "B" ,
					       Item : new MenuItem("SendMail",
							"item_addr_sendmail",
							addrMsg.org_btn_send, "B", sendMail,false)},
						{type: "B" ,
					       Item : new MenuItem("copyMember",
							"item_addr_copymember",
							addrMsg.addr_btn_copy_member, "BM", copyMember,true)}
                       ];
	var menuList;
	if(skin != "skin3"){
		
		menuList = [
		               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1]}
		               ];
	}else{
		menuList = [
		               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroupSkin3]}
		               ];
	}
	
	
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");	
	
	if(skin=="skin3"){
		jQuery(".mail_body_menu").css("display","none");
		jQuery(".mail_body_tabmenu").css("border-bottom","1px solid #C8CDD1");
		jQuery("#pageNavi").css("padding-right","8px");
	}
}

function loadExtendToolBar(){

	var menuBarOpt = {
			mode : "list",
			tabID : "menuBarTab",
			contentID : "menuBarContent",
			navigationID : "pageNavi",
			navigationBottomID : "pageBottomNavi"
	};
	
	var basicGroup1 = [
	  	 	         {type: "B" ,
		  	 	        Item : new MenuItem("SendMail",
		  	 			"item_addr_sendmail",
		  	 			addrMsg.org_btn_send, "B", sendMail,false)},
	  	 			{type: "B" ,
		  	 	        Item : new MenuItem("SendMailToClass",
		  	 			"item_addr_sendallmail",
		  	 			addrMsg.org_btn_sendall, "B", extendSendMail,false)},
	  	 			
	  	 			{type: "B" ,
		  	 	        Item : new MenuItem("copyMember",
		  	 			"item_addr_copymember",
		  	 			addrMsg.addr_btn_copy_member, "B", copyMember,true)}
	  	 		];
	
	var basicGroupSkin3 = [
	  	  	 	         {type: "B" ,
	 		  	 	        Item : new MenuItem("SendMail",
	 		  	 			"item_addr_sendmail",
	 		  	 			addrMsg.org_btn_send, "B", sendMail,false)},
	 	  	 			{type: "B" ,
	 		  	 	        Item : new MenuItem("SendMailToClass",
	 		  	 			"item_addr_sendallmail",
	 		  	 			addrMsg.org_btn_sendall, "B", extendSendMail,false)},
	 	  	 			
	 	  	 			{type: "B" ,
	 		  	 	        Item : new MenuItem("copyMember",
	 		  	 			"item_addr_copymember",
	 		  	 			addrMsg.addr_btn_copy_member, "BM", copyMember,true)}
	 	  	 		];
	var menuList;
	if(skin != "skin3"){
		menuList = [
		               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroup1]}
		               ];
	}else{
		menuList = [
		               {name:addrMsg.addr_btn_tab_basic,id:"basic",initOn:true,linkFunc:changeToolbarMode,
		            	groupItem : [basicGroupSkin3]}
		               ];
	}
	
	menuBar = new  MailMenuBar(menuBarOpt,menuList);
	menuBar.makeToolbar("basic");	
}

function copyMember(target){
	var id = jQuery(target).attr("sid");
	
	if(idMap.size()==0)
	{
		alert(addrMsg.addr_info_msg_013);
		return;
	}
	
	AddressBookService.getJSonPrivateGroupList(
			{callback:function(pfolderList){	
				var dlist = [];
				dlist[0] =  {"id":"0", "name":addrMsg.addr_common_label_001};
				pfolderList.each(function(f){		
					dlist[dlist.length] =  {"id":f.id, "name":f.name};
				});	
				
				var flist = jQuery("<div></div>").addClass("flist_drow")
				.groupListDrow(dlist,"/design/default/image/icon/icon_plus.gif",copyMoveMsg, "copy");
				
				showSubMenuLayer(id,flist);
			}}
	);
	
	
}

var oldSubMenuID="";
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

function clearSubMenu(){
	var oldL = jQuery("#funcSubLayer");
	if($("funcSubLayer")){		
		oldL.hide();		
		oldL.remove();	
		oldSubMenuID = "";		
	}	
}

function toggleToolBarItem(id,isOver){
	
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


var orgMemberDupStateArray = [];
var orgMemberDecideArray = [];
function copyMoveMsg(target,type){	
	var toFolderSeq = jQuery(target).attr("fname");
	
	var ids = [];
	deptMap.each(function(val) {
		ids[ids.length] = val.value;
	});
	
	if(type == "copy"){
		AddressBookService.checkMemberAddrDupFromOrg(ids,toFolderSeq,
				{			
					callback:function(data){
						for(var i=0 ; i<data.size(); i++){
							orgMemberDupStateArray.push(
									{name:data[i].memberName, email:data[i].memberEmail, 
									 orgCode:data[i].orgCode, userSeq:data[i].userSeq, 
									 dupCnt:data[i].dupCnt,groupSeq:data[i].groupSeq}
							);							
						}
						decideAddrAddType();
						
					}
				});
		
	} 
	clearSubMenu();
}

var orgPopOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};

function decideAddrAddType(){
	var addrObj = orgMemberDupStateArray.pop();
	//var orgPopOpt = clone(popupOpt);
	
	if(!addrObj){
		if(parseInt(orgMemberDecideArray.length) == 0){
			return;
		}
		copyOrgMember(orgMemberDecideArray);
		return;
	}
	
	if(parseInt(addrObj.dupCnt) > 0){
		
		jQuery("#dupEmailAddress").empty();
		jQuery("#dupEmail").empty();
		jQuery("#dupName").empty();
		jQuery("#dupQuestion").empty();
		jQuery("#dupEmailAddress").append(addrMsg.addr_add_type_12);
		jQuery("#dupEmail").append(addrObj.email);
		jQuery("#dupName").append(addrObj.name);
		
		orgPopOpt.hideCloseBtn = true;
		orgPopOpt.minHeight = 100;
		orgPopOpt.minWidth = 470;
		orgPopOpt.openFunc = function(){};
		orgPopOpt.closeFunc = function(){};
				
		if(parseInt(addrObj.dupCnt) == 1){
			jQuery("#dupQuestion").append(addrMsg.addr_add_type_10);								
			orgPopOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("overWrite",addrObj);}},
								   {name:addrMsg.addr_add_type_07,func:function(){addrWrite("addWrite",addrObj);}},
								   {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",addrObj);}}
								   ];			
		}
		
		if(parseInt(addrObj.dupCnt) > 1){
			jQuery("#dupQuestion").append(msgArgsReplace(addrMsg.addr_add_type_11,[addrObj.dupCnt]));
			orgPopOpt.btnList = [{name:addrMsg.addr_add_type_06,func:function(){addrWrite("addWrite",addrObj);}},
			                       {name:addrMsg.addr_add_type_08,func:function(){addrWrite("noWrite",addrObj);}}
			                       ];
		}		
		
		if(parseInt(orgMemberDupStateArray.length) > 0){
			orgPopOpt.btnList.push({name:addrMsg.addr_add_type_09,func:function(){addrWrite("cancel",addrObj);}});
			orgPopOpt.minWidth = 550;
		}
		
		jQuery("#addAddrConfirm").jQpopup("open",orgPopOpt);
		jQuery("#addAddrConfirm_px.btn_X").hide();
		
	}else{
		orgMemberDecideArray.push({orgCode:addrObj.orgCode,userSeq:addrObj.userSeq,groupSeq:addrObj.groupSeq,addrAddType:"addWrite"});
		setTimeout('decideAddrAddType()', 100);
	}
}
function addrWrite(type,data){
	
	if(type == "overWrite"){
		closeOrgAddConfirm();
		orgMemberDecideArray.push({orgCode:data.orgCode,userSeq:data.userSeq,groupSeq:data.groupSeq,addrAddType:type});
		alert(addrMsg.addr_add_type_13);
		setTimeout('decideAddrAddType()', 100);
	}else if(type == "addWrite"){
		closeOrgAddConfirm();
		orgMemberDecideArray.push({orgCode:data.orgCode,userSeq:data.userSeq,groupSeq:data.groupSeq,addrAddType:type});
		alert(addrMsg.addr_add_type_14);
		setTimeout('decideAddrAddType()', 100);
	}else if(type == "noWrite"){
		closeOrgAddConfirm();
		setTimeout('decideAddrAddType()', 100);
	}else if(type == "cancel"){
		if(confirm(addrMsg.addr_add_type_17)){
			closeOrgAddConfirm();
			alert(addrMsg.addr_add_type_16);
			copyOrgMemberCancel(orgMemberDecideArray);
		}else{
		}
	}	
}


function copyOrgMemberCancel(arrayObj){
	AddressBookService.copyOrgMember(arrayObj,{
		callback:function(data){
			if(data.isSuccess){
				orgMemberDupStateArray = [];
				orgMemberDecideArray = [];
			}else{
				alert(addrMsg.addr_error_msg_04);
				orgMemberDupStateArray = [];
				orgMemberDecideArray = [];
			}
		}
	});
}

function copyOrgMember(arrayObj){
	
	AddressBookService.copyOrgMember(arrayObj,{
		callback:function(data){
			if(data.isSuccess){
				alert(addrMsg.addr_add_type_15);
				orgMemberDupStateArray = [];
				orgMemberDecideArray = [];
			}else{
				alert(addrMsg.addr_error_msg_01);
				orgMemberDupStateArray = [];
				orgMemberDecideArray = [];
			}
		}
	});	
}

function closeOrgAddConfirm(){
	jQuery("#dupEmailAddress").empty();
	jQuery("#dupEmail").empty();
	jQuery("#dupName").empty();
	jQuery("#dupQuestion").empty();		
	jQuery("#addAddrConfirm").jQpopup("close");	
}

function historyCallBackFunc(url, mode){
	var isLoad = true;
	if(LayoutInfo.mode != mode){		
		isLoad = false;
	}
	return isLoad;
}

function setWorkTitle(fullCode){
	var orgCodes = fullCode.split(':');
	var folderName = "";

	if (orgCodes.length <= 1) {
		folderName = fullCode;
	} else {
		for (i=1; i < orgCodes.length; i++) {
			if (folderName == "") {
				folderName += jQuery("#link_"+orgCodes[i]).text();
			} else {
				folderName += " > "+ jQuery("#link_"+orgCodes[i]).text();
			}
		}
	}

	jQuery(".TM_work_title").text(folderName);
}

function movePage(page){
	orgControl.movePage(page);
}

function changeToolbarMode(tabId){
	menuBar.makeToolbar(tabId);	
}

function sendMail(){
	var emails = $("emails").value;
	if(emails=='')
	{
		alert(addrMsg.addr_info_msg_030);
		return;
	}
	
	var f = $("mailForm");
	f.to.value = emails;	
	
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";	
	f.target = "popupWrite";
	f.submit();
	
	jQuery("#extendSendMailDialog").jQpopup("close");
}

function extendSendMail(){
	var orgPopOpt = clone(popupOpt);
	orgPopOpt.minHeight = 210;
	orgPopOpt.minWidth = 570;
	orgPopOpt.openFunc = function(){
		jQuery(".popup_body").css("width","100%");
		jQuery("#titleCodeSelect").empty();
		jQuery("#titleCodeSelect").selectbox({selectId:"titleCode",
			selectFunc:"",width:150},"",titleCodeArray);
		
		jQuery("#classCodeSelect").empty();
		jQuery("#classCodeSelect").selectbox({selectId:"classCode",
			selectFunc:"",width:150},"",classCodeArray);
		
	};
	
	
	orgPopOpt.closeFunc = function(){
		jQuery(".popup_body").css("width","");
	};
	
	jQuery("#extendSendMailDialog").jQpopup("open",orgPopOpt);
	var f = $("extendSendMailForm");
	f.reset();
	
	jQuery("#titleInclude").attr("checked",false);
	jQuery("#classInclude").attr("checked",false);
	jQuery("#deptInclude").attr("checked",false);
	
	jQuery("#currentOrg").text(jQuery(".TM_work_title").text());
	jQuery("#currentOrg").attr("title", jQuery(".TM_work_title").text());
}
	


function initService(){
	orgControl.loadOrgTree();
	orgControl.loadMailTo();
	orgControl.loadMemberList();
	
	setWorkTitle(comMsg.comn_top_org);
}

function initDialog(){
}

function closeDialog(){
	jQuery("#extendSendMailDialog").removeModal("bodyModalMask");
}

function viewOrgMember(orgCode, orgName, fullCode){
	jQuery("#orgTree li a").css("font-weight", "normal");
	jQuery("#link_"+orgCode).css("font-weight", "bold");
	loadExtendToolBar();
	//TIMSSEVEN-632
	//var param = orgControl.listParam;
	var param = {};
	param.orgCode = orgCode;
	param.orgFullCode = fullCode;
	orgControl.listParam = param;
	jQuery("#selectedOrgCode").val(orgCode);
	jQuery("#selectedOrgName").val(orgName);
	
	setWorkTitle(fullCode);
	
	orgControl.loadMemberList(param);
}

function selectEmail(id) {
	var checked = jQuery("#"+id).attr("checked");
	var orgCode = jQuery("#"+id+"_orgCode").attr("value");
	var seq = jQuery("#"+id+"_seq").attr("value");
	
	if(checked){
		if(idMap.get("#"+id)){
			return;
		}else{
			jQuery("#"+id).parent().parent().addClass("TM_checkLow");
			idMap.set("#"+id, id);	
			deptMap.set(id, orgCode + "," + seq);
		}
	}else{
		jQuery("#"+id).parent().parent().removeClass("TM_checkLow");
		idMap.unset("#"+id);
		deptMap.unset(id);
		
		jQuery("#memberSeq_all").attr("checked", false);
	}
	
	var str = "";
    var name = jQuery("#"+id+"_name").attr("value");
    var title = jQuery("#"+id+"_title").attr("value");
    var dept = jQuery("#"+id+"_dept").attr("value");
    var email = jQuery("#"+id+"_email").attr("value");
    
    name = name + "/" + title + "/" + dept;
    var fullEmail = "\"" + name + "\""+ "<"+ email+ ">";
  
	var emails = $("emails").value;
	arr = emails.split(",");
	
	for (var i = 0; i < arr.length; i++) {
        if ((arr[i] == fullEmail) && (!checked)) {
            for (var i = 0; i < arr.length; i++) {
                if ((arr[i].length > 0) && (arr[i] != fullEmail)) {
                    str += arr[i] + ",";
                }
            }
            
            $("emails").value = str;

            return;
        }
    }
	
	if (checked) {
    	arr.push(fullEmail);
    }

    for (var i = 0; i < arr.length; i++) {
    	if (arr[i].length > 0) {
    		str += arr[i] + ",";
        }
    }
    $("emails").value = str;
}

function checkAll(){
	if (jQuery("#memberSeq_all").attr("checked")){
		jQuery(".member_list :checkbox").each(function(i){
			if(jQuery(this).attr("id") != "memberSeq_all"){
				jQuery(this).attr("checked", true)
				selectEmail(jQuery(this).attr("id"));
			}
		});
	}else{
		jQuery(".member_list :checkbox").each(function(i){
			jQuery(this).attr("checked", false)
			selectEmail(jQuery(this).attr("id"));
		});
		idMap = new Hash();
		deptMap = new Hash();
	}
}

function searchOrg(){
	var searchType = jQuery('#searchType').val();
	var keywordObj = jQuery('#keyWord');
	
	if(!checkInputLength("jQuery", keywordObj, mailMsg.alert_search_nostr, 2, 64)) {
		return;
	}
	
	if (!checkInputText($("keyWord"), 2, 255, true)){
		return false;
	}
	
	if(!checkInputValidate("jQuery", keywordObj, "case5")) {
		return;
	}		
	
	
	var param = orgControl.listParam;
	if(param == null){
		param = {};
	}
	param.page = 1;
	param.searchType = searchType;
	param.keyWord = keywordObj.val();
	
	orgControl.loadMemberList(param);
}

function viewAddress(id, orgCode){
	OrgService.getJsonMember(id,orgCode,
			{callback:function(member){
				
				jQuery('#memberName').html(member.name + "&nbsp;");
				jQuery('#memberEmail').html(member.email + "&nbsp;");
				jQuery('#empno').html(member.empno + "&nbsp;");
				jQuery('#mobileNo').html(member.mobile + "&nbsp;");
				jQuery('#departmentName').html(member.deptName + "&nbsp;");
				jQuery('#titleName').html(member.titleName + "&nbsp;");
				jQuery('#className').html(member.className + "&nbsp;");
				jQuery('#officeTel').html(member.officeTel + "&nbsp;");
				
				var orgPopOpt = clone(popupOpt);
				orgPopOpt.minHeight = 300;
				orgPopOpt.minWidth = 450;
				orgPopOpt.openFunc = function(){
					jQuery(".TB_addressAdd").css("width","100%");
				};
				orgPopOpt.closeFunc = function(){
					jQuery(".TB_addressAdd").css("width","");
				};
				jQuery("#orgViewDialog").jQpopup("open",orgPopOpt);
				
			}}
	);
	
	readPicture("myPicture", id);
}

function makeOrgAddress(selectBox, orgCode, codeClass) {
	var index = selectBox.selectedIndex;
	var text = selectBox.options[index].text;
	var value = selectBox.options[index].value;
	if (text == "") {
		text = "#"+orgCode+"."+codeClass+"."+value;
	}
	else {
		text = "\""+text+"\"<#"+orgCode+"."+codeClass+"."+value+">";
	}
	
	return text;
}

//#:00000.class.001.false
function sendMailToClass(){
	var classInclude = $('classInclude').checked;
	var classCode = $('classCode').value;
	var orgCode = $('selectedOrgCode').value;
	
	var name = jQuery('#classCodeSelect_selectText').text();
	
	if(classCode =='')
		return;
	if(classCode =='undefined')
		return;
	
	if(orgCode != ''){
		if(classCode == ''){
			alert(addrMsg.addr_info_msg_047);
			return;
		}
		
		var str = "#"+ orgCode + "." + "class" + "." + classCode + "." + classInclude;
		var fullEmail = "\"" + name + "\" "+ "<"+ str+ ">";
		
		var f = $("mailForm");		
		f.to.value = fullEmail;		
		var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
		f.method = "post";
		f.action="/dynamic/mail/writeMessage.do";	
		f.target = "popupWrite";
		f.submit();
		jQuery("#extendSendMailDialog").jQpopup("close");
		
	}
}

//#:00000.title.001.false
function sendMailToTitle(){
	//����
	var titleInclude = $('titleInclude').checked;
	var titleCode = $('titleCode').value;
	var orgCode = $('selectedOrgCode').value;
	
	var name = jQuery('#titleCodeSelect_selectText').text();
	
	if(titleCode =='')
		return;
	if(titleCode =='undefined')
		return;
	
	if(orgCode != ''){
		if(titleCode == ''){
			alert(addrMsg.addr_info_msg_046);
			return;
		}
		var str = "#"+ orgCode + "." + "title" + "." + titleCode + "." + titleInclude;
		
		var fullEmail = "\"" + name + "\" "+ "<"+ str+ ">";
		
		var f = $("mailForm");
		f.to.value = fullEmail;	
		var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
		f.method = "post";
		f.action="/dynamic/mail/writeMessage.do";	
		f.target = "popupWrite";
		f.submit();
		jQuery("#extendSendMailDialog").jQpopup("close");
	}
}

//#:00000.all.all.false
function sendMailToDept(){
	//����
	var deptInclude = $('deptInclude').checked;
	var orgCode = $('selectedOrgCode').value;
	var orgName = $("selectedOrgName").value;
	
	if(orgCode != ''){
		var str = "#"+ orgCode + "." + "all" + "." + "all" + "." + deptInclude;		
		str = "\"" + orgName + "\" "+ "<"+ str+ ">";
		
		var f = $("mailForm");
		f.to.value = str;
		var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
		f.method = "post";
		f.action="/dynamic/mail/writeMessage.do";	
		f.target = "popupWrite";
		f.submit();
		jQuery("#extendSendMailDialog").jQpopup("close");
	}
}

function sendMailToDept2(){
	//����
	var deptInclude = $('deptInclude').checked;
	var orgCode = $('selectedOrgCode').value;
	var orgName = $("selectedOrgName").value;
	
	
	OrgService.getJsonDeptAddress(orgCode,
			{callback:function(result){
				var str = "#"+ result.address;		
				str = "\"" + orgName + "\" "+ "<"+ str+ ">";
				
				var f = $("mailForm");
				f.to.value = str;
				var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
				f.method = "post";
				f.action="/dynamic/mail/writeMessage.do";	
				f.target = "popupWrite";
				f.submit();
				jQuery("#extendSendMailDialog").jQpopup("close");
				
			}}
	);
}

function sendSingleMail(name, email){
	var fullEmail = "\"" + name + "\""+ "<"+ email+ ">";
	
	var f = $("mailForm");	
	f.to.value = fullEmail;	
	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");	
	f.method = "post";
	f.action="/dynamic/mail/writeMessage.do";	
	f.target = "popupWrite";
	f.submit();	
	
}

function sortAddress(sortBy, sortDir){
	
	var param = orgControl.listParam;
	if(param == null){
		param = {};
	}
	param.page = 1;
	param.sortBy = sortBy;
	param.sortDir = sortDir;
	
	orgControl.loadMemberList(param);
}

function readPicture(id, userSeq) {
	var url = "/setting/viewPicture.do";
	var param = {};
	param.userSeq = userSeq;

	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isExist) {
				jQuery("#"+id).empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", data.pictureSrc);
				jQuery("#"+id).append(img);
				return;
			}
			else {
				if (data.msg = 'empty') {
					jQuery("#"+id).empty();
				}
				else if (data.msg = 'error') {
					alert(comMsg.error_default);
				}
				return;
			}
		}, "json");
}

function viewMiniPicure(id){
	jQuery("#orgViewDialog").jQpopup("open",orgPopOpt);
}

var flagHelp = false;

function popUp(e,userSeq) {
	
	if (flagHelp) {
		return;
	}

	jQuery("#helpLayer").css("width","120px");

	var content = "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='border:1px solid #3D83C1;'>"+
					"<tr>"+
						"<td style='backgroud-color:#F6F4F4'>" +
						"	<div id='overMyPicture' style='border:1px solid #CDCDCD;background:#ffffff;width:120px;height:145px'></div>"+
						"</td>"+
					"</tr>"+
				   "</table>";

	jQuery("#helpLayer").html(content);		  	
	flagHelp = true;
	
	var url = "/setting/viewPicture.do";
	var param = {};
	param.userSeq = userSeq;
	var defultImg = "/design/common/image/noPhoto2.jpg";
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isExist) {
				jQuery("#overMyPicture").empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", data.pictureSrc);
				jQuery("#overMyPicture").append(img);
				return;
			}else{
				jQuery("#overMyPicture").empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", defultImg);
				jQuery("#overMyPicture").append(img);
				return;				
			}
		});
	var extScrollTop = 170;
	var extScrollLeft = 230;
	if(isMsie9) {
		extScrollTop=120;
		extScrollLeft = 260;
	}
	
	x = (document.all) ? window.event.x + document.body.scrollLeft + extScrollLeft : e.pageX;
	y = (document.all) ? window.event.y + document.body.scrollTop + extScrollTop : e.pageY;
	
	jQuery("#helpLayer").css("left", x+30+"px");
	jQuery("#helpLayer").css("top", y+"px");
	jQuery("#helpLayer").show();
}

function memeberPicPopUp(objId, userSeq) {
	
	if (flagHelp) {
		return;
	}

	jQuery("#helpLayer").css("width","120px");

	var content = "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='border:1px solid #3D83C1;'>"+
					"<tr>"+
						"<td style='backgroud-color:#F6F4F4'>" +
						"	<div id='overMyPicture' style='border:1px solid #CDCDCD;background:#ffffff;width:120px;height:145px'></div>"+
						"</td>"+
					"</tr>"+
				   "</table>";

	jQuery("#helpLayer").html(content);		  	
	flagHelp = true;
	
	var url = "/setting/viewPicture.do";
	var param = {};
	param.userSeq = userSeq;
	var defultImg = "/design/common/image/noPhoto2.jpg";
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isExist) {
				jQuery("#overMyPicture").empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", data.pictureSrc);
				jQuery("#overMyPicture").append(img);
				return;
			}else{
				jQuery("#overMyPicture").empty();
				var img = jQuery("<img>");
				img.css("width", "120px");
				img.css("height", "145px"); 
				img.attr("src", defultImg);
				jQuery("#overMyPicture").append(img);
				return;				
			}
		});
	
	var obj = jQuery("#"+objId);
	var left = obj.offset().left;
	var top = obj.offset().top;
	var height = obj.height();
	top += height + 1;
	
	jQuery("#helpLayer").css("position","absolute");
	jQuery("#helpLayer").css({"top":top,"left":left});
	jQuery("#helpLayer").css("z-index","3");	
	jQuery("#helpLayer").show();
}

function popClose() {
	jQuery("#helpLayer").hide();
	jQuery("#helpLayer").empty();
	flagHelp = false;
}