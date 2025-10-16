<%@ page isELIgnored="false"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail.css" />
<style type="text/css">
#printArea html, 
#printArea address,
#printArea blockquote,
#printArea body,#printArea dd,#printArea div,#printArea dl,
#printArea dt,#printArea fieldset,#printArea form,#printArea frame,
#printArea frameset,#printArea h1,#printArea h2, #printArea h3, 
#printArea h4,#printArea h5, #printArea h6, #printArea noframes,
/*#printArea ol, #printArea p, #printArea ul, #printArea center,#printArea dir, */
#printArea ol, #printArea ul, #printArea center,#printArea dir, 
#printArea hr, #printArea menu, #printArea pre   { display: block; color:#000000;width:auto !important; height:auto !important;border:none; padding:0px; margin:0px;}
#printArea li              { display: list-item;}
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
/* #printArea h4, #printArea p,#printArea blockquote, #printArea ul,#printArea fieldset, #printArea form,#printArea ol, #printArea dl, #printArea dir,#printArea menu            { margin: 1.12em 0; } */
#printArea h4, #printArea blockquote, #printArea ul,#printArea fieldset, #printArea form,#printArea ol, #printArea dl, #printArea dir,#printArea menu            { margin: 1.12em 0; }
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
.MsoNormal{*text-align: auto !important;*width:auto !important; margin:auto !important;text-indent: 0px !important;}
.MsoListParagraph{*text-align: left !important;*width:auto !important;}
.MsoNormalTable {margin:auto !important;}
</style>
<script type="text/javascript">
function closeWin(){
	window.close();
}

function printMsg(){
	jQuery("#titleLayer").hide();
	jQuery("#btnAreaPane").hide();
	jQuery("#btnAreaPaneLine").hide();
	jQuery("#printBtn").hide();
	setTimeout(function(){
		window.print();
		setTimeout(function(){
			showContent();
		},1000);
	},1000);
}

function showContent(){
	jQuery("#titleLayer").show();
	jQuery("#btnAreaPane").show();
	jQuery("#btnAreaPaneLine").show();
	jQuery("#printBtn").show();
}
</script>
</head>
<body class="popupBody" style="background: #FFFFFF">

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr><td>
<div class="popup_style2">
	<div class="title" id="titleLayer">
		<span>
		<c:if test="${readType eq 'pop'}">
			<tctl:msg key="mail.popupview"/>
		</c:if>
		<c:if test="${readType eq 'print'}">
			<tctl:msg key="mail.print"/>
		</c:if>
		</span>
		<a class="btn_X" href="javascript:;" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>	
	<div class="TM_modal_content_wrapper" style="padding:0px;">
		<div class="TM_content_wrapper">	
			<div class="TM_mail_content">							
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
					<col></col>
					<col width="80px"></col>			
								
					<tr>
					<td class="TM_r_subject">
						<span>
						<c:if test="${not empty message.subject}">
						${fn:escapeXml(message.subject)}
						</c:if>
						<c:if test="${empty message.subject}">
							<tctl:msg key="header.nosubject"/>
						</c:if>
						</span>			
					</td>
					<td>
					<a class="btn_style3" id="printBtn" href="javascript:;" onclick="printMsg()"><span><tctl:msg bundle="common" key="comn.print"/></span></a>
					</td>						
					</tr>
				</table>
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
					<col width="80px"></col>
					<col></col>	
					<tr>
					<td class="TM_rh_index_ex">						
						<tctl:msg key="mail.from" /> :				
					</td>
					<td class='TM_rh_content' valign="top">
						${fn:escapeXml(message.fromString)}
					</td>						
					</tr>
				</table>
				
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
					<col width="80px"></col>
					<col></col>				
					<tr>
					<td class='TM_rh_index_ex'><tctl:msg key="mail.senddate" /> :</td>
					<td class='TM_rh_content' valign="top">${message.sentDateForRead}</td>				
					</tr>
					
					<tr>
					<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.to" /> :</td>
					<td class='TM_rh_content' valign="top">
						<c:forEach var="iaddr" items="${message.to}">
				  			<c:if test="${not empty iaddr.personal}">							
								"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;																			
							</c:if>
							<c:if test="${empty iaddr.personal}">
								${iaddr.address}									
							</c:if>
						</c:forEach>	
					</td>				
					</tr>
					
					<c:if test="${!empty message.cc}">
					<tr>
					<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.cc" /> :</td>
					<td class='TM_rh_content' valign="top">
						<c:forEach var="iaddr" items="${message.cc}">							
							<c:if test="${not empty iaddr.personal}">							
								"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;									
							</c:if>
							<c:if test="${empty iaddr.personal}">
								${iaddr.address}									
							</c:if>
						</c:forEach>	
					</td>	
					</tr>		
					</c:if>
					<tr>
					<td class='TM_rh_content' colspan="2">
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
				   			<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
				   			${fileData.fileName} [<tctl:formatSize>${fileData.size75}</tctl:formatSize>]
				   		    &nbsp;		   		    
				   		    <br>
						</c:forEach>				
						
						<c:if test="${not empty vcards}">
						<strong>VCard :</strong>  
						<c:forEach var="vcardData" items="${vcards}" varStatus="loop">											
							<img src="/design/common/image/icon/ic_vcard.png"  align="absmiddle"> [<tctl:formatSize>${vcardData.size75}</tctl:formatSize>]
						</c:forEach>
						</c:if>		
					</td>
					</tr>						
				</table>								
					
			</div>
		</div>
		<div style="background: #ffffff">
		<table width="630" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr><td valign="top" style="text-align: left;padding:10px; background: #ffffff;">
				<div id="printArea">
				${fn:replace(htmlContent,"&nbsp;&nbsp;&nbsp;"," ")}
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
	<div class="dotLine" id="btnAreaPaneLine"></div>
	<div class="btnArea" id="btnAreaPane">		
		<a class="btn_style3" href="javascript:;" onclick="printMsg()"><span><tctl:msg bundle="common" key="comn.print"/></span></a>		
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</div>
</td></tr>
</table>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>