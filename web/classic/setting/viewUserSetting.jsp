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
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=settingMsg&locale=<%=locale%>"></script>
<script language = "javascript">
function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery("#setting_menu").addClass("on");	
	jQuery("#s_contentMain").css("overflow","auto");

	jQuery("#userLocaleSelect").selectbox({selectId:"userLocale",selectFunc:"",width:140},
			"${userEtcInfoVo.userLocale}",
			[{index:settingMsg.conf_profile_23,value:"ko"},
			 {index:settingMsg.conf_profile_24,value:"en"},
			 {index:settingMsg.conf_profile_language_japanese,value:"jp"}]);	

	jQuery("#pageLineCntSelect").selectbox({selectId:"pageLineCnt",selectFunc:"",width:70},
		"${userEtcInfoVo.pageLineCnt}",
		[{index:15+settingMsg.conf_profile_13,value:"15"},
		 {index:20+settingMsg.conf_profile_13,value:"20"},
		 {index:25+settingMsg.conf_profile_13,value:"25"},
		 {index:30+settingMsg.conf_profile_13,value:"30"},
		 {index:50+settingMsg.conf_profile_13,value:"50"},
		 {index:80+settingMsg.conf_profile_13,value:"80"}]);

	var activeXArray = [];

		jQuery("#activex_title").text("");
		jQuery("#activex_title").css("border-right","0").css("background-color","#ffffff");
		jQuery("#activeXUseSelect").hide();
	
		activeXArray.push({index:settingMsg.conf_profile_75,value:"enable"});
		activeXArray.push({index:settingMsg.conf_profile_76,value:"disable"});
	jQuery("#activeXUseSelect").selectbox({selectId:"activeXUse",selectFunc:""},
			"${userEtcInfoVo.activeXUse}",activeXArray);

	jQuery("#notiIntervalSelect").selectbox({selectId:"notiInterval",selectFunc:""},
			"${userEtcInfoVo.notiInterval}",
			[{index:settingMsg.conf_profile_38,value:"0"},
			 {index:1+settingMsg.conf_profile_39,value:"1"},
			 {index:5+settingMsg.conf_profile_39,value:"5"},
			 {index:10+settingMsg.conf_profile_39,value:"10"},
			 {index:30+settingMsg.conf_profile_39,value:"30"},
			 {index:1+settingMsg.conf_profile_40,value:"60"}]);
	
	
	jQuery("#saveSendBoxSelect").selectbox({selectId:"saveSendBox",selectFunc:""},
			"${userEtcInfoVo.saveSendBox}",
			[{index:settingMsg.common_20,value:"on"},
			 {index:settingMsg.common_23,value:"off"}]);

	jQuery("#receiveNotiSelect").selectbox({selectId:"receiveNoti",selectFunc:""},
			"${userEtcInfoVo.receiveNoti}",
			[{index:settingMsg.conf_profile_receipt_ok,value:"on"},
			 {index:settingMsg.conf_profile_receipt_no,value:"off"}]);

	jQuery("#wmodeSelect").selectbox({selectId:"wmode",selectFunc:""},
		"${userEtcInfoVo.writeMode}",
		[{index:"HTML",value:"html"},
		 {index:"TEXT",value:"text"}]);

	jQuery("#composeModeSelect").selectbox({selectId:"composeMode",selectFunc:""},
			"${userEtcInfoVo.composeMode}",
			[{index:settingMsg.conf_profile_72,value:"normal"},
			 {index:settingMsg.conf_profile_73,value:"popup"}]);

	jQuery("#encodingSelect").selectbox({selectId:"encoding",selectFunc:""},
			"${userEtcInfoVo.charSet}",
			[{index:settingMsg.conf_profile_34,value:"UTF-8"},
			 {index:settingMsg.conf_profile_27,value:"EUC-KR"},
			 {index:settingMsg.conf_profile_29,value:"US-ASCII"},
			 {index:settingMsg.conf_profile_28,value:"ISO-2022-JP"},
			 {index:settingMsg.conf_profile_32,value:"GB2312"},
			 {index:settingMsg.conf_profile_33,value:"BIG5"}
			 ]);

	jQuery("#writeNotiSelect").selectbox({selectId:"writeNoti",selectFunc:""},
			"${userEtcInfoVo.writeNoti}",
			[{index:settingMsg.conf_profile_75,value:"enable"},
			 {index:settingMsg.conf_profile_76,value:"disable"}]);

	jQuery("#renderModeSelect").selectbox({selectId:"renderMode",selectFunc:""},
			"${userEtcInfoVo.renderMode}",
			[{index:"AJAX",value:"ajax"},
			 {index:"HTML",value:"html"}]);
	 	
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);		
}

<%@ include file="settingFrameScript.jsp" %>

function saveUserSetting() {
	var f = document.settingForm;

	if(!checkInputLength("", f.senderName, "", 0, 48)) {
		return;
	}
	if(!checkInputValidate("", f.senderName, "senderName")) {
		return;
	}

	if (trim(f.senderEmail.value) != "") {
		if(!isEmail(f.senderEmail.value)){
			alert(settingMsg.common_2);
			f.senderEmail.focus();
	        return;
		}
	}

	var saveSent = $("saveSendBox").value;
	var receiveNoti = $("receiveNoti").value; 
	if(saveSent == "off" && receiveNoti == "on"){
		if(confirm(settingMsg.conf_alert_userinfo_receivenoti)){
			$("receiveNoti").value="on";
			$("saveSendBox").value="on";
		} else {
			return;
		}
	}

	f.action = "/setting/modifySetting.do";
	f.method = "post";
	f.submit();
}

function resetUserSetting() {
	var f = document.settingForm;
	f.reset();

	jQuery("#userLocaleSelect").selectboxSetValue("${userEtcInfoVo.userLocale}");
	jQuery("#pageLineCntSelect").selectboxSetValue("${userEtcInfoVo.pageLineCnt}");
	jQuery("#activeXUseSelect").selectboxSetValue("false");
	jQuery("#notiIntervalSelect").selectboxSetValue("${userEtcInfoVo.notiInterval}");	
	jQuery("#saveSendBoxSelect").selectboxSetValue("${userEtcInfoVo.saveSendBox}");
	jQuery("#receiveNotiSelect").selectboxSetValue("${userEtcInfoVo.receiveNoti}");
	jQuery("#wmodeSelect").selectboxSetValue("${userEtcInfoVo.writeMode}");
	jQuery("#composeModeSelect").selectboxSetValue("${userEtcInfoVo.composeMode}");
	jQuery("#writeNotiSelect").selectboxSetValue("${userEtcInfoVo.writeNoti}");
	jQuery("#encodingSelect").selectboxSetValue("${userEtcInfoVo.charSet}");
	jQuery("#renderModeSelect").selectboxSetValue("${userEtcInfoVo.renderMode}");
}

function defaultUserSetting() {
	var f = document.settingForm;
	if (LOCALE == 'ko') {
		jQuery("#userLocaleSelect").selectboxSetIndex(0);
	}else if (LOCALE == 'jp') {
		jQuery("#userLocaleSelect").selectboxSetIndex(2);
	} else {
		jQuery("#userLocaleSelect").selectboxSetIndex(1);
	}

	jQuery("#renderModeSelect").selectboxSetIndex(0);
	jQuery("#pageLineCntSelect").selectboxSetIndex(0);
	jQuery("#notiIntervalSelect").selectboxSetIndex(0);	
	f.searchAllFolder.checked = false;
	f.hiddenImg.checked = true;
	f.hiddenTag.checked = true;
	f.wmode.selectedIndex = 0;
	jQuery("#saveSendBoxSelect").selectboxSetIndex(0);
	jQuery("#receiveNotiSelect").selectboxSetIndex(0);
	jQuery("#encodingSelect").selectboxSetIndex(0);
	f.senderName.value = "${userName}";
	f.senderEmail.value = "${email}";
	f.vcardApply.checked = false;
}

function manageVcard() {
	this.location = "/setting/viewVcardInfo.do";
}

function DivChange(str){
	displayDetail = document.getElementById("detail" + str)
	if(displayDetail.style.display=="inline"){
		displayDetail.style.display = "none"
	}else{
		displayDetail.style.display = "inline"
	}
}
onloadRedy("init()");
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">

<form name="settingForm">	
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>
		
		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">		
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="conf.profile.title" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.profile.1" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.profile.54" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><tctl:msg key="conf.profile.22" bundle="setting"/></th>
							<td width="50%">								
								<div id="userLocaleSelect"></div>								
							</td>
							<th><tctl:msg key="conf.profile.3" bundle="setting"/></th>
							<td width="50%" class="last">
								<div id="pageLineCntSelect"></div>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.37" bundle="setting"/></th>
							<td width="50%">								
								<div id="notiIntervalSelect"></div>
								<input type="hidden" name="afterLogin" value="${userEtcInfoVo.afterLogin}"/>
							</td>
							<th><tctl:msg key="conf.profile.78" bundle="setting"/></th>
							<td width="50%" class="last">
								<div id="renderModeSelect"></div>
							</td>								
						</tr>
						<tr class="last">
							<th><tctl:msg key="conf.profile.search.option" bundle="setting"/></th>
							<td width="50%">
								<label>
									<input type="checkbox" name="searchAllFolder" <c:if test="${userEtcInfoVo.searchAllFolder == 'on'}">checked</c:if> /> <span><tctl:msg key="conf.profile.search.folder" bundle="setting"/></span>
								</label>
							</td>
							<th id="activex_title"><tctl:msg key="conf.profile.77" bundle="setting"/></th>
							<td width="50%" class="last">
								<div id="activeXUseSelect"></div>
							</td>						
						</tr>
					</table>

					<div class="title_bar"><span><tctl:msg key="conf.profile.2" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">			
						<tr>
							<th><tctl:msg key="conf.profile.44" bundle="setting"/></th>
							<td colspan="3">
								<label>
									<input type="checkbox" name="hiddenImg" <c:if test="${userEtcInfoVo.hiddenImg == 'on'}">checked</c:if>/> <span><tctl:msg key="conf.profile.46" bundle="setting"/></span>
								</label>
							</td>
						</tr>
						<tr class="last">
							<th><tctl:msg key="conf.profile.45" bundle="setting"/></th>
							<td colspan="3">
								<label>
									<input type="checkbox" name="hiddenTag" <c:if test="${userEtcInfoVo.hiddenTag == 'on'}">checked</c:if>/> <span><tctl:msg key="conf.profile.47" bundle="setting"/></span>
								</label>
								<a class="btn_help" title="help" onMouseOver="DivChange('01')" onMouseOut="DivChange('01')"></a>
						<!-- help message -->
								<div id="detail01" style="display:none; position:absolute"> 
									<div id=pop style="top:-50px;left:10px;position:absolute;border:1 solid #C3B997;background-color:#FDFBF4;color:#6B4A1B;padding:5;line-height:14px;font-size:11px">
										<table width="110px">
											<tr>
												<td>
													<u><b><tctl:msg key="conf.profile.48" bundle="setting"/></b></u>
												</td>
											</tr>
											<tr>
												<td style="padding-left: 20px">
												<li>HTML
												<li>HEAD
												<li>META
												<li>BODY
												<li>SCRIPT
												<li>IFRAME
												<li>EMBED
												</td>
											</tr>
										</table>
									</div>
								</div> 
							</td>
						</tr>
					</table>
					
					<div class="title_bar"><span><tctl:msg key="conf.profile.4" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><tctl:msg key="conf.profile.5" bundle="setting"/></th>
							<td width="50%">								
								<div id="saveSendBoxSelect"></div>
							</td>
							<th><tctl:msg key="conf.profile.7" bundle="setting"/></th>
							<td width="50%">
								<div id="receiveNotiSelect"></div>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.25" bundle="setting"/></th>
							<td>								
								<div id="wmodeSelect"></div>
								<input type="hidden" name="forwardingMode" value="${userEtcInfoVo.forwardingMode}">
							</td>								
							
							<th><tctl:msg key="conf.profile.71" bundle="setting"/></th>
							<td class="last">									
								<div id="composeModeSelect"></div>
							</td>							
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.26" bundle="setting"/></th>
							<td>								
								<div id="encodingSelect"></div>
							</td>
							
							<th><tctl:msg key="conf.profile.74" bundle="setting"/></th>
							<td class="last">									
								<div id="writeNotiSelect"></div>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.9" bundle="setting"/></th>
							<td colspan="3" style="padding-top: 3px;">
								<input type="text" name="senderName" class="IP200" maxlength="20" value="${userEtcInfoVo.senderName}"/>
								<p style="padding:3px 0px"><font color="#0099ff"><tctl:msg key="conf.profile.10" bundle="setting"/></font></p>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.53" bundle="setting"/></th>
							<td colspan="3">
								<input type="text" class="IP200" name="senderEmail" value="${userEtcInfoVo.senderEmail}"/>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="conf.profile.11" bundle="setting"/></th>
							<td colspan="3">
								<label><input type="checkbox" name="vcardApply" <c:if test="${userEtcInfoVo.vcardAttach == 'on'}">checked</c:if> /> <span class="ch_ment"><tctl:msg key="conf.profile.12" bundle="setting"/></span></label>
								<a href="#" onclick="manageVcard()" class="btn_TB_modify"><span><tctl:msg key="conf.sign.13" bundle="setting"/></span></a>
							</td>
						</tr>
					</table>		
					<table collspan="0" cellpadding="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="saveUserSetting()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetUserSetting()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="defaultUserSetting()"><span><tctl:msg key="common.default" bundle="setting"/></span></a>
						</td></tr>
					</table>
					</div>
					</div>
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>		
</form>
</div>

<%@include file="/common/bottom.jsp"%>

</body>
</html>