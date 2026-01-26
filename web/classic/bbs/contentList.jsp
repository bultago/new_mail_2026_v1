<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/bbs_style.css" />
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/bbs/bbsListMenuBar.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.js"></script>
<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>
<script language="javascript">

var menuBar;
function init(){	
	setTopMenu('bbs');

	loadToolBar();
	if(skin != "skin3"){
		checkMenubar();
	}else{
		checkMenubarSkin3();
	}
	
	var f = document.contentListForm;	
	if ("${fn:escapeXml(searchType)}" != "7") {
		f.inputKeyWord.value = f.keyWord.value;
	}

	var mainLayerPane = new LayerPane("b_mainBody","TM_m_mainBody");	
	var menuLayerPane = new LayerPane("b_leftMenuContent","TM_m_leftMenu",220,220,350);
	var contentLayerPaneWapper = new LayerPane("b_contentBodyWapper","",300,100,500);

	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();
	
	mainSplitter = new SplitterManager(mainLayerPane,
										menuLayerPane,
										contentLayerPaneWapper,
										"sm","mainvsplitbar","hsplitbar");	
	mainSplitter.setReferencePane(["b_contentBody","copyRight"]);	
	mainSplitter.setSplitter("v",true);
	jQuery(window).autoResize(jQuery("#b_mainBody"),"#copyRight");

	

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
	
	/*menuBar.setPageNavi("p",
						{total:'${totalCount}',
						base:USER_PAGEBASE,
						page:'${currentPage}',
						isListSet:false,
						isLineCntSet:true,
						pagebase:USER_PAGEBASE,
						changeAfter:reloadListPage});*/

	menuBar.setPageNaviBottom("p",
			{total:'${totalCount}',
			base:USER_PAGEBASE,
			page:'${currentPage}',
			isListSet:false,
			isLineCntSet:true,
			pagebase:USER_PAGEBASE,
			changeAfter:reloadListPage});

	setCurrentPage('${currentPage}');
	
	jQuery("#bbsLeftMenu").treeview({cookieId:"TBST",persist:"cookie"});
	jQuery("#bbs_select_${boardVO.bbsId} a").addClass("menu_selected");
	setTitle();

	jQuery(window).resizeInnerFrame({resizeId:"#b_bbsListWrapper", 
		mainId:"#b_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:false,
		isMainHeight:true,
		extHeight:57});
	jQuery(window).resizeInnerFrame({resizeId:"#b_bbsListContent", 
		mainId:"#b_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		notCheckTrigger:false,
		isMainHeight:true,
		extHeight:57});	
	jQuery("#searchTypeSelect").selectbox({selectId:"inputSearchType",
		selectFunc:""},
		"${fn:escapeXml(searchType)}",
		[{index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/>",value:"s"},
		 {index:"<tctl:msg key="bbs.content.list.content" bundle="bbs"/>",value:"b"},
		 {index:"<tctl:msg key="bbs.content.list.creator" bundle="bbs"/>",value:"c"},
		 {index:"<tctl:msg key="bbs.content.list.attach" bundle="bbs"/>",value:"af"},
		 {index:"<tctl:msg key="bbs.content.list.subject" bundle="bbs"/> + <tctl:msg key="bbs.content.list.content" bundle="bbs"/>",value:"sb"}]);

	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");	
	
	if ("${boardVO.bbsType}" == "secret") {
		jQuery(".TM_finfo_search").hide();
	}	
	
	setSearchStatus('${fn:escapeXml(keyWord)}');
}
function setSearchStatus(keyWord) {
    if (keyWord != "") jQuery("#bbsSearchCancel").show();
}
function cancelSearch(){
	location.href = "/bbs/listContent.do?bbsId=${boardVO.bbsId}";
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
	
	<c:choose>
		<c:when test="${bbsAdmin == 'true' && boardVO.writeAuth == 'ADMIN'}">
			jQuery("#basic_0").show();
		</c:when>
		<c:otherwise>
			<c:if test="${boardVO.writeAuth == 'LOGIN' || boardVO.writeAuth == 'ALL'}">
				jQuery("#basic_0").show();
			</c:if>
		</c:otherwise>
	</c:choose>
	
	<c:if test="${bbsAdmin != 'true'}">
		jQuery("#deleteBt_item").hide();
	</c:if>
}
function checkMenubarSkin3() {
	menuBar.hideMenu("writeBt");
	menuBar.hideMenu("deleteBt");
	
	<c:choose>
		<c:when test="${bbsAdmin == 'true'  && boardVO.writeAuth == 'ADMIN'}">	
			menuBar.showMenu("writeBt");
			menuBar.showMenu("deleteBt");
		</c:when>
		<c:otherwise>
			<c:if test="${boardVO.writeAuth == 'LOGIN' || boardVO.writeAuth == 'ALL'}">
				menuBar.showMenu("writeBt");
				menuBar.showMenu("deleteBt");
			</c:if>
		</c:otherwise>
	</c:choose>	
	
	<c:if test="${bbsAdmin != 'true'}">
		menuBar.hideMenu("deleteBt");		
	</c:if>
}

function movePage(num) {
	var f = document.contentListForm;
	f.currentPage.value = num;
	f.action = "/bbs/listContent.do";
	f.method = "post";
	f.submit();
}

function reloadListPage(){
	var f = document.contentListForm;
	//f.currentPage.value = '${currentPage}';
	f.currentPage.value = '1';
	f.action = "/bbs/listContent.do";
	f.method = "post";
	f.submit();
}

function viewContent(contentId,parentId,orderNo) {
	var f = document.contentListForm;
	f.contentId.value = contentId;
	f.parentId.value = parentId;
	f.orderNo.value = orderNo;
	f.readType.value = "";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
}

function viewContentPopup(contentId,parentId,orderNo) {
	var f = document.contentListForm;
	f.contentId.value = contentId;
	f.parentId.value = parentId;
	f.orderNo.value = orderNo;
	f.readType.value = "popup";

	var popWin = window.open("about:blank","popupRead","scrollbars=yes,width=740,height=640");
	var oldTarget = f.target;
	f.target = "popupRead";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
}

function previewContent(contentId,parentId,orderNo) {

	closePreview();
	
	var f = document.contentListForm;
	f.contentId.value = contentId;
	f.parentId.value = parentId;
	f.orderNo.value = orderNo;
	f.readType.value = "preview";

	var preIframe = jQuery("<iframe name='preContent' frameborder='0' width='100%' height='300px' scrolling='yes'></iframe>");
	jQuery("#bbs_preview_"+contentId).append(preIframe);
	jQuery("#bbs_preview_"+contentId).show();
	
	var oldTarget = f.target;
	f.target = "preContent";
	f.action = "/bbs/viewContent.do";
	f.method = "post";
	f.submit();
	f.target = oldTarget;
}

function closePreview() {
	jQuery(".previewTd").empty();
	jQuery(".previewTd").hide();
}
//////////////////////////////////
function modifyContent() {
	var f = document.forms[0];
	var cnt = checkedCnt(f.contentSel);
	if (cnt == 0) {
		alert(bbsMsg.bbs_alert_modify_noselect);
		return;
	}
	else if (cnt > 1) {
		alert(bbsMsg.bbs_alert_modify_overselect);
		return;
	}
	else {
		var index=0;
		if (f.contentSel.length) {
			for (i=0; i<f.contentSel.length; i++) {
				if (f.contentSel[i].checked) {
					index = i;
					break;
				}
			}
			f.contentId.value = f.contentSel[index].value;
		} 
		else {
			f.contentId.value=f.contentSel.value;
		}
	}
	f.isModify.value="true";
	f.method = "post";
	f.action = "/bbs/modifyContent.do";
	f.submit();
}

function deleteContent() {
	var f = document.forms[0];

	var cnt = checkedCnt(f.contentSel);
	if (cnt == 0) {
		alert(bbsMsg.bbs_alert_delete_noselect);
		return;
	}

	if (!confirm(bbsMsg.bbs_content_delete_confirm)) {
		return;
	}
	
	f.action = "/bbs/deleteContent.do";
	f.method = "post";
	f.submit();
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
	var treeLink = jQuery("<a href='/bbs/listContent.do?bbsId="+bbsId+"'>"+escape_tag(title)+"</a>");
	treeUl.css("display","none");
	treeSpan.addClass("icon_bbs");
	treeSpan.addClass(typeCss);
	treeSpan.append(treeLink);
	treeLi.append(treeSpan);
	treeUl.append(treeLi);
	tree.append(treeUl);
}

function setTitle() {
	var fullId = '${boardVO.bbsFullId}';
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
onloadRedy("init()");
//////////////////////////////////

</script>
</head>
<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="b_mainBody">

<form name="contentListForm">
<input type="hidden" name="currentPage" value="${pm.page}">
<input type="hidden" name="bbsId" value="${boardVO.bbsId}">
<input type="hidden" name="contentId">
<input type="hidden" name="parentId">
<input type="hidden" name="orderNo">
<input type="hidden" name="isModify">
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
				<img src="/design/common/image/blank.gif" class="TM_barLeft">			
				<div class="TM_finfo_data"><span class="TM_work_title" id="workTitle"></span></div>
				<div class="TM_finfo_search">
					<div class="TM_mainSearch">							
					<div style="float: left">					
						<div id="searchTypeSelect"></div>
					</div>
					<div style="float: left;padding-left:3px;">
						<input type="text" name="_tmp" style="display:none">
						<input type="text" class="searchBox"  name="inputKeyWord" id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchContent() : '';" /><a href="#" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="bbsSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchContent()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a> <a href="#" onclick="searchMine()" class="TM_search_btn"><span><tctl:msg key="bbs.content.list.mine" bundle="bbs"/></span></a>
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

			<div id="b_contentBody" >
				<div id="b_contentMain" >
					<div class="TM_listWrapper">
					<table class="TM_bbsListHeader" cellpadding="0" cellspacing="0">
						<colgroup span="8">
							<col width="30px"></col>
							<col width="60px"></col>
							<col width="30px"></col>
							<col></col>
							<col width="20px"></col>
							<col width="100px"></col>
							<col width="100px"></col>
							<col width="70px"></col>
						</colgroup>
						<tr>
							<th scope="col">
								<input type="checkbox" name="conAll" onclick="checkAllTdLine(contentListForm.contentSel, this.checked)">
							</th>		
							<th scope="col"><tctl:msg key="bbs.content.list.contentId" bundle="bbs"/></th>
							<th scope="col">&nbsp;</th>
							<th scope="col"><tctl:msg key="bbs.content.list.subject" bundle="bbs"/></th>
							<th scope="col"></th>
							<th scope="col"><tctl:msg key="bbs.content.list.creator" bundle="bbs"/></th>
							<th scope="col"><tctl:msg key="bbs.content.list.createDate" bundle="bbs"/></th>
							<th scope="col"><tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/></th>
						</tr>
					</table>
					
					<div id='b_bbsListWrapper'>
					<div id='b_bbsListContent' class='TM_listWrapper'>
					<table id="bbsListTable" cellpadding="0" cellspacing="0">
						<colgroup span="8">
							<col width="30px"></col>
							<col width="60px"></col>
							<col width="20px"></col>
							<col></col>
							<col width="20px"></col>
							<col width="100px"></col>
							<col width="100px"></col>
							<col width="70px"></col>
						</colgroup>
						<c:forEach var="contentnotice" items="${contentnoticeList}">
						<tr class="noticeTr">
							<td class="bbsTd">&nbsp;</td>
							<td class="bbsTd"><span class="btn_news"><span><tctl:msg key="bbs.notice" bundle="bbs"/></span></span></td>
							<td class="bbsTd">
								<c:if test="${contentnotice.attCnt > 0}">
									<img src="/design/default/image/icon/ic_attach_file.gif" border="0" align="absmiddle">
								</c:if>
							</td>
							<td class="bbsTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${contentnotice.subject}">
											<div class='TM_HiddenTextDiv' style="font-weight:bold">
												<a href="#" onclick="viewContent('${contentnotice.contentId}','${contentnotice.parentId}','${contentnotice.orderNo}')">${contentnotice.subject}</a>
											</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsTd"><a href="javascript:;" onclick="viewContentPopup('${contentnotice.contentId}','${contentnotice.parentId}','${contentnotice.orderNo}')"><img src="/design/default/image/icon/ic_popup.gif"></a></td>
							<td class="bbsTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${contentnotice.creatorName}">
											<div class='TM_HiddenTextDiv' style="text-align:center">${contentnotice.creatorName}</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsTd">${fn:substring(contentnotice.createTime,0,4)}-${fn:substring(contentnotice.createTime,4,6)}-${fn:substring(contentnotice.createTime,6,8)}</td>
							<td class="bbsTd">${contentnotice.refCnt}</td>
						</tr>	
						</c:forEach>
						<c:if test="${!empty contentList}">
						<c:forEach var="content" items="${contentList}">
						<tr>
							<td class="bbsTd">
									<input type="checkbox" name="contentSel" value="${content.contentId}" onclick="checkTdLine(this)">
							</td>
							<td class="bbsTd">${content.contentId}</td>
							<td class="bbsTd">
								<c:if test="${content.attCnt > 0}">
									<img src="/design/default/image/icon/ic_attach_file.gif" border="0" align="absmiddle">
								</c:if>
							</td>
							<td class="bbsTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${content.subject}">
											<div class='TM_HiddenTextDiv'>
												<c:if test="${(empty keyWord) && content.depth > 0}">
													<c:forEach begin="0" end="${content.depth}" varStatus="loop">
														<c:if test="${loop.count <= 10}">&nbsp;</c:if>
													</c:forEach>
													<img src="/design/default/image/icon_bbs_reply.gif" border="0">
												</c:if>
												<a href="#" onclick="viewContent('${content.contentId}','${content.parentId}','${content.orderNo}')">${content.subject}</a>
											</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsTd">
								<%--<a href="javascript:;" onclick="previewContent('${content.contentId}','${content.parentId}','${content.orderNo}')">[<tctl:msg key="bbs.preview" bundle="bbs"/>]</a>--%>
								<a href="javascript:;" onclick="viewContentPopup('${content.contentId}','${content.parentId}','${content.orderNo}')"><img src="/design/default/image/icon/ic_popup.gif" align="absmiddle"></a>
							</td>
							<td class="bbsTd">
								<table class='TM_HiddenTextTable'>
									<tr>
										<td style="border:0;" title="${content.creatorName}">
											<div class='TM_HiddenTextDiv' style="text-align:center">${content.creatorName}</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="bbsTd">${fn:substring(content.createTime,0,4)}-${fn:substring(content.createTime,4,6)}-${fn:substring(content.createTime,6,8)}</td>
							<td class="bbsTd">${content.refCnt}</td>
						</tr>
						<tr>
							<td id="bbs_preview_${content.contentId}" class="previewTd" colspan="8" style="display:none"></td>
						</tr>	
						</c:forEach>
						</c:if>
						<c:if test="${empty contentList}">
						<tr>
							<td colspan="8" class="bbsTd"><tctl:msg key="bbs.content.empty" bundle="bbs"/></td>
						</tr>
						</c:if>
					</table>
					</div>
					</div>
					<c:if test="${!empty contentList}">
					<div id='pageBottomNavi' class='pageNavi'></div>
					</c:if>
					</div>
				</div>
				<div id="b_contentSub" ></div>
			</div>
			</div>
		</div>	
</form>
</div>

<%@include file="/common/bottom.jsp"%>
</body>
</html>