<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%@ include file="/common/header.jsp" %>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/bbs_style.css" />

<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>
<script type="text/javascript" src="/classic/bbs/bbsWriteMenuBar.js"></script>
<%--<script type="text/javascript" src="/editor/ckeditor/ckeditor.js"></script>--%>
<script type="text/javascript" src="/editor/smarteditor/js/service/HuskyEZCreator.js?dummy=" charset="utf-8"></script>
<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/ext-lib/html5FileUploader.js"></script>

<script language = "javascript">
var basicAttachUploadControl;
var MAX_ATTACH_SIZE = "${boardVo.attMaxSize}";
var MAX_ATTACH_COUNT = "${boardVo.attMaxCnt}";
var locale = LOCALE;
var isOcxUpload = false;
var isOcxUploadDownModule = false;
var menuBar;

function init(){	
	setTopMenu('bbs');

	var mainLayerPane = new LayerPane("b_mainBody","TM_m_mainBody");	
	var menuLayerPane = new LayerPane("b_leftMenuContent","TM_m_leftMenu",220,220,350);
	var contentLayerPaneWapper = new LayerPane("b_contentBodyWapper","",300,100,500);
	
	
	mainSplitter = new SplitterManager(mainLayerPane,
										menuLayerPane,
										contentLayerPaneWapper,
										"sm","mainvsplitbar","hsplitbar");	
	mainSplitter.setReferencePane(["b_contentBody","copyRight"]);	
	mainSplitter.setSplitter("v",true);
	jQuery(window).autoResize(jQuery("#b_mainBody"),"#copyRight");
	
	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();

	var contentLayerPane = new LayerPane("b_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("b_contentMain","TM_m_contentMain",300,0,0);
	var previewLayerPane = new LayerPane("b_contentSub","TM_m_contentSub",400,0,0);		
	
	contentSplitter = new SplitterManager(contentLayerPane,
			listLayerPane,
			previewLayerPane,
			"sc","vsplitbar","hsplitbar");
	
	contentSplitter.setSplitter("n",false);

	jQuery(window).autoResize(jQuery("#b_contentBody"),"#copyRight");	
	jQuery(window).trigger("resize");

	contentSplitter.setSplitter("n",false);
	
	loadToolBar();

	if(skin != "skin3"){
		menubarControl();
	}else{
		menubarControlSkin3();
	}
	
	menuBar.setPageNavi("b",{});
	jQuery("#bbsLeftMenu").treeview({cookieId:"TBST",persist:"cookie"});
	jQuery("#bbs_select_${boardVo.bbsId} a").addClass("menu_selected");
	setTitle();
	jQuery.removeProcessBodyMask();
	jQuery("#searchTypeSelect").selectbox({selectId:"inputSearchType",
		selectFunc:""},
		"${fn:escapeXml(searchType)}",
		[{index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/>",value:"1"},
		 {index:"<tctl:msg key="bbs.content.list.creator" bundle="bbs"/>",value:"2"},
		 {index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/> + <tctl:msg key="bbs.content.list.creator" bundle="bbs"/>",value:"4"}]);

	if ("${boardVo.bbsType}" == "secret") {
		jQuery(".TM_finfo_search").hide();
	}
	start();
}

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").outerHeight(true):25;
	}
	var topAreaSize = 4;
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#b_leftMenuContent", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontent", 
		mainId:"#b_leftMenuContent", 
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

function menubarControl() {
	jQuery("#saveBt_item").hide();
	jQuery("#updateBt_item").hide();

	var writeAuth = '${boardVo.writeAuth}';
	var isAdmin = '${bbsAdmin}';

	<c:if test="${(bbsAdmin == 'true' && boardVo.writeAuth == 'ADMIN') || (boardVo.writeAuth == 'LOGIN' || boardVo.writeAuth == 'ALL')}">
		<c:if test="${isModify == 'true'}">
			jQuery("#updateBt_item").show();
		</c:if>
		<c:if test="${isModify != 'true'}">
			jQuery("#saveBt_item").show();
		</c:if>
	</c:if>
}
function menubarControlSkin3(){
	
	menuBar.hideMenu("saveBt");
	menuBar.hideMenu("updateBt");

	<c:if test="${(bbsAdmin == 'true' && boardVo.writeAuth == 'ADMIN') || (boardVo.writeAuth == 'LOGIN' || boardVo.writeAuth == 'ALL')}">
		<c:if test="${isModify == 'true'}">		
			menuBar.showMenu("updateBt");
		</c:if>
		<c:if test="${isModify != 'true'}">
			menuBar.showMenu("saveBt");
		</c:if>
	</c:if>
}

var oEditors = [];
function editorBoxScript()
{
     nhn.husky.EZCreator.createInIFrame({
 	    oAppRef: oEditors,
 	    elPlaceHolder: "smarteditor",
 	    sSkinURI: "/editor/smarteditor/SmartEditor2Skin.html",
 	    fCreator: "createSEditor2",
    	htParams : {
			fOnBeforeUnload : function(){}
		}
 	});  
}

var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};

function modalPopupImageUpload(src){
	var popOpt = clone(popupOpt);
	popOpt.btnList = null;
	var height = 170;
	var width = 390;
	popOpt.minHeight = height;
	popOpt.minWidth = width;
	popOpt.openFunc = function(){
		setTimeout(function(){
			jQuery("#editorImageUploadIframe").attr("src",src);
			jQuery("#editorImageUploadIframe").css("height",(height-20)+"px");
			jQuery("#editorImageUploadIframe").css("width",(width-10)+"px");
		},100);
		jQuery("#editorImageUpload_jqbtn").hide();
	};
	popOpt.closeFunc = function(){
		
	};
	
	jQuery("#editorImageUpload").jQpopup("open",popOpt);
}

function modalPopupImageUploadClose(){
	jQuery("#editorImageUpload").jQpopup("close");
}

function saveContent() {
	var f = document.contentwriteForm;
	
	if(!checkInputLength("", f.subject, bbsMsg.bbs_alert_subject_empty, 2, 255)) {
		return;
	}
	if(!checkInputValidate("", f.subject, "onlyBack")) {
		return;
	}

	<c:if test="${boardVo.bbsType == 'any'}">
		if(!checkInputLength("", f.creatorName, bbsMsg.bbs_alert_creatorname_empty, 2, 48)) {
			return;
		}
		if(!checkInputValidate("", f.creatorName, "folderName")) {
			return;
		}
	</c:if>
	
	var writeMode = jQuery("#writeMode").val();
	if (jQuery("#notice").attr("checked")) {
		f.isNotice.value= "1";
	}
	if (writeMode == "1") {
		f.content.value = GetHtmlMessage();
	} else {
		f.content.value = jQuery("#contentText").val();
	}

    var confirmMsg = "";
    if ("${isModify}" == "true") {
    	f.action = "/bbs/updateContent.do";
    	confirmMsg = bbsMsg.bbs_content_modify_confirm;
    } 
    else {
    	f.action = "/bbs/saveContent.do";
    	confirmMsg = bbsMsg.bbs_content_save_confirm;
    }

	if (!confirm(confirmMsg)) {
		return;
	}	
	
	uploadFiles();
}

function uploadFiles(){
	startUploadAttach();	
}

function requestSaveContents(){
	
	jQuery("body").loadWorkMaskOnly(true);	
	
	var f = document.contentwriteForm;
	
	var attstr = getAttachString();
	if (attstr != "") {
    	f.attFiles.value = attstr;
	}
	
	f.method = "post";
	f.submit(); 
	
}

function GetStyle() {
	var pstr = "<style type='text/css'>\n"
		+ ".TerraceMsg { font-size: 12px; font-family: Dotum, Arial, Verdana, Sans-Serif;}\n"
		+ ".Bold { font-weight: bold; }\n"
		+ "</style>";
	return pstr;
}

function GetHtmlMessage(type)
{		
    //var oEditor = CKEDITOR.instances.ckeditor ;
    oEditors.getById["smarteditor"].exec("UPDATE_CONTENTS_FIELD", []);
	var editorData = document.getElementById("smarteditor").value;
    var defaultStyle='';
   
    if(type!='ck')
    {
    	defaultStyle=GetStyle();
    }
    
	//return oEditor.getData();
	return editorData;
}

function checkAttachFile() {
	var f = document.contentwriteForm;
	var str = "";
	<c:if test = "${!empty attList}">
		<c:forEach var="attFile" items="${attList}">
			var path = "${attFile.attPath}";
			var name = "${attFile.attName}";
			var size = "${attFile.attSize}";

			addlist(name, size, path);
		</c:forEach>
	</c:if>
}

function start() {

	var attAuth = "${boardVo.attMaxCnt}";
	if (attAuth == "0") {
		jQuery("#upload_area").hide();
		jQuery("#upload_info").hide();
	}
	
	var f = document.contentwriteForm;
	var isNotice = "${contentVo.isNotice}";
	if (isNotice == "1") {
		f.notice.checked = true;
	}
	var basicControlOpt = {
			controlType:"normal",
			btnId:"basicUploadControl",
			btnCid:"basicUploadBtn",
			formName:"theFile",
			param:{"uploadType":"flash"},
			url:"/file/uploadAttachFile.do",
			maxFileSize:(MAX_ATTACH_SIZE * 1024 * 1024),
			fileType:"*.*",
			locale:LOCALE,
			btnWidth:100,
			btnHeight:20,			
			debug:false,
			autoStart:false,
			handler:basicUploadListeners,
			listId:"basicUploadAttachList",
			startUploadFunc:startUploadAttach};
	
	basicAttachUploadControl = new UploadBasicControl(basicControlOpt);		
	chgAttachMod('simple');		
	setTimeout('checkAttachFile()', 1000);
}

function makeTree(bbsId, bbsUpId, title, bbsType) {

	var bbsTypeName = "";
	var typeCss = "";

	if (bbsType == "" || bbsType == "basic") {
		bbsTypeName = bbsMsg.bbs_type_001;
	} else if (bbsType == "any") {
		bbsTypeName = bbsMsg.bbs_type_002;
		typeCss = "bbs_any";
	} else if (bbsType == "secret") {
		bbsTypeName = bbsMsg.bbs_type_003;
		typeCss = "bbs_secret";
	}
	
	var tree = jQuery("#bbs_"+bbsUpId);
	var treeUl = jQuery("<ul></ul>");
	var treeLi = jQuery("<li id='bbs_"+bbsId+"'></li>");
	var treeSpan = jQuery("<div id='bbs_select_"+bbsId+"' title='"+bbsTypeName+"'></div>");
	var treeLink = jQuery("<a href='/bbs/listContent.do?bbsId="+bbsId+"'>"+title+"</a>");
	treeUl.css("display","none");
	treeSpan.addClass("icon_bbs");
	treeSpan.addClass(typeCss);
	treeSpan.append(treeLink);
	treeLi.append(treeSpan);
	treeUl.append(treeLi);
	tree.append(treeUl);
}

function setTitle() {
	var fullId = '${boardVo.bbsFullId}';
	var treeIds = fullId.split('|');
	var title = "";
	if (treeIds.length) {
		for (i=0; i<treeIds.length; i++) {
			title += jQuery('#bbs_select_'+treeIds[i]+' a').text();
			if (i < treeIds.length-1) {
				title += " > ";
			}
		}
	}
	jQuery("#workTitle").text(title);
}
</script>
</head>
<body onload="init()">
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="b_mainBody">
	<div id="b_leftMenuContent">
			<div id="leftMenuRcontentWrapper">
			<div id="leftMenuRcontent">
			<div>	
			<div id="m_leftMenu">			
				<div style="white-space:nowrap;">
					<ul id="bbsLeftMenu">
						<c:forEach var="board" items="${boardList}">
							<c:if test="${board.bbsUpId > 0}">
								<script language = "javascript">
									makeTree('${board.bbsId}','${board.bbsUpId}', '${board.bbsName}', '${board.bbsType}');
								</script>
							</c:if>
							<c:if test="${board.bbsUpId <= 0}">
							<li id="bbs_${board.bbsId}">
								<div id="bbs_select_${board.bbsId}" class="icon_bbs <c:if test="${board.bbsType == 'any'}">bbs_any</c:if><c:if test="${board.bbsType == 'secret'}">bbs_secret</c:if>" title="<c:if test="${empty board.bbsType || board.bbsType == 'basic'}"><tctl:msg key="bbs.type.001" bundle="bbs"/></c:if><c:if test="${board.bbsType == 'any'}"><tctl:msg key="bbs.type.002" bundle="bbs"/></c:if><c:if test="${board.bbsType == 'secret'}"><tctl:msg key="bbs.type.003" bundle="bbs"/></c:if>">
									<a href="/bbs/listContent.do?bbsId=${board.bbsId}">${board.bbsName}</a>
								</div>
							</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>				
			</div>
			</div>
			</div>
			</div>
			<%@include file="/common/sideMenu.jsp"%>
	</div>
	
	<div id="b_contentBodyWapper" class="TM_contentBodyWapper">
		<div class="TM_folderInfo">
			<img src="/design/common/image/blank.gif" class="TM_barLeft">			
			<div class="TM_finfo_data"><span class="TM_work_title" id="workTitle"></span></div>
			<div class="TM_finfo_search">
				<div class="TM_mainSearch">							
				<div style="float: left">					
					<div id="searchTypeSelect"></div>				
				</div>
				<div style="float: left;padding-left:3px;">
					<input type="text" name="_tmp" style="display:none">
					<input type="text" class="searchBox"  name="inputKeyWord" id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchContent() : '';" /><a href="#" onclick="searchContent()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a> <a href="#" onclick="searchMine()" class="TM_search_btn"><span><tctl:msg key="bbs.content.list.mine" bundle="bbs"/></span></a>
				</div>
				<div class="fclear"></div>								
				</div>
			</div>	
			<img src="/design/common/image/blank.gif" class="TM_barRight">		
		</div>
		
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
			
		<div id="b_mainContent" class="TM_mainContent">	
			<div id="mailMenubar">		
				<div class="mail_body_tabmenu">
					<div class="mail_body_tab" id="menuBarTab"></div>
					<div id="pageNavi"  class="mail_body_navi"></div>
				</div>		
				<div class="mail_body_menu">
					<div class="menu_main_unit" id="menuBarContent"></div>
				</div>
			</div>
			
			
			<div id="b_contentBody">
				<div id="b_contentMain" >			
					<div class="TM_content_wrapper">
						<div class="TM_mail_content">
							<form name="contentwriteForm" onsubmit="return false;">
								<input type="hidden" name="bbsId" value="${boardVo.bbsId}">
								<input type="hidden" name="bbsType" value="${boardVo.bbsType}">
								<input type="hidden" name="contentId" value="${fn:escapeXml(contentId)}">
								<input type="hidden" name="parentId" value="${fn:escapeXml(parentId)}">
								<input type="hidden" name="depth" value="${fn:escapeXml(depth)}">
								<input type="hidden" name="orderNo" value="${fn:escapeXml(orderNo)}">
								<input type="hidden" name="isReply" value="${fn:escapeXml(isReply)}">
								<input type="hidden" name="isNotice">
								<input type="hidden" name="content">
								<input type="hidden" name="attFiles">
								<input type="hidden" name="searchType">
								<input type="hidden" name="keyWord">
								<input type="hidden" name="currentPage">
								<input type="hidden" name="writeMode" id="writeMode" value="1">		
								<script type="text/javascript">makePAID();</script>
								<table cellpadding="0" border="0" id="bbs_subject" class="TM_tableList">
									<col width="60px"></col>
									<col></col>
									<tr>
									<th nowrap><tctl:msg key="bbs.content.list.subject" bundle="bbs"/></th>
									<td><input class="TM_write_input" type="text" name="subject" value="${contentVo.subject}"></td>
									</tr>
									<c:if test="${boardVo.bbsType == 'any'}">
									<tr>
									<th nowrap><tctl:msg key="bbs.content.list.creator" bundle="bbs"/></th>
									<td><input class="IP200"  type="text" id="creatorName" name="creatorName" value="${contentVo.creatorName}" maxlength="30"></td>
									</tr>
									</c:if>
								</table>
		
								<div class="TM_separate2"></div>
		
								<table cellpadding="0"  cellspacing="0" border="0" id="editorTable">
								<c:if test="${bbsAdmin == 'true'}">
									<tr height="30px">
										<td class="TM_bbs_toolbar">
											<input type="checkbox" name="notice" id="notice"> <tctl:msg key="bbs.content.write.isnotice" bundle="bbs"/>
										</td>
									</tr>
								</c:if>
									<tr>
										<td id="modeText" style="display:none;border:1px solid #d6d6d6; padding:5px;">
											<textarea name="contentText" id="contentText" rows="30" style="width: 100%;border:none; background:#ffffff">${fn:escapeXml(contentVo.textContent)}</textarea>
											<textarea name="contentHtml" rows="30" style="display:none"><div style="word-wrap: break-word; word-break: break-all;">${fn:escapeXml(contentVo.htmlContent)}</div></textarea>
										</td>
									</tr>
									<tr>
										<td id="modeHtml" height="400px">
										<%--<textarea cols="100" id="ckeditor" name="ckeditor" rows="10">${contentVo.htmlContent}</textarea>--%>
										<textarea name="smarteditor" id="smarteditor" rows="10" cols="100" style="width: 100%; height: auto; ">${contentVo.htmlContent}</textarea>
						                    <script type="text/javascript">
						                        editorBoxScript();
						                    </script>
										</td>
									</tr>
								</table>
							</form>
							
							<div class="TM_separate2"></div>
		
							<div id="upload_area">
								<%@include	file="uploadPage.jsp"%>
							</div>
							<div id="upload_info" style="text-align:right;padding:5px;" >
								<span>[<tctl:msg key="attach.count.max" arg0="${boardVo.attMaxCnt}" bundle="bbs"/>]</span>
								<%-- <span id="att_ocx_quota_info">[<b><tctl:msg key="mail.attach.normal"/> </b>(<span id=ocx_normal_size>0B</span> / ${boardVo.attMaxSize}MB)]</span> --%>    
								<span id="att_simple_quota_info">[<b><tctl:msg key="mail.attach.normal"/> </b>(<span id=basic_normal_size>0B</span> / ${boardVo.attMaxSize}MB)]</span>
							</div>
						</div>
					</div>
				</div>
				<div id="b_contentSub" ></div>
				</div>				
			</div>
		</div>
	</div>
</div>
<%@include file="/common/bottom.jsp"%>

<div id="attachUploadProgress"  title="<tctl:msg key="comn.upload.title" bundle="common"/>" style="display:none">
<form name="attachUploadProgressForm" id="attachUploadProgressForm">	
	<div id="attachUploadProgressContents">			
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;">
			<col></col>
			<col width="100px"></col>			
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.filename" bundle="common"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.status" bundle="common"/>
			</th>
			</tr>
		</table>			
		<div id="uploadLoadingBox" style="height:300px; overflow-X:hidden;overflow-Y:auto; padding:1px;position: relative;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="attachFileUploadTable" style="width:100%;*width:;table-layout:fixed;position: relative;">
				<col></col>
				<col width="100px"></col>				
			</table>
		</div>
	</div>
</form>
</div>
<div id="editorImageUpload" title="<tctl:msg key="mail.editor.image.upload.title"/>" style="display:none">
    <iframe id="editorImageUploadIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

</body>
</html>