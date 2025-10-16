<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script type="text/javascript">
function closeWin(){
	if(parent){
		parent.modalPopupWritePreviewClose(); 
		parent.jQpopupClear();
	}	
	else
		window.close();	
}

function getMessageText(){
	return $("messageText").value;
}

function resizeTextFrame(height,width){
	/*
	$("messageContentFrame").style.height=height+"px";
	winResize();
	*/
}
if(parent)
	parent.setWritePreviewTitle("<tctl:msg key="mail.preview"/>");
	
</script>
</head>
<body class="popupBody" style="background:none;">


<div class="popup_style2" style="border:0px;">
	<div class="title" style="display:none;">
		<span><tctl:msg key="mail.preview"/></span>
		<a class="btn_X" href="javascript:;" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>
	<div class="TM_modal_content_wrapper" style="height:595px;overflow-y:auto;">	
		
		<table width="100%" cellpadding="0"  cellspacing="0" class="TM_modalTable">
			<col width="150px"></col>
			<col ></col>
			
			<tr>
			<td class="pindex"><tctl:msg key="mail.subject" /></td>
			<td class="pcontent"><div title="${fn:escapeXml(subject)}" >${fn:escapeXml(subject)}&nbsp;</div></td>
			</tr>
			
			<tr>
			<td class="pindex"><tctl:msg key="mail.from" /></td>
			<td class="pcontent"><div title="${fn:replace(from, '"', '\'')}" >${from}&nbsp;</div></td>
			</tr>
			
			<tr>
			<td class="pindex"><tctl:msg key="mail.to" /></td>
			<td class="pcontent"><div title="${fn:replace(fn:escapeXml(to), '"', '\'')}" >${fn:escapeXml(to)}&nbsp;</div></td>
			</tr>
			
			<c:if test="${not empty cc}">
			<tr>
			<td class="pindex"><tctl:msg key="mail.cc" /></td>
			<td class="pcontent"><div title="${fn:replace(fn:escapeXml(cc), '"', '\'')}"  >${fn:escapeXml(cc)}&nbsp;</div></td>
			</tr>
			</c:if>
			
			<c:if test="${not empty bcc}">
			<tr>
			<td class="pindex"><tctl:msg key="mail.bcc" /></td>
			<td class="pcontent"><div title="${fn:replace(fn:escapeXml(bcc), '"', '\'')}" >${fn:escapeXml(bcc)}&nbsp;</div></td>
			</tr>
			</c:if>
			
			<tr>
			<td colspan="2" class="pcontent">
				<textarea id="messageText" style="display:none;">
					<c:out value="${content}" escapeXml="true"/>
				</textarea>
				
				<%--<iframe frameborder="0" style='overflow-x:auto;overflow-y:hidden' width="98%" height="300px"
							src="/dynamic/mail/messageContent.html" id="messageContentFrame">--%>
					<iframe frameborder="0" style='overflow-x:auto;overflow-y:hidden' width="100%" height="470px"
							src="/dynamic/mail/messageContent.html" id="messageContentFrame">
							
			</iframe>
			</td>			
			</tr>						
		
		</table>
				
	</div>	
	<%--<div class="dotLine"></div>--%>
	<div class="btnArea">
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</div>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>