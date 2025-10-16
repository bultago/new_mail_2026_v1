var menuPage = 1;
var maxPage = 1;
var menuType = "folder";
function goMenu(id, type) {	
	if (id != "") {
		id = id+"_";
	}
	if (type == "pre" && menuPage == 1) {
		return;
	}
	else if (type == "pre" && menuPage > 1) {		
		menuPage--;
		document.getElementById("menu_flist").innerHTML = getFolderListEl(menuType);
	}
	else if (type == "next" && menuPage < maxPage) {		
		menuPage++;
		document.getElementById("menu_flist").innerHTML = getFolderListEl(menuType);
	}
	else if (type == "next" && menuPage == maxPage) {
		return;
	}
}

function viewSelectMenu(evt, type) {
	var menu = document.getElementById("select_menus");
	var box = document.getElementById("menu_list");
		
	if (type == 'move' || type == 'flag') {
		menu.style.marginRight = 0+"px";
		box.style.top = 70+"px";
	} else if (type == 'moveBottom' || type == 'flagBottom') {
		var posY = 0;
		if (isMsie) {			
			posY = evt.srcElement.parentNode.parentNode.offsetTop;
			posY = posY + 10;
		} else {
			posY = evt.pageY;
		}		
		
		menu.style.marginRight = 0+"px";
		if(type == 'flagBottom'){
			box.style.top = (posY-190)+"px";
		}else {
			box.style.top = (posY-300)+"px";
		}
		
	} else if (type == 'folder') {
		menu.style.marginRight = "auto";
		box.style.top = -7+"px";	
		
	}
	
	if(menuType != type){
		menu.style.display = 'none';
	}

	menuType = type;
	document.getElementById("menu_flist").innerHTML = getFolderListEl(type);	
	document.getElementById("menu_flist").style.height = (type == 'flag' || type == 'flagBottom')?"50px":"170px";
	
	if (menu.style.display != 'block') {
		menu.style.display = 'block';
	} else {
		menu.style.display = 'none';
		closeMenuBox('');
	}
}


function getFolderListEl(type){
	var folderList = [];
	var startCnt,endCnt;
	var menuHtml = "";
	if(type == "folder"){	
		for ( var i = 0; i < dfolderList.length; i++) {
			folderList.push(dfolderList[i]);
		}
	} else if(type == "move" || type == "moveBottom"){
		for ( var i = 0; i < dfolderList.length; i++) {
			if(dfolderList[i].id != "menu_item_Sent" &&
				dfolderList[i].id != "menu_item_Draft" &&
				dfolderList[i].id != "menu_item_mdn"){
				folderList.push(dfolderList[i]);
			}
		}
	} else if(type == "addrPGroup"){	
		folderList = addrGroupList;
	} else if(type == "addrGGroup"){
		folderList = addrGGroupList;
	} else if(type == "addrGBook"){
		folderList = addrGBookList;
	} else if(type == "flag" || type == "flagBottom"){
		folderList = seenFlagList;
	} else if(type == "calendar") {
		folderList = calendarMenu;
	} else if(type == "bbs") {
		folderList = bbsMenu;
	}
	
	if(type == "move" || type == "moveBottom" || type == "folder"){
		for ( var i = 0; i < ufolderList.length; i++) {
			folderList.push(ufolderList[i]);
		}
	}
	
	var fsize = folderList.length;
	
	maxPage = Math.ceil(fsize / 6);
	
	var nbtnStyle, pbtnStyle, nbtnLeft,bboxStyle;
    pbtnStyle = (menuPage == 1)?"none":"block";
    nbtnStyle = (maxPage == 0 || menuPage == maxPage)?"none":"block";
    nbtnLeft = (menuPage == 1)?"0px":"30px";
    bboxStyle = (maxPage == 0 || (menuPage == 1 && menuPage == maxPage)) ? "block":"none";
    var btnHtml = '<div id="blank_box" class="blankBox" style="display:'+bboxStyle+';">&nbsp;</div>'
    			+'<a href="javascript:;" onclick="goMenu(\'\',\'pre\')" id="mboxBtnPre" class="btn_sub_prev" style="position: absolute; top:0px; left:0px;display:'+pbtnStyle+';">prev</a>'
    			+'<a href="javascript:;" onclick="goMenu(\'\',\'next\')" id="mboxBtnNext" class="btn_sub_next" style="position: absolute; top:0px; left:'+nbtnLeft+';display:'+nbtnStyle+'">next</a>'
    			+'<a href="javascript:;" onclick="closeMenuBox(\'\')" class="btn_close x">X</a>';
	
	startCnt = (menuPage-1) * 6;
	endCnt = startCnt + 6;
	endCnt = (endCnt > fsize)?fsize:endCnt;
	for ( var i = startCnt; i < endCnt; i++) {
		folderName = getEmptySpace(folderList[i].depth) + folderList[i].name;
		menuHtml +="<li><label><a href=\""+folderList[i].link+"\">"+folderName+"</a></label></li>";		
	}
	document.getElementById("subMenuBox").innerHTML = btnHtml;
	return menuHtml;
}
function getEmptySpace(depth){
	var empty = "";
	for ( var i = 0; i < depth; i++) {
		empty += "&nbsp;&nbsp;";
	}
	
	return empty;
}
function closeMenuBox(id) {
	var menu = document.getElementById("select_menus");
	menu.style.display = 'none';	
	document.getElementById("menu_flist").innerHTML = "";
	document.getElementById("subMenuBox").innerHTML = "";
	menuPage = 1;
}

function folderAction(folderName) {
	if (menuType == 'folder') {
		if(pageCategory == "write"){
			var f = document.writeForm;				
			if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){
				if(!confirm(mailMsg.confirm_escapewrite)){
					return;
				}
			}
		}
		this.location = "/hybrid/mail/mailList.do?folderName="+encodeURIComponent(encodeURIComponent(folderName));
	} else if (menuType == 'move' || menuType == 'moveBottom') {
		moveMessage(folderName);
	}
	closeMenuBox('');
}

function mdnfolderAction(){
	if(pageCategory == "write"){
		if(isExistReceiveAddr('to') || isExistReceiveAddr('cc') || isExistReceiveAddr('bcc') || trim(f.subject.value) != "" || trim(f.content.value) != ""){
			if(!confirm(mailMsg.confirm_escapewrite)){
				return;
			}
		}
	}
	this.location = "/hybrid/mail/mailMdnList.do";
}

function toggleAllSelect(list) {
	var allSelect1 = document.getElementById("all_select1");
	var allSelect2 = document.getElementById("all_select2");
	var allAtt1 = allSelect1.attributes;
	var allAtt2 = (allSelect2)?allSelect2.attributes:null;
	var check = allAtt1.getNamedItem("check").value;
	var flag = false;
	var nextCheck = "";
	
	if (check == "off") {
		flag = true;
		nextCheck = "on";
	} else {
		flag = false;
		nextCheck = "off";
	}
	
	if (!list) {
		return;
	}

	if (list.length) {
		for (var i = 0; i < list.length; i++) {
			list[i].checked = flag;
		}
	} else {
		list.checked = flag;
	}
	
	allAtt1.getNamedItem("check").value = nextCheck;
	if(allSelect2)allAtt2.getNamedItem("check").value = nextCheck;
}

function goWrite(){
	document.location = "/hybrid/mail/mailWrite.do";
}

function executeUrl(url, paramArray, isPopup) {

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
	if (isPopup) {
		window.open(url+param);
	} else {		
		this.location = url+param;
	}
	paramArray = null;
}

function makeMultiValue(obj) {
	var resultArray = [];
	var dataArray;
	var folderName="";
	var uid="";
	 
	if (obj) {
		if(obj.length) {
			for (var i=0; i<obj.length; i++) {
				if (obj[i].checked) {
					dataArray = obj[i].value.split('|');
					folderName += dataArray[0] + "|";
					uid += dataArray[1] + "|";
				}
			}
		} else {
			if (obj.checked) {
				dataArray = obj.value.split('|');
				folderName += dataArray[0] + "|";
				uid += dataArray[1] + "|";
			}
		}
		resultArray.push({name:"folderName",value:folderName});
		resultArray.push({name:"uid",value:uid});
	}
	return resultArray;
}

function makeMultiParam(obj) {
	var param = "";
	var checkCnt = 0;
	if (obj) {
		if(obj.length) {
			for (var i=0; i<obj.length; i++) {
				if (obj[i].checked) {
					if(checkCnt > 0)param +="|";
					param += trim(obj[i].value);
					checkCnt++;
					
				}
			}
		} else {
			if (obj.checked) {
				param += obj.value;
			}
		}
	}
	return param;
}

function checkOS(){
	 
    var ua = window.navigator.userAgent.toLowerCase();

    if(ua.indexOf("android") > -1)
     return "android";
    else if (ua.indexOf("iphone") > -1)
     return "iphone";
    else 
     return -1;
}

function isExistReceiveAddr(type){
	
	var wrap = jQuery("#"+type+"AddrWrap");
	var isExist = false;
	wrap.children().each(function(i){
		
		if(jQuery(this).css("display") == "block"){		
			isExist =  true;
		}
	});
	
	return isExist;
}

function makeAddrWrap(receive,addr){
	
	var addrStr = escape_tag(addr);
	
	if(addrStr.indexOf(",") > -1){
		addrStr = addrStr.substring(0,addrStr.indexOf(","));
	}
	
	jQuery("#"+receive+"AddrWrap").append("<span class='addrRadiusWrap' data-addr='"+addrStr+"'><span class='addrWrapElli'>"
			+addrStr+
			"</span><span class='addrWrapdeleteImg' onclick='deleteAddrWrap(this)'></span></span>");
}