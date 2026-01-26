<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<script type="text/javascript">
			function goBack(){history.back();}
		</script>
	</head>
	<body>
	<div class="wrapper">
		<div class="header">
			<h1>Terrace Mail Suite</h1>
			<div class="ts">
				<div class="btn_l"></div>
				<div class="btn_r"><a href="/hybrid/common/home.do" class="btn0"><span><tctl:msg key="comn.top.mobilehome" bundle="common"/></span></a></div>
			</div>
			<div class="hh"><h2><tctl:msg key="error.title" bundle="common"/></h2></div>
		</div>
		<div class="container">
			<div id="title_top_box" class="title_box">
				<div class="btn_l">&nbsp;</div>				
			</div>			
			<div class="mail_error">
				<div class="error_icon"></div>
				<h5><tctl:msg key="error.title" bundle="common"/></h5>
				<p class="scrip">
					<tctl:msg key="error.default" bundle="common"/>
				</p>
				<p class="btn_area">
					<a href="javascript:goBack();" class="btn2"><span><tctl:msg key="mail.goback"/></span></a>
				</p>
			</div>
		</div>
	</div>
	<%@include file="/hybrid/common/footer.jsp"%>
</body>
</html>