<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/bbs_style.css" />
<script type="text/javascript" src="/js/ext-lib/jquery.util.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.ui.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>
<script type="text/javascript" src="/classic/bbs/bbsReadMenuBar.js"></script>

<title>Bbs</title>
<script type="text/javascript" src="/i18n?bundle=bbs&var=bbsMsg&locale=<%=locale%>"></script>

<script language = "javascript">

function downloadFile(part)
{   
    var bbsType = "${boardVo.bbsType}";
    var url = "";
    if ("notice" == bbsType) {
        url = "/bbs/downloadNoticeAttach.do";
    } 
    else {
        url = "/bbs/downloadAttach.do";
    }
    url = url+"?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
    this.location = url;
}

function downloadAttachAll() {
    var part = jQuery("#attSaveAllBtn").attr("allpart");
    var bbsType = "${boardVo.bbsType}";
    var url = "";
    if ("notice" == bbsType) {
        url = "/bbs/downloadAllNoticeAttach.do";
    } 
    else {
        url = "/bbs/downloadAllAttach.do";
    }
    url = url+"?bbsId=${contentVo.bbsId}&uid=${contentVo.bbsUid}&part="+part;
    this.location = url;
}

function toggleAttachrInfo(setValue){   
    var infoLayer = jQuery("#attachList");
    var settingValue;

    if(setValue){
        settingValue = setValue;
    } else {
        settingValue = infoLayer.css("display");        
    }

    if(settingValue == "none"){
        infoLayer.show();
    }else {
        infoLayer.hide();
    }
    //winResize();  
}

function getMessageText(){
    return $("messageText").value;
}

var contentFrameWidth;
function resizeTextFrame(height,width){
    $("messageContentFrame").style.height=height+25+"px";
    jQuery("#messageContentFrame").css({"overflow-x":"hidden","overflow-y":"hidden"});
    contentFrameWidth = width;  
    var func = function(){
        var wrapper = jQuery("#readPopupFrame");
        var parentWrapper = jQuery("#readPopupFrame").parent();
        if(parentWrapper.width() < contentFrameWidth){
            wrapper.css("width",(contentFrameWidth+30)+ "px");
        } else {            
            wrapper.css("width",(isMsie)?"":"100%");            
        }
    };
    jQuery(window).bind("resize",func);
    func(); 
}

function closeWin(){
    var readType = "${readType}";
    if (readType == 'preview') {
        parent.closePreview();
    } else {
        if(opener){
            window.close(); 
        }else{
            parent.modalPopupForNoticeContentClose();
            parent.modalPopupForNoticeListClose();
            parent.jQpopupClear();
        }
    }
}

function printMsg(){
    window.print(); 
}

function gotoList() {
    var f = document.contentForm;
    f.action = "/bbs/listNoticeContentPopup.do";
    f.submit();
}
function setTitle(){
    var title = "";
    <c:if test="${readType eq 'popup'}">
        title = "<tctl:msg key="bbs.popup" bundle="bbs"/>";
    </c:if>
    <c:if test="${readType eq 'print'}">
        title = "<tctl:msg key="bbs.print" bundle="bbs"/>";
    </c:if>
    <c:if test="${readType eq 'preview'}">
        title = "<tctl:msg key="bbs.preview" bundle="bbs"/>";
    </c:if>
    <c:if test="${boardVo.bbsType == 'notice'}">
        title = "${fn:escapeXml(boardVo.bbsName)}";
    </c:if>
    
    if(parent){
        try{
        parent.setNoticeContentPopupTitle(title);
        }catch(e){}
    }
}
setTitle();

function init() {
    if (opener) {
        jQuery("body").addClass("popupBody");
        jQuery(".title").show();
        jQuery(".TM_mail_content").css("height","100%");
        
    }else{
        jQuery("body").css("background","none");
    }
    
    
}
</script>
</head>
<body onload="init();">

<div class="popup_style2" style="border:0px;" id="readPopupFrame">
    
    <div class="title" style="display:none;">
        <span class="titlearrow">
        <c:if test="${readType eq 'popup'}">
            <tctl:msg key="bbs.popup" bundle="bbs"/>
        </c:if>
        <c:if test="${readType eq 'print'}">
            <tctl:msg key="bbs.print" bundle="bbs"/>
        </c:if>
        <c:if test="${readType eq 'preview'}">
            <tctl:msg key="bbs.preview" bundle="bbs"/>
        </c:if>
        <c:if test="${boardVo.bbsType == 'notice'}">
            ${boardVo.bbsName}
        </c:if>
        </span>
        <a class="btn_X" href="#" onclick="closeWin()"><tctl:msg bundle="common"  key="comn.close"/></a>
    </div>
    
    
    <div class="TM_modal_content_wrapper" style="padding:0px;"> 
        <div class="TM_content_wrapper">
            <div class="TM_mail_content" style="border:0px;height:435px;overflow: auto;">
                <table cellpadding="0" cellspacing="0" border="0" class="TM_r_table">
                    <col width="50px"></col>
                    <col></col>
                    <col width="250px"></col>           
                                
                    <tr>
                    <td colspan="3" class="TM_r_subject">
                        <span>${contentVo.subject}</span>                                               
                    </td>                       
                    </tr>                   
                    <tr>
                    <td class="TM_rh_index">                                
                        <tctl:msg key="bbs.content.list.creator" bundle="bbs"/> :               
                    </td>
                    <td class='TM_rh_content' valign="top">
                        ${contentVo.creatorName}
                    </td>
                    <td class="TM_rh_index" align="right">                                                      
                        <tctl:msg key="bbs.content.list.contentId" bundle="bbs"/> :
                        ${contentVo.contentId}
                        /                       
                        <tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/> :
                        ${contentVo.refCnt}
                        (<tctl:msg key="bbs.content.list.createDate" bundle="bbs"/> :
                        ${fn:substring(contentVo.createTime,0,4)}-${fn:substring(contentVo.createTime,4,6)}-${fn:substring(contentVo.createTime,6,8)})                      
                    </td>                   
                    </tr>
                </table>

                <table cellpadding="0" cellspacing="0" border="0" class="TM_r_atable">
                    <col></col>
                    <col width="150px"></col>
                    <tr>
                    <td class='TM_ra_l'>
                    <c:if test="${empty contentVo.attachFiles}">
                        <tctl:msg key="mail.noattach" />
                    </c:if>
                    <c:if test="${!empty contentVo.attachFiles}">
                        ${fn:length(contentVo.attachFiles)} <tctl:msg key="mail.existattach" />
                    </c:if>
                    </td>
                    <td class='TM_ra_r'>
                        &nbsp;
                        <c:if test="${!empty contentVo.attachFiles}">
                        <a href="#n" class="btn_basic" id="attSaveAllBtn"  allpart="" onclick="downloadAttachAll()"><span><tctl:msg key="mail.saveall"/></span></a>
                        <a href="#n" class="btn_basic" onclick="toggleAttachrInfo()"><span><tctl:msg key="mail.viewlist"/></span></a>
                        </c:if>
                    </td>
                    </tr>
                    <c:if test="${!empty contentVo.attachFiles}">
                    <tr>
                    <td id="attachList" class='TM_ra_c' colspan="2" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                        <c:forEach var="fileData" items="${contentVo.attachFiles}" varStatus="loop">                
                            <c:forTokens var="file" items="${fileData.fileName}" delims=".">
                                <c:set var="fileType" value="${fn:toLowerCase(file)}"/>                              
                            </c:forTokens>
                            <c:choose>
                                <c:when test="${fileType=='doc' ||  fileType=='docx'||  fileType=='gif' || 
                                        fileType=='pdf' ||  fileType=='html'||  fileType=='hwp' || 
                                        fileType=='jpg' ||  fileType=='bmp' ||  fileType=='ppt' || 
                                        fileType=='pptx'||  fileType=='txt' ||  fileType=='xls' || 
                                        fileType=='xlsx'||  fileType=='zip' ||  fileType=='xml' ||
                                        fileType=='mpeg'||  fileType=='avi' ||  fileType=='htm' ||
                                        fileType=='mp3' ||  fileType=='mp4' ||  fileType=='eml'}">                                      
                                    <c:set var="attachImgName" value="ic_att_${fileType}"/>
                                </c:when>                                           
                                <c:otherwise>
                                    <c:set var="attachImgName" value="ic_att_unknown"/>                                     
                                </c:otherwise>
                            </c:choose>
                            
                            <c:if test="${fileData.size75 > 0 }">
                                <a href="#n" onclick="downloadFile('${fileData.path}')" class="rdown" title="${fileData.fileName}">                     
                                <img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileAlt}" align="absmiddle"/>&nbsp;
                                ${fileData.fileName} <span id="attachSizeL_${loop.count}"></span>
                                </a>
                                <script language=javascript>
                                $('attachSizeL_${loop.count}').innerHTML='['+ printSize(Math.round( ${fileData.size75* 0.964981} ) ) +']';                  
                                jQuery("#attSaveAllBtn").attr("allpart",jQuery("#attSaveAllBtn").attr("allpart")+"${fileData.path}_");              
                                </script>                                                       
                            </c:if>
                            <br>
                        </c:forEach>
                    </td>
                    </tr>
                    </c:if>                         
                </table>
    
                <table cellpadding="0" cellspacing="0" border="0" class="TM_r_ctable">
                    <tr><td class="TM_r_content">           
                    <textarea id="messageText" style="display:none;">
                        <c:out value="${contentVo.htmlContent}" escapeXml="true"/>
                    </textarea>         
                    <iframe frameborder="0" width="100%" height="300px" scrolling='no' src="/classic/bbs/messageContent.jsp" id="messageContentFrame"></iframe>             
                    </td></tr>
                </table>
            </div>
        </div>
    </div>

    <c:if test="${readType ne 'preview'}">
    <div class="dotLine"></div>
    <div class="btnArea">
        
        <c:if test="${readType eq 'print'}">
            <a class="btn_style3" href="javascript:;" onclick="printMsg()"><span><tctl:msg bundle="common" key="comn.print"/></span></a>
        </c:if>
        <c:if test="${boardVo.bbsType == 'notice' && viewType == 'list'}">
            <a class="btn_style3" href="#" onclick="gotoList()"><span><tctl:msg bundle="common" key="comn.list"/></span></a>
        </c:if>
        <a class="btn_style3" href="javascript:;" onclick="closeWin()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
    </div>
    </c:if>
</div>
<form name="contentForm" method="post">
    <input type="hidden" name="bbsIndex" value="${fn:escapeXml(bbsIndex)}">
    <input type="hidden" name="currentPage" value="${fn:escapeXml(currentPage)}">
    <input type="hidden" name="paid" value="<%=sessionID %>">
</form>
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>