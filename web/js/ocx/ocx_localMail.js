function sendOcxLocalMail(email,hostInfo,folders,fuid,mailSubject,mailSize){	    	
	
	var url = []; 
	
	var isFolderMulti = false;
	if(folders.length > 1){
		isFolderMulti = true;
	}
	
	if(fuid.length>1){
		var folderName;
		for(i=0 ; i<fuid.length ; i++){			
			if(isFolderMulti){
				folderName = folders[i];
			} else {
				folderName = folders[0];
			}			
			url[url.length] = hostInfo+"/mail/downLocalMailMessages.do?folder="+encodeURI(folderName)+"&uid="+fuid[i];			
		 }
	}else{
		url[url.length] = hostInfo+"/mail/downLocalMailMessages.do?folder="+encodeURI(folders[0])+"&uid="+fuid[0];
	}	    		
	
	var sendLocalOcx = document.localMailApiForm.sendOcxApi;
	sendLocalOcx.Logon(email,0);
	var iFolder = parseInt(sendLocalOcx.SelectLocalFolder(2));

	if(iFolder==1){
		alert(mailMsg.alert_localmail_error_root);
	}else if(iFolder>1){		
		for(i=0 ; i<fuid.length ; i++){
			if(i == fuid.length - 1){				
				sendLocalOcx.AddWebMessage(email,url[i],mailSubject[i],iFolder,mailSize[i],1);
			}else{				
				sendLocalOcx.AddWebMessage(email,url[i],mailSubject[i],iFolder,mailSize[i],0);
			}
		}
		alert(mailMsg.alert_localmail_move);
	}
	sendLocalOcx.Logoff();		
}