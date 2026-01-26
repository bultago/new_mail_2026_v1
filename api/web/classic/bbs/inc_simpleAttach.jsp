<div id="basicUploadAttachList"></div>
<script type="text/javascript">
var basicUploadListeners = {
		swfuploadLoaded: function(event){},
		fileQueued: function(event, file){			
				
		},
		fileQueueError: function(event, file, errorCode, message){
			alert(comMsg.error_fileupload + "["+errorCode+"]");
		},
		fileDialogStart: function(event){},
		fileDialogComplete: function(event, numFilesSelected, numFilesQueued){},
		uploadStart: function(event, file){
			
		},
		uploadProgress: function(event, file, bytesLoaded){			
			
		},
		uploadSuccess: function(event, file, serverData){			
				
		},
		uploadComplete: function(event, file){			
			
		},
		uploadError: function(event, file, errorCode, message){			
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
	basicAttachUploadControl.setAttachSize("normalMax", (Number(MAX_ATTACH_SIZE) * 1024 * 1024));
	updateAttachQuota(0);
}


function startUploadAttach(){		
	requestSaveContents();	
}
</script>
