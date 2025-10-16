<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
		<script type="text/javascript" src="/hybrid/calendar/calendar.js"></script>
		<script type="text/javascript">			
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href=\"javascript:toggleCalendarType();\" class='btn_dr'><tctl:msg key="scheduler.asset.001" bundle="scheduler"/>"
			+"</a>";				

		}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/hybrid/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<div class="title_box" style="text-align:center;">
					<div class="btn_l">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					</div>
				</div>
				<div>
					<p class="warn" style="margin:10px;text-align:center;"><strong><tctl:msg key="scheduler.asset.033" bundle="scheduler"/></strong></p>	
				</div>
			</div>
			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>