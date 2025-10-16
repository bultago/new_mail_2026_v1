<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<div id="replyContent">
<ul class="reply_type board_list">
	<c:forEach var="boardContentReply" items="${bbsContentReplyVo}">	
	<li>
		<div>
			<a href="" style="float:left;padding:3px 0 3px 8px"><span class="info">${boardContentReply.creatorName} | ${fn:substring(boardContentReply.createTime,0,4)}-${fn:substring(boardContentReply.createTime,4,6)}-${fn:substring(boardContentReply.createTime,6,8)}
			${fn:substring(boardContentReply.createTime,8,10)}:${fn:substring(boardContentReply.createTime,10,12)}:${fn:substring(boardContentReply.createTime,12,14)}</span></a>
			<c:if test = "${boardContentReply.bbsAdmin || mailUserSeq == boardContentReply.creatorSeq}">
			<div style="padding-right:8px;padding-top:3px"><a class="btn2" href="javascript:deleteReplyMessage('${boardContentReply.replyId}')" style="float:right;"><span><tctl:msg key="bbs.mobile.008" bundle="bbs"/></span></a></div>
			</c:if>							
		</div>
		<div style="clear:both;padding:0 0 4px 8px">${boardContentReply.content}</div>												
									
	</li>				
	</c:forEach>
</ul>
<%@include file="/hybrid/common/pageCounter.jsp"%>
</div>
<script type="text/javascript">
var contents = document.getElementById("replyContent").innerHTML;
parent.setReplyContents(contents);
</script>