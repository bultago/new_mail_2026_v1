<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg"></script>

<script language="javascript">
var agt = "<%=agent%>";
if((agt.indexOf("MSIE") != -1) && (agt.indexOf("Opera") == -1))
	agent = "ie";
else if(agt.indexOf('Gecko') != -1)
	agent = "gecko";
else if(agt.indexOf('Opera') != -1)
    agent = "opera";
else agent = "nav";

function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery(window).trigger("resize");
	
	jQuery("#smsReserv_menu").addClass("on");
	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	
}

<%@ include file="settingFrameScript.jsp" %>
function deleteSmsReserv(requestTime){
	var f = document.smsReservForm;
	
	f.deleteRequestTime.value = requestTime;

	f.action = "/dynamic/ums/deleteSmsReserv.do";
    f.method = "post";
    f.submit();
}
onloadRedy("init()");
</script>

</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="smsReservForm">
		<input type="hidden" name="deleteRequestTime"/>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.smsReserv.menu" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.smsReserv.title" bundle="setting"/></li>					
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
						<div>
							<div class="title_bar"><span><tctl:msg key="conf.smsReserv.list" bundle="setting"/></span></div>
							
		
							<table id="settingListTable" cellpadding="0" cellspacing="0" style="border-top:0px">
								<colgroup span="4">
									<col width="100px"></col>
									<col></col>
									<col width="100px"></col>
									<col width="100px"></col>									
								</colgroup>
								<tr>
									<th><tctl:msg key="conf.smsReserv.date" bundle="setting"/></th>
									<th><tctl:msg key="conf.smsReserv.content" bundle="setting"/></th>
									<th><tctl:msg key="conf.smsReserv.totalCnt" bundle="setting"/></th>
									<th><tctl:msg key="conf.smsReserv.delete" bundle="setting"/></th>
								</tr>
								<c:if test="${empty smsReservList}">
								<tr>
									<td align="center" colspan="4"><tctl:msg key="conf.smsReserv.noList" bundle="setting"/></td>
								</tr>
								</c:if>
								<c:forEach var="smsReservList" items="${smsReservList}">
								<tr>
									<td style="text-align:center">										
										${fn:substring(smsReservList.sendTime,0,11)}
									</td>
									<td style="text-align:left">
										${fn:escapeXml(smsReservList.msgBody)}
									</td>
									<td style="text-align:center">
										${smsReservList.cnt}
									</td>
									<td style="text-align:center">										
										<a href="javascript:;" onclick="deleteSmsReserv('${fn:escapeXml(smsReservList.requestTime)}')" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a>
									</td>
								</tr>
								</c:forEach>
							</table>
							<div style="height: 50px;"></div>
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