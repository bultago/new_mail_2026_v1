<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
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
			<span><tctl:msg key="error.msg.title" bundle="common"/></span>			
		</div>
		<div class="TM_errorContentWrapper">
			<div class="TM_errorContentMain">				
				<tctl:msg key="error404.001" bundle="common"/>
			</div>
			<div class="TM_errorContentSub">
				<tctl:msg key="error404.002" bundle="common"/><br>
				<tctl:msg key="error404.003" bundle="common"/>
			</div>
		</div>						
		<div class="btnArea">
			<a class="btn_style3" href="javascript:;" onclick="backPage();"><span><tctl:msg key="error.msg.004" bundle="common"/></span></a>
		</div>		
	</div>
</div>
</body>
</html>