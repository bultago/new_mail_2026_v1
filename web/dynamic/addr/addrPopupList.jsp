<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<input type="hidden" id="groupSeq" value="${fn:escapeXml(groupSeq)}">
<input type="hidden" id="bookSeq" value="${fn:escapeXml(bookSeq)}">  
<input type="hidden" id="leadingPattern" value="${leadingPattern}">
<input type="hidden" id="presearchType" value="${fn:escapeXml(searchType)}">
<input type="hidden" id="prekeyWord" value="${fn:escapeXml(keyWord)}">
<input type="hidden" id="leadingGroupSeq" value="${fn:escapeXml(groupSeq)}">

<table class="TB_addSearchFrom" cellpadding="0" cellspacing="0">
	<tr>
		<td class="inputArea" style="padding: 0px 5px">
			<div style="float:left;white-space:nowrap;margin-top:4px">
				<strong><tctl:msg key="addr.search.addr" bundle="addr"/></strong>
			</div>
			<div style="float:left;white-space:nowrap;padding-left:10px;padding-right:2px;">	
				<div id="searchTypeSelect"></div>
				<script type="text/javascript">
				jQuery("#searchTypeSelect").selectbox({selectId:"searchType",
						selectFunc:""},
						"${fn:escapeXml(searchType)}",
						[{index:"<tctl:msg key="addr.info.label.001" bundle="addr"/>",value:"fname"},
						 {index:"<tctl:msg key="addr.info.label.006" bundle="addr"/>",value:"email"}]);		
				</script>
			</div>
			<div style="float:left;white-space:nowrap">
				<input type="text" name="_tmp" style="width:0;height:0;display:none"/>
				<input type="text" id="keyWord" name="keyWord" class="IP200" style="width:150px" value="${fn:escapeXml(keyWord)}" onKeyPress="(keyEvent(event) == 13) ? searchAddress() : '';"/>
				<a class="btn_TB_modify" href="javascript:;" onclick="searchAddress()"><span><tctl:msg key="comn.search" bundle="common"/></span></a>
			</div>
		</td>
	</tr>
</table>
<script>clearLeading()</script>
<c:forEach var="charater" items="${leadingCharaters}" varStatus="loop">
	<script>makeLeading('${charater}','${leadingPattern}')</script>
</c:forEach>

<form name="privateAddrForm">
<table class="TB_addList" cellpadding="0" cellspacing="0">
	<tr>
		<th class="checkbox"><input id="memberSeq_all" type="checkbox" onclick="checkAll(this,privateAddrForm.sel);"></th>
		<th class="name"><tctl:msg key="addr.info.label.001" bundle="addr"/></th>
		<th class="email"><tctl:msg key="addr.info.label.006" bundle="addr"/></th>
	</tr>
	<c:forEach var="members" items="${members}" varStatus="loop">
	<tr>
		<td class="lineRight" align="center" style="height:20px;">
			<input id="memberSeq_${members.memberSeq}" type="checkbox" name="sel" value="${members.memberName}|${members.memberEmail}">		
		</td>
		<td class="lineRight" style="height:20px;">
			<table class='TM_HiddenTextTable'>
				<tr>
					<td style="border:0;" title="${members.memberName}">
						<div class='TM_HiddenTextDiv'>${members.memberName}</div>
					</td>
				</tr>
			</table>
		</td>
		<td style="height:20px;">
			<table class='TM_HiddenTextTable'>
				<tr>
					<td style="border:0;" title="${members.memberEmail}">
						<div class='TM_HiddenTextDiv'>${members.memberEmail}</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>	
	</c:forEach>
</table>
<table border="0" width="100%" cellspacing="1" style="margin-top:10px">
	<tr>
		<td align="center">
			<div id="pageCounter" class="pageNum">
				<%@include file="/common/pageCounter.jsp" %>
			</div>
		</td>
	</tr>
</table>
</form>