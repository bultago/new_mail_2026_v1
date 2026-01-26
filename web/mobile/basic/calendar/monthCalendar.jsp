<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
		<script type="text/javascript" src="/mobile/basic/calendar/calendar.js"></script>
		<script type="text/javascript">			
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href=\"javascript:toggleCalendarType();\" class='btn_dr'><tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/>"
			+"</a>";				

		}

		jQuery().ready(function(){
		    setTimeout(function() { window.scrollTo(0, 1) }, 1000);
		});
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/mobile/basic/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			<c:set var="thisYear" value="${fn:substring(calendar.thisday,0,4)}"/>	
			<c:set var="thisMonth" value="${fn:substring(calendar.thisday,4,6)}"/>
			<div class="container">
				<div class="title_box">
					<div class="date_num">
						<a class="btn_prev" href="/mobile/calendar/monthCalendar.do?date=${fn:substring(calendar.preday,0,6)}01"></a>
						<span class="date">${thisYear}.${thisMonth}</span>
						<a class="btn_next" href="/mobile/calendar/monthCalendar.do?date=${fn:substring(calendar.nextday,0,6)}01"></a>
					</div>
				</div>
				<table class="month">
					<colgroup span="7" width="14%"></colgroup>
					<tr>
						<th class="hol none"><tctl:msg key="scheduler.date.0" bundle="scheduler"/></th>
						<th><tctl:msg key="scheduler.date.1" bundle="scheduler"/></th>
						<th><tctl:msg key="scheduler.date.2" bundle="scheduler"/></th>
						<th><tctl:msg key="scheduler.date.3" bundle="scheduler"/></th>
						<th><tctl:msg key="scheduler.date.4" bundle="scheduler"/></th>
						<th class="none"><tctl:msg key="scheduler.date.5" bundle="scheduler"/></th>
						<th class="none"><tctl:msg key="scheduler.date.6" bundle="scheduler"/></th>
					</tr>
					<c:forEach var="date" items="${calendar.dateList}" varStatus="loop">
						<c:set var="index" value="0"/>
						<c:if test="${loop.index % 7 == 0}">
						<tr>
						</c:if>
							<td class="<c:if test="${loop.count % 7 == 0}"> none </c:if><c:if test="${date eq calendar.thisday}"> cho </c:if><c:if test="${date eq calendar.today}"> today </c:if><c:if test="${(fn:substring(date,4,6) eq thisMonth)&&(loop.count % 7 == 1)}"> hol </c:if><c:if test="${(fn:substring(date,4,6) ne thisMonth)&&(loop.count % 7 == 1)}"> hol_dim </c:if><c:if test="${(fn:substring(date,4,6) ne thisMonth)&&(loop.count % 7 != 1)}"> dim </c:if>">
								<a href="/mobile/calendar/monthCalendar.do?date=${date}">
									<c:forEach var="calendarInfo" items="${calendar.calendarInfos}">
										<c:if test="${(date eq calendarInfo.drowStartDate) || ((date >= calendarInfo.drowStartDate) && (date <= calendarInfo.drowEndDate))}">
											<c:set var="index" value="${index+1}"/>
										</c:if>
									</c:forEach>
									<c:if test="${fn:startsWith(fn:substring(date,6,8), '0')}">
										<c:set var="drowDate" value="${fn:substring(date,7,8)}"/>
									</c:if>
									<c:if test="${!fn:startsWith(fn:substring(date,6,8), '0')}">
										<c:set var="drowDate" value="${fn:substring(date,6,8)}"/>
									</c:if>
									${drowDate}
									<c:if test="${index > 0}">
										<span class="clip"></span>
										<span class="plus">+${index}</span>
									</c:if>
								</a>
							</td>
						<c:if test="${loop.count % 7 == 0}">
						</tr>
						</c:if>	
					</c:forEach>
				</table>
				
				<ul class="schedule_list">
				<c:forEach var="holiday" items="${calendarHolidayList}">
					<c:if test="${calendar.thisday eq holiday.holidayDate}">
						<li style="background-color:#FFFFCD">
							<a class="last" href="javascript:;">
								<span class="list_title" style='<c:if test="${holiday.holiday eq 'on'}">color:red</c:if>'><tctl:msg key="scheduler.holiday" bundle="scheduler"/></span>
								<span class="list_title_sub">
									${holiday.holidayName}
								</span>
								<strong class="link_arrow">&gt;</strong>
							</a>
						</li>
					</c:if>
				</c:forEach>
				<c:forEach var="calendarInfo" items="${calendar.calendarInfos}">
					<c:choose>
						<c:when test="${calendar.thisday eq calendarInfo.drowStartDate}">
							<li>
								<a class="last" href="/mobile/calendar/viewCalendar.do?schedulerId=${calendarInfo.schedulerId}&date=${calendar.thisday}">
									<span class="list_title">${calendarInfo.title}</span>
									<span class="list_title_sub">
										<c:if test="${calendarInfo.allDay eq 'on'}">
											<tctl:msg key="scheduler.allday" bundle="scheduler"/>
										</c:if>
										<c:if test="${calendarInfo.allDay ne 'on'}">
										<c:choose>
											<c:when test="${(calendar.thisday ne calendarInfo.drowStartDate) && (calendar.thisday ne calendarInfo.drowEndDate)}">
												<tctl:msg key="scheduler.allday" bundle="scheduler"/>
											</c:when>
											<c:otherwise>
												${fn:substring(calendarInfo.drowStartTime, 0, 2)}:${fn:substring(calendarInfo.drowStartTime, 2, 4)}
												~
												<c:if test="${calendar.thisday ne calendarInfo.drowEndDate}">
													<fmt:parseDate var="edate" pattern="yyyyMMdd" value="${calendarInfo.drowEndDate}"/>
													<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd"/>													
												</c:if>
												${fn:substring(calendarInfo.drowEndTime, 0, 2)}:${fn:substring(calendarInfo.drowEndTime, 2, 4)}
											</c:otherwise>
										</c:choose>
										</c:if>
									</span>
									<strong class="link_arrow">&gt;</strong>
								</a>
							</li>
						</c:when>
						<c:when test="${(calendar.thisday >= calendarInfo.drowStartDate) && (calendar.thisday <= calendarInfo.drowEndDate)}">
							<li>
								<a class="last" href="/mobile/calendar/viewCalendar.do?schedulerId=${calendarInfo.schedulerId}&date=${calendar.thisday}">
									<span class="list_title">${calendarInfo.title}</span>
									<span class="list_title_sub">
										<c:if test="${calendarInfo.allDay eq 'on'}">
											<tctl:msg key="scheduler.allday" bundle="scheduler"/>
										</c:if>
										<c:if test="${calendarInfo.allDay ne 'on'}">
										<c:choose>
											<c:when test="${(calendar.thisday ne calendarInfo.drowStartDate) && (calendar.thisday ne calendarInfo.drowEndDate)}">
												<tctl:msg key="scheduler.allday" bundle="scheduler"/>
											</c:when>
											<c:otherwise>
												<c:if test="${calendar.thisday ne calendarInfo.drowStartDate}">
													<fmt:parseDate var="sdate" pattern="yyyyMMdd" value="${calendarInfo.drowStartDate}"/>
													<fmt:formatDate value="${sdate}" pattern="yyyy-MM-dd"/>													
												</c:if>
												${fn:substring(calendarInfo.drowStartTime, 0, 2)}:${fn:substring(calendarInfo.drowStartTime, 2, 4)}
												~
												<c:if test="${calendar.thisday ne calendarInfo.drowEndDate}">
													<fmt:parseDate var="edate" pattern="yyyyMMdd" value="${calendarInfo.drowEndDate}"/>
													<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd"/>													
												</c:if>
												${fn:substring(calendarInfo.drowEndTime, 0, 2)}:${fn:substring(calendarInfo.drowEndTime, 2, 4)}
											</c:otherwise>
										</c:choose>
										</c:if>
									</span>
									<strong class="link_arrow">&gt;</strong>
								</a>
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
					<li class="schedule_add">
						<a href="/mobile/calendar/writeCalendar.do?date=${calendar.thisday}&listType=month"><tctl:msg key="scheduler.title.mobile.add" bundle="scheduler"/></a>
					</li>
				</ul>
			</div>

			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>