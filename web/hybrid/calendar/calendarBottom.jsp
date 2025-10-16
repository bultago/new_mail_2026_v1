<div id="popupWrap" class="pop_calendar_wrap">
	<div id="selectDate"></div>
	<div id="selectPopup" class="pop_time">
		<div class="menu">
			<ul>
				<li><a id="select_time_type1" href="javascript:selectAMPM('am');" class="on"><tctl:msg key="scheduler.am" bundle="scheduler"/></a></li>
				<li><a id="select_time_type2" href="javascript:selectAMPM('pm');"><tctl:msg key="scheduler.pm" bundle="scheduler"/></a></li>
			</ul>
		</div>
		<ul class="list">
			<li>
				<a class="twelveHour" style="display:none" href="javascript:setTime('1200')">12:00</a>
			<c:forEach var="hour" begin="0" end="5">
				<c:set var="drowHour" value="${(hour < 10) ? '0' : ''}${hour}"/>
				<a <c:if test="${hour == 0}">class="zeroHour"</c:if> href="javascript:setTime('${drowHour}00')">${drowHour}:00</a>
			</c:forEach>
			</li>
			<li>
				<a class="twelveHour" style="display:none" href="javascript:setTime('1230')">12:30</a>
			<c:forEach var="hour" begin="0" end="5">
				<c:set var="drowHour" value="${(hour < 10) ? '0' : ''}${hour}"/>
				<a <c:if test="${hour == 0}">class="zeroHour"</c:if> href="javascript:setTime('${drowHour}30')">${drowHour}:30</a>
			</c:forEach>
			</li>
			<li>
			<c:forEach var="hour" begin="6" end="11">
				<c:set var="drowHour" value="${(hour < 10) ? '0' : ''}${hour}"/>
				<a href="javascript:setTime('${drowHour}00')">${drowHour}:00</a>
			</c:forEach>
			</li>
			<li>
			<c:forEach var="hour" begin="6" end="11">
				<c:set var="drowHour" value="${(hour < 10) ? '0' : ''}${hour}"/>
				<a href="javascript:setTime('${drowHour}30')">${drowHour}:30</a>
			</c:forEach>
			</li>
		</ul>
		<div class="paging"><a class="btn3" href="javascript:closeTimePopup()"><span><tctl:msg key="comn.close" bundle="common"/></span></a></div>
	</div>
</div>
<div id="mainMask" class="mainMask"/>