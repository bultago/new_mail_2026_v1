<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<input type="hidden" id="myMode" value="day">
<input type="hidden" id="myYear" value="${schedulerVo.thisYear}">
<input type="hidden" id="myMonth" value="${schedulerVo.thisMonth}">
<input type="hidden" id="myDay" value="${schedulerVo.thisDay}">
<input type="hidden" id="thisDate" value="${schedulerVo.todayStr}">

<div class="TM_listWrapper">
<table class="dayScheduler_list">
	<c:forEach var="count" begin="0" end="20">
	<tr>
		<c:if test="${count == 0}">
		<td class="numTD" nowrap><tctl:msg key="scheduler.anniversary" bundle="scheduler"/></td>
		</c:if>
		<c:if test="${count != 0}">
		<td class="numTD">${count}</td>
		</c:if>
		<td class="schedulerLineTd <c:if test="${schedulerVo.todayStr == schedulerVo.thisdayStr}">today</c:if>">
			<table class="schedulerTable">
				<c:if test="${count == 0}">
				<tr>
					<td id="date_td_${schedulerVo.thisdayStr}" class="schedulerDateTd">
						<span id="scheduler_holiday_${schedulerVo.thisdayStr}"></span>
						<div id="schedule_day_all_${schedulerVo.thisdayStr}">
							<div id="plan_all_${schedulerVo.thisdayStr}" class="planAll"></div>
						</div>
					</td>
					<td align="right" id="plan_td_${schedulerVo.thisdayStr}" height="30px">
						<a id="plan_count_${schedulerVo.thisdayStr}" count="0" href="javascript:;" class="planCount" onclick="openPlanAll('${schedulerVo.thisdayStr}');event.cancelBubble=true"></a>
					</td>
				</tr>
				</c:if>
				<c:if test="${count != 0}">
				<tr>
					<td colspan="2" class="scheduleDayTd">
						<div class="scheduleDiv" id="schedule_day_${schedulerVo.thisdayStr}" onclick="viewInputBox('${schedulerVo.thisdayStr}')">
							<div id="plan_${schedulerVo.thisdayStr}_${count}" status="n" class="plan" ids="${schedulerVo.thisdayStr}"></div>
						</div>
					</td>
				</tr>
				</c:if>
			</table>
		</td>
	</tr>
	</c:forEach>
</table>
</div>
<c:if test="${fn:startsWith(fn:substring(lunar,4,6), '0')}">
<c:set var="lunarMonth" value="${fn:substring(lunar,5,6)}"/>
</c:if>
<c:if test="${!fn:startsWith(fn:substring(lunar,4,6), '0')}">
<c:set var="lunarMonth" value="${fn:substring(lunar,4,6)}"/>
</c:if>
<script type="text/javascript">
var tabMenuData = {
		year:'${schedulerVo.thisYear}', yearText:'<tctl:msg key="scheduler.year" bundle="scheduler"/>', 
       	month:'${schedulerVo.thisMonth}', monthText:'<tctl:msg key="scheduler.month.${schedulerVo.thisMonth}" bundle="scheduler"/>',
       	day:'${schedulerVo.thisDay}', dayText:'<tctl:msg key="scheduler.day" bundle="scheduler"/>',
   	  	lunarMonth:'${fn:substring(lunar,4,6)}', lunarDay:'${fn:substring(lunar,6,8)}', lunarText:'<tctl:msg key="scheduler.lunar" bundle="scheduler" />',
		lunarMonthText:'<tctl:msg key="scheduler.month.${lunarMonth}" bundle="scheduler"/>',
   	  	prevLink:'gotoDay("${schedulerVo.prevYear}","${schedulerVo.prevMonth}","${schedulerVo.prevDay}")',
   		nextLink:'gotoDay("${schedulerVo.nextYear}","${schedulerVo.nextMonth}","${schedulerVo.nextDay}")',
   		dayOfWeek:'${schedulerVo.thisDayOfWeek}'
   	   	};
makeDayTabMenu(tabMenuData);
readHoliday('${schedulerVo.thisYear}', '${schedulerVo.thisYear}0101');
setSchedulerTab('0');
setSchedulerInfo('<tctl:msg key="scheduler.menu.day" bundle="scheduler"/>');
schedulerDataControl.setTimeList(makeTimeArray());
schedulerDataControl.readDayScheduleList('${schedulerVo.thisYear}','${schedulerVo.thisMonth}','${schedulerVo.thisDay}');
jQuery("#pageNavi").empty();
</script>