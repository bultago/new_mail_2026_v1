<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/popup_style.css" />

<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
</head>

<script language="javascript">
function init() {
	/*
	setTimeout(function(){
		winResize();
	},500);
	*/
	var title ="<tctl:msg key="conf.pop.44" bundle="setting"/>";
	if(parent)
		parent.setExtPop3PopupTitle(title);
}

function resetForm() {
	var f = document.extMailForm;
	f.reset();
}

function getPop3() {
	var f = document.extMailForm;

	if (checkedCnt(f.rules) == 0) {
		alert(settingMsg.common_5);
		return;
	}
	
	f.action = "/setting/viewSelectedExtMail.do";
	f.method="post";
    f.submit();
}

function closeWin(){
	if(parent){
		parent.modalPopupForExtPop3Close(); 
		parent.jQpopupClear();
	}else
		window.close();	
}
</script>

<body onload="javascript:init();" class="popupBody" style="background:none;">

<form name="extMailForm">
	<div class="popup_style1" style="border:0px;background:none;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="conf.pop.44" bundle="setting"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="window.close()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>

		<table class="TB_popup_style1_body" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td class="bg_left" style="display:none;" />
					<td class="popup_content" style="padding-bottom:0px;">
						<div class="sub_toolbar" style="border-top:1px solid #ACACAC;display:none;">
							<a href="#" onclick="getPop3()" class="btn_basic"><span><tctl:msg key="common.16" bundle="setting"/></span></a>
						</div>
						<table id="settingListTable" cellpadding="0" cellspacing="0" style="">
							<colgroup span="7">
								<col width="30px"></col>
								<col></col>
								<col width="80px"></col>
								<col width="150px"></col>
								<col width="120px"></col>
								<col width="100px"></col>
							</colgroup>
							<tr>
								<th><input type="checkbox" onclick="checkAll(this,extMailForm.rules)"/></th>
								<th><tctl:msg key="conf.pop.31" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.39" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.32" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.30" bundle="setting"/></th>
								<th><tctl:msg key="conf.pop.24" bundle="setting"/></th>
							</tr>
							<c:if test="${empty pop3List}">
							<tr>
								<td colspan="6" align="center"><tctl:msg key="conf.pop.list.empty" bundle="setting"/></td>
							</tr>
							</c:if>
							<c:forEach var="pop" items="${pop3List}">
							<tr>
								<td align="center">
									<input type="checkbox" name="rules" value="${fn:escapeXml(pop.pop3Host)}|${fn:escapeXml(pop.pop3Id)}">
								</td>
								<td>
									<table class='TM_HiddenTextTable'>
										<tr>
											<td style="border:0;" title="${fn:escapeXml(pop.pop3Host)}">
												<div class='TM_HiddenTextDiv'>${fn:escapeXml(pop.pop3Host)}</div>
											</td>
										</tr>
									</table>
								</td>
								<td>${fn:escapeXml(pop.pop3Port)}</td>
								<td>
								<table class='TM_HiddenTextTable'>
                                    <tr>
                                        <td style="border:0;" title="${fn:escapeXml(pop.pop3Id)}">
                                            <div class='TM_HiddenTextDiv' style="text-align:center;">${pop.pop3Id}</div>
                                        </td>
                                    </tr>
                                </table>
								</td>
								<td>
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
								<td>
									<c:if test="${pop.pop3Del == '1'}">O</c:if>
									<c:if test="${pop.pop3Del != '1'}">X</c:if>
								</td>
							</tr>
							</c:forEach>
						</table>
					</td>
					<td class="bg_right" style="display:none;"/>
				</tr>
			</tbody>
		</table>
		<div class="popup_style1_down" style="display:none;">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>
	<div class="btnArea">
		<a href="javascript:;" onclick="getPop3()" class="btn_style2"><span><tctl:msg key="common.16" bundle="setting"/></span></a>
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>