<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%
	String path = (String)request.getAttribute("path");
	String currentPath = new String(path);
	
	if (path.length() > 0) {
		if (!path.substring(0, 1).equals("/"))
			path = "/" + path;
	} else {
		path = "/" + path;
	}
	path = path.replaceAll("\\.", "\\/");
	String xpath = (path.equals("/")) ? "" : path;
	String ppath = path;
	try {
		ppath = path.substring(0, path.lastIndexOf("/"));
		//System.out.println("------- RREPATH[" + ppath + "]");
	} catch (Exception e) {
	}
	String sync = request.getParameter("sync");
	String targetpath = (String)request.getAttribute("targetpath");
%>
<html>
<head>
<%@ include file="/common/simpleHeader.jsp" %>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/webfolder_style.css"/>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.swfupload.js"></script>
<script type="text/javascript" src="/js/swfupload-lib/swfupload.js"></script>
<script type="text/javascript" src="/js/ext-lib/html5FileUploader.js"></script>
<style type="text/css">	
body {
	background: #ffffff;
	min-height:0px;
	min-width:0px;
	margin: 0px;
	padding: 0px;
	width: 100%;	
}
html,body {
    scrollbar-face-color: #E2E9FC; 
	scrollbar-shadow-color: #; 
	scrollbar-highlight-color: #FFFFFF; 
	scrollbar-3dlight-color: #DBEBFE; 
	scrollbar-darkshadow-color:#CCCCCE; 
	scrollbar-track-color: #FFFFFF; 
	scrollbar-arrow-color: #4D6088;
}
</style>
<script type="text/javascript" src="/i18n?bundle=webfolder&var=webfolderMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&var=mailMsg&locale=<%=locale%>"></script>

<SCRIPT TYPE="text/javascript">
var basicAttachUploadControl;
var MAX_ATTACH_SIZE = "${attachMaxSize}";
var locale = LOCALE;
var isOcxUpload = false;
var isOcxUploadDownModule = false;

var folderType = "${type}";
var shareSeq = "${shareSeq}";
var shareId = "${shareId}";

function shareFolderDialog(mode) {

	var form = document.listForm;

	if (getNChecked(form.fid) > 1) {
		alert(webfolderMsg.folder_alert_share_error1);
		return;
	}
	  
	if (getNChecked(form.uid) > 0) {
		if (mode == 'view') {
			alert(webfolderMsg.alert_noselect4);
		} else {
        	alert(webfolderMsg.alert_noselect2);
		}
        return;
    }

	var isShare;
	var fid;
	var fPath;
	var fnodeNum;
	var type;
	var param = getFolderParam();
	 
	if (getNChecked(form.fid) == 1) {
		if (form.fid.length > 0) {
	        for (var i = 0; i < form.fid.length; i++) {
	            if (form.fid[i].checked) {
	            	isShare = form.fshare[i].value;
	            	fid = form.fuid[i].value;
	            	fPath = form.fpath[i].value;
	            	fnodeNum = form.fnodeNum[i].value;
	            	break;
	            }
	        }
	    }
	    else {
	    	isShare = form.fshare.value;
	    	fid = form.fuid.value;
        	fPath = form.fpath.value;
        	fnodeNum = form.fnodeNum.value;
	    }

		fPath = fPath.substring(13);
		fPath = replaceAll(fPath, '.','/');

		if (mode == 'view') {
			if (isShare != 'true') {
				alert(webfolderMsg.folder_alert_share_error3);
				return;
			}
		} else {
			if (isShare == 'true') {
				alert(webfolderMsg.folder_alert_share_error2);
				return;
			}
		}
	} else {
	   fPath = param.path;
	   fid = form.shareFolderId.value;
	   fnodeNum = param.nodeNum;

	   var folderId = parseInt(fid, 10);

		if (folderId > 0) {
			isShare = 'true';
		}
	   
		if(fPath == "/"){
			if (mode == 'view') {
				alert(webfolderMsg.alert_noselect4);
				return;
			} else {
				alert(webfolderMsg.folder_root);
				return;
			}
			return;
		}

	   if (mode == 'view') { 
			if (isShare != 'true') {
				alert(webfolderMsg.folder_alert_share_error3);
				return;
			}
		} else {
			if (isShare == 'true') {
				alert(webfolderMsg.folder_alert_share_error2);
				return;
			}
		}
		
	}
	type = param.type;
	parent.shareFolderDialog(fPath, fnodeNum, type, fid);
}

function searchShareFolderPopup() {	
	
	var url ="/webfolder/searchFolder.do";	
	openSimplePopup(url,640,330,false);
	
	
}
function deleteShareFolder() {

   var form = document.listForm;
   var param = getFolderParam();

   if (getNChecked(form.fid) > 1) {
		alert(webfolderMsg.folder_alert_share_error1);
		return;
	}
	  
	if (getNChecked(form.uid) > 0) {
       alert(webfolderMsg.alert_noselect4);
       return;
    }

	var isShare;
	var fid;
	var fPath;
	var fnodeNum;
	var param = getFolderParam();
	 
	if (getNChecked(form.fid) == 1) {
		if (form.fid.length > 0) {
	        for (var i = 0; i < form.fid.length; i++) {
	            if (form.fid[i].checked) {
	            	isShare = form.fshare[i].value;
	            	fid = form.fid[i].value;
	            	fPath = form.fpath[i].value;
	            	fnodeNum = form.fnodeNum[i].value;
	            	break;
	            }
	        }
	    }
	    else {
	    	isShare = form.fshare.value;
	    	fid = form.fid.value;
       		fPath = form.fpath.value;
       		fnodeNum = form.fnodeNum.value;
	    }

		fPath = fPath.substring(13);
		fPath = replaceAll(fPath, '.','/');
		
		if (isShare != 'true') {
			alert(webfolderMsg.folder_alert_share_error3);
			return;
		}

		form.path.value = fPath;
		form.nodeNum.value = fnodeNum;
		form.type.value = param.type;
	} else {
	   
	   fPath = param.path;
	   
	   if(fPath == "/"){
			alert(webfolderMsg.alert_noselect4);
			return;
	   }

		var shareFid = parseInt(form.shareFolderId.value,10);
		if (shareFid <= 0) {
			alert(webfolderMsg.folder_alert_share_error3);
			return;
		}
		form.path.value = param.path;
		form.nodeNum.value = param.nodeNum;
		form.type.value = param.type;
	}	
   
	if (confirm(webfolderMsg.question_cancleshare)){
	   form.action = "/webfolder/deleteShareFolder.do";
	   form.method = "post";
	   form.submit();
	}	
}

function getNChecked(cb) {
	var nchecked = 0;

	if (cb != null) {
		if (cb.length > 1) {
			for (var i = 0; i < cb.length; i++) {
				nchecked += (cb[i].checked)? 1 : 0;
			}
		} else {
			nchecked += (cb.checked)? 1 : 0;
		}
	}
	return nchecked;
}

function getCheckedValue(obj, isEncode) {
	var value = "";
	if (obj && obj.length) {
		var item = "";
		for (var i=0; i < obj.length; i++) {
			if (obj[i].checked) {
				item = obj[i].value;

				if (isEncode) {
					item = Base64.encode(item);
				}
				if (value == "") {
					value += item;
				} 
				else {
					value += ","+item;	
				}
			}
		}
	} 
	else {
		var item = "";
		if (obj && obj.checked) {
			item = obj.value;
			if (isEncode) {
				item = Base64.encode(item);
			}
			value = item;
		}
	}

	return value;	
}

function isVaildFolders(actionType) {

	var f = document.listForm;
	var checkFolder = f.fid;
	var checkShare = f.fshare;
	var checkChild = f.fchild;
	
	var isChild = false;
	var isShare = false;
	
	if((actionType == "move" || 
		actionType == "del") &&
		checkFolder){	
		if (checkFolder.length > 1) {
			for (var i = 0; i < checkFolder.length; i++) {
				if(checkFolder[i].checked && 
					checkChild[i].value == "true"){
					isChild = true;
					break;				
				}
				
				if(checkFolder[i].checked && 
					checkShare[i].value == "true"){
					isShare = true;
					break;				
				}
			}
		} else {			
			if(checkFolder.checked &&
				checkChild.value == "true"){
				isChild = true;
			}
			if(checkFolder.checked &&
				checkShare.value == "true"){
				isShare = true;						
			}
		}
	}
	
	if(isChild){
		alert(webfolderMsg.share_alert_003);
		return false;
	}
	
	if(isShare){
		alert(webfolderMsg.share_alert_004);
		return false;
	}
	
	return true;	

}

function getNCheckChild() {
	var f = document.listForm;
	var checkFolder = f.fid;
	var checkChild = f.fchild;	
	
	if(checkFolder){
		if (checkFolder.length > 1) {
			for (var i = 0; i < checkFolder.length; i++) {
				if(checkFolder[i].checked && 
					checkChild[i].value == "true"){
					return true;				
				} 
			}
		} else {
			if(checkFolder.checked &&
				checkChild.value == "true"){
				return true;
			}
		}
	}
	
	return false;	

}

function markall(me) {
    var form = document.listForm;

    if (form.fid != null) {
        if (form.fid.length > 0) {
            for (var i = 0; i < form.fid.length; i++) {
                form.fid[i].checked = me.checked;
                linecheck(me,form.fid[i].value);
            }
        }
        else {
            form.fid.checked = me.checked;
            linecheck(me,form.fid.value);
        }
    }

    if (form.uid != null) {
        if (form.uid.length > 0) {
            for (var i = 0; i < form.uid.length; i++) {
                form.uid[i].checked = me.checked;
                linecheck(me,form.uid[i].value);
            }
        }
        else {
            form.uid.checked = me.checked;
            linecheck(me,form.uid.value);
        }
    }
}

function remove() {
    var form = document.listForm;
    var nfidchck = getNChecked(form.fid);
    var nchecked = nfidchck + getNChecked(form.uid);

    
    if (nchecked == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }
    
    if(!isVaildFolders("del")){
    	return;
    }

    if (nfidchck > 0) {
        if (!confirm(webfolderMsg.alert_removefolder)) {
            return;
        }
    }else if (!confirm(webfolderMsg.alert_remove)) {
        return;
    }
    
    var param = getFolderParam();
    var url = "/webfolder/deleteFolders.do";
    var paramArray = [];
    var fid = getCheckedValue(form.fid, true);
    var uid = getCheckedValue(form.uid, false);

    paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});
	paramArray.push({name:"fid", value:fid});
	paramArray.push({name:"uid", value:uid});
	executeUrl(url, paramArray);
}

function writeMail() {
    var form = document.listForm;
    var folder = form.currentPath.value;
    var param = getFolderParam();
	
    if (getNChecked(form.fid) > 0) {
        alert(webfolderMsg.alert_onlyfile);
        return;
    }

    if (getNChecked(form.uid) == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }

    var wuid = "";
    if (form.uid.length) {
		for (i=0; i<form.uid.length; i++) {
			if (form.uid[i].checked) {
				wuid += form.uid[i].value+"|";
			}
		}
    } else {
		wuid = form.uid.value;
    }
			 
	form.wfolderType.value = folderType;
	form.wfolderShareSeq.value = shareSeq;
	form.wuid.value = wuid;
	form.folder.value = param.path;

	var popWin = window.open("about:blank","popupWrite","scrollbars=yes,width=800,height=640");
	var oldTarget = form.target;
	form.method = "post";
	form.action="/dynamic/mail/writeMessage.do";	
	form.target = "popupWrite";
	form.submit();
	form.target = oldTarget;
}

function sort(key, dir) {

	var f = document.listForm;
	var searchType = f.searchType.value;
	var keyWord = f.keyWord.value;

	var param = getFolderParam();
	var paramArray = [];
	var url = "/webfolder/listFolders.do";
	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});
	paramArray.push({name:"sortby", value:key});
	paramArray.push({name:"sortDir", value:dir});
	paramArray.push({name:"searchType", value:searchType});
	paramArray.push({name:"keyWord", value:Base64.encode(keyWord)});

	executeUrl(url, paramArray);
}

function movePage(page) {

	var f = document.listForm;
	var searchType = f.searchType.value;
	var keyWord = f.keyWord.value;
	var sortby = f.sortby.value;
	var sortDir = f.sortDir.value;

	var param = getFolderParam();
	var paramArray = [];
	var url = "/webfolder/listFolders.do";
	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});
	paramArray.push({name:"sortby", value:sortby});
	paramArray.push({name:"sortDir", value:sortDir});
	paramArray.push({name:"searchType", value:searchType});
	paramArray.push({name:"keyWord", value:Base64.encode(keyWord)});
	paramArray.push({name:"currentPage", value:page});
	
	executeUrl(url, paramArray);
}

</script>
<SCRIPT TYPE="text/javascript">

function isEquals(targetPath, cb){
	if (cb != null) {
        if (cb.length > 1) {
            for (var i = 0; i < cb.length; i++) {
				if(cb[i].checked && targetPath == cb[i].value){
					return true;
				}
            }
        }
        else {
			if(targetPath == cb.value){
				return true;
			}
        }
    }
}

function renameFolderWin() {
	var form = document.listForm;
	var isShare = false;
	if ((getNChecked(form.fid) == 0) && (getNChecked(form.uid) == 0)) {
		 alert(webfolderMsg.alert_noitem);
	     return;
	}

	if (getNChecked(form.fid) > 1) {
        alert(webfolderMsg.alert_rename_err1);
        return;
    }

	if (getNChecked(form.uid) > 0) {
        alert(webfolderMsg.alert_rename_err2);
        return;
    }

    if (form.fid.length > 0) {
        for (var i = 0; i < form.fid.length; i++) {
            if (form.fid[i].checked) {
            	form.folderName.value = form.fid[i].value;
            	isShare = form.fshare[i].value;
            	break;
            }
        }
    }
    else {
    	form.folderName.value = form.fid.value;
    	isShare = form.fshare.value;
    }
	
	if (isShare == 'true') {
		alert(webfolderMsg.share_alert_005);
		return;
	}

	parent.renameFolderWin(form.folderName.value);
}

function renameFolder(rfName) {
	var form = document.listForm;
	var url = "/webfolder/renameFolder.do";
	var param = getFolderParam();
	var folderName = form.folderName.value;
	var paramArray = [];
	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});
	paramArray.push({name:"rfName", value:Base64.encode(rfName)});
	paramArray.push({name:"folderName", value:Base64.encode(folderName)});

	parent.setListLoaded(false);
	executeUrl(url, paramArray);
    
}

function copyAndMove(actionType,targetType,targetPath,targetNodeNum,targetUserSeq) {
    var form = document.listForm;
    var nchecked = getNChecked(form.fid) + getNChecked(form.uid);
    var nfidchck = getNChecked(form.fid);    
    
    var param = getFolderParam();    
    var currentPath = form.currentPath.value;
    currentPath = currentPath.substring(14);
    
    var targetPathVal =  targetPath.substring(currentPath.length);
            
    var parseCurrentPath = form.path.value;    
    
    // source is folder && target folder is subfolder of the source folder 
    
    if ((nfidchck > 0) && 
    	(targetType == folderType) &&
    	((targetPath.indexOf(currentPath) == 0) || 
    	(targetPath == currentPath))    	
    	) {
	    alert(webfolderMsg.alert_moveerr);	    
	    return;
    }
   
    if ((actionType == "move") &&
    	(getNChecked(form.uid) > 0) && 
    	(targetType == folderType) &&
    	((targetPath == currentPath))    	
    	) {
    	alert(webfolderMsg.share_alert_008);
    	return;
    }
    
    if(folderType == 'share' &&
    	targetType == 'share'){
    	alert(webfolderMsg.share_alert_002);	    
	    return;
    }

    if (nchecked == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }
    
    if((nfidchck > 0) && isEquals(targetPath, form.fid) && (folderType == targetType)){
		alert(webfolderMsg.alert_equals);
		return;
    }
    
    if(nfidchck > 0){
    	parent.syncTreeChildNode(targetType,targetNodeNum);
    }

	var srootVal = "";
	if(folderType == "share"){	
		srootVal = param.sroot;							
	} else {
		srootVal = parent.getROOT(targetNodeNum);		
	}

	if (typeof(srootVal) != 'undefined') {
		srootVal = Base64.encode(srootVal);
	}
	
	if(actionType == "copy"){
		//parent.progress(webfolderMsg.folder_work_002);
	} else if(actionType == "move"){
		//parent.progress(webfolderMsg.folder_work_003);
	}

	var fid = getCheckedValue(form.fid, true);
	var uid = getCheckedValue(form.uid, false);

	var param = getFolderParam();
	var paramArray = [];
	var url = "/webfolder/copyAndMoveFolders.do";
	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:srootVal});

	paramArray.push({name:"targetPath", value:Base64.encode(targetPath)});
	paramArray.push({name:"targetType", value:targetType});
	paramArray.push({name:"targetUserSeq", value:targetUserSeq});

	paramArray.push({name:"cmType", value:actionType});
	paramArray.push({name:"fid", value:fid});
	paramArray.push({name:"uid", value:uid});

    parent.setListLoaded(false);
	executeUrl(url, paramArray);
	      
}

function search(mode) {
    var form = document.listForm;
    var nchecked = getNChecked(form.fid) + getNChecked(form.uid);

    if (nchecked == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }
    
    if(!isVaildFolders(mode)){
    	return;
    }
    
    parent.openCopyandMove(mode);    
}

function goFolder(fullPath,nodeId){

	parent.resetSearch();
	
	var path = fullPath.substring(14);

	var param = getFolderParam();

	var paramArray = [];
	var url = "/webfolder/listFolders.do";
	paramArray.push({name:"path", value:Base64.encode(path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(nodeId)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});

	executeUrl(url, paramArray);
}

function goPreFolder(path,nodeId){
	if(nodeId.length > 1){
		nodeId = nodeId.substr(0,nodeId.lastIndexOf("|"));
		nodeId = nodeId.substr(0,nodeId.lastIndexOf("|")+1);
	}
	goFolder("WEBFOLDERROOT"+path,nodeId);
}

function getCheckSharePostData(){
	var form = document.listForm;
	if(folderType == 'share'){		
		form.userSeq.value=shareSeq;
		var sharedRoot = parent.getROOT(form.nodeNum.value);
		form.sroot.value=sharedRoot; 
	} else {
		form.userSeq.value="";
		form.sroot.value="";	
	}
}

function getFolderParam(){
	var obj = new Object();
	obj.type = folderType;
	obj.nodeNum = document.listForm.nodeNum.value;
	obj.userSeq = shareSeq;
	obj.path = document.listForm.path.value;
	
	if(folderType == 'share'){		
		var sharedRoot = parent.getROOT(document.listForm.nodeNum.value);		 
		obj.sroot = sharedRoot;		
	} else {
		obj.sroot = "";
	}
	return obj;
}

function InitSync() {
	var cuurentNum = '${nodeNum}';
	
	var jsonStr = "{";
	jsonStr += "\"treeNodeid\":\"${type}_tree_node_"+cuurentNum+"\",";
	jsonStr += "\"treeNodeShare\":\"${isFolderShare}\",";
	jsonStr +="\"nodes\":[";	
	
	var i = 0;
	<c:if test="${not empty folders}">
	<c:forEach items="${folders}" var="folder" varStatus="status">		
		jsonStr += "{";
		jsonStr += "\"folderName\":\"${folder.folderName}\",";
		jsonStr += "\"type\":\"${type}\",";
		<c:if test="${type == 'share'}">
		jsonStr += "\"nodePath\":\"${shareSeq}|${folder.realPath}\",";
		jsonStr += "\"share\":\"false\",";
		</c:if>
		<c:if test="${type == 'user' || type == 'public'}">
		jsonStr += "\"nodePath\":\"${folder.realPath}\",";
		jsonStr += "\"share\":\"${folder.share}\",";
		</c:if>
		jsonStr += "\"child\":\"${folder.child}\",";
		jsonStr += "\"nodeNum\":\"" + encodeURI("${folder.nodeNumber}") + "\"";
		jsonStr += "}";
		<c:if test="${!status.last}">
		jsonStr += ",";
		</c:if>
	</c:forEach>
	</c:if>	
	
	jsonStr += "]}";
	
	parent.syncTreeNode(folderType,cuurentNum,jsonStr);

	setSearch();	
	setFunctionMenu(parent.getIsPop());
	parent.setListLoaded(true);

	var basicControlOpt = {
			controlType:"normal",
			btnId:"basicUploadControl",
			btnCid:"basicUploadBtn",
			param:{},
			maxFileSize:(MAX_ATTACH_SIZE * 1024 * 1024),
			fileType:"*.*",
			locale:LOCALE,
			btnWidth:100,
			btnHeight:20,			
			debug:false,
			autoStart:false,
			handler:{},
			listId:"basicUploadAttachList"};
	
	basicAttachUploadControl = new UploadBasicControl(basicControlOpt);	
	chgAttachMod('simple');	
}

function setFunctionMenu(isPop){
	if(isPop){
		jQuery("#fileUploadTable").hide();
		jQuery("#fileUploadInfoTable").hide();
	}
}

function downloadFile(uid){
	var f = document.listForm;
	getCheckSharePostData();
	f.action = "/webfolder/downloadFile.do?dwuid="+encodeURI(uid);
	f.method = "post";
	f.submit();
}

function downloadFiles(){
	var f = document.listForm;
	getCheckSharePostData();

	if (getNChecked(f.fid) > 0) {
        alert(webfolderMsg.alert_onlydownfile);
        return;
    }

    if (getNChecked(f.uid) == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }

    var wuid = "";
    if (f.uid.length) {
		for (i=0; i<f.uid.length; i++) {
			if (f.uid[i].checked) {
				wuid += f.uid[i].value+"|";
			}
		}
    } else {
		wuid = f.uid.value;
    }
	
	f.action = "/webfolder/downloadFile.do?dwuid="+encodeURI(wuid);
	f.method = "post";
	f.submit();
}


function overShareLayer(){
	document.getElementById("menulayer1").style.display='';
}
function outShareLayer(){
	document.getElementById("menulayer1").style.display='none';
}

function addAttachFile(){
	var form = document.listForm;
    var folder = form.currentPath.value;

    if (getNChecked(form.fid) > 0) {
        alert(webfolderMsg.alert_onlyfile);
        return;
    }

    if (getNChecked(form.uid) == 0) {
        alert(webfolderMsg.alert_noitem);
        return;
    }
    
	var param = getFolderParam();
	form.path.value = param.path;
	form.userSeq.value = param.userSeq;
	form.sroot.value = param.sroot;
	form.nodeNum.value = encodeURI(param.nodeNum);
	form.type.value = param.type;		
	
	form.action = "/webfolder/writeAttachFile.do";
	form.submit();
}

function createFolder(fName) {
	
	var form = document.listForm;

	var curpath = form.path.value;
	var folders = curpath.split("/");
	 // permit max 5 depth
	if (folders.length >= 6) {
		alert(webfolderMsg.alert_maxdepth);
		return;
	}

	var url = "/webfolder/createFolder.do";
	var param = getFolderParam();
	var paramArray = [];
	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});
	paramArray.push({name:"fName", value:Base64.encode(fName)});

	executeUrl(url, paramArray);	
    parent.setListLoaded(false);
}

function searchList() {
	var form = document.listForm;

	var keywordObj = parent.document.searchForm.keyWord;
	
	if(!checkInputLength("", keywordObj, webfolderMsg.alert_search_empty_keyword, 2, 64)) {
		return;
	}
	
	if (!checkInputText(keywordObj, 2, 64, true)){
		return false;
	}
	
	if(!checkInputValidate("", keywordObj, "case5")) {
		return;
	}

	var url = "/webfolder/listFolders.do";
	var param = getFolderParam();
	var paramArray = [];

	paramArray.push({name:"path", value:Base64.encode(param.path)});
	paramArray.push({name:"type", value:param.type});
	paramArray.push({name:"userSeq", value:param.userSeq});
	paramArray.push({name:"nodeNum", value:encodeURI(param.nodeNum)});
	paramArray.push({name:"sroot", value:Base64.encode(param.sroot)});

	paramArray.push({name:"searchType", value:parent.document.searchForm.searchType.value});
	paramArray.push({name:"keyWord", value:Base64.encode(keywordObj.value)});

	executeUrl(url, paramArray);
}

function init() {
	parent.setQuotaInfo('${quotaUsage}', '${quotaLimit}','${quotaUsagePercent}');
	checkMenubar();
	InitSync();
	parent.resizeFrame(jQuery("body").height());
	setTitle();
	timeCheck();
	setTimeout(function(){parent.setPageNavi('${totalCount}','${pageBase}','${currentPage}');},100);
}

function checkMenubar() {
	parent.checkMenubar('${isFolderShare}','${isRoot}','${type}','${folderAuth}');

	if ('${folderAuth}' == 'R') {
		jQuery("#fileUploadTable").hide();
		jQuery("#fileUploadInfoTable").hide();
	} 
	else {
		jQuery("#fileUploadTable").show();
		jQuery("#fileUploadInfoTable").show();
	}
}

function resetSearch() {
	var f = document.listForm;
	f.keyWord.value = "";
	f.currentPage.value = "1";
}

function setSearch() {
	var f = document.listForm;
	parent.setSearch(f.keyWord.value);
}

function setTitle() {
	var f = document.listForm;
	var path = f.path.value;
	var title = "";
	var folderArray;

	title = webfolderMsg.webfolder_title;
	if (folderType == 'share') {
		title = webfolderMsg.webfolder_share_title;
	}
	if(folderType == 'public'){
		title = webfolderMsg.webfolder_public_title;
	}
	if (path == "/") {
		parent.setTitle(title);
	}
	else {
		folderArray = path.split("/");
		if (folderArray && folderArray.length > 0) {
			for (var i=1; i < folderArray.length; i++) {
				title = title + " > "+ folderArray[i];
			}
		}
		parent.setTitle(title);
	}		
}

function toogleAttListSelect(flag){
	if(flag){
		jQuery(".TM_attList").show();
	} else {
		jQuery(".TM_attList").hide();
	}
}

function executeUrl(url, paramArray) {

	var param = "";
	if (paramArray.length > 0) {
		param += "?";
		for (var i = 0 ; i < paramArray.length ; i++){
			if (i > 0) {
				param += "&";
			}
			param += paramArray[i].name+"="+paramArray[i].value;
		}
	}
	this.location = url+param;
}

var interval;
var resizeCnt = 0;

function resizeFrame(){
	if (resizeCnt > 10) {
		clearInterval(interval);
	}
	jQuery("list_frame").css("padding-bottom","20px");
	var height = document.getElementById('list_frame').offsetHeight;		
	var width = jQuery("#webfolderListTable").width();
	parent.resizeTextFrame(height,width);
	resizeCnt++;
}

function timeCheck(){		
	interval = setInterval(resizeFrame,1000);
}
</SCRIPT>
</head>
<body onLoad="init();">
<div id="list_frame">
	<form name="listForm">
		<input type="hidden" name="wmode" value="popup"/>
		<input type="hidden" name="path" value="<%= path %>">
		<input type="hidden" name="currentPath" value="${currentPath}">		 
		<input type="hidden" name="targetPath">
		<input type="hidden" name="targetType">
		<input type="hidden" name="targetUserSeq">
		<input type="hidden" name="nodeNum" value="${nodeNum}">
		<input type="hidden" name="folderName" value="init"/>
	    <input type="hidden" name="folderPath"/>
	    <input type="hidden" name="sroot" value="${sroot}"/>
	    <input type="hidden" name="type" value="${type}"/>
	    <input type="hidden" name="userSeq" value="${userSeq}"/>
	    <input type="hidden" name="foldercreator" value="${shareId}">
	    <input type="hidden" name="shareFolderId" value="${fid}"/>
	    <input type="hidden" name="cmType"/>
		<input type="hidden" name="sortby" value="${sortby}"/>
		<input type="hidden" name="sortDir" value="${sortDir}"/>
		<input type="hidden" name="searchType" value="name"/>
		<input type="hidden" name="keyWord" value="${keyWord}"/>
		<input type="hidden" name="currentPage" value="${currentPage}"/>
		<input type="hidden" name="wuid">
		<input type="hidden" name="workName" value="write">
		<input type="hidden" name="wtype" value="webfolder">
		<input type="hidden" name="wfolderType">
		<input type="hidden" name="wfolderShareSeq">
		<input type="hidden" name="folder">
		<input type="hidden" name="fName">
		<input type="hidden" name="rfName">
	
		<div class="TM_listWrapper">
			<table id="webfolderListTable" cellpadding="0" cellspacing="0" class="TM_tableList" style="width:100%;*width:;table-layout: fixed">
				<tr>
					<th style="width:30px;">
						<input type="checkbox" name="file_all" onClick="checkAllTdLine(listForm.fid, this.checked);checkAllTdLine(listForm.uid, this.checked)"/>
					</th>
					<!-- name begin -->
					<th>
						<c:if test="${sortby != 'name'}">
							<A HREF="javascript:sort('name', 'desc')"><tctl:msg key="list.filename" bundle="webfolder" /></A>
						</c:if> 
						<c:if test="${sortby == 'name'}">
							<c:if test="${sortDir == 'desc'}">
								<A HREF="javascript:sort('name', 'asce')" class='sortSelectItem'><tctl:msg key="list.filename" bundle="webfolder" /></A>
								<A HREF="javascript:sort('name', 'asce')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_next.gif"/></A>
							</c:if>
							<c:if test="${sortDir == 'asce'}">
								<A HREF="javascript:sort('name', 'desc')" class='sortSelectItem'><tctl:msg key="list.filename" bundle="webfolder" /></A>
								<A HREF="javascript:sort('name', 'desc')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_prev.gif"/></A>
							</c:if>
						</c:if>
					<!-- name end -->
					</th>
					<!-- size begin -->
					<th style="width:100px;"> 
						<c:if test="${sortby != 'size'}">
							<A HREF="javascript:sort('size', 'desc')" ><tctl:msg key="list.size" bundle="webfolder" /></A>
						</c:if>
						<c:if test="${sortby == 'size'}">
							<c:if test="${sortDir == 'desc'}">
								<A HREF="javascript:sort('size', 'asce')" class='sortSelectItem'><tctl:msg key="list.size" bundle="webfolder" /></A>
								<A HREF="javascript:sort('size', 'asce')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_next.gif"/></A>
							</c:if>
							<c:if test="${sortDir == 'asce'}">
								<A HREF="javascript:sort('size', 'desc')" class='sortSelectItem'><tctl:msg key="list.size" bundle="webfolder" /></A>
								<A HREF="javascript:sort('size', 'desc')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_prev.gif"/></A>
							</c:if>
						</c:if>
					</th>
					<!-- size end -->
					<!-- kind begin -->
					<th style="width:100px;"> 
						<c:if test="${sortby != 'kind'}">
							<A HREF="javascript:sort('kind', 'desc')"><tctl:msg key="list.kind" bundle="webfolder" /></A>
						</c:if>
						<c:if test="${sortby == 'kind'}">
							<c:if test="${sortDir == 'desc'}">
								<A HREF="javascript:sort('kind', 'asce')" class='sortSelectItem'><tctl:msg key="list.kind" bundle="webfolder" /></A>
								<A HREF="javascript:sort('kind', 'asce')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_next.gif"/></A>
							</c:if>
							<c:if test="${sortDir == 'asce'}">
								<A HREF="javascript:sort('kind', 'desc')" class='sortSelectItem'><tctl:msg key="list.kind" bundle="webfolder" /></A>
								<A HREF="javascript:sort('kind', 'desc')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_prev.gif"/></A>
							</c:if>
						</c:if>
					</th>
					<!-- kind end -->
					<!-- date begin -->
					<th style="width:100px;" nowrap>
						<c:if test="${sortby != 'date'}">
							<A HREF="javascript:sort('date', 'desc')"><tctl:msg key="list.date" bundle="webfolder" /></A>
						</c:if>
						<c:if test="${sortby == 'date'}">
							<c:if test="${sortDir == 'desc'}">
								<A HREF="javascript:sort('date', 'asce')" class='sortSelectItem'><tctl:msg key="list.date" bundle="webfolder" /></A>
								<A HREF="javascript:sort('date', 'asce')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_next.gif"/></A>
							</c:if>
							<c:if test="${sortDir == 'asce'}">
								<A HREF="javascript:sort('date', 'desc')" class='sortSelectItem'><tctl:msg key="list.date" bundle="webfolder" /></A>
								<A HREF="javascript:sort('date', 'desc')" class='sortSelectItem'><img src="/design/common/image/icon/icon_bullet_prev.gif"/></A>
							</c:if>
						</c:if>
					</th>
					<!-- date end -->
				</tr>
				<c:if test="${empty folders && empty messages && isRoot}">
				<tr>
					<td colspan="5" class="webfolderTd" align="center"><tctl:msg key="folder.nosearch" bundle="webfolder"/></td>
				</tr>
				</c:if>
				<c:if test="${isRoot == false}">
					<tr align="center">
						<td height="17" class="webfolderTd">&nbsp;</td>
						<td align="left" class="webfolderTd fileName">
							<a href="javascript:goPreFolder('<%= ppath %>','${nodeNum}')"><img src="/design/common/image/icon/icon_folder_root.gif" /></a>&nbsp;..
						</td>
						<td class="webfolderTd">&nbsp;</td>
						<td class="webfolderTd">&nbsp;</td>
						<td class="webfolderTd">&nbsp;</td>
					</tr>
				</c:if>
				<c:forEach var="folder" items="${folders}">
				<tr align="center" id="tr_${fn:escapeXml(folder.folderName)}">
					<td height="17" class="webfolderTd">
						<input type="checkbox" name="fid" value="${fn:escapeXml(folder.folderName)}" onclick="checkTdLine(this)">
						<input type="hidden" name="fuid" value="${folder.fuid}">
						<input type="hidden" name="fpath" value="${folder.realPath}">
						<input type="hidden" name="fchild" value="${folder.child}">
						<input type="hidden" name="fshare" value="${folder.share}">
						<input type="hidden" name="fnodeNum" value="${folder.nodeNumber}">
					</td>
					<td align="left" class="webfolderTd fileName">						
						<div>
							<c:if test="${folder.share eq true}">					
								<img src="/design/common/image/icon/icon_folder_share_close.gif">
							</c:if>
							<c:if test="${folder.share eq false}">								
								<img src="/design/common/image/icon/ic_cfolder.gif">
							</c:if>
							<a href="#" onclick="goFolder('${folder.realPath}','${folder.nodeNumber}');">${fn:escapeXml(folder.folderName)}</a>
						</div>
					</td>
					<td class="webfolderTd">
					<p>&nbsp;</p>
					</td>
					<td class="webfolderTd">
					<p>&nbsp;</p>
					</td>
					<td class="webfolderTd">
					<p>&nbsp;</p>
					</td>
				</tr>
				</c:forEach>				
				<c:forEach var="message" items="${messages}">
				<c:set var = "fileType" value="${fn:toLowerCase(message.webFolderFileExt)}"/>
					<tr align="center" id="tr_${message.uid}">
						<td height="17" class="webfolderTd">
							<input type="checkbox" NAME="uid" value="${message.uid}" onclick="checkTdLine(this)"> 
							<input type="hidden" name="param" value="xxx	${message.subject}	${message.webFolderFileSize}	${message.uid}	<%= path %>">
						</td>
						<td align="left" class="webfolderTd fileName" title="${message.subject}">
							<c:choose>
					   			<c:when test="${fileType=='doc' || 	fileType=='docx'|| 	fileType=='gif' || 
												fileType=='pdf' || 	fileType=='html'|| 	fileType=='hwp' || 
												fileType=='jpg' || 	fileType=='bmp' ||	fileType=='ppt' || 
												fileType=='pptx'|| 	fileType=='txt' || 	fileType=='xls' || 
												fileType=='xlsx'|| 	fileType=='zip' || 	fileType=='xml' ||
												fileType=='mpeg'||	fileType=='avi' || 	fileType=='htm' ||
												fileType=='mp3' ||	fileType=='mp4' ||	fileType=='eml'}">							   				
					   				<c:set var="attachImgName" value="ic_att_${fileType}"/>
					   			</c:when>								   			
					   			<c:otherwise>
					   				<c:set var="attachImgName" value="ic_att_unknown"/>						   				
					   			</c:otherwise>
				   			</c:choose>
								<img src="/design/common/image/icon/${attachImgName}.gif" alt="${fileType}">
								<a href="#" onClick="downloadFile('${message.uid}');">${message.subject}</a>		
						</td>
						<td class="webfolderTd">
							<p>
								<c:if test="${!empty message.webFolderFileSize}">
								<tctl:formatSize>${message.webFolderFileSize}</tctl:formatSize>
								</c:if>
							</p>
						</td>
						<td class="webfolderTd">
							<p>
								<c:choose>
								   <c:when test="${fileType=='doc' || 	fileType=='docx'|| 	fileType=='gif' || 
												fileType=='pdf' || 	fileType=='html'|| 	fileType=='hwp' || 
												fileType=='jpg' || 	fileType=='bmp' ||	fileType=='ppt' || 
												fileType=='pptx'|| 	fileType=='txt' || 	fileType=='xls' || 
												fileType=='xlsx'|| 	fileType=='zip' || 	fileType=='xml' ||
												fileType=='mpeg'||	fileType=='avi' || 	fileType=='htm' ||
												fileType=='mp3' ||	fileType=='mp4' ||	fileType=='eml'}">								   				
								   		<c:set var="extStr" value="${fileType}"/>
								   	</c:when>								   			
								   	<c:otherwise>
								   		<c:set var="extStr" value="unknown"/>						   				
								   	</c:otherwise>
							   	</c:choose>
								<tctl:msg key="folder.file.info.${extStr}" bundle="webfolder"/>
							</p>
						</td>
						<td class="webfolderTd">
							<p><tctl:formatDate>${message.webFolderFileDate}</tctl:formatDate></p>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>	
		<c:if test="${!empty messages || !empty folders}">
			<div id='pageBottomNavi' class='pageNavi pageNaviTopNone'></div>
		</c:if>
	</form>
	
	<table id="fileUploadTable" width="100%">
		<tr id="folerUploadL">
			<td valign="top" ><%@include file="uploadPage.jsp"%></td>
		</tr>
	</table>
	
	<div id="fileUploadInfoTable" style="text-align:right;padding:5px;" >
		<span id="att_simple_quota_info">[<b><tctl:msg key="button.upload" bundle="webfolder"/> <tctl:msg key="list.capacity" bundle="webfolder"/></b> (<span id="basic_normal_size">0B</span> / ${attachMaxSize}MB)]</span>
	</div>
</div>


<div id="attachUploadProgress"  title="<tctl:msg key="comn.upload.title" bundle="common"/>" style="display:none">
<form name="attachUploadProgressForm" id="attachUploadProgressForm">	
	<div id="attachUploadProgressContents"  style="height:300px;">			
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;">
			<col></col>
			<col width="100px"></col>			
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.filename" bundle="common"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.status" bundle="common"/>
			</th>
			</tr>
		</table>			
		<div id="uploadLoadingBox" style="height:260px; overflow-X:hidden;overflow-Y:auto; padding:1px;position: relative;margin-bottom: 5px;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="attachFileUploadTable" style="width:100%;*width:;table-layout:fixed;position: relative;height:245px;">
				<col></col>
				<col width="100px"></col>				
			</table>
		</div>
	</div>
</form>
</div>
</body>
</html>
