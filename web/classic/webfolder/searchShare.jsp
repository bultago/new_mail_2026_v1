<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

	<form name="searchShareForm">
	<input type="hidden" name="currentPage" value="${pm.page}">
	<input type="hidden" id="presearchtype" value="${searchType}">
	<input type="hidden" id="prekeyword" value="${keyWord}">
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#ffffff" style="padding-top:5px;">
				<div style="float:left">
				<div id="shareSearchTypeSelect"></div>
				<script type="text/javascript">
					var shareSearchTypeArray = [];
					shareSearchTypeArray.push({index:'<tctl:msg key="folder.userid" bundle="webfolder" />',value:"uid"});
					shareSearchTypeArray.push({index:'<tctl:msg key="folder.username" bundle="webfolder" />',value:"name"});
				</script>
				</div>
				<div style="float:left;padding-left:1px;">
					<input type="text" id="shareKeyWord" name="shareKeyWord" value="${keyWord}" class="IP200" style="width:220px;" onKeyPress="(keyEvent(event) == 13) ? searchShareFolder() : '';">
					<input type="text" name="_tmp" style="display:none"/>
				</div>
				<div style="float:left;padding-left:1px;">
					<ul>
						<li style="margin-top:0px;"><a href="#" onclick="searchShareFolder()" class="btn_style4"><span><tctl:msg key="folder.search" bundle="webfolder"/></span></a></li>
					</ul>
				</div>
			</td>
		</tr>
	</table>
	<table id="webfolderListTable" cellpadding="0" cellspacing="0" style="border:0px;border-top:#C6C6C6 solid 1px;margin-top:5px;">
		<colgroup span="5">
			<col width="30px"></col>
			<col width="130px"></col>
			<col width="130px"></col>
			<col></col>
			<col width="100px"></col>
		</colgroup>
		<tr>
			<th>
				<input type="checkbox" onclick="checkAllTdLine(searchShareForm.fuid, this.checked);" name="shareAll"/>
			</th>
			<th><tctl:msg key="folder.userid" bundle="webfolder" /></th>
			<th><tctl:msg key="folder.username" bundle="webfolder" /></th>
			<th><tctl:msg key="folder.sharename" bundle="webfolder" /></th>
			<th><tctl:msg key="folder.auth" bundle="webfolder" /></th>
		</tr>
		<c:if test="${empty shareList}">
		<tr>
			<td colspan="5" class="webfolderTd filename"><tctl:msg key="folder.nosearch" bundle="webfolder" /></td>
		</tr>
		</c:if>
		<c:forEach var="share" items="${shareList}">
		<tr id="tr_${share.fuid}" style="<c:if test="${share.alreadyShare}">background-color:#EEFDFF</c:if>">
			<td class="webfolderTd"><c:if test="${!share.alreadyShare}"><input type="checkbox" name="fuid" id="fuid" value="${share.fuid}" onclick="checkTdLine(this)"></c:if></td>
			<td class="webfolderTd">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;" title="${share.uid}">
							<div class='TM_HiddenTextDiv'>${share.uid}</div>
						</td>
					</tr>
				</table>
			</td>
			<td class="webfolderTd">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;" title="${share.name}">
							<div class='TM_HiddenTextDiv'>${share.name}</div>
						</td>
					</tr>
				</table>
			</td>
			<td class="webfolderTd filename">
				<table class='TM_HiddenTextTable'>
					<tr>
						<td style="border:0;" title="${share.folderPath}">
							<div class='TM_HiddenTextDiv'>${share.folderPath}</div>
						</td>
					</tr>
				</table>
			</td>
			<td class="webfolderTd">
				<c:if test="${share.auth == 'R'}"><tctl:msg key="folder.read"	bundle="webfolder"/></c:if>
				<c:if test="${share.auth == 'W'}"><tctl:msg key="folder.write"	bundle="webfolder" /></c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
	<br>
	<div id="pageCounter" class="pageNum">
		<%@include file="/common/pageCounter.jsp" %>
	</div>
	</form>
	<script language="javascript">
		var type = "";
		<c:if test="${searchType == 'uid'}">
			type = "uid";
		</c:if>
		<c:if test="${searchType == 'name'}">
			type = "name";
		</c:if>
		jQuery("#shareSearchTypeSelect").selectbox({selectId:"shareSearchType",width:75}, type, shareSearchTypeArray);
	</script>