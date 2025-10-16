<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<div class="calendar_s_date">
	&nbsp;
	<a href="javascript:;" class="prev" onclick="gotoCalendar('${schedulerVo.prevYear}','${schedulerVo.prevMonth}')">prev</a>
	<span class="date">
		${schedulerVo.thisYear}<tctl:msg key="scheduler.year" bundle="scheduler"/>
		<tctl:msg key="scheduler.month.${schedulerVo.thisMonth}" bundle="scheduler"/>
	</span>
	<a href="javascript:;" class="next" onclick="gotoCalendar('${schedulerVo.nextYear}','${schedulerVo.nextMonth}')">next</a>
</div>

<table class="calendar_s_day">
	<tbody>
	<tr class="title">			
		<td class="sunday"><tctl:msg key="scheduler.date.0" bundle="scheduler"/></td>
		<td><tctl:msg key="scheduler.date.1" bundle="scheduler"/></td>
		<td><tctl:msg key="scheduler.date.2" bundle="scheduler"/></td>
		<td><tctl:msg key="scheduler.date.3" bundle="scheduler"/></td>
		<td><tctl:msg key="scheduler.date.4" bundle="scheduler"/></td>
		<td><tctl:msg key="scheduler.date.5" bundle="scheduler"/></td>
		<td class="saturay"><tctl:msg key="scheduler.date.6" bundle="scheduler"/></td>
	</tr>
	<c:forEach var="monthDay" items="${schedulerVo.monthDayList}" varStatus="loop">
	<c:if test="${loop.index % 7 == 0}">
	<tr>
	</c:if>	
		<td id="calendar_date_${monthDay}" class="<c:if test="${schedulerVo.todayStr == monthDay}">today</c:if><c:if test="${fn:substring(schedulerVo.thisdayStr,4,6) != fn:substring(monthDay,4,6)}"> prevNext</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6)) && (loop.index % 7 == 0)}"> sunday</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6))&&(loop.count % 7 == 0)}"> saturay</c:if>" count="0">
			<a href="javascript:;" onclick="gotoCalendarDay('${fn:substring(monthDay,0,4)}','${fn:substring(monthDay,4,6)}','${fn:substring(monthDay,6,8)}')">
				<c:if test="${fn:startsWith(fn:substring(monthDay,6,8), '0')}">
					${fn:substring(monthDay,7,8)}
				</c:if>
				<c:if test="${!fn:startsWith(fn:substring(monthDay,6,8), '0')}">
					${fn:substring(monthDay,6,8)}
				</c:if>
			</a>
		</td>
	<c:if test="${loop.count % 7 == 0}">
	</tr>
	</c:if>	
	</c:forEach>
</table>
<script language="javascript">
schedulerDataControl.setCalendarDateList(${schedulerVo.monthDayList});
schedulerDataControl.readCalendarScheduleList('${schedulerVo.thisYear}','${schedulerVo.thisMonth}');	
</script>