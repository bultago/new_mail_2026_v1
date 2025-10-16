<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@include file="/common/authHeader.jsp"%>
<div class="TM_content_wrapper">	
	<div class="TM_mail_content">
		
		<table cellpadding="0" cellspacing="0" border="0" width="100%" class="TM_r_table">
			<col width="80px"></col>
			<col></col>			
						
			<tr>
			<td colspan="2" class="TM_r_subject">
				<span>${mdnBean.messageTitle}</span>								
			</td>						
			</tr>
			<tr>
			<td class="TM_rh_index">				
				<tctl:msg key="mail.senddate" /> :				
			</td>
			<td class='TM_rh_content' valign="top">
				${mdnBean.sendDate}
			</td>						
			</tr>
			
			<tr>
			<td class="TM_rh_index">				
				<tctl:msg key="mail.to" /> :				
			</td>
			<td class='TM_rh_content' valign="top">
				<tctl:msg key="mail.mdn.total"  arg0="${mdnBean.countTotal}"/>
			</td>						
			</tr>
			
			<tr>			
			<td colspan="2" class='TM_rh_content'>
				<div class="TM_info_panel">
			  		<strong><tctl:msg key="mail.mdn.read" /></strong> : ${mdnBean.countRead} &nbsp;
			  		<strong><tctl:msg key="mail.mdn.unread" /></strong> :  ${mdnBean.countUnseen}&nbsp;
			  		<strong><tctl:msg key="mail.mdn.fail" /></strong> :  ${mdnBean.countFail}&nbsp;
			  		<strong><tctl:msg key="mail.mdn.recall" /></strong> :  ${mdnBean.countRecall}&nbsp;
			  		<strong><tctl:msg key="mail.mdn.etc" /></strong> :  ${mdnBean.countEtc}&nbsp;
			  		</div>	
			  	
			</td>						
			</tr>
			<tr><td colspan="2" class='TM_rh_content'><span style="color:#FF7272">(<tctl:msg key="mail.mdn.info"/>)</span></td></tr>
		</table>
		
	
		
	<form name="mdnForm"  id="mdnForm">
	<input type="hidden" id="messageId" value="${mdnBean.messageID}">
	<input type="hidden" id="uid" value="${uid}">
	<table cellspacing="0" cellpadding="0" class="TM_b_list">
		<col width="40px"></col>		
		<col></col>
		<col width="150px"></col>
		<col width="250px"></col>
		
		<tr>
		<th scope="col"><input type="checkbox"  id="allChk" onclick="allChkEmail(this)"></th>
		<th scope="col"><tctl:msg key="mail.to" /></th>	
		<th scope="col"><tctl:msg key="mail.sendstatus" /></th>
		<th scope="col"><tctl:msg key="mail.mdn.msg" /></th>
		</tr>
		
		<c:forEach var="mdnContent" items="${mdnBean.rcptVos}" varStatus="loop">
		<tr class="TM_mailLow">
			<td>
				<c:choose>
				<c:when test="${mdnContent.localDomain && 
								(mdnContent.code eq '200' || 
								mdnContent.code eq '201' || 
								mdnContent.code eq '300')}">
					<input type="checkbox" name="recallEmail" value="${mdnContent.address}" onclick="chkEmail(this)"/>
				</c:when>
				<c:otherwise>
					<input type="checkbox" disabled />
				</c:otherwise>
				</c:choose>
					
			</td>
			<td class="TM_list_cut" title="${fn:escapeXml(mdnContent.printAddress)}" >${fn:escapeXml(mdnContent.printAddress)}</td>
			<td><span class="TM_MDNStatus"><tctl:msg key="${mdnContent.status}" /></span></td>
			<td>
			<c:if test="${mdnContent.code eq '1' || mdnContent.code eq '1000'}">
				${mdnContent.message}
			</c:if>
			<c:if test="${not empty mdnContent.message && mdnContent.code ne '1' && mdnContent.code ne '1000'}">				
				<tctl:msg key="${mdnContent.message}" />
			</c:if>
			
			</td>
		</tr>
		</c:forEach>
	</table>
	</form>
	
	</div>
	<c:if test="${!empty mdnBean.rcptVos}">
		<div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
	</c:if>	
</div>

<script type="text/javascript">
var chkIdx = 0;
chkHash = new Hash();

function allChkEmail(thisObj){
	var isChk = thisObj.checked;
	var f = document.mdnForm;
	var chkList = f.recallEmail;
	if(chkList){
		if(chkList.length > 1){
			for ( var i = 0; i < chkList.length; i++) {
				if(chkList[i].checked != isChk){
					chkList[i].checked = isChk;
					chkEmail(chkList[i]);
				}
			}
		} else {
			if(chkList.checked != isChk){
				chkList.checked = isChk;
				chkEmail(chkList);
			}			
		}
	}	
}

function recallMsg(){
	if(chkIdx == 0){
		alert(comMsg.comn_error_001);
		return;
	}
	var vals = chkHash.keys();
	var rmailStr = "";

	for ( var i = 0; i < vals.length; i++) {		
		if(i > 0){
			rmailStr += "|";
		}
		rmailStr += vals[i];				
	}

	if (!confirm(mailMsg.mail_mdn_confirm)) {
		return;
	}

	var param = {};
	param.mid = $("messageId").value;
	param.uid = $("uid").value;
	param.rmailStr = rmailStr;
		
	mailControl.recallMessage(param);
	
}

function chkEmail(chkObj){
	var isChk = chkObj.checked;
	var chkVal = chkObj.value;
	if(isChk){
		chkHash.set(chkVal,chkVal);
		chkIdx++;
	} else {
		chkHash.unset(chkVal);
		chkIdx--;
	}
}


function loadMdnReadPage(){
	var total = ${mdnBean.mdnResponseTotal};
	var page = ${mdnBean.mdnResponsePage};
	var pagebase = ${pageBase};
		
	loadMDNViewToolBar();
	
	menuBar.setPageNaviBottom("p",
		{total:total,
		base:pagebase,
		page:page,
		isListSet:true,
		isLineCntSet:true,
		pagebase:USER_PAGEBASE,
		changeAfter:reloadListPage});
	
	setCurrentPage(page);
	
}

loadMdnReadPage();




</script>