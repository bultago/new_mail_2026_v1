<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">

<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>


<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>


<script language="javascript">
function init(){	
	<%@ include file="settingCommonScript.jsp" %>

	if(skin == "skin3"){
		jQuery("#mailMenubar").css("margin-top","5px");
		jQuery(".mail_body_menu").css({"height":"64px","border":"1px solid #C8CDD1"});
		jQuery(".menu_main_unit").css("border-right","0px");
	}
	
	var backupInfo = ${backupInfo.json};	
	loadManage(backupInfo);	

	jQuery("#folder_manage_menu").addClass("on");
	jQuery(".TM_manage_graphBox").show();	
	jQuery.removeProcessBodyMask();
	
	checkSearchAllFolderMsg("all");

}

<%@ include file="settingFrameScript.jsp" %>

var mailSearchConfig = ${mailSearchConfig};
var isSearchAllFolder = ${isSearchAllFolder};
var LayoutInfo = {mode:"manage"};
var ufolderList = [];
var popupOpt = {
	closeName:comMsg.comn_close,
	btnClass:"btn_style3"			
};


function viewFolder(folder,param){
	jQuery("#goFolderform").append(jQuery("<input type=hidden name='folder' value='"+folder+"'>"));
	if(param){
		var paramList = param.split("&");
		for ( var i = 0; i < paramList.length; i++) {
			var paramValues = paramList[i].split("=");
			jQuery("#goFolderform").append(jQuery("<input type=hidden name='"+paramValues[0]+"' value='"+paramValues[1]+"'>"));
		}
	}
	
	document.goFolderform.action = "/dynamic/mail/mailCommon.do";
	document.goFolderform.method="post";
	document.goFolderform.submit();	
	 	
}

function setTagForm(name,color,oldId){
	$("tagName").value = name;
	$("tagColor").value = color;	
	$("oldTagId").value = oldId;	
}

function setSearchFolderForm(folderName, id, querys){
	if(!querys){querys = "";}
	var values = querys.split("/%");
	var sfolder = values[0];	
	var queryCondition = values[1];
	var pattern = values[2];
	var from = values[3];
	var to = values[4];
	var flag = values[5];
	
	if(folderName){
		$("searchFolderName").value = folderName;
	} else {
		$("searchFolderName").value = "";
	}	
	jQuery("#s2folderSelect").empty();	
	jQuery("#s2folderSelect").folderSelectList("s2folder",ufolderList,sfolder,'s',checkSearchAllFolderMsg);
	
	var searchCondition = [];
	searchCondition.push({index:mailMsg.search_subject,value:"s"});
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_body,value:"b"});
	}
	searchCondition.push({index:mailMsg.search_attname,value:"af"});
	if(mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_attcontent,value:"ab"});
	}	
	if(mailSearchConfig.bodySearch == "on"){
		searchCondition.push({index:mailMsg.search_sbody,value:"sb"});
	}	
	searchCondition.push({index:mailMsg.search_sattname,value:"saf"});
	if(mailSearchConfig.bodySearch == "on" && mailSearchConfig.attachSearch == "on"){
		searchCondition.push({index:mailMsg.search_sattcontent,value:"sab"});
	}
	
	jQuery("#searchFolderConditionSelect").empty();
	jQuery("#searchFolderConditionSelect").selectbox({selectId:"searchFolderCondition",selectFunc:"",width:150},
			queryCondition,searchCondition);
	
	
	if(!from){from = "";}
	$("searchFolderFrom").value = from;
	
	if(!to){to = "";}
	$("searchFolderTo").value = to;
	
	if(!pattern){pattern = "";}	
	$("searchFolderPattern").value = pattern;
	
	if(!id){id = "";} 
	$("oldSFolderId").value = id;
	
	if (!flag){flag = "";}
	var searchFolderFlagObj = $("searchFolderForm").searchFolderFlag;
	for (var i=0; i<searchFolderFlagObj.length; i++) {
		if (searchFolderFlagObj[i].value == flag) {
			searchFolderFlagObj[i].checked = true;
			break;
		}
	}
	
	var stype = sfolder;
	if (querys == "") stype = "all";
	checkSearchAllFolderMsg(stype);
}

function checkSearchAllFolderMsg(val) {
	var searchId = "sfolderTargetDesc";
	if (jQuery("#m_searchFolderForm").css("display") != "none") {
		searchId = "s2AllfolderDesc";
	}
	if (val == "all") {
		var searchAllfolderMsg = settingMsg.conf_profile_search_folder;
		if (!isSearchAllFolder) {
			searchAllfolderMsg = settingMsg.conf_profile_search_folder_exc;
		}
		jQuery("#"+searchId).text(searchAllfolderMsg);
	} else {
		jQuery("#"+searchId).text("");
	}
}
</script>
<style type="text/css">
table.sfInnerTable {border:0px}
table.sfInnerTable td {border:0px;padding:5px;}
</style>
</head>
<body onload="init()">
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="forwardForm">
		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="mail.foldermgnt"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			
			<div id="mailMenubar" >
				<div class="mail_body_menu" style="border-top:#ACACAC solid 1px;">
					<div class="menu_main_unit" id="menuBarContent">
						<div id='backup_info' class='TM_backupInfo'></div>
					</div>
				</div>
			</div>	
						

			<div style="margin-top: 5px;"></div>

			<div id="s_contentBody" >
				<div id="s_contentMain" >
					<div id="main_wrapper" class="smain_content_wrapper">					
					<div>
					<div class="title_bar title_bar_br"><span><tctl:msg key="mail.foldermgnt"/></span></div>
					<div class="TM_listWrapper">
					<table cellpadding="0" cellspacing="0" class="TB_mailAdmin">
						<tbody>
							<tr>
								<th class="mail_box"><tctl:msg key="mail.folder"/></th>
								<th class="mail_aging"><tctl:msg key="mail.aging"/></th>
								<th class="mail_count"><tctl:msg key="mail.count"/></th>			
								<th class="mail_capacity"><tctl:msg key="mail.quota"/></th>
							</tr>
							<tr>
								<th colspan="4" class="user_mailBox alignLeft">
									<strong><tctl:msg key="mail.defaultbox"/></strong>				
								</th>
							</tr>							
							<c:forEach var="dfolder" items="${dfolders}">
							<tr>
								<td colspan="2">				
									<div class="TM_fm_dfolder_wrap" fname="${dfolder.encName}" fullname="${dfolder.fullName}" depth="${dfolder.depth}">					
										<div class="tcfolder"></div>										
										<a href="#n" class="TM_mfolder" onclick="viewFolder('${dfolder.fullName}')">${fn:escapeXml(dfolder.name)}</a>
									</div>
								</td>
								<td class="alignCenter"><strong class="orange">${dfolder.unseenCnt}</strong><tctl:msg key="mail.countunit"/>/${dfolder.totalCnt}<tctl:msg key="mail.countunit"/></td>
								<td class="TD_bar last" align="center">
									<div class="TM_manage_graphBox" style="display:none">
										<div class="TM_graphBar" style="width:${dfolder.quota.percent}%"></div>
										<div class="TM_capacity">${dfolder.quota.usageUnit}</div>
										&nbsp;					
									</div>
								</td>
							</tr>
							<script>ufolderList[ufolderList.length] =  {"depth": 0,"fname":"${dfolder.fullName}","name":escape_tag("${dfolder.name}"),"ename":"${dfolder.encName}"};</script>
							</c:forEach>
						</tbody>
					</table>
					</div>
					<div class="TM_listWrapper">	
					<table cellpadding="0" cellspacing="0" class="TB_mailAdmin">
						<col></col>
						<col width="100px"></col>
						<col width="100px"></col>
						<col width="160px"></col>
						<tbody>							
							<tr>
								<th colspan="4" class="user_mailBox alignLeft">
									<strong><tctl:msg key="mail.userbox"/></strong>
									<input type="text"  id="ufolderMain"  class="basicInput" style="width:200px"/>
									<a href="javascript:;" class="btn_basic" onclick="addMainFolderService()"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
								</th>
							</tr>
							<c:forEach var="ufolder" items="${ufolders}" varStatus="info">
							<tr id="ufolder_tr_${info.index}">
								<td>
									<div fname="${ufolder.encName}" fullname="${ufolder.fullName}" depth="${ufolder.depth}" shared="${ufolder.share}" sharedUid="${ufolder.sharedUid}" class="TM_fm_ufolder_wrap">						
										<c:if test="${!ufolder.share}">
										<div class="tcfolder"></div>
										</c:if>
										<c:if test="${ufolder.share}">
										<div class="tsfolder"></div>
										</c:if>		
										<a href="#n" class="TM_mfolder" id="ufolder_link_${info.index}" onclick="viewFolder('${ufolder.fullName}')"></a>					
									</div>	
									<script type="text/javascript">
										jQuery("#ufolder_link_${info.index}").append(escape_tag('${ufolder.name}'));
									</script>										
								</td>	
								<td>
									<div style="float: left;">		
										<div id="againg_${info.index}_select" aging="${ufolder.againg}"></div>
									</div>
									<div class="cls"></div>
									<script type="text/javascript">
									jQuery("#againg_${info.index}_select").selectbox({selectId:"againg_${info.index}",width:90,
											selectFunc:function(val){changeAging('${info.index}','${ufolder.encName}',val);}},
											'${ufolder.againg}',
											[{index:mailMsg.folder_aging_noaging,value:'-1'},
											 {index:mailMsg.folder_aging_unlimited,value:'0'},
											 {index:mailMsg.folder_aging_30,value:'30'},
											 {index:mailMsg.folder_aging_90,value:'90'},
											 {index:mailMsg.folder_aging_120,value:'100'}]);		
									</script>			
													
								</td>		
								<td class="alignCenter">
									<c:if test="${ufolder.unseenCnt < 0 || ufolder.totalCnt < 0}">
										<tctl:msg key="comn.loadding" bundle="common"/>
									</c:if>
									<c:if test="${ufolder.unseenCnt > -1 && ufolder.totalCnt > -1}">
									<strong class="orange">${ufolder.unseenCnt}</strong><tctl:msg key="mail.countunit"/>/${ufolder.totalCnt}<tctl:msg key="mail.countunit"/>
									</c:if>
								</td>
								<td class="TD_bar last" align="center">									
									<div class="TM_manage_graphBox" style="display:none;">
										<div class="TM_graphBar" style="width:${ufolder.quota.percent}%"></div>										
										<div class="TM_capacity">${ufolder.quota.usageUnit}</div>
										&nbsp;					
									</div>										
								</td>
							</tr>
							<script>ufolderList[ufolderList.length] =  {"depth": "${ufolder.depth}","fname":"${ufolder.fullName}","name":escape_tag("${ufolder.name}"),"ename":"${ufolder.encName}"};</script>
							</c:forEach>							
						</tbody>
					</table>
					</div>
					
					<div class="title_bar title_bar_br"><span><tctl:msg key="mail.sfoldermgnt"/></span></div>					
					<div class="TM_listWrapper">
					<table cellpadding="0" cellspacing="0" class="TB_mailAdmin">
						<colgroup>
							<col width="200px;"></col>
							<col></col>
						</colgroup>
						<tbody>
							<tr>
								<th><tctl:msg key="mail.sfolder.add.title"/></th>
								<td>
									<table class="sfInnerTable" style="border:0px;">
										<colgroup>
											<col width="150px;"></col>
											<col></col>
										</colgroup>
										<tr>
											<td align="right"><tctl:msg key="mail.sfolder.name"/></td>
											<td><input type="text" id="sfolderName" class="IP300"/></td>
										</tr>
										<tr>
											<td align="right"><tctl:msg key="mail.from"/></td>
											<td><input type="text" id="sfolderFrom" class="IP300"/></td>
										</tr>
										<tr>
											<td align="right"><tctl:msg key="mail.to"/> (<tctl:msg key="mail.sfolder.include.001"/>)</td>
											<td><input type="text" id="sfolderTo" class="IP300"/></td>
										</tr>
										<tr>
											<td align="right"><tctl:msg key="mail.sword"/></td>
											<td>			
												<div style="float: left;"><div id="sfolderConditionSelect"></div></div>			
												<div style="float: left; margin-left:3px;">
												<input type="text" id="sfolderValue" class="IP100px" style="width:145px;"/>
												</div>		
											</td>
										</tr>
										<tr>
											<td align="right"><tctl:msg key="mail.folder"/></td>
											<td>
												<div style="float: left;"><div id="sfolderTargetSelect"></div></div>
												<div style="float: left; margin-left:3px;">
													<div id="sfolderTargetDesc"></div>
												</div>
											</td>
										</tr>
										<tr>
											<td align="right"><tctl:msg key="mail.sfolder.add.condition"/></td>
											<td>
												<label><input type="radio" name="searchFolderFlag" value="" checked/> <tctl:msg key="mail.mdn.notselect"/></label>
												<label><input type="radio" name="searchFolderFlag" value="F"/> <tctl:msg key="menu.quick.flag"/></label>
												<label><input type="radio" name="searchFolderFlag" value="T"/> <tctl:msg key="menu.quick.attach"/></label>
												<label><input type="radio" name="searchFolderFlag" value="S"/> <tctl:msg key="menu.quick.read"/></label>
												<label><input type="radio" name="searchFolderFlag" value="U"/> <tctl:msg key="menu.quick.unread"/></label>
											</td>
										</tr>
										<tr>
											<td colspan="2" align="center">
												<a href="javascript:;" onclick="addMainSearchFolderService()" class="btn_basic"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<c:forEach var="sfolder" items="${sfolders}">
							<tr>
								<td>
									<div class="TM_fm_sfolder_wrap" sid="${sfolder.id}" qval="${sfolder.query}" fname="${sfolder.name}">
										<div class="tcfolder"></div>						
										<a href="#n" class="TM_mfolder" onclick="viewSearchFolder(this)" query="${sfolder.query}">${fn:escapeXml(sfolder.name)}</a>
									</div>
								</td>
								<td><span class="TM_sfolder_query">${fn:escapeXml(sfolder.query)}</span></td>
							</tr>
							</c:forEach>							
						</tbody>
					</table>
					</div>
					
					<div class="title_bar title_bar_br"><span><tctl:msg key="mail.tagmgnt"/></span></div>					
					<div class="TM_listWrapper">
					<table cellpadding="0" cellspacing="0" class="TB_tagAdmin">
						<tbody>
							<tr>
								<th class="alignLeft">
									<a href="javascript:;" onclick="addMTag()" class="btn_basic"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
								</th>			
							</tr>		
							<c:forEach var="tag" items="${tags}">
							<tr>
								<td>
									<div class="TM_fm_tag_wrap" fname="${tag.name}" sid="${tag.id}" tcolor="${tag.color}">
										<div class="tagimg_base timg_${fn:toLowerCase(fn:replace(tag.color,'#',''))}" style="float:left"></div>										
										<a href="#n" onclick="viewTagMessage('${tag.id}')" class="tagManageList">${fn:escapeXml(tag.name)}</a>										
									</div>				
								</td>			
							</tr>	
							</c:forEach>					
						</tbody>
					</table>
					</div>
					<div style='height:50px;'></div>
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

<form name="goFolderform" id="goFolderform">
	<input type="hidden" name="workName" value="golistparam"/>
</form>

<%@include file="/dynamic/mail/mailCommonModal.jsp"%>
<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style='display:none'></iframe>
</body>
</html>
