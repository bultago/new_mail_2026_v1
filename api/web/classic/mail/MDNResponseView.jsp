<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />

<script type="text/javascript" src="/js/ext-lib/jquery.colorpicker.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.datepick.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dftree.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-menuLayer.js"></script>
<script type="text/javascript" src="/js/common-lib/common-form.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>


<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailBasicCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>
<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<style type="text/css">html {*overflow-y:scroll;}</style>
<script type="text/javascript">
setAlMailMode(true);
<%@include file="userSetting.jsp"%>
LayoutInfo.mode = "mdnread";

var menuBar;
var paneMode;
LayoutInfo.mode = "mdnread";

var chkIdx = 0;
chkHash = new Hash();

function allChkEmail(thisObj){
	var isChk = thisObj.checked;
	var f = document.mdnForm;
	var chkList = f.recallEmail;
	if(chkList){
		if(chkList.length > 1){
			for ( var i = 0; i < chkList.length; i++) {
				if(chkList[i].checked != isChk){
					chkList[i].checked = isChk;
					chkEmail(chkList[i]);
				}
			}
		} else {
			if(chkList.checked != isChk){
				chkList.checked = isChk;
				chkEmail(chkList);
			}			
		}
	}	
}

function recallMsg(){
	if(chkIdx == 0){
		alert(comMsg.comn_error_001);
		return;
	}
	var vals = chkHash.keys();
	var rmailStr = "";

	for ( var i = 0; i < vals.length; i++) {		
		if(i > 0){
			rmailStr += "|";
		}
		rmailStr += vals[i];				
	}

	if (!confirm(mailMsg.mail_mdn_confirm)) {
		return;
	}

	var param = {};
	param.mid = $("messageId").value;
	param.uid = $("uid").value;
	param.rmailStr = rmailStr;
		
	mailControl.recallMessage(param);
	
}

function chkEmail(chkObj){
	var isChk = chkObj.checked;
	var chkVal = chkObj.value;
	if(isChk){
		chkHash.set(chkVal,chkVal);
		chkIdx++;
	} else {
		chkHash.unset(chkVal);
		chkIdx--;
	}
}

function init() {

	setTopMenu('mail');
	
	mainInitFunc();
	folderInitLoad();
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}	
	
	var total = ${mdnBean.mdnResponseTotal};
	var page = ${mdnBean.mdnResponsePage};
	var pagebase = ${pageBase};

	var workTitle =  "<span class='TM_work_title'>"+mailMsg.mail_receivenoti+"</span>";	
	setWorkTitle(workTitle);

	var searchParam = {};
	searchParam.keyWord = jQuery("#keyWord").val();
	searchParam.adv = "off";

	var listParam = {};
	listParam.page = "${mdnListPage}";
	listParam.pattern = "${mdnListPattern}";

	mailControl.setCurrentFolder("Sent");
	mailControl.setSearchParam(searchParam);
	mailControl.setListParam(listParam);

	loadMDNViewToolBar();	
	menuBar.setPageNaviBottom("p",
            {total:total,
            base:pagebase,
            page:page,
            isListSet:true,
            isLineCntSet:true,
            pagebase:USER_PAGEBASE,
            changeAfter:reloadMDNListPage});
        
        setCurrentPage(page);

	setSearchStatus("${fn:escapeXml(keyWord)}","off");

}
</script>

</head>
<body>
	<%@include file="/common/topmenu.jsp"%>
	<table width="100%" cellpadding="0" cellspacing="0" class="TM_html_m_table">
		<tr>
			<td class="TM_m_leftMenu" style="width:220px;vertical-align:top;">
				<%@include file="mailLeftCommon.jsp"%>
				<%@include file="sideMenu.jsp"%>
			</td>
			<td class="mainvsplitbar"></td>
			<td class="TM_main_hframe">
				<div class="posr">
					<div class="TM_folderInfo posr">				
						<img class="TM_barLeft" src="/design/common/image/blank.gif">								
						<div class="TM_finfo_data" id="workTitle"></div>
						<div class="TM_finfo_search" id="siSearchBox">
							<div class="TM_mainSearch">
								<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" value="${fn:escapeXml(keyWord)}" /><a href="javascript:;" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="mailSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a><a href="javascript:;" onclick="showAdsearchBox()" id="adSearchBoxBtn" class="TM_advsearch_btn"><span><tctl:msg key="mail.adsearch"/></span></a>
								<input type="text" name="_tmp" style="width:0px;height:0px;display:none"/>
							</div>	
						</div>													
						<img class="TM_barRight" src="/design/common/image/blank.gif">
					</div>
					<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
					<div class="TM_mainContent">
					<div id="mailMenubar" >			
						<div class="mail_body_tabmenu posr">
							<div class="mail_body_tab" id="menuBarTab"></div>
							<div>
								<div id="processMessageContent" class="TM_processMessage"></div>
							</div>
							<div id="pageNavi"  class="mail_body_navi">				
							</div>
						</div>		
						<div class="mail_body_menu">
							<div class="menu_main_unit" id="menuBarContent"></div>
						</div>
					</div>	
					<div id="m_contentBody" >			
						<div id="m_contentMain">
						<div class="TM_content_wrapper">
							<div class="TM_mail_content">
							
							<table cellpadding="0" cellspacing="0" border="0" width="100%" class="TM_r_table">
								<col width="80px"></col>
								<col></col>			
											
								<tr>
								<td colspan="2" class="TM_r_subject">
									<span>${fn:escapeXml(mdnBean.messageTitle)}</span>								
								</td>						
								</tr>
								<tr>
								<td class="TM_rh_index">				
									<tctl:msg key="mail.senddate" /> :				
								</td>
								<td class='TM_rh_content' valign="top">
									${mdnBean.sendDate}
								</td>						
								</tr>
								
								<tr>
								<td class="TM_rh_index">				
									<tctl:msg key="mail.to" /> :				
								</td>
								<td class='TM_rh_content' valign="top">
									<tctl:msg key="mail.mdn.total"  arg0="${mdnBean.countTotal}"/>
								</td>						
								</tr>
								
								<tr>			
								<td colspan="2" class='TM_rh_content'>
									<div class="TM_info_panel">
								  		<strong><tctl:msg key="mail.mdn.read" /></strong> : ${mdnBean.countRead} &nbsp;
								  		<strong><tctl:msg key="mail.mdn.unread" /></strong> :  ${mdnBean.countUnseen}&nbsp;
								  		<strong><tctl:msg key="mail.mdn.fail" /></strong> :  ${mdnBean.countFail}&nbsp;
								  		<strong><tctl:msg key="mail.mdn.recall" /></strong> :  ${mdnBean.countRecall}&nbsp;
								  		<strong><tctl:msg key="mail.mdn.etc" /></strong> :  ${mdnBean.countEtc}&nbsp;
								  		</div>	
								  	
								</td>						
								</tr>
								<tr><td colspan="2" class='TM_rh_content'><span style="color:#FF7272">(<tctl:msg key="mail.mdn.info"/>)</span></td></tr>
							</table>
							
							<form name="mdnForm"  id="mdnForm">
							<input type="hidden" id="messageId" value="${mdnBean.messageID}">
							<input type="hidden" id="uid" value="${uid}">
							<input type="hidden" name="keyWord" id="keyWord" value="${fn:escapeXml(keyWord)}"/>
							<input type="hidden" name="adv" id="off"/>
							<input type="hidden" id="mdnlistpage" value="${mdnListPage}">
							<input type="hidden" id="mdnlistpattern" value="${mdnListPattern}">
							<table cellspacing="0" cellpadding="0" class="TM_b_list">
								<col width="40px"></col>		
								<col></col>
								<col width="150px"></col>
								<col width="250px"></col>
								
								<tr>
								<th scope="col"><input type="checkbox"  id="allChk" onclick="allChkEmail(this)"></th>
								<th scope="col"><tctl:msg key="mail.to" /></th>	
								<th scope="col"><tctl:msg key="mail.sendstatus" /></th>
								<th scope="col"><tctl:msg key="mail.mdn.msg" /></th>
								</tr>
								
								<c:forEach var="mdnContent" items="${mdnBean.rcptVos}" varStatus="loop">
								<tr class="TM_mailLow">
									<td>
										<c:choose>
										<c:when test="${mdnContent.localDomain && 
														(mdnContent.code eq '200' || 
														mdnContent.code eq '201' || 
														mdnContent.code eq '300')}">
											<input type="checkbox" name="recallEmail" value="${mdnContent.address}" onclick="chkEmail(this)"/>
										</c:when>
										<c:otherwise>
											<input type="checkbox" disabled />
										</c:otherwise>
										</c:choose>
											
									</td>
									<td class="TM_list_cut" title="${fn:escapeXml(mdnContent.printAddress)}" >${fn:escapeXml(mdnContent.printAddress)}</td>
									<td><span class="TM_MDNStatus"><tctl:msg key="${mdnContent.status}" /></span></td>
									<td>
									<c:if test="${mdnContent.code eq '1' || mdnContent.code eq '1000'}">
										${mdnContent.message}
									</c:if>
									<c:if test="${not empty mdnContent.message && mdnContent.code ne '1' && mdnContent.code ne '1000'}">				
										<tctl:msg key="${mdnContent.message}" />
									</c:if>
									
									</td>
								</tr>
								</c:forEach>
							</table>
							</form>
							
						</div>
                        <c:if test="${!empty mdnBean.rcptVos}">
					        <div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
					    </c:if> 
					</div>
					</div>
				</div>				
				</div>
				</div>
			</td>
		</tr>
	</table>
	<%@include file="/common/bottom.jsp"%>
	
	<form name="popupWriteForm" id="popupWriteForm"></form>	
	
	<%@include file="/dynamic/mail/mailCommonModal.jsp"%>
	
	<script type="text/javascript">init();</script>
</body>
</html>