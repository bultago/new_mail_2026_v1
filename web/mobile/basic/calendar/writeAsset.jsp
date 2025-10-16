<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
		<script type="text/javascript" src="/mobile/basic/calendar/calendar.js"></script>
		<script type="text/javascript">			
		function makeTopLink(){
			var topLink = document.getElementById("mailTopLink");
			topLink.innerHTML = "<a href=\"javascript:toggleCalendarType();\" class='btn_dr'><tctl:msg key="scheduler.asset.001" bundle="scheduler"/>"
			+"</a>";				

		}
		</script>
	</head>
	<body>
		<div class="wrapper" id="bodyWrapper">		
			
			<%@include file="/mobile/basic/calendar/calendar_body_top.jsp"%>
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<form name="writeForm" method="post" action="/mobile/calendar/saveCalendar.do">
				<input type="hidden" id="thisDate" name="thisDate" value="${calendarWriteVo.thisdate}"/>
				<input type="hidden" id="currentTime" name="currentTime" value="${calendarWriteVo.current}"/>
				<input type="hidden" id="startDate" name="startDate"/>
				<input type="hidden" id="endDate" name="endDate"/>
				<input type="hidden" id="ampm" name="ampm"/>
				<div class="title_box">
					<div class="btn_l">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					</div>
					<div class="btn_r">
						<a class="btn2" href="javascript:saveCalendar()"><span><tctl:msg key="scheduler.save" bundle="scheduler"/></span></a>
					</div>
				</div>
				<div>
					<input type="checkbox" id="checkAsset" name="checkAsset" checked="checked" style="display:none"/>
					<table class="reserv_info">
						<tr id="assetWrap">
							<th><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></th>
							<td>
								<c:if test="${!empty calendarWriteVo.assetList}">
								<table class="tb_date">
								<c:forEach var="asset" items="${calendarWriteVo.assetList}" varStatus="loop">
									<c:if test="${!loop.first && categorySeq ne asset.categorySeq}">
										</select></td></tr>
									</c:if>
									<c:if test="${categorySeq ne asset.categorySeq}">
										<tr>
											<td style="padding:3px 0px;">
												${asset.categoryName}
											</td>
										</tr>
										<tr>
										<td style="padding-bottom:3px;">
										<select id="asset_${asset.categorySeq}" name="asset_${asset.categorySeq}" class="st_full ast" onchange="resetAssetTest();">
										<option value=""><tctl:msg key="scheduler.asset.029" bundle="scheduler"/></option>
									</c:if>
										<option value="${asset.assetSeq}">${asset.assetName}</option>
										<c:set var="categorySeq" value="${asset.categorySeq}"/>
								</c:forEach>
								</table>
								<input type="hidden" id="assetReserveValue" name="assetReserveValue"/>
								</c:if>								
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
							<td>
								<table class="tb_date">
									<tr>
										<td>
											<c:set var="sdate" value="${calendarWriteVo.sdate}"/>
											<c:set var="stime" value="${calendarWriteVo.stime}"/>
											<fmt:parseDate var="stimeFormat" pattern="HHmm" value="${stime}"/>
											<a id="sdate_link" href="javascript:selectDate('start');" class="selDate">${fn:substring(sdate,0,4)}-${fn:substring(sdate,4,6)}-${fn:substring(sdate,6,8)}</a>
											<input type="hidden" id="inputStartDate" name="inputStartDate" value="${calendarWriteVo.sdate}"/>
											<a id="stime_link" href="javascript:selectTime('start');" class="selTime"><fmt:formatDate value="${stimeFormat}" pattern="a hh:mm"/></a><br/>
											<input type="hidden" id="inputStartTime" name="inputStartTime" value="${stime}"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<th></th>
							<td>
								<table class="tb_date">
									<tr>
										<td>
											<c:set var="edate" value="${calendarWriteVo.edate}"/>
											<c:set var="etime" value="${calendarWriteVo.etime}"/>
											<fmt:parseDate var="etimeFormat" pattern="HHmm" value="${etime}"/>
											<a id="edate_link" href="javascript:selectDate('end');" class="selDate">${fn:substring(edate,0,4)}-${fn:substring(edate,4,6)}-${fn:substring(edate,6,8)}</a>
											<input type="hidden" id="inputEndDate" name="inputEndDate" value="${calendarWriteVo.edate}"/>
											<a id="etime_link" href="javascript:selectTime('end');" class="selTime"><fmt:formatDate value="${etimeFormat}" pattern="a hh:mm"/></a>
											<input type="hidden" id="inputEndTime" name="inputEndTime" value="${etime}"/>
										</td>
									</tr>
								</table>
								<p class="allright"><label><input type="checkbox" id="allDay" name="allDay" onclick="resetAssetTest()"/> <tctl:msg key="scheduler.allday" bundle="scheduler"/></label></p>
								<p class="allright"><a class="btn4 reserv_search" href="javascript:searchSchedulerAsset()"><span><tctl:msg key="scheduler.asset.030" bundle="scheduler"/></span></a></p>
								<br/>
							</td>
						</tr>
						<tr id="assetEmptyMsg" style="display:none;">
							<th></th>
							<td>
								<p class="warn">
									<strong><tctl:msg key="scheduler.asset.026" bundle="scheduler"/></strong><br/>
									<span><tctl:msg key="scheduler.asset.031" bundle="scheduler"/><span>
								</span></span></p>
							</td>
						</tr>
						<tr id="assetEmptyResult" style="display:none;">
							<th><tctl:msg key="scheduler.asset.006" bundle="scheduler"/></th>
							<td><input type="text" id="contect" name="contect" class="it_full"/></td>
						</tr>
						<tr id="assetHaveMsg" style="display:none;">
							<th></th>
							<td>
								<p class="warn">
									<strong><tctl:msg key="scheduler.asset.025" bundle="scheduler"/></strong><br/>
									<span><tctl:msg key="scheduler.asset.032" bundle="scheduler"/></span>
								</p>
							</td>
						</tr>
						<tr id="assetHaveResult" style="display:none;">
							<th></th>
							<td id="existList"></td>
						</tr>
					</table>
					<table class="reserv_info" id="schedulerContents" style="display:none;">
						<tr>
							<th><tctl:msg key="scheduler.search.subject" bundle="scheduler"/></th>
							<td><input type="text" id="title" name="title" class="it_full"/></td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.location" bundle="scheduler"/></th>
							<td><input type="text" id="location" name="location" class="it_full"/></td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.search.content" bundle="scheduler"/></th>
							<td><textarea id="content" name="content" rows="6" cols="" class="tx_full"></textarea></td>
						</tr>
						<tr>
							<th></th>
							<td style="padding-bottom:5px;"><label><input type="checkbox" id="checkShare" name="checkShare" onclick="useShare()"/> <tctl:msg key="scheduler.share.check.info" bundle="scheduler"/></label></td>
						</tr>
						<tr id="shareWrap" style="display:none;">
							<th><tctl:msg key="scheduler.share.title" bundle="scheduler"/></th>
							<td>
								<select id="shareValue" name="shareValue" class="st_full">
									<option value=""><tctl:msg key="scheduler.share.select" bundle="scheduler"/></option>
									<c:if test="${!empty calendarWriteVo.shareList}">
									<c:forEach var="share" items="${calendarWriteVo.shareList}">
										<option value="${share.shareSeq}">${fn:escapeXml(share.shareName)}</option>
									</c:forEach>
									</c:if>
								</select>
								<p class="allright"><label><input type="checkbox" id="checkSelfTarget" name="checkSelfTarget" onclick="useSelfShare()"/> <tctl:msg key="scheduler.share.self.target" bundle="scheduler"/></label></p>
								<p class="allright"><label><input type="checkbox" id="sendMail" name="sendMail" checked="checked"/> <tctl:msg key="scheduler.share.sendmail.001" bundle="scheduler"/></label></p>
							</td>
						</tr>
						<tr id="selfShareWrap" style="display:none;">
							<th><tctl:msg key="scheduler.share.self.target" bundle="scheduler"/></th>
							<td>
								<table class="tb_direct_mail">
									<tr>
										<td><input type="text" name="mailUid" class="it_full"/></td>
										<td class="tb_blank">@</td>
										<td><input type="text" name="mailDomain" class="it_full"/></td>
										<td width="52" align="right"><a class="btn3" href="javascript:addSelfTarget()"><span><tctl:msg key="comn.add" bundle="common"/></span></a></td>
									</tr>
								</table>
								<ul id="selfTargetList" class="direct_list"></ul>
							</td>
						</tr>
					</table>
				</div>
				
				<div class="title_box">
					<div class="btn_l">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					</div>
					<div class="btn_r">
						<a class="btn2" href="javascript:saveCalendar()"><span><tctl:msg key="scheduler.save" bundle="scheduler"/></span></a>
					</div>
				</div>
				</form>
			</div>
			
			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
		<%@include file="/mobile/basic/calendar/calendarBottom.jsp"%>
		<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>
	</body>
</html>