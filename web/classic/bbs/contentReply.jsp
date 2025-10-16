<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@include file="/common/authHeader.jsp"%>
<table id="replylistTable">
	<c:forEach var="boardContentReply" items="${boardContentReplyList}">
	<tr height="25px">
		<td width="150px">${boardContentReply.creatorName}</td>
		<td align="left" style="overflow:hidden;">
			<div style="word-break:break-all;padding:10px;0px; ">${fn:replace(fn:escapeXml(boardContentReply.content),'&lt;br&gt;', '<br/>')}</div>
		</td>
		<td width="100px">
			${fn:substring(boardContentReply.createTime,0,4)}-${fn:substring(boardContentReply.createTime,4,6)}-${fn:substring(boardContentReply.createTime,6,8)}
			${fn:substring(boardContentReply.createTime,8,10)}:${fn:substring(boardContentReply.createTime,10,12)}
		</td>
		<td width="60px" align="right" style="padding-right:0px">
			<c:if test = "${bbsAdmin || mailUserSeq == boardContentReply.creatorSeq}">
			<a href="javascript:;" onclick="deleteReplyMessage('${boardContentReply.replyId}')" class="btn_del">
				<span><tctl:msg key="bbs.delete" bundle="bbs"/></span>
			</a>
			</c:if>
		</td>
	</tr>
	</c:forEach>
	<tr height="25px">
		<td colspan="4" align="center">
			<div id="pageCounter" class="pageNum">
				<%@include file="/common/pageCounter.jsp" %>
			</div>
		</td>
	</tr>
</table>
<script language = "javascript">
setTotalReplyCount('${boardContentReplyListCount}');
</script>