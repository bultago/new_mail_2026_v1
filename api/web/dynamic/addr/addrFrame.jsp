<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/address_style.css" />
<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/popup_style.css" />

<script type="text/javascript" src="/js/ext-lib/jcookie.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>


<script type="text/javascript" src="addrCommon.js"></script>
<script type="text/javascript" src="addrMenuBar.js"></script>
<script type="text/javascript" src="/js/common-lib/common-tree.js"></script>
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>

<script type="text/javascript">
var addrOption = {
		mainLID : "a_contentMain",
		subLID : "a_contentSub",
		treeLID : "sharedTreeviewer",
		listAction : "/dynamic/addr/listAddress.do",
		viewAction : "/dynamic/addr/viewAddress.do",
		writeAction : "/dynamic/addr/addAddress.do",
		readerListAction : "/dynamic/addr/readerList.do",
		moderatorListAction : "/dynamic/addr/moderatorList.do",
		sharedListAction : "/dynamic/addr/sharedBookList.do"
	};

var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};

</script>

</head>

<body>

<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>
<input type="hidden" id="bookSeq">
<input type="hidden" id="groupSeq">
<input type="hidden" id="emails" style="width: 500px;">
<input type="hidden" id="selectedGroupName">
<input type="hidden" id="installLocale" value="${installLocale}">
<form id="mailForm" onsubmit="false">
	<input type="hidden" name="wmode" value="popup">
	<input type="hidden" name="wtype" value="send">
	<input type="hidden" id="to" name="to">
</form>
<form id="printForm" onsubmit="false">
	<input type="hidden" name="groupSeq">
	<input type="hidden" name="bookSeq">
	<input type="hidden" name="leadingPattern">
	<input type="hidden" name="searchType">
	<input type="hidden" name="keyWord">
	<input type="hidden" name="pagePrint" value="true">
</form>
<div id="a_mainBody">
	<div id="a_leftMenu">
		<div>				
			<div id="ml_btnFMain_s">
				<div class="TM_topLeftMain">				
				<div class="mainBtn" >					
					<table cellpadding="0" cellspacing="0" border="0" class="TM_mainBtn_bg">
						<tr>
						<td nowrap style="white-space: nowrap;" >
							<div class="TM_t_left_btn"><a href="javascript:;" onclick="leftMenuControl.showPrivateTreeViewer();" class="TM_mainBtn_split" ><div class="TM_addrPersonal"><tctl:msg key="addr.tree.tab1.title" bundle="addr"/></div></a></div>
						</td>						
						<td nowrap style="white-space: nowrap;" >
							<div class="TM_t_right_btn"><a href="javascript:;" onclick="leftMenuControl.showSharedTreeViewer();" ><div class="TM_addrPublic"><tctl:msg key="addr.tree.tab2.title" bundle="addr"/></div></a></div>
						</td>
						</tr>
					</table>					
				</div>			
				</div>
			</div>
			
			<div id="leftMenuRcontentWrapper">
			<div id="leftMenuRcontent">
			<div>	
			<div id="privateTreeviewerWrapper">
			<ul id="privateTreeviewer">
				<li class="splitLine">
					<span class="icon_address"><a onclick="viewPrivateGroupMember('', 0);" href="javascript:;"><tctl:msg key="addr.tree.all.label" bundle="addr"/></a></span>
				</li>
				<li>
					<span class="tree-toolbar">
						<a onclick="leftMenuControl.addPrivateGroup();" href="javascript:;"><tctl:msg key="addr.tree.add.label" bundle="addr"/></a> 
						<a onclick="leftMenuControl.managePrivateGroup();" href="javascript:;"><tctl:msg key="addr.tree.manage.label" bundle="addr"/></a>
					</span>
					<span class="icon_address"><tctl:msg key="addr.tree.groups.label" bundle="addr"/></span>
						<ul id="privateGroups"></ul>
				</li>
			</ul>			
			</div>
			
			<div id="sharedTreeviewerWrapper">
				<ul id="sharedTreeviewer">
				</ul>	
			</div>
			</div>
			</div>
			</div>
			<%@include file="/common/sideMenu.jsp"%>		
		</div>
	</div>

	<div id="a_contentBodyWapper" class="TM_contentBodyWapper">
		
		<div style="position:relative">	
		<div class="TM_folderInfo">
			<img src="/design/common/image/blank.gif" class="TM_barLeft">
			<div class="TM_finfo_data addr_div_ellipsised">
				<span id="addrGroupLabel" class="TM_work_title"><tctl:msg key="addr.tree.tab1.title" bundle="addr"/></span>
			</div>
			<div class="TM_finfo_search">
				<div class="TM_mainSearch">					
				<div style="float: left">					
					<div id="searchTypeSelect"></div>
				</div>
				<div style="float: left">
					&nbsp;<input type="text" class="searchBox"  id="skeyWord" name="skeyWord"  onKeyPress="(keyEvent(event) == 13) ? searchAddress() : '';" /><a href="#" onclick="searchAddress()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a>
				</div>
				<div class="fclear"></div>					
				</div>
			</div>	
			<img src="/design/common/image/blank.gif" class="TM_barRight">			
		</div>
		</div>	
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>		
		<div id="a_mainContent" class="TM_mainContent">
		<div>		
			<div class="mail_body_tabmenu">
				<div class="mail_body_tab" id="menuBarTab"></div>
				<div id="pageNavi"  class="mail_body_navi">				
				</div>
				<div id="print" class="mail_body_navi">
				</div>
			</div>		
			<div class="mail_body_menu">
				<div class="menu_main_unit" id="menuBarContent"></div>
			</div>
		</div>	
		
		<div id="a_contentBody" >
			<div id="a_contentMain"></div>
			<div id="a_contentSub"></div>
		</div>
		</div>
	</div>
</div>

<div id="privateGroupDialog"  title="<tctl:msg key="addr.dialog.title.001" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>		
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.001" bundle="addr"/></th>
		<td><input type="text" id="pGroupName" name="pGroupName" class="IP200" onKeyPress="(keyEvent(event) == 13) ? okPrivateGroupAddPressed() : '';"/></td>
		</tr>
	</table>
	
</div>

<div id="sharedGroupDialog"  title="<tctl:msg key="addr.dialog.title.001" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>		
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.001" bundle="addr"/></th>
		<td><input type="text" id="sGroupName" name="sGroupName" class="IP200" onKeyPress="(keyEvent(event) == 13) ? okSharedGroupAddPressed() : '';"/></td>
		</tr>
	</table>
</div>

<div id="managePrivateGroupDialog"  title="<tctl:msg key="addr.dialog.title.002" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>		
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.002" bundle="addr"/></th>
		<td>
			<div id="privateGroupFolderSelect"></div>
		</td>
		</tr>
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.001" bundle="addr"/></th>
		<td><input type="text" id="newPrivateGroupName" name="newPrivateGroupName" class="IP200" /></td>
		</tr>
	</table>
</div>

<div id="manageSharedGroupDialog"  title="<tctl:msg key="addr.dialog.title.002" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.002" bundle="addr"/></th>
		<td>
			<div id="sharedGroupFolderSelect"></div>
		</td>
		</tr>
		<tr>
		<th class="lbout"><tctl:msg key="addr.group.label.001" bundle="addr"/></th>
		<td><input type="text" id="newSharedGroupName" name="newSharedGroupName" class="IP200"/></td>
		</tr>
	</table>

</div>

<div id="sharedAddrDialog"  title="<tctl:msg key="addr.btn.add.book" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>	
		<tr>
		<th class="lbout"><tctl:msg key="addr.book.label.001" bundle="addr"/></th>
		<td><input type="text" class="addrBookName" id="sharedAddrAddInput" name="sharedAddrAddInput" class="IP200" onKeyPress="(keyEvent(event) == 13) ? okSharedAddressBookAddPressed() : '';"/></td>
		</tr>
	</table>
</div>

<div id="sharedAddrRenameDialog"  title="<tctl:msg key="addr.btn.rename.book" bundle="addr"/>" style="display: none">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>
		<tr>
		<th class="lbout"><tctl:msg key="addr.book.label.001" bundle="addr"/></th>
		<td><input type="text" class="addrBookName" id="sharedAddrRenameInput" name="sharedAddrRenameInput" class="IP200" onKeyPress="(keyEvent(event) == 13) ? okSharedAddressBookRenamePressed() : '';"/></td>
		</tr>
	</table>
</div>

<div id="addAddrDialog"  title="<tctl:msg key="addr.dialog.title.004" bundle="addr"/>" style="display: none">
<div id="addAddress">
	<div class="addTree_tab">
		<a id="add_header1" class="btn_tabmenu2_on" href="#" onclick="toggleTab2('add_header', 'fragment-', 1, 2, 3)"><span><tctl:msg key="addr.dialog.header.tab1.title" bundle="addr"/></span></a>
		<a id="add_header2" class="btn_tabmenu2_off" href="#" onclick="toggleTab2('add_header', 'fragment-', 2, 1, 3)"><span><tctl:msg key="addr.dialog.header.tab2.title" bundle="addr"/></span></a>
		<a id="add_header3" class="btn_tabmenu2_off" href="#" onclick="toggleTab2('add_header', 'fragment-', 3, 1, 2)"><span><tctl:msg key="addr.dialog.header.tab3.title" bundle="addr"/></span></a>
	</div>
    <div id="fragment-1" >
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="100px"></col>
			<col></col>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.003" bundle="addr"/></th>
			<td>
				<input type="text" id="lastName" value="" class="IP100px" onkeyup="checkName()">
				<input type="hidden" id="memberSeq" value="">
			</td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.001" bundle="addr"/></th>
			<td><input type="text" id="firstName" value="" class="IP100px" onkeyup="checkName()"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.002" bundle="addr"/></th>
			<td><input type="text" id="middleName" value="" class="IP100px" onkeyup="checkName()"></td>
			</tr>
			<tr>
			<th class="lbout">* <tctl:msg key="addr.info.label.004" bundle="addr"/></th>
			<td>
				<input type="text" id="memberName" value="" class="IP200">
				<c:if test="${installLocale eq 'jp'}">
				<br>
				<div style="padding-top: 5px;">
					<tctl:msg key="addr.info.msg.052" bundle="addr"/>
				</div>	
				</c:if>
			</td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.005" bundle="addr"/></th>
			<td><input type="text" id="nickName" value="" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout">* <tctl:msg key="addr.info.label.006" bundle="addr"/></th>
			<td><input type="text"  id="memberEmail" value="" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.007" bundle="addr"/></th>
			<td><input id="birthDay" type="text" value="" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.008" bundle="addr"/></th>
			<td><input type="text" id="anniversaryDay" value="" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
			<td><input type="text" id="mobileNo" value="" class="IP200"></td>
			</tr>
		</table>
		
		
    </div>
    <div id="fragment-2"  style="display: none">
       <table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="100px"></col>
			<col></col>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.011" bundle="addr"/></th>
			<td>
				<input type="text"  id="homePost1"  class="IP50" style="width: 30px;" maxlength="4">
				-
				<input type="text"  id="homePost2"  class="IP50" style="width: 40px;" maxlength="4">
				<input type="hidden"  id="homePost">
			</td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.012" bundle="addr"/></th>
			<td><input type="text" id="homeCountry" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.013" bundle="addr"/></th>
			<td><input type="text" id="homeState" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.014" bundle="addr"/></th>
			<td><input type="text" id="homeCity" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.015" bundle="addr"/></th>
			<td><input type="text" id="homeStreet" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.016" bundle="addr"/></th>
			<td><input type="text" id="homeExtAddress" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.017" bundle="addr"/></th>
			<td><input type="text" id="homeTel" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.018" bundle="addr"/></th>
			<td><input type="text" id="homeFax" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.019" bundle="addr"/></th>
			<td>
				<input type="text" id="privateHomepage" class="IP200" value="<tctl:msg key="addr.info.label.020" bundle="addr"/>">
			</td>
			</tr>
		</table>
    </div>
    <div id="fragment-3"  style="display: none">
        <table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="100px"></col>
			<col></col>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.031" bundle="addr"/></th>
			<td><input type="text" id="companyName" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
			<td><input type="text" id="departmentName" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
			<td><input type="text" id="titleName" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.021" bundle="addr"/></th>
			<td>
				<input type="text"  id="officePost1" class="IP50" style="width: 30px;" maxlength="4">
				-
				<input type="text"  id="officePost2" class="IP50" style="width: 40px;" maxlength="4">
			</td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.022" bundle="addr"/></th>
			<td><input type="text" id="officeCountry" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.023" bundle="addr"/></th>
			<td><input type="text" id="officeState" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.024" bundle="addr"/></th>
			<td><input type="text" id="officeCity" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.025" bundle="addr"/></th>
			<td><input type="text" id="officeStreet" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.026" bundle="addr"/></th>
			<td><input type="text" id="officeExtAddress" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.027" bundle="addr"/></th>
			<td><input type="text" id="officeTel" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.028" bundle="addr"/></th>
			<td><input type="text" id="officeFax" class="IP200"></td>
			</tr>
			<tr>
			<th class="lbout"><tctl:msg key="addr.info.label.029" bundle="addr"/></th>
			<td>
				<input type="text" id="officeHomepage" class="IP200" value="<tctl:msg key="addr.info.label.030" bundle="addr"/>">
			</td>
			</tr>
		</table>
    </div>
</div>
</div>
<div id="addAddrConfirm" title="<tctl:msg key="addr.dialog.title.004" bundle="addr"/>" style="display:none">
    <div style="text-align:center;font-weight:bold;height:50px;line-height:30px">
        <span id="dupQuestion"></span><br>
        <span id="dupEmailAddress" style="color:#3D83C1"></span> : <span id="dupEmail" style="color:#FF8000"></span> / <span id="dupName" style="color:#FF8000"></span>
    </div>
</div>
<div id="addReaderDialog"  title="<tctl:msg key="addr.dialog.title.006" bundle="addr"/>" style="display: none">
	<div id="addReaderFrame"></div>
</div>
<div id="addModeratorDialog"  title="<tctl:msg key="addr.dialog.title.007" bundle="addr"/>" style="display: none">
	<div id="addModeratorFrame"></div>
</div>

<div id="importAddressDialog" title="<tctl:msg key="addr.dialog.title.008" bundle="addr"/>" style="display: none">

<form id="uploadForm" enctype="multipart/form-data" method="post">
<input type="hidden" name="theBookSeq"/>
<input type="hidden" name="theGroupSeq"/>
<input type="hidden" name="theVendor"/>
<input type="hidden" name="theEncoding"/>
<input type="hidden" name="addrAddType"/>
	<table cellpadding="0" cellspacing="0" class="TB_addressAdd" id="sharedAddrAdd" style="width:100%">
	<tr>
		<th>
			<tctl:msg key="addr.book.label.002" bundle="addr"/>
		</th>
		<td>
		 	<input type="file" class="addr_attFile" name="theFile"/>
		</td>
	</tr>
	<tr>
		<th>
			<tctl:msg key="addr.book.label.003" bundle="addr"/>
		</th>
		<td>
			<div id="vendorSelect"></div>
		</td>
	</tr>
	<tr>
		<th>
			<tctl:msg key="addr.book.label.004" bundle="addr"/>
		</th>
		<td>
			<div id="encodingSelect"></div>
		</td>
	</tr>
	<tr>
		<th>
			<tctl:msg key="addr.group.label.002" bundle="addr"/>
		</th>
		<td>
			<div id="toGroupSelect"></div>
		</td>
	</tr>
	<tr>
        <th>
            <tctl:msg key="addr.add.type.04" bundle="addr"/>
        </th>
        <td>
            <div id="dupAddressSelect"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div class="TM_notice_check" style="padding:10px 5px 10px 45px">
                <tctl:msg key="addr.add.type.05" bundle="addr"/>
            </div>          
        </td>
    </tr>
	</table>
</form>	
</div>

<div id="exportAddressDialog" title="<tctl:msg key="addr.dialog.title.009" bundle="addr"/>" style="display: none">

<form id="downloadForm" method="post">
<input type="hidden" name="theBookSeq"/>
<input type="hidden" name="theGroupSeq"/>
	<table cellpadding="0" cellspacing="0" class="TB_addressAdd" id="sharedAddrAdd" style="width:100%">
	<tr>
		<th>
			<tctl:msg key="addr.book.label.003" bundle="addr"/>
		</th>
		<td>
			<div id="theVendorSelect"></div>
		</td>
	</tr>
	<tr>
		<th>
			<tctl:msg key="addr.book.label.004" bundle="addr"/>
		</th>
		<td>
			<div id="theEncodingSelect"></div>
		</td>
	</tr>
	</table>
</form>	

</div>
<div id="workHiddenFrame" style="display: none"></div>
<%@include file="/common/bottom.jsp"%>
</body>
</html>