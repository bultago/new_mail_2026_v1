<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="/design/mobile/css/calendar_blue.css" />
		<script type="text/javascript" src="/i18n?bundle=scheduler&var=schedulerMsg&locale=<%=locale%>"></script>
		<script type="text/javascript" src="/mobile/basic/calendar/calendar.js"></script>
		<script type="text/javascript">
			function init() {
				useShare();
				useAsset();

				<c:if test="${!empty calendarInfoVo.assetList}">
					var assetVal = "";
					<c:forEach var="asset" items="${calendarInfoVo.assetList}" varStatus="loop">
						jQuery("#asset_${asset.categorySeq}").val("${asset.assetSeq}");
						<c:if test="${loop.first}">
							jQuery("#contect").val("${asset.contect}");
							jQuery("#assetEmptyResult").show();
						</c:if>
						if (assetVal != "") {
							assetVal += "_";
						}
						assetVal += "${asset.assetSeq}";
						isCheckAsset = true;
					</c:forEach>
					jQuery("#assetReserveValue").val(assetVal);
				</c:if>

				<c:forEach var="self" items="${calendarInfoVo.share.selfTargets}" varStatus="loop">
					attachSelfList(${loop.index}, "${self}");
				</c:forEach>
			}

			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				<c:if test="${calendarInfoVo.schedulerId > 0}">
				topLink.innerHTML = "<a href='javascript:toggleCalendarType();' class='btn_dr'><tctl:msg key="scheduler.title.modify" bundle="scheduler"/>"
				</c:if>
				<c:if test="${empty calendarInfoVo || calendarInfoVo.schedulerId == 0}">
				topLink.innerHTML = "<a href='javascript:toggleCalendarType();' class='btn_dr'><tctl:msg key="scheduler.title.insert" bundle="scheduler"/>"
				</c:if>
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
				<input type="hidden" id="schedulerId" name="schedulerId" value="${calendarInfoVo.schedulerId}"/>
				<input type="hidden" id="type" name="type" value="${fn:escapeXml(calendarWriteVo.type)}"/>
				<input type="hidden" id="thisDate" name="thisDate" value="${fn:escapeXml(calendarWriteVo.thisdate)}"/>
				<input type="hidden" id="currentTime" name="currentTime" value="${fn:escapeXml(calendarWriteVo.current)}"/>
				<input type="hidden" id="startDate" name="startDate" value="${fn:escapeXml(calendarInfoVo.startDate)}"/>
				<input type="hidden" id="endDate" name="endDate" value="${fn:escapeXml(calendarInfoVo.endDate)}"/>
				<input type="hidden" name="repeatFlag" value="${fn:escapeXml(calendarInfoVo.repeatFlag)}"/>
				<input type="hidden" name="repeatTerm" value="${fn:escapeXml(calendarInfoVo.repeatTerm)}"/>
				<input type="hidden" name="repeatEndDate" value="${fn:escapeXml(calendarInfoVo.repeatEndDate)}"/>
				<input type="hidden" id="ampm" name="ampm"/>
				<input type="hidden" name="listType" value="${fn:escapeXml(listType)}"/>
				
				<div class="title_box">
					<div class="btn_l">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					</div>
					<div class="btn_r">
						<a class="btn2" href="javascript:saveCalendar()"><span><tctl:msg key="scheduler.save" bundle="scheduler"/></span></a>
					</div>
				</div>
				<div class="mail_write_wrap">
					<%if("ko".equals(locale)){%>
					<fmt:setLocale scope="request" value="ko_KR"/>
					<%} else if("jp".equals(locale)){%>
					<fmt:setLocale scope="request" value="ja_JP"/>
					<%} else {%>
					<fmt:setLocale scope="request" value="en_US"/>
					<%}%>
					<c:if test="${(empty calendarInfoVo) || (!empty calendarInfoVo && calendarWriteVo.type eq 'only')}">
						<c:set var="sdate" value="${fn:escapeXml(calendarWriteVo.sdate)}"/>
						<c:set var="edate" value="${fn:escapeXml(calendarWriteVo.edate)}"/>
						<c:set var="stime" value="${fn:escapeXml(calendarWriteVo.stime)}"/>
						<c:set var="etime" value="${fn:escapeXml(calendarWriteVo.etime)}"/>
						<input type="hidden" name="ignoreTime" value="${fn:escapeXml(calendarWriteVo.sdate)}"/>
					</c:if>
					<c:if test="${!empty calendarInfoVo && calendarWriteVo.type ne 'only'}">
						<c:set var="sdate" value="${fn:escapeXml(fn:substring(calendarInfoVo.startDate,0,8))}"/>
						<c:set var="edate" value="${fn:escapeXml(fn:substring(calendarInfoVo.endDate,0,8))}"/>
						<c:set var="stime" value="${fn:escapeXml(fn:substring(calendarInfoVo.startDate,8,12))}"/>
						<c:set var="etime" value="${fn:escapeXml(fn:substring(calendarInfoVo.endDate,8,12))}"/>
					</c:if>
					<c:set var="sdateFormat" value="${fn:substring(sdate,0,4)}-${fn:substring(sdate,4,6)}-${fn:substring(sdate,6,8)}"/>
					<c:set var="edateFormat" value="${fn:substring(edate,0,4)}-${fn:substring(edate,4,6)}-${fn:substring(edate,6,8)}"/>
					<fmt:parseDate var="stimeFormat" pattern="HHmm" value="${stime}"/>
					<fmt:parseDate var="etimeFormat" pattern="HHmm" value="${etime}"/>
					<table class="reserv_info">
						<tr>
							<th><tctl:msg key="scheduler.search.subject" bundle="scheduler"/></th>
							<td><input type="text" id="title" name="title" class="it_full" value="${calendarInfoVo.title}"/></td>
						</tr>
						<tr>
							<th></th>
							<td>
								<c:if test="${(calendarInfoVo.schedulerId > 0) && (!empty calendarInfoVo.assetList)}">
									<p class="warn"><tctl:msg key="scheduler.asset.028" bundle="scheduler"/></p>
								</c:if>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
							<td>
								<table class="tb_date">
									<tr>
										<td>
											<a id="sdate_link" href="javascript:selectDate('start');" class="selDate">${sdateFormat}</a>
											<input type="hidden" id="inputStartDate" name="inputStartDate" value="${sdate}"/>
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
											<a id="edate_link" href="javascript:selectDate('end');" class="selDate">${edateFormat}</a>
											<input type="hidden" id="inputEndDate" name="inputEndDate" value="${edate}"/>
											<a id="etime_link" href="javascript:selectTime('end');" class="selTime"><fmt:formatDate value="${etimeFormat}" pattern="a hh:mm"/></a>
											<input type="hidden" id="inputEndTime" name="inputEndTime" value="${etime}"/>
										</td>
									</tr>
								</table>
								<p class="allright"><label><input type="checkbox" id="allDay" name="allDay" onclick="resetAssetTest()" <c:if test="${calendarInfoVo.allDay eq 'on'}">checked="checked"</c:if>/> <tctl:msg key="scheduler.allday" bundle="scheduler"/></label></p>
							</td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.location" bundle="scheduler"/></th>
							<td><input type="text" id="location" name="location" class="it_full" value="${calendarInfoVo.location}"/></td>
						</tr>
						<tr>
							<th><tctl:msg key="scheduler.search.content" bundle="scheduler"/></th>
							<td><textarea id="content" name="content" rows="6" cols="" class="tx_full">${calendarInfoVo.content}</textarea></td>
						</tr>
						<tr>
							<th></th>
							<td><label><input type="checkbox" id="checkShare" name="checkShare" onclick="useShare()" <c:if test="${!empty calendarInfoVo.share}">checked="checked"</c:if>/> <tctl:msg key="scheduler.share.check.info" bundle="scheduler"/></label></td>
						</tr>
						<tr id="shareWrap" style="display:none;">
							<th><tctl:msg key="scheduler.share.title" bundle="scheduler"/></th>
							<td>
								<select id="shareValue" name="shareValue" class="st_full">
									<option value=""><tctl:msg key="scheduler.share.select" bundle="scheduler"/></option>
									<c:if test="${!empty calendarWriteVo.shareList}">
									<c:forEach var="share" items="${calendarWriteVo.shareList}">
										<option value="${share.shareSeq}" <c:if test="${share.shareSeq eq calendarInfoVo.share.shareValue}">selected</c:if> >${fn:escapeXml(share.shareName)}</option>
									</c:forEach>
									</c:if>
								</select>
								<p class="allright"><label><input type="checkbox" id="checkSelfTarget" name="checkSelfTarget" onclick="useSelfShare()" <c:if test="${!empty calendarInfoVo.share.selfTargets}">checked="checked"</c:if> /> <tctl:msg key="scheduler.share.self.target" bundle="scheduler"/></label></p>
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
						<tr <c:if test="${empty calendarWriteVo.assetList}">style="display:none;"</c:if>>
							<th><tctl:msg key="scheduler.asset.001" bundle="scheduler"/></th>
							<td><label><input type="checkbox" id="checkAsset" name="checkAsset" onclick="useAsset()" <c:if test="${!empty calendarInfoVo.assetList}">checked="checked"</c:if>/> <tctl:msg key="scheduler.asset.012" bundle="scheduler"/></label></td>
						</tr>
						<tr id="assetWrap" style="display:none;">
							<th></th>
							<td>
								<c:if test="${!empty calendarWriteVo.assetList}">
								<table class="tb_date">
								<c:forEach var="asset" items="${calendarWriteVo.assetList}" varStatus="loop">
									<c:if test="${!loop.first && categorySeq ne asset.categorySeq}">
										</select></td></tr>
									</c:if>
									<c:if test="${categorySeq ne asset.categorySeq}">
										<tr>
											<td style="padding:3px 0px">
												${asset.categoryName}
											</td>
										</tr>
										<tr>
										<td>
										<select id="asset_${asset.categorySeq}" name="asset_${asset.categorySeq}" class="st_full ast" onchange="resetAssetTest();">
										<option value=""><tctl:msg key="scheduler.asset.029" bundle="scheduler"/></option>
									</c:if>
										<option value="${asset.assetSeq}">${asset.assetName}</option>
										<c:set var="categorySeq" value="${asset.categorySeq}"/>
								</c:forEach>
								</table>
								<input type="hidden" id="assetReserveValue" name="assetReserveValue"/>
								</c:if>
								<p class="allright"><a class="btn4 reserv_search" href="javascript:searchSchedulerAsset()"><span><tctl:msg key="scheduler.asset.030" bundle="scheduler"/></span></a></p>
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
						<tr height="20"><th></th><td></td></tr>
					</table>
				</div>				
				</form>
				<div class="title_box title_box_bottom">
					<div class="btn_l">
						<a class="btn2" href="javascript:history.back(-1)"><span><tctl:msg key="comn.prelist" bundle="common"/></span></a>
					</div>
					<div class="btn_r">
						<a class="btn2" href="javascript:saveCalendar()"><span><tctl:msg key="scheduler.save" bundle="scheduler"/></span></a>
					</div>
				</div>
			</div>
			
			
			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
		<%@include file="/mobile/basic/calendar/calendarBottom.jsp"%>
		<script type="text/javascript">init();</script>
		<iframe name="hidden_frame" id="reqFrame" src="about:blank" frameborder="0" width="0" height="0" style="display:none;"></iframe>
	</body>
</html>