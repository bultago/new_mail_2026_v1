<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">			
			function moveto_page(page) {
				var url="/mobile/mail/mailMdnList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:page});
				
				executeUrl(url, paramArray);
			}

			function viewMDN(uid) {
				var url="/mobile/mail/mailMdnRead.do";
				var paramArray = [];
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"listPage", value:"${fn:escapeXml(page)}"});
				
				executeUrl(url, paramArray);
			}

			function deleteMessage() {
				var f = document.mdnForm;
				if (checkedCnt(f.msgId) == 0) {
					alert('<tctl:msg key="mail.nomessage"/>');
					return;
				}

				if (!confirm('<tctl:msg key="confirm.delete"/>')) {
					return;
				}

				var uids;
				var datas = makeMultiValue(f.msgId);
				if (datas.length > 0) {
					for (var i = 0 ; i < datas.length ; i++){
						if (datas[i].name == 'uid') {
							uids = datas[i].value;
						}
					}
				}

				var url="/mobile/mail/mailMdnWork.do";
				var paramArray = [];
				paramArray.push({name:"workMode", value:"delete"});
				paramArray.push({name:"uids", value:uids});
				paramArray.push({name:"folderNames", value:doubleUrlEncode("Sent")});
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
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
			<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			<form name="mdnForm">
				<input type="hidden" name="page" value="${fn:escapeXml(page)}"/>
			<div class="container">
				<div class="title_box">
					<div class="btn_l"><a href="/mobile/mail/folderList.do" class="btn2" title="<tctl:msg key="mail.folderlist"/>"><span><tctl:msg key="mail.folderlist"/></span></a></div>
					<div class="btn_r"><a href="/mobile/mail/mailWrite.do" class="btn2" title="<tctl:msg key="mail.write"/>"><span><tctl:msg key="mail.write"/></span></a></div>
				</div>					
				<div class="list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.mdnForm.msgId);" id="all_select1" check="off" class="btn3" title="<span><tctl:msg key="mail.select.all"/>"><span><tctl:msg key="mail.select.all"/></span></a>
						<a href="javascript:deleteMessage()" class="btn3" title="<tctl:msg key="menu.delete"/>"><span><tctl:msg key="menu.delete"/></span></a>
					</div>
				</div>				

				<ul class="mail_list" id="document_body">
					<c:if test="${empty mdnListInfo || empty mdnListInfo.mdnList}">
						<li class="messageNone"><tctl:msg key="mail.nomessage"/></li>
					</c:if>
					<c:if test="${!empty mdnListInfo && !empty mdnListInfo.mdnList}">
						<c:forEach var="mdn" items="${mdnListInfo.mdnList}">
							<li>
								<input type="checkbox" id="chk_${mdn.uid}" name="msgId" class="mail_state" value="Sent|${mdn.uid}"/>								
								<c:if test="${mdn.mdnCnt > 0}">
								<a href="javascript:viewMDN('${mdn.uid}');" class="mail_read_link">									
									<span class="mail_subject">
										<c:if test="${empty mdn.subject}">
											<tctl:msg key="header.nosubject"/>
										</c:if>
										<c:if test="${!empty mdn.subject}">
											${fn:escapeXml(mdn.subject)}
										</c:if>
									</span>
									<span style="display:none">|</span>
									<span class="mail_name">${mdn.to}</span>
								</a>
								</c:if>
								<c:if test="${mdn.mdnCnt <= 0}">
								<span class="mail_read_link">									
									<span class="mail_subject">${mdn.subject}</span>
									<span style="display:none">|</span>
									<span class="mail_name">${mdn.to}</span>
								</span>
								</c:if>
								<span style="display:none">|</span>
								<span class="mdn_rdate <c:if test="${mdn.mdnCnt == 1}">mdn_one</c:if>">
									<c:if test="${mdn.mdnCnt <= 0}">
									<tctl:msg key="mail.mdn.notselect"/>
									</c:if>
									<c:if test="${mdn.mdnCnt == 1}">
										<c:choose>	
										     <c:when test="${mdn.code == '1'}">
										     	<tctl:msg key="mail.mdn.recall"/>
										     </c:when>
										     <c:when test="${mdn.code == '1000'}">
										     	<c:if test="${fn:contains(mdn.rdate,':')}">
													${fn:substring(mdn.rdate,0,5)}
												</c:if>
												<c:if test="${fn:contains(mdn.rdate,'/')}">
													${fn:substring(mdn.rdate,2,10)}
												</c:if>
										     </c:when>
										     <c:when test="${mdn.code == '100'}">
										     	<tctl:msg key="mail.mdn.wait"/>
										     </c:when>
										     <c:when test="${mdn.code == '200'}">
										     	<tctl:msg key="mail.mdn.unseen"/>
										     </c:when>
										     <c:when test="${mdn.code == '201'}">
										     	<tctl:msg key="mail.mdn.unseen"/>
										     </c:when>
										     <c:when test="${mdn.code == '300'}">
										     	<tctl:msg key="mail.mdn.unseen"/>
										     </c:when>
										     <c:otherwise>
										    	<tctl:msg key="mail.mdn.fail"/>
										     </c:otherwise>
									     </c:choose>
									</c:if>
									<c:if test="${mdn.mdnCnt > 1}">
									${mdn.readCnt} / ${mdn.mdnCnt}
									</c:if>
								</span>
								<span style="display:none">|</span>
								<span class="date">
									<c:if test="${fn:contains(mdn.date,':')}">
										${fn:substring(mdn.date,0,5)}
									</c:if>
									<c:if test="${fn:contains(mdn.date,'/')}">
										${fn:substring(mdn.date,2,10)}
									</c:if>
								</span>
							</li>
						</c:forEach>
					</c:if>
				</ul>
					
				<%@include file="/mobile/basic/common/pageCounter.jsp"%>
					
				<div class="list_box second_list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.mdnForm.msgId)" id="all_select2" check="off" class="btn3" title="<tctl:msg key="mail.select.all"/>"><span><tctl:msg key="mail.select.all"/></span></a>
						<a href="javascript:deleteMessage()" class="btn3" title="<tctl:msg key="menu.delete"/>"><span><tctl:msg key="menu.delete"/></span></a>
					</div>										
				</div>
			</div>
			</form>
			<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>
			
			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>