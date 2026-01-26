<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript">			
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href='javascript:toggleCalendarType();' class='btn_dr'><tctl:msg key="scheduler.title.view" bundle="scheduler"/>"
			+"</a>";				

		}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/hybrid/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<div class="title_box">
					<div class="btn_l">
						<c:if test="${target ne 'home'}">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
						</c:if>
						<c:if test="${calendarInfo.modify}">
						<a class="btn2" href="/hybrid/calendar/deleteCalendarQuestion.do?schedulerId=${calendarInfo.schedulerId}&date=${fn:escapeXml(calendarInfo.date)}"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
						</c:if>						
					</div>
					<div class="btn_r">
						<c:if test="${calendarInfo.modify}">
						<c:if test="${calendarInfo.repeatFlag eq 'on'}">
							<a class="btn2" href="/hybrid/calendar/modifyCalendarQuestion.do?schedulerId=${calendarInfo.schedulerId}&date=${fn:escapeXml(calendarInfo.date)}"><span><tctl:msg key="comn.modfy" bundle="common"/></span></a>
						</c:if>
						<c:if test="${calendarInfo.repeatFlag ne 'on'}">
							<a class="btn2" href="/hybrid/calendar/modifyCalendar.do?schedulerId=${calendarInfo.schedulerId}&date=${fn:escapeXml(calendarInfo.date)}"><span><tctl:msg key="comn.modfy" bundle="common"/></span></a>
						</c:if>
						</c:if>
					</div>
				</div>
				<div class="schedule_view">
					<h5>${calendarInfo.title}</h5>
					<p class="date">
						<fmt:parseDate var="sdate" pattern="yyyyMMddHHmm" value="${calendarInfo.startDate}"/>
						<fmt:parseDate var="edate" pattern="yyyyMMddHHmm" value="${calendarInfo.endDate}"/>
						
						<c:if test="${calendarInfo.allDay eq 'on'}">
							<fmt:formatDate value="${sdate}" pattern="yyyy-MM-dd EEEE"/> (<tctl:msg key="scheduler.allday" bundle="scheduler"/>)
							<c:if test="${fn:substring(calendarInfo.startDate,0,8) ne fn:substring(calendarInfo.endDate,0,8)}">
							<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd EEEE"/> (<tctl:msg key="scheduler.allday" bundle="scheduler"/>)
							</c:if>
						</c:if>
						<c:if test="${calendarInfo.allDay ne 'on'}">
						<c:choose>
						<c:when test="${fn:substring(calendarInfo.startDate,0,8) eq fn:substring(calendarInfo.endDate,0,8)}">
							<fmt:formatDate value="${sdate}" pattern="yyyy-MM-dd EEEE"/>
							
							<fmt:formatDate value="${sdate}" pattern="a hh:mm"/>
							~
							<fmt:formatDate value="${edate}" pattern="a hh:mm"/>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${sdate}" pattern="yyyy-MM-dd EEEE a hh:mm"/>
							~
							<fmt:formatDate value="${edate}" pattern="yyyy-MM-dd EEEE a hh:mm"/>
						</c:otherwise>
						</c:choose>
						</c:if>
					</p>
					<c:if test="${calendarInfo.repeatFlag eq 'on'}">
					<p class="repeat">
						[<tctl:msg key="scheduler.repeat" bundle="scheduler"/>]
						<c:set var="rpt1" value="${fn:substring(calendarInfo.repeatTerm,0,2)}"/>
						<c:choose>
							<c:when test="${rpt1 eq '01'}">
								<fmt:formatNumber var="date1" type="number" value="${fn:substring(calendarInfo.repeatTerm,4,6)}"/>
								${date1}<tctl:msg key="scheduler.day" bundle="scheduler"/><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/>
							</c:when>
							<c:when test="${rpt1 eq '02'}">
								<fmt:formatNumber var="date1" type="number" value="${fn:substring(calendarInfo.repeatTerm,2,4)}"/>
								${date1}<tctl:msg key="scheduler.week" bundle="scheduler"/><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/>
								<c:set var="weekdays" value="${fn:substring(calendarInfo.repeatTerm,4,-1)}"/>
								<c:forEach var="day" begin="0" end="${fn:length(weekdays)/2}" varStatus="loop">
									<c:set var="daycode" value="${fn:substring(weekdays,(day*2),(day*2)+2)}"/>
									<c:choose>
										<c:when test="${daycode eq '01'}">
											<tctl:msg key="scheduler.date.sunday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '02'}">
											<tctl:msg key="scheduler.date.monday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '03'}">
											<tctl:msg key="scheduler.date.tuesday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '04'}">
											<tctl:msg key="scheduler.date.wednesday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '05'}">
											<tctl:msg key="scheduler.date.thursday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '06'}">
											<tctl:msg key="scheduler.date.friday" bundle="scheduler"/>
										</c:when>
										<c:when test="${daycode eq '07'}">
											<tctl:msg key="scheduler.date.saturday" bundle="scheduler"/>
										</c:when>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:when test="${rpt1 eq '03'}">
								<fmt:formatNumber var="date1" type="number" value="${fn:substring(calendarInfo.repeatTerm,2,4)}"/>
								${date1}<tctl:msg key="scheduler.months" bundle="scheduler"/><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/>
								<c:set var="repeatLength" value="${fn:length(calendarInfo.repeatTerm)}"/>
								<c:if test="${repeatLength eq 6}">
									<fmt:formatNumber var="date2" type="number" value="${fn:substring(calendarInfo.repeatTerm,4,6)}"/>
									${date2}<tctl:msg key="scheduler.day" bundle="scheduler"/>
								</c:if>
								<c:if test="${repeatLength eq 8}">
									<c:set var="date2" value="${fn:substring(calendarInfo.repeatTerm,4,6)}"/>
									<c:set var="date3" value="${fn:substring(calendarInfo.repeatTerm,6,8)}"/>
									<c:choose>
										<c:when test="${date2 eq '01'}">
											<tctl:msg key="scheduler.date.first" bundle="scheduler"/>
										</c:when>
										<c:when test="${date2 eq '02'}">
											<tctl:msg key="scheduler.date.second" bundle="scheduler"/>
										</c:when>
										<c:when test="${date2 eq '03'}">
											<tctl:msg key="scheduler.date.third" bundle="scheduler"/>
										</c:when>
										<c:when test="${date2 eq '04'}">
											<tctl:msg key="scheduler.date.fourth" bundle="scheduler"/>
										</c:when>
										<c:when test="${date2 eq '05'}">
											<tctl:msg key="scheduler.date.fifth" bundle="scheduler"/>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${date3 eq '01'}">
											<tctl:msg key="scheduler.date.sunday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '02'}">
											<tctl:msg key="scheduler.date.monday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '03'}">
											<tctl:msg key="scheduler.date.tuesday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '04'}">
											<tctl:msg key="scheduler.date.wednesday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '05'}">
											<tctl:msg key="scheduler.date.thursday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '06'}">
											<tctl:msg key="scheduler.date.friday" bundle="scheduler"/>
										</c:when>
										<c:when test="${date3 eq '07'}">
											<tctl:msg key="scheduler.date.saturday" bundle="scheduler"/>
										</c:when>
									</c:choose>
								</c:if>
							</c:when>
							<c:when test="${rpt1 eq '04'}">
								<fmt:formatNumber var="date1" type="number" value="${fn:substring(calendarInfo.repeatTerm,2,4)}"/>
								<fmt:formatNumber var="date2" type="number" value="${fn:substring(calendarInfo.repeatTerm,4,6)}"/>
								<tctl:msg key="scheduler.every.year" bundle="scheduler"/>
								${date1}<tctl:msg key="scheduler.month" bundle="scheduler"/> 
								${date2}<tctl:msg key="scheduler.day" bundle="scheduler"/>
							</c:when>
						</c:choose>
						<c:if test="${!empty calendarInfo.repeatEndDate}">
							<fmt:parseDate var="redate" pattern="yyyyMMdd" value="${calendarInfo.repeatEndDate}"/>
							(<tctl:msg key="scheduler.repeat.enddate" bundle="scheduler"/>:<fmt:formatDate value="${redate}" pattern="yyyy-MM-dd"/>)
						</c:if>
					</p>
					</c:if>
					
					<dl class="reserv_info">
						<c:if test="${!empty calendarInfo.assetList}">
						<dt><tctl:msg key="scheduler.asset.001" bundle="scheduler"/> </dt>
						<dd class="boxL"><c:forEach var="asset" items="${calendarInfo.assetList}" varStatus="loop"><c:set var="contect" value="${asset.contect}"/>${asset.categoryName} - ${asset.assetName}<c:if test="${!loop.last}"><br/></c:if></c:forEach></dd>
						<dt class="fl"><tctl:msg key="scheduler.asset.006" bundle="scheduler"/> :</dt>
						<dd>&nbsp;${contect}</dd>
						</c:if>
						
						<c:if test="${!empty calendarInfo.share}">
						<c:set var="share" value="${calendarInfo.share}"/>
						<dt class="fl"><tctl:msg key="scheduler.asset.009" bundle="scheduler"/> :</dt>
						<dd>&nbsp;${share.userName}</dd>
						<dt><tctl:msg key="scheduler.asset.010" bundle="scheduler"/></dt>
						<dd class="boxL">
						<c:if test="${!empty share.shareTargets}"><c:forEach var="shareTarget" items="${share.shareTargets}" varStatus="loop">${shareTarget} <c:if test="${!loop.last}"> , </c:if></c:forEach></c:if>
						</dd>
						<c:if test="${!empty share.shareName}"> 
						<dt class="fl"><tctl:msg key="scheduler.share.title" bundle="scheduler"/> :</dt>
						<dd>&nbsp;${share.shareName}</dd>
						</c:if>
						</c:if>
						<dt class="fl"><tctl:msg key="scheduler.location" bundle="scheduler"/> :</dt>
						<dd>&nbsp;${calendarInfo.location}</dd>
						<dt><tctl:msg key="scheduler.search.content" bundle="scheduler"/></dt>
						<dd class="boxL">${empty calendarInfo.content ? '&nbsp;' : calendarInfo.content}</dd>
					</dl>
				</div>
			</div>

			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>