<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">			
			function submit() {
				document.forms[0].submit();
			}
			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href='javascript:toggleCalendarType();' class='btn_dr'><tctl:msg key="scheduler.title.modify" bundle="scheduler"/>"
				+"</a>";				

			}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/mobile/basic/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<form action="/mobile/calendar/modifyCalendar.do">
					<input type="hidden" name="date" value="${fn:escapeXml(date)}"/>
					<input type="hidden" name="schedulerId" value="${schedulerId}"/>					
				
				<div class="mail_ok">
					<p class="ok"><span>o</span></p>
					<h5 style="margin-bottom:10px;">
						<c:if test="${repeatFlag ne 'on'}">
						<tctl:msg key="scheduler.alert.schedule.modify" bundle="scheduler"/>
						</c:if>
						<c:if test="${repeatFlag eq 'on'}">
						<tctl:msg key="scheduler.repeat.modify.confirm" bundle="scheduler"/>
						</c:if>
					</h5>
					<p class="scrip">
						<c:if test="${repeatFlag eq 'on'}">
							<label><input type="radio" name="type" value="only" checked="checked"/> <tctl:msg key="scheduler.repeat.modify.only" bundle="scheduler"/></label><br/>
							<label><input type="radio" name="type" value="all"/> <tctl:msg key="scheduler.repeat.modify.all" bundle="scheduler"/></label>
						</c:if>
					</p>
					<p class="btn_area">
						<a class="btn2" href="javascript:submit();"><span><tctl:msg key="comn.confirm" bundle="common"/></span></a>
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.cancel" bundle="common"/></span></a>
					</p>
				</div>
				</form>
			</div>

			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>