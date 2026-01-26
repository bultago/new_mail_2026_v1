var leftMenuControl, addrControl, orgControl;

jQuery().ready(function(){

	addrControl = new AddrControl(addrOption);
	orgControl = new OrgControl(orgOption);
	leftMenuControl = new LeftMenuControl();
	
	initService();
	
	if(MENU_STATUS.org && MENU_STATUS.org == "on")jQuery("#orgTabBtn").show();
	else jQuery("#orgTabBtn").hide();
});

var LeftMenuControl = Class.create({
	initialize: function(){
		this.pfolderList = null;
	},
	
	getPrivateGroupList:function(){
		var dlist = [];		
		this.pfolderList.each(function(f){			
			dlist[dlist.length] =  {"id":f.id, "name":f.name};
		});	
		
		return dlist;
	},
	
	addSharedAddressBook : function(){
		jQuery('#sharedAddrDialog').dialog('open');
	},
	
	loadPrivateAddressGroup : function(){
		var _this = this;
		AddressBookService.getJSonPrivateGroupList(
				{callback:function(addressList){		
					_this.printPrivateAddress(addressList);
				}}
		);
	},
	
	loadSharedAddressGroup : function(book){
		var target = jQuery('#'+book).find("#sharedGroups");
		
		var _this = this;
		AddressBookService.getJSonSharedGroupList(book,
				{callback:function(addressList){		
					_this.printSharedAddressGroup(book, target, addressList);
				}}
		);
	},
	
	printPrivateAddress : function(addressList){
		this.pfolderList = addressList;
		
		jQuery('#privateGroups').empty();

		var groupName;
		var groupSeq;
		for (var i=0; i<addressList.length; i++) {
			groupName = addressList[i].name;
			groupSeq = addressList[i].id;
			jQuery("<li><span id='gadr_span_"+groupSeq+"' class='closePopGroup'><a href='javascript:;' id='gadr_"+groupSeq+"' class='gadr' onclick='viewPrivateGroupMember(\""+groupName+"\","+groupSeq+");'><label>"+groupName+"</label></a></span></li>").appendTo("#privateGroups");
		}
	},
	
	loadSharedAddressBook : function(){
		addrControl.loadSharedBookList();
	},
	
	printSharedAddressBook : function(bookList){
		var _this = this;
		jQuery('#sharedTreeviewer').empty();
		
		addrControl.loadSharedAddress();
		
		jQuery('#sharedTreeviewer').treeview();
	},
	
	printSharedAddressGroup : function(bookSeq, obj, addressList){
		obj.empty();
		var groupName;
		var groupSeq;
		for (var i=0; i<addressList.length; i++) {
			groupName = addressList[i].name;
			groupSeq = addressList[i].id;
			jQuery("<li><span id='span_"+ bookSeq+"_"+groupSeq+"' class='closePopGroup'><a id='tree_"+ bookSeq+"_"+groupSeq+"' class='padr' href='javascript:;' onclick='viewSharedGroupMember(\""+groupName+"\","+ bookSeq+","+groupSeq+");'><label>"+groupName+"</label></a></span></li>").appendTo(obj);
		}
	},
	
	showPrivateTreeViewer : function(){
		jQuery('#privateTreeviewerWrapper').show();
		jQuery('#sharedTreeviewerWrapper').hide();
		jQuery('#orgTreeviewerWrapper').hide();
		jQuery("#alphabetArea").show();
		
		jQuery('#bookSeq').val('');
	},
	
	showSharedTreeViewer : function(){
		jQuery('#privateTreeviewerWrapper').hide();
		jQuery('#orgTreeviewerWrapper').hide();
		jQuery('#sharedTreeviewerWrapper').show();
		jQuery("#alphabetArea").show();
		
		jQuery('#bookSeq').val('');
	},
	
	showOrgTreeViewer : function() {
		jQuery('#privateTreeviewerWrapper').hide();
		jQuery('#sharedTreeviewerWrapper').hide();
		jQuery('#orgTreeviewerWrapper').show();
		jQuery("#alphabetArea").hide();
		
		jQuery('#bookSeq').val('');
	},
	
	moveMember : function(){
	}
});

var AddrControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.param = null;
		this.listParam = {};
	},
	loadAddressList : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionNotMaskLoader.loadAction(this.opt.mainLID, this.opt.listAction, param);
	},
	loadAddressView : function(param){
		
		ActionNotMaskLoader.loadAction(this.opt.subLID, this.opt.viewAction, param);
	},
	SharedAddressBook : function(param){
		
		ActionNotMaskLoader.loadAction(this.opt.subLID, this.opt.viewAction, param);
	},
	loadSharedBookList : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionNotMaskLoader.loadAction(this.opt.treeLID, this.opt.sharePopupTreeAction, param);
	},
	movePage:function(page){
		var param = this.listParam;
		if(param == null){
			param = {};
		}
		param.page = page;
		this.listParam = param;
		this.loadAddressList(param); 
	}
	
});

var OrgControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;
		this.param = null;
		this.listParam = {};
	},
	loadOrgTree : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionNotMaskLoader.loadAction(this.opt.treeLID, this.opt.treeAction, param);
	},
	loadMemberList : function(param){
		if(param){
			this.param = param;			
		}
		
		ActionNotMaskLoader.loadAction(this.opt.mainLID, this.opt.listAction, param);
	}
});

function initService(){

	jQuery("#privateTreeviewer").treeview();
	privateAddr();
}

function setMenu(index) {
	jQuery('.addTree_tab a').removeClass();
	jQuery('.addTree_tab a').addClass('btn_tabmenu2_off');
	jQuery('.addTree_tab a:eq('+index+')').removeClass();
	jQuery('.addTree_tab a:eq('+index+')').addClass('btn_tabmenu2_on');
}

function privateAddr() {
	var param = {"addrType":"popup"};
	
	setMenu('0');
	
	jQuery("#addrType").val('gadr');
	jQuery(".group").show();
	leftMenuControl.loadPrivateAddressGroup();
	leftMenuControl.showPrivateTreeViewer();
	addrControl.loadAddressList(param);
}

function sharedAddr() {
	var param;
	
	setMenu('1');
	
	jQuery("#addrType").val('padr');
	jQuery(".group").show();
	leftMenuControl.loadSharedAddressBook();
	leftMenuControl.showSharedTreeViewer();
	setTimeout("runShareList()",500);
}

function runShareList() {
	if (jQuery("#sharedTreeviewer div.folder a:first").attr("onclick")) {
		jQuery("#sharedTreeviewer div.folder a:first").attr("onclick")();
	} else {
		viewSharedGroupMember('',-1, 0);
	}
}

function orgAddr() {
	var param = {};
	param.isPopup = "Y";
	
	setMenu('2');
	jQuery("#addrType").val('org');
	jQuery(".group").hide();
	orgControl.loadOrgTree(param);
	leftMenuControl.showOrgTreeViewer();
	setTimeout("runOrgList()",500);
}

function runOrgList() {
	if (jQuery('#orgTree td.treeNodeWrapperContents a:first').attr('onclick')) {
		jQuery('#orgTree td.treeNodeWrapperContents a:first').attr('onclick')();
	} else {
		viewOrgMember("", "", "");
	}
}

function viewPrivateGroupMember(groupName, groupSeq){
	jQuery('#bookSeq').val('');
	jQuery('#groupName').val(groupName);
	var param = {"addrType":"popup", "groupSeq" : groupSeq};
	addrControl.loadAddressList(param);
	
	jQuery(".gadr").removeClass("btn_alphabet_on");
	jQuery(".closePopGroup").removeClass("openPopGroup");
	jQuery("#gadr_span_"+groupSeq).addClass("openPopGroup");
	jQuery("#gadr_"+groupSeq).addClass("btn_alphabet_on");

}

function viewSharedGroupMember(groupName, bookSeq, groupSeq){
	jQuery('#bookSeq').val(bookSeq);
	jQuery('#groupName').val(groupName);
	var param = {"groupSeq" : groupSeq,"bookSeq":bookSeq,"addrType":"popup"};
	addrControl.loadAddressList(param);
	
	jQuery(".padr").removeClass("btn_alphabet_on");
	jQuery(".closePopGroup").removeClass("openPopGroup");
	jQuery("#span_"+bookSeq+"_"+groupSeq).addClass("openPopGroup");
	jQuery("#tree_"+bookSeq+"_"+groupSeq).addClass("btn_alphabet_on");
}

function makeLeading(charater, leadingPattern) {
	var leadingCount = jQuery("#leadingGroup li").size();
	var lead = jQuery("<a href=\"javascript:srchByLeading('"+charater+"')\"></a>");
	
	if (charater == leadingPattern) {
		lead.addClass("btn_alphabet2_on");
	}
	else {
		lead.addClass("btn_alphabet2");
	}
	
	var leadSpan = jQuery("<span></span>");
	leadSpan.text(charater);
	lead.append(leadSpan);
	
	if (leadingCount < 17) {
		var leadLi = jQuery("<li style='white-space:nowrap;'></li>").append(lead);
		jQuery("#leadingGroup").append(leadLi);
	} else {
		for (i=0; i<leadingCount; i++) {
			if (jQuery("#leadingGroup li:eq("+i+") a").size() == 1) {
				jQuery("#leadingGroup li:eq("+i+")").append(lead);
				break;
			}
		}
	}
}

function clearLeading() {
	jQuery("#leadingGroup").empty();
}

function srchByLeading(leadingPattern) {
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery("#leadingGroupSeq").val();
	var param = {"addrType":"popup","groupSeq" : groupSeq,"leadingPattern":leadingPattern, "bookSeq":bookSeq};
	if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
		param.bookSeq = 0;
	addrControl.loadAddressList(param);
}

function searchAddress() {
	var searchType = jQuery('#searchType').val();
	var keyWordObj = jQuery('#keyWord');
	var keyWord = keyWordObj.val();	
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery('#groupSeq').val();
	var leadingPattern = jQuery('#leadingPattern').val();
	
	if(!checkInputLength("jQuery", keyWordObj, addrMsg.addr_info_msg_022, 2, 130)) {
		return;
	}
	if(!checkInputValidate("jQuery", keyWordObj, "case3")) {
		return;
	}
		
	var param = {"addrType":"popup","groupSeq" : groupSeq,"leadingPattern":leadingPattern, "bookSeq":bookSeq, "searchType" : searchType, "keyWord" : keyWord};
	if(!param.bookSeq || typeof(param.bookSeq) == 'undefined' || param.bookSeq == 'undefined')
		param.bookSeq = 0;
	
	addrControl.loadAddressList(param);
}

function moveto_page(page) {
	var searchType = jQuery('#presearchType').val();
	var keyWord = jQuery('#prekeyWord').val();
	var bookSeq = jQuery('#bookSeq').val();
	var groupSeq = jQuery('#groupSeq').val();
	var leadingPattern = jQuery('#leadingPattern').val();
	
	var param = {"addrType":"popup", "groupSeq":groupSeq,"bookSeq":bookSeq, "page":page, "leadingPattern":leadingPattern, "searchType" : searchType, "keyWord" : keyWord};
	addrControl.loadAddressList(param);
}

function viewOrgMember(orgCode, orgName){
	
	jQuery('#orgName').val(orgName);
	var param = {"orgType":"popup", "orgCode" : orgCode};
	orgControl.loadMemberList(param);
	
	jQuery(".closePopGroup").removeClass("openPopGroup");
	jQuery("#org_span_"+orgCode).addClass("openPopGroup");
	jQuery(".org").removeClass("btn_alphabet_on");
	jQuery("#org_"+orgCode).addClass("btn_alphabet_on");
	jQuery(".treeNodeWrapperContents a").css("font-weight", "");
	jQuery("#link_"+orgCode).css("font-weight", "bold");
}

function moveOrgPage(page){
	var orgCode = jQuery("#orgCode").val();
	var param = {"orgType":"popup", "orgCode" : orgCode, "page":page};
	orgControl.loadMemberList(param);
}

function makeOrgAddress(selectBox, type, orgCode, subType) {
	var text = jQuery("#"+selectBox+"Select_selectText").text();
	var value = jQuery("#"+selectBox).val();
	
	if (text == "") {
		text = "#"+orgCode+"."+type+"."+value+"."+subType;
	}
	else {
		text = "\""+text+"\"<#"+orgCode+"."+type+"."+value+"."+subType+">";
	}
	
	return text;
}

function addressAdd(list) {
	var type = jQuery("#addrType").val();
	var f = document.privateAddrForm;
	var flag = false;
	if (type == 'org') {
		f = document.orgForm;
		var orgCode = jQuery("#orgCode").val();
		var orgName = jQuery('#orgName').val();
		var subType = "false";
		if (f.titleInclude && f.titleInclude.checked) {
			if (f.titleSubAll && f.titleSubAll.checked) {
				subType = "true";
			}
			var orgText = makeOrgAddress("titleCode", "title", orgCode, subType);
			setOption(list, orgText, orgText);
			flag = true;
		}
		if (f.classInclude && f.classInclude.checked) {
			subType = "false";
			if (f.classSubAll && f.classSubAll.checked) {
				subType = "true";
			}
			var orgText = makeOrgAddress("classCode", "class", orgCode, subType);
			setOption(list, orgText, orgText);
			flag = true;
		}
		if (f.currentAll.checked) {
			var orgText = "";
			var currentSubFlag = f.currentSubAll.checked;
			if (orgName == "") {
				orgText = "#"+orgCode+".all.all."+currentSubFlag;
			}
			else {
				orgText = "\""+orgName+"\"<#"+orgCode+".all.all."+currentSubFlag+">";
				setOption(list, orgText, orgText);	
			}
			flag = true;
		}
	}
	
	if (checkedCnt(f.sel) > 0) {
		if (f.sel.length) {
			for (var i=0; i< f.sel.length; i++) {
				if (f.sel[i].checked) {
					var data = f.sel[i].value;
					var dataArray = data.split('|');
					var text = "";
					if (dataArray[0] == "") {
						text = dataArray[1];
					}
					else {
						text = "\""+dataArray[0]+"\"<"+dataArray[1]+">";
					}
					setOption(list, text, text);
				}
			}
		}
		else {
			if (f.sel.checked) {
				var data = f.sel.value;
				var dataArray = data.split('|');
				var text = "";
				if (dataArray[0] == "") {
					text = dataArray[1];
				}
				else {
					text = "\""+dataArray[0]+"\"<"+dataArray[1]+">";
				}
				setOption(list, text, text);
			}
		}
		flag = true;
	}
	
	if (!flag) {
		alert(addrMsg.addr_info_msg_013);
	}
}

function checkDup(list, text) {
	var isDup = false;

	for (var i=0; i<list.options.length; i++) {
		if (list[i].text == text) {
			isDup = true;
			break;
		}
	}
	return isDup;
}

function setOption(list, text, value) {
	
	var listCount = list.options.length;
	
	if (listCount == 0) {
		list.options[0] = new Option(text,value);	
	}
	else {
		if (!checkDup(list, text)) {
			list.options[listCount] = new Option(text,value);
		}
	}
}

function addressDel(list) {
	
	var listCount = list.options.length;
	var flag = false;
	if (listCount == 0) {
		alert(addrMsg.addr_info_msg_031);
		return;
	}
	else {
		for (var i=listCount-1; i >= 0; i--) {
			if (list.options[i].selected == true) {
				list.options[i] = null;
				flag = true;
			}
		}
		if (!flag) {
			alert(addrMsg.addr_info_msg_031);
		}
	}
}

function addGroup(list) {
	var type = jQuery("#addrType").val();
	var bookSeq = jQuery("#bookSeq").val();
	var groupSeq = jQuery("#groupSeq").val();
	var groupName = jQuery('#groupName').val();
	var listCount = list.options.length;
	
	if (type == 'gadr') {	
		if (trim(groupSeq) == "0") {
			alert(addrMsg.addr_info_msg_045);
			return;
		}
		else {
			groupSeq = "$"+groupSeq;
			groupName = "$"+groupName;
			if (!checkDup(list, groupName)) {
				list.options[listCount] = new Option(groupName,groupName);
			}
		}
	}
	else if (type == 'padr') {
		if (trim(groupSeq) == "0") {
			alert(addrMsg.addr_info_msg_045);
			return;
		}
		else {
			groupSeq = "&"+bookSeq+"."+groupSeq;
			groupName = "&"+bookSeq+"."+groupName;
			if (!checkDup(list, groupName)) {
				list.options[listCount] = new Option(groupName,groupName);
			}
		}
	}
}