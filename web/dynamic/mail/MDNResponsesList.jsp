<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@include file="/common/authHeader.jsp"%>
<script type="text/javascript">
	function displayMDN(lid,uid, mid, fd, code) {
		var pstr = "";

		if (code == "1") {
			pstr += mailMsg.mail_mdn_recall;
		} else if (code == "1000") {
			pstr += fd;
		} else if (code == "100") {
			pstr += mailMsg.mail_mdn_wait;
		} else if (code == "200" || code == "201" || code == "300") {
			pstr += mailMsg.mail_mdn_unseen;
		} else {
			pstr += mailMsg.mail_mdn_fail;
		}
		
		jQuery("#"+lid).html(pstr);
	}	
</script>

<form name="listForm" id="listForm">
<div class="TM_listWrapper">
<table cellspacing="0" cellpadding="0" id="m_messageList" >
	<col width="40px"></col>		
	<col width="90px"></col>
	<col width="100px"></col>
	<col width="150px"></col>
	<col></col>
	
	<tr>
	<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
	<th scope="col"><tctl:msg key="mail.senddate"/></th>	
	<th scope="col"><tctl:msg key="mail.receivenoti"/></th>
	<th scope="col"><tctl:msg key="mail.to"/></th>	
	<th scope="col"><tctl:msg key="mail.subject"/></th>
	</tr>
	<c:if test="${empty messages}">
		<tr class="TM_mailLow">
			<td colspan="5"><tctl:msg key="mail.nomessage"/></td>
		</tr>
	</c:if>
	<c:if test="${!empty messages}">
		<c:forEach var="message" items="${messages}" varStatus="loop">
		<tr id="${message.folderName}_${message.id}" class="TM_mailLow">
		<td class="TM_chkTd">
			<div class="TM_chkW" >			
				<input type="checkbox"  name="msgId"  id="chk_${message.id}"  value="${message.id}" onclick="checkMessage(this)"  onfocus="this.blur()"/>		
			</div>
		</td>	
		<td>${message.sendDateForList}</td>
		<td>
			<c:if test="${message.MDNCount < 0}">
				<span class="TM_MDNStatus">
				<tctl:msg key="mail.mdn.notselect"/>
				</span>
			</c:if>
			<c:if test="${message.MDNCount == 1}">
				<span class="TM_MDNStatus" id="mdn_${loop.count}"></span>
				<script>displayMDN("mdn_${loop.count}","${message.id}",
						"${message.messageID}",
						"${message.MDNResponses[0].sentDate3}",
						"${message.MDNResponses[0].code}");</script>			
			</c:if>
			<c:if test="${message.MDNCount > 1}">
				<a href="javascript:;" onclick=""> ${message.MDNReadCount} / ${message.MDNCount} </a> 
			</c:if>
		</td>
		<td class="TM_list_from"  title="${fn:escapeXml(message.to)}" >
			<div nowrap>		 
			${fn:escapeXml(message.sendToSimple)}
			</div>
		</td>
		<td class="TM_list_subject">
			<c:if test="${message.MDNCount > 0}">		
			<strong><a href="javascript:;" onclick="viewMDN('${message.id}')" id="msg_subject_${message.id}">${fn:escapeXml(message.subject)}</a></strong>
			</c:if>
			<c:if test="${message.MDNCount <= 0}">
				${fn:escapeXml(message.subject)}
			</c:if>
		</td>
		</tr>	
		</c:forEach>
	</c:if>
</table>
	
</div>
<c:if test="${!empty messages}">
		<div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
	</c:if>
</form>

<script type="text/javascript">

	chkMsgCnt = 0;	
	isAllFolder = false;
	chkMsgHash = new Hash();
	
	function loadMdnListPage(){		
		
		var currentPage = ${currentPage};
		var pageBase = ${pageBase};
		var totalMessage = ${total};		
			
		var workTitle =  "<span class='TM_work_title'>"+mailMsg.mail_receivenoti+"</span>";	
		setWorkTitle(workTitle); 
	
		
		loadMDNListToolBar();
		
		jQuery("#siSearchBox").show();
		/*menuBar.setPageNavi("p",{total:totalMessage,
			base:pageBase,
			page:currentPage,
			isListSet:true,
			isLineCntSet:true,
			pagebase:USER_PAGEBASE,
			changeAfter:reloadListPage});*/

		menuBar.setPageNaviBottom("p",{total:totalMessage,
			base:pageBase,
			page:currentPage,
			isListSet:true,
			isLineCntSet:true,
			pagebase:USER_PAGEBASE,
			changeAfter:reloadListPage});

		setCurrentPage(currentPage);		
		
		$("m_contentMain").scrollTop = 0;

		
	}

	function viewMDN(uid){
		var param = {"uid":uid};
		mailControl.viewMDNContent(param);
	}

	loadMdnListPage();
</script>

