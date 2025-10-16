<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/address_style.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/org_style.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/popup_style.css" />

<script type="text/javascript" src="/js/ext-lib/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.async.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.form.js"></script>
<script type="text/javascript" src="addrPopup.js"></script>
<script type="text/javascript" src="/dwr/interface/AddressBookService.js"></script>
<script type="text/javascript" src="/dwr/interface/OrgService.js"></script>
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>

<script type="text/javascript">
var addrOption = {
		mainLID : "popupList",
		treeLID : "sharedTreeviewer",
		listAction : "/dynamic/addr/addrPopupList.do",
		sharePopupTreeAction : "/dynamic/addr/sharePopupTree.do"
	};
var orgOption = {
		mainLID : "popupList",
		treeLID : "orgTree",
		treeAction : "/dynamic/org/orgPopupTree.do",
		listAction : "/dynamic/org/orgPopupList.do"
	};

var titleOption = {
		mainLID : "popupList",
		treeLID : "orgTree",
		treeAction : "/dynamic/org/titlePopupTree.do",
		listAction : "/dynamic/org/titlePopupList.do"
	};
var gradeOption = {
		mainLID : "popupList",
		treeLID : "orgTree",
		treeAction : "/dynamic/org/gradePopupTree.do",
		listAction : "/dynamic/org/gradePopupList.do"
	};
	
function closePop(){
	if(opener){
		window.close();
	}else{
		parent.modalPopupForSearchAddrClose();
	}
}

function confirmAddr(){
	var toList = $("toList");	
	var toListVal = getSddrList(toList.options);
	var ccList = $("ccList");	
	var ccListVal = getSddrList(ccList.options);
	var bccList = $("bccList");	
	var bccListVal = getSddrList(bccList.options);

	if(toListVal == "" && ccListVal == "" && bccListVal == ""){
		alert(addrMsg.addr_error_msg_11);
		return;
	}
	/*
	try{
		opener.document;
	}catch(e){
		alert('<tctl:msg key="addr.error.msg.13" bundle="addr"/>');
		window.close();
		return;
	}
	*/
	if(opener){
		opener.insertAddr("to",toListVal);
		opener.insertAddr("cc",ccListVal);
		opener.insertAddr("bcc",bccListVal);
		window.close();
	}else{
		parent.insertAddr("to",toListVal);
		parent.insertAddr("cc",ccListVal);
		parent.insertAddr("bcc",bccListVal);
		parent.modalPopupForSearchAddrClose();
	}
	
}

function getSddrList(listOptions){
	
	var list =[];
	if(listOptions){
		for ( var i = 0; i < listOptions.length; i++) {
			list[list.length]= listOptions[i].value;		
		}
	}

	return list;
}

function jQpopupClear(){
	setTimeout(function(){
		jQuery(":text:first").focus();
	},300);
	
}

if(parent){
	parent.setSearchAddrPopupTitle("<tctl:msg key="addr.search.addr" bundle="addr"/>");
	jQpopupClear();
}
	
	

</script>
</head>

<body class="popupBody">
<div id="mainLoaddingMessage" class="TM_main_loadding_mask">
	<div id="mainLoadMessage" class="TM_c_loadding"></div>
</div>

<input type="hidden" id="bookSeq"> 
	<div class="popup_style1" style="border:0px;">
		<div class="popup_style1_title" style="display:none;">
			<div class="popup_style1_title_left">
				<span class="SP_title"><tctl:msg key="addr.search.addr" bundle="addr"/></span>
			</div>
			<div class="popup_style1_title_right">
				<a href="javascript:;" class="btn_X" onclick="closePop()"><tctl:msg key="comn.close" bundle="common"/></a>
			</div>
		</div>
		
		<table class="TB_popup_style1_body">
			<tbody>
				<tr>
					<td colspan="5">
						<div class="addTree_tab" style="margin-top:8px;">
							<a href="javascript:;" onclick="privateAddr()"><span><tctl:msg key="addr.tree.tab1.title" bundle="addr"/></span></a>
							<a href="javascript:;" onclick="sharedAddr()"><span><tctl:msg key="addr.tree.tab2.title" bundle="addr"/></span></a>
							<a href="javascript:;" onclick="orgAddr()" id="orgTabBtn"><span><tctl:msg key="comn.top.org" bundle="common"/></span></a>
						</div>
					</td>
				</tr>
			</tbody>
		 </table>
		 <div style="clear:both;height:8px;"></div>
		 <table class="TB_popup_style1_body">
			<tbody>
				<tr>
					<td class="bg_left" style="background:none;"></td>
					<td class="popup_content" style="padding-bottom:0px;">
						<table class="TB_content">
							<tbody>
								<tr>
									<td class="bg_left" style="background:none;"></td>
									<td class="leftArea" style="width:230px;">
										<div id="privateTreeviewerWrapper" class="TM_popupContentWrapper">
										<ul id="privateTreeviewer" class="addTree" style="border:#c8c8c8 1px solid;height:385px;">
											<li><span class="icon_address"><a id="gadr_0" class="gadr" onclick="viewPrivateGroupMember('', 0);" href="javascript:;"><tctl:msg key="comn.all" bundle="common"/></a></span>
												<ul id="privateGroups"></ul>
											</li>
										</ul> 
										</div>
										<div id="sharedTreeviewerWrapper" class="TM_popupContentWrapper">
											<ul id="sharedTreeviewer" class="addTree" style="border:#c8c8c8 1px solid;height:385px;"></ul>	
										</div>
										<div id="orgTreeviewerWrapper" class="TM_popupContentWrapper">
											<ul id="orgTree" class="addTree" style="border:#c8c8c8 1px solid;height:385px;"></ul>	
										</div>
									</td>
									<td class="bg_left" style="background:none;"></td>
									<td class="centerArea" style="width:550px;">
										<table class="TB_addSearch">
											<tbody>
											<tr>
												<td class="centerArea">													
													<div id="popupList" class="addList" style="overflow-y:auto;overflow-x:hidden;"></div>
												</td>
												<td id="alphabetArea" class="alphabetArea">
													<ul id="leadingGroup"></ul>
												</td>
											</tr>
											</tbody>
										</table>
									</td>
									
									<td class="rightArea" style="width:370px;">
										<form name="addressForm">
										<input type="hidden" id="addrType" value="gadr">
										<input type="hidden" id="groupName">
										<input type="hidden" id="orgName">
										<table class="TB_recive">
											<tbody>
												<tr>
													<td style="height:5px;">
													</td>
												</tr>
												<tr>
													<td style="width:85px;">
														<ul class="btnArea" style="padding-left:10px;padding-right:10px;">
															<li class="group"><a class="btn_addressAdd" href="javascript:;" onclick="addGroup(addressForm.toList)"><span><tctl:msg key="addr.dialog.title.001" bundle="addr"/></span></a></li>
															<li><a class="btn_addressAdd" style="margin-left:10px;" href="javascript:;" onclick="addressAdd(addressForm.toList)"><span><tctl:msg key="addr.tree.add.label" bundle="addr"/></span></a></li>
															<li><a class="btn_addressDel" style="margin-left:10px;" href="javascript:;" onclick="addressDel(addressForm.toList)"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
														</ul>
													</td>
													<td>
														<ul class="contentArea">
															<li><span class="title_arrow4"><tctl:msg key="mail.to" /></span></li>
															<li><select name="toList"  id="toList" size="7" multiple=""/></li>
														</ul>
													</td>
												</tr>
												<tr>
													<td style="height:10px;">
													</td>
												</tr>
												<tr>
													<td>
														<ul class="btnArea" style="padding-left:10px;padding-right:10px;">
															<li class="group"><a class="btn_addressAdd" href="javascript:;" onclick="addGroup(addressForm.ccList)"><span><tctl:msg key="addr.dialog.title.001" bundle="addr"/></span></a></li>
															<li><a class="btn_addressAdd" style="margin-left:10px;" href="javascript:;" onclick="addressAdd(addressForm.ccList)"><span><tctl:msg key="addr.tree.add.label" bundle="addr"/></span></a></li>
															<li><a class="btn_addressDel" style="margin-left:10px;" href="javascript:;" onclick="addressDel(addressForm.ccList)"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
														</ul>
													</td>
													<td>
														<ul class="contentArea">
															<li><span class="title_arrow4"><tctl:msg key="mail.cc" /></span></li>
															<li><select name="ccList" id="ccList" size="7" multiple=""/></li>
														</ul>
													</td>
												</tr>
												<tr>
													<td style="height:10px;">
													</td>
												</tr>
												<tr>
													<td>
														<ul class="btnArea" style="padding-left:10px;padding-right:10px;">
															<li class="group"><a class="btn_addressAdd" href="javascript:;" onclick="addGroup(addressForm.bccList)"><span><tctl:msg key="addr.dialog.title.001" bundle="addr"/></span></a></li>
															<li><a class="btn_addressAdd" style="margin-left:10px;" href="javascript:;" onclick="addressAdd(addressForm.bccList)"><span><tctl:msg key="addr.tree.add.label" bundle="addr"/></span></a></li>
															<li><a class="btn_addressDel" style="margin-left:10px;" href="javascript:;" onclick="addressDel(addressForm.bccList)"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
														</ul>
													</td>
													<td>
														<ul class="contentArea">
															<li><span class="title_arrow4"><tctl:msg key="mail.bcc" /></span></li>
															<li><select name="bccList"  id="bccList" size="7" multiple=""/></li>
														</ul>
													</td>
												</tr>
											</tbody>
										</table>
										</form>
									</td>
								</tr>
							</tbody>
						</table>
						<div class="downBtn" style="margin-bottom:0px;">
							<a class="btn_style2" href="javascript:;" onclick="confirmAddr()"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
							<a class="btn_style3" href="javascript:;" onclick="closePop();"><span><tctl:msg key="comn.cancel" bundle="common"/></span></a>
						</div>
					</td>
					<td class="bg_right" style="background:none;"></td>
				</tr>
			</tbody>
		</table>
		<div class="popup_style1_down" style="display:none;">
			<div class="popup_style1_down_left"><img src="/design/common/image/blank.gif" class="popup_style1_down_right"></div>
		</div>
	</div>	
<%@include file="/common/xecureOcx.jsp" %>
</body>
</html>