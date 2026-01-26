/**
 * jquery.dftree.js   2008-11-19
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
jQuery.fn.menuSelector = function(menuOpt,nodeData){
	return this.each(function() {
		
		function clickMenu(event){
			jQuery("#"+treeId+"_tfeditorLinkPane").remove();

			var eventTarget = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);			

			var menuWL = jQuery("<div>");
			menuWL.attr("id",treeId+"_tfeditorLinkPane");
			menuWL.css("position","relative");
			
			var subMenuOpt = {blankImgSrc : menuOpt.blankImgSrc,width:50};
			jQuery(menuWL).makeSubMenu(subMenuOpt,menuOpt.menuLink,nodeData);			
			jQuery("#"+nodeData.nwid).append(menuWL);
		}		

		function menuOver(){
			var preJobId = getJobMap(nodeData.treeId);
			
			if(jQuery(this).parent().attr("id") != 
				jQuery("#"+preJobId).parent()
				.parent().parent().parent().parent().parent().attr("id")){				
				
				jQuery(this).addClass(menuOpt.overClass);			
				icon.bind("click",clickMenu);
				jQuery(this).find("div:not([name=vchild])").append(icon);				
			}			
		}

		function menuOut(){		
			jQuery(this).removeClass(menuOpt.overClass);
			icon.unbind("click");			
			jQuery(this).find("#"+treeId+"_tfeditorLink").remove();
			jQuery("#"+treeId+"_tfeditorLinkPane").remove();
		}		

		var menu = jQuery(this);
		var id = menu.parent().attr("id");
		var treeId = nodeData.treeId;
		var menuTimeout;
		
		var icon = jQuery("<img>");
		icon.attr("src",menuOpt.blankImgSrc);
		
		icon.addClass(menuOpt.iconClass);			
		icon.css("cursor","pointer");
		icon.attr("id",treeId+"_tfeditorLink");
		
		menu.hover(menuOver,menuOut);
	});
}

jQuery.fn.DFTree = function(treeId,nodelist,opt){
	return this.each(function() {		
		
		function drowNode(sNodeId,startNode,depth,nodes){
			jQuery.each(nodes, function(){
				var folder = this;
				var nodeId = folder.id;
				var isSharedFolder = folder.share;
				var node = jQuery("<div>").attr("id",nodeId).addClass(opt.nodeClass);
				
				var wrapPane = jQuery("<div>").attr("id","tfwrap_"+treeId+"_"+nodeId);				
				wrapPane.attr("fname",this.encName);
				wrapPane.attr("fullname",this.fullName);
				wrapPane.addClass(opt.menuOpt.outClass);
								
				var folderImg;
				if(isSharedFolder){
					folderImg = jQuery("<div>").addClass(opt.sharedfolderClass);
				} else {
					folderImg = jQuery("<div>").addClass(opt.closefolderClass);
				}
				
				var fname = getFolderNameEscape(folder.fullName);				
				var link = jQuery("<a href='javascript:;' id='link_folder_"+fname+"'></a>");
				var nodeData = {
						treeId : treeId,
						nwid : "tfwrap_"+treeId+"_"+nodeId,
						id : nodeId,
						depth : folder.depth,
						fullName : folder.fullName,
						shared : folder.share,
						sharedUid : folder.sharedUid
				};

				link.append(escape_tag(ellipsisString(this.name,20)));
				link.addClass(opt.nodeLinkClass);
				link.click(function(e){opt.nodelink(nodeData)});
				
				
				var unread = jQuery("&nbsp;<span id='mf_"+fname+"_newCnt'></span>");				
				if(parseInt(this.unseenCnt) > 0 ){
					var unseenLink = jQuery("<a href='javascript:;'></a>").append(folder.unseenCnt).addClass("TM_unseenLink");
					unseenLink.click(function(){
						goFolder(folder.fullName,"U");	
					});
					unread.append("(");
					unread.append(unseenLink);
					unread.append(")");
				}
				
				
				var listT = jQuery("<table cellspacing='0' cellpadding='0' border='0'></table>").addClass("treeNodeWrapperTable");				
				var listTr = jQuery("<tr></tr>");
				
				if(folder.child){
					var ov = jQuery.cookie("DFN_tfchild_"+nodeId);					
					var childImg;
					if(ov == "V"){
						childImg = jQuery("<div name='vchild' childName='tfchild_"+nodeId+"'>").addClass(opt.openChildClass);
					} else {
						childImg = jQuery("<div name='vchild' childName='tfchild_"+nodeId+"'>").addClass(opt.closeChildClass);
					}
					
					childImg.attr("id",nodeId + "_tfchildbtn")
					childImg.click(function(e){childNodeView(e,opt)});					
					listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperCIcon").append(childImg));					
				} else {
					listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperCIcon"));
				}
				
				listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperIcon").append(folderImg));
				listTr.append(jQuery("<td id='tfn_cont_"+nodeId+"'></td>").addClass("treeNodeWrapperContents").append(link).append(unread));				
				listT.append(listTr);				
				wrapPane.append(listT);								
				
				if(opt.nodeMenuUse){					
					wrapPane.menuSelector(opt.menuOpt,nodeData);
				}
				node.append(wrapPane);

				if(folder.child){
					var childNodePane = jQuery("<div>").addClass(opt.childNodeClass);
					if(ov == "V"){
						childNodePane.css("display","block");
					} else {
						childNodePane.css("display","none");
					}
					childNodePane.attr("id","tfchild_"+nodeId);					

					drowNode(nodeId,childNodePane,++depth,folder.child);					
					node.append(childNodePane);
					
				}

				startNode.append(node);				
				
			});
		}
		
		var treeLayer = jQuery(this);		
		var depth = 0;
		drowNode(treeId,treeLayer,depth,nodelist);
		

	});
}

jQuery.fn.DFTreeAdd = function(treeId,parentId,parentFullName,opt){
	return this.each(function() {		
		

		function clearInputNode(id){
			jQuery("#"+id).remove();
			removeJobMap(treeId);
			removeJobMap("addNode");
		}

		function addTreeNode(inputId){			
						
			var folderName = jQuery("#"+treeId+'_newNode').val();
			
			if(!checkFolderName('folder',$(treeId+'_newNode'))){
				return;
			}
			
			jQuery("#"+inputId).remove();			
			jQuery("#tfwrap_"+inputId).remove();
			removeJobMap(treeId);
			removeJobMap("addNode");
			
			if(parentFullName && parentFullName != ""){
				folderName = parentFullName +"."+folderName;
			}
			opt.addFolderFunc(folderName);
			
		}
	
		function addInputNode(id,parentNode){		
			var node = jQuery("<div>").attr("id",id).addClass(opt.nodeClass);
			var wrapPane = jQuery("<div>").attr("id","tfwrap_"+id);
			var folderImg = jQuery("<div>").addClass(opt.closefolderClass);			
			var inputbox = jQuery("<input id='"+treeId+"_newNode' type='text' size='10' />");
			var submit = jQuery("<a href='javascript:;'></a>");
			var cancel = jQuery("<a href='javascript:;'></a>");

			submit.append(jQuery("<img>").attr("src",opt.blankImgSrc)
					.attr("align","absmiddle").addClass(opt.nodeConfirmClass));			
			submit.click(function(){addTreeNode(id)});
			cancel.append(jQuery("<img>").attr("src",opt.blankImgSrc)
					.attr("align","absmiddle").addClass(opt.nodeCancelClass));
			cancel.click(function(){clearInputNode(id)});

			inputbox.bind((jQuery.browser.opera ? "keypress" : "keydown") + ".addfolder", function(event) {
				if(keyEvent(event) == 13){
					addTreeNode(id);
				}
			});
			
			wrapPane.append(folderImg);
			wrapPane.append(inputbox);			
			wrapPane.append(submit);
			wrapPane.append(cancel);
			
			node.append(jQuery("<div>").addClass(opt.emptyChildClass));
			node.append(wrapPane);
			
			var saveJobNodeId = id;
			if(parentNode == treeLayer){
				parentNode.prepend(node);
			} else if(!isChildExist){
				var childNodePane = jQuery("<div>").addClass(opt.childNodeClass);
				childNodePane.attr("id","tfchild_"+id);
				childNodePane.append(node);
				parentNode.append(childNodePane);
				saveJobNodeId = "tfchild_"+id;
			} else {				
				var childPaneNode = jQuery(parentNode.find("div[id^=tfchild]")[0]);				
				var childViewBtn = jQuery(parentNode.find("."+opt.closeChildClass)[0])
				childViewBtn.removeClass(opt.closeChildClass);
				childViewBtn.addClass(opt.openChildClass);
				childPaneNode.show();
				childPaneNode.prepend(node);
			}
			
			inputbox.focus();

			setJobMap(treeId,saveJobNodeId);			
		}		

		var treeLayer = jQuery(this);
		var folderLength = treeLayer.find("div").length;
		var id = treeId+"_newFolder_"+folderLength++;
		var parentNode = (parentId)?jQuery("#"+parentId):treeLayer;		
		var isChildExist = (parentNode != treeLayer 
							&& parentNode.find("div[id*=tfchild]").length == 0)?false:true;		
		
		var addPreJobId = getJobMap("addNode");		
		if(parentId && addPreJobId && parentId == addPreJobId){
			return;
		}
		resetInputBox(treeId);		
		addInputNode(id,parentNode);
		setJobMap("addNode",parentId);

	});
}

jQuery.fn.DFTreeRemove = function(nodeId,fullName,opt){
	return this.each(function() {
		var treeLayer = jQuery(this);
		var removeNode = jQuery(treeLayer.find("div[id="+nodeId+"]")[0]);
		var childNode = removeNode.parent();
		
		if(childNode.find("."+opt.nodeClass).length == 1){
			var parentNode = childNode.parent();			
			jQuery("#"+parentNode.attr("id")+"_tfchildbtn").remove();
			childNode.remove();
		}
				
		removeNode.remove();
		opt.deleteFolderFunc(fullName);

	});
}

jQuery.fn.DFTreeModify = function(treeId,nodeId,fullName,opt){
	return this.each(function() {

		function clearInputNode(id){
			jQuery("#"+id).remove();
			linkNode.show();
			newCntPane.show();
			removeJobMap(treeId);
		}

		function modifyTreeNode(id){
			var newName = jQuery("#"+treeId+'_modfiyNode').val();
			
			if(!checkFolderName("folder",$(treeId+'_modfiyNode'))){
				return;
			}		
			
			var oldName = fullName;
			jQuery("#"+id).remove();
			
			var idx = oldName.lastIndexOf(".");
			var parentName = "";
			if(idx > -1){
				parentName = oldName.substr(0,idx);
			}
			opt.modifyFolderFunc(oldName, parentName, newName);			
			
			//linkNode.text(inputValue);
			//linkNode.show();
		}		

		var treeLayer = jQuery(this);
		var modifyNode = jQuery(treeLayer.find("div[id="+nodeId+"]")[0]);
		var warpPane = jQuery("#tfn_cont_"+nodeId);		
		var newCntPane = jQuery("#"+modifyNode.attr("id")+"_newCnt");
		var linkNode = jQuery(modifyNode.find("."+opt.nodeLinkClass)[0]);
		var preText = linkNode.text();

		
		var inputPane = jQuery("<div>");
		var inputPaneId = treeId+"_tfmodifyBox";
		inputPane.attr("id",inputPaneId);
		var inputbox = jQuery("<input id='"+treeId+"_modfiyNode' type='text' size='10'/>");
		inputbox.attr("value", preText);
		var submit = jQuery("<a href='javascript:;'></a>");
		var cancel = jQuery("<a href='javascript:;'></a>");

		submit.append(jQuery("<img>").attr("src",opt.blankImgSrc)
				.attr("align","absmiddle").addClass(opt.nodeConfirmClass));
		submit.click(function(){modifyTreeNode(inputPaneId)});
		cancel.append(jQuery("<img>").attr("src",opt.blankImgSrc)
				.attr("align","absmiddle").addClass(opt.nodeCancelClass));
		cancel.click(function(){clearInputNode(inputPaneId)});		

		inputbox.bind((jQuery.browser.opera ? "keypress" : "keydown") + ".addfolder", function(event) {
			if(keyEvent(event) == 13){
				modifyTreeNode(inputPaneId);
			}
		});
		
		inputPane.append(inputbox);
		inputPane.append(submit);		
		inputPane.append(cancel);
		
		resetInputBox(treeId);
		warpPane.append(inputPane);		
		linkNode.hide();
		newCntPane.hide();
		setJobMap(treeId,inputPaneId);	

	});
}

function getDepth(nodeId){
	var depth = nodeId.split("_");
	depth = depth.length-2;
	return (depth < 0)?0:depth;
}

function childNodeView(event,opt){				
	var childViewBtn = jQuery(event.target);
	var id = jQuery(childViewBtn).attr("childName");
	
	var childView = jQuery("#"+id);
	
	if(childView.css("display") == "none"){
		childView.show();		
		childViewBtn.removeClass(opt.closeChildClass);
		childViewBtn.addClass(opt.openChildClass);		
		jQuery.cookie("DFN_"+id , "V",{path:"/"});
	} else {
		childView.hide();
		childViewBtn.removeClass(opt.openChildClass);
		childViewBtn.addClass(opt.closeChildClass);
		jQuery.cookie("DFN_"+id , "N", {path:"/"});
	}				
}

var treeJobMap = new Hash();
function setJobMap(key,value){
	treeJobMap.set(key,value);
}
function removeJobMap(key){
	treeJobMap.unset(key);
}
function getJobMap(key){
	return treeJobMap.get(key);
}
function resetInputBox(treeId){
	var preJobId = getJobMap(treeId);
	if(preJobId && preJobId != ""){		
		jQuery("#"+preJobId).parent().find("a").each(function(){jQuery(this).show();});
		jQuery("#"+preJobId).remove();
		setJobMap(treeId,"");
		removeJobMap("addNode");
	}
}

var DFTree = Class.create({
	initialize: function(name,opt,nodelist){
		this.name = name;
		this.opt = opt;
		this.nodelist = nodelist;
		this.nodeIds = [];
		this.parseList(this.nodelist);
	},	
	creatTree : function(){
		jQuery("#"+this.name).DFTree(this.name,this.nodelist,this.opt);
	},
	addNode : function(parentId,parentFullName){		
		jQuery("#"+this.name).DFTreeAdd(this.name,parentId,parentFullName,this.opt);
	},
	removeNode : function(nodeId,fullName){
		jQuery("#"+this.name).DFTreeRemove(nodeId,fullName,this.opt);
	},
	modifyNode : function(nodeId,fullName){
		jQuery("#"+this.name).DFTreeModify(this.name,nodeId,fullName,this.opt);
	},
	parseList : function(list){
		for(var i = 0 ; i < list.length ; i++){
			this.nodeIds[this.nodeIds.length] = {
					"dropid" : "#tfwrap_"+ this.name+ "_" +list[i].id,
					"id":list[i].id,
					"depth": list[i].depth,
					"fname":list[i].fullName,
					"name":list[i].name,
					"ename":list[i].encName};
			
			if(list[i].child){		
				this.parseList(list[i].child);
			}
		}
	},
	getNodeIDList : function(){
		return this.nodeIds;
	}	
	
});



jQuery.fn.simpleMenuSelector = function(menuOpt,nodeData){
	return this.each(function() {
		
		function clickMenu(event){
			jQuery("#"+type+"_sieditorLinkPane").remove();

			var eventTarget = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);
			
			var menuWL = jQuery("<div>");
			menuWL.attr("id",type+"_sieditorLinkPane");
			menuWL.css("position","relative");		
			
			var subMenuOpt = {blankImgSrc : menuOpt.blankImgSrc,width:50};
			jQuery(menuWL).makeSubMenu(subMenuOpt,menuOpt.menuLink,nodeData);
			
			jQuery("#"+nodeData.nwid).append(menuWL);
		}		

		function menuOver(){
			var preJobId = getJobMap(nodeData.treeId);			
			if(jQuery(this).parent().attr("id") != 
				jQuery("#"+preJobId).parent().parent().attr("id")){

				jQuery(this).addClass(menuOpt.overClass);			
				icon.bind("click",clickMenu);
				jQuery(this).find("div").append(icon);							
			}			
		}

		function menuOut(){
			jQuery(this).removeClass(menuOpt.overClass);
			icon.unbind("click");			
			jQuery(this).find("#"+type+"_sieditorLink").remove();
			jQuery("#"+type+"_sieditorLinkPane").remove();
		}		

		var menu = jQuery(this);
		var id = menu.attr("id");
		var type = nodeData.type;
		
		var icon = jQuery("<img>");
		icon.attr("src",menuOpt.blankImgSrc);
		
		icon.addClass(menuOpt.iconClass);			
		icon.css("cursor","pointer");
		icon.attr("id",type+"_sieditorLink");


		menu.hover(menuOver,menuOut);
	});
}

jQuery.fn.DTagTree = function(nodelist,opt){
	return this.each(function() {
		
		function drowList(sList, nodes){
			jQuery.each(nodes, function(){
				var tag = this;				
				var wrapPane = jQuery("<div>").attr("id","tnwrap_"+tag.id).attr("droptype","tag");
				wrapPane.addClass(opt.wrapClass);				
				var tagImg = jQuery("<div>").addClass(opt.folderClass);
				var color = tag.color;
				color = color.substr(1,color.length);
				tagImg.addClass("timg_"+color.toLowerCase());
				tagImg.css("float","left");
				var link = jQuery("<a href='javascript:;' id='link_tag_"+tag.id+"'></a>");				
//				link.append(tag.name);		
				link.append(escape_tag(ellipsisString(tag.name,20)));		
				var nodeData = {
					nwid : "tnwrap_"+tag.id,
					id : tag.id,						
					tagName : escape_tag(tag.name),
					tagColor : tag.color,
					tagBlockId : "tnwrap_"+tag.id,					
					type : opt.type
				};
				
				link.click(function(){opt.linkFunc(nodeData)});
				
				var listT = jQuery("<table cellspacing='0' cellpadding='0' border='0'></table>").addClass("treeNodeWrapperTable");
				var listTr = jQuery("<tr></tr>");				
				listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperIcon").append(tagImg));
				listTr.append(jQuery("<td></td>").addClass("treeNodeWrapperContents").append(link));
				listT.append(listTr);				
				wrapPane.append(listT);
				wrapPane.simpleMenuSelector(opt.menuOpt,nodeData);
				sList.append(wrapPane);
			});
		}
		
		var tagLayer = jQuery(this);
		drowList(tagLayer,nodelist)
		
	});
}


jQuery.fn.DSearchFolderTree = function(nodelist,opt){
	return this.each(function() {
		
		function drowList(sList, nodes){
			jQuery.each(nodes, function(){
				var sfolder = this;				
				var wrapPane = jQuery("<div>").attr("id","sfnwrap_"+sfolder.id);
				wrapPane.addClass(opt.wrapClass);				
				var folderImg = jQuery("<div>").addClass(opt.folderClass);				
				var link = jQuery("<a href='javascript:;' id='link_sfolder_"+sfolder.id+"'></a>");
				link.append(escape_tag(sfolder.name));
				var nodeData = {
					nwid : "sfnwrap_"+sfolder.id,
					id : sfolder.id,						
					name : escape_tag(sfolder.name),
					query : sfolder.query,
					type : opt.type
				};
				link.click(function(){opt.linkFunc(nodeData)});
				var listT = jQuery("<table cellspacing='0' cellpadding='0' border='0'></table>").addClass("treeNodeWrapperTable");
				var listTr = jQuery("<tr></tr>");				
				listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperIcon").append(folderImg));
				listTr.append(jQuery("<td></td>").addClass("treeNodeWrapperContents").append(link));
				listT.append(listTr);
				wrapPane.append(listT);				
				wrapPane.simpleMenuSelector(opt.menuOpt,nodeData);
				sList.append(wrapPane);
			});
		}
		
		var folderLayer = jQuery(this);
		drowList(folderLayer,nodelist)
		
	});
}


jQuery.fn.DSharedFolderTree = function(nodelist,opt){
	return this.each(function() {
		
		function drowList(sList, nodes){
			jQuery.each(nodes, function(){
				var shfolder = this;				
				var wrapPane = jQuery("<div>").attr("id","shfnwrap_"+shfolder.id);
				wrapPane.addClass(opt.wrapClass);				
				var folderImg = jQuery("<div>").addClass(opt.folderClass);				
				var link = jQuery("<a href='javascript:;' id='link_shfolder_"+shfolder.encname+"'></a>");
				link.append(shfolder.name);
				var nodeData = {						
					id : shfolder.id,						
					name : shfolder.name,
					encname : shfolder.encname,
					userid : shfolder.userid					
				};
				link.click(function(){opt.linkFunc(nodeData)});
				var info = jQuery("<span>");
				info.append("&nbsp;["+shfolder.username+"("+shfolder.useruid+")]");
				
				var listT = jQuery("<table cellspacing='0' cellpadding='0' border='0'></table>").addClass("treeNodeWrapperTable");
				var listTr = jQuery("<tr></tr>");				
				listTr.append(jQuery("<td nowrap></td>").addClass("treeNodeWrapperIcon").append(folderImg));
				listTr.append(jQuery("<td></td>").addClass("treeNodeWrapperContents").append(link).append(info));
				listT.append(listTr);				
				wrapPane.append(listT);								
				sList.append(wrapPane);
			});
		}
		
		var folderLayer = jQuery(this);
		drowList(folderLayer,nodelist)
		
	});
}

jQuery.fn.folderMenuSelector = function(menuOpt,data){
	return this.each(function() {
		
		function clickMenu(event){
			jQuery("#"+type+"_sieditorLinkPane").remove();

			var eventTarget = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);
			var offset = eventTarget.offset();
			
			var left = parseInt(offset.left);

			var menuWL = jQuery("<div>");
			menuWL.attr("id",type+"_sieditorLinkPane");
			menuWL.css("position","relative");			
			
			var subMenuOpt = {blankImgSrc : menuOpt.blankImgSrc,width:80};
			jQuery(menuWL).makeSubMenu(subMenuOpt,menuOpt.menuLink,data);					
		
			eventTarget.parent().append(menuWL);
		}		

		function menuOver(){
			if(!jQuery(this).hasClass("ufolder_work")){
				jQuery(this).addClass(menuOpt.overClass);			
				icon.bind("click",clickMenu);
				jQuery(this).find("div").append(icon);
			}
		}

		function menuOut(){
			jQuery(this).removeClass(menuOpt.overClass);
			icon.unbind("click");			
			jQuery(this).find("#"+type+"_sieditorLink").remove();
			jQuery("#"+type+"_sieditorLinkPane").remove();
		}		

		var menu = jQuery(this);
		var id = menu.attr("id");
		var type = "fm";
		
		var icon = jQuery("<img>");
		icon.attr("src",menuOpt.blankImgSrc);
		
		icon.addClass(menuOpt.iconClass);			
		icon.css("cursor","pointer");
		icon.attr("id",type+"_sieditorLink");


		menu.hover(menuOver,menuOut);
	});
};


jQuery.fn.folderManageMenu = function(opt){
	var fList = jQuery(this);
	var idx = 0;
	jQuery.each(fList,function(){
		var folderElement = jQuery(this);
		var fname = folderElement.attr("fname");
		var fullname = folderElement.attr("fullname");
		var depth = folderElement.attr("depth");
		var sid = folderElement.attr("sid");
		var sqval = folderElement.attr("qval");
		var tcolor = folderElement.attr("tcolor");
		var shared = folderElement.attr("shared");
		shared = (shared == "true")?true:false;
		var sharedUid = folderElement.attr("sharedUid");
		sharedUid = Number(sharedUid);
		if(Number(depth) == 1){
			folderElement.addClass("ufolder_level1");
		} else if(Number(depth) == 2){
			folderElement.addClass("ufolder_level2");
		}		
		var data = {
				"fname":fname,
				"fullName":fullname,
				"idx":idx,
				"depth":depth,
				"sid":sid,
				"sqval":sqval,
				"tcolor":tcolor,
				"shared":shared,
				"sharedUid":sharedUid
				};
		folderElement.folderMenuSelector(opt,data);
		idx++;
	});
};