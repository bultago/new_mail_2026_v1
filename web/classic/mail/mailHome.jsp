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

	var portlet = [];
	portlet[portlet.length] = {target:"view_user1", part:"A", name:"${customMap['portlet1'].portletName}", url:"${customMap['portlet1'].portletUrl}"};
	portlet[portlet.length] = {target:"view_user2",part:"B", name:"${customMap['portlet2'].portletName}", url:"${customMap['portlet2'].portletUrl}"};
	portlet[portlet.length] = {target:"view_user3",part:"C", name:"${customMap['portlet3'].portletName}", url:"${customMap['portlet3'].portletUrl}"};
	portlet[portlet.length] = {target:"view_user4",part:"D", name:"${customMap['portlet4'].portletName}", url:"${customMap['portlet4'].portletUrl}"};

	var usePortlet = true;
	for (var i=0; i<portlet.length; i++) {
		usePortlet = true;
		if(!MENU_STATUS.calendar || MENU_STATUS.calendar != "on") {
			if(("Calendar" == portlet[i].name) || ("TodaySchedule" == portlet[i].name)) {
				usePortlet = false;
			}
		}
	
		if(!MENU_STATUS.bbs || MENU_STATUS.bbs != "on") {
			if((portlet[i].url.indexOf("/noticeView.do") > 0)) {
				usePortlet = false;
			}
		}
		if (usePortlet) {
			ActionNotMaskLoader.loadAction(portlet[i].target, portlet[i].url+"&part="+portlet[i].part+"&dummy="+makeRandom(),"");
		}
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
			<div class="TM_folderInfo posr">				
				<img src="/design/common/image/blank.gif" class="TM_barLeft">								
				<div id="workTitle" class="TM_finfo_data"></div>										
				<img src="/design/common/image/blank.gif" class="TM_barRight">
			</div>
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
			<div id="m_contentBody">
				<div id="lastLoginInfo" class="TM_lloginC"></div>
	
				<table class="TB_mailHome TM_tableList" border="0">
				<tbody>
					<tr class="TR_firstLine">
						<td class="TD_part1" nowrap>
							<div class="subPortletWrapper" id="view_user1"></div>
						</td>
						<td width="8" class="blank" nowrap></td>
						<td class="TD_part2">
							<div class="subPortletWrapper" id="view_user2"></div>
						</td>
					</tr>
					<tr><td colspan="3" height="8"></td></tr>
					<tr height="245">
						<td class="TD_part3" nowrap>
							<div class="subPortletWrapper" id="view_user3"></div>
						</td>
						<td></td>
						<td class="TD_part4">
							<div class="subPortletWrapper" id="view_user4"></div>
						</td>
					</tr>
				</tbody>
				</table>
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