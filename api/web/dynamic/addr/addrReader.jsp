<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

 <table cellpadding="0" cellspacing="0" class="jq_innerTable">
	<col width="100px"></col>
	<col></col>
	<tr>
	<th class="lbout">
		<tctl:msg key="register.021" bundle="common"/>
	</th>
	<td>
	 	<input style="width: 100px;" type="text" id="readerEmail"/> 
	 	<a href="#" onclick="okReaderAddPressed();" class="btn_basic"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
	 	<a href="#" onclick="okReaderDelPressed();" class="btn_basic"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
	 	<a href="#" onclick="okReaderSearchPressed();" class="btn_basic"><span><tctl:msg key="comn.search" bundle="common"/></span></a>
	</td>
	</tr>
</table>

<div style="height: 5px;"></div>

<div id="readerList">
<form id="readerForm">
<table cellspacing="0" cellpadding="0" class="popup_list">
	<col width="20px"></col>
	<col width="80px" ></col>
	<col></col>
	
	<tr>
		<th scope="col"><input type="checkbox" id="chkAll" onClick="javascript:checkall2('chkAll', 'popup_list')"/></th>
		<th scope="col"><tctl:msg key="addr.table.header.001" bundle="addr"/></th>
		<th scope="col"><tctl:msg key="addr.table.header.002" bundle="addr"/></th>
	</tr>
	<c:forEach var="members" items="${readerList}" varStatus="loop">
	<tr>
		<td>
			<input id="readerSeq_${members.userSeq}" name="readerSeqs" type="checkbox" value="${members.userSeq}">		
		</td>
		<td>
			<a onclick="viewAddress(${members.userSeq});" href="javascript:;">${members.userName}</a> 
		</td>
		<td>
			${members.userEmail}
		</td>
	</tr>
	</c:forEach>
	<tr>
		<td align="center" colspan="3">
			<c:forEach var="page" items="${pm.pages}">
				<c:if test="${page == pm.page}">
					${page}
				</c:if>
				<c:if test="${page != pm.page}">
					<a href="javascript:moveReaderPage('${page}')">${page}</a>
				</c:if>
			</c:forEach>
		</td>
	</tr>
</table>
</form>
</div>
