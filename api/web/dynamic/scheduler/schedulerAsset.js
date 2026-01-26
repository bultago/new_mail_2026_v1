var assetLoading = false;

var ScheduerAsset = Class.create({
	initialize: function(){
		this.weekDateList = null;
		this.timeList = null;
		this.categoryMap = null;
	},
	makeCategoryMap : function(categoryList) {
		var categoryMap = new Hash();
		if (categoryList && categoryList.length) {
			for ( var i = 0; i < categoryList.length; i++) {
				categoryMap.set(categoryList[i].categorySeq,{id:categoryList[i].categorySeq, name : categoryList[i].categoryName, assetList:categoryList[i].assetList});
			}
		}
		this.categoryMap = categoryMap;
	},
	getCategoryMap : function () {
		return this.categoryMap;
	},
	getCategoryInfo : function (categorySeq) {
		var info = this.categoryMap.get(categorySeq);
		return info;
	},
	setAssetDateList : function(weekDateList){
		this.weekDateList = weekDateList;
	},
	setTimeList : function(timeList){
		this.timeList = timeList;
	},
	readAssetCategoryList: function() {
		SchedulerAssetService.readAssetCategoryList(function(assetData) {
			drowAssetCategoryList(assetData);
		});
	},
	readAssetCategoryDescription: function(categorySeq) {
		SchedulerAssetService.readAssetCategoryDescription(categorySeq,function(descriptionData) {
			viewAssetCategoryDescription(descriptionData);
		});
	},
	readAssetSchedule : function (planSeq, startDate, endDate) {
		var _this = this;
		SchedulerAssetService.readAssetSchedule(planSeq, {
			callback:function(assetData){
			_this.setAssetScheduleData(assetData, startDate, endDate);
		}});
	},
	readAssetScheduleList : function(year, month, day, assetSeq){		
		assetLoading = true;
		
		SchedulerAssetService.readAssetScheduleList(year, month, day, assetSeq, {
			callback:function(assetData){
			checkAssetPlan(assetData);
		}});
	},
	readAssetNotDuplicateList : function(startDate, endDate, assetValue) {
		var _this = this;
		SchedulerAssetService.readAssetNotDuplicateList(startDate, endDate, {
			callback:function(assetData){
			makeAssetNotDuplicate(assetData,false, assetValue);
		}});
	},
	readAssetNotDuplicateIgnoreMyList : function(startDate, endDate, assetSubData) {
		var _this = this;
		SchedulerAssetService.readAssetNotDuplicateIgnoreMyList(startDate, endDate, {
			callback:function(assetData){
			makeAssetNotDuplicate(assetData,true, assetSubData);
		}});
	},
	assetLayoutRender : function(year, month, day){		
		var _this = this;
		if (!year || !month || !day) {
			year = 0;
			month = 0;
			day = 0;
		}
		
		assetLoading = true;
		
		SchedulerService.getWeekInfo(year, month, day, {
			callback:function(assetInfo){
			assetScheduleRender(assetInfo);
		}});
	},
	saveAssetSchedule : function(data) {
		var _this = this;
		SchedulerAssetService.saveAssetSchedule(data, {
			callback:function(isSuccess){
			_this.checkSuccess(isSuccess);
			reload();
		}});
	},
	deleteAssetSchedule : function(planSeq) {
		var _this = this;
		SchedulerAssetService.deleteAssetSchedule(planSeq, {
			callback:function(isSuccess){
			_this.checkSuccess(isSuccess);
			reload();
		}});
	},
	modifyAssetSchedule : function(data) {
		var _this = this;
		SchedulerAssetService.modifyAssetSchedule(data, {
			callback:function(isSuccess){
			_this.checkSuccess(isSuccess);
			reload();
		}});
	},
	modifyWriteAssetSchedule : function(planSeq) {
		var _this = this;
		SchedulerAssetService.readAssetSchedule(planSeq, {
			callback:function(assetData){
			_this.setWriteAssetScheduleData(assetData);
		}});
	},
	checkAssetScheduleDuplicate : function(data) {
		var _this = this;
		SchedulerAssetService.checkAssetScheduleDuplicate(data.assetSeq, data.planSeq, data.startDate, data.endDate, {
			callback:function(assetData){
			checkSaveAssetSchedule(assetData, data);
		}});
	},
	checkMyScheduleOrShareSchedule : function(planSeq, startDate, endDate) {
		var _this = this;
		SchedulerAssetService.checkMyScheduleOrShareSchedule(planSeq, {
			callback:function(assetData){
			if (assetData.isMime) {
				viewSchedule(assetData.schedulerId, startDate, endDate);
			} else {
				assetViewBox(planSeq, startDate, endDate);
			}
		}});
	},
	checkSuccess : function (isSuccess) {
		if (isSuccess == "fail")
			alert(comMsg.error_msg_001);
	},
	setAssetScheduleData : function(assetData, startDate, endDate) {
		var startDateFormat = makeDateFormat(startDate);
		var endDateFormat = makeDateFormat(endDate);
		var startTimeFormat = makeTimeFormat(startDate);
		var endTimeFormat = makeTimeFormat(endDate);

		var title = makeDateFormat(assetData.createTime)+" "+makeTimeFormat(assetData.createTime);
		
		startDateFormat = startDateFormat+" "+startTimeFormat;
		endDateFormat = endDateFormat+" "+endTimeFormat;
		
		var mailUserSeq = jQuery("#mailUserSeq").val();
		if (assetData.mailUserSeq != parseInt(mailUserSeq,10)) {
			jQuery("#view_asset_box_jqbtn a:eq(2)").show();
		} else {
 			jQuery("#view_asset_box_jqbtn a").show();
		}
		jQuery('#planSeq').val(assetData.planSeq);
		jQuery("#assetScheduleId").val(assetData.schedulerId);
		jQuery('#view_asset_box_pht').text(title);
		jQuery('#view_asset_date').text(startDateFormat+" ~ "+endDateFormat);
		jQuery('#view_asset_user').text(assetData.userName);
		jQuery('#view_asset_user').attr("title", assetData.title);
		jQuery('#view_asset_contect').text(assetData.contect);
		jQuery('#view_asset_contect').attr("title", assetData.contect);
	},
	setWriteAssetScheduleData : function(assetData) {
		
		if (assetData.schedulerId > 0) {
			alert(schedulerMsg.scheduler_asset_018);
			return;
		}
		
		jQuery('#planSeq').val(assetData.planSeq);
		jQuery("#assetScheduleId").val(assetData.schedulerId);

		var startDate = assetData.startDate;
		var endDate = assetData.endDate;
		var startDateFormat = makeDateFormat(startDate);
		var endDateFormat = makeDateFormat(endDate);
		var startTimeFormat = makeTimeFormat(startDate);
		var endTimeFormat = makeTimeFormat(endDate);

		jQuery('#inputAssetStartDate').val(startDateFormat);
		jQuery('#inputAssetEndDate').val(endDateFormat);
		jQuery('#inputAssetStartTime').val(startTimeFormat);
		jQuery('#inputAssetEndTime').val(endTimeFormat);

		var shour,smin,ehour,emin;
		shour = startDate.substring(8,10);
		smin = startDate.substring(10,12);
		ehour = endDate.substring(8,10);
		emin = endDate.substring(10,12);
		jQuery("#startAssetTime").val(shour+","+smin);
		jQuery("#endAssetTime").val(ehour+","+emin);

		var sampm = (shour >= 12)?1:0;
		var eampm = (ehour >= 12)?1:0;
		shour = ((shour > 12)?shour-12:shour);
		ehour = ((ehour > 12)?ehour-12:ehour);

		var stimeStr = shour+":"+smin;
		var etimeStr = ehour+":"+emin;

		jQuery("#start_asset_time_picker_ampmSelect").data("ampm",sampm);
		jQuery("#end_asset_time_picker_ampmSelect").data("ampm",eampm);
		jQuery("#start_asset_time_picker_timeSelect").data("date",stimeStr);
		jQuery("#end_asset_time_picker_timeSelect").data("date",etimeStr);

		jQuery("#asset_user_name").text(assetData.userName);
		jQuery("#assetContect").val(assetData.contect);

		makeAssetModifyDialog();
	}
});


function drowAssetCategoryList(assetData) {
	var layout;
	var subLayout;
	var assetLink;
	var assetToggleImage;
	var categoryTitle;
	var assetArray;
	var asset;
	var assetGroupId;
	var assetDescription;
	var assetLayout;
	if (assetData && assetData.length) {
		for (var i=0; i<assetData.length; i++) {
			assetGroupId = 's_assetGroup_'+i;
			layout = jQuery("<div></div>").addClass("schedulerLeftSubMenuDiv");
			subLayout = jQuery("<div></div>").addClass("schedulerLeftSubMenuTitleDiv");
			assetLink = jQuery("<a></a>").attr("href","javascript:toggleMenu('"+assetGroupId+"');toogleMenuTrigger();");
			assetToggleImage = jQuery("<img src='/design/common/image/btn_menu_mius.gif'/>").attr("id",assetGroupId+"_view");
			categoryTitle = jQuery("<a></a>").attr("href","javascript:toggleMenu('"+assetGroupId+"');toogleMenuTrigger();");
			assetDescription = jQuery("<a></a>").attr("href","javascript:getCategoryDescription('"+assetData[i].categorySeq+"')").text(schedulerMsg.scheduler_asset_002);
			assetDescription = assetDescription.addClass("TM_ml_sideBtn jpf").css({"position":"absolute","right":"8px"});
			assetLayout = jQuery("<div></div>").attr("id",assetGroupId).addClass("assetIconWrap");
			
			assetArray = assetData[i].assetList;
			if (assetArray && assetArray.length) {
				for (var j=0;j<assetArray.length; j++) {
					asset = jQuery("<div onclick=\"viewAssetSchedule('"+assetData[i].categorySeq+"','"+assetArray[j].assetSeq+"')\"></div>").attr("id","asset_"+i+"_"+j);
					asset.text(assetArray[j].assetName);
					asset.attr("title", assetArray[j].assetName);
					asset.addClass("assetIconTitle");
					assetLayout.append(asset);
				}
			}
			assetLink.append(assetToggleImage);
			categoryTitle.text(jQuery.trim(assetData[i].categoryName));
			categoryTitle.attr("title", jQuery.trim(assetData[i].categoryName));
			
			var margin = "55px";
			if (LOCALE == 'jp') {
				margin = "70px";
			} else if (LOCALE == 'en') {
				margin = "75px";
			}
			subLayout.css("margin-right", margin);
			subLayout.append(assetLink).append("&nbsp;").append(categoryTitle).append(assetDescription);
			layout.append(subLayout).append(assetLayout);
			jQuery("#s_assetGroup").append(layout);
			jQuery("#s_assetGroup").attr("data",assetData.length);
			setToggleTreeMenu(assetGroupId);
		}
		setToggleTreeMenu("s_assetGroup");
		
		scheduerAssetControl.makeCategoryMap(assetData);
	} else {
		jQuery("#asset_tab").hide();
		jQuery("#assetMenu").hide();		
	}
}

function makeAssetNotDuplicate(assetData, isModify, assetSubData) {
	var layout;
	var categoryLayout;
	var assetLayout;
	var assetSelect;
	var assetSelectId;
	var assetId;
	var assetArray;
	var assetSelectArray = [];

	if (assetData && assetData.length) {
		for (var i=0; i<assetData.length; i++) {
			assetSelectId = 'assetSelect_'+assetData[i].categorySeq;
			assetId = 'assetVal_'+assetData[i].categorySeq;
			
			layout = jQuery("<div class='inputPane'></div>");
			
			categoryLayout = jQuery("<div class='assetName'></div>");
			categoryLayout.text(jQuery.trim(assetData[i].categoryName));
			categoryLayout.attr("title",jQuery.trim(assetData[i].categoryName));
			
			assetLayout= jQuery("<div class='assetSelect'></div>");
			
			assetSelect= jQuery("<div></div>").attr("id",assetSelectId);
			
			assetArray = assetData[i].assetList;
			if (assetArray && assetArray.length) {
				assetLayout.append(assetSelect);
				layout.append(categoryLayout).append(assetLayout).append("<div class='cls'></div>");
				jQuery("#asset_control_content").append(layout);
				
				assetSelectArray = [];
				
				assetSelectArray.push({"index":schedulerMsg.scheduler_asset_013, "value":"0"});
				for (var j=0;j<assetArray.length; j++) {
					assetSelectArray.push({"index":assetArray[j].assetName, "value":assetArray[j].assetSeq});
				}
				jQuery("#"+assetSelectId).selectbox({selectId:assetId, width:150}, "",assetSelectArray);
			}
		}
		layout = jQuery("<div class='inputPane'></div>");
		categoryLayout = jQuery("<div class='assetName'></div>").text(schedulerMsg.scheduler_asset_006);
		assetLayout= jQuery("<div class='assetSelect'><input type='text' id='contect' name='contect' class='IP200' style='width:150px'/></div>");
		layout.append(categoryLayout).append(assetLayout).append("<div class='cls'></div>");
		jQuery("#asset_control_content").append(layout);
		jQuery("#contect").val(jQuery("#mobileNo").val());
		jQuery("#asset_control").css("height",(25*assetData.length)+60+"px");
		
		if (isModify) {
			if (assetSubData && assetSubData.length) {
				var assetSelectId = "";
				for (var i=0; i<assetSubData.length; i++) {
					assetSelectId = 'assetSelect_'+assetSubData[i].categorySeq;
					jQuery("#"+assetSelectId).selectboxSetValue(assetSubData[i].assetSeq);
				}
			}
			jQuery("#contect").val(assetSubData[0].contect);
		} else {
			var categorySeq = jQuery("#categorySeq").val();
			var assetSeq = jQuery("#assetSeq").val();
			assetSelectId = 'assetSelect_'+categorySeq;
			jQuery("#"+assetSelectId).selectboxSetValue(assetSeq);
		}
		jQuery("#asset_control").show();
	} else {
		alert(schedulerMsg.scheduler_asset_022);
		jQuery("#assetReserve").attr("checked",false);
		viewAssetBox(false);
	}
}

function setAssetSchedule() {
	var assetSeqStr = "";
	var assetSum = 0;
	var assetVal;
	var count = 0;
	jQuery("#asset_control_content :input[type=hidden]").each(function(i){
		assetVal = jQuery(this).val();
		if (assetVal > 0) {
			assetSeqStr += (assetSeqStr == "") ? assetVal : "_"+assetVal;
		}
		assetSum+=parseInt(assetVal,10);
	});
	
	if (assetSum == 0) {
		alert(schedulerMsg.scheduler_asset_014);
		return;
	}
	
	var contectObj = jQuery("#contect");
	
	if(!checkInputLength("jQuery", contectObj, schedulerMsg.scheduler_asset_007, 0, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", contectObj, "onlyBack")) {
		return;
	}
	
	jQuery("#assetReserve").val(assetSeqStr);
	hideAssetSchedule();
}

function hideAssetSchedule() {
	jQuery("#asset_control").hide();
	jQuery("#location").attr("readOnly", false);
	jQuery("#content").attr("readOnly", false);
}

function cancelAssetSchedule() {
	jQuery("#assetReserve").attr("checked", false);
	jQuery("#assetReserve").val("");
	viewAssetBox(false);
}

function getCategoryDescription(categorySeq) {
	scheduerAssetControl.readAssetCategoryDescription(categorySeq);
}

function viewAssetCategoryDescription(descriptionData) {
	var description = descriptionData.categoryDescription;
	if (jQuery.trim(description) == "") {
		description = schedulerMsg.scheduler_asset_003;
	}
	jQuery("#description_content").html(description);
	
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 500,
			minWidth:600,
			openFunc:function(){
	 			jQuery("#asset_category_description_box_pht").text(descriptionData.categoryName);
			}
		};
	jQuery("#asset_category_description_box").jQpopup("open",popupOpt);
}

function viewAssetSchedule(categorySeq, assetSeq) {
	
	if (!assetSeq) {
		return;
	}

	if (assetLoading) {
		return;
	}
	var categoryInfo = scheduerAssetControl.getCategoryInfo(categorySeq);
	var categoryName = categoryInfo.name;
	var assetName = "";
	var assetList = categoryInfo.assetList;
	if (assetList && assetList.length) {
		for (var i=0; i<assetList.length; i++) {
			if (assetSeq == assetList[i].assetSeq) {
				assetName = assetList[i].assetName;
				break;
			}
		}
		setSchedulerInfo(categoryName+" > "+assetName);
	}
	jQuery("#categorySeq").val(categorySeq);
	jQuery("#assetSeq").val(assetSeq);
	
	scheduerAssetControl.assetLayoutRender();
}

function loadAssetSchedule(year, month, day) {
	scheduerAssetControl.assetLayoutRender(year, month, day);
}

function assetScheduleRender(assetInfo) {
	var assetdateList = assetInfo.dateList;

	var assetScheduleData = {
		hiddenFormData:"",
		headerTable:"<div class='TM_listWrapper'><table class='scheduler_week_TB TM_scheduler_basicTB' cellpadding='0' cellspacing='0'><tr><td valign=top>",
		bodyTable:"<table class='TM_scheduler_basicTB' cellpadding='0' cellspacing='0'><tr><td colspan='2'><table width='100%' id='scheduler_week' cellpadding='0' cellspacing='0'><tr><td class='dateTd' nowrap></td>",
		bodyRepeat:"<td class='dateTd {dateType}'>{day} ({dayType})</td>",
		bodyRepeatTail:"</tr>",
		timeBox1:"<tr><td class='timeTD'>{timeFormat}</td><td class='schedulerProcessTd {today0}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{0}','{00}:00');\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{0}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today1}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{1}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{1}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today2}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{2}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{2}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today3}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{3}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{3}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today4}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{4}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{4}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today5}'><table class='schedulerTable' cellpadding='0' cellspacing='0'><tbody><tr><td onclick=\"assetInputBox('{5}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{5}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today6}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{6}','{00}:00')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{6}_{00}00' class='progressScheduleDiv'/></td></tr></tbody></table></td></tr>",
		timeBox2:"<tr><td nowrap='' class='timeTD'>30"+schedulerMsg.scheduler_min+"</td><td class='schedulerProcessTd {today0}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{0}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{0}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today1}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{1}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{1}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today2}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{2}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{2}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today3}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{3}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{3}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today4}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{4}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{4}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today5}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{5}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{5}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td><td class='schedulerProcessTd {today6}'><table cellpadding='0' cellspacing='0' class='schedulerTable'><tbody><tr><td onclick=\"assetInputBox('{6}','{00}:30')\" class='schedulerProcessDate'><div index='0' count='0' total='' pwidth='' id='progress_{6}_{00}30' class='progressScheduleDiv'/></td></tr></tbody></table></td></tr>",
		scheduleTimeRepeatTail:"</tr>",
		bodyTableTail:"</table></td></tr></table></td></tr></table></div>"
	};
	
	var stringBuffer = new StringBuffer();
	stringBuffer.append(assetScheduleData.hiddenFormData);
	stringBuffer.append(assetScheduleData.headerTable);
	stringBuffer.append(assetScheduleData.bodyTable);
	
	var headerInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	var thisdayMonth = "";
	var assetListMonth = "";
	var bodyRepeat = "";
	var day = "";
	var dayType = "";
	var dateType = "";
	for (var i=0; i<assetdateList.length; i++) {
		bodyRepeat = assetScheduleData.bodyRepeat;
		thisdayMonth = assetInfo.thisdayStr.substring(4,6);
		assetListMonth = assetdateList[i].date.substring(4,6);
		
		dateType = "";
		
		if ((i % 7) == 0) {
			dateType = "TM_F_POINT";
		} else if (i % 7 == 6) {
			dateType = "TM_F_POINT2";
		}
	
		bodyRepeat = replaceAll(bodyRepeat,"{dateType}",dateType);
		
		day = assetdateList[i].date.substring(4,6);
		
		if (assetdateList[i].date.charAt(4) == "0") {
			day = assetdateList[i].date.substring(5,6);
		} 
		
		day+= " / ";
			
		if (assetdateList[i].date.charAt(6) == "0") {
			day += assetdateList[i].date.substring(7,8);
		} 
		else {
			day += assetdateList[i].date.substring(6,8);
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
	stringBuffer.append(assetScheduleData.bodyRepeatTail);
	var bodyRepeatInfo = stringBuffer.toString();
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

		timeInfoTmp = assetScheduleData.timeBox1+assetScheduleData.timeBox2;
		timeInfoTmp = replaceAll(timeInfoTmp,"{timeFormat}",timeFormat);
		timeInfoTmp = replaceAll(timeInfoTmp,"{00}",l);

		stringBuffer.append(timeInfoTmp);
	}

	var timeInfo = stringBuffer.toString();
	stringBuffer.destroy();
	
	for (var m=0; m<assetdateList.length; m++) {
		timeInfo = replaceAll(timeInfo,"{"+m+"}",assetdateList[m].date);
		repeatToday = "";
		if (assetInfo.todayStr == assetdateList[m].date) {
			repeatToday = "today";
		}
		timeInfo = replaceAll(timeInfo,"{today"+m+"}",repeatToday);
	}
	
	var bodyTableTailInfo = assetScheduleData.bodyTableTail;
	
	jQuery("#body1").html(headerInfo+bodyRepeatInfo+timeInfo+bodyTableTailInfo);
	jQuery("#body1").show();
	
	jQuery("#myMode").val("asset");
	jQuery("#myYear").val(assetInfo.thisYear);
	jQuery("#myMonth").val(assetInfo.thisMonth);
	jQuery("#myDay").val(assetInfo.thisDay);
	jQuery("#thisDate").val(assetInfo.todayStr);
	jQuery("#currentDate").val(assetInfo.thisdayStr);
	
	assetScheduleSetting(assetInfo);
}

function assetScheduleSetting(assetInfo) {
	setSchedulerTab('4');
	jQuery("#currentType").val("asset");
	
	var firstLunar = assetInfo.firstLunar+"";
	var lastLunar = assetInfo.lastLunar+"";
	
	var firstLunarMonth = firstLunar.substring(4,6);
	if (firstLunar.charAt(4) == "0") {
		firstLunarMonth = firstLunar.substring(5,6);
	} 
	
	var lastLunarMonth = lastLunar.substring(4,6);
	if (lastLunar.charAt(4) == "0") {
		lastLunarMonth = lastLunar.substring(5,6);
	} 

	var tabMenuData = {firstYear:assetInfo.firstYear, firstMonth:assetInfo.firstMonth, firstDay:assetInfo.firstDay,
			lastYear:assetInfo.lastYear, lastMonth:assetInfo.lastMonth, lastDay:assetInfo.lastDay,
			yearText:schedulerMsg.scheduler_year, 
	       	firstMonthText:getMonthStr(assetInfo.firstMonth), 
	       	dayText:schedulerMsg.scheduler_day,
	       	firstLunarYear:firstLunar.substring(0,4), firstLunarMonth:firstLunar.substring(4,6), firstLunarDay:firstLunar.substring(6,8),
	       	lastLunarYear:lastLunar.substring(0,4), lastLunarMonth:lastLunar.substring(4,6), lastLunarDay:lastLunar.substring(6,8),
	       	firstLunarMonthText:getMonthStr(parseInt(firstLunarMonth,10)),
	       	lastLunarMonthText:getMonthStr(parseInt(lastLunarMonth,10)),
	   	  	lunarText:schedulerMsg.scheduler_lunar,
	   	 	lastMonthText:getMonthStr(assetInfo.lastMonth),
	   	  	prevLink:'loadAssetSchedule("'+assetInfo.prevYear+'","'+assetInfo.prevMonth+'","'+assetInfo.prevDay+'")',
	   		nextLink:'loadAssetSchedule("'+assetInfo.nextYear+'","'+assetInfo.nextMonth+'","'+assetInfo.nextDay+'")'
	};
	makeWeekTabMenu(tabMenuData);
	readHoliday(assetInfo.thisYear, assetInfo.dateList);
	scheduerAssetControl.setAssetDateList(assetInfo.dateList);
	scheduerAssetControl.setTimeList(makeTimeArray());
	readAssetScheduleList(assetInfo.thisYear,assetInfo.thisMonth,assetInfo.thisDay);
	assetLoading = false;
}

function readAssetScheduleList(year, month, day) {
	var assetSeq = jQuery("#assetSeq").val();
	scheduerAssetControl.readAssetScheduleList(year, month, day, assetSeq);
}

function checkAssetPlan(assetData) {
	if (assetData && assetData.length) {
		var drowList;
		var data;
		var timeSize;
		var drowStartDate;
		var drowStartTime;
		var drowEndTime;
		var schedule = {};
		for (var i=0; i<assetData.length; i++) {
			drowList = assetData[i].drowplanList;
			for (var j=0; j<drowList.length; j++) {
				data = drowList[j].split("|");
				timeSize = parseInt(data[0], 10);
				drowStartDate = data[1];
				drowStartTime = parseInt(data[2], 10);
				drowEndTime = parseInt(data[3], 10);
				
				if (drowStartTime == 0) {
					drowStartTime = "000";
				} else if (drowStartTime == 30) {
					drowStartTime = "030";
				}
				
				schedule.drowStartTime = drowStartTime;
				schedule.drowEndTime = drowEndTime;
				schedule.title = assetData[i].userName;
				schedule.schedulerId = assetData[i].planSeq;
				schedule.timeSize = timeSize;
				
				jQuery("#progress_"+drowStartDate+"_"+drowStartTime).append("<div id='plan_"+drowStartDate+"_"+drowStartTime+"' class='timeplan' onclick='checkAssetViewBox(\""+assetData[i].planSeq+"\",\""+assetData[i].startDate+"\",\""+assetData[i].endDate+"\");event.cancelBubble=true'></div>");
				schedulerDataControl.makeTimeScheduleBar("#progress_"+drowStartDate+"_"+drowStartTime, "#plan_"+drowStartDate+"_"+drowStartTime, schedule ,"98%",((30*timeSize)+timeSize-1)+"px","0px","0px");
			}
		}
	}
}

function makeAssetInputDialog() {
	resetAssetInputForm();
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 200,
			minWidth:380,
			btnList : [{name:comMsg.comn_add,func:saveAssetSchedule}],	
			openFunc:function(){
				jQuery("#assetScheduleId").val("");
				jQuery('#planSeq').val("");
	 			jQuery("#input_asset_box_pht").text(schedulerMsg.scheduler_asset_001+" "+schedulerMsg.scheduler_asset_004);
	 			jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	 			jQuery("#inputAssetStartDate").datepick({dateFormat:'yy-mm-dd'});
	 			jQuery("#inputAssetEndDate").datepick({dateFormat:'yy-mm-dd'});
	 			jQuery("#datepick-div").css("z-index","10001");
			},
			closeFunc:function(){
				jQuery("#inputAssetStartDate").removeClass("hasDatepick");
				jQuery("#inputAssetEndDate").removeClass("hasDatepick");
				jQuery(".timePick").hide();
				resetAssetInputForm();
				jQuery.datepick._hideDatepick();
			}
		};
	jQuery("#input_asset_box").jQpopup("open",popupOpt);	
}

function makeAssetModifyDialog() {
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 200,
			minWidth:380,
			btnList : [{name:comMsg.comn_modfy,func:saveAssetSchedule}],	
			openFunc:function(){
	 			jQuery("#input_asset_box_pht").text(schedulerMsg.scheduler_title_modify);
			},
			closeFunc:function(){
				jQuery("#inputAssetStartDate").removeClass("hasDatepick");
				jQuery("#inputAssetEndDate").removeClass("hasDatepick");
				jQuery(".timePick").hide();
				resetAssetInputForm();
				jQuery.datepick._hideDatepick();
			}
		};
	jQuery("#input_asset_box").jQpopup("open",popupOpt);
	jQuery.datepick.setDefaults(jQuery.datepick.regional[LOCALE]);
	jQuery("#inputAssetStartDate").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#inputAssetEndDate").datepick({dateFormat:'yy-mm-dd'});
	jQuery("#datepick-div").css("z-index","10001");
}

function assetInputBox(date, time) {
	jQuery('#saveType').val("add");
	
	makeInputDialog();
	makeInputBoxDate(date, time);
	
	jQuery("#assetReserve").attr("checked",true);
	viewAssetBox(true);
}

function checkAssetViewBox(planSeq, startDate, endDate) {
	scheduerAssetControl.checkMyScheduleOrShareSchedule(planSeq, startDate, endDate);
}

function assetViewBox(planSeq, startDate, endDate) {
	resetAssetViewSchedule();
	scheduerAssetControl.readAssetSchedule(planSeq, startDate, endDate);
	jQuery("#view_asset_box").jQpopup("close");
	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 180,
			minWidth:380,
			btnList : [{name:comMsg.comn_modfy,func:modifyAssetSchedule},{name:comMsg.comn_del,func:deleteAssetSchedule}],	
			openFunc:function(){
				jQuery("#view_asset_box_jqbtn a").hide();
			},
			closeFunc:function(){
			}
		};
	
	jQuery("#view_asset_box").jQpopup("open",popupOpt);
}

function resetAssetInputForm() {
	jQuery('#start_asset_time_picker').hide(function(){removeTimePickSelect();});
	jQuery('#end_asset_time_picker').hide(function(){removeTimePickSelect();});
}

function resetAssetViewSchedule() {
	jQuery('#planSeq').val("");
	jQuery('#view_asset_box_pht').text("");
	jQuery('#view_asset_user').text("");
	jQuery('#view_asset_user').attr("title", "");
	jQuery('#view_asset_contect').text("");
	jQuery('#view_asset_contect').attr("title", "");
	jQuery('#view_asset_date').text("");
}

function saveAssetSchedule() {
	
	var planSeq = jQuery("#planSeq").val();
	var schedulerId = jQuery("#assetScheduleId").val();
	var assetSeq = jQuery("#assetSeq").val();
	
	var contectObj = jQuery("#assetContect");
	var contect = contectObj.val(); 
	 
	if(!checkInputLength("jQuery", contectObj, schedulerMsg.scheduler_asset_007, 0, 64)) {
		return;
	}
	if(!checkInputValidate("jQuery", contectObj, "folderName")) {
		return;
	}
	
	var startDate = jQuery("#inputAssetStartDate").val().split('-');
	var endDate = jQuery("#inputAssetEndDate").val().split('-');
	var startTime = jQuery("#startAssetTime").val().split(',');
	var endTime =jQuery("#endAssetTime").val().split(',');
	var start = new Date(startDate[0],startDate[1]-1, startDate[2]);
	var end = new Date(endDate[0],endDate[1]-1,endDate[2]);
	
	if (!checkTimeRange(start, end, startTime, endTime)) {
		return;
	}
	
	var startDateStr = startDate[0]+startDate[1]+startDate[2]+startTime[0]+startTime[1];
	var endDateStr = endDate[0]+endDate[1]+endDate[2]+endTime[0]+endTime[1];
	
	var data = {
		  planSeq:planSeq,
		  schedulerId:schedulerId,
		  assetSeq:assetSeq,
		  startDate:startDateStr,
		  endDate:endDateStr,
		  contect:contect
		};
	
	scheduerAssetControl.checkAssetScheduleDuplicate(data);
}

function saveWork(data) {
	var saveType = jQuery('#assetSaveType').val();
	if (saveType == "add") {
		scheduerAssetControl.saveAssetSchedule(data);
	}
	else if (saveType == "modify") {
		scheduerAssetControl.modifyAssetSchedule(data);
	}
	jQuery("#input_asset_box").jQpopup("close");
}

function modifyAssetSchedule() {
	jQuery('#assetSaveType').val("modify");
	
	var planSeq = jQuery("#planSeq").val();
	
	scheduerAssetControl.modifyWriteAssetSchedule(planSeq);
	
	jQuery("#view_asset_box").jQpopup("close");
}

function deleteAssetSchedule() {
	var planSeq = jQuery("#planSeq").val();
	var assetScheduleId = jQuery("#assetScheduleId").val();
	
	if (assetScheduleId > 0) {
		alert(schedulerMsg.scheduler_asset_020);
		return;
	}
	
	if (!confirm(schedulerMsg.scheduler_asset_008)) {
		return;
	}
	
	scheduerAssetControl.deleteAssetSchedule(planSeq);
	jQuery("#view_asset_box").jQpopup("close");
}

function checkSaveAssetSchedule(assetData, data) {
	if(assetData.isSuccess && !assetData.isDup) {
		saveWork(data);
	} else if (assetData.isSuccess && assetData.isDup) {
		var categorySeq = jQuery("#categorySeq").val();
		var categoryName = jQuery.trim(scheduerAssetControl.getCategoryInfo(categorySeq).name);
		alert(msgArgsReplace(schedulerMsg.scheduler_asset_011,[categoryName]));
	} else {
		alert(schedulerMsg.scheduler_asset_017);
	}
}

function viewAssetBox(checked) {
	
	if (jQuery("#repeat").attr("checked")) {
		alert(schedulerMsg.scheduler_asset_015);
		jQuery("#assetReserve").attr("checked",false);
		return;
	}
	
	if (checked){
		makeAssetControl();
		jQuery("#location").attr("readOnly", true);
		jQuery("#content").attr("readOnly", true);
	}else{
		jQuery("#asset_control").hide();
		jQuery("#location").attr("readOnly", false);
		jQuery("#content").attr("readOnly", false);
	}
}

function makeAssetControl(assetValue) {
	
	var checked = jQuery("#assetReserve").attr("checked");
	
	if (!checked) {
		return;
	}
	
	removeAssetControl();
	
	var startDate = jQuery("#inputStartDate").val().split('-');
	var endDate = jQuery("#inputEndDate").val().split('-');
	var startTime = jQuery("#startTime").val().split(',');
	var endTime =jQuery("#endTime").val().split(',');
	
	if (jQuery("#allday").attr("checked")) {
		startTime[0] = "00";
		startTime[1] = "00";
		endTime[0] = "23";
		endTime[1] = "30";
	}
	 
	var startDateStr = startDate[0]+startDate[1]+startDate[2]+startTime[0]+startTime[1];
	var endDateStr = endDate[0]+endDate[1]+endDate[2]+endTime[0]+endTime[1]; 
	
	scheduerAssetControl.readAssetNotDuplicateList(startDateStr, endDateStr, assetValue);
}

function setAssetControl(assetData) {
	
	removeAssetControl();
	
	var startDate = jQuery("#inputStartDate").val().split('-');
	var endDate = jQuery("#inputEndDate").val().split('-');
	var startTime = jQuery("#startTime").val().split(',');
	var endTime =jQuery("#endTime").val().split(',');
	 
	var startDateStr = startDate[0]+startDate[1]+startDate[2]+startTime[0]+startTime[1];
	var endDateStr = endDate[0]+endDate[1]+endDate[2]+endTime[0]+endTime[1]; 
	
	scheduerAssetControl.readAssetNotDuplicateIgnoreMyList(startDateStr, endDateStr, assetData);
}

function removeAssetControl() {
	jQuery("#asset_control_content").empty();
}

function gotoAsset() {
	var categoryMap = scheduerAssetControl.getCategoryMap();
	if (categoryMap && categoryMap.size() > 0) {
		var categoryList = categoryMap.values();
		for (var i=0; i < categoryList.length; i++) {
			var assetList = categoryList[i].assetList;
			if (assetList && assetList.length) {
				viewAssetSchedule(categoryList[i].id, assetList[0].assetSeq);
				break;
			}
		}
	}
}