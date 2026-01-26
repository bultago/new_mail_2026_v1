<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<% String sessionID = request.getSession().getId(); %>
<%@include file="/common/authHeader.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Mail Write</title>
<SCRIPT type="text/javascript">
function makePAID() {document.write('<input type="hidden" id="paid" name="paid" value="<%=sessionID%>"/>');}
</SCRIPT>
</head>
	<style>
      body,td {
        font-size: 10pt;
      }
      table {
        background-color: black;
        border: 1px black solid;
        border-collapse: collapse;
      }
      th {
        border: 1px outset silver;
        background-color: maroon;
        color: white;
      }
      tr {
        background-color: white;
        margin: 1px;
      }
    </style>
<body>

<form name="writeForm" method="post" action="/addr/pAddrSave.do">
	

	<input type="hidden" name="type" value="private">
    <script type="text/javascript">makePAID();</script>
	<table width="60%" border="1" style="">
	<tr>
		<td colspan="2">
			<INPUT TYPE="submit" value="submit">
		</td>
	</tr>
	<tr>
		<td style="width:20%">이름</td>
		<td>
			<input type="text" name="name" style="width:95%" value="">						
		</td>
	</tr>
	<tr>
		<td>메일주소</td>
		<td>
			<input type="text" name="email" style="width:95%" value="">						
		</td>
	</tr>
	<tr>
		<td>회사</td>
		<td>
			<input type="text" name="company" style="width:95%" value="">						
		</td>
	</tr>
	<tr>
		<td>이동전화</td>
		<td>
			<input type="text" name="mobile" style="width:95%" value="">						
		</td>
	</tr>
	<tr>
		<td>그룹</td>
		<td>
			<select name="groupSeq">
				<option value="0">--</option>
			<c:forEach var="group" items="${groups}" varStatus="loop">
				<option value="${group.groupSeq}">${group.groupName}</option>
			</c:forEach>
			</select>						
		</td>
	</tr>
	</table>
</form>
</body>
</html>