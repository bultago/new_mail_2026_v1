<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />
<script type="text/javascript">
    RENDERMODE = "html";
</script>
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
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/mail-lib/folderManageScript.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailBasicCommon.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailAction.js"></script>
<script type="text/javascript" src="/js/mail-lib/mailMenuBar.js"></script>
<script type="text/javascript" src="/js/mail-lib/layoutRenderer.js"></script>
<script type="text/javascript" src="/js/ocx/ocx_localMail.js"></script>
<tctl:extModuleCheck moduleName="expressE">
<script type="text/javascript" src='/XecureExpressE/XecureExpress.js'></script>
</tctl:extModuleCheck>
<style type="text/css">html {*overflow-y:scroll;}</style>
<script type="text/javascript">
setAlMailMode(true);
<%@include file="userSetting.jsp"%>

paneMode = "n";
LayoutInfo.mode = "read";
mainInitFunc();
readMsgData = {
	folderFullName:'${message.folderFullName}',
	folderEncName:'${message.folderEncName}',
	uid:'${message.uid}',
	preUid:'${preUid}',
	nextUid:'${nextUid}',
	fid:'${message.folderFullName}_${message.uid}',
	sharedFlag:'${fn:escapeXml(sharedFlag)}',
	sharedUserSeq:'${fn:escapeXml(sharedUserSeq)}',
	sharedFolderName:'${fn:escapeXml(sharedFolderName)}',
	size:'${message.size}',
	filesLength:'${filesLength}',
	hiddenImg:${hiddenImg}};

function init() {

	setTopMenu('mail');

	if(!menuBar || menuBar.getMode() != "list"){
		loadListToolBar();
	}
	
	if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
		jQuery("#folder_manage_link").hide();
	}
	
	var readParam = '${readParam}'.evalJSON();
	if(!readParam.page){
		readParam.page = 1;	
	}

	currentFolderType = "${folderType}";
	isMDNSend = '${MDNCheck}';

	var sharedParam  = {};
	var sharedFlag = readParam.sharedFlag;
	mailRealtionListControl = new MailRealtionListControl();
	
	sharedParam.isShared = (sharedFlag == 'shared') ? true : false;
	sharedParam.sharedFlag = sharedFlag;
	sharedParam.sharedUserSeq = readParam.sharedUserSeq;
	sharedParam.sharedFolderName = readParam.sharedFolderName;

	var searchParam = {};
	searchParam.keyWord = readParam.keyWord;
	searchParam.adv = readParam.adv;
	searchParam.category = readParam.category;

	mailControl.setSharedParam(sharedParam);
	mailControl.setSearchParam(searchParam);
	mailControl.setListParam(readParam);	

	setSearchStatus(readParam.keyWord, readParam.adv);

	folderInitLoad();
	loadHtmlMessageReadPage();
	
	var folderName="${folderFullName}";
	var totalCnt="${messageCount}";
	var unreadCnt="${unreadMessageCount}";
	updateWorkReadCount(folderName,totalCnt,unreadCnt);
}

</script>

</head>
<body>
	<%@include file="/common/topmenu.jsp"%>
	<table width="100%" cellpadding="0" cellspacing="0" class="TM_html_m_table">
		<tr>
			<td class="TM_m_leftMenu" style="width:220px;vertical-align:top;min-width:220px;white-space:nowrap;" nowrap>
				<%@include file="mailLeftCommon.jsp"%>
				<%@include file="sideMenu.jsp"%>
			</td>
			<td class="mainvsplitbar" style="min-width:7px;white-space:nowrap;" nowrap>&nbsp;</td>
			<td class="TM_main_hframe">
				<div class="posr">
				<div style="z-index:101;position: absolute; right:10px;">
					<div id="adSearchBox"  class="popup_style2 TM_advsearch_box">
						<form id="adsearchForm">
						<div class="title">
							<span><tctl:msg key="mail.adsearch"/></span>
							<a class="btn_X" href="#" onclick="hiddenAdsearchBox()"><tctl:msg bundle="common"  key="comn.close"/></a>
						</div>
						
						<table cellpadding="0" cellspacing="0" style='width:100%' class="jq_innerTable">
							<col width="150px"></col>
							<col></col>
							<tr>
								<th class="lbout"><tctl:msg key="mail.from"/></th>
								<td><input type="text" id="adFrom" class="IP300" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/></td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.to"/> (<tctl:msg key="mail.sfolder.include.001"/>)</th>
								<td><input type="text" id="adTo" class="IP300" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/></td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.sword"/></th>
								<td>
									<div style="float: left;"><div id="adConditionSelect"></div></div>
									<div style="float: left;margin-left:3px;">
									<input type="text" id="sdkeyWord" class="IP100px" style="width:145px;" onKeyPress="(keyEvent(event) == 13) ? adSearchMessage() : '';"/>
									</div>
								</td>
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.folder"/></th>
								<td>
									<div style="float: left;"><div id="adSfolderSelect"></div></div>
									<div style="float: left; margin-left:3px;">
										<div id="sAllfolderDesc"></div>
									</div>
								</td>
							</tr>												
							<tr>
								<th class="lbout"><tctl:msg key="mail.searchperiod"/></th>
								<td>
									<input type="text" class="IP100px"  id="adStartDate" readonly="readonly" style="width:140px;"/>
									<span>~</span>
									<input type="text" class="IP100px"  id="adEndDate"  readonly="readonly" style="width:140px;"/>
								</td>					
							</tr>
							<tr>
								<th class="lbout"><tctl:msg key="mail.sfolder.add.condition"/></th>
								<td>
									<label><input type="radio" name="searchFolderFlag" value="" checked/> <tctl:msg key="mail.mdn.notselect"/></label>
									<label><input type="radio" name="searchFolderFlag" value="F"/> <tctl:msg key="menu.quick.flag"/></label>
									<label><input type="radio" name="searchFolderFlag" value="T"/> <tctl:msg key="menu.quick.attach"/></label>
									<label><input type="radio" name="searchFolderFlag" value="S"/> <tctl:msg key="menu.quick.read"/></label>
									<label><input type="radio" name="searchFolderFlag" value="U"/> <tctl:msg key="menu.quick.unread"/></label>
								</td>
							</tr>
						</table>
						<div class="btnArea">
							<a class="btn_style3" href="#" onclick="adSearchMessage();"><span><tctl:msg key="mail.search"/></span></a>
						</div>
						</form>
					</div>
				</div>
				<div class="TM_folderInfo posr">				
					<img class="TM_barLeft" src="/design/common/image/blank.gif">								
					<div class="TM_finfo_data" id="workTitle"></div>
					<div class="TM_finfo_search" id="siSearchBox">
						<div class="TM_mainSearch">
							<input type="text" class="searchBox"  id="skword"  onKeyPress="(keyEvent(event) == 13) ? searchMessage() : '';" /><a href="#" onclick="cancelSearch()"><img src="/design/common/image/blank.gif" id="mailSearchCancel" class="TM_search_cancel"></a><a href="#" onclick="searchMessage()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a><a href="#" onclick="showAdsearchBox()" id="adSearchBoxBtn" class="TM_advsearch_btn"><span><tctl:msg key="mail.adsearch"/></span></a>
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
					<div id="readWrapper" class="TM_content_wrapper" style="width: 100%;">
					<form id="readMessageForm" name="readMessageForm">
						<input type="hidden" id="chk_${message.uid}" femail="${fn:escapeXml(message.fromAddress)}" temail="${fn:escapeXml(message.toString1)}"/>	
						<div class="TM_mail_content">
							<table cellspacing="0" cellpadding="0" border="0" class="TM_r_table">
								<col width="80px"><col>
								<tbody>
								<tr>
									<td class="TM_r_subject" colspan="2">
										<span id="msg_subject_${message.uid}">
											<c:if test="${not empty message.subject}">
											${fn:escapeXml(message.subject)}
											</c:if>
											<c:if test="${empty message.subject}">
												<tctl:msg key="header.nosubject"/>
											</c:if>
										</span>
										<c:if test="${useGeoIp &&
											message.folderFullName ne 'Sent' && 
											message.folderFullName ne 'Drafts' && 
							   		    	message.folderFullName ne 'Reserved'}">
										<a href='javascript:;' class='btn_basic' onclick="viewMailFromIp('${message.folderEncName}','${message.uid}');"><span class='geoipSpan'><tctl:msg key="mail.geoip.title"/></span></a>
										</c:if>
										<a href="#" onclick="viewSource('${message.folderEncName}','${message.uid}')"><img src="/design/common/image/blank.gif" class="TM_r_source" title="<tctl:msg key="mail.sourceview"/>" align="absmiddle"></a>
										&nbsp; &nbsp;<a href="javascript:;" onclick="viewRelationMsg('${message.folderEncName}','${message.uid}')"><img src="/design/common/image/icon/ic_arrow_sort.gif" id="btnRelation" align="absmiddle" /> <tctl:msg key="mail.relation"/></a>
										<div id="relationMsgPane" class="TM_relation_list"></div>
									</td>
								</tr>
								<tr>
									<td class="TM_rh_index">
										<div style='float:left;padding-right:3px;'><a href="#none" onclick="toggleHeaderInfo()"><img src="/design/common/image/blank.gif" align="absmiddle" id="btnRcptInfo" class="closeRcptBtn"></a></div>
										<tctl:msg key="mail.from" /> :				
									</td>
									<td class='TM_rh_content' valign="top">
										<a href="#"  onclick="writeMessage('${fn:escapeXml(message.fromString)}')">${fn:escapeXml(message.fromString)}</a>			  		
										<input type="hidden" name="fromAddAddr"  value="${fn:escapeXml(message.fromString)}">				
										<a id="add_from_addr" href="#" onclick="addAddr('from')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
									</td>
								</tr>
								</tbody>
							</table>
							<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table" id="rmsg_info" style="display:none">
								<col width="80px"></col>
								<col></col>				
								<tr>
								<td class='TM_rh_index_ex'><tctl:msg key="mail.senddate" /> :</td>
								<td class='TM_rh_content' valign="top">${message.sentDateForRead}</td>				
								</tr>
								
								<tr>
								<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.to" /> :</td>
								<td class='TM_rh_content' valign="top">
									<span id="toAddrContents"></span>
									<a id="add_to_addr" href="#"  onclick="addAddr('to')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
								</td>				
								</tr>
								
								<c:if test="${!empty cc}">
								<tr>
								<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.cc" /> :</td>
								<td class='TM_rh_content' valign="top">
									<span id="ccAddrContents"></span>
									<a id="add_cc_addr" href="#" onclick="addAddr('cc')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
								</td>	
								</tr>		
								</c:if>
								
								<c:if test="${!empty bcc}">
								<tr>
								<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.bcc" /> :</td>
								<td class='TM_rh_content' valign="top">
									<span id="bccAddrContents"></span>
									<a id="add_bcc_addr" href="#" onclick="addAddr('bcc')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
								</td>	
								</tr>		
								</c:if>						
							</table>		
							<table cellpadding="0" cellspacing="0" border="0" class="TM_r_atable">
								<col></col>
								<col width="180px"></col>
								<tr>
									<td class='TM_ra_l'>
									<c:if test="${filesLength == 0}">
										<tctl:msg key="mail.noattach" />
									</c:if>
									<c:if test="${filesLength > 0}">
										${filesLength} <tctl:msg key="mail.existattach" />
									</c:if>
									</td>
									<td class='TM_ra_r'>
										<c:if test="${filesLength > 0}">
										<a href="#" class="btn_basic" id="attSaveAllBtn"  allpart="" onclick="downloadAllAttach('${message.uid}','${message.folderEncName}')"><span><tctl:msg key="mail.saveall"/></span></a>
										<a href="#" class="btn_basic" onclick="toggleAttachInfo()"><span><tctl:msg key="mail.viewlist"/></span></a>
										</c:if>
										<c:if test="${filesLength <= 0}">
										&nbsp;
										</c:if>
									</td>
								</tr>
								<c:if test="${filesLength > 0}">
								<tr>
									<td id="attachList" class='TM_ra_c' colspan="2">
										<c:forEach var="fileData" items="${files}" varStatus="loop">				
											<c:forTokens var="file" items="${fileData.fileName}" delims=".">
												<c:set var="fileType" value="${fn:toLowerCase(file)}"/>								 
											</c:forTokens>	  
											
											<c:choose>
									   			<c:when test="${fileType=='doc' || 	fileType=='docx'|| 	fileType=='gif' || 
																fileType=='pdf' || 	fileType=='html'|| 	fileType=='hwp' || 
																fileType=='jpg' || 	fileType=='bmp' ||	fileType=='ppt' || 
																fileType=='pptx'|| 	fileType=='txt' || 	fileType=='xls' || 
																fileType=='xlsx'|| 	fileType=='zip' || 	fileType=='xml' ||
																fileType=='mpeg'||	fileType=='avi' || 	fileType=='htm' ||
																fileType=='mp3' ||	fileType=='mp4' ||  fileType=='eml'}">							   				
									   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
									   				<c:set var="fileAlt" value="${fileType}"/>
									   			</c:when>								   			
									   			<c:otherwise>
									   				<c:set var="attachImgName" value="ic_att_unknown"/>
									   				<c:set var="fileAlt" value="${fileType}"/>							   				
									   			</c:otherwise>
								   			</c:choose>
								   			
								   			<c:if test="${fileData.size75 > 0 }">
							                  	<a href="#" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${fileData.path}')" class="rdown">						
								   				<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
								   				${fileData.fileName} <span id="attachSizeL_${loop.count}"></span>
												</a>
												<c:if test="${fileType=='eml'}">
													<a href="#" onclick="readNestedMessage('${message.uid}','${message.folderEncName}','${fileData.path}')" class="rdown">						
									   				<img src="/design/common/image/blank.gif" alt="<tctl:msg key="mail.popupview"/>" title="<tctl:msg key="mail.popupview"/>" class='popupReadIcon' align='absmiddle'/>
													</a>
												</c:if>
											
												<c:if test="${fileData.tnefType}">
													<c:if test="${not empty fileData.tnefFiles}">
													[
													<c:forEach var="tnefFile" items="${fileData.tnefFiles}" varStatus="idx">
														<c:if test="${idx.index > 0}">
														,
														</c:if>
														<a href="#" onclick="downLoadTnefAttach('${message.uid}','${message.folderEncName}','${fileData.path}','${tnefFile.attachKey}')" 
															class="rdown">
														${tnefFile.fileName}
														</a>
													</c:forEach>
													]
													</c:if>
												</c:if>
											
									   		    <script language=javascript>
													$('attachSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${fileData.size75* 0.964981} ) ) +']';					
													jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${fileData.path}_");				
									   		    </script>
									   		    <c:if test="${sharedFlag ne 'shared' && 
									   		    	message.folderFullName ne 'Sent' && 
									   		    	message.folderFullName ne 'Reserved'}">		   		    			   		    
									   		    	<a href="#" onclick="deleteAttachFile('${message.uid}','${message.folderEncName}','${fileData.path}');" class="rdown"><img src="/design/common/image/blank.gif" class='TM_ra_del' align="absmiddle"></a>
								   		    	</c:if>	   		    
								   		    </c:if>
								   		    
								   		    <c:if test="${fileData.size75 == 0 }">
								   		    	<span class="rdeleted">
								   		    		<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp;
								   					${fileData.fileName}
								   					[<tctl:msg key="mail.deleteattach"/>]
								   		    	</span>
								   		    </c:if>
								   		    <br>
										</c:forEach>				
										
										<c:if test="${not empty vcards}">
										<strong>VCard :</strong>  
										<c:forEach var="vcardData" items="${vcards}" varStatus="loop">
											<a href="#" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${vcardData.path}')" class="rdown">				
											<img src="/design/common/image/icon/ic_vcard.png"  align="absmiddle"></a> <span id="vcardSizeL_${loop.count}"></span>
											<script language="javascript">
												$('vcardSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${vcardData.size75* 0.964981} ) ) +']';
												jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${vcardData.path}_");
								   		    </script>			
										</c:forEach>
										</c:if>		
									</td>
								</tr>
								</c:if>
								<c:if test="${ruleAdmin}">
								<tr>
									<td class='TM_ra_n' colspan="2">
										<img src="/design/common/image/blank.gif" class='TM_ra_nic' align="absmiddle">
										SPAM RATE[<span class="TM_ra_rateval">${spamRate}</span>]
										-
										<c:if test="${spamAdmin}"> 
										<a href="#" class="btn_basic" onclick="registBayesianRuleMessage('spam','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitspam"/></span></a>
										</c:if>
										<c:if test="${hamAdmin}"> 
										<a href="#" class="btn_basic" onclick="registBayesianRuleMessage('white','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitham"/></span></a>
										</c:if>
									</td>
								</tr>
								</c:if>
								<c:if test="${hiddenImg}">
								<tr>
									<td class='TM_ra_n' colspan="2">			
										<img src="/design/common/image/blank.gif" class='TM_ra_nic' align="absmiddle">
										<tctl:msg key="mail.noimage"/>
										<div class="func_link">
										<a href="#" onclick="readViewImg('${message.folderEncName}','${message.uid}');" ><tctl:msg key="mail.viewimage"/></a>  
										<span id="view_setting"> | <a href="/setting/viewSetting.do" ><tctl:msg key="mail.setting"/></a></span>
										</div>				
									</td>
								</tr>
								</c:if>
								<c:if test="${integrityUse eq 'on' && sharedFlag ne 'shared'}">
								<tr>
									<td class='TM_ra_n' colspan="2">
										<img src="/design/common/image/blank.gif" class='TM_ra_nic' align="absmiddle">
										[<span id="integrityMsg"><tctl:msg key="mail.integrity.notcheck"/></span>]			
										<span id="integrityBtn" ><a href="#" class="btn_basic" onclick="confirmIntegrity('${message.folderEncName}','${message.uid}')"><span><tctl:msg key="mail.integrity"/></span></a></span>
									</td>
								</tr>				
								</c:if>
							</table>
							
							<textarea id="messageText" style="display:none;">
								<c:out value="${htmlContent}" escapeXml="true"/>
								
								<c:if test="${not empty imageAttach}">
								<div style="text-align: center;border-top:2px solid #d6d6d6;padding-top:5px">			  
								<c:forEach var="imagesUrl" items="${imageAttach}" varStatus="loop">
									<c:if test="${not empty imagesUrl}">				
									<img src="${imagesUrl}"/>
									</c:if>				
								</c:forEach>
								</div>
								</c:if>	
							</textarea>
		
							<table cellpadding="0" cellspacing="0" border="0" style="width:100%;table-layout:fixed;border:#D6D6D6 solid 1px;border-top:0px;">
								<tr>
									<td class="TM_r_content">
										<div id="messageReadWrap" style="width:100%;overflow-x:auto;overflow-y:hidden;">
											<div id="messageReadContent">		
												<iframe frameborder="0" scrolling="no" width="100%" height="300px" src="/classic/mail/messageContent.html" id="messageContentFrame"></iframe>
											</div>
										</div>
									</td>
								</tr>
							</table>	
						</div>
					</form>
					</div>
				</div>
				</div>
			</td>
		</tr>
	</table>
	<%@include file="/common/bottom.jsp"%>
	
	<div id="m_sourceView"  title="<tctl:msg key="mail.sourceview"/>" style="width:100%; display:none">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">		
			<col></col>	
			<tr>		
			<td height="400px"><iframe name="sourceFrame" id="sourceFrame" src="about:blank" frameborder="0" width="100%" height="400px" style="height:400px;"></iframe></td>
			</tr>
		</table>	
	</div>
	
	<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>

	<form name="popupReadForm" id="popupReadForm">
		<input type="hidden" name="uid"/>
		<input type="hidden" name="folder"/>
		<input type="hidden" name="readType"/>
		<input type="hidden" name="sharedFlag"/>
		<input type="hidden" name="sharedUserSeq"/>
		<input type="hidden" name="sharedFolderName"/>
		<input type="hidden" name="part"/>
		<input type="hidden" name="nestedPart"/>
	</form>
	<form name="popupWriteForm" id="popupWriteForm"></form>
	
	<%if(isMsie){ %>
		<c:if test="${localmail}">
		<%@include file="/dynamic/mail/inc_ocxLocalMail.jsp"%>
		</c:if>
	<%}%>
	
	<%@include file="/dynamic/mail/mailCommonModal.jsp"%>

<script type="text/javascript">

var toAddrList = [];
var ccAddrList = [];
var bccAddrList = [];
var addrTempBuffer = new StringBuffer();

if(!MENU_STATUS.addr || MENU_STATUS.addr != "on") {
	jQuery("#add_from_addr").hide();
	jQuery("#add_to_addr").hide();
	jQuery("#add_cc_addr").hide();
}

if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
	jQuery("#view_setting").hide();
}

<c:forEach var="iaddr" items="${to}" varStatus="idx">					
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>
	<c:if test="${not empty iaddr.personal}">							
	addrTempBuffer.append('<a href="#" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
	addrTempBuffer.append('<input type="hidden" name="toAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');										
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="#" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="toAddAddr"  value="${iaddr.address}">');
</c:if>
toAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>

<c:forEach var="iaddr" items="${cc}" varStatus="idx">
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>						
<c:if test="${not empty iaddr.personal}">							
addrTempBuffer.append('<a href="#" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="ccAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="#" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="ccAddAddr"  value="${iaddr.address}">');
</c:if>
ccAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>

<c:forEach var="iaddr" items="${bcc}" varStatus="idx">
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>						
<c:if test="${not empty iaddr.personal}">							
addrTempBuffer.append('<a href="#" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="bccAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="#" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="bccAddAddr"  value="${iaddr.address}">');
</c:if>
bccAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>
addrTempBuffer = null;

makeRcptAddrMore("to","toAddrContents",toAddrList);
if(ccAddrList.length > 0)makeRcptAddrMore("cc","ccAddrContents",ccAddrList);
if(bccAddrList.length > 0)makeRcptAddrMore("bcc","bccAddrContents",bccAddrList);

currentFolderType = "${folderType}";
isMDNSend = '${MDNCheck}';
isPopupload = false;

init();
</script>
</body>
</html>