<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
* {margin: 0; padding: 0;}
body {
color:#555555;
font-family: "MS PGothic","Osaka", "Sans-serif";
font-size : 12px;		
background-color:#EEFDFF;
}
.TM_errorBox {
    margin: 100px auto;
    width: 500px;
}
.popup_style2 {
    background: none repeat scroll 0 0 #FFFFFF;
    border-color: #7EAFD8 #5D90BB #5D90BB #7EAFD8;
    border-style: solid;
    border-width: 1px;
}
.popup_style2 .title {
    background-color: #E8F1F8;
    border-bottom: 1px solid #9EBEED;
    height: 21px;
    line-height: 23px;
    padding-left: 16px;
    position: relative;
}
.popup_style2 .title span {
    color: #000000;
    font-weight: bold;
}
.TM_errorContentWrapper {
    padding: 5px;
    text-align: center;
}
.TM_errorContentMain {
    color: #000000;
    font-size: 14px;
    font-weight: bold;
    padding: 30px 0 0 10px;
}
.TM_errorContentSub {
    line-height: 14px;
    margin-top: 10px;
}
</style>
<script type="text/javascript">
function backPage(){
	history.back();	
}
</script>

</head>
<body>
<div style="clear: both;"></div>
<div id="m_mainBody">	
	<div class="popup_style2 TM_errorBox">		
		<div class="title">
			<span><font color="blue">&gt;</font> <tctl:msg key="error.msg.title" bundle="common"/></span>			
		</div>
		<div class="TM_errorContentWrapper">
			<div class="TM_errorContentMain">
				<tctl:msg key="error403.001" bundle="common"/>
			</div>
			<div class="TM_errorContentSub">
				<tctl:msg key="error403.002" bundle="common"/><br>
				<tctl:msg key="error403.003" bundle="common"/>
			</div>
			<div style="text-align:center;padding-top:10px;">
				<a href="javascript:;" onclick="backPage();"><span><tctl:msg key="error.msg.004" bundle="common"/></span></a>
			</div>
		</div>							
	</div>
</div>
</body>
</html>