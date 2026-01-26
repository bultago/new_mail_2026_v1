<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
response.setHeader("cache-control","no-cache"); 
response.setHeader("expires","0"); 
response.setHeader("pragma","no-cache");
%>
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/scheduler_style.css">
<link rel="stylesheet" type="text/css" href="/design/common/css/mail_tag_list.css" />
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/dynamic/scheduler/schedulerShare.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
<script language="javascript">
function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery(window).trigger("resize");
	
	jQuery("#scheduler_menu").addClass("on");

	var menuOpt = {
			overClass : "tcmenuOverClass",
	 		iconClass : "tcmenuIcon",
	 		blankImgSrc : "/design/common/image/blank.gif",
	 		subOverClass : "tcsubmenu",

	 		// menuLink
	 		menuLink : [
	 		            {name:comMsg.comn_modfy,link:modifySettingShareGroup},					
						{name:comMsg.comn_del,link:deleteSettingShareGroup}
	 			 	]
	};
	
	if(jQuery(".shareGroupWraper")){
		jQuery(".shareGroupWraper").folderManageMenu(menuOpt);		
	}		

	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
	jQuery(window).trigger("resize");
}

<%@ include file="settingFrameScript.jsp" %>

function addSettingShareGroup() {
	
	resetShareGroup();
	jQuery(".TB_scheduleShare").css("width", "0%");
	jQuery("#shareSeq").val("");
	jQuery("#shareGroupName").val("");
	jQuery("#shareColor").val("");

	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 300,
			minWidth:530,
			btnList : [{name:comMsg.comn_add,func:saveSettingShareGroup}],	
			openFunc:function(){
	 			jQuery(".TB_scheduleShare").css("width", "100%");
	 			jQuery("#share_box_pht").text(schedulerMsg.scheduler_share_add);		
			},
			beforeCloseFunc:function() {
				resetShareGroup();
			}
		};
	jQuery("#share_box").jQpopup("open",popupOpt);
}

function saveSettingShareGroup() {
	var f = document.scheduleShareForm;
	var shareGroupNameObj = jQuery("#shareGroupName");
	if(!checkInputLength("jQuery", shareGroupNameObj, schedulerMsg.scheduler_share_name_empty, 1, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", shareGroupNameObj, "shareName")) {
		return;
	}

	var shareName = jQuery.trim(shareGroupNameObj.val());

	var shareSeq = jQuery("#shareSeq").val();
	if (jQuery.trim(shareSeq) == "") {
		<c:if test="${!empty shareGroupList}">
			var count = 0;
			<c:forEach var="shareGroup" items="${shareGroupList}">
				if (shareName.toLowerCase() == "${fn:toLowerCase(shareGroup.shareName)}") {
					alert(schedulerMsg.scheduler_share_name_exist);
					return;
				}
				count++;
			</c:forEach>
	
			if (count >= 50) {
				alert(schedulerMsg.scheduler_share_add_maxgroup);
				return;
			}
		</c:if>
	} else {
		
		<c:if test="${!empty shareGroupList}">
			<c:forEach var="shareGroup" items="${shareGroupList}">
				if (jQuery.trim(shareSeq) != "${shareGroup.shareSeq}" && shareName.toLowerCase() == "${fn:toLowerCase(shareGroup.shareName)}") {
					alert(schedulerMsg.scheduler_share_name_exist);
					return;
				}
			</c:forEach>
		</c:if>
	}
	
	//추후 공유 색 지정 default:녹색
	var shareColor = "#008000";
	jQuery("#shareColor").val(shareColor);
	
	var targetType;
	var targetValueOptions = f.shareTarget.options;
	var targetValues = [];
	if (f.shareType[0].checked) {
		targetType = f.shareType[0].value;
		if(!targetValueOptions || targetValueOptions.length == 0){
			alert(schedulerMsg.scheduler_share_target_notarget);		
			return;
		}
		for (var i = 0; i < targetValueOptions.length ; i++) {
			targetValues[targetValues.length] = targetValueOptions[i].value;
		}
	} else {
		targetType = f.shareType[1].value;
	}

	selectAll(f.shareTarget);

	f.action = "/setting/saveSchedulerSetting.do";
	f.method = "post";
	f.submit();
	
	jQuery("#share_box").jQpopup("close");
}

function deleteSettingShareGroup(data) {
	var f = document.schedulerForm;
	
	if (!confirm(schedulerMsg.scheduler_share_alert_delete)) {
		return;
	} 

	var param = {"checkShareSeq":data.sid};
	setParamPAID(param);
	jQuery.post("/setting/deleteSchedulerSetting.do",param,
		function(data) {
			if (data.isSuccess) {
				alert(settingMsg.del_ok);
				window.location.reload();
			}
			else {
				alert(settingMsg.del_fail);
			}
		},"json");
	
}

function modifySettingShareGroup(data) {
	var param = {shareSeq:data.sid};
	jQuery.post("/setting/getJsonSchedulerShare.do",param,modifySharePopup,"json");
}

onloadRedy("init()");
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="schedulerForm">
<script type="text/javascript">makePAID();</script>
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.scheduler" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.scheduler.001" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
						<div>
							<div class="title_bar"><span><tctl:msg key="conf.scheduler.002" bundle="setting"/></span></div>
							<div class="sub_toolbar">
								<a href="#" onclick="addSettingShareGroup()" class="btn_style4"><span><tctl:msg key="common.new" bundle="setting"/></span></a>
							</div>
		
							<table id="settingListTable" cellpadding="0" cellspacing="0">
								<tr>
									<th><tctl:msg key="conf.scheduler.003" bundle="setting"/></th>
								</tr>
								<c:if test="${empty shareGroupList}">
								<tr>
									<td align="center"><tctl:msg key="conf.scheduler.004" bundle="setting"/></td>
								</tr>
								</c:if>
								<c:forEach var="shareGroup" items="${shareGroupList}">
								<tr>
									<td style="text-align:left">
										<div class="shareGroupWraper" fname="${shareGroup.shareName}" sid="${shareGroup.shareSeq}" tcolor="${shareGroup.shareColor}">
											<div class="tagimg_base timg_${fn:substring(shareGroup.shareColor,1,-1)}" style="float: left;"></div>
											<span style="margin-left:10px;">${shareGroup.shareName}</span>
										</div>
									</td>
								</tr>
								</c:forEach>
							</table>
							<div style="height: 50px;"></div>
						</div>		
					</div>
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>		
</form>
</div>

<div id="share_box" style="display:none">
<form id="scheduleShareForm" name="scheduleShareForm">
	<input type="hidden" id="shareSeq" name="shareSeq"/>
	<input type="hidden" id="shareColor" name="shareColor"/>
	<table class="TB_scheduleShare" cellpadding="0" cellspacing="0">
		<tr>
			<th class="alignLeft"><tctl:msg key="scheduler.share.name" bundle="scheduler"/></th>	
			<td><input type="text" name="shareGroupName" id="shareGroupName" class="IP200"></td>
		</tr>
		<tr>
			<th class="alignLeft" style="border-bottom:1px solid #E8E8E8;"><tctl:msg key="scheduler.share.target" bundle="scheduler"/></th>	
			<td style="border-bottom:1px solid #E8E8E8;">
				<label>
					<input type="radio" name="shareType" value="select" onclick="checkShareTargetMode()" checked />
					<tctl:msg key="scheduler.share.target.select" bundle="scheduler"/>
				</label>
				<label>
					<input type="radio" name="shareType" value="domain" onclick="checkShareTargetMode()"/>
					<tctl:msg key="scheduler.share.target.domain" bundle="scheduler"/>
				</label>
			</td>
		</tr>
		<tr id="select_option_tr">
			<td colspan="2">
				<table class="TB_cols2_content" cellpadding="0" cellspacing="0">
					<tr>
						<td colspan="3">
							<div style="float:left">
								<div id="selectTypeSelect"></div>
								<script type="text/javascript">
									var selectArray = [];
									selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.id" bundle="scheduler"/>',value:"uid"});
									selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.name" bundle="scheduler"/>',value:"name"});
									if(MENU_STATUS.org && MENU_STATUS.org == "on") {
									<c:if test="${orgUse}">
									selectArray.push({index:'<tctl:msg key="scheduler.share.target.search.org" bundle="scheduler"/>',value:"org"});
									</c:if>
									}
								</script>
							</div>
							<div id="share_notorg" style="float:left;padding-left:1px;">
								<input type="text" id="shareKeyword" name="shareKeyword" class="IP200" style="width:220px;" onKeyPress="(keyEvent(event) == 13) ? searchShareUser() : '';">
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
<script language="javascript">
jQuery("#selectTypeSelect").selectbox({selectId:"shareSearchType",selectFunc:checkShareSelectType,width:80}, "uid", selectArray);
</script>

<%@include file="/common/bottom.jsp"%>

</body>
</html>