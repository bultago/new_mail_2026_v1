<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<% String sessionID = request.getSession().getId(); %>

<div class="TM_content_wrapper" style="padding-bottom:30px">
	<div class="send_ok">
		<div class="send_ok_content">
		<form name="sendConfirmForm" id="sendConfirmForm">
			<input type="hidden" id="paid" name="paid" value="<%=sessionID%>"/>
			<input type="hidden" id="send_msg_id" value="${sendResult.saveMid}">
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
					<a href="#n" onclick="goWritePopup();" class="send_after"><tctl:msg key="mail.rewrite"/></a>					
				</div>
			</c:if>
			
			<c:if test="${sendResult.errorOccur}">
				<div id="sendErrorContent" var="sendAddr">
					<ul id="sendErrorMsg" class="send_ok_title" >
						<li class="msg_title" style="padding-top:18px"><strong class="red"><tctl:msg key="mail.send.fail"/></strong></li>
						<%--<li class="title_sub"><tctl:msg key="mail.send.fail.msg"/></li> --%>
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
			</c:if>
			
		</form>
		</div>
	</div>
</div>

<script type="text/javascript">

function goWritePopup(){
	document.location = "/dynamic/mail/writeMessage.do?wmode=popup&wtype=send";
}

function sendResult(){
	jQuery("#popupTitle").html(mailMsg.mail_send_title);
	loadSendToolBar();	
	var isError = ${sendResult.errorOccur};
	if(isError){
		menuBar.disableToolBarItem("address");
	}else {
		var msgId = jQuery("#send_msg_id").val();
		markSentSeenFlag(msgId);
	}
	jQuery(window).trigger("resize");
}

function setSendAddressList(){	
	var toAddr = jQuery.trim(jQuery("#toSendAddress").html());
	var ccAddr = jQuery.trim(jQuery("#ccSendAddress").html());
	var bccAddr = jQuery.trim(jQuery("#bccSendAddress").html());
	var addrStr = "";

	if(toAddr && toAddr != ""){
		toAddr = replaceAll(toAddr,"&lt;","<");
		toAddr = replaceAll(toAddr,"&gt;",">");
		addrStr = toAddr
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

sendResult();
</script>
<%@include file="/common/xecureOcx.jsp" %>