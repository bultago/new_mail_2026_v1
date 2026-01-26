<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />

<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/ext-lib/html5FileUploader.js"></script>

<%if (isMinify) {%>
	<script type="text/javascript" src="/js/ext-lib/ext-mail-all.min.js"></script>
	<script type="text/javascript" src="/js/swfupload-lib/swf-all.min.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mail-all.min.js"></script>
<%} else {%>
	<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
	<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
	<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>
	
	<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
	<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailDynamicCommon.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailAttach.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailBasicAttach.js"></script>
	<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>
	<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
	<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<%}%>


<script type="text/javascript" src="/js/ocx/ocx_localMail.js"></script>
<script type="text/javascript" src="/editor/smarteditor/js/service/HuskyEZCreator.js?dummy=" charset="utf-8"></script>


<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<script type="text/javascript">
setAlMailMode(true);
var IS_USE_EXPRESS_E = (isMsie && <%=ExtPartConstants.isXecureExpressE()%>);
var IS_USE_WECURE_WEB = (isMsie && <%=ExtPartConstants.isXecureWebUse()%>);
var workMailParam = ${workParam};
var mailSearchConfig = ${mailSearchConfig};
var isPopupWrite = ${fn:escapeXml(popupWrite)};
var isWriteNoti = ${fn:escapeXml(writeNoti)};
var isLocalMail = ${fn:escapeXml(localmail)};
var isSearchAllFolder = ${fn:escapeXml(isSearchAllFolder)};
var treeOpt = {

	 	//Style Class Define
	 	nodeClass : "tnode",
	 	childNodeClass : "tchild-node",
	 	closefolderClass : "tcfolder",
	 	sharedfolderClass : "tsfolder",
	 	openfolderClass : "tofolder",
	 	closeChildClass : "tcChild",
	 	openChildClass : "toChild",
	 	emptyChildClass : "emChild",
	 	nodeLinkClass : "tcnodelink",
	 	nodeConfirmClass : "tcconfirmIcon",
	 	nodeCancelClass : "tccancelIcon",	
	 	blankImgSrc : "/design/common/image/blank.gif",

	 	//menuUse [true / false]
	 	nodeMenuUse : true,	
	 	
	 	//link function
	 	nodelink: nodeLink,
	 	
	 	addFolderFunc:addFolder,

	 	deleteFolderFunc:deleteFolder,
	 	
	 	modifyFolderFunc:modifyFolder,
	 	
	 	//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		outClass : "tcmenuOutClass",
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [	 			 		
	 			 		{name:mailMsg.menu_add,link:addChildNode},
	 			 		{name:mailMsg.menu_modfy,link:modifyNode},
	 			 		{name:mailMsg.menu_remove,link:removeNode},
	 			 		{name:mailMsg.menu_empty,link:cleanFolder},
	 			 		{name:mailMsg.menu_shared,link:viewSharedFolderSetting}
	 			 	]
	 	}	

	 };

var mailOption = {
	mainLID : "m_contentMain",
	subLID : "m_contentSub",
	listTableID : "m_messageList",	
	listAction : "/dynamic/mail/listMessage.do",
	readAction : "/dynamic/mail/readMessage.do",
	writeAction : "/dynamic/mail/writeMessage.do",
	sendAction : "/dynamic/mail/sendMessage.do",	
	initSubPage : "/dynamic/mail/messageNone.jsp",
	mdnAction : "/dynamic/mail/listMDNResponses.do",
	mdnViewAction : "/dynamic/mail/viewMDNResponses.do",
	recallAction : "/dynamic/mail/recallMessage.do",
	errorPage : "/common/error/subError.jsp"
	
};

var dndOption = {			
		helperID:"dragHelper",
		helperUseClass:"TM_dhelper",
		helperUnuseClass:"TM_dhelper_unuse",
		helperRefID:"msg_subject_",
		dropOverClass:"TM_dropareahover"
};


var tagOption = {

		type : "tag",
		wrapClass : "TM_tag_tree_wrapper",
		drowFrame : "m_userTag",
		folderClass : "tagimg_base",
		linkFunc:listTagMessage,

		//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
						{name:mailMsg.menu_modfy,link:modifyTag},					
						{name:mailMsg.menu_remove,link:deleteTag}
	 			 	]
	 	}	
};

var searchFolderOption = {

		type : "sfolder",
		wrapClass : "TM_tree_wrapper",
		drowFrame : "m_userSearchFolder",
		folderClass : "tcfolder",
		linkFunc:listSearchFolder,
		//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [	 			 		
	 			 		{name:mailMsg.menu_modfy,link:modifySearchFolder},					
						{name:mailMsg.menu_remove,link:deleteSearchFolder}
	 			 	]
	 	}	
};

var sharedFolderOption = {

		type : "shfolder",
		wrapClass : "TM_tree_wrapper",
		drowFrame : "m_userSharringFolder",
		folderClass : "tcfolder",
		linkFunc:sharedFolderLink			
};

var flagOpt = {
	onFlagedImg : "/design/default/image/icon/ic_flag_on.gif",
	offFlagedImg : "/design/default/image/icon/ic_flag_off.gif",
	seenImg : "/design/default/image/icon/ic_mail_seen.gif",
	forwardImg : "/design/default/image/icon/ic_mail_fw.gif",
	replyImg : "/design/default/image/icon/ic_mail_reply.gif",
	unseenImg : "/design/default/image/icon/ic_mail_unseen.gif",
	attrImg : "/design/default/image/icon/ic_attach_file.gif",
	flagFunc : switchFlagFlaged	
};

var popupOpt = {
	closeName:comMsg.comn_close,
	btnClass:"btn_style3"			
};


</script>
</head>
<body>

<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="m_mainBody">	
	<div id="m_leftMenu">		
		<div id="leftMenuContent">			
			<div id="ml_btnFMain">				
				<div class="TM_topLeftMain">					
					<div class="mainBtn" >					
						<table cellpadding="0" cellspacing="0" border="0" class="TM_mainBtn_bg">
							<tr>
							<td nowrap style="white-space: nowrap;" >
								<div class="TM_t_left_btn"><a href="javascript:;" onclick="goWrite();" class="TM_mainBtn_split" ><div class="TM_mainWriteBtn"><tctl:msg key="mail.write"/></div></a></div>
							</td>						
							<td nowrap style="white-space: nowrap;" >
								<div class="TM_t_right_btn"><a href="javascript:;" onclick="goMDNList();" ><div class="TM_mainReceiveNotiBtn"><tctl:msg key="mail.receivenoti"/></div></a></div>
							</td>
							</tr>
						</table>					
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
			</div>
			<tctl:extModuleCheck moduleName="expressE" msie="true">
			<div class="TM_secureMailWrite" onclick="goWriteSecureMail();">
				<span class="swriteBtn"><tctl:msg key="mail.secure.write"/></span>
			</div>
			</tctl:extModuleCheck>
			<div id="leftMenuRcontentWrapper" style="position: relative;">
			<div id="leftMenuRcontent">
			<div>			
			<div id="m_defultFolder" >
				<div style="clear: both;"></div>		
				<div id="df_Inbox"  fname="Inbox"  fullname="Inbox" class="TM_ml_defaultFolder TM_df_inbox">
					<a href="javascript:;" onclick="goFolder('Inbox')" id="link_folder_Inbox"><tctl:msg key="folder.inbox" /></a>
					<span id="mf_Inbox_newCnt"></span>
					<a id="folder_manage_link" href="#"  onclick="goFolderManage()" class="TM_ml_sideBtn jpf"><tctl:msg key="comn.mgnt"  bundle="common"/></a>
				</div>				
				<div id="df_Sent"  fname="Sent"  fullname="Sent" class="TM_ml_defaultFolder TM_df_sent">
					<a href="javascript:;" onclick="goFolder('Sent')" id="link_folder_Sent"><tctl:msg key="folder.sent" /></a>
					<span id=mf_Sent_newCnt></span>
				</div>
				<div id="df_Drafts"  fname="Drafts"  fullname="Drafts" class="TM_ml_defaultFolder TM_df_drafts">
					<a href="javascript:;" onclick="goFolder('Drafts')" id="link_folder_Drafts"><tctl:msg key="folder.drafts" /></a>
					<span id="mf_Drafts_newCnt"></span>
				</div>
				<div id="df_Reserved"  fname="Reserved"  fullname="Reserved" class="TM_ml_defaultFolder TM_df_reserved">
					<a href="javascript:;" onclick="goFolder('Reserved')" id="link_folder_Reserved"><tctl:msg key="folder.reserved" /></a>
					<span id="mf_Reserved_newCnt"></span>
				</div>
				<div id="df_Spam" fname="Spam"  fullname="Spam" class="TM_ml_defaultFolder TM_df_spam">
					<a href="javascript:;" onclick="goFolder('Spam')" id="link_folder_Spam"><tctl:msg key="folder.spam" /></a>
					<span id="mf_Spam_newCnt"></span>
					<a href="javascript:;"  onclick="emptyFolder('Spam')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.empty" /></a>
				</div>
				<div id="df_Trash" fname="Trash"  fullname="Trash"  class="TM_ml_defaultFolder TM_df_trash">
					<a href="javascript:;" onclick="goFolder('Trash')" id="link_folder_Trash"><tctl:msg key="folder.trash" /></a>
					<span id="mf_Trash_newCnt"></span>
					<a href="javascript:;" onclick="emptyFolder('Trash')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.empty" /></a>
				</div>
				
				<div id="df_Quotaviolate" fname="Quotaviolate"  fullname="Quotaviolate"  class="TM_ml_defaultFolder TM_df_quotaviolate">
					<a href="javascript:;" onclick="goFolder('Quotaviolate')" id="link_folder_Quotaviolate"><tctl:msg key="folder.quotaviolate" /></a>
					<span id="mf_Quotaviolate_newCnt"></span>					
				</div>	
							
			</div>
						
			<%if(isMsie){%>
			<c:if test="${localmail}">
			<div class="TM_ml_defaultFolder TM_df_localmail">
				<a href="/dynamic/mail/localMailbox.do" ><tctl:msg key="mail.localmail"/></a>				
			</div>
			</c:if>
			<%}%>			
						
			<div class="TM_ml_defaultFolder TM_df_outermail">				
				<a href="javascript:;" onclick="pop3Win()"><tctl:msg key="conf.pop.44" bundle="setting"/></a>
			</div>
			
			<c:if test="${useArchive}">
			<div class="TM_ml_defaultFolder TM_df_outermail">
				<a href="${archiveSSOUrl}" target="_blank"><tctl:msg key="mail.achive.link"/></a>				
			</div>
			</c:if>
			
			<div style="clear: both;"></div>
			
			<div class="TM_ml_MenuTitle">
				<a href="javascript:;" onclick="toggleMenu('m_userFolderTree');"><img src="/design/common/image/btn_menu_mius.gif" id="m_userFolderTree_view"></a>
				<a href="#" onclick="toggleMenu('m_userFolderTree')"><tctl:msg key="folder.user" /></a>
				<a href="javascript:;" onclick="addNode('m_userFolderTree')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
			</div>			
			<div id="m_userFolderTree" ></div>
			
			<div style="height:2px;clear: both;"><div class="TM_ml_line"></div></div>			
			
			<div class="TM_ml_MenuTitle">
				<a href="javascript:;" onclick="toggleMenu('m_userTag');"><img src="/design/common/image/btn_menu_mius.gif" id="m_userTag_view"></a>
				<a href="#" onclick="toggleMenu('m_userTag')"><tctl:msg key="folder.tag" /></a>
				<a href="javascript:;" onclick="addTag()" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
			</div>			
			<div id="m_userTag" ></div>
			
			<div style="height:2px;clear: both;"><div class="TM_ml_line"></div></div>	
			
			<div class="TM_ml_MenuTitle">
				<a href="javascript:;" onclick="toggleMenu('m_searchFolderWrapper');"><img src="/design/common/image/btn_menu_mius.gif" id="m_searchFolderWrapper_view"></a>
				<a href="#" onclick="toggleMenu('m_searchFolderWrapper')"><tctl:msg key="folder.search" /></a>
				<a href="javascript:;" onclick="addSearchFolder()" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
			</div>
			<div id="m_searchFolderWrapper" >		
			<div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu1" onclick="viewQuickList('flaged',true,true)"><tctl:msg key="menu.quick.flag"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu2" onclick="viewQuickList('attached',true,true)"><tctl:msg key="menu.quick.attach"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu3" onclick="viewQuickList('unseen',true,true)"><tctl:msg key="menu.quick.unread"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu4" onclick="viewQuickList('seen',true,true)"><tctl:msg key="menu.quick.read"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu5" onclick="viewQuickList('yesterday',true,true)"><tctl:msg key="menu.quick.yesterday"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu6" onclick="viewQuickList('today',true,true)"><tctl:msg key="menu.quick.today"/></a>
				</div>
			</div>
			<div id="m_userSearchFolder"></div>
			</div>
			
			<div style="height:2px;clear: both;"><div class="TM_ml_line"></div></div>	
			
			<div class="TM_ml_MenuTitle">
				<a href="javascript:;" onclick="toggleMenu('m_userSharringFolder')"><img src="/design/common/image/btn_menu_mius.gif" id="m_userSharringFolder_view"></a>
				<a href="#"  onclick="toggleMenu('m_userSharringFolder')"><tctl:msg key="mail.shared.title" /></a>								
			</div>
			<div id="m_userSharringFolder"></div>	
			
			<div style="height:2px;clear: both;"><div class="TM_ml_line"></div></div>		
			</div>						
			</div>			
			</div>
			<%@include file="/common/sideMenu.jsp"%>	
		</div>
	</div>	
	<!-- #LeftPane -->
	
	<div id="m_contentBodyWapper" class="TM_contentBodyWapper">		
			
			<div>
			<div id="adSearchBox"  style="z-index:101;position: absolute; right:10px;" class="popup_style2 TM_advsearch_box">
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
			
			
			<div class="TM_folderInfo">				
				<img src="/design/common/image/blank.gif" class="TM_barLeft">								
				<div id="workTitle" class="TM_finfo_data"></div>
				<div id="siSearchBox" class="TM_finfo_search">
					<div class="TM_mainSearch">
						<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" /><a href="javascript:;" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="mailSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a><a href="javascript:;" onclick="showAdsearchBox()" id="adSearchBoxBtn" class="TM_advsearch_btn"><span><tctl:msg key="mail.adsearch"/></span></a>
					</div>	
				</div>						
				<img src="/design/common/image/blank.gif" class="TM_barRight">
			</div>
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
		<div id="m_mainContent" class="TM_mainContent">
		<div id="mailMenubar" >			
			<div class="mail_body_tabmenu">
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
		
		<div id="m_contentBody" >
			<div id="m_homeBody" ></div>
			<div id="m_contentMain"></div>
			<div id="m_contentSub"></div>
		</div>
		</div>
	</div>
</div>

<script type="text/javascript">
if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
	jQuery("#folder_manage_link").hide();
}
</script>
<%@include file="/common/bottom.jsp"%>


<div id="m_sourceView"  title="<tctl:msg key="mail.sourceview"/>" style="width:100%; display:none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">		
		<col></col>	
		<tr>		
		<td><iframe name="sourceFrame" id="sourceFrame" src="about:blank" frameborder="0" style="width:100%;"></iframe></td>
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

</body>
</html>