<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<c:if test="${!empty noticeBbs}">
<div class="title">
	<h3>${noticeBbs.bbsName}</h3>
	<p class="more"><a href="#" onclick="listContentPopup('${fn:escapeXml(bbsIndex)}')"><tctl:msg key="comn.more" bundle="common"/></a></p>
</div>
<ul class="list">
	<c:forEach var="noticeContent" items="${noticeContentList}" begin="0" end="5">
	<li style="white-space:nowrap;">
		- <a href="#" onclick="viewContentPopup('${noticeBbs.bbsId}','${noticeContent.contentId}','${fn:escapeXml(bbsIndex)}')" title="${noticeContent.subject}">${noticeContent.subject}</a>
	</li>
	</c:forEach>
</ul>
</c:if>