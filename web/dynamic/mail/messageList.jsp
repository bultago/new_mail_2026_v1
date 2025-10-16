<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<script type="text/javascript">
currentFolderType = "${folderType}";
flagOpt.folderType = currentFolderType;
</script>

<form name="listForm" id="listForm">
<input type="hidden" name="allSearchSelectCheck" id="allSearchSelectCheck" value="off"/>

<div class="TM_listWrapper">
<table cellspacing="0" cellpadding="0" id="m_messageList" >

<c:if test="${viewMode eq 'h' || viewMode eq 'n'}">
<%-- //////////////////  VIEW MODE H START /////////////////////--%>
<c:set var="colSize" value="7"/>
<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">		
	<col width="40px"></col>
	<col width="55px" ></col>
	<c:if test="${folderType eq 'all'}">
	<c:set var="colSize" value="8"/>
	<col width="90px" ></col>
	</c:if>
	<col width="80px"></col>
	<col ></col>	
	<col width="15px"></col>
	<col width="90px"></col>
	<col width="70px"></col>
	
	<tr>
	<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
	<th scope="col"><tctl:msg key="mail.kind"/></th>
	<c:if test="${folderType eq 'all'}">
	<th scope="col"><tctl:msg key="mail.folder"/></th>
	</c:if>
	<th scope="col">		
		<c:if test="${sortBy eq 'from'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;"  onclick="sortMessage('from','desc')" class="sortitem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>			
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('from','asce')" class="sortitem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'from'}">
			<a href="javascript:;" onclick="sortMessage('from','desc')">
		</c:if>		
		<tctl:msg key="mail.from"/></a>		
	</th>
	<th scope="col">
		<c:if test="${sortBy eq 'subj'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('subj','desc')" class="sortitem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('subj','asce')" class="sortitem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'subj'}">
			<a href="javascript:;" onclick="sortMessage('subj','desc')">
		</c:if>
		<tctl:msg key="mail.subject"/></a>
	</th>
	<th scope="col">&nbsp;</th>
</c:if>
	
<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	<col width="40px"></col>
	<col width="55px" ></col>	
	<col ></col>
	<col width="10px"></col>
	<col width="80px"></col>	
	<col width="90px"></col>
	<col width="70px"></col>
	
	<tr>
	<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
	<th scope="col"><tctl:msg key="mail.kind"/></th>
		
	<th scope="col">
		<c:if test="${sortBy eq 'subj'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('subj','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('subj','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'subj'}">
			<a href="javascript:;" onclick="sortMessage('subj','desc')">
		</c:if>
		<tctl:msg key="mail.subject"/></a>
	</th>
	<th>&nbsp;</th>
	<th scope="col">		
		<c:if test="${sortBy eq 'to'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;"  onclick="sortMessage('to','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>			
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('to','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'to'}">
			<a href="javascript:;" onclick="sortMessage('to','desc')">
		</c:if>		
		<tctl:msg key="mail.to"/></a>		
	</th>	
	</c:if>
	<th scope="col">
		<c:if test="${sortBy eq 'arrival'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('arrival','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('arrival','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'arrival'}">
			<a href="javascript:;" onclick="sortMessage('arrival','desc')">
		</c:if>
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
		</a>
	</th>
	<th scope="col">
		<c:if test="${sortBy eq 'size'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('size','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('size','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
		</c:if>
		<c:if test="${sortBy ne 'size'}">
			<a href="javascript:;" onclick="sortMessage('size','desc')">
		</c:if>
		<tctl:msg key="mail.size"/></a>
	</th>
	</tr>
	
	<c:if test="${empty messageBeans}">
	<tr><td colspan="${colSize}">
		<tctl:msg key="mail.nomessage"/>
	</td></tr>
	</c:if>
	<c:if test="${not empty messageBeans}">
	<c:forEach var="message" items="${messageBeans}" varStatus="loop">
	<tr onmousedown="makeDrag('${message.folderName}_${message.id}')" id="${message.folderName}_${message.id}" <c:choose><c:when test="${message.seen}">class="TM_mailLow"</c:when><c:otherwise>class="TM_unseenLow"</c:otherwise></c:choose>>
	
	<td class="TM_chkTd">	
		<div class="TM_chkW" >			
			<input type="checkbox"  name="msgId"  id="chk_${message.id}"  value="${message.id}" onclick="checkMessage(this)"  onfocus="this.blur()"/>
			<input type="hidden" id="${message.id}_femail"  value="${fn:escapeXml(message.fromEmail)}">
			<input type="hidden" id="${message.id}_temail"  value="${fn:escapeXml(message.toEmail)}">
			<input type="hidden" name="${message.id}_mailsize" id="${message.id}_mailsize" value="${message.byteSize}"/>						                                			
		</div>
	</td>
	<td class="TM_list_flag" flagcontents="${message.flag}">		
		<span><img src="/design/common/image/ajax-loader.gif"/></span>		
	</td>
	<c:if test="${folderType eq 'all'}">
	<td class="TM_list_from"  title="${message.folderFullName}">
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
	</c:if>
	<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">	
	<td class="TM_list_subject">		
		<c:if test="${folderType ne 'draft'}">
			<a href="javascript:;" class="msubject"  onclick="removePreview();readMessage('${message.folderName}','${message.id}',${loop.count});" onmouseover="overPriview('msg_subject_${message.id}')" onmouseout="outPriview()" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>
		<c:if test="${folderType eq 'draft'}">
			<a href="javascript:;" class="msubject"  onclick="removePreview();writeDraftMessage('${message.folderName}','${message.id}',${loop.count});" onmouseover="overPriview('msg_subject_${message.id}')" onmouseout="outPriview()" id="msg_subject_${message.id}">${message.subject}</a>
		</c:if>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${message.id}_priview">${fn:escapeXml(message.preview)}</span>-
		</c:if>						
	</td>
	<td>&nbsp;</td>
	<td class="TM_list_from"  title="${fn:escapeXml(message.to)}">
		<div nowrap>
		<a href="javascript:;" 
		onclick="makeAddrSubMenu('to_${loop.count}', '${fn:escapeXml(message.sendToSimple)}')">
		${fn:escapeXml(message.sendToSimple)}			
		</a>
		</div>
		<div id="to_${loop.count}" ></div>
	</td>	
	</c:if>	
	<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
	<td class="TM_list_from"  title="${fn:escapeXml(message.from)}">
		<div nowrap>
		<a href="javascript:;" 
		onclick="makeAddrSubMenu('from_${loop.count}', '<tctl:encodeURL>${message.from}</tctl:encodeURL>')">
			${fn:escapeXml(message.fromSimple)}			
		</a>
		</div>
		<div id="from_${loop.count}"></div>		
	</td>
	<c:if test="${folderType ne 'quotaviolate'}">
	<td class="TM_list_subject" id="msg_dnd_${message.id}" nowrap>				
		<span class="TM_tag_list" tagContents="${message.tagNameList}"></span>
		<c:if test="${message.priority != 3}">
           	<img src="/design/common/image/ic_import.gif">
        </c:if>
		<a href="javascript:;" class="msubject" onclick="removePreview();readMessage('${message.folderName}','${message.id}',${loop.count});" onmouseover="overPriview('msg_subject_${message.id}')" onmouseout="outPriview()" id="msg_subject_${message.id}">
			<c:if test="${message.spamRate < 0}">
				<strike>${message.subject}</strike>
			</c:if>
			<c:if test="${message.spamRate >= 0}">
				${message.subject}
			</c:if>				
		</a>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${message.id}_priview">${fn:escapeXml(message.preview)}</span>-
		</c:if>				
	</td>	
	<td><a href="javascript:;" onclick="readPopMessage('${message.folderName}','${message.id}',${loop.count})"><img src="/design/default/image/icon/ic_popup.gif"></a></td>
	</c:if>
	<c:if test="${folderType eq 'quotaviolate'}">
	<td class="TM_list_subject" nowrap>
		<c:if test="${message.priority != 3}">
           	<img src="/design/common/image/ic_import.gif">
        </c:if>		
		<a href="javascript:;" class="msubject" id="msg_subject_${message.id}">
			${message.subject}	
		</a>		
		<c:if test="${not empty message.preview }">
		&nbsp; -<span id="msg_subject_${message.id}_priview">${fn:escapeXml(message.preview)}</span>-
		</c:if>		
	</td>
	<td>&nbsp;</td>
	</c:if>
	</c:if>	
	
	<td>${message.dateForList}</td>
	<td>${message.size}</td>		
	</tr>
	</c:forEach>
	</c:if>
	
</c:if>	
<%-- //////////////////  VIEW MODE H END /////////////////////--%>

<c:if test="${viewMode eq 'v'}">
<%-- //////////////////  VIEW MODE V START /////////////////////--%>
<c:set var="colSize" value="5"/>		
	<col width="40px"></col>
	<col width="45px" ></col>	
	<col ></col>	
	<col width="15px"></col>
	<col width="70px"></col>
	
	<tr>
	<th scope="col"><input type="checkbox"  id="allChk" onclick="allCheckMessage(listForm.msgId,this.checked)"></th>
	<th scope="col" colspan="4" style="text-align: left">
		
		<select id="sortMsgSelect" onchange="sortMessageSelect()">
			<option value=""><tctl:msg key="mail.sortselect"/></option>
			<option value="subj"><tctl:msg key="mail.subject"/></option>
			<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">
			<option value="to"><tctl:msg key="mail.to"/></option>
			</c:if>
			<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
			<option value="from"><tctl:msg key="mail.from"/></option>
			</c:if>			
			<c:if test="${folderType eq 'sent'}">
			<option value="arrival"><tctl:msg key="mail.senddate"/></option>
			</c:if> 
			<c:if test="${folderType eq 'draft'}">		
			<option value="arrival"><tctl:msg key="mail.writedate"/></option>	
			</c:if>
			<c:if test="${folderType eq 'reserved'}">
			<option value="arrival"><tctl:msg key="mail.reserveddate"/></option>
			</c:if>		
			<c:if test="${folderType ne 'sent' && folderType ne 'draft' &&	folderType ne 'reserved'}">
			<option value="arrival"><tctl:msg key="mail.receivedate"/></option>
			</c:if>
			<option value="size"><tctl:msg key="mail.size"/></option>			
		</select>
		
		<c:if test="${sortBy eq 'subj'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('subj','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('subj','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
			<tctl:msg key="mail.subject"/></a>
		</c:if>		
		
		<c:if test="${sortBy eq 'from'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;"  onclick="sortMessage('from','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>			
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('from','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
			<tctl:msg key="mail.from"/></a>
		</c:if>
	
		<c:if test="${sortBy eq 'to'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;"  onclick="sortMessage('to','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>			
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('to','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
			<tctl:msg key="mail.to"/></a>
		</c:if>
		
				
	
		<c:if test="${sortBy eq 'arrival'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('arrival','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('arrival','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
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
			</a>
		</c:if>
			
		<c:if test="${sortBy eq 'size'}">
			<c:if test="${sortDir eq 'asce'}">
				<a href="javascript:;" onclick="sortMessage('size','desc')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_up.gif">
			</c:if>
			<c:if test="${sortDir eq 'desc'}">
				<a href="javascript:;" onclick="sortMessage('size','asce')" class="sortSelectItem">
				<img src="/design/common/image/icon/ic_bullet_down.gif">
			</c:if>
			<tctl:msg key="mail.size"/></a>
		</c:if>
	
	</th>
	</tr>
	
	<c:if test="${empty messageBeans}">
	<tr><td colspan="${colSize}">
		<tctl:msg key="mail.nomessage"/>
	</td></tr>
	</c:if>
	
	<c:if test="${not empty messageBeans}">
	<c:forEach var="message" items="${messageBeans}" varStatus="loop">
	<tr onmousedown="makeDrag('${message.folderName}_${message.id}')" id="${message.folderName}_${message.id}" <c:choose><c:when test="${message.seen}">class="TM_mailLow"</c:when><c:otherwise>class="TM_unseenLow"</c:otherwise></c:choose>>
	
	<td class="TM_chkTd">	
		<div class="TM_chkW" >			
			<input type="checkbox"  name="msgId"  id="chk_${message.id}"  value="${message.id}" onclick="checkMessage(this)"  onfocus="this.blur()"/>
			<input type="hidden" id="${message.id}_femail"  value="${fn:escapeXml(message.fromEmail)}">
			<input type="hidden" id="${message.id}_temail"  value="${fn:escapeXml(message.toEmail)}">
			<input type="hidden" name="${message.id}_mailsize" id="${message.id}_mailsize" value="${message.byteSize}"/>						                                			
		</div>
	</td>
	<td class="TM_list_flag" flagcontents="${message.flag}">
		<span><img src="/design/common/image/ajax-loader.gif"/></span>
	</td>
	<td  class="TM_list_subject" nowrap>
		<c:if test="${folderType eq 'all'}">
		<div class="TM_list_from TM_mlist_c_wrapper" title="${message.folderFullName}">		
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
		</div>
		</c:if>	
		
		<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">
			<div class="TM_list_from TM_mlist_c_wrapper"  title="${fn:escapeXml(message.to)}">
				<div nowrap>
				<a href="javascript:;" 
				onclick="makeAddrSubMenu('to_${loop.count}', '<tctl:encodeURL>${message.to}</tctl:encodeURL>')">
				${fn:escapeXml(message.sendToSimple)}			
				</a>
				</div>
				<div id="to_${loop.count}" ></div>
			</div>			
			<div>		
			<c:if test="${folderType ne 'draft'}">
				<a href="javascript:;" class="msubject" onclick="removePreview();readMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${message.id}')" onmouseout="outPriview()" id="msg_subject_${message.id}">${message.subject}</a>
			</c:if>
			<c:if test="${folderType eq 'draft'}">
				<a href="javascript:;" class="msubject" onclick="removePreview();writeDraftMessage('${message.folderName}','${message.id}',${loop.count})" onmouseover="overPriview('msg_subject_${message.id}')" onmouseout="outPriview()" id="msg_subject_${message.id}">${message.subject}</a>
			</c:if>		
				&nbsp; -<span id="msg_subject_${message.id}_priview">${fn:escapeXml(message.preview)}</span>-
			</div>
		</c:if>
		
		<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
			<div class="TM_list_from TM_mlist_c_wrapper"  title="${fn:escapeXml(message.from)}">
				<div nowrap>
				<a href="javascript:;" 
				onclick="makeAddrSubMenu('from_${loop.count}', '<tctl:encodeURL>${message.from}</tctl:encodeURL>')">
					${fn:escapeXml(message.fromSimple)}			
				</a>
				</div>
				<div id="from_${loop.count}"></div>		
			</div>
			<div>
			<span class="TM_tag_list" tagContents="${message.tagNameList}"></span>			
			<c:if test="${message.priority != 3}">
	           	<img src="/design/common/image/ic_import.gif">
	        </c:if>
			<a href="javascript:;" class="msubject" onclick="removePreview();readMessage('${message.folderName}','${message.id}',${loop.count})"	id="msg_subject_${message.id}">
				<c:if test="${message.spamRate < 0}">
					<strike>${message.subject}</strike>
				</c:if>
				<c:if test="${message.spamRate >= 0}">
					${message.subject}
				</c:if>				
			</a>		
			<c:if test="${not empty message.preview }">	
			&nbsp; -<span id="msg_subject_${message.id}_priview">${fn:escapeXml(message.preview)}</span>-
			</c:if>
			</div>	
		</c:if>				
	</td>	
	<td>
	<c:if test="${folderType ne 'sent' && folderType ne 'draft' && folderType ne 'reserved'}">
		<a href="javascript:;" onclick="readPopMessage('${message.folderName}','${message.id}',${loop.count})"><img src="/design/default/image/icon/ic_popup.gif"></a>
	</c:if>
	<c:if test="${folderType eq 'sent' || folderType eq 'draft' || folderType eq 'reserved'}">
		&nbsp;
	</c:if>	
	</td>	
	<td style="text-align: right;padding-right:5px">
		<div class="TM_mlist_c_wrapper">${message.dateForList}</div>
		<div class="TM_mlist_c_wrapper">${message.size}</div>
	</td>	
	</tr>	
	</c:forEach>
	</c:if>

<%-- //////////////////  VIEW MODE V END /////////////////////--%>
</c:if>		
</table>
</div>
</form>

<script type="text/javascript">

	chkMsgCnt = 0;	
	isAllFolder = false;
	chkMsgHash = new Hash();	
	
	pagingInfo = {"total":${total},"pageBase":${pageBase}};
	
	function loadMessageListPage(){		
		var totalCnt = ${messageCount};		
		var unreadCnt = ${unreadMessageCount};
		var folderName = "${folderName}";
		currentFolderViewName = folderName;
		currentFolder = "${folderFullName}";
		
		var currentPage = ${currentPage};
		var pageBase = ${pageBase};
		var totalMessage = ${total};
				
		if(!menuBar || menuBar.getMode() != "list"){
			loadListToolBar();
		}
	
		if(currentFolderType == "all"){
			isAllFolder = true;							
		}
		
		currentFolderName = folderName;
		updateWorkReadCount(folderName,totalCnt,unreadCnt);		
		setMailMenuBarStatus();	

		jQuery("#siSearchBox").show();
		menuBar.setPageNavi("p",
				{total:totalMessage,
				base:pageBase,
				page:currentPage,
				isListSet:true,
				isLineCntSet:true,
				pagebase:USER_PAGEBASE,
				changeAfter:reloadListPage});
			
		menuBar.toggleSideItem(true);		
		$("m_contentMain").scrollTop = 0;

		jQuery(window).trigger("resize");
					
	}	
	
jQuery().ready(function(){
	var folderList = getDropFolderList();			
	setTimeout(function(){
		jQuery(".TM_list_flag").printFlag(flagOpt);
		if(currentFolderType != "shared"){
			jQuery(".TM_tag_list").printTag(showTagMenu);
		} else {
			jQuery(".TM_tag_list").empty();
		}
		if(currentFolderType != "reserved" &&  currentFolderType != "shared"){
			dndManager.applyDrop(folderList,"table#m_messageList tr:not(:first)",dropFunc);
		} else {
			dndManager.destroyDrop(folderList);
		}
	},50);
	
	loadMessageListPage();
});

</script>

