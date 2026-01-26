<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<div class="TM_listWrapper">
<table id="scheduler_process" class="TM_scheduler_basicTB">
<c:forEach var="hours" begin="0" end="23">
	<tr>
		<td class="timeTD" nowrap>
			<c:if test="${hours < 12}">${hours}</c:if>
			<c:if test="${hours == 12}">${hours}</c:if>
			<c:if test="${hours > 12}">${hours - 12}</c:if>
			<c:if test="${hours < 12}"><tctl:msg key="scheduler.am" bundle="scheduler"/></c:if>
			<c:if test="${hours >= 12}"><tctl:msg key="scheduler.pm" bundle="scheduler"/></c:if>
		</td>
		<td class="dottedTD">
			<table class="schedulerTable">
				<colgroup span="7">          		
			        <col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
				</colgroup>
				<tr>
					<td class="schedulerProcessDate" onclick="viewInputBox('${schedulerVo.thisdayStr}','${hours}:00')">
						<div class="progressScheduleDiv" id="progress_${schedulerVo.thisdayStr}_${hours}00" count="0" index="0"></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="timeTD">
		30<tctl:msg key="scheduler.min" bundle="scheduler"/>
		</td>
		<td class="schedulerProcessTd">
			<table class="schedulerTable">
				<colgroup span="7">          		
			        <col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="14%"></col>
					<col width="13%"></col>
					<col width="13%"></col>
				</colgroup>
				<tr>
					<td class="schedulerProcessDate" onclick="viewInputBox('${schedulerVo.thisdayStr}','${hours}:30')">
						<div class="progressScheduleDiv" id="progress_${schedulerVo.thisdayStr}_${hours}30" count="0" index="0"></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</c:forEach>
</table>
</div>