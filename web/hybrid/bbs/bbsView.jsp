<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>		
		<script type="text/javascript">			
			function init(){
				if(checkOS() == "android"){
					eval("window.TMSMobile.getAuthKey('setAuthKeySuccess','','','')");
				}else{
					window.location = "tmsmobile://getAuthKey?setAuthKeySuccess&uid='uid'&folder='folder'&part='folder'";
				}
				//authKey = "4c3972506175714d4533644e766c2b644f68615966673d3d"; // mailadm test authKey
			}
			
			function setAuthKeySuccess(r){
				authKey = r.authKey;
			}
			
			function downloadFile(part){	
				
				var url="/hybrid/bbs/bbsDownloadService.do";
				var paramArray = [];
				paramArray.push({name:"bbsId", value:"${contentVo.bbsId}"});
				paramArray.push({name:"uid", value:"${contentVo.bbsUid}"});
				paramArray.push({name:"part", value:part});
				paramArray.push({name:"authKey", value:authKey});		

				var host = location.protocol+"//"+window.location.host;
				var saveUrl = url + "?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part+"&authKey="+authKey;
				if(checkOS() == "android"){
					executeUrl(url, paramArray, true);
				}else{
					window.location = "tmsmobile://attachSave?"+host+saveUrl;
				}
				
			}
			function replyContent() {
				var f = document.contentForm;
				f.isReply.value="true";
				f.method = "post";
				f.action = "/hybrid/bbs/bbsContentWrite.do";
				f.submit();
			}			
			function modifyContent() {
				 var f = document.contentForm;
				// f.isModify.value="true";
				 f.method = "post";
				 f.action = "/hybrid/bbs/bbsContentModify.do";
				 f.submit();
			}
			function deleteContent() {

				if (!confirm(bbsMsg.bbs_content_delete_confirm)) {
					return;
				}
				
				var f = document.contentForm;
				f.action = "/hybrid/bbs/bbsContentDelete.do";
				f.method = "post";
				f.submit();
			}
			function getReplyMessage() {
				var url = "/hybrid/bbs/bbsContentViewReply.do";
				var param = jQuery.param({"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}"});
				jQuery("#reqFrame").attr("src",url + "?" + param);
			}
			function setReplyContents(contents){
				jQuery("#replylistDiv").html(contents);
			}
			function saveReplyMessage() {
				var obj = document.getElementById("replyText");
				if (!checkMaxLength(obj,'replyText',600)) {
					return;
				}
				
				var url = "/hybrid/bbs/bbsContentSaveReply.do";
				var content = jQuery("#replyText").val();
				if (content == "") {
					alert(bbsMsg.bbs_reply_content_empty);
					jQuery("#replyText").focus();
					return;
				}
				var param = jQuery.param({"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}","content":content});
				jQuery("#reqFrame").attr("src",url + "?" + param);
			}
			function saveReplyMessageResult(isSuccess){
				if (isSuccess) {
					getReplyMessage();
					jQuery("#replyText").val("");					
					return;
				}
				else {
					alert(bbsMsg.bbs_reply_save_fail);
					return;
				}
			}
			
			function deleteReplyMessage(replyId) {
				var url = "/hybrid/bbs/bbsContentDeleteReply.do";
				var param = jQuery.param({"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}","replyId":replyId});

				if (!confirm(bbsMsg.bbs_reply_delete_confirm)) {
					return;
				}

				jQuery("#reqFrame").attr("src",url + "?" + param);				
			}
			function deleteReplyMessageResult(isSuccess){
				if (isSuccess) {
					getReplyMessage();				
					return;
				}
				else {
					alert(bbsMsg.bbs_reply_delete_fail);
					return;
				}
			}
			
			function moveto_page(page) {
				var url = "/hybrid/bbs/bbsContentViewReply.do";
				var param = {"bbsId":"${contentVo.bbsId}","contentId":"${contentVo.contentId}","isNotice":"${contentVo.isNotice}", "page":page};
				jQuery("#replylistDiv").load(url, param);
			}

			function viewContent(contentId,parentId,orderNo) {
				var f = document.contentForm;
				f.contentId.value = contentId;
				f.parentId.value = parentId;
				f.orderNo.value = orderNo;				
				f.action = "/hybrid/bbs/bbsContentView.do";
				f.method = "post";
				f.submit();
			}
			
			jQuery().ready(function(){
				var writeAuth = '${contentVo.writeAuth}';
				var isCreator = '${contentVo.creator}';
				var bbsType = '${contentVo.bbsType}';
				var isAdmin = '${contentVo.bbsAdmin}';
				var bbsId = '${contentVo.bbsId}';
				
				jQuery("#deleteEditeBtnT").hide();
				jQuery("#deleteEditeBtnB").hide();
				
				if (bbsType == 'any') {
					jQuery("#replyBtnT").hide();
					jQuery("#replyBtnB").hide();
					jQuery("#deleteEditeBtnT").hide();
					jQuery("#deleteEditeBtnB").hide();
				}
				if (isAdmin == 'true'|| isCreator == 'true') {
					jQuery("#replyBtnT").show();
					jQuery("#replyBtnB").show();
					jQuery("#modifyBtnT").hide();
					jQuery("#modifyBtnB").hide();
					jQuery("#deleteEditeBtnT").show();
					jQuery("#deleteEditeBtnB").show();
				}
				
				if (isCreator == 'true') {
					jQuery("#modifyBtnT").show();
					jQuery("#modifyBtnB").show();
				}
				
				if(isAdmin != 'true' && isCreator != 'true' && bbsId == '1'){
					jQuery("#replyBtnT").hide();
					jQuery("#replyBtnB").hide();
				}
				getReplyMessage();
			});
		</script>
	</head>
	<body onload="init();">
		<div class="wrapper">		
			<%@include file="bbsTop.jsp"%>
			<%@include file="/hybrid/bbs/bbs_body_top.jsp"%>
			<div class="hh">
			<h2>
				<a href="javascript:viewSelectMenu(this, 'bbs');" class="btn_dr">
					${contentVo.bbsName}
				</a>
			</h2>
			</div>
			<div class="container">
				<form name="contentForm">
					<input type="hidden" name="bbsId" value="${contentVo.bbsId}">
					<input type="hidden" name="contentId" value="${contentVo.contentId}">
					<input type="hidden" name="parentId" value="${contentVo.parentId}">
					<input type="hidden" name="depth" value="${contentVo.depth}">
					<input type="hidden" name="orderNo" value="${contentVo.orderNo}">
					<input type="hidden" name="isReply" value="">
					<input type="hidden" name="currentPage" value="${currentPage}">
					<input type="hidden" name="keyWord" value="${keyWord}">
					<input type="hidden" name="searchType" value="${searchType}">
				</form>
				<div class="title_box">
					<div class="btn_l">	
						<c:if test="${contentVo.nextContent[0].parentId > 0}">
						<a href="javascript:viewContent('${contentVo.nextContent[0].contentId}','${contentVo.nextContent[0].parentId}','${contentVo.nextContent[0].orderNo}');" class="btn2"><span>&lt;</span></a>
						</c:if>
						<a href="/hybrid/bbs/bbsContentList.do?bbsId=${contentVo.bbsId}" class="btn2"><span><tctl:msg key="bbs.mobile.006" bundle="bbs"/></span></a>						
						<c:if test="${contentVo.prevContent[0].parentId > 0}">
						<a href="javascript:viewContent('${contentVo.prevContent[0].contentId}','${contentVo.prevContent[0].parentId}','${contentVo.prevContent[0].orderNo}');" class="btn2"><span>&gt;</span></a>	
						</c:if>
						<a href="javascript:replyContent()" class="btn2" id="replyBtnT"><span><tctl:msg key="bbs.mobile.007" bundle="bbs"/></span></a>
					</div>
					<div class="btn_r" id="deleteEditeBtnT">
						<a href="javascript:deleteContent()" id="deleteBtnT" class="btn2"><span><tctl:msg key="bbs.mobile.008" bundle="bbs"/></span></a>
						<a href="javascript:modifyContent()" id="modifyBtnT" class="btn2"><span><tctl:msg key="bbs.mobile.009" bundle="bbs"/></span></a>
					</div>
				</div>
				<div class="view_form">
				<div class="info">
					<h3>${contentVo.subject}</h3>
					<dl>
						<dt><tctl:msg key="bbs.mobile.005" bundle="bbs"/> :</dt>
						<dd>"${contentVo.creatorName}"(${contentVo.email})</dd>
						<dt><tctl:msg key="bbs.mobile.011" bundle="bbs"/> :</dt>
						<dd>${fn:substring(contentVo.createTime,0,4)}-${fn:substring(contentVo.createTime,4,6)}-${fn:substring(contentVo.createTime,6,8)} 
						${fn:substring(contentVo.createTime,8,10)}:${fn:substring(contentVo.createTime,10,12)}:${fn:substring(contentVo.createTime,12,14)}
						</dd>
						<dt><tctl:msg key="bbs.mobile.012" bundle="bbs"/> :</dt>
						<dd>${contentVo.refCnt}</dd>
					</dl>
				</div>
				<p class="cont">
									
					${contentVo.htmlContent}
					
				</p>
				<dl class="file">
					<c:if test="${empty contentVo.attachFiles}">
						<dt><tctl:msg key="mail.noattach" /></dt>
					</c:if>
					<c:if test="${!empty contentVo.attachFiles}">
						<dt>${fn:length(contentVo.attachFiles)}<tctl:msg key="mail.existattach" /></dt>
						<c:forEach var="fileData" items="${contentVo.attachFiles}" varStatus="loop">
							<c:if test="${fileData.size75 > 0 }">
							<dd>
								<div style="float:left;width:80%;">
								<span class="fileName">
									${fileData.fileName}
									<script language=javascript>
									document.write('['+ printSize(Math.round( ${fileData.size* 0.964981} ) ) +']');
						  		    </script> 
								</span>
								</div>
								<div>
								<a href="#n" class="btn_save" onclick="downloadFile('${fileData.path}')"><tctl:msg key="bbs.mobile.013" bundle="bbs"/></a>
								</div>
							</dd>		
							</c:if>					
						</c:forEach>
						<dd class="download_info">
			 				<span>* <tctl:msg key="mail.attach.download.info"/></span>
						</dd>
					</c:if>										
				</dl>
				<dl>
					<dt style="text-align:center;padding-bottom:4px;border-bottom:1px solid #DFDFDF">
						
						<div style="float:left;padding-left:8px;padding-top:5px"><tctl:msg key="bbs.mobile.010" bundle="bbs"/></div><div class="btn_r" style="float:right;padding-right:8px;padding-top:0px"><a class="btn2" onclick="saveReplyMessage()" href="javascript:;"><span><tctl:msg key="bbs.save" bundle="bbs"/></span></a></div>
						<textarea name="replyText" id="replyText" style="background:#FFFFFF;width:98%;border:1px solid #ADC6EC;color:#666666;height:37px;padding:4px 0 0 4px;"></textarea> 
					</dt>					
				</dl>				
				<div id="replylistDiv"></div>
			</div>	
					
				<div class="title_box title_box_bottom">
					<div class="btn_l">
						<c:if test="${contentVo.prevContent[0].parentId > 0}">
						<a href="javascript:viewContent('${contentVo.prevContent[0].contentId}','${contentVo.prevContent[0].parentId}','${contentVo.prevContent[0].orderNo}');" class="btn2"><span><</span></a>	
						</c:if>					
						<a href="/hybrid/bbs/bbsContentList.do?bbsId=${contentVo.bbsId}" class="btn2"><span><tctl:msg key="bbs.mobile.006" bundle="bbs"/></span></a>
						<c:if test="${contentVo.nextContent[0].parentId > 0}">
						<a href="javascript:viewContent('${contentVo.nextContent[0].contentId}','${contentVo.nextContent[0].parentId}','${contentVo.nextContent[0].orderNo}');" class="btn2"><span>></span></a>
						</c:if>
						<a href="javascript:replyContent()" class="btn2" id="replyBtnB"><span><tctl:msg key="bbs.mobile.007" bundle="bbs"/></span></a>
					</div>
					<div class="btn_r" id="deleteEditeBtnB">
						<a href="javascript:deleteContent()" id="deleteBtnB" class="btn2"><span><tctl:msg key="bbs.mobile.008" bundle="bbs"/></span></a>
						<a href="javascript:modifyContent()" id="modifyBtnB" class="btn2"><span><tctl:msg key="bbs.mobile.009" bundle="bbs"/></span></a>
					</div>
				</div>
				</div>
			</div>
			<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>
			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>