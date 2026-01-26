<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	String locale = request.getParameter("locale");
	locale = StringEscapeUtils.escapeHtml(locale);
	
	String base = request.getParameter("base");
	base = StringEscapeUtils.escapeHtml(base);
%>

<html>
<head>
<script type="text/javascript" src="/js/ext-lib/noclick.js"></script>
<link rel="stylesheet" type="text/css" href="/design/common/css/bbs_<%=locale%>.css" />
<script type="text/javascript" src="/js/core-lib/jquery-1.4.2.js"></script>
<script type="text/javascript">
	var base="<%=base%>";
	var resizeDone = false;
	var resizeCnt = 0;
	var interval;

	function setContent(){
		document.write(parent.getMessageText());
	}
    
    function resizeInit(){
       resize();
        /*if(base!='writePreview'){
			parent.receiveInfoModeCheck();//receiveinfo open
		}*/
    }
	function resize(){		
		if (resizeDone || resizeCnt > 10) {
			clearInterval(interval);
			document.getElementById("contentBox").style.paddingBottom = "20px";
		}
				
		var height = jQuery('#contentTC').height();        
        height = (height > 350)?height:350;     
        var width = jQuery("#contentT").width();
        if (parent.resizeTextFrame) {
			parent.resizeTextFrame(height,width);
        }
		resizeCnt++;	
	}
	
	function timsCheck(){		
		if (!resizeDone) {
			interval = setInterval(resize,1000);
		}
	}

	function init(){
		resizeDone = true;
	}
	 
</script>
</head>

<body onload="init();">
<div id="contentBox" style="width: 97%">
    <table cellpadding="0" cellspacing="0" border="0" id="contentT" style="font-size:12px;">
        <tr>
            <td id="contentTC">
                <div>
	<script>
		setContent();
		resizeInit();
		timsCheck();
	</script>
</div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
