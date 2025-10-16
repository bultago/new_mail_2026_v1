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
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=setting&locale=<%=locale%>"></script>
<script type="text/javascript">
function getMessageText(){
	return $("messageText").value;
}

var contentFrameWidth;
function resizeTextFrame(height,width){
	$("messageContentFrame").style.height=height+25+"px";
	jQuery("#messageContentFrame").css({"overflow-x":"hidden","overflow-y":"hidden"});
	contentFrameWidth = width;	
	var func;	
	func = function(){
		var wrapper = jQuery("#readPopupFrame");
		var parentWrapper = jQuery("#readPopupFrame").parent();
		if(parentWrapper.width() < contentFrameWidth){
			wrapper.css("width",(contentFrameWidth+30)+ "px");
		} else {			
			wrapper.css("width",(isMsie)?"":"100%");			
		}
	};
	jQuery(window).bind("resize",func);	
	func();	
}

function closeWin(){
	window.close();
}

function printMsg(){	
	window.print();	
}

function downLoadAttach(uid, folder, part){	
	var npart = $("nestedPartTmp").value;	
	npart = (npart !="")? npart+"|"+part:part;
	var part = $("orgPart").value;
	var param = {"folder":folder, 
			"uid":uid, "part":part,
			"nestedPart":npart,
			"type":"normal"
			};			
	$("reqFrame").src = "/mail/downloadAttach.do?"+jQuery.param(param);	
}

function readNestedMessage(uid, folder, part){
	
	var popupReadForm = document.popupReadForm;
	popupReadForm.uid.value = uid;
	popupReadForm.folder.value = folder;	
	
	var param = {};
	var npart = $("nestedPartTmp").value;		
	popupReadForm.nestedPart.value = (npart !="")? npart+"|"+part:part;
	
	var wname = "popupRead"+makeRandom();	
	var popWin = window.open("about:blank",wname,"resizable=yes,scrollbars=yes,width=800,height=640");
	popupReadForm.method = "post";
	popupReadForm.action="/dynamic/mail/readNestedMessage.do";
	popupReadForm.target = wname;
	popupReadForm.submit();		
}


</script>
</head>
<body class="popupBody" style="background: #FFFFFF">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr><td>
<div class="popup_style2">
	<div class="title" id="titleLayer">
		<span>
		<tctl:msg key="mail.popupview"/>
		</span>
		<a class="btn_X" href="javascript:;" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>
	<div class="TM_modal_content_wrapper" style="padding:0px;">
		<div class="TM_content_wrapper">	
			<div class="TM_mail_content">			
				<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
					<col width="80px"></col>
					<col></col>			
								
					<tr>
					<td colspan="2" class="TM_r_subject">
						<span>
						<c:if test="${not empty message.subject}">
						${fn:escapeXml(message.subject)}
						</c:if>
						<c:if test="${empty message.subject}">
							<tctl:msg key="header.nosubject"/>
						</c:if>
						</span>			
					</td>						
					</tr>
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
						<c:forEach var="iaddr" items="${to}" varStatus="idx">
							<c:if test="${idx.index > 0}">,&nbsp;</c:if>
				  			<c:if test="${not empty iaddr.personal}">							
								"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;																			
							</c:if>
							<c:if test="${empty iaddr.personal}">
								${iaddr.address}									
							</c:if>
						</c:forEach>	
					</td>				
					</tr>
					
					<c:if test="${!empty cc}">
					<tr>
					<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.cc" /> :</td>
					<td class='TM_rh_content' valign="top">
						<c:forEach var="iaddr" items="${message.cc}" varStatus="idx">
							<c:if test="${idx.index > 0}">,&nbsp;</c:if>							
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
				   			<c:if test="${fileData.size75 > 0 }">
			                  	<a href="javascript:;" onclick="downLoadAttach('${uid}','${folder}','${fileData.path}')" class="rdown">						
				   				<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
				   				${fileData.fileName} [<tctl:formatSize>${fileData.size75}</tctl:formatSize>]
								</a>
								<c:if test="${fileType=='eml'}">
									<a href="javascript:;" onclick="readNestedMessage('${uid}','${folder}','${fileData.path}')">						
					   				<img src="/design/common/image/blank.gif" alt="<tctl:msg key="mail.popupview"/>" title="<tctl:msg key="mail.popupview"/>" class='popupReadIcon' align='absmiddle'/>
									</a>
								</c:if>
				   		    </c:if>
				   		    
				   		    <c:if test="${fileData.size75 == 0 }">
				   		    	<span class="rdeleted">
				   		    		<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
				   					${fileData.fileName}
				   					[<tctl:msg key="mail.deleteattach"/>]
				   		    	</span>
				   		    </c:if>
				   		    <br>
						</c:forEach>				
						
						<c:if test="${not empty vcards}">
						<strong>VCard :</strong>  
						<c:forEach var="vcardData" items="${vcards}" varStatus="loop">
							<a href="javascript:;" onclick="downLoadAttach('${uid}','${folder}','${vcardData.path}')" class="rdown">				
							<img src="/design/common/image/icon/ic_vcard.png"  align="absmiddle"></a> [<tctl:formatSize>${vcardData.size75}</tctl:formatSize>]
						</c:forEach>
						</c:if>		
					</td>
					</tr>						
				</table>								
					
			</div>
		</div>
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
	
		<table cellpadding="0" cellspacing="0" border="0" class="TM_r_ctable">
			<tr><td align="center" class="TM_r_content">			
			<iframe frameborder="0" width="100%" height="300px" scrolling='no' src="/dynamic/mail/messageContent.html" id="messageContentFrame"></iframe>
			</td></tr>
		</table>	
				
	</div>	
	<div class="dotLine"></div>
	<div class="btnArea">		
		<a class="btn_style3" href="javascript:;" onclick="printMsg()"><span><tctl:msg bundle="common" key="comn.print"/></span></a>		
		<a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</div>
</td></tr>
</table>
<input type="hidden" name="nestedPartTmp" id="nestedPartTmp" value="${nestedPart}"/>
<input type="hidden" name="orgPart" id="orgPart" value="${orgPart}"/>
<form name="popupReadForm" id="popupReadForm">
	<input type="hidden" name="uid" value="${uid}"/>
	<input type="hidden" name="folder" value="${folder}"/>
	<input type="hidden" name="readType"/>
	<input type="hidden" name="sharedFlag" value="${fn:escapeXml(sharedFlag)}"/>
	<input type="hidden" name="sharedUserSeq" value="${fn:escapeXml(sharedUserSeq)}"/>
	<input type="hidden" name="sharedFolderName" value="${fn:escapeXml(sharedFolderName)}"/>
	<input type="hidden" name="part" value="${orgPart}"/>
	<input type="hidden" name="nestedPart"/>
</form>

<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0"></iframe>

<%@include file="/dynamic/mail/mailCommonModal.jsp"%>

<div id="mainLoaddingMessage" class="TM_main_loadding_mask">
	<div id="mainLoadMessage" class="TM_c_loadding"></div>
</div>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>