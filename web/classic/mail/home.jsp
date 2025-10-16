<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

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


<script type="text/javascript">

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
		
</script>