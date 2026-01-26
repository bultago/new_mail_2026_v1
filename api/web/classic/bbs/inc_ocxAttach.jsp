<script type="text/javascript">

function ocx_file()
{
	var ocx = document.uploadForm.powerupload;
	ocx.AttachFile();
}

function ocx_init()
{
	var ocx = document.uploadForm.powerupload;
	$("ocx_normal_size").innerHTML = printSize(0);
	ocx.SetAttachMaxSize("NORMAL", 1024 * 1024 * MAX_ATTACH_SIZE);
	ocx.SetAttachMaxCount("NORMAL", MAX_ATTACH_COUNT);
	ocx.SetUrlData( "UPLOAD", hostInfo + "/file/uploadAttachFile.do");
	ocx.SetUrlData( "UPFIELD", "theFile");

}

function ocx_upload()
{
	var ocx = document.uploadForm.powerupload;
	ocx.SetUploadParam( "type", "ocx");
	ocx.SetUploadParam( "writeFile", "true");
	ocx.SetUploadParam( "upldtype", "upld");
	ocx.SetUploadParam( "attfile", "true");
	ocx.SetUploadParam( "userId", "mailadm");
	ocx.SetUploadParam( "queryValue", "Successful");
	ocx.SetUploadParam( "text", "text");
	ocx.SetUploadParam( "uploadType", "power");
	return ocx.UploadFiles();
}

function ocx_info()
{
	return "";
	
}

</script>
<div id="ocxCompL"></div>
<%if(isMsie){ %>
<script language=javascript for="TPOWERUPLOAD" event="OnFileAttached(key, idx)">

	var ocx = document.uploadForm.powerupload;
	document.getElementById("ocx_normal_size").innerHTML = 
		printSize(ocx.GetAttachedSize("NORMAL"));
</script>

<script language=javascript for="TPOWERUPLOAD" event="OnFileUploaded(key, idx)">

	var ocx = document.uploadForm.powerupload;
	document.getElementById("ocx_normal_size").innerHTML = 
		printSize(ocx.GetAttachedSize("NORMAL"));
</script>
 
<script language=javascript for="TPOWERUPLOAD" event="OnFileDeleted(key, idx)">

	var ocx = document.uploadForm.powerupload;
	document.getElementById("ocx_normal_size").innerHTML = 
		printSize(ocx.GetAttachedSize("NORMAL"));
</script>

<script language=javascript for="TPOWERUPLOAD" event="OnChAttachMethod(key, idx)">

	var ocx = document.uploadForm.powerupload;
	document.getElementById("ocx_normal_size").innerHTML = 
		printSize(ocx.GetAttachedSize("NORMAL"));
</script>
<%} %>
<%--
 <span id="bigattachMessageSpan"  style='font-size:12px;color:#666666; text-align: left; '>* 
(<bean:message key="bigattach.07" arg0="<%= BIGATTACH_EXPIRE %>" 
arg1="<%= BIGATTACH_DOWNCNT %>" />)</span>
 --%>