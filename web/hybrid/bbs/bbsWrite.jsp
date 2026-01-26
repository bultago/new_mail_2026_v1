<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/bbs_blue.css" />
		<script type="text/javascript">		
		pageCategory = "writeBBS";
		function saveContent() {
			
			var f = document.contentwriteForm;
			
			if(!checkInputLength("", f.subject, bbsMsg.bbs_alert_subject_empty, 2, 255)) {
				return false;
			}
			if(!checkInputValidate("", f.subject, "onlyBack")) {
				return false;
			}
			
			<c:if test="${bbsContentVo.bbsType == 'any'}">
				if(!checkInputLength("", f.creatorName, bbsMsg.bbs_alert_creatorname_empty, 2, 48)) {
					return false;
				}
				if(!checkInputValidate("", f.creatorName, "folderName")) {
					return false;
				}
			</c:if>
			
			if (jQuery("#isNotice").attr("checked")) {
				f.isNotice.value= "1";
			}
			
			f.content.value = jQuery("#contentText").val();
			

		 
		    var confirmMsg = "";
		    if ("${isModify}" == "true") {
		    	f.action = "/hybrid/bbs/bbsContentUpdate.do";
		    	confirmMsg = bbsMsg.bbs_content_modify_confirm;
		    } 
		    else {
		    	f.action = "/hybrid/bbs/bbsContentSave.do";
		    	confirmMsg = bbsMsg.bbs_content_save_confirm;
		    }

			if (!confirm(confirmMsg)) {
				return;
			}	
					  
		    f.method = "post";
			f.submit();		
		}

		function reset(){			
			var f = document.contentwriteForm;
			f.reset();
		}
		
		function goList(bbsid){
			var titleObj = jQuery("#subject");
			var contentObj = jQuery("#contentText");
			if(titleObj.val() != "" || contentObj.val() != "" ){
				if(!confirm('<tctl:msg key="confirm.BBSEscapewrite"/>')){
					return;
				}
			}
			
			document.location = "/hybrid/bbs/bbsContentList.do?bbsId="+bbsid;
			
		}
		
		function nativeGoBack(){
			
			var titleObj = jQuery("#subject");
			var contentObj = jQuery("#contentText");
			
			if(titleObj.val() != "" || contentObj.val() != "" ){
				if(!confirm('<tctl:msg key="confirm.BBSEscapewrite"/>')){
						return;
				}
			}
			
			eval("window.TMSMobile.goBack('true')");
			
		}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			<%@include file="bbsTop.jsp"%>
			<%@include file="/hybrid/bbs/bbs_body_top.jsp"%>
			<div class="hh">
				<h2>
					<a href="javascript:viewSelectMenu(this, 'bbs');" class="btn_dr">
						${bbsContentVo.bbsName}
					</a>
				</h2>
			</div>
			<div class="container">
				
				<div class="title_box">
					<div class="btn_l">
						<a class="btn2" href="javascript:goList('${bbsId}');"><span><tctl:msg key="comn.list" bundle="common"/></span></a>
						<a class="btn2" href="javascript:reset();"><span><tctl:msg key="bbs.mobile.003" bundle="bbs"/></span></a>
					</div>
					<div class="btn_r"><a class="btn2" href="javascript:saveContent()"><span><tctl:msg key="bbs.mobile.004" bundle="bbs"/></span></a></div>
				</div>
				<div class="write_wrap">
					<form name="contentwriteForm" onSubmit="return saveContent()">
					<input type="hidden" name="bbsId" value="${bbsId}">
					<input type="hidden" name="bbsType" value="${bbsContentVo.bbsType}">				
					<input type="hidden" name="parentId" value="${bbsContentVo.parentId}">					
					<input type="hidden" name="content">
					<input type="hidden" name="orderNo" value="${fn:escapeXml(orderNo)}">
					<input type="hidden" name="isReply" value="${fn:escapeXml(isReply)}">
					<input type="hidden" name="depth" value="${fn:escapeXml(depth)}">
					<input type="hidden" name="contentId" value="${bbsContentVo.contentId}">
										
					<table class="write_form">
						<tr height="10"><th></th><td></td></tr>
						<tr>
							<th><tctl:msg key="bbs.content.list.subject" bundle="bbs"/></th>
							<td><input type="text" id="subject" name="subject" class="it_full"/ value="${bbsContentVo.subject}"></td>
						</tr>
						<c:if test="${bbsContentVo.bbsType == 'any'}">
							<tr>
								<th><tctl:msg key="bbs.mobile.005" bundle="bbs"/></th>
								<td><input type="text" id="creatorName" name="creatorName" class="it_full" maxlength="30"/></td>
							</tr>
						</c:if>
						<tr>
							<th><tctl:msg key="bbs.content.list.content" bundle="bbs"/></th>
							<td>
								<textarea rows="6" cols="" class="tx_full" id="contentText" name="contentText">${fn:replace(bbsContentVo.htmlContent,"<br/>","")}</textarea>
								<c:if test="${bbsContentVo.bbsAdmin == 'true'}">
								<label class="notice"><input type="checkbox" id="isNotice" name="isNotice" <c:if test="${bbsContentVo.isNotice eq '1'}">checked</c:if>/> <tctl:msg key="bbs.content.write.isnotice" bundle="bbs"/></label>
								</c:if>
							</td>
						</tr>
						<tr height="20"><th></th><td></td></tr>
					</table>
					</form>
				</div>
				<div class="title_box title_box_bottom">
					<div class="btn_l">
						<a class="btn2" href="javascript:goList('${bbsId}');"><span><tctl:msg key="comn.list" bundle="common"/></span></a>
						<a class="btn2" href="javascript:reset();"><span><tctl:msg key="bbs.mobile.003" bundle="bbs"/></span></a>
					</div>
					<div class="btn_r"><a class="btn2" href="javascript:saveContent()"><span><tctl:msg key="bbs.mobile.004" bundle="bbs"/></span></a></div>
				</div>
			</div>

			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>