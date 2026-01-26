<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="/design/common/css/common.css">
<link rel="stylesheet" type="text/css" href="/design/default/css/layout.css">
<link rel="stylesheet" type="text/css" href="/design/default/css/mail.css" />
</head>
<body>

<div class="portlet_body">

<div class="TM_content_wrapper">	
	<div class="TM_mail_content">		
		<div class="TM_mail_subject" id="msg_subject_${message.uid}">
			${message.subject} 
		</div>
		<div class="TM_mail_header">			
			<div class="TM_header_line">		  	
			  	<div class="TM_header_index"><tctl:msg key="mail.from" /> : </div>
			  	<div class="TM_header_content">
			  		${fn:escapeXml(message.fromString)}
			  	</div>			  	
				<div class="cls"></div>
			</div>
			<div id="headerInfo" style="display:none">				
				<div class="TM_header_line">		  	
				  	<div class="TM_header_index"><tctl:msg key="mail.senddate" /> : </div>
				  	<div class="TM_header_content">
				  		${message.receivedDateForRead}
				  	</div>			  	
					<div class="cls"></div>
				</div>
				
				<div class="TM_header_line">		  	
				  	<div class="TM_header_index"><tctl:msg key="mail.to" /> : </div>
				  	<div class="TM_header_content">
				  		<c:forEach var="iaddr" items="${message.to}">
				  			<c:if test="${not empty iaddr.personal}">							
							"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;																	
							</c:if>
							<c:if test="${empty iaddr.personal}">
							${iaddr.address}							
							</c:if>
						</c:forEach>			  		
				  	</div>
				  	<div class="TM_header_func">				  		
				  	</div>
					<div class="cls"></div>
				</div>
				
				<c:if test="${!empty message.cc}">
				<div class="TM_header_line">		  	
				  	<div class="TM_header_index"><tctl:msg key="mail.cc" /> : </div>
				  	<div class="TM_header_content">
				  		<c:forEach var="iaddr" items="${message.cc}">							
							<c:if test="${not empty iaddr.personal}">							
							"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;							
							</c:if>
							<c:if test="${empty iaddr.personal}">
							${iaddr.address}							
							</c:if>
						</c:forEach>			  		
				  	</div>
				  	<div class="TM_header_func">				  		
				  	</div>
					<div class="cls"></div>
				</div>
				</c:if>
			</div>		
		</div>
		
		
		<div class="TM_attach_control" style="display:block">
			<img src="/design/default/image/icon/ic_mail_file.gif" align="absmiddle">
			<c:if test="${filesLength == 0}">
				<strong><tctl:msg key="mail.noattach" /></strong>
			</c:if>
			<c:if test="${filesLength > 0}">
				<strong>${filesLength} <tctl:msg key="mail.existattach" /></strong>
			</c:if>
		</div>			
		
		<div id="attachList" class="TM_attach_list" style="display:block">
			<div style="padding-bottom:5px;">			
			<c:forEach var="fileData" items="${files}" varStatus="loop">				
				<c:forTokens var="file" items="${fileData.fileName}" delims=".">
					<c:set var="fileType" value="${fn:toLowerCase(file)}"/>								 
				</c:forTokens>	  
				
				<c:choose>
		   			<c:when test="${fileType=='doc' || fileType=='docx' || fileType=='gif' || fileType=='pdf' || fileType=='html' || fileType=='hwp' || 
		   			fileType=='jpg' || fileType=='ppt' || fileType=='pptx' || fileType=='txt' || fileType=='xls' || fileType=='xlsx' || fileType=='zip'|| 
		   			fileType=='eml'}">							   				
		   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
		   				<c:set var="fileAlt" value="${fileType}"/>
		   			</c:when>								   			
		   			<c:otherwise>
		   				<c:set var="attachImgName" value="ic_att_unknown"/>
		   				<c:set var="fileAlt" value="${fileType}"/>							   				
		   			</c:otherwise>
	   			</c:choose>
	   			
	   			<c:if test="${fileData.size75 > 0 }">
                  	<a href="javascript:;" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${fileData.path}')" class="TM_attfile_downlink">						
	   			<img src="/design/default/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
	   			${fileData.fileName} <span id="attachSizeL_${loop.count}"></span>
				</a>
				
				<c:if test="${fileData.tnefType}">
					<c:if test="${not empty fileData.tnefFiles}">
					[
					<c:forEach var="tnefFile" items="${fileData.tnefFiles}" varStatus="idx">
						<c:if test="${idx.index > 0}">
						,
						</c:if>
						<a href="javascript:;" onclick="downLoadTnefAttach('${message.uid}','${message.folderEncName}','${fileData.path}','${tnefFile.attachKey}')" 
							class="TM_attfile_downlink">
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
	   		    &nbsp;
	   		    	<c:if test="${sharedFlag ne 'shared'}">		   		    
	   		    	<a href="javascript:;" onclick="deleteAttachFile('${message.uid}','${message.folderEncName}','${fileData.path}');" class="TM_attfile_dellink"><img src="/design/default/image/icon/ic_close_small.gif" align="absmiddle"></a>
	   		    	</c:if>	   		    
	   		    </c:if>
	   		    
	   		    <c:if test="${fileData.size75 == 0 }">
	   		    	<span class="TM_attfile_deleted">
	   		    		<img src="/design/default/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
	   					${fileData.fileName}
	   					[<tctl:msg key="mail.deleteattach"/>]
	   		    	</span>
	   		    </c:if>
	   		    <br>
			</c:forEach>
			</div>		
			
			<div style="padding-top:5px; border-top:#d6d6d6 solid 1px">
			<c:if test="${not empty vcards}">
			<strong>VCard :</strong>  
			<c:forEach var="vcardData" items="${vcards}" varStatus="loop">
				<a href="javascript:;" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${vcardData.path}')" class="TM_attfile_downlink">				
				<img src="/design/default/image/icon/ic_vcard.gif"  align="absmiddle"></a> <span id="vcardSizeL_${loop.count}"></span>
				<script language="javascript">
					$('vcardSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${vcardData.size75* 0.964981} ) ) +']';
					jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${vcardData.path}_");
	   		    </script>			
			</c:forEach>
			</c:if>
			</div>														
		</div>
		
		<c:if test="${ruleAdmin}">
		<div class="TM_mail_spamRate">
			SPAM RATE[<span class="RateValue">${spamRate}</span>]
			-
			<c:if test="${spamAdmin}"> 
			<a href="javascript:;" class="btn_basic" onclick="registBayesianRuleMessage('spam','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitspam"/></span></a>
			</c:if>
			<c:if test="${hamAdmin}"> 
			<a href="javascript:;" class="btn_basic" onclick="registBayesianRuleMessage('white','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitham"/></span></a>
			</c:if>
		</div>	
		</c:if>	
		
		<c:if test="${hiddenImg}">
		<div class="TM_mail_noimg">
			<tctl:msg key="mail.noimage"/>
			-<a href="javascript:;" onclick="readViewImg('${message.folderEncName}','${message.uid}');"><tctl:msg key="mail.viewimage"/></a> [<a href="/setting/viewSetting.do"><tctl:msg key="mail.setting"/></a>]
		</div>	
		</c:if>
		
		<c:if test="${integrityUse eq 'on' && sharedFlag ne 'shared'}">
		<div class="TM_mail_integrity">
			[<span id="integrityMsg"><tctl:msg key="mail.integrity.notcheck"/></span>]			
			<span id="integrityBtn" ><a href="javascript:;" class="btn_basic" onclick="confirmIntegrity('${message.folderEncName}','${message.uid}')"><span><tctl:msg key="mail.integrity"/></span></a></span>
		</div>	
		</c:if>
		
		<div class="TM_mail_body">		
			
			${htmlContent}
			
			<br>			
			<c:if test="${not empty imageAttach}">
			<div style="overflow-y:hidden; overflow-x:auto;">			  
			<c:forEach var="imagesUrl" items="${imageAttach}" varStatus="loop">
				<c:if test="${not empty imagesUrl}">				
				<hr>				
				<img src="${imagesUrl}"/>
				</c:if>				
			</c:forEach>
			</div>
			</c:if>
									
		</div>
			
	</div>
</div>
</div>

</body>

</html>
