<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/address_style.css" />

<script type="text/javascript">
function printMsg(){
	window.print();	
}

function movePage(page){
	var f = $("printForm");
	f.page.value = page;
	f.submit();
}

</script>

</head>
<body onload="printMsg();">

<form id="printForm" action="/dynamic/addr/listAddress.do">
	<input type="hidden" name="groupSeq" value="${fn:escapeXml(groupSeq)}">
	<input type="hidden" name="bookSeq" value="${fn:escapeXml(bookSeq)}">
	<input type="hidden" name="leadingPattern" value="${leadingPattern}">
	<input type="hidden" name="searchType" value="${fn:escapeXml(searchType)}">
	<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}">
	<input type="hidden" name="page">
	<input type="hidden" name="pagePrint" value="true">
</form>
<div class="address_list">
	<table class="addr_list_print_header" width="760" style="width:760px;table-layout:auto">
		<col width="120px" ></col>
		<col width="200px" ></col>
		<col width="200px"></col>
		<col width="120px"></col>
		<col width="120px"></col>
		<tr>
			<th scope="col">
				<tctl:msg key="addr.table.header.001" bundle="addr"/>
			</th>
			<th scope="col">
				<tctl:msg key="addr.table.header.002" bundle="addr"/>
			</th>
			<th scope="col" >
				<tctl:msg key="addr.table.header.004" bundle="addr"/>
			</th>
			<th scope="col">
				<tctl:msg key="addr.table.header.003" bundle="addr"/>
			</th>
			<th scope="col">
				<tctl:msg key="addr.table.header.005" bundle="addr"/>
			</th>
		</tr>
		<c:forEach var="members" items="${members}" varStatus="loop">
		<tr>
			<td >
				${members.memberName}&nbsp;
			</td>
			<td>
				${members.memberEmail}&nbsp;
			</td>
			<td >
				${members.companyName}&nbsp;
			</td>
			<td nowrap>
				${members.mobileNo}&nbsp;
				
			</td>
			<td nowrap>
				${members.officeTel}&nbsp;
			</td>
		</tr>
		</c:forEach>
		<tr class="last">
			<td class="last" colspan="5" align="center">
				<c:forEach var="page" items="${pm.pages}" varStatus="loop">
				<c:if test="${page == pm.page}">
					${page}
				</c:if>
				<c:if test="${page != pm.page}">
					<a href="javascript:movePage('${page}')">${page}</a>
				</c:if>
				</c:forEach>
			</td>
		</tr>
		
	</table>
</div>
</body>
</html>