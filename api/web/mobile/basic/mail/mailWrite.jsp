<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>		
		<script type="text/javascript" src="/js/ext-lib/jquery.autocomplete.js"></script>
		<script type="text/javascript">
			if(parent){
				try{				
				parent.toggleAddress();				
				} catch(e){}
			}
			
			pageCategory = "write";
			var USEREMAIL = '${email}';		

			function toggleRcpt(){
				var rcptBCCL = document.getElementById("rcptBCCL");
				if(rcptBCCL.style.display == "none"){
					rcptBCCL.style.display = "";
					document.btnRcptCrtl.src = "/design/common/image/btn/btn_content_minus.gif";	
				}
				else{
					rcptBCCL.style.display = "none";
					document.btnRcptCrtl.src = "/design/common/image/btn/btn_content_plus.gif";	
				}							
			}
			
			function insertMySelfNormal(obj) {		
				var f = document.writeForm;	
				if (obj.checked) {
					var toValue = f.to.value;
					var setValue;
					if (toValue.indexOf(USEREMAIL) < 0) {
						if (toValue != "") {
							if (toValue.substring(toValue.length - 1, toValue.length) == ',')
								setValue = toValue + USEREMAIL;
							else
								setValue = toValue + "," + USEREMAIL + ",";
						} else {
							setValue = USEREMAIL + ",";
						}
						f.to.value = setValue;
					}
				} else {
					var addr_array = getEmailArray(f.to.value);
					var pstr = "";		
					if (f.to.value != "") {
						var idx = 0;
						for ( var i = 0; i < addr_array.length; i++) {
							var address = addr_array[i];
							if (address.indexOf(USEREMAIL) < 0) {
								if(idx > 0)pstr += ",";																	
								pstr += address;
								idx++;
							}
						}
					}
					f.to.value = pstr;
				}
				f.to.focus();
			}

			function sendMail(sendType){
				var f = document.writeForm;
				var toObj = f.to;
				var ccObj = f.cc;
				var bccObj = f.bcc;
				
				var to = toObj.value;	
				var cc = ccObj.value;
				var bcc = bccObj.value;
				var subject = f.subject.value;


				if (sendType != "draft"  &&
					sendType == "normal" && 
					trim(to) == "") {
					
					alert('<tctl:msg key="error.norecipient"/>');					
					toObj.focus();
					return;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(to)) {
					toObj.focus();
					return;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(cc)) {
					ccObj.focus();
					return;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(bcc)) {
					bccObj.focus();
					return;
				}

				if (trim(subject) == "") {
					alert('<tctl:msg key="alert.nosubject"/>');
					f.subject.focus();
					return;
				}
				
				if(!checkInputText(f.subject,1,255,false)){
					return;
				}			
				
				f.sendType.value = sendType;
				if(f.receivnoti.checked){
					f.receivnoti.value="on";
				}
			
				f.attachList.value = getAttachString();
			
				if(sendType == "draft" ){
					if (f.signUse) {
						f.signUse.checked = false;
					}
					f.savesent.value = "off";					
					if(!confirm('<tctl:msg key="mail.send.drafts.confirm"/>')){
						return;
					}
				} else {
					if(!confirm('<tctl:msg key="mail.send.confirm"/>')){
						return;
					}
				}

				if(f.sendFlag.value == "draftForwardAttached"){
					f.sendFlag.value = "forwardAttached";
				}							
				f.submit();
			}
			function setBody(html){
				jQuery("body").empty();
				jQuery("body").html(html);
			}
			function checkEmailInvalidAddress(str) {
			    var addr_array = getEmailArray(str);

			    for(var i = 0; i < addr_array.length; i++) {       
			        
			        var address = addr_array[i];        
					address = trim(address);
					
			        var email = get_email(address);		
			        // Address Group Check
			        // Public Address Group Check
			        // Organization 
			        if(email.charAt(0) == '$'
			            || email.charAt(0) == '&'
			            || email.charAt(0) == '+'
			            || email.charAt(0) == '#') {
			            continue;
			        }
			       
			        var isDomain = false;
			        
					if(address == "") {
						continue;
					}else if(isEmail(email)) {			
						continue;
					}		
					else {
						alert('<tctl:msg key="alert.invalidaddress"/>\n\n'
							+address);
						return false;
					}
			    }

			    return true;
			}

			function cancel(){
				document.location.reload();
			}

			function goBack(){
				var f = document.writeForm;				
				if(trim(f.to.value) != "" || 
					trim(f.cc.value) != "" || 
					trim(f.bcc.value) != "" ||
					trim(f.subject.value) != "" ||
					trim(f.content.value) != ""){					
					if(confirm('<tctl:msg key="confirm.escapewrite"/>')){
						history.back();
					}
				} else {
					history.back();
				}						
			}

			function goFolderList(){
				var f = document.writeForm;	
				if(trim(f.to.value) != "" || 
					trim(f.cc.value) != "" || 
					trim(f.bcc.value) != "" ||
					trim(f.subject.value) != "" ||
					trim(f.content.value) != ""){					
					if(confirm('<tctl:msg key="confirm.escapewrite"/>')){
						document.location.href = "/mobile/mail/folderList.do";
					}
				} else {
					document.location.href = "/mobile/mail/folderList.do";
				}	
			}

			function init(){							 
				var f = document.writeForm;
				var to = document.getElementById("toTempVal").innerHTML;				
				var cc = document.getElementById("ccTempVal").innerHTML;
				var bcc = document.getElementById("bccTempVal").innerHTML;
				
				f.to.value = (to)?unescape_tag(trim(to)):"";				
				f.cc.value = (cc)?unescape_tag(trim(cc)):"";				
				f.bcc.value = (bcc)?unescape_tag(trim(bcc)):"";
												
				if(trim(f.cc.value) != "" || 
					trim(f.bcc.value) != ""){
					toggleRcpt();
				}

				setTitleBar('<tctl:msg key="mail.write"/>');

				jQuery("#to").autocomplete("/mail/searchEmailByName.do");	
				jQuery("#cc").autocomplete("/mail/searchEmailByName.do");	
				jQuery("#bcc").autocomplete("/mail/searchEmailByName.do");			
					
			}
			
			function checkAddrFrame(){
				try{
					window.frames.addrFrame.checkFlag();
				} catch(e){
					toggleAddress();
				}
			}

			var interval;
			function toggleAddress() {
				var addrFrameBox = document.getElementById("addrFrameBox");
				var addrFrame = document.getElementById("addrFrame");
				isLoadComplate = false;
				if (addrFrameBox.style.display != 'block') {					
					addrFrame.src = '/mobile/addr/writePrivateAddrList.do?dummy='+makeRandom();
					addrFrameBox.style.display = 'block';					
					hiddenWriteFrame(true);
				} else {										
					addrFrameBox.style.display = 'none';
					addrFrame.src = "about:blank";							
					hiddenWriteFrame(false);
					clearInterval(interval);										
				}
			}

			function resizeInterval() {
				if (isLoadComplate) {
					interval = setInterval("resizeAddr()",500);
				}
			}

			function resizeAddr() {
				if (isLoadComplate) {					
					window.frames.addrFrame.resizeAddrFrame();
					clearInterval(interval);
					isLoadComplate = false;
				}
			}
			function resizeAddrIFrame(height) {
				var addrFrame = document.getElementById("addrFrame");
				addrFrame.style.height = height+"px";
			}

			function hiddenWriteFrame(flag) {
				
				if (flag) {
					document.getElementById("title_top_box").style.display = 'none';
					document.getElementById("list_top_box").style.display = 'none';
					document.getElementById("list_sub_box").style.display = 'none';
					document.getElementById("mail_write_box").style.display = 'none';
					document.getElementById("list_bottom_box").style.display = 'none';
					document.getElementById("mail_upload_box").style.display = 'none';
				
				} else {
					document.getElementById("title_top_box").style.display = 'block';
					document.getElementById("list_top_box").style.display = 'block';
					document.getElementById("list_sub_box").style.display = 'block';
					document.getElementById("mail_write_box").style.display = 'block';
					document.getElementById("list_bottom_box").style.display = 'block';
					document.getElementById("mail_upload_box").style.display = 'block';

				}
			}

			function insertEmailAddress(type, value) {
				var f = document.writeForm;
				var existValue = "";
				var insertObj;
				var isMoreRcpt = false;
				if (type == 'to') {
					insertObj = f.to;
				} else if (type == 'cc') {
					insertObj = f.cc;
					isMoreRcpt = true;
				} else if (type == 'bcc') {
					insertObj = f.bcc;
					isMoreRcpt = true;
				}
				existValue = insertObj.value;
				if (trim(existValue) != "") {
					if (existValue.substring(existValue.length-1) == ",") {
						insertObj.value = existValue+value;
					} else {
						insertObj.value = existValue+","+value;
					}
				} else {
					insertObj.value = value;
				}
				toggleAddress();					
				if(isMoreRcpt && document.getElementById("rcptBCCL").style.display == "none"){
					toggleRcpt();		
				}
			}

			var isLoadComplate = false;
			function isLoadComplateAddr(flag) {
				isLoadComplate = flag;
			}

		
			var MAX_ATTACH_SIZE = ${maxAttachSize};
			
			
			function attachFile(){
				jQuery("#uploadFileInput").click();
			}
			
			function uploadfile() {	
				var f = document.uploadForm;
				if(f.theFile.value == "") {
					return;
				}
				document.getElementById("upload_att_Frame").innerHTML="<iframe name='up_hidden_frame' id='up_hidden_frame' src='about:blank' frameborder='0' width='1' height='1' style='width:1px;height:1px;'></iframe>";	
				f.uploadType.value = "basic";
				f.maxAttachFileSize.value = MAX_ATTACH_SIZE;
				f.action = "/file/uploadAttachFile.do";
				f.method = "post";
				f.target= "up_hidden_frame";	
				f.submit();	
				setBodyMask();
				
			}
			function setBodyMask() {
		        jQuery("body").append(jQuery("<div>").attr("id","mask").addClass("subMask"));
		        var theBody = document.getElementsByTagName("BODY")[0];
		        
		        var fullHeight = getViewportHeight();
		        var fullWidth = getViewportWidth();
		        
		        if (fullHeight > theBody.scrollHeight) {
		            popHeight = fullHeight;
		        } else {
		            popHeight = theBody.scrollHeight;
		        }
		        
		        if (fullWidth > theBody.scrollWidth) {
		            popWidth = fullWidth;
		        } else {
		            popWidth = theBody.scrollWidth;
		        }
		        
		        jQuery("#mask").css("height",popHeight + "px");
		        if(!jQuery.browser.mozilla){
		            jQuery("#mask").css("width",popWidth + "px");
		        }
		        
		        var width = jQuery(window).width();
                var height = jQuery(window).height();
                var msgFrame = jQuery("<div id='contentLoadMask'>");
                msgFrame.css("display","none"); 
                msgFrame.addClass("TM_c_loadding"); 
                jQuery("#mask").append(msgFrame);  
                var ctop = (height/2 - msgFrame.height()/2);
                var cleft = (width/2 - msgFrame.width()/2); 
                jQuery("#contentLoadMask").css({top:ctop+"px",left:cleft+"px"});
                setTimeout(function(){
                    jQuery("#contentLoadMask").show();
                },1)    
			    
			}
			
			function getViewportHeight() {
			    if (window.innerHeight!=window.undefined) return window.innerHeight;
			    if (document.compatMode=='CSS1Compat') return document.documentElement.clientHeight;
			    if (document.body) return document.body.clientHeight; 

			    return window.undefined; 
			}
			function getViewportWidth() {
			    var offset = 17;
			    var width = null;
			    if (window.innerWidth!=window.undefined) return window.innerWidth; 
			    if (document.compatMode=='CSS1Compat') return document.documentElement.clientWidth; 
			    if (document.body) return document.body.clientWidth; 
			}
			function makeUploadFrame(name){	
				
			}

			function addlist(n, s, p, uid, type) {
				jQuery("#mask").remove();
			    var f = document.uploadForm;
			    var select = f.attlist;
			    var text = n+" ("+s+" bytes)";
			    var _value = p + "\t" + n + "\t" + s + "\t" + uid + "\t";
			    var atype = (type)?type:"normal";			    
		    	if(atype == "normal"){
					// HTML UPLOAD
			        var attsize = parseInt(f.attsize.value) + parseInt(s);
			
			        if(attsize > (MAX_ATTACH_SIZE*1024*1024)) {
			            alert(MAX_ATTACH_SIZE+"MB <tctl:msg key="ocx.upalert_size"/>");
			            return;
			        }
			
			        f.attsize.value = attsize;
			        document.getElementById("totalsize").innerHTML = printSize(f.attsize.value);
			        
			        var len = f.attlist.length;
			        var newopt = new Option(text, _value, true, true);
			        select.options[len] = newopt;
			        
		    	}   
			    
			}


			function deletefile() {
				document.getElementById("upload_att_Frame").innerHTML="<iframe name='up_hidden_frame' id='up_hidden_frame' src='about:blank' frameborder='0' width='1' height='1' style='width:1px;height:1px;'></iframe>";
				var f = document.uploadForm;
				try{		
					f.enctype = "";
					f.target= "up_hidden_frame";
				    f.method = "post";    
				    
			        f.action = "/file/deleteAttachFile.do";
			
					if (f.attlist.value == "") {
						return ;
					}
			        f.attfile.value = f.attlist.value;
			        f.submit();
				} finally {
					f.enctype = "multipart/form-data";
				}  
			}

			function delattlist(src, name, size, upldtype) {
			    var f = document.uploadForm;
			    
			    var select = f.attlist;
			    var len = f.attlist.length;     
			    for(i = 0; i < len; i++) {
			        if(select.options[i].value.indexOf(src) == 0) {
			            select.options[i] = null;
			            select.selectedindex = len -1;            
			            f.attsize.value = parseInt(f.attsize.value) - parseInt(size);            
			            document.getElementById("totalsize").innerHTML = printSize(f.attsize.value);            
			        }
			    }
			}

			function getAttachString() {
				var pstr = "";				
				var f2 = document.uploadForm;

				for(var i = 0; i < f2.attlist.length; i++) {
					pstr += f2.attlist.options[i].value + "\n";
				}
				return pstr;
			}		

		


			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'><tctl:msg key="mail.write"/>"
				+"</a>";				

			}
		</script>
	</head>
	<body>
	
		<div class="wrapper">
		<%@include file="mail_top.jsp"%>
		<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>
		<script type="text/javascript">makeTopLink();</script>
						
			<div class="container">
				<form name="writeForm" method="post" action="/mobile/mail/mailSend.do" target="sendResultFrame">	
				<input type="hidden" name="sendType" id="sendType" value="${writeResultVO.writeType}"/>
				<input type="hidden" name="encode" id="encode" value="utf-8"/>				
				<input type="hidden" name="savesent" id="savesent" value="on"/>
				<input type="hidden" name="senderEmail" id="senderEmail" value="${writeResultVO.senderEmail}"/>
				<input type="hidden" name="senderName" id="senderName" value="${writeResultVO.senderName}"/>
				<input type="hidden" name="editMode" id="editMode" value="html"/>
				<input type="hidden" name="charset" id="charset" value="utf-8"/>			
				<input type="hidden" name="uid" id="uid" value="${writeResultVO.uidsValue}"/>
				<input type="hidden" name="draftMid" id="draftMid" value="${writeResultVO.draftMsgId}"/><!-- draftMid -->
				<input type="hidden" name="sendFlag" id="sendFlag" value="${writeResultVO.sendFlag}"/>
				<input type="hidden" name="folder" id="folder" value="${writeResultVO.folderName}"/>				
				<input type="hidden" name="attachList"/><!-- draftMid -->
			
				<div id="title_top_box" class="title_box">
					<div class="btn_l"><a href="javascript:goBack();" class="btn2" title="<tctl:msg key="mail.goback"/>"><span><tctl:msg key="mail.goback"/></span></a></div>
					<div class="btn_r"><a href="javascript:goFolderList();" class="btn2" title="<tctl:msg key="mail.folderlist"/>"><span><tctl:msg key="mail.folderlist"/></span></a></div>
				</div>					
				<div id="list_top_box" class="list_box">
					<div class="btn_l">
						<a href="javascript:sendMail('normal');" class="btn3"><span><tctl:msg key="menu.send"/></span></a><!-- 보내기 -->
						<a href="javascript:sendMail('draft');" class="btn3"><span><tctl:msg key="menu.draft"/></span></a><!-- 임시저장 -->
						<a href="javascript:cancel();" class="btn3"><span><tctl:msg key="mail.cancel"/></span></a><!-- 취소 -->
					</div>
					<div class="btn_r"><a href="javascript:toggleAddress()" class="btn_dr3"><tctl:msg key="intro.mail.addr.title"/></a></div>
				</div>
				<div id="list_sub_box" class="sub_box">
					<label><input type="checkbox" name="receivnoti" id="receivnoti" value="on" <c:if test="${writeResultVO.receiveNoti eq 'on'}">checked</c:if>/><tctl:msg key="menu.receivenoti"/></label><!-- 수신확인 -->
					<label><input type="checkbox" name="onesend" id="onesend" value="on"/><tctl:msg key="menu.onesend"/></label>
					<c:if test="${!empty signDataList}">
					<label><input type="checkbox" name="signUse" id="signUse" value="on" <c:if test="${writeResultVO.signAttach eq 'on'}">checked</c:if>/><tctl:msg key="mail.sign"/></label><!-- 서명첨부 -->
					<select id="signSeq" name="signSeq">						
						<c:forEach var="signData" items="${signDataList}">
							<option value="${signData.signSeq }" <c:if test="${signData.defaultSign eq 'T'}">selected</c:if>>
								<c:out value="${signData.signName}"/>
							</option>						
						</c:forEach>
					</select>
					</c:if>
				</div>
				<div id="addrFrameBox" style="display:none;"><iframe id="addrFrame" name="addrFrame" frameborder="0" scrolling="no" style="width:100%;"></iframe></div>
				<div id="mail_write_box" class="mail_write">
					<dl class="w1" id="document_body">
						<dt><tctl:msg key="mail.to"/></dt>
						<dd>
							<input type="text" name="to" id="to"/>
							<div id="toTempVal" style="display:none"><c:out value="${writeResultVO.to}"/></div>
						</dd>
					</dl>
					<div class="w1">
						<input type="checkbox" name="reply_me"  id="reply_me" onclick="insertMySelfNormal(this)"/><label for="reply_me"><tctl:msg key="mail.myself"/></label>
						&nbsp;
						<a href="javascript:;" onclick="toggleRcpt()">					
							<img src="/design/common/image/btn/btn_content_plus.gif" align="absmiddle" id="btnRcptCrtl" name="btnRcptCrtl" alt='<tctl:msg key="mail.cc"/>/<tctl:msg key="mail.bcc"/>'/>	
						</a>
						<tctl:msg key="mail.cc"/>/<tctl:msg key="mail.bcc"/>											
					</div>
					<div id="rcptBCCL"  class="w1" style="display:none">
						<dl class="w1">
							<dt><tctl:msg key="mail.cc"/></dt>
							<dd>
								<input type="text" name="cc" id="cc"/>
								<div id="ccTempVal" style="display:none"><c:out value="${writeResultVO.cc}"/></div>								
							</dd>
						</dl>
						<dl class="w1">
							<dt><tctl:msg key="mail.bcc"/></dt>
							<dd>
								<input type="text" name="bcc" id="bcc" />
								<div id="bccTempVal" style="display:none"><c:out value="${writeResultVO.bcc}"/></div>								
							</dd>
						</dl>
					</div>
					<dl class="w1">
						<dt><tctl:msg key="mail.subject"/></dt>
						<dd><input  name="subject" id="subject" value="<c:out value="${writeResultVO.subject}"/>"/></dd>
					</dl>
					<dl class="w2">
						<dd>
						<c:choose>
							<c:when test="${fn:indexOf(writeResultVO.sendFlag,'forward')>-1}">
						<textarea class="TM_write_input" name="content" id="content"><tctl:msg key="mail.forwardmessage"/></textarea>
							</c:when>
							<c:otherwise>
						<textarea class="TM_write_input" name="content" id="content"><c:out value="${writeResultVO.textNormalContent}"/></textarea>	
							</c:otherwise>
						</c:choose>						
						</dd>
					</dl>										
				</div>
				</form>
		
				<form name="uploadForm" method="post"				 
					enctype="multipart/form-data">
					<input type="hidden" name=upldtype value='upld'>
					<input type="hidden" name="writeFile" value="true">
					<input type="hidden" name="attfile" value="true">
					<input type="hidden" name="attsize" value="0">
					<input type="hidden" name="uploadType">
					<input type="hidden" name="maxAttachFileSize">
			        <input type="file" name="theFile"  id="uploadFileInput" onChange="uploadfile()" class="TM_attFile" style="opacity:0" accept="*"/>	
				<div id="mail_upload_box" class="mail_write">
					<dl class="w1">
						<dt><tctl:msg key="mail.attach"/></dt>
						<dd>
							<span id="att_simple_input_file">
							     <a href="javascript:;" class="btn2" onclick="attachFile();return false;"><span>파일첨부</span></a>
							</span>
							<a href="javascript:;" class="btn2" onclick="deletefile();return false;"><span><tctl:msg key="mail.attach.delete"/></span></a>
						</dd>
					</dl>
					<dl class="w2">
						<div><select size="3"  class="TM_attList"  name="attlist"></select></div>
						<span id="totalsize">0B</span> / ${maxAttachSize}MB
						<div id="upload_att_Frame" style="display:none"></div>
					</dl>
				</div>
				</form>
		
				<div id="list_bottom_box" class="list_box">
					<div class="btn_l">
						<a href="javascript:sendMail('normal');" class="btn3" title="<tctl:msg key="menu.send"/>"><span><tctl:msg key="menu.send"/></span></a>
						<a href="javascript:sendMail('draft');" class="btn3" title="<tctl:msg key="menu.draft"/>"><span><tctl:msg key="menu.draft"/></span></a>
						<a href="javascript:cancel();" class="btn3" title="<tctl:msg key="mail.cancel"/>"><span><tctl:msg key="mail.cancel"/></span></a>
					</div>
				</div>			
			</div>
		<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>
		
		<%@include file="/mobile/basic/common/footer.jsp"%>		
		</div>
		<script>init();</script>
		<iframe name="sendResultFrame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>
	</body>

</html>