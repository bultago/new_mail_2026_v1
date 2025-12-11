<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<div class="TM_listWrapper">
	<table cellspacing="0" cellpadding="0" class="TM_member_list_header">
		<col width="30px"></col>
		<col width="100px" ></col>
		<col width="100px"></col>
		<col width="100px"></col>
		<col width="100px"></col>
		<col width="120px"></col>
		<col width="120px"></col>
		<col></col>
		
		<tr>
			<th scope="col"><input id="memberSeq_all" type="checkbox" onclick="checkAll();"></th>
			<th scope="col">
				<c:if test="${sortBy eq 'dept'}">
					<c:if test="${sortDir eq 'asc'}">
						<a href="javascript:;"  onclick="sortAddress('dept','desc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_up.gif">
					</c:if>			
					<c:if test="${sortDir eq 'desc'}">
						<a href="javascript:;" onclick="sortAddress('dept','asc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_down.gif">
					</c:if>
				</c:if>
				<c:if test="${sortBy ne 'dept'}">
					<a href="javascript:;" onclick="sortAddress('dept','desc')">
				</c:if>	
				<tctl:msg key="addr.table.header.013" bundle="addr"/>
			</th>
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
				<c:if test="${sortBy eq 'title'}">
					<c:if test="${sortDir eq 'asc'}">
						<a href="javascript:;"  onclick="sortAddress('title','desc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_up.gif">
					</c:if>			
					<c:if test="${sortDir eq 'desc'}">
						<a href="javascript:;" onclick="sortAddress('title','asc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_down.gif">
					</c:if>
				</c:if>
				<c:if test="${sortBy ne 'title'}">
					<a href="javascript:;" onclick="sortAddress('title','desc')">
				</c:if>	
				<tctl:msg key="addr.table.header.011" bundle="addr"/></a>
			</th>
			<th scope="col">
				<c:if test="${sortBy eq 'class'}">
					<c:if test="${sortDir eq 'asc'}">
						<a href="javascript:;"  onclick="sortAddress('class','desc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_up.gif">
					</c:if>			
					<c:if test="${sortDir eq 'desc'}">
						<a href="javascript:;" onclick="sortAddress('class','asc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_down.gif">
					</c:if>
				</c:if>
				<c:if test="${sortBy ne 'class'}">
					<a href="javascript:;" onclick="sortAddress('class','desc')">
				</c:if>	
				<tctl:msg key="addr.table.header.012" bundle="addr"/></a>
			</th>
			<th scope="col">
				<c:if test="${sortBy eq 'tel'}">
					<c:if test="${sortDir eq 'asc'}">
						<a href="javascript:;"  onclick="sortAddress('tel','desc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_up.gif">
					</c:if>			
					<c:if test="${sortDir eq 'desc'}">
						<a href="javascript:;" onclick="sortAddress('tel','asc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_down.gif">
					</c:if>
				</c:if>
				<c:if test="${sortBy ne 'tel'}">
					<a href="javascript:;" onclick="sortAddress('tel','desc')">
				</c:if>	
				<tctl:msg key="addr.table.header.005" bundle="addr"/></a>
			</th>
			<th scope="col">
				<c:if test="${sortBy eq 'mobile'}">
					<c:if test="${sortDir eq 'asc'}">
						<a href="javascript:;"  onclick="sortAddress('mobile','desc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_up.gif">
					</c:if>			
					<c:if test="${sortDir eq 'desc'}">
						<a href="javascript:;" onclick="sortAddress('mobile','asc')" class="sortSelectItem">
						<img src="/design/common/image/icon/ic_bullet_down.gif">
					</c:if>
				</c:if>
				<c:if test="${sortBy ne 'mobile'}">
					<a href="javascript:;" onclick="sortAddress('mobile','desc')">
				</c:if>	
				<tctl:msg key="addr.table.header.003" bundle="addr"/></a>
			</th>
			<th scope="col" >
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
				<tctl:msg key="register.021" bundle="common"/></a>
			</th>
		</tr>
	</table>
</div>
<div id='o_memberListWrapper'>
<div id='o_memberListContent' class='TM_listWrapper'>
<div class="member_list">	
	<table cellspacing="0" cellpadding="0" class="TM_member_list_body">
		<col width="30px"></col>
		<col width="100px" ></col>
		<col width="100px"></col>
		<col width="100px"></col>
		<col width="100px"></col>
		<col width="120px"></col>
		<col width="120px"></col>
		<col></col>
		<c:forEach var="members" items="${members}" varStatus="loop">
		<tr>
		
			<td class="center">
				<input id="memberSeq_${members.mailUserSeq}_${members.orgCode}" type="checkbox" value="${members.orgCode}|${members.mailUserSeq}" 
					onclick="selectEmail('memberSeq_${members.mailUserSeq}_${members.orgCode}');">
				
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_seq" value="${members.mailUserSeq}"/>
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_name" value="${members.memberName}"/>		
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_title" value="${members.titleName}"/>
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_dept" value="${members.deptName}"/>
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_orgCode" value="${members.orgCode}"/>
				<input type="hidden" id="memberSeq_${members.mailUserSeq}_${members.orgCode}_email" value="${members.memberEmail}"/>
			</td>
			<td>
				<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
				<tbody>
				<tr>
					<td class='treeNodeWrapperContents'>
					${members.deptName}&nbsp;
					</td>
				</tr>
				</tbody>
				</table>
			</td>
			<td>
				<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
				<tbody>
				<tr>
					<td class='treeNodeWrapperContents'>
					<a id="orgMember_${loop.count}" onmouseout="popClose()" onmouseover="memeberPicPopUp('orgMember_${loop.count}','${members.mailUserSeq}')"  onclick="viewAddress(${members.mailUserSeq},'${members.orgCode}');" href="javascript:;">${members.memberName}</a>&nbsp;
					</td>
				</tr>
				</tbody>
				</table>
			</td>
			<td>
				<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
				<tbody>
				<tr>
					<td class='treeNodeWrapperContents'>
					${members.titleName} &nbsp;
					</td>
				</tr>
				</tbody>
				</table>
			</td>
			<td>
				<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
				<tbody>
				<tr>
					<td class='treeNodeWrapperContents'>
					${members.className}&nbsp;
					</td>
				</tr>
				</tbody>
				</table>
				  
			</td>
			<td class="center">
				${members.officeTel}&nbsp;
			</td>
			<td class="center">
				${members.mobileNo} &nbsp;
			</td>
			<td>
				<table cellspacing='0' cellpadding='0' border='0' class='treeNodeWrapperTable'>
				<tbody>
				<tr>
					<td class='treeNodeWrapperContents'>
					<a onclick="sendSingleMail('${members.memberName}', '${members.memberEmail}');" href="javascript:;">${members.mailUid}</a>&nbsp;
					</td>
				</tr>
				</tbody>
				</table>
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

jQuery(".member_list :checkbox").each(function(i){
	
	if(idMap.get("#"+jQuery(this).attr("id")) != null){
		jQuery("#"+jQuery(this).attr("id")).parent().parent().addClass("TM_checkLow");
		jQuery("#"+jQuery(this).attr("id")).attr("checked",true);
	}
});

var currentPage = ${currentPage};
var pageBase = ${pageBase};
var totalMessage = ${total};

function reloadListPage(){
	movePage(currentPage);
}

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
	
	menuBar.toggleSideItem(true);

	jQuery(window).resizeInnerFrame({resizeId:"#o_memberListWrapper", 
		mainId:"#o_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		wrapperMode:true,
		notCheckTrigger:true,
		isMainHeight:true, 
		extHeight:58});
	jQuery(window).resizeInnerFrame({resizeId:"#o_memberListContent", 
		mainId:"#o_contentMain", 
		sideObjId:["#copyRight"], 
		isNoneWidthChk:true,
		notCheckTrigger:true,
		isMainHeight:true,
		extHeight:28});
	
	jQuery(window).trigger("resize");
});
</script>