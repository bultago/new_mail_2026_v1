<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/simpleHeader.jsp" %>
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
	
	infoObj.linkMethod = 'viewFolder';
	infoObj.treeId = treeId;
	
	initTree(infoObj);
	if(type == 'share'){
		parent.setRootArray(sFolderRoots);
	}
}
function viewFolder(folder,node,nodeNum,type){
	//alert(parent.getListLoaded());
	if(!parent.getListLoaded()){
		return;
	}
	
	var userSeq = "";
	var param = "";
	if (folder.indexOf("|") > -1){
		var vals = folder.split("\|");
		userSeq = vals[0];				
		param = vals[1].substring(14);
	} else {
		param = folder.substring(14);
	}

	var paramArray = [];
	paramArray.push({name:"path", value:Base64.encode(param)});
	paramArray.push({name:"type", value:type});
	paramArray.push({name:"userSeq", value:userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(nodeNum)});
	
	if(type == "share"){
		var sharedRoot = parent.getROOT(node);
		paramArray.push({name:"sroot", value:Base64.encode(sharedRoot)});
	}

	parent.checkOldNode(node);		
	parent.checkCurrentNode(node);

	parent.resetSearch();

	var url = "/webfolder/listFolders.do";
	
	parent.fraContent.executeUrl(url, paramArray);
}

var sFolderRoots = new Array();
function setSFolderRoot(root){
	sFolderRoots[sFolderRoots.length] = root;		
}
function viewFolder1(folder,node,nodeNum){
	return;
}
	
</script>
</head>
<body id="treebody" nowrap>
<div class="TM_w_treeBody">
<table class='TM_HiddenTextTable'>
	<tr>
		<td style="border:0;">
			<div class='TM_HiddenTextDiv'>
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.title" bundle="webfolder"/></div>
				</div>
				<ul id="user_folder_tree" class="folderTree" style="margin-bottom:20px;">
					<c:forEach var="fdata" items="${userFolderList}" varStatus="loop">
					<li id="user_node_${fdata.nodeNumber}">
						<a href="javascript:viewFolder('${fdata.realPath}','user_link_${fdata.nodeNumber}','${fdata.nodeNumber}','user');" id="user_link_${fdata.nodeNumber}" type="user" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="user_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					</c:forEach>
				</ul>
				
				<div style="height:1px;clear: both;"><div class="TM_ml_line"></div></div>
				
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.share.title" bundle="webfolder"/></div>
				</div>
				<ul id="share_folder_tree" class="folderTree" style="margin-bottom:20px;padding-top:4px;">
					<c:forEach var="fdata" items="${shareFolderList}" varStatus="loop">
					<li id="share_node_${fdata.nodeNumber}">
						<a href="javascript:viewFolder('${fdata.realPath}','share_link_${fdata.nodeNumber}','${fdata.nodeNumber}','share');" id="share_link_${fdata.nodeNumber}" type="share" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="share_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					<script>setSFolderRoot('${fdata.shareRoot}');</script>
					</c:forEach>
					<c:if test="${empty shareFolderList}">
						<div style="padding:5px;"><tctl:msg key="share.none" bundle="webfolder"/></div>
					</c:if>
				</ul>
				
				<div style="height:1px;clear: both;"><div class="TM_ml_line"></div></div>
				
				<div class="webfolderTitleWrap" style="font-weight: bold;">
					<div class="webfolderTitle"><tctl:msg key="webfolder.public.title" bundle="webfolder"/></div>
				</div>
				<ul id="public_folder_tree" class="folderTree">
					<c:forEach var="fdata" items="${publicFolderList}" varStatus="loop">
					<li id="public_node_${fdata.nodeNumber}">
						<a href="javascript:viewFolder('${fdata.realPath}','public_link_${fdata.nodeNumber}','${fdata.nodeNumber}','public');" id="public_link_${fdata.nodeNumber}" type="public" name="${fdata.path}" onclick="setEvent(this)">			
						${fdata.folderName}</a>
						<c:if test="${fdata.child eq true}">
						<ul id="public_tree_node_${fdata.nodeNumber}"></ul>
						</c:if>
					</li>
					</c:forEach>
					<c:if test="${empty publicFolderList}">
						<tctl:msg key="public.none" bundle="webfolder"/>
					</c:if>
				</ul>
			</div>
		</td>
	</tr>
</table>
</div>
<script language="javascript">
	treeRender('user_folder_tree', 'user');
	treeRender('share_folder_tree', 'share');
	treeRender('public_folder_tree', 'public');
	parent.loadList();
</script>
</body>
</html>