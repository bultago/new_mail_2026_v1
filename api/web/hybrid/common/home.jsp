<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
	<%@include file="/hybrid/common/header.jsp"%>
<%
Date today = new Date();
request.setAttribute("today",today);
%>	

	
	<script type="text/javascript">

	function init(){
		setLoginTime();		
	}
	function setLoginTime(){
		var loginTime = "${loginTimeInfo}";
		var loginStr;
		if(loginTime != ""){
			var loginTimeInfo = loginTime.split("|");
			loginStr = msgArgsReplace(comMsg.comn_logintime_m2,
								[loginTimeInfo[0].substring(0,4),
								loginTimeInfo[0].substring(4,6),
								loginTimeInfo[0].substring(6,8),
								loginTimeInfo[0].substring(8,10),
								loginTimeInfo[0].substring(10,12),
								loginTimeInfo[0].substring(12,14),
								loginTimeInfo[1]]);
			
		} else {
			loginStr = comMsg.comn_loginfirst;
		}
		document.getElementById("lastLoginInfo").innerHTML = loginStr;
	}
	
	function settingPage(){
		if(checkOS() == "android"){
			eval("window.TMSMobile.viewSetting()");
		}else{
			window.location = "tmsmobile://viewSetting";
		}
	}
	
	</script>
	</head>
<body onload="init()">

	<div class="wrapper">
		<div class="header">
			<h1>Terrace Mail Suite</h1>			
			<div class="hh"><h2><tctl:msg key="comn.top.today" bundle="common"/></h2><span style="position:absolute;top:7px;right:10px;cursor:pointer;" onclick="settingPage()"><img src="/design/mobile/image/sawtooth.png"/></span></span></div>
		</div>		
		<div class="container">
			<div class="my_home">
				<div class="my_info">
					<h5><%=userName%></h5>
					<p><%=userEmail%></p>
					<p id="lastLoginInfo"></p>
				</div>
				<ul class="my_list">
					<li>
						<div>
							<a href="/hybrid/mail/mailList.do?page=1&flag=U&folderName=all" class="mail">
								<span><tctl:msg key="menu.quick.unread"/></span> 
								<strong class="mail_num">${unreadMailCount}</strong>
								<strong class="menu_arrow">></strong>
							</a>
						</div>
					</li>
					<c:if test="${calendarUse eq 'on'}">
					<li>
						<div class="last">
							<a href="/hybrid/calendar/monthCalendar.do" class="schedule">
								<span><tctl:msg key="scheduler.menu.today" bundle="scheduler"/></span>
								<strong class="menu_arrow">></strong>
							</a>
						</div>
						<ul class="today_schedule_list">						
						
							<c:if test="${not empty todaySchedule}">
							<c:forEach items="${todaySchedule}" var="schedule">							
								<li style="cursor:pointer;" onclick="document.location='/hybrid/calendar/viewCalendar.do?schedulerId=${schedule.schedulerId}&date=<fmt:formatDate value="${today}" pattern="yyyyMMdd"/>'">${schedule.title}</li>
							</c:forEach>
							</c:if>
							<c:if test="${empty todaySchedule}">
							<li><tctl:msg key="scheduler.empty" bundle="scheduler"/></li>
							</c:if>
						</ul>
					</li>
					</c:if>
				</ul>
			</div>
			<%@include file="/hybrid/common/icon_bottom.jsp" %>
		</div>
		
		<%@include file="/hybrid/common/footer.jsp" %>
		
	</div>
</body>
</html>