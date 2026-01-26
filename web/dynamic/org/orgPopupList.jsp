<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<form name="orgForm">
<input type="hidden" id="orgCode" name="orgCode" value="${orgCode}">
<table cellpadding="0" cellspacing="0" class="TB_addSearchFrom">
	<tr>
		<td style="padding: 0px 5px">
			<c:if test="${!empty titleCodeList}">
				<div class="floatLeft">
					<input type="checkbox" name="titleInclude" id="titleInclude">
					<tctl:msg key="org.info.label.002" bundle="addr"/>
				</div>
				<div class="floatLeft">				
					<div id="titleCodeSelect"></div>
					<script type="text/javascript">
					var titleCodeArray = [];								
					<c:forEach var="titleCode" items="${titleCodeList}">
					titleCodeArray.push({index:"${fn:escapeXml(titleCode.codeName)}",value:"${fn:escapeXml(titleCode.code)}"});
					</c:forEach>
					jQuery("#titleCodeSelect").selectbox({selectId:"titleCode",selectFunc:"",width:150},"",titleCodeArray);		
					</script>
				</div>
				<div class="floatLeft">
					<input type="checkbox" name="titleSubAll" id="titleSubAll">
					<tctl:msg key="org.info.label.004" bundle="addr"/>
				</div>
			</c:if>
		</td>
	</tr>
	<tr>
		<td style="padding: 0px 5px">
			<c:if test="${!empty classCodeList}">
				<div class="floatLeft">
					<input type="checkbox" name="classInclude" id="classInclude">
					<tctl:msg key="org.info.label.003" bundle="addr"/> 
				</div>
				<div class="floatLeft">	
					<div id="classCodeSelect"></div>
					<script type="text/javascript">
					var classCodeArray = [];								
					<c:forEach var="classCode" items="${classCodeList}">
					classCodeArray.push({index:"${fn:escapeXml(classCode.codeName)}",value:"${fn:escapeXml(classCode.code)}"});
					</c:forEach>
					jQuery("#classCodeSelect").selectbox({selectId:"classCode",selectFunc:"",width:150},"",classCodeArray);		
					</script>
				</div>
				<div class="floatLeft">
					<input type="checkbox" name="classSubAll" id="classSubAll">
					<tctl:msg key="org.info.label.004" bundle="addr"/>
				</div>
			</c:if>
		</td>
	</tr>
	<tr>
		<td style="padding: 0px 5px">
			<div class="floatLeft">
				<input type="checkbox" name="currentAll" id="currentAll">
				<tctl:msg key="org.info.label.005" bundle="addr"/>
			</div>
			<div class="floatLeft">
				<input type="checkbox" name="currentSubAll" id="currentSubAll">
				<tctl:msg key="org.info.label.004" bundle="addr"/>
			</div>
		</td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" class="TB_addList">
	<colgroup>
		<col width="30px" />
		<col width="100px" />
		<col width="" />
		<col width="80px" />
	</colgroup>
	<tr>
		<th class="checkbox"><input id="memberSeq_all" type="checkbox" onclick="checkAll(this, orgForm.sel)"></th>
		<th class="orgname"><tctl:msg key="addr.table.header.001" bundle="addr"/></th>
		<th class="email" style="width:;"><tctl:msg key="addr.table.header.002" bundle="addr"/></th>
		<th class="nextname" style="width:60px;"><tctl:msg key="addr.table.header.011" bundle="addr"/></th>
	</tr>
	<c:forEach var="members" items="${members}" varStatus="loop">
	<tr>
		<td class="lineRight" align="center">
			<input id="memberSeq_${members.mailUserSeq}" type="checkbox" name="sel" value="${members.memberName}|${members.memberEmail}">		
		</td>
		<td class="lineRight">
			${members.memberName} 
		</td>
		<td class="lineRight">
			<table class='TM_HiddenTextTable'>
				<tr>
					<td style="border:0;" title="${members.memberEmail} ">
						<div class='TM_HiddenTextDiv'>${members.memberEmail} </div>
					</td>
				</tr>
			</table>
		</td>
		<td class="lineRight">
			<c:if test="${empty members.titleName}">&nbsp;</c:if>
			${members.titleName}			 
		</td>
	</tr>	
	</c:forEach>
</table>
<table border="0" width="100%" cellspacing="1">
	<tr>
		<td align="center">
			<div id="pageCounter" class="pageNum">
				<c:if test="${!pm.firstWindow}">
					<a href="javascript:moveOrgPage('${pm.prevWindow}')" class="first"><img src="/design/default/image/btn/btn_tofirst.gif" align="absmiddle" border="0"></a>
				</c:if>
				
				<c:if test="${!pm.firstPage}">
					<a href="javascript:moveOrgPage('${pm.prevPage}')" class="prev"><img src="/design/default/image/btn/btn_toprev.gif" align="absmiddle" border="0"></a>
				</c:if>
				
				<c:forEach var="page" items="${pm.pages}">
					<c:if test="${page == pm.page}">
						<span class="choiseNum">${page}</span>
					</c:if>
					
					<c:if test="${page != pm.page}">
						<a href="javascript:moveOrgPage('${page}')" style="padding:1px 5px">${page}</a>
					</c:if>
				</c:forEach>
				
				<c:if test="${!pm.lastPage}">
					<a href="javascript:moveOrgPage('${pm.nextPage}')" class="next"><img src="/design/default/image/btn/btn_tonext.gif" align="absmiddle" border="0"></a>
				</c:if>
				
				<c:if test="${!pm.lastWindow}">
					<a href="javascript:moveOrgPage('${pm.nextWindow}')" class="end"><img src="/design/default/image/btn/btn_tolast.gif" align="absmiddle" border="0"></a>
				</c:if>
			</div>
		</td>
	</tr>
</table>
</form>

<script language="javascript">
var orgCode = jQuery("#orgCode").val();
if (orgCode == "") {
	jQuery(".TB_addSearchFrom").hide();
}
</script>