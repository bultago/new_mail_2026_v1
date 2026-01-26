<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false"%>
<html>
<head>
<%@include file="/common/header.jsp"%>

<script type="text/javascript">

function localMailOut(){
	var ocx = document.localM.localmail;
	ocx.Logoff();
}

jQuery().ready(function(){
	setTopMenu('mail');
	makeLocalMailBox("localMailOcx",USEREMAIL,hostInfo,LOCALE);
});
</script>

</head>
<body onunload="localMailOut();">


<%@include file="/common/topmenu.jsp"%>

<div style="clear: both;"></div>

<div class="TM_m_mainBody">

	<div class="TM_localmail_wrapper">
	<form name="localM" method="post">
		<div id="localMailOcx"></div>
		
	
		<script type="text/javascript" for="LocalMailBox" event="OnMessageReply(uid,type)">
								
			var ocx = document.localM.localmail;
			var replytype =type;
			var folderName="Trash";
			var midUid = uid;			
   			var URL = hostInfo+"/dynamic/mail/writeMessage.do?"+
   			"wmode=localpopup&wtype="+replytype+"&folder="+folderName+"&puids="+midUid+"&fwmode=parsed";
   			var settings ="width=740px, height=680px, top=0px, left=0px ,scrollbars=yes, resizable=no";
   			var winObject = window.open("","",settings);
			winObject.document.location = URL;
			
			// Trash mail delete
			
		</script>
		
		<script type="text/javascript" for="LocalMailBox" event="OnGotoHome">
			gotoURL(hostInfo+"/dynamic/mail/mailCommon.do");
		</script>
		
	</form>
	</div>
</div>
</body>
</html>