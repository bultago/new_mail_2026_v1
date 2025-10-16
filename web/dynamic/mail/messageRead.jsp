<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<div class="TM_content_wrapper" id="readWrapper">
<form name="readMessageForm" id="readMessageForm">
<input type="hidden" id="chk_${message.uid}" femail="${fn:escapeXml(message.fromAddress)}" temail="${fn:escapeXml(message.toString1)}"/>	
	<div class="TM_mail_content">
		<table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
			<col width="80px"></col>
			<col></col>			
						
			<tr>
			<td colspan="2" class="TM_r_subject">
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
				<a href='javascript:;' class='btn_basic' onclick="viewMailFromIp('${message.folderEncName}','${message.uid}');"><span class='geoipSpan'><tctl:msg key="mail.geoip.title"/></span></a>&nbsp;
				</c:if>
				<a href="javascript:;" onclick="viewSource('${message.folderEncName}','${message.uid}')"><img src="/design/common/image/blank.gif" class="TM_r_source" title="<tctl:msg key="mail.sourceview"/>" align="absmiddle"></a>
				&nbsp; &nbsp;<a href="javascript:;" onclick="viewRelationMsg('${message.folderEncName}','${message.uid}')"><img src="/design/common/image/icon/ic_arrow_sort.gif" id="btnRelation" align="absmiddle"/> <tctl:msg key="mail.relation"/></a>
				<div id="relationMsgPane" class="TM_relation_list"></div>				
			</td>						
			</tr>
			<tr>
			<td class="TM_rh_index">
				<div style='float:left;padding-right:3px;'><a href="#none" onclick="toggleHeaderInfo()"><img src="/design/common/image/blank.gif" align="absmiddle" id="btnRcptInfo" class="closeRcptBtn"></a></div>
				<tctl:msg key="mail.from" /> :				
			</td>
			<td class='TM_rh_content' valign="top">
				<a href="javascript:;"  onclick="writeMessage('${fn:escapeXml(message.fromString)}')">${fn:escapeXml(message.fromString)}</a>			  		
				<input type="hidden" name="fromAddAddr"  value="${fn:escapeXml(message.fromString)}">				
				<a id="add_from_addr" href="javascript:;" onclick="addAddr('from')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
			</td>						
			</tr>
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
				<a id="add_to_addr" href="javascript:;"  onclick="addAddr('to')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
			</td>				
			</tr>
			
			<c:if test="${!empty message.cc}">
			<tr>
			<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.cc" /> :</td>
			<td class='TM_rh_content' valign="top">
				<span id="ccAddrContents"></span>
				<a id="add_cc_addr" href="javascript:;" onclick="addAddr('cc')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
			</td>	
			</tr>		
			</c:if>	
			
			<c:if test="${!empty message.bcc}">
			<tr>
			<td class='TM_rh_index_ex' valign="top"><tctl:msg key="mail.bcc" /> :</td>
			<td class='TM_rh_content' valign="top">
				<span id="bccAddrContents"></span>
				<a id="add_bcc_addr" href="javascript:;" onclick="addAddr('bcc')" class="TM_rh_func"><tctl:msg key="mail.addradd"/></a>
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
				<a href="javascript:;" class="btn_basic" id="attSaveAllBtn"  allpart="" onclick="downloadAllAttach('${message.uid}','${message.folderEncName}')"><span><tctl:msg key="mail.saveall"/></span></a>
				<a href="javascript:;" class="btn_basic" onclick="toggleAttachInfo()"><span><tctl:msg key="mail.viewlist"/></span></a>
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
	                  	<a href="javascript:;" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${fileData.path}')" class="rdown">						
		   				<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
		   				${fileData.fileName} [<tctl:formatSize>${fileData.size75}</tctl:formatSize>]
						</a>
						<c:if test="${fileType=='eml'}">
							<a href="javascript:;" onclick="readNestedMessage('${message.uid}','${message.folderEncName}','${fileData.path}')" class="rdown">						
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
							<a href="javascript:;" onclick="downLoadTnefAttach('${message.uid}','${message.folderEncName}','${fileData.path}','${tnefFile.attachKey}')" 
								class="rdown">
							${tnefFile.fileName}
							</a>
						</c:forEach>
						]
						</c:if>
					</c:if>
					
		   		    <script language=javascript>											
						jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${fileData.path}_");				
		   		    </script>
		   		    &nbsp;
		   		    	<c:if test="${sharedFlag ne 'shared' && 
		   		    	message.folderFullName ne 'Sent' && 
		   		    	message.folderFullName ne 'Reserved'}">		   		    			   		    
		   		    	<a href="javascript:;" onclick="deleteAttachFile('${message.uid}','${message.folderEncName}','${fileData.path}');" class="rdown"><img src="/design/common/image/blank.gif" class='TM_ra_del' style="vertical-align:middle;"></a>
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
					<a href="javascript:;" onclick="downLoadAttach('${message.uid}','${message.folderEncName}','${vcardData.path}')" class="rdown">				
					<img src="/design/common/image/icon/ic_vcard.png"  align="absmiddle"></a> [<tctl:formatSize>${vcardData.size75}</tctl:formatSize>]
					<script language="javascript">
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
				<a href="javascript:;" class="btn_basic" onclick="registBayesianRuleMessage('spam','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitspam"/></span></a>
				</c:if>
				<c:if test="${hamAdmin}"> 
				<a href="javascript:;" class="btn_basic" onclick="registBayesianRuleMessage('white','${message.folderFullName}','${message.uid}')"><span><tctl:msg key="bayesian.submitham"/></span></a>
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
				<a href="javascript:;" onclick="readViewImg('${message.folderEncName}','${message.uid}');" ><tctl:msg key="mail.viewimage"/></a>  
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
				<span id="integrityBtn" ><a href="javascript:;" class="btn_basic" onclick="confirmIntegrity('${message.folderEncName}','${message.uid}')"><span><tctl:msg key="mail.integrity"/></span></a></span>
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
	
		<table cellpadding="0" cellspacing="0" border="0" class="TM_r_ctable">
			<tr><td align="center" class="TM_r_content">			
			<iframe frameborder="0" width="100%" height="300px" scrolling='no' src="/dynamic/mail/messageContent.html" id="messageContentFrame"></iframe>
			</td></tr>
		</table>			
	</div>
</form>
</div>


<script type="text/javascript">
var toAddrList = [];
var ccAddrList = [];
var bccAddrList = [];
var addrTempBuffer = new StringBuffer();

if(!MENU_STATUS.addr || MENU_STATUS.addr != "on") {
	jQuery("#add_from_addr").hide();
	jQuery("#add_to_addr").hide();
	jQuery("#add_cc_addr").hide();
	jQuery("#add_bcc_addr").hide();
}

if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
	jQuery("#view_setting").hide();
}

<c:forEach var="iaddr" items="${message.to}" varStatus="idx">					
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>
	<c:if test="${not empty iaddr.personal}">							
	addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
	addrTempBuffer.append('<input type="hidden" name="toAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');										
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="toAddAddr"  value="${iaddr.address}">');
</c:if>
toAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>

<c:forEach var="iaddr" items="${message.cc}" varStatus="idx">
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>						
<c:if test="${not empty iaddr.personal}">							
addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="ccAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="ccAddAddr"  value="${iaddr.address}">');
</c:if>
ccAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>

<c:forEach var="iaddr" items="${message.bcc}" varStatus="idx">
<c:if test="${idx.index > 0}">addrTempBuffer.append(", ")</c:if>						
<c:if test="${not empty iaddr.personal}">							
addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;\')">"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="bccAddAddr"  value="&quot;${fn:escapeXml(iaddr.personal)}&quot;&lt;${iaddr.address}&gt;">');
</c:if>
<c:if test="${empty iaddr.personal}">
addrTempBuffer.append('<a href="javascript:;" onclick="writeMessage(\'${iaddr.address}\')">&lt;${iaddr.address}&gt;</a>');
addrTempBuffer.append('<input type="hidden" name="bccAddAddr"  value="${iaddr.address}">');
</c:if>
bccAddrList.push(addrTempBuffer.toString());
addrTempBuffer.destroy();
</c:forEach>
addrTempBuffer = null;


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

currentFolderType = "${folderType}";
isMDNSend = '${MDNCheck}';
isPopupload = false;
jQuery().ready(function(){
	makeRcptAddrMore("to","toAddrContents",toAddrList);
	if(ccAddrList.length > 0)makeRcptAddrMore("cc","ccAddrContents",ccAddrList);
	if(bccAddrList.length > 0)makeRcptAddrMore("bcc","bccAddrContents",bccAddrList);
	loadMessageReadPage();	
});
</script>