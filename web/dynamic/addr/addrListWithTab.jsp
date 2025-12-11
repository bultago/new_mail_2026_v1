<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<input type="hidden" id="groupSeq" value="${fn:escapeXml(groupSeq)}"> 
<input type="hidden" id="leadingPattern" value="${leadingPattern}">

<input type="hidden" id="writeAuth" value="${auth.writeAuth}">
<input type="hidden" id="readAuth" value="${auth.readAuth}">
<input type="hidden" id="creatorAuth" value="${auth.creatorAuth}">
<input type="hidden" id="leadingGroupSeq" value="${fn:escapeXml(groupSeq)}">

<div class="address_title">
<c:forEach var="charater" items="${leadingCharaters}" varStatus="loop">
	<a href="javascript:srchByLeading('${charater}');" class="btn_alphabet <c:if test="${leadingPattern == charater}"> btn_alphabet_on"</c:if>"><span>${charater}</span></a>
</c:forEach>
</div>

<div class='TM_listWrapper'>
<table cellspacing="0" cellpadding="0" class="TM_addr_list_header">	
	<col width="30px"></col>
	<col width="120px" ></col>
	<col></col>
	
	<tr>
		<th scope="col"><input id="memberSeq_all" type="checkbox" onclick="checkAll();"></th>
		<th scope="col">
			<c:if test="${sortBy eq 'name'}">
				<c:if test="${sortDir eq 'asc'}">
					<a href="javascript:;"  onclick="sortAddress('name','desc')" class="sortSelectItem">
					<img src="/design/common/image/icon/ic_bullet_up.gif">
				</c:if>			
				<c:if test="${sortDir eq 'desc'}">
					<a href="javascript:;" onclick="sortAddress('name','asc')" class="sortSelectItem">
					<img src="/design/common/image/icon/ic_bullet_down.gif">
				</c:if>
			</c:if>
			<c:if test="${sortBy ne 'name'}">
				<a href="javascript:;" onclick="sortAddress('name','desc')">
			</c:if>	
			<tctl:msg key="addr.table.header.001" bundle="addr"/></a>
		</th>
		<th scope="col">
			<c:if test="${sortBy eq 'email'}">
				<c:if test="${sortDir eq 'asc'}">
					<a href="javascript:;"  onclick="sortAddress('email','desc')" class="sortSelectItem">
					<img src="/design/common/image/icon/ic_bullet_up.gif">
				</c:if>			
				<c:if test="${sortDir eq 'desc'}">
					<a href="javascript:;" onclick="sortAddress('email','asc')" class="sortSelectItem">
					<img src="/design/common/image/icon/ic_bullet_down.gif">
				</c:if>
			</c:if>
			<c:if test="${sortBy ne 'email'}">
				<a href="javascript:;" onclick="sortAddress('email','desc')">
			</c:if>
			<tctl:msg key="addr.table.header.002" bundle="addr"/></a>
		</th>
	</tr>
</table>
</div>
<div id='a_addrMListWrapper'>
<div id='a_addrMListContent' class='TM_listWrapper'>
<div class="address_list">
	<table cellspacing="0" cellpadding="0" class="TM_addr_list_body">
		<col width="30px"></col>
		<col width="120px" ></col>
		<col></col>
		<c:forEach var="members" items="${members}" varStatus="loop">
		<tr onclick="viewAddress2(${members.addrbookSeq}, ${members.memberSeq});">
		
			<td class="center">
				<input id="memberSeq_${members.memberSeq}" type="checkbox" value="${members.memberSeq}" onclick="rememberMe(${members.memberSeq}, '${fn:escapeXml(members.memberName)}', '${fn:escapeXml(members.memberEmail)}');">
				<input type="hidden" id="memberSeq_${members.memberSeq}_name" value="${fn:escapeXml(members.memberName)}">		
				<input type="hidden" id="memberSeq_${members.memberSeq}_email" value="${fn:escapeXml(members.memberEmail)}">
			</td>
			<td>
				<a onclick="viewAddress2(${members.addrbookSeq}, ${members.memberSeq});" href="javascript:;">${fn:escapeXml(members.memberName)}</a> 
			</td>
			<td>
				<a onclick="sendSingleMail('${fn:escapeXml(members.memberName)}', '${fn:escapeXml(members.memberEmail)}');" href="#">${fn:escapeXml(members.memberEmail)}</a>
			</td>
		</tr>	
		</c:forEach>
	</table>
</div>
</div>
</div>
<c:if test="${!empty members}">
<div id='pageBottomNavi' class='pageNavi'></div>
</c:if>

<script type="text/javascript">

jQuery(".address_list :checkbox").each(function(i){
	if(idMap.get(jQuery(this).attr("id")) != null){
		
		jQuery("#"+jQuery(this).attr("id")).parent().parent().addClass("TM_checkLow");
		jQuery("#"+jQuery(this).attr("id")).attr("checked",true);
	}
});

var currentPage = ${currentPage};
var pageBase = ${pageBase};
var totalMessage = ${total};

jQuery().ready(function(){	
	/*menuBar.setPageNavi("p",
			{total:totalMessage,
		base:pageBase,
		page:currentPage,
		isListSet:false,
		isLineCntSet:true,
		pagebase:USER_PAGEBASE,
		changeAfter:reloadListPage});*/

	menuBar.setPageNaviBottom("p",
			{total:totalMessage,
		base:pageBase,
		page:currentPage,
		isListSet:false,
		isLineCntSet:true,
		pagebase:USER_PAGEBASE,
		changeAfter:reloadListPage});

	setCurrentPage(currentPage);

	if(jQuery('#bookSeq').val() !=""){
		loadSharedToolBar();
	}
	menuBar.toggleSideItem(true);
	setMenuBarStatus();

	jQuery(window).resizeInnerFrame({resizeId:"#a_addrMListWrapper", 
		mainId:"#a_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:false,
		isMainHeight:true,
		extHeight:83});
	jQuery(window).resizeInnerFrame({resizeId:"#a_addrMListContent", 
		mainId:"#a_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		notCheckTrigger:false,
		isMainHeight:true,
		extHeight:83});	
	jQuery(window).trigger("resize");
});

</script>