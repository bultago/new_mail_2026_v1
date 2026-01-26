<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<input type="hidden" id="myMode" value="month">
<input type="hidden" id="myYear" value="${schedulerVo.thisYear}">
<input type="hidden" id="myMonth" value="${schedulerVo.thisMonth}">
<input type="hidden" id="thisDate" value="${schedulerVo.todayStr}">

<div class="TM_listWrapper">
<table id="scheduler_month">
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
		<td class="TM_F_POINT dateTd"><tctl:msg key="scheduler.date.sunday" bundle="scheduler"/></td>
		<td class="dateTd"><tctl:msg key="scheduler.date.monday" bundle="scheduler"/></td>
		<td class="dateTd"><tctl:msg key="scheduler.date.tuesday" bundle="scheduler"/></td>
		<td class="dateTd"><tctl:msg key="scheduler.date.wednesday" bundle="scheduler"/></td>
		<td class="dateTd"><tctl:msg key="scheduler.date.thursday" bundle="scheduler"/></td>
		<td class="dateTd"><tctl:msg key="scheduler.date.friday" bundle="scheduler"/></td>
		<td class="TM_F_POINT2 dateTd"><tctl:msg key="scheduler.date.saturday" bundle="scheduler"/></td>
	</tr>
	<c:forEach var="monthDay" items="${schedulerVo.monthDayList}" varStatus="loop">
		<c:if test="${loop.index % 7 == 0}">
			<tr>
		</c:if>	
			<td class="schedulerLineTd <c:if test="${schedulerVo.todayStr == monthDay}">today</c:if>">
				<table class="schedulerTable" onclick="viewInputBox('${monthDay}')">
					<tr>
						<td id="date_td_${monthDay}" class="schedulerDateTd <c:if test="${fn:substring(schedulerVo.thisdayStr,4,6) != fn:substring(monthDay,4,6)}">prevNextTd</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6)) && (loop.index % 7 == 0)}">TM_F_POINT3</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6))&&(loop.count % 7 == 0)}">TM_F_POINT4</c:if>">
							<c:if test="${fn:startsWith(fn:substring(monthDay,6,8), '0')}">
								${fn:substring(monthDay,7,8)}
							</c:if>
							<c:if test="${!fn:startsWith(fn:substring(monthDay,6,8), '0')}">
								${fn:substring(monthDay,6,8)}
							</c:if>
							<span id="scheduler_holiday_${monthDay}" class="schedulerHoliday"></span>
							<div id="schedule_month_all_${monthDay}">
								<div id="plan_all_${monthDay}" class="planAll"></div>
							</div>
						</td>
						<td align="right" id="plan_td_${monthDay}">
							<a id="plan_count_${monthDay}" count="0" href="javascript:;" class="planCount" onclick="openPlanAll('${monthDay}');event.cancelBubble=true"></a>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="scheduleMonthTd">
							<div class="scheduleDiv" id="schedule_month_${monthDay}">
								<div id="plan_${monthDay}_1" status="n" class="plan" ids="${monthDay}"></div>
								<div id="plan_${monthDay}_2" status="n" class="plan" ids="${monthDay}"></div>
								<div id="plan_${monthDay}_3" status="n" class="plan" ids="${monthDay}"></div>
								<div id="plan_${monthDay}_4" status="n" class="plan" ids="${monthDay}"></div>
							</div>
						</td>
					</tr>
				</table>
			</td>
		<c:if test="${loop.count % 7 == 0}">
			</tr>
		</c:if>	
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
var monthTextString = '<tctl:msg key="scheduler.month.${schedulerVo.thisMonth}" bundle="scheduler"/>';
if (LOCALE == 'ko') {
	monthTextString = '<tctl:msg key="scheduler.month" bundle="scheduler"/>';
}
var tabMenuData = {year:'${schedulerVo.thisYear}', yearText:'<tctl:msg key="scheduler.year" bundle="scheduler"/>', 
                   	month:'${schedulerVo.thisMonth}', monthText:monthTextString,
                   	day:'', dayText:'<tctl:msg key="scheduler.day" bundle="scheduler"/>',
               	  	lunarMonth:'${fn:substring(lunar,4,6)}', lunarDay:'${fn:substring(lunar,6,8)}', lunarText:'<tctl:msg key="scheduler.lunar" bundle="scheduler" />',
               	  	prevLink:'gotoMonth("${schedulerVo.prevYear}","${schedulerVo.prevMonth}")', lunarMonthText:'<tctl:msg key="scheduler.month.${lunarMonth}" bundle="scheduler"/>',
               		nextLink:'gotoMonth("${schedulerVo.nextYear}","${schedulerVo.nextMonth}")'};
makeMonthTabMenu(tabMenuData);
readHoliday('${schedulerVo.thisYear}', ${schedulerVo.monthDayList});
setSchedulerTab('2');
setSchedulerInfo('<tctl:msg key="scheduler.menu.month" bundle="scheduler"/>');
schedulerDataControl.setMonthDateList(${schedulerVo.monthDayList});
schedulerDataControl.readMonthScheduleList('${schedulerVo.thisYear}','${schedulerVo.thisMonth}');
jQuery("#pageNavi").empty();
jQuery(".plan").draggable({
	cursor: 'move',
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
		scheduleDnD('month', ui.draggable, dropId, ${schedulerVo.monthDayList});
	}
});
</script>
