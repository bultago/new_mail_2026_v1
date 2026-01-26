<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/webfolder_style.css" />
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/webfolder/webfolderMenuBar.js"></script>
<script type="text/javascript" src="/i18n?bundle=webfolder&var=webfolderMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script language="javascript">
// file upload change

var menuBar;

jQuery().ready(function(){
	
	setTopMenu('webfolder');

	var mainLayerPane = new LayerPane("w_mainBody","TM_m_mainBody");	
	var menuLayerPane = new LayerPane("w_leftMenuContent","TM_m_leftMenu",220,220,220);
	var contentLayerPaneWapper = new LayerPane("w_contentBodyWapper","",300,100,500);
	
	
	mainSplitter = new SplitterManager(mainLayerPane,
										menuLayerPane,
										contentLayerPaneWapper,
										"sm","mainvsplitbar","hsplitbar");	
	mainSplitter.setReferencePane(["w_contentBody","copyRight"]);	
	mainSplitter.setSplitter("v",true);
	jQuery(window).autoResize(jQuery("#w_mainBody"),"#copyRight");

	jQuery(window).autoResize(jQuery("#leftMenuRcontentWrapper"),"#copyRight",true);

	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();

	var contentLayerPane = new LayerPane("w_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("w_contentMain","TM_m_contentMain",300,0,0);
	var previewLayerPane = new LayerPane("w_contentSub","TM_m_contentSub",400,0,0);		
	
	contentSplitter = new SplitterManager(contentLayerPane,
			listLayerPane,
			previewLayerPane,
			"sc","vsplitbar","hsplitbar");
	
	contentSplitter.setSplitter("n",false);

	jQuery(window).autoResize(jQuery("#w_contentBody"),"#copyRight");	
	jQuery(window).trigger("resize");
	contentSplitter.setSplitter("n",false);
	
	jQuery("#w_contentMain").css("overflow-X","hidden");
	jQuery.removeProcessBodyMask();
});

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;
	}
	var topAreaSize = jQuery("#wl_top").outerHeight(true)+4;
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#w_leftMenuContent", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});	
	
	inResizefunc(jQuery(window),jQuery("#leftMenuRcontentWrapper"),true);
				
	jQuery(window).bind("resize", function(){
		var iheight = jQuery("#leftMenuRcontentWrapper").height();
		jQuery("#deeptree_user").height((isMsie)?iheight-2:iheight);
		});
	
	jQuery(window).trigger("resize");
}

var folderNode = "";

function getFolderNode(){
	return folderNode;
}

function setFolderNode(node){
	folderNode = node;
}

function resizeFrame(height){
	if (height > 400) $("fraContent").style.height=height+"px";	
}
 
var oldNode = "";
function checkOldNode(node){
	if(oldNode != ""){	
		window.frames.deeptree_user.setOldNode(oldNode);
	}
	oldNode = node;
}

function checkCurrentNode(node){
	window.frames.deeptree_user.setCurrentNode(node);
}

function syncTreeNode(folderType,cnum,jsonStr){
		
	if(oldNode == ""){
		oldNode = "user_link_"+cnum;
		if (window.frames.deeptree_user) {
		window.frames.deeptree_user.setCurrentNode(oldNode);
		}
	} else {
		if(folderType == "user"){
			checkOldNode("user_link_"+cnum);			
			window.frames.deeptree_user.setCurrentNode("user_link_"+cnum);
		} else if(folderType == "share"){
			checkOldNode("share_link_"+cnum);
			window.frames.deeptree_user.setCurrentNode("share_link_"+cnum);
		} else if(folderType == "public"){
			checkOldNode("public_link_"+cnum);
			window.frames.deeptree_user.setCurrentNode("public_link_"+cnum);
		}
	}
	window.frames.deeptree_user.syncNode(folderType,cnum,jsonStr);	
}

function syncTreeChildNode(folderType,nodeNum){
	window.frames.deeptree_user.syncAddBranch(folderType,nodeNum);	
}

var sFolderRoots;
function setRootArray(array){
	sFolderRoots = array;	
}

function getROOT(id){
	var idx;
	if(id.indexOf("_") > -1){
		var vals = id.split("\_");
		idx = vals[2];	
	} else {
		idx = id;
	}
	idx = idx.substr(0,idx.indexOf("|"));
	var sROOTId = sFolderRoots[Number(idx)];
	
	return sROOTId;
}

var isLoaded = false;
function loadList(){
	window.fraContent.document.location = "/webfolder/listFolders.do";
	isLoaded = true;
}

var isListLoaded = false;
function setListLoaded(val){
	isListLoaded = val;
}

function getListLoaded(){
	return isListLoaded;
}

function getIsPop(){
	return false;
}

function progress(str){
	if(str){
		viewProgress(str);
	} else {
		viewProgress();
	}
	
}

function removeProgress(){
	hiddenProgress();
}

function setQuotaInfo(used, total, usedPercent) {
	jQuery("#ml_quotaUseAge").text(used);
	jQuery("#ml_quotaTotal").text(total);
	if (usedPercent > 100) {
		usedPercent = 100;
	}
	jQuery("#ml_quotaGraphBar").css("width",usedPercent+"%");	
}

function setPageNavi(total, pageBase, currentPage) {
	//menuBar.setPageNavi("p",{total:total,base:pageBase,page:currentPage}); 
	menuBar.setPageNaviBottom("p",{total:total,base:pageBase,page:currentPage});	
	parentSetCurrentPage(currentPage);
}

function movePage(page) {
	window.fraContent.movePage(page);
}

function closeShareDialog() {
	
}

function moveto_page(page) {
	var searchType = jQuery("#presearchtype").val();
	var keyWordObj = jQuery("#prekeyword");
	var keyWord = keyWordObj.val();

	var param = {"searchType":searchType, "keyWord":keyWord, "currentPage":page};
	jQuery("#search_share_div").load("/webfolder/searchShareFolder.do", param);
}

function markall(me, target) {
	if (target != null) {
        if (target.length > 0) {
            for (var i = 0; i < target.length; i++) {
            	target[i].checked = me.checked;
                linecheck(me,target[i].value);
            }
        }
        else {
        	target.checked = me.checked;
            linecheck(me,target.value);
        }
    }
}

function shareFolderDialog(path, nodeNum, type, fuid) {


	var param = {"path":path, "nodeNum":encodeURI(nodeNum), "type":type, "fuid":fuid};
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 450,
			minWidth:635,
			btnList : [{name:webfolderMsg.button_ok,func:makeShare}],	
			openFunc:function(){
				jQuery("#share_folder_div").load("/webfolder/shareFolder.do", param);
				if (getListLoaded()) {
					
				}
			},
			closeFunc:function(){
				jQuery("#share_folder_div").empty();
				if (getListLoaded()) {
					
				}
			}
		};
	
	jQuery("#share_folder_pop").jQpopup("open",popupOpt);

}

function changeShareType(me) {
	if (me.checked) {
		jQuery("#alluserOption").show();
		jQuery("#selectShare").hide();
	}
	else {
		jQuery("#alluserOption").hide();
		jQuery("#selectShare").show();
	}
}

function searchUser() {
	var f = document.shareFolderForm;

	var searchType = f.searchType.value;
	var keyWord = f.keyWord.value;

	if (searchType == "uid") {

		if(!checkInputLength("", f.keyWord, webfolderMsg.alert_search_empty_keyword, 2, 64)) {
			return;
		}
		if(!checkInputValidate("", f.keyWord, "id")) {
			return;
		}
	} else if (searchType == "name") {
		if(!checkInputLength("", f.keyWord, webfolderMsg.alert_search_empty_keyword, 2, 255)) {
			return;
		}
		if(!checkInputValidate("", f.keyWord, "userName")) {
			return;
		}
	}

	var resultBox = f.resultBox;
	var cnt = resultBox.length
	for(var i=0 ; i<cnt ; i++){
		resultBox.options[0] = null;
	}
	
    if(f.keyWord.value == ""){
        alert(webfolderMsg.alert_search_empty_keyword);
        f.keyWord.focus();
        return;
    }

    jQuery("#share_notorg_btn").hide();
	jQuery("#searching_user").show();

	jQuery.post("/webfolder/searchUser.do", {"searchType":searchType, "keyWord":keyWord}, function(json){
		if (json.result == "success") {
			var member = json.member;
			for (i=0; i<member.length; i++) {
				addResultBox(webfolderMsg.folder_share_type_user+":"+member[i].name+"("+member[i].uid+")", "U|"+member[i].userSeq+"/"+ member[i].name);
			}
		} 
		else {
	  		alert(json.result);
		}

		jQuery("#share_notorg_btn").show();
		jQuery("#searching_user").hide();
	},"json");		
}

function addResultBox(text,value){

	var f = document.shareFolderForm;
	var resultBox = f.resultBox;
	var readBox = f.readAuthBox;
	var rwBox = f.rwAuthBox;
	var resultCnt = resultBox.options.length;
	var readBoxCnt = readBox.options.length;
	var rwBoxCnt = rwBox.options.length;
	var chk_same = false;	
	var chk = false;
	var newOption;
	
	if(resultCnt == 0){
		if(readBoxCnt != 0){
			for(var i=0 ; i<readBoxCnt ; i++){
				if(text == readBox[i].text){
					chk = true;
					break;
				}
			}
		}
		if(rwBoxCnt !=0){
			for(var j=0 ; j<rwBoxCnt ; j++){
				if(text == rwBox[j].text){
					chk = true;
					break;
				}
			}
		}
		if(!chk){
			newOption = new Option(text,value);
			newOption.title = text;
			resultBox.options[0] = newOption;	
		}
	}else{
		var len = resultBox.options.length;
		
		for(var i=0 ; i<len ; i++){			
			if(resultBox.options[i].value == value){
				chk_same = true;
				break;	
			} 
		}
		if(readBoxCnt != 0){
			for(var m=0 ; m<readBoxCnt ; m++){
				
				if(text == readBox[m].text){
					chk_same = true;
					break;
				}
			}
		}
		if(rwBoxCnt !=0){
			
			for(var n=0 ; n<rwBoxCnt ; n++){
				
				if(text == rwBox[n].text){
					chk_same = true;
					break;
				}
			}
		}
		if(!chk_same){
			newOption = new Option(text,value);
			newOption.title = text;
			resultBox.options[len] = newOption;
		}
	}
}

function moveItem(start,end, compare){
	var f = document.shareFolderForm;
  	var endLength = end.length;
  	var chk_same = false;
  	var newOption;
  	
  	for(i=0 ; i<start.length; i++){
		if(start.options[i].selected && start.options[i].value){
			start.options[i].selected=false;

			for(k=0; k < end.length; k++) {
				if (end.options[k].value == start.options[i].value) {
					chk_same = true;
					break;
				}
			}

			if (compare) {
				for(k=0; k < compare.length; k++) {
					if (compare.options[k].value == start.options[i].value) {
						chk_same = true;
						break;
					}
				}
			}
			
			if(!chk_same){
				newOption = new Option(start[i].text, start.options[i].value);
				newOption.title = start[i].text;
				end.options[endLength] = newOption;
				endLength++;
			}
			chk_same = false;
		}
 	}
 	
 	for(var l=0 ; l<end.length ; l++){
 		var endvalue = end.options[l].value;
 		for(var m=0; m<start.length; m++) {
 			if(endvalue==start.options[m].value) {
 			start.options[m] = null;
 			break;
 			}
 		}
 	}
 	 	 		 	
}

function deleteItem(list){
	var listCount = list.options.length;
	var flag = false;
	if (listCount == 0) {
		alert(comMsg.common_form_007);
		return;
	}
	else {
		for (var i=listCount-1; i >= 0; i--) {
			if (list.options[i].selected == true) {
				list.options[i] = null;
				flag = true;
			}
		}
		if (!flag) {
			alert(comMsg.common_form_007);
		}
	}
}

function makeShare(){
	
	var form = document.shareFolderForm;
	var readAuth = form.readAuthBox;
	var rwAuth = form.rwAuthBox;
	var commentObj = form.comment;
	
	if(!checkInputLength("", commentObj, "", 0, 255)) {
		return;
	}
	
	if(!form.alluserCheck.checked && readAuth.length < 1 && rwAuth.length < 1  ){
		alert(webfolderMsg.alert_noselect3);	
		return;
	}
	for(var i=0 ; i<readAuth.length ; i++){
		readAuth.options[i].selected = true;
	}
		
	for(var j=0 ; j<rwAuth.length ; j++){
		rwAuth.options[j].selected = true;
	}

	if (form.alluserCheck.checked) {
		form.shareMode.value = "all";
	} 
	else {
		form.shareMode.value = "each";
	}

	if (form.mode.value == "new") {
		form.action="/webfolder/makeShareFolder.do";
	}
	else if (form.mode.value == "modify") {
		form.action="/webfolder/modifyShareFolder.do";
	}
	
	form.target = "fraContent";
	form.method="post";
    form.submit();
    setListLoaded(false);
    jQuery("#share_folder_pop").jQpopup("close");

}

function resetSearch() {
	jQuery("#keyWord").val("");
	window.fraContent.resetSearch();
}

function setSearch(value) {
	jQuery("#keyWord").val(value);
}

function checkMenubar(isFolderShare, isRoot, type, folderAuth) {
	if(skin != "skin3"){
		jQuery(".menu_group").show();
		jQuery(".micom").show();
		jQuery("#folerUploadL").show();
		jQuery("#extra_0").show();
		jQuery("#extra_1").show();
		jQuery(".mail_body_tab_wrapper a:eq(1)").show();
		
		if (type == "public" || type == "share") {
			jQuery("#extra_0").hide();
			jQuery("#extra_1").hide();
			menuBar.makeToolbar("basic");
			jQuery(".mail_body_tab_wrapper a:eq(1)").hide();
		}
		
		if (folderAuth == "R") {
			jQuery("#basic_0").hide();
			jQuery("#basic_1").hide();
			jQuery("#folerUploadL").hide();
		}
	}else{
		menuBar.makeToolbar("basic");
		if (type == "public" || type == "share") {
			menuBar.hideMenu("shareOption");
		}

		if (folderAuth == "R") {
			menuBar.hideMenu("createFolder");
			menuBar.hideMenu("renameFolder");
			menuBar.hideMenu("deleteFolderAndFile");  
			menuBar.hideMenu("searchMoveTarget");
			menuBar.hideMenu("searchCopyTarget");
			menuBar.hideMenu("downloadFiles");
			jQuery("#folerUploadL").hide();
		}
	}
}

function openCopyandMove(mode) {
	jQuery("#copyandmoveContent").empty();
	
	var popOpt = {};
	popOpt.closeName = comMsg.comn_close;
	popOpt.btnClass="btn_style3";	
	popOpt.minHeight = 355;
	popOpt.minWidth = 320;
	popOpt.openFunc = function(){
		var url = "/classic/webfolder/selectFolderList.jsp";
	    var param = {"mode": mode};	    
		jQuery("#copyandmoveContent").load(url,param);		
	    if (getListLoaded()) {
	    	
	    }
	};
	popOpt.closeFunc = function(){
		if (getListLoaded()) {
	    	
	    }
	};    
    jQuery("#copyandmove").jQpopup("open",popOpt); 
}

function toggleTree(id) {
	if (jQuery("#"+id).hasClass("bar_off")) {
		jQuery("#"+id).removeClass("bar_off");
		jQuery("#"+id).addClass("bar_on");
		jQuery("#"+id+"_content").hide();
	} else {
		jQuery("#"+id).removeClass("bar_on");
		jQuery("#"+id).addClass("bar_off");
		jQuery("#"+id+"_content").show();
	}
}

function checkSelectType() {
	var selectType = jQuery("#searchType").val();

	if (selectType == "org") {
		checkOrgList();
	} 
	else {
		jQuery("#share_org").hide();
		jQuery("#share_notorg").show();
		jQuery("#share_org_btn").hide();
		jQuery("#share_notorg_btn").show();
		jQuery("#shareKeyword").val("");
	}
}

function checkOrgList() {
	if (jQuery("#selectOrgSelect").html() == "") {
		getOrgList();
	} else {
		var orgOptionsCount = jQuery("#selectOrgSelect").selectboxGetOptionsCount();

		if (orgOptionsCount == 0) {
			alert(webfolderMsg.folder_share_alert_org_loading_empty);
			jQuery("#searchTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			return;
		}
		else {
			jQuery("#share_notorg").hide();
			jQuery("#share_org").show();
			jQuery("#share_notorg_btn").hide();
			jQuery("#share_org_btn").show();
		}
	}	
}


function getOrgList(type, auth, seq) {
	jQuery("#share_notorg_btn").hide();
	jQuery("#searching_user").show();
	
	var param = {};
	var func;
	
	jQuery.post("/dynamic/org/orgJsonlist.do",param,function(data) {
		if (type == "modify") {
			settingOrgSet(data, auth, seq);
		} else {
			settingOrgInit(data);
		}
	},"json");
}

function settingOrgSet(data, auth, seq) {
	if (data.isSuccess) {
		var orgList = data.orgList;
		jQuery("#selectOrgSelect").empty();
		var orgArray = [];
		var orgNameArray;
		var orgFullCodeArray;
		var orgName = "";
		var orgFullCode = "";
		if (orgList.length > 0) {
			for (var i=0; i < orgList.length; i++) {
				orgName = "";
				orgFullCode = "";
				orgNameArray = orgList[i].nameArray;
				orgFullCodeArray = orgList[i].codeArray; 
				if (orgNameArray && orgNameArray.length > 0) {
					for (var j=0; j < orgNameArray.length; j++) {
						if (j == 0) {
							orgName += orgNameArray[j];
						} else {
							orgName += ">"+ orgNameArray[j];
						}
					}
				}
				if (orgFullCodeArray && orgFullCodeArray.length > 0) {
					orgFullCode = orgFullCodeArray[orgFullCodeArray.length-1];
				}
				orgArray.push({index:orgName,value:orgFullCode});
			}

			jQuery("#selectOrgSelect").selectbox({selectId:"orgSelect",width:220}, "", orgArray);
			jQuery("#share_notorg_btn").show();
			jQuery("#searching_user").hide();

			var rSelect = jQuery("#readAuthBox");
			var rwSelect = jQuery("#rwAuthBox");
			var select;

			if (auth == "R") {
				select = rSelect;
			} else if(auth == "W") {
				select = rwSelect;	
			}

			includeSub = "";
			if (seq.lastIndexOf('+') != -1) {
				seq = seq.substring(0, seq.length-1);
				includeSub = "+";
			}
			
			var targetName = jQuery("#selectOrgSelect").selectboxGetText(seq);

			if (targetName != "") {
				if(!checkSharedList("O|"+seq) && !checkSharedList("O|"+seq+"+")){
					select.append(jQuery("<option value='O|"+seq+includeSub+"' title='"+webfolderMsg.folder_share_type_org+":"+targetName+includeSub+"'>"+webfolderMsg.folder_share_type_org+":"+targetName+includeSub+"</option>"));
				}
			}
		} else {
			alert(webfolderMsg.folder_share_alert_org_loading_empty);
			jQuery("#searchTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			return;
		}
	} else {
		alert(webfolderMsg.folder_share_alert_org_loading_fail);
		jQuery("#searchTypeSelect").selectboxSetValue("uid");
		jQuery("#shareSearchType").val("uid");
		return;
	}
}

function settingOrgInit(data) {
	if (data.isSuccess) {
		var orgList = data.orgList;
		jQuery("#selectOrgSelect").empty();
		var orgArray = [];
		var orgNameArray;
		var orgFullCodeArray;
		var orgName = "";
		var orgFullCode = "";
		if (orgList.length > 0) {
			for (var i=0; i < orgList.length; i++) {
				orgName = "";
				orgFullCode = "";
				orgNameArray = orgList[i].nameArray;
				orgFullCodeArray = orgList[i].codeArray; 
				if (orgNameArray && orgNameArray.length > 0) {
					for (var j=0; j < orgNameArray.length; j++) {
						if (j == 0) {
							orgName += orgNameArray[j];
						} else {
							orgName += ">"+ orgNameArray[j];
						}
					}
				}
				if (orgFullCodeArray && orgFullCodeArray.length > 0) {
					orgFullCode = orgFullCodeArray[orgFullCodeArray.length-1];
				}
				orgArray.push({index:orgName,value:orgFullCode});
			}
			jQuery("#selectOrgSelect").selectbox({selectId:"orgSelect",width:220}, "", orgArray);
			jQuery("#share_notorg").hide();
			jQuery("#share_org").show();
			jQuery("#share_notorg_btn").hide();
			jQuery("#share_org_btn").show();
			jQuery("#searching_user").hide();
		} else {
			alert(webfolderMsg.folder_share_alert_org_loading_empty);
			jQuery("#searchTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			jQuery("#share_notorg_btn").show();
			jQuery("#searching_user").hide();
			return;
		}
	} else {
		alert(webfolderMsg.folder_share_alert_org_loading_fail);
		jQuery("#searchTypeSelect").selectboxSetValue("uid");
		jQuery("#shareSearchType").val("uid");
		jQuery("#share_notorg_btn").show();
		jQuery("#searching_user").hide();
	}
}

function setSharedOrgList(){
	var select = jQuery("#resultBox");
	var includeSubCheck = jQuery("#includeSub").attr("checked");
	var selectText = jQuery("#selectOrgSelect_selectText").text();
	var selectValue = jQuery("#orgSelect").val();
	var includeSubText = "";
	if (includeSubCheck) {
		includeSubText = "+";
	}
	if(!checkSharedList("O|"+selectValue) && !checkSharedList("O|"+selectValue+"+")){
		select.append(jQuery("<option value='O|"+selectValue+includeSubText+"' title='"+selectText+includeSubText+"'>"+webfolderMsg.folder_share_type_org+":"+selectText+includeSubText+"</option>"));
	}
}

function checkSharedList(userSeq){
	var selectOption = jQuery("#resultBox option");
	var isExist = false;
	for ( var i = 0; i < selectOption.length; i++) {
		if(userSeq == jQuery(selectOption[i]).attr("value")){
			isExist = true;
			break;
		}
	}
	
	return isExist;
}

function setUserShareList(auth, type, seq, uid, name) {
	var rSelect = jQuery("#readAuthBox");
	var rwSelect = jQuery("#rwAuthBox");
	var select;

	if (auth == "R") {
		select = rSelect;
	} 
	else if (auth == "W") {
		select = rwSelect;
	}
	
	if (type == "1") {
		var option = jQuery("<option value='U|"+seq+"/"+name+"' title='"+webfolderMsg.folder_share_type_user+":"+name+"("+uid+")'>"+webfolderMsg.folder_share_type_user+":"+name+"("+uid+")</option>");
		select.append(option);
	} else {
		if (jQuery("#selectOrgSelect").html() == "") {
			getOrgList("modify", auth, seq);
		} else {
			includeSub = "";
			if (seq.lastIndexOf('+')) {
				seq = seq.substring(0, seq.length-1);
				includeSub = "+";
			}

			var targetName = jQuery("#selectOrgSelect").selectboxGetText(seq);
			
			if (targetName != "") {
				if(!checkSharedList("O|"+seq) && !checkSharedList("O|"+seq+"+")){
					select.append(jQuery("<option value='O|"+seq+includeSub+"' title='"+webfolderMsg.folder_share_type_org+":"+targetName+includeSub+"'>"+webfolderMsg.folder_share_type_org+":"+targetName+includeSub+"</option>"));
				}
			}
		}
	}
}

function createFolderWin() {	
	if("open" == jQuery("#createFolder").jQpopup("status"))return;	
	var popOpt = {};	
	popOpt.closeName = comMsg.comn_close;
	popOpt.btnClass="btn_style3";	
	popOpt.btnList = [{name:comMsg.comn_confirm,func:createFolder}];
	popOpt.minHeight = 120;
	popOpt.minWidth = 330;
	popOpt.closeFunc = function(){
		jQuery("#fNameInput").val("");
		if (getListLoaded()) {
			
	    }
	};
	
	jQuery("#createFolder").jQpopup("open",popOpt);
		if (getListLoaded()) {
	    	
	    }
}

function createFolder() {
	var folderObj = jQuery("#fNameInput");
	var folderName = folderObj.val();

	if(!checkInputLength("jQuery", folderObj, webfolderMsg.alert_nofile, 2, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", folderObj, "folderName")) {
		return;
	}
	if(!isFolderName(folderName) || folderName.indexOf("|") > -1 ) {
        alert(settingMsg.conf_alert_mailbox_invalidname+"\n\n"
                +"%,&,*,.,/,\\,',`,|,\"");
        folderObj.val("");
        folderObj.focus();
        return;
    }
	
	
	window.fraContent.createFolder(jQuery.trim(folderName));

	jQuery("#createFolder").jQpopup("close");
}

function renameFolderWin(folderName) {
	if("open" == jQuery("#renameFolder").jQpopup("status"))return;
	var popOpt = {};
	popOpt.closeName = comMsg.comn_close;
	popOpt.btnClass="btn_style3";	
	popOpt.btnList = [{name:comMsg.comn_confirm,func:renameFolder}];
	popOpt.minHeight = 120;
	popOpt.minWidth = 330;
	popOpt.closeFunc = function(){
		jQuery("#rfNameInput").val("");
		if (getListLoaded()) {
			
	    }
	};
	
	jQuery('#selectFolder').text(folderName);    
	jQuery("#renameFolder").jQpopup("open",popOpt);
	if (getListLoaded()) {
    	
    }
}

function renameFolder() {
	var folderObj = jQuery("#rfNameInput");
	var folderName = folderObj.val();

	if(!checkInputLength("jQuery", folderObj, webfolderMsg.alert_nofile, 2, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", folderObj, "folderName")) {
		return;
	}

	window.fraContent.renameFolder(jQuery.trim(folderName));
	jQuery("#renameFolder").jQpopup("close");
}

function setTitle(title) {
	jQuery("#workTitle").text(title);
}

function searchList() {
	window.fraContent.searchList();
}

function resizeTextFrame(height,width){
	
	//$("fraContent").style.height=height-20+"px";
	jQuery("#fraContent").css({"overflow-x":"hidden","overflow-y":"hidden"});
	contentFrameWidth = width;	
	var func = function(){
			var wrapper = jQuery("#w_contentMain");
			var parentWrapper = jQuery("#w_contentMain").parent();
			wrapper.css("width",(isMsie6)?"":"100%");
		};
		jQuery(window).bind("resize",func);
	
	func();	
}
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask(0);</script>
<div style="clear: both;"></div>

<div id="w_mainBody">	
	<div id="w_leftMenuContent">				
		<div id="wl_top">
		<div class="titleBar_title">
			<span class="title_webfolder"><tctl:msg key="webfolder.quota.info" bundle="webfolder"/></span>
		</div>			
		
		<div id="ml_quotaBox">				
			<div class="TM_graphBox">
				<div id="ml_quotaGraphBar" class="TM_graphBar" ></div>
				<div class="TM_capacity">
					<span id="ml_quotaUseAge" class="TM_quotaUsage">0M</span> / <span id="ml_quotaTotal">0M</span>
				</div>
				&nbsp;					
			</div>
		</div>
		</div>
		
		<div id="leftMenuRcontentWrapper">
			<div id="leftMenuRcontent">
				<div class="webfolder_subContent">
					<iframe frameborder="0" width="100%" align="top" src="/webfolder/folderTree.do" name='deeptree_user' id="deeptree_user" scrolling="auto"></iframe>
				</div>
			</div>
		</div>
		<%@include file="/common/sideMenu.jsp"%>
	</div>

	<div id="w_contentBodyWapper" class="TM_contentBodyWapper">
		<div class="TM_folderInfo">
			<img class="TM_barLeft" src="/design/common/image/blank.gif"/>								
			<div class="TM_finfo_data"> <span class="TM_work_title" id="workTitle"><tctl:msg key="webfolder.title.01" bundle="webfolder"/></span></div>
			<div class="TM_finfo_search">
				<form name="searchForm">
					<input type="hidden" name="searchType" value="name"/>
				<div class="TM_mainSearch">						
					<div style="float: left">
						<input type="text" class="searchBox"  name="keyWord" id="keyWord" onKeyPress="(keyEvent(event) == 13) ? searchList() : '';" /><a href="#" onclick="searchList()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a>
						<input type="text" name="_tmp" style="display:none;"/>
					</div>
					<div class="fclear"></div>	
				</div>
				</form>
			</div>
			<img class="TM_barRight" src="/design/common/image/blank.gif"/>						
		</div>		
		
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
			
		<div id="w_mainContent" class="TM_mainContent">
		<div id="mailMenubar">		
			<div class="mail_body_tabmenu">
				<div class="mail_body_tab" id="menuBarTab"></div>
				<div id="pageNavi"  class="mail_body_navi"></div>
			</div>		
			<div class="mail_body_menu">
				<div class="menu_main_unit" id="menuBarContent"></div>
			</div>
		</div>
		<div id="w_contentBody">
			<div id="w_contentMain">
				<iframe id="fraContent" name="fraContent" frameborder='0' style="width: 100%;min-height:400px;" scrolling="no"></iframe>				
			</div>			
			<div id="w_contentSub" ></div>
		</div>	
		</div>
	</div>
</div>

<%@include file="/common/bottom.jsp"%>

<div style="clear: both;"></div>

<div id="createFolder" title="<tctl:msg key="folder.create" bundle="webfolder"/>" style="display:none;">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="100px"></col>
		<col></col>
		<tr>
		<th class="lbout"><tctl:msg key="folder.name" bundle="webfolder"/></th>
		<td><input type="text" id="fNameInput" name="fNameInput" class="IP200"/></td>
		</tr>		
	</table>		
</div>

<div id="renameFolder" title="<tctl:msg key="folder.rename" bundle="webfolder"/>" style="display:none;">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="100px"></col>
		<col></col>
		<tr>
		<th class="lbout"><tctl:msg key="folder.curname" bundle="webfolder"/></th>
		<td><span id="selectFolder"></span></td>
		</tr>
		<tr>
		<th class="lbout"><tctl:msg key="folder.newname" bundle="webfolder"/></th>
		<td><input type="text" id="rfNameInput" name="rfNameInput" class="IP200"/></td>
		</tr>		
	</table>
</div>

<div id="search_share_pop" title="<tctl:msg key="folder.searchshare" bundle="webfolder"/>">
	<div id="search_share_div"></div>
</div>
<div id="share_folder_pop" title="<tctl:msg key="folder.share" bundle="webfolder"/>">
	<div id="share_folder_div"></div>
</div>
<div id="searchFolder" class="menuBox" style="height:280px"></div>

<div id="copyandmove" title="<tctl:msg key="folder.select" bundle="webfolder"/>" style="display:none">
	<div id="copyandmoveContent"></div>
</div>

<script language="javascript">
loadToolBar();
</script>


</body>
</html>