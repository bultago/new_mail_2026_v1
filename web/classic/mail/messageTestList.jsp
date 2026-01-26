<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="/design/common/css/common.css">
<link rel="stylesheet" type="text/css" href="/design/default/css/layout.css">
<link rel="stylesheet" type="text/css" href="/design/default/css/mail.css" />
</head>
<body>

<div class="roundTitle">
	<p>
		<span class="title">${folderName}</span>
		<a href="javascript:;" onclick="goFolder('${message.folderName}')" class="btn_style5"><span><tctl:msg key="comn.detail" bundle="common"/></span></a>
	</p>
</div>

<div class="portlet_body">

<table cellspacing="0" cellpadding="0" class="TM_portlet_messageList" >
<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
	
	<c:if test="${folderType eq 'all'}">
	<col width="90px" ></col>
	<col width="3px"></col>
	</c:if>	
	<col width="80px"></col>	
	<col ></col>
	<col width="90px"></col>
	
	<tr>
	<c:if test="${folderType eq 'all'}">
	<th scope="col"><tctl:msg key="mail.folder"/></th>
	<th></th>
	</c:if>		
	<th scope="col">
		<tctl:msg key="mail.from"/>		
	</th>
	<th scope="col">		
		<tctl:msg key="mail.subject"/>
	</th>	
</c:if>
	
<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	
	<col ></col>
	<col width="10px"></col>
	<col width="80px"></col>			
	<col width="90px"></col>	
	
	<tr>		
	<th scope="col">		
		<tctl:msg key="mail.subject"/>
	</th>
	<th></th>
	<th scope="col">		
		<tctl:msg key="mail.to"/>		
	</th>	
</c:if>
	<th scope="col">		
		<c:if test="${folderType eq 'sent'}">
			<tctl:msg key="mail.senddate"/>
		</c:if> 
		<c:if test="${folderType eq 'draft'}">		
			<tctl:msg key="mail.writedate"/>	
		</c:if>
		<c:if test="${folderType eq 'reserved'}">
			<tctl:msg key="mail.reserveddate"/>
		</c:if>		
		<c:if test="${folderType ne 'sent' && folderType ne 'draft' &&	folderType ne 'reserved'}">
			<tctl:msg key="mail.receivedate"/>
		</c:if>
	</th>	
	</tr>
	
	<c:forEach var="message" items="${messageBeans}" varStatus="loop">
	<tr id="${message.folderName}_${message.id}"		
	<c:choose>
		<c:when test="${message.seen}">
	 		class="TM_mailLow">
		</c:when>
		<c:otherwise>
			class="TM_unseenLow">	
		</c:otherwise>
	</c:choose>	
	<c:if test="${folderType eq 'all'}">
	<td class="TM_list_folderName"  title="${message.folderFullName}">
		<c:choose>
			<c:when test="${message.folderName eq 'Inbox'}">
		 		<tctl:msg key="folder.inbox" />
			</c:when>
			<c:when test="${message.folderName eq 'Sent'}">
		 		<tctl:msg key="folder.sent" />
			</c:when>
			<c:when test="${message.folderName eq 'Drafts'}">
		 		<tctl:msg key="folder.drafts" />
			</c:when>
			<c:when test="${message.folderName eq 'Reserved'}">
		 		<tctl:msg key="folder.reserved" />
			</c:when>
			<c:when test="${message.folderName eq 'Spam'}">
		 		<tctl:msg key="folder.spam" />
			</c:when>
			<c:when test="${message.folderName eq 'Trash'}">
		 		<tctl:msg key="folder.trash" />
			</c:when>
			<c:otherwise>
					${message.folderDepthName}
			</c:otherwise>
		</c:choose>	
	</td>
	<td></td>
	</c:if>
	<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	<td class="TM_list_subject">		
		<c:if test="${folderType ne 'draft'}">
			<a href="javascript:;" onclick="readHomeMessage('${message.folderName}','${message.id}',${loop.count})" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>
		<%--
		<c:if test="${folderType eq 'draft'}">
			<a href="javascript:;" onclick="writeHomeMessage('${message.folderName}','${message.id}',${loop.count})" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>
		 --%>		
		<c:if test="${not empty message.preview }">
		<span>&nbsp; - ${fn:escapeXml(message.preview)} -</span>
		</c:if>						
	</td>
	<td></td>	
	<td class="TM_list_from"  title="${fn:escapeXml(message.to)}">
		<div nowrap>		
		${fn:escapeXml(message.sendToSimple)}
		</div>
	</td>
	</c:if>	
	<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
	<td class="TM_list_from"  title="${fn:escapeXml(message.from)}">
		<div nowrap>		
			${fn:escapeXml(message.fromSimple)}
		</div>		
	</td>	
	<td class="TM_list_subject" nowrap>
		<c:if test="${message.priority != 3}">
           	<img src="/design/common/image/ic_import.gif">
        </c:if>		
		<a href="javascript:;" onclick="readHomeMessage('${message.folderName}','${message.id}',${loop.count})" id="msg_subject_${message.id}">
			${message.subject}	
		</a>		
		<c:if test="${not empty message.preview }">
		<span>&nbsp; - ${fn:escapeXml(message.preview)} -</span>
		</c:if>		
	</td>	
	</c:if>
	
	<td>${message.dateForList}</td>
	</tr>
	</c:forEach>
</table>
</div>

</body>

</html>
