<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<div style="padding:5px;">
	<a href="javascript:;" onclick="deleteBigAttach()" id="crtBtnSimple" class="btn_basic"><span><tctl:msg bundle="common" key="comn.del"/></span></a>
</div>
<form name="bigAttachForm" id="bigAttachForm">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;table-layout:fixed;">
	<colgroup span="5">
		<col width="30px"></col>
		<col ></col>
		<col width="80px"></col>		
		<col width="100px"></col>		
		<col width="100px"></col>
		<%--<col width="80px"></col>--%>
	</colgroup>
	<tr>
		<th class="clistHeader" ><input type="checkbox" name="allMark" onclick="markAll(this.checked,document.bigAttachForm.fuid)"></th>
		<th class="clistHeader"><tctl:msg key="bigattach.list.001"/></th>
		<th class="clistHeader"><tctl:msg key="bigattach.list.002"/></th>
		<th class="clistHeader"><tctl:msg key="bigattach.list.003"/></th>
		<th class="clistHeader"><tctl:msg key="bigattach.list.004"/></th>
		<%-- <th class="clistHeader" scope="col"><tctl:msg key="bigattach.list.005"/></th>--%>
	</tr>
	
	
	<tr>	
		<td colspan="5" align="center" style="padding:0px;">
		<c:if test="${not empty attList}">
		<div id="bigAttachListWrapper" style="overflow-x:hidden;vertical-align: top;overflow-y:auto; height:150px;" class="default_scroll">
		</c:if>
		<c:if  test="${empty attList}">
		<div id="bigAttachListWrapper" style="vertical-align: top;height:150px;">
		</c:if>
		<table cellpadding="0" cellspacing="0" border="0" class="jq_innerTable" style="width:100%;*width:;table-layout:fixed;">
			<colgroup span="5">
				<col width="30px"></col>
				<col></col>
				<col width="80px"></col>
				<col width="100px"></col>
				<col width="100px"></col>
				<%--<col width="64px"></col>--%>
			</colgroup>
			
			<c:if test="${not empty attList}">		
			<c:forEach var="attFile" items="${attList}">
				<tr class="TM_mailLow">
				<td style='text-align: center;padding:0px 5px 0px 0px;' ><input type="checkbox" name="fuid"  value="${attFile.messageUid}"></td>
				<td>${attFile.fileName}</td>
				<td style='text-align: center;'><tctl:formatSize>${attFile.fileSize}</tctl:formatSize></td>
				<td style='text-align: center;'><tctl:formatDate>${attFile.registTime}</tctl:formatDate></td>
				<td style='text-align: center;'><tctl:formatDate>${attFile.expireTime}</tctl:formatDate></td>
				<%--<td style='text-align: center;'>${attFile.downloadCount}</td>--%>			
				</tr>		
			</c:forEach>
			</c:if>
			<c:if  test="${empty attList}">
			<tr align="center" height="17px">
			<td colspan="5" style="text-align: center">
				<tctl:msg key="bigattach.list.006"/>
			</td>
			</tr>
			</c:if>		
		</table>	
		</div>
		</td>
	</tr>		
</table>
</form>

<script type="text/javascript">
if(isMsie7){
	jQuery("#bigAttachListWrapper").css("padding-right","16px");	
}

if(isMsie6){
    jQuery("#bigAttachListWrapper table").css("width","");
}

function deleteBigAttach(){
	
	if(!chkBoxCheck(document.bigAttachForm.fuid)){
		alert(comMsg.error_noselected);
		return;
	}

	if(!confirm(mailMsg.bigattach_list_010)){
		return;
	}
		
	var formData = Form.serialize($("bigAttachForm"));	
	var url = "/dynamic/mail/deleteBigAttach.do?"+formData;
	jQuery.getJSON(url,function(jObj){
		if(jObj.result == "success"){
			alert(mailMsg.bigattach_list_008);
			updateHugeQuota(Number(jObj.useage));			
		} else {
			alert(mailMsg.bigattach_list_009);
		}		
		ActionBasicLoader.loadAction("bigAttachContents", "/dynamic/mail/listBigAttach.do?dummy="+makeRandom());
	});
	
		
	
}
</script>
