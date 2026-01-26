<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
response.setHeader("cache-control","no-cache"); 
response.setHeader("expires","0"); 
response.setHeader("pragma","no-cache");
%>
<html>
<head>

<%@include file="/common/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/setting_style.css">
<%--<script type="text/javascript" src="/editor/ckeditor/ckeditor.js"></script>--%>
<script type="text/javascript" src="/editor/smarteditor/js/service/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/i18n?bundle=setting&var=signMsg&locale=<%=locale%>"></script>
<script language="javascript">
function init(){
	<%@ include file="settingCommonScript.jsp" %>
	jQuery(window).trigger("resize");	
	
	jQuery("#sign_menu").addClass("on");
	jQuery("#signApplySelect").selectbox({selectId:"signApply",selectFunc:""},
			"${signVo.signApply}",
			[{index:signMsg.conf_sign_3,value:"T"},
			 {index:signMsg.conf_sign_4,value:"F"}]);

	jQuery("#signLocationSelect").selectbox({selectId:"signLocation",selectFunc:""},
		"${signVo.signLocation}",
		[{index:signMsg.conf_sign_forward_location_bottom,value:"outside"},
		 {index:signMsg.conf_sign_forward_location_content,value:"inside"}]);
	 
	setTimeout(function(){
		jQuery.removeProcessBodyMask();
		mainSplitter.setSplitter("v",true);
		jQuery(window).trigger("resize");
	}, 100);
}


<%@ include file="settingFrameScript.jsp" %>

function newSignData() {
	
	var f = document.signForm;

	if (objCnt(f.checkSignSeq) >= 10) {
		alert(signMsg.conf_alert_sign_maxcount);
		return;
	}

	jQuery("#writeSignContent").empty();
	var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3",
		minHeight: 420,
		minWidth:770,
		btnList : [{name:signMsg.common_save,func:saveSign},{name:signMsg.conf_sign_template_button_preview,func:previewSign},{name:signMsg.conf_sign_template_button_edit,func:editSign}],	
		openFunc:function(){
			jQuery("#writeSignDialog_jqbtn a:eq(2)").hide();
			jQuery("#writeSignContent").load("/setting/writeSignData.do");},
		closeFunc:function(){
			f.signImagePath.value = "";
			f.signImageName.value = "";
			editSign();
		}
	};	
	jQuery("#writeSignDialog").jQpopup("open",popupOpt);	
}

function editSign(){
	jQuery("#writeSignPreview").hide();
	jQuery("#writeSignContent").show();
    jQuery("#writeSignDialog_jqbtn a:eq(1)").show();
	jQuery("#writeSignDialog_jqbtn a:eq(2)").hide();
}
function previewSign(){
	var inputForm = document.signDataForm;
	var signMode = inputForm.signMode.value;
    var signText = "";
	jQuery("#writeSignContent").hide();
	if (signMode == "text") {
        signText = inputForm.content_text.value;
    }
    else {
        signText = GetHtmlMessage();
    }
    var img = jQuery("#sign_image").html();
    var html ="<table border=0 cellpadding=0 cellspacing=0 >";
    html+="<tr><td>";
    if(signMode !="tmp3"){
        html+=img
    }
    html+="</td><td width=10></td><td style='vertical-align: top;font-size:9pt;'>";
    html+=signText
    html+="</td></tr></table>";
    
    
	jQuery("#writeSignPreview").empty();
	jQuery("#writeSignPreview").html(html);
	jQuery("#writeSignPreview").show();
	jQuery("#writeSignDialog_jqbtn a:eq(1)").hide();
	jQuery("#writeSignDialog_jqbtn a:eq(2)").show();
}
function modifySignData(signSeq) {

	jQuery("#writeSignContent").empty();
	var param = {"signSeq":signSeq};
	var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3",
		minHeight: 420,
		minWidth:770,
		btnList : [{name:signMsg.common_save,func:saveSign},{name:signMsg.conf_sign_template_button_preview,func:previewSign},{name:signMsg.conf_sign_template_button_edit,func:editSign}],	
		openFunc:function(){
			jQuery("#writeSignDialog_jqbtn a:eq(2)").hide();
			jQuery("#writeSignContent").load("/setting/modifySignData.do",param);	
		},
		closeFunc:function(){editSign();}
	};	
	jQuery("#writeSignDialog").jQpopup("open",popupOpt);	
}

function checkModifySignData() {
	var f = document.signForm;

	if (checkedCnt(f.checkSignSeq) == 0) {
		alert(signMsg.conf_alert_modify_noselect);
		return;
	}
	else if (checkedCnt(f.checkSignSeq) > 1) {
		alert(signMsg.conf_alert_modify_overselect);
		return;
	}

	var signSeq = "";
	if(f.checkSignSeq.length) {
		for(i = 0; i < f.checkSignSeq.length; i++) {
			if (f.checkSignSeq[i].checked) {
				signSeq = f.checkSignSeq[i].value;
				break;
			}
		}
	}
	else {
		signSeq = f.checkSignSeq.value;
	}

	modifySignData(signSeq);
}

function checkDeleteSignData() {
	var f = document.signForm;

	if (checkedCnt(f.checkSignSeq) == 0) {
		alert(signMsg.conf_alert_delete_noselect);
		return;
	}

	if (checkedCnt(f.checkSignSeq) == objCnt(f.checkSignSeq)) {
		if(confirm(signMsg.conf_alert_sign_alldelete)) {
			f.deleteType.value = "all";
		} 
		else {
			return;
		}	
	} else {
		if(!confirm(signMsg.conf_filter_3)) {
			return;
		} 
	}

	f.action = "/setting/deleteSignData.do";
	f.method = "post";
	f.submit();
}
function imageUploadDisplay(mode){
	if(mode=="tmp3"){
		  jQuery("#signImageWrap").hide();
          jQuery("#sign_image").hide();
	}else{
		  jQuery("#signImageWrap").show();
          jQuery("#sign_image").show();
	}
}
function changeMode() {
	var f = document.signDataForm;
	var value = f.signMode.value;
	imageUploadDisplay(value);
	
	if (value == "html") {
		jQuery("#mode_html").show();
		jQuery("#mode_text").hide();
		
		if(!oEditorFlag){
			editorBoxScript();
		} else {
			oEditors.getById["smarteditor"].setIR(f.content_text.value);	
		}
	}
	else if(value=="tmp1"){
		if( jQuery("#mode_text").is(':visible')){
			alert(signMsg.conf_sign_template_text_mode);
			jQuery("#signModeSelect").selectboxSetValue("text");
            return ;
        }
		
			var tempHtml= '<table border="0" cellspacing="4" style="width: 500px;">'+
			'<tbody>'+
				'<tr><td>'+signMsg.conf_userinfo_name+' '+signMsg.conf_sign_template_title+'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_company +'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_departmentname +'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_email +'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_company+signMsg.conf_userinfo_telephone +'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_fax +'</td></tr>'+
				'<tr><td>'+signMsg.conf_userinfo_basic_mobile +'</td></tr>'+
				'<tr><td>'+signMsg.conf_sign_template_homepage +'</td></tr>'+
		    '</tbody>'+
		    '</table>';
		    //CKEDITOR.instances.ckeditor.setData(tempHtml);
		    oEditors.getById["smarteditor"].setIR(tempHtml);
		}
		else if(value=="tmp2"){
			if( jQuery("#mode_text").is(':visible')){
	            alert(signMsg.conf_sign_template_text_mode);
	            jQuery("#signModeSelect").selectboxSetValue("text");
	            return ;
	        }
			 var tempHtml=
	                '<div style="padding-bottom: 0px; background-color: gray; padding-left: 0px; width: 500px; padding-right: 0px; height: 5px; padding-top: 0px">'+
	                ' &nbsp;</div>'+
	                '<div style="position: relative;left:400px;padding-bottom: 0px; background-color: blue; padding-left: 0px; width: 100px; padding-right: 0px; height: 60px; padding-top: 0px">'+
	                ' &nbsp;</div>'+
	                '<table cellpadding="0" cellspacing="3" style="border-bottom: 0px; border-left: 0px; margin: 10px 0px; width: 500px; table-layout: fixed; border-top: 0px; border-right: 0px">'+
	                '<tbody>'+
		                '<tr>'+
			                '<td colspan="2">'+signMsg.conf_userinfo_company+' '+signMsg.conf_userinfo_departmentname+'</td>'+
			                '<td>'+signMsg.conf_userinfo_name+' '+signMsg.conf_sign_template_title+'</td>'+
		                '</tr>'+
		                '<tr>'+
			                '<td>'+signMsg.conf_userinfo_company+signMsg.conf_userinfo_telephone +'</td>'+
			                '<td>'+signMsg.conf_userinfo_basic_mobile +'</td>'+
			                '<td>'+signMsg.conf_userinfo_email +'</td>'+
		                '</tr>'+
	                '</table> '+
	                '<div style="padding-bottom: 0px; background-color: blue; padding-left: 0px; width: 100px; padding-right: 0px; height: 15px; padding-top: 0px">'+
	                ' &nbsp;</div>'+
	                '<div style="padding-bottom: 0px; background-color: gray; padding-left: 0px; width: 500px; padding-right: 0px; height: 5px; padding-top: 0px">'+
	                ' &nbsp;</div>';
			 //CKEDITOR.instances.ckeditor.setData(tempHtml);
			 oEditors.getById["smarteditor"].setIR(tempHtml);
		
		}
		else if(value=="tmp3"){
			if( jQuery("#mode_text").is(':visible')){
				alert(signMsg.conf_sign_template_text_mode);
				jQuery("#signModeSelect").selectboxSetValue("text");
                return ;
            }
		
			
			var tempHtml=
				'<div style="padding-bottom: 0px; background-color: gray; padding-left: 0px; width: 500px; padding-right: 0px; height: 5px; padding-top: 0px; font-size: 0px;">'+
				' &nbsp;</div>'+
				'<div style="padding-bottom: 0px; background-color: red; padding-left: 0px; width: 500px; padding-right: 0px; height: 2px; padding-top: 0px; font-size: 0px;">'+
				' &nbsp;</div>'+
				'<table cellpadding="0" cellspacing="3" style="border-bottom: 0px; border-left: 0px; margin: 10px 0px; width: 500px; table-layout: fixed; border-top: 0px; border-right: 0px">'+
					'<tbody>'+
						'<tr><td colspan="2">'+signMsg.conf_userinfo_company+'</td></tr>'+
						'<tr><td colspan="2">'+signMsg.conf_userinfo_departmentname +''+signMsg.conf_userinfo_name+' '+signMsg.conf_sign_template_title+'</td></tr>'+
						'<tr>'+
							'<td>'+signMsg.conf_userinfo_company+signMsg.conf_userinfo_telephone +'</td>'+
							'<td>'+signMsg.conf_userinfo_basic_mobile +'</td>'+
						'</tr>'+
						'<tr><td colspan="2">'+signMsg.conf_userinfo_email +'</td></tr>'+
					'</tbody>'+
				'</table> '+
				'<div style="padding-bottom: 0px; background-color: gray; padding-left: 0px; width: 500px; padding-right: 0px; height: 5px; padding-top: 0px; font-size: 0px;">'+
				' &nbsp;</div>'+
				'<div style="padding-bottom: 0px; background-color: red; padding-left: 0px; width: 500px; padding-right: 0px; height: 2px; padding-top: 0px; font-size: 0px;">'+
				' &nbsp;</div>';
			//CKEDITOR.instances.ckeditor.setData(tempHtml);
			oEditors.getById["smarteditor"].setIR(tempHtml);
		}
		else {
			if(value=="") return;
			if( jQuery("#mode_html").is(':visible') && !confirm(signMsg.conf_sign_16)){
				jQuery("#signModeSelect").selectboxSetValue("html");
                return ;
            }
			var htmlContent = GetHtmlMessage();
			if (htmlContent == '<br />' || htmlContent == '&nbsp;') {
				htmlContent = "";
			}
			f.content_text.value = htmlContent;
			try {
	    	
			} catch(e) {}
			jQuery("#mode_html").hide();
			jQuery("#mode_text").show();
		}
}
function GetHtmlMessage() {
	/*
	 var oEditor = CKEDITOR.instances.ckeditor ;
    return oEditor.getData();
    */
    oEditors.getById["smarteditor"].exec("UPDATE_CONTENTS_FIELD", []);
	var editorData = document.getElementById("smarteditor").value;	
    return editorData;
}

function uploadImage() {
	var f = document.signDataForm;

	var signImage = f.theFile.value;
	
	if (!isImgFile(signImage)) {
		alert(signMsg.conf_sign_8);
		f.theFile.value = "";
	
		return;
	}
	
	jQuery("#workHiddenFrame").makeUploadFrame("up_hidden_frame");    
    f.target= "up_hidden_frame";	
	f.method = "post";
	f.action = "/setting/uploadSignImage.do";
	f.submit();
}

function setSignImage(url, src, name) {

	if (!url || !src || !name) {
		alert(signMsg.conf_sign_8);
		return;
	}

	jQuery("#sign_image").empty();
	var img = jQuery("<img>");
	img.css("width", "110px");
	img.css("height", "135px"); 
	img.attr("src", url);
	jQuery("#sign_image").append(img);

	var f = document.signForm;
	f.signImagePath.value = src;
	f.signImageName.value = name;

	jQuery("#workHiddenFrame").empty();
}

function saveSign() {
	var inputForm = document.signDataForm;
	var saveForm = document.signForm;
	var type = inputForm.type.value;

	if(!checkInputLength("", inputForm.signName, signMsg.conf_sign_alert_signName_empty, 1, 20)) {
		return;
	}
	if(!checkInputValidate("", inputForm.signName, "folderName")) {
		return;
	}
	var signName = inputForm.signName.value; 
	var isSameName = false;
	
	<c:forEach var="signData" items="${signVo.signDataVos}">
		if (jQuery.trim(signName) == "${fn:escapeXml(signData.signName)}") {
			if (type == "modify") {
				if(inputForm.beforeSignName.value != inputForm.signName.value) {
					isSameName = true;
				}
			} else {
				isSameName = true;
			}
		}
	</c:forEach>

	if (isSameName) {
		alert(signMsg.conf_sign_alert_signName_same);
		return;
	}

	var signMode = inputForm.signMode.value;
	var signText = "";
	var defaultFlag = "F";
	
	if (signMode == "text") {
		signText = inputForm.content_text.value;
	}
	else {
		signText = GetHtmlMessage();
	}
	
	var len = str_realLength(signText);
	if (len < 1 && len > 2048) {			
		alert(msgArgsReplace(comMsg.error_inputlength,[1,2048]));		
		return;
	}
	
	
	if (inputForm.defaultFlag.checked) {
		defaultFlag = "T";
	}

	saveForm.signName.value = jQuery.trim(signName);
	saveForm.defaultSign.value = defaultFlag;
	saveForm.signMode.value = signMode;
	saveForm.signText.value = signText;
	saveForm.signSeq.value = inputForm.signSeq.value;

	if (type == "write") {
		saveForm.action = "/setting/saveSignData.do";
	}
	else if (type == "modify") {
		saveForm.action = "/setting/updateSignData.do";
	}
	saveForm.method = "post";
	saveForm.submit();
}

function modifySign() {
	var f = document.signForm;

	if ((f.signApply.value == "T") && (objCnt(f.checkSignSeq) == 0)) {
		if (confirm(signMsg.conf_alert_sign_not_exist)) {
			f.signApply.value = "F";
		}
		else {
			return;
		}
	}

	f.action = "/setting/modifySign.do";
	f.method = "post";
	f.submit();
}

function resetSign() {
	var f = document.signForm;
	f.reset();

	jQuery("#signApplySelect").selectboxSetValue("${signVo.signApply}");
	jQuery("#signLocationSelect").selectboxSetValue("${signVo.signLocation}");
}
//var ckeditor;
var oEditors = [];
var oEditorFlag = false;
function editorBoxScript() {
	/*
    ckeditor=CKEDITOR.instances.ckeditor;
       if(ckeditor){
           CKEDITOR.remove(ckeditor);
       }
	    var config = {};
	    
	    config.width="600";
	    config.height="150";
	    config.language=(LOCALE == "jp")?"ja":LOCALE;
	    config.font_names=comMsg.editor_fontnames;
	    config.toolbar_Full =
	        [
	            { name: 'tms1',    items : ['Styles','Format','Font','FontSize'] },
	            { name: 'tms2',    items : ['Bold','Italic','Underline','Strike','Subscript','Superscript',
	                                        'NumberedList','BulletedList','-','Outdent','Indent','-','TextColor',
	                                        'BGColor','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'] }
	        ];
	
	    ckeditor = CKEDITOR.replace( 'ckeditor',config);  
	    ckeditor.on( 'instanceReady', function( e ){ 
	    	var writer = ckeditor.dataProcessor.writer;
			writer.indentationChars = '';
			writer.lineBreakChars = '';
	    	jQuery("#cke_bottom_ckeditor").hide(); 
	    }); 
	*/
	nhn.husky.EZCreator.createInIFrame({
	    oAppRef: oEditors,
	    elPlaceHolder: "smarteditor",
	    sSkinURI: "/editor/smarteditor/SmartEditor2SkinNoImage.html",
	    fCreator: "createSEditor2",
	   	htParams : {
    	    bUseVerticalResizer: false,
			fOnBeforeUnload : function(){}
		},
		fOnAppLoad : function(){
			oEditorFlag = true;
			setCookie("bHideResizeNotice",1,365*10);
		},
		initEditorBoxHeight : "0"
	});
}

onloadRedy("init()");
</script>
</head>

<body>
<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>

<div id="s_mainBody">
<form name="signForm">
<input type="hidden" name="signName">
<input type="hidden" name="defaultSign">
<input type="hidden" name="signImagePath">
<input type="hidden" name="signImageName">
<input type="hidden" name="signMode">
<input type="hidden" name="signText">
<input type="hidden" name="signSeq">
<input type="hidden" name="deleteType">
<script type="text/javascript">makePAID();</script>

		<div id="s_leftMenuContent">
			<%@include file="leftMenu.jsp"%>
		</div>

		<div id="s_contentBodyWapper" class="TM_contentBodyWapper">
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data" id="workTitle">
					<span class="TM_work_title"><tctl:msg key="location.conf" bundle="setting"/></span>
					<span class="TM_work_title_sub"> | <tctl:msg key="location.sign" bundle="setting"/></span>
				</div>
				<div class="TM_finfo_search"></div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">		
			</div>		
			
			<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>			
			<div id="s_mainContent" class="TM_mainContent">
			<div class="explanation">
				<ul>
					<li><tctl:msg key="conf.sign.1" bundle="setting"/></li>
				</ul>
			</div>
			<div id="s_contentBody" >
				<div id="s_contentMain">
					<div id="main_wrapper" class="smain_content_wrapper">
					<div>
					<div class="title_bar"><span><tctl:msg key="conf.sign.basic.setting" bundle="setting"/></span></div>
					<table class="TB_cols2" cellpadding="0" cellspacing="0">
						<tr>
							<th><span><tctl:msg key="conf.sign.2" bundle="setting"/></span></th>
							<td>								
								<div id="signApplySelect"></div>
							</td>
						</tr>
						<tr class="last">
							<th><span><tctl:msg key="conf.sign.forward.location" bundle="setting"/></span></th>
							<td>								
								<div id="signLocationSelect"></div>
							</td>
						</tr>
					</table>
					
					<table cellpadding="0" cellspacing="0" border="0" class="TM_tableList">
						<tr><td align="center" class="TM_s_btnArea">
						<a class="btn_style2" href="#" onclick="modifySign()"><span><tctl:msg key="common.save" bundle="setting"/></span></a>
						<a class="btn_style3" href="#" onclick="resetSign()"><span><tctl:msg key="common.cancel" bundle="setting"/></span></a>
						</td></tr>
					</table>
					
					<div class="title_bar"><span><tctl:msg key="conf.sign.edit" bundle="setting"/></span></div>
					<div class="sub_toolbar">
						<a href="javascript:newSignData()" class="btn_style4"><span><tctl:msg key="common.new" bundle="setting"/></span></a>
						<a href="javascript:checkModifySignData()" class="btn_style4"><span><tctl:msg key="common.24" bundle="setting"/></span></a>
						<a href="javascript:checkDeleteSignData()" class="btn_style4"><span><tctl:msg key="common.11" bundle="setting"/></span></a>
					</div>
					<table id="settingListTable" cellpadding="0" cellspacing="0">
						<colgroup span="2">
							<col width="30px"></col>
							<col></col>
						</colgroup>
						<tr>
							<th><input type="checkbox" name="allCheck" onclick="checkAll(this,signForm.checkSignSeq)"></th>
							<th><tctl:msg key="conf.sign.list.title" bundle="setting"/></th>
						</tr>
						<c:if test="${empty signVo.signDataVos}">
						<tr>
							<td colspan="2" align="center"><tctl:msg key="conf.sign.list.empty" bundle="setting"/></td>
						</tr>
						</c:if>
						<c:forEach var="signData" items="${signVo.signDataVos}">
						<tr style="<c:if test="${signData.defaultSign == 'T'}">background: #F2F7FD;</c:if>">
							<td align="center">
								<input type="checkbox" name="checkSignSeq" value="${signData.signSeq}">
							</td>
							<td class="subject">
								<a href="#" onclick="modifySignData('${signData.signSeq}')">${fn:escapeXml(signData.signName)}</a> 
								<c:if test="${signData.defaultSign == 'T'}"><span style="color:red">(<tctl:msg key="conf.sign.5" bundle="setting"/>)</span></c:if>
							</td>
						</tr>
						</c:forEach>
					</table>					
					
					</div>
					</div>		
				</div>
				<div id="s_contentSub" ></div>
			</div>
		</div>
	</div>		
</form>
</div>

<div id="writeSignDialog" title="<tctl:msg key="conf.sign.edit" bundle="setting" />">
	<div id="writeSignContent"></div>
	<div id="writeSignPreview" style="display: none; padding: 10px;"></div>
</div>

<%@include file="/common/bottom.jsp"%>

</body>
</html>