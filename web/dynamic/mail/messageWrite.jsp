<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<script type="text/javascript">
debugInfo("WRITE_RECEIVE_DATA");
debugInfo("WRITE_DROW_START");
<c:if test="${writeBean.xecureExpressWrite}">
EXPRESS_MAX_SEND = ${writeBean.expressMaxSendCnt};
</c:if>
var MAX_ATTACH_SIZE = ${maxAttachSize};
var MAX_BIG_ATTACH_SIZE = ${maxBigAttachSize};
var BIGATTACH_EXPIRE  = "${bigAttachExpireday}";  // DAY
var BIGATTACH_DOWNCNT = "${bigAttachDownCnt}"; // DOWNLOAD COUNT
var BIGATTACH_DOWNCNT_USE = "${useBigAttachDownCnt}"; // DOWNLOAD COUNT_USE

var today = new Date(${systemTime});
var expiredate = new Date(today.getTime() + (((parseInt(BIGATTACH_EXPIRE)>0)?parseInt(BIGATTACH_EXPIRE)-1:0) * 24 * 60 * 60 * 1000));
var quotausage = ${useBigAttachQuota};
var maxbigattachquota = ${maxBigAttachQuota} * 1024 * 1024;
var bigattachquota = maxbigattachquota - quotausage;
bigattachquota = (bigattachquota < 0)?maxbigattachquota:bigattachquota;
var locale = LOCALE;

var wmode = "${wmode}";
function editorBoxScript()
{   
    var html = "";
    if (document.writeForm.sendFlag.value == "draftForwardAttached") {
        document.writeForm.sendFlag.value = "forwardAttached";
    }
    
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "smarteditor",
        sSkinURI: "/editor/smarteditor/SmartEditor2Skin.html",
        fCreator: "createSEditor2",
        htParams : {
            fOnBeforeUnload : function(){}
        }, 
        fOnAppLoad : function(){
            if(wmode == "reply" || wmode == "replyall"){
                try{
                var oSelection = oEditors.getById["smarteditor"].getEmptySelection(); 
                oSelection.selectNodeContents(oEditors.getById["smarteditor"].getWYSIWYGDocument().body); 
                oSelection.collapseToStart(); 
                oSelection.select(); 
                oEditors.getById["smarteditor"].exec("FOCUS",[]);
                }catch(e){}
            }
            setTimeout(function(){
                    jQuery(".TM_m_contentMain").css("overflow", "auto");
            }, 500);
        },
        initEditorBoxHeight : "0"
    });
}


var AttachList = [];
${writeBean.attacheString}

var popupOpt = {
        closeName:comMsg.comn_close,
        btnClass:"btn_style3"           
    };

function modalPopupImageUpload(src){
    var popOpt = clone(popupOpt);
    popOpt.btnList = null;
    var height = 170;
    var width = 390;
    popOpt.minHeight = height;
    popOpt.minWidth = width;
    popOpt.openFunc = function(){
        setTimeout(function(){
            jQuery("#editorImageUploadIframe").attr("src",src);
            jQuery("#editorImageUploadIframe").css("height",(height-20)+"px");
            jQuery("#editorImageUploadIframe").css("width",(width-10)+"px");
        },100);
        jQuery("#editorImageUpload_jqbtn").hide();
    };
    popOpt.closeFunc = function(){
        
    };
    
    jQuery("#editorImageUpload").jQpopup("open",popOpt);
}

function modalPopupImageUploadClose(){
    jQuery("#editorImageUpload").jQpopup("close");
}

</script>



<div class="TM_content_wrapper">
    <div class="TM_mail_content">   
    
        <form method="post" name="writeForm" id="writeForm" onsubmit="return false;">
        <input type="hidden" name="attlist" id="attlist">
        <input type="hidden" name="webfolderlist" id="webfolderlist">
        <input type="hidden" name="content" id="content">
        <input type="hidden" name="bigAttachContent" id="bigAttachContent">     
        <input type="hidden" name="folder" id="folder" value="${writeBean.folderName}">
        <input type="hidden" name="uid" id="uid" value="${writeBean.uidsValue}">
        <input type="hidden" name="sendFlag" id="sendFlag" value="${writeBean.sendFlag}">
        <input type="hidden" name="sendType" id="sendType" value="${writeBean.writeType}">
        <input type="hidden" name="sendViewMode" id="sendViewMode" value="normal">
        <input type="hidden" name="returl" value="/">       
                
        <input type="hidden" name="forwardingmode" id="forwardingmode" value="${writeBean.forwardingMode}">     
        <input type="hidden" name="senderName" id="senderName" value="${writeBean.senderName}">
        <input type="hidden" name="senderEmail" id="senderEmail" value="${writeBean.senderEmail}">
        <input type="hidden" name="draftMid" id="draftMid" value="${writeBean.draftMsgId}">
        <input type="hidden" name="trashMid" id="trashMid" value="${writeBean.trashMsgId}">     
        <input type="hidden" name="letterMode" id="letterMode" value="off">
        <input type="hidden" name="letterSeq" id="letterSeq">
        <input type="hidden" name="signLocation" id="signLocation" value="${signLocation}"> 
        
        <c:if test="${writeBean.rcptMode eq 'normal'|| 
                        writeBean.rcptMode eq 'noneac'}">
        <table cellpadding="0" border="0" id="mail_rcpt" class="TM_tableList">
            <col width="150px"></col>
            <col></col>
            <col width="250px"></col>           
            <tr>
            <th style="vertical-align:top;">
            <tctl:msg key="mail.to"/>
            <span style="margin-left:5px;"><input type="checkbox" name="reply_me"  id="reply_me" onclick="insertMySelf(this)"></span>
            <label for="reply_me"><span class="TM_sitem" style="cursor:pointer;"><tctl:msg key="mail.myself"/></span></label>                   
            </th>
            <td><textarea  name="to" id="to"  onfocus="focusRcpt('to')" onpropertychange="expandRcpt(this,'tempTo')" class="TM_rcpt_input">${fn:escapeXml(writeBean.to)}</textarea></td>
            <td nowrap style="vertical-align:top;">                     
                <div style="margin-left:3px;padding-top:2px;text-align: right">
                <div style='float:left;text-align:left;'><div id="lastrcptSelect"></div></div>
                <c:if test="${!writeBean.xecureExpressWrite}">
                <div style='float:left;;margin-left:3px'>
                    <a id="search_addr_link" href="javascript:;" class="btn_basic" onclick="openSearchAddr()"><span><tctl:msg key="mail.searchaddr"/></span></a>
                </div>
                </c:if>             
                </div>              
            </td>
            </tr>
            <tr>
            <th style="vertical-align:top;">
                <tctl:msg key="mail.cc"/>
                <a href="javascript:;" onclick="toggleRcpt()" class="TM_bl_slink">                      
                    <img src="/design/common/image/blank.gif" align="absmiddle" id="btnRcptCrtl" class="closeRcptBtn">  
                </a>
            </th>
            <td colspan="2">
                <textarea  name="cc" id="cc"  onfocus="focusRcpt('cc')" onpropertychange="expandRcpt(this,'tempCc')" class="TM_rcpt_input">${fn:escapeXml(writeBean.cc)}</textarea>                                                                     
            </td>
            </tr>
            <tr id="rcptBCCL" style="display:none">
            <th style="vertical-align:top;"><tctl:msg key="mail.bcc"/></th>
            <td colspan="2" >
                <textarea  name="bcc" id="bcc"  onfocus="focusRcpt('bcc')" onpropertychange="expandRcpt(this,'tempBcc')" class="TM_rcpt_input">${fn:escapeXml(writeBean.bcc)}</textarea>                                        
            </td>
            </tr>
            <tr>
            <th><tctl:msg key="mail.subject"/></th>
            <td colspan="2" >
                <input class="TM_write_input"  type="text" name="subject" id="subject" value="${fn:escapeXml(writeBean.subject)}">
            </td>
            </tr>           
        </table>
        <div id="tempTo"  class="TM_tempRcpt"></div>
        <div id="tempCc"  class="TM_tempRcpt"></div>
        <div id="tempBcc"  class="TM_tempRcpt"></div>
        <input type="hidden" name="toTempVal" id="toTempVal"/>      
        <input type="hidden" name="ccTempVal" id="ccTempVal"/>
        <input type="hidden" name="bccTempVal" id="bccTempVal"/>        
        </c:if>
        <c:if test="${writeBean.rcptMode eq 'searchaddr'}">
        <table cellpadding="0" border="0" id="mail_rcpt" class="TM_tableList">
            <col width="150px"></col>
            <col width="80px"></col>
            <col></col>
            <col width="200px"></col>           
            <tr>
            <th style="padding:0px 5px;vertical-align:top;">
            <tctl:msg key="mail.to"/>           
            <input type="checkbox" name="reply_me"  id="reply_me" onclick="insertMySelf(this)"><label for="reply_me"><span class="TM_sitem" style="cursor:pointer;"><tctl:msg key="mail.myself"/></span></label>
            </th>
            <td style="padding:3px 0px 0px 0px;"><div id="rcptCategorySelect"></div></td>
            <td>
                <input class="TM_write_input"  type="text" name="searchRcptAddr" onKeyPress="(keyEvent(event) == 13) ? searchRcptAddress() : '';" id="searchRcptAddr"/>
            </td>
            <td style="padding-left:5px;">
                <a href="javascript:;" class="btn_basic" onclick="searchRcptAddress()"><span><tctl:msg key="mail.rcptsearch"/></span></a>
                <c:if test="${!writeBean.xecureExpressWrite}">
                <a id="search_addr_link" href="javascript:;" class="btn_basic" onclick="openSearchAddr()"><span><tctl:msg key="mail.searchaddr"/></span></a>
                </c:if>
            </td>           
            </tr>
            <tr>
            <th style="padding:5px;" valign="top">
            <tctl:msg key="mail.rcptlist"/>         
            </th>
            <td colspan="2" style="padding:5px 0px 5px 0px;">
                <select multiple="multiple" style="width:100%" size="10" name="rcptSearchList" id="rcptSearchList"></select>
            </td>
            <td style="padding:5px 0px 5px 5px; vertical-align: top;">
                <div id="lastrcptSelect"></div>
                <div style="padding:5px 0px 0px 0px;"><a href="javascript:;" class="btn_basic" onclick="deleteRcptSearchList()"><span><tctl:msg key="mail.rcptdelete"/></span></a></div>                
            </td>
            </tr>           
            <tr>
            <th><tctl:msg key="mail.subject"/></th>
            <td colspan="3" >
                <input class="TM_write_input"  type="text" name="subject" id="subject" value="${fn:escapeXml(writeBean.subject)}">
            </td>
            </tr>
        </table>
        <input type="hidden" name="to" id="to"/>        
        <input type="hidden" name="cc" id="cc"/>
        <input type="hidden" name="bcc" id="bcc"/>
        <div id="toRcptValue" style="display:none">${fn:escapeXml(writeBean.to)}</div>
        <div id="ccRcptValue" style="display:none">${fn:escapeXml(writeBean.cc)}</div>
        <div id="bccRcptValue" style="display:none">${fn:escapeXml(writeBean.bcc)}</div>
        </c:if>     
        </form>
        
        <div class="TM_separate2"></div>        
        
        <div>
        <table cellpadding="0"  cellspacing="0" border="0" id="editorTable">            
            <col></col>
            <col width="380px"></col>       
            
            <tr>            
            <td class="TM_mail_toolbarLeft">
                <div style='float:left;*margin-top:3px;'>
                    <div id="editorModeSelect"></div>               
                </div>
                <div id="select_sign_area" style='float:left;margin:0px 3px 0px 3px;'>              
                <input type="checkbox" name="sign" id="signAttach" name="signAttach" onclick="checkSign()">
                <label for="signAttach"> 
                    <tctl:msg key="mail.sign"/>
                </label>
                </div>
                <div style='float:left;*margin-top:3px;'>
                <div id="signListPane">
                    <div id="signListSelect"></div>
                </div>
                </div>
                
                <div id="select_vcard_area" style='float:left;margin:0px 3px 0px 3px;'>
                <input type="checkbox" name="vcard" id="vcardAttach"  name="vcardAttach">
                <label for="vcardAttach">
                    <tctl:msg key="mail.vcard"/>
                </label> 
                </div>
                <div style='float:left;margin:0px 3px 0px 3px;width: 60px;'>
                    <input type="checkbox" name="priority"  id="priority">
                    <label for="priority">
                        <img src="/design/common/image/ic_import.gif" align="absmiddle">
                        <tctl:msg key="mail.Importance"/>
                    </label>
                </div>
                <div class="cls"></div>
            </td>
            <td align="right" class="TM_mail_toolbarRight">
                <c:if test="${writeBean.xecureExpressWrite}">
                <tctl:msg key="comn.top.mail" bundle="common"/><tctl:msg key="mail.charset"/><div id="encharsetSelect" style="text-align: left;float: right"></div>
                </c:if>
                <c:if test="${!writeBean.xecureExpressWrite}">
                    <table width="100%" cellpadding="0"  cellspacing="0" border="0">
                    <tr>
                    <td width="100%"><tctl:msg key="comn.top.mail" bundle="common"/><tctl:msg key="mail.charset"/></td>
                    <td style="white-space: nowrap;text-align: left" nowrap><div id="encharsetSelect"></div></td>                   
                    <c:if test="${!writeBean.xecureExpressWrite}">
                    <td style="padding-left:5px;white-space: nowrap" id="htmlMoreFuncPane" nowrap>                                                  
                    <a href="javascript:;" onclick="toggleLetterPane()">
                    <img src="/design/common/image/blank.gif" align="absmiddle" id="letterBtn" class="closeRcptBtn">
                    <tctl:msg key="mail.letterpaper"/></a>
                                        
                    <a href="javascript:;" onclick="toggleTemplatePane()" id="docTemplateBtn">
                    <img src="/design/common/image/blank.gif" align="absmiddle" id="templateBtn" class="closeRcptBtn">
                    <tctl:msg bundle="common" key="doctemplate.title"/></a>
                    </td>                   
                    </c:if>                 
                    </tr>
                    <tr><td>
                        <div style='position: absolute;right:40px;'><div id="letterPane" style="width: 180px;"></div></div>
                        <div style='position: absolute;right:20px;'><div id="templatePane" style="width: 180px;"></div></div>
                    </td></tr>
                </table>
                </c:if>
            </td></tr>
            <tr><td  colspan="2"  id="modeTextWrapper">
                <div id="modeText" style="border:1px solid #d6d6d6;" >
                <form name="contentForm">
                    <table width="100%" cellpadding="0"  cellspacing="0" border="0">
                    <tr><td style="padding-left:5px">
                    <textarea name="contentTemp" id="contentTemp" style="display: none"><c:if test="${writeBean.sendFlag eq 'forwardAttached'}"><tctl:msg key="mail.forwardmessage"/></c:if>${writeBean.textContent}</textarea>
                    <iframe frameborder="0" width="100%" height="360px" scrolling='no' src="about:blank" id="textContentFrame" name="textContentFrame"></iframe>                    
                    <textarea name="content_html" id="content_html" rows="30" style="display:none">${writeBean.htmlContent}</textarea>
                    </td></tr>
                    </table>                                
                </form>
                </div>
            </td></tr>
            <tr><td colspan="2"  id="modeHtmlWrapper" valign="top" style="height:400px;">               
                <div id="modeHtml" >
                <%--<textarea cols="100" id="ckeditor" name="ckeditor" rows="10">--%>
                <textarea name="smarteditor" id="smarteditor" rows="10" cols="100" style="width: 100%; height: auto; display:none; ">
                <c:if test="${writeBean.sendFlag eq 'forwardAttached'}">
                    <br><br><div style='font-weight:bold;text-align:center'><tctl:msg key="mail.forwardmessage"/></div><br><br>
                </c:if>
                <c:if test="${writeBean.sendFlag ne 'forwardAttached'}">
                    ${writeBean.htmlContent}
                </c:if>
                </textarea>
                    <script type="text/javascript">
                        editorBoxScript();
                    </script>
                </div>
            </td></tr>          
        </table>
        </div>
                
        <div class="TM_separate2"></div>
        
        <div>
            <div id="reservSecureTabBySkin3"></div>
            <div class="TM_separate2"></div>
            <form name="massRcptUploadForm" method="post"                
                    enctype="multipart/form-data">
                    <input type="hidden" name="attachtype">
                    <input type="hidden" name="uploadType">                 
                    <input type="hidden" name="massFilePath" id="massFilePath">                     
            <c:if test="${!writeBean.xecureExpressWrite && massRcptConfirm == 'true'}">
            <div class="TM_mail_toolbar">
                <strong><tctl:msg key="mail.massrcpt"/></strong>
                <input type="file" class="TM_inputFile"  name="massRcptFile" id="massRcptFile" onchange="uploadMassUserFile()">
                <span id="massRcptFileInfo" style="display:none">
                    <span id="massFileName"></span>(<span id="massFileSize"></span>)                                    
                    <a href="javascript:;" onclick="deleteMassRcptFile()" class="btn_del" ><span><tctl:msg bundle="common" key="comn.del"/></span></a>
                </span>             
            </div>
            <div id="upload_rcpt_Frame"></div>
            <div class="TM_separate2"></div>                        
            </c:if>
            </form>
                                
            <form name="uploadForm" method="post"                
                    enctype="multipart/form-data">
                <input type="hidden" name=upldtype value='upld'>
                <input type="hidden" name="writeFile" value="true">
                <input type="hidden" name="attfile" value="true">
                <input type="hidden" name="attsize" value="0">
                <input type="hidden" name="uploadType">
                <input type="hidden" name="maxAttachFileSize">      
                
            <div class="TM_mail_toolbar">
                <table cellpadding="0" cellspacing="0" border="0">                  
                    <tr>
                    <td style="padding-right:5px;" nowrap><strong><tctl:msg key="mail.attach"/></strong></td>                   
                    <td style="width:100%;padding-left:10px;" nowrap>
                    <div style="float:left;padding:0px 5px 0px 0px;" id="fileuploader_add_btn_area"><a href="javascript:;" class="btn_add" id="fileuploader_add_btn"><span><tctl:msg key="mail.attach.add"/></span></a></div>
                    <div style="float:left;padding:0px 5px 0px 0px;"><a href="#n" onclick="deletefile()" class="btn_del"><span><tctl:msg key="mail.attach.delete"/></span></a></div>
                    <div style="float:left;padding:0px 5px 0px 0px;"><a href="#n" onclick="viewBigAttachManager()"  id="bigAttachMgn" class="btn_basic"><span><tctl:msg key="mail.attach.manager"/></span></a></div>
                    <div style="float:left;white-space: nowrap;"><a href="javascript:;" onclick="openWebfolder()" id="att_btn_webfolder" class="btn_add"><span><tctl:msg key="mail.attach.webfolder"/></span></a></div>
                    </td>
                    </tr>
                    <tr>
                    <td colspan="2">
                    <div id="html5FileUploader">
                        <div id="html5FileUploaderArea"></div>
                        <script>
                        var html5FileUploaderParams = {"drawArea":"html5FileUploaderArea", "callLocation":"mail-ajax", "locale":LOCALE};                       
                        var html5fileUploaderObj = new html5fileUploader(html5FileUploaderParams);
                        html5fileUploaderObj.createFileUploader();    
                        </script>                           
                    </div> 
					<div id="simpleFileInit" class="TM_mail_toolbar" style="display:none;">                                                             
		                <input type="file" size="50" onchange="uploadSimpleFile()" id="simpleFile" name="theFile" class="TM_attFile">
		            </div>                                      
                    </td>
                    </tr>
                    
                </table>    
            </div>   
            
            <div class="TM_attach_area">        
                <div id="att_simple_area"><div id="basicUploadAttachList"></div></div>
                <div id="bigattachMessageSpan"  style='padding:5px;'></div>             
            </div>
            <div id="att_simple_quota_info" class="TM_attach_quota"></div>
            <div id="upload_att_Frame" ></div>
            </form>             
        </div>
    
    </div>
</div>

<form name="previewForm" id="previewForm">
    <input type="hidden" name="to"/>
    <input type="hidden" name="cc"/>
    <input type="hidden" name="bcc"/>
    <input type="hidden" name="subject"/>
    <input type="hidden" name="senderName"/>
    <input type="hidden" name="senderEmail"/>
    <input type="hidden" name="attachsign"/>
    <input type="hidden" name="signseq"/>
    <input type="hidden" name="content"/>
    <input type="hidden" name="wmode"/>
</form>

<script type="text/javascript">
var reserveTimeSet = ${writeBean.currentDateInfo};  
var letterControl,docTemplateControl;

if((!MENU_STATUS.addr || MENU_STATUS.addr != "on") && (!MENU_STATUS.org && MENU_STATUS.org != "on")) {
    jQuery("#search_addr_link").hide();
}

if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
    jQuery("#select_sign_area").hide();
    jQuery("#select_vcard_area").hide();
}
debugInfo("WRITE_DROW_END");

function openSearchAddr(){
    var windowHeight = 500;
    var windowWidth = 1024;
    modalPopupForSearchAddr('/dynamic/addr/addrPopup.do?actionType=write', windowWidth, windowHeight);
}

function setSearchAddrPopupTitle(title){
    jQuery("#searchAddr_pht").html(title);
}

function modalPopupForSearchAddr(src, width, height){
    var popOpt = clone(popupOpt);
    popOpt.btnList = null;
    popOpt.minHeight = height;
    popOpt.minWidth = width;
    jQuery("#searchAddr").css({"height":height+"px","width":width+"px"});
    popOpt.openFunc = function(){
        setTimeout(function(){
            jQuery("#searchAddrIframe").attr("src",src);
            jQuery("#searchAddrIframe").css("height",(height)+"px");
            jQuery("#searchAddrIframe").css("width",(width-10)+"px");
        },300);
        jQuery("#searchAddr_jqbtn").hide();
    };
    popOpt.beforeCloseFunc = function(){
        jQuery("#searchAddrIframe").attr("src","/common/zero.html");
        jQpopupClear();
    };
    
    jQuery("#searchAddr").jQpopup("open",popOpt);
}

function modalPopupForSearchAddrClose(){
    jQuery("#searchAddr").jQpopup("close");
}

function jQpopupClear(){
    setTimeout(function(){
        jQuery(":text:first").focus();
    },100);
    
}

function toggleRcpt(setVal){
    var btnImg = jQuery("#btnRcptCrtl");
    var bcc = jQuery("#rcptBCCL");
    var val = bcc.css("display");
    var isShow = (val == "none");
    if(setVal){
        isShow = setVal;
    }
    
    if(isShow){
        btnImg.removeClass("closeRcptBtn");
        btnImg.addClass("openRcptBtn");
        expandRcpt($('bcc'),'tempTo');
        bcc.show();
    } else {
        btnImg.removeClass("openRcptBtn");
        btnImg.addClass("closeRcptBtn");                
        bcc.hide();
        expandRcpt($('bcc'),'tempTo');
    }   
}

function viewBigAttachManager(){    
    var popOpt = clone(popupOpt);
    popOpt.minHeight = 300;
    popOpt.minWidth = 650;
    
    jQuery("#bigAttachManager").jQpopup("open",popOpt);
    
    ActionNotMaskLoader.loadAction("bigAttachContents", "/dynamic/mail/listBigAttach.do?dummy="+makeRandom(),"");
}



function openWebfolder() {
    var url= "/webfolder/webfolderPopup.do";    
    modalPopupForWebfolder(url, 860, 495);
}

function autocompleterHide(id){
    var select = jQuery("#"+id+"_actb");
    if(select)select.hide();
}

function escapeAutocomplete(){
    autocompleterHide("to");
    autocompleterHide("cc");
    autocompleterHide("bcc");       
}

function loadMessageWritePage(writeSettingInfo){    
    
    isLetter = false;
    isDocTemplate = false;
    
    if(writeSettingInfo.rcptMode == "searchaddr"){
        isRcptModeNormal = false;
    } else if(writeSettingInfo.rcptMode == "noneac"){
        isRcptModeNoneAC = true;
    }
    if(!isRcptModeNormal){      
        jQuery("#rcptCategorySelect").selectbox({selectId:"rcptCategory",selectFunc:""},
                "",[{index:mailMsg.mail_rcptto,value:"to"},
                    {index:mailMsg.mail_cc,value:"cc"},
                    {index:mailMsg.mail_bcc,value:"bcc"}]);

        setRcptSearchList("to",jQuery("#toRcptValue").text());
        setRcptSearchList("cc",jQuery("#ccRcptValue").text());
        setRcptSearchList("bcc",jQuery("#bccRcptValue").text());
    }   
    jQuery("#siSearchBox").hide();
    
    loadWriteToolBar(writeSettingInfo);
    menuBar.setPageNavi("d");
    secureMailModule = writeSettingInfo.securemailModule;
    
    if(!IS_XPRESS_MAIL_WRITE){
        setWorkTitle("<span class='TM_work_title'>"+mailMsg.mail_write+"</span>");

        var simpleQuota = "[<b>"+mailMsg.mail_attach_normal+"</b>"+
                            "(<span id=basic_normal_size>0B</span> / ${maxAttachSize}MB)"+
                            "<span id='basic_bigattach_size'>&nbsp;&nbsp;&nbsp; <b>"+mailMsg.mail_attach_bigattach+"</b>"+
                                "(<span id=basic_huge_size>0B</span> / <span id=basic_huge_quota>0B</span>)</span>]";
        $("att_simple_quota_info").innerHTML=simpleQuota;       
        
        menuBar.toggleSideItem(true);
        isOcxUploadDownModule = false;
        isOcxUpload = false;

        chgAttachMod("simple");
        
        setTimeout(function(){
            checkUploadfile();
        },1000);

        jQuery("#resevDate").data("maxReservedDay",writeSettingInfo.maxReservedDay)
    } else {
        userDN = "${writeBean.userDN}";
        secureMailMakeMode = "${writeBean.secureMakeMode}";
        setWorkTitle("<span class='TM_work_title'>"+mailMsg.mail_secure_write+"</span>");
        attachFileListLength = 1;
        loadExpressE("expressOcx");
    }

    if(writeSettingInfo.signAttach == "on" && (writeSettingInfo.writeType != "rewrite")){
        $("signAttach").checked = true;
    }

    if(writeSettingInfo.vcardAttach == "on" && (writeSettingInfo.writeType != "rewrite")){
        $("vcardAttach").checked = true;
    }   

    var charset = writeSettingInfo.encoding;

    if(!charset){charset = "UTF-8"} 
    jQuery("#encharsetSelect").selectbox({selectId:"encharset",selectFunc:"",width:140},
                                        charset,[
                                        {index:mailMsg.mail_charset_utf8,value:"UTF-8"},
                                        {index:mailMsg.mail_charset_euckr,value:"EUC-KR"},
                                        {index:mailMsg.mail_charset_usascii,value:"US-ASCII"},
                                        {index:mailMsg.mail_charset_jp2022,value:"ISO-2022-JP"},
                                        {index:mailMsg.mail_charset_gb2312,value:"GB2312"},
                                        {index:mailMsg.mail_charset_big5,value:"BIG5"}
                                        ]);

    jQuery("#editorModeSelect").selectbox({selectId:"editorMode",selectFunc:chgEditorMod},
            "",[{index:"HTML",value:"html"},{index:"TEXT",value:"text"}]);  

    checkSign();            
        
    var editorMode = writeSettingInfo.editorMode;
    if(editorMode.toLowerCase() == "html"){     
        chgEditorMod("html",true);
    } else if(editorMode.toLowerCase() == "text"){
        chgEditorMod("text",true);
    }
    
    $("m_contentMain").scrollTop = 0;

    if(isRcptModeNormal){
        if($("bcc").value != "")toggleRcpt();
    }
    if(isRcptModeNormal && !isRcptModeNoneAC){
        var qs = (IS_XPRESS_MAIL_WRITE)?"?notOrg=T":"";
        jQuery("#to").autocomplete("/mail/searchEmailByName.do"+qs);    
        jQuery("#cc").autocomplete("/mail/searchEmailByName.do"+qs);    
        jQuery("#bcc").autocomplete("/mail/searchEmailByName.do"+qs);
    }
    setTimeout(function(){
        try{
        	if(isRcptModeNormal){
            	new emulEvent('to');
	            expandRcpt($('to'),'tempTo');
    	        new emulEvent('cc');
        	    expandRcpt($('cc'),'tempTo');
            	new emulEvent('bcc');
	            expandRcpt($('bcc'),'tempTo');
    	    }       
        	if(wmode != "reply" && wmode != "replyall"){                                
            	if(writeSettingInfo.rcptMode == "normal" ||
                	    writeSettingInfo.rcptMode == "noneac"){
	                if($("to")){
	                    $("to").focus();
                    }
    	        } else {
        	        if($("searchRcptAddr")){
	                    $("searchRcptAddr").focus();
                    }
    	        }
        	}
        }catch(e){}
    },1000);    
    getSignInfo();
    getLastRcptList();
    debugInfo("WRITE_FUNC_END");
    debugInfo("WRITE_END");
    if(PDEBUGLOGGING){
        loggingData("MAIL_WRITE");
    }
    if(isMsie6)jQuery(window).trigger("resize");
    jQuery("#fileAddBtn").css({"cursor":"pointer","width":"100px","height":"20px","background":"url(/design/common/image/btn/bg_attach_file_btn_"+LOCALE+".gif) no-repeat left top"});
}

var isRcptModeNormal = true;
var isRcptModeNoneAC = false;

jQuery().ready(function(){
    debugInfo("WRITE_FUNC_START");
    letterControl = new LetterControl("letterPane");
    docTemplateControl = new DocTemplateControl("templatePane");

    var doctemplateUse = ${docTemplateApply};
    if(!doctemplateUse)jQuery("#docTemplateBtn").hide();
    
    jQuery("#textContentFrame").attr("src","/dynamic/mail/textContent.jsp?locale="+LOCALE);
    var basicControlOpt = {
            controlType:"power",
            btnId:"basicUploadControl",
            btnCid:"basicUploadBtn",
            formName:"theFile",
            param:{"uploadType":"flash"},
            url:"/file/uploadAttachFile.do",
            maxFileSize:bigattachquota,
            fileType:"*.*",
            locale:LOCALE,
            btnWidth:100,
            btnHeight:20,           
            debug:false,
            autoStart:false,
            handler:basicUploadListeners,
            listId:"basicUploadAttachList",
            startUploadFunc:startUploadAttach};
    
    basicAttachUploadControl = new UploadSimpleBasicControl(basicControlOpt);

    var bigAttachInfoStr; 
    if(BIGATTACH_DOWNCNT_USE == "on"){
        bigAttachInfoStr = msgArgsReplace(mailMsg.bigattach_15,[BIGATTACH_EXPIRE,BIGATTACH_DOWNCNT]);
    } else {
        bigAttachInfoStr = msgArgsReplace(mailMsg.bigattach_07,[BIGATTACH_EXPIRE]);
    }
    jQuery("#bigattachMessageSpan").html(bigAttachInfoStr);
        
    IS_XPRESS_MAIL_WRITE = ${writeBean.xecureExpressWrite};
    var writeSettingInfo = ${writeBean.writeSettingInfo};
    isSendInfoCheckUse = ${sendCheckApply};
    
    loadMessageWritePage(writeSettingInfo);
    
    if(isMsie && LOCALE =="ko"){
        jQuery("#subject").css("ime-mode","active");
    }
    isAutoSaveStart = false;
    isAutoSave = false;
    isSendWork = false; 
    
    if(!IS_XPRESS_MAIL_WRITE){      
        setTimeout(function(){
            runAutoSaveProcess();
        },500);
    }
});
</script>

