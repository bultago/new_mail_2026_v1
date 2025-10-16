<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<div class="TM_listWrapper">
<table class="TM_scheduler_basicTB">
	<tr>
		<td>
			<table id="scheduler_process" width="100%">
			<c:forEach var="hours" begin="0" end="23">
				<tr>
					<td class="timeTD" nowrap>
						<c:if test="${hours < 12}">${hours}</c:if>
						<c:if test="${hours == 12}">${hours}</c:if>
						<c:if test="${hours > 12}">${hours - 12}</c:if>
						<c:if test="${hours < 12}"><tctl:msg key="scheduler.am" bundle="scheduler"/></c:if>
						<c:if test="${hours >= 12}"><tctl:msg key="scheduler.pm" bundle="scheduler"/></c:if>
					</td>
					<c:forEach var="weekDay" items="${weekDateList}" varStatus="loop">
					<td class="weekDottedTD <c:if test="${schedulerVo.todayStr == weekDay}">today</c:if>">
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
								<td class="schedulerProcessDate" onclick="viewInputBox('${weekDay}','${hours}:00')">
									<div class="progressScheduleDiv" id="progress_${weekDay}_${hours}00" count="0" index="0"></div>
								</td>
							</tr>
						</table>
					</td>
					</c:forEach>
				</tr>
				<tr>
					<td class="timeTD">
						30<tctl:msg key="scheduler.min" bundle="scheduler"/>
					</td>
					<c:forEach var="weekDay" items="${weekDateList}" varStatus="loop">
					<td class="schedulerProcessTd <c:if test="${schedulerVo.todayStr == weekDay}">today</c:if>">
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
								<td class="schedulerProcessDate" onclick="viewInputBox('${weekDay}','${hours}:30')">
									<div class="progressScheduleDiv" id="progress_${weekDay}_${hours}30" count="0" index="0"></div>
								</td>
							</tr>
						</table>
					</td>
					</c:forEach>
				</tr>
			</c:forEach>
			</table>
		</td>
	</tr>
</table>
</div>