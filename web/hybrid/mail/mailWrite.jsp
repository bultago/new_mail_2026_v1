<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
	<style>     
		#mask {
	        position:absolute;        
	        z-index:9000;        
	        background-color:#000; 
	        opacity:0.8;       
	        display:none;        
	        left:0;      
	        top:0;    
		}     
		.window{
			display: none;      
			position:absolute;        
			left:100px;      
			top:100px;      
			z-index:10000;    
			border: 4px solid #4B97E2;
			border-radius: 5px;
			background-color: #4B97E2;
		}   
		
		.window div{
			border:0px solid #4B97E2;
			width:150px; 
			height:35px;
			text-align:center;
			padding-top:15px;
			background-color:#fff;		
			font-weight:bold;
			font-size: 18px;
				
		}
		
		.window .top{
			border-radius:5px 5px 0px 0px;
			border-bottom:1px solid #4B97E2;
		}
		
		.window .bottom{
			border-radius:0px 0px 5px 5px;
			border-top:1px solid #4B97E2;
		}
		
		.deleteImg {
			float:right;
			text-align:right;
			background:url(/design/mobile/image/btn_close.png) center center no-repeat;
			width:35px;
			height:35px;
			background-size:15px,15px;	
			margin-top:2px;				
		}
		
		.addrWrapdeleteImg{
			float:right;
			text-align:right;
			background:url(/design/mobile/image/btn_close.png) center center no-repeat;
			width:25px;
			height:20px;
			background-size:15px,15px;			
		}
		
		.attachPart{
			border-bottom:1px solid #DFDFDF;
			padding:10px;
			height:40px;
			line-height:260%;
		}
		.addrRadiusWrap{
			border:1px solid #ADC6EC;
			background-color:#EDF4FC;
			display:block;
			padding:5px 0px 5px 5px;
			border-radius: 10px;
			margin-bottom: 5px;
			height:20px;
			line-height:130%;
		}
		.addrWrapElli{
			text-overflow:ellipsis;
			display:inline-block;
			overflow:hidden;
			white-space:nowrap;
			width:80%;
			font-weight:bold !important;
		}
		.autoCompleteRow{
			font-size:110% !important;
			padding:6px !important;
			border-bottom:1px solid #ccc;
			border-left:1px solid #ccc;
			border-right:1px solid #ccc;
			background-color:#E4E5E9;
			color:#3881e2;
		}
		.fileAttachName{
			text-overflow:ellipsis;
			display:inline-block;
			overflow:hidden;
			white-space:nowrap;
			width:60%;
		}
		
		.sendCheckWrap{
			display: none;      
			position:absolute;        
			z-index:10000;    
			border: 3px solid #4B97E2;
			border-radius: 5px;
			background-color: #4B97E2;	
			left:3px;		
			font-size: 15px;
		}
		
		.sendCheckWrap div.alert{
			border:0px solid #4B97E2;
			width:100%; 
			text-align:left;
			padding-top:5px;
			background-color:#fff;
			font-size: 15px;
			border-bottom:1px solid #c6c6c6;
		}
		
		.sendCheckWrap div.title{
			height:30px;	
			border-bottom:1px solid #c6c6c6;
			color: white;
			font-weight: bold;
			text-align: center;
			line-height: 200%;
			font-size: 15px;
		}
		.applyTitle{
			font-size:15px;
			font-weight:bold;
			background-color:#F7F7F7;
			height:20px;	
			text-align:center;		
			border-bottom:1px solid #c6c6c6;
			padding-left:10px;
		}
		.applyContent{
			background-color:#fff;
			font-size:15px;
			min-height:25px;
			text-align:left;
			border-bottom:1px solid #c6c6c6;
			line-height: 200%;
			padding-left:10px;
		}
		.applyBackground{
			background-color:#fff;
			text-align: center;
			height: 30px;
			padding-top: 10px;
		}
		
		.autoCompleteWrap div:first-child{
			border-top:1px solid #ccc;
		}
	 </style>
		<%@include file="/hybrid/common/header.jsp"%>		
		
		<script type="text/javascript">
		
		var MAX_ATTACH_SIZE_MB = ${maxAttachSize};
		var CURRENT_ATTACH_SIZE_BYTE = 0;
		
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
			
			function insertMySelfEmail(obj){
				if (obj.checked) {
					
					jQuery("#toAddrWrap").append("<span id='' class='addrRadiusWrap myselfEmail' data-addr='"+USEREMAIL+"'><span class='addrWrapElli'>"
							+USEREMAIL+
							"</span><span class='addrWrapdeleteImg' onclick='deleteAddrWrap(this)'></span></span>");
							
				}else{
					jQuery(".myselfEmail").css("display","none");					
				}
			}
			
			function getEmailAddr(wrapId){
				var toWrap = jQuery("#"+wrapId);
				var len = toWrap.children().length;
				var addrData = "";
				if( len > 0){
					toWrap.children().each(function(i){						
						var wrapBlock = jQuery(this).css("display");
						if(wrapBlock == "block"){
							if(i === len -1){
								addrData += jQuery(this).attr("data-addr");
							}else{
								addrData += jQuery(this).attr("data-addr") + ",";
							}
							addrData = addrData.replace(/&lt;/g, "<");
							addrData = addrData.replace(/&gt;/g, ">");
							addrData = addrData.replace(/&quot;/g, "\"");
						}
					});
				}
				return addrData;
			}
			
			function getEmailAddrNoPersonal(wrapId){
				var toWrap = jQuery("#"+wrapId);
				var len = toWrap.children().length;
				var addrData = "";
				if( len > 0){
					toWrap.children().each(function(i){						
						var wrapBlock = jQuery(this).css("display");
						if(wrapBlock == "block"){
							if(i === len -1){
								addrData += get_email(unescape_tag(jQuery(this).attr("data-addr")));
							}else{
								addrData += get_email(unescape_tag(jQuery(this).attr("data-addr"))) + ",";
							}							
						}
					});
				}
				return addrData;
			}

			function sendMail(sendType){
				
				var f = document.writeForm;
				
				if(!checkSendInfo(sendType)){
					return;
				}		
				
				//file attach
				
				var attachWrap = jQuery("#fileAttachWrap");
				if( attachWrap.children().length > 0){
					
					var len = attachWrap.children().length;
					var attachData = "";
					attachWrap.children().each(function(i){						
						
						var test = jQuery(this).css("display");
						if(test == "block"){
							if(i === len -1){
								attachData += jQuery(this).attr("data-down");
							}else{
								attachData += jQuery(this).attr("data-down") + "\n";
							}
						}
					});
					f.attachList.value = attachData;
				}
				
				var to = getEmailAddr("toAddrWrap");
				var cc = getEmailAddr("ccAddrWrap");
				var bcc = getEmailAddr("bccAddrWrap");
				
				f.to.value = to;
				f.cc.value = cc;
				f.bcc.value = bcc;
				if("${writeResultVO.sendFlag}" == "forward"){
					mobileForward();
				}
				
				f.action = "/hybrid/mail/mailSend.do";
				f.submit();
			}
			
			function mobileForward(){
				var f = document.writeForm;
				f.editMode.value = "html";
				
				//alert("orgAttach["+f.forwardOrgAttach.value+"]");
				
				if(f.forwardOrgAttach.value != ""){
					var tempAddrList = f.attachList.value;
					if(tempAddrList == ""){
						f.attachList.value = f.forwardOrgAttach.value;
						//alert("null["+f.attachList.value+"]");
					}else{
						//alert("tempaddrList["+tempAddrList+"]");
						f.attachList.value = f.forwardOrgAttach.value + tempAddrList;
						//alert("notnull["+f.attachList.value+"]");
					}
				}
				
				var tempContent = escapeForward(f.content.value);				
				
				var ascs = "";				
				jQuery.each(tempContent, function(i,v){
					var asc = v.charCodeAt(0);
					ascs += asc==10?"<br/>":v;
				});
				
				f.content.value = ascs + f.forwardOrgText.value;
			}
			
			function escapeForward(s) {
				if (s && Number(s)) {
					return s;
				}
				
				var r = "";
				if(s && s != "" && typeof(s) == "string"){
					r = replaceAll(s, '<', '&lt;');
					r = replaceAll(r, '>', '&gt;');
					r = replaceAll(r, '\"', '&quot;');
					r = replaceAll(r, '\"', '&#034;');
					r = replaceAll(r, '\'', '&#039;');
					r = replaceAll(r, '\r', '<br>');
				}
				return r;
			}
			
			function checkSendInfo(sendType){
				
				var f = document.writeForm;
				
				var to = getEmailAddr("toAddrWrap");
				var cc = getEmailAddr("ccAddrWrap");
				var bcc = getEmailAddr("bccAddrWrap");
				
				var subject = f.subject.value;

				if (sendType != "draft"  &&
					sendType == "normal" && 
					trim(to) == "") {
					
					alert('<tctl:msg key="error.norecipient"/>');					
					return false;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(to)) {
					return false;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(cc)) {
					return false;
				}

				if(sendType != "draft" &&
						!checkEmailInvalidAddress(bcc)) {
					return false;
				}

				if (trim(subject) == "") {
					alert('<tctl:msg key="alert.nosubject"/>');
					f.subject.focus();
					return false;
				}
				
				if(!checkInputText(f.subject,1,255,false)){
					return false;
				}			
				
				f.sendType.value = sendType;
				if(f.receivnoti.checked){
					f.receivnoti.value="on";
				}
				
				if(sendType == "draft" ){
					if (f.signUse) {
						f.signUse.checked = false;
					}
					f.savesent.value = "off";					
					if(!confirm('<tctl:msg key="mail.send.drafts.confirm"/>')){
						return false;
					}
				} else {
					if(!confirm('<tctl:msg key="mail.send.confirm"/>')){
						return false;
					}
				}

				if(f.sendFlag.value == "draftForwardAttached"){
					f.sendFlag.value = "forwardAttached";
				}
				
				return true;
				
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
				if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){
					if(confirm('<tctl:msg key="confirm.escapewrite"/>')){
						history.back();
					}
				} else {
					history.back();
				}						
			}
			
			function nativeGoBack(){
				
				var f = document.writeForm;				
				if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){					
					if(confirm('<tctl:msg key="confirm.escapewrite"/>')){
						if(checkOS() == "android"){
							eval("window.TMSMobile.goBack('true')");
						}else{					
							window.location = "tmsmobile://goBack?true";
						}
					}
				} else {
					if(checkOS() == "android"){
						eval("window.TMSMobile.goBack('true')");
					}else{					
						window.location = "tmsmobile://goBack?true";
					}		
				}
			}

			function goFolderList(){
				var f = document.writeForm;	
				if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){					
					if(confirm('<tctl:msg key="confirm.escapewrite"/>')){
						document.location.href = "/hybrid/mail/folderList.do";
					}
				} else {
					document.location.href = "/hybrid/mail/folderList.do";
				}
			}
			
			function setAttach(attachUrl){
				
				var attachPart = attachUrl.split("\t");
				var fileName = attachPart[1];
				var fileSize = parseInt(attachPart[2]);
				
				
				var fileformat = fileName.substring(fileName.lastIndexOf(".")+1);
				var reg = "avi|bmp|doc|docx|eml|excel|gif|htm|html|hwp|jpg|mp3|mp4|mpeg|pdf|ppt|pptx|txt|xls|xlsx|xml|zip";
				var imgUrl;
				
				if(!fileformat.match(reg)){
					fileformat = "unknown";	
				}		
				
				imgUrl = "/design/common/image/icon/ic_att_"+fileformat+".gif";
				
				fileSize = parseInt(fileSize/1024);
				var attachWrap = document.getElementById('fileAttachWrap');
				attachWrap.innerHTML += "<div class='attachPart' id='"+attachUrl+"' data-down='"+attachUrl+"' data-filename='"+fileName+"' data-filesize='"+fileSize+"'>"+
									"<img src='"+imgUrl+"'/>&nbsp;"+
									 "<span class='fileAttachName'>"+fileName+"</span><div class='deleteImg' onclick='deleteAttach(this)'></div><span style='float:right'>["+fileSize+"KB]</span>"+
									 "</div>";
				
				
			}
			
			function parseWriteAttach(){
				
				var attachString = jQuery("#attachString").val();
				var attach = attachString.split("\n");
				
				for(var i=0 ; i<attach.length -1 ; i++){
					setAttach(attach[i]);
				}
				
			}

			function init(){
								
				var f = document.writeForm;
				var to = document.getElementById("toTempVal").innerHTML;				
				var cc = document.getElementById("ccTempVal").innerHTML;
				var bcc = document.getElementById("bccTempVal").innerHTML;
				
				
				if(to != ""){
					if(to.indexOf(',') > -1){
						var toArray = to.split(',');
						for(var i =0 ; i<toArray.length ; i++){
							makeAddrWrap("to",trim(toArray[i]));
						}
					}else{
						makeAddrWrap("to",trim(to));	
					}
					
				}
				
				if(cc != ""){
					if(cc.indexOf(',') > -1){
						var ccArray = cc.split(',');
						for(var i =0 ; i<ccArray.length ; i++){
							makeAddrWrap("cc",trim(ccArray[i]));
						}
					}else{
						makeAddrWrap("cc",trim(cc));
					}
					toggleRcpt();
				}
				
				if(bcc != ""){
					if(bcc.indexOf(',') > -1){
						var bccArray = bcc.split(',');
						for(var i =0 ; i<bccArray.length ; i++){
							makeAddrWrap("bcc",trim(bccArray[i]));
						}
					}else{
						makeAddrWrap("bcc",trim(bcc));
					}
					toggleRcpt();
				}
				
				parseWriteAttach();				
				

				setTitleBar('<tctl:msg key="mail.write"/>');
				
				if(checkOS() == "android"){
					eval("window.TMSMobile.getAuthKey('setAuthKey','','','')");
				}else{
					window.location = "tmsmobile://getAuthKey?setAuthKey&uid&folder&part";
				}
				
				
				
				//authKey = "4c3972506175714d4533644e766c2b644f68615966673d3d"; // mailadm test authKey				
				jQuery("#toInput").keyup(function(e){
					if(e.keyCode == 188 || e.keycode == 13){
						appendAddr('to');
					}else{
						autoComplete('to');
					}
				}).blur(function(){
					checkBlur('to');
				});
				
				jQuery("#ccInput").keyup(function(e){
					if(e.keyCode == 188 || e.keycode == 13){
						appendAddr('cc');
					}else{
						autoComplete('cc');
					}
				}).blur(function(){
					checkBlur('cc');
				});
				
				jQuery("#bccInput").keyup(function(e){
					if(e.keyCode == 188 || e.keycode == 13){
						appendAddr('bcc');
					}else{
						autoComplete('bcc');
					}
				}).blur(function(){
					checkBlur('bcc');
				});				
					
			}
			
			function checkBlur(type){
				var addrwrap = jQuery("#"+type+"AutoWrap").html();
				
				if(addrwrap == ""){
					appendAddr(type);
					return;
				}
				
				if(jQuery("#"+type+"AutoWrap").children().length == 1){
					makeAddrWrap(type,jQuery("#"+type+"AutoWrap").children().html());
					jQuery("#"+type+"Input").val('');
					jQuery("#"+type+"AutoWrap").html('');
					return;
				}
			}
			
			function autoComplete(type){
				var keyword = jQuery("#"+type+"Input").val();
				
				if(keyword == null || keyword == ""){
					jQuery("#"+type+"AutoWrap").html('');
					return;
				}
				
				var url = "/hybrid/mailSearchEmailService.do?authKey="+authKey+"&keyword="+keyword+"&autoAddr=?"; 
				jQuery.ajax({
					type: 'GET',   
					url: url,    
					async: false,
					jsonpCallback: 'autoAddr', 
					contentType: "application/json",
					dataType: 'jsonp',    
					success: function(json) {
						var addrHtml = "";
						
						for(var i=0 ; i<json.addrList.length ; i++){
							addrHtml += "<div class='autoCompleteRow' onclick='autoCompleteAppend(this,\""+type+"\")'>"+json.addrList[i]+"</div>";     
						}
						jQuery("#"+type+"AutoWrap").html('');
						jQuery("#"+type+"AutoWrap").html(addrHtml);
					},    
					error: function(e) {
						console.log("1:  "+e.message);    
					}});
			}
			
			function autoCompleteAppend(obj,receive){
				
				jQuery("#"+receive+"AutoWrap").html('');
				jQuery("#"+receive+"Input").val('');
				
				
				makeAddrWrap(receive,jQuery(obj).html());
				
				/*jQuery("#"+receive+"AddrWrap").append("<span class='addrRadiusWrap' data-addr='"+jQuery(obj).html()+"'><span class='addrWrapElli'>"
						+jQuery(obj).html()+
						"</span><span class='addrWrapdeleteImg' onclick='deleteAddrWrap(this)'></span></span>");*/
				
				
			}
			
			function setAuthKey(r){
				authKey = r.authKey;
			}
			
			function appendAddr(receive){
				var addr = jQuery("#"+receive+"Input").val();
				
				if(addr != ""){
					makeAddrWrap(receive,addr);
				}
				
				/*jQuery("#"+receive+"AddrWrap").append("<span class='addrRadiusWrap' data-addr='"+addr+"'><span class='addrWrapElli'>"
						+addr+
						"</span><span class='addrWrapdeleteImg' onclick='deleteAddrWrap(this)'></span></span>");*/
				
				jQuery("#"+receive+"Input").val('');				
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
					addrFrame.src = '/hybrid/addr/writePrivateAddrList.do?dummy='+makeRandom();
					addrFrameBox.style.display = 'block';					
//					hiddenWriteFrame(true);
				} else {										
					addrFrameBox.style.display = 'none';
//					addrFrame.src = "about:blank";							
//					hiddenWriteFrame(false);
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
					<%if (!isMobile) {%>
					document.getElementById("mail_upload_box").style.display = 'none';
					<%}%>
				} else {
					document.getElementById("title_top_box").style.display = 'block';
					document.getElementById("list_top_box").style.display = 'block';
					document.getElementById("list_sub_box").style.display = 'block';
					document.getElementById("mail_write_box").style.display = 'block';
					document.getElementById("list_bottom_box").style.display = 'block';
					<%if (!isMobile) {%>
					document.getElementById("mail_upload_box").style.display = 'block';
					<%}%>
				}
			}

			function insertEmailAddress(type, value) {
				var f = document.writeForm;
				var existValue = "";
				var insertObj;
				var isMoreRcpt = false;
				if (type == 'to') {
					//insertObj = f.to;
				} else if (type == 'cc') {
					//insertObj = f.cc;
					isMoreRcpt = true;
				} else if (type == 'bcc') {
					//insertObj = f.bcc;
					isMoreRcpt = true;
				}
								
				makeAddrWrap(type,value);
				
				/*existValue = insertObj.value;
				if (trim(existValue) != "") {
					if (existValue.substring(existValue.length-1) == ",") {
						insertObj.value = existValue+value;
					} else {
						insertObj.value = existValue+","+value;
					}
				} else {
					insertObj.value = value;
				}*/
				
				
				toggleAddress();					
				if(isMoreRcpt && document.getElementById("rcptBCCL").style.display == "none"){
					toggleRcpt();		
				}
			}

			var isLoadComplate = false;
			function isLoadComplateAddr(flag) {
				isLoadComplate = flag;
			}

			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'><tctl:msg key="mail.write"/>"
				+"</a>";				

			}
			
			function doubleCheckLayerClose(){
				
				jQuery("#mask").hide();              
				jQuery('.sendCheckWrap').hide();
				initDoubleCheckLayer();		
				
			}
					
			function nativeAttachCall(type){
				
				jQuery("#mask").hide();              
				jQuery('.window').hide();
				
				if(type == "camera"){
					if(checkOS() == "android"){
						eval("window.TMSMobile.callCamera('attachFileSuccess','attachFileFail')");
					}else{					
						window.location = "tmsmobile://callCamera?attachFileSuccess&attachFileFail";
					}
					
				}else{
					if(checkOS() == "android"){
						eval("window.TMSMobile.callAlbum('attachFileSuccess','attachFileFail')");
					}else{
						window.location = "tmsmobile://callAlbum?attachFileSuccess&attachFileFail";
					}
				}
			}
			function attachFileSuccess(r){
				
				
				CURRENT_ATTACH_SIZE_BYTE += parseInt(r.fileSize);
				
				if(CURRENT_ATTACH_SIZE_BYTE > MAX_ATTACH_SIZE_MB * 1024 * 1024){
					alert(MAX_ATTACH_SIZE_MB+'MB <tctl:msg key="ocx.upalert_size"/>');
					return;
				}
				
				var fileformat = r.fileName.substring(r.fileName.lastIndexOf(".")+1);
				var reg = "avi|bmp|doc|docx|eml|excel|gif|htm|html|hwp|jpg|mp3|mp4|mpeg|pdf|ppt|pptx|txt|xls|xlsx|xml|zip";
				var imgUrl;
				
				if(!fileformat.match(reg)){
					fileformat = "unknown";	
				}		
				imgUrl = "/design/common/image/icon/ic_att_"+fileformat+".gif";
				
				var fileSize = parseInt(r.fileSize/1024);
				var attachWrap = document.getElementById('fileAttachWrap');
				attachWrap.innerHTML += "<div class='attachPart' id='"+r.filePath+"' data-down='"+r.filePath+"' data-filename='"+r.fileName+"' data-filesize='"+r.fileSize+"'>"+
				 					"<img src='"+imgUrl+"'/>&nbsp;"+
									 "<span class='fileAttachName'>"+r.fileName+"</span><div class='deleteImg' onclick='deleteAttach(this)'></div><span style='float:right'>["+fileSize+"KB]</span>"+
									 "</div>";
			}
			function attachFileFail(r){
				
				alert("fail::"+r.errorCode);
				
			}
			
			
			function wrapWindowByMask(){
				
				jQuery("#albumSelect").css("background-color","#fff").css("color","#000");
				jQuery("#cameraSelect").css("background-color","#fff").css("color","#000");
				
				var maskHeight = jQuery(document).height();
				var maskInnerHeight = window.innerHeight; 
				var maskWidth = jQuery(window).width();
				
				var middlePosition = maskHeight - maskInnerHeight + maskInnerHeight/2 - 60;
				
				jQuery('#mask').css({'width':maskWidth,'height':maskHeight});                     
				jQuery('#mask').fadeIn(200);             
				jQuery('#mask').fadeTo("fast",0.8);              
				jQuery('.window').css("left","25%").css("top",middlePosition+"px");
				jQuery('.window').show();
			}
			
			function sendCheckProcess(sendType){
				
				if(sendType == "normal"){
				
					if(${sendCheckApply} || ${sendEmailCheck} || ${sendAttachCheck} || ${sendKeywordCheck}){
						
						if(!checkSendInfo(sendType)){
							return;
						}	
						openDoubleCheckLayer();
						
					}else{
						sendMail(sendType);
					}         
				}else{
					sendMail(sendType);
				}
			}
			
			function initDoubleCheckLayer(){
				jQuery("#emailCheckInfo").hide();
				jQuery("#attachCheckInfo").hide();
				jQuery("#keywordCheckInfo").hide();
				jQuery("#checkApplyInfo").hide();
				jQuery("#checkApplyWrap").hide();
				jQuery("#toWrap").hide();
				jQuery("#ccWrap").hide();
				jQuery("#bccWrap").hide();
				jQuery("#attachWrap").hide();
				jQuery("#subjectPart").html('');
				jQuery("#toPart").html('');
				jQuery("#ccPart").html('');
				jQuery("#bccPart").html('');
				jQuery("#attachPart").html('');
			}
			
			function openDoubleCheckLayer(){
				var maskHeight = jQuery(document).height();
				var maskInnerHeight = window.innerHeight; 
				var maskWidth = jQuery(window).width();				
				var middlePosition = maskHeight - maskInnerHeight + maskInnerHeight/2 - 60;
				jQuery("html, body").animate({ scrollTop: 0 }, "fast"); 
				jQuery('#mask').css({'width':maskWidth,'height':maskHeight});                     
				jQuery('#mask').fadeIn(200);             
				jQuery('#mask').fadeTo("fast",0.8);              
				jQuery('.sendCheckWrap').css("top","0px").css("width",window.innerWidth-13+"px");
				jQuery('.sendCheckWrap').show();
				
				initDoubleCheckLayer();		
				
				if(${sendCheckApply}){
					jQuery("#checkApplyInfo").show();
					jQuery("#checkApplyWrap").show();
					
					appendSubject();
					appendEmailAddress("to");
					appendEmailAddress("cc");
					appendEmailAddress("bcc");
					appendAttach();
				}
				
				if(${sendEmailCheck}){
					var to = getEmailAddrNoPersonal("toAddrWrap");
					var cc = getEmailAddrNoPersonal("ccAddrWrap");
					var bcc = getEmailAddrNoPersonal("bccAddrWrap");
					
					if(cc != ""){
						to += ","+cc;
					}
					
					if(bcc != ""){
						to += ","+bcc;
					}
					
					var toArray = to.split(','); 
					
					var url = "/hybrid/mailSearchAddrOrgService.do?authKey="+authKey+"&emailArray="+toArray+"&addrOrgSearch=?"; 
					jQuery.ajax({
						type: 'GET',   
						url: url,    
						async: false,
						jsonpCallback: 'addrOrgSearch', 
						contentType: "application/json",
						dataType: 'jsonp',    
						success: function(json) {
							if(json.errorCode == 0){
								if(!json.isExist){
									jQuery("#emailCheckInfo").show();
								}
							}
						},    
						error: function(e) {
							alert(e.message);    
						}
					});					
				}
				
				if(${sendAttachCheck}){
					var content = jQuery("#content").val();
					var keyword = "${sendAttachData}";
					console.log(searchContentKeyword(content,keyword));
					if(searchContentKeyword(content,keyword)){
						var attachWrap = jQuery("#fileAttachWrap");
						if( attachWrap.children().length > 0){
							attachWrap.children().each(function(i){
								var test = jQuery(this).css("display");
								jQuery("#attachCheckInfo").show();
								if(test == "block"){
									jQuery("#attachCheckInfo").hide();
									return;
								}
							});
						}else{
							jQuery("#attachCheckInfo").show();
						}
					}					
				}
				
				if(${sendKeywordCheck}){
					var content = jQuery("#content").val();
					var keyword = "${sendKeywordData}";
					
					if(searchContentKeyword(content,keyword)){
						jQuery("#keywordCheckInfo").show();		
					}
				}
				
			}
			
			function searchContentKeyword(content,keyword){
				var specials = new RegExp("[.*+?|()\\[\\]{}\\\\]", "g"); // .*+?|()[]{}\
		    	keyword = keyword.replace(specials, "\\$&");
		    	var kFormat = keyword.replace(/,/g, '|');
		    	return content.match(new RegExp("(" + kFormat + ")", "gi"));
			}
			
			function appendAttach(){
				var attachWrap = jQuery("#fileAttachWrap");
				if( attachWrap.children().length > 0){
					
					var len = attachWrap.children().length;
					var attachHtml = "";	
					if(len > 0){
						jQuery("#attachWrap").show();
						attachWrap.children().each(function(i){
							var attachFileName = "";
							var attachData = "";
							var test = jQuery(this).css("display");
							
							if(test == "block"){
								attachData = jQuery(this).attr("data-down");
								attachFileName = jQuery(this).attr("data-filename");
								attachHtml += "<div data-down='"+attachData+"'><input type='checkbox' value='"+attachData+"'/>&nbsp;"+attachFileName+"</div>";
							}
						});	
						
						jQuery("#attachPart").append(attachHtml);
					}
				}
			}
			
			function appendSubject(){
				var subject = jQuery("#subject").val(); 
				jQuery("#subjectPart").append(subject);
			}
			
			function appendEmailAddress(type){
				var toWrap = jQuery("#"+type+"AddrWrap");
				var len = toWrap.children().length;
				var addrPart = "";
				var userDomain = USEREMAIL.substr(USEREMAIL.indexOf("@")+1); 
				
				if( len > 0){
					jQuery("#"+type+"Wrap").show();
					toWrap.children().each(function(i){
						var addrData = "";
						
						var wrapBlock = jQuery(this).css("display");
						if(wrapBlock == "block"){
							
							addrData = jQuery(this).attr("data-addr");
							
							addrData = escape_tag(addrData);
							
							if(addrData.indexOf(userDomain) > -1){
								addrPart += "<div><input type='checkbox' value='"+addrData+"'/>&nbsp;"+addrData+"</div>";	
							}else{
								addrPart += "<div><span style='background-color:yellow'><input type='checkbox' value='"+addrData+"'/>&nbsp;"+addrData+"</span></div>";
							}
							
														
						}
					});
					jQuery("#"+type+"Part").html(addrPart);
				}
				
			}
			
			function doubleCheckLayerSend(){
				var to = "";
				var cc = "";
				var bcc = "";
				var attach = "";
				
				var inputChkTo = jQuery("#toWrap input:checked");
				if(inputChkTo.length > 0){
					
					for(var i=0 ; i<inputChkTo.length ; i++){
	    	    		to += inputChkTo[i].value+",";	    	    		
	    	    	}
					
				}else{
					alert('<tctl:msg key="error.norecipient"/>');
					return;
				}
				
				var inputChkCc = jQuery("#ccWrap input:checked");
				if(inputChkCc.length > 0){
					
					for(var i=0 ; i<inputChkCc.length ; i++){
	    	    		cc += inputChkCc[i].value+",";	    	    		
	    	    	}
					
				}
				
				var inputChkBcc = jQuery("#bccWrap input:checked");
				if(inputChkBcc.length > 0){
					
					for(var i=0 ; i<inputChkBcc.length ; i++){
	    	    		bcc += inputChkBcc[i].value+",";	    	    		
	    	    	}
				}
				
				var inputChkAttach = jQuery("#attachWrap input:checked");
				if(inputChkAttach.length > 0){
					
					for(var i=0 ; i<inputChkAttach.length ; i++){
	    	    		attach += inputChkAttach[i].value+"\n";
	    	    	}
				}
				
				var f = document.writeForm;
				f.to.value = to;
				f.cc.value = cc;
				f.bcc.value = bcc;
				f.attachList.value = attach;
				
				
				if("${writeResultVO.sendFlag}" == "forward"){
					mobileForward();
				}
				
				jQuery("#doubleLayerBtnPart").html('<tctl:msg key="mail.send.title"/>');
				
				f.action = "/hybrid/mail/mailSend.do";
				f.submit();
				
			}
			
			function deleteAttach(e){
				
				if(!confirm('<tctl:msg key="mobile.delete.attach"/>')){
					return;
				}
				
				jQuery(e).parent().css("display","none");
				var deleteFileSize = parseInt(jQuery(e).parent().attr("data-filesize"));
				CURRENT_ATTACH_SIZE_BYTE = CURRENT_ATTACH_SIZE_BYTE - deleteFileSize;
				
			}
			
			function deleteAddrWrap(e){
				jQuery(e).parent().css("display","none");
			}
			
			jQuery(document).ready(function(){
				
				jQuery('#mask').click(function () {
					jQuery(this).hide();              
					jQuery('.window').hide();          
				});
				
				jQuery("#albumSelect").click(function(){
					jQuery(this).css("background-color","#4b97e2").css("color","#fff");
					nativeAttachCall('album');
					
				});
				
				jQuery("#cameraSelect").click(function(){
					jQuery(this).css("background-color","#4b97e2").css("color","#fff");
					nativeAttachCall('camera');
				});
				
			});
			
		</script>
	</head>
	<body>
		<div id="mask"></div> 
		<div class="window">
			<div id="albumSelect" class="top"><tctl:msg key="mobile.album"/></div>
			<div id="cameraSelect" class="bottom"><tctl:msg key="mobile.camera"/></div>				
		</div>
		<div class="sendCheckWrap">
			<div class="title"><tctl:msg key="mail.rcptcheck"/></div>
			<div class="alert" id="emailCheckInfo"><tctl:msg key="mail.sendcheck.info.001"/></div>
			<div class="alert" id="attachCheckInfo"><tctl:msg key="mail.sendcheck.info.002"/></div>
			<div class="alert" id="keywordCheckInfo"><tctl:msg key="mail.sendcheck.info.003"/></div>
			<div class="alert" id="checkApplyInfo"><tctl:msg key="mail.rcptcheck.info"/></div>
			<div id="checkApplyWrap">
				<div>
					<div class="applyTitle"><tctl:msg key="search.subject"/></div>
					<div class="applyContent" id="subjectPart"></div>
				</div>
				<div id="toWrap">
					<div class="applyTitle"><tctl:msg key="mail.to"/></div>
					<div class="applyContent" id="toPart">						
					</div>
				</div>	
				<div id="ccWrap">
					<div class="applyTitle"><tctl:msg key="mail.cc"/></div>
					<div class="applyContent" id="ccPart">						
					</div>
				</div>
				<div id="bccWrap">
					<div class="applyTitle"><tctl:msg key="mail.bcc"/></div>
					<div class="applyContent" id="bccPart">						
					</div>
				</div>
				<div id="attachWrap">
					<div class="applyTitle"><tctl:msg key="mail.attach"/></div>
					<div class="applyContent" id="attachPart">						
					</div>
				</div>
			</div>
			<div class="btn_l applyBackground" id="doubleLayerBtnPart">
					<a href="javascript:doubleCheckLayerSend();" class="btn3"><span><tctl:msg key="menu.send"/></span></a>
					<a href="javascript:doubleCheckLayerClose();" class="btn3"><span><tctl:msg key="comn.close" bundle="common"/></span></a>						
			</div>
		</div>
		<div class="wrapper">
		<%@include file="mail_top.jsp"%>
		<%@include file="/hybrid/mail/mail_body_top.jsp"%>
		<script type="text/javascript">makeTopLink();</script>
						
			<div class="container">
				<form name="writeForm" method="post" onSubmit="return sendMail('normal')">
				<input type="hidden" name="to" id="to" />
				<input type="hidden" name="cc" id="cc" />
				<input type="hidden" name="bcc" id="bcc" />
				<input type="hidden" name="sendType" id="sendType" value="${writeResultVO.writeType}"/>
				<input type="hidden" name="encode" id="encode" value="utf-8"/>				
				<input type="hidden" name="savesent" id="savesent" value="on"/>
				<input type="hidden" name="senderEmail" id="senderEmail" value="${writeResultVO.senderEmail}"/>
				<input type="hidden" name="senderName" id="senderName" value="${writeResultVO.senderName}"/>
				<input type="hidden" name="editMode" id="editMode" value="text"/>
				<input type="hidden" name="charset" id="charset" value="utf-8"/>			
				<input type="hidden" name="uid" id="uid" value="${writeResultVO.uidsValue}"/>
				<input type="hidden" name="draftMid" id="draftMid" value="${writeResultVO.draftMsgId}"/><!-- draftMid -->
				<input type="hidden" name="sendFlag" id="sendFlag" value="${writeResultVO.sendFlag}"/>
				<input type="hidden" name="folder" id="folder" value="${writeResultVO.folderName}"/>				
				<input type="hidden" name="attachList" value=""/><!-- draftMid -->
				<input type="hidden" name="attachString" id="attachString" value="${fn:escapeXml(writeResultVO.attachString)}"/>
			
				<div id="title_top_box" class="title_box">
					<div class="btn_l"><a href="javascript:goBack();" class="btn2" title="<tctl:msg key="mail.goback"/>"><span><tctl:msg key="mail.goback"/></span></a></div>
					<div class="btn_r"><a href="javascript:goFolderList();" class="btn2" title="<tctl:msg key="mail.folderlist"/>"><span><tctl:msg key="mail.folderlist"/></span></a></div>
				</div>					
				<div id="list_top_box" class="list_box">
					<div class="btn_l">
						<a href="javascript:sendCheckProcess('normal');" class="btn3"><span><tctl:msg key="menu.send"/></span></a><!-- 보내기 -->
						<a href="javascript:sendCheckProcess('draft');" class="btn3"><span><tctl:msg key="menu.draft"/></span></a><!-- 임시저장 -->
						<a href="javascript:cancel();" class="btn3"><span><tctl:msg key="mail.cancel"/></span></a><!-- 취소 -->
					</div>
					<div class="btn_r">
						<c:if test="${addrUse eq 'on'}">
						<a href="javascript:toggleAddress()" class="btn_dr3"><tctl:msg key="intro.mail.addr.title"/></a>
						</c:if>
					</div>
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
							<div id="toAddrWrap">								
							</div>
							<input type="text" name="toInput" id="toInput"/>
							
							<div id="toTempVal" style="display:none"><c:out value="${writeResultVO.to}"/></div>
						</dd>
						<div id='toAutoWrap' class='autoCompleteWrap'></div>
					</dl>
					<dl class="w1">
						<dt>
							<a href="javascript:;" onclick="toggleRcpt()" style="color:#666">
							<tctl:msg key="mail.cc"/>/<tctl:msg key="mail.bcc"/>
								<img src="/design/common/image/btn/btn_content_plus.gif" style="vertical-align:top;margin-top:2px" id="btnRcptCrtl" name="btnRcptCrtl" alt='<tctl:msg key="mail.cc"/>/<tctl:msg key="mail.bcc"/>'/>	
							</a>
						</dt>
						<dd style="text-align:right">
							<input style="width:7%" type="checkbox" name="reply_me"  id="reply_me" onclick="insertMySelfEmail(this)"/>
							<label for="reply_me" style="vertical-align:top;line-height:26px;"><tctl:msg key="mail.myself"/></label>
						</dd>	
					</dl>
					<div id="rcptBCCL"  class="w1" style="display:none">
						<dl class="w1">
							<dt><tctl:msg key="mail.cc"/></dt>
							<dd>
								<div id="ccAddrWrap">								
								</div>
								<input type="text" name="ccInput" id="ccInput"/>
																
								<div id="ccTempVal" style="display:none"><c:out value="${writeResultVO.cc}"/></div>								
							</dd>
							<div id='ccAutoWrap' class='autoCompleteWrap'></div>
						</dl>
						<dl class="w1">
							<dt><tctl:msg key="mail.bcc"/></dt>
							<dd>
								<div id="bccAddrWrap">								
								</div>
								<input type="text" name="bccInput" id="bccInput" />
								
								<div id="bccTempVal" style="display:none"><c:out value="${writeResultVO.bcc}"/></div>								
							</dd>
							<div id='bccAutoWrap' class='autoCompleteWrap'></div>
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
								<textarea style="height:15em" class="TM_write_input" name="content" id="content" placeholder="<c:if test="${writeResultVO.sendFlag eq 'forward'}"><tctl:msg key="mail.forwardmessage.parse"/></c:if><c:if test="${writeResultVO.sendFlag ne 'forward'}"><tctl:msg key="mail.forwardmessage"/></c:if>"></textarea>
								<textarea id="forwardOrgText" style="width:0px;height:0px;display:none;">${writeResultVO.htmlContent}</textarea>
								<input type="hidden" id="forwardOrgAttach" name="forwardOrgAttach" value="${fn:escapeXml(writeResultVO.attachString)}"/>
							</c:when>
							<c:otherwise>
								<textarea style="height:15em" class="TM_write_input" name="content" id="content"><c:out value="${writeResultVO.textNormalContent}"/></textarea>	
							</c:otherwise>
						</c:choose>
						</dd>
					</dl>										
				</div>
				</form>
				<div id="fileAttachWrap">		
				</div>
				<div id="list_bottom_box" class="list_box">
					<div class="btn_l">
						<a href="javascript:sendCheckProcess('normal');" class="btn3" title="<tctl:msg key="menu.send"/>"><span><tctl:msg key="menu.send"/></span></a>
						<a href="javascript:sendCheckProcess('draft');" class="btn3" title="<tctl:msg key="menu.draft"/>"><span><tctl:msg key="menu.draft"/></span></a>
						<a href="javascript:cancel();" class="btn3" title="<tctl:msg key="mail.cancel"/>"><span><tctl:msg key="mail.cancel"/></span></a>
						
					</div>
					<div>
						<div style="position:absolute;top:5px;right:10px">
							<a href="javascript:wrapWindowByMask();" class="btn3"><span><tctl:msg key="mail.attach"/></span></a>
						</div>
					</div>
				</div>			
			</div>
			
		<%@include file="/hybrid/mail/mail_body_footer.jsp"%>
		
		<%@include file="/hybrid/common/footer.jsp"%>		
		</div>
		<script>init();</script>
	</body>
</html>