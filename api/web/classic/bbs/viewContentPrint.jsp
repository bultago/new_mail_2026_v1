<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/bbs_style.css" />
<style type="text/css">
.TerraceMsg {
    line-height: 1.2 !important;
}
#printArea html, 
#printArea address,
#printArea blockquote,
#printArea body,#printArea dd,#printArea div,#printArea dl,
#printArea dt,#printArea fieldset,#printArea form,#printArea frame,
#printArea frameset,#printArea h1,#printArea h2, #printArea h3, 
#printArea h4,#printArea h5, #printArea h6, #printArea noframes,
#printArea ol, #printArea p, #printArea ul, #printArea center,#printArea dir, 
#printArea hr, #printArea menu, #printArea pre   { display: block; color:#000000;width:auto !important; height:auto !important;border:none; padding:0px; margin:0px;}
#printArea li              { display: list-item;}
#printArea ul              { list-style: inherit !important;}
#printArea head            { display: none }
#printArea table           { display: table }
#printArea tr              { display: table-row }
#printArea thead           { display: table-header-group }
#printArea tbody           { display: table-row-group }
#printArea tfoot           { display: table-footer-group }
#printArea col             { display: table-column }
#printArea colgroup        { display: table-column-group }
#printArea td, th          { display: table-cell }
#printArea caption         { display: table-caption }
#printArea th              { font-weight: bolder; text-align: center }
#printArea caption         { text-align: center }
#printArea body            { margin: 8px }
#printArea h1              { font-size: 2em; margin: .67em 0; background:none !important;}
#printArea h2              { font-size: 1.5em; margin: .75em 0; background:none !important;}
#printArea h3              { font-size: 1.17em; margin: .83em 0; background:none !important;}
#printArea h4, #printArea p,#printArea blockquote, #printArea ul,#printArea fieldset, #printArea form,#printArea ol, #printArea dl, #printArea dir,#printArea menu            { margin: 1.12em 0; }
#printArea h5              { font-size: .83em; margin: 1.5em 0 ;background:none;text-indent:0;}
#printArea h6              { font-size: .75em; margin: 1.67em 0 ;background:none;text-indent:0;}
#printArea h1, #printArea h2, #printArea h3, #printArea h4,#printArea h5, #printArea h6, #printArea b,#printArea strong{ font-weight: bolder ;background:none;text-indent:0;}
#printArea blockquote      { margin-left: 40px; margin-right: 40px }
#printArea i, #printArea cite, #printArea em,#printArea var, #printArea address    { font-style: italic }
#printArea pre, #printArea tt, #printArea code,#printArea kbd, #printArea samp       { font-family: monospace }
#printArea pre             { white-space: pre }
#printArea button, #printArea textarea,#printArea input, #printArea select   { display: inline-block}
#printArea big             { font-size: 1.17em }
#printArea small, #printArea sub, #printArea sup { font-size: .83em }
#printArea sub             { vertical-align: sub }
#printArea sup             { vertical-align: super }
#printArea table           { border-spacing: 2px; }
#printArea thead, #printArea tbody,#printArea tfoot           { vertical-align: middle }
#printArea td, #printArea th, #printArea tr      { vertical-align: inherit }
#printArea s, #printArea strike, #printArea del  { text-decoration: line-through }
#printArea hr              { border: 1px inset }
#printArea ol, #printArea ul, #printArea dir,#printArea menu, #printArea dd        { margin-left: 40px }
#printArea ol              { list-style-type: decimal }
#printArea ol #printArea ul, #printArea  ul ol,#printArea ul ul, #printArea ol ol    { margin-top: 0; margin-bottom: 0 }
#printArea u, #printArea ins          { text-decoration: underline }
#printArea br:before       { content: "\A"; white-space: pre-line }
#printArea center          { text-align: center }
#printArea :link, :visited { text-decoration: underline }
#printArea :focus          { outline: thin dotted invert }
.MsoNormal{*text-align: auto !important;*width:auto !important; margin:auto !important;}
.MsoListParagraph{*text-align: left !important;*width:auto !important;}
.MsoNormalTable {margin:auto !important;}
</style>
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/bbs/bbsReadMenuBar.js"></script>

<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>

<script language = "javascript">

function downloadFile(part)
{	
	var bbsType = "${boardVo.bbsType}";
	var url = "";
	if ("notice" == bbsType) {
		url = "/bbs/downloadNoticeAttach.do";
	} 
	else {
		url = "/bbs/downloadAttach.do";
	}
	url = url+"?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
	this.location = url;
}

function downloadAttachAll() {
	var part = jQuery("#attSaveAllBtn").attr("allpart");
	var bbsType = "${boardVo.bbsType}";
	var url = "";
	if ("notice" == bbsType) {
		url = "/bbs/downloadAllNoticeAttach.do";
	} 
	else {
		url = "/bbs/downloadAllAttach.do";
	}
	url = url+"?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
	this.location = url;
}

function toggleAttachrInfo(setValue){	
	var infoLayer = jQuery("#attachList");
	var settingValue;

	if(setValue){
		settingValue = setValue;
	} else {
		settingValue = infoLayer.css("display");		
	}

	if(settingValue == "none"){
		infoLayer.show();
	}else {
		infoLayer.hide();
	}
	//winResize();	
}

function getMessageText(){
	return $("messageText").value;
}

function resizeTextFrame(height,width){
	//$("messageContentFrame").style.height=height+"px";
	//winResize();	
}

function closeWin(){
	var readType = "${readType}";
	if (readType == 'preview') {
		parent.closePreview();
	} else {
		window.close();	
	}
}

function hideTag() {
	jQuery("#title_area").hide();
	jQuery("#dot_area").hide();
	jQuery("#print_area").hide();
}

function showTag() {
	jQuery("#title_area").show();
	jQuery("#dot_area").show();
	jQuery("#print_area").show();
}

function printMsg(){
	hideTag();
	setTimeout(function(){
		window.print();
		setTimeout(function(){
			showTag();
		},1000);
	},1000);
}

function gotoList() {
	var f = document.contentForm;
	f.action = "/bbs/listNoticeContentPopup.do";
	f.submit();
}
function init(){
	if (opener) {
        jQuery("body").addClass("popupBody");
        
    }else{
        jQuery("body").css("background","none");
    }
}
</script>
</head>
<body  style="background: #FFFFFF" onload="init();">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
			<div class="popup_style2">
				
				<div id="title_area" class="title">
					<span class="titlearrow">
						<tctl:msg key="bbs.print" bundle="bbs"/>
					</span>
					<a class="btn_X" href="#" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
				</div>
				
				
				<div class="TM_modal_content_wrapper" style="padding:0px;">	
					<div class="TM_content_wrapper">
						<div class="TM_mail_content">
							<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
								<col width="50px"></col>
								<col></col>
								<col width="250px"></col>			
											
								<tr>
								<td colspan="3" class="TM_r_subject">
									<span>${contentVo.subject}</span>												
								</td>						
								</tr>					
								<tr>
								<td class="TM_rh_index">								
									<tctl:msg key="bbs.content.list.creator" bundle="bbs"/> :				
								</td>
								<td class='TM_rh_content' valign="top">
									${contentVo.creatorName}
								</td>
								<td class="TM_rh_index" align="right">														
									<tctl:msg key="bbs.content.list.contentId" bundle="bbs"/> :
									${contentVo.contentId}
									/						
									<tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/> :
									${contentVo.refCnt}
									(<tctl:msg key="bbs.content.list.createDate" bundle="bbs"/> :
									${fn:substring(contentVo.createTime,0,4)}-${fn:substring(contentVo.createTime,4,6)}-${fn:substring(contentVo.createTime,6,8)})						
								</td>					
								</tr>
							</table>
			
							<table cellpadding="0" cellspacing="0" border="0" class="TM_r_atable">
								<col></col>
								<col width="150px"></col>
								<tr>
								<td class='TM_ra_l'>
								<c:if test="${empty contentVo.attachFiles}">
									<tctl:msg key="mail.noattach" />
								</c:if>
								<c:if test="${!empty contentVo.attachFiles}">
									${fn:length(contentVo.attachFiles)} <tctl:msg key="mail.existattach" />
								</c:if>
								</td>
								<td class='TM_ra_r'>
									<c:if test="${!empty contentVo.attachFiles}">
									<a href="#n" class="btn_basic" id="attSaveAllBtn"  allpart="" onclick="downloadAttachAll()"><span><tctl:msg key="mail.saveall"/></span></a>
									<a href="#n" class="btn_basic" onclick="toggleAttachrInfo()"><span><tctl:msg key="mail.viewlist"/></span></a>
									</c:if>
								</td>
								</tr>
								<c:if test="${!empty contentVo.attachFiles}">
								<tr>
								<td id="attachList" class='TM_ra_c' colspan="2"  style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
									<c:forEach var="fileData" items="${contentVo.attachFiles}" varStatus="loop">				
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
										fileType=='mp3' ||	fileType=='mp4' ||	fileType=='eml'}">							   				
								   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
								   			</c:when>								   			
								   			<c:otherwise>
								   				<c:set var="attachImgName" value="ic_att_unknown"/>						   				
								   			</c:otherwise>
							   			</c:choose>
							   			
							   			<c:if test="${fileData.size75 > 0 }">
						                  	<a href="#n" onclick="downloadFile('${fileData.path}')" class="rdown" title="${fileData.fileName}">						
							   				<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp;
							   				${fileData.fileName} <span id="attachSizeL_${loop.count}"></span>
							   				</a>
							   				<script language=javascript>
											$('attachSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${fileData.size75* 0.964981} ) ) +']';					
											jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${fileData.path}_");				
							   		    	</script>						   		    		   		    
							   		    </c:if>
							   		    <br>
									</c:forEach>
								</td>
								</tr>
								</c:if>							
							</table>
							<div style="background: #ffffff">
					        <table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
					            <tr><td valign="top" style="text-align: left;padding: 5px 5px 25px; background: #ffffff">
					                <div id="printArea">
					                ${fn:replace(contentVo.htmlContent,"&nbsp;&nbsp;&nbsp;"," ")}
					                <script>
					                jQuery("img").each(function(){
					                        var src = jQuery(this).attr("src");
					                        if(src.indexOf("mid") > -1 ||
					                                src.indexOf("cid") > -1){
					                                jQuery(this).attr("src","");
					                        }
					                });
					                </script>
					                </div>
								</td></tr>
							</table>
						</div>
			            </div>
			            
			            
						<c:if test="${readType ne 'preview'}">
						<div id="dot_area" class="dotLine" style="height:10px;"></div>
						<div id="print_area" class="btnArea">
							
							<c:if test="${readType eq 'print'}">
								<a class="btn_style3" href="javascript:;" onclick="printMsg()"><span><tctl:msg bundle="common" key="comn.print"/></span></a>
							</c:if>
							<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
							<c:if test="${boardVo.bbsType == 'notice' && viewType == 'list'}">
								<a class="btn_style3" href="#" onclick="gotoList()"><span><tctl:msg bundle="common" key="comn.list"/></span></a>
							</c:if>
						</div>
						</c:if>
					</div>
				</div>
			</div>
		</td>
	</tr>
</table>
<form name="contentForm" method="post">
	<input type="hidden" name="bbsIndex" value="${fn:escapeXml(bbsIndex)}">
	<input type="hidden" name="currentPage" value="${fn:escapeXml(currentPage)}">
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>