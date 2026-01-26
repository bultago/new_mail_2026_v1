<script language = "javascript">
var today = new Date(1234227867105);
var expiredate = new Date(today.getTime() + (7 * 24 * 60 * 60 * 1000));
var quotausage = 0;
var userquota = 0;
var bigattachquota = (1024 * 1024 * 500) - quotausage + userquota;

    function chgAttachMod(type) {
        var simpleCrtBtn = jQuery("#att_btn_simple");
        var simpleIncFrame = jQuery("#att_simple_area");
        var simpleQuotaInfo = jQuery("#att_simple_quota_info");
        var simpleFileObj = jQuery("#simpleFileInit"); //201802 HTML5 Uploader
    
        try {
            simpleCrtBtn.show();
            simpleIncFrame.show();
            simpleQuotaInfo.show();
    
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

    function addFolderUploadFiles() {
        var form = document.uploadForm;
        var oldTarget = form.target;
        var param = getFolderParam();
        form.path.value = param.path;
        form.userSeq.value = param.userSeq;
        form.sroot.value = param.sroot;
        form.nodeNum.value = param.nodeNum;
        form.type.value = param.type;

        var attstr = getAttachString();

        if (attstr == "") {
            alert(webfolderMsg.alert_noupload);
            return;
        }
        form.attlists.value = attstr;
        form.target = document.listForm.target;
        form.uploaderType.value = "normal";

        jQuery("body").loadWorkMaskOnly(true);

        form.action = '/webfolder/uploadFiles.do';
        form.submit();
        form.target = oldTarget;
        jQuery("object").hide();
    }
    
    function updateAttachQuota(size){
        $("basic_normal_size").innerHTML = printSize(size);
        basicAttachUploadControl.setAttachSize("normalUse", size);  
    }
    
    function basic_init(){
        basicAttachUploadControl.init();
        basicAttachUploadControl.makeBtnControl();
        basicAttachUploadControl.makeListControl();
        basicAttachUploadControl.setAttachSize("normalMax", (MAX_ATTACH_SIZE * 1024 * 1024));
        updateAttachQuota(0);
    }   
</script>

<div class="TM_separate2"></div>
<br/>
<form name="uploadForm" method="post" enctype="multipart/form-data">
    <input type="hidden" name=upldtype value='upld'>
    <input type="hidden" name="writeFile" value="true">
    <input type="hidden" name="attfile" value="true">
    <input type="hidden" name="attsize" value="0">
    <input type="hidden" name="userId" value="jhlee@terracetech.com">
    <input type="hidden" name="path">
    <input type="hidden" name="sroot">
    <input type="hidden" name="nodeNum">
    <input type="hidden" name="userSeq">
    <input type="hidden" name="type">
    <input type="hidden" name="attlists" value="">
    <input type="hidden" name="attachType">
    <input type="hidden" name="uploadType">
    <input type="hidden" name="maxAttachFileSize">
    <input type="hidden" name="uploaderType">
    
<div class="TM_webfolder_toolbar">  
    <table cellpadding="0"  cellspacing="0" border="0">
        <tr>
        <td style="padding-right:5px;" nowrap><strong><tctl:msg key="list.fileattaching" bundle="webfolder"/></strong></td>
        <td width="100%" style="padding-left:10px;" nowrap>
        <div style="float:left;padding:0px 5px 0px 0px;" id="fileuploader_add_btn_area"><a href="javascript:;" class="btn_add" id="fileuploader_add_btn"><span><tctl:msg key="mail.attach.add"/></span></a></div>		
        <div style="float:left;padding:0px 5px 0px 0px;"><a href="#" class="btn_basic" onclick="addFolderUploadFiles()"><span><tctl:msg key="webfolder.folder.file.add" bundle="webfolder"/></span></a></div>
        <div style="float:left;padding:0px 5px 0px 0px;white-space: nowrap"><a href="#n" onclick="deletefile()" class="btn_del"><span><tctl:msg key="mail.attach.delete"/></span></a></div>
        </td>
        </tr>
        <tr>
        <td colspan="2">
        <div id="html5FileUploader">
            <div id="html5FileUploaderArea"></div>
            <script>
                var html5FileUploaderParams = {"drawArea":"html5FileUploaderArea", "callLocation":"webfolder", "locale":LOCALE};                       
                var html5fileUploaderObj = new html5fileUploader(html5FileUploaderParams);
                html5fileUploaderObj.createFileUploader();    
            </script> 
        </div>
		<div id="simpleFileInit" class="TM_bbs_toolbar" style="display:none;">                                                             
		    <input type="file" size="50" onchange="uploadSimpleFile(this)" id="simpleFile" name="theFile" class="TM_attFile">
		</div>         
        </td>
        </tr>       
    </table>                
</div>      

<div id="upload_att_Frame" ></div> 


<div class="TM_attach_area" style="height:130px;">      
    <div id="att_simple_area">              
        <div id="basicUploadAttachList"></div>
    </div>                  
</div>
<div id="uploadHiddenFrame"></div>
</form>