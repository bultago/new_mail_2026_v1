<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		
		<script type="text/javascript">			
			function submit() {
				document.forms[0].submit();
			}
			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href='javascript:toggleCalendarType();' class='btn_dr'><tctl:msg key="scheduler.delete" bundle="scheduler"/>"
				+"</a>";				

			}
		</script>
	</head>
	<body>
		<div class="wrapper">		
			
			<%@include file="/hybrid/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<form action="/hybrid/calendar/deleteCalendar.do">
					<input type="hidden" name="date" value="${fn:escapeXml(date)}"/>
					<input type="hidden" name="schedulerId" value="${schedulerId}"/>
					<input type="hidden" name="repeatFlag" value="${repeatFlag}"/>
				
				<div class="mail_error">
					<p class="icon_x"><span>x</span></p>
					<h5>
						<c:if test="${repeatFlag ne 'on'}">
						<tctl:msg key="scheduler.alert.schedule.delete" bundle="scheduler"/>
						</c:if>
						<c:if test="${repeatFlag eq 'on'}">
						<tctl:msg key="scheduler.repeat.delete.confirm" bundle="scheduler"/>
						</c:if>
					</h5>
					<p class="scrip">
						<c:if test="${repeatFlag eq 'on'}">
							<label><input type="radio" name="deleteType" value="only" checked="checked"/> <tctl:msg key="scheduler.repeat.delete.only" bundle="scheduler"/></label><br/>
							<label><input type="radio" name="deleteType" value="all"/> <tctl:msg key="scheduler.repeat.delete.all" bundle="scheduler"/></label>
						</c:if>
					</p>
					<p class="btn_area">
						<a class="btn2" href="javascript:submit();"><span><tctl:msg key="comn.confirm" bundle="common"/></span></a>
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.cancel" bundle="common"/></span></a>
					</p>
				</div>
				</form>
			</div>

			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>