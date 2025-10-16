<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>


<div class="roundTitle">
	<p>
		<span class="title">${folderName}</span>
		<a href="javascript:;"		
		onclick="goFolder('${folderFullName}'<c:if test="${unseen eq true}">,'U'</c:if>)" 
		class="btn_style5"><span><tctl:msg key="comn.detail" bundle="common"/></span></a>
	</p>
</div>

<div class="portlet_body">
<c:if test="${part eq 'B' || part eq 'D'}">
<table cellspacing="0" cellpadding="0" class="TM_portlet_messageList" >
<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
	
	<c:if test="${folderType eq 'all'}">
	<col width="90px" ></col>
	<col width="3px"></col>
	</c:if>	
	<col width="80px"></col>	
	<col ></col>
	<col width="110px"></col>	
		
</c:if>
	
<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	
	<col ></col>
	<col width="10px"></col>
	<col width="80px"></col>			
	<col width="110px"></col>
		
</c:if>	
	
	<c:forEach var="message" items="${messageBeans}" varStatus="loop">
	<tr id="${message.folderName}_${message.id}" <c:choose><c:when test="${message.seen}">class="TM_mailLow"</c:when><c:otherwise>class="TM_unseenLow"</c:otherwise></c:choose>>
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
		<a href="javascript:;" class="${folderType}_msubject" onclick="removePreview();readHomeMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${folderType}_${message.id}')" onmouseout="outPriview()" id="msg_subject_${folderType}_${message.id}">
			<c:if test="${not empty message.subject}">
			${message.subject}
			</c:if>
			<c:if test="${empty message.subject}">
				<tctl:msg key="header.nosubject"/>
			</c:if>
		</a>
		</c:if>
		<%--
		<c:if test="${folderType eq 'draft'}">
			<a href="javascript:;" onclick="writeHomeMessage('${message.folderName}','${message.id}',${loop.count})" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>
		 --%>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${folderType}_${message.id}_priview">${message.preview}</span>-
		</c:if>						
	</td>
	<td>&nbsp;</td>	
	<td class="TM_list_from"  title="${fn:escapeXml(message.to)}" style="padding-left:10px;">
		<div nowrap>		
		${fn:escapeXml(message.sendToSimple)}
		</div>
	</td>
	</c:if>	
	<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
	<td class="TM_list_from"  title="${fn:escapeXml(message.from)}" style="padding-left:10px;">
		<div nowrap>		
			${fn:escapeXml(message.fromSimple)}
		</div>		
	</td>	
	<td class="TM_list_subject" nowrap>
		<c:if test="${message.priority != 3}">
           	<img src="/design/common/image/ic_import.gif">
        </c:if>		
		<a href="javascript:;" class="${folderType}_msubject" onclick="removePreview();readHomeMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${folderType}_${message.id}')" onmouseout="outPriview()" id="msg_subject_${folderType}_${message.id}">
			<c:if test="${not empty message.subject}">
			${fn:escapeXml(message.subject)}
			</c:if>
			<c:if test="${empty message.subject}">
				<tctl:msg key="header.nosubject"/>
			</c:if>
		</a>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${folderType}_${message.id}_priview">${message.preview}</span>-
		</c:if>		
	</td>	
	</c:if>
	
	<td>${message.dateForList}</td>
	</tr>
	</c:forEach>
</table>
</c:if>

<c:if test="${part eq 'A' || part eq 'C'}">
<table cellspacing="0" cellpadding="0" class="TM_portlet_messageList" >
	<col width="98%"></col>	
	
	<c:forEach var="message" items="${messageBeans}" varStatus="loop">
	<tr id="${message.folderName}_${message.id}" <c:choose><c:when test="${message.seen}">class="TM_mailLow"</c:when><c:otherwise>class="TM_unseenLow"</c:otherwise></c:choose>>
	<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	<td class="TM_list_subject">		
		<c:if test="${folderType ne 'draft'}">
			<a href="javascript:;" class="${folderType}_msubject" onclick="removePreview();readHomeMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${folderType}_${message.id}')" onmouseout="outPriview()" id="msg_subject_${folderType}_${message.id}">${message.subject}</a>
		</c:if>
		<%--
		<c:if test="${folderType eq 'draft'}">
			<a href="javascript:;" onclick="writeHomeMessage('${message.folderName}','${message.id}',${loop.count})" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>
		 --%>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${folderType}_${message.id}_priview">${message.preview}</span>-
		</c:if>						
	</td>
	</c:if>	
	<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">	
	<td class="TM_list_subject" nowrap>
		<c:if test="${message.priority != 3}">
           	<img src="/design/common/image/ic_import.gif">
        </c:if>		
		<a href="javascript:;" class="${folderType}_msubject" onclick="removePreview();readHomeMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${folderType}_${message.id}')" onmouseout="outPriview()" id="msg_subject_${folderType}_${message.id}">
			<c:if test="${not empty message.subject}">
			${fn:escapeXml(message.subject)}
			</c:if>
			<c:if test="${empty message.subject}">
				<tctl:msg key="header.nosubject"/>
			</c:if>
		</a>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${folderType}_${message.id}_priview">${message.preview}</span>-
		</c:if>		
	</td>	
	</c:if>	
	</tr>
	</c:forEach>
</table>
</c:if>
</div>