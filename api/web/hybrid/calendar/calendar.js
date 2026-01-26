jQuery.fn.calendar = function(opt){
	if (!opt) opt = {};
	var drowTarget = jQuery(this);
	var today = opt.today;
	var thisday = opt.thisday;
	var selectday = opt.selectday;
	var mol = [31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	var weekTitle = [];
	weekTitle[0] = schedulerMsg.scheduler_date_0;
	weekTitle[1] = schedulerMsg.scheduler_date_1;
	weekTitle[2] = schedulerMsg.scheduler_date_2;
	weekTitle[3] = schedulerMsg.scheduler_date_3;
	weekTitle[4] = schedulerMsg.scheduler_date_4;
	weekTitle[5] = schedulerMsg.scheduler_date_5;
	weekTitle[6] = schedulerMsg.scheduler_date_6;
	
	if (selectday) {
		thisday = selectday.substring(0,6);
	}
	
	if (!thisday) {
		var date = new Date();
		var month = date.getMonth()+1;
		thisday = date.getFullYear()+""+((month < 10) ? "0"+month : month);
	}
	
	if (!today) {
		var date = new Date();
		var month = date.getMonth()+1;
		today = date.getFullYear()+""+((month < 10) ? "0"+month : month)+""+date.getDate();
	}
	
	function getLastDate (y, m) {
		if (m < 0) {
			y -= 1;
			m = 11;
		}
		var lastdate = mol[m];
		if (lastdate == 0) {
			lastdate = (((y % 100 != 0) && (y % 4 == 0)) || (y % 400 == 0)) ? 29 : 28;
		}
		return lastdate;
	}
	
	function getTitle() {
		var titleWrap = jQuery("<div class='title_box'></div>");
		var titleLbtnWrap = jQuery("<div style='text-align:center;padding-top:5px;line-height:150%;'></div>");
		var titleRbtnWrap = jQuery("<div class='btn_r'></div>");
		var preBtn = jQuery("<a class='btn_prev'> </a>").click(function() {moveMonth(getPrevDate())});
		var nextBtn = jQuery("<a class='btn_next'> </a>").click(function() {moveMonth(getNextDate())});
		var closeBtn = jQuery("<a class='btn_x'>"+comMsg.comn_close+"</a>").click(function() {closeCal()});
		titleLbtnWrap.append(preBtn);
		titleLbtnWrap.append("<span class='date'> "+getThisYear()+schedulerMsg.scheduler_year+" "+getThisMonth()+schedulerMsg.scheduler_month+" </span>");
		titleLbtnWrap.append(nextBtn);
		titleRbtnWrap.append(closeBtn);
		
		return titleWrap.append(titleLbtnWrap).append(titleRbtnWrap);
	}
	
	function getWeekTitle() {
		var wrap = jQuery("<tr></tr>");
		var day;
		for (var i=0; i<weekTitle.length; i++) {
			day = jQuery("<th></th>");
			if (i==0) day.addClass("hol");
			day.append(weekTitle[i]);
			wrap.append(day);
		}
		return wrap;
	}
	
	function getDateList(parent) {
		var date = new Date(getThisYear(), getThisMonth()-1, 1);
		var day = date.getDay();
		var dateArray = [];
		var preLastday = getLastDate(getThisYear(), (getThisMonth()-2));
		var preDate = getPrevDate();
		var nextDate = getNextDate();
		for (var i=day-1; i>=0; i--) {
			dateArray.push({index:"p",text:(preLastday-i),value:preDate+""+(preLastday-i)});
		}
		var lastday = getLastDate(getThisYear(), getThisMonth()-1);
		for (var i=1; i<=lastday; i++) {
			dateArray.push({index:"c",text:i,value:thisday+""+(i < 10 ? "0"+i : i)});
		}
		if  (dateArray.length < 42) {
			var count = 1;
			for (var i=dateArray.length; i < 42; i++) {
				dateArray.push({index:"n",text:count,value:nextDate+""+(count < 10 ? "0"+count : count)});
				count++;
			}
		}
		
		var tr;
		var td;
		for (var i=0; i < dateArray.length; i++) {
			if (i % 7 == 0) {
				if (i > 0) {
					parent.append(tr);
				}
				tr = jQuery("<tr></tr>");
			}
			td = jQuery("<td><a href='javascript:setDate(\""+dateArray[i].value+"\")'>"+dateArray[i].text+"</a></td>");
			if (dateArray[i].index == "p" || dateArray[i].index == "n") {
				if (i % 7 == 0) {
					td.addClass("hol_dim");
				} else {
					td.addClass("dim");
				}
			} else {
				if (i % 7 == 0) {
					td.addClass("hol");
				}
				if (today == dateArray[i].value) {
					td.addClass("today");
				}
				if (selectday && selectday == dateArray[i].value) {
					td.addClass("cho");
				}
			}
			tr.append(td);
			
			if (i == 41) {
				parent.append(tr);
			}
		}
	}
	
	function getThisYear() {
		return thisday.substring(0,4);
	}
	function getThisMonth() {
		return thisday.substring(4,6);
	}
	function getPrevDate() {
		var date = new Date(getThisYear(), (getThisMonth()-1));
		date.setMonth(date.getMonth()-1);
		var month = date.getMonth()+1;
		return date.getFullYear()+""+((month < 10) ? "0"+month : month);
	}
	function getNextDate() {
		var date = new Date(getThisYear(), (getThisMonth()-1));
		date.setMonth(date.getMonth()+1);
		var month = date.getMonth()+1;
		return date.getFullYear()+""+((month < 10) ? "0"+month : month);
	}
	function getDate() {
		var wrap = jQuery("<table class='month'></table>");
		wrap.append("<colgroup width='14%' span='7'></colgroup>");
		wrap.append(getWeekTitle());
		getDateList(wrap);
		return wrap;
	}
	
	function closeCal() {
		closeDatePopup();
	}
	
	function moveMonth(date) {
		thisday = date;
		drowCalendar();
	}
	function drowCalendar() {
		drowTarget.empty();
		drowTarget.addClass("pop_calendar pop_time").append(getTitle()).append(getDate());
	}
	drowCalendar();
};

function assetRangeCheck(startDate, endDate, startTime, endTime, allDay, today, current) {
	var start = new Date(startDate.substring(0,4), startDate.substring(4,6)-1, startDate.substring(6));
	var end = new Date(endDate.substring(0,4), endDate.substring(4,6)-1, endDate.substring(6));
	
	var todayDate = new Date(current.substring(0,4), current.substring(4,6)-1, current.substring(6,8));
	if (!isCheckAsset && (start.getTime() < todayDate.getTime())) {
		alert(schedulerMsg.scheduler_asset_021);
		return false;
	}

	var parseStartHour = parseInt(startTime.substring(0,2),10);
	var parseEndHour = parseInt(endTime.substring(0,2),10);
	var parseStartMin = parseInt(startTime.substring(2,4),10);
	var parseEndMin = parseInt(endTime.substring(2,4),10);
	
	if (!allDay) {
		if (!isCheckAsset && (start.getTime() == todayDate.getTime() && end.getTime() == todayDate.getTime())) {
			var currentHour = parseInt(current.substring(8,10), 10);
			var currentMin = parseInt(current.substring(10,12), 10);
			if ((parseEndHour < currentHour) || ((parseEndHour == currentHour) && (parseEndMin < currentMin))) {
				alert(schedulerMsg.scheduler_asset_024);
				return false;
			}
		}
	}
	return true;
}

function checkTimeRange(startDate, endDate, startTime, endTime, allDay) {
	var start = new Date(startDate.substring(0,4), startDate.substring(4,6)-1, startDate.substring(6));
	var end = new Date(endDate.substring(0,4), endDate.substring(4,6)-1, endDate.substring(6));
	var parseStartHour = parseInt(startTime.substring(0,2),10);
	var parseEndHour = parseInt(endTime.substring(0,2),10);
	var parseStartMin = parseInt(startTime.substring(2,4),10);
	var parseEndMin = parseInt(endTime.substring(2,4),10);

	if (start.getTime() > end.getTime()) {
		 alert(schedulerMsg.scheduler_alert_startDate_big);
		 return false;
	 }
	 else if (start.getTime() == end.getTime()) {
		 if (!allDay) {
			 if (parseStartHour > parseEndHour) {
				 alert(schedulerMsg.scheduler_alert_startTime_big);
				 return false; 
			 }
			 else if (parseStartHour == parseEndHour) {
				 if (parseStartMin > parseEndMin) {
					 alert(schedulerMsg.scheduler_alert_startTime_big);
					 return false; 
				 } else if (parseStartMin == parseEndMin) {
					 alert(schedulerMsg.scheduler_alert_startEnd_same);
					 return false; 
				 }
			 }
		 }
	 }
	return true;
}

function useShare() {
	var isUse = jQuery("#checkShare").attr("checked");
	if (isUse) {
		jQuery("#shareWrap").show();
	} else {
		jQuery("#shareWrap").hide();
		jQuery("#checkSelfTarget").attr("checked",false);
	}
	useSelfShare();
}

function useSelfShare() {
	var isUse = jQuery("#checkSelfTarget").attr("checked");
	if (isUse) {
		jQuery("#selfShareWrap").show();
	} else {
		jQuery("#selfShareWrap").hide();
		jQuery("#selfTargetList").empty();
	}
}

function useAsset() {
	var isUse = jQuery("#checkAsset").attr("checked");
	if (isUse) {
		jQuery("#assetWrap").show();
	} else {
		jQuery("#assetWrap").hide();
	}
	resetAssetTest();
}

function resetAssetTest() {
	isCheckAsset = false;
	jQuery("#assetReserveValue").val("");
	jQuery("#assetEmptyMsg").hide();
	jQuery("#assetHaveMsg").hide();
	jQuery("#assetEmptyResult").hide();
	jQuery("#assetHaveResult").hide();
	if(document.getElementById("schedulerContents")){
		jQuery("#schedulerContents").hide();
		jQuery("#checkShare").attr("checked",false);
		useShare();
	}
}

function attachSelfList(count, email) {
	var list = document.getElementById("selfTargetList");
	var li = document.createElement("li");
	li.id = "target_"+count;
	li.innerHTML = '<span class=\'share_email\'>'+email+'</span><a href=\'javascript:deleteRow('+count+')\' class="btn3"><span>'+comMsg.comn_del+'</span></a>'+
				   '<input type="hidden" id="email_'+count+'" name="selfTargetList" value="'+email+'"/>';
	list.appendChild(li);
}

var assetSeqs = [];
function searchSchedulerAsset() {
	var url="/hybrid/calendar/assetCalendar.do";
	
	var schedulerId = (document.getElementById("schedulerId"))?jQuery("#schedulerId").val():"";	
	var startDate = jQuery("#inputStartDate").val();
	var startTime = jQuery("#inputStartTime").val();
	var endDate = jQuery("#inputEndDate").val();
	var endTime = jQuery("#inputEndTime").val();
	var today = jQuery("#thisDate").val();
	var current = jQuery("#currentTime").val();
	var allDay = jQuery("#allDay").attr("checked");

	if (!checkTimeRange(startDate, endDate, startTime, endTime, allDay)) {
		return;
	}
	
	if (!assetRangeCheck(startDate, endDate, startTime, endTime, allDay, today, current)) {
		return;
	}
	
	if (allDay) {
		startTime = "0000";
		endTime = "2330";
	}

	assetSeqs = [];
	jQuery(".ast").each(function() {
		if (jQuery(this).val() != "") {
			assetSeqs.push(jQuery(this).val());
		}
	});
	if (assetSeqs.length == 0) {
		alert(schedulerMsg.scheduler_asset_014);
		return;
	}
	var param = jQuery.param({"startDate":startDate+""+startTime,"endDate":endDate+""+endTime,"schedulerId":schedulerId,"assetSeqs":assetSeqs});		
	jQuery("#reqFrame").attr("src",url + "?" + param);
}

function searchSchedulerAssetResult(data) {
	if (data && data.length > 0) {			
		var recp = schedulerMsg.scheduler_asset_005;
		var contect = schedulerMsg.scheduler_asset_006;
		var rec = schedulerMsg.scheduler_asset_001;
		var assetObj;
		jQuery("#existList").empty();
		for (var i=0; i<data.length; i++) {
			assetObj = jQuery("<dl class='reserv_info' style='margin-bottom:5px;'><dt>"+recp+" :</dt><dd>&nbsp;"+data[i].userName+"</dd><dt>"+contect+" :</dt><dd>&nbsp;"+data[i].contect+"</dd><dt>"+rec+" :</dt><dd>&nbsp;"+data[i].assetName+"</dd></dl>");
			jQuery("#existList").append(assetObj);
		}
		jQuery("#assetEmptyMsg").hide();
		jQuery("#assetEmptyResult").hide();
		jQuery("#assetHaveMsg").show();
		jQuery("#assetHaveResult").show();
		if(document.getElementById("schedulerContents")){
			jQuery("#schedulerContents").hide();
			jQuery("#checkShare").attr("checked",false);
			useShare();
		}
	} else {			
		var assetSeqStr = "";
		for (var i=0; i<assetSeqs.length; i++) {
			if (i == 0) {
				assetSeqStr += assetSeqs[i];
			} else {
				assetSeqStr += "_"+assetSeqs[i];
			}
		}
		isCheckAsset = true;
		jQuery("#assetReserveValue").val(assetSeqStr);
		jQuery("#assetHaveMsg").hide();
		jQuery("#assetHaveResult").hide();
		jQuery("#assetEmptyMsg").show();
		jQuery("#assetEmptyResult").show();
		if(document.getElementById("schedulerContents")){
			jQuery("#schedulerContents").show();
		}
	}
	
	assetSeqs = [];
}

function saveCalendar() { 
	var f = document.writeForm;
	var titleObj = jQuery("#title");
	if(!checkInputLength("jQuery", titleObj, schedulerMsg.scheduler_alert_notitle, 2, 128)) {
		return false;
	}
	if(!checkInputValidate("jQuery", titleObj, "onlyBack")) {
		return false;
	}
	var allDay = jQuery("#allDay").attr("checked");
	var startDate = jQuery("#inputStartDate").val();
	var startTime = jQuery("#inputStartTime").val();
	var endDate = jQuery("#inputEndDate").val();
	var endTime = jQuery("#inputEndTime").val();
	var today = jQuery("#thisDate").val();
	var current = jQuery("#currentTime").val();
	
	if (!checkTimeRange(startDate, endDate, startTime, endTime, allDay)) {
		return false;
	}

	var locationObj = jQuery("#location");
	var contentObj = jQuery("#content");
	if(!checkInputLength("jQuery", locationObj, "", 0, 128)) {
		return false;
	}
	if(!checkInputValidate("jQuery", locationObj, "onlyBack")) {
		return false;
	}
	if(!checkInputLength("jQuery", contentObj, "", 0, 1024)) {
		return false;
	}
	
	var contectObj = jQuery("#contect");
	if(contectObj != ""){
		if(!checkInputLength("jQuery", contectObj, "", 0, 128)) {
			return false;
		}
	}

	//scheduler share
	var sharecheck = jQuery("#checkShare").attr("checked");
	var checkSelfTarget = jQuery("#checkSelfTarget").attr("checked");
	var shareValue = "";
	var selfTargetListCount = 0;
	jQuery("input[name='selfTargetList']").each(function(i) {
		selfTargetListCount++;
	});

	if (sharecheck) {
		shareValue = jQuery("#shareValue").val();
		if (shareValue == "") {
			if (!checkSelfTarget) {
				if (jQuery("#shareGroup option").length > 1) 
					alert(schedulerMsg.scheduler_share_alert_group_noselect);
				else 
					alert(schedulerMsg.scheduler_share_alert_group_empty);
				return false;
			}
		}
		
		if (checkSelfTarget) {
			if (selfTargetListCount == 0) {
				alert(comMsg.error_login_email);
				jQuery("#targetId").focus();
				return false;
			}
		}
	}

	var checkAsset = jQuery("#checkAsset").attr("checked");
	if (checkAsset) {
		if (!isCheckAsset) {
			alert(schedulerMsg.scheduler_asset_027);
			return false;
		}
		
		if (!assetRangeCheck(startDate, endDate, startTime, endTime, allDay, today, current)) {
			return false;
		}
		var contectObj = jQuery("#contect");
		
		if(!checkInputLength("jQuery", contectObj, schedulerMsg.scheduler_asset_007, 0, 32)) {
			return;
		}
		if(!checkInputValidate("jQuery", contectObj, "onlyBack")) {
			return;
		}
	}
	
	if (allDay) { 
		jQuery("#allDay").val("on");
		startTime = "0000";
		endTime = "2330";
	}
	else {
		jQuery("#allDay").val("off");
	}

	jQuery("#startDate").val(startDate+""+startTime);
	jQuery("#endDate").val(endDate+""+endTime);
	
	f.action = "/hybrid/calendar/saveCalendar.do";
	f.submit();
}

function saveAssetCalendar() {
	var f = document.writeForm;
	var allDay = jQuery("#allDay").attr("checked");
	var startDate = jQuery("#inputStartDate").val();
	var startTime = jQuery("#inputStartTime").val();
	var endDate = jQuery("#inputEndDate").val();
	var endTime = jQuery("#inputEndTime").val();
	var today = jQuery("#thisDate").val();
	var current = jQuery("#currentTime").val();
	
	var checkAsset = jQuery("#checkAsset").attr("checked");
	if (checkAsset) {
		if (!assetRangeCheck(startDate, endDate, startTime, endTime, allDay, today, current)) {
			return;
		}

		if (!isCheckAsset) {
			alert(schedulerMsg.scheduler_asset_027);
			return;
		}
	}
}

var isCheckAsset = false;		
function addSelfTarget() {
	var f = document.writeForm;
	var mailUid = f.mailUid.value;
	var domain = f.mailDomain.value;
	
	if (mailUid == "") {
		alert(comMsg.error_login_id);
		f.mailUid.focus();
		return;
	}
	if (domain == "") {
		alert(comMsg.error_login_domain);
		f.mailDomain.focus();
		return;
	}
	if (!isId(mailUid)) {
		alert(comMsg.error_id);
		f.mailUid.select();
		return;
	}
	if (!isDomain(domain)) {
		alert(comMsg.error_domain);
		f.mailDomain.select();
		return;
	}
	
	var email = mailUid+"@"+domain;

	var list = document.getElementById("selfTargetList");
	var childNodes = list.childNodes;
	var isDup = false;
	for (var i=0; i<childNodes.length; i++) {
		var index = childNodes[i].id.split("_")[1];
		if (email == document.getElementById("email_"+index).value) {
			isDup = true;
		}
	}

	if (isDup) {
		alert(comMsg.common_form_005);
		return;
	}

	attachSelfList(childNodes.length, email);

	f.mailUid.value = "";
	f.mailDomain.value = "";
}

function deleteRow(index) {
	var list = document.getElementById("selfTargetList");
	var target = document.getElementById("target_"+index);
	list.removeChild(target);
}

function selectDate(type) {
	startEndDate = type;
	var value;
	if (type == 'start') {
		value = jQuery("#inputStartDate").val();
	} else {
		value = jQuery("#inputEndDate").val();
	}
	var height = jQuery("#bodyWrapper").height();	
	jQuery("#mainMask").css("left","0px");
	jQuery("#mainMask").css("height",height+"px");
	jQuery("#popupWrap").show();
	jQuery("#selectDate").calendar({selectday:value});
	jQuery("#selectDate").show();
	
	if(!jQuery.browser.opera){
		var posy = ((jQuery(window).height() - jQuery("#selectDate").height()) / 2)    
		+ jQuery(window).scrollTop();
		jQuery("#selectDate").css("top",posy+"px");
	}	
}

function closeDatePopup() {
	jQuery("#selectPopup").hide();
	jQuery("#popupWrap").hide();
	jQuery("#mainMask").css("left","-2000px");
	jQuery("#selectDate").hide();
}

function selectTime(type) {
	startEndType = type;
	var height = jQuery("#bodyWrapper").height();	
	jQuery("#mainMask").css("left","0px");
	jQuery("#mainMask").css("height",height+"px");
	jQuery("#popupWrap").show();
	jQuery("#selectPopup").show();
		
	var timeObj;
	if (type == 'start') {
		timeObj = jQuery("#inputStartTime");
	} else {
		timeObj = jQuery("#inputEndTime");
	}
	var time = timeObj.val();
	var hour = time.substring(0,2);
	if (hour >= 12) {
		selectAMPM("pm");
	} else {
		selectAMPM("am");
	}
	if(!jQuery.browser.opera){
		var posy = ((jQuery(window).height() - jQuery("#selectPopup").height()) / 2)    
		+ jQuery(window).scrollTop();
		jQuery("#selectPopup").css("top",posy+"px");
	}
}

var startEndType = "start";
var startEndDate = "start";

function closeTimePopup() {
	jQuery("#selectPopup").hide();
	jQuery("#popupWrap").hide();
	jQuery("#mainMask").css("left","-2000px");
	jQuery("#selectPopup").hide();
}

function selectAMPM(type) {
	if (type == 'am') {
		jQuery(".twelveHour").hide();
		jQuery(".zeroHour").css("display","block");
		jQuery("#ampm").val("am");
		jQuery("#select_time_type2").removeClass("on");
		jQuery("#select_time_type1").addClass("on");
	} else {
		jQuery(".zeroHour").hide();
		jQuery(".twelveHour").css("display","block");
		jQuery("#ampm").val("pm");
		jQuery("#select_time_type1").removeClass("on");
		jQuery("#select_time_type2").addClass("on");
	}
}

function setTime(time) {
	var ampm = jQuery("#ampm").val();
	var ampmstr = schedulerMsg.scheduler_am;
	var hour = time.substring(0,2);
	var min = time.substring(2,4);
	if (ampm == 'pm') {
		hour = parseInt(hour,10);
		if (hour < 12) hour += 12;
		ampmstr = schedulerMsg.scheduler_pm;
	}
	var timeval = hour+""+min;
	if (startEndType == "start") {
		jQuery("#inputStartTime").val(timeval);
		jQuery("#stime_link").text(ampmstr+" "+time.substring(0,2)+":"+min);
	} 
	else {
		jQuery("#inputEndTime").val(timeval);
		jQuery("#etime_link").text(ampmstr+" "+time.substring(0,2)+":"+min);
	}
	closeTimePopup();
	resetAssetTest();
}

function setDate(date) {
	if (startEndDate == "start") {
		jQuery("#inputStartDate").val(date);
		jQuery("#sdate_link").text(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
	} 
	else {
		jQuery("#inputEndDate").val(date);
		jQuery("#edate_link").text(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
	}
	closeDatePopup();
	resetAssetTest();
}

var calendarMenu = [];
calendarMenu.push({id:"calendar_menu_month",name:schedulerMsg.scheduler_tab_monthly,depth:0,link:"/hybrid/calendar/monthCalendar.do"});
calendarMenu.push({id:"calendar_menu_week",name:schedulerMsg.scheduler_tab_weekly,depth:0,link:"/hybrid/calendar/weekCalendar.do"});
calendarMenu.push({id:"calendar_menu_asset",name:schedulerMsg.scheduler_asset_001,depth:0,link:"/hybrid/calendar/writeAssetCalendar.do"});

function webGoBack(){
	
	if(pageCategory == "writeCalendar"){
		
		var titleObj = jQuery("#title");
		var locationObj = jQuery("#location");
		var contentObj = jQuery("#content");
		
		if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" ){
			if(!confirm(mailMsg.confirm_calendarEscapewrite)){
				return;
			}
		}
	}else{
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
	
	history.back();
}

function nativeGoBack(){
	
	if(pageCategory == "writeCalendar"){
		var titleObj = jQuery("#title");
		var locationObj = jQuery("#location");
		var contentObj = jQuery("#content");
		
		if(titleObj.val() != "" || locationObj.val() != "" || contentObj.val() != "" ){
			if(!confirm(mailMsg.confirm_calendarEscapewrite)){
				return;
			}
		}
	}else{
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
	eval("window.TMSMobile.goBack('true')");
}

