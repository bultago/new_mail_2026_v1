jQuery.fn.makeUploadFrame = function(name){	
	var hframe = jQuery(this);
	hframe.empty();
	hframe.html("<iframe name='"+name+"' id='"+name+"' src='about:blank' frameborder='0' width='1' height='1' style='width:1px;height:1px;'></iframe>");
}

function innerHtml(el, html) {
	if( el ) {
        var oldEl = (typeof el === "string" ? document.getElementById(el) : el);
        var newEl = document.createElement(oldEl.nodeName);

        // Preserve any properties we care about (id and class in this example)
        newEl.id = oldEl.id;
        newEl.className = oldEl.className;

        //set the new HTML and insert back into the DOM
        newEl.innerHTML = html;
        if(oldEl.parentNode)
	        oldEl.parentNode.replaceChild(newEl, oldEl);
        else
        oldEl.innerHTML = html;

        //return a reference to the new element in case we need it
        return newEl;
	}
};


var StringBuffer = Class.create({
	initialize: function () {
        this._buffer = [];
    },
    append: function (str) {
        this._buffer.push(str);
        return this;
    },
    getBuffer:function(){
    	return this._buffer;
    },
    toString: function () {
        var delimiter = arguments.length ? arguments[0] : "";
        return this._buffer.join(delimiter);
    },
    length: function () {
        return this._buffer.join("").length;
    },
    destroy: function () {
        this._buffer = [];
    }
});

var HashMap = Class.create({
	initialize: function () {
        this.map = [];
        this.keys = [];
    },
    getKeys: function(){
    	return this.keys;
    },
    put: function (key,value) {
    	if(this.map[key] != null){
    		this.remove(key);
    	}
    	this.map[key] = value;
    	this.keys.push(key);
    },
    get: function (key) {
    	return this.map[key];
    },
    getValues: function () {
    	var size = this.size();
    	var values = [];
    	for (var i = 0 ; i < size; i++){
    		values.push(this.get(this.keys[i]));
    	}
    	
    	return values;
    },
    remove: function (key){
    	var newMap = [];
    	var newKey = [];
    	var size = this.size();
    	for (var i = 0 ; i < size ; i++){
    		if(this.keys[i] != key){
    			newMap[this.keys[i]] = this.get(this.keys[i]);
    			newKey.push(this.keys[i]);
    		}    		
    	}
    	this.map = newMap;
        this.keys = newKey;
    },
    toString: function () {
    	var delimiter = arguments.length ? arguments[0] : "";
        return this._buffer.join(delimiter);
    },
    size: function () {
        return this.keys.length;
    },
    destroy: function () {
        this.map = [];
        this.keys = [];
    }
});

jQuery.fn.folderSelectList = function(fid,flist,selectItem,type,func){
	var ptype = "m";
	if(type){
		ptype= type;	
	}
	
	var exeFunc = "";
	if (func) exeFunc = func;
	    
	var select = jQuery(this);
	var folderArray = [];
	var selectFolderValue = selectItem;
	if(ptype == "s"){
		folderArray.push({index:comMsg.comn_allfolder,value:"all"});		
	}
	for (var i = 0 ; i < flist.length ; i++) {		
		
		if(flist[i].fname != "Reserved" &&
				flist[i].fname != "Drafts" &&
				flist[i].fname != "Quotaviolate"){
			var folderName = flist[i].name;			
			if(flist[i].depth == 1){
				folderName = "&nbsp;&nbsp;&nbsp;&nbsp;"+folderName;
			} else if(flist[i].depth == 2){
				folderName = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+folderName;
			}
			if(selectItem && (flist[i].fname == selectItem)){
				selectFolderName = flist[i].name;
			}		
			
			folderArray.push({index:escape_tag(folderName),value:flist[i].fname});		
		}
	}
	
	select.selectbox({selectId:fid,selectFunc:exeFunc,width:150},selectFolderValue,folderArray);
};

jQuery.fn.timeSelectList = function(fid, term, end, unit, selectItem){
	
	var select = jQuery(this);
	var checkVal = parseInt(Number(selectItem)/term) * term;	
	if(checkVal < 10) checkVal = "0"+checkVal;
	
	var timeArray = [];
	for (var i = 0 ; i <= end ; ) {
		var val = i;
		if(i < 10) val = "0"+i;
		
		timeArray.push({index:(val+unit),value:val});
		i = i + term;
	}	
	select.selectbox({selectId:fid,selectFunc:"",width:60},checkVal,timeArray);
};

jQuery.fn.folderListDrow = function(flist,folderImg,linkFunc,type){
	var drowList = jQuery(this);	
	var drowListWrap = jQuery("<ul>");	
	for (var i = 0 ; i < flist.length ; i++) {
		if(flist[i].fname != "Sent" && 
			flist[i].fname != "Reserved" &&
			flist[i].fname != "Drafts" &&
			flist[i].fname != "Quotaviolate"){
			var folderName = escape_tag(flist[i].name);
			
			var fItem = jQuery("<li></li>");			
			fItem.addClass("flist_item");
			fItem.append(jQuery("<img>").attr("src",folderImg).css("margin-right","5px"));			
			fItem.append(jQuery("<span></span>").addClass("jpf").append(folderName).attr("title",folderName));
			fItem.hover(function(){jQuery(this).addClass("flist_item_over");},
					function(){jQuery(this).removeClass("flist_item_over");});
			fItem.attr("fname",flist[i].fname);			
			fItem.click(function(){linkFunc(jQuery(this),type);});
			if(flist[i].depth == 1){
				fItem.css("margin-left","10px");
			} else if(flist[i].depth == 2){
				fItem.css("margin-left","15px");
			}			
			drowListWrap.append(fItem);
		}
	}
	drowList.append(drowListWrap);
	return drowList;
};

jQuery.fn.tagListDrow = function(tmap,addFunc,removeFunc){
	var drowList = jQuery(this);
	
	var drowListWrap = jQuery("<ul>");
	var tagNames = tmap.keys();
	
	for (var i = 0 ; i < tagNames.length ; i++) {			
			var tagName = tagNames[i];
			var tagInfo = tmap.get(tagName);
			var tagColor = tagInfo.color;
			tagColor = replaceAll(tagColor,"#","");
			var tItem = jQuery("<li></li>");
			tItem.addClass("flist_item");
			tItem.append(jQuery("<div></div>").css({"float":"left","width":"70px","overflow":"hidden","text-overflow":"ellipsis"})
						.append(
							jQuery("<img src='/design/common/image/blank.gif'>")
							.addClass("tagimg_base").addClass("timg_"+tagColor.toLowerCase()).css({"margin-top":"3px"})
						).append(jQuery("<span></span>").append(" "+tagName)));
			tItem.append(jQuery("<div></div>").attr("tid",tagInfo.id).css({"float":"left","text-align":"right","width":"55px"})
					.append(jQuery("<img class='tcconfirmIcon' align='absmiddle' src='/design/common/image/blank.gif'/>").attr("title",comMsg.comn_add).click(function(){addFunc(jQuery(this).parent());}))					
					.append(jQuery("<img class='tccancelIcon' align='absmiddle' src='/design/common/image/blank.gif'/>").attr("title",comMsg.comn_del).click(function(){removeFunc(jQuery(this).parent());}))
			);			
			tItem.hover(function(){jQuery(this).addClass("flist_item_over");},
					function(){jQuery(this).removeClass("flist_item_over");});
			tItem.attr("tid",tagInfo.id);
			tItem.click(function(){addFunc(jQuery(this));});
			tItem.attr("title",tagName);
			drowListWrap.append(tItem);
		
	}
	drowList.append(drowListWrap);
	return drowList;
};

jQuery.fn.pageNavigation = function(mode,opt){
	var pageCnt;	
	function afterProcess(data,textStatus){
		var func = opt.changeAfter;		
		if(textStatus == "success" && data.result == "success"){
			USER_PAGEBASE = pageCnt;
			if(func){				
				func();
			}
		}
	}
	
	function changeLineCnt(val){		
		var param = {"pageLineCnt":val};		
		jQuery.post("/setting/updateUserInfo.do",param,afterProcess,"json");				
	}
	
	function getNextWindow(total,pageSize,page,windowSize) {
        var npages = Math.ceil(total/pageSize);
        var mod = page % windowSize;
        var p = page + windowSize - ((mod != 0)? mod : windowSize) + 1;

        if (p <= npages) {
            return p;
        }
        else {
            return -1;
        }
    }
	
	function getPrevWindow(page,windowSize) {
        if (page > windowSize) {
            var mod = page % windowSize;
            return page - windowSize - ((mod != 0)? mod : windowSize) + 1;
        }
        else {
            return -1;
        }
    }
	
	
	var pageNavi = jQuery(this);
	pageNavi.empty();	
	var pageNaviId = pageNavi.attr("id");
	var pageDrow,pageList;
	var pageArray = [];
	var page = 0;
	var pageBase;
	var pageStart = 1;
	var pageEnd = 1;
	var npages;
	var marginStyle = {};
	var preTerm,nextTerm;
	var naviSize = 13;
	var naviImgSize = 12;
	var isNaviBold = false;
	
	if(isMsie6){
		marginStyle.preTerm = "margin-top:5px;";			
		marginStyle.bar = "padding-top:2px;";			
		marginStyle.nextTerm = "margin-top:5px;";
	} else if(isMsie7){
		marginStyle.preTerm = "padding-top:7px;";
		marginStyle.bar = "padding-top:5px;";			
		marginStyle.nextTerm = "padding-top:7px;";
	} else if(jQuery.browser.safari){
		marginStyle.preTerm = "padding-top:7px;";
		marginStyle.bar = "padding-top:4px;";			
		marginStyle.nextTerm = "padding-top:7px;";
	} else {
		marginStyle.preTerm = "padding-top:6px;";
		marginStyle.bar = "padding-top:4px;";			
		marginStyle.nextTerm = "padding-top:6px;";
	}
	
	if(mode == "p"){
		page = parseInt(opt.page);
		var total = parseInt(opt.total);
		var base = parseInt(opt.base);
		
		npages = Math.ceil(total/base);
		var pagetotal = npages - page;
		
		
		pageStart = Math.floor(page/20) * 20;
		
		if(pageStart == page){
			pageStart = pageStart -19;
		} else {
			pageStart = pageStart + 1;
		}
		
		if(pagetotal > 20){
			pageEnd = pageStart + 19;		
		} else {
			pageEnd = npages;
		}
		
		nextTerm = getNextWindow(total,base,page,20);
		preTerm = getPrevWindow(page,20);
		
		
		
		pageDrow = "<div class='navi_content mail_pagenavi' >";		
		
		var pagingWrapper = "<div style='float:left;'>";
		
		if(preTerm > 0){
			pagingWrapper += "<div style='float:left;"+marginStyle.preTerm+"'>" +
					"<a href='javascript:movePage("+preTerm+");'>" +
					"<img src='/design/common/image/ic_paging_term_pre.gif' " +
					"class='navi_img'/></a>" +
					"</div>";
		}
		
		if(page > 1){
			pagingWrapper += "<div style='float:left;'>" +
					"<a href='javascript:movePage("+(page-1)+");' class='pre'>" +
					"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;' src='/design/common/image/ic_paging_pre.png' class='navi_img'/>" +
					"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_prelist+"</span></a></div>";			
			
			if(page < npages){
				pagingWrapper += "<div style='font-weight:"+(isNaviBold?"bold":"")+";float:left;"+marginStyle.bar+"'>|</div>";
			}			
			
		}		
		
		if(page < npages){			
			pagingWrapper += "<div style='float:left;'>" +
					"<a href='javascript:movePage("+(page+1)+");' class='next'>" +
					"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_nextlist+"</span>" +
					"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;' src='/design/common/image/ic_paging_next.png' class='navi_img'/></a></div>";			
		}
		
		if(nextTerm > 0){
			pagingWrapper += "<div style='float:left;"+marginStyle.nextTerm+"'>" +
					"<a href='javascript:movePage("+nextTerm+");'>" +
					"<img src='/design/common/image/ic_paging_term_next.gif' class='navi_img'/>" +
					"</a></div>";			
		}
		pagingWrapper += "</div>";
		pageDrow += pagingWrapper;
		
				
	} else if(mode == "n"){		
		var prePageUid = parseInt(opt.preUid);
		var nextPageUid = parseInt(opt.nextUid);
		
		pageDrow = "<div class='navi_content mail_pagenavi' style='margin-top:30px;' >";
		
		var pagingWrapper = "<div style='float:left;'>";
		
		if(prePageUid > 0){
			
			pagingWrapper += "<div style='float:left;padding-top: 3px;'>" +
			"<a href=\"javascript:readMessage('"+opt.folderName+"',"+prePageUid+");\" class='pre'>" +
			"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;' src='/design/common/image/ic_paging_pre.png' class='navi_img'/>" +
			"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_prelist+"</span></a></div>";
			
			if(opt.isListSet || nextPageUid > 0){
				pagingWrapper += "<div style='font-weight:"+(isNaviBold?"bold":"")+";float:left;"+marginStyle.bar+"'>|</div>";
			}
		}
		
		if(opt.isListSet){
			pageList = "<div style='float:left;padding-top: 4px;'>" +
					"<a href='javascript:listPage();' class='plist'>" +
					"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_list+"</span></a></div>";
			
			pagingWrapper += pageList;				
		}		
		
		if(nextPageUid > 0){
			if(opt.isListSet){
				pagingWrapper += "<div style='font-weight:"+(isNaviBold?"bold":"")+";float:left;"+marginStyle.bar+"'>|</div>";
			}			
			pagingWrapper += "<div style='float:left;padding-top: 3px;'>" +
			"<a href=\"javascript:readMessage('"+opt.folderName+"',"+nextPageUid+");\" class='next'>" +
			"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_nextlist+"</span>" +
			"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;' src='/design/common/image/ic_paging_next.png' class='navi_img'/>"+
			"</a></div>";				
			
			
		}
		
		pagingWrapper += "</div>";
		pageDrow += pagingWrapper;
		
	}
	else if(mode == "b"){		
		var preContentId = parseInt(opt.preContentId);
		var nextContentId = parseInt(opt.nextContentId);
		
		pageDrow = "<div class='navi_content mail_pagenavi' style='margin-top:30px;' >";
		
		var pagingWrapper = "<div style='float:left;'>";
		
		if(preContentId > 0){	
			
			pagingWrapper += "<div style='float:left;padding-top: 3px; '>" +
			"<a href=\"javascript:viewContent('"+preContentId+"','"+opt.preParentId+"','"+opt.preOrderNo+"');\" class='pre'>" +			
			"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;' src='/design/common/image/ic_paging_pre.png' class='navi_img'/>" +
			"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_prelist+"</span></a></div>";
	
			pagingWrapper += "<div style='font-weight:"+(isNaviBold?"bold":"")+";float:left;"+marginStyle.bar+"'>|</div>";
		}
		
		if(opt.isListSet){
			pageList = "<div style='float:left;padding-top: 4px;'>" +
			"<a href='javascript:listPage();' class='plist'>" +
			"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_list+"</span></a></div>";
	
			pagingWrapper += pageList;
		}
		
		if(nextContentId > 0){	
			
			pagingWrapper += "<div style='font-weight:"+(isNaviBold?"bold":"")+";float:left;"+marginStyle.bar+"'>|</div>";
			
			pagingWrapper += "<div style='float:left;padding-top: 3px; '>" +
			"<a href=\"javascript:viewContent('"+nextContentId+"','"+opt.nextParentId+"','"+opt.nextOrderNo+"');\" class='next'>" +
			"<span style='font-weight:"+(isNaviBold?"bold":"")+";font-size:"+naviSize+"px;'>"+comMsg.comn_nextlist+"</span>" +
			"<img style='width:"+naviImgSize+"px;height:"+naviImgSize+"px;'src='/design/common/image/ic_paging_next.png' class='navi_img'/></a></div>";			
			
		}
		
		pagingWrapper += "</div>";
		pageDrow += pagingWrapper;
	}
	
	if(mode == "p" || mode == "n" || mode == "b"){		
		
		if(mode == "p"){
			if(pageEnd > 1){			
				for ( var i = pageStart ; i <= pageEnd; i++) {
					pageArray.push({index:i,value:i});						
				}
				
				pageDrow += "<div id='pagingSelect' style='display:block' class='navi_content'></div>";			
			}
		}
		
		if(opt.isLineCntSet){
			pageDrow += "<div id='pagingBase' style='display:block' class='navi_content'></div>";			
		}		
		
		pageDrow += "</div>";
		$(pageNaviId).innerHTML = pageDrow;
		jQuery("#toolbarSide").css("background", "none");
	}
	
	if(mode == "p" && $("pagingSelect")){
		jQuery("#pagingSelect").selectbox({selectId:"paging",
			selectFunc:movePage},page,pageArray);
	}
	
	if((mode == "p" || mode == "n" || mode == "b") 
			&& opt.isLineCntSet &&  $("pagingBase")){		
		pageBase = opt.base;
		
		jQuery("#pagingBase").selectbox({selectId:"pageBaseVal",
				selectFunc:changeLineCnt,width:110},Number(pageBase),
				[{index:comMsg.comn_listsetup+"(15)",value:15},
				 {index:comMsg.comn_listsetup+"(20)",value:20},
				 {index:comMsg.comn_listsetup+"(25)",value:25},
				 {index:comMsg.comn_listsetup+"(30)",value:30},
				 {index:comMsg.comn_listsetup+"(50)",value:50},
				 {index:comMsg.comn_listsetup+"(80)",value:80}]);
	}	
};
function pagingMouseOver(obj){
	obj.style.cursor = "pointer";
	obj.style.color = "#ff8000";
}

function pagingMouseOut(obj){
	if(jQuery(obj).attr("class") != "pageStyleSelect"){
		obj.style.color = "#555555";
	}
}

jQuery.fn.pageBottomNavigation = function(mode,opt){
	var pageCnt;	
	function afterProcess(data,textStatus){
		var func = opt.changeAfter;		
		if(textStatus == "success" && data.result == "success"){
			USER_PAGEBASE = pageCnt;
			if(func){				
				func();
			}
		}
	}
	
	function changeLineCnt(val){		
		var param = {"pageLineCnt":val};		
		jQuery.post("/setting/updateUserInfo.do",param,afterProcess,"json");				
	}
	
	function getNextWindow(total,pageSize,page,windowSize) {
        var npages = Math.ceil(total/pageSize);
        var mod = page % windowSize;
        var p = page + windowSize - ((mod != 0)? mod : windowSize) + 1;

        if (p <= npages) {
            return p;
        }
        else {
            return -1;
        }
    }
	
	function getPrevWindow(page,windowSize) {
        if (page > windowSize) {
            var mod = page % windowSize;
            return page - windowSize - ((mod != 0)? mod : windowSize) + 1;
        }
        else {
            return -1;
        }
    }
	
	
	var pageNavi = jQuery(this);
	pageNavi.empty();	
	var pageNaviId = pageNavi.attr("id");
	var pageDrow = "";
	var pageList = "";
	var pageArray = [];
	var page = 0;
	var pageBase;
	var pageStart = 1;
	var pageEnd = 1;
	var npages;
	var marginStyle = {};
	var preTerm,nextTerm;	
	var naviSize = 13;
	var naviImgSize = 12;
	
	if(isMsie6){
		marginStyle.preTerm = "margin-top:5px;";			
		marginStyle.bar = "padding-top:2px;";			
		marginStyle.nextTerm = "margin-top:5px;";
	} else if(isMsie7){
		marginStyle.preTerm = "padding-top:7px;";
		marginStyle.bar = "padding-top:5px;";			
		marginStyle.nextTerm = "padding-top:7px;";
	} else if(jQuery.browser.safari){
		marginStyle.preTerm = "padding-top:7px;";
		marginStyle.bar = "padding-top:4px;";			
		marginStyle.nextTerm = "padding-top:7px;";
	} else {
		marginStyle.preTerm = "padding-top:6px;";
		marginStyle.bar = "padding-top:4px;";			
		marginStyle.nextTerm = "padding-top:6px;";
	}
	var total = 0;
	var base = 0;
	if(mode == "p"){
		page = parseInt(opt.page);
		total = parseInt(opt.total);
		base = parseInt(opt.base);
		
		npages = Math.ceil(total/base);
		var pagetotal = npages - page;
		
		
		pageStart = Math.floor(page/10) * 10;
		
		if(pageStart == page){
			pageStart = pageStart -9;
		} else {
			pageStart = pageStart + 1;
		}
		
		if(npages - pageStart > 9){
			pageEnd = pageStart + 9;		
		} else {
			pageEnd = npages;
		}
		
		nextTerm = getNextWindow(total,base,page,10);
		preTerm = getPrevWindow(page,10);
		
		
		
				
		var pagingWrapper = "";
		pageDrow = "";
		/*pageDrow = "<div class='navi_content mail_pagenavi' >";	
		var pagingWrapper = "<div style='float:left;'>";
		
		if(preTerm > 0){
			pagingWrapper += "<div style='float:left;"+marginStyle.preTerm+"'>" +
					"<a href='javascript:movePage("+preTerm+");'>" +
					"<img src='/design/common/image/ic_paging_term_pre.gif' " +
					"class='navi_img'/></a>" +
					"</div>";
		}*/
		if(pageEnd > 0){
			
			if(preTerm > 0){
				pagingWrapper += "<span><a href='javascript:movePage(1);'><img src='/design/common/image/btn_first.gif' width='21px' height='15px' align='absmiddle'/></a></span>&nbsp;";
			}else{
				//pagingWrapper += "<span><img src='/design/common/image/ic_paging_term_pre.gif' width='8px' height='7px' align='absmiddle'/></span>&nbsp;";
				pagingWrapper += "";
			}
		
				
			if(preTerm > 0){
				//pagingWrapper += "<span><a href='javascript:movePage("+(preTerm)+");'><img src='/design/freechal/image/btn/btn_prev.gif' width='16px' height='15px' align='absmiddle'/></a></span>";
				pagingWrapper += "<span><a href='javascript:movePage("+(preTerm)+");'><img src='/design/common/image/btn_prev.gif' width='16px' height='15px' align='absmiddle'/></a></span>";
			}else{
				pagingWrapper += "";
			}
		}
		
		/*if(page > 1){
			pagingWrapper += "<div style='float:left;'>" +
					"<a href='javascript:movePage("+(page-1)+");' class='pre'>" +
					"<img src='/design/common/image/ic_paging_pre.gif' class='navi_img'/>" +
					"<span>"+comMsg.comn_prelist+"</span></a></div>";			
			
			if(page < npages){
				pagingWrapper += "<div style='float:left;"+marginStyle.bar+"'>|</div>";
			}			
			
		}	*/	
		
		if(pageEnd > 0){				
			for ( var i = pageStart ; i <= pageEnd; i++) {
				pageArray.push({index:i,value:i});						
			}
			
			//pageDrow += "<div id='pagingSelect' style='display:block' class='navi_content'></div>";
			var pageNumbers = ""
			var pageId = pageStart;
				for(var p=0; p<pageArray.length ; p++){
					pageNumbers += "<span style='padding:0px 10px 0 10px;' onclick='javascript:movePage("+pageArray[p].index+")' id='page"+pageId+"' class='pageStyleBasic' onMouseOver='pagingMouseOver(this)' onMouseOut='pagingMouseOut(this)'>"+pageArray[p].index+"</span>";
					//pageIdArray.push({index:pageId,value:pageId});
					//pageIdArray += pageId+"|";
					//pageNumbers += "<a href='javascript:movePage("+pageArray[p].index+")' class='pageStyleBasic' >"+pageArray[p].index+"</a>";
					pageId++;						
				}
			//pagingWrapper += "<div id='pagingSelect"+position+"' ></div>";
			pagingWrapper += pageNumbers;
		}
		
		/*if(page < npages){			
			pagingWrapper += "<div style='float:left;'>" +
					"<a href='javascript:movePage("+(page+1)+");' class='next'>" +
					"<span>"+comMsg.comn_nextlist+"</span>" +
					"<img src='/design/common/image/ic_paging_next.gif' class='navi_img'/></a></div>";			
		}
		
		if(nextTerm > 0){
			pagingWrapper += "<div style='float:left;"+marginStyle.nextTerm+"'>" +
					"<a href='javascript:movePage("+nextTerm+");'>" +
					"<img src='/design/common/image/ic_paging_term_next.gif' class='navi_img'/>" +
					"</a></div>";			
		}
		pagingWrapper += "</div>";*/
		if(pageEnd > 0){
			if(nextTerm > 0){
				//pagingWrapper += "<a href='javascript:movePage("+(nextTerm)+");'><img src='/design/freechal/image/btn/btn_next.gif' width='16px' height='15px' align='absmiddle'/></a>&nbsp;";
				pagingWrapper += "<a href='javascript:movePage("+nextTerm+");'><img src='/design/common/image/btn_next.gif' width='16px' height='15px' align='absmiddle'/></a>&nbsp;";	
			}else{
				pagingWrapper += "";
			}
			if(nextTerm > 0){	
				pagingWrapper += "<a href='javascript:movePage("+npages+");'><img src='/design/common/image/btn_last.gif' width='21px' height='15px' align='absmiddle'/></a>";
			}else{
				pagingWrapper += "";
			}
		}

		pageDrow += pagingWrapper;
		
				
	} else if(mode == "n"){		
		var prePageUid = parseInt(opt.preUid);
		var nextPageUid = parseInt(opt.nextUid);
		
		pageDrow = "<div class='navi_content mail_pagenavi'>";
		
		var pagingWrapper = "<div style='float:left;'>";
		
		if(prePageUid > 0){
			
			pagingWrapper += "<div style='float:left;'>" +
			"<a href=\"javascript:readMessage('"+opt.folderName+"',"+prePageUid+");\" class='pre'>" +
			"<img src='/design/common/image/ic_paging_pre.gif' class='navi_img'/>" +
			"<span>"+comMsg.comn_prelist+"</span></a></div>";
			
			if(opt.isListSet || nextPageUid > 0){
				pagingWrapper += "<div style='float:left;"+marginStyle.bar+"'>|</div>";
			}
		}
		
		if(opt.isListSet){
			pageList = "<div style='float:left;'>" +
					"<a href='javascript:listPage();' class='plist'>" +
					"<span style='font-size:"+naviSize+"px;'>"+comMsg.comn_list+"</span></a></div>";
			
			pagingWrapper += pageList;				
		}		
		
		if(nextPageUid > 0){
			if(opt.isListSet){
				pagingWrapper += "<div style='float:left;"+marginStyle.bar+"'>|</div>";
			}			
			pagingWrapper += "<div style='float:left;'>" +
			"<a href=\"javascript:readMessage('"+opt.folderName+"',"+nextPageUid+");\" class='next'>" +
			"<span>"+comMsg.comn_nextlist+"</span>" +
			"<img src='/design/common/image/ic_paging_next.gif' class='navi_img'/>"+
			"</a></div>";				
			
			
		}
		
		pagingWrapper += "</div>";
		pageDrow += pagingWrapper;
		
	}
	else if(mode == "b"){		
		var preContentId = parseInt(opt.preContentId);
		var nextContentId = parseInt(opt.nextContentId);
		
		pageDrow = "<div class='navi_content mail_pagenavi' >";
		
		var pagingWrapper = "<div style='float:left;'>";
		
		if(preContentId > 0){	
			
			pagingWrapper += "<div style='float:left;'>" +
			"<a href=\"javascript:viewContent('"+preContentId+"','"+opt.preParentId+"','"+opt.preOrderNo+"');\" class='pre'>" +			
			"<img src='/design/common/image/ic_paging_pre.gif' class='navi_img'/>" +
			"<span>"+comMsg.comn_prelist+"</span></a></div>";
	
			pagingWrapper += "<div style='float:left;"+marginStyle.bar+"'>|</div>";
		}
		
		if(opt.isListSet){
			pageList = "<div style='float:left;'>" +
			"<a href='javascript:listPage();' class='plist'>" +
			"<span>"+comMsg.comn_list+"</span></a></div>";
	
			pagingWrapper += pageList;
		}
		
		if(nextContentId > 0){	
			
			pagingWrapper += "<div style='float:left;"+marginStyle.bar+"'>|</div>";
			
			pagingWrapper += "<div style='float:left;'>" +
			"<a href=\"javascript:viewContent('"+nextContentId+"','"+opt.nextParentId+"','"+opt.nextOrderNo+"');\" class='next'>" +
			"<span>"+comMsg.comn_nextlist+"</span>" +
			"<img src='/design/common/image/ic_paging_next.gif' class='navi_img'/></a></div>";			
			
		}
		
		pagingWrapper += "</div>";
		pageDrow += pagingWrapper;
	}
	
	if(mode == "n" || mode == "b"){		
		
		if(opt.isLineCntSet){
			pageDrow += "<div id='pagingBase' style='display:block' class='navi_content'></div>";			
		}		
		
		pageDrow += "</div>";
		
	}else{
		$("pageNavi").innerHTML = "<div id='pagingBase' style='display:block' class='navi_content'></div>";
	}
	if(total > 0){
		if(!pageNaviId){
			if(typeof(fraContent) != 'undefined')
				fraContent.$("pageBottomNavi").innerHTML = pageDrow;
			else
				$(pageNaviId).innerHTML = pageDrow;
		}else{
			$(pageNaviId).innerHTML = pageDrow;
		}
	}
	
	/*if(mode == "p" && $("pagingSelect")){
		jQuery("#pagingSelect").selectbox({selectId:"paging",
			selectFunc:movePage},page,pageArray);
	}*/
	
	if((mode == "p" || mode == "n" || mode == "b")	&& opt.isLineCntSet &&  $("pagingBase")){		
		pageBase = opt.base;
		
		jQuery("#pagingBase").selectbox({selectId:"pageBaseVal",
				selectFunc:changeLineCnt,width:110},Number(pageBase),
				[{index:comMsg.comn_listsetup+"(15)",value:15},
				 {index:comMsg.comn_listsetup+"(20)",value:20},
				 {index:comMsg.comn_listsetup+"(25)",value:25},
				 {index:comMsg.comn_listsetup+"(30)",value:30},
				 {index:comMsg.comn_listsetup+"(50)",value:50},
				 {index:comMsg.comn_listsetup+"(80)",value:80}]);
	}	
};
jQuery.fn.printFlag = function(opt){
	var flagItems = jQuery(this);
	
	jQuery.each(flagItems,function(){		 
		var flag = jQuery(this);
		var msgLow = flag.parent();
		var msgID = msgLow.attr("id");
		var flagStr = jQuery.trim(flag.attr("flagcontents"));
		
		flag.empty();		
			
		var flagedFlag = jQuery("<img align='absmiddle' id='"+msgID+"_flagedF' class='flagF'>");
		if(flagStr.indexOf("C") > -1){
			flagedFlag.attr("src",opt.onFlagedImg);
			flagedFlag.attr("flaged","false");
		} else {
			flagedFlag.attr("src",opt.offFlagedImg);
			flagedFlag.attr("flaged","true");
		}
		if(opt.folderType != "shared"){
			flagedFlag.click(function(){								
					opt.flagFunc([msgID],"F",jQuery(this).attr("flaged"));
			});
		}		
		flag.append(flagedFlag);	
		
		
		var readFlag = jQuery("<img align='absmiddle' id='"+msgID+"_readF' class='flagR'>");		
		if(flagStr.indexOf("A") > -1){
			readFlag.attr("src",opt.replyImg);
		} else if(flagStr.indexOf("F") > -1){
			readFlag.attr("src",opt.forwardImg);
		} else if(flagStr.indexOf("S") > -1){
			readFlag.attr("src",opt.seenImg);
		}else {
			readFlag.attr("src",opt.unseenImg);
		}
		
		flag.append(readFlag);		
		
		if(flagStr.indexOf("T") > -1){			
			flag.append(jQuery("<img align='absmiddle' src='"+opt.attrImg+"'>"));
		}	
				
	});
};

jQuery.fn.printTag = function(menuFunc,msgID,tagStr){
	var tagPrintLayer = jQuery(this);	
	if(tagStr && tagStr != ""){
		var tagNames = tagStr.split("|");
		var size = tagNames.length-1; 
		if(size > 0){
			var tagList = jQuery("<span style='z-index:1001;'></span>");
			for ( var i = 0; i < size; i++) {
				var tag = jQuery("<img src='/design/common/image/blank.gif'>");
				var tagInfo = tagControl.getTagInfo(tagNames[i]);
				var tagID = msgID + "_"+tagInfo.id;
				tag.attr("id","m_"+tagID);
				tag.addClass("tagimg_base");
				tag.addClass("timg_"+replaceAll(tagInfo.color,"#",""));
				tag.attr("title",tagNames[i]);										
				tag.mouseover(function(event){menuFunc(jQuery(this),event);});
				tagList.append(tag);
				
			}
			tagPrintLayer.prepend(tagList);
		}
		
	}
};

var PageMainLoadingManager = {	
	processLoadStack:[],
	processCompleteStack:[],
	isWork:false,
	isFirst:true,	
	initLoadView : function(){		
		jQuery("#mbodyLoadMask").show();
		jQuery("#mbodyLoadContent").show();
		PageMainLoadingManager.isWork = true;
		jQuery("#mainWorkProgressBar").css("width","0%");
		
	},
	startLoad:function(jobStack){		
		if(PageMainLoadingManager.isFirst){			
			PageMainLoadingManager.processLoadStack = jobStack;
			setTimeout(PageMainLoadingManager.checkProgress,100);
		}		
		PageMainLoadingManager.isFirst = false;
	},
	checkProgress:function(){
		var totalWorkCnt = PageMainLoadingManager.processLoadStack.length;		
		var completeWorkCnt = PageMainLoadingManager.processCompleteStack.length;
		
		var percent = (completeWorkCnt/totalWorkCnt) * 100;		
		if(percent >= 100){
			percent = 100;
		}
		jQuery("#mainWorkProgressBar").css("width",percent+"%");		
		if(percent == 100){
			setTimeout(PageMainLoadingManager.clearLoad,2000);
		} else {
			setTimeout(PageMainLoadingManager.checkProgress,100);
		}		
	},
	clearLoad:function(){		
		jQuery("#mbodyLoadMask").hide();
		jQuery("#mbodyLoadContent").hide();		
		PageMainLoadingManager.isWork = false;
		PageMainLoadingManager.processLoadStack = [];
		PageMainLoadingManager.processCompleteStack = [];
	},
	completeWork:function(name){
		if(PageMainLoadingManager.isWork){
			PageMainLoadingManager.processCompleteStack.push(name);			
		}
	}	
};


var bodyMaskIsShown = false;
var bodtMaskPaneId = "";
function addEvent(obj, evType, fn){
 if (obj.addEventListener){
    obj.addEventListener(evType, fn, false);
    return true;
 } else if (obj.attachEvent){
    var r = obj.attachEvent("on"+evType, fn);
    return r;
 } else {
    return false;
 }
}

function addEventAttach(id, evType, fn, exceptObjName){	
	var obj = document.getElementById(id);
	if (obj.addEventListener){
	    obj.addEventListener(evType, function(event){ 
	    	var eventTarget = (event.target) ? event.target : event.srcElement;
	    	if(eventTarget.nodeName != exceptObjName)eval(fn);	    	
	    }, false);
	    return true;
	 } else if (obj.attachEvent){
	    var r = obj.attachEvent("on"+evType, function(event){
	    	var eventTarget = (event.target) ? event.target : event.srcElement;
	    	if(eventTarget.nodeName != exceptObjName)eval(fn);	    	
	    });
	    return r;
	 } else {
	    return false;
	 }
}

function getViewportHeight() {
	if (window.innerHeight!=window.undefined) return window.innerHeight;
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientHeight;
	if (document.body) return document.body.clientHeight; 

	return window.undefined; 
}
function getViewportWidth() {
	var offset = 17;
	var width = null;
	if (window.innerWidth!=window.undefined) return window.innerWidth; 
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientWidth; 
	if (document.body) return document.body.clientWidth; 
}

function setBodyMaskSize() {
	if(bodyMaskIsShown){
		var theBody = document.getElementsByTagName("BODY")[0];
		
		var fullHeight = getViewportHeight();
		var fullWidth = getViewportWidth();
		
		// Determine what's bigger, scrollHeight or fullHeight / width
		if (fullHeight > theBody.scrollHeight) {
			popHeight = fullHeight;
		} else {
			popHeight = theBody.scrollHeight;
		}
		
		if (fullWidth > theBody.scrollWidth) {
			popWidth = fullWidth;
		} else {
			popWidth = theBody.scrollWidth;
		}
		
		jQuery("#"+bodtMaskPaneId).css("height",popHeight + "px");
		if(!jQuery.browser.mozilla){
			jQuery("#"+bodtMaskPaneId).css("width",popWidth + "px");
		}
	}
	
}

addEvent(window, "resize", setBodyMaskSize);

jQuery.makeBodyMask = function(maskId,isTtransparent){
	bodyMaskIsShown = true;
	var id = "smask";
	if(maskId){
		id = maskId;
	}
	
	bodtMaskPaneId = id;	
	jQuery("body").append(jQuery("<div>").attr("id",id).addClass((isTtransparent)?"subMaskW":"subMask"));
	
	setBodyMaskSize();	
};

var loadTimeout;
jQuery.makeProcessBodyMask = function(setTime){	
	jQuery("#mbodyLoadMask").show();
	var width = jQuery(window).width();
	var height = jQuery(window).height();
	jQuery("#mbodyLoadMask").css({width:width+"px",height:height+"px"});
	if(isMsie){
		window.document.body.scroll = "no";
	}
	var msgFrame = jQuery("<div id='contentLoadMask'>");
	msgFrame.css("display","none");	
	msgFrame.addClass("TM_c_loadding");	
	jQuery("#mbodyLoadMask").append(msgFrame);	
	var ctop = (height/2 - msgFrame.height()/2);
	var cleft = (width/2 - msgFrame.width()/2);	
	jQuery("#contentLoadMask").css({top:ctop+"px",left:cleft+"px"});
	setTimeout(function(){
		jQuery("#contentLoadMask").show();
	},1)	
	jQuery("object").hide();
	var time = (typeof(setTime) == 'undefined')?5000:setTime;
	if(time > 0){
		loadTimeout = setTimeout(function(){
			document.location.reload();
		},time);
	}
}

jQuery.removeProcessBodyMask = function(){	
	jQuery("#mbodyLoadMask").remove();
	clearTimeout(loadTimeout);
	if(isMsie){
		window.document.body.scroll = "auto";
		jQuery("object").show();
	}
}

jQuery.removeBodyMask = function(maskId){
	bodyMaskIsShown = false;
	var id = "smask";
	if(maskId){
		id = maskId;
	}
	bodtMaskPaneId = "";
	jQuery("#"+id).remove();
};

function resizeMask(resizeObject,id){		
	var cheight = jQuery(resizeObject).height();	
	jQuery("#"+id).css("height",cheight+"px");			
}

jQuery.fn.makeMask = function(maskId,isWhite){
	var id = "smask";
	if(maskId){
		id = maskId;
	}
	var maskArea = jQuery(this);	
	maskArea.append(jQuery("<div>").attr("id",id).addClass((isWhite)?"subMaskW":"subMask"));
	var cheight = maskArea.children().height();
	var mheight = jQuery("#"+id).height();

	if(cheight > mheight){
		jQuery("#"+id).css("height",cheight);
	}	
	jQuery(maskArea).bind("resize", function(event){
		var resizeObject = jQuery(this);
			resizeMask(resizeObject,id);
		});
	jQuery(maskArea).css("overflow","hidden");	
};
jQuery.fn.removeMask = function(maskId){
	var id = "smask";
	if(maskId){
		id = maskId;
	}
	
	jQuery("#"+id).remove();
	var maskArea = jQuery(this);	
	jQuery(maskArea).unbind("resize", function(event){
		var resizeObject = (event.target) ? jQuery(event.target) : jQuery(event.srcElement);
			resizeMask(resizeObject,id);
		});
	jQuery(maskArea).css("overflow","auto");	
		
};

jQuery.fn.makeModal = function(maskID,top,left){	
	
	var bodyFrame =  jQuery("body");
	var modalFrame = jQuery(this);
	var stop = (top)?top:-1;
	var sleft = (left)?left:-1;
	
	
	function centerModalWin() {	
		var bHeight = jQuery(window).height();		
		var bWidth = jQuery(window).width();
		
		var ctop = (stop > 0)?top:(bHeight/2 - modalFrame.height()/2);
		var cleft = (sleft > 0)?left:(bWidth/2 -modalFrame.width()/2);		
		
		modalFrame.css("top",  ctop+ "px");
		modalFrame.css("left", cleft+ "px");			
	}
	
	var id = modalFrame.attr("id");
	modalFrame.show();
	jQuery.makeBodyMask(maskID);	
	jQuery("object:not(div#"+id+" object)").hide();
	jQuery("select:not(div#"+id+" select)").hide();
	bodyFrame.bind("resize",centerModalWin);		
	centerModalWin();		
};
jQuery.fn.removeModal = function(maskID){
	var bodyFrame =  jQuery("body");
	var modalFrame = jQuery(this);
	
	function centerModalWin() {					
	}
	
	modalFrame.hide();	
	jQuery.removeBodyMask(maskID);
	bodyFrame.unbind("resize",centerModalWin);
	jQuery("object").show();
	jQuery("select").show();
	
};

jQuery.fn.loadbar = function(hid){
	if(hid){
		jQuery("#"+hid).css("overflow","hidden");
	}
	var loadPane = jQuery(this);
	loadPane.empty();
	var wrapper = jQuery("<div><div>");
	wrapper.css("text-align","center");		
	wrapper.append("<img src='/design/common/image/ajax-loader.gif' align='absmiddle' height=20 vapsce=5 >");
	loadPane.append(wrapper);	
}


jQuery.fn.loadWorkMaskOnly = function(isMessage,isPopup){
	var loadPane = jQuery(this);	
	var msgFrame = jQuery("<div id='contentLoadMask'>");
	msgFrame.addClass("TM_c_loadding");
    
	jQuery("object").each(function(){
		var id = jQuery(this).attr("id");
		if(id.indexOf("SWFUpload") < 0){
			jQuery(this).hide();
		}
	});
	jQuery("select").hide();
	loadPane.makeMask("contentBodyMask",true);	
	
	if(isMessage){
		jQuery("#contentBodyMask").after(msgFrame);		
		
		var bHeight = loadPane.outerHeight(true);		
		var bWidth = loadPane.outerWidth();
		var cHeight = loadPane.children().outerHeight(true);
		var scrollTop = loadPane.scrollTop();
		
		if(bWidth == 0){				
			jQuery("#contentBodyMask").css("width",jQuery(window).width()+"px");
		}
		
		if(bHeight == 0 ){				
			jQuery("#contentBodyMask").css("height",jQuery(window).height()+"px");
		}			
		bHeight = (bHeight == 0)?jQuery(window).height():bHeight;
		bWidth = (bWidth == 0)?jQuery(window).width():bWidth;
		
		var ctop = (bHeight/2 - msgFrame.height()/2);
		var cleft = (bWidth/2 -msgFrame.width()/2);		
		if(bHeight < cHeight && scrollTop > 0){
			var size = (cHeight - bHeight);
			if(size > scrollTop){
				size = size - scrollTop;
			}
			ctop = ctop + size;
		}		
		msgFrame.css({"top":ctop + "px","left":cleft + "px"});
	}
};

jQuery.fn.loadWorkMask = function(isMessage,loadParam, isMask){	
	//if(!PageMainLoadingManager.isWork){		
		var loadPane = jQuery(this);
		var id = loadPane.attr("id");
		var msgFrame = jQuery("<div id='contentLoadMask'>");		
		msgFrame.addClass("TM_c_loadding");				
		loadPane.makeMask("contentBodyMask", !isMask);		
		jQuery("object").hide();		
		
		if(isMessage){
			jQuery("#contentBodyMask").after(msgFrame);	
			
			var bHeight = loadPane.outerHeight(true);		
			var bWidth = loadPane.outerWidth(true);
			var cHeight = loadPane.children().outerHeight(true);
			var scrollTop = loadPane.scrollTop();
			
			if(bWidth == 0){				
				jQuery("#contentBodyMask").css("width",jQuery(window).width()+"px");
			}
			
			if(bHeight == 0){				
				jQuery("#contentBodyMask").css("height",jQuery(window).height()+"px");
			}			
			bHeight = (bHeight == 0)?jQuery(window).height():bHeight;
			bWidth = (bWidth == 0)?jQuery(window).width():bWidth;
			
			var ctop = (bHeight/2 - msgFrame.height()/2);
			var cleft = (bWidth/2 -msgFrame.width()/2);		
			if(bHeight < cHeight && scrollTop > 0){
				var size = (cHeight - bHeight);
				if(size > scrollTop){
					size = size - scrollTop;
				}
				ctop = ctop + size;
			}		
			msgFrame.css({"top":ctop + "px","left":cleft + "px"});
		}
	//}
	
	if(loadParam){		
		jQuery("#"+loadParam.cid).load(loadParam.url,loadParam.param,function(){		
			if(loadParam.exeFunction){
				loadParam.exeFunction();
			}
			jQuery("#"+loadParam.cid).removeWorkMask();
			jQuery("object").show();
			setTimeout(function(){
				ActionLoader.isloadAction = false;
			},1000);
		});
	} else {
		ActionLoader.isloadAction = false;
	}
};

jQuery.fn.removeWorkMask = function(){
	var loadPane = jQuery(this);
	loadPane.removeMask("contentBodyMask");
	jQuery("#contentLoadMask").remove();
};

jQuery.fn.makeSubMenu = function(opt,funcList,param){
	
	var parentFrame = jQuery(this);
	parentFrame.css("position","relative");			
	
	var menuL = jQuery("<div>");
	menuL.addClass("sub_menu_wrapper");
	var width = "";
	if(opt.width > 0){
		menuL.css("width",(opt.width+8)+"px");
		width = opt.width +"px";
	}
	
	parentFrame.append(menuL);
	
	var i = 1;
	var len = funcList.length;
	var linkTag,contents,menuItemPane;
	jQuery.each(funcList, function(){
		var name = this.name;
		var link = this.link;
		menuItemPane = jQuery("<div>");				
		menuItemPane.css("cursor","pointer");
		menuItemPane.css("z-index","5");
		menuItemPane.css("position","relative");		
		menuItemPane.addClass("sub_item");		
		
		linkTag = jQuery("<a href='javascript:;'></a>");
		contents = jQuery("<span class='item_body jpf'></span>").append(name);
		if(opt.width > 0){
			contents.css("width",width);
		}
		
		linkTag.addClass("sitem");		
		linkTag.append(contents.hover(function(){jQuery(this).addClass("item_body_over");},
				function(){jQuery(this).removeClass("item_body_over");}));										
		linkTag.click(function(event){
						jQuery(event.target).parent().parent().parent().hide();
						link(param);
						});
		menuItemPane.append(linkTag);						
		menuL.append(menuItemPane);
		i++;
	});	
};

var ActionLoader = {};
ActionLoader.isloadAction = false;
ActionLoader.loadAction = function(canvarsID,url,param,isMessage,exeFunction){
	ActionLoader.isloadAction = true;
	var lparam = {};
	var efunc = false;
	if(param){
		lparam = param; 
	}
	
	if(exeFunction){
		efunc = exeFunction;
	}
	var loadParam = {"cid":canvarsID,"url":url,"param":lparam,"exeFunction":efunc};	
	jQuery("#"+canvarsID).loadWorkMask(isMessage, loadParam);	
};

ActionLoader.postLoadAction = function(action,param,afterFunc,dataType){
	ActionLoader.isloadAction = true;
	jQuery.post(action,param,function(data,textStatus){		
		afterFunc(data,textStatus);
		setTimeout(function(){
			ActionLoader.isloadAction = false;
		},1000);		
	},dataType);	
};

var ActionNotMaskLoader = {};
ActionNotMaskLoader.loadAction = function(canvarsID,url,param,exeFunction){
	ActionLoader.isloadAction = true;
	var lparam = {};	
	if(param){
		lparam = param; 
	}
	$(canvarsID).scrollTop = 0;	
	jQuery("#"+canvarsID).load(url,lparam,function(){		
		if(exeFunction){
			exeFunction();
		}
		ActionLoader.isloadAction = false;				
	});
};

var ActionBasicLoader = {};
ActionBasicLoader.loadAction = function(canvarsID,url,param){	
	jQuery("#"+canvarsID).load(url,param);
};

var preViewTimeOut;
var priviewList = [];
function overPriview(id,idx){
	var subjectLink = jQuery("#"+id);
	var subjectLinkParent = subjectLink.parent();
	var subject = subjectLink.html();
	
	var left = subjectLinkParent.offset().left;
	var top = subjectLinkParent.offset().top;
	var wtop = jQuery(window).height();
	
	var preview = priviewList[idx];		
	if(!preview){
		preview = "";
	}	
	
	var height = subjectLinkParent.height();
	
	if(!preview){
		preview = "";
	}	
	
	top += height + 1;
	//top = ((top+80) < wtop)?top:top-80;
	
	if(!$("previewContentLayer")){
		preViewTimeOut = setTimeout(function(){			
			var previewPane = jQuery("<div id='previewContentLayer'></div>").addClass("TM_previewWrapper").css("margin-right","10px");					
			previewPane.append(jQuery("<div class='psubj'>").append(subject));
			if(preview != ""){
				previewPane.append(jQuery("<div class='pview'>").append(preview));
			}
			//jQuery("body").append(previewPane.css({"top":top+10,"left":left+10}));
			jQuery("body").append(previewPane.css({"top":top,"left":left}));
			previewPane.css("z-index","3");		
			previewPane.show();	
		},500);		
	}	
}
function outPriview(){
	jQuery("#previewContentLayer").remove();
	clearTimeout(preViewTimeOut);
}

function removePreview(){
	jQuery("#previewContentLayer").remove();
	clearTimeout(preViewTimeOut);
}

jQuery.fn.selectboxSetIndex = function(idx){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	var inputId = jQuery("#"+sid+"_selectMain").data("selectId");
	
	var optionList = selectPane.find(".selectItem");
	var option,val;
	for ( var i = 0; i < optionList.length; i++) {
		option = jQuery(optionList[i]).removeClass("jselect_listselect");
		val = option.attr("selectValue");		
		if(i == idx){
			option.addClass("jselect_listselect");
			jQuery("#"+inputId).val(val);
			jQuery("#"+sid+"_selectText").html(option.html());	
			break;
		}		
	}	
};

jQuery.fn.selectboxSetValue = function(value){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	var inputId = jQuery("#"+sid+"_selectMain").data("selectId");	
	
	var optionList = selectPane.find(".selectItem");
	var option,val;
	for ( var i = 0; i < optionList.length; i++) {
		option = jQuery(optionList[i]).removeClass("jselect_listselect");
		val = option.attr("selectValue");
		
		if(val == value){
			option.addClass("jselect_listselect");
			jQuery("#"+inputId).val(value);
			jQuery("#"+sid+"_selectText").html(option.html());	
			break;
		}		
	}	
};

jQuery.fn.selectboxGetText = function(value){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	var inputId = jQuery("#"+sid+"_selectMain").data("selectId");	
	
	var optionList = selectPane.find(".selectItem");
	var option,val;
	var returnValue = "";
	for ( var i = 0; i < optionList.length; i++) {
		option = jQuery(optionList[i]);
		val = option.attr("selectValue");
		if(val == value){
			returnValue = option.html();
			break;
		}
	}
	return returnValue;
};

jQuery.fn.selectboxGetOptionsCount = function(){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	var inputId = jQuery("#"+sid+"_selectMain").data("selectId");
	
	var optionList = selectPane.find(".selectItem");
	if (!optionList || !optionList.length) {
		return 0;
	} else {
		return optionList.length;
	}
};

jQuery.fn.selectboxDisable = function(){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	selectPane.data("use","F");	
	jQuery("#"+sid+"_selectMain").css("background","#EFEFEF");
};

jQuery.fn.selectboxEnable = function(){
	var selectPane = jQuery(this);
	var sid = selectPane.attr("id");
	selectPane.data("use","T");
	jQuery("#"+sid+"_selectMain").css("background","#FFFFFF");	
};
jQuery.fn.selectbox = function(opt,selectValue,selectOptionList){
	var selectId = opt.selectId;		
	var selectFunc = opt.selectFunc;
	var isIE6 = (jQuery.browser.msie && jQuery.browser.version < 7);
	
	var selectPane = jQuery(this);	
	var sid = selectPane.attr("id");
	var toggleFlag = "off";	
	function toggleList(){
		if("F" != selectPane.data("use")){
			var listHeight = jQuery("#"+sid+"_selectList").height();
			var sList = jQuery("#"+sid+"_selectList");
			
			if(opt.height) {
				listHeight = opt.height;
			} else {
				if(listHeight > 150){
					listHeight = 150;				
				}
			}
			
			sList.css("height",listHeight+"px");			
	
			if(toggleFlag == "off"){
				toggleFlag = "on";
				if(isIE6){
					jQuery("#"+sid+"_sbgFrame").show();					
				}				
				sList.show();				
			} else {
				toggleFlag = "off";
				if(isIE6){
					jQuery("#"+sid+"_sbgFrame").hide();					
				}
				sList.hide();
			}
			
			if(isIE6){
				jQuery("#"+sid+"_sbgFrame").css("height",(listHeight-1)+"px");				
			}
		
		}
		
	}
	
	function showSelectList(){	
		if(toggleFlag == "on"){
			jQuery("#"+sid+"_selectList").show();
			if(isIE6){
				jQuery("#"+sid+"_sbgFrame").show();
				
			}
		}
	}
	
	function hideSelectList(){
		toggleFlag = "off";
		jQuery("#"+sid+"_selectList").hide();
		if(isIE6){
			jQuery("#"+sid+"_sbgFrame").hide();			
		}
	}
	
	function selectItem(text,val,iid){
		var li = jQuery("#"+sid+"_selectList div.selectItem").removeClass("jselect_listselect");
		jQuery("#"+iid).addClass("jselect_listselect");
		jQuery("#"+sid+"_selectText").html(text).attr("title",text);
		jQuery("#"+selectId).val(val);
		
		if(selectFunc){
			selectFunc(val);
		}	
		
	}
	selectPane.data("use","T");
	var selectMain = jQuery("<div class='jselect'>").attr("id",sid+"_selectMain")
					.hover(function(){
								clearTimeout(selectPane.data("timeout"));
								showSelectList();
							},
							function(){
								selectPane.data("timeout",setTimeout(function(){hideSelectList();},100));
							});
	var selectContent = jQuery("<div class='selectContent jpf'>").attr("id",sid+"_selectText")
						.click(function(){
		toggleList();		
	});
	var selectIcon = jQuery("<div class='selectIcon'></div>").attr("id",sid+"_selectIcon").click(function(){
		toggleList();			
	});
	
	selectMain.append(selectContent).append(selectIcon);
	var selectListWrapper = jQuery("<div style='position:relative;' id='"+sid+"_listWraaper'></div>");
	
	if(isIE6){
		selectListWrapper.append("<iframe frameborder='0' tabindex='-1' class='bgFrame' id='"+sid+"_sbgFrame'></iframe>");		
	}
	
	var selectList = jQuery("<div class='selectList jpf'>").attr("id",sid+"_selectList");
					
	if(opt.width){
		selectList.css("width",opt.width+"px");
	}
	
	var selectStyle = "";
	var selectText = "";
	var isSelected = false;
	var selectIndexText;
	for(var i = 0 ; i < selectOptionList.length ; i++){
		selectStyle = "";
		if(i == 0){
			selectText = selectOptionList[i].index;
			if(!selectValue || selectValue == ""){
				selectValue = selectOptionList[i].value;
				isSelected = true;
			}
		}
		if(selectOptionList[i].value == selectValue){
			selectStyle = "jselect_listselect";
			selectText = selectOptionList[i].index;
			isSelected = true;
		}
		
		selectIndexText = selectOptionList[i].index;
		if(opt.textMaxLength){
			if(opt.textMaxLength < selectIndexText.length){
				selectIndexText = selectIndexText.substring(0,opt.textMaxLength);
				selectIndexText += "...";
			}
		}
		
		selectList
		.append(jQuery("<div class='selectItem jpf'></div>").attr("id",sid+"_sList_Item_"+i)
		.attr("selectValue",selectOptionList[i].value).addClass(selectStyle)
		.hover(function(){
			showSelectList();
			jQuery(this).addClass("jselect_listover");
			},function(){jQuery(this).removeClass("jselect_listover");})
		.click(function(){hideSelectList(); selectItem(escape_tag(jQuery(this).text()),jQuery(this).attr("selectValue"),jQuery(this).attr("id"))})
		.attr("title", unescape_tag_title(selectOptionList[i].index))
		.append((opt.escape)?escape_tag(selectIndexText):selectIndexText));
	}
	
	selectListWrapper.append(selectList);
	selectMain.append(jQuery("<div class='cls'></div>"));
	selectMain.append(selectListWrapper);
	selectPane.append(selectMain);
	selectPane.append(jQuery("<input type='hidden' name='"+selectId+"' id='"+selectId+"' value='"+selectValue+"'>"));
	selectMain.data("selectId",selectId);	
	
	if(!isSelected){
		jQuery("#"+sid+"_sList_Item_0").addClass("jselect_listselect");
		selectValue = jQuery("#"+sid+"_sList_Item_0").attr("selectValue");
		jQuery("#"+selectId).val(selectValue);
	}
	
	if(opt.textMaxLength){
		if(opt.textMaxLength < selectText.length){
			selectText = selectText.substring(0,opt.textMaxLength);
			selectText += "...";
		}
	}
	jQuery("#"+sid+"_selectText").html(escape_tag(selectText)).attr("title",escape_tag(selectText));
	
	var listWidth;
	if(opt.width){
		var listWidth = opt.width;		
		jQuery("#"+sid+"_selectList").css("width",listWidth+"px");		
		jQuery("#"+sid+"_selectMain").css("width",listWidth+"px");
		jQuery("#"+sid+"_selectText").css("width",(listWidth-27)+"px");		
		
		
	} else {	
		var mainWidth = jQuery("#"+sid+"_selectMain").width();
		listWidth = jQuery("#"+sid+"_selectList").width();
		
		listWidth = listWidth +17;	
		
		jQuery("#"+sid+"_selectList").css("width",listWidth+"px");
			
		if(listWidth > mainWidth){		
			jQuery("#"+sid+"_selectMain").css("width",listWidth+"px");
			jQuery("#"+sid+"_selectText").css("width",(listWidth-jQuery("#"+sid+"_selectIcon").width()-10)+"px");		
		}
	}
	if(isIE6){
		jQuery("#"+sid+"_sbgFrame").css("width",(listWidth-1)+"px");
	}
	
	jQuery("#"+sid+"_listWraaper").css("width",listWidth+"px");
	
	jQuery("#"+sid+"_selectList").css("overflow-X","hidden");
	jQuery("#"+sid+"_selectList").css("overflow-Y","auto");
}; 

var debugDate;
var debugStartTime;
function debugInfo(jobname,isStart){	
	if(DEBUGMODE == "enable" || PDEBUGLOGGING){
		var msec;
		var msecStr;
		var ndate = new Date();
		var cdate;		
		if(isStart){
			debugStartTime = new Date();
			debugDate = new Date();
			jQuery("#debugInfo").empty();
			jQuery("#debugInfo").append(jobname+",");
		} else {
			cdate = new Date(ndate.getTime() - debugDate.getTime());
			msec = cdate.getMilliseconds();
			msecStr = parseInt(msec/10);
			if (msec < 100) {
				msecStr = "0"+msecStr;
			}
			jQuery("#debugInfo").append(","+jobname+",");
			jQuery("#debugInfo").append("DTIME,"+cdate.getMinutes()+":"+cdate.getSeconds()+":"+msecStr+",");
			debugDate = ndate;
		}
		msec = ndate.getMilliseconds();
		msecStr = parseInt(msec/10);
		if (msec < 100) {
			msecStr = "0"+msecStr;
		}
		jQuery("#debugInfo").append("NTIME,"+ndate.getMinutes()+":"+ndate.getSeconds()+":"+msecStr);
		
	}
}
function loggingData(action){
	var msec;
	var endTime = new Date();
	var cdate = new Date(endTime.getTime()-debugStartTime.getTime());
	msec = (cdate.getMilliseconds()+"").substring(0,2);	
	jQuery("#debugInfo").append(",TOTAL,"+cdate.getMinutes()+":"+cdate.getSeconds()+":"+msec);	
	var data = jQuery("#debugInfo").text();
	var url = "/pdebug?mode=l&user="+USEREMAIL+"&action="+action+"&data="+encodeURI(data);	
	jQuery("#pdebuglogging").css("background","url("+url+")");
	debugStartTime = null;
}

function makeForm(name,action,method,param){
	var form = 	jQuery("<div id='"+name+"FormLayer'><form name='"+name+"' id='"+name+"' method='"+method+"' action='"+action+"'></form></div>");
	jQuery("body").append(form);
	if(param){
		jQuery.each(param,function(key, val){				
				if(jQuery.isArray(val)){
					for ( var i = 0; i < val.length; i++) {
						jQuery("#"+name).append(jQuery("<input type='hidden' name='"+key+"' id='"+name+key+i+"'/>"));
					}
				} else {
					jQuery("#"+name).append(jQuery("<input type='hidden' name='"+key+"' id='"+name+key+"'/>"));
				}
		});		
	}
	if(param){
		jQuery.each(param,function(key, val){				
				if(key && val){
					if(jQuery.isArray(val)){						
						for ( var i = 0; i < val.length; i++) {
							$(name+key+i).value = val[i];
						}
					} else {
						$(name+key).value = val;
					}
				}
		});		
	}	
	
	return $(name);
}