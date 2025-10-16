<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>

<div style="width:100%">
	<div class="downloadOutlookBody">
		<div class="downloadOutlookNotice">
			<span><tctl:msg key="scheduler.outlook.module.download.notice" bundle="scheduler"/></span>
		</div>
		<div class="downloadOutlookButtonWrap">
			<div onclick="downloadOutlook()">
				<tctl:msg key="scheduler.outlook.module.download" bundle="scheduler"/>
			</div>
		</div>
		<div style="width:100%;">
			<div class="outlookExTitle"><font color="#135EC4">1.</font> <tctl:msg key="scheduler.outlook.desc.ex1" bundle="scheduler"/></div>
			<div class="outlookExWrap"><img src="/design/common/image/outlook_ex1.jpg"/></div>
			<div class="outlookExTitle"><font color="#135EC4">2.</font> <tctl:msg key="scheduler.outlook.desc.ex2" bundle="scheduler"/></div>
			<div class="outlookExWrap"><img src="/design/common/image/outlook_ex2.jpg"/></div>
			<div class="outlookExTitle"><font color="#135EC4">3.</font> <tctl:msg key="scheduler.outlook.desc.ex3" bundle="scheduler"/></div>
			<div class="outlookExWrap" style="margin-bottom:0px;">
				<div><img src="/design/common/image/outlook_ex3_1.jpg"/></div>
				<div><img src="/design/common/image/outlook_ex3_2.png"/></div>
				<div><img src="/design/common/image/outlook_ex3_3.jpg"/></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
setSchedulerInfo('<tctl:msg key="scheduler.outlook.title" bundle="scheduler"/>');
makeOutlookTabMenu('<tctl:msg key="scheduler.outlook.desc" bundle="scheduler"/>');
</script>