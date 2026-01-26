<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/bbs_blue.css" />
		<script type="text/javascript">			
			
		</script>
	</head>
	<body>
		<div class="wrapper">		
			<%@include file="bbsTop.jsp"%>
			<%@include file="/mobile/basic/bbs/bbs_body_top.jsp"%>
			<div class="hh">
				<h2>
					<a href="javascript:viewSelectMenu(this, 'bbs');" class="btn_dr">
						<tctl:msg key="bbs.mobile.001" bundle="bbs"/>
					</a>
				</h2>
			</div>
			<div class="container">
				<ul class="normal_type board_home">
					<c:forEach var="bbs" items="${bbsInfoList}" varStatus="loop">
						<li class="${loop.last ? 'last':''} <c:if test="${bbs.bbsDepth > 0}">reply depth${bbs.bbsDepth}</c:if>"><a href="/mobile/bbs/bbsContentList.do?bbsId=${bbs.bbsId}"><span class="${(bbs.bbsType eq 'secret') ? 'secret' : ((bbs.bbsType eq 'any') ? 'any' : '')}">${bbs.bbsName}</span><c:if test="${bbs.existNew eq true}">&nbsp;&nbsp;<span class="ic_newContent">new</span></c:if></a></li>
					</c:forEach>
				</ul>
			</div>

			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>