<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/note_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail_write_style.css">

<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>

<script type="text/javascript" src="noteOption.js"></script>
<script type="text/javascript" src="noteCommon.js"></script>
<script type="text/javascript" src="noteMenuBar.js"></script>
<script type="text/javascript" src="noteLayoutRender.js"></script>
<script type="text/javascript" src="noteAction.js"></script>
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>

</head>
<body>
<%@include file="/common/topmenu.jsp"%>

<div id="m_mainBody">	
	<div id="m_leftMenu">		
		<div id="leftMenuContent">			
			<div id="ml_btnFMain_s">				
				<div class="TM_topLeftMain">					
					<div class="mainBtn" >					
						<table cellpadding="0" cellspacing="0" border="0" class="TM_mainBtn_bg">
							<tr>
							<td nowrap style="white-space: nowrap;" >
								<div class="TM_t_left_btn"><a href="javascript:;" onclick="writeNote();" class="TM_mainBtn_split" ><div class="NT_mainWriteBtn"><tctl:msg key="note.msg.005" bundle="common"/></div></a></div>
							</td>						
							<td nowrap style="white-space: nowrap;" >
								<div class="TM_t_right_btn"><a href="javascript:;" onclick="reloadNoteList();" ><div class="NT_mainReceiveBtn"><tctl:msg key="note.msg.006" bundle="common"/></div></a></div>
							</td>
							</tr>
						</table>					
					</div>
				</div>
			</div>
			<div id="leftMenuRcontentWrapper" style="position: relative;">
				<div id="leftMenuRcontent">
					<div>			
						<div id="m_defultFolder" >
							<div style="clear: both;"></div>		
							<div id="df_Inbox" class="TM_ml_defaultFolder NT_df_inbox">
								<a href="javascript:;" onclick="goFolder('Inbox')" id="link_folder_Inbox"><tctl:msg key="note.msg.001" bundle="common"/></a>
								<span id="mf_Inbox_newCnt"></span>
							</div>				
							<div id="df_Sent" class="TM_ml_defaultFolder NT_df_sent">
								<a href="javascript:;" onclick="goFolder('Sent')" id="link_folder_Sent"><tctl:msg key="note.msg.002" bundle="common"/></a>
							</div>
							<div id="df_Drafts" class="TM_ml_defaultFolder NT_df_save">
								<a href="javascript:;" onclick="goFolder('Save')" id="link_folder_Drafts"><tctl:msg key="note.msg.003" bundle="common"/></a>
							</div>
							<div class="TM_ml_defaultFolder NT_df_setting">
								<a href="javascript:;" onclick="goSetting()"><tctl:msg key="note.msg.004" bundle="common"/></a>
							</div>
						</div>
									
						<div style="clear: both;"></div>			
					</div>						
				</div>			
			</div>
			<%@include file="/common/sideMenu.jsp"%>	
		</div>
	</div>	
	<!-- #LeftPane -->
	
	<div id="m_contentBodyWapper" class="TM_contentBodyWapper">		
		<div class="TM_folderInfo">				
			<img src="/design/common/image/blank.gif" class="TM_barLeft">								
			<div id="workTitle" class="TM_finfo_data"></div>
			<div id="siSearchBox" class="TM_finfo_search">
				<div class="TM_mainSearch">
					<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" /><a href="javascript:;" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="noteSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a>
				</div>	
			</div>						
			<img src="/design/common/image/blank.gif" class="TM_barRight">
		</div>
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
		<div id="m_mainContent" class="TM_mainContent">
		<div id="noteMenubar" >			
			<div class="mail_body_tabmenu">
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
		
		<div id="m_contentBody" >
			<div id="m_contentMain"></div>
			<div id="m_contentSub"></div>
		</div>
		</div>
	</div>
</div>

<%@include file="/common/bottom.jsp"%>

<form name="noteForm" onsubmit="false">
	<input type="hidden" name="to"/>
	<input type="hidden" name="content"/>
	<input type="hidden" name="keyWord"/>
	<input type="hidden" name="folderName"/>
	<input type="hidden" name="uid"/>
	<input type="hidden" name="flag"/>
</form>

</body>
</html>