var folder_tree;
var treeInfo;

function initTree(infoObj)
{
	treeInfo = infoObj;
	folder_tree = document.getElementById(treeInfo.treeId);
	var menuItems = folder_tree.getElementsByTagName('LI');	// Get an array of all menu items
	for(var no=0;no<menuItems.length;no++){
		nodePrint(menuItems[no],"false");			
	}
}

		
function showHideNode(e)
{
	thisNode = this;		
	if(thisNode.style.visibility=='hidden')return;		
	var parentNode = thisNode.parentNode;

	var viewNode = parentNode.getElementsByTagName('UL')[0];
	var atag = parentNode.getElementsByTagName('A')[0];
	var folderImg = parentNode.getElementsByTagName('IMG')[1];		

	if(thisNode.src.indexOf('plus')>=0){
				
		var countChildNode = viewNode.getElementsByTagName('LI');
		if(countChildNode.length == 0){	
			thisNode.src = treeInfo.imageFolder + treeInfo.folderLoadingImage;				
			viewNode.style.display='';
			addChildNodeRequest(viewNode.id,atag.name, atag.type);
		} else {
			thisNode.src = thisNode.src.replace('plus','minus');		
			viewNode.style.display='';
		}
		
		var folderImgSource = treeInfo.folderOpenImage;
		
		if(folderImg.src.indexOf(treeInfo.folderSCloseImage) > -1){
			folderImgSource = treeInfo.folderSOpenImage;
		}else if(atag.name == "/"){
			folderImgSource = treeInfo.folderROOTImage;
		}		
		
		folderImg.src = treeInfo.imageFolder + folderImgSource;

	}else{
		thisNode.src = thisNode.src.replace('minus','plus');
		viewNode.style.display='none';
		
		var folderImgSource = treeInfo.folderCloseImage;
		
		if(folderImg.src.indexOf(treeInfo.folderSOpenImage) > -1){
			folderImgSource = treeInfo.folderSCloseImage;
		}else if(atag.name == "/"){
			folderImgSource = treeInfo.folderROOTImage;
		}

		folderImg.src = treeInfo.imageFolder + folderImgSource;
	}			
}

function addChildNodeRequest(nodeId,nodePath,type){
			
	//ajax Request nodeId,nodePath
	var id = treeInfo.defaultUserId;
	if (nodePath.indexOf("|") > -1){
		var vals = nodePath.split("\|");
		id = vals[0];
		nodePath = vals[1];
	}
	
	var ajax = new Ajax.Request(
		"/webfolder/listFolderData.do",
		{
			method : 'post',
			parameters: {userSeq: id, path:nodePath, nodeName:nodeId, type:type}, 
			onSuccess : addChildNode,
			onFailure : errorNode
		}
	);

	//addChildNode(result);
}

function errorNode(){
	alert('error');
}

function addChildNode(result){				
	var resultObj = eval('(' + result.responseText + ')');
	appendChildNode(resultObj);
}

function appendChildNode(resultObj){
	var liItems = folder_tree.getElementsByTagName('LI');
	var nodeNum = liItems.length;
	
	var nodeNumber = resultObj.treeNodeid;
	var parentNode = document.getElementById(nodeNumber);
	var addNodes = resultObj.nodes;
	var folderImg = parentNode.parentNode.getElementsByTagName('IMG')[0];	
	folderImg.src = treeInfo.imageFolder + treeInfo.minusImage;

	for (var i = 0; i < addNodes.length ; i++){
		
		var li = document.createElement('LI');
		li.id = addNodes[i].type+"_node_"+addNodes[i].nodeNum;
		li.style.marginLeft = 6+"px";
		li.style.marginTop = 5+"px";
		li.style.paddingBottom = 0+"px";
		
		var atag = document.createElement('A');
		nodeNum++;
		atag.id = addNodes[i].type+"_link_"+addNodes[i].nodeNum;
		atag.href = "javascript:"+treeInfo.linkMethod+"('"+addNodes[i].nodePath+"','"+addNodes[i].type+"_link_"+addNodes[i].nodeNum+"','"+addNodes[i].nodeNum+"','"+addNodes[i].type+"')";
		atag.name = addNodes[i].nodePath;
		atag.type = addNodes[i].type;
		var text = document.createTextNode(addNodes[i].folderName);
				
		atag.appendChild(text);
		li.appendChild(atag);			
		
		if (addNodes[i].child == "true"){				
			var ul = document.createElement('UL');
			ul.style.display='';
			ul.id = addNodes[i].type+"_tree_node_"+addNodes[i].nodeNum;	
			li.appendChild(ul);
		}
		
		nodePrint(li,addNodes[i].share);
		parentNode.appendChild(li);
	}		
	
}


function nodePrint(node,isShare){
	
	var subItems = node.getElementsByTagName('UL');
	var img = document.createElement('IMG');
	img.src = treeInfo.imageFolder + treeInfo.plusImage;
	var folderMargin = 2+"px";
	img.style.marginLeft = 4+"px";
	if(subItems.length==0){
		img.style.display='none';
		folderMargin = 	22+"px";
	} else {
		img.onclick = showHideNode;
	}

	var aTag = node.getElementsByTagName('A')[0];
	aTag.onclick=getNodeEvent;
	
	var folderImgSource = treeInfo.folderCloseImage;
	if(isShare == "true"){
		folderImgSource = treeInfo.folderSCloseImage;
	}
	if(aTag.name == "/"){
		folderImgSource = treeInfo.folderROOTImage;
	}

	var folderImg = document.createElement('IMG');
	folderImg.src = treeInfo.imageFolder + folderImgSource;
	folderImg.style.marginLeft = folderMargin;
	folderImg.style.marginRight = 5+"px";

	node.insertBefore(folderImg,aTag);
	node.insertBefore(img,folderImg);
	node.title = trim(aTag.innerHTML);
	
	//alert(document.getElementById('folder_tree').innerHTML);		
}

function printTreeBranch(node){
	var subItems = node.getElementsByTagName('UL');
	var nodeBoxImg = node.getElementsByTagName('IMG')[0];
	var folderImg = node.getElementsByTagName('IMG')[1];
	var img = document.createElement('IMG');
	img.src = treeInfo.imageFolder +treeInfo.plusImage;
	
	var folderMargin = 2+"px";
	if(subItems.length==0){
		nodeBoxImg.style.display='none';			
		folderMargin = 22+"px";
		if(folderImg.src.indexOf(treeInfo.folderSOpenImage) > -1){
			folderImg.src = treeInfo.imageFolder +treeInfo.folderSCloseImage;	
		} else if(folderImg.src.indexOf(treeInfo.folderROOTImage) < 0){
			folderImg.src = treeInfo.imageFolder +treeInfo.folderCloseImage;
		}	
	} else {
		nodeBoxImg.style.display='';
		subItems[0].style.display='';
		nodeBoxImg.onclick = showHideNode;
	}		
	
	folderImg.style.marginLeft = folderMargin;
	folderImg.style.marginRight = 5+"px";
}


function getNodeEvent(e){
	thisNode = this;		
	var parentNode = thisNode.parentNode;
	var viewNode = parentNode.getElementsByTagName('UL')[0];		
	
}

function setOldNode(oldNode){
	var oldN = document.getElementById(oldNode);
	if(oldN){
		oldN.style.fontWeight="normal";		
	}			
}

function setCurrentNode(node){
	var nodeObj = document.getElementById(node);
	if(nodeObj){
		nodeObj.style.fontWeight="bold";
	}				
}

function syncNode(folderType,cnum,jsonStr){
		
	var treeNode = document.getElementById(folderType+"_tree_node_"+cnum);		
	var jObj = eval('(' + jsonStr + ')');
	
	var nodes = jObj.nodes;		
	
	if(!treeNode){
		if (nodes.length > 0){							
			var li = document.getElementById(folderType+"_node_"+cnum);
			var ul = document.createElement('UL');
			ul.style.display='none';
			ul.id = folderType+"_tree_node_"+cnum;	
			li.appendChild(ul);
			treeNode = ul;
			printTreeBranch(li);								
		} else {
			cnum = cnum.substr(0,cnum.lastIndexOf("|"));
			cnum = cnum.substr(0,cnum.lastIndexOf("|")+1);						
			treeNode = document.getElementById(folderType+"_tree_node_"+cnum);
		}
	} else {				
		if (nodes.length == 0){				
			var li = document.getElementById(folderType+"_node_"+cnum);
			var ul = document.getElementById(folderType+"_tree_node_"+cnum);
			ul.parentNode.removeChild(ul);
			treeNode = false;
			printTreeBranch(li);
		} else {				
			treeNode.parentNode.removeChild(treeNode);
			var li = document.getElementById(folderType+"_node_"+cnum);
			var ul = document.createElement('UL');
			ul.style.display='';
			ul.id = folderType+"_tree_node_"+cnum;
			li.appendChild(ul);
			treeNode = ul;
			var nodeImgOld = li.getElementsByTagName('IMG')[0];
			nodeImgOld.src = nodeImgOld.src.replace('minus','plus');
		}
		
	}		
	
	if(treeNode){
		var nodeImg = treeNode.parentNode.getElementsByTagName('IMG')[0];
		var folderImg = treeNode.parentNode.getElementsByTagName('IMG')[1];
		var atag = treeNode.parentNode.getElementsByTagName('A')[0];	
		var countChildNode = treeNode.getElementsByTagName('LI');
						
		if(nodeImg.src.indexOf('plus')>=0){
			if(countChildNode.length == 0){
				appendChildNode(jObj);
			} else {
				nodeImg.src = nodeImg.src.replace('plus','minus');		
				treeNode.style.display='';
			}
						
			var folderImgSource = treeInfo.folderOpenImage;
			if(jObj.treeNodeShare == 'true'){
				folderImgSource = treeInfo.folderSOpenImage;	
			} else if(atag.name == "/"){
				folderImgSource = treeInfo.folderROOTImage;
			}
			folderImg.src = treeInfo.imageFolder + folderImgSource;
		}
	}

}

function syncAddBranch(folderType,nodeNum){
	var node = document.getElementById(folderType+"_node_"+nodeNum);
	var branchNode = document.getElementById(folderType+"_tree_node_"+nodeNum);
		
	if(node && !branchNode){		
		var ul = document.createElement('UL');
		ul.style.display='';
		ul.id = folderType+"_tree_node_"+nodeNum;
		node.appendChild(ul);
		printTreeBranch(node);
	}	
}

