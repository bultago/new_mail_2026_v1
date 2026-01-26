<div id="basicUploadAttachList"></div>
<script type="text/javascript">
var uploadAttachFilesError = false;
var basicUploadListeners = {
		swfuploadLoaded: function(event){},
		fileQueued: function(event, file){			
			var size = file.size;
			var quota = basicAttachUploadControl.getAttachQuotaInfo();						
			if((size > quota.normalMax)){
    			alert(msgArgsReplace(webfolderMsg.error_upload_over,[MAX_ATTACH_SIZE]));
    			jQuery("#basicUploadControl").swfupload("cancelUpload",file.id);
    			return;
			}
	    		
			jQuery("#basicUploadControl").swfupload("addFileParam",file.id,"attachtype","normal");
			updateAttachQuota(size + quota.normalUse);
			basicAttachUploadControl.addAttachList(file);
			basicAttachUploadControl.addUploadQueueFile(file);			
		},
		fileQueueError: function(event, file, errorCode, message){
			alert(comMsg.error_fileupload + "["+errorCode+"]");
		},
		fileDialogStart: function(event){},
		fileDialogComplete: function(event, numFilesSelected, numFilesQueued){},
		uploadStart: function(event, file){
			$("uploadLoadingBox").scrollTop = file.index * 40;
		},
		uploadProgress: function(event, file, bytesLoaded){			
			var fileSize = file.size;
			var fileUploadPercent = Math.ceil(bytesLoaded / fileSize * 100); 
			fileUploadPercent = (fileUploadPercent >= 100) ? 100 : fileUploadPercent;
			$(file.id+"GraphBar").style.width = fileUploadPercent+"%";			
			$(file.id+"Status").innerHTML = fileUploadPercent+"%";
		},
		uploadSuccess: function(event, file, serverData){			
			var data = eval("("+serverData+")");
			if(data.uploadResult == "success"){
				basicAttachUploadControl.setUploadCompleteFile(file.id,data);
			} else {
				alert(comMsg.error_fileupload);
				$(file.id).checked = true;
				deletefile();
				jQuery("#attachUploadProgress").jQpopup("close");				
				basicAttachUploadControl.stopUpload();
			}			
		},
		uploadComplete: function(event, file){			
			$(file.id+"Status").innerHTML = comMsg.comn_upload_complete;
			if(!basicAttachUploadControl.isNextUploadQueue(file.id)){
				jQuery("#attachUploadProgress").jQpopup("close");				
				setTimeout(function(){
					ocxSubmit();
				},500);
			}
		},
		uploadError: function(event, file, errorCode, message){
			if(errorCode != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED){
				jQuery("#attachUploadProgress").jQpopup("close");
				alert(comMsg.error_fileupload);
			}
		}
	};

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


function startUploadAttach(){	
	var html,file;
	var fileLists = basicAttachUploadControl.getUploadQueueFile();
	
	if(fileLists.length > 0){
		jQuery("table#attachFileUploadTable  tr").remove();
		for ( var i = 0; i < fileLists.length; i++) {
			file = fileLists[i];
			html = "<tr><td style='height:40px' valign='top'>"+
			"<p style='width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap' title='"+escape_tag(file.name)+"'> <img src='/design/common/image/icon/ic_att_"+getFileTypeImage(file.name)+".gif'/>&nbsp;"+escape_tag(file.name)+"</p>"+
			"<div id='"+file.id+"GBox' style='position:relative'><div id='"+file.id+"GraphBar' class='TM_upload_graphBar'></div>" +
			"<div class='TM_upload_capacity'><span class='TM_quotaUsage'>"+printSize(file.size)+"</span></div></div>"+
			"</td><td id='"+file.id+"Status' style='padding-top:5px;text-align:center'>"+comMsg.comn_upload_ready+"</td></tr>";			
			jQuery("#attachFileUploadTable").append(html);
		}

		var popOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3"			
		};
		popOpt.btnList = [];
		popOpt.hideCloseBtn = true;		
		popOpt.minHeight = 150;
		popOpt.minWidth = 400;		
		popOpt.openFunc = function(){		
			jQuery("div#attachUploadProgress table").css("width","100%");		
			basicAttachUploadControl.startUpload();		
		};
		popOpt.notResize = true;
		popOpt.closeFunc = function(){
			jQuery("div#attachUploadProgress table").css("width","");						
		};
		
		jQuery("#attachUploadProgress").jQpopup("open",popOpt);
	
	} else {
		alert(webfolderMsg.alert_noupload);
		return;
	}
}
</script>