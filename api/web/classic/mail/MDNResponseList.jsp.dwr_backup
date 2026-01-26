<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />

<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>

<script type="text/javascript" src="/dwr/interface/MailFolderService.js"></script>
<script type="text/javascript" src="/dwr/interface/MailMessageService.js"></script>
<script type="text/javascript" src="/dwr/interface/MailTagService.js"></script>
<script type="text/javascript" src="/dwr/interface/MailSearchFolderService.js"></script>
<script type="text/javascript" src="/dwr/interface/MailCommonService.js"></script>

<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailBasicCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>
<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<style type="text/css">html {*overflow-y:scroll;}</style>
<script type="text/javascript">
setAlMailMode(true);
<%@include file="userSetting.jsp"%>

LayoutInfo.mode = "mdnlist";
var menuBar;
var paneMode;
LayoutInfo.mode = "mdnlist";

chkMsgCnt = 0;	
isAllFolder = false;
chkMsgHash = new Hash();


function displayMDN(lid,uid, mid, fd, code) {
	var pstr = "";

	if (code == "1") {
		pstr += mailMsg.mail_mdn_recall;
	} else if (code == "1000") {
		pstr += fd;
	} else if (code == "100") {
		pstr += mailMsg.mail_mdn_wait;
	} else if (code == "200" || code == "201" || code == "300") {
		pstr += mailMsg.mail_mdn_unseen;
	} else {
		pstr += mailMsg.mail_mdn_fail;
	}
	
	jQuery("#"+lid).html(pstr);
}

function viewMDN(uid){
	var param = {"uid":uid};
	param.mdnlistpage = ${currentPage};
	param.mdnlistpattern = jQuery("#keyWord").val();
	mailControl.viewMDNContent(param);
}

function init() {

	setTopMenu('mail');
	
	mainInitFunc();
	folderInitLoad();
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}
	
	var currentPage = ${currentPage};
	var pageBase = ${pageBase};
	var totalMessage = ${total};		
		
	var workTitle =  "<span class='TM_work_title'>"+mailMsg.mail_receivenoti+"</span>";	
	setWorkTitle(workTitle); 

	mailControl.setCurrentFolder("Sent");

	var searchParam = {};
	searchParam.keyWord = jQuery("#keyWord").val();
	searchParam.adv = "off";	

	loadMDNListToolBar();	
	mailControl.setSearchParam(searchParam);
	
	jQuery("#siSearchBox").show();
	/*menuBar.setPageNavi("p",{total:totalMessage,
		base:pageBase,
		page:currentPage,
		isListSet:true,
		isLineCntSet:true,
		pagebase:USER_PAGEBASE,
		changeAfter:reloadMDNListPage});*/	

	menuBar.setPageNaviBottom("p",{total:totalMessage,
		base:pageBase,
		page:currentPage,
		isListSet:true,
		isLineCntSet:true,
		pagebase:USER_PAGEBASE,
		changeAfter:reloadMDNListPage});
		
	setCurrentPage(currentPage);
	setSearchStatus("${fn:escapeXml(keyWord)}","off");
}
</script>

</head>
<body>
	<%@include file="/common/topmenu.jsp"%>
	<table width="100%" cellpadding="0" cellspacing="0" class="TM_html_m_table">
		<tr>
			<td class="TM_m_leftMenu" style="width:220px;vertical-align:top;">
				<%@include file="mailLeftCommon.jsp"%>
				<%@include file="sideMenu.jsp"%>
			</td>
			<td class="mainvsplitbar"></td>
			<td class="TM_main_hframe" >
				<div class="posr">
				
				<form name="listForm" id="listForm">					
					<input type="hidden" name="keyWord" id="keyWord" value="${fn:escapeXml(keyWord)}"/>
					<input type="hidden" name="adv" id="off"/>
					<div class="TM_folderInfo posr">				
						<img class="TM_barLeft" src="/design/common/image/blank.gif">								
						<div class="TM_finfo_data" id="workTitle"></div>
						<div class="TM_finfo_search" id="siSearchBox">
							<div class="TM_mainSearch">
								<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" value="${fn:escapeXml(keyWord)}" /><a href="javascript:;" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="mailSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a><a href="javascript:;" onclick="showAdsearchBox()" id="adSearchBoxBtn" class="TM_advsearch_btn"><span><tctl:msg key="mail.adsearch"/></span></a>
								<input type="text" name="_tmp" style="width:0px;height:0px;display:none"/>
							</div>	
						</div>						
						<img class="TM_barRight" src="/design/common/image/blank.gif">
					</div>
					<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
					<div class="TM_mainContent">
					<div id="mailMenubar" >			
						<div class="mail_body_tabmenu posr">
							<div class="mail_body_tab" id="menuBarTab"></div>
							<div>
								<div id="processMessageContent" class="TM_processMessage"></div>
							</div>
							<div id="pageNavi"  class="mail_body_navi">				
							</div>
						</div>		
						<div class="mail_body_menu">
							<div class="menu_main_unit" id="menuBarContent"></div>
						</div>
					</div>	
					<div class="TM_listWrapper">
					<table cellspacing="0" cellpadding="0" id="m_messageList" >
						<col width="40px"></col>		
						<col width="90px"></col>
						<col width="100px"></col>
						<col width="150px"></col>
						<col></col>
						
						<tr>
						<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
						<th scope="col"><tctl:msg key="mail.senddate"/></th>	
						<th scope="col"><tctl:msg key="mail.receivenoti"/></th>
						<th scope="col"><tctl:msg key="mail.to"/></th>	
						<th scope="col"><tctl:msg key="mail.subject"/></th>
						</tr>
						
						<c:if test="${empty messages}">
							<tr class="TM_mailLow">
								<td colspan="5"><tctl:msg key="mail.nomessage"/></td>
							</tr>
						</c:if>
						<c:if test="${!empty messages}">
							<c:forEach var="message" items="${messages}" varStatus="loop">
							<tr id="${message.folderName}_${message.id}" class="TM_mailLow">
							<td class="TM_chkTd">
								<div class="TM_chkW" >			
									<input type="checkbox"  name="msgId"  id="chk_${message.id}"  value="${message.id}" onclick="checkMessage(this)"  onfocus="this.blur()"/>		
								</div>
							</td>	
							<td>${message.sendDateForList}</td>
							<td>
								<c:if test="${message.MDNCount < 0}">
									<span class="TM_MDNStatus">
									<tctl:msg key="mail.mdn.notselect"/>
									</span>
								</c:if>
								<c:if test="${message.MDNCount == 1}">
									<span class="TM_MDNStatus" id="mdn_${loop.count}"></span>
									<script>displayMDN("mdn_${loop.count}","${message.id}",
											"${message.messageID}",
											"${message.MDNResponses[0].sentDate3}",
											"${message.MDNResponses[0].code}");</script>			
								</c:if>
								<c:if test="${message.MDNCount > 1}">
									<a href="javascript:;" onclick=""> ${message.MDNReadCount} / ${message.MDNCount} </a> 
								</c:if>
							</td>
							<td class="TM_list_from"  title="${fn:escapeXml(message.to)}" >
								<div nowrap>		 
								${fn:escapeXml(message.sendToSimple)}
								</div>
							</td>
							<td class="TM_list_subject">
								<c:if test="${message.MDNCount > 0}">		
								<strong><a href="javascript:;" onclick="viewMDN('${message.id}')" id="msg_subject_${message.id}">${fn:escapeXml(message.subject)}</a></strong>
								</c:if>
								<c:if test="${message.MDNCount <= 0}">
									${fn:escapeXml(message.subject)}
								</c:if>
							</td>
							</tr>	
							</c:forEach>
						</c:if>
					</table>
					</div>
					<c:if test="${!empty messages}">
					<div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
					</c:if>
					</div>
				</form>
				</div>
			</td>
		</tr>
	</table>
	<%@include file="/common/bottom.jsp"%>
	
	<form name="popupWriteForm" id="popupWriteForm"></form>	
	
	<%@include file="/dynamic/mail/mailCommonModal.jsp"%>
	
	<script type="text/javascript">init();</script>
</body>
</html>