<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<script language = "javascript">
var SchedulerDataControl = Class.create({
	initialize: function(){
	},setCalendarDateList : function(calendarDateList) {
		this.calendarDateList = calendarDateList;
	},readCalendarScheduleList : function(year, month){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		SchedulerService.getJsonMonthScheduleList(year, month,{
			callback:function(calendarScheduleList){		
		_this.checkCalendarSchedule(calendarScheduleList);
		}});
	},checkCalendarSchedule : function(calendarScheduleList) {
		var _this = this;
		var drowStartDate;
		var data; var size; var startDate; var index; var count;
		var dateCount = new Array(_this.calendarDateList.length);

		for(var i=0; i < this.calendarDateList.length; i++) {
			dateCount[i] = 0;
		}
		
		for (var i=0; i<calendarScheduleList.length; i++) {
			for (var j=0; j < calendarScheduleList[i].drowplanList.length; j++) {
				data = calendarScheduleList[i].drowplanList[j].split("|");
				size = data[0];
				startDate = data[1]; 

				for(var k=0; k < this.calendarDateList.length; k++) {
					if (startDate == this.calendarDateList[k]) {
						index = k;
						break;
					}
				}
				index = parseInt(index,10);
				size = parseInt(size,10);

				for(var l=index; l < index+size; l++) {
					dateCount[l] = dateCount[l] + 1;
				}
			}
		}
		
		for(var i=0; i < this.calendarDateList.length; i++) {
			count = dateCount[i];
			jQuery("#calendar_date_"+this.calendarDateList[i]).attr("count",count);
			if (count > 0) {
				jQuery("#calendar_date_"+this.calendarDateList[i]+" a").addClass("ok");
			}
		}
	}
});

var schedulerDataControl;
jQuery().ready(function(){
	schedulerDataControl = new SchedulerDataControl();
	schedulerDataControl.setCalendarDateList(${schedulerVo.monthDayList});
	schedulerDataControl.readCalendarScheduleList('${schedulerVo.thisYear}','${schedulerVo.thisMonth}');	
});

function gotoCalendar(obj, year, month) {
	
	var parentId = jQuery(obj).parent().parent().parent().parent().attr("id");

	var url = "/portlet/calendarView.do";
	var param = {"year":year, "month":month};

	jQuery("#"+parentId).load(url, param);
}

function gotoCalendarDay(year, month, day) {
	var url = "/dynamic/scheduler/schedulerCommon.do";
	var param = "?type=calendar&calendarYear="+year+"&calendarMonth="+month+"&calendarDay="+day;
	this.location = url+param;
}
</script>

<div class="mailHome_calendar">
	<div class="roundTitle">
		<p class="calendar_date">
			<a href="javascript:;" class="prev" onclick="gotoCalendar(this, '${schedulerVo.prevYear}','${schedulerVo.prevMonth}')"></a>
			<span class="title">
				${schedulerVo.thisYear}<tctl:msg key="scheduler.year" bundle="scheduler"/>
				<tctl:msg key="scheduler.month.${schedulerVo.thisMonth}" bundle="scheduler"/>
			</span>
			<a href="javascript:;" class="next" onclick="gotoCalendar(this, '${schedulerVo.nextYear}','${schedulerVo.nextMonth}')"></a>
			<a href="#" onclick="gotoCalendarDay('${schedulerVo.todayYear}','${schedulerVo.todayMonth}', '${schedulerVo.todayDay}')" class="btn_style5"><span><tctl:msg key="scheduler.menu.today" bundle="scheduler"/></span></a>
		</p>
	</div>
	<div class="calendar">
		<table class="calendar_s_day">
			<tr class="title">
				<th class="sunday"><tctl:msg key="scheduler.date.0" bundle="scheduler"/></th>
				<th><tctl:msg key="scheduler.date.1" bundle="scheduler"/></th>
				<th><tctl:msg key="scheduler.date.2" bundle="scheduler"/></th>
				<th><tctl:msg key="scheduler.date.3" bundle="scheduler"/></th>
				<th><tctl:msg key="scheduler.date.4" bundle="scheduler"/></th>
				<th><tctl:msg key="scheduler.date.5" bundle="scheduler"/></th>
				<th class="saturay last"><tctl:msg key="scheduler.date.6" bundle="scheduler"/></th>
			</tr>
			<c:forEach var="monthDay" items="${schedulerVo.monthDayList}" varStatus="loop">
			<c:if test="${loop.index % 7 == 0}">
			<tr>
			</c:if>	
				<td id="calendar_date_${monthDay}" class="<c:if test="${schedulerVo.todayStr == monthDay}">today</c:if><c:if test="${fn:substring(schedulerVo.thisdayStr,4,6) != fn:substring(monthDay,4,6)}"> prevNext</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6)) && (loop.index % 7 == 0)}"> sunday</c:if><c:if test="${(fn:substring(schedulerVo.thisdayStr,4,6) == fn:substring(monthDay,4,6))&&(loop.count % 7 == 0)}"> saturay</c:if>" count="0">
					<a href="#" onclick="gotoCalendarDay('${fn:substring(monthDay,0,4)}','${fn:substring(monthDay,4,6)}','${fn:substring(monthDay,6,8)}')">
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
	</div>
</div>