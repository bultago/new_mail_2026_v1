<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>

<table id="search_table" cellpadding="0" cellspacing="0">
	<colgroup span="2">          		
        <col></col>
		<col width="300px"></col>
	</colgroup>
	<tr>
		<th class="dateTd"><tctl:msg key="scheduler.search.subject" bundle="scheduler"/></th>
		<th class="dateTd"><tctl:msg key="scheduler.term" bundle="scheduler"/></th>
	</tr>
</table>

<table id="search_share_table" cellpadding="0" cellspacing="0" style="display:none">
	<colgroup span="2">          		
        <col></col>
		<col width="300px"></col>
	</colgroup>
	<tr>
		<th colspan="2" class="dateTd"><tctl:msg key="scheduler.share.schedule" bundle="scheduler"/></th>
	</tr>
</table>
<script type="text/javascript">
setSchedulerTab('3');
jQuery("#currentType").val("");
setSchedulerInfo('<tctl:msg key="scheduler.title" bundle="scheduler"/> \> <tctl:msg key="scheduler.search" bundle="scheduler"/>');
var tabMenuData = {result:'<tctl:msg key="scheduler.search.result" bundle="scheduler"/>', 
				   resultall:'<tctl:msg key="scheduler.search.keyword.empty" bundle="scheduler"/>'};
makeSearchTabMenu(tabMenuData);
</script>