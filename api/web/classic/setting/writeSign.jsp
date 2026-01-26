<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<div style="height:400px;">	
<form name="signDataForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="type" value="${type}">
<input type="hidden" name="signSeq" value="${signDataVo.signSeq}">
<input type="hidden" id="beforeSignName" name="beforeSignName" value="${signDataVo.signName}">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;border-right:1px solid #E6E6E6;">
		<tr>
		<th class="lbout" style="width:140px;"><tctl:msg key="conf.sign.subject" bundle="setting"/></th>			
		<td>
			<input type="text" id="signName" name="signName" class="IP200" style="width:250px" value="${signDataVo.signName}" maxlength="20">
			<input type="checkbox" name="defaultFlag" <c:if test="${signDataVo.defaultSign == 'T'}">checked</c:if>> <tctl:msg key="conf.sign.basic.setting" bundle="setting"/>
		</td>
		</tr>
		<tr id="signImageWrap">
		<th class="lbout" style="width:140px;"><tctl:msg key="conf.sign.image.upload" bundle="setting"/></th>
		<td>
			<input type="file" name="theFile" onChange="uploadImage()" style="height:20px;" />					
		</td>
		</tr>		
		<tr>
		<th class="lbout" style="width:140px;"><tctl:msg key="conf.sign.editor.type" bundle="setting"/></th>
		<td>						
			<div id="signModeSelect"></div>			
		</td>
		</tr>
		<tr>
		<td style="padding:10px 10px 10px 15px" align="center" valign="top">
			<div id="sign_image" style="border:1px solid #CDCDCD;width:110px;height:135px">
				<c:if test="${!empty signDataVo.signImageUrl}">
					<img src="${signDataVo.signImageUrl}" style="width:110px;height:135px">
				</c:if>
			</div>
		</td>						
		<td style="padding:10px 0px 10px 0px">
			<!--  Text Box -->
		    <div id="mode_text" style="display:<c:if test="${signDataVo.signMode == 'text'}">show</c:if><c:if test="${signDataVo.signMode != 'text'}">none</c:if>;">
			<!-- form name="contentForm" -->
			    <textarea name="content_text" style="width:405px;height:130px;font-size:small;">${signDataVo.signText}</textarea>
				<textarea name="content_html" rows="8" style="display:none">${signDataVo.signText}</textarea>
			<!-- /form -->
		    </div>
		    <!--/Text Box -->
		    <!-- Editor Box -->
		    <div id="mode_html" style="display:<c:if test="${signDataVo.signMode == 'text'}">none</c:if><c:if test="${signDataVo.signMode != 'text'}">show;</c:if>">
		    	<textarea name="smarteditor" id="smarteditor" rows="10" cols="100" style="width: 600px; height: 150px; ">${signDataVo.signText}</textarea>
				<c:if test="${signDataVo.signMode != 'text'}"><script type="text/javascript">editorBoxScript();</script></c:if>
			<%--
				<textarea cols="100" id="ckeditor" name="ckeditor" rows="10">${signDataVo.signText}</textarea>
                            <script type="text/javascript">editorBoxScript();</script>
			--%>
		    </div>
		    <!-- /Editor Box -->
		</td>
		</tr>
	</table>
</form>
</div>
<div id="workHiddenFrame"></div>
<script>

jQuery("#signModeSelect").selectbox({selectId:"signMode",selectFunc:changeMode},
		"${signDataVo.signMode}",
		[{index:"HTML",value:"html"},
		 {index:"TEXT",value:"text"},
		 {index:"--------",value:""},
		 {index:signMsg.conf_sign_template_1,value:"tmp1"},
		 {index:signMsg.conf_sign_template_2,value:"tmp2"},
		 {index:signMsg.conf_sign_template_3,value:"tmp3"},
		 {index:"--------",value:""}
		 ]);	
		 
</script>