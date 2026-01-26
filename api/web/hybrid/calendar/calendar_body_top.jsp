<script type="text/javascript">
	function toggleCalendarType(){
		var calctypeBox = document.getElementById("calendarTypeBox");
		if (calctypeBox.style.display == "block") {
			calctypeBox.style.display = "none";			
			jQuery("#datePart").css("visibility","");
			jQuery("#datePart a").css("float","");
		} else {
			calctypeBox.style.display = "block";
			jQuery("#datePart").css("visibility","hidden");
			jQuery("#datePart a").css("float","left");
		}
	}
	function goCalendar(type){
		
		if(pageCategory == "writeCalendar"){
			var titleObj = jQuery("#title");
			var locationObj = jQuery("#location");
			var contentObj = jQuery("#content");
			
			if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" ){
				if(!confirm(mailMsg.confirm_calendarEscapewrite)){
					return;
				}
			}
		}
		
		if(pageCategory == "writeAsset"){
			var contectObj = jQuery("#contect");
			var titleObj = jQuery("#title");
			var locationObj = jQuery("#location");
			var contentObj = jQuery("#content");
			
			if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" || contectObj.val() != ""){
				if(!confirm(mailMsg.confirm_calendarEscapewrite)){
					return;
				}
			}
		}
		
		if(type == "month"){
			document.location.href = "/hybrid/calendar/monthCalendar.do";
		}else if(type == "week"){
			document.location.href = "/hybrid/calendar/weekCalendar.do";
		}else{
			document.location.href = "/hybrid/calendar/writeAssetCalendar.do";
		}
	}
	function goPage(type){
		if(type==1){
			goCalendar('month');
		}else if(type==2){
			goCalendar('week');
		}else if(type==3){
			goCalendar('asset');
		}
	}
</script>
<style> 
   #calendarWrap{
	   	transition-duration: 1s;
		-moz-transition-duration: 1s; 
		-webkit-transition-duration: 1s;
		-o-transition-duration: 1s;  
   }
   dl.menus a{
	   	display:inline-block;
	   	width:80px;
   }
</style>
<div class="blind">
	<a href="#document_body" id="directLink" title="<tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/>"><tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/></a>				
</div>
		
<div class="header">
	<h1>Terrace Mail Suite</h1>
	<div class="ts">
		<div class="btn_l"><a href="javascript:excuteAction('calendar')"><span class="ic_tit_schedule"><tctl:msg key="comn.top.calendar" bundle="common"/></span></a></div>
		<div class="btn_r"><a href="javascript:excuteAction('home')" class="btn0"><span><tctl:msg key="comn.top.mobilehome" bundle="common"/></span></a></div>
	</div>
	<div class="hh"><h2 id="mailTopLink"></h2></div>
</div>

<div id="calendarTypeBox" class="selectMenuWrap">
	<div class="menuList" style="top:-7px;">
		<div class="menuWrap">
			<dl class="menus">
				<dd><ul style="height:100px;">
					<li onclick="goPage(1);"><label><a><tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/></a></label></li>
					<li onclick="goPage(2);"><label><a><tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/></a></label></li>
					<li onclick="goPage(3);"><label><a><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></a></label></li>
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