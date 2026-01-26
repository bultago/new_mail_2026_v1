var LayoutInfo = {mode:"month"};
var SchedulerControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.param = null;
	},
	loadMonthSchedule : function(param){
		if(param){
			this.param = param;
		}
		
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.monthAction,
				param,true);//function(){PageMainLoadingManager.completeWork("mainCalendar");});
		
		LayoutInfo.mode = "month";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.monthAction,param,"month");
		
	},
	loadWeekSchedule : function(param){
		if(param){
			this.param = param;
		}
		
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.weekAction,
				param,true);
		
		LayoutInfo.mode = "week";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.weekAction,param,"week");
	},
	loadDaySchedule : function(param){
		if(param){
			this.param = param;
		}
		
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.dayAction,
				param,false);
		LayoutInfo.mode = "day";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.dayAction,param,"day");
	},
	loadProgressWeekSchedule : function(param,exeFunction){
		if(param){
			this.param = param;
		}
		
		ActionNotMaskLoader.loadAction(this.opt.subLID,
				this.opt.progressWeekScheduler,
				param,exeFunction);
	},
	loadProgressDaySchedule : function(param,exeFunction){
		if(param){
			this.param = param;
		}
		
		ActionLoader.loadAction(this.opt.subLID,
				this.opt.progressDayScheduler,
				param,true,exeFunction);
	},
	clearProgressSchedule : function(){
		ActionNotMaskLoader.loadAction(this.opt.subLID,
				this.opt.progressSchedulerInit,"");
	},
	searchSchedule : function(exeFunction){
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.searchScheduler,"",true,exeFunction);
		
		LayoutInfo.mode = "search";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.searchScheduler,{},"search");
	},
	outlookSchedule : function(){
		ActionLoader.loadAction(this.opt.mainLID,
				this.opt.outlookScheduler,"",true,"");
		
		LayoutInfo.mode = "search";
		HistoryManager.historyManagerPush(this.opt.mainLID,this.opt.searchScheduler,{},"search");
	},
	loadSchedulerCalendar : function(param) {
		if(param) {
			this.param = param;
		}
		
		ActionNotMaskLoader.loadAction(this.opt.calendarID,
				this.opt.schedulerCalendar,
				param);//function(){PageMainLoadingManager.completeWork("subCalendar");});
	}
});

var SchedulerDataControl = Class.create({
	initialize: function(){
	},
	readMonthScheduleList : function(year, month){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		
		SchedulerService.getJsonMonthScheduleList(year, month,{
			callback:function(monthScheduleList){
			_this.monthScheduleList = monthScheduleList;
			_this.checkMonthSchedule(monthScheduleList);
		}});
	},
	readDayAllInfo : function(year, month, day, type, thisDate){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		if (type == "month") {
			SchedulerService.getJsonMonthScheduleList(year, month,{
				callback:function(monthScheduleList){
				_this.dayAllScheduleRender(monthScheduleList, type, thisDate);
			}});
		} else if (type == "week") {
			if (!day) {
				day = 0;
			}
			SchedulerService.getJsonWeekScheduleList(year, month, day,{
				callback:function(weekScheduleList){
				_this.dayAllScheduleRender(weekScheduleList, type, thisDate);
			}});
		} else if (type == "day") {
			if (!day) {
				day = 0;
			}
			SchedulerService.getJsonDayScheduleList(year, month, day,{
				callback:function(dayScheduleList){
				_this.dayAllScheduleRender(dayScheduleList, type, thisDate);
			}});
		}
	},
	getMonthInfo : function(year, month){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		monthLoading = true;
		SchedulerService.getMonthInfo(year, month,{
			callback:function(monthInfo){
			monthScheduleRender(monthInfo);
		}});
	},
	getCalendarInfo : function(year, month){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		
		SchedulerService.getCalendarInfo(year, month,{
			callback:function(calendarInfo){
			setTimeout(function(){
					calendarRender(calendarInfo);
			},500);
		}});
	},
	readCalendarScheduleList : function(year, month){		
		var _this = this;
		if (!year || !month) {
			year = 0;
			month = 0;
		}
		
		SchedulerService.getJsonMonthScheduleList(year, month,{
			callback:function(calendarScheduleList){		
		_this.checkCalendarSchedule(calendarScheduleList);
		}});
	},
	readWeekScheduleList : function(year, month, day) {
		var _this = this;
		if (!year || !month || !day) {
			year = 0;
			month = 0;
			day = 0;
		}
		
		SchedulerService.getJsonWeekScheduleList(year, month, day,{
			callback:function(weekScheduleList){
			_this.weekScheduleList = weekScheduleList;
			_this.checkWeekSchedule(weekScheduleList);
		}});
	},
	getWeekInfo : function(year, month, day){		
		var _this = this;
		if (!year || !month || !day) {
			year = 0;
			month = 0;
			day = 0;
		}
		
		weekLoading = true;
		
		SchedulerService.getWeekInfo(year, month, day, {
			callback:function(weekInfo){
				weekScheduleRender(weekInfo);
		}});
	},
	getDayInfo : function(year, month, day){		
		var _this = this;
		if (!year || !month || !day) {
			year = 0;
			month = 0;
			day = 0;
		}
		
		dayLoading = true;
		
		SchedulerService.getDayInfo(year, month, day, {
			callback:function(dayInfo){
				dayScheduleRender(dayInfo);
		}});
	},
	readDayScheduleList : function(year, month, day) {
		var _this = this;
		if (!year || !month || !day) {
			year = 0;
			month = 0;
			day = 0;
		}
		
		SchedulerService.getJsonDayScheduleList(year, month, day,{
			callback:function(dayScheduleList){		
		_this.checkDaySchedule(dayScheduleList);
		}});
	},
	saveSchedule : function (scheduleData) {
		var _this = this;
		
		SchedulerService.saveSchedule(scheduleData, {
			callback:function(result) {
		_this.checkSuccess(result, "save", scheduleData.checkSendMail);
		reload();
		}});
	},
	deleteSchedule : function (schedulerId, checkSendMail) {
		var _this = this;
		
		SchedulerService.deleteSchedule(schedulerId, checkSendMail, {
			callback:function(result) {
			_this.checkSuccess(result, "delete", checkSendMail);
		}});
	},
	deleteRepeatSchedule : function (schedulerId, repeatDate) {
		var _this = this;
		
		SchedulerService.deleteRepeatSchedule(schedulerId, repeatDate, {
			callback:function(result) {
			_this.checkSuccess(result, "", "off");
		}});
	},
	modifyWriteSchedule : function (schedulerId, type) {
		var _this = this;
		
		SchedulerService.getJsonSchedule(schedulerId, {
			callback:function(scheduleData) {
		_this.setWriteSchedleData(scheduleData, type);
		}});
	},
	modifySchedule : function (scheduleData) {
		var _this = this;
		
		SchedulerService.modifySchedule(scheduleData, {
			callback:function(result) {
			_this.checkSuccess(result, "modify", scheduleData.checkSendMail);
		}});
	},
	repeatModifySchedule : function (scheduleData) {
		var _this = this;
		
		SchedulerService.repeatModifySchedule(scheduleData, {
			callback:function(result) {
		_this.checkSuccess(result, "", "off");
		}});
	},
	readSchedule : function (schedulerId, startDate, endDate) {
		var _this = this;
		
		SchedulerService.getJsonSchedule(schedulerId, {
			callback:function(scheduleData) {
		_this.setScheduleData(scheduleData, startDate, endDate);
		}});
	},
	searchSchedule : function (searchType, keyWord, currentPage) {
		var _this = this;
		
		SchedulerService.getJsonSearchScheduleList(searchType, keyWord, currentPage, {
			callback:function(searchScheduleList) {
		_this.checkSearchSchedule(searchScheduleList, keyWord);
		}});
	},
	dndUpdateSchedule : function (type, year, month, schedulerId, startDate, firstDate, lastDate) {
		var _this = this;
		
		SchedulerService.getDnDJsonSchedule(year, month, schedulerId, startDate, firstDate, lastDate, {
			callback:function(schedule) {

			_this.drowDndSchedule(type, schedule);
			_this.checkDndCalendarSchedule(year, month);
			_this.checkDndScheduleSendMailNew(schedulerId, schedule, startDate);
		}});
	},
	dndUpdateScheduleNew : function (type, year, month, schedulerId, startDate, firstDate, lastDate, fromStart) {
		var _this = this;
		
		SchedulerService.getDnDJsonSchedule(year, month, schedulerId, startDate, firstDate, lastDate, {
			callback:function(schedule) {

			_this.drowDndSchedule(type, schedule);
			_this.checkDndCalendarSchedule(year, month);
			_this.checkDndScheduleSendMailNew(schedulerId, schedule, fromStart);
		}});
	},
	drowDndSchedule : function (type, schedule) {
		var scheduleList;
		var index = 0;
		if (type == "month") {
			scheduleList = this.monthScheduleList;
		} else if (type == "week") {
			scheduleList = this.weekScheduleList;
		}
		
		for (var i=0; i<scheduleList.length; i++) {
			if (schedule[0].schedulerId == scheduleList[i].schedulerId) {
				index = i;
				break;
			}
		}
		
		scheduleList[index].startDate = schedule[0].startDate;
		scheduleList[index].endDate = schedule[0].endDate;
		scheduleList[index].drowStartDate = schedule[0].drowStartDate;
		scheduleList[index].drowEndDate = schedule[0].drowEndDate;
		scheduleList[index].planSize = schedule[0].planSize;
		scheduleList[index].timeSize = schedule[0].timeSize;
		scheduleList[index].drowplanList = schedule[0].drowplanList;
		
		
		var content = jQuery("#body3").text();
		content = content.replace("<!--","");
		content = content.replace("-->","");

		if (jQuery("#body1").css("display") == "none") {
			jQuery("#body2 *").removeAttr("id");
			jQuery("#body1").html(content);
		} else {
			jQuery("#body1 *").removeAttr("id");
			jQuery("#body2").html(content);
		}
		
		if (type == "month") {
			this.monthScheduleList = scheduleList;
			this.checkMonthSchedule(scheduleList, "dnd");
		}else if (type == "week") {
			this.weekScheduleList = scheduleList;
			this.checkWeekSchedule(scheduleList, "dnd");
		}
		
		jQuery(window).trigger("resize");
	},
	readHoliday : function (startYear, firstDate) {
		var _this = this;
		
		SchedulerService.getJsonHoliday(startYear, firstDate, {
			callback:function(holidayList) {
			_this.setHoliday(holidayList);
		}});
	},
	setHoliday : function (holidayList) {
		var classes;
		var holidaySpanSize = 0;
		for (i=0; i<holidayList.length; i++) {
			if (jQuery("#scheduler_holiday_"+holidayList[i].holidayDate).size() > 0) {
				holidaySpanSize = jQuery("#scheduler_holiday_"+holidayList[i].holidayDate+ " span").size();
				if (holidaySpanSize > 0)
					jQuery("#scheduler_holiday_"+holidayList[i].holidayDate).append(",<span id='holi_"+holidayList[i].holidayDate+"_"+holidaySpanSize+"' style='padding-left:3px' title='"+holidayList[i].holidayName+"'>"+escape_tag(holidayList[i].holidayName)+"</span>");
				else
					jQuery("#scheduler_holiday_"+holidayList[i].holidayDate).append("<span id='holi_"+holidayList[i].holidayDate+"_"+holidaySpanSize+"' style='padding-left:3px' title='"+holidayList[i].holidayName+"'>"+escape_tag(holidayList[i].holidayName)+"</span>");
				
				if (holidayList[i].isHoliday == "on") {
					if (jQuery("#date_td_"+holidayList[i].holidayDate).hasClass("TM_F_POINT4")) {
						jQuery("#date_td_"+holidayList[i].holidayDate).removeClass("TM_F_POINT4");
					}
					
					jQuery("#holi_"+holidayList[i].holidayDate+"_"+holidaySpanSize).addClass("TM_F_POINT3")
					jQuery("#date_td_"+holidayList[i].holidayDate).addClass("TM_F_POINT3");
				} else {
					jQuery("#holi_"+holidayList[i].holidayDate+"_"+holidaySpanSize).addClass("TM_F_POINT5");
				}
			}
		}
	},
	dayAllScheduleRender : function (scheduleList, type, thisDate) {
		var _this = this;
		var dateList;
		var drowWeek;
		var monthSchedule;
		var holiday;

		if (type == "month") {
			dateList = this.monthDateList;
		}
		else if (type == "week") {
			dateList = this.weekDateList;
		}
		else if (type == "day") {
			dateList = thisDate;
		}

		for(var i=0; i < scheduleList.length; i++) {
			drowWeek = scheduleList[i].drowplanList;
			holiday = scheduleList[i].holiday;
			if (holiday != "on") {
				if (drowWeek && drowWeek.length) {
					for (var m=0; m<drowWeek.length; m++) {
						data = drowWeek[m].split("|");
						size = data[0];
						startDate = data[1];
						index = 0;
						
						for(var j=0; j < dateList.length; j++) {
							if (startDate == dateList[j].date) {
								index = j;
								break;
							}
						}
						for (var k=0; k < size; k++) {
							if (dateList[index+k].date == thisDate) {
								planAllSize = jQuery("#plan_all_"+dateList[index+k].date+" .planAllBody").size();
								jQuery("#plan_all_"+dateList[index+k].date).append("<div id='plan_all_body_"+dateList[index+k].date+'_'+(planAllSize+1)+"' class='planAllBody' onclick='viewSchedule(\""+scheduleList[i].schedulerId+"\",\""+scheduleList[i].startDate+"\",\""+scheduleList[i].endDate+"\")'></div>");
								_this.makeScheduleBar("#plan_all_"+dateList[index+k].date, "#plan_all_body_"+dateList[index+k].date+'_'+(planAllSize+1), scheduleList[i],98+"%","16px","0px","0px");
							}
						}
					}
				} else {
					planAllSize = jQuery("#plan_all_"+dateList+" .planAllBody").size();
					jQuery("#plan_all_"+dateList).append("<div id='plan_all_body_"+dateList+'_'+(planAllSize+1)+"' class='planAllBody' onclick='viewSchedule(\""+scheduleList[i].schedulerId+"\",\""+scheduleList[i].startDate+"\",\""+scheduleList[i].endDate+"\")'></div>");
					_this.makeScheduleBar("#plan_all_"+dateList, "#plan_all_body_"+dateList+'_'+(planAllSize+1), scheduleList[i],98+"%","16px","0px","0px");
				}
			}
		}
		jQuery("#plan_all_"+thisDate).show();
	},
	checkSuccess : function (result, type, checkSendMail) {
		if (result.isSuccess == "fail") {
			alert(comMsg.error_msg_001);
			reload();
		} else if (result.isSuccess == "reserved") {
			alert(schedulerMsg.scheduler_asset_034);
			reload();
		} else {
			if (checkSendMail == 'on') {
				this.sendMail(type, result.schedulerId);
				reload();
			} else {
				reload();
			}
		}
	},
	sendMail : function(type, schedulerId) {
		var _this = this;
		SchedulerService.getJsonSchedule(schedulerId, {
			callback:function(scheduleData) {
			_this.setSendMailData(type, scheduleData);
		}});
	},
	checkDndScheduleSendMail : function (schedulerId, scheduleList) {
		if (scheduleList && scheduleList.length > 0) {
			var scheduleData;
			for (var i=0; i< scheduleList.length; i++) {
				if (schedulerId == scheduleList[i].schedulerId) {
					scheduleData = scheduleList[i];
					break;
				}
			}
			if (scheduleData && scheduleData.checkShare == "on") {
					this.sendMail("modify", schedulerId);
			}
		}
	},
	checkDndScheduleSendMailNew : function (schedulerId, scheduleList, fromStart) {
		if (scheduleList && scheduleList.length > 0) {
			var scheduleData;
			for (var i=0; i< scheduleList.length; i++) {
				if (schedulerId == scheduleList[i].schedulerId) {
					scheduleData = scheduleList[i];
					break;
				}
			}
			if (scheduleData && scheduleData.checkShare == "on") {
				if(scheduleData.checkSendMail == "on" && fromStart != scheduleData.startDate.substring(0,8)){
					this.sendMail("modify", schedulerId);
				}
			}
		}
	},
	sendMailProcess : function(type, schedulerId, shareSeq, subject, content) {
		SchedulerService.sendMailProcess(type, schedulerId, shareSeq, subject, content);
	},
	setMonthDateList : function(monthDateList){
		this.monthDateList = monthDateList;
	},
	setCalendarDateList : function(calendarDateList) {
		this.calendarDateList = calendarDateList;
	},
	setWeekDateList : function(weekDateList){
		this.weekDateList = weekDateList;
	},
	setTimeList : function(timeList){
		this.timeList = timeList;
	},
	setScheduleData : function(scheduleData, startDate, endDate) {
		var _this = this;
		var startDateFormat = makeDateFormat(startDate);
		var endDateFormat = makeDateFormat(endDate);
		var startTimeFormat = makeTimeFormat(startDate);
		var endTimeFormat = makeTimeFormat(endDate);
		var title = makeDateFormat(scheduleData.createTime)+" "+makeTimeFormat(scheduleData.createTime);
		var repeatMessage = "";
		var numValue;
		var repeatValue1 = "";
		var repeatValue2 = "";
		var repeatValue3 = "";
		var shareText = "";
		var shareUserName = "";
		var shareTargetName = "";
		
		if (scheduleData.checkShare == "on") {
			var mailUserSeq = jQuery("#mailUserSeq").val();
			if (scheduleData.mailUserSeq != parseInt(mailUserSeq,10)) {
				shareText = scheduleData.shareName;
				jQuery("#view_box_jqbtn a:eq(2)").show();
			} else {
				var shareValue = scheduleData.shareValue;
				var shareGroupList = shareGroupControl.shareGroupList;
	 			if (shareGroupList && shareGroupList.length > 0) {
	 				for (var i=0; i<shareGroupList.length; i++) {
	 					if (shareValue == shareGroupList[i].id) {
	 						shareText = shareGroupList[i].name;
	 						break;
	 					}
	 				}
	 			}
	 			jQuery("#view_box_jqbtn a").show();
			}
			
			if (shareText != "") {
				jQuery("#view_share_tr").show();
			}
			jQuery("#view_share_dot_tr").show();
			jQuery("#view_share_user_tr").show();
			jQuery("#view_share_target_tr").show();
			
			shareUserName = scheduleData.userName;
			var count = 0;
			if (scheduleData.shareTagetNameList && scheduleData.shareTagetNameList.length) {
				for (var i=0; i<scheduleData.shareTagetNameList.length; i++) {
					if (scheduleData.shareTagetNameList[i] == "") {
						continue;
					}
					if (count == 0) {
						shareTargetName += scheduleData.shareTagetNameList[i];
					}
					else {
						shareTargetName += "," + scheduleData.shareTagetNameList[i];
					}
					count++;
				}
			}
			jQuery("#check_send_mail").val(scheduleData.checkSendMail);
		} else {
			
			jQuery("#view_share_tr").hide();
			jQuery("#view_share_dot_tr").hide();
			jQuery("#view_share_user_tr").hide();
			jQuery("#view_share_target_tr").hide();
			
			jQuery("#view_box_jqbtn a").show();
		}
		
		if (scheduleData.allDay != "on") {
			startDateFormat = startDateFormat+" "+startTimeFormat;
			endDateFormat = endDateFormat+" "+endTimeFormat;
		}
		if (scheduleData.repeatFlag == "on") {
			repeatMessage = _this.makeRepeatMessage(scheduleData);
			jQuery("#view_repeat_tr").show();
		}
		
		var planInfo = scheduleData.assetPlanInfoList;
		if (planInfo && planInfo.length) {
			var categoryName = "";
			var assetName = "";
			var assetList = "";
			for (var i=0; i<planInfo.length; i++) {
				categoryName = escape_tag(jQuery.trim(planInfo[i].categoryName));
				assetName = escape_tag(jQuery.trim(planInfo[i].assetName));
				assetList += "["+categoryName+"-"+assetName+"] ";
			}
			jQuery("#view_scheduler_asset").append(assetList);
			jQuery("#view_scheduler_asset").attr("title",unescape_tag(assetList));
			jQuery("#view_scheduler_asset_tr").show();
			
			jQuery("#view_scheduler_asset_contect").append(escape_tag(planInfo[0].contect));
			jQuery("#view_scheduler_asset_contect").attr("title",planInfo[0].contect);
			jQuery("#view_scheduler_asset_contect_tr").show();
		}

		jQuery('#schedulerId').val(scheduleData.schedulerId);
		jQuery('#schedulerShare').val(scheduleData.checkShare);
		jQuery('#schedulerRepeat').val(scheduleData.repeatFlag);
		jQuery('#schedulerSelectDate').val(startDate);
		jQuery('#repeatSelectStartDate').val(startDate);
		jQuery('#repeatSelectEndDate').val(endDate);
		jQuery('#view_box_ph').append("<span class='sViewTitle'>"+schedulerMsg.scheduler_title_write+" : "+title+"</span>");
		jQuery('#view_date').text(startDateFormat+" ~ "+endDateFormat);
		jQuery('#view_title').text(scheduleData.title);
		jQuery('#view_title_title').attr("title", scheduleData.title);
		jQuery('#view_location').text(scheduleData.location);
		jQuery('#view_location_title').attr("title", scheduleData.location);
		jQuery('#view_content').val(scheduleData.content);
		jQuery('#view_repeat').text(repeatMessage);
		jQuery('#view_share').append(shareText);
		jQuery('#view_share_user_name').text(shareUserName);
		jQuery('#view_share_user_name').attr("title", shareUserName);
		
		jQuery('#view_share_target_title').attr("title", shareTargetName);
		jQuery('#view_share_target_name').text(shareTargetName);
	},
	setSendMailData : function(type, scheduleData) {
		var _this = this;
		var template = shareGroupControl.template;
		var startDateFormat = makeDateFormat(scheduleData.startDate);
		var endDateFormat = makeDateFormat(scheduleData.endDate);
		var startTimeFormat = makeTimeFormat(scheduleData.startDate);
		var endTimeFormat = makeTimeFormat(scheduleData.endDate);
		var title = makeDateFormat(scheduleData.createTime)+" "+makeTimeFormat(scheduleData.createTime);
		var repeatMessage = "";
		var numValue;
		var repeatValue1 = "";
		var repeatValue2 = "";
		var repeatValue3 = "";
		var shareText = "";
		var shareUserName = "";
		var shareTargetName = "";
		var subject = "";
		var subjectContent = "";
		var escapeTitle = escape_tag(scheduleData.title);
		if(type == "save") {
			subject = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_002,[scheduleData.title]);
			subjectContent = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_005,[escapeTitle]);
		}
		else if(type == "modify") {
			subject = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_003,[scheduleData.title]);
			subjectContent = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_006,[escapeTitle]);
		}
		else if(type == "delete") {
			subject = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_004,[scheduleData.title]);
			subjectContent = msgArgsReplace(schedulerMsg.scheduler_share_sendmail_007,[escapeTitle]);
		}
		
		subjectContent = "<p><span style='font-size:15px'><b>"+subjectContent+"</b></span></p>";
		
		var sb = new StringBuffer();
		sb.append(template.Header);
		sb.append("<tr>");
		sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_search_subject));
		sb.append(template.TitleTd.replace("#{TITLE}",escapeTitle));
		sb.append("</tr>");
		
		if (scheduleData.allDay != "on") {
			startDateFormat = startDateFormat+" "+startTimeFormat;
			endDateFormat = endDateFormat+" "+endTimeFormat;
		}
		sb.append("<tr>");
		sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_term));
		sb.append(template.DateTd.replace("#{DATE}",startDateFormat+" ~ "+endDateFormat));
		sb.append("</tr>");
		
		if (scheduleData.repeatFlag == "on") {
			repeatMessage = _this.makeRepeatMessage(scheduleData);
			sb.append("<tr>");
			sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_repeat_control));
			sb.append(template.RepeatTd.replace("#{REPEAT}",repeatMessage));
			sb.append("</tr>");
		}
		
		var planInfo = scheduleData.assetPlanInfoList;
		if (planInfo && planInfo.length) {
			var categoryName = "";
			var assetName = "";
			var assetList = "";
			for (var i=0; i<planInfo.length; i++) {
				categoryName = escape_tag(jQuery.trim(planInfo[i].categoryName));
				assetName = escape_tag(jQuery.trim(planInfo[i].assetName));
				assetList += "["+categoryName+"-"+assetName+"] ";
			}
			
			sb.append("<tr>");
			sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_asset_001));
			sb.append(template.AssetTd.replace("#{ASSET}",assetList));
			sb.append("</tr>");
			
			sb.append("<tr>");
			sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_asset_006));
			sb.append(template.AssetContectTd.replace("#{ASSETCONTECT}",escape_tag(planInfo[0].contect)));
			sb.append("</tr>");
		}

		var shareValue = scheduleData.shareValue;
		
		if (scheduleData.checkShare == "on") {
			var mailUserSeq = jQuery("#mailUserSeq").val();
			if (scheduleData.mailUserSeq != parseInt(mailUserSeq,10)) {
				shareText = scheduleData.shareName;
			} else {
				var shareValue = scheduleData.shareValue;
				if (shareValue == null) {
					shareValue = "0";
				} else {
					var shareGroupList = shareGroupControl.shareGroupList;
		 			if (shareGroupList && shareGroupList.length > 0) {
		 				for (var i=0; i<shareGroupList.length; i++) {
		 					if (shareValue == shareGroupList[i].id) {
		 						shareText = shareGroupList[i].name;
		 						break;
		 					}
		 				}
		 			}
				}
			}

			shareUserName = scheduleData.userName;
			sb.append("<tr>");
			sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_asset_009));
			sb.append(template.ShareUserTd.replace("#{SHAREUSER}",escape_tag(shareUserName)));
			sb.append("</tr>");

			var count = 0;
			if (scheduleData.shareTagetNameList && scheduleData.shareTagetNameList.length) {
				for (var i=0; i<scheduleData.shareTagetNameList.length; i++) {
					if (scheduleData.shareTagetNameList[i] == "") {
						continue;
					}
					if (count == 0) {
						shareTargetName += scheduleData.shareTagetNameList[i];
					}
					else {
						shareTargetName += "," + scheduleData.shareTagetNameList[i];
					}
					count++;
				}
				sb.append("<tr>");
				sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_asset_010));
				sb.append(template.ShareTargetTd.replace("#{SHARETARGET}",escape_tag(shareTargetName)));
				sb.append("</tr>");
			}
			
			sb.append("<tr>");
			sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_share_title));
			sb.append(template.ShareTd.replace("#{SHARE}",escape_tag(shareText)));
			sb.append("</tr>");
			
		}

		sb.append("<tr>");
		sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_location));
		sb.append(template.LocationTd.replace("#{LOCATION}",escape_tag(scheduleData.location)));
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append(template.CommonTh.replace("#{MESSAGE}",schedulerMsg.scheduler_search_content));
		sb.append(template.ContentTd.replace("#{CONTENT}",scheduleData.content));
		sb.append("</tr>");
		sb.append(template.Tail);

		var content = sb.toString();
		content = GetStyle()+'<div class="TerraceMsg"><div style="word-wrap: break-word;">'+subjectContent+content+'</div></div>';
		schedulerDataControl.sendMailProcess(type, scheduleData.schedulerId, shareValue, subject, content);
	},
	makeRepeatMessage : function(scheduleData) {
		var every = schedulerMsg.scheduler_repeat_every;
		var at = schedulerMsg.scheduler_repeat_at;
		var repeatType = scheduleData.repeatTerm.substring(0,2);
		
		repeatMessage = "";
		if (repeatType == "01") {
			repeatValue1 = scheduleData.repeatTerm.substring(4);
			numValue = parseInt(repeatValue1,10);
			repeatMessage += numValue+schedulerMsg.scheduler_day;
			repeatMessage += every;
		}
		else if (repeatType == "02") {
			repeatValue1 = scheduleData.repeatTerm.substring(2,4);
			numValue = parseInt(repeatValue1,10);
			repeatValue2 = scheduleData.repeatTerm.substring(4);
			repeatMessage += numValue+ schedulerMsg.scheduler_week;
			repeatMessage += every+" ";				
			
			var repeatSubValue = "";
			for (var i=0; i < repeatValue2.length/2; i++) {
				repeatSubValue = repeatValue2.substring(i*2, (i*2)+2);
				if (i > 0) {
					repeatMessage += ",";
				}
				if(repeatSubValue == "01"){
					repeatMessage += schedulerMsg.scheduler_date_sunday;
				} else if(repeatSubValue == "02"){
					repeatMessage += schedulerMsg.scheduler_date_monday;
				} else if(repeatSubValue == "03"){
					repeatMessage += schedulerMsg.scheduler_date_tuesday;
				} else if(repeatSubValue == "04"){
					repeatMessage += schedulerMsg.scheduler_date_wednesday;
				} else if(repeatSubValue == "05"){
					repeatMessage += schedulerMsg.scheduler_date_thursday;
				} else if(repeatSubValue == "06"){
					repeatMessage += schedulerMsg.scheduler_date_friday;
				} else if(repeatSubValue == "07"){
					repeatMessage += schedulerMsg.scheduler_date_saturday;
				}
			}
		}
		else if (repeatType == "03") {
			var repeatLength = scheduleData.repeatTerm.length;
			repeatValue1 = scheduleData.repeatTerm.substring(2,4);
			numValue = parseInt(repeatValue1,10);
			repeatMessage += numValue+ schedulerMsg.scheduler_months;
			repeatMessage += every+" ";
			
			if (repeatLength == 6) {
				repeatValue2 = scheduleData.repeatTerm.substring(4);
				numValue = parseInt(repeatValue2,10);
				repeatMessage += numValue+ schedulerMsg.scheduler_day;
				repeatMessage += at;
			}
			else if (repeatLength == 8){
				repeatValue2 = scheduleData.repeatTerm.substring(4,6);
				repeatValue3 = scheduleData.repeatTerm.substring(6);					
				
				if(repeatValue2 == "01"){
					repeatMessage += schedulerMsg.scheduler_date_first;
				} else if(repeatValue2 == "02"){
					repeatMessage += schedulerMsg.scheduler_date_second;
				} else if(repeatValue2 == "03"){
					repeatMessage += schedulerMsg.scheduler_date_third;
				} else if(repeatValue2 == "04"){
					repeatMessage += schedulerMsg.scheduler_date_fourth;
				} else if(repeatValue2 == "05"){
					repeatMessage += schedulerMsg.scheduler_date_fifth;
				}
				
				
				repeatMessage += " ";					
				
				if(repeatValue3 == "01"){
					repeatMessage += schedulerMsg.scheduler_date_sunday;
				} else if(repeatValue3 == "02"){
					repeatMessage += schedulerMsg.scheduler_date_monday;
				} else if(repeatValue3 == "03"){
					repeatMessage += schedulerMsg.scheduler_date_tuesday;
				} else if(repeatValue3 == "04"){
					repeatMessage += schedulerMsg.scheduler_date_wednesday;
				} else if(repeatValue3 == "05"){
					repeatMessage += schedulerMsg.scheduler_date_thursday;
				} else if(repeatValue3 == "06"){
					repeatMessage += schedulerMsg.scheduler_date_friday;
				} else if(repeatValue3 == "07"){
					repeatMessage += schedulerMsg.scheduler_date_saturday;
				}
				
				repeatMessage += at;
			}
		}
		else if (repeatType == "04") {
			repeatValue1 = scheduleData.repeatTerm.substring(2,4);
			repeatValue2 = scheduleData.repeatTerm.substring(4);
			repeatMessage += schedulerMsg.scheduler_every_year+" ";
			numValue = parseInt(repeatValue1,10);
			repeatMessage += numValue+schedulerMsg.scheduler_month;
			repeatMessage += " ";
			numValue = parseInt(repeatValue2,10);
			repeatMessage += numValue+schedulerMsg.scheduler_day;;
			repeatMessage += at;
		}
		
		if (scheduleData.repeatEndDate && trim(scheduleData.repeatEndDate).length == 8) {
			var repeatEndDateFormat = makeDateFormat(scheduleData.repeatEndDate);
			repeatMessage +=" ("+schedulerMsg.scheduler_repeat_enddate+":"+repeatEndDateFormat+")";
		}
		
		return repeatMessage;
	},
	changeObjShowHide : function(id, text) {
		if (trim(text) != "") {
			jQuery('#'+id).show();
			return true;
		} 
		else {
			jQuery('#'+id).hide();
			return false;
		}
	},
	setWriteSchedleData : function(scheduleData, modifyType) {
		var _this = this;
		jQuery('#schedulerId').val(scheduleData.schedulerId);
		jQuery('#title').val(scheduleData.title);
		
		var startDate = scheduleData.startDate;
		var endDate = scheduleData.endDate;
		
		if (modifyType == 'repeat') {
			startDate = jQuery('#repeatSelectStartDate').val();
			endDate = jQuery('#repeatSelectEndDate').val();
		}
		
		var startDateFormat = makeDateFormat(startDate);
		var endDateFormat = makeDateFormat(endDate);
		var startTimeFormat = makeTimeFormat(startDate);
		var endTimeFormat = makeTimeFormat(endDate);
		
		jQuery('#inputStartDate').val(startDateFormat);
		jQuery('#inputEndDate').val(endDateFormat);
		jQuery('#inputStartTime').val(startTimeFormat);
		jQuery('#inputEndTime').val(endTimeFormat);
		
		var shour,smin,ehour,emin;
		shour = startDate.substring(8,10);
		smin = startDate.substring(10,12);
		ehour = endDate.substring(8,10);
		emin = endDate.substring(10,12);
		jQuery("#startTime").val(shour+","+smin);
		jQuery("#endTime").val(ehour+","+emin);
		
		var sampm = (shour >= 12)?1:0;
		var eampm = (ehour >= 12)?1:0;
		shour = ((shour > 12)?shour-12:shour);
		ehour = ((ehour > 12)?ehour-12:ehour);
		
		var stimeStr = ((Number(shour)< 10)?"0"+shour:shour)+":"+smin;
		var etimeStr = ((Number(ehour)< 10)?"0"+ehour:ehour)+":"+emin;

		jQuery("#start_time_picker_ampmSelect").data("ampm",sampm);
		jQuery("#end_time_picker_ampmSelect").data("ampm",eampm);
		jQuery("#start_time_picker_timeSelect").data("date",stimeStr);
		jQuery("#end_time_picker_timeSelect").data("date",etimeStr);
		
		jQuery('#location').val(scheduleData.location);
		jQuery('#content').val(scheduleData.content);
		
		if (scheduleData.allDay == "on") {
			jQuery('#allday').attr("checked",true);
			checkAllday(true);
		}
		if (scheduleData.holiday == "on") {
			jQuery('#holiday').attr("checked",true);
		}
		if (scheduleData.repeatFlag == "on" && modifyType == 'normal') {
			jQuery('#repeat').attr("checked",true);
			var repeatType = scheduleData.repeatTerm.substring(0,2);
			if (repeatType == "01") {
				jQuery(".repeatControl :radio:eq(1)").attr("checked",true);
				var repeatValue = scheduleData.repeatTerm.substring(4);
				jQuery("#dayTermSelect").data("sval",repeatValue);
			}
			else if (repeatType == "02") {
				jQuery(".repeatControl :radio:eq(2)").attr("checked",true);
				var repeatValue1 = scheduleData.repeatTerm.substring(2,4);
				var repeatValue2 = scheduleData.repeatTerm.substring(4);
				
				var repeatSubValue = "";
				for (var i=0; i < repeatValue2.length/2; i++) {
					repeatSubValue = repeatValue2.substring(i*2, (i*2)+2);
					jQuery(".inputSelectRightWrapper :checkbox").each(function(j) {
						if(repeatSubValue == jQuery(this).val()) {
							jQuery(this).attr("checked", true);
						}
					});
				}
				if (LOCALE == 'en') {
					jQuery("#repeat_control").width("350px");
				} else {
					jQuery("#repeat_control").width("280px");
				}
				jQuery("#weekTerm1Select").data("sval",repeatValue1);
				//jQuery("#weekTerm2Select").data("sval",repeatValue2);
				jQuery("#weekSubWrapper").show();
				jQuery(".inputSelectRightWrapper").show();
			}
			else if (repeatType == "03") {
				var repeatLength = scheduleData.repeatTerm.length;
				var repeatValue1 = scheduleData.repeatTerm.substring(2,4);
				var repeatValue2 = "";
				var repeatValue3 = "";
				
				jQuery(".repeatControl :radio:eq(3)").attr("checked",true);
				
				jQuery("#monthTerm1Select").data("sval",repeatValue1);				
				if (repeatLength == 6) {
					jQuery(".repeatControl :radio:eq(4)").attr("checked",true);
					repeatValue2 = scheduleData.repeatTerm.substring(4);					
					jQuery("#monthSubTerm1Select").data("sval",repeatValue2);
				}
				else if (repeatLength == 8){
					jQuery(".repeatControl :radio:eq(5)").attr("checked",true);
					repeatValue2 = scheduleData.repeatTerm.substring(4,6);
					repeatValue3 = scheduleData.repeatTerm.substring(6);					
					jQuery("#monthSubTerm2Select").data("sval",repeatValue2);
					jQuery("#monthSubTerm3Select").data("sval",repeatValue3);					
				}
			}
			else if (repeatType == "04") {
				jQuery(".repeatControl :radio:eq(6)").attr("checked",true);
				var repeatValue1 = scheduleData.repeatTerm.substring(2,4);
				var repeatValue2 = scheduleData.repeatTerm.substring(4);				
				jQuery("#yearTerm1Select").data("sval",repeatValue1);				
				jQuery("#yearTerm2Select").data("sval",repeatValue2);				
			}
			
			if (scheduleData.repeatEndDate && trim(scheduleData.repeatEndDate).length == 8) {
				jQuery("#checkEndDate").attr("checked", true);
				var repeatEndDateFormat = makeDateFormat(scheduleData.repeatEndDate);
				jQuery("#repeatEndDate").val(repeatEndDateFormat);
			}
			repeatSchedule(true);
		}

		makeInputShareGroupList();
		
		if (scheduleData.checkShare == "on") {
			var shareValue = scheduleData.shareValue;
			var isSame = false;
			var shareGroupList = shareGroupControl.shareGroupList;
 			if (shareGroupList && shareGroupList.length > 0) {
 				for (var i=0; i<shareGroupList.length; i++) {
 					if (shareValue == shareGroupList[i].id) {
 						isSame = true;
 						break;
 					}
 				}
 			}
 			
 			if (isSame) {
 				jQuery("#shareGroupSelect").selectboxSetValue(shareValue);
 				jQuery("#checkShare").attr("checked", true);
 				checkUseShare();
 				jQuery('#schedulerShare').val(scheduleData.checkShare);
 			}
 			
 			var selfTargetList = scheduleData.shareSelfTargetList;
 			if (selfTargetList && selfTargetList.length > 0) {
 				jQuery("#checkShare").attr("checked", true);
 				checkUseShare();
 				jQuery('#schedulerShare').val(scheduleData.checkShare);
 				jQuery("#selfShareTarget").attr("checked", true);
 				checkSelfTarget();
 				jQuery("#shareTargetEmail").empty();
 				for (var i=0; i<selfTargetList.length; i++) {
 					var option = jQuery("<option></option>").attr("value",selfTargetList[i]).attr("title",selfTargetList[i]).append(selfTargetList[i]);
 					jQuery("#shareTargetEmail").append(option);
 				}
 			}
 			
 			if (scheduleData.checkSendMail == "on") {
 				jQuery("#sendMail").attr("checked", true);
 			} else {
 				jQuery("#sendMail").attr("checked", false);
 			}
		}
		
		if (scheduleData.checkAsset == "on" && modifyType == 'normal') {
			jQuery('#assetReserve').attr("checked",true);
			
			setAssetControl(scheduleData.assetPlanInfoList);
		}
		
	},
	checkSearchSchedule : function(searchScheduleList, keyWord) {
		var _this = this;
		var schedule = "";
		var currentPage = 1;
		var pageBase = 15;
		var totalCount = 0;
		var viewShareTable = false;
		var scheduleCount = 0;
		var shareScheduleCount = 0;
		jQuery(".odd").remove();
		jQuery(".even").remove();
		jQuery('#search_share_table').hide();

		for (var i=0; i<searchScheduleList.length; i++) {
			var startDateFormat = makeDateFormat(searchScheduleList[i].startDate);
			var endDateFormat = makeDateFormat(searchScheduleList[i].endDate);
			var startTimeFormat = makeTimeFormat(searchScheduleList[i].startDate);
			var endTimeFormat = makeTimeFormat(searchScheduleList[i].endDate);
			
			if (searchScheduleList[i].allDay != "on") {
				startDateFormat = startDateFormat+" "+startTimeFormat;
				endDateFormat = endDateFormat+" "+endTimeFormat;
			}
			
			var year = searchScheduleList[i].startDate.substring(0,4);
			var month = searchScheduleList[i].startDate.substring(4,6);
			var day = searchScheduleList[i].startDate.substring(6,8);
			
			if (searchScheduleList[i].type == "share") {
				
				if (shareScheduleCount % 2 == 1) 
					schedule = "<tr class='even'>";
				else
					schedule = "<tr class='odd'>";
				
				schedule +=	"<td><span style='cursor:pointer;' onclick='viewSchedule(\""+searchScheduleList[i].schedulerId+"\",\""+searchScheduleList[i].startDate+"\",\""+searchScheduleList[i].endDate+"\")'>"+escape_tag(searchScheduleList[i].title)+"</span>";
				if (searchScheduleList[i].repeatFlag == "on") {
					schedule += " <img src='/design/common/image/icon/ic_repeat.gif'/>";
				}
				schedule +=	"</td>";
				schedule +=	"<td>"+startDateFormat+" ~ "+endDateFormat+"</td>";
				schedule += "</tr>";
				
				jQuery('#search_share_table').append(schedule);
				viewShareTable = true;
				shareScheduleCount++;
			} else {
				if (scheduleCount % 2 == 1) 
					schedule = "<tr class='even'>";
				else
					schedule = "<tr class='odd'>";
				
				schedule +=	"<td><span style='cursor:pointer;' onclick='viewSchedule(\""+searchScheduleList[i].schedulerId+"\",\""+searchScheduleList[i].startDate+"\",\""+searchScheduleList[i].endDate+"\")'>"+escape_tag(searchScheduleList[i].title)+"</span>";
				if (searchScheduleList[i].repeatFlag == "on") {
					schedule += " <img src='/design/common/image/icon/ic_repeat.gif'/>";
				}
				schedule +=	"</td>";
				schedule +=	"<td>"+startDateFormat+" ~ "+endDateFormat+"</td>";
				schedule += "</tr>";
				
				jQuery('#search_table').append(schedule);
				currentPage = searchScheduleList[i].currentPage;
				pageBase = searchScheduleList[i].pageBase;
				totalCount = searchScheduleList[i].schedulerCount;
				scheduleCount++;
			}
		}
		
		if (scheduleCount == 0) {
			schedule = "<tr class='odd'>";
			schedule += "<td colspan = '2' align='center'>"+schedulerMsg.scheduler_search_result_empty+"</td>";
			schedule += "</tr>";
			
			jQuery('#search_table').append(schedule);
		}
		
		if (viewShareTable) {
			jQuery('#search_share_table').show();
		}
		
		jQuery("#currentPage").val(currentPage);
		var param = {total:totalCount,base:pageBase,page:currentPage};
		jQuery("#pageNavi").pageNavigation("p",param);
		
	},
	checkMonthSchedule : function(monthScheduleList, dnd) {
		var _this = this;
		var startDate; var drowStartDate; var holiday; 
		var planSize; var drowIndex; var drowDay;

		for (var i=0; i<monthScheduleList.length; i++) {
			startDate = monthScheduleList[i].startDate.substring(0,8);
			drowStartDate = monthScheduleList[i].drowStartDate;
			planSize = monthScheduleList[i].planSize;
			holiday = monthScheduleList[i].holiday;
			
			if (holiday == "on") {
				_this.drowHoliday(startDate, drowStartDate, monthScheduleList[i], this.monthDateList);
			}
			else {
				drowIndex = _this.checkDrowIndex("month",drowStartDate, planSize);
				_this.setDrowIndex("month", drowStartDate, planSize, drowIndex);
				_this.drowMonthSchedule(monthScheduleList[i], drowIndex, monthScheduleList[i].drowplanList);
			}
		}
		_this.drowPlanCount(4,this.monthDateList);
		
		if (dnd && dnd == "dnd") {
			showHideContent();
		}
		
		monthLoading = false;
	},
	
	checkWeekSchedule : function(weekScheduleList, dnd) {
		var _this = this;
		var startDate; var drowStartDate; var planSize;
		var drowIndex; var drowDay; var holiday;

		for (var i=0; i<weekScheduleList.length; i++) {
			startDate = weekScheduleList[i].startDate.substring(0,8);
			drowStartDate = weekScheduleList[i].drowStartDate;
			planSize = weekScheduleList[i].planSize;
			holiday = weekScheduleList[i].holiday;
			
			if (holiday == "on") {
				_this.drowHoliday(startDate, drowStartDate, weekScheduleList[i], this.weekDateList);
			}
			else {
				drowIndex = _this.checkDrowIndex("week",drowStartDate, planSize);
				if (drowIndex >= (jQuery(".numTD").length -1)) {
					jQuery(".numTD:last").parent().after(jQuery("<tr></tr>").append(replaceAll(jQuery(".numTD:last").parent().html(),"_"+drowIndex,"_"+(drowIndex+1))));
					jQuery(".numTD:last").text((Number(jQuery(".numTD:last").text())+1));
				}
				_this.setDrowIndex("week", drowStartDate, planSize, drowIndex);
				_this.drowWeekSchedule(weekScheduleList[i], drowIndex, weekScheduleList[i].drowplanList);
			}
		}
		//_this.drowPlanCount(5, this.weekDateList);
		
		_this.checkProgressSchedule(weekScheduleList);
		
		if (dnd && dnd == "dnd") {
			showHideContent();
		}

		weekLoading = false;
	},
	
	checkDaySchedule : function(dayScheduleList) {
		var _this = this;
		var schedulerId; var startDate; var drowStartDate; var title;
		var planSize; var holiday; var drowIndex; var drowDay;
		var dateList;
		
		for (var i=0; i<dayScheduleList.length; i++) {
			
			startDate = dayScheduleList[i].startDate.substring(0,8);
			drowStartDate = dayScheduleList[i].drowStartDate;
			holiday = dayScheduleList[i].holiday;

			if (holiday == "on") {
				_this.drowHoliday(startDate, drowStartDate, dayScheduleList[i]);
			}
			else {
				drowIndex = _this.checkDayDrowIndex(drowStartDate);
				if (drowIndex >= 5) {
					jQuery(".numTD:last").parent().after(jQuery("<tr></tr>").append(replaceAll(jQuery(".numTD:last").parent().html(),"_"+drowIndex,"_"+(drowIndex+1))));
					jQuery(".numTD:last").text((Number(jQuery(".numTD:last").text())+1));
				}
				_this.setDayDrowIndex(drowStartDate, drowIndex);
				_this.drowDaySchedule(dayScheduleList[i], drowIndex, drowStartDate);
			}
		}
		dateList = jQuery("#dateList").val();
		
		//_this.drowPlanCount(5, dateList);

		_this.checkProgressSchedule(dayScheduleList);

		jQuery(".plan").css("cursor", "");
		
		dayLoading = false;
	},
	checkCalendarSchedule : function(calendarScheduleList) {
		var _this = this;
		var drowStartDate;
		var data; var size; var startDate; var index; var count;
		var dateCount = new Array(this.calendarDateList.length);

		for(var i=0; i < this.calendarDateList.length; i++) {
			dateCount[i] = 0;
		}
		
		for (var i=0; i<calendarScheduleList.length; i++) {
			for (var j=0; j < calendarScheduleList[i].drowplanList.length; j++) {
				data = calendarScheduleList[i].drowplanList[j].split("|");
				size = data[0];
				startDate = data[1]; 

				for(var k=0; k < this.calendarDateList.length; k++) {
					if (startDate == this.calendarDateList[k].date) {
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
			jQuery("#calendar_date_"+this.calendarDateList[i].date).attr("count",count);
			if (count > 0) {
				jQuery("#calendar_date_"+this.calendarDateList[i].date+" a").addClass("ok");
			}
		}
	},
	checkDndCalendarSchedule : function(year, month) {
		gotoCalendar(year, month);
	},
	drowMonthSchedule : function (monthSchedule, drowIndex, drowWeek) {
		var _this = this;
		var size = 0;
		var extSize = 0;
		var data;
		var startDate;
		
		for(var i=0; i < drowWeek.length; i++) {
			data = drowWeek[i].split("|");
			size = data[0];
			startDate = data[1];
			
			size = parseInt(size, 10);
			
			switch (size) {
			case 7:
				extSize = (isMsie) ? ((isMsie7) ? -4 : -3):3;
				break;
			case 6:
				extSize = (isMsie) ? ((isMsie7) ? -5 : -3):2;
				break;
			case 5:
				extSize = (isMsie) ? ((isMsie7) ? -1 : -2):2;
				break;
			case 4:
				extSize = (isMsie) ? -1:1;
				break;
			case 3:
				extSize = (isMsie) ? -1:1;
				break;
			case 2:
				extSize = (isMsie) ? -1:0;
				break;
			case 1:
				extSize = -1;
				break;
			default:
				break;
			}
			_this.makeScheduleBar("#schedule_month_"+startDate, "#plan_"+startDate+"_"+drowIndex, monthSchedule,((100*size)+extSize)+"%","16px",20*(drowIndex-1)+"px","0px");
			_this.makeClickEvent("#schedule_month_"+startDate+" "+"#plan_"+startDate+"_"+drowIndex, "viewSchedule('"+monthSchedule.schedulerId+"','"+monthSchedule.startDate+"','"+monthSchedule.endDate+"')");
		}
	},
	
	drowWeekSchedule : function (weekSchedule, drowIndex, drowDay) {
		var _this = this;
		var count = 0;
		var data;
		var size = 0;
		var extSize = 0;
		var startDate;
		
		for(var i=0; i < drowDay.length; i++) {
			data = drowDay[i].split("|");
			size = data[0];
			startDate = data[1];
			extSize = (size >= 4)?2:((size >= 2)?1:0);
			
			_this.makeScheduleBar("", "#plan_"+startDate+"_"+drowIndex, weekSchedule,((100*size)+extSize)+"%","16px","1px","0px");
			_this.makeClickEvent("#plan_"+startDate+"_"+drowIndex, "viewSchedule('"+weekSchedule.schedulerId+"','"+weekSchedule.startDate+"','"+weekSchedule.endDate+"')");
		}
	},
	
	drowDaySchedule : function (daySchedule, drowIndex, drowStartDate) {
		var _this = this;
		var size = 1;		
		_this.makeScheduleBar("", "#plan_"+drowStartDate+"_"+drowIndex, daySchedule,99*size+"%","16px","1px","0px");
		_this.makeClickEvent("#schedule_day_"+drowStartDate+" "+"#plan_"+drowStartDate+"_"+drowIndex, "viewSchedule('"+daySchedule.schedulerId+"','"+daySchedule.startDate+"','"+daySchedule.endDate+"')");
	},
	
	drowPlanCount : function (maxCount, dateList) {
		var count = 0;
		if (dateList instanceof Array) {
			for (var i=0; i<dateList.length; i++) {
				count = jQuery("#plan_td_"+dateList[i].date+" #plan_count_"+dateList[i].date).attr("count");
				count = parseInt(count,10);
				if (count > maxCount) {
					jQuery("#plan_td_"+dateList[i].date+ " #plan_count_"+dateList[i].date).text("+"+(count-maxCount));
				}
			}
		} else {
			count = jQuery("#plan_td_"+dateList+" #plan_count_"+dateList).attr("count");
			count = parseInt(count,10);
			if (count > maxCount) {
				jQuery("#plan_td_"+dateList+ " #plan_count_"+dateList).text("+"+(count-maxCount));
			}
		}
	},
	
	drowHoliday : function (startDate, drowStartDate, scheduleList, dateList) {
		var startIndex = 0;
		var schedulerId = scheduleList.schedulerId;
		var holidayName = scheduleList.title;
		var holidaySize = scheduleList.planSize;

		if (startDate = drowStartDate) {
			if (jQuery("#scheduler_holiday_"+startDate+ " span").size() > 0)
				jQuery("#scheduler_holiday_"+startDate).append(",<span style='padding-left:3px;cursor:pointer' onclick='viewSchedule(\""+schedulerId+"\",\""+scheduleList.startDate+"\",\""+scheduleList.endDate+"\")' type='user'>"+escape_tag(holidayName)+"</span>");
			else
				jQuery("#scheduler_holiday_"+startDate).append("<span style='padding-left:3px;cursor:pointer' onclick='viewSchedule(\""+schedulerId+"\",\""+scheduleList.startDate+"\",\""+scheduleList.endDate+"\")' type='user'>"+escape_tag(holidayName)+"</span>");
		}
		
		if (dateList && dateList.length) {
			for(var i=0; i<dateList.length; i++) {
				if (drowStartDate == dateList[i].date) {
					startIndex = i;
					break;
				}
			}
			
			for (var j=startIndex; j<startIndex+holidaySize; j++) {
				if (jQuery("#date_td_"+dateList[j].date).hasClass("TM_F_POINT4"))
					jQuery("#date_td_"+dateList[j].date).removeClass("TM_F_POINT4");
				
				jQuery("#date_td_"+dateList[i].date).addClass("TM_F_POINT3");
			}
		} 
		else {
			if (jQuery("#date_td_"+drowStartDate).hasClass("TM_F_POINT4"))
				jQuery("#date_td_"+drowStartDate).removeClass("TM_F_POINT4");
			
			jQuery("#date_td_"+drowStartDate).addClass("TM_F_POINT3");
		}
	},
	
	drowMonthPlanAll : function (monthInfo, thisDate) {
		var _this = this;
		var size = 0;
		var index = 0;
		var planAllSize = 0;
		var data;
		var startDate;
		
		for(var i=0; i < monthInfo.drowplanList.length; i++) {
			data = drowWeek[i].split("|");
			size = data[0];
			startDate = data[1];
			index = 0;
			
			for(var j=0; j < this.monthDateList.length; j++) {
				if (startDate == this.monthDateList[j].date) {
					index = j;
					break;
				}
			}
			for (var k=0; k < size; k++) {
				
				planAllSize = jQuery("#plan_all_"+this.monthDateList[index+k].date+" .planAllBody").size();
				jQuery("#plan_all_"+this.monthDateList[index+k].date).append("<div id='plan_all_body_"+(planAllSize+1)+"' class='planAllBody' onclick='viewSchedule(\""+monthSchedule.schedulerId+"\",\""+monthSchedule.startDate+"\",\""+monthSchedule.endDate+"\")'></div>");
				_this.makeScheduleBar("#plan_all_"+this.monthDateList[index+k].date, "#plan_all_body_"+(planAllSize+1), monthSchedule,100+"%","16px","0px","0px");
			}
		}
	},
	
	drowWeekPlanAll : function (weekSchedule, drowIndex, drowDay) {
		var _this = this;
		var data = drowDay.split("|");
		var size = data[0];
		var startDate = data[1];
		var planAllSize = 0;
			
		for(var j=0; j < this.weekDateList.length; j++) {
			if (startDate == this.weekDateList[j].date) {
				index = j;
				break;
			}
		}

		for (var k=0; k < size; k++) {
			planAllSize = jQuery("#plan_all_"+this.weekDateList[index+k].date+" .planAllBody").size();
			jQuery("#plan_all_"+this.weekDateList[index+k].date).append("<div id='plan_all_body_"+(planAllSize+1)+"' class='planAllBody' onclick='viewSchedule(\""+weekSchedule.schedulerId+"\",\""+weekSchedule.startDate+"\",\""+weekSchedule.endDate+"\")'></div>");
			_this.makeScheduleBar("#plan_all_"+this.weekDateList[index+k].date, "#plan_all_body_"+(planAllSize+1), weekSchedule,100+"%","16px","0px","0px");
		}
	},
	
	drowDayPlanAll : function (daySchedule, drowIndex, drowStartDate) {
		var _this = this;
		var planAllSize = jQuery("#plan_all_"+drowStartDate+" .planAllBody").size();
			jQuery("#plan_all_"+drowStartDate).append("<div id='plan_all_body_"+(planAllSize+1)+"' class='planAllBody' onclick='viewSchedule(\""+daySchedule.schedulerId+"\",\""+daySchedule.startDate+"\",\""+daySchedule.endDate+"\")'></div>");
			_this.makeScheduleBar("#plan_all_"+drowStartDate, "#plan_all_body_"+(planAllSize+1), daySchedule,100+"%","16px","0px","0px");
	},
	
	checkWeekDrowList : function(drowStartDate, planSize) {
		var index = 0;
		var first = 0;
		var start = 0;
		var dayDrow;
		for (var i=0; i<this.weekDateList.length; i++) {
			if (drowStartDate == this.weekDateList[i].date) {
				index = i;
				break;
			}
		}
		
		if (index > 0) {
			first = 7 - index;
		}

		if (index+planSize > 7) {
			dayDrow = first+"|"+this.weekDateList[index].date;
		} 
		else {
			dayDrow = planSize+"|"+this.weekDateList[index].date;
		}
		
		return dayDrow;
	},
	
	checkMonthDrowList : function(type, drowStartDate, planSize) {
		var start = 0;
		var count = 0;
		var weekTerm = 0;
		var overCheck = false;
		var weekDrow = new Array();
		var first = 0;
		var drowFirst = 0;
		var thisIndex = 0;
		var dateList;
		
		if (type == "month") {
			dateList = this.monthDateList;
		}
		else if (type == "calendar") {
			dateList = this.calendarDateList;
		}
		
		for (var i=0; i<dateList.length; i++) {
			if (drowStartDate == dateList[i].date) {
				count = i;
				break;
			}
		}

		thisIndex = (count % 7);

		if (thisIndex > 0) {
			first = 7-thisIndex;
		} 
		else {
			first = 7;
		}

		if ((thisIndex+planSize) >= 7) {
			weekDrow[start++] = first+"|"+dateList[count].date;
			planSize = planSize - first;
			overCheck = true;
		}
		
		while(planSize >= 7) {
			planSize = planSize - 7;
			weekDrow[start++] = 7+"|"+dateList[count+first+weekTerm].date;
			weekTerm += 7;
		}

		if (planSize > 0) {
			if (overCheck) {
				if (weekTerm > 0) {
					weekDrow[start++] = planSize+"|"+dateList[count+first+weekTerm].date;
				}
				else {
					weekDrow[start++] = planSize+"|"+dateList[count+first].date;
				}
			}
			else {
				weekDrow[start++] = planSize+"|"+dateList[count].date;
			}
		}

		return weekDrow;
	},
	
	checkDayDrowIndex : function(date) {
		var status;
		var drowIndex = -1;
		var size = jQuery("div[ids="+date+"]").size();

		for (var i=1; i<=size; i++) {
			status = jQuery("#plan_"+date+"_"+i).attr("status");

			if (status == "n") {
				drowIndex = i;
				break;
			}
		}
		
		return drowIndex;
		
	},
	
	checkDrowIndex : function(type, drowStartDate, planSize) {
		var flag = true;
		var index = 0;
		var size = 0;
		var maxSize = 0;
		var drowIndex = -1;
		var dateList;
		var status;
		if (type == "month") {
			dateList = this.monthDateList;
			maxSize = this.monthDateList.length;
		}
		else if (type == "week") {
			dateList = this.weekDateList;
			maxSize = this.weekDateList.length;
		}
		
		for (i=0; i<dateList.length; i++) {
			if (drowStartDate == dateList[i].date) {
				index = i;
				break;
			}
		}
		
		size = jQuery("div[ids="+drowStartDate+"]").size();

		if (index+planSize <= maxSize) {
			maxSize = index+planSize;
		}
		
		for (i=1; i<=size; i++) {
			flag = true;
			for (j=index; j<maxSize; j++) {
				status = jQuery("#plan_"+dateList[j].date+"_"+i).attr("status");

				if (status != "n") {
					flag = false;
				}
			}
			if (flag) {
				drowIndex = i;
				break;
			}
		}
		
		return drowIndex;
	},
	
	resetDrowIndex : function(type, drowIndex, drowStartDate, planSize, schedulerId) {
		var _this = this;
		var index = 0;
		var dateList;
		var drowDay;
		var data;
		var size = 0;
		var startDate;
		
		if (type == "week") {
			dateList = _this.weekDateList;
			drowDay = _this.checkWeekDrowList(drowStartDate, planSize);
			
			data = drowDay.split("|");
			size = data[0];
			startDate = data[1];
			
			for (var i=0; i<dateList.length; i++) {
				if (startDate == dateList[i].date) {
					index = i;
					break;
				}
			}
			
			index = parseInt(index, 10);
			size = parseInt(size, 10);
			
			for (var j=index; j<index+size; j++) {	
				_this.resetCalendarIndex(dateList[j].date);
			}	
		}
		else {
			dateList = _this.monthDateList;
			drowDay = _this.checkMonthDrowList("month", drowStartDate, planSize);
			
			for(var i=0; i < drowDay.length; i++) {
				data = drowDay[i].split("|");
				size = data[0];
				startDate = data[1];
				
				for (var k=0; k<dateList.length; k++) {
					if (startDate == dateList[k].date) {
						index = k;
						break;
					}
				}

				index = parseInt(index, 10);
				size = parseInt(size, 10);
				
				for (var j=index; j<index+size; j++) {
					_this.resetCalendarIndex(dateList[j].date);
				}	
			}
		}
	},
	
	resetCalendarIndex : function (date) {
		var count;
		count = jQuery("#calendar_date_"+date).attr("count");
		count = parseInt(count,10);
		count = count-1;
		if (count <= 0) {
			jQuery("#calendar_date_"+date).attr("count","0");
			jQuery("#calendar_date_"+date+" a").removeClass("ok");
		}
		else {
			jQuery("#calendar_date_"+date).attr("count",count);
		}
	},
	
	setDayDrowIndex : function(drowStartDate, drowIndex) {
		var index = 0;
		var count = 0;
		
		count = jQuery("#plan_count_"+drowStartDate).attr("count");
		jQuery("#plan_count_"+drowStartDate).attr("count", (parseInt(count,10)+1));
		
		if (drowIndex != -1) {
			jQuery("#plan_"+drowStartDate+"_"+drowIndex).attr("status","u");
		}
	},
	
	setDrowIndex : function(type, drowStartDate, planSize, drowIndex) {
		var index = 0;
		var count = 0;
		var dateList;
		
		if (type == "month") {
			dateList = this.monthDateList;
		}
		else if (type == "week") {
			dateList = this.weekDateList;
		}
		
		for (i=0; i<dateList.length; i++) {
			if (drowStartDate == dateList[i].date) {
				break;
			}
			index++;
		}

		for (j=index; j<index+planSize; j++) {
			count = jQuery("#plan_count_"+dateList[j].date).attr("count");

			jQuery("#plan_count_"+dateList[j].date).attr("count", (parseInt(count,10)+1));
			
			if (drowIndex != -1) {
				jQuery("#plan_"+dateList[j].date+"_"+drowIndex).attr("status","u");
			}
		}
	},
	
	checkProgressSchedule : function(scheduleList) {
		var _this = this;
		var startDate; var endDate; var repeatFlag; var holiday;
		var drowStartDate; var drowEndDate; var allDay;

		_this.setTimeScheduleIndex(scheduleList);

		for (var i=0; i<scheduleList.length; i++) {
			startDate = scheduleList[i].startDate.substring(0,8);
			endDate = scheduleList[i].endDate.substring(0,8);
			drowStartDate = scheduleList[i].drowStartDate;
			drowEndDate = scheduleList[i].drowEndDate;
			allDay = scheduleList[i].allDay;
			repeatFlag = scheduleList[i].repeatFlag; 
			holiday = scheduleList[i].holiday;
			if ((allDay != "on") && (holiday != "on") && (repeatFlag != "on") && ((startDate == drowStartDate) && (endDate == drowEndDate) && (startDate == endDate))) {
				_this.drowTimeSchedule(scheduleList[i]);
			}
		}

	},
	
	setTimeScheduleIndex : function(scheduleList) {
		var startDate; var endDate; var timeSize; var holiday;
		var drowStartDate; var drowEndDate; var drowStartTime; var allDay;
		
		for (var i=0; i<scheduleList.length; i++) {
			startDate = scheduleList[i].startDate.substring(0,8);
			endDate = scheduleList[i].endDate.substring(0,8);
			drowStartDate = scheduleList[i].drowStartDate;
			drowEndDate = scheduleList[i].drowEndDate;
			drowStartTime = scheduleList[i].drowStartTime;
			timeSize = scheduleList[i].timeSize;
			allDay = scheduleList[i].allDay;
			holiday = scheduleList[i].holiday;
			
			if ((allDay != "on") && (holiday != "on") && ((startDate == drowStartDate) && (endDate == drowEndDate) && (startDate == endDate))) {
				for(var j=0; j<this.timeList.length; j++) {
					if (drowStartTime == this.timeList[j]) {
						index = j;
						break;
					}
				}
				
				for (var k=index; k<(index+timeSize); k++) {
					timePlanCount = jQuery("#progress_"+drowStartDate+"_"+this.timeList[k]).attr("count");
					timePlanCount = parseInt(timePlanCount,10);
					jQuery("#progress_"+drowStartDate+"_"+this.timeList[k]).attr("count",timePlanCount+1);
				}
			}
		}
	},
	
	drowTimeSchedule : function (schedule) {
		var _this = this;
		var drowStartDate; var drowStartTime; 
		var timeSize;var planSize; var planWidth;
		var index = 0;
		var timePlanCount = 0;
		var timePlanIndex = 0;
		var maxCount = 0;
		var maxIndex = 0;
		var total;
		var timeList = this.timeList;
		drowStartDate = schedule.drowStartDate;
		drowStartTime = schedule.drowStartTime;
		
		timeSize = schedule.timeSize;
		if (drowStartTime == 0) {
			drowStartTime = "000";
		} else if (drowStartTime == 30) {
			drowStartTime = "030";
		}
		
		for(var j=0; j<timeList.length; j++) {			
			if (drowStartTime == timeList[j]) {
				index = j;
				break;
			}
		}
		
		for (var k=index; k<index+timeSize; k++) {
			timePlanCount = jQuery("#progress_"+drowStartDate+"_"+timeList[k]).attr("count");
			timePlanIndex = jQuery("#progress_"+drowStartDate+"_"+timeList[k]).attr("index");
			timePlanCount = parseInt(timePlanCount,10);
			timePlanIndex = parseInt(timePlanIndex,10);
			
			if (maxCount < timePlanCount)
				maxCount = timePlanCount;
			
			if (maxIndex < timePlanIndex)
				maxIndex = timePlanIndex;
			
			jQuery("#progress_"+drowStartDate+"_"+timeList[k]).attr("index",timePlanIndex+1);
		}
		
		planWidth = (100 / maxCount);
		
		var drowIndex = 0;
		var dupCheck = false;
		var drowIndex = 0;
		total = jQuery("#progress_"+drowStartDate+"_"+timeList[index]).attr("total");
		var totalArray = total.split(',');
		while(drowIndex <= maxCount) {
			dupCheck = false; 
			for (var i=0; i <totalArray.length; i++) {
				if (totalArray[i] != "" && drowIndex == totalArray[i]) {
					dupCheck = true;
					continue;
				}
			}
			if (dupCheck) {
				drowIndex++;
			} else {
				break;
			}
		}
		
		for (var s=index; s<index+timeSize; s++) {
			total = jQuery("#progress_"+drowStartDate+"_"+timeList[s]).attr("total");
			jQuery("#progress_"+drowStartDate+"_"+this.timeList[s]).attr("total",total+drowIndex+",");
			
			pwidth = jQuery("#progress_"+drowStartDate+"_"+timeList[s]).attr("pwidth");
			jQuery("#progress_"+drowStartDate+"_"+this.timeList[s]).attr("pwidth",pwidth+(planWidth-2)+",");
		}
		
		total = jQuery("#progress_"+drowStartDate+"_"+drowStartTime).attr("total");
		var totalArray = total.split(',');
		timePlanIndex = jQuery("#progress_"+drowStartDate+"_"+drowStartTime).attr("index");
		
		jQuery("#progress_"+drowStartDate+"_"+drowStartTime).append("<div id='plan_"+drowStartDate+"_"+drowStartTime+"_"+(timePlanIndex)+"' class='timeplan' onclick='viewSchedule(\""+schedule.schedulerId+"\",\""+schedule.startDate+"\",\""+schedule.endDate+"\")'></div>");
		_this.makeTimeScheduleBar("#progress_"+drowStartDate+"_"+drowStartTime, "#plan_"+drowStartDate+"_"+drowStartTime+"_"+(timePlanIndex), schedule ,(planWidth-2)+"%",((30*timeSize)+timeSize-1)+"px","0px",((totalArray[timePlanIndex-1])*planWidth)+"%");
		
		
	},
	makeScheduleBar : function (parentId, currentId, schedule, width, height, top, left) {
		var scheduleBar = {
			scheduleHeader:"<div class='planTop'><div class='b2'/></div>",
			scheduleBody:"<table class='schTable' cellpadding='0' cellspacing='0'><tr><td><div class='schDiv'>&nbsp;"+escape_tag(schedule.title)+"</div></td></tr></table>",
			scheduleTail:"<div class='planBottom'><div class='b2'/></div>"
		};
		
		var mailUserSeq = jQuery("#mailUserSeq").val();
		if (schedule.shareColor) {
			if (schedule.mailUserSeq != parseInt(mailUserSeq,10)) {
				jQuery(parentId+" "+currentId).addClass('share');
				jQuery(parentId+" "+currentId).attr("share", "on");
			} else {
				shareValue = "share";
			}
		}
		
		var stringBuffer = new StringBuffer();
		stringBuffer.append(scheduleBar.scheduleHeader);		
		stringBuffer.append(scheduleBar.scheduleBody);
		stringBuffer.append(scheduleBar.scheduleTail);
		var barHtml = stringBuffer.toString();
		stringBuffer.destroy();
		
		jQuery(parentId+" "+currentId).addClass('planLine');
		jQuery(parentId+" "+currentId).attr("title", schedule.title);
		jQuery(parentId+" "+currentId).attr("key",schedule.schedulerId);
		jQuery(parentId+" "+currentId).attr("size",schedule.planSize);
		jQuery(parentId+" "+currentId).attr("repeat",schedule.repeatFlag);
		jQuery(parentId+" "+currentId).attr("asset",schedule.checkAsset);
		jQuery(parentId+" "+currentId).css({"width":width,"height":height,"top":top,"left":left,cursor:"move"});		
		jQuery(parentId+" "+currentId).html(barHtml);
	},
	makeTimeScheduleBar : function (parentId, currentId, schedule, width, height, top, left) {
		
		if (schedule.drowStartTime == schedule.drowEndTime) {
			return;
		}
		
		var scheduleBar = {
			scheduleHeader:"<div class='planTop'><div class='b2'/></div>",
			scheduleBody:"<table class='schTable' cellpadding='0' cellspacing='0'><tr><td><div class='schDiv'>&nbsp;"+escape_tag(schedule.title)+"</div></td></tr></table>",
			scheduleTail:"<div class='planBottom'><div class='b2'/></div>"
		};
		
		var stringBuffer = new StringBuffer();
		stringBuffer.append(scheduleBar.scheduleHeader);
		stringBuffer.append(scheduleBar.scheduleBody);
		stringBuffer.append(scheduleBar.scheduleTail);
		var barHtml = stringBuffer.toString();
		stringBuffer.destroy();

		var mailUserSeq = jQuery("#mailUserSeq").val();
		if (schedule.shareColor) {
			if (schedule.mailUserSeq != parseInt(mailUserSeq,10)) {
				jQuery(parentId+" "+currentId).css('background',schedule.shareColor);
				jQuery(parentId+" "+currentId).css('color','#ffffff');
				jQuery(parentId+" "+currentId).attr("share", "on");
			}
		}
		jQuery(parentId+" "+currentId).parent().css("position","relative");
		jQuery(parentId+" "+currentId).addClass('planLine');
		jQuery(parentId+" "+currentId).attr("title", schedule.title);
		jQuery(parentId+" "+currentId).attr("key",schedule.schedulerId);
		jQuery(parentId+" "+currentId).attr("date",schedule.drowStartDate);
		jQuery(parentId+" "+currentId).attr("size",schedule.timeSize);
		jQuery(parentId+" "+currentId).css({"width":width,"height":height,"top":top,"left":left});		
		jQuery(parentId+" "+currentId).html(barHtml);
	},
	
	makeClickEvent : function (target, func) {
		jQuery(target).click(function () { 
		      eval(func);
		});
	}
});


var mainSplitter, contentSplitter, mainTreeMap;
var leftMenuControl, schedulerControl, schedulerDataControl, dndManager, shareGroupControl, scheduerAssetControl;
var monthLoading = false;
var weekLoading = false;
var dayLoading = false;

jQuery().ready(function(){
	setTopMenu('scheduler');
//	PageMainLoadingManager.initLoadView();
//	
//	var jobStack = [];
//	jobStack.push("mainCalendar");		
//	jobStack.push("subCalendar");
//	
//	PageMainLoadingManager.startLoad(jobStack);		
	
	var mainLayerPane = new LayerPane("c_mainBody","TM_m_mainBody");
	var menuLayerPane = new LayerPane("c_leftMenu","TM_m_leftMenu",220,230,350);
	var contentLayerPaneWapper = new LayerPane("c_contentBodyWapper","",300,100,500);
	
	
	mainSplitter = new SplitterManager(mainLayerPane,
			menuLayerPane,
			contentLayerPaneWapper,
			"sm","mainvsplitbar","hsplitbar");
	mainSplitter.setReferencePane(["c_contentBody","copyRight"]);
	mainSplitter.setSplitter("v",true);	
	jQuery(window).autoResize(jQuery("#c_mainBody"), "#copyRight");
	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();	
	
	var contentLayerPane = new LayerPane("c_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("c_contentMain","TM_m_contentMain",300,0,0);
	var previewLayerPane = new LayerPane("c_contentSub","TM_m_contentSub",400,0,0);
	
	contentSplitter = new SplitterManager(contentLayerPane,
											listLayerPane,
											previewLayerPane,
											"sc","vsplitbar","hsplitbar");
	contentSplitter.setSplitter("n",true);	
	jQuery(window).autoResize(jQuery("#c_contentBody"),"#copyRight");	
	contentSplitter.setSplitter("n",true);
	
	schedulerControl = new SchedulerControl(schedulerOption);
	shareGroupControl = new ShareGroupControl(shareGroupOption);
	schedulerDataControl = new SchedulerDataControl();
	scheduerAssetControl = new ScheduerAsset();
	initService();
	jQuery.removeProcessBodyMask();
	
	jQuery("#searchTypeSelect").selectbox({selectId:"searchType",
		selectFunc:""},
		"0",
		[{index:schedulerMsg.scheduler_search_subject,value:0},
		 {index:schedulerMsg.scheduler_search_content,value:1},
		 {index:(schedulerMsg.scheduler_search_subject+"+"+schedulerMsg.scheduler_search_content),value:2}]);	
});

function resizeLeftMenuSize(){
	var sideMenuHeight = 0;
	if(IS_LMENU_USE){
		var sideMenuShow = jQuery("#leftSideMenu").attr("viewmenu");
		sideMenuHeight = (sideMenuShow == "show")?jQuery("#leftSideMenu").height():25;
	}
	var topAreaSize = jQuery("#ml_btnFMain_s").height()+3;
	var extHeight = (!IS_LMENU_USE)?topAreaSize:topAreaSize+sideMenuHeight;
	
	var inResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontentWrapper", 
		mainId:"#c_leftMenu", 
		sideObjId:["#copyRight"],
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:extHeight});
	
	var outResizefunc = jQuery(window).resizeInnerFrame({resizeId:"#leftMenuRcontent", 
		mainId:"#c_leftMenu", 
		sideObjId:["#copyRight"],
		wrapperMode:false,
		isNoneWidthChk:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:false});
	
	inResizefunc(jQuery(window),jQuery("#leftMenuRcontentWrapper"),true);
	outResizefunc(jQuery(window),jQuery("#leftMenuRcontent"),true);			
	
	
	jQuery(window).trigger("resize");
}

function initService(){
	var param;	
	HistoryManager.loadHistoryManager(historyCallBackFunc);
	checkCalendar();
	loadCalendarSchedule();		
	shareGroupControl.reloadShareInfo();
	scheduerAssetControl.readAssetCategoryList();
}

function historyCallBackFunc(url, mode){
	var isLoad = true;
	if(LayoutInfo.mode != mode){
		if(mode == "month"){
			schedulerControl.loadMonthSchedule({});
			isLoad = false;
		} else if(mode == "week"){
			schedulerControl.loadWeekSchedule({});
			isLoad = false;
		} else if(mode == "day"){
			schedulerControl.loadDaySchedule({});
			isLoad = false;
		} else if(mode == "search"){		
			schedulerControl.searchSchedule({});
			isLoad = false;
		}
	}
	return isLoad;
}

function makeDateFormat (date) {
	if (!date) {
		return "";
	}
	return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
}

function makeTimeFormat (time) {
	if (!time) {
		return "";
	}
	
	var ampm = schedulerMsg.scheduler_am;
	var hour = time.substring(8,10);
	var min = time.substring(10,12);
	if (hour > 12) {
		hour = hour - 12;
		if (hour < 10) {
			hour = "0"+hour;
		}
		ampm = schedulerMsg.scheduler_pm;
	} else if (hour == 12) {
		ampm = schedulerMsg.scheduler_pm;
	}
	return ampm+" "+hour+":"+min;
}

function makeInputDialog() {
	
	jQuery("#input_box .TB_scheduleAdd").css("width", "0%");
	cancelRepeatSchedule();
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 460,
			minWidth:650,
			btnList : [{name:comMsg.comn_add,func:saveSchedule}],	
			openFunc:function(){
	 			jQuery("#input_box .TB_scheduleAdd").css("width", "100%");
	 			jQuery("#input_box_pht").text(schedulerMsg.scheduler_title_insert);
	 			jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	 			jQuery("#inputStartDate").datepick({dateFormat:'yy-mm-dd', onSelect:makeAssetControl});
	 			jQuery("#inputEndDate").datepick({dateFormat:'yy-mm-dd', onSelect:makeAssetControl});
	 			jQuery("#repeatEndDate").datepick({dateFormat:'yy-mm-dd'});
	 			jQuery("#datepick-div").css("z-index","10001");
	 			
	 			makeInputShareGroupList();
	 			var assetGroup = jQuery("#s_assetGroup").attr("data");
	 			if(!assetGroup){
	 				jQuery("#assetReserveLabel").hide();
	 			}
	 			
			},
			closeFunc:function(){
				jQuery("#input_box .TB_scheduleAdd").css("width", "0%");
				jQuery("#inputStartDate").removeClass("hasDatepick");
				jQuery("#inputEndDate").removeClass("hasDatepick");
				jQuery("#repeatEndDate").removeClass("hasDatepick");
				resetInputForm();
				jQuery(".timePick").hide();

				jQuery.datepick._hideDatepick();
			}
		};
	jQuery("#input_box").jQpopup("open",popupOpt);	
}

function makeModifyDialog() {
	jQuery("#input_box .TB_scheduleAdd").css("width", "0%");
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 460,
			minWidth:650,
			btnList : [{name:comMsg.comn_modfy,func:saveSchedule}],	
			openFunc:function(){
	 			jQuery("#input_box .TB_scheduleAdd").css("width", "100%");
	 			jQuery("#input_box_pht").text(schedulerMsg.scheduler_title_modify);
	 			var assetGroup = jQuery("#s_assetGroup").attr("data");
	 			if(!assetGroup){
	 				jQuery("#assetReserveLabel").hide();
	 			}
			},
			closeFunc:function(){
				jQuery("#input_box .TB_scheduleAdd").css("width", "0%");
				jQuery("#inputStartDate").removeClass("hasDatepick");
				jQuery("#inputEndDate").removeClass("hasDatepick");
				jQuery("#repeatEndDate").removeClass("hasDatepick");
				resetInputForm();
				jQuery(".timePick").hide();
				jQuery.datepick._hideDatepick();
				cancelRepeatSchedule();
			}
		};
	jQuery("#input_box").jQpopup("open",popupOpt);
	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	jQuery("#inputStartDate").datepick({dateFormat:'yy-mm-dd', onSelect:makeAssetControl});
	jQuery("#inputEndDate").datepick({dateFormat:'yy-mm-dd', onSelect:makeAssetControl});
	jQuery("#repeatEndDate").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#datepick-div").css("z-index","10001");
}

function checkCalendar() {
	var type = jQuery("#cType").val();

	if (type == "calendar") {
		var year = jQuery("#calendarYear").val();
		var month = jQuery("#calendarMonth").val();
		var day = jQuery("#calendarDay").val();
		loadDaySchedule(year, month, day);
	} else if (type == "today") {
		var scheduleId = jQuery("#scheduleId").val();
		var calendarStartDate = jQuery("#calendarStartDate").val();
		var calendarEndDate = jQuery("#calendarEndDate").val();
		loadDaySchedule();
		viewSchedule(scheduleId, calendarStartDate, calendarEndDate);
	} else {
		loadMonthSchedule();
	}
}

function makeRepeatClickEvt() {
	jQuery("#repeat_control :radio").click(function(){
		if (this.id == "repeatType") {
			if (jQuery(this).val() == "03") {
				jQuery("#monthSubType").attr("checked", true);
			}
			else {
				jQuery("#month_sub_repeat :radio").each(function(i) {
					jQuery(this).attr("checked", false);
				});
			}
		}
	});
}

function resetRepeat() {
	
	jQuery("#repeat").val('');
	jQuery("#repeat").attr("checked", false);
	jQuery("#checkEndDate").attr("checked",false);
	jQuery("#repeatEndDate").val('');
	
	jQuery("#repeat_control :radio").each(function(i) {
		if (this.id == "repeatType" && jQuery(this).val() == "00") {
			jQuery(this).attr("checked", true);
		} else {
			jQuery(this).attr("checked", false);
		}
	});
	
	jQuery("#repeat_control").hide();
}

function resetViewSchedule() {
	jQuery('#schedulerId').val("");
	jQuery('#schedulerShare').val("");
	jQuery('#schedulerRepeat').val("");
	jQuery('#schedulerSelectDate').val("");
	jQuery('#repeatSelectStartDate').val("");
	jQuery('#repeatSelectEndDate').val("");
	jQuery('#view_box_pht').text("");
	jQuery('#view_date').text("");
	jQuery('#view_title').text("");
	jQuery('#view_title_title').attr("title", "");
	jQuery('#view_location').text("");
	jQuery('#view_location_title').attr("title", "");
	jQuery('#view_content').val("");
	jQuery('#view_repeat').text("");
	jQuery('#view_share').text("");
	jQuery('#check_send_mail').val("");
	
	jQuery("#view_repeat_tr").hide();
	jQuery("#view_share_tr").hide();
	jQuery("#view_share_dot_tr").hide();
	jQuery("#view_share_user_tr").hide();
	jQuery("#view_share_target_tr").hide();
	jQuery("#view_scheduler_asset").text("");
	jQuery("#view_scheduler_asset_contect").text("");
	jQuery("#view_scheduler_asset_tr").hide();
	jQuery("#view_scheduler_asset_contect_tr").hide();

}

function contentSplitterChange(mode){
	contentSplitter.setSplitter(mode);
	paneMode = mode;
	initSubMode();	
}

function gotoMonth(year, month) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	var param = {"year" : year, "month":month};
	schedulerControl.loadMonthSchedule(param);
	contentSplitter.setSplitter("n");
}

function changeMonth() {
	var year = jQuery('#select_year').val();
	var month = jQuery('#select_month').val();
	loadMonthSchedule(year, month);
}

function gotoWeek(year, month, day) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	if (!day) {
		day = "0";
	}
	var param = {"year" : year, "month":month, "day":day};
	schedulerControl.loadWeekSchedule(param);
	contentSplitter.setSplitter("n");
}

function gotoDay(year, month, day) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	if (!day) {
		day = "0";
	}
	var param = {"year" : year, "month":month, "day":day};
	schedulerControl.loadDaySchedule(param);
	contentSplitter.setSplitter("n");
}

function setSchedulerInfo(name){
	jQuery("#finfo_fname").text(name);	
}

function openPlanAll(date) {
	
	if (jQuery("#plan_all_"+date).css("display") != 'none') {
		jQuery("#plan_all_"+date).empty();
		jQuery("#plan_all_"+date).hide();
		return;
	}
	
	var myMode = jQuery("#myMode").val();
	var myYear = jQuery("#myYear").val();;
	var myMonth = jQuery("#myMonth").val();
	var myDay = "";
	if (myMode == "week" || myMode == "day") {
		myDay = jQuery("#myDay").val();
	}

	if (myMode == "month")
		schedulerDataControl.readDayAllInfo(myYear, myMonth, 0, "month", date);
	else if (myMode == "week") {
		schedulerDataControl.readDayAllInfo(myYear, myMonth, myDay, "week", date);
	}
	else if (myMode == "day") {
		schedulerDataControl.readDayAllInfo(myYear, myMonth, myDay, "day", date);
	}
	
}

function checkDndAfter(date) {
	var myMode = jQuery("#myMode").val();
	var myYear = jQuery("#myYear").val();;
	var myMonth = jQuery("#myMonth").val();
	var myDay = "";
	if (myMode == "week" || myMode == "day") {
		myDay = jQuery("#myDay").val();
	}

	if (myMode == "month")
		schedulerDataControl.readDayAllInfo(myYear, myMonth, 0, "month", date);
	else if (myMode == "week") {
		schedulerDataControl.readDayAllInfo(myYear, myMonth, myDay, "week", date);
	}
	else if (myMode == "day") {
		schedulerDataControl.readDayAllInfo(myYear, myMonth, myDay, "day", date);
	}
}

function viewInputBox(date, time) {	
	jQuery('#saveType').val("add");
	
	if (jQuery("#view_box_p").css("display") != 'block') {		 
		 makeInputDialog();
	}
	makeInputBoxDate(date, time);
}

function makeInputBoxDate(date, time) {
	 var year = date.substring(0,4);
	 var month = date.substring(4,6);
	 var day = date.substring(6,8);
	 var dayString = year+"-"+month+"-"+day;
	 var thisDate = new Date(year,month-1,day);
	 thisDate.setMonth(thisDate.getMonth()+1);
	 var monthString = parseInt(thisDate.getMonth()+1, 10);
	 var dateString = thisDate.getDate();
	 
	 if (monthString < 10) {
		 monthString = "0" + monthString; 
	 }
	 if (dateString < 10) {
		 dateString = "0" + dateString; 
	 }
	 var repeatEndDateString = thisDate.getFullYear()+"-"+monthString+"-"+dateString;
	 
	 jQuery("#inputStartDate").val(dayString);
	 jQuery("#inputEndDate").val(dayString);
	 jQuery("#repeatEndDate").val(repeatEndDateString);
	 
	 settingTimePick(dayString, time);
}

function settingTimePick(date, time) {
	 var dateArray = date.split("-");
	 var t = new Date();
	 var d = new Date(dateArray[0],dateArray[1]-1,dateArray[2]);
	 var shour = t.getHours();
	 var smin = t.getMinutes();
	 var ehour;
	 var emin;
	 
	 var sampm = 0;
	 var eampm = 0;
	 
	 if (time) {
		var date = time.split(':');
		shour = date[0];
		smin = date[1];
		shour = parseInt(shour,10);
		smin = parseInt(smin,10);
	 }
	 
	 if (shour > 12) {
		sampm = 1;
		shour = shour - 12;		
	 } else if (shour == 12) {
		sampm = 1;
	 } else if (shour == 11 && smin > 30) {
		sampm = 1;
	 }
	 
	 if (smin > 30) {
		shour = (shour == 12)?1:shour + 1;
		smin = "00";
	 }
	 else if (smin == 0) {
		smin = "00";
	 } else {
		smin = "30";
	 }

	 var stimeStr = ((Number(shour)< 10)?"0"+shour:shour)+":"+smin;
	
	 
	 if(smin == "30"){
		 if(Number(shour) == 12){
			 eampm = (sampm == 1)?0:1;
			 ehour = 1;		 
		 } else if(Number(shour) == 11) {
			 eampm = (sampm == 1)?0:1;
			 ehour = (eampm == 1)?12:0;
		 }
		 else {
			 eampm = sampm;
			 ehour = shour+1;
		 }
		 emin = "00";
	 } else {
		 eampm = sampm;
		 ehour = shour;
		 emin = "30";
	 }
	 
	 
	 var etimeStr = ((Number(ehour)< 10)?"0"+ehour:ehour)+":"+emin;
	 
	
	shour = (Number(shour) == 12) ? 0 : shour;
	ehour = (Number(ehour) == 12) ? 0 : ehour;
	
	if (ehour == 0 && emin == "00" && eampm == 0) {
		d.setDate(d.getDate()+1);
		var monthString = d.getMonth()+1;
		if (monthString < 10) {
			 monthString = "0" + monthString; 
		}
		var dateString = d.getDate();
		if (dateString < 10) {
			 dateString = "0" + dateString; 
		}
		jQuery("#inputEndDate").val(d.getFullYear()+"-"+monthString+"-"+dateString);
	}
	
	var stimeStrFormat = ((Number(shour)< 10)?"0"+shour:shour)+":"+smin;
	var etimeStrFormat = ((Number(ehour)< 10)?"0"+ehour:ehour)+":"+emin;
	 
	var sampmText,eampmText;
	sampmText = (sampm == 0)?schedulerMsg.scheduler_am:schedulerMsg.scheduler_pm;
	eampmText = (eampm == 0)?schedulerMsg.scheduler_am:schedulerMsg.scheduler_pm;
	
	var type = jQuery("#currentType").val();
	
	var stpickerId = "start_time_picker";
	var etpickerId = "end_time_picker";
	var stimeId = "startTime";
	var etimeId = "endTime";
	var inputStimeId = "inputStartTime";
	var inputEtimeId = "inputEndTime";
	
	jQuery("#"+stpickerId+"_ampmSelect").data("ampm",sampm);
	jQuery("#"+etpickerId+"_ampmSelect").data("ampm",eampm);
	jQuery("#"+stpickerId+"_timeSelect").data("date",stimeStrFormat);
	jQuery("#"+etpickerId+"_timeSelect").data("date",etimeStrFormat);
	
	 jQuery("#"+inputStimeId).val(sampmText+" "+stimeStr);	
	 
	 jQuery("#"+inputEtimeId).val(eampmText+" "+etimeStr);
	 
	 var sTimeArray = stimeStr.split(':');
	 var eTimeArray = etimeStr.split(':');
	
	 if (sampm == 1) {
		 sTimeArray[0] = (parseInt(sTimeArray[0],10) == 12) ? parseInt(sTimeArray[0],10) : parseInt(sTimeArray[0],10)+12;		 
	 }
	 
	 if (eampm == 1) {
		 eTimeArray[0] = (parseInt(eTimeArray[0],10) == 12) ? parseInt(eTimeArray[0],10) : parseInt(eTimeArray[0],10)+12;		 
	 }

	 jQuery("#"+stimeId).val(sTimeArray);
	 jQuery("#"+etimeId).val(eTimeArray);
}

function makeTimePickSelect(){
	
	var type = jQuery("#currentType").val();
	
	var stpickerId = "start_time_picker";
	var etpickerId = "end_time_picker";
	var stimeId = "startTime";
	var etimeId = "endTime";
	
	var sampm = jQuery("#"+stpickerId+"_ampmSelect").data("ampm");
	var eampm = jQuery("#"+etpickerId+"_ampmSelect").data("ampm");
	var sdate = jQuery("#"+stpickerId+"_timeSelect").data("date");
	var edate = jQuery("#"+etpickerId+"_timeSelect").data("date");	
	
	var timeArray = [];
	 timeArray.push({index:"12:00",value:"00:00"});
	 timeArray.push({index:"12:30",value:"00:30"});
	 var hourSelectIndex;
	 for ( var i = 1; i < 12; i++) {
		 if(i < 10){
			 hourSelectIndex = "0"+i;
		 } else{
			 hourSelectIndex = i;
		 }
		 timeArray.push({index:hourSelectIndex+":00",value:hourSelectIndex+":00"});
		 timeArray.push({index:hourSelectIndex+":30",value:hourSelectIndex+":30"});
	}	 
	jQuery("#"+stpickerId+"_ampmSelect").selectbox(
			 {selectId:stpickerId+"_ampm",selectFunc:""},sampm,
			 [{index:schedulerMsg.scheduler_am,value:"0"},
			  {index:schedulerMsg.scheduler_pm,value:"1"}]);
	jQuery("#"+stpickerId+"_timeSelect").selectbox(
			 {selectId:stpickerId+"_time",selectFunc:""},sdate,
			 timeArray);
	 
	jQuery("#"+etpickerId+"_ampmSelect").selectbox(
			 {selectId:etpickerId+"_ampm",selectFunc:""},eampm,
			 [{index:schedulerMsg.scheduler_am,value:"0"},
			  {index:schedulerMsg.scheduler_pm,value:"1"}]);
	 
	jQuery("#"+etpickerId+"_timeSelect").selectbox(
			 {selectId:etpickerId+"_time",selectFunc:""},edate,
			 timeArray);
	
	 var sTimeValue = $(stpickerId+"_time").value;
	 var eTimeValue = $(etpickerId+"_time").value;
	 var sTimeArray = sTimeValue.split(':');
	 var eTimeArray = eTimeValue.split(':');
	
	 if (sampm == 1) {
		 sTimeArray[0] = parseInt(sTimeArray[0],10)+12;		 
	 }
	 
	 if (eampm == 1) {
		 eTimeArray[0] = parseInt(eTimeArray[0],10)+12;		 
	 }

	 jQuery("#"+stimeId).val(sTimeArray);
	 jQuery("#"+etimeId).val(eTimeArray);
}

function removeTimePickSelect(){
	
	var type = jQuery("#currentType").val();
	
	var stpickerId = "start_time_picker";
	var etpickerId = "end_time_picker";

	jQuery("#"+stpickerId+"_ampmSelect").empty();
	jQuery("#"+stpickerId+"_timeSelect").empty();	 
	jQuery("#"+etpickerId+"_ampmSelect").empty();	 
	jQuery("#"+etpickerId+"_timeSelect").empty();
}

function resetInputForm() {
	jQuery("#title").val('');
	jQuery("#location").val('');
	jQuery("#content").val('');
	jQuery("#allday").attr("checked",false);
	jQuery("#holiday").attr("checked",false);
	jQuery('#start_time_picker').hide(function(){removeTimePickSelect();});
	jQuery('#end_time_picker').hide(function(){removeTimePickSelect();});
	
	checkAllday(false);
	resetRepeat();
	resetInputShareGroup();
	
	jQuery("#assetReserve").attr("checked",false);
	hideAssetSchedule();
}

function addSchedule() {
	var date = jQuery('#currentDate').val();
	viewInputBox(date);
}

function checkAllday(checked) {
	if (checked) {
		jQuery("#inputStartTime").hide();
		jQuery("#inputEndTime").hide();
		if (jQuery("#assetReserve").attr("checked")) {
			makeAssetControl();
		}
	}
	else {
		jQuery("#inputStartTime").show();
		jQuery("#inputEndTime").show();
	}
}

var openTimePickerID="";
function toggleTimePicker(id) {	
	if("none" != jQuery("#"+id).css("display")){
		hideTimePicker(id);		
		openTimePickerID = "";
	}else{
		if(openTimePickerID != ""){
			hideTimePicker(openTimePickerID);
		}
		showTimePicker(id);
		openTimePickerID = id;
		
	}
}

function showTimePicker(id) {
	jQuery("#"+id).slideDown();
	makeTimePickSelect();
}

function hideTimePicker(id) {
	jQuery("#"+id).slideUp();
	removeTimePickSelect();
}

function hideAllTimePicker() {
	jQuery("#start_time_picker").slideUp();
	jQuery("#end_time_picker").slideUp();
	jQuery("#start_asset_time_picker").slideUp();
	jQuery("#end_asset_time_picker").slideUp();
	removeTimePickSelect();
}

function setTimePicker(id, timeId, targetId){	
	
	var ampmValue = jQuery("#"+id+"_ampm").val();
	var timeValue = jQuery("#"+id+"_time").val();
	var timeText = jQuery("#"+id+"_timeSelect").selectboxGetText(timeValue);
	var ampmText = (Number(ampmValue) == 0)?schedulerMsg.scheduler_am:schedulerMsg.scheduler_pm;
	
	var timeArray = timeValue.split(':');		 
	if (ampmValue == 1) {
		timeArray[0] = parseInt(timeArray[0],10)+12;
	} else {
		timeText = timeValue;
	}
	jQuery("#"+id+"_ampmSelect").data("ampm", ampmValue);
	jQuery("#"+id+"_timeSelect").data("date", timeValue);
	jQuery("#"+timeId).val(timeArray);
	jQuery("#"+targetId).val(ampmText+" "+timeText);
	toggleTimePicker(id);
	makeAssetControl();
}

function viewSchedule(schedulerId, startDate, endDate) {
	resetViewSchedule();
	schedulerDataControl.readSchedule(schedulerId, startDate, endDate);
	jQuery("#view_box").jQpopup("close");
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 500,
			minWidth:420,
			btnList : [{name:comMsg.comn_modfy,func:modifySchedule},{name:comMsg.comn_del,func:deleteSchedule}],	
			openFunc:function(){
				jQuery("#view_box_pht").text(schedulerMsg.scheduler_title_view);
				jQuery("#view_box_jqbtn a").hide();
			},
			closeFunc:function(){
			}
		};
	
	jQuery("#view_box").jQpopup("open",popupOpt);
}

function checkTimeRange(start, end, startTime, endTime, allDay) {
	if (start.getTime() > end.getTime()) {
		 alert(schedulerMsg.scheduler_alert_startDate_big);
		 return false;
	 }
	 else if (start.getTime() == end.getTime()) {
		 if (!allDay) {
			 if (parseInt(startTime[0],10) > parseInt(endTime[0],10)) {
				 alert(schedulerMsg.scheduler_alert_startTime_big);
				 return false; 
			 }
			 else if (parseInt(startTime[0],10) == parseInt(endTime[0],10)) {
				 if (parseInt(startTime[1],10) > parseInt(endTime[1],10)) {
					 alert(schedulerMsg.scheduler_alert_startTime_big);
					 return false; 
				 } else if (parseInt(startTime[1],10) == parseInt(endTime[1],10)) {
					 alert(schedulerMsg.scheduler_alert_startEnd_same);
					 return false; 
				 }
			 }
		 }
	 }
	return true;
}

function saveSchedule() {

	var titleObj = jQuery("#title");
	var title = titleObj.val(); 
	 
	if(!checkInputLength("jQuery", titleObj, schedulerMsg.scheduler_alert_notitle, 2, 128)) {
		return;
	}
	if(!checkInputValidate("jQuery", titleObj, "onlyBack")) {
		return;
	}
	
	 var startDate = jQuery("#inputStartDate").val().split('-');
	 var endDate = jQuery("#inputEndDate").val().split('-');
	 var startTime = jQuery("#startTime").val().split(',');
	 var endTime =jQuery("#endTime").val().split(',');
	 var start = new Date(startDate[0],startDate[1]-1, startDate[2]);
	 var end = new Date(endDate[0],endDate[1]-1,endDate[2]);
	 var allday = jQuery("#allday").attr("checked");

	 if (startDate.length < 3) {
		 alert(schedulerMsg.scheduler_alert_startDate_empty);
		 return;
	 }
	 
	 if (endDate.length < 3) {
		 alert(schedulerMsg.scheduler_alert_endDate_empty);
		 return;
	 }
	 
	 
	if (!checkTimeRange(start, end, startTime, endTime, allday)) {
		 return;
	}
	 
	
	var startDateStr = startDate[0]+startDate[1]+startDate[2]+startTime[0]+startTime[1];
	var endDateStr = endDate[0]+endDate[1]+endDate[2]+endTime[0]+endTime[1];
	
	var schedulerId = jQuery('#schedulerId').val();
	var locationObj = jQuery("#location");
	var location = locationObj.val();
	var contentObj = jQuery("#content");
	var content = contentObj.val();
	var holiday = jQuery("#holiday").attr("checked");
	var repeatFlag = jQuery("#repeat").attr("checked");
	var repeatTerm = jQuery("#repeat").val();
	var checkEndDate = jQuery("#checkEndDate").attr("checked");
	var repeatEndDate = jQuery("#repeatEndDate").val().split('-');
	var repeatEndDateStr = "";

	if (allday) { 
		allday = "on";
		startDateStr = startDate[0]+startDate[1]+startDate[2]+"0000";
		endDateStr = endDate[0]+endDate[1]+endDate[2]+"2330";
	}
	else {
		allday = "";
	}
	if (holiday) 
		holiday = "on";
	else 
		holiday = "";

	if (repeatFlag) {
		repeatFlag = "on";
		if (trim(repeatTerm) == "") {
			alert(schedulerMsg.scheduler_alert_repeatTerm_empty);
			return;
		} 
		else {
			var checkRepeatTerm = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
			var repeatType = repeatTerm.substring(0,2);
			var repeatValue1;
			var repeatValue2;
			if (repeatType == "01") {
				repeatValue1 = repeatTerm.substring(4);
				repeatValue1 = parseInt(repeatValue1, 10);
				if (checkRepeatTerm >= repeatValue1) {
					alert(schedulerMsg.scheduler_alert_repeat_big_term);
					return;
				}
			} else if (repeatType == "02") {
				repeatValue1 = repeatTerm.substring(2,4);
				repeatValue1 = parseInt(repeatValue1, 10);
				repeatValue2 = repeatTerm.substring(4);
				
				if (checkRepeatTerm >= repeatValue1*7) {
					alert(schedulerMsg.scheduler_alert_repeat_big_term);
					return;
				}
				
				var subRepeatArray = new Array(repeatValue2.length/2);
				var minTerm = 0;
				for (var i=0; i < repeatValue2.length/2; i++) {
					subRepeatArray[i] = parseInt(repeatValue2.substring(i*2, (i*2)+2), 10);
				}
				
				if (subRepeatArray.length > 1) {
					minTerm = subRepeatArray[1] - subRepeatArray[0];
					for (var i=1; i < subRepeatArray.length-1; i++) {
						if (minTerm > (subRepeatArray[i+1] - subRepeatArray[i])) {
							minTerm = subRepeatArray[i+1] - subRepeatArray[i];
						}
					}
					if (checkRepeatTerm >= minTerm) {
						alert(schedulerMsg.scheduler_alert_repeat_big_term);
						return;
					}
				}
				
			} else if (repeatType == "03") {
				repeatValue1 = repeatTerm.substring(2,4);
				repeatValue1 = parseInt(repeatValue1, 10);
				if (checkRepeatTerm >= repeatValue1*30) {
					alert(schedulerMsg.scheduler_alert_repeat_big_term);
					return;
				}
			} else if (repeatType == "04") {
				repeatValue1 = repeatTerm.substring(2,4);
				repeatValue1 = parseInt(repeatValue1, 10);
				if (checkRepeatTerm >= 365) {
					alert(schedulerMsg.scheduler_alert_repeat_big_term);
					return;
				}
			}
		}
	}
	else {
		repeatFlag = "off";
	}

	if (checkEndDate) {
		if (repeatEndDate.length < 3) {
			alert(schedulerMsg.scheduler_alert_repeatEndDate_empty);
			return;
		}
		
		var repeatEnd = new Date(repeatEndDate[0],repeatEndDate[1]-1,repeatEndDate[2]);
		 if (end.getTime() > repeatEnd.getTime()) {
			 alert(schedulerMsg.scheduler_alert_repeat_big);
			 return;
		 }
		
		repeatEndDateStr = repeatEndDate[0]+repeatEndDate[1]+repeatEndDate[2];
	}
	
	if(!checkInputLength("jQuery", locationObj, "", 0, 128)) {
		return;
	}
	if(!checkInputValidate("jQuery", locationObj, "onlyBack")) {
		return;
	}
	
	if(!checkInputLength("jQuery", contentObj, "", 0, 1024)) {
		return;
	}
	
	//scheduler share
	var sharecheck = jQuery("#checkShare").attr("checked");
	var selftargetcheck = jQuery("#selfShareTarget").attr("checked");
	var selftarget = jQuery("#shareTargetEmail option");
	var shareValue = "";
	var checkShare = "off";
	var checkSelfTarget = "off";
	var checkSendMail = "off";
	var selfTargetList =[];

	if (sharecheck) {
		shareValue = jQuery("#shareGroup").val();
		if (shareValue == "") {
			if (!selftargetcheck) {
				if (jQuery("#shareGroupSelect").selectboxGetOptionsCount() > 1) 
					alert(schedulerMsg.scheduler_share_alert_group_noselect);
				else 
					alert(schedulerMsg.scheduler_share_alert_group_empty);
				return;
			}
		}
		
		if (selftargetcheck) {
			if (selftarget.length == 0) {
				alert(comMsg.error_login_email);
				jQuery("#targetId").focus();
				return;
			} else {
				for (var i=0; i<selftarget.length; i++) {
					selfTargetList.push(jQuery(selftarget[i]).attr("value"));
				}
				checkSelfTarget = "on";
			}
		} else {
			checkSelfTarget = "off";
		}
		
		checkShare = "on";
		
		var sendMail = jQuery("#sendMail").attr("checked");
		if (sendMail) {
			checkSendMail = "on";
		}
	}
	
	//scheduler asset
	var assetReserveFlag = jQuery("#assetReserve").attr("checked");
	var assetReserveValue = "";
	var contect = "";
	var checkAsset = "off";
	if (assetReserveFlag) {
		var today = jQuery("#thisDate").val();
		var todayDate = new Date(today.substring(0,4), today.substring(4,6)-1, today.substring(6));
		if (start.getTime() < todayDate.getTime()) {
			alert(schedulerMsg.scheduler_asset_021);
			return;
		}
		

		var parseStartHour = parseInt(startTime[0],10);
		var parseEndHour = parseInt(endTime[0],10);
		var parseStartMin = parseInt(startTime[1],10);
		var parseEndMin = parseInt(endTime[1],10);
		
		if (!allday) {	 
			var current = jQuery("#currentTime").val();
			if (start.getTime() == todayDate.getTime() && end.getTime() == todayDate.getTime()) {
				var currentHour = parseInt(current.substring(8,10), 10);
				var currentMin = parseInt(current.substring(10,12), 10);
				if ((parseEndHour < currentHour) || ((parseEndHour == currentHour) && (parseEndMin < currentMin))) {
					alert(schedulerMsg.scheduler_asset_024);
					return;
				}
			}
		}
		
		assetReserveValue = jQuery("#assetReserve").val();
		if (assetReserveValue == "on" || assetReserveValue == "") {
			alert(schedulerMsg.scheduler_asset_014);
			return;
		}
		
		checkAsset = "on";
		contect = jQuery("#contect").val();
	}
	 
	var data = {
			  schedulerId:schedulerId,
			  title:title,
			  startDate:startDateStr,
			  endDate:endDateStr,
			  allDay:allday,
			  holiday:holiday,
			  location:location,
			  content:content,
			  repeatFlag:repeatFlag,
			  repeatTerm:repeatTerm,
			  repeatEndDate:repeatEndDateStr,
			  checkShare:checkShare,
			  shareValue:shareValue,
			  checkAsset:checkAsset,
			  assetReserveValue:assetReserveValue,
			  contect:contect,
			  checkSelfTarget:checkSelfTarget,
			  selfTargetList:selfTargetList,
			  checkSendMail:checkSendMail
			};
	
	var saveType = jQuery('#saveType').val();
	if (saveType == "add") {
		schedulerDataControl.saveSchedule(data);
	}
	else if (saveType == "modify") {
		schedulerDataControl.modifySchedule(data);
	} 
	else if (saveType == "repeatModify") {
		data.ignoreTime = jQuery('#schedulerSelectDate').val().substring(0,8);
		schedulerDataControl.repeatModifySchedule(data);
	}

	jQuery("#input_box").jQpopup("close");
}

function reload() {
	var myMode = jQuery("#myMode").val();
	var myYear = jQuery("#myYear").val();;
	var myMonth = jQuery("#myMonth").val();
	var myDay = "";
	if (myMode == "week" || myMode == "day" || myMode == "asset") {
		myDay = jQuery("#myDay").val();
	}
	
	if (myMode == "month")
		loadMonthSchedule(myYear, myMonth);
	else if (myMode == "week") {
		loadWeekSchedule(myYear, myMonth, myDay);
	}
	else if (myMode == "day") {
		loadDaySchedule(myYear, myMonth, myDay);
	}
	
	if (myMode == "asset") {
		loadAssetSchedule(myYear, myMonth, myDay);
	} else {
		loadCalendarSchedule(myYear, myMonth);
	}
}

function deleteSchedule() {
	var schedulerId = jQuery('#schedulerId').val();
	var checkShare = jQuery('#schedulerShare').val();
	var checkRepeat = jQuery('#schedulerRepeat').val();
	var checkSendMail = jQuery('#check_send_mail').val();
	var msg = "";
	if (checkShare == "on") {
		msg = schedulerMsg.scheduler_share_alert_schedule_delete;
	} else {
		msg = schedulerMsg.scheduler_alert_schedule_delete;
	}
	
	if (!confirm(msg)) {
		return;
	}
	
	if (checkRepeat == "on") {
		jQuery("#view_box").jQpopup("close");
		
		jQuery(".deleteRepeat").hide();
		jQuery(".modifyRepeat").hide();
		jQuery(".deleteRepeatSub").hide();
		jQuery(".modifyRepeatSub").hide();
		var popupOpt = {
				closeName:comMsg.comn_close,
				btnClass:"btn_style3",
				minHeight: 170,
				minWidth:400,
				btnList : [{name:comMsg.comn_confirm,func:deleteRepeatSchedule}],	
				openFunc:function(){
					jQuery("#confirm_box_pht").text(schedulerMsg.scheduler_repeat_delete_title);
				},
				closeFunc:function(){}
			};
		jQuery(".deleteRepeat").show();
		jQuery(".deleteRepeatSub").show();
		
		jQuery("#confirm_box").jQpopup("open",popupOpt);
	} else {
		schedulerDataControl.deleteSchedule(schedulerId, checkSendMail);
		jQuery("#view_box").jQpopup("close");
	}
}

function deleteRepeatSchedule() {
	var f = document.scheduleRepeatForm;
	var schedulerId = jQuery('#schedulerId').val();
	if (f.deleteMode[0].checked) {
		var repeatStartDate = jQuery('#schedulerSelectDate').val();
		schedulerDataControl.deleteRepeatSchedule(schedulerId, repeatStartDate);
	} else {
		schedulerDataControl.deleteSchedule(schedulerId);
	}
	jQuery("#confirm_box").jQpopup("close");
}

function modifySchedule() {
	jQuery('#saveType').val("modify");
	var schedulerId = jQuery('#schedulerId').val();
	var checkRepeat = jQuery('#schedulerRepeat').val();
	if (checkRepeat == "on") {
		jQuery("#view_box").jQpopup("close");
		
		jQuery(".deleteRepeat").hide();
		jQuery(".modifyRepeat").hide();
		jQuery(".deleteRepeatSub").hide();
		jQuery(".modifyRepeatSub").hide();
		var popupOpt = {
				closeName:comMsg.comn_close,
				btnClass:"btn_style3",
				minHeight: 170,
				minWidth:400,
				btnList : [{name:comMsg.comn_confirm,func:modifyRepeatSchedulePop}],	
				openFunc:function(){
					jQuery("#confirm_box_pht").text(schedulerMsg.scheduler_repeat_modify_title);
				},
				closeFunc:function(){}
			};
		jQuery(".modifyRepeat").show();
		jQuery(".modifyRepeatSub").show();
		
		jQuery("#confirm_box").jQpopup("open",popupOpt);
	} else {
		schedulerDataControl.modifyWriteSchedule(schedulerId, 'normal');
		jQuery("#view_box").jQpopup("close");
		makeModifyDialog();
	}
}

function modifyRepeatSchedulePop() {
	var f = document.scheduleRepeatForm;
	var schedulerId = jQuery('#schedulerId').val();
	
	if (f.modifyMode[0].checked) {
		jQuery('#saveType').val("repeatModify");
		schedulerDataControl.modifyWriteSchedule(schedulerId, 'repeat');
	} else {
		schedulerDataControl.modifyWriteSchedule(schedulerId, 'normal');
	}
	makeModifyDialog();
	jQuery("#confirm_box").jQpopup("close");
}

function modifyRepeatSchedule() {
	var f = document.scheduleRepeatForm;
	var schedulerId = jQuery('#schedulerId').val();
	if (f.modifyMode[0].checked) {
		schedulerDataControl.modifyRepeatSchedule(schedulerId, repeatStartDate);
	} else {
		schedulerDataControl.deleteSchedule(schedulerId);
	}
	jQuery("#confirm_box").jQpopup("close");
}

function viewDownload() {
	schedulerControl.outlookSchedule();
}

function downloadOutlook() {
	this.location = "/data/sync/Setup.exe";
}

function gotoSearch() {	
	
	jQuery('#keyWord').val("");
	contentSplitter.setSplitter("n");
	schedulerControl.searchSchedule();
}

function validateSearch() {
	var keyword = jQuery('#keyWord');
	
	if(!checkInputLength("jQuery", keyword, schedulerMsg.scheduler_search_keyword_empty, 2, 255)) {
		return;
	}
	if(!checkInputValidate("jQuery", keyword, "onlyBack")) {
		return;
	}
	contentSplitter.setSplitter("n");
	schedulerControl.searchSchedule(searchSchedule);
}

function searchSchedule() {
	var keyword = jQuery('#keyWord');
	var searchType = jQuery('#searchType').val();
	var keyWordValue = keyword.val();
	var currentPage = jQuery("#currentPage").val();
	schedulerDataControl.searchSchedule(searchType, keyWordValue, currentPage);
}

function scheduleDnD(type, from, toId) {
	var schedulerId = from.attr("key");
	var size = parseInt(from.attr("size"),10);
	var fromIdData = from.attr('id').split('_');
	var fromStart = fromIdData[1];
	var drowIndex = fromIdData[2];
	var startDate = toId.substring(toId.lastIndexOf('_')+1);
	var dateList;
	if (type == "month") {
		dateList = schedulerDataControl.monthDateList;
	} else if (type == "week") {
		dateList = schedulerDataControl.weekDateList;
	}
	var firstDate = dateList[0].date+"";
	var lastDate = dateList[dateList.length-1].date+"";
	var year = jQuery("#myYear").val()+"";
	var month = jQuery("#myMonth").val()+"";

	//schedulerDataControl.dndUpdateSchedule(type, year, month, schedulerId, startDate, firstDate, lastDate);
	schedulerDataControl.dndUpdateScheduleNew(type, year, month, schedulerId, startDate, firstDate, lastDate, fromStart);
}

function readHoliday(thisYear, dateList) {
	if (dateList instanceof Array)
		schedulerDataControl.readHoliday(thisYear, dateList[0].date);
	else
		schedulerDataControl.readHoliday(thisYear, dateList);
}

function repeatSchedule(checked) {
	if (jQuery("#assetReserve").attr("checked")) {
		alert(schedulerMsg.scheduler_asset_016);
		jQuery("#repeat").attr("checked",false);
		return;
	}
	
	if (checked){
		jQuery("#repeat_control").show();
		makeRepeatControl();
		jQuery("#location").attr("readOnly", true);
		jQuery("#content").attr("readOnly", true);
	}else{
		jQuery("#repeat_control").hide();
		removeRepeatControl();
		jQuery("#location").attr("readOnly", false);
		jQuery("#content").attr("readOnly", false);
	}
}

function hideRepeatSchedule() {
	jQuery("#repeat_control").hide();
	removeRepeatControl();
	jQuery("#location").attr("readOnly", false);
	jQuery("#content").attr("readOnly", false);
}

function cancelRepeatSchedule() {
	jQuery("#repeat").attr("checked", false);
	hideRepeatSchedule();
}

function removeRepeatControl(){
	jQuery("#dayTermSelect").empty();
	jQuery("#weekTerm1Select").empty();
	jQuery("#weekTerm2Select").empty();
	jQuery("#monthTerm1Select").empty();
	jQuery("#monthSubTerm1Select").empty();
	jQuery("#monthSubTerm2Select").empty();
	jQuery("#monthSubTerm3Select").empty();
	jQuery("#yearTerm1Select").empty();
	jQuery("#yearTerm2Select").empty();
}

function toggleRepeatSelectCheck(chkNum){
	switch (chkNum) {
	case '0':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",false);
		$("checkEndDate").disabled = true;
		$("repeatEndDate").disabled = true;
		$("repeatEndDate").style.backgroundColor = "#F7F7F7";
		jQuery("#repeat_control").width("230px");
		jQuery("#month_sub_repeat :radio").attr("checked", false);
		break;
	case '1':		
		jQuery("#dayTermSelect").selectboxEnable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		jQuery("#repeat_control").width("230px");
		jQuery("#month_sub_repeat :radio").attr("checked", false);
		break;
		
	case '2':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxEnable();
		jQuery("#weekTerm2Select").selectboxEnable();
		jQuery("#weekSubWrapper").show();
		jQuery(".inputSelectRightWrapper").show();
		jQuery("#monthTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		if (LOCALE == 'en') {
			jQuery("#repeat_control").width("350px");
		} else {
			jQuery("#repeat_control").width("280px");
		}
		jQuery("#month_sub_repeat :radio").attr("checked", false);
		break;
		
	case '3':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxEnable();
		jQuery("#monthSubTerm1Select").selectboxEnable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		jQuery("#repeat_control").width("230px");
		jQuery("#monthSubType").attr("checked", true);
		break;
		
	case '31':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxEnable();
		jQuery("#monthSubTerm1Select").selectboxEnable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		jQuery("#repeat_control").width("230px");
		jQuery("#repeatMType").attr("checked", true);
		break;
	case '32':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxEnable();
		jQuery("#monthSubTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm2Select").selectboxEnable();
		jQuery("#monthSubTerm3Select").selectboxEnable();
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		jQuery("#repeat_control").width("230px");	
		jQuery("#repeatMType").attr("checked", true);
		break;
		
	case '4':		
		jQuery("#dayTermSelect").selectboxDisable();
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
		jQuery("#weekSubWrapper").hide();
		jQuery(".inputSelectRightWrapper").hide();
		jQuery("#monthTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm1Select").selectboxDisable();
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
		jQuery("#yearTerm1Select").selectboxEnable();
		jQuery("#yearTerm2Select").selectboxEnable();
		jQuery("#checkEndDate").attr("checked",true);
		$("checkEndDate").disabled = false;
		$("repeatEndDate").disabled = false;
		$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		jQuery("#repeat_control").width("230px");
		jQuery("#month_sub_repeat :radio").attr("checked", false);
		break;
	case 'end':		
		$("repeatEndDate").disabled = (!$("checkEndDate").checked);
		if(!$("checkEndDate").checked){
			$("repeatEndDate").style.backgroundColor = "#F7F7F7";
		} else {
			$("repeatEndDate").style.backgroundColor = "#FFFFFF";
		}		
		break;
	}
}
function makeRepeatControl(){
	var termArray = [];
	var termVal;
	var setVal;
	var f = document.inputScheduleForm;
	var repeatType = f.repeatType;
	var isRepeatUse = false;
	for ( var i = 1; i <= 30; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_day,value:termVal});
	}	
	setVal = jQuery("#dayTermSelect").data("sval");
	
	jQuery("#dayTermSelect").selectbox(
			 {selectId:"dayTerm",selectFunc:"",height:110},setVal,
			 termArray);
	if(!repeatType[1].checked){
		jQuery("#dayTermSelect").selectboxDisable();		
	} else {
		isRepeatUse = true;
	}		
	
	termVal = "";
	termArray = [];
	for ( var i = 1; i <= 30; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_week,value:termVal});
	}
	
	setVal = jQuery("#weekTerm1Select").data("sval");
	jQuery("#weekTerm1Select").selectbox(
			 {selectId:"weekTerm1",selectFunc:"",width:60, height:110,textMaxLength:3},setVal,
			 termArray);
	
	termVal = "";
	termArray = [];
	termArray.push({index:termVal+schedulerMsg.scheduler_date_sunday,value:"01"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_monday,value:"02"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_tuesday,value:"03"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_wednesday,value:"04"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_thursday,value:"05"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_friday,value:"06"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_saturday,value:"07"});
	
	setVal = jQuery("#weekTerm2Select").data("sval");
	jQuery("#weekTerm2Select").selectbox(
			 {selectId:"weekTerm2",selectFunc:"",width:60,textMaxLength:3},setVal,
			 termArray);
	
	if(!repeatType[2].checked){
		jQuery("#weekTerm1Select").selectboxDisable();
		jQuery("#weekTerm2Select").selectboxDisable();
	} else {
		isRepeatUse = true;
	}
	
	termVal = "";
	termArray = [];
	for ( var i = 1; i <= 30; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_months,value:termVal});
	}
	
	setVal = jQuery("#monthTerm1Select").data("sval");
	jQuery("#monthTerm1Select").selectbox(
			 {selectId:"monthTerm1",selectFunc:"",height:110},setVal,
			 termArray);
	
	if(!repeatType[3].checked){
		jQuery("#monthTerm1Select").selectboxDisable();		
	} else {
		isRepeatUse = true;
	}
	
	termVal = "";
	termArray = [];
	for ( var i = 1; i <= 31; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_day,value:termVal});
	}
	
	setVal = jQuery("#monthSubTerm1Select").data("sval");
	jQuery("#monthSubTerm1Select").selectbox(
			 {selectId:"monthSubTerm1",selectFunc:"",height:110},setVal,
			 termArray);
	
	if(!f.monthSubType[0].checked){
		jQuery("#monthSubTerm1Select").selectboxDisable();		
	}
	
	termVal = "";
	termArray = [];
	termArray.push({index:termVal+schedulerMsg.scheduler_date_first,value:"01"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_second,value:"02"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_third,value:"03"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_fourth,value:"04"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_fifth,value:"05"});	
	
	setVal = jQuery("#monthSubTerm2Select").data("sval");
	jQuery("#monthSubTerm2Select").selectbox(
			 {selectId:"monthSubTerm2",selectFunc:""},setVal,
			 termArray);
	
	termVal = "";
	termArray = [];
	termArray.push({index:termVal+schedulerMsg.scheduler_date_sunday,value:"01"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_monday,value:"02"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_tuesday,value:"03"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_wednesday,value:"04"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_thursday,value:"05"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_friday,value:"06"});
	termArray.push({index:termVal+schedulerMsg.scheduler_date_saturday,value:"07"});
	
	setVal = jQuery("#monthSubTerm3Select").data("sval");
	jQuery("#monthSubTerm3Select").selectbox(
			 {selectId:"monthSubTerm3",selectFunc:"",textMaxLength:3},setVal,
			 termArray);
	
	if(!f.monthSubType[1].checked){
		jQuery("#monthSubTerm2Select").selectboxDisable();
		jQuery("#monthSubTerm3Select").selectboxDisable();
	} else {
		isRepeatUse = true;
	}
	
	termVal = "";
	termArray = [];
	for ( var i = 1; i <= 12; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_month,value:termVal});
	}
	
	setVal = jQuery("#yearTerm1Select").data("sval");
	jQuery("#yearTerm1Select").selectbox(
			 {selectId:"yearTerm1",selectFunc:checkYearRepeat,width:60,height:90,textMaxLength:3},setVal,
			 termArray);
	
	termVal = "";
	termArray = [];
	for ( var i = 1; i <= 31; i++) {		
		termVal = (i<10)?"0"+i:i+"";		
		termArray.push({index:i+""+schedulerMsg.scheduler_day,value:termVal});
	}
	
	setVal = jQuery("#yearTerm2Select").data("sval");
	jQuery("#yearTerm2Select").selectbox(
			 {selectId:"yearTerm2",selectFunc:checkYearRepeat,height:90},setVal,
			 termArray);
	
	if(!repeatType[4].checked){
		jQuery("#yearTerm1Select").selectboxDisable();
		jQuery("#yearTerm2Select").selectboxDisable();
	} else {
		isRepeatUse = true;
	}
	if(isRepeatUse){
		$("checkEndDate").disabled = false;
	} else {
		$("checkEndDate").disabled = true;
	}
	toggleRepeatSelectCheck("end");	
}

function setRepeatSchedule() {
	var repeatType = "";
	var subRepeatType = "";
	var repeatTerm1 = "";
	var repeatTerm2 = "";
	var repeatTerm3 = "";
	
	jQuery("#repeat_control :radio").each(function(i){
	   if (jQuery(this).attr("checked")) {
		   if (this.id == "repeatType" || this.id == "repeatMType") {
			   repeatType = jQuery(this).val();
		   } else {
			   subRepeatType = jQuery(this).val();
		   }
	   }
	});

	if (repeatType == "01"){
		repeatTerm1 = "01";
		repeatTerm1 += jQuery("#dayTerm").val();
	} 
	else if (repeatType == "02"){
		repeatTerm1 = jQuery("#weekTerm1").val();
		//repeatTerm2 = jQuery("#weekTerm2").val();
		var count = 0;
		jQuery(".inputSelectRightWrapper :checkbox").each(function(j) {
			if (jQuery(this).attr("checked")) {
				repeatTerm2 += jQuery(this).val();
				count++;
			}
		});
		if (count == 0) {
			alert(schedulerMsg.scheduler_alert_repeatTerm_empty);
			return;
		}
		
	} 
	else if (repeatType == "03"){
		repeatTerm1 = jQuery("#monthTerm1").val();
		if (subRepeatType == "01") {
			repeatTerm2 = jQuery("#monthSubTerm1").val();
		}
		else {
			repeatTerm2 = jQuery("#monthSubTerm2").val();
			repeatTerm3 = jQuery("#monthSubTerm3").val();
		}
	} 
	else if (repeatType == "04") {
		repeatTerm1 = jQuery("#yearTerm1").val();
		repeatTerm2 = jQuery("#yearTerm2").val();
	} 
	else {
		hideRepeatSchedule();
		jQuery("#repeat").attr("checked", false);
		return;
	}
	
	var repeatInfo = repeatType+""+repeatTerm1+""+repeatTerm2+""+repeatTerm3;
	jQuery("#repeat").val(repeatInfo);
	hideRepeatSchedule();
}

function gotoCalendar(year, month) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	loadCalendarSchedule(year, month);
}

function gotoCalendarDay(year, month, day) {
	loadDaySchedule(year, month, day);
}

function timeDragnDropSet(){
	jQuery(".timeplan").draggable({
		cursor: 'move',
		cursorAt: { top: -5, left: -5 },
		helper: function(event) {
			var id = jQuery(this).attr('id');
			return jQuery('<div class="dragHelper">'+jQuery("#"+id+" .schDiv").text()+'</div>');
		}
	});
}

function makeTimeArray() {
	var time = new Array();
	var zero = "00";
	var half = "30";
	for(i=0; i<24; i++) {
		time.push(i+zero);
		time.push(i+half);
	}
	return time;
}

function setSchedulerTab(index) {
	jQuery('.tabMenu_btn a').removeClass();
	jQuery('.tabMenu_btn a').addClass('off');
	jQuery('.tabMenu_btn a:eq('+index+')').removeClass('off');
	jQuery('.tabMenu_btn a:eq('+index+')').addClass('on');
}

function makeMonthTabMenu(tabMenuData) {
	jQuery('.leftArea').empty();
	jQuery('.rightArea').empty();
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('year').text(tabMenuData.year+tabMenuData.yearText));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('prev').attr('href','javascript:'+tabMenuData.prevLink));
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_num').text(tabMenuData.month));
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_text').text(tabMenuData.monthText));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('next').attr('href','javascript:'+tabMenuData.nextLink));
	if (installLocale != 'jp') {
		jQuery('.leftArea').append(jQuery('<span></span>').addClass('lunar').text('('+tabMenuData.lunarText+') '+tabMenuData.lunarMonthText+' '+tabMenuData.lunarDay+tabMenuData.dayText));
	}
}

function makeWeekTabMenu(tabMenuData) {
	jQuery('.leftArea').empty();
	jQuery('.rightArea').empty();
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('year').text(tabMenuData.firstYear+tabMenuData.yearText+' '));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('prev').attr('href','javascript:'+tabMenuData.prevLink));
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('week_num').text(tabMenuData.firstMonthText+' '+tabMenuData.firstDay+tabMenuData.dayText+' ~ '+tabMenuData.lastMonthText+' '+tabMenuData.lastDay+tabMenuData.dayText));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('next').attr('href','javascript:'+tabMenuData.nextLink));
	if (installLocale != 'jp') {
		jQuery('.rightArea').append(jQuery('<span></span>').text('('+tabMenuData.lunarText+') '+tabMenuData.firstLunarYear+tabMenuData.yearText+' '+
				tabMenuData.firstLunarMonthText+' '+tabMenuData.firstLunarDay+tabMenuData.dayText+' ~ '+
				tabMenuData.lastLunarMonthText+' '+tabMenuData.lastLunarDay+tabMenuData.dayText
				)
		);
	}
}

function makeDayTabMenu(tabMenuData) {
	var dayofWeek;
	
	if(tabMenuData.dayOfWeek == 1)
		dayofWeek = schedulerMsg.scheduler_date_sunday;
	else if (tabMenuData.dayOfWeek == 2)
		dayofWeek = schedulerMsg.scheduler_date_monday;
	else if (tabMenuData.dayOfWeek == 3)
		dayofWeek = schedulerMsg.scheduler_date_tuesday;
	else if (tabMenuData.dayOfWeek == 4)
		dayofWeek = schedulerMsg.scheduler_date_wednesday;
	else if (tabMenuData.dayOfWeek == 5)
		dayofWeek = schedulerMsg.scheduler_date_thursday;
	else if (tabMenuData.dayOfWeek == 6)
		dayofWeek = schedulerMsg.scheduler_date_friday;
	else if (tabMenuData.dayOfWeek == 7)
		dayofWeek = schedulerMsg.scheduler_date_saturday;

	jQuery('.leftArea').empty();
	jQuery('.rightArea').empty();
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('year').text(tabMenuData.year+tabMenuData.yearText+' '+tabMenuData.monthText+' ('+dayofWeek+')'));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('prev').attr('href','javascript:'+tabMenuData.prevLink));
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_num').text(tabMenuData.day));
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_text').text(tabMenuData.dayText));
	jQuery('.leftArea').append(jQuery('<a></a>').addClass('next').attr('href','javascript:'+tabMenuData.nextLink));
	if (installLocale != 'jp') {
		jQuery('.leftArea').append(jQuery('<span></span>').addClass('lunar').text('('+tabMenuData.lunarText+') '+tabMenuData.lunarMonthText+' '+tabMenuData.lunarDay+tabMenuData.dayText));
	}
	if (tabMenuData.dayOfWeek == 1) {
		jQuery(".month_num").addClass('TM_F_POINT3');
		jQuery(".month_text").addClass('TM_F_POINT3');
	}
	else if (tabMenuData.dayOfWeek == 7) {
		jQuery(".month_num").addClass('TM_F_POINT4');
		jQuery(".month_text").addClass('TM_F_POINT4');
	}
}

function makeSearchTabMenu(tabMenuData) {
	jQuery('.leftArea').empty();
	jQuery('.rightArea').empty();
	var keyWord = jQuery('#keyWord').val();
	if (trim(keyWord) == "") {
		jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_text').text(tabMenuData.resultall));
	}
	else {
		jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_num TM_F_POINT4').text('\''+keyWord+'\''));
		jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_text').text(tabMenuData.result));
	}
}

function makeOutlookTabMenu(text) {
	jQuery('.leftArea').empty();
	jQuery('.rightArea').empty();
	jQuery('.leftArea').append(jQuery('<span></span>').addClass('month_text').text(text));
}

function listPage() {
	var param;
	schedulerControl.loadMonthSchedule(param);
}

function movePage(num) {
	var searchType = jQuery('#searchType').val();
	var keyWord = jQuery('#keyWord').val();
	schedulerDataControl.searchSchedule(searchType, keyWord, num);
}

function loadMonthSchedule(year, month) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}

	if (monthLoading) {
		return;
	}
	schedulerDataControl.getMonthInfo(year, month);	
}

function loadWeekSchedule(year, month, day) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	if (!day) {
		day = "0";
	}

	if (weekLoading) {
		return;
	}
	
	schedulerDataControl.getWeekInfo(year, month, day);	
}

function loadDaySchedule(year, month, day) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	if (!day) {
		day = "0";
	}
	
	if (dayLoading) {
		return;
	}

	schedulerDataControl.getDayInfo(year, month, day);	
}

function loadCalendarSchedule(year, month) {
	if (!year) {
		year="0";
	}
	if (!month) {
		month = "0";
	}
	schedulerDataControl.getCalendarInfo(year, month);	
}

function dayScheduleRender(dayInfo) {
	var dayScheduleData = {
		hiddenFormData:"",
		headerTable:"<div class='TM_listWrapper'><table class='dayScheduler_list' cellpadding='0' cellspacing='0'>",
		scheduleRepeatHeader:"<tr><td class='numTD'>{anniversary}</td>",
		scheduleRepeat:"<td class='schedulerLineTd {today}' onclick=\"viewInputBox('{date}')\"><table class='schedulerTable' cellpadding='0' cellspacing='0'>",
		scheduleHoliday:"<tr><td id='date_td_{date}' class='schedulerDateTd' style='width:100%'><div class='holidayWrap'><span id='scheduler_holiday_{date}'></span></div><div id='schedule_day_all_{date}'><div id='plan_all_{date}' class='planAll' style='margin-top:25px'></div></div></td><td align='right' id='plan_td_{date}' height='30px'><a id='plan_count_{date}' count='0' href='javascript:;' class='planCount' onclick=\"openPlanAll('{date}');event.cancelBubble=true\"></a></td></tr>",
		scheduleBox:"<tr><td colspan='2' class='scheduleDayTd'><div class='scheduleDiv' id='schedule_day_{date}'><div id='plan_{date}_{count}' status='n' class='plan' ids='{date}'></div></div></td></tr>",
		scheduleTail:"</table></td></tr>",
		scheduleTimeRepeat:"<tr><td class='timeTD' nowrap>{timeFormat}</td>",
		scheduleTimeBox:"<td class='{timeClass} {today}'><table class='schedulerTable' cellpadding='0' cellspacing='0'><tr><td class='schedulerProcessDate' onclick=\"viewInputBox('{date}','{ftime}')\"><div class='progressScheduleDiv' id='progress_{date}_{time}' count='0' index='0' total='' pwidth=''></div></td></tr></table></td>",
		scheduleTimeRepeatTail:"</tr>",
		bodyTableTail:"</table></div>"
	};
	
	var stringBuffer = new StringBuffer();
	stringBuffer.append(dayScheduleData.hiddenFormData);
	stringBuffer.append(dayScheduleData.headerTable);
	var headerInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var scheduleRepeatHeader = "";
	var anniversaryValue = "";
	var thisdayStr = dayInfo.thisdayStr;
	var todayStr = dayInfo.todayStr;
	var scheduleRepeat = "";
	var repeatToday = "";
	var scheduleHoliday = "";
	
	for (var i=0; i<6; i++) {
		scheduleRepeatHeader = dayScheduleData.scheduleRepeatHeader;
		
		anniversaryValue = i;
		if (i == 0) {
			anniversaryValue = schedulerMsg.scheduler_anniversary;
		}
		
		scheduleRepeatHeader = replaceAll(scheduleRepeatHeader,"{anniversary}",anniversaryValue);
		stringBuffer.append(scheduleRepeatHeader);
		
		scheduleRepeat = dayScheduleData.scheduleRepeat;
		
		repeatToday = "";
		if (todayStr == thisdayStr) {
			repeatToday = "today";
		}
		scheduleRepeat = replaceAll(scheduleRepeat,"{today}",repeatToday);
		scheduleRepeat = replaceAll(scheduleRepeat,"{date}",thisdayStr);
		stringBuffer.append(scheduleRepeat);
		
		if (i == 0) {
			scheduleHoliday = dayScheduleData.scheduleHoliday;
			scheduleHoliday = replaceAll(scheduleHoliday,"{date}",thisdayStr);
			stringBuffer.append(scheduleHoliday);
		}
		else {
			scheduleBox = dayScheduleData.scheduleBox;
			scheduleBox = replaceAll(scheduleBox,"{date}",thisdayStr);
			scheduleBox = replaceAll(scheduleBox,"{count}",i);
			stringBuffer.append(scheduleBox);
		}
		stringBuffer.append(dayScheduleData.scheduleTail);
	}
	var scheduleInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var timeFormat = "";
	var scheduleTimeRepeat = "";
	var scheduleTimeBox = "";
	for (var l=0; l<24; l++) {
		timeFormat = l-12;
		if (l <= 12) {
			timeFormat = l;
		}
		
		if (l < 12) {
			timeFormat += " "+schedulerMsg.scheduler_am;
		}
		else {
			timeFormat += " "+schedulerMsg.scheduler_pm;
		}
		
		var timeArray = {time:[[timeFormat,"weekDottedTD","00"],["30"+schedulerMsg.scheduler_min,"schedulerProcessTd","30"]]};

		for (var timeIndex=0; timeIndex<timeArray.time.length; timeIndex++) {
			scheduleTimeRepeat = dayScheduleData.scheduleTimeRepeat;
			scheduleTimeRepeat = replaceAll(scheduleTimeRepeat,"{timeFormat}",timeArray.time[timeIndex][0]);
			stringBuffer.append(scheduleTimeRepeat);
			
			scheduleTimeBox = dayScheduleData.scheduleTimeBox;
			scheduleTimeBox = replaceAll(scheduleTimeBox,"{timeClass}",timeArray.time[timeIndex][1]);
			
			repeatToday = "";
			if (todayStr == thisdayStr) {
				repeatToday = "today";
			}
			scheduleTimeBox = replaceAll(scheduleTimeBox,"{today}",repeatToday);
			scheduleTimeBox = replaceAll(scheduleTimeBox,"{date}",thisdayStr);
			scheduleTimeBox = replaceAll(scheduleTimeBox,"{time}",l+timeArray.time[timeIndex][2]);
			scheduleTimeBox = replaceAll(scheduleTimeBox,"{ftime}",l+":"+timeArray.time[timeIndex][2]);
			
			stringBuffer.append(scheduleTimeBox);
	
		}
		stringBuffer.append(dayScheduleData.scheduleTimeRepeatTail);	
	}
	var timeInfo = stringBuffer.toString();
	stringBuffer.destroy();
	var bodyTableTailInfo = dayScheduleData.bodyTableTail;
	
	jQuery("#body1").html(headerInfo+scheduleInfo+timeInfo+bodyTableTailInfo);
	jQuery("#body1").show();
	jQuery("#body2").hide();
	jQuery("#body2").empty();
	
	jQuery("#myMode").val("day");
	jQuery("#myYear").val(dayInfo.thisYear);
	jQuery("#myMonth").val(dayInfo.thisMonth);
	jQuery("#myDay").val(dayInfo.thisDay);
	jQuery("#thisDate").val(todayStr);
	jQuery("#dateList").val(thisdayStr);
	jQuery("#currentDate").val(thisdayStr);

	dayScheduleSetting(dayInfo);
}

function dayScheduleSetting(dayInfo) {
	var lunar = dayInfo.lunar+"";
	var lunarMonth = lunar.substring(4,6);
	if (lunar.charAt(4) == "0") {
		lunarMonth = lunar.substring(5,6);
	} 

	var tabMenuData = {
			year:dayInfo.thisYear, yearText:schedulerMsg.scheduler_year, 
	       	month:dayInfo.thisMonth, monthText:getMonthStr(dayInfo.thisMonth),
	       	day:dayInfo.thisDay, dayText:schedulerMsg.scheduler_day,
	   	  	lunarMonth:lunar.substring(4,6), lunarDay:lunar.substring(6,8), lunarText:schedulerMsg.scheduler_lunar,
			lunarMonthText:getMonthStr(parseInt(lunarMonth,10)),
	   	  	prevLink:'loadDaySchedule("'+dayInfo.prevYear+'","'+dayInfo.prevMonth+'","'+dayInfo.prevDay+'")',
	   		nextLink:'loadDaySchedule("'+dayInfo.nextYear+'","'+dayInfo.nextMonth+'","'+dayInfo.nextDay+'")',
	   		dayOfWeek:dayInfo.thisDayOfWeek
	   	   	};
	makeDayTabMenu(tabMenuData);
	readHoliday(dayInfo.thisYear, dayInfo.thisYear+'0101');
	setSchedulerTab('0');
	jQuery("#currentType").val("");
	setSchedulerInfo(schedulerMsg.scheduler_menu_day);
	schedulerDataControl.setTimeList(makeTimeArray());
	schedulerDataControl.readDayScheduleList(dayInfo.thisYear,dayInfo.thisMonth,dayInfo.thisDay);
	jQuery("#pageNavi").empty();
}

function weekScheduleRender(weekInfo) {
	var weekdateList = weekInfo.dateList;

	var weekScheduleData = {
		hiddenFormData:"",
		headerTable:"<div class='TM_listWrapper'><table class='scheduler_week_TB TM_scheduler_basicTB' cellpadding='0' cellspacing='0'><tr><td valign=top>",
		bodyTable:"<table class='TM_scheduler_basicTB' cellpadding='0' cellspacing='0'><tr><td colspan='2'><table width='100%' id='scheduler_week' cellpadding='0' cellspacing='0'><tr><td class='dateTd' nowrap></td>",
		bodyRepeat:"<td class='dateTd {dateType}'>{day} ({dayType})</td>",
		bodyRepeatTail:"</tr>",
		scheduleRepeatHeader:"<tr><td class='numTD'>{anniversary}</td>",
		scheduleRepeat:"<td class='schedulerLineTd {today}' onclick=\"viewInputBox('{date}')\"><table class='schedulerTable' cellpadding='0' cellspacing='0'>",
		scheduleHoliday:"<tr><td id='date_td_{date}' class='schedulerDateTd' style='width:100%'><div class='holidayWrap'><span id='scheduler_holiday_{date}'></span></div><div id='plan_all_{date}' class='planAll'></div></td><td align='right' id='plan_td_{date}'><a id='plan_count_{date}' count='0' href='javascript:;' class='planCount' onclick=\"openPlanAll('{date}');event.cancelBubble=true\"></a></td></tr>",
		scheduleBox:"<tr><td colspan='2' class='scheduleWeekTd' align='center'><div class='scheduleDiv' id='schedule_week_{date}'><div id='plan_{date}_{count}' status='n' class='plan' ids='{date}' onmouseover=\"acceptDragAndDrop('plan_{date}_{count}','week')\"></div></div></td></tr>",
		scheduleTail:"</table></td>",
		scheduleRepeatTail:"</tr>",
		//scheduleTimeRepeat:"<tr><td class='timeTD' nowrap>{timeFormat}</td>",
		//scheduleTimeBox:"<td class='{timeClass} {today}'><table class='schedulerTable'><tr><td class='schedulerProcessDate' onclick=\"viewInputBox('{date}','{ftime}')\"><div class='progressScheduleDiv' id='progress_{date}_{time}' count='0' index='0'></div></td></tr></table></td>",
		timeBox1:"<tr><td class='timeTD'>{timeFormat}</td><td class='schedulerProcessTd {today0}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{0}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{0}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today1}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{1}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{1}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today2}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{2}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{2}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today3}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{3}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{3}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today4}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{4}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{4}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today5}'><table class='schedulerTable' cellpadding='0' cellspacing='0'><tbody><tr><td onclick=\"viewInputBox('{5}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{5}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today6}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{6}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{6}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td></tr>",
		timeBox2:"<tr><td nowrap='' class='timeTD'>30"+schedulerMsg.scheduler_min+"</td><td class='schedulerProcessTd {today0}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{0}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{0}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today1}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{1}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{1}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today2}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{2}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{2}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today3}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{3}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{3}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today4}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{4}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{4}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today5}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{5}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{5}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today6}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"viewInputBox('{6}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{6}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td></tr>",
		scheduleTimeRepeatTail:"</tr>",
		bodyTableTail:"</table></td></tr></table></td></tr></table></div>"
	};
	
	var stringBuffer = new StringBuffer();
	stringBuffer.append(weekScheduleData.hiddenFormData);
	stringBuffer.append(weekScheduleData.headerTable);
	stringBuffer.append(weekScheduleData.bodyTable);
	
	var headerInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var thisdayMonth = "";
	var weekListMonth = "";
	var bodyRepeat = "";
	var day = "";
	var dayType = "";
	var dateType = "";
	for (var i=0; i<weekdateList.length; i++) {
		bodyRepeat = weekScheduleData.bodyRepeat;
		thisdayMonth = weekInfo.thisdayStr.substring(4,6);
		weekListMonth = weekdateList[i].date.substring(4,6);
		
		dateType = "";
		
		if ((i % 7) == 0) {
			dateType = "TM_F_POINT";
		} else if (i % 7 == 6) {
			dateType = "TM_F_POINT2";
		}
	
		bodyRepeat = replaceAll(bodyRepeat,"{dateType}",dateType);
		
		day = weekdateList[i].date.substring(4,6);
		
		if (weekdateList[i].date.charAt(4) == "0") {
			day = weekdateList[i].date.substring(5,6);
		} 
		
		day+= " / ";
			
		if (weekdateList[i].date.charAt(6) == "0") {
			day += weekdateList[i].date.substring(7,8);
		} 
		else {
			day += weekdateList[i].date.substring(6,8);
		}
		bodyRepeat = replaceAll(bodyRepeat,"{day}",day);
		
		switch (i) {
			case 0 : dayType = schedulerMsg.scheduler_date_0;break;
			case 1 : dayType = schedulerMsg.scheduler_date_1;break;
			case 2 : dayType = schedulerMsg.scheduler_date_2;break;
			case 3 : dayType = schedulerMsg.scheduler_date_3;break;
			case 4 : dayType = schedulerMsg.scheduler_date_4;break;
			case 5 : dayType = schedulerMsg.scheduler_date_5;break;
			case 6 : dayType = schedulerMsg.scheduler_date_6;break;
		}
		bodyRepeat = replaceAll(bodyRepeat,"{dayType}",dayType);
		stringBuffer.append(bodyRepeat);
	}
	stringBuffer.append(weekScheduleData.bodyRepeatTail);
	var bodyRepeatInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var scheduleRepeatHeader = "";
	var anniversaryValue = "";
	var scheduleRepeat = "";
	var weekListDate = "";
	var scheduleHoliday = "";
	var scheduleBox = "";
	var repeatToday = "";
	var timebox1 = "";
	var timebox2 = "";
	for (var j=0; j<6; j++) {
		scheduleRepeatHeader = weekScheduleData.scheduleRepeatHeader;
		
		anniversaryValue = j;
		if (j == 0) {
			anniversaryValue = schedulerMsg.scheduler_anniversary;
		}
		
		scheduleRepeatHeader = replaceAll(scheduleRepeatHeader,"{anniversary}",anniversaryValue);
		stringBuffer.append(scheduleRepeatHeader);
			
		for (k=0; k<weekdateList.length; k++) {
			weekListDate = weekdateList[k].date;
			scheduleRepeat = weekScheduleData.scheduleRepeat;
			
			repeatToday = "";
			if (weekInfo.todayStr == weekListDate) {
				repeatToday = "today";
			}
			scheduleRepeat = replaceAll(scheduleRepeat,"{today}",repeatToday);
			scheduleRepeat = replaceAll(scheduleRepeat,"{date}",weekListDate);
			stringBuffer.append(scheduleRepeat);
			
			if (j == 0) {
				scheduleHoliday = weekScheduleData.scheduleHoliday;
				scheduleHoliday = replaceAll(scheduleHoliday,"{date}",weekListDate);
				stringBuffer.append(scheduleHoliday);
			}
			else {
				scheduleBox = weekScheduleData.scheduleBox;
				scheduleBox = replaceAll(scheduleBox,"{date}",weekListDate);
				scheduleBox = replaceAll(scheduleBox,"{count}",j);
				stringBuffer.append(scheduleBox);
			}
			stringBuffer.append(weekScheduleData.scheduleTail);
		}
		stringBuffer.append(weekScheduleData.scheduleRepeatTail);
	}
	
	var scheduleInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var timeFormat = "";
	var scheduleTimeRepeat = "";
	var scheduleTimeBox = "";
	var timeInfoTmp = "";
	for (var l=0; l<24; l++) {
		timeFormat = l-12;
		if (l <= 12) {
			timeFormat = l;
		}
		
		if (l < 12) {
			timeFormat += " "+schedulerMsg.scheduler_am;
		}
		else {
			timeFormat += " "+schedulerMsg.scheduler_pm;
		}

		timeInfoTmp = weekScheduleData.timeBox1+weekScheduleData.timeBox2;
		timeInfoTmp = replaceAll(timeInfoTmp,"{timeFormat}",timeFormat);
		timeInfoTmp = replaceAll(timeInfoTmp,"{00}",l);

		stringBuffer.append(timeInfoTmp);
	}

	var timeInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	for (var m=0; m<weekdateList.length; m++) {
		timeInfo = replaceAll(timeInfo,"{"+m+"}",weekdateList[m].date);
		repeatToday = "";
		if (weekInfo.todayStr == weekdateList[m].date) {
			repeatToday = "today";
		}
		timeInfo = replaceAll(timeInfo,"{today"+m+"}",repeatToday);
	}
	
	var bodyTableTailInfo = weekScheduleData.bodyTableTail;
	
	jQuery("#body1").html(headerInfo+bodyRepeatInfo+scheduleInfo+timeInfo+bodyTableTailInfo);
	jQuery("#body1").show();
	jQuery("#body2").hide();
	jQuery("#body2").empty();
	jQuery("#body3").text("<!--"+headerInfo+bodyRepeatInfo+scheduleInfo+timeInfo+bodyTableTailInfo+"-->");
	
	jQuery("#myMode").val("week");
	jQuery("#myYear").val(weekInfo.thisYear);
	jQuery("#myMonth").val(weekInfo.thisMonth);
	jQuery("#myDay").val(weekInfo.thisDay);
	jQuery("#thisDate").val(weekInfo.todayStr);
	jQuery("#currentDate").val(weekInfo.thisdayStr);
	
	weekScheduleSetting(weekInfo);
}

function weekScheduleSetting(weekInfo) {
	var firstLunar = weekInfo.firstLunar+"";
	var lastLunar = weekInfo.lastLunar+"";
	
	var firstLunarMonth = firstLunar.substring(4,6);
	if (firstLunar.charAt(4) == "0") {
		firstLunarMonth = firstLunar.substring(5,6);
	} 
	
	var lastLunarMonth = lastLunar.substring(4,6);
	if (lastLunar.charAt(4) == "0") {
		lastLunarMonth = lastLunar.substring(5,6);
	} 

	var tabMenuData = {firstYear:weekInfo.firstYear, firstMonth:weekInfo.firstMonth, firstDay:weekInfo.firstDay,
			lastYear:weekInfo.lastYear, lastMonth:weekInfo.lastMonth, lastDay:weekInfo.lastDay,
			yearText:schedulerMsg.scheduler_year, 
	       	firstMonthText:getMonthStr(weekInfo.firstMonth), 
	       	dayText:schedulerMsg.scheduler_day,
	       	firstLunarYear:firstLunar.substring(0,4), firstLunarMonth:firstLunar.substring(4,6), firstLunarDay:firstLunar.substring(6,8),
	       	lastLunarYear:lastLunar.substring(0,4), lastLunarMonth:lastLunar.substring(4,6), lastLunarDay:lastLunar.substring(6,8),
	       	firstLunarMonthText:getMonthStr(parseInt(firstLunarMonth,10)),
	       	lastLunarMonthText:getMonthStr(parseInt(lastLunarMonth,10)),
	   	  	lunarText:schedulerMsg.scheduler_lunar,
	   	 	lastMonthText:getMonthStr(weekInfo.lastMonth),
	   	  	prevLink:'loadWeekSchedule("'+weekInfo.prevYear+'","'+weekInfo.prevMonth+'","'+weekInfo.prevDay+'")',
	   		nextLink:'loadWeekSchedule("'+weekInfo.nextYear+'","'+weekInfo.nextMonth+'","'+weekInfo.nextDay+'")'
	};
	makeWeekTabMenu(tabMenuData);
	readHoliday(weekInfo.thisYear, weekInfo.dateList);
	setSchedulerTab('1');
	jQuery("#currentType").val("");
	setSchedulerInfo(schedulerMsg.scheduler_menu_week);
	schedulerDataControl.setWeekDateList(weekInfo.dateList);
	schedulerDataControl.setTimeList(makeTimeArray());
	schedulerDataControl.readWeekScheduleList(weekInfo.thisYear,weekInfo.thisMonth,weekInfo.thisDay);

	jQuery("#pageNavi").empty();
}

function monthScheduleRender(monthInfo) {
	var monthScheduleData = {
		hiddenFormData:"",
		headerTable:"<div class='TM_listWrapper'><table cellpadding='0' cellspacing='0' class='schedulerMonth'><tr><td class='TM_F_POINT dateTd plan_cell'>"+schedulerMsg.scheduler_date_sunday+"</td><td class='dateTd plan_cell'>"+schedulerMsg.scheduler_date_monday+"</td><td class='dateTd plan_cell'>"+schedulerMsg.scheduler_date_tuesday+"</td><td class='dateTd plan_cell'>"+schedulerMsg.scheduler_date_wednesday+"</td><td class='dateTd plan_cell'>"+schedulerMsg.scheduler_date_thursday+"</td><td class='dateTd plan_cell'>"+schedulerMsg.scheduler_date_friday+"</td><td class='TM_F_POINT2 dateTd plan_cell'>"+schedulerMsg.scheduler_date_saturday+"</td></tr></table>",
		bodyTable:"<table cellpadding='0' cellspacing='0' class='schedulerMonth sub_schedulerMonth'>",
		bodyRepeat:"{tr}<td class='schedulerLineTd plan_cell {today}'><table cellpadding='0' cellspacing='0' class='schedulerTable' onclick=\"viewInputBox('{date}')\"><tr><td id='date_td_{date}' class='schedulerDateTd {dateType}' style='width:85%'><div class='holidayWrap'><span>{day}</span><span id='scheduler_holiday_{date}' class='schedulerHoliday'></span></div><div id='schedule_month_all_{date}'><div id='plan_all_{date}' class='planAll'></div></div></td><td align='right' id='plan_td_{date}' style='width:15%'><a id='plan_count_{date}' count='0' href='javascript:;' class='planCount' onclick=\"openPlanAll('{date}');event.cancelBubble=true\"></a></td></tr><tr><td colspan='2' class='scheduleMonthTd'><div class='scheduleDiv' id='schedule_month_{date}'><div id='plan_{date}_1' status='n' class='plan' ids='{date}' onmouseover=\"acceptDragAndDrop('plan_{date}_1','month')\"></div><div id='plan_{date}_2' status='n' class='plan' ids='{date}' onmouseover=\"acceptDragAndDrop('plan_{date}_2','month')\"></div><div id='plan_{date}_3' status='n' class='plan' ids='{date}' onmouseover=\"acceptDragAndDrop('plan_{date}_3','month')\"></div><div id='plan_{date}_4' status='n' class='plan' ids='{date}' onmouseover=\"acceptDragAndDrop('plan_{date}_4','month')\"></div></div></td></tr></table></td>{endTr}",
		bodyEndTable:"</table></div>"
	};
	
	var stringBuffer = new StringBuffer();
	stringBuffer.append(monthScheduleData.hiddenFormData);
	stringBuffer.append(monthScheduleData.headerTable);

	var headerInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var monthdateList = monthInfo.dateList;
	var bodyHeader = monthScheduleData.bodyTable;
	var bodyContent = "";
	for (i=0; i<monthdateList.length; i++) {
		var body = monthScheduleData.bodyRepeat;

		body = replaceAll(body,"{date}",monthdateList[i].date);

		if ((i % 7) == 0) {
			body = replaceAll(body,"{tr}","<tr>");
		} else {
			body = replaceAll(body,"{tr}","");
		}
		if (monthInfo.todayStr == monthdateList[i].date) {
			body = replaceAll(body,"{today}","today");
		} else {
			body = replaceAll(body,"{today}","");
		}
		if (monthInfo.thisdayStr.substring(4,6) != monthdateList[i].date.substring(4,6)) {
			body = replaceAll(body,"{dateType}","prevNextTd");
		} else if ((monthInfo.thisdayStr.substring(4,6) == monthdateList[i].date.substring(4,6))) {
			if ((i % 7) == 0) {
				body = replaceAll(body,"{dateType}","TM_F_POINT3");
			} else if (i % 7 == 6) {
				body = replaceAll(body,"{dateType}","TM_F_POINT4");
			} else {
				body = replaceAll(body,"{dateType}","");
			}
		}
		if (monthdateList[i].date.charAt(6) == "0") {
			body = replaceAll(body,"{day}",monthdateList[i].date.substring(7,8));
		} 
		else {
			body = replaceAll(body,"{day}",monthdateList[i].date.substring(6,8));
		}
		
		if (((i+1) % 7) == 0) {
			body = replaceAll(body,"{endTr}","</tr>");
			stringBuffer.append(body);
			bodyContent += stringBuffer.toString();
			stringBuffer.destroy();
		} else {
			body = replaceAll(body,"{endTr}","");
			stringBuffer.append(body);
		}
	}
	var bodyTail = monthScheduleData.bodyEndTable;

	jQuery("#body1").html(headerInfo+bodyHeader+bodyContent+bodyTail);
	jQuery("#body2").hide();
	jQuery("#body1").show();
	jQuery("#body3").text("<!--"+headerInfo+bodyHeader+bodyContent+bodyTail+"-->");

	jQuery("#myMode").val("month");
	jQuery("#myYear").val(monthInfo.thisYear);
	jQuery("#myMonth").val(monthInfo.thisMonth);
	jQuery("#thisDate").val(monthInfo.todayStr);
	jQuery("#currentDate").val(monthInfo.thisdayStr);
	
	monthScheduleSetting(monthInfo);
}

function monthScheduleSetting(monthInfo) {
	var lunar = monthInfo.lunar+"";
	var lunarMonth = lunar.substring(4,6);
	if (lunar.charAt(4) == "0") {
		lunarMonth = lunar.substring(5,6);
	}
	
	var monthTextString = schedulerMsg.scheduler_month;

	if (LOCALE == 'en') {
		monthTextString = getMonthStr(monthInfo.thisMonth);
	}

	var tabMenuData = {year:monthInfo.thisYear, yearText:schedulerMsg.scheduler_year, 
           	month:monthInfo.thisMonth, monthText:monthTextString,
           	day:'', dayText:schedulerMsg.scheduler_day,
           	lunarMonth:lunar.substring(4,6), lunarDay:lunar.substring(6,8), lunarText:schedulerMsg.scheduler_lunar,
       	  	prevLink:'loadMonthSchedule("'+monthInfo.prevYear+'","'+monthInfo.prevMonth+'")', lunarMonthText:getMonthStr(parseInt(lunarMonth,10)),
       		nextLink:'loadMonthSchedule("'+monthInfo.nextYear+'","'+monthInfo.nextMonth+'")'
       		};
	makeMonthTabMenu(tabMenuData);
	readHoliday(monthInfo.thisYear, monthInfo.dateList);
	setSchedulerTab('2');
	jQuery("#currentType").val("");
	setSchedulerInfo(schedulerMsg.scheduler_menu_month);
	schedulerDataControl.setMonthDateList(monthInfo.dateList);
	schedulerDataControl.readMonthScheduleList(monthInfo.thisYear,monthInfo.thisMonth);
	
	jQuery("#pageNavi").empty();
}

function acceptDragAndDrop(id, type) {
	jQuery("#"+id).draggable({
		cursor: 'move',
		cursorAt: { top: 10, left: -15 },
		opacity: 0.7, 
		helper: function(event) {			
			var id = jQuery(this).attr('id');		
			var dragText;
			dragText = escape_tag(jQuery("#"+id+" .schDiv").text());			
			var dragHelper = '<div class="dragHelper">'+dragText+'</div>';			
			return jQuery(dragHelper);
		},
		appendTo: 'body',
		zIndex:10000
	});
	jQuery(".scheduleDiv").droppable({
		accept: ".plan",
		
		//activeClass: 'ui-state-hover',
		//hoverClass: 'drag-over',
		drop: function(ev, ui) {
			dropId = jQuery(this).attr('id');
			if (ui.draggable.attr('repeat') == 'on') {
				alert(schedulerMsg.scheduler_repeat_donot_move);
				return;
			}
			
			if (ui.draggable.attr('share') == 'on') {
				alert(schedulerMsg.scheduler_share_alert_donot_move);
				return;
			}
			
			if (ui.draggable.attr('asset') == 'on') {
				alert(schedulerMsg.scheduler_asset_019);
				return;
			}
			
			scheduleDnD(type, ui.draggable, dropId);
		}
	});
}

function calendarRender(calendarInfo) {
	var calendarData = {
		headerDiv:"<div class='calendar_s_date'>&nbsp;<a href='javascript:;' class='prev' onclick=\"gotoCalendar('{prevYear}','{prevMonth}')\">prev</a><span class='date'>{thisYear}"+schedulerMsg.scheduler_year+" {thisMonth}</span><a href='javascript:;' class='next' onclick=\"gotoCalendar('{nextYear}','{nextMonth}')\">next</a></div>",
		headTable:"<table cellpadding='0' cellspacing='0' class='calendar_s_day'><tr class='title'><td class='sunday'>"+schedulerMsg.scheduler_date_0+"</td><td>"+schedulerMsg.scheduler_date_1+"</td><td>"+schedulerMsg.scheduler_date_2+"</td><td>"+schedulerMsg.scheduler_date_3+"</td><td>"+schedulerMsg.scheduler_date_4+"</td><td>"+schedulerMsg.scheduler_date_5+"</td><td class='saturay'>"+schedulerMsg.scheduler_date_6+"</td></tr></table>",
		bodyTable:"<table cellpadding='0' cellspacing='0' class='calendar_s_day'>",
		bodyRepeat:"{tr}<td id='calendar_date_{date}' class='{today} {dateType}' count='0'><a href='javascript:;' onclick=\"gotoCalendarDay('{monthYear}','{monthMonth}','{monthDay}')\">{day}</a></td>{endTr}",
		bodyEndTable:"</table>"
	};
	var stringBuffer = new StringBuffer();
	var head = calendarData.headerDiv;
	head = replaceAll(head,"{prevYear}",calendarInfo.prevYear);
	head = replaceAll(head,"{prevMonth}",calendarInfo.prevMonth);
	head = replaceAll(head,"{thisYear}",calendarInfo.thisYear);
	
	head = replaceAll(head,"{thisMonth}",getMonthStr(calendarInfo.thisMonth));
	head = replaceAll(head,"{nextYear}",calendarInfo.nextYear);
	head = replaceAll(head,"{nextMonth}",calendarInfo.nextMonth);

	stringBuffer.append(head);
	stringBuffer.append(calendarData.headTable);
	
	var headerInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var calendardateList = calendarInfo.dateList;
	var bodyHeader = calendarData.bodyTable;
	var bodyContent = "";
	for (i=0; i<calendardateList.length; i++) {
		var body = calendarData.bodyRepeat;

		body = replaceAll(body,"{date}",calendardateList[i].date);
		body = replaceAll(body,"{monthYear}",calendardateList[i].date.substring(0,4));
		body = replaceAll(body,"{monthMonth}",calendardateList[i].date.substring(4,6));
		body = replaceAll(body,"{monthDay}",calendardateList[i].date.substring(6,8));

		if ((i % 7) == 0) {
			body = replaceAll(body,"{tr}","<tr>");
		} else {
			body = replaceAll(body,"{tr}","");
		}
		if (calendarInfo.todayStr == calendardateList[i].date) {
			body = replaceAll(body,"{today}","today");
		} else {
			body = replaceAll(body,"{today}","");
		}
		if (calendarInfo.thisdayStr.substring(4,6) != calendardateList[i].date.substring(4,6)) {
			body = replaceAll(body,"{dateType}","prevNext");
		} else if ((calendarInfo.thisdayStr.substring(4,6) == calendardateList[i].date.substring(4,6))) {
			if ((i % 7) == 0) {
				body = replaceAll(body,"{dateType}","sunday");
			} else if (i % 7 == 6) {
				body = replaceAll(body,"{dateType}","saturay");
			} else {
				body = replaceAll(body,"{dateType}","");
			}
		}
		if (calendardateList[i].date.charAt(6) == "0") {
			body = replaceAll(body,"{day}",calendardateList[i].date.substring(7,8));
		} 
		else {
			body = replaceAll(body,"{day}",calendardateList[i].date.substring(6,8));
		}
		
		if (((i+1) % 7) == 0) {
			body = replaceAll(body,"{endTr}","</tr>");
			stringBuffer.append(body);
			bodyContent += stringBuffer.toString();
			stringBuffer.destroy();
		} else {
			body = replaceAll(body,"{endTr}","");
			stringBuffer.append(body);
		}
	}
	var bodyTail = calendarData.bodyEndTable;
	jQuery("#menu_carendar").empty();
	$("menu_carendar").innerHTML = headerInfo+bodyHeader+bodyContent+bodyTail;
	schedulerDataControl.setCalendarDateList(calendardateList);
	schedulerDataControl.readCalendarScheduleList(calendarInfo.thisYear,calendarInfo.thisMonth);
}

function getMonthStr(month) {
	var thisMonth = "";
	switch (month) {
		case 1 : thisMonth = schedulerMsg.scheduler_month_1;break;
		case 2 : thisMonth = schedulerMsg.scheduler_month_2;break;
		case 3 : thisMonth = schedulerMsg.scheduler_month_3;break;
		case 4 : thisMonth = schedulerMsg.scheduler_month_4;break;
		case 5 : thisMonth = schedulerMsg.scheduler_month_5;break;
		case 6 : thisMonth = schedulerMsg.scheduler_month_6;break;
		case 7 : thisMonth = schedulerMsg.scheduler_month_7;break;
		case 8 : thisMonth = schedulerMsg.scheduler_month_8;break;
		case 9 : thisMonth = schedulerMsg.scheduler_month_9;break;
		case 10 : thisMonth = schedulerMsg.scheduler_month_10;break;
		case 11 : thisMonth = schedulerMsg.scheduler_month_11;break;
		case 12 : thisMonth = schedulerMsg.scheduler_month_12;break;
	}
	
	return thisMonth;
}

function showHideContent() {
	if (jQuery("#body1").css("display") == "none") {
		jQuery("#body2").hide();
		jQuery("#body1").show();
	} else {
		jQuery("#body1").hide();
		jQuery("#body2").show();
	}
}

function GetStyle() {
	var font = (LOCALE != "jp")?"Dotum, Arial, Verdana, Sans-Serif":"MS PGothic,Osaka, Sans-serif";
	var pstr = "<style type='text/css'>\n"
		+ ".TerraceMsg { font-size: 12px; font-family:"+font+
		";line-height:17px;}\n"
		+ ".Bold { font-weight: bold; }\n"
		+ "</style>";
	return pstr;
}

function checkYearRepeat() {
	var extDateMap = new HashMap();
	extDateMap.put("2","29");
	extDateMap.put("4","30");
	extDateMap.put("6","30");
	extDateMap.put("9","30");
	extDateMap.put("11","30");
	
	var month = Number(jQuery("#yearTerm1").val());
	var date = Number(jQuery("#yearTerm2").val());

	var extDate = extDateMap.get(month);
	if (extDate) {
		if (date > Number(extDate)) {
			alert(msgArgsReplace(schedulerMsg.scheduler_alert_repeat_year_dateover,[extDate]));
			jQuery("#yearTerm2Select").selectboxSetValue(extDate);
		}
	}
}