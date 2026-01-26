<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />

<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>


<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAttach.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailBasicCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>

<script type="text/javascript" src="/js/ocx/ocx_localMail.js"></script>

<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<style type="text/css">html {*overflow-y:scroll;}</style>
<script type="text/javascript">
setAlMailMode(true);
LayoutInfo.mode = "intro";
<%@include file="userSetting.jsp"%>

function init() {

	setTopMenu('mail');
	
	mainInitFunc();
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}

	var workTitle = "<span class='TM_work_title'>"+comMsg.comn_top_mailhome+"</span>";
	var loginTime = "${loginTimeInfo}";
	var loginStr;
	if(loginTime != ""){
		var loginTimeInfo = loginTime.split("|");
		loginStr = "<div class='TM_lastLoginText jpf'>"+			
					msgArgsReplace(comMsg.comn_logintime,
							[loginTimeInfo[0].substring(0,4),
							loginTimeInfo[0].substring(4,6),
							loginTimeInfo[0].substring(6,8),
							loginTimeInfo[0].substring(8,10),
							loginTimeInfo[0].substring(10,12),
							loginTimeInfo[0].substring(12,14),
							loginTimeInfo[1]])+"</div>";
		
	} else {
		loginStr = "<div class='TM_lastLoginText'>"+comMsg.comn_loginfirst+"</div>";
	}
	jQuery("#lastLoginInfo").html(loginStr);
	setWorkTitle(workTitle);
	
	folderInitLoad();
	
	
	
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
		<td class="TM_main_hframe" style="background:none;">
		<div class="posr">
		<div class="TM_folderInfo posr">				
			<img src="/design/common/image/blank.gif" class="TM_barLeft">								
			<div id="workTitle" class="TM_finfo_data"></div>										
			<img src="/design/common/image/blank.gif" class="TM_barRight">
		</div>
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
		<div id="m_contentBody">
			<div id="lastLoginInfo" class="TM_lloginC"></div>
			<div class="title_desc_l">
				<div class="title_desc_r">
					<span class="title_desc_icon"><tctl:msg key="intro.tooltip"/></span>
				</div>
			</div>
			<div id="incon" class="portlet_intro_body" style="height:450px;">
				<div class="mailFirst_menu">
					<div><a onclick="goFolder('Inbox');" href="javascript:;" class="btn_mailFirst1" title="<tctl:msg key="intro.mail.read.title"/>"></a></div>
					<dl>
						<dt><tctl:msg key="intro.mail.read.title"/></dt>
						<dd><tctl:msg key="intro.mail.read.tooltip"/></dd>
					</dl>
					<div class="dotLineH" style="margin:20px 0px 10px 0px;"></div>
					<div><a onclick="goWrite();" href="javascript:;" class="btn_mailFirst2" title="<tctl:msg key="intro.mail.write.title"/>"></a></div>
					<dl>
						<dt><tctl:msg key="intro.mail.write.title"/></dt>
						<dd><tctl:msg key="intro.mail.write.tooltip"/></dd>
					</dl>
					<div class="dotLineH" style="margin:20px 0px 10px 0px;"></div>
					<div><a href="/dynamic/addr/addrCommon.do" class="btn_mailFirst3" title="<tctl:msg key="intro.mail.addr.title"/>"></a></div>
					<dl>
						<dt><tctl:msg key="intro.mail.addr.title"/></dt>
						<dd><tctl:msg key="intro.mail.addr.tooltip"/></dd>
					</dl>
					<div class="dotLineH" style="margin:20px 0px 10px 0px;"></div>
					<div><a href="/setting/viewLayout.do" class="btn_mailFirst4" title="<tctl:msg key="intro.mail.home.title"/>"></a></div>
					<dl>
						<dt><tctl:msg key="intro.mail.home.title"/></dt>
						<dd><tctl:msg key="intro.mail.home.tooltip"/></dd>
					</dl>
					<div class="dotLineH" style="margin:20px 0px 10px 0px;"></div>
					<div><a href="javascript:;" onclick="viewHelp();" class="btn_mailFirst5" title="<tctl:msg key="intro.mail.help.title"/>"></a></div>
					<dl>
						<dt><tctl:msg key="intro.mail.help.title"/></dt>
						<dd><tctl:msg key="intro.mail.help.tooltip"/></dd>
					</dl>				
				</div>
			</div>
			
		</div>
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