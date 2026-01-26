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

<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>

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
LayoutInfo.mode = "send";
<%@include file="userSetting.jsp"%>
function goList(){
	
}

function setSendAddressList(){	
	var toAddr = jQuery.trim(jQuery("#toSendAddress").html());
	var ccAddr = jQuery.trim(jQuery("#ccSendAddress").html());
	var bccAddr = jQuery.trim(jQuery("#bccSendAddress").html());
	var addrStr = "";

	if(toAddr && toAddr != ""){
		toAddr = replaceAll(toAddr,"&lt;","<");
		toAddr = replaceAll(toAddr,"&gt;",">");
		addrStr = toAddr;		
	}	
	if(ccAddr && ccAddr != null){
		ccAddr = replaceAll(ccAddr,"&lt;","<");
		ccAddr = replaceAll(ccAddr,"&gt;",">");
		addrStr += (addrStr != "")?","+ccAddr:ccAddr;
	}
	
	if(bccAddr && bccAddr != null){
		bccAddr = replaceAll(bccAddr,"&lt;","<");
		bccAddr = replaceAll(bccAddr,"&gt;",">");
		addrStr += (addrStr != "")?","+bccAddr:bccAddr;
	}
	setAddressList(getEmailArray(addrStr));	
}

function init() {

	setTopMenu('mail');
	
	mainInitFunc();
	folderInitLoad();
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}

	setWorkTitle("<span class='TM_work_title'>"+mailMsg.mail_send_title+"</span>");
	jQuery(window).autoResize(jQuery("#m_contentBody"), "#copyRight");
	
	loadSendToolBar();	
	var isError = ${sendResult.errorOccur};
	if(isError){
		menuBar.disableToolBarItem("address");
	}else {
		var msgId = jQuery("#send_msg_id").val();
		markSentSeenFlag(msgId);
	}
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
		<td class="TM_main_hframe">			
		<div class="posr">
		<div class="TM_folderInfo posr">				
			<img src="/design/common/image/blank.gif" class="TM_barLeft">								
			<div id="workTitle" class="TM_finfo_data"></div>										
			<img src="/design/common/image/blank.gif" class="TM_barRight">
		</div>
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
		<div class="TM_mainContent">		
		<div id="mailMenubar" >			
			<div class="mail_body_tabmenu posr">
				<div class="mail_body_tab" id="menuBarTab"></div>
				<div>
					<div id="processMessageContent" class="TM_processMessage"></div>
				</div>				
			</div>		
			<div class="mail_body_menu">
				<div class="menu_main_unit" id="menuBarContent"></div>
			</div>
		</div>		
	
		<div id="m_contentBody" >			
			<div id="m_contentMain">
			<div class="TM_content_wrapper">
				<div class="TM_mail_content">	
				
				<div>
					<div class="send_ok_content">
					<form name="sendConfirmForm" id="sendConfirmForm">
						<input type="hidden" id="send_msg_id" value="${sendResult.saveMid}">
						<script type="text/javascript">makePAID();</script>
						<c:if test="${!sendResult.errorOccur}">
							<div id="sendSuccessContent">			
								<ul class="send_ok_title">
									<li class="msg_title">
										<c:choose>
										<c:when test="${sendResult.sendType eq 'reserved'}">
										<tctl:msg key="mail.reserved.success"/>
										</c:when>					
										<c:when test="${sendResult.sendType eq 'draft'}">
										<tctl:msg key="mail.drafts.success"/>
										</c:when>
										<c:otherwise>
											<tctl:msg key="mail.send.success"/>	
										</c:otherwise>
										</c:choose>		
									</li>
									<li class="title_sub">					
										<c:if test="${sendResult.sendType eq 'reserved' || 
														sendResult.sendType eq 'draft' || 
														sendResult.sendType eq 'savesent'}">
										<tctl:msg key="mail.send.success.msg" arg0="${sendResult.sendFolderName}"/>
										</c:if>
									</li>
								</ul>
								<div class="dotLine"></div>
								<div class="send_ok_btnArea">
									<div class="title_arrow4"><tctl:msg key="mail.to"/></div>				
								</div>					
								
								<div class="send_ok_mailList">
									${fn:replace(fn:escapeXml(sendResult.validTo),',','<br>')}
								</div>
								<div id="toSendAddress" style="display:none">
									${fn:escapeXml(sendResult.validTo)}
								</div>	
												
								
								<c:if test="${not empty sendResult.validCc}">
								<div class="send_ok_btnArea">
									<div class="title_arrow4"><tctl:msg key="mail.cc"/></div>				
								</div>					
								<div class="send_ok_mailList">
									${fn:replace(fn:escapeXml(sendResult.validCc),',','<br>')}
								</div>					
								<div id="ccSendAddress" style="display:none">								
									${fn:escapeXml(sendResult.validCc)}
								</div>
								</c:if>
								
								<c:if test="${not empty sendResult.validBcc}">
								<div class="send_ok_btnArea">
									<div class="title_arrow4"><tctl:msg key="mail.bcc"/></div>				
								</div>					
								<div class="send_ok_mailList">								
									${fn:replace(fn:escapeXml(sendResult.validBcc),',','<br>')}
								</div>					
								<div id="bccSendAddress" style="display:none">								
									${fn:escapeXml(sendResult.validBcc)}
								</div>			
								</c:if>
								
								<c:if test="${not empty sendResult.invalidAddress}">
								<div class="send_ok_btnArea">
									<div class="title_arrow4"><tctl:msg key="mail.send.fail.address"/></div>				
								</div>
								<div class="send_ok_mailList">
									${fn:replace(fn:escapeXml(sendResult.invalidAddress),',','<br>')}
								</div>				
									
								</c:if>
							</div>
							
							<div class="senf_after_func">
								<a href="#n" onclick="goWrite();" class="send_after"><tctl:msg key="mail.rewrite"/></a>
								|
								<c:choose>
								<c:when test="${sendResult.sendType eq 'reserved'}">
									<a href="#n" onclick="goFolder('Reserved');" class="send_after"><tctl:msg key="mail.goreserved"/></a>
								</c:when>					
								<c:when test="${sendResult.sendType eq 'draft'}">
									<a href="#n" onclick="goFolder('Drafts');" class="send_after"><tctl:msg key="mail.godraft"/></a>
								</c:when>
								<c:otherwise>
									<a href="#n" onclick="goFolder('Inbox');" class="send_after"><tctl:msg key="mail.goinbox"/></a>	
								</c:otherwise>
								</c:choose>					
								|
								<a href="#n" onclick="goMailHome();" class="send_after"><tctl:msg key="mail.gomailhome"/></a>
							</div>
						</c:if>
						
						<c:if test="${sendResult.errorOccur}">
							<div id="sendErrorContent" var="sendAddr">
								<ul id="sendErrorMsg" class="send_ok_title" >
									<li class="msg_title" style="padding-top:18px"><strong class="red"><tctl:msg key="mail.send.fail"/></strong></li>
									<c:if test="${sendResult.detectVirus}">
									<li class="title_sub">					
										<tctl:msg key="mail.drafts.success"/>
									</li>
									</c:if>
								</ul>
								<div class="dotLine"></div>
								<c:choose>
									<c:when test="${sendResult.detectVirus}">
									<div class="send_ok_btnArea">
										<div class="title_arrow4">
											<tctl:msg key="virus.check.detect.title" bundle="common"/>
										</div>				
									</div>
									<div class="send_ok_mailList">
										${fn:replace(sendResult.errorMessage, "\\n", "<br>")}
									</div>
									</c:when>
									<c:when test="${sendResult.noRcpt}">
									<div class="send_ok_btnArea">
										<div class="title_arrow4"><tctl:msg key="mail.send.fail.norcpt"/></div>				
									</div>
									<div class="send_ok_mailList">
										<tctl:msg key="mail.drafts.success"/>
									</div>								
									</c:when>
									<c:otherwise>
									<div class="send_ok_btnArea">
										<div class="title_arrow4"><tctl:msg key="mail.send.fail.address"/></div>				
									</div>
									<div class="send_ok_mailList">
										${fn:replace(fn:escapeXml(sendResult.invalidAddress),',','<br>')}
									</div>
									 <c:if test="${!empty sendResult.errorMessage}">
                                     <div class="send_ok_btnArea">
                                        <div class="title_arrow4">
                                            <tctl:msg key="mail.send.fail.reason"/>
                                        </div>              
                                     </div>
			                        <div class="send_ok_mailList">
			                            ${fn:replace(sendResult.errorMessage, "\\n", "<br>")}
			                        </div>
				                    </c:if>
									</c:otherwise>
								</c:choose>			
							</div>
							
							<div class="senf_after_func">					
								<a href="#n" onclick="goFolder('Drafts');" class="send_after"><tctl:msg key="mail.godraft"/></a>										
								|
								<a href="#n" onclick="goMailHome();" class="send_after"><tctl:msg key="mail.gomailhome"/></a>
							</div>
						</c:if>			
						
						
					</form>
					</div>
				</div>
				
				</div>
			</div>			
			</div>
		</div>
		</div>
		</div>
	</td>
	</tr>
</table>
<%@include file="/common/bottom.jsp"%>

<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>

<%@include file="/dynamic/mail/mailCommonModal.jsp"%>

<script type="text/javascript">init();</script>
</body>
</html>