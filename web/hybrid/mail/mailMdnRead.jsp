<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		
		<script type="text/javascript">			
			function moveto_page(page) {
				var url="/hybrid/mail/mailMdnRead.do";
				var f = document.mdnForm;
				var uid = f.uid.value;

				var paramArray = [];
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"page", value:page});
				paramArray.push({name:"listPage", value:"${fn:escapeXml(listPage)}"});
				
				executeUrl(url, paramArray);
			}

			function goPage(uid){
				var url="/hybrid/mail/mailMdnRead.do";
				var f = document.mdnForm;				
				var paramArray = [];
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"page", value:1});
				paramArray.push({name:"listPage", value:"${fn:escapeXml(listPage)}"});

				executeUrl(url, paramArray);			
			}

			function recallMsg(email) {
				var f = document.mdnForm;
				var uid = f.uid.value;
				var messageID = f.messageID.value;
				var url="/hybrid/mail/mailMdnRecall.do";
			
				var paramArray = [];
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"mid", value:messageID});
				paramArray.push({name:"recallEmails", value:email});
				
				executeUrl(url, paramArray);
			}

			function goMdnList(){
				var url = "/hybrid/mail/mailMdnList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(listPage)}"});
				
				executeUrl(url, paramArray);
			}

			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'><tctl:msg key="mail.receivenoti"/>"
				+"</a>";				

			}
		</script>
	</head>
	<body>
		<div class="wrapper">	
			<%@include file="mail_top.jsp"%>
			<%@include file="/hybrid/mail/mail_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			<div class="container">
				<div class="title_box">
					<div class="btn_l">
						<%--<c:if test="${!empty mdnContent.preUid && mdnContent.preUid > 0}">
						<a href="javascript:goPage(${mdnContent.preUid});" class="btn2"><strong>&lt;</strong></a>
						</c:if> --%>
						<a href="javascript:goMdnList();" class="btn2" title="<tctl:msg key="mail.viewlist"/>"><span><tctl:msg key="mail.viewlist"/></span></a>
						<%--<c:if test="${!empty mdnContent.nextUid && mdnContent.nextUid > 0}">
						<a href="javascript:goPage(${mdnContent.nextUid});" class="btn2"><strong>&gt;</strong></a>
						</c:if>--%>
					</div>
					<div class="btn_r"><a id="link_2" href="/hybrid/mail/mailWrite.do" class="btn2" title="<tctl:msg key="mail.write"/>"><span><tctl:msg key="mail.write"/></span></a></div>
				</div>

				<form name="mdnForm">
				<input type="hidden" name="page" value="${fn:escapeXml(page)}"/>
				<input type="hidden" name="listPage" value="${fn:escapeXml(listPage)}"/>
				
				<input type="hidden" name="uid" value="${fn:escapeXml(uid)}"/>
				<input type="hidden" name="messageID" value="${mdnContent.messageID}"/>
				
				<ul class="mail_list" id="document_body">
					<li class="mdn_view_subject">
						<span class="mail_read_link">
							<span class="mail_name">${mdnContent.sendDate}</span>
							<span class="mail_subject">
								<c:if test="${empty mdnContent.messageTitle}">
									<tctl:msg key="header.nosubject"/>
								</c:if>
								<c:if test="${!empty mdnContent.messageTitle}">
									${fn:escapeXml(mdnContent.messageTitle)}
								</c:if>
							</span>
						</span>
					</li>
				</ul>
				
				<c:if test="${!empty mdnContent && !empty mdnContent.rcptVos}">
				<ul class="mail_list">
					<c:forEach var="rcpt" items="${mdnContent.rcptVos}">
						<li>
							<span class="mdn_rdate mdn_one" style="top:25px;">
								<c:choose>
									<c:when test="${rcpt.localDomain && 
													(rcpt.code eq '200' || 
													 rcpt.code eq '201' || 
													 rcpt.code eq '300')}">
										<a href="javascript:recallMsg('${fn:escapeXml(rcpt.address)}');" class="btn3"><tctl:msg key="mail.mdn.recall"/></a>
									</c:when>
									<c:otherwise>&nbsp;</c:otherwise>
								</c:choose>
							</span>
							<span class="date">&nbsp;</span>
							<span class="mail_read_link">
							<span class="mdn_addr">${fn:escapeXml(rcpt.printAddress)}</span>
							<span class="mail_subject">
								<c:if test="${rcpt.code eq '1' || rcpt.code eq '1000'}">
									${rcpt.message}
								</c:if>
								<c:if test="${not empty rcpt.message && rcpt.code ne '1' && rcpt.code ne '1000'}">				
									<tctl:msg key="${rcpt.message}" />
								</c:if>
								[
								<span class="mdn_one">
									<tctl:msg key="${rcpt.status}" />
								</span>
								]
							</span>
							</span>
						</li>
					</c:forEach>
					</ul>
				</c:if>
				</form>
				
				<%@include file="/hybrid/common/pageCounter.jsp"%>
			</div>
			
			<%@include file="/hybrid/mail/mail_body_footer.jsp"%>

			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>