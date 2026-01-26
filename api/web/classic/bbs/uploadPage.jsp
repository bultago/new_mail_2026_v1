<script language = "javascript">
    var locale = "ko";

    function chgAttachMod(type) {       
        var simpleCrtBtn = jQuery("#att_btn_simple");
        var simpleIncFrame = jQuery("#att_simple_area");
        var simpleQuotaInfo = jQuery("#att_simple_quota_info");
        var simpleModeBtn = jQuery("#crtBtnSimple");
        var simpleFileObj = jQuery("#simpleFileInit"); //201802 HTML5 Uploader

        try {
            // POWER UPLOAD or FILE UPLOAD
            simpleModeBtn.hide();
            simpleCrtBtn.show();
            simpleIncFrame.show();
            simpleQuotaInfo.show();     
            jQuery("#crtBtnPower").hide();

            //201802 
            var html5FileApiSupport = {
                filereader : typeof FileReader != 'undefined',
                dnd : 'draggable' in document.createElement('span'),
                formdata : !!window.FormData,
                progress : "upload" in new XMLHttpRequest
            };

			var isLowerVersionIE = false;
	        if(/msie/.test(navigator.userAgent.toLowerCase()) && /trident/.test(navigator.userAgent.toLowerCase())){
	            var canvas = document.createElement('canvas'); 
	            if( !('getContext' in canvas) ) {
	                isLowerVersionIE = true;
	            }
	        } 
	        
	        if(!isLowerVersionIE){      
	            "filereader formdata progress".split(' ').forEach(function(api) {
	                if (html5FileApiSupport[api] === false) {
	                    document.getElementById("html5FileUploader").className = "hidden";
	                    simpleFileObj.show();
	                    simpleCrtBtn.hide();
	                } else {
	                    simpleFileObj.hide();
	                    simpleCrtBtn.hide();
	                }
	            });
	        } else {
	            document.getElementById("html5FileUploader").className = "hidden";
	            simpleFileObj.show();
	            simpleCrtBtn.hide();
	        }

            basic_init();
            
        } catch (e) {
        }

    }

    function uploadfile() {

        var f = document.uploadForm;
        if (f.theFile.value == "") {
            return;
        }
        jQuery("#uploadHiddenFrame").makeUploadFrame("upload_hidden_frame");
        f.target = "upload_hidden_frame";

        var attSize = f.attlist.length;

        if (attSize >= MAX_ATTACH_COUNT) {
            alert(msgArgsReplace(bbsMsg.attach_count_maxalert, [ MAX_ATTACH_COUNT ]));
            return;
        }

        f.uploadType.value = "basic";
        f.maxAttachFileSize.value = MAX_ATTACH_SIZE;
        f.action = "/file/uploadAttachFile.do";
        f.method = "post";
        f.submit();

        var fileUploadHtml = "<input type='file' name='theFile'  onChange='uploadfile()'";
        fileUploadHtml = fileUploadHtml + ((jQuery.browser.safari) ? "/>" : " class='TM_attFile' />");
        $("simpleFileInit").innerHTML = fileUploadHtml;
    }

    function deletefile() {
        var checkObjIds = basicAttachUploadControl.getCheckAttachFileIds();
        if (checkObjIds.length > 0) {
            for (var i = 0; i < checkObjIds.length; i++) {
                basicAttachUploadControl.deleteAttachList(checkObjIds[i]);
            }
            var quota = basicAttachUploadControl.getAttachQuotaInfo();
            updateAttachQuota(quota.normalUse);
            $("basicAttachChkAll").checked = false;
        }
    }

    function addlist(n, s, p, uid) {
        var f = document.uploadForm;
        var select = f.attlist;
        var text = n + " (" + s + " bytes)";
        var _value = p + "\t" + n + "\t" + s + "\t" + uid + "\t";

        var upldtype = p.substring((p.length - 1), p.length);

        var attsize = parseInt(s);

        // HTML UPLOAD
        attsize = parseInt(f.attsize.value) + parseInt(s);

        if (attsize > (MAX_ATTACH_SIZE * 1024 * 1024)) {
            alert(MAX_ATTACH_SIZE + "MB " + mailMsg.ocx_upalert_size);
            return;
        }
        f.attsize.value = attsize;

        var file = {
            "path" : p,
            "size" : s,
            "name" : n,
            "uid" : uid,
            "type" : "normal",
            "notChage" : true,
            "id" : "SWFUpload_0_a" + makeRandom()
        };
        basicAttachUploadControl.addAttachList(file);
        updateAttachQuota(attsize);
        
    }

    function getAttachString() {
        var pstr = "";      
        var uploadedFile = basicAttachUploadControl.getFileList("normal");
        for (var i = 0; i < uploadedFile.length; i++) {
            pstr += uploadedFile[i].path + "\t" + uploadedFile[i].name + "\t" + uploadedFile[i].size + "\t" + uploadedFile[i].uid + "\n";
        }

        return pstr;
    }

    function writeInit(mode) {}
</script>

<form name="uploadForm" method="post" enctype="multipart/form-data">
    <input type="hidden" name=upldtype value='upld'>
    <input type="hidden" name="writeFile" value="true">
    <input type="hidden" name="attfile" value="true">
    <input type="hidden" name="attsize" value="0">
    <input type="hidden" name="userId" value="jhlee@terracetech.com">
    <input type="hidden" name="path">
    <input type="hidden" name="sroot">
    <input type="hidden" name="nodeNum">
    <input type="hidden" name="id">
    <input type="hidden" name="type">
    <input type="hidden" name="attlists" value="">
    <input type="hidden" name="attachType">
    <input type="hidden" name="uploadType">
    <input type="hidden" name="maxAttachFileSize">
   
<div class="TM_bbs_toolbar">
    <table cellpadding="0"  cellspacing="0" border="0">
        <tr>
        <td style="padding-right:5px;" nowrap><strong><tctl:msg key="mail.attach"/></strong></td>
        <td width="100%" style="padding-left:10px;" nowrap>             
		<div style="float:left;padding:0px 5px 0px 0px;" id="fileuploader_add_btn_area"><a href="javascript:;" class="btn_add" id="fileuploader_add_btn"><span><tctl:msg key="mail.attach.add"/></span></a></div>
        <div style="float:left;padding:0px 5px 0px 0px;white-space: nowrap"><a href="#n" onclick="deletefile()" class="btn_del"><span><tctl:msg key="mail.attach.delete"/></span></a></div>
        </td>       
        </tr>
        <tr>
        <td colspan="2">
        <div id="html5FileUploader">
            <div id="html5FileUploaderArea"></div>
            <script>
            var html5FileUploaderParams = {"drawArea":"html5FileUploaderArea", "callLocation":"bbs", "locale":LOCALE};                               
            var html5fileUploaderObj = new html5fileUploader(html5FileUploaderParams);
            html5fileUploaderObj.createFileUploader();    
            </script>                           
        </div>
		<div id="simpleFileInit" class="TM_bbs_toolbar" style="display:none;">                                                             
		    <input type="file" size="50" onchange="uploadSimpleFile()" id="simpleFile" name="theFile" class="TM_attFile">
		</div>        
        </td>
        </tr>
    </table>                
</div>  
<div id="upload_att_Frame" ></div>  
<div class="TM_attach_area">        
    <div id="att_simple_area">              
        <%@include file="inc_simpleAttach.jsp" %>
    </div>                  
</div>
<div id="uploadHiddenFrame"></div>
</form>