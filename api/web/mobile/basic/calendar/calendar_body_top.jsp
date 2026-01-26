<script type="text/javascript">
	function toggleCalendarType(){
		var calctypeBox = document.getElementById("calendarTypeBox");
		if (calctypeBox.style.display == "block") {
			calctypeBox.style.display = "none";
		} else {
			calctypeBox.style.display = "block";
		}
	}
	function goPage(type){
		if(type==1){
			location.href = "/mobile/calendar/monthCalendar.do";
		}else if(type==2){
			location.href = "/mobile/calendar/weekCalendar.do";
		}else if(type==3){
			location.href = "/mobile/calendar/writeAssetCalendar.do";
		}
	}
</script>

<div class="blind">
	<a href="#document_body" id="directLink" title="<tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/>"><tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/></a>				
</div>
			
<div class="header">
	<h1>Terrace Mail Suite</h1>
	<c:if test="${!isHybrid}">
	<div class="ts">
		<div class="btn_l"><a href="javascript:excuteAction('calendar')"><span class="ic_tit_schedule"><tctl:msg key="comn.top.calendar" bundle="common"/></span></a></div>
		<div class="btn_r"><a href="/mobile/common/home.do" class="btn0"><span><tctl:msg key="comn.top.mobilehome" bundle="common"/></span></a></div>
	</div>
	</c:if>
	<div class="hh"><h2 id="mailTopLink"></h2></div>
</div>
<div id="calendarTypeBox" class="selectMenuWrap">
	<div class="menuList" style="top:-7px;">
		<div class="menuWrap">
			<dl class="menus">
				<dd><ul style="height:100px;">
					<%--
					<li><label><a href="/mobile/calendar/monthCalendar.do"><tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/></a></label></li>
					<li><label><a href="/mobile/calendar/weekCalendar.do"><tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/></a></label></li>
					<li><label><a href="/mobile/calendar/writeAssetCalendar.do"><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></a></label></li>
					--%>
					<li onclick="goPage(1)"><label><a><tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/></a></label></li>
					<li onclick="goPage(2)"><label><a><tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/></a></label></li>
					<li onclick="goPage(3)"><label><a><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></a></label></li>
				</ul></dd>
			</dl>
			<dl><dd>
				<ul class="menuArea">
					<li style="position:relative;">
						<div style="display: block;" class="blankBox" id="blank_box">&nbsp;</div>
						<a class="btn_close x" onclick="toggleCalendarType()" href="javascript:;">X</a>
					</li>
				</ul>
			</dd></dl>				
		</div>
	</div>
</div>