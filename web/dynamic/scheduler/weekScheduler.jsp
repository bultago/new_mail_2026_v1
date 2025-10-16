<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<input type="hidden" id="myMode" value="week">
<input type="hidden" id="myYear" value="${schedulerVo.thisYear}">
<input type="hidden" id="myMonth" value="${schedulerVo.thisMonth}">
<input type="hidden" id="myDay" value="${schedulerVo.thisDay}">
<input type="hidden" id="thisDate" value="${schedulerVo.todayStr}">

<div class="TM_listWrapper">
<table class="scheduler_week_TB TM_scheduler_basicTB">
	<tr>
		<td valign=top>
			<table class="TM_scheduler_basicTB">
				<tr>
					<td colspan="2">
						<table width="100%" id="scheduler_week">
							<tr>
								<td class="dateTd" nowrap></td>
							<c:forEach var="weekDay" items="${schedulerVo.weekDayList}" varStatus="loop">
								<td class="<c:if test="${loop.index % 7 == 0}">TM_F_POINT</c:if><c:if test="${loop.index % 7 == 6}">TM_F_POINT2</c:if> dateTd">
									<c:if test="${fn:startsWith(fn:substring(weekDay,4,6), '0')}">
										${fn:substring(weekDay,5,6)}
									</c:if>
									<c:if test="${!fn:startsWith(fn:substring(weekDay,4,6), '0')}">
										${fn:substring(weekDay,4,6)}
									</c:if>
									/
									<c:if test="${fn:startsWith(fn:substring(weekDay,6,8), '0')}">
										${fn:substring(weekDay,7,8)}
									</c:if>
									<c:if test="${!fn:startsWith(fn:substring(weekDay,6,8), '0')}">
										${fn:substring(weekDay,6,8)}
									</c:if>
									(<tctl:msg key="scheduler.date.${loop.index}" bundle="scheduler"/>)
								</td>
							</c:forEach>
							</tr>
							<c:forEach var="count" begin="0" end="20">
							<tr>
								<c:if test="${count == 0}">
								<td class="numTD"><tctl:msg key="scheduler.anniversary" bundle="scheduler"/></td>
								</c:if>
								<c:if test="${count != 0}">
								<td class="numTD">${count}</td>
								</c:if>
								<c:forEach var="weekDay" items="${schedulerVo.weekDayList}" varStatus="loop">
								<td class="schedulerLineTd <c:if test="${schedulerVo.todayStr == weekDay}">today</c:if>">
									<table class="schedulerTable">
									<c:if test="${count == 0}">
										<tr>
											<td id="date_td_${weekDay}" class="schedulerDateTd">
												<span id="scheduler_holiday_${weekDay}"></span>
												<div id="schedule_week_all_${weekDay}">
													<div id="plan_all_${weekDay}" class="planAll"></div>
												</div>
											</td>
											<td align="right" id="plan_td_${weekDay}">
												<a id="plan_count_${weekDay}" count="0" href="javascript:;" class="planCount" onclick="openPlanAll('${weekDay}');event.cancelBubble=true"></a>
											</td>
										</tr>
									</c:if>
									<c:if test="${count != 0}">	
										<tr>
											<td colspan="2" class="scheduleWeekTd" align="center">
												<div class="scheduleDiv" id="schedule_week_${weekDay}" onclick="viewInputBox('${weekDay}')">
													<div id="plan_${weekDay}_${count}" status="n" class="plan" ids="${weekDay}"></div>
												</div>
											</td>
										</tr>
									</c:if>
									</table>
								</td>
								</c:forEach>
							</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<c:if test="${fn:startsWith(fn:substring(firstLunar,4,6), '0')}">
<c:set var="firstLunarMonth" value="${fn:substring(firstLunar,5,6)}"/>
</c:if>
<c:if test="${!fn:startsWith(fn:substring(firstLunar,4,6), '0')}">
<c:set var="firstLunarMonth" value="${fn:substring(firstLunar,4,6)}"/>
</c:if>
<c:if test="${fn:startsWith(fn:substring(lastLunar,4,6), '0')}">
<c:set var="lastLunarMonth" value="${fn:substring(lastLunar,5,6)}"/>
</c:if>
<c:if test="${!fn:startsWith(fn:substring(lastLunar,4,6), '0')}">
<c:set var="lastLunarMonth" value="${fn:substring(lastLunar,4,6)}"/>
</c:if>
<script type="text/javascript">
var tabMenuData = {firstYear:'${schedulerVo.firstYear}', firstMonth:'${schedulerVo.firstMonth}', firstDay:'${schedulerVo.firstDay}',
		lastYear:'${schedulerVo.lastYear}', lastMonth:'${schedulerVo.lastMonth}', lastDay:'${schedulerVo.lastDay}',
		yearText:'<tctl:msg key="scheduler.year" bundle="scheduler"/>', 
       	firstMonthText:'<tctl:msg key="scheduler.month.${schedulerVo.firstMonth}" bundle="scheduler"/>', 
       	dayText:'<tctl:msg key="scheduler.day" bundle="scheduler"/>',
       	firstLunarYear:'${fn:substring(firstLunar,0,4)}', firstLunarMonth:'${fn:substring(firstLunar,4,6)}', firstLunarDay:'${fn:substring(firstLunar,6,8)}',
       	lastLunarYear:'${fn:substring(lastLunar,0,4)}', lastLunarMonth:'${fn:substring(lastLunar,4,6)}', lastLunarDay:'${fn:substring(lastLunar,6,8)}',
       	firstLunarMonthText:'<tctl:msg key="scheduler.month.${firstLunarMonth}" bundle="scheduler"/>',
       	lastLunarMonthText:'<tctl:msg key="scheduler.month.${lastLunarMonth}" bundle="scheduler"/>',
   	  	lunarText:'<tctl:msg key="scheduler.lunar" bundle="scheduler" />',
   	 	lastMonthText:'<tctl:msg key="scheduler.month.${schedulerVo.lastMonth}" bundle="scheduler"/>',
   	  	prevLink:'gotoWeek("${schedulerVo.prevYear}","${schedulerVo.prevMonth}","${schedulerVo.prevDay}")',
   		nextLink:'gotoWeek("${schedulerVo.nextYear}","${schedulerVo.nextMonth}","${schedulerVo.nextDay}")'};
makeWeekTabMenu(tabMenuData);
readHoliday('${schedulerVo.thisYear}', ${schedulerVo.weekDayList});
setSchedulerTab('1');
setSchedulerInfo('<tctl:msg key="scheduler.menu.week" bundle="scheduler"/>');
schedulerDataControl.setWeekDateList(${schedulerVo.weekDayList});
schedulerDataControl.setTimeList(makeTimeArray());
schedulerDataControl.readWeekScheduleList('${schedulerVo.thisYear}','${schedulerVo.thisMonth}','${schedulerVo.thisDay}');
jQuery("#pageNavi").empty();
jQuery(".plan").draggable({
	cursor: 'move',
	delay: 500,
	cursorAt: { top: -5, left: -5 },
	helper: function(event) {
		var id = jQuery(this).attr('id');
		return jQuery('<div class="dragBar" style="padding:5px;width:100px;overflow:hidden" nowrap>'+jQuery("#"+id+" .title").text()+'</div>');
	}
});
jQuery(".scheduleDiv").droppable({
	accept: ".plan",
	//activeClass: 'ui-state-hover',
	//hoverClass: 'ui-state-active',
	drop: function(ev, ui) {
		dropId = jQuery(this).attr('id');
		if (ui.draggable.attr('repeat') == 'on') {
			alert(schedulerMsg.scheduler_repeat_donot_move);
			return;
		}
		scheduleDnD('week', ui.draggable, dropId, ${schedulerVo.weekDayList});
	}
});
</script>