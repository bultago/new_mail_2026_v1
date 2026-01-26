<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>

<script language="javascript">
function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery(window).trigger("resize");
	
	jQuery("#home_menu").addClass("on");
	jQuery("#afterLoginSelect").selectbox({selectId:"afterLogin",selectFunc:toggleHomeLayout},
			"${homeSetting}",
			[{index:settingMsg.conf_profile_50,value:"home"},
			 {index:settingMsg.conf_profile_70,value:"intro"},
			 {index:settingMsg.conf_profile_51,value:"mail"},
			 {index:settingMsg.conf_profile_55,value:"manage"}]);

	toggleHomeLayout();



	<c:forEach var="idx" begin="1" end="${layout.portletCount}">
		var portletArray${idx} = [];		
		<c:forEach var="portlet" items="${portlets}">
		<c:choose>	
		     <c:when test="${portlet.portletName == 'Calendar'}">
		     portletName = "<tctl:msg key="scheduler.title" bundle="scheduler"/>";
		     </c:when>
			 <c:when test="${portlet.portletName == 'TodaySchedule'}">
			 portletName = "<tctl:msg key="scheduler.menu.today" bundle="scheduler"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'Quota'}">
		     portletName = "<tctl:msg key="mail.quota.info"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'Clock'}">
		     portletName = "<tctl:msg key="comn.analog.clock" bundle="common"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'MailALL'}">
		     portletName = "<tctl:msg key="folder.all"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'Sent'}">
		     portletName = "<tctl:msg key="folder.sent"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'Inbox'}">
		     portletName = "<tctl:msg key="folder.inbox"/>";
		     </c:when>
		     <c:when test="${portlet.portletName == 'Unread'}">
		     portletName = "<tctl:msg key="menu.quick.unread"/>";
		     </c:when>
		     <c:otherwise>
		     portletName = "${fn:escapeXml(portlet.portletName)}";
		     </c:otherwise>
	     </c:choose>	   
	     <c:choose>	     	
	     	<c:when test="${portlet.portletName == 'Calendar' || portlet.portletName == 'TodaySchedule'}">
		     	if(MENU_STATUS.calendar && MENU_STATUS.calendar == "on") {
		    		portletArray${idx}.push({index:portletName,value:"${portlet.portletSeq}"});
			    }
	     	</c:when>
	     	<c:when test="${fn:indexOf(portlet.portletUrl,'noticeView.do') > -1}">
				if(MENU_STATUS.bbs && MENU_STATUS.bbs == "on") {
					portletArray${idx}.push({index:portletName,value:"${portlet.portletSeq}"});
	    		}	
	     	</c:when>
	     	<c:otherwise>	     	 	
	     		portletArray${idx}.push({index:portletName,value:"${portlet.portletSeq}"});
	     	</c:otherwise>
	     </c:choose>	     
		</c:forEach>		
		jQuery("#portlet${idx}Select").selectbox({selectId:"portlet${idx}",selectFunc:"",width:120,escape:true},
				"${customMap[idx].portletSeq}",portletArray${idx});
	</c:forEach>

	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
}

<%@ include file="settingFrameScript.jsp" %>

function checkPortlet(idx,val,array){
	for(var i = 0; i < array.length ; i++){
		if(i != idx){			
			if(array[i] == val){				
				return false;
				break;
			}
		}
	}
	return true;
}

function modifyLayout() {
	var f = document.forms[0];
	var portlet0 = $("portlet1").value;
	var portlet1 = $("portlet2").value;
	var portlet2 = $("portlet3").value;
	var portlet3 = $("portlet4").value;
	var portletArray = [];
	portletArray[0]=portlet0;
	portletArray[1]=portlet1;
	portletArray[2]=portlet2;
	portletArray[3]=portlet3;

	if($("afterLogin").value == "home"){
		if(!checkPortlet(0,portlet0,portletArray) ||
			!checkPortlet(1,portlet1,portletArray) ||
			!checkPortlet(2,portlet2,portletArray) ||
			!checkPortlet(3,portlet3,portletArray)){
			alert(settingMsg.conf_mailhome_setting_004);
			return;
		}
	}	
	

	f.action = "/setting/saveLayout.do";
	f.method = "post";
	f.submit();
}

function resetLayout() {
	
	jQuery("#afterLoginSelect").selectboxSetValue("${homeSetting}");
	toggleHomeLayout();	
	<c:forEach var="idx" begin="1" end="${layout.portletCount}">
	jQuery("#portlet${idx}Select").selectboxSetValue("${customMap[idx].portletSeq}");
	</c:forEach>
}

function toggleHomeLayout(){
	var afterLogin = $("afterLogin").value;
	if(afterLogin == "home"){
		jQuery("#settingHomeLayout").show();
	} else {
		jQuery("#settingHomeLayout").hide();
	}
}
var portletName;
onloadRedy("init()");
</script>

</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="layoutForm">

		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="menu_conf.home" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
				
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.mailhome.title" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.profile.50" bundle="setting"/></span></div>
					
					<div style="position: relative;">
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
						<th>							
							<tctl:msg key="conf.profile.49" bundle="setting"/>		
						</th>
						<td>
							<div id="afterLoginSelect"></div>							
						</td>
						</tr>
					</table>
					<table class="TB_cols2" id="settingHomeLayout">
						<tr>
						<th>							
							<tctl:msg key="conf.mailhome.portlet.setting" bundle="setting"/>				
						</th>
						<td style="padding:10px;">
						
							<table cellpadding="5" cellspacing="5" border="0" class="TM_layout_setting_table">
								<tr>
								<td width="80px" bgcolor="#FFFFFF"><tctl:msg key="conf.mailhome.setting.001" bundle="setting"/></td>
								<td height="35px" colspan="2" bgcolor="#D8D8D8">
									<tctl:msg key="conf.mailhome.setting.002" bundle="setting"/>
								</td>
								</tr>
								<tr>
								<td rowspan="3" bgcolor="#E8E8E8">
									<tctl:msg key="conf.mailhome.setting.003" bundle="setting"/>									
								</td>
								<td height="20px" colspan="2" bgcolor="#E8E8E8"></td>
								</tr>
								<tr>
								<td width="130px" class="portletContent" align="center">
									<tctl:msg key="conf.mailhome.portlet" bundle="setting"/>(1)
									<div id="portlet1Select" style="text-align: left;"></div>
								</td>
								<td class="portletContent" align="center">
									<tctl:msg key="conf.mailhome.portlet" bundle="setting"/>(2)
									<div id="portlet2Select" style="text-align: left; margin-left:50px;"></div>
								</td>
								</tr>
								<tr>
								<td class="portletContent" align="center">
									<tctl:msg key="conf.mailhome.portlet" bundle="setting"/>(3)
									<div id="portlet3Select" style="text-align: left;"></div>
								</td>
								<td class="portletContent" align="center">
									<tctl:msg key="conf.mailhome.portlet" bundle="setting"/>(4)
									<div id="portlet4Select" style="text-align: left;margin-left:50px;"></div>
								</td>
								</tr>																
							</table>
						
						</td>
						</tr>
					</table>
							
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="modifyLayout()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetLayout()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						</td></tr>
					</table>
					</div>
					
					</div>
					</div>
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>			
</form>
</div>

<%@include file="/common/bottom.jsp"%>

</body>
</html>