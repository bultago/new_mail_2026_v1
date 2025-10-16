/* About DropDown Menu */
var viewTime = null;
var viweStatus;

function menuLayerOpen(id) {
	if ( viweStatus == 'out' || viweStatus != id ) {
		return;
	}
	
	if ( viweStatus == 'ly_etc_dn1') {
		
		if(isMsie){
			var height = findPosY(document.getElementById("etc_dn_sp"));
			document.getElementById(id).style.pixelTop = height+20;
		}
		else {
			var height = findPosY(document.getElementById("etc_dn_sp"));
			document.getElementById(id).style.top = height+13+"px";
		}
	}

	clearTimeout(viewTime);
	document.getElementById(id).style.display='inline';
}

function menuLayerClose(id) {
	if ( viweStatus != 'out' ) {
		return;
	}
	document.getElementById(id).style.display='none';
}

function menuLayerOut(id) {
	// menuLayerClose(id);
	viewTime = setTimeout("menuLayerClose('"+id+"')",300);
}
/* /About DropDown Menu */

function findPosY(obj){
 var curtop = 0;
 if(obj.offsetParent){
  while(obj.offsetParent){
   curtop += obj.offsetTop;
   obj = obj.offsetParent;
  }
 }else if(obj.y) curtop += obj.y;

 return curtop;
}

function setFolderName(item, itemName, target) {
	
	var count = 0;

	for (i=0;i<item.length;i++) {
		if(item.charAt(i) ==".")
		count++;
	}

	if (count > 0) {
		itemName = item.replace(/\./g, "/");
	}
	
	if (target == item) {
		jQuery('#parentFolder_default').text(itemName);
	}
}

function selPFolder(fName, rName, b, target) {	

	if (rName.indexOf(".") != -1) {
		fName = rName.replace(/\./g, "/");
	}

	if (target == 'new') {
		jQuery('#parentFolder_new').text(fName);
		jQuery('#parentFolder').val(rName);
	
		if (b) {
			viweStatus='out'; menuLayerOut('newPfBox');
			jQuery('#newPfBox').hide();
		}
	}
	else {
		jQuery('#parentFolder_default').text(fName);
		if (rName != '') {
			jQuery('#policy').val("move "+rName);
		}
		else {
			jQuery('#policy').val('');
		}

		if (b) {
			viweStatus='out'; menuLayerOut('pfBox');
			jQuery('#pfBox').hide();
		}
	}
}

function setFolderList(name, aliasName, target) {
	var count = 0;
	var index = 0;
	var liStr = "";
	var isNew = false;
	for (var i=0;i<name.length;i++) {
		if(name.charAt(i) ==".")
		count++;
	}

	/* Folder Depth */
	if ((count == 2) && (target != 'new')) {
		liStr += "<li class='list_st_dep3 left_folderlist' ";
	}
	else if ((count == 2) && (target == 'new')) {
		isNew = true;
	}
	else if (count == 1) {
		liStr += "<li class='list_st_dep2 left_folderlist' ";
	}
	else if ((target == 'new') && 
			((name == 'Inbox') || 
					(name == 'Spam') || 
					(name == 'Trash') || 
					(name == 'Reserved') ||
					(name == 'Drafts') ||
					(name == 'Quotaviolate'))) {
		isNew = true;
	}
	else {
		liStr += "<li class='left_folderlist' ";
	}

	if (!isNew) {
		liStr += "onmouseover='yellowline(this)' onmouseout='grayline(this)'";
		liStr += ">";
		liStr += "<a href=\"javascript:selPFolder('"+aliasName+"','"+name+"', true, '"+target+"')\">";
		liStr += aliasName;
		liStr += "</a></li>";		
	}
	
	return liStr;
}

