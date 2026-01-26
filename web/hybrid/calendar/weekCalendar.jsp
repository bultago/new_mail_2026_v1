<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
		<script type="text/javascript" src="/hybrid/calendar/calendar.js"></script>
		<script type="text/javascript">			
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href=\"javascript:toggleCalendarType();\" class='btn_dr'><tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/>"
			+"</a>";				

		}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/hybrid/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container" id="calendarWrap">
				<div class="title_box">
					<div class="date_num" id="datePart">
						<a class="btn_prev" href="/hybrid/calendar/weekCalendar.do?date=${calendar.preday}"></a>
						<span class="date">
							<c:forEach var="date" items="${calendar.dateList}" varStatus="loop">
								<c:if test="${loop.first}">
									${fn:substring(date,0,4)}.${fn:substring(date,4,6)}.${fn:substring(date,6,8)} -
								</c:if>
								<c:if test="${loop.last}">
									${fn:substring(date,0,4)}.${fn:substring(date,4,6)}.${fn:substring(date,6,8)}
								</c:if>
							</c:forEach>
						</span>
						<a class="btn_next" href="/mobile/calendar/weekCalendar.do?date=${calendar.nextday}"></a>
					</div>
				</div>
				<ul class="calendar_list week">
					<c:forEach var="date" items="${calendar.dateList}" varStatus="loop">
						<li>
							<h4 class="date <c:if test="${date eq calendar.today}"> today </c:if> <c:if test="${loop.index == 0}"> border_none </c:if>">
								<em class="<c:if test="${loop.index == 0}"> hol </c:if>">${fn:substring(date,0,4)}.${fn:substring(date,4,6)}.${fn:substring(date,6,8)} (<tctl:msg key="scheduler.date.${loop.index}" bundle="scheduler"/>)</em>
								<a class="btn2" href="/hybrid/calendar/writeCalendar.do?date=${date}&listType=week"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
							</h4>
							<ul class="schedule_list">
								<c:forEach var="holiday" items="${calendarHolidayList}">
									<c:if test="${date eq holiday.holidayDate}">
										<li style="background-color:#FFFFCD">
											<a href="javascript:;">
												<span class="list_title" style='<c:if test="${holiday.holiday eq 'on'}">color:red</c:if>'><tctl:msg key="scheduler.holiday" bundle="scheduler"/></span>
												<span class="list_title_sub">
													${holiday.holidayName}
												</span>
												<strong class="link_arrow">&gt;</strong>
											</a>
										</li>
									</c:if>
								</c:forEach>
								<c:set var="drowToday" value="off"/>
								<c:forEach var="calendarInfo" items="${calendar.calendarInfos}">
									<c:choose>
									<c:when test="${date eq calendarInfo.drowStartDate}">
										<c:set var="drowToday" value="on"/>
										<li>
											<a href="/hybrid/calendar/viewCalendar.do?schedulerId=${calendarInfo.schedulerId}">
												<span class="list_title">${calendarInfo.title}</span>
												<span class="list_title_sub">
													<c:if test="${calendarInfo.allDay eq 'on'}">
														<tctl:msg key="scheduler.allday" bundle="scheduler"/>
													</c:if>
													<c:if test="${calendarInfo.allDay ne 'on'}">
													<c:choose>
														<c:when test="${(date ne calendarInfo.drowStartDate) && (date ne calendarInfo.drowEndDate)}">
															<tctl:msg key="scheduler.allday" bundle="scheduler"/>
														</c:when>
														<c:otherwise>
															${fn:substring(calendarInfo.drowStartTime, 0, 2)}:${fn:substring(calendarInfo.drowStartTime, 2, 4)}
															~
															<c:if test="${date ne calendarInfo.drowEndDate}">
																<fmt:parseDate var="edate" pattern="yyyyMMdd" value="${calendarInfo.drowEndDate}"/>
																<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd"/>													
															</c:if>
															${fn:substring(calendarInfo.drowEndTime, 0, 2)}:${fn:substring(calendarInfo.drowEndTime, 2, 4)}
														</c:otherwise>
													</c:choose>
													</c:if>
												</span>
											</a>
										</li>
									</c:when>
									<c:when test="${(date >= calendarInfo.drowStartDate) && (date <= calendarInfo.drowEndDate)}">
										<c:set var="drowToday" value="on"/>
										<li>
											<a href="/hybrid/calendar/viewCalendar.do?schedulerId=${calendarInfo.schedulerId}">
												<span class="list_title">${calendarInfo.title}</span>
												<span class="list_title_sub">
													<c:if test="${calendarInfo.allDay eq 'on'}">
														<tctl:msg key="scheduler.allday" bundle="scheduler"/>
													</c:if>
													<c:if test="${calendarInfo.allDay ne 'on'}">
													<c:choose>
														<c:when test="${(date ne calendarInfo.drowStartDate) && (date ne calendarInfo.drowEndDate)}">
															<tctl:msg key="scheduler.allday" bundle="scheduler"/>
														</c:when>
														<c:otherwise>
															<c:if test="${date ne calendarInfo.drowStartDate}">
																<fmt:parseDate var="sdate" pattern="yyyyMMdd" value="${calendarInfo.drowStartDate}"/>
																<fmt:formatDate value="${sdate}" pattern="yyyy-MM-dd"/>													
															</c:if>
															${fn:substring(calendarInfo.drowStartTime, 0, 2)}:${fn:substring(calendarInfo.drowStartTime, 2, 4)}
															~
															<c:if test="${date ne calendarInfo.drowEndDate}">
																<fmt:parseDate var="edate" pattern="yyyyMMdd" value="${calendarInfo.drowEndDate}"/>
																<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd"/>													
															</c:if>
															${fn:substring(calendarInfo.drowEndTime, 0, 2)}:${fn:substring(calendarInfo.drowEndTime, 2, 4)}
														</c:otherwise>
													</c:choose>
													</c:if>
												</span>
											</a>
										</li>
									</c:when>
									</c:choose>
								</c:forEach>
								<c:if test="${drowToday == 'off'}">
									<li>
										<p class="schedule_none"><tctl:msg key="scheduler.empty" bundle="scheduler"/></p>
									</li>
								</c:if>
							</ul>
						</li>
					</c:forEach>
				</ul>
			</div>

			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>