<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">	
			function goList(){
				var url = "/mobile/mail/mailList.do";
				var paramArray = [];
				var lfolderName = "${lfolderName}";
				var folderName = doubleUrlEncode("${fn:escapeXml(folderName)}");
				if(lfolderName == "all"){
					folderName = "all";
				}				
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"folderName", value:folderName});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				
				executeUrl(url, paramArray);
			}

			function goPage(uid){
				var url = "/mobile/mail/mailRead.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"${fn:escapeXml(page)}"});
				paramArray.push({name:"uid", value:uid});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				
				executeUrl(url, paramArray);
			}
			var toaddr = [];
			var ccaddr = [];
			<c:forEach var="iaddr" items="${contents.tos}" varStatus="idx">
			toaddr[${idx.index}]="${fn:escapeXml(iaddr.emailAddress)}";									
			</c:forEach>			
			<c:forEach var="iaddr" items="${contents.ccs}" varStatus="idx">			
			ccaddr[${idx.index}] = "${fn:escapeXml(iaddr.emailAddress)}";	
			</c:forEach>
			
			function rcptAddrPrint(type){
				var addrStr ,addrPID, addrList;
				addrStr = "";
				var isOver = false;
				if(type == "to"){
					addrList = toaddr;
					addrPID = "toAddr";
				} else if(type == "cc"){
					addrList = ccaddr;
					addrPID = "ccAddr";
				}
				isOver = (addrList.length > 5)?true:false;
				
				for(var i = 0 ; i < addrList.length ; i++){
					if(isOver && i == 4){addrStr += "<div id='"+addrPID+"Hidden' style='display:none;font-size:0.95em;color:#666666'>";}
					if(i > 0){addrStr += ", ";}
					addrStr += addrList[i];
					
				}
				if(isOver){
					addrStr += "</div> <a href='#n' onclick='toggleRcpt(\""+type+"\")' class='btn2'><span id='"+addrPID+"HBtn'><tctl:msg key="comn.more" bundle="common"/></span></a>";
				}

				document.getElementById(addrPID).innerHTML = addrStr;
			}

			function toggleRcpt(type){
				var rcptLObj,rcptBObj;				
				if(type == "to"){
					rcptLObj = document.getElementById("toAddrHidden");
					rcptBObj = document.getElementById("toAddrHBtn");
				} else if(type == "cc"){
					rcptLObj = document.getElementById("ccAddrHidden");
					rcptBObj = document.getElementById("ccAddrHBtn");
				}

				if(rcptLObj.style.display == "none"){
					rcptLObj.style.display = "block";
					rcptBObj.innerHTML = "<tctl:msg key="comn.close" bundle="common"/></span></a>";
				} else {
					rcptLObj.style.display = "none";
					rcptBObj.innerHTML = "<tctl:msg key="comn.more" bundle="common"/></span></a>";
				}
			}

			function goWrite(type){
				var url = "/mobile/mail/mailWrite.do";
				var paramArray = [];
				paramArray.push({name:"wtype", value:type});
				paramArray.push({name:"uid", value:${uid}});
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				if(type == "forward"){
					paramArray.push({name:"fwmode", value:"attached"});
				}

				executeUrl(url, paramArray);	
			}


			function deleteMessage() {
				var f = document.readForm;

				if (!confirm('<tctl:msg key="confirm.delete"/>')) {
					return;
				}
								
				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"flag", value:"${flag}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"delete"});
				paramArray.push({name:"uids", value:${uid}});
				paramArray.push({name:"page", value:"${page}"});
				paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});

				executeUrl(url, paramArray);
			}

			function moveMessage(folderName) {
				var f = document.readForm;				

				var url="/mobile/mail/mailWork.do";
				var paramArray = [];
				paramArray.push({name:"folderName", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});
				paramArray.push({name:"targetFolderName", value:doubleUrlEncode(folderName)});
				paramArray.push({name:"flag", value:"${flag}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"workMode", value:"move"});
				paramArray.push({name:"uids", value:"${uid}"});
				paramArray.push({name:"page", value:"${page}"});
				paramArray.push({name:"folderNames", value:doubleUrlEncode("${fn:escapeXml(folderName)}")});

				executeUrl(url, paramArray);
			}

			function downLoadAttach(uid, folder, part){
				var downForm = document.downForm;
            	downForm.uid.value = uid;
            	downForm.folder.value = doubleUrlEncode(folder);	                        	
            	downForm.part.value = part;
            	downForm.method = "post";            	
                downForm.action="/mail/downloadAttach.do";                
            	downForm.submit();
			}

			function init() {
				setTitleBar("${contents.subject}");
			}

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
			
			var popWin;
            function readNestedMessage(uid, folder, part) {            	
            	var downForm = document.downForm;
            	downForm.uid.value = uid;
            	downForm.folder.value = folder;	                        	
            	downForm.part.value = part;
            	
            	var wname = "nestedPopupRead";            	
            	popWin = window.open("about:blank",wname,"resizable=no,scrollbars=yes");
            	downForm.method = "post";            	
                downForm.action="/mobile/mail/readNestedMessageMobile.do";                
            	downForm.target = wname;
            	downForm.submit();		            	            	
            }
          
            function closePop() {
            	popWin.close();
            }
		</script>
	</head>
	<body onload="init();">
		<div class="wrapper">
			<form name="downForm">
				<input type="hidden" name="folder"/>
				<input type="hidden" name="uid"/>
				<input type="hidden" name="part"/>
				<input type="hidden" name="downAgent" value="mobile"/>
			</form>	
			<%@include file="mail_top.jsp"%>		
			<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			<div class="container">
				<form name="readForm">
					<input type="hidden" name="uid" value="${fn:escapeXml(uid)}"/>
					<input type="hidden" name="sortBy" value="${fn:escapeXml(sortBy)}"/>
					<input type="hidden" name="sortDir" value="${fn:escapeXml(sortDir)}"/>
					<input type="hidden" name="page" value="${fn:escapeXml(page)}"/>
					<input type="hidden" name="pageBase" value="${fn:escapeXml(pageBase)}"/>
					<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}"/>
					<input type="hidden" name="folderName" value="${fn:escapeXml(folderName)}"/>
				<div class="title_box">
					<div class="btn_l">
						<c:if test="${!empty contents.preUid && contents.preUid > 0}">
						<a href="javascript:goPage(${contents.preUid});" class="btn2" title="<tctl:msg key="comn.page.pre" bundle="common"/>"><span>&lt;</span><span class="blind"><tctl:msg key="comn.page.pre" bundle="common"/></span></a>
						</c:if>
						<a href="javascript:goList();" class="btn2" title="<tctl:msg key="comn.list" bundle="common"/>"><span><tctl:msg key="comn.list" bundle="common"/></span></a>
						<c:if test="${!empty contents.nextUid && contents.nextUid > 0}">
						<a href="javascript:goPage(${contents.nextUid});" class="btn2" title="<tctl:msg key="comn.page.next" bundle="common"/>"><span>&gt;</span><span class="blind"><tctl:msg key="comn.page.next" bundle="common"/></span></a>
						</c:if>
					</div>
					<div class="btn_r"><a id="link_2"  href="#n" onclick="goWrite();" class="btn2" title="<tctl:msg key="mail.write"/>"><span><tctl:msg key="mail.write"/></span></a></div>
				</div>					
				<div class="list_box">
					<div class="btn_l">
						<a href="javascript:goWrite('reply')" class="btn6" title="<tctl:msg key="menu.reply"/>"><span><tctl:msg key="menu.reply"/></span></a>
						<a href="javascript:goWrite('replyall')" class="btn6" title="<tctl:msg key="menu.replyall"/>"><span><tctl:msg key="menu.replyall"/></span></a>
						<a href="javascript:goWrite('forward')" class="btn6" title="<tctl:msg key="menu.forward"/>"><span><tctl:msg key="menu.forward"/></span></a>
						<a href="javascript:deleteMessage();" class="btn6" title="<tctl:msg key="menu.delete"/>"><span><tctl:msg key="menu.delete"/></span></a>
					</div>
					<div class="btn_r"><a id="link_9" href="javascript:void(0);" onclick="viewSelectMenu(event,'move')" class="btn_dr3" title="<tctl:msg key="menu.move"/>"><tctl:msg key="menu.move"/></a></div>
				</div>	
				<div class="mail_view">
					<div class="info">						
						<h3>
							<span style="display:none"><tctl:msg key="mail.subject"/> : </span>
							<c:if test="${empty contents.subject}">
								<tctl:msg key="header.nosubject"/>
							</c:if>
							<c:if test="${!empty contents.subject}">
								${fn:escapeXml(contents.subject)}
							</c:if>
						</h3>						
						<dl>
							<dt><tctl:msg key="mail.from"/> :</dt>
							<dd>${fn:escapeXml(contents.from.emailAddress)}</dd>
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
								<fmt:parseDate var="rdate" pattern="yyyyMMdd HH:mm:ss" value="${contents.date}"/>
								<fmt:formatDate value="${rdate}" pattern="yyyy/MM/dd EEEE a h:mm:ss"/>
							</dd>
							<dt><tctl:msg key="mail.to"/> :</dt>
							<dd id="toAddr">
								<script>rcptAddrPrint("to");</script>
							</dd>
							<c:if test="${not empty contents.ccs}">
							<dt><tctl:msg key="mail.cc"/> :</dt>
							<dd id="ccAddr">
								<script>rcptAddrPrint("cc");</script>
							</dd>
							</c:if>
						</dl>
					</div>					
					<span style="display:none"><tctl:msg key="search.body"/></span>
					<div id="document_body" class="cont" style="word-wrap: break-word; word-break: break-all;">
						<textarea id="messageText" style="display:none;">
						${fn:escapeXml(contents.contents)}
						</textarea>
						<iframe frameborder="0" width="100%" height="100px" scrolling='no' src="/mobile/basic/mail/messageContent.html" id="messageContentFrame"></iframe>
					</div>
					<dl class="file">
						<dt>
						<c:if test="${contents.attachsCnt == 0}">
							<tctl:msg key="mail.noattach" />
						</c:if>
						<c:if test="${contents.attachsCnt > 0}">
							${contents.attachsCnt} <tctl:msg key="mail.existattach" />
						</c:if>
						</dt>
						
						<c:if test="${contents.attachsCnt > 0}">
						<c:forEach var="fileData" items="${contents.attachs}" varStatus="loop">				
							<c:forTokens var="file" items="${fileData.name}" delims=".">
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
					 				<c:if test="${fileData.size > 0 }">
					 					<div style="float:left;width:80%;">
										<span class="fileName"><img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp;
						 				${fileData.name}
						 				<script language=javascript>
										document.write('['+ printSize(Math.round( ${fileData.size* 0.964981} ) ) +']');
						  		    	</script>
						 				</span>						 				
						 				</div>
						 				
						 				<c:if test="${fileType=='eml'}">
											<div>
												<a href="javascript:readNestedMessage('${contents.uid}','${contents.folderEncName}','${fileData.depth}')" class="btn_save" style="margin-left:10px;" title="<tctl:msg key="menu.open"/>">
													<tctl:msg key="menu.open"/>
												</a>
											</div>                                        	
										</c:if>
						 				<c:if test="${fileType!='eml'}">
							 				<div>						 				
								 				<a href="javascript:downLoadAttach('${contents.uid}','${contents.folderEncName}','${fileData.depth}')" class="btn_save" style="margin-left:10px;" title="<tctl:msg key="menu.save"/>">
													<tctl:msg key="menu.save"/>
												</a>
											</div>
										</c:if>  
									</c:if>
								</dd>	   	
						</c:forEach>
								<dd class="download_info">
					 				<span>* <tctl:msg key="mail.attach.download.info"/></span>
								</dd>						
						</c:if>
						
					</dl>
				</div>

				<div class="list_box">
					<div class="btn_l">
						<a href="javascript:goWrite('reply')" class="btn6" title="<tctl:msg key="menu.reply"/>"><span><tctl:msg key="menu.reply"/></span></a>
						<a href="javascript:goWrite('replyall')" class="btn6" title="<tctl:msg key="menu.replyall"/>"><span><tctl:msg key="menu.replyall"/></span></a>
						<a href="javascript:goWrite('forward')" class="btn6" title="<tctl:msg key="menu.forward"/>"><span><tctl:msg key="menu.forward"/></span></a>
						<a href="javascript:deleteMessage();" class="btn6" title="<tctl:msg key="menu.delete"/>"><span><tctl:msg key="menu.delete"/></span></a>
					</div>
					<div class="btn_r"><a href="javascript:void(0);" onclick="viewSelectMenu(event,'moveBottom');" class="btn_dr3" title="<tctl:msg key="menu.move"/>"><tctl:msg key="menu.move"/></a></div>
				</div>
				</form>		
			</div>
		</div>
		<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>
		
		<%@include file="/mobile/basic/common/footer.jsp"%>
	</body>
</html>