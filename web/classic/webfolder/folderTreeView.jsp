<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/header.jsp" %>
<script type="text/javascript">
notIntervalCheckLoad = false;
</script>
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/webfolder_style.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/webfolder_tree_style.css" />
<script type="text/javascript" src="/classic/webfolder/webFolderTree.js"></script>
<c:set var="User['MAIL_UID']" value="jhlee"/>
<script type="text/javascript">
	
function treeRender(treeId, type){
	var infoObj = new Object();
	
	infoObj.imageFolder = '/design/common/image/icon/';	// Path to images
	infoObj.folderOpenImage = 'ic_cfolder.gif';
	infoObj.folderCloseImage = 'ic_cfolder.gif';
	infoObj.folderROOTImage = 'icon_folder_base.gif';
	infoObj.folderLoadingImage = 'icon_loading_s.gif';
	infoObj.folderSOpenImage = 'icon_folder_share_close.gif';
	infoObj.folderSCloseImage = 'icon_folder_share_close.gif';
	
	infoObj.plusImage = 'icon_folder_plus.gif';
	infoObj.minusImage = 'icon_folder_minus.gif';
	infoObj.defaultUserId = '${User["MAIL_UID"]}';
	
	infoObj.linkMethod = 'selectFolder';
	infoObj.treeId = treeId;
	
	initTree(infoObj);
}

function selectFolder(folder,node,nodeNum,type){

	if(!parent.getListLoaded()){
		return;
	}

	var userSeq = "";
	var param = "";
	if (folder.indexOf("|") > -1){
		var vals = folder.split("\|");
		userSeq = vals[0];				
		param = vals[1].substring(14);
	}else {
		param = folder.substring(14);
	}
		
	parent.setTargetFolder(type,param,nodeNum,userSeq);	
	}

</script>


<body leftmargin="15" marginwidth="15" topmargin="0" marginheight="0" nowrap>
<table class='TM_HiddenTextTable'>
	<tr>
		<td style="border:0;">
			<div class='TM_HiddenTextDiv'>
			
				<c:set var="folderCount" value="0"/>
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.title" bundle="webfolder"/></div>
				</div>
				<ul id="user_folder_tree" class="folderTree" style="margin-bottom:20px;">
					<c:forEach var="fdata" items="${userFolderList}" varStatus="loop">
					<c:if test="${fdata.privil == 'W'}">
					<li id="user_node_${fdata.nodeNumber}">
						<a href="javascript:selectFolder('${fdata.realPath}','user_link_${fdata.nodeNumber}','${fdata.nodeNumber}','user');" id="user_link_${fdata.nodeNumber}" type="user" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="user_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					</c:if>
					</c:forEach>
				</ul>
				
				<c:set var="folderCount" value="0"/>
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.share.title" bundle="webfolder"/></div>
				</div>
				<ul id="share_folder_tree" class="folderTree" style="margin-bottom:20px;padding-top:4px;">
					<c:forEach var="fdata" items="${shareFolderList}" varStatus="loop">
					<c:if test="${fdata.privil == 'W'}">
					<li id="share_node_${fdata.nodeNumber}">
						<a href="javascript:selectFolder('${fdata.realPath}','share_link_${fdata.nodeNumber}','${fdata.nodeNumber}','share');" id="share_link_${fdata.nodeNumber}" type="share" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="share_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					<c:set var="folderCount" value="${folderCount+1}"/>
					</c:if>
					</c:forEach>
					<c:if test="${folderCount == 0}">
						<div style="padding:5px;"><tctl:msg key="share.nowrite" bundle="webfolder"/></div>
					</c:if>
				</ul>
				
				<c:set var="folderCount" value="0"/>
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.public.title" bundle="webfolder"/></div>
				</div>
				<ul id="public_folder_tree" class="folderTree">
					<c:forEach var="fdata" items="${publicFolderList}" varStatus="loop">
					<c:if test="${fdata.privil == 'W'}">
					<li id="public_node_${fdata.nodeNumber}">
						<a href="javascript:selectFolder('${fdata.realPath}','public_link_${fdata.nodeNumber}','${fdata.nodeNumber}','public');" id="public_link_${fdata.nodeNumber}" type="public" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="public_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					</c:if>
					</c:forEach>
				</ul>
			</div>
		</td>
	</tr>
</table>
<script language="javascript">
	treeRender('user_folder_tree', 'user');
	treeRender('share_folder_tree', 'share');
	treeRender('public_folder_tree', 'public');
</script>
</body>

</html>
