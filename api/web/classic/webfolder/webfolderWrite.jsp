<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/webfolder_style.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/popup_style.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/common.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/layout.css"/>
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/webfolder/webfolderWriteMenuBar.js"></script>

<script type="text/javascript" src="/i18n?bundle=webfolder&var=webfolderMsg&locale=<%=locale%>"></script>

<script language="javascript">
var menuBar;
var oldNode = "";

jQuery().ready(function(){
	loadToolBar();
	if(skin == "skin3"){
		jQuery("#menuBarTab").css("width","420px");
		jQuery("#mailMenubar .mail_body_tabmenu").css({"border-top":"1px solid #C8CDD1"});
		jQuery("#mailMenubar .mail_body_menu").css("display","none");
	}
});

function resizeFrame(height){	
	//$("fraContent").style.height=height+"px";
	//winResize();	
}

var isLoaded = false;
function loadList(){
	window.fraContent.document.location = "/webfolder/listFolders.do";
	isLoaded = true;
}

function movePage(page) {
	window.fraContent.movePage(page);
} 

function setPageNavi(total, pageBase, currentPage) {
	//menuBar.setPageNavi("p",{total:total,base:pageBase,page:currentPage});
	menuBar.setPageNaviBottom("p",{total:total,base:pageBase,page:currentPage});
	parentSetCurrentPage(currentPage);
}

function setQuotaInfo(used, total, usedPercent) {}

function checkMenubar(isFolderShare, isRoot, type, folderAuth) {}

function resetSearch() {};

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
		window.frames.deeptree_user.setCurrentNode(oldNode);
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

var isListLoaded = false;
function setListLoaded(val){
	isListLoaded = val;
}

function getListLoaded(){
	return isListLoaded;
}

function getIsPop(){
	return true;
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

function toggleTree(id) {
	jQuery(".treeContent").hide();
	jQuery(".webfolder_subContent .tree").removeClass("bar_off");
	jQuery(".webfolder_subContent .tree").removeClass("bar_on");
	jQuery(".webfolder_subContent .tree").addClass("bar_on");
	
	jQuery("#"+id).removeClass("bar_on");
	jQuery("#"+id).addClass("bar_off");
	jQuery("#"+id+"_content").show();
	
}

function setTitle() {}
function setSearch(value) {}
function resizeTextFrame(height,width){}

function closeWin(){
	if(parent){
		parent.modalPopupForWebfolderClose(); 
		parent.jQpopupClear();
	}	
	else
		window.close();	
}
</script>
</head>
<body style='margin:0px;padding:0px;background:none;'>
	<div class="popup_style1" style="border:0px;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="webfolder.title.02" bundle="webfolder"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>

		<table class="TB_popup_style1_body">
			<tbody>
				<tr>
					<td class="bg_left"/>
					<td class="popup_content" style="background:none;">
						<table cellpadding="0" cellspacing="0" style="width:100%;height:400px;">
							<tr>
								<td width="200px" height="100%" valign="top">
									<div class="webfolder_subContent">
										<iframe frameborder="0" width="100%" height="438" align="top" src="/webfolder/folderTree.do" name='deeptree_user'></iframe>
									</div>
								</td>
								<td width="3px" height="100%">&nbsp;</td>
								<td valign="top" height="100%">
									<div id="m_contentBodyWapper">
										<div id="mailMenubar">		
											<div class="mail_body_tabmenu">
												<div class="mail_body_tab" id="menuBarTab"></div>
												<div id="pageNavi"  class="mail_body_navi"></div>
											</div>		
											<div class="mail_body_menu">
												<div class="menu_main_unit" id="menuBarContent"></div>
											</div>
										</div>
										<iframe frameborder="0" style="width:100%;height:350px;" align="middle" valign="top"
																	name="fraContent" id="fraContent" scrolling="auto"> 
										</iframe>	
									</div>
								</td>
							</tr>
						</table>
					</td>
					<td class="bg_right"/>
				</tr>
			</tbody>
		</table>
		<div class="popup_style1_down" style="display:none;">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>
	<div class="dotLine"></div>
	<div class="btnArea" style="margin-top:10px;">
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>