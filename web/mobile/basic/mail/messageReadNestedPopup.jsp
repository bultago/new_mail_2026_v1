<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
    <head>
        <%@include file="/mobile/basic/common/header.jsp"%>
        
        <script type="text/javascript"> 
        
            function makeTopLink(){
                var topLink = document.getElementById("mailTopLink");
                topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'>${contents.folderName}"
                +"</a>";                
            }
            
            function resizeTextFrame(height,width){
                document.getElementById("messageContentFrame").style.height=height+25+"px";
            }

            function getMessageText(){
                return document.getElementById("messageText").value;
            }
            
            function readNestedMessage(uid, folder, part) {            	
            	var downForm = document.downForm;
            	downForm.uid.value = uid;
            	downForm.folder.value = folder;	                        	
            	downForm.part.value = part;
            					            	
            	var param = {};
            	//var npart = $("nestedPartTmp").value;
            	var npart = jQuery("#nestedPartTmp").val();
            	downForm.nestedPart.value = (npart !="")? npart+"|"+part:part;
            					            	
            	var wname = "popupRead"+makeRandom();            	
            	var popWin = window.open("about:blank",wname,"resizable=no,scrollbars=yes");
            	downForm.method = "post";            	
                downForm.action="/mobile/mail/readNestedMessageMobile.do";
                
            	downForm.target = wname;
            	downForm.submit();		
            }
            
            /**
            function closeWin(){
            	window.close();
            }
            */
            
            function closeWin(){            	
            	var win = window.open("","_self");
            	win.close();
            }
          
            var extensions = ["hwp", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "jpg", "gif", "png", "bmp","eml"];

            /*
            function downLoadAttach(uid, folder, part) {
            	var downForm = document.downForm;
            	var npart = jQuery("#nestedPartTmp").val();            	
            	npart = (npart !="")? npart+"|"+part:part;
            	            
            	//var param = {"folder":folder, "uid":uid, "part":part, "nestedPart":npart, "type":"normal"};
            	                            	
                var param = {"folder":folder, "uid":uid, "part":part, "nestedPart":npart, "type":"normal","isApp":"true"};
                
                    jQuery.post("/mail/downloadAttach.do", param, function (data) {
                        if(data.isSuccess){                        
                            var fileUrl = "${downloadUrl}"+data.filePath;                
                            var extension = fileUrl.substr(fileUrl.lastIndexOf(".")+1,fileUrl.length);
                            var executeFile = false;
                            for(var i=0;i<extensions.length;i++){
                                if(extensions[i]==extension){
                                    executeFile = true;
                                    break;
                                }
                            }
                            
                            if(executeFile){ 
                                location=fileUrl;
                            }
                            else{
                                alert('<tctl:msg key="mobile.view.file.fail" bundle="common"/>');
                            }
                        }else{
                            alert('<tctl:msg key="mobile.download.fail" bundle="common"/>');
                            
                        }
                }, "json");            
            }
            */
            
            function downLoadAttach(uid, folder, part){
				var downForm = document.downForm;
				
				var npart = jQuery("#nestedPartTmp").val();            	
            	npart = (npart !="")? npart+"|"+part:part;
            	            
                var param = {"folder":folder, "uid":uid, "part":part, "nestedPart":npart, "type":"normal","isApp":"true"};
                
            	downForm.uid.value = uid;
            	downForm.folder.value = doubleUrlEncode(folder);	                        	
            	downForm.nestedPart.value = npart;
            	downForm.type.value = "normal";
                downForm.action="/mail/downloadAttach.do";
                downForm.method="post";
            	downForm.submit();
				
			}
            function closePop() {
            	if (opener) {
            		opener.closePop();
            	}
            	window.close();
            }                     
        </script>
    </head>
    <body onload="init();">
        <div class="wrapper">
        	<input type="hidden" name="orgPart" id="orgPart" value="${orgPart}"/>
			<input type="hidden" name="nestedPartTmp" id="nestedPartTmp" value="${nestedPart}"/>
            <form name="downForm">
                <!-- <input type="hidden" name="folder"/> 
                <input type="hidden" name="uid"/>-->
                <input type="hidden" name="downAgent" value="mobile"/>     
                
                <input type="hidden" name="uid" value="${uid}"/>
				<input type="hidden" name="folder" value="${folder}"/>
				<input type="hidden" name="readType"/>
				<input type="hidden" name="sharedFlag" value="${sharedFlag}"/>
				<input type="hidden" name="sharedUserSeq" value="${sharedUserSeq}"/>
				<input type="hidden" name="sharedFolderName" value="${sharedFolderName}"/>
				<input type="hidden" name="part" value="${orgPart}"/>
				<input type="hidden" name="nestedPart"/>
				<input type="hidden" name="type" value="normal" />
				<input type="hidden" name="isApp" value="true" />   
            </form> 
            <%@include file="mail_top.jsp"%>        
            
            <div class="header">
				<h1>Terrace Mail Suite</h1>
				<div class="ts">
						<div class="btn_l"><a href="javascript:excuteAction('${folder}')"><span class="ic_tit_mail"><tctl:msg key="comn.top.mail" bundle="common"/></span></a></div>						
				</div>				
			</div>
            
            <!-- <script type="text/javascript">makeTopLink();</script>  -->
            <div class="container">
                <form name="readForm">
                    <input type="hidden" name="uid" value="${uid}"/>                                                                                
                    <input type="hidden" name="folderName" value="${folderName}"/>                                                    
                <div class="mail_view">
                    <div class="info">                      
                        <h3>
                            <span style="display:none"><tctl:msg key="mail.subject"/> : </span>
                            <c:if test="${not empty message.subject}">
								${fn:escapeXml(message.subject)}
							</c:if>
							<c:if test="${empty message.subject}">
								<tctl:msg key="header.nosubject"/>
							</c:if>
                        </h3>                       
                        <dl>
                            <dt><tctl:msg key="mail.from"/> :</dt>
                            <dd>${fn:escapeXml(message.fromString)}</dd>
                            <dt><tctl:msg key="mail.senddate"/> :</dt>
                            <dd>
                                <c:if test="${userLocale == 'ko'}">
                                <fmt:setLocale value="ko"/>
                                </c:if>
                                <c:if test="${userLocale == 'en'}">
                                <fmt:setLocale value="en"/>
                                </c:if>
                                <c:if test="${userLocale == 'jp'}">
                                <fmt:setLocale value="ja"/>
                                </c:if>
                                <!--<fmt:parseDate var="rdate" pattern="yyyyMMdd HH:mm:ss" value="${contents.date}"/>
                                <fmt:formatDate value="${rdate}" pattern="yyyy/MM/dd EEEE a h:mm:ss"/>-->
                                ${message.sentDateForRead}
                            </dd>
                            <dt><tctl:msg key="mail.to"/> :</dt>
                            <dd id="toAddr">
                                <c:forEach var="iaddr" items="${to}" varStatus="idx">
									<c:if test="${idx.index > 0}">,&nbsp;</c:if>
					  				<c:if test="${not empty iaddr.personal}">							
										"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;																			
									</c:if>
									<c:if test="${empty iaddr.personal}">
										${iaddr.address}									
									</c:if>
								</c:forEach>	
                            </dd>
                            <c:if test="${not empty contents.ccs}">
                            	<dt><tctl:msg key="mail.cc"/> :</dt>
                            	<dd id="ccAddr">
                                	<c:forEach var="iaddr" items="${message.cc}" varStatus="idx">
										<c:if test="${idx.index > 0}">,&nbsp;</c:if>							
										<c:if test="${not empty iaddr.personal}">							
											"${fn:escapeXml(iaddr.personal)}"&lt;${iaddr.address}&gt;									
										</c:if>
										<c:if test="${empty iaddr.personal}">
											${iaddr.address}									
										</c:if>
									</c:forEach>
                            	</dd>
                            </c:if>
                        </dl>
                    </div>                  
                    <span style="display:none"><tctl:msg key="search.body"/></span>
                    <div id="document_body" class="cont" style="word-wrap: break-word; word-break: break-all;">
                        <textarea id="messageText" style="display:none;">
	                        <c:out value="${htmlContent}" escapeXml="true"/>			
				 			<c:if test="${not empty imageAttach}">
							<div style="text-align: center">			  
							<c:forEach var="imagesUrl" items="${imageAttach}" varStatus="loop">
								<c:if test="${not empty imagesUrl}">				
									<hr>				
									<img src="${imagesUrl}"/>
								</c:if>				
							</c:forEach>
							</div>
							</c:if>	
                        </textarea>
                        <iframe frameborder="0" width="100%" height="100px" scrolling='no' src="/mobile/basic/mail/messageContent.html" id="messageContentFrame"></iframe>
                    </div>
                    <dl class="file">
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
				   			
				   			<dd>  							
				   			<c:if test="${fileData.size75 > 0 }">
				   			    <div>
				   			    	 <span style="margin-top:8px;background:none;">
				                  	<a href="javascript:;" onclick="downLoadAttach('${uid}','${folder}','${fileData.path}')" class="rdown">						
					   					<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
					   					${fileData.fileName} [<tctl:formatSize>${fileData.size75}</tctl:formatSize>]
					   					<%--${fileType}--%>
									</a>
									</span>
									
								     <span style="margin-top:5px;background:none;">
								 <c:if test="${fileType=='eml'}">
                                      <a style="" href="javascript:readNestedMessage('${uid}','${folder}','${fileData.path}')" class="btn_save" style="margin-left:10px;" title="<tctl:msg key="menu.open"/>">
                                          <tctl:msg key="menu.open"/>
                                      </a>
	                             </c:if>
                                 <c:if test="${fileType!='eml'}">
                                      <a style="" href="javascript:downLoadAttach('${uid}','${folder}','${fileData.path}')" class="btn_save" style="margin-left:10px;" title="<tctl:msg key="menu.open"/>">
                                            <tctl:msg key="menu.save"/>
                                      </a>
	                             </c:if>
	                             	</span>
	                             
								</div>								
								<!-- 
								<c:if test="${fileType=='eml'}">
									<a href="javascript:;" onclick="readNestedMessage('${uid}','${folder}','${fileData.path}')">						
					   				<img src="/design/common/image/blank.gif" alt="<tctl:msg key="mail.popupview"/>" title="<tctl:msg key="mail.popupview"/>" class='popupReadIcon' align='absmiddle'/>
									</a>
								</c:if>
								 -->
				   		    </c:if>
				   		    
				   		    <c:if test="${fileData.size75 == 0 }">
				   		    	<span class="rdeleted">
				   		    		<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp
				   					${fileData.fileName}
				   					[<tctl:msg key="mail.deleteattach"/>]
				   		    	</span>
				   		    </c:if>
				   		    </dd>
						</c:forEach>				
						
						<c:if test="${not empty vcards}">
						<strong>VCard :</strong>  
						<c:forEach var="vcardData" items="${vcards}" varStatus="loop">
							<a href="javascript:;" onclick="downLoadAttach('${uid}','${folder}','${vcardData.path}')" class="rdown">				
							<img src="/design/common/image/icon/ic_vcard.png"  align="absmiddle"></a> [<tctl:formatSize>${vcardData.size75}</tctl:formatSize>]
						</c:forEach>						
						</c:if>	
                    </dl>
                </div>
                
			<div class="footer">
				<%-- 
				<div class="footer_btn">
					<a href="javascript:closeWin()" class="btn2" title="<tctl:msg key="comn.close" bundle="common"/>"><span><tctl:msg key="comn.close" bundle="common"/></span></a>				
				</div>
				 --%>
 <%-- 
				<div class="footer_btn">
					<a href="javascript:excuteAction('${folder}')" class="btn2"><span class="ic_tit_mail"><tctl:msg key="comn.maillist" bundle="common"/></span></a>						
				</div>
 --%>
	  
				 <div class="footer_btn">
					<a href="javascript:closePop()" class="btn2"><span class="ic_tit_mail"><tctl:msg key="comn.close" bundle="common"/></span></a>						
				</div>
						
			</div>
                </form>     
            </div>
        </div>  
    </body>
</html>