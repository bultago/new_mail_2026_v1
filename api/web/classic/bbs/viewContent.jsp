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
<script type="text/javascript" src="/classic/bbs/bbsReadMenuBar.js"></script>

<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>
<script language = "javascript">

function init() {	
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
	var listLayerPane = new LayerPane("b_contentMain","TM_m_contentMain",0,0,0);
	var previewLayerPane = new LayerPane("b_contentSub","TM_m_contentSub",215,0,0);		
	
	contentSplitter = new SplitterManager(contentLayerPane,
			listLayerPane,
			previewLayerPane,
			"sc","vsplitbar","hsplitbar");
	
	contentSplitter.setSplitter("n",false);
	jQuery(window).autoResize(jQuery("#b_contentBody"),"#copyRight");
	contentSplitter.setSplitter("n",false);

	jQuery("#bbsLeftMenu").treeview({cookieId:"TBST",persist:"cookie"});
	jQuery("#bbs_select_${boardVo.bbsId} a").addClass("menu_selected");
	setTitle();

	jQuery(window).trigger("resize");
	jQuery.removeProcessBodyMask();

	var f = document.contentForm;	
	if ("${fn:escapeXml(searchType)}" != "7") {
		f.inputKeyWord.value = "${fn:escapeXml(keyWord)}";
	}
	
	jQuery("#searchTypeSelect").selectbox({selectId:"inputSearchType",
		selectFunc:""},
		"${fn:escapeXml(searchType)}",
		[{index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/>",value:"s"},
		 {index:"<tctl:msg key="bbs.content.list.content" bundle="bbs"/>",value:"b"},
		 {index:"<tctl:msg key="bbs.content.list.creator" bundle="bbs"/>",value:"c"},
		 {index:"<tctl:msg key="bbs.content.list.attach" bundle="bbs"/>",value:"af"},
		 {index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/> + <tctl:msg key="bbs.content.list.content" bundle="bbs"/>",value:"sb"}]);
	 
	setTimeout(function(){jQuery(window).trigger("resize");},100);
	if(skin != "skin3"){
		checkMenubar();
	}else{
		checkMenubarSkin3();
	}
	getReplyMessage();
	var replyCookie = getCookie("TSBR");
	if (replyCookie == "close") {
		jQuery("#replyBox").hide();
		jQuery("#toggleLink").removeClass("open");
		jQuery("#toggleLink").addClass("close");
	}

	if ("${boardVo.bbsType}" == "secret") {
		jQuery(".TM_finfo_search").hide();
	}
	jQuery("#messageContentFrame").attr("src","/classic/bbs/messageContent.jsp?locale="+LOCALE);
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

function checkMenubar() {
	jQuery("#basic_0").hide();
	jQuery("#modifyBt_item").hide();

	<c:if test="${boardVo.bbsType == 'any'}">
		jQuery("#replyBt_item").hide();
		jQuery("#modifyBt_item").hide();
		jQuery("#deleteBt_item").hide();
	</c:if>		
	
	<c:if test="${boardVo.writeAuth == 'LOGIN' || boardVo.writeAuth == 'ALL'}">	
		jQuery("#modifyBt_item").hide();
		jQuery("#deleteBt_item").hide();
		jQuery("#basic_0").show();
	</c:if>	
	
	<c:if test="${bbsAdmin == 'true' || contentVo.creator == 'true'}">
		jQuery("#deleteBt_item").show();
		jQuery("#basic_0").show();
	</c:if>	
	
	<c:if test="${contentVo.creator == 'true'}">
		jQuery("#modifyBt_item").show();
	</c:if>				
}

function checkMenubarSkin3() {
	
	menuBar.hideMenu("replyBt");
	menuBar.hideMenu("modifyBt");
	menuBar.hideMenu("deleteBt");

	<c:if test="${boardVo.bbsType == 'any'}">
		menuBar.hideMenu("replyBt");
		menuBar.hideMenu("modifyBt");
		menuBar.hideMenu("deleteBt");
	</c:if>	

	<c:if test="${boardVo.writeAuth == 'LOGIN' || boardVo.writeAuth == 'ALL'}">	
		jQuery("#modifyBt_item").hide();
		menuBar.showMenu("replyBt");
		menuBar.showMenu("moveBt");		
	</c:if>	
	
	<c:if test="${bbsAdmin == 'true' || contentVo.creator == 'true'}">
		menuBar.showMenu("replyBt");
		menuBar.showMenu("modifyBt");
		menuBar.showMenu("deleteBt");
		menuBar.showMenu("moveBt");
	</c:if>	
	
	<c:if test="${contentVo.creator == 'true'}">
		menuBar.showMenu("modifyBt");
	</c:if>
}

function replyContent() {
	var f = document.contentForm;
	f.isReply.value="true";
	f.method = "post";
	f.action = "/bbs/writeContent.do";
	f.submit();
}

function modifyContent() {
	var f = document.contentForm;
	f.isModify.value="true";
	f.method = "post";
	f.action = "/bbs/modifyContent.do";
	f.submit();
}

function viewContent(contentId,parentId,orderNo) {
	var f = document.contentForm;
	f.contentId.value = contentId;
	f.parentId.value = parentId;
	f.orderNo.value = orderNo;
	f.readType.value = "";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
}

function deleteContent() {

	if (!confirm(bbsMsg.bbs_content_delete_confirm)) {
		return;
	}
	
	var f = document.forms[0];
	f.contentSel.value = f.contentId.value;
	f.action = "/bbs/deleteContent.do";
	f.method = "post";
	f.submit();
}

function downloadFile(part)
{	
	url = "/bbs/downloadAttach.do?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
	this.location = url;
}

function downloadAttachAll() {
	var part = jQuery("#attSaveAllBtn").attr("allpart");
	url = "/bbs/downloadAllAttach.do?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
	this.location = url;
}

function toggleAttachrInfo(setValue){	
	var infoLayer = jQuery("#attachList");
	var settingValue;

	if(setValue){
		settingValue = setValue;
	} else {
		settingValue = infoLayer.css("display");		
	}

	if(settingValue == "none"){
		infoLayer.show();
	}else {
		infoLayer.hide();
	}
}

function getMessageText(){
	return $("messageText").value;
}

var contentFrameWidth;
function resizeTextFrame(height,width){
    $("messageContentFrame").style.height=height+25+"px";
    jQuery("#messageContentFrame").css({"overflow-x":"hidden","overflow-y":"hidden"});
    contentFrameWidth = width;  
    var func = function(){
            var wrapper = jQuery("#readWrapper");
            var parentWrapper = jQuery("#readWrapper").parent();
            if(parentWrapper.width() < contentFrameWidth){
                wrapper.css("width",(contentFrameWidth+30)+ "px");
            } else {            
                wrapper.css("width",(isMsie)?"":"100%");            
}
        };
        jQuery(window).bind("resize",func);
    func(); 
}

function writeMail() {
	var url = "/dynamic/mail/writeMessage.do?wmode=popup&wtype=bbs&bbsid=${contentVo.bbsId}";
	if ("${contentVo.bbsUid}" == "0") {
		url += "&bmid=${contentVo.msgId}";
	} else {
		url += "&buid=${contentVo.bbsUid}";
	}	
	
	var windowHeight = 680;
	var windowWidth = 740;
	var centerLeft = parseInt((window.screen.availWidth - windowWidth) / 2);    
	var centerTop = parseInt((window.screen.availHeight - windowHeight) / 2);
	var settings ="width="+windowWidth+"px, height="+windowHeight+"px, top="+centerTop+"px, left="+centerLeft+"px ,scrollbars=yes, resizable=no";
	var winObject = window.open("","",settings);	
	winObject.document.location = url;
}

function viewContentPopup() {
	var f = document.contentForm;
	f.contentId.value = "${contentVo.contentId}";
	f.parentId.value = "${contentVo.parentId}";
	f.orderNo.value = "${contentVo.orderNo}";
	f.readType.value = "popup";

	var windowHeight = 680;
	var windowWidth = 740;
	var centerLeft = parseInt((window.screen.availWidth - windowWidth) / 2);    
	var centerTop = parseInt((window.screen.availHeight - windowHeight) / 2);
	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width="+windowWidth+"px,height="+windowHeight+"px, top="+centerTop+"px, left="+centerLeft+"px");
	var oldTarget = f.target;
	f.target = "popupRead";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
}

function viewContentPrint() {
	var f = document.contentForm;
	f.contentId.value = "${contentVo.contentId}";
	f.parentId.value = "${contentVo.parentId}";
	f.orderNo.value = "${contentVo.orderNo}";
	f.readType.value = "print";

	var windowHeight = 680;
	var windowWidth = 740;
	var centerLeft = parseInt((window.screen.availWidth - windowWidth) / 2);    
	var centerTop = parseInt((window.screen.availHeight - windowHeight) / 2);
	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width="+windowWidth+"px,height="+windowHeight+"px, top="+centerTop+"px, left="+centerLeft+"px");
	var oldTarget = f.target;
	f.target = "popupRead";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
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

function checkMaxLength(obj, target, limit) {
	var data = obj.value;
	var dataLength = str_realLength(data);
	if (dataLength > limit) {
		alert(msgArgsReplace(bbsMsg.bbs_reply_limit,[limit]));
		obj.value = obj.value.replace(/\r\n$/, "");
		obj.value = cutMessageLength(obj.value, limit, target);
		return false;
	} else {
		jQuery("#"+target).text(dataLength);
		return true;
	}
}

function replyToggle() {
	jQuery("#replyBox").toggle();
	if (jQuery("#toggleLink").hasClass("open")) {
		jQuery("#toggleLink").removeClass("open");
		jQuery("#toggleLink").addClass("close");
		setCookie("TSBR","close",365);
	} else {
		jQuery("#toggleLink").removeClass("close");
		jQuery("#toggleLink").addClass("open");
		setCookie("TSBR","open",365);
	}
}

function getReplyMessage() {
	var url = "/bbs/viewContentReply.do";
	var param = {"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}"};
	jQuery("#replylistDiv").load(url, param);
}

function saveReplyMessage() {
	var obj = document.getElementById("replyText");
	if (!checkMaxLength(obj,'textlimit',600)) {
		return;
	}
	
	var url = "/bbs/saveContentReply.do";
	var content = jQuery("#replyText").val();
	if (content == "") {
		alert(bbsMsg.bbs_reply_content_empty);
		jQuery("#replyText").focus();
		return;
	}
	var param = {"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}","content":content};

	jQuery.post(url, param, function (data) {
		if (data.isSuccess) {
			getReplyMessage();
			jQuery("#replyText").val("");
			jQuery("#textlimit").text("0");
			
			return;
		}
		else {
			alert(bbsMsg.bbs_reply_save_fail);
			return;
		}
	}, "json");
}

function deleteReplyMessage(replyId) {
	var url = "/bbs/deleteContentReply.do";
	var param = {"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}","replyId":replyId};
	setParamPAID(param);
	if (!confirm(bbsMsg.bbs_reply_delete_confirm)) {
		return;
	}
	
	jQuery.getJSON(url, param, 
		function (data) {
			if (data.isSuccess) {
				getReplyMessage();
				return;
			}
			else {
				alert(bbsMsg.bbs_reply_delete_fail);
				return;
			}
		});
}

function moveto_page(page) {
	var url = "/bbs/viewContentReply.do";
	var param = {"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}", "currentPage":page};
	jQuery("#replylistDiv").load(url, param);
}

function setTotalReplyCount(count) {
	jQuery("#totalcnt").text(count);
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
<script language="javascript">jQuery.makeProcessBodyMask(0);</script>
<div style="clear: both;"></div>

<div id="b_mainBody">

<form name="contentForm">
<input type="hidden" name="bbsId" value="${contentVo.bbsId}">
<input type="hidden" name="contentId" value="${contentVo.contentId}">
<input type="hidden" name="parentId" value="${contentVo.parentId}">
<input type="hidden" name="depth" value="${contentVo.depth}">
<input type="hidden" name="orderNo" value="${contentVo.orderNo}">
<input type="hidden" name="isReply">
<input type="hidden" name="isModify">
<input type="hidden" name="contentSel">
<input type="hidden" name="currentPage" value="${fn:escapeXml(currentPage)}">
<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}">
<input type="hidden" name="searchType" value="${fn:escapeXml(searchType)}">
<input type="hidden" name="readType">
<script type="text/javascript">makePAID();</script>
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
			<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
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
				<div id="print" class="mail_body_navi"></div>
				<div id="popup" class="mail_body_navi"></div>	
			</div>		
			<div class="mail_body_menu">
				<div class="menu_main_unit" id="menuBarContent"></div>
			</div>
		</div>
		
		<div id="b_contentBody">
			<div id="b_contentMain">
			<div class="TM_content_wrapper" id="readWrapper">
				<div class="TM_mail_content">		
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
					<col width="50px"></col>
					<col></col>
					<col width="250px"></col>			
								
					<tr>
					<td colspan="3" class="TM_r_subject">
						<span>${contentVo.subject}</span>												
					</td>						
					</tr>					
					<tr>
					<td class="TM_rh_index">								
						<tctl:msg key="bbs.content.list.creator" bundle="bbs"/> :				
					</td>
					<td class='TM_rh_content' valign="top">
						${contentVo.creatorName}
					</td>
					<td class="TM_rh_index" align="right">														
						<tctl:msg key="bbs.content.list.contentId" bundle="bbs"/> :
						${contentVo.contentId}
						/						
						<tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/> :
						${contentVo.refCnt}
						(<tctl:msg key="bbs.content.list.createDate" bundle="bbs"/> :
						${fn:substring(contentVo.createTime,0,4)}-${fn:substring(contentVo.createTime,4,6)}-${fn:substring(contentVo.createTime,6,8)})						
					</td>					
					</tr>
				</table>
				
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_atable">
					<col></col>
					<col width="150px"></col>
					<tr>
					<td class='TM_ra_l'>
					<c:if test="${empty contentVo.attachFiles}">
						<tctl:msg key="mail.noattach" />
					</c:if>
					<c:if test="${!empty contentVo.attachFiles}">
						${fn:length(contentVo.attachFiles)} <tctl:msg key="mail.existattach" />
					</c:if>
					</td>
					<td class='TM_ra_r'>
						<c:if test="${!empty contentVo.attachFiles}">
						<a href="#n" class="btn_basic" id="attSaveAllBtn"  allpart="" onclick="downloadAttachAll()"><span><tctl:msg key="mail.saveall"/></span></a>
						<a href="#n" class="btn_basic" onclick="toggleAttachrInfo()"><span><tctl:msg key="mail.viewlist"/></span></a>
						</c:if>
						<c:if test="${empty contentVo.attachFiles}">
							&nbsp;
						</c:if>
					</td>
					</tr>
					<c:if test="${!empty contentVo.attachFiles}">
					<tr>
					<td id="attachList" class='TM_ra_c' colspan="2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
						<c:forEach var="fileData" items="${contentVo.attachFiles}" varStatus="loop">				
							<c:forTokens var="file" items="${fileData.fileName}" delims=".">
								<c:set var="fileType" value="${fn:toLowerCase(file)}"/>								 
							</c:forTokens>
							<c:choose>
					   			<c:when test="${fileType=='doc' || 	fileType=='docx'|| 	fileType=='gif' || 
										fileType=='pdf' || 	fileType=='html'|| 	fileType=='hwp' || 
										fileType=='jpg' || 	fileType=='bmp' ||	fileType=='ppt' || 
										fileType=='pptx'|| 	fileType=='txt' || 	fileType=='xls' || 
										fileType=='xlsx'|| 	fileType=='zip' || 	fileType=='xml' ||
										fileType=='mpeg'||	fileType=='avi' || 	fileType=='htm' ||
										fileType=='mp3' ||	fileType=='mp4' ||	fileType=='eml'}">							   				
					   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
					   			</c:when>								   			
					   			<c:otherwise>
					   				<c:set var="attachImgName" value="ic_att_unknown"/>						   				
					   			</c:otherwise>
				   			</c:choose>
				   			
				   			<c:if test="${fileData.size75 > 0 }">
			                  	<a href="#n" onclick="downloadFile('${fileData.path}')" class="rdown" title="${fileData.fileName}">						
				   				<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp;
				   				${fileData.fileName} <span id="attachSizeL_${loop.count}"></span>
				   				</a>
				   				<script language=javascript>
								$('attachSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${fileData.size75* 0.964981} ) ) +']';					
								jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${fileData.path}_");				
				   		    	</script>						   		    		   		    
				   		    </c:if>
				   		    <br>
						</c:forEach>
					</td>
					</tr>
					</c:if>							
				</table>
				
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_ctable">
					<tr><td class="TM_r_content">
					<textarea id="messageText" style="display:none;">
						<c:out value="${contentVo.htmlContent}" escapeXml="true"/>
					</textarea>			
					<iframe frameborder="0" width="100%" height="300px" scrolling='no'  id="messageContentFrame"></iframe>
					</td></tr>
				</table>						
					
				
				<div style="margin-top:10px">
					<div style="margin-bottom:5px;">
					<a id="toggleLink" href="javascript:;" onclick="replyToggle()" class="replytitle open">
						<tctl:msg key="bbs.reply.title" bundle="bbs"/>(<span id="totalcnt">0</span>)
					</a>
					</div>					
					<div id="replyBox" class="TM_reply_body">
						<div id="replylistDiv"></div>				

						<table id="replyWriteTable">
							<col></col>
							<col width="80px"></col>
							<tr>
							<td style="border:medium none">
								<textarea name="replyText" id="replyText" onkeyup="checkMaxLength(this,'textlimit',600)" style="background:#F0F0F2;width:99%"></textarea>
							</td>
							<td align="center" nowrap>
								<a class="btn_add" onclick="saveReplyMessage()" href="javascript:;">
									<span><tctl:msg key="bbs.save" bundle="bbs"/></span>
								</a><br/>
							</td>
							</tr>
							<tr>
							<td class="txtcnt">
								<span class="limit"><span class="point" id="textlimit">0</span> / 600bytes</span>
							</td>
							</tr>
						</table>
					</div>
				</div>
				
				<c:set var="preContentId" value="0"/>
				<c:set var="preParentId" value="0"/>	
				<c:set var="preOrderNo" value="0"/>	
				<c:set var="nextContentId" value="0"/>
				<c:set var="nextParentId" value="0"/>	
				<c:set var="nextOrderNo" value="0"/>
				<c:if test="${!empty nextContent || !empty prevContent}">		
					<table id="bbsListTable" cellpadding="0" cellspacing="0" class="TM_readOtherTable">
						<colgroup span="7">
							<col width="30px"></col>
							<col width="50px"></col>
							<col width="30px"></col>
							<col></col>
							<col width="100px"></col>
							<col width="100px"></col>
							<col width="70px"></col>
						</colgroup>	
					<c:if test="${!empty nextContent}">
					<c:forEach var="next" items="${nextContent}">
						<tr>
							<td class="bbsOtherTd">▲</td>
							<td class="bbsOtherTd">${next.contentId}</td>
							<td class="bbsOtherTd">
								<c:if test="${next.attCnt > 0}">
									<img src="/design/default/image/icon/ic_attach_file.gif" border="0" align="absmiddle">
								</c:if>
							</td>
							<td class="bbsOtherTd subject">
								<c:if test="${next.depth != 0}">
									<c:forEach begin="0" end="${next.depth}" varStatus="loop">
										<c:if test="${loop.count <= 10}">&nbsp;</c:if>
									</c:forEach>
									<img src="/design/common/image/icon_bbs_reply.gif" border="0">
								</c:if>
								<a href="javascript:viewContent('${next.contentId}','${next.parentId}','${next.orderNo}')">${next.subject}</a>
							</td>
							<td class="bbsOtherTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${next.creatorName}">
											<div class='TM_HiddenTextDiv'>${next.creatorName}</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsOtherTd">${fn:substring(next.createTime,0,4)}-${fn:substring(next.createTime,4,6)}-${fn:substring(next.createTime,6,8)}</td>
							<td class="bbsOtherTd">${next.refCnt}</td>
						</tr>
						<c:set var="nextContentId" value="${next.contentId}"/>
						<c:set var="nextParentId" value="${next.parentId}"/>	
						<c:set var="nextOrderNo" value="${next.orderNo}"/>		
					</c:forEach>	
					</c:if>
					<c:if test="${!empty prevContent}">
					<c:forEach var="prev" items="${prevContent}">
						<tr>
							<td class="bbsOtherTd">▼</td>
							<td class="bbsOtherTd">${prev.contentId}</td>
							<td class="bbsOtherTd">
								<c:if test="${prev.attCnt > 0}">
									<img src="/design/default/image/icon/ic_attach_file.gif" border="0" align="absmiddle">
								</c:if>
							</td>
							<td class="bbsOtherTd subject">
								<c:if test="${prev.depth != 0}">
									<c:forEach begin="0" end="${prev.depth}" varStatus="loop">
										<c:if test="${loop.count <= 10}">&nbsp;</c:if>
									</c:forEach>
									<img src="/design/common/image/icon_bbs_reply.gif" border="0">
								</c:if>
								<a href="javascript:viewContent('${prev.contentId}','${prev.parentId}','${prev.orderNo}')">${prev.subject}</a>
							</td>
							<td class="bbsOtherTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${prev.creatorName}">
											<div class='TM_HiddenTextDiv'>${prev.creatorName}</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsOtherTd">${fn:substring(prev.createTime,0,4)}-${fn:substring(prev.createTime,4,6)}-${fn:substring(prev.createTime,6,8)}</td>
							<td class="bbsOtherTd">${prev.refCnt}</td>
						</tr>
						<c:set var="preContentId" value="${prev.contentId}"/>
						<c:set var="preParentId" value="${prev.parentId}"/>	
						<c:set var="preOrderNo" value="${prev.orderNo}"/>		
					</c:forEach>	
					</c:if>
					</table>
				</c:if>
				</div>
			</div>				
			</div>
			<div id="b_contentSub" ></div>			
		</div>
		</div>
	</div>
</form>
</div>
<script language="javascript">
var menuBar;
loadToolBar();
menuBar.setPageNavi("b",{
preContentId:'${nextContentId}',preParentId:'${nextParentId}',preOrderNo:'${nextOrderNo}',
nextContentId:'${preContentId}',nextParentId:'${preParentId}',nextOrderNo:'${preOrderNo}',
isListSet:true
});
</script>
<%@include file="/common/bottom.jsp"%>
</body>
</html>