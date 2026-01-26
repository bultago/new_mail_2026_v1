<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.mail.*"%>
<%@	page import="com.terracetech.tims.webmail.mail.manager.TMailStoreFactory" %>
<%@	page import="com.terracetech.tims.webmail.mail.manager.Pop3Manager" %>
<%@	page import="com.terracetech.tims.webmail.setting.vo.Pop3VO" %>
<%@	page import="com.terracetech.tims.mail.TMailStore" %>
<%@	page import="com.terracetech.tims.mail.TMailFolder" %>
<%@	page import="com.terracetech.tims.mail.TMailMessage" %>
<%@	page import="com.terracetech.tims.webmail.common.MakeMessage" %>
<%@	page import="com.terracetech.tims.common.I18nResources" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.mail.MessagingException"%><html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/popup_style.css" />

<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>

<script language="javascript">
function init() {
	getPopMessage();
	//winResize();
}

function getPopMessage() {
	var f = document.pop3Form;
	f.action = "/mail/receivePopMessage.do";
	f.target = "pop3Process";
	f.method = "post";
	f.submit();

	jQuery(".loader").show();
}

function closeWin(){
	if(parent){
		parent.modalPopupForExtPop3Close(); 
		parent.jQpopupClear();
	}else
		window.close();	
}
</script>
</head>

<body onload="init()" class="popupBody" style="background:none;">
	<form name="pop3Form">
	<div class="popup_style1" style="border:0px;background:none;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left" style="display:none;">
				<span class="SP_title"><tctl:msg key="conf.pop.44" bundle="setting"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>

		<table class="TB_popup_style1_body">
			<tbody>
				<tr>
					<td class="bg_left" style="display:none;" />
					<td class="popup_content" style="padding-bottom:0px;">
						<table id="settingListTable" style="">
							<colgroup span="5">
								<col width="130px"></col>
								<col width="100px"></col>
								<col width="130px"></col>
								<col width="110px"></col>
								<col></col>
							</colgroup>
							<tr>
								<th><tctl:msg key="conf.pop.31" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.32" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.30" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.46" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.45" bundle="setting"/></th>
							</tr>
							<c:if test="${empty pop3List}">
							<tr>
								<td colspan="5" align="center"><tctl:msg key="conf.pop.list.empty" bundle="setting"/></td>
							</tr>
							</c:if>
							<c:forEach var="pop" items="${pop3List}" varStatus="loop">
								<input type="hidden" name="rules" value="${fn:escapeXml(pop.pop3Host)}|${fn:escapeXml(pop.pop3Id)}">
							<tr>
								<td>
									<table class='TM_HiddenTextTable'>
										<tr>
											<td style="border:0;" title="${fn:escapeXml(pop.pop3Host)}">
												<div class='TM_HiddenTextDiv'>${fn:escapeXml(pop.pop3Host)}</div>
											</td>
										</tr>
									</table>
								</td>
								<td>
                                <table class='TM_HiddenTextTable'>
                                    <tr>
                                        <td style="border:0;" title="${fn:escapeXml(pop.pop3Id)}}">
                                            <div class='TM_HiddenTextDiv' style="text-align:center;">${fn:escapeXml(pop.pop3Id)}</div>
                                        </td>
                                    </tr>
                                </table>
                                </td>
								<td>
									<img src='/design/common/image/ajax-loader.gif' align='absmiddle' vapsce='5' id="pop3_loader_${loop.index}" class="loader" style="display:none"/>
									<c:set var="tmpTarget" value="${fn:escapeXml(pop.pop3Boxname)}"/>
								    <c:set var="index" value="${fn:indexOf(tmpTarget, ' ')}"/>
								    <c:set var="target" value="${fn:substring(tmpTarget,index+1,-1)}"/>
								    <c:choose>	
									     <c:when test="${target == 'Inbox'}">
									     <tctl:msg key="conf.filter.39" bundle="setting"/>
									     </c:when>
									     <c:when test="${target == 'Sent'}">
									     <tctl:msg key="conf.filter.38" bundle="setting"/>
									     </c:when>
									     <c:when test="${target == 'Trash'}">
									     <tctl:msg key="conf.filter.41" bundle="setting"/>
									     </c:when>
									     <c:when test="${target == 'Spam'}">
									     <tctl:msg key="conf.filter.42" bundle="setting"/>
									     </c:when>
									     <c:when test="${target == 'Reserved'}">
									     <tctl:msg key="conf.filter.43" bundle="setting"/>
									     </c:when>
									     <c:when test="${target == 'Drafts'}">
									     <tctl:msg key="conf.filter.40" bundle="setting"/>
									     </c:when>
									     <c:otherwise>
									     ${fn:replace(target,".","/")}
									     </c:otherwise>
								     </c:choose>
								</td>
								<td style="text-align:center;">
									<strong class="orange"><span id="success_${loop.index}">0</span></strong><tctl:msg key="mail.countunit"/> /
									<span id="total_${loop.index}">0</span><tctl:msg key="mail.countunit"/>
									<p><tctl:msg key="conf.pop.54" bundle="setting"/> : <span id="fail_${loop.index}">0</span><tctl:msg key="mail.countunit"/></p>
								</td>
								<td id="graph_wrap_${loop.index}" class="TD_bar last" style="text-align:center;">
									<div class="TM_extmail_graphBox" style="width:170px;margin-left:25px;">						
										<div id="graph_${loop.index}" class="TM_quotaGraphBar"></div>										
										<div class="TM_capacity">
											<span id="percent_${loop.index}">0%</span>
										</div>
									</div>
								</td>
							</tr>
							</c:forEach>
						</table>
					</td>
					<td class="bg_right" style="display:none;" />
				</tr>
			</tbody>
		</table>
		<div class="popup_style1_down" style="display:none;">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>
	<div class="btnArea">
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</form>
<iframe name="pop3Process" src="about:blank" width="0px" height="0px" frameborder="0"></iframe>
<%@include file="/common/xecureOcx.jsp" %>	
</body>
</html>