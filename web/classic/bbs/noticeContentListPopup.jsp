<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/bbs_style.css" />
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/bbs/bbsReadMenuBar.js"></script>

<title>Bbs</title>
<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>

<script language = "javascript">
function init() {
	//winResize();
	var title ="${fn:escapeXml(noticeBbs.bbsName)}";
	if (opener) {
        jQuery("body").addClass("popupBody");
        
    }else{
    	jQuery("body").css("background","none");
    	parent.setNoticeListPopupTitle(title);
    }
	
	
}

function closeWin(){
	if (opener) {
        opener.closePop();
    }else{
    	parent.modalPopupForNoticeListClose(); 
        parent.jQpopupClear();
    }
}

function viewContent(contentId) {
	var f = document.noticeListForm;
	f.contentId.value = contentId;

	f.action = "/bbs/viewNoticeContentPopup.do";
	f.method = "post";
	f.submit();
}

function moveto_page(page) {
	var f = document.noticeListForm;
	f.currentPage.value = page;
	f.action = "/bbs/listNoticeContentPopup.do";
	f.method = "post";
	f.submit();
}

</script>
</head>
<body onload="init()">
<form name="noticeListForm">
<input type="hidden" name="bbsId" value="${noticeBbs.bbsId}">
<input type="hidden" name="contentId">
<input type="hidden" name="viewType" value="list">
<input type="hidden" name="bbsIndex" value="${fn:escapeXml(bbsIndex)}">
<input type="hidden" name="currentPage" value="${fn:escapeXml(currentPage)}">
<div class="popup_style2" style="border:0px;">
	<%--
	<div class="title">
		<span>${noticeBbs.bbsName}</span>
		<div id="pageNavi"  class="mail_body_navi"></div>
		<a class="btn_X" href="javascript:;" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>
	--%>
	<div class="TM_modal_content_wrapper">	
		<div class="TM_content_wrapper" style="height:420px;">
			<table id="bbsListTable" cellpadding="0" cellspacing="0" style="border-top:1px solid #C6C6C6;">
				<colgroup span="7">
					<col width="50px"></col>
					<col width="20px"></col>
					<col></col>
					<col width="100px"></col>
					<col width="100px"></col>
					<col width="70px"></col>
				</colgroup>
				<tr>
					<th scope="col"><tctl:msg key="bbs.content.list.contentId" bundle="bbs"/></th>
					<th scope="col"></th>
					<th scope="col"><tctl:msg key="bbs.content.list.subject" bundle="bbs"/></th>
					<th scope="col"><tctl:msg key="bbs.content.list.creator" bundle="bbs"/></th>
					<th scope="col"><tctl:msg key="bbs.content.list.createDate" bundle="bbs"/></th>
					<th scope="col"><tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/></th>
				<tr>
				<c:if test="${!empty noticeContentList}">
				<c:forEach var="content" items="${noticeContentList}">
				<tr>
					<td class="bbsTd">${content.contentId}</td>
					<td class="bbsTd">
						<c:if test="${content.attCnt > 0}">
							<img src="/design/default/image/icon/ic_attach_file.gif" border="0" align="absmiddle">
						</c:if>
					</td>
					<td class="bbsTd subject">
						<c:if test="${content.depth != 0}">
							<c:forEach begin="0" end="${content.depth}" varStatus="loop">
								<c:if test="${loop.count <= 10}">&nbsp;</c:if>
							</c:forEach>
							<img src="/design/common/image/icon_bbs_reply.gif" border="0">
						</c:if>
						<a href="#" onclick="viewContent('${content.contentId}')">${content.subject}</a>
					</td>
					<td class="bbsTd">${content.creatorName}</td>
					<td class="bbsTd">${fn:substring(content.createTime,0,4)}-${fn:substring(content.createTime,4,6)}-${fn:substring(content.createTime,6,8)}</td>
					<td class="bbsTd">${content.refCnt}</td>
				</tr>	
				</c:forEach>
				</c:if>
				<c:if test="${empty noticeContentList}">
				<tr>
					<td colspan="6" class="bbsTd"><tctl:msg key="bbs.content.empty" bundle="bbs"/></td>
				</tr>
				</c:if>
			</table>
		</div>
	</div>
	<div id="pageCounter" class="pageNum">
		<%@include file="/common/pageCounter.jsp" %>
	</div>
	<div class="dotLine"></div>
	<div class="btnArea">
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</div>
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>