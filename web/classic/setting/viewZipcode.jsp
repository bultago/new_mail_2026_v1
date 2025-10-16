<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<form name="searchZipcodeForm" onsubmit="return false;">
<input type="hidden" id="searchType" value="${fn:escapeXml(type)}">
<input type="hidden" id="prekeyword" value="${fn:escapeXml(dong)}">
<div style="text-align:center; padding:3px 0px;">
	<input type="text" id="dong" name="dong" value="${fn:escapeXml(dong)}" class="IP200" onKeyPress="(keyEvent(event) == 13) ? searchZipcode() : '';" />
	<input type="text" name="_tmp" style="display:none;width:0px;height:0px;"/>
	<a class="btn_basic" href="#" onclick="searchZipcode()"><span><tctl:msg key="common.10" bundle="setting"/></span></a>						
</div>
<table id="settingListTable" style="border-left:1px solid #ACACAC;border-right:1px solid #ACACAC;">
	<colgroup span="2">
		<col width="100px"></col>
		<col></col>
	</colgroup>
	<tr>
		<th><tctl:msg key="conf.userinfo.postcode" bundle="setting"/></th>
		<th><tctl:msg key="conf.userinfo.address" bundle="setting"/></th>
	</tr>
	<c:if test="${empty zipcodeList && dong != ''}">
	<tr>
		<td colspan="2" align="center"><tctl:msg key="conf.userinfo.postcode.list.empty" bundle="setting"/></td>
	</tr>
	</c:if>

	<c:forEach var="zipcode" items="${zipcodeList}">
	<tr>
		<td align="center">
			<a href="javascript:;" onclick="selectZipcode('${fn:escapeXml(type)}','${fn:escapeXml(zipcode.zipcode)}','${fn:escapeXml(zipcode.sido)}','${fn:escapeXml(zipcode.gugun)}','${fn:escapeXml(zipcode.dong)}','${fn:escapeXml(zipcode.bunji)}')">
				${fn:escapeXml(zipcode.zipcode)}
			</a>
		</td>
		<td class="subject zipCode">
			<a href="javascript:;" onclick="selectZipcode('${fn:escapeXml(type)}','${fn:escapeXml(zipcode.zipcode)}','${fn:escapeXml(zipcode.sido)}','${fn:escapeXml(zipcode.gugun)}','${fn:escapeXml(zipcode.dong)}','${fn:escapeXml(zipcode.bunji)}')">
				${fn:escapeXml(zipcode.sido)} ${fn:escapeXml(zipcode.gugun)} ${fn:escapeXml(zipcode.dong)} ${fn:escapeXml(zipcode.bunji)}
			</a>
		</td>
	</tr>
	</c:forEach>
</table>
<table width="100%" cellspacing="1" cellpadding="5">
	<tr>
		<td align="center" style="padding-top: 5px">
			<div id="pageCounter" class="pageNum">
				<%@include file="/common/pageCounter.jsp" %>
			</div>
		</td>
	</tr>
</table>
</form>
<c:if test="${empty zipcodeList || dong == ''}">
<div style="text-align: center;margin-top:10px;">
	<p style="padding:5px"><tctl:msg key="zipcode.001" bundle="common"/></p>
	<p style="padding:5px"><tctl:msg key="zipcode.002" bundle="common"/></p>
</div>
</c:if>