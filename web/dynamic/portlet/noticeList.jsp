<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<script language = "javascript">
function gotoBbs(bbsId, contentId, parentId, orderNo) {
	var url = "/bbs/viewContent.do";
	var param = "?bbsId="+bbsId+"&contentId="+contentId+"&parentId="+parentId+"&orderNo="+orderNo;
	this.location = url+param;
}

function viewDetail() {
	var f = document.bbsForm;
	var url = "/bbs/listContent.do";
	var param = "?bbsId="+f.bbsId.value;
	this.location = url+param;
}
</script>

<form name="bbsForm">
<input type="hidden" name="bbsId" value="${fn:escapeXml(bbsId)}">
<input type="hidden" name="contentId">
<input type="hidden" name="parentId">
<input type="hidden" name="orderNo">
</form>
	<div class="roundTitle">
		<p>
			<span class="title"><tctl:msg key="bbs.title" bundle="bbs"/> (${boardVO.bbsName})</span>
			<a href="#" onclick="viewDetail()" class="btn_style5"><span><tctl:msg key="comn.detail" bundle="common"/></span></a>
		</p>
	</div>
	<div class="portlet_body" style="text-overflow:ellipsis;overflow:hidden">
<c:if test="${part eq 'B' || part eq 'D'}">
	<table cellspacing="0" cellpadding="0" class="TM_portlet_messageList" >
	<col ></col>
	<col width="80px"></col>
	<col width="50px"></col>	
	<c:forEach var="content" items="${contentList}">
	<tr>
		<td style="padding-left:10px">
			<table class='TM_HiddenTextTable'>
				<tr>
					<td style="border:0;" title="${content.subject}">
						<div class='TM_HiddenTextDiv'>
							<c:if test="${content.depth != 0}">
								<c:forEach begin="0" end="${content.depth}" varStatus="loop">
									<c:if test="${loop.count <= 10}">&nbsp;</c:if>
								</c:forEach>
								<img src="/design/common/image/icon_bbs_reply.gif" border="0">
							</c:if>
							<a href="#" onclick="gotoBbs('${content.bbsId}','${content.contentId}','${content.parentId}','${content.orderNo}')">${content.subject}</a>
						</div>
					</td>
				</tr>
			</table>
		</td>
		<td style="padding-left:10px">
			<table class='TM_HiddenTextTable'>
				<tr>
					<td style="border:0;" title="${content.creatorName}">
						<div class='TM_HiddenTextDiv'>${content.creatorName}</div>
					</td>
				</tr>
			</table>
		</td>
		<td align="center">${content.refCnt}</td>
	</tr>
	</c:forEach>
	</table>
</c:if>

<c:if test="${part eq 'A' || part eq 'C'}">
	<ul class="notice_portlet_body">
		<c:forEach var="content" items="${contentList}">
		<li class="dot" title="${content.subject}">
			<c:if test="${content.depth != 0}">
				<c:forEach begin="0" end="${content.depth}" varStatus="loop">
					<c:if test="${loop.count <= 10}">&nbsp;</c:if>
				</c:forEach>
				<img src="/design/common/image/icon_bbs_reply.gif" border="0">
			</c:if>
			<a href="#" onclick="gotoBbs('${content.bbsId}','${content.contentId}','${content.parentId}','${content.orderNo}')">${content.subject}</a>
		</li>
		</c:forEach>
	</ul>
</c:if>
	
	</div>
