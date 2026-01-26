/*
 *  TMS7 - HTML5 Uploader Module
 *	Copyright ⓒ 2018-2020 mwkim@solinsystem.co.kr
 */
var html5fileUploader = function(params) {
	/*
	 * ### params ###
	 * drawArea : 파일업로더가 출력될 위치 
	 * callLocation : 호출위치 (예 : mail-html, mail-ajax, bbs, webfolder)
	 * locale : ko, en, jp...
	 */
	
	if(!params.drawArea){
		params.drawArea = "html5FileUploaderArea";	
	}
	
	if(!params.locale){
		params.locale = "ko";	
	}
	
	
    var html5Checker = {
            filereader: typeof FileReader != 'undefined',
            dnd: 'draggable' in document.createElement('span'),
            formdata: !!window.FormData,
            progress: "upload" in new XMLHttpRequest
        };
    
    var fileUploadElements = {
    		"article": {
    			"id": ""
    		},
    		"progressOuter": {
    			"id": "uploadprogressOuter"
    		},			
    		"progress": {
    			"id": "uploadprogress"
    		},
    		"holder": {
    			"id": "holder"
    		},
    		"upload": {
                "id": "upload"
            },
            "fileuploader": {
            	"id": "fileuploader"
            },
            "fileuploaderAddButton": {
            	"id": "fileuploader_add_btn"
            },
			"fileuploaderAddButtonArea": {
				"id": "fileuploader_add_btn_area"
			}
    };
    
    var module = {};
    
    var _createFileUploader = function(){ 
				
		//하위버전 IE인지 확인 (IE8 이하) 
		var isLowerVersionIE = false;
		if(/msie 7/.test(navigator.userAgent.toLowerCase()) || /msie 8/.test(navigator.userAgent.toLowerCase())){
			isLowerVersionIE = true; 
		} 
		
		//IE10 이상에서만 동작
		if(!isLowerVersionIE){
			//스타일 정의
			var uploaderStyle = document.createElement("style");
			var uploaderStyleString = "";
			if(params.callLocation == "mail-html" || params.callLocation == "mail-ajax" || params.callLocation == "mail-popup"){
				uploaderStyleString = "#" + params.drawArea + "{ padding-top: 4px;   padding-right: 3px; } \n";
			} else {
				uploaderStyleString = "#" + params.drawArea + "{ padding-top: 4px;   padding-right: 8px; } \n";
			}
			
			uploaderStyle.innerHTML = 
				uploaderStyleString
				+ "#" + fileUploadElements.holder.id + " { border: 1px solid #ccc; min-height: 30px; } \n"  
				+ "#" + fileUploadElements.holder.id + ".hover { border: 10px dashed #0c0; } \n"
				+ "#" + fileUploadElements.holder.id + " img { display: block; margin: 10px auto; } \n"
				+ "#" + fileUploadElements.holder.id + " p { margin: 10px; font-size: 14px; } \n"			
				+ "#" + fileUploadElements.progress.id + " { width: 100%; color: #0063a6; border-top: 1px solid #ccc; border-left: 1px solid #ccc; border-right: 1px solid #ccc; border-bottom: 0px; background: #ccc; } \n"
				+ "#" + fileUploadElements.progress.id + "::-webkit-progress-bar { background: #ccc; } \n"			
				+ "#" + fileUploadElements.progress.id + "::-webkit-progress-value { background-color:#0063a6; } \n"		
				+ "#" + fileUploadElements.progress.id + "::-moz-progress-bar { background: #ccc; } \n"						
				+ "#" + fileUploadElements.progress.id + ":after { content: ''; } \n"
				+ ".fail { background: #c00; padding: 2px; color: #fff; } \n"
				+ ".hidden { display: none !important;}";
					
			//외부 레이어
			var article = document.createElement("article");
			
			//상단 진행바 외부 레이어 
			var outerProgressDiv = document.createElement("div");
			if(!(/Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor))){
				outerProgressDiv.setAttribute("style", "padding-right:2px");
			}
			outerProgressDiv.setAttribute("id", fileUploadElements.progressOuter.id);
			
			//상단 진행바 
			var innerProgress = document.createElement("progress");
			innerProgress.setAttribute("id", fileUploadElements.progress.id);
			innerProgress.setAttribute("max", "100");
			innerProgress.setAttribute("value", "0");
			innerProgress.innerHTML = '0';
			
			//내부 레이어  
			var innerDiv = document.createElement("div");
			innerDiv.setAttribute("id", fileUploadElements.holder.id);
			innerDiv.setAttribute("style", "text-align: center; padding-top: 20px");
			
			//안내 문구 (내부)
			if(params.locale == "en"){
				innerDiv.innerHTML = '<strong>Upload files by dragging, dropping, or selecting</strong>'; //끌어놓기 하거나 선택하여 파일 업로드    		
			} else if(params.locale == "jp"){
				innerDiv.innerHTML = '<strong>\u30C9\u30E9\u30C3\u30B0\uFF06\u30C9\u30ED\u30C3\u30D7\u3059\u308B\u304B\u3001\u9078\u629E\u3057\u3066\u30D5\u30A1\u30A4\u30EB\u3092\u30A2\u30C3\u30D7\u30ED\u30FC\u30C9\u3059\u308B</strong>'; //끌어놓기 하거나 선택하여 파일 업로드 
			} else {
				innerDiv.innerHTML = '<strong>\uC774 \uACF3\uC5D0 \uB04C\uC5B4\uB193\uAE30 \uD558\uC5EC \uD30C\uC77C\uCCA8\uBD80</strong>'; //이 곳에 끌어놓기하여 파일 첨부
			}
			
			//첨부 영역
			var innerP = document.createElement("p");
			innerP.setAttribute("id", fileUploadElements.upload.id);
			innerP.setAttribute("class", "hidden");
			
			//파일 컨트롤러 
			var innerFile = document.createElement("input");
			innerFile.setAttribute("type", "file");
			innerFile.setAttribute("id", fileUploadElements.fileuploader.id);
			innerFile.setAttribute("multiple", "multiple");
			
			//파일추가 버튼 기능 정의 
			var fileuploaderAddButtonObj = document.getElementById(fileUploadElements.fileuploaderAddButton.id);			
			
			//기능 정의
			if (html5Checker.dnd) {
				innerDiv.ondragover = function() {
					this.className = 'hover';
					return false;
				};
				innerDiv.ondragend = function() {
					this.className = '';
					return false;
				};
				innerDiv.ondrop = function(e) {
					this.className = '';
					e.preventDefault();
					readfiles(e.dataTransfer.files);
				};
				innerDiv.onclick = function(e) {
					innerFile.click();
				};
				innerFile.onchange = function() {
					readfiles(this.files);
				};
				fileuploaderAddButtonObj.onclick = function(e){
					innerFile.click();
				};		
							
				//위에서 만들어진 파일첨부 영역 조립 
				innerP.appendChild(innerFile);
				outerProgressDiv.appendChild(innerProgress);
				article.appendChild(outerProgressDiv);
				article.appendChild(innerDiv);
				article.appendChild(innerP);
				
				//파일첨부 영역 화면에 세팅 
				document.getElementById(params.drawArea).appendChild(uploaderStyle);
				document.getElementById(params.drawArea).appendChild(article);
			} else {			
				//하위버전용 파일업로더 세팅 
				createSimpleFileUploader();
			}
		} else {
			if(/msie 8/.test(navigator.userAgent.toLowerCase())){
				//하위버전용 파일업로더 세팅 
				var fileLabelOuter = document.createElement("label");
				fileLabelOuter.setAttribute("for", "simpleFile");
				fileLabelOuter.setAttribute("style", "background:transparent url(/design/common/image/btn/btn_add_left.gif) repeat-x scroll left top;	color:#777777;	display:inline-block; line-height:21px; padding-left:15px;	text-decoration:none;");

				var fileLabelInnerSpan = document.createElement("span");
				fileLabelInnerSpan.setAttribute("style", "background:transparent url(/design/common/image/btn/btn_basic_right.gif) no-repeat scroll right top;	display:block;	padding-right:8px;");			
				if(params.locale == "en"){
					fileLabelInnerSpan.innerHTML = "Add files";
				} else if(params.locale == "jp"){
					fileLabelInnerSpan.innerHTML = "\u30D5\u30A1\u30A4\u30EB\u306E\u8FFD\u52A0";
				} else {
					fileLabelInnerSpan.innerHTML = "\uD30C\uC77C\uCD94\uAC00";
				}	
				
				fileLabelOuter.appendChild(fileLabelInnerSpan);
				document.getElementById(fileUploadElements.fileuploaderAddButtonArea.id).innerHTML = "";
				document.getElementById(fileUploadElements.fileuploaderAddButtonArea.id).appendChild(fileLabelOuter);						
			} else {
				document.getElementById(fileUploadElements.fileuploaderAddButtonArea.id).innerHTML = "";
			}			
		}
    };
    
    module.createFileUploader = _createFileUploader;

	//html9에서만 사용하는 파일업로더 
	function createSimpleFileUploader(){
		var fileUploaderAddButtonObj = document.getElementById(fileUploadElements.fileuploaderAddButton.id);
		
		fileUploaderAddButtonObj.onclick = function(e){
			document.getElementById("simpleFile").click();
		};
	}	
    
    function loadEnd(e) {
        jQuery("#uploadprogress").attr('value',0);
    }

    function readfiles(files) {
        //debugger;
        var formData = html5Checker.formdata ? new FormData() : null;
        var useAttachQuota;
        var hugeSize = 0;
        var preFileSize = 0;
        var overSizeMsg;
        
        if(params.callLocation == "mail-html" || params.callLocation == "mail-ajax" || params.callLocation == "mail-popup"){
        	useAttachQuota = maxbigattachquota;
        	hugeSize = basicAttachUploadControl.getAttachSize("hugeUse");
        } else if(params.callLocation == "webfolder" || params.callLocation == "bbs"){
        	useAttachQuota = basicAttachUploadControl.getAttachQuotaInfo().normalMax;
        	hugeSize = basicAttachUploadControl.getAttachQuotaInfo().normalUse;        
        }
        
        if(params.callLocation == "mail-html" || params.callLocation == "mail-ajax" || params.callLocation == "mail-popup"){
            // 파일크기의 총합이 대용량 첨부로 허용되는 용량(000MB)을 초과합니다. 확인 후 다시 시도 바랍니다.
        	overSizeMsg = "\ud30c\uc77c\ud06c\uae30\uc758 \ucd1d\ud569\uc774 \ub300\uc6a9\ub7c9 \ucca8\ubd80\ub85c \ud5c8\uc6a9\ub418\ub294 \uc6a9\ub7c9(" + printSize(useAttachQuota) + ")\uc744 \ucd08\uacfc\ud569\ub2c8\ub2e4. \ud655\uc778 \ud6c4 \ub2e4\uc2dc \uc2dc\ub3c4 \ubc14\ub78d\ub2c8\ub2e4.";                
        } else if(params.callLocation == "webfolder" || params.callLocation == "bbs"){
        	// 파일크기의 총합이 허용되는 용량(000MB)을 초과합니다. 확인 후 다시 시도 바랍니다.
        	overSizeMsg = "\ud30c\uc77c\ud06c\uae30\uc758 \ucd1d\ud569\uc774 \ud5c8\uc6a9\ub418\ub294 \uc6a9\ub7c9(" + printSize(useAttachQuota) + ")\uc744 \ucd08\uacfc\ud569\ub2c8\ub2e4. \ud655\uc778 \ud6c4 \ub2e4\uc2dc \uc2dc\ub3c4 \ubc14\ub78d\ub2c8\ub2e4.";
        }
        
        // file size sum
        for (var i = 0; i < files.length; i++) {
            if (html5Checker.formdata) {
            	preFileSize = preFileSize + files[i].size;
            }
        }
        
        // file size check 
		if((hugeSize + preFileSize) > useAttachQuota) {			
			alert(overSizeMsg);
			return;
		} else {		
			for (var i = 0; i < files.length; i++) {
	            if (html5Checker.formdata) {            	
	    			formData.append('theFile', files[i]);    			    			
	            }
	        }
		}
        
        // now post a new XHR request
        if (html5Checker.formdata) {
            var xhr = new XMLHttpRequest();
            xhr.addEventListener("loadend", loadEnd);
            
            xhr.onload = function() {
            	document.getElementById(fileUploadElements.progress.id).value = 100;
            	document.getElementById(fileUploadElements.progress.id).innerHTML = 100;
            };

            if (html5Checker.progress) {
                xhr.upload.onprogress = function(event) {
                    if (event.lengthComputable) {
                        var complete = (event.loaded / event.total * 100 | 0);
                        document.getElementById(fileUploadElements.progress.id).value = complete; 
                        document.getElementById(fileUploadElements.progress.id).innerHTML = complete;
                    }
                };
            }

            xhr.onreadystatechange = function(e) {
                var status;
                var data;
                if (4 == this.readyState || 1 == this.readyState) {
                    status = xhr.status;
                    data = JSON.parse(xhr.responseText);
                    //console.log(['xhr upload complete', e]);
                    //console.log(data);
                    //console.log(data.uploadResult);
                    //console.log(data.length);
                    //console.log(status);
                    if(data.uploadResult == "success"){
                        for (j in data.files) {                         
                        	//첨부파일 목록에 파일 추가 
                        	try {
                        		if(data.files[j].fileName){
		                        	if(params.callLocation == "mail-html" || params.callLocation == "mail-ajax" || params.callLocation == "mail-popup"){
			                        	if(chkBasicAttachFileSize(data.files[j])){
			                        		var tmpAttFileId = addBasicList(data.files[j].fileName, data.files[j].fileSize, data.files[j].filePath, data.files[j].uid, "normal");                        		
			                        		changeBasicBigAttachFileType(data.files[j], tmpAttFileId);                        		
			                        	}                                 
		                        	} else if(params.callLocation == "bbs"){
		                        		//첨부파일 목록에 파일 추가                             
										var tmpAttFileId = addlist(data.files[j].fileName, data.files[j].fileSize, data.files[j].filePath, data.files[j].uid);
		                        	} else if(params.callLocation == "webfolder"){
		                        		//첨부파일 목록에 파일 추가                             
										var tmpAttFileId = addlist(data.files[j].fileName, data.files[j].fileSize, data.files[j].filePath, data.files[j].uid);
		                        	}
                        		}
                        	} catch (e) {
                        		//TODO
                        	}
                        }
                    }
                }
            };

            xhr.open('POST', '/file/uploadAttachFile.do');
            formData.append("uploadType", "ajax");
            xhr.send(formData);
        }
    }
    
    return module;
};


//201911 HTML5 Uploader
function uploadSimpleFile() {
	jQuery("#up_file_basic_frame").remove();
	var f = document.uploadForm;
	if (f.theFile.value == "") {
		return;
	}
	jQuery("#upload_att_Frame").makeUploadFrame("up_file_basic_frame");
	f.maxAttachFileSize.value = MAX_ATTACH_SIZE;
	f.uploadType.value = "basic";
	f.action = "/file/uploadAttachFile.do";
	f.method = "post";
	f.target = "up_file_basic_frame";
	f.submit();
	var fileObj = jQuery("#simpleFile");
	fileObj.replaceWith(fileObj = fileObj.clone(true));
}

