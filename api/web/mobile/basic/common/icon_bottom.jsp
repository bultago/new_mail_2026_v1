<ul class="ic_btn">
	<c:if test="${mailUse eq 'on'}">
	<li>
		<a href="/mobile/mail/mailList.do">
			<span class="btn_ic btn_ic_mail">Icon</span>			
			<span <%if(locale.equals("jp")){%>style="font-size:12px;"<%}%>><tctl:msg key="comn.top.mail" bundle="common"/></span>
		</a>
	</li>
	</c:if>
	<c:if test="${addrUse eq 'on'}">
	<li>
		<a href="/mobile/addr/privateAddrList.do">
			<span class="btn_ic btn_ic_address">Icon</span>
			<span <%if(locale.equals("jp")){%>style="font-size:12px;"<%}%>><tctl:msg key="comn.top.addr" bundle="common"/></span>
		</a>
	</li>
	</c:if>
	<c:if test="${calendarUse eq 'on'}">
	<li>
		<a href="/mobile/calendar/monthCalendar.do">
			<span class="btn_ic btn_ic_schedule">Icon</span>
			<span <%if(locale.equals("jp")){%>style="font-size:12px;"<%}%>><tctl:msg key="comn.top.calendar" bundle="common"/></span>
		</a>
	</li>
	</c:if>
	<c:if test="${bbsUse eq 'on'}">
	<li>
		<a href="/mobile/bbs/bbsList.do">
			<span class="btn_ic btn_ic_board">Icon</span>
			<span <%if(locale.equals("jp")){%>style="font-size:12px;"<%}%>><tctl:msg key="comn.top.bbs" bundle="common"/></span>
		</a>
	</li>
	</c:if>
</ul>