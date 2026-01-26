<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">			

			function moveto_page(page) {
				var url="/mobile/mail/mailList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:page});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				
				executeUrl(url, paramArray);
			}

			function readMessage(folderName,uid){
				var f = document.listForm;
				var url="/mobile/mail/mailRead.do";
				var page = f.page.value;
				var paramArray = [];				
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:doubleUrlEncode(folderName)});
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				if ("${listInfoVo.folderName}" == "all") {
					paramArray.push({name:"lfolderName", value:"all"});
				}
				executeUrl(url, paramArray);
			}

			function writeDraftMessage(uid){
				var f = document.listForm;
				var url="/mobile/mail/mailWrite.do";

				var paramArray = [];
				paramArray.push({name:"wtype", value:"drafts"});
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"folderName", value:"Drafts"});
				executeUrl(url, paramArray);	
				
			}

			function deleteMessage() {
				var f = document.listForm;
				if (checkedCnt(f.msgId) == 0) {
					alert('<tctl:msg key="mail.nomessage"/>');
					return;
				}

				if (!confirm('<tctl:msg key="confirm.delete"/>')) {
					return;
				}

				var uids;
				var folderNames;
				var datas = makeMultiValue(f.msgId);
				if (datas.length > 0) {
					for (var i = 0 ; i < datas.length ; i++){
						if (datas[i].name == 'uid') {
							uids = datas[i].value;
						} else if (datas[i].name == 'folderName') {
							folderNames = datas[i].value;
						}
					}
				}

				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"delete"});
				paramArray.push({name:"uids", value:uids});
				if ("${listInfoVo.folderName}" == "all") {
					paramArray.push({name:"folderNames", value:doubleUrlEncode(folderNames)});
				} else {
					paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				}

				executeUrl(url, paramArray);
			}

			function moveMessage(folderName) {
				var f = document.listForm;
				if (checkedCnt(f.msgId) == 0) {
					alert('<tctl:msg key="mail.nomessage"/>');
					return;
				}				
				var uids;
				var folderNames;
				var datas = makeMultiValue(f.msgId);
				if (datas.length > 0) {
					for (var i = 0 ; i < datas.length ; i++){
						if (datas[i].name == 'uid') {
							uids = datas[i].value;
						} else if (datas[i].name == 'folderName') {
							folderNames = datas[i].value;
						}
					}
				}

				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"targetFolderName", value:doubleUrlEncode(folderName)});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"move"});
				paramArray.push({name:"uids", value:uids});
				if ("${listInfoVo.folderName}" == "all") {
					paramArray.push({name:"folderNames", value:doubleUrlEncode(folderNames)});
				} else {
					paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				}

				executeUrl(url, paramArray);
			}

			function flagMessage(flagUse, msgInfo){
				var f = document.listForm;
				var data = msgInfo.split("|");
				var uids = data[1];
				var folderName = data[0];

				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:doubleUrlEncode(folderName)});				
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"flag"});
				paramArray.push({name:"uids", value:uids});
				paramArray.push({name:"flagType", value:"F"});
				paramArray.push({name:"flagUse", value:(flagUse)?false:true});
				
				if ("${listInfoVo.folderName}" == "all") {
					paramArray.push({name:"folderNames", value:doubleUrlEncode(folderName)});
				} else {
					paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				}			
				executeUrl(url, paramArray);
			}

			var seenFlagList = [];
			seenFlagList.push({id:"fc_0",name:"<tctl:msg key="menu.flag.read"/>",fullname:"",depth:0,link:"javascript:changeSeenFlag(true)"});
			seenFlagList.push({id:"fc_1",name:"<tctl:msg key="menu.flag.unread"/>",fullname:"",depth:0,link:"javascript:changeSeenFlag(false)"});

			function changeSeenFlag(flagUse){
				var f = document.listForm;
				if (checkedCnt(f.msgId) == 0) {
					alert('<tctl:msg key="mail.nomessage"/>');
					return;
				}				
				var uids;
				var folderNames;
				var datas = makeMultiValue(f.msgId);
				if (datas.length > 0) {
					for (var i = 0 ; i < datas.length ; i++){
						if (datas[i].name == 'uid') {
							uids = datas[i].value;
						} else if (datas[i].name == 'folderName') {
							folderNames = datas[i].value;
						}
					}
				}

				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});				
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"flag"});
				paramArray.push({name:"flagType", value:"S"});
				paramArray.push({name:"flagUse", value:flagUse});
				paramArray.push({name:"uids", value:uids});
				if ("${listInfoVo.folderName}" == "all") {
					paramArray.push({name:"folderNames", value:doubleUrlEncode(folderNames)});
				} else {
					paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				}

				executeUrl(url, paramArray);
			}

			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'>${fn:escapeXml(listInfoVo.folderAliasName)}"
				<c:if test="${listInfoVo.folderName != 'all'}">
				+" <span>${listInfoVo.unreadCnt}</span> / <span>${listInfoVo.totalCnt}</span>"
				</c:if>
				+"</a>";				

			}
		</script>
	</head>
	<body>
		<div class="m_tms_wrap">		
			<%@include file="mail_top.jsp"%>
			<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>		
			<script type="text/javascript">makeTopLink();</script>
			<div class="container">
				<form name="listForm">
				<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}"/>
				<input type="hidden" name="page" value="${fn:escapeXml(page)}"/>
				<input type="hidden" name="folderName" value="${fn:escapeXml(folderName)}"/>
				<input type="hidden" name="sortBy" value="${fn:escapeXml(sortBy)}"/>
				<input type="hidden" name="sortDir" value="${fn:escapeXml(sortDir)}"/>
				<input type="hidden" name="flag" value="${fn:escapeXml(flag)}"/>
				<input type="hidden" name="uid" value=""/>			
				
				<div class="title_box">
					<div class="btn_l"><a href="/mobile/mail/folderList.do" class="btn2" title="<tctl:msg key="mail.folderlist"/>"><span><tctl:msg key="mail.folderlist"/></span></a></div>
					<div class="btn_r">
						<a href="/mobile/mail/mailWrite.do" class="btn2" title="<tctl:msg key="mail.write"/>"><span><tctl:msg key="mail.write"/></span></a>						
					</div>
				</div>					
				<div class="list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.listForm.msgId);" id="all_select1" check="off" class="btn3" title="<tctl:msg key="mail.select.all"/>"><span><tctl:msg key="mail.select.all"/></span></a>
						<a href="javascript:deleteMessage();"  class="btn3" title="<tctl:msg key="menu.delete"/>"><span><tctl:msg key="menu.delete"/></span></a>						
						<div>
						</div>	
					</div>
					<div class="btn_r">
						<a href="javascript:void(0);" onclick="viewSelectMenu(event,'flag');" class="btn_dr3" title="<tctl:msg key="menu.flag"/>"><tctl:msg key="menu.flag"/></a>
						<a href="javascript:void(0);" onclick="viewSelectMenu(event,'move');" class="btn_dr3" title="<tctl:msg key="menu.move"/>"><tctl:msg key="menu.move"/></a>						
					</div>
				</div>				

				<ul class="mail_list" id="document_body">
					<c:if test="${empty listInfoVo || empty listInfoVo.listContents}">
						<li class="messageNone"><tctl:msg key="mail.notexist"/></li>
					</c:if>
					<c:if test="${!empty listInfoVo && !empty listInfoVo.listContents}">
						<c:forEach var="listContent" items="${listInfoVo.listContents}">
							<li class="<c:if test="${!fn:contains(listContent.flag, 'S')}">mail_unseen</c:if>">								
								<input type="checkbox" id="chk_${listContent.id}" name="msgId" class="mail_state" value="${listContent.folderName}|${listContent.id}"/>
								<c:if test="${listInfoVo.folderName ne 'Drafts'}">
								<a href="javascript:readMessage('${listContent.folderName}','${listContent.id}');" class="mail_read_link  <c:if test="${!fn:contains(listContent.flag, 'S')}">unseen</c:if>">
								</c:if>
								<c:if test="${listInfoVo.folderName eq 'Drafts'}">
								<a href="javascript:writeDraftMessage('${listContent.id}');" class="mail_read_link  <c:if test="${!fn:contains(listContent.flag, 'S')}">unseen</c:if>">
								</c:if>
									<span class="mail_subject <c:if test="${fn:contains(listContent.flag, 'T')}">file</c:if>">
										<c:if test="${empty listContent.subject}">
											<tctl:msg key="header.nosubject"/>
										</c:if>
										<c:if test="${!empty listContent.subject}">
											${fn:escapeXml(listContent.subject)}
										</c:if>
									</span>
									<span style="display:none">|</span>
									<span class="mail_name <c:if test="${!fn:contains(listContent.flag, 'S')}">icon_unread</c:if> <c:if test="${fn:contains(listContent.flag, 'S')}">icon_read</c:if>">
										<c:if test="${listInfoVo.folderName == 'all'}">
											<c:choose>
												<c:when test="${listContent.folderName eq 'Inbox'}">
											 		<tctl:msg key="folder.inbox" />
												</c:when>
												<c:when test="${listContent.folderName eq 'Sent'}">
											 		<tctl:msg key="folder.sent" />
												</c:when>
												<c:when test="${listContent.folderName eq 'Drafts'}">
											 		<tctl:msg key="folder.drafts" />
												</c:when>
												<c:when test="${listContent.folderName eq 'Reserved'}">
											 		<tctl:msg key="folder.reserved" />
												</c:when>
												<c:when test="${listContent.folderName eq 'Spam'}">
											 		<tctl:msg key="folder.spam" />
												</c:when>
												<c:when test="${listContent.folderName eq 'Trash'}">
											 		<tctl:msg key="folder.trash" />
												</c:when>
												<c:otherwise>
													${listContent.folderFullName}
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${listInfoVo.folderName != 'all'}">
											<c:if test="${not empty listContent.name}">${listContent.name}</c:if>
											<c:if test="${empty listContent.name}">${listContent.email}</c:if>											
										</c:if>
									</span>									
								</a>
								<span style="display:none">|</span>
								<a class="btn_flag <c:if test="${fn:contains(listContent.flag, 'F')}">flag_on</c:if>" href="#n" onclick="flagMessage(<c:if test="${fn:contains(listContent.flag, 'F')}">true</c:if><c:if test="${!fn:contains(listContent.flag, 'F')}">false</c:if>,'${listContent.folderName}|${listContent.id}')">
									&nbsp;
									<span style="display:none"><tctl:msg key="menu.quick.flag"/>
									<c:if test="${fn:contains(listContent.flag, 'F')}"><tctl:msg key="comn.enabled" bundle="common"/></c:if>
									<c:if test="${!fn:contains(listContent.flag, 'F')}"><tctl:msg key="comn.disabled" bundle="common"/></c:if>
									</span>
								</a>
								<span style="display:none">|</span>
								<span class="date">
									<c:if test="${!empty listContent.date}">
										<fmt:parseDate var="parseDate" value="${listContent.date}" pattern="yyyyMMdd HH:mm:ss" />
										<c:if test="${fn:contains(listContent.date, thisDate)}">
											<fmt:formatDate value="${parseDate}" pattern="HH:mm"/>
										</c:if>
										<c:if test="${!fn:contains(listContent.date, thisDate) && fn:contains(listContent.date, fn:substring(thisDate,0,4))}">
											<fmt:formatDate value="${parseDate}" pattern="MM/dd"/>
										</c:if>
										<c:if test="${!fn:contains(listContent.date, fn:substring(thisDate,0,4))}">
											<fmt:formatDate value="${parseDate}" pattern="yy/MM/dd"/>
										</c:if>
									</c:if>
								</span>
							</li>
						</c:forEach>
					</c:if>
				</ul>
					
				<%@include file="/mobile/basic/common/pageCounter.jsp"%>
					
				<div class="list_box second_list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.listForm.msgId);" id="all_select2" check="off" class="btn3" title="<tctl:msg key="mail.select.all"/>"><span><tctl:msg key="mail.select.all"/></span></a>
						<a href="javascript:deleteMessage();"  class="btn3"><span><tctl:msg key="menu.delete"/></span></a>
					</div>
					<div class="btn_r">
						<a href="javascript:void(0);" onclick="viewSelectMenu(event,'flagBottom');" class="btn_dr3" title="<tctl:msg key="menu.flag"/>"><tctl:msg key="menu.flag"/></a>
						<a href="javascript:void(0);" onclick="viewSelectMenu(event,'moveBottom');" class="btn_dr3" title="<tctl:msg key="menu.flag"/>"><tctl:msg key="menu.move"/></a>
					</div>					
				</div>
			</form>
			</div>
			
			<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>

			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>