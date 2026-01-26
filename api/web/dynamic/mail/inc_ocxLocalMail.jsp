<script type="text/javascript" for="LocalMailAPI" event="OnDownloadMessage(itotal)">    
	var sendLocalOcx = document.localMailApiForm.sendOcxApi;
	var i,j=0;	
	var uids = new Array();
	var folders = new Array();	
	for (i = 0 ; i < itotal ; i++){
		var uid = sendLocalOcx.GetDownloadedWebMsgData("UID",i);
		var down = sendLocalOcx.GetDownloadedWebMsgData("DOWNLOAD",uid);

		var url,indexfolder,indexuid,folder,uid,foldername,uidnum;
		if(down.toLowerCase() == 'true'){			
			url = sendLocalOcx.GetDownloadedWebMsgData("MSGURL",uid);
			indexfolder = url.indexOf("?");
			indexuid = url.indexOf("&");
			folder = url.substring(indexfolder+1, indexuid);
			uid = url.substring(indexuid+1);
			foldername = folder.substring(folder.indexOf("=")+1);
			foldername = decodeURIComponent(foldername);
			uidnum = uid.substring(uid.indexOf("=")+1);
			uids[uids.length] = uidnum;
			folders[folders.length] = foldername;			
		}
	}		
	mailControl.cleanMessages(uids,folders);	
</script>

<form name="localMailApiForm" id="localMailApiForm">
<div id="ocxLocalMailApi" style="width:1px;height:1px;padding:0px;margin:0px"></div>
<script type="text/javascript">
	makeWebToLocalOcx("ocxLocalMailApi",LOCALE);
</script>
</form>