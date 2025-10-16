<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/scheduler_style.css" />
<link rel="stylesheet" type="text/css" href="/design/common/css/mail_tag_list.css" />

<script type="text/javascript" src="/js/ext-lib/jcookie.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/dwr/interface/SchedulerService.js"></script>
<script type="text/javascript" src="/dwr/interface/SchedulerShareService.js"></script>
<script type="text/javascript" src="/dwr/interface/SchedulerAssetService.js"></script>
<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="schedulerCommon.js"></script>
<script type="text/javascript" src="schedulerShare.js"></script>
<script type="text/javascript" src="schedulerAsset.js"></script>

<script type="text/javascript">

var schedulerOption = {
		mainLID : "body1",
		subLID : "c_contentSub",
		calendarID : "menu_carendar",
		monthAction : "/dynamic/scheduler/monthScheduler.do",
		weekAction : "/dynamic/scheduler/weekScheduler.do",
		dayAction : "/dynamic/scheduler/dayScheduler.do",
		progressWeekScheduler : "/dynamic/scheduler/progressWeekScheduler.do",
		progressDayScheduler : "/dynamic/scheduler/progressDayScheduler.do",
		progressSchedulerInit : "/dynamic/scheduler/progressSchedulerInit.jsp",
		searchScheduler : "/dynamic/scheduler/searchScheduler.jsp",
		schedulerCalendar : "/dynamic/scheduler/schedulerCalendar.do",
		outlookScheduler : "/dynamic/scheduler/outlookScheduler.jsp"
	};

var shareGroupOption = {

		type : "share",
		wrapClass : "TM_share_tree_wrapper",
		drowFrame : "s_shareGroup",
		folderClass : "tagimg_base",
		linkFunc:none,

		//submenu option
	 	menuOpt : {
	 		// Style Class Define
	 		overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
						{name:comMsg.comn_modfy,link:modifyShareGroup},					
						{name:comMsg.comn_del,link:deleteShareGroup}
	 			 	]
	 	}	
};

var installLocale = "${installLocale}";

</script>

</head>

<body>

<%@include file="/common/topmenu.jsp"%>
<div style="clear:both"></div>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div id="c_mainBody">
	<div id="c_leftMenu">
		<div>
			<div id="ml_btnFMain_s">
				<div class="TM_topLeftMain">				
				<div class="mainBtn" >					
					<table cellpadding="0" cellspacing="0" border="0" class="TM_mainBtn_bg">
						<tr>
						<td nowrap style="white-space: nowrap;" >
							<div class="TM_t_left_btn"><a href="javascript:;" onclick="loadDaySchedule();" class="TM_mainBtn_split" ><div class="TM_schedulerToday"><tctl:msg key="scheduler.menu.today" bundle="scheduler"/></div></a></div>
						</td>						
						<td nowrap style="white-space: nowrap;" >
							<div class="TM_t_right_btn"><a href="javascript:;" onclick="addSchedule();" ><div class="TM_schedulerAdd"><tctl:msg key="scheduler.menu.add" bundle="scheduler"/></div></a></div>
						</td>
						</tr>
					</table>					
				</div>			
				</div>
			</div>
			<div id="leftMenuRcontentWrapper" style="position:relative;">
			<div id="leftMenuRcontent">
			<div>	
				<div id="menu_carendar"></div>
			
				<div style="height:20px;clear: both;"></div>			
				
				<c:if test="${installLocale != 'jp'}">
				<div class="TM_ml_line"></div>
				<div style="font-weight:bold;" class="schedulerLeftMenuDiv">
					<img src="/design/common/image/btn_menu_mius.gif"> <a href="#" onclick="viewDownload()"><tctl:msg key="scheduler.outlook.title" bundle="scheduler"/></a>
				</div>
				</c:if>
				<div class="TM_ml_line"></div>
					
				<div class="schedulerLeftMenuDiv">
					<a href="javascript:;" onclick="toggleMenu('s_shareGroup');toogleMenuTrigger();"><img src="/design/common/image/btn_menu_mius.gif" id="s_shareGroup_view"></a>
					<a href="#" onclick="toggleMenu('s_shareGroup')"><tctl:msg key="scheduler.share.title" bundle="scheduler"/></a>
					<a href="javascript:;" onclick="addShareGroup()" class="TM_ml_sideBtn jpf" style="position:absolute;right:8px;"><tctl:msg key="btn.add" /></a>				
				</div>			
				<div id="s_shareGroup"></div>
				
				<div class="TM_ml_line"></div>
					
				<div id="assetMenu" class="schedulerLeftMenuDiv">
					<a href="javascript:;" onclick="toggleMenu('s_assetGroup');toogleMenuTrigger();"><img src="/design/common/image/btn_menu_mius.gif" id="s_assetGroup_view"></a>
					<a href="#" onclick="toggleMenu('s_assetGroup')"><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></a>
				</div>			
				<div id="s_assetGroup"></div>
			</div>
			</div>
			</div>		
			<%@include file="/common/sideMenu.jsp"%>
		</div>
	</div>
	
	<div id="c_contentBodyWapper" class="TM_contentBodyWapper">
		<div style="position:relative">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif" class="TM_barLeft">
				<div class="TM_finfo_data" style="width:400px;overflow:hidden;white-space:nowrap;"><span class="TM_work_title" id="finfo_fname"></span></div>
				<div class="TM_finfo_search">
					<div class="TM_mainSearch">
					<div style="float: left">					
						<div id="searchTypeSelect"></div>
					</div>
					<div style="float: left">
						&nbsp;<input type="text" class="searchBox"  id="keyWord" name="keyWord"  onKeyPress="(keyEvent(event) == 13) ? validateSearch() : '';" /><a href="#" onclick="validateSearch()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a>
					</div>
					<div class="fclear"></div>					
					</div>
				</div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
		</div>
		
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
		
		<div class="TM_mainContent">
		<div class="tabMenu">			
			<div class="mail_body_tabmenu">
			<div class="mail_body_tab_wrapper_out">
				<div class="mail_body_tab_wrapper">
					<div class="tabMenu_btn">
						<a onclick="loadDaySchedule()" href="javascript:;"><span><tctl:msg key="scheduler.tab.daily" bundle="scheduler"/></span></a>
						<a onclick="loadWeekSchedule()" href="javascript:;"><span><tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/></span></a>
						<a onclick="loadMonthSchedule()" href="javascript:;"><span><tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/></span></a>
						<a onclick="gotoSearch()" href="javascript:;"><span><tctl:msg key="scheduler.search" bundle="scheduler"/></span></a>
						<a id="asset_tab" onclick="gotoAsset()" href="javascript:;"><span><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></span></a>
					</div>
					<div id="pageNavi"  class="mail_body_navi"></div>
				</div>
			</div>
			</div>		
		</div>

		<div id="date_month">
			<div class="leftArea"></div>
			<div class="rightArea"></div>
		</div>
		
		<div id="c_contentBody">
			<div id="c_contentMain">
				<div id="body1"></div>
				<div id="body2" style="display:none;"></div>
				<div id="body3" style="display:none;"></div>
			</div>
			<div id="c_contentSub"></div>
		</div>
		</div>
	</div>
</div>


<%@include file="/common/bottom.jsp"%>


<input type='hidden' id='myMode'>
<input type='hidden' id='myYear' name='myYear'>
<input type='hidden' id='myMonth' name='myMonth'>
<input type='hidden' id='myDay' name='myDay'>
<input type='hidden' id='thisDate' name='thisDate'>
<input type='hidden' id='dateList' name='dateList'>
<input type='hidden' id='currentDate' name='currentDate'>
<input type='hidden' id='mailUserSeq' name='mailUserSeq' value='${mailUserSeq}'>
<input type='hidden' id='schedulerSelectDate' name='schedulerSelectDate'>
<input type='hidden' id='repeatSelectStartDate' name='repeatSelectStartDate'>
<input type='hidden' id='repeatSelectEndDate' name='repeatSelectEndDate'>
<input type="hidden" id="currentTime" value="${currentTime}">

<input type='hidden' id='categorySeq' name='categorySeq'>
<input type='hidden' id='assetSeq' name='assetSeq'>
<input type="hidden" id="currentType" name="currentType">


<div id="input_box" style="display:none">
<form id="inputScheduleForm" name="inputScheduleForm">
<input type="hidden" name="schedulerId" id="schedulerId">
<input type="hidden" name="schedulerShare" id="schedulerShare">
<input type="hidden" name="schedulerRepeat" id="schedulerRepeat">
<input type="hidden" name="saveType" id="saveType">
<input type="hidden" id="startTime">
<input type="hidden" id="endTime">
	<div style="overflow-y:auto;overflow-x:hidden;height:400px;width:100%;">
	<table class="TB_scheduleAdd" cellpadding="0" cellspacing="0">
		<tr>
			<th><tctl:msg key="scheduler.search.subject" bundle="scheduler"/></th>	
			<td><input type="text" name="title" id="title" class="IP100"></td>
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
			<td>
				<input type="text" id="inputStartDate" class="IP_calendar" onclick="hideAllTimePicker()" readonly="readonly"/>
				<input type="text" id="inputStartTime" class="IP_calendar2" readonly="readonly" onclick="toggleTimePicker('start_time_picker')"/>
				<div style="position:relative;">
					<div id="start_time_picker" class="timePick" style="left:140px;">
						<div style="float: left"><div id="start_time_picker_ampmSelect"></div></div>
						<div style="float: left"><div id="start_time_picker_timeSelect"></div></div>
						<div class="cls"></div>						
						<p align="center" style="margin-top:5px">
							<a href="#" class="btn_basic" onclick="setTimePicker('start_time_picker','startTime','inputStartTime')"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a> 
							<a href="#" class="btn_basic" onclick="hideTimePicker('start_time_picker')"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>
						</p>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th/>
			<td>	
				<input type="text" id="inputEndDate" class="IP_calendar" onclick="hideAllTimePicker()" readonly="readonly"/>
				<input type="text" id="inputEndTime" class="IP_calendar2" readonly="readonly" onclick="toggleTimePicker('end_time_picker')"/>
				<div style="position:relative;">
					<div id="end_time_picker" class="timePick" style="left:140px;">
						<div style="float: left"><div id="end_time_picker_ampmSelect"></div></div>
						<div style="float: left"><div id="end_time_picker_timeSelect"></div></div>
						<div class="cls"></div>
						<p align="center" style="margin-top:5px">
							<a href="#" class="btn_basic" onclick="setTimePicker('end_time_picker','endTime','inputEndTime')"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a> 
							<a href="#" class="btn_basic" onclick="hideTimePicker('end_time_picker')"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>
						</p>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.title.select" bundle="scheduler"/></th>
			<td>	
				<label><input type="checkbox" id="allday" name="allday" onclick="checkAllday(this.checked)"> <tctl:msg key="scheduler.allday" bundle="scheduler"/> </label>
				<label><input type="checkbox" id="repeat" value="" onclick="repeatSchedule(this.checked)"> <tctl:msg key="scheduler.repeat.control" bundle="scheduler"/> </label>
				<label><input type="checkbox" id="holiday" name="holiday"> <tctl:msg key="scheduler.anniversary.set" bundle="scheduler"/> </label>
				<label id="assetReserveLabel"><input type="checkbox" id="assetReserve" name="assetReserve" onclick="viewAssetBox(this.checked)"> <tctl:msg key="scheduler.asset.012" bundle="scheduler"/> </label>
			</td>		
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.location" bundle="scheduler"/></th>
			<td>
			<div style="position:relative;">
			<div style="position:absolute;top:0px;left:0px;">
			<div id="repeat_control" class="repeatControl">
				<div><b><tctl:msg key="scheduler.repeat" bundle="scheduler"/></b></div>					
				<div class="inputPane">
					<input type="radio" name="repeatType" id="repeatType" value="00" onclick="toggleRepeatSelectCheck('0')" checked> <tctl:msg key="scheduler.repeat.none" bundle="scheduler"/>
				</div>					
				<div class="inputPane">						
					<div style='float: left'>
						<input type="radio" name="repeatType" id="repeatType" value="01" onclick="toggleRepeatSelectCheck('1')"> <tctl:msg key="scheduler.tab.daily" bundle="scheduler"/>
					</div>
					<div class="inputSelectWrapper"><div id="dayTermSelect"></div></div>
					<div class="inputSelectText"><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/></div>
					<div class="cls"></div>
				</div>
				<div class="inputPane">
					<div style='float: left'>
						<input type="radio" name="repeatType" id="repeatType" value="02" onclick="toggleRepeatSelectCheck('2')"> <tctl:msg key="scheduler.tab.weekly" bundle="scheduler"/>
					</div>
					<div class="inputSelectWrapper"><div id="weekTerm1Select"></div></div>
					<div class="inputSelectText"><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/></div>
					<div class="cls"></div>
				</div>
				<div id="weekSubWrapper" class="inputPane" style="display:none;">
					<div class="inputSelectRightWrapper">
						<input type="checkbox" name="weekTerm2" value="01"/><tctl:msg key="scheduler.date.0" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="02"/><tctl:msg key="scheduler.date.1" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="03"/><tctl:msg key="scheduler.date.2" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="04"/><tctl:msg key="scheduler.date.3" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="05"/><tctl:msg key="scheduler.date.4" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="06"/><tctl:msg key="scheduler.date.5" bundle="scheduler"/>
						<input type="checkbox" name="weekTerm2" value="07"/><tctl:msg key="scheduler.date.6" bundle="scheduler"/>
					</div>
					<div class="cls"></div>
				</div>
				<div class="inputPane">
					<div style='float: left'>
						<input type="radio" name="repeatType" id="repeatMType" value="03" onclick="toggleRepeatSelectCheck('3')"> <tctl:msg key="scheduler.tab.monthly" bundle="scheduler"/>
					</div>
					<div class="inputSelectWrapper"><div id="monthTerm1Select"></div></div>
					<div class="inputSelectText"><tctl:msg key="scheduler.repeat.every" bundle="scheduler"/></div>
					<div class="cls"></div>
				</div>
				<div id="month_sub_repeat" class="monthSubRepeat">
					<div>
						<div style='float: left'><input type="radio" name="monthSubType" id="monthSubType" value="01" onclick="toggleRepeatSelectCheck('31')"></div>
						<div class="inputSelectWrapper"><div id="monthSubTerm1Select"></div></div>
						<div class="inputSelectText"><tctl:msg key="scheduler.repeat.at" bundle="scheduler"/></div>
						<div class="cls"></div>
					</div>
					<div>
						<div style='float: left'><input type="radio" name="monthSubType" id="monthSubType" value="02" onclick="toggleRepeatSelectCheck('32')"></div>
						<div class="inputSelectWrapper"><div id="monthSubTerm2Select"></div></div>								
						<div class="inputSelectWrapper"><div id="monthSubTerm3Select"></div></div>
						<div class="inputSelectText"><tctl:msg key="scheduler.repeat.at" bundle="scheduler"/></div>
						<div class="cls"></div>
					</div>
				</div>
				<div class="inputPane">
					<div style='float: left'>						
						<input type="radio" name="repeatType" id="repeatType" value="04" onclick="toggleRepeatSelectCheck('4')"> <tctl:msg key="scheduler.every.year" bundle="scheduler"/>
					</div>
					<div class="inputSelectWrapper"><div id="yearTerm1Select"></div></div>
					<div class="inputSelectWrapper"><div id="yearTerm2Select"></div></div>
					<div class="inputSelectText"><tctl:msg key="scheduler.repeat.at" bundle="scheduler"/></div>
					<div class="cls"></div>
				</div>
				<div class="inputPane">
					<input type="checkbox" name="checkEndDate" id="checkEndDate" onclick="toggleRepeatSelectCheck('end')">
					<tctl:msg key="scheduler.enddate.set" bundle="scheduler"/> : <input type="text" name="repeatEndDate" id="repeatEndDate" class="IP100px" readonly="readonly" style="width:90px">
				</div>
				<div class="inputPane" align="center" style="margin-top:10px">
					<a href="#" class="btn_basic" onclick="setRepeatSchedule()"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a>
					<a href="#" class="btn_basic" onclick="cancelRepeatSchedule()"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>						
				</div>
			</div>
			<div id="asset_control" class="assetControl">
				<div id="asset_control_content"></div>
				<div class="inputPane" align="center" style="margin-top:10px">
					<a href="#" class="btn_basic" onclick="setAssetSchedule()"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a>
					<a href="#" class="btn_basic" onclick="cancelAssetSchedule()"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>						
				</div>
			</div>
			</div>
			</div>
			
			<input type="text" name="location" id="location" class="IP100">			
			</td>
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.search.content" bundle="scheduler"/></th>
			<td><textarea name="content" id="content" class="TA100P"></textarea></td>
		</tr>
		<tr id="check_share_dot_tr">
			<th style="height:10px;">&nbsp;</th>
			<td><div class="dotLine"/></td>
		</tr>
		<tr id="check_share_tr">
			<th><tctl:msg key="scheduler.share" bundle="scheduler"/></th>
			<td style="padding-bottom:5px;">
				<input type="checkbox" id="checkShare" name="checkShare" onclick="checkUseShare()"/>
				<tctl:msg key="scheduler.share.check.info" bundle="scheduler"/>
				<div id="shareGroupWrap" style="display:none;padding-top:10px;">
					<div style="float:left;padding-left:5px;padding-top:3px;">
						<tctl:msg key="scheduler.share.select" bundle="scheduler"/>
					</div>
					<div style="float:left;padding-left:10px;">
						<div id="shareGroupSelect"></div>
					</div>
					<div style="float:left;padding-left:10px;width:50px;">
						<a class="btn_help" onMouseOver="viewShareGroupHelp()" onMouseOut="hideShareGroupHelp()"></a>
						<div class="schedulerGroupHelpWraper">
							<div id="schedulerHelp" class="schedulerGroupHelp">
								<tctl:msg key="scheduler.share.sharegroup.info" bundle="scheduler"/>
							</div>
						</div>
					</div>
					<div style="clear:both;"></div>
					<div style="padding:3px">
						<input type="checkbox" id="selfShareTarget" onclick="checkSelfTarget()"/>
						<tctl:msg key="scheduler.share.self.target" bundle="scheduler"/>
					</div>
					<div id="selfTargetWrap" style="display:none">
						<table cellpadding="0" cellspacing="0" style="table-layout:fixed;">
							<tr>	
								<td style="width:210px;padding:0">
									<div style="padding:3px;">
										<input type="text" id="targetId" class="IP100px" style="width:85px;padding-top:0px;height:17px;"/>@<input type="text" id="targetDomain" class="IP100px" style="padding-top:0px;height:17px;"/>
									</div>
									<div style="padding:3px;">
										<select id="shareTargetEmail" name="shareTargetEmail" style="width:200px;height:100px;" multiple="multiple"></select>
									</div>
								</td>
								<td style="padding:0" valign="top">
									<ul>
										<li style="padding-top:3px;"><a class="btn_style4" href="javascript:;" onclick="addTargetEmail();"><span><tctl:msg key="common.9" bundle="setting"/></span></a></li>
										<li style="padding-top:2px;"><a class="btn_style4" href="javascript:;" onclick="selectAll(document.inputScheduleForm.shareTargetEmail);"><span><tctl:msg key="common.13" bundle="setting"/></span></a></li>
										<li style="padding-top:2px;"><a class="btn_style4" href="javascript:;" onclick="deleteList(document.inputScheduleForm.shareTargetEmail);"><span><tctl:msg key="common.11" bundle="setting"/></span></a></li>
									</ul>
								</td>
							</tr>
						</table>
					</div>
					<div style="padding:3px">
						<input type="checkbox" id="sendMail" checked="checked"/>
						<tctl:msg key="scheduler.share.sendmail.001" bundle="scheduler"/>
					</div>
				</div>
			</td>
		</tr>
	</table>
	</div>
</form>
</div>

<div id="view_box" style="display:none">
	<table class="TB_schedule_reference" cellpadding="0" cellspacing="0">
		<tr id="view_title_tr">
			<th><tctl:msg key="scheduler.search.subject" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td id="view_title_title" style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><b><span id="view_title"></span></b></div>
						</td>
					</tr>
				</table>
			</td>	
		</tr>
		<tr id="view_date_tr">
			<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
			<td><span id="view_date" class="blue"></span></td>
		</tr>
		<tr id="view_repeat_tr">
			<th><tctl:msg key="scheduler.repeat.control" bundle="scheduler"/></th>
			<td><span id="view_repeat" style="color:red;font-size:11px"></span></td>
		</tr>
		<tr id="view_scheduler_asset_tr" style="display:none;">
			<th><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_scheduler_asset" style="color:green;font-size:11px"></span></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="view_scheduler_asset_contect_tr" style="display:none;">
			<th><tctl:msg key="scheduler.asset.006" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_scheduler_asset_contect" style="color:green;font-size:11px"></span></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="view_share_dot_tr">
			<th style="padding:0px;">&nbsp;</th>
			<td style="padding:0px;"><div class="dotLine" /></td>
		</tr>
		<tr id="view_share_user_tr" style="display:none;">
			<th><tctl:msg key="scheduler.asset.009" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_share_user_name" style="color:green;font-size:11px"></span></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="view_share_target_tr" style="display:none;">
			<th><tctl:msg key="scheduler.asset.010" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td id="view_share_target_title" style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_share_target_name" style="color:green;font-size:11px"></span></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="view_share_tr">
			<th><tctl:msg key="scheduler.share.title" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_share" style="color:green;font-size:11px"></span></div>
						</td>
					</tr>
				</table>
				<input type="hidden" id="check_send_mail" name="checkSendMail"/>
			</td>
		</tr>
		<tr id="view_location_dot_tr">
			<th style="padding:0px;">&nbsp;</th>
			<td style="padding:0px;"><div class="dotLine"/></td>
		</tr>
		<tr id="view_location_tr">
			<th><tctl:msg key="scheduler.location" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td id="view_location_title" style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><span id="view_location"></span></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="view_content_dot_tr">
			<th style="padding:0px;">&nbsp;</th>
			<td style="padding:0px;"><div class="dotLine" /></td>
		</tr>
		<tr id="view_content_tr">
			<th><tctl:msg key="scheduler.search.content" bundle="scheduler"/></th>
			<td><textarea name="viewContent" id="view_content" style="width:300px;height:120px;background-color:white;border:0px;" readonly="readonly"></textarea></td>
		</tr>
	</table>
</div>

<div id="share_box" style="display:none">
<form id="scheduleShareForm" name="scheduleShareForm" onsubmit="return false;">
	<input type="hidden" id="shareSeq" name="shareSeq"/>
	<input type="hidden" id="shareColor" name="shareColor"/>
	<table class="TB_scheduleShare" cellpadding="0" cellspacing="0">
		<tr>
			<th><tctl:msg key="scheduler.share.name" bundle="scheduler"/></th>	
			<td><input type="text" name="shareGroupName" id="shareGroupName" class="IP200"></td>
		</tr>
		<tr>
			<th style="border-bottom:1px solid #E8E8E8;"><tctl:msg key="scheduler.share.target" bundle="scheduler"/></th>	
			<td style="border-bottom:1px solid #E8E8E8;">
				<label>
					<input type="radio" name="shareType" id="shareTypeSelect" value="select" onclick="checkShareTargetMode()" checked="checked"/>
					<tctl:msg key="scheduler.share.target.select" bundle="scheduler"/>
				</label>
				<label>
					<input type="radio" name="shareType" id="shareTypeDomain" value="domain" onclick="checkShareTargetMode()"/>
					<tctl:msg key="scheduler.share.target.domain" bundle="scheduler"/>
				</label>
			</td>
		</tr>
	</table>
	<table class="TB_scheduleShare" cellpadding="0" cellspacing="0">
		<tr id="select_option_tr">
			<td colspan="2">
				<table class="TB_cols2_content">
					<tr>
						<td colspan="3">
							<div style="float:left">
								<div id="selectTypeSelect"></div>
							</div>
							<div id="share_notorg" style="float:left;padding-left:1px;">
								<input type="text" id="shareKeyword" name="shareKeyword" class="IP200" style="width:220px;" onKeyPress="(keyEvent(event) == 13) ? searchShareUser() : '';">
								<input type="text" name="_tmp" style="display:none"/>
							</div>
							<div id="share_org" style="float:left;padding-left:1px;display:none">
								<div id="selectOrgSelect"></div>
							</div>
							<div id="share_notorg" style="float:left;padding-left:1px;">
								<ul>
									<li id="share_notorg_btn" style="margin-top:0px;"><a href="#" onclick="searchShareUser()" class="btn_style4"><span><tctl:msg key="comn.search" bundle="common"/></span></a></li>
									<li id="searching_user" style="margin-top:4px;padding-left:5px;display:none"><font color="#7F9DB9"><tctl:msg key="scheduler.share.target.searching" bundle="scheduler"/>..</font></li>
									<li id="share_org_btn" style="margin-top:0px;display:none"><a href="#" onclick="setSharedOrgList()" class="btn_style4"><span><tctl:msg key="comn.add" bundle="common"/></span></a></li>
								</ul>
							</div>
						</td>
					</tr>
					<tr>
						<td style="width:210px">
							<select id="searchShare" name="searchShare" multiple="multiple" style="width:210px;height:100px;margin-top:10px;"></select>
						</td>
						<td align="center">
							<ul>
								<li style="margin-bottom:10px;"><a href="#" onclick="addSharedUser()" class="btn_leftmove"><span><tctl:msg key="comn.add" bundle="common"/></span></a></li>
								<li><a href="#" onclick="removeSharedUser()" class="btn_rightmove"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
							</ul>
						</td>
						<td style="width:210px">
							<select id="shareTarget" name="shareTarget" multiple="multiple" style="width:210px;height:100px;margin-top:10px;"></select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</div>

<div id="confirm_box" style="display:none">
<form id="scheduleRepeatForm" name="scheduleRepeatForm" onsubmit="return false;">
	<table class="TB_schedule_reference" cellpadding="0" cellspacing="0">
		<tr>
			<td><tctl:msg key="scheduler.repeat.select" bundle="scheduler"/></td>
		</tr>
		<tr>
			<td>
				<div class="modifyRepeat"><tctl:msg key="scheduler.repeat.modify.confirm" bundle="scheduler"/></div>
				<div class="deleteRepeat"><tctl:msg key="scheduler.repeat.delete.confirm" bundle="scheduler"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="modifyRepeatSub"><label><input type="radio" name="modifyMode" checked> <tctl:msg key="scheduler.repeat.modify.only" bundle="scheduler"/></label></div>
				<div class="modifyRepeatSub"><label><input type="radio" name="modifyMode" checked> <tctl:msg key="scheduler.repeat.modify.all" bundle="scheduler"/></label></div>
				<div class="deleteRepeatSub"><label><input type="radio" name="deleteMode" checked> <tctl:msg key="scheduler.repeat.delete.only" bundle="scheduler"/></label></div>
				<div class="deleteRepeatSub"><label><input type="radio" name="deleteMode" checked> <tctl:msg key="scheduler.repeat.delete.all" bundle="scheduler"/></label></div>
			</td>
		</tr>
	</table>
</form>
</div>

<div id="input_asset_box" style="display:none">
<form id="inputAssetScheduleForm" name="inputAssetScheduleForm">
<input type="hidden" name="planSeq" id="planSeq">
<input type="hidden" name="assetScheduleId" id="assetScheduleId"/>
<input type="hidden" name="assetSaveType" id="assetSaveType">
<input type="hidden" id="startAssetTime">
<input type="hidden" id="endAssetTime">
<input type="hidden" id="mobileNo" value="${mobileNo}"/>
	<table class="TB_scheduleAdd" cellpadding="0" cellspacing="0">
		<tr>
			<th><tctl:msg key="scheduler.asset.005" bundle="scheduler"/></th>	
			<td id="asset_user_name">${userName}</td>
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.asset.006" bundle="scheduler"/></th>	
			<td><input type="text" id="assetContect" name="assetContect" class="IP200" value="${mobileNo}" style="width:272px;"/></td>
		</tr>
		<tr>
			<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
			<td>
				<input type="text" id="inputAssetStartDate" class="IP_calendar" onclick="hideAllTimePicker()" readonly="readonly"/>
				<input type="text" id="inputAssetStartTime" class="IP_calendar2" onclick="toggleTimePicker('start_asset_time_picker')" readonly="readonly"/>
				<div style="position:relative;">
					<div id="start_asset_time_picker" class="timePick" style="left:140px;">
						<div style="float: left"><div id="start_asset_time_picker_ampmSelect"></div></div>
						<div style="float: left"><div id="start_asset_time_picker_timeSelect"></div></div>
						<div class="cls"></div>						
						<p align="center" style="margin-top:5px">
							<a href="#" class="btn_basic" onclick="setTimePicker('start_asset_time_picker','startAssetTime','inputAssetStartTime')"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a> 
							<a href="#" class="btn_basic" onclick="hideTimePicker('start_asset_time_picker')"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>
						</p>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th/>
			<td>	
				<input type="text" id="inputAssetEndDate" class="IP_calendar" onclick="hideAllTimePicker()" readonly="readonly"/>
				<input type="text" id="inputAssetEndTime" class="IP_calendar2" readonly="readonly" onclick="toggleTimePicker('end_asset_time_picker')"/>
				<div style="position:relative;">
					<div id="end_asset_time_picker" class="timePick" style="left:140px;">
						<div style="float: left"><div id="end_asset_time_picker_ampmSelect"></div></div>
						<div style="float: left"><div id="end_asset_time_picker_timeSelect"></div></div>
						<div class="cls"></div>
						<p align="center" style="margin-top:5px">
							<a href="#" class="btn_basic" onclick="setTimePicker('end_asset_time_picker','endAssetTime','inputAssetEndTime')"><span><tctl:msg key="scheduler.ok" bundle="scheduler"/></span></a> 
							<a href="#" class="btn_basic" onclick="hideTimePicker('end_asset_time_picker')"><span><tctl:msg key="scheduler.cancel" bundle="scheduler"/></span></a>
						</p>
					</div>
				</div>
			</td>
		</tr>
	</table>
</form>
</div>

<div id="asset_category_description_box" style="display:none">
	<table class="TB_schedule_reference" cellpadding="0" cellspacing="0" style="border:0;table-layout:fixed;">
		<tr>
			<td style="height:400px;"><div id="description_content" class="assetCategoryDescWrap"></div></td>
		</tr>
	</table>
</div>

<div id="view_asset_box" style="display:none">
	<table class="TB_schedule_reference" cellpadding="0" cellspacing="0">
		<tr id="view_asset_user_tr">
			<th><tctl:msg key="scheduler.asset.005" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td id="view_asset_user_title" style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><b><span id="view_asset_user"></span></b></div>
						</td>
					</tr>
				</table>
			</td>	
		</tr>
		<tr id="view_asset_contect_tr">
			<th><tctl:msg key="scheduler.asset.006" bundle="scheduler"/></th>
			<td style="padding-left:0px;">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td id="view_asset_contect_title" style="border:0;padding-top:0px;padding-bottom:0px;">
							<div class='TM_HiddenTextDiv'><b><span id="view_asset_contect"></span></b></div>
						</td>
					</tr>
				</table>
			</td>	
		</tr>
		<tr id="view_asset_date_tr">
			<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
			<td><span id="view_asset_date" class="blue"></span></td>
		</tr>
	</table>
</div>

<script language="javascript">
var selectArray = [];
selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.id" bundle="scheduler"/>',value:"uid"});
selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.name" bundle="scheduler"/>',value:"name"});
<c:if test="${orgUse}">
selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.org" bundle="scheduler"/>',value:"org"});
</c:if>

if(skin == "skin3"){
	jQuery(".mail_body_tabmenu").css({"border-bottom":"0px","background-color":"#ECEFF4"});
	jQuery(".tabMenu a span").hover(function(){jQuery(this).addClass("schedulerOn");},
			function(){jQuery(this).removeClass("schedulerOn");});
	jQuery(".tabMenu span").css({"padding-left":"5px","padding-right":"5px"});
	
}
</script>
<input type="hidden" id="cType" value="${type}">
<input type="hidden" id="calendarYear" value="${calendarYear}">
<input type="hidden" id="calendarMonth" value="${calendarMonth}">
<input type="hidden" id="calendarDay" value="${calendarDay}">
<input type="hidden" id="scheduleId" value="${scheduleId}">
<input type="hidden" id="calendarStartDate" value="${calendarStartDate}">
<input type="hidden" id="calendarEndDate" value="${calendarEndDate}">
<input type="hidden" id="currentPage" value="1">

</body>
</html>