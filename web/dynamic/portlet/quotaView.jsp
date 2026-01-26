<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>

<div class="roundTitle">
	<p>
		<span class="title"><tctl:msg key="mail.quota.info"/></span>		
	</p>
</div>
<div class="portlet_body"> 
<ul class="quota_portlet_body">
	<li class="mail_title"><strong><tctl:msg key="mail.quota.mail"/></strong> (${mailQuota.percent}% <tctl:msg key="comn.use" bundle="common"/>)</li>
	<li class="graph">
		<div class="bar">
			<div class="bar_used" style="width:${mailQuota.percent}%"></div>
		</div>
		<div class="num">
			<span class="num0">0%</span>
			<span class="num50">
				<img src="/design/common/image/blank.gif" width="1" height="5" style="background:#c5c5c5;position:absolute; top:-5px;right:12px">
				50%
			</span>
			<span class="num100">100%</span>
		</div>
	</li>
	<li class="ment"><tctl:msg key="mail.quota"/> <strong class="blue">${mailQuota.usageUnit}</strong>/${mailQuota.limitUnit}</li>
	<li class="dotLine"></li>
	<li class="mail_title"><strong><tctl:msg key="webfolder.quota.info" bundle="webfolder"/></strong> (${webfolderQuota.percent}% <tctl:msg key="comn.use" bundle="common"/>)</li>
	<li class="graph">		
		<div class="bar">
			<div class="bar_used" style="width:${webfolderQuota.percent}%"></div>
		</div>
		<div class="num">
			<span class="num0">0%</span>
			<span class="num50">
				<img src="/design/common/image/blank.gif" width="1" height="5" style="background:#c5c5c5;position:absolute; top:-5px;right:12px">
				50%
			</span>
			<span class="num100">100%</span>
		</div>
	</li>
	<li class="ment"><tctl:msg key="mail.quota"/> <strong class="blue">${webfolderQuota.usageUnit}</strong>/${webfolderQuota.limitUnit}</li>
</ul>
</div>
