var ShareGroupControl = Class.create({	
	initialize: function(opt){
		this.opt = opt;		
		this.param = null;		
		this.shareMap = new Hash();
		
		this.template = {
			Header : "<table cellspacing='0' cellpadding='0' style='border:1px solid #E8E8E8;width:100%;font-size:12px;'>",
			CommonTh : "<th style='background:none repeat scroll 0 0 #F7F7F7;border-right:1px solid #E8E8E8;padding:5px;text-align:right;width:70px; white-space:nowrap;'>#{MESSAGE}</th>",
			TitleTd : "<td style='padding:5px;'><b><span>#{TITLE}</span></b></td>",
			DateTd : "<td style='padding:5px;'><span style='color:#006ECF;'>#{DATE}</span></td>",
			RepeatTd : "<td style='padding:5px;'><span style='color: red; font-size: 11px;'>#{REPEAT}</span></td>",
			AssetTd : "<td style='padding:5px;'><span style='color: green; font-size: 11px;'>#{ASSET}</span></td>",
			AssetContectTd : "<td style='padding:5px;'><span style='color: green; font-size: 11px;'>#{ASSETCONTECT}</span></td>",
			ShareUserTd : "<td style='padding:5px;'><span style='color: green; font-size: 11px; word-wrap: break-word;'>#{SHAREUSER}</span></td>",
			ShareTargetTd : "<td style='padding:5px;'><span style='color: green; font-size: 11px;'>#{SHARETARGET}</span></td>",
			ShareTd : "<td style='padding:5px;'><span style='color: green; font-size: 11px;'>#{SHARE}</span></td>",
			LocationTd : "<td style='padding:5px;'><span>#{LOCATION}</span></td>",
			ContentTd : "<td style='padding:5px;'><textarea readonly='readonly' style='width: 100%; height: 120px; background-color: white; border: 0px none;font-size:12px;'>#{CONTENT}</textarea></td>",
			Tail : "</table>"
		};
	},
	saveShareInfo:function(shareName, shareColor, targetType, targetValues) {
		SchedulerShareService.saveShareGroup(shareName, shareColor, targetType, targetValues, function(isSuccess) {
			checkSuccess(isSuccess);
			shareGroupControl.reloadShareInfo();
		});
	},
	reloadShareInfo:function(){
		var _this = this;
		jQuery("#"+this.opt.drowFrame).loadbar("c_leftMenu");		
		SchedulerShareService.getShareGroupList(function(shareGroupList) {
			_this.shareGroupList = shareGroupList;
			_this.updateShareTree(shareGroupList);
			jQuery("#c_leftMenu").css("overflow","auto");
		});
	},
	updateShareTree:function(shareGroupList){
		jQuery("#"+this.opt.drowFrame).empty();
		jQuery("#"+this.opt.drowFrame).DTagTree(shareGroupList,this.opt);
		setToggleTreeMenu(this.opt.drowFrame);
	},
	modifyShareInfo:function(shareSeq) {
		SchedulerShareService.getShareGroupInfo(shareSeq, function(shareGroupInfo) {
			modifySharePopup(shareGroupInfo, "scheduler");
		});
	},
	updateShareInfo:function(shareSeq, shareName, shareColor, targetType, targetValues) {
		SchedulerShareService.modifyShareGroup(shareSeq, shareName, shareColor, targetType, targetValues, function(isSuccess) {
			checkSuccess(isSuccess);
			shareGroupControl.reloadShareInfo();
		});
	},
	deleteShareGroup:function(shareSeq) {
		SchedulerShareService.deleteShareGroup(shareSeq, function(isSuccess) {
			checkSuccess(isSuccess);
			shareGroupControl.reloadShareInfo();
		});
	}
});

function toogleMenuTrigger(){
	jQuery(window).trigger("resize");
}

function toggleMenu(id){
	var menu = jQuery("#"+id);	
	if(menu.css("display") != "none"){
		setDisplayLeftTree("N",id);
		jQuery.cookie("SR_"+id , "N",{path:"/"});
	} else {		
		setDisplayLeftTree("V",id);
		jQuery.cookie("SR_"+id , "V",{path:"/"});		
	}	
}

function setDisplayLeftTree(mode,id){
	var menu = jQuery("#"+id);
	var menuIcon = jQuery("#"+id+"_view");
	
	if(mode == "V"){
		menu.show();
		menuIcon.attr("src","/design/common/image/btn_menu_mius.gif");
	} else {
		menu.hide();
		menuIcon.attr("src","/design/common/image/btn_menu_plus.gif");		
	}	
	
}

function setToggleTreeMenu(id){
	var viewValue = jQuery.cookie("SR_"+id);
	if(viewValue){
		setDisplayLeftTree(viewValue,id);
	}
}

function addShareGroup() {
	
	resetShareGroup();
	jQuery(".TB_scheduleShare").css("width", "0%");
	jQuery("#shareSeq").val("");
	jQuery("#shareGroupName").val("");
	jQuery("#shareColor").val("");

	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 300,
			minWidth:530,
			btnList : [{name:comMsg.comn_add,func:saveShareGroup}],	
			openFunc:function(){
	 			jQuery(".TB_scheduleShare").css("width", "100%");
	 			jQuery("#share_box_pht").text(schedulerMsg.scheduler_share_add);
	 			makeShareTypeSelect();
			},
			beforeCloseFunc:function() {
				resetShareGroup();
			}
		};
	jQuery("#share_box").jQpopup("open",popupOpt);
}

function makeModifySharePopup(selectRadioId,share) {
	
	jQuery(".TB_scheduleShare").css("width", "0%");

	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 300,
			minWidth:530,
			btnList : [{name:comMsg.comn_modfy,func:saveShareGroup}],	
			openFunc:function(){
				jQuery("#shareSeq").val(share.id);
				jQuery("#shareGroupName").val(unescape_tag(share.name+" "));
				jQuery("#shareColor").val(share.color);
	 			jQuery(".TB_scheduleShare").css("width", "100%");
	 			jQuery("#share_box_pht").text(schedulerMsg.scheduler_share_modify);
	 			makeShareTypeSelect();
	 			jQuery("#" + selectRadioId).attr("checked", "checked");
			},
			beforeCloseFunc:function() {
				resetShareGroup();
			}
		};
	jQuery("#share_box").jQpopup("open",popupOpt);
}

function makeModifySettingSharePopup(selectRadioId,share) {
	jQuery(".TB_scheduleShare").css("width", "0%");

	var popupOpt = {
			closeName:comMsg.comn_close,
			btnClass:"btn_style3",
			minHeight: 300,
			minWidth:530,
			btnList : [{name:comMsg.comn_modfy,func:saveSettingShareGroup}],	
			openFunc:function(){
				jQuery("#shareSeq").val(share.id);
				jQuery("#shareGroupName").val(unescape_tag(share.name+" "));
				jQuery("#shareColor").val(share.color);
	 			jQuery(".TB_scheduleShare").css("width", "100%");
	 			jQuery("#share_box_pht").text(schedulerMsg.scheduler_share_modify);
	 			makeShareTypeSelect();
	 			if(selectRadioId=="shareTypeDomain")
	 			jQuery("#scheduleShareForm input:radio[name=shareType]:eq(1)").attr("checked","checked");
	 			jQuery("#" + selectRadioId).attr("checked", "checked");
			},
			beforeCloseFunc:function() {
				resetShareGroup();
			}
		};
	jQuery("#share_box").jQpopup("open",popupOpt);
}

function resetShareGroup() {
	var f = document.scheduleShareForm;
	jQuery("#shareGroupName").val("");
	if (isMsie8) {
		jQuery("#shareTypeSelect").attr("checked", "checked");
	} else {
		f.shareType[0].checked = true;
	}
	checkShareTargetMode();
	jQuery("#selectTypeSelect").selectboxSetValue("uid");
	jQuery("#shareSearchType").val("uid");
	jQuery("#searchShare").empty();
	jQuery("#shareTarget").empty();
	checkShareSelectType();
}

function saveShareGroup() {
	var f = document.scheduleShareForm;
	var shareGroupNameObj = jQuery("#shareGroupName");
	var shareSeq = jQuery("#shareSeq").val();
	if(!checkInputLength("jQuery", shareGroupNameObj, schedulerMsg.scheduler_share_name_empty, 1, 32)) {
		return;
	}
	if(!checkInputValidate("jQuery", shareGroupNameObj, "shareName")) {
		return;
	}

	var shareName = escape_tag(jQuery.trim(shareGroupNameObj.val()));	
	var shareGroupList = shareGroupControl.shareGroupList;
	if (jQuery.trim(shareSeq) == "") {
		if (shareGroupList && shareGroupList.length > 0) {
			for (var i=0; i<shareGroupList.length; i++) {
				if (shareName.toLowerCase() == shareGroupList[i].name.toLowerCase()) {
					alert(schedulerMsg.scheduler_share_name_exist);
					return;
				}
			}
			
			if (shareGroupList.length >= 50) {
				alert(schedulerMsg.scheduler_share_add_maxgroup);
				return;
			}
		}
	} else {
		if (shareGroupList && shareGroupList.length > 0) {
			for (var i=0; i<shareGroupList.length; i++) {
				if (jQuery.trim(shareSeq) != shareGroupList[i].id && shareName.toLowerCase() == shareGroupList[i].name.toLowerCase()) {
					alert(schedulerMsg.scheduler_share_name_exist);
					return;
				}
			}
		}
	}
	

	var shareColor = "#008000";
	var targetType;
	var targetValueOptions = f.shareTarget.options;
	var targetValues = [];
	if (f.shareType[0].checked) {
		targetType = f.shareType[0].value;
		if(!targetValueOptions || targetValueOptions.length == 0){
			alert(schedulerMsg.scheduler_share_target_notarget);		
			return;
		}
		for (var i = 0; i < targetValueOptions.length ; i++) {
			targetValues[targetValues.length] = targetValueOptions[i].value;
		}
	} else {
		targetType = f.shareType[1].value;
	}
	
	if (jQuery.trim(shareSeq) == "") {
		shareGroupControl.saveShareInfo(shareName, shareColor, targetType, targetValues);
	} 
	else {
		shareGroupControl.updateShareInfo(shareSeq, shareName, shareColor, targetType, targetValues);
	}
	jQuery("#share_box").jQpopup("close");
}

function modifyShareGroup(nodeData) {
	shareGroupControl.modifyShareInfo(nodeData.id);
}

function deleteShareGroup(nodeData) {
	if (!confirm(schedulerMsg.scheduler_share_alert_delete)) {
		return;
	}
	shareGroupControl.deleteShareGroup(nodeData.id);
}

function modifySharePopup(shareInfo, type) {
	
	resetShareGroup();
	
	var f = document.scheduleShareForm;
	
	if(shareInfo.isSuccess == "fail") {
		alert(schedulerMsg.scheduler_share_info_loading_fail);
		return;
	}
	var share = shareInfo.share;
	var select = jQuery("#shareTarget");
	var shareTargetList = shareInfo.shareTarget;
	var shareTarget;
	var targetType="";
	var targetValue="";
	var targetName = "";
	
	var selectRadioId = "shareTypeSelect";
	if (shareTargetList && shareTargetList.length > 0) {
		for (var i=0; i < shareTargetList.length; i++) {
			f.shareType[0].checked = true;
			
			shareTarget = shareTargetList[i];
			targetType = shareTarget.targetType;
			targetValue = shareTarget.targetValue;

			if (targetType == "org") {
				if (jQuery("#selectOrgSelect").html() == "") {
					getOrgList('modify', targetValue);
				} else {
					targetName = jQuery("#selectOrgSelect").selectboxGetText(targetValue);
					
					if (targetName != "") {
						if(!checkSelectDup("shareTarget", targetValue)){
							var option = jQuery("<option></option>").attr("value","O|"+targetValue).attr("title",schedulerMsg.scheduler_share_type_org+":"+targetName).append(schedulerMsg.scheduler_share_type_org+":"+targetName);
							select.append(option);
						}
					}
				}
			} else if (targetType == "user") {
				targetName = shareTarget.targetName;
				if (targetName != "") {
					if(!checkSelectDup("shareTarget", targetValue)){
						var option = jQuery("<option></option>").attr("value","U|"+targetValue).attr("title",schedulerMsg.scheduler_share_type_user+":"+targetName).append(schedulerMsg.scheduler_share_type_user+":"+targetName);
						select.append(option);
					}
				}
			} else {
				selectRadioId = "shareTypeDomain";
				f.shareType[1].checked = true;
			}
			checkShareTargetMode();
		}
	}
	
	if (type && type == 'scheduler') {
		makeModifySharePopup(selectRadioId,share);
	}
	else {
		makeModifySettingSharePopup(selectRadioId,share);
	}
}

function checkSuccess (isSuccess) {
	if (isSuccess == "fail") {
		alert(comMsg.error_msg_001);
	}
}

function checkShareSelectType() {
	var selectType = jQuery("#shareSearchType").val();

	if (selectType == "org") {
		checkOrgList();
	} 
	else {
		jQuery("#share_org").hide();
		jQuery("#share_notorg").show();
		jQuery("#share_org_btn").hide();
		jQuery("#share_notorg_btn").show();
		jQuery("#shareKeyword").val("");
	}
}

function checkShareTargetMode() {
	var f = document.scheduleShareForm;
	if (f.shareType[0].checked) {
		jQuery("#select_option_tr").show();
	} else {
		jQuery("#select_option_tr").hide();
	}
}

function searchShareUser() {
	var searchType = jQuery("#shareSearchType").val();
	var keyWordObj = jQuery("#shareKeyword");
	var keyWordValue = jQuery.trim(keyWordObj.val());
	var searchId;
	var searchName;
	
	if (searchType == "name") {
		if(!checkInputLength("jQuery", keyWordObj, schedulerMsg.scheduler_share_alert_search_nostr, 2, 255)) {
			return;
		}
		if(!checkInputValidate("jQuery", keyWordObj, "folderName")) {
			return;
		}
		searchName = keyWordValue;
		searchId = "";
	} else if (searchType == "uid") {
		if(!checkInputLength("jQuery", keyWordObj, schedulerMsg.scheduler_share_alert_search_nostr, 1, 64)) {
			return;
		}
		if(!checkInputValidate("jQuery", keyWordObj, "id")) {
			return;
		}
		searchId = keyWordValue;
		searchName = "";
	}
	
	var param = {"searchName":searchName,"searchId":searchId};
	
	jQuery("#share_notorg_btn").hide();
	jQuery("#searching_user").show();
	jQuery.post("/common/searchMailUserJson.do",param,settingSearchSharedUser,"json");
	jQuery("#keyWordObj").val("");
}

function settingSearchSharedUser(data, textStatus) {
	if(textStatus == "success"){
		var list = data.userList;

		if(!list || list.length ==0){
			jQuery("#share_notorg_btn").show();
			jQuery("#searching_user").hide();
			alert(schedulerMsg.scheduler_share_alert_search_nouser);
		} else {
			setSharedSearchUserList(list);
		}
	}
}

function setSharedSearchUserList(searchList){
	var select = jQuery("#searchShare");	
	if(searchList && searchList.length > 0){
		select.empty();
		for ( var i = 0; i < searchList.length; i++) {
			if(!checkSelectDup("shareTarget", searchList[i].mailUserSeq)){
				var option = jQuery("<option></option>").attr("value","U|"+searchList[i].mailUserSeq).attr("title",schedulerMsg.scheduler_share_type_user+":"+searchList[i].mailUid+"("+searchList[i].userName+")").append(schedulerMsg.scheduler_share_type_user+":"+searchList[i].mailUid+"("+searchList[i].userName+")");
				select.append(option);
			}
		}
	}
	jQuery("#share_notorg_btn").show();
	jQuery("#searching_user").hide();
}

function setSharedOrgList(){
	var select = jQuery("#shareTarget");
	var selectText = jQuery("#selectOrgSelect_selectText").text();
	var selectValue = jQuery("#orgSelect").val();
	if(!checkSelectDup("shareTarget", "O|"+selectValue)){
		var option = jQuery("<option></option>").attr("value","O|"+selectValue).attr("title",schedulerMsg.scheduler_share_type_org+":"+selectText).append(schedulerMsg.scheduler_share_type_org+":"+selectText);
		select.append(option);
	}
}

function getOrgList(mode, value) {
	jQuery("#share_notorg_btn").hide();
	jQuery("#searching_user").show();
	var param = {};
	jQuery.post("/dynamic/org/orgJsonlist.do",param, function(data) {
		if (mode == 'modify') {
			settingOrgSet(data, value);
		} else {
			settingOrgInit(data);
		}
	},"json");
}

function settingOrgSet(data, targetValue) {
	if (data.isSuccess) {
		var orgList = data.orgList;
		jQuery("#selectOrgSelect").empty();
		var orgArray = [];
		var orgNameArray;
		var orgFullCodeArray;
		var orgName = "";
		var orgFullCode = "";
		if (orgList.length > 0) {
			for (var i=0; i < orgList.length; i++) {
				orgName = "";
				orgFullCode = "";
				orgNameArray = orgList[i].nameArray;
				orgFullCodeArray = orgList[i].codeArray; 
				if (orgNameArray && orgNameArray.length > 0) {
					for (var j=0; j < orgNameArray.length; j++) {
						if (j == 0) {
							orgName += orgNameArray[j];
						} else {
							orgName += ">"+ orgNameArray[j];
						}
					}
				}
				if (orgFullCodeArray && orgFullCodeArray.length > 0) {
					orgFullCode = orgFullCodeArray[orgFullCodeArray.length-1];
				}
				orgArray.push({index:orgName,value:orgFullCode});
			}
			jQuery("#selectOrgSelect").selectbox({selectId:"orgSelect",width:220}, "", orgArray);
			jQuery("#share_notorg_btn").show();
			jQuery("#searching_user").hide();
			
			var select = jQuery("#shareTarget");
			
			var targetName = jQuery("#selectOrgSelect").selectboxGetText(targetValue);
			
			if (targetName != "") {
				if(!checkSelectDup("shareTarget", targetValue)){
					var option = jQuery("<option></option>").attr("value","O|"+targetValue).attr("title",schedulerMsg.scheduler_share_type_org+":"+targetName).append(schedulerMsg.scheduler_share_type_org+":"+targetName);
					select.append(option);
				}
			}
		} else {
			alert(schedulerMsg.scheduler_share_alert_org_loading_empty);
			jQuery("#selectTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			return;
		}
	} else {
		alert(schedulerMsg.scheduler_share_alert_org_loading_fail);
		jQuery("#selectTypeSelect").selectboxSetValue("uid");
		jQuery("#shareSearchType").val("uid");
		return;
	}
}

function settingOrgInit(data) {
	if (data.isSuccess) {
		var orgList = data.orgList;
		jQuery("#selectOrgSelect").empty();
		var orgArray = [];
		var orgNameArray;
		var orgFullCodeArray;
		var orgName = "";
		var orgFullCode = "";
		if (orgList.length > 0) {
			for (var i=0; i < orgList.length; i++) {
				orgName = "";
				orgFullCode = "";
				orgNameArray = orgList[i].nameArray;
				orgFullCodeArray = orgList[i].codeArray; 
				if (orgNameArray && orgNameArray.length > 0) {
					for (var j=0; j < orgNameArray.length; j++) {
						if (j == 0) {
							orgName += orgNameArray[j];
						} else {
							orgName += ">"+ orgNameArray[j];
						}
					}
				}
				if (orgFullCodeArray && orgFullCodeArray.length > 0) {
					orgFullCode = orgFullCodeArray[orgFullCodeArray.length-1];
				}
				orgArray.push({index:orgName,value:orgFullCode});
			}
			jQuery("#selectOrgSelect").selectbox({selectId:"orgSelect",width:220}, "", orgArray);
			jQuery("#share_notorg").hide();
			jQuery("#share_org").show();
			jQuery("#share_notorg_btn").hide();
			jQuery("#share_org_btn").show();
			jQuery("#searching_user").hide();
		} else {
			alert(schedulerMsg.scheduler_share_alert_org_loading_empty);
			jQuery("#selectTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			jQuery("#share_notorg_btn").show();
			jQuery("#searching_user").hide();
			return;
		}
	} else {
		alert(schedulerMsg.scheduler_share_alert_org_loading_fail);
		jQuery("#selectTypeSelect").selectboxSetValue("uid");
		jQuery("#shareSearchType").val("uid");
		jQuery("#share_notorg_btn").show();
		jQuery("#searching_user").hide();
		return;
	}
}

function checkOrgList() {
	if (jQuery("#selectOrgSelect").html() == "") {
		getOrgList();
	} else {
		var orgOptionsCount = jQuery("#selectOrgSelect").selectboxGetOptionsCount();

		if (orgOptionsCount == 0) {
			alert(schedulerMsg.scheduler_share_alert_org_loading_empty);
			jQuery("#selectTypeSelect").selectboxSetValue("uid");
			jQuery("#shareSearchType").val("uid");
			return;
		}
		else {
			jQuery("#share_notorg").hide();
			jQuery("#share_org").show();
			jQuery("#share_notorg_btn").hide();
			jQuery("#share_org_btn").show();
		}
	}	
}

function makeInputShareGroupList() {
	var shareGroupList = shareGroupControl.shareGroupList;
	
	jQuery("#shareGroupSelect").empty();
	
	if (shareGroupList && shareGroupList.length > 0) {
		var shareArray = [];
		shareArray.push({index:"------------------------------",value:""});
		for (var i=0; i<shareGroupList.length; i++) {
			shareArray.push({index:shareGroupList[i].name,value:shareGroupList[i].id});
		}
		jQuery("#shareGroupSelect").selectbox({selectId:"shareGroup",selectFunc:"",width:200}, "", shareArray);
	} else {
		var shareArray = [{index:"------------------------------",value:""}];
		jQuery("#shareGroupSelect").selectbox({selectId:"shareGroup",selectFunc:"",width:200}, "", shareArray);
		jQuery("#shareGroupSelect").selectboxDisable();
	}
}

function resetInputShareGroup() {
	jQuery("#shareGroupSelect").empty();
	jQuery("#shareTargetEmail").empty();
	jQuery("#checkShare").attr("checked", false);
	jQuery("#selfShareTarget").attr("checked", false);
	checkUseShare();
	checkSelfTarget();
}

function checkUseShare() {
	var check = jQuery("#checkShare").attr("checked");
	if (check) {
		jQuery("#shareGroupWrap").show();
	} else {
		jQuery("#shareGroupWrap").hide();
	}
}

function checkSelfTarget() {
	var check = jQuery("#selfShareTarget").attr("checked");
	if (check) {
		jQuery("#selfTargetWrap").show();
	} else {
		jQuery("#selfTargetWrap").hide();
	}
}

function addTargetEmail() {
	var targetId = trim(jQuery("#targetId").val());
	var targetDomain = trim(jQuery("#targetDomain").val());
	
	if (targetId == "") {
		alert(comMsg.error_login_id);
		jQuery("#targetId").focus();
		return;
	}
	if (targetDomain == "") {
		alert(comMsg.error_login_domain);
		jQuery("#targetDomain").focus();
		return;
	}
	if (!isId(targetId)) {
		alert(comMsg.error_id);
		jQuery("#targetId").select();
		return;
	}
	if (!isDomain(targetDomain)) {
		alert(comMsg.error_domain);
		jQuery("#targetDomain").select();
		return;
	}
	
	var email = targetId+"@"+targetDomain;
	
	var select = jQuery("#shareTargetEmail");
	if (!checkSelectDup("shareTargetEmail", email)) {
		var option = jQuery("<option></option>").attr("value",email).attr("title",email).append(email);
		select.append(option);
	}
	jQuery("#targetId").val("");
	jQuery("#targetDomain").val("");
}

function checkSelectDup(id, value){
	var selectOption = jQuery("#"+id+" option");
	var isExist = false;
	for ( var i = 0; i < selectOption.length; i++) {
		if(value == jQuery(selectOption[i]).attr("value")){
			isExist = true;
			break;
		}
	}
	
	return isExist;
}

function addSharedUser(){
	var selectedIndex = $("searchShare").selectedIndex;
	var searchListOptions = $("searchShare").options;
	var shareListOptions = $("shareTarget").options;
	if(selectedIndex < 0){
		alert(comMsg.error_noselect);
		return;
	}
	
	var uid, text, suid;	
	for (var i = 0; i < searchListOptions.length ; i++) {
		if (searchListOptions[i].selected) {
			uid = searchListOptions[i].value;
			text = searchListOptions[i].text;
			var isExist = false;
			for ( var j = 0; j < shareListOptions.length; j++) {
				suid = shareListOptions[j].value;
				if(uid == suid){
					isExist = true;
					break;
				}
			}	
			
			if(!isExist){
				var option = jQuery("<option></option>").attr("value",uid).attr("title",text).append(text);
				jQuery("#shareTarget").append(option);
			}
			searchListOptions[i] = null;
			i--;
		}
	}
}

function removeSharedUser(){
	var shareListOptions = $("shareTarget").options;
	var searchListOptions = $("searchShare").options;
	var selectedIndex = $("shareTarget").selectedIndex;
	if(selectedIndex < 0){
		alert(comMsg.error_noselect);
		return;
	}
	
	for (var i = 0; i < shareListOptions.length;) {
		if (shareListOptions[i].selected) {
			uid = shareListOptions[i].value;
			text = shareListOptions[i].text;
			var isExist = false;
			for ( var j = 0; j < searchListOptions.length; j++) {
				suid = searchListOptions[j].value;
				if(uid == suid){
					isExist = true;
					break;
				}
			}	
			
			if(!isExist){
				var option = jQuery("<option></option>").attr("value",uid).attr("title",text).append(text);
				jQuery("#searchShare").append(option);
			}
			shareListOptions[i] = null;
		} else {
			i++;
		}
	}
}

function viewShareGroupHelp() {
	jQuery("#schedulerHelp").show();
}
function hideShareGroupHelp() {
	jQuery("#schedulerHelp").hide();
}

function none(){}

function makeShareTypeSelect(){
	jQuery("#selectTypeSelect").empty();
	jQuery("#selectTypeSelect").selectbox({selectId:"shareSearchType",
										selectFunc:checkShareSelectType,
										width:80}, 
										"uid", selectArray);
}