<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>	
		<script type="text/javascript">
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'><tctl:msg key="mail.send.title"/>"
			+"</a>";				

		}
		function markSentSeenFlag(msgId) {
			var param = {"messageId":msgId};
			jQuery.post("/mail/changeSentMessageFlag.do",param);
		}
		function init() {
			var isError = ${sendResultVO.errorOccur};
			if (!isError) {
				var msgId = jQuery("#send_msg_id").val();
				markSentSeenFlag(msgId);
			}
		}
		</script>
	</head>
	<body onload="init();">
		<%@include file="mail_top.jsp"%>
		<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>
		<script type="text/javascript">makeTopLink();</script>
		
		<form name="sendForm" method="post" target="hidden_frame">
			<input type="hidden" id="send_msg_id" value="${sendResultVO.messageId}"/>						
			<div class="container">				
				<div class="title_box">
					<div class="btn_l"><a href="/mobile/mail/mailMdnList.do" class="btn2" title="<tctl:msg key="mail.receivenoti"/>"><span><tctl:msg key="mail.receivenoti"/></span></a></div><!-- 수신확인  -->
					<div class="btn_r"><a href="/mobile/mail/folderList.do" class="btn2" title="<tctl:msg key="mail.folderlist"/>"><span><tctl:msg key="mail.folderlist"/></span></a></div><!-- 메일함 목록 -->
				</div>				
					
				<c:if test="${!sendResultVO.errorOccur}">
					<c:if test="${!sendResultVO.checkErrorOccur}">
					<div class="msg" id="document_body">
						<p class="simbol ok">o</p>
						<c:if test="${sendResultVO.sendType eq 'drafts'}">
							<h5><tctl:msg key="mail.drafts.success"/></h5>
						</c:if>
						<c:if test="${sendResultVO.sendType eq 'normal'}">
							<h5><tctl:msg key="mail.send.success"/></h5>
						</c:if>
						
						<p class="scrip"><tctl:msg key="mail.send.success.msg" arg0="${sendResultVO.sendFolderName}"/></p>
						<div class="btn_area">
							<a href="/mobile/mail/mailWrite.do" class="btn3" title="<tctl:msg key="mail.rewrite"/>"><span><tctl:msg key="mail.rewrite"/></span></a><!-- 새로운 메일쓰기 -->
							<a href="/mobile/mail/mailList.do?folderName=Inbox" class="btn3" title="<tctl:msg key="mail.goinbox"/>"><span><tctl:msg key="mail.goinbox"/></span></a><!-- 받은메일함 이동 -->
						</div>
					</div>
					</c:if>
					<c:if test="${sendResultVO.checkErrorOccur}">
					<div class="msg" id="document_body">
						<p class="simbol error">!</p>
						<h5><tctl:msg key="mail.send.fail.check"/></h5>
						<p class="scrip">
							<tctl:msg key="mail.send.fail.check.msg"/>
						</p>
						<dl>
						<dt class="invalid_index">
							- <tctl:msg key="mail.send.fail.check.list"/> -
						</dt>
						<dd class="invalid_box">
						<c:forEach items="${sendResultVO.invalidArrayAddrs}" var="address">
							${fn:escapeXml(address)}<br>
						</c:forEach>
						</dd>
						</dl>						
						<div class="btn_area">
							<a href="/mobile/mail/mailWrite.do" class="btn3" title="<tctl:msg key="mail.rewrite"/>"><span><tctl:msg key="mail.rewrite"/></span></a><!-- 새로운 메일쓰기 -->
							<a href="/mobile/mail/mailList.do?folderName=Inbox" class="btn3" title="<tctl:msg key="mail.goinbox"/>"><span><tctl:msg key="mail.goinbox"/></span></a><!-- 받은메일함 이동 -->
						</div>
					</div>
					</c:if>
				</c:if>
				
				<c:if test="${sendResultVO.errorOccur}">
					<div class="msg" id="document_body">
						<p class="simbol error">x</p>
						<h5><tctl:msg key="mail.send.fail"/></h5>
						<p class="scrip">
							<tctl:msg key="mail.send.fail.msg.mobile"/>
						</p>
						<div class="btn_area">
							<a href="/mobile/mail/mailList.do?folderName=Drafts" class="btn3" title="<tctl:msg key="mail.godraft"/>"><span><tctl:msg key="mail.godraft"/></span></a><!-- 임시 보관함이동 -->
							<a href="/mobile/mail/mailList.do?folderName=Inbox" class="btn3" title="<tctl:msg key="mail.goinbox"/>"><span><tctl:msg key="mail.goinbox"/></span></a><!-- 받음메일함이동 -->
						</div>
					</div>
				</c:if>
			</div>					
		</form>
		
		<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>
		<%@include file="/mobile/basic/common/footer.jsp"%>	
			<script type="text/javascript">
				var html = jQuery("body").html();
				parent.setBody(html);
			</script>
	</body>
</html>