
function makePoweruploadOcx(id,locale){
	
}

function makeNormalUploadOcx(id,locale){
		
}

function makeWebToLocalOcx(id, locale){
	locale = (locale == "jp")?"en":locale;
	var ocxStr ='<object '
	 +'ID="LocalMailAPI" '
	 +'name="sendOcxApi" '
	 +'classid="clsid:65D100C9-E47C-4C0E-8197-A99EF3765B06" '
	 +'CODEBASE="'+hostInfo+'/ocxcab/TWebMail.cab#version=1,0,0,5" '
	// +'CODEBASE="TWebMail.cab#version=1,0,0,1" '
	 +'width="0" height="0"> '
	 +'<param name="moduletype" value="api"> '
	 +'<param name="lang" value="'+locale+'"> '
	+'</object>';
	
	$(id).innerHTML = ocxStr;
}

function makeLocalMailBox(id, email,hostInfo,locale){
	locale = (locale == "jp")?"en":locale;
	var ocxStr = '<object '
   +'ID="LocalMailBox" '
   +'name="localmail" '
   +'classid="clsid:65D100C9-E47C-4C0E-8197-A99EF3765B06" '
   +'CODEBASE="'+hostInfo+'/ocxcab/TWebMail.cab#version=1,0,0,5" '
  // +'CODEBASE="./TWebMail.cab#version=1,0,0,1" '
   +'width="100%" height="700"> '
   +'<param name="moduletype" value="ctrl"> '
   +'<param name="cmdbar" value="toolbar"> '
   +'<param name="account" value="'+email+'"> '
   +'<param name="webfolder" value="'+hostInfo+'/mail/localToFolderList.do"> '
   +'<param name="movemessage" value="'+hostInfo+'/mail/moveLocalMessage.do"> '   
   +'<param name="sendurl" value="'+hostInfo+'/mail/writeForLocalMessage.do">'
   +'<param name="lang" value="'+locale+'"> '
   +'</object> ';	
	
   $(id).innerHTML = ocxStr;
   
}

function makeMessageUploadOcx(id,locale){
		
}
