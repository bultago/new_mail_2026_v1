function updateHugeQuota(useageQuota){
    bigattachquota = maxbigattachquota - useageQuota;
    bigattachquota = (bigattachquota < 0)?maxbigattachquota:bigattachquota;    
	$("basic_huge_quota").innerHTML = printSize(bigattachquota);
	basicAttachUploadControl.setAttachSize("hugeMax",bigattachquota);    
}

function makeBigAttachContent(){
	var bigAttachInfo = {};
	var html;
	var linkArray = [];

	var big_str = "<br><br><table width='100%' border=0 cellspacing=0 cellpadding=0>"
		+"<tr height=27>"
		+"<td colspan=3 style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;white-space:nowrap'>"
		+"<b><span style='font-size:13px;'>"+mailMsg.bigattach_06+"</span></b>&nbsp;&nbsp;&nbsp;"
		+"<span style='font-size:12px;color:#666666;'>( "+msgArgsReplace(mailMsg.bigattach_07,[BIGATTACH_EXPIRE,BIGATTACH_DOWNCNT])+")</span>"
		+"</td>"
		+"<tr height=27 bgcolor=#FAFAFA style='font-size:13px;font-weight:bold'>"
		+"<td align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
		+mailMsg.bigattach_03+"</td>"
		+"<td width=150 align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
		+mailMsg.bigattach_04+"</td>"
		+"<td width=150 align=center style='padding:4 10 0 0; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
		+mailMsg.bigattach_09+"</td></tr>";

	var big_cnt = 0;	
	var bigAttachInfos = [];
	var type,size,filename,filepath,uid,link;
	var bigAttachInfo;
	
	var hugeFiles = basicAttachUploadControl.getFileList("huge");
	for (var i = 0; i < hugeFiles.length; i++) {			
		size 	= hugeFiles[i].size;
		filename = hugeFiles[i].name;			
		uid = hugeFiles[i].uid;			
		bigAttachInfos.push({"size":size,"fileName":filename,"uid":uid});
	}
	
	for (var i = 0; i < bigAttachInfos.length; i++) {
		bigAttachInfo = bigAttachInfos[i];				
		size 	= bigAttachInfo.size;
		filename = bigAttachInfo.fileName;		
		linkArray.push("email="+USEREMAIL+"&uid="+bigAttachInfo.uid);
		
       	big_str += "<tr height=29 style='font-size:12px'>"
			// FILEINFO
           	+"<td style='padding:4 0 0 20; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
           	+"<a href='{tims_bigattach_link_"+i+"}' target='_self'><span style='text-decoration : none; color=#666666;'>"
			+filename+" ("+printSize(size)+")</span></a>"
           	+"</td>"
           	// DATE
           	+"<td width=150 align=center style='padding:4 0 0 10; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
           	+today.getFullYear()+"/"+(today.getMonth()+1)+"/"+today.getDate()
           	+" ~ "+expiredate.getFullYear()+"/"+(expiredate.getMonth()+1)+"/"+expiredate.getDate()
           	+"</td>"
           	// DONWLINK
           	+"<td width=150 align=center style='padding:4 10 0 0; border-top-width:0; border-right-width:0; border-bottom-width:1; border-left-width:0; border-color:rgb(216,216,216); border-style:solid;'>"
           	+"<a href='{tims_bigattach_link_"+i+"}' target='_self'><span style='text-decoration : none; color=#02338D;'>"
			+"["+mailMsg.bigattach_05+"]</span></a>"
           	+"</td>";

		big_cnt++;
	}

	big_str += "</table>";


	if (big_cnt > 0) {		
		bigAttachInfo.mode="on";
		bigAttachInfo.html = big_str;
		bigAttachInfo.links = linkArray;
	} else {
		bigAttachInfo.mode="off";
		bigAttachInfo.html = "";
		bigAttachInfo.links = linkArray;		
	}	 

	return bigAttachInfo;
	
}

function hugeMailCheck(){
	var hugeSize;
	
	hugeSize = basicAttachUploadControl.getAttachSize("hugeUse");
	
	var chkReserve = $("reservation").checked;
    var reserverdObj = $('reservation');
    var securemailObj = $('securemail');

	if (hugeSize > 0 && chkReserve) {	
		  if(securemailObj && securemailObj.disabled){
		   		chkSecureMail(true);
		   }
		   alert(mailMsg.write_alert006);
		   chkReservedMail(false);		   		   		   
	} else if(hugeSize > 0 && securemailObj && securemailObj.checked){
	      if(reserverdObj.disabled){	   				   	
	    	  chkReservedMail(true);
		   }
		   alert(mailMsg.write_alert005);		   
		   chkSecureMail(false);
	} else if(hugeSize == 0){
	    if(reserverdObj.disabled){
	    	chkReservedMail(true);
	    }
		else if(securemailObj && securemailObj.disabled){
		   chkSecureMail(true);
		}
	}	
}


function basic_init(){
	basicAttachUploadControl.setAttachSize("hugeMax", bigattachquota);
	basicAttachUploadControl.setAttachSize("hugeUse", 0);
	basicAttachUploadControl.setAttachSize("normalMax", 1024 * 1024 * MAX_ATTACH_SIZE);
	basicAttachUploadControl.setAttachSize("normalUse", 0);
	$("basic_normal_size").innerHTML = printSize(0);
	$("basic_huge_size").innerHTML = printSize(0);
	$("basic_huge_quota").innerHTML = printSize(bigattachquota);
}

function updateAttachQuota(type,size){
	if(type == "hugeUse"){
		$("basic_huge_size").innerHTML = printSize(size);
		basicAttachUploadControl.setAttachSize("hugeUse", size);
	} else if(type == "normalUse"){
		$("basic_normal_size").innerHTML = printSize(size);
		basicAttachUploadControl.setAttachSize("normalUse", size);
	}
}

var uploadAttachFilesError = false;
var uploadAttachFilesComplete = false;
var basicUploadListeners = {
		swfuploadLoaded: function(event){},
		fileQueued: function(event, file){			
			var size = file.size;
			var quota = basicAttachUploadControl.getAttachQuotaInfo();
			var isNormalOver = ((size + quota.normalUse) > quota.normalMax)?true:false;
			if(((size > quota.normalMax) ||	isNormalOver)&& 
					basicAttachUploadControl.getHugeUploadUse()){					
				file.type = "huge";				
				if((size + quota.hugeUse) > quota.hugeMax) {
					if(isNormalOver)alert(mailMsg.ocx_upalert_size); 
					else alert(printSize(quota.hugeMax)+" "+mailMsg.ocx_upalert_size);
	    			jQuery("#basicUploadControl").swfupload("cancelUpload",file.id);
	    			return;
	    		}
				jQuery("#basicUploadControl").swfupload("addFileParam",file.id,"attachtype","huge");
				jQuery("#basicUploadControl").swfupload("addFileParam",file.id,"regdate",today.getTime());
				updateAttachQuota("hugeUse",size + quota.hugeUse);
				hugeMailCheck();
			} else {
				file.type = "normal";
				if((size + quota.normalUse) > quota.normalMax) {
					alert(printSize(quota.normalMax)+" "+mailMsg.ocx_upalert_size);	
					jQuery("#basicUploadControl").swfupload("cancelUpload",file.id);
	    			return;
				}
				jQuery("#basicUploadControl").swfupload("addFileParam",file.id,"attachtype","normal");
				updateAttachQuota("normalUse",size + quota.normalUse);
			}			
			basicAttachUploadControl.addAttachList(file);
			basicAttachUploadControl.addUploadQueueFile(file);			
		},
		fileQueueError: function(event, file, errorCode, message){
			if(errorCode == "-120"){
				alert(mailMsg.ocx_virtxt_failquest);
			} else {
				alert(comMsg.error_fileupload + "["+errorCode+"]");
			}
		},
		fileDialogStart: function(event){},
		fileDialogComplete: function(event, numFilesSelected, numFilesQueued){},
		uploadStart: function(event, file){
			if(!uploadAttachFilesError){
				$("uploadLoadingBox").scrollTop = file.index * 40;
			}else {
				jQuery("#attachUploadProgress").jQpopup("close");				
			}
		},
		uploadProgress: function(event, file, bytesLoaded){			
			var fileSize = file.size;
			var fileUploadPercent = Math.ceil(bytesLoaded / fileSize * 100); 
			fileUploadPercent = (fileUploadPercent > 100) ? 100 : fileUploadPercent;
			$(file.id+"GraphBar").style.width = fileUploadPercent+"%";			
			$(file.id+"Status").innerHTML = fileUploadPercent+"%";
		},
		uploadSuccess: function(event, file, serverData){			
			var data = eval("("+serverData+")");
			if(data.uploadResult == "success" && parseInt(data.uid,10) > -1){ //TCUSTOM-3411 20170731 modify
				basicAttachUploadControl.setUploadCompleteFile(file.id,data);
			} else {
				if(!uploadAttachFilesError){					
					alert(comMsg.error_fileupload);
					uploadAttachFilesError = true;
				}
				$(file.id).checked = true;
				deletefile();
				//jQuery("#attachUploadProgress").jQpopup("close");
				
			}
		},
		uploadComplete: function(event, file){			
			$(file.id+"Status").innerHTML = comMsg.comn_upload_complete;
			if(!basicAttachUploadControl.isNextUploadQueue(file.id)){
				uploadAttachFilesComplete = true;
				jQuery("#attachUploadProgress").jQpopup("close");
				var param = jQuery("#basicUploadControl").data("sendParam");				
				setTimeout(function(){
						requestSendAction(param);
				},500);
			}
		},
		uploadError: function(event, file, errorCode, message){
			if(errorCode != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED){
				jQuery("#attachUploadProgress").jQpopup("close");
				if(!uploadAttachFilesError){					
					alert(comMsg.error_fileupload);
					uploadAttachFilesError = true;
				}
			}
		}
	};
var basicAttachUploadControl;
function chgAttachMod(type)
{
	var simpleCrtBtn 		= jQuery("#att_btn_simple");	
	var simpleIncFrame	 	= jQuery("#att_simple_area");
	var simpleQuotaInfo 	= jQuery("#att_simple_quota_info");
	var simpleModeBtn	 	= jQuery("#crtBtnSimple");
	
	var simpleFileObj 		= jQuery("#simpleFileInit");
	
	if(MENU_STATUS.webfolder && MENU_STATUS.webfolder == "on")jQuery("#att_btn_webfolder").show();
	else jQuery("#att_btn_webfolder").hide();
	
	try {			
		//파일업로더 화면 처리 		
		simpleModeBtn.hide();
		
		jQuery("#crtBtnPower").hide();
		jQuery("#basicUploadControl").hide();
		jQuery("#basic_bigattach_size").show();
		jQuery("#bigAttachMgn").show();
		jQuery("#bigattachMessageSpan").show();
		
		//html5 사용 가능여부 확인 
		var html5FileApiSupport = {
	        filereader: typeof FileReader != 'undefined',
	        dnd: 'draggable' in document.createElement('span'),
	        formdata: !!window.FormData,
	        progress: "upload" in new XMLHttpRequest
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
		
		simpleIncFrame.show();
		simpleQuotaInfo.show();
		
		basicAttachUploadControl.init();
		basicAttachUploadControl.makeBtnControl();
		basicAttachUploadControl.makeListControl();
		basic_init();
	} catch (e) {throw e}
}

function checkUploadfile() {
    var f = document.writeForm;
	var name, size, upkey, uid, atype;

	if (AttachList.length > 0) {		
		for(var i = 0; i < AttachList.length; i++) {			
			upkey = AttachList[i][0];
			name  = AttachList[i][1];
			size  = AttachList[i][2];
			uid   = (AttachList[i].length > 3) ? AttachList[i][3] : "";			
			atype = ((size > MAX_ATTACH_SIZE*1024*1024))?"huge":"normal";

			addlist(name, size, upkey, uid,atype);
        }
        fileCheck = true;
    }else{
        fileCheck = true;
    }
}

function deletefile() {	
	var f = document.uploadForm;	
		var checkObjIds = basicAttachUploadControl.getCheckAttachFileIds();
		if(checkObjIds.length > 0){			
			for (var i = 0 ; i < checkObjIds.length ; i++) {
				basicAttachUploadControl.deleteAttachList(checkObjIds[i]);
			}			
			var quota = basicAttachUploadControl.getAttachQuotaInfo();
			updateAttachQuota("hugeUse",quota.hugeUse);
			updateAttachQuota("normalUse",quota.normalUse);
			$("basicAttachChkAll").checked = false;
		}                       
}
function addWebfolderAttach(attachList){
	var name, size, upkey, uid,atype;	
	for(var i = 0; i < attachList.length; i++) {		
		upkey = attachList[i][0];
		name  = attachList[i][1];
		size  = attachList[i][2];
		atype = ((size > MAX_ATTACH_SIZE*1024*1024))?"huge":"normal";		
		addlist(name, size, upkey, "",atype);
	}
}

function addlist(n, s, p, uid, type) {	
    var f = document.uploadForm;
    var select = f.attlist;
    var text = n+" ("+s+" bytes)";
    var _value = p + "\t" + n + "\t" + s + "\t" + uid + "\t";
    var atype = (type)?type:"normal";
    var hugeSize;

    var upldtype= "";
    try{
    	upldtype = p.substring((p.length-1), p.length);    	
    } catch (e){}
    if (upldtype == "r") {
        document.massmail.rcptsfile.value = p;
		$("filename").innerHTML = text;
        //alert("upload mass rcpt file!!");
    } else {
    	var attsize = parseInt(s);
    	if(atype == "normal"){
			// HTML UPLOAD
	        attsize = parseInt(f.attsize.value) + parseInt(s);
	
	        if(attsize > (MAX_ATTACH_SIZE*1024*1024)) {
	            alert(MAX_ATTACH_SIZE+"MB "+mailMsg.ocx_upalert_size);	                         
	            return;
	        }	
	        f.attsize.value = attsize;	        
    	} else if(atype == "huge"){    		
    		hugeSize = basicAttachUploadControl.getAttachSize("hugeUse");
	    	
    		if((hugeSize + attsize) > maxbigattachquota) {
    			alert(printSize(maxbigattachquota)+"MB "+mailMsg.ocx_upalert_size);
    			return;
    		}
    	}
				
			var file = {"path":p,"size":s,"name":n,
				"uid":uid,"type":atype,"notChage":false,
					"id":"SWFUpload_0_a"+makeRandom()};
			basicAttachUploadControl.addAttachList(file);
		if(atype == "huge") {
			updateAttachQuota("hugeUse",hugeSize + attsize);
		} else {
			updateAttachQuota("normalUse",attsize);
		}
    }
}

function getAttachString() {
	var pstr = "";
		var uploadedFile = basicAttachUploadControl.getFileList("normal");
		for(var i = 0; i < uploadedFile.length; i++) {			
			pstr += uploadedFile[i].path + "\t"
			+ uploadedFile[i].name + "\t"
			+ uploadedFile[i].size + "\t"
			+ uploadedFile[i].uid + "\n"
			;
		}

	return pstr;
}

function chgAttachFileType(fid){
	var fileChkObj = jQuery("#"+fid);
	var type = fileChkObj.attr("atttype");
	var size = Number(fileChkObj.attr("attsize"));
	var useSize,maxSize;
	var quota = basicAttachUploadControl.getAttachQuotaInfo();
	if(type == "huge"){
		if((size > quota.normalMax) ||
			(size+quota.normalUse > quota.normalMax)){
			alert(printSize(quota.normalMax)+" "+mailMsg.ocx_upalert_size);
			return;
		}
		updateAttachQuota("hugeUse",quota.hugeUse - size);
		updateAttachQuota("normalUse",quota.normalUse + size);
	} else {
		if((size > quota.hugeMax) ||
			(size+quota.hugeUse > quota.hugeMax)){
			alert(printSize(quota.hugeMax)+" "+mailMsg.ocx_upalert_size);
			return;
		}
		updateAttachQuota("hugeUse",quota.hugeUse + size);
		updateAttachQuota("normalUse",quota.normalUse - size);
	}
	basicAttachUploadControl.chageAttachType(fid);
	hugeMailCheck();
}

function startUploadAttach(param){	
	requestSendAction(param);
}

//201802 HTML5 Uploader
function addBasicList(n, s, p, uid, type) {	
    var f = document.uploadForm;
    var atype = (type)?type:"normal";
    var hugeSize;
   
	var attsize = parseInt(s);
	if(atype == "normal"){
		// HTML UPLOAD
        attsize = parseInt(f.attsize.value) + parseInt(s);	
        f.attsize.value = attsize;	        
	} else if(atype == "huge"){    		
		hugeSize = basicAttachUploadControl.getAttachSize("hugeUse");
		
		if((hugeSize + attsize) > maxbigattachquota) {
			alert(printSize(maxbigattachquota)+"MB "+mailMsg.ocx_upalert_size);
			return;
		}
	}
        		
	var fileId = "SWFUpload_0_a"+makeRandom();
	var file = {"path":p,"size":s,"name":n,"uid":uid,"type":atype,"notChage":false, "id":fileId};
	basicAttachUploadControl.addAttachList(file);
		
	//전체 일반첨부파일 사이즈 계산
	var normalUploadedFile = basicAttachUploadControl.getFileList("normal");
    var normalFileSize = 0;
    for(var i = 0; i < normalUploadedFile.length; i++) {            
        normalFileSize += normalUploadedFile[i].size;
    }
    updateAttachQuota("normalUse",normalFileSize);
    
    //전체 일반첨부파일 사이즈 계산
    var hugeUploadedFile = basicAttachUploadControl.getFileList("huge");
    var hugeFileSize = 0;
    for(var j = 0; j < hugeUploadedFile.length; j++) {          
        hugeFileSize += hugeUploadedFile[j].size;
    }
    updateAttachQuota("hugeUse",hugeFileSize);
	
	return fileId;
}

//201802 HTML5 Uploader
function changeBasicBigAttachFileType(objFile, fid){
	//전체 일반첨부파일 사이즈 계산 (파일첨부 전 데이터)
	var normalUploadedFile = basicAttachUploadControl.getFileList("normal");
    var normalFileSize = 0;
    for(var i = 0; i < normalUploadedFile.length; i++) {            
        normalFileSize += normalUploadedFile[i].size;
    }
	
    normalFileSize = normalFileSize - objFile.fileSize;
    
	if((objFile.fileSize > (1024 * 1024 * MAX_ATTACH_SIZE)) || (objFile.fileSize + normalFileSize > (1024 * 1024 * MAX_ATTACH_SIZE))){
		//첨부된 파일 사이즈가 큰 경우 대용량으로 목록에 추가
		basicAttachUploadControl.chageAttachType(fid);
	} else if(objFile.attachMode == "huge") {
		//원래부터 대용량인 경우 대용량으로 목록에 추가
		basicAttachUploadControl.chageAttachType(fid);
	}
	
	//전체 일반첨부파일 사이즈 계산 후 등록(파일첨부 후 데이터)
	normalUploadedFile = basicAttachUploadControl.getFileList("normal");
    normalFileSize = 0;
    for(var i = 0; i < normalUploadedFile.length; i++) {            
        normalFileSize += normalUploadedFile[i].size;
    }
    updateAttachQuota("normalUse",normalFileSize);
	
    //전체 대용량첨부파일 사이즈 계산 후 등록 (파일첨부 후 데이터)
    var hugeUploadedFile = basicAttachUploadControl.getFileList("huge");
    var hugeFileSize = 0;
    for(var j = 0; j < hugeUploadedFile.length; j++) {          
        hugeFileSize += hugeUploadedFile[j].size;
    }
    updateAttachQuota("hugeUse",hugeFileSize);	
	hugeMailCheck();
}

function chkBasicAttachFileSize(objFile){	
	//전체 일반첨부파일 사이즈 계산
	var normalUploadedFile = basicAttachUploadControl.getFileList("normal");
    var normalFileSize = 0;
    for(var i = 0; i < normalUploadedFile.length; i++) {            
        normalFileSize += normalUploadedFile[i].size;
    }
    
    //전체 대용량첨부파일 사이즈 계산
    var hugeUploadedFile = basicAttachUploadControl.getFileList("huge");
    var hugeFileSize = 0;
    for(var i = 0; i < hugeUploadedFile.length; i++) {          
        hugeFileSize += hugeUploadedFile[i].size;
    }    
    
    if((objFile.fileSize > (1024 * 1024 * MAX_ATTACH_SIZE)) || (objFile.fileSize + normalFileSize > (1024 * 1024 * MAX_ATTACH_SIZE))){
		//첨부된 파일 사이즈가 큰 경우 대용량으로 목록에 추가
    	if((objFile.fileSize > (1024 * 1024 * MAX_BIG_ATTACH_SIZE)) || (objFile.fileSize + hugeFileSize > (1024 * 1024 * MAX_BIG_ATTACH_SIZE))){
    		alert(printSize(maxbigattachquota) + " " + mailMsg.ocx_upalert_size + " (" + mailMsg.bigattach_11 + ")");
    		return false;    		
    	}
	} else if(objFile.attachMode == "huge") {
		//원래부터 대용량인 경우 대용량으로 목록에 추가
    	if((objFile.fileSize > (1024 * 1024 * MAX_BIG_ATTACH_SIZE)) || (objFile.fileSize + hugeFileSize > (1024 * 1024 * MAX_BIG_ATTACH_SIZE))){
    		alert(printSize(maxbigattachquota) + " " + mailMsg.ocx_upalert_size + " (" + mailMsg.bigattach_11 + ")");
    		return false;    		
    	}
    } 
    
   return true;
}