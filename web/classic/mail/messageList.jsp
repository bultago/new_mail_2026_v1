<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />
<script type="text/javascript">
    RENDERMODE = "html";
</script>
<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>

<!-- DWR 제거 및 REST API로 전환 (2025-10-21) -->
<script type="text/javascript" src="/resources/js/api-utils.js"></script>
<script type="text/javascript" src="/resources/js/mail-api.js"></script>

<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailBasicCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>
<script type="text/javascript" src="/js/ocx/ocx_localMail.js"></script>
<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<style type="text/css">html {*overflow-y:scroll;}</style>
<script type="text/javascript">
setAlMailMode(true);
<%@include file="userSetting.jsp"%>

paneMode = "n";
var tagArray = [];
var priviewList = [];
LayoutInfo.mode = "list";
mainInitFunc();
var listMsgData = {
	listType:'${listType}',
	folder:'${folderFullName}',
	flag:'${fn:escapeXml(flag)}',
	page:'${fn:escapeXml(page)}',
	sortBy:'${fn:escapeXml(sortBy)}',
	sortDir:'${fn:escapeXml(sortDir)}',
	keyWord:'${fn:escapeXml(keyWord)}',
	adv:'${fn:escapeXml(advancedSearch)}',
	category:'${fn:escapeXml(category)}',
	fromaddr:'${fn:escapeXml(fromaddr)}',
	toaddr:'${fn:escapeXml(toaddr)}',
	sharedFlag:'${fn:escapeXml(sharedFlag)}',
	sharedUserSeq:'${fn:escapeXml(sharedUserSeq)}',
	sharedFolderName:'${fn:escapeXml(sharedFolderName)}',
	tagId:'${tagId}'
};
function init() {

	setTopMenu('mail');

	if(!menuBar || menuBar.getMode() != "list"){
		loadListToolBar();
	}
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}
	debugInfo("LIST_DROW_END",true);
	if(PDEBUGLOGGING){
		//setTimeout("loggingData('MAIL_LIST')", 100);
	}
	var mailInfo = '${mailInfo}'.evalJSON();

	var sharedParam  = {};
	var sharedFlag = jQuery("#sharedFlag").val();
	sharedParam.isShared = (sharedFlag == 'shared') ? true : false;
	sharedParam.sharedFlag = jQuery("#sharedFlag").val();
	sharedParam.sharedUserSeq = jQuery("#sharedUserSeq").val();
	sharedParam.sharedFolderName = jQuery("#sharedFolderName").val();

	var searchParam = {};
	searchParam.keyWord = jQuery("#keyWord").val();
	searchParam.adv = jQuery("#adv").val();
	searchParam.fromaddr = jQuery("#fromaddr").val();
	searchParam.toaddr = jQuery("#toaddr").val();
	searchParam.category = jQuery("#category").val();
	searchParam.sdate = jQuery("#sdate").val();
	searchParam.edate = jQuery("#edate").val();

	settingMailListEvent();
	
	loadHtmlMessageListPage(mailInfo, tagArray, sharedParam, searchParam);

	mailControl.setListParam(listMsgData);
	setSearchStatus(jQuery("#keyWord").val(), jQuery("#adv").val());

	folderInitLoad(settingTagAndFolder);
	
	jQuery(window).autoResize(jQuery(".TM_mainContent"), "#copyRight");

	if(isMsie){
		setTimeout(function(){
			jQuery("html").scrollTop(getStoreScrollHeight());	
		}, 100);
	}else{
		setTimeout(function(){
			jQuery("body").scrollTop(getStoreScrollHeight());	
		}, 50);
	}
	
}

function settingTagAndFolder() {
	if(currentFolderType != "shared"){
		updateTag(tagArray, showTagMenu);
	}
	
	var folderList = getDropFolderList();
	if(!dndManager)dndManager = new DnDManager(dndOption);
	if(currentFolderType != "reserved" &&  currentFolderType != "shared"){
		dndManager.applyDrop(folderList,"table#m_messageList tr",dropFunc);
	}  else {
		dndManager.destroyDrop(folderList);
	}
}
function settingMailListEvent() {
	var folderType = "${folderType}";
	var isExceptFolder = (folderType == 'sent' || folderType == 'draft' || folderType == 'reserved')?true:false;
	var $evtParent = jQuery("#m_messageList");
	
	$evtParent.bind('click', function(e) {
		
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();

		if (nodeName == "input") {
			if (target.name == "msgId") {
				checkMessage(target);
			}
		} else if (nodeName == "span") {
			var type = $target.attr("type");
			if (type == "flag") {
				var mid = $target.attr("mid");
				switchFlagFlaged([mid],'F',mid+"_flagedF");
			}
		} else if (nodeName == "td") {
			/*
			if ($target.hasClass("TM_list_subject")) {
				var folderName = $target.attr("fname");
				var uid = $target.attr("uid");
				var idx = $target.attr("idx");
				removePreview();
				if (folderType == "draft") {
					writeDraftMessage(folderName,uid,idx);
				} else {
					readMessage(folderName,uid,idx);
				}
			}
			*/
		} else if (nodeName == "img") {
			var $parent = $target.parent();
			var type = $parent.attr("type");
			if (type == "popup") {
				var folderName = $parent.attr("fname");
				var uid = $parent.attr("uid");
				var idx = $parent.attr("idx");
				readPopMessage(folderName,uid,idx);
			}
		} else if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "addr") {
				var idx = $target.attr("idx");
				var idxName = "from_"+idx;
				if (isExceptFolder) {
					idxName = "to_"+idx;
				}
				makeAddrSubMenu(idxName, target);
			}
			else if (type == "subject") {
				var $parent = $target.parent();
				var folderName = $parent.attr("fname");
				var uid = $parent.attr("uid");
				var idx = $parent.attr("idx");
				removePreview();
				if (folderType == "draft") {
					writeDraftMessage(folderName,uid,idx);
				} else {
					readMessage(folderName,uid,idx);
				}
			}
		}
	});
	
	$evtParent.bind('mouseover', function(e) {
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();
		var dragId;
		if (nodeName == "td") {
			dragId = $target.closest('tr').attr("id");
		} 
		else {
			dragId = $target.parent("td.TM_list_subject").closest('tr').attr("id");
		}
		
		if (dragId) {
			makeDrag(dragId);
		}
		
		if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "subject") {
				var mtr_id = $target.attr("mtr_id");
				var privIdx = $target.attr("privIdx");
				overPriview("msg_subject_"+mtr_id,privIdx);
			}
		}
	});
	
	$evtParent.bind('mouseout', function(e) {
		var target = e.target;
		var $target = jQuery(target);
		var nodeName = target.nodeName.toLowerCase();
		
		if (nodeName == "a") {
			var type = $target.attr("type");
			if (type == "subject") {
				outPriview();
			}
		}
	});
}

</script>

</head>
<body>
	<%@include file="/common/topmenu.jsp"%>
	<table cellpadding="0" cellspacing="0" class="TM_html_m_table">
		<tr>
			<td class="TM_m_leftMenu" style="width:220px;vertical-align:top;">
				<%@include file="mailLeftCommon.jsp"%>
				<%@include file="sideMenu.jsp"%>
			</td>
			<td class="mainvsplitbar"></td>
			<td class="TM_main_hframe" >
				<div class="posr">
				<div style="z-index:101;position: absolute; right:10px;">
					<div id="adSearchBox"  class="popup_style2 TM_advsearch_box">
						<form id="adsearchForm">
						<div class="title">
							<span><tctl:msg key="mail.adsearch"/></span>
							<a class="btn_X" href="javascript:;" onclick="hiddenAdsearchBox()"><tctl:msg bundle="common"  key="comn.close"/></a>
						</div>
						
						<table cellpadding="0" cellspacing="0" style='width:100%' class="jq_innerTable">
							<col width="150px"></col>
							<col></col>
							<tr>
								<th class="lbout"><tctl:msg key="mail.from"/></th>
								<td><input type="text" id="adFrom" class="IP300" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/></td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.to"/> (<tctl:msg key="mail.sfolder.include.001"/>)</th>
								<td><input type="text" id="adTo" class="IP300" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/></td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.sword"/></th>
								<td>
									<div style="float: left;"><div id="adConditionSelect"></div></div>
									<div style="float: left;margin-left:3px;">
									<input type="text" id="sdkeyWord" class="IP100px" style="width:145px;" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/>
									</div>
								</td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.folder"/></th>
								<td>
									<div style="float: left;"><div id="adSfolderSelect"></div></div>
									<div style="float: left; margin-left:3px;">
										<div id="sAllfolderDesc"></div>
									</div>
								</td>
							</tr>												
							<tr>
								<th class="lbout"><tctl:msg key="mail.searchperiod"/></th>
								<td>
									<input type="text" class="IP100px"  id="adStartDate" readonly="readonly" style="width:140px;"/>
									<span>~</span>
									<input type="text" class="IP100px"  id="adEndDate"  readonly="readonly" style="width:140px;"/>
								</td>					
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.sfolder.add.condition"/></th>
								<td>
									<label><input type="radio" name="searchFolderFlag" value="" checked/> <tctl:msg key="mail.mdn.notselect"/></label>
									<label><input type="radio" name="searchFolderFlag" value="F"/> <tctl:msg key="menu.quick.flag"/></label>
									<label><input type="radio" name="searchFolderFlag" value="T"/> <tctl:msg key="menu.quick.attach"/></label>
									<label><input type="radio" name="searchFolderFlag" value="S"/> <tctl:msg key="menu.quick.read"/></label>
									<label><input type="radio" name="searchFolderFlag" value="U"/> <tctl:msg key="menu.quick.unread"/></label>
								</td>
							</tr>
						</table>				
						<div class="btnArea">
							<a class="btn_style3" href="javascript:;" onclick="adSearchMessage();"><span><tctl:msg key="mail.search"/></span></a>
						</div>
						</form>
					</div>
				</div>
				<form name="listForm" id="listForm">
					<input type="hidden" id="allSearchSelectCheck" name="allSearchSelectCheck" value="off"/>
					<input type="hidden" id="folderName" name="folderName" value="${folderFullName}"/>
					<input type="hidden" id="flag" name="flag" value="${fn:escapeXml(flag)}"/>
					<input type="hidden" id="page" name="page" value="${page}"/>
					<input type="hidden" id="sortBy" name="sortBy" value="${fn:escapeXml(sortBy)}"/>
					<input type="hidden" id="sortDir" name="sortDir" value="${fn:escapeXml(sortDir)}"/>
					<input type="hidden" id="keyWord" name="keyWord" value="${fn:escapeXml(keyWord)}"/>
					<input type="hidden" id="adv" name="adv" value="${fn:escapeXml(advancedSearch)}"/>
					<input type="hidden" id="category" name="category" value="${fn:escapeXml(category)}"/>
					<input type="hidden" id="fromaddr" name="fromaddr" value="${fn:escapeXml(fromaddr)}"/>
					<input type="hidden" id="toaddr" name="toaddr" value="${fn:escapeXml(toaddr)}"/>
					<input type="hidden" id="sdate" name="sdate" value="${fn:escapeXml(sdate)}"/>
					<input type="hidden" id="edate" name="edate" value="${fn:escapeXml(edate)}"/>
					<input type="hidden" id="sharedFlag" name="sharedFlag" value="${fn:escapeXml(sharedFlag)}"/>
					<input type="hidden" id="sharedUserSeq" name="sharedUserSeq" value="${fn:escapeXml(sharedUserSeq)}"/>
					<input type="hidden" id="sharedFolderName" name="sharedFolderName" value="${fn:escapeXml(sharedFolderName)}"/>
					
					<div class="TM_folderInfo posr">				
						<img class="TM_barLeft" src="/design/common/image/blank.gif">								
						<div class="TM_finfo_data" id="workTitle"></div>
						<div class="TM_finfo_search" id="siSearchBox">
							<div class="TM_mainSearch">
								<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" /><a href="javascript:;" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="mailSearchCancel" class="TM_search_cancel"></a><a href="javascript:;" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a><a href="javascript:;" onclick="showAdsearchBox()" id="adSearchBoxBtn" class="TM_advsearch_btn"><span><tctl:msg key="mail.adsearch"/></span></a>
								<input type="text" name="_tmp" style="width:0px;height:0px;display:none"/>
							</div>	
						</div>						
						<img class="TM_barRight" src="/design/common/image/blank.gif">
					</div>
					<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
					<div class="TM_mainContent">
					<div id="mailMenubar">			
						<div class="mail_body_tabmenu posr">
							<div class="mail_body_tab" id="menuBarTab"></div>
							<div>
								<div id="processMessageContent" class="TM_processMessage"></div>
							</div>
							<div id="pageNavi"  class="mail_body_navi" style="padding-right:5px">				
							</div>
							<div id="reload" class="mail_body_navi">
							</div>
							<div id="print" class="mail_body_navi">
							</div>
							<div id="localMail" class="mail_body_navi">
							</div>
							<div id="autoSaveArea" class="mail_body_navi" style='padding-right:5px'>
							</div>	
						</div>	
						<div class="mail_body_menu">
							<div class="menu_main_unit" id="menuBarContent"></div>
						</div>
					</div>
					<table id="msgListHeader" class="TM_b_list">	
					<c:set var="colSize" value="7"/>
					<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">		
						<col width="40px"></col>
						<col width="55px" ></col>
						<c:if test="${folderType eq 'all'}">
						<c:set var="colSize" value="8"/>
						<col width="90px" ></col>
						</c:if>
						<col width="80px"></col>
						<col ></col>	
						<col width="20px"></col>
						<col width="110px"></col>
						<col width="70px"></col>
						
						<tr>
							<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
							<th scope="col"><tctl:msg key="mail.kind"/></th>
							<c:if test="${folderType eq 'all'}">
							<th scope="col"><tctl:msg key="mail.folder"/></th>
							</c:if>
							<th scope="col">		
								<c:if test="${sortBy eq 'from'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;"  onclick="sortMessage('from','desc')" class="sortitem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>			
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('from','asce')" class="sortitem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'from'}">
									<a href="javascript:;" onclick="sortMessage('from','desc')">
								</c:if>		
								<tctl:msg key="mail.from"/></a>		
							</th>
							<th scope="col">
								<c:if test="${sortBy eq 'subj'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;" onclick="sortMessage('subj','desc')" class="sortitem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('subj','asce')" class="sortitem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'subj'}">
									<a href="javascript:;" onclick="sortMessage('subj','desc')">
								</c:if>
								<tctl:msg key="mail.subject"/></a>
							</th>
							<th scope="col">&nbsp;</th>
					</c:if>
	
					<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
						<col width="40px"></col>
						<col width="55px" ></col>	
						<col ></col>
						<col width="10px"></col>
						<col width="80px"></col>	
						<col width="110px"></col>
						<col width="70px"></col>
						
						<tr>
							<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
							<th scope="col"><tctl:msg key="mail.kind"/></th>
							<th scope="col">
								<c:if test="${sortBy eq 'subj'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;" onclick="sortMessage('subj','desc')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('subj','asce')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'subj'}">
									<a href="javascript:;" onclick="sortMessage('subj','desc')">
								</c:if>
								<tctl:msg key="mail.subject"/></a>
							</th>
							<th>&nbsp;</th>
							<th scope="col">		
								<c:if test="${sortBy eq 'to'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;"  onclick="sortMessage('to','desc')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>			
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('to','asce')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'to'}">
									<a href="javascript:;" onclick="sortMessage('to','desc')">
								</c:if>		
								<tctl:msg key="mail.to"/></a>		
							</th>	
					</c:if>
							<th scope="col">
								<c:if test="${sortBy eq 'arrival'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;" onclick="sortMessage('arrival','desc')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('arrival','asce')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'arrival'}">
									<a href="javascript:;" onclick="sortMessage('arrival','desc')">
								</c:if>
								<c:if test="${folderType eq 'sent'}">
									<tctl:msg key="mail.senddate"/>
								</c:if> 
								<c:if test="${folderType eq 'draft'}">		
									<tctl:msg key="mail.writedate"/>	
								</c:if>
								<c:if test="${folderType eq 'reserved'}">
									<tctl:msg key="mail.reserveddate"/>
								</c:if>		
								<c:if test="${folderType ne 'sent' && folderType ne 'draft' &&	folderType ne 'reserved'}">
									<tctl:msg key="mail.receivedate"/>
								</c:if>		
								</a>
							</th>
							<th scope="col">
								<c:if test="${sortBy eq 'size'}">
									<c:if test="${sortDir eq 'asce'}">
										<a href="javascript:;" onclick="sortMessage('size','desc')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_up.gif">
									</c:if>
									<c:if test="${sortDir eq 'desc'}">
										<a href="javascript:;" onclick="sortMessage('size','asce')" class="sortSelectItem">
										<img src="/design/common/image/icon/ic_bullet_down.gif">
									</c:if>
								</c:if>
								<c:if test="${sortBy ne 'size'}">
									<a href="javascript:;" onclick="sortMessage('size','desc')">
								</c:if>
								<tctl:msg key="mail.size"/></a>
							</th>
						</tr>
					</table>
					<div id='m_msgListWrapper'>
					<div id='m_msgListContent'>
					<table cellspacing="0" cellpadding="0" id="m_messageList">
						<c:set var="colSize" value="7"/>
						<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">		
							<col width="40px"></col>
							<col width="${enableCcview ? '70px':'55px'}" ></col>
							<c:if test="${folderType eq 'all'}">
							<c:set var="colSize" value="8"/>
							<col width="90px" ></col>
							</c:if>
							<col width="80px"></col>
							<col ></col>	
							<col width="20px"></col>
							<col width="110px"></col>
							<col width="70px"></col>
						</c:if>
						<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
							<col width="40px"></col>
							<col width="55px" ></col>	
							<col ></col>
							<col width="20px"></col>
							<col width="80px"></col>	
							<col width="110px"></col>
							<col width="70px"></col>
						</c:if>
						<c:if test="${empty messageBeans}">
						<tr class="TM_mailLow">
							<td colspan="${colSize}"><tctl:msg key="mail.nomessage"/></td>
						</tr>
						</c:if>
						<c:set var="previewCount" value="0"/>
						<c:if test="${!empty messageBeans}">
						<c:forEach var="message" items="${messageBeans}" varStatus="loop">
						<c:set var="folderVal" value="${fn:replace(message.folderName,' ','_-_')}"/>
						<c:set var="folderVal" value="${fn:replace(folderVal,'.','-_-')}"/>
						<c:set var="folderVal" value="${fn:replace(folderVal,'(','-S-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,')','-E-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'<','-LT-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'>','-GT-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'=','-EQ-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'@','-AT-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'^','-CA-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'|','-BA-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'~','-TD-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'{','-OCB-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'}','-CCB-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'!','-EM-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,';','-SM-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'+','-PL-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'#','-SH-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,'[','-SSB-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,']','-ESB-')}"/> 
						<c:set var="folderVal" value="${fn:replace(folderVal,',','-CM-')}"/> 
						<tr id="${folderVal}_${message.id}" <c:choose><c:when test="${message.seen}">class="TM_mailLow"</c:when><c:otherwise>class="TM_unseenLow"</c:otherwise></c:choose> style="height:28px;">
							<td class="TM_chkTd">	
								<div class="TM_chkW" >			
									<input type="checkbox"  name="msgId"  id="chk_${folderVal}_${message.id}"  value="${message.id}" femail='${fn:escapeXml(message.fromEmail)}' temail='${fn:escapeXml(message.toEmail)}' mailsize='${message.byteSize}'/>                           			
								</div>
							</td>
							<td class="TM_list_flag">
								<c:if test="${fn:contains(message.flag, 'F')}">
									<span flaged="false" id="${folderVal}_${message.id}_flagedF" class="flagON" type="flag" mid="${folderVal}_${message.id}" src="/design/default/image/icon/ic_flag_on.gif"></span>
								</c:if>
								<c:if test="${!fn:contains(message.flag, 'F')}">
									<span flaged="true" id="${folderVal}_${message.id}_flagedF" type="flag" mid="${folderVal}_${message.id}" class="flagOFF"></span>
								</c:if>
								<c:choose>									
									<c:when test="${fn:contains(message.flag, 'A')}">
									<span class="flagRE" id="${folderVal}_${message.id}_readF"></span>
									</c:when>
									<c:when test="${fn:contains(message.flag, 'C')}">
									<span class="flagFW" id="${folderVal}_${message.id}_readF"></span>
									</c:when>
									<c:when test="${fn:contains(message.flag, 'S')}">
									<span class="flagSE" id="${folderVal}_${message.id}_readF"></span>
									</c:when>
									<c:otherwise>
									<span class="flagUNSE" id="${folderVal}_${message.id}_readF"></span>
									</c:otherwise>
								</c:choose>
								<c:if test="${enableCcview}" >
									<c:if test="${fn:contains(message.cc, email)}">
										<img src='/design/common/image/icon/icon_cc.gif' style='float:left;padding-left:2px;'></img>
									</c:if>	
								</c:if>
								<c:if test="${fn:contains(message.flag, 'T')}">
									<span class="flagAT"></span>
								</c:if>	
							</td>
							
							<c:if test="${folderType eq 'all'}">
							<td class="TM_list_from"  title="${message.folderFullName}">
								<c:choose>
									<c:when test="${message.folderName eq 'Inbox'}">
								 		<tctl:msg key="folder.inbox" />
									</c:when>
									<c:when test="${message.folderName eq 'Sent'}">
								 		<tctl:msg key="folder.sent" />
									</c:when>
									<c:when test="${message.folderName eq 'Drafts'}">
								 		<tctl:msg key="folder.drafts" />
									</c:when>
									<c:when test="${message.folderName eq 'Reserved'}">
								 		<tctl:msg key="folder.reserved" />
									</c:when>
									<c:when test="${message.folderName eq 'Spam'}">
								 		<tctl:msg key="folder.spam" />
									</c:when>
									<c:when test="${message.folderName eq 'Trash'}">
								 		<tctl:msg key="folder.trash" />
									</c:when>
									<c:otherwise>
											${message.folderDepthName}
									</c:otherwise>
								</c:choose>	
							</td>
							</c:if>
							<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
							<td class="TM_list_subject" fname="${message.folderName}" uid="${message.id}" idx="${loop.count}">		
								<c:if test="${folderType ne 'draft'}">
									<a id="msg_subject_${folderVal}_${message.id}" href="javascript:;" class="msubject" type="subject" prividx="${previewCount}" mtr_id="${folderVal}_${message.id}"><c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}</a>
								</c:if>
								<c:if test="${folderType eq 'draft'}">
									<a id="msg_subject_${folderVal}_${message.id}" href="javascript:;" class="msubject" type="subject" prividx="${previewCount}" mtr_id="${folderVal}_${message.id}"><c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}</a>
								</c:if>		
								<c:if test="${not empty message.preview}">
						        	<c:set var="previewCount" value="${previewCount+1}"/>
									<script language="javascript">
										priviewList.push('${message.preview}');
									</script>
								</c:if>									
							</td>
							<td><c:if test="${folderType eq 'sent'}"><a href="javascript:;" fname="${message.folderName}" uid="${message.id}" idx="${loop.count}" type="popup"><img src="/design/default/image/icon/ic_popup.gif" title='<tctl:msg key="mail.popupview"/>'></a></c:if></td>
							<td class="TM_list_from"  title="${fn:escapeXml(message.to)}">
								<div nowrap>
								<a href="javascript:;" type="addr" idx="${loop.count}" addr="${fn:escapeXml(message.to)}">${fn:escapeXml(message.sendToSimple)}</a>
								</div>
								<div id="to_${loop.count}" ></div>
							</td>	
							</c:if>	
							<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
								<td class="TM_list_from"  title="${fn:escapeXml(message.from)}">
									<div nowrap>
									<a href="javascript:;" type="addr" idx="${loop.count}" addr="${fn:escapeXml(message.from)}">${fn:escapeXml(message.fromSimple)}</a>
									</div>
									<div id="from_${loop.count}"></div>		
								</td>
								<c:if test="${folderType ne 'quotaviolate'}">
								<td class="TM_list_subject" id="m_s_${folderVal}_${message.id}" fname="${message.folderName}" uid="${message.id}" idx="${loop.count}" nowrap>
									<c:if test="${!empty message.tagNameList}">
									<script language="javascript">
										tagArray.push({id:"m_s_${folderVal}_${message.id}",mid:"${folderVal}_${message.id}",tag:"${message.tagNameList}"});
									</script>
									</c:if>				
									<span class="TM_tag_list" tagContents="${message.tagNameList}"></span>
									<c:if test="${message.priority != 3}">
							           	<img src="/design/common/image/ic_import.gif">
							        </c:if>
									<a id="msg_subject_${folderVal}_${message.id}" href="javascript:;" class="msubject" type="subject" prividx="${previewCount}" mtr_id="${folderVal}_${message.id}">
										<c:if test="${message.spamRate < 0}">
											<strike><c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}</strike>
										</c:if>
										<c:if test="${message.spamRate >= 0}">
											<c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}
										</c:if>				
									</a>
									<c:if test="${not empty message.preview}">
							        	<c:set var="previewCount" value="${previewCount+1}"/>
										<script language="javascript">
											priviewList.push('${message.preview}');
										</script>
									</c:if>					
								</td>	
								<td><a href="javascript:;" fname="${message.folderName}" uid="${message.id}" idx="${loop.count}" type="popup"><img src="/design/default/image/icon/ic_popup.gif" title='<tctl:msg key="mail.popupview"/>'></a></td>
								</c:if>
								<c:if test="${folderType eq 'quotaviolate'}">
								<td class="TM_list_subject" id="m_s_${folderVal}_${message.id}" nowrap>
									<c:if test="${message.priority != 3}">
							           	<img src="/design/common/image/ic_import.gif">
							        </c:if>
									<a id="msg_subject_${folderVal}_${message.id}" href="javascript:;" class="msubject">
										<c:if test="${message.spamRate < 0}">
											<strike><c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}</strike>
										</c:if>
										<c:if test="${message.spamRate >= 0}">
											<c:if test="${empty message.subject}"><tctl:msg key="header.nosubject"/></c:if>${fn:escapeXml(message.subject)}
										</c:if>				
									</a>		
								</td>	
								<td>&nbsp;</td>
								</c:if>
							</c:if>	
							<td class="TM_list_Date_Size">${message.dateForList}</td>
							<td class="TM_list_size">${message.size}</td>		
						</tr>
						</c:forEach>
						</c:if>
					</table>
					<c:if test="${!empty messageBeans}">
					<div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
					</c:if>
					</div>
					</div>
					</div>
				</form>
				</div>
			</td>
		</tr>
	</table>
	<%@include file="/common/bottom.jsp"%>
	
	<div id="m_sourceView"  title="<tctl:msg key="mail.sourceview"/>" style="width:100%; display:none">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">		
			<col></col>	
			<tr>		
			<td height="400px"><iframe name="sourceFrame" id="sourceFrame" src="about:blank" frameborder="0" width="100%" height="400px" style="height:400px;"></iframe></td>
			</tr>
		</table>	
	</div>
	
	<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>

	<form name="popupReadForm" id="popupReadForm">
		<input type="hidden" name="uid"/>
		<input type="hidden" name="folder"/>
		<input type="hidden" name="readType"/>
		<input type="hidden" name="sharedFlag"/>
		<input type="hidden" name="sharedUserSeq"/>
		<input type="hidden" name="sharedFolderName"/>
		<input type="hidden" name="part"/>
		<input type="hidden" name="nestedPart"/>
	</form>
	<form name="popupWriteForm" id="popupWriteForm"></form>
	
	<%if(isMsie){ %>
		<c:if test="${localmail}">
		<%@include file="/dynamic/mail/inc_ocxLocalMail.jsp"%>
		</c:if>
	<%}%>
	
	<%@include file="/dynamic/mail/mailCommonModal.jsp"%>
	
	<script type="text/javascript">init();</script>
</body>
</html>