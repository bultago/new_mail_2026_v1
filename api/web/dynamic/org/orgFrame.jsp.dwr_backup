<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/org_style.css" />

<script type="text/javascript" src="/js/ext-lib/jcookie.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.popup.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.treeview.async.js"></script>
<script type="text/javascript" src="/js/ext-lib/jquery.dynamicPane.js"></script>
<script type="text/javascript" src="/js/ext-lib/ddaccordion.js"></script>
<script type="text/javascript" src="/js/ext-lib/menuToolBar.js"></script>

<script type="text/javascript" src="/dwr/interface/OrgService.js"></script>
<script type="text/javascript" src="/dwr/interface/AddressBookService.js"></script>

<script type="text/javascript" src="orgCommon.js"></script>
<script type="text/javascript" src="/js/common-lib/common-tree.js"></script>
<script type="text/javascript" src="/i18n?bundle=addr&var=addrMsg&locale=<%=locale%>"></script>
<script type="text/javascript" src="/i18n?bundle=mail&locale=<%=locale%>"></script>

<script type="text/javascript">
var orgOption = {
		mainLID : "o_contentMain",
		treeLID : "orgTreeviewer",
		subLID : "o_contentSub",
		treeAction : "/dynamic/org/orgTree.do",
		listAction : "/dynamic/org/memberList.do",
		toAction : "/dynamic/org/orgSendTo.jsp",
		viewAction : "/dynamic/addr/viewAddress.do"
	};

var popupOpt = {
		closeName:comMsg.comn_close,
		btnClass:"btn_style3"			
	};
var titleCodeArray = [];
<c:forEach var="titleCode" items="${titleCodeList}">
titleCodeArray.push({index:"${fn:escapeXml(titleCode.codeName)}",value:"${fn:escapeXml(titleCode.code)}"});						
</c:forEach>

var classCodeArray = [];
<c:forEach var="classCode" items="${classCodeList}">
classCodeArray.push({index:"${fn:escapeXml(classCode.codeName)}",value:"${fn:escapeXml(classCode.code)}"});						
</c:forEach>
</script>

</head>

<body>

<%@include file="/common/topmenu.jsp"%>
<script language="javascript">jQuery.makeProcessBodyMask();</script>
<div style="clear: both;"></div>
<input type="hidden" id="selectedOrgCode">
<input type="hidden" id="selectedOrgName">

<form id="mailForm" >
	<input type="hidden" name="wmode" value="popup">
	<input type="hidden" name="wtype" value="send">
	<input type="hidden" id="to" name="to">
</form>

<div id="o_mainBody">
	<div id="o_leftMenu">
		<div id="leftMenuRcontentWrapper">
		<div id="leftMenuRcontent">			
		<div>
			<div id="ml_btnFMain2">
				<div class="topBtn_bgArea">
					<div class="btn_leftTop"><ul></ul></div>
				</div>
			</div>
			<div id="orgTreeviewer"></div>
		</div>
		</div>
		</div>
		<%@include file="/common/sideMenu.jsp"%>	
	</div>

	<div id="o_contentBodyWapper" class="TM_contentBodyWapper">
		<div>
			<div class="TM_folderInfo">
				<img src="/design/common/image/blank.gif"" class="TM_barLeft">			
				<div class="TM_finfo_data">
					<span class="TM_work_title"></span>
					<span class="TM_work_title_sub"></span>
				</div>
				<div class="TM_finfo_search">
					<div class="TM_mainSearch">
					<div style="float: left">					
						<div id="searchTypeSelect"></div>
					</div>
					<div style="float: left">
						&nbsp;<input type="text" class="searchBox"  name="keyWord" id="keyWord"  onKeyPress="(keyEvent(event) == 13) ? searchOrg() : '';" /><a href="#" onclick="searchOrg()" class="TM_search_btn"><span><tctl:msg key="mail.search"/></span></a>
					</div>
					<div class="fclear"></div>	
					</div>
				</div>	
				<img src="/design/common/image/blank.gif"" class="TM_barRight">			
			</div>		
		</div>
		<div class="TM_topContentSplitWrapper"><div class="TM_topContentSplit"></div></div>
			
		<div class="TM_mainContent">	
		<div>		
			<div class="mail_body_tabmenu">
				<div class="mail_body_tab" id="menuBarTab"></div>
				<div id="pageNavi"  class="mail_body_navi">				
				</div>
			</div>		
			<div class="mail_body_menu">
				<div class="menu_main_unit" id="menuBarContent"></div>
			</div>
		</div>	
		
		<div id="o_contentBody" >
			<div id="o_contentMain">
				
			</div>
			<div id="o_contentSub"></div>
		</div>
		</div>
	</div>
</div>

<div id="orgViewDialog"  title="<tctl:msg key="addr.dialog.view.title" bundle="addr"/>" style="display: none">
	<div id="addAddress">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<tr>
			<td class="leftborder" rowspan="8" style="padding:10px 10px 10px 10px;background:#f7f7f7;width:123px" align="center">
				<div id="myPicture" style="border:1px solid #CDCDCD;background:#ffffff;width:120px;height:145px">
				</div>
			</td>	
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.001" bundle="addr"/></th>
			<td id="memberName"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.006" bundle="addr"/></th>
			<td id="memberEmail"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.010" bundle="addr"/></th>
			<td id="empno"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.009" bundle="addr"/></th>
			<td id="mobileNo"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.027" bundle="addr"/></th>
			<td id="officeTel"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.032" bundle="addr"/></th>
			<td id="departmentName"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.033" bundle="addr"/></th>
			<td id="titleName"></td>
		</tr>
		<tr>
			<th class="leftborder" style="width:100px"><tctl:msg key="addr.info.label.034" bundle="addr"/></th>
			<td id="className"></td>
		</tr>
	</table>    
	</div>
</div>

<div id="photoViewDialog"  title="<tctl:msg key="addr.dialog.header.tab1.title" bundle="addr"/>" style="display: none">
	<div id="simplePicture" style="border:1px solid #CDCDCD;background:#ffffff;width:120px;height:145px"></div>
</div>

<div id="extendSendMailDialog"  title="<tctl:msg key="org.popup.sendmail.title" bundle="addr"/>" style="display: none">
	<div>
	<form id="extendSendMailForm">	
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="150px"></col>
			<col></col>
			<col width="130px"></col>
			<tr>
				<th class="lbout"><tctl:msg key="org.info.label.005" bundle="addr"/></th>
				<td id="currentOrg" colspan="2" style="padding:5px 2px;font-weight:bold;"></td>
			</tr>
			<c:if test="${!empty titleCodeList}">		
			<tr>
				<th class="lbout"><tctl:msg key="org.info.label.002" bundle="addr"/></th>
				<td>
					<div style="float: left">		
				 	<input type="checkbox" id="titleInclude" name="titleInclude" value=""/><tctl:msg key="org.info.label.004" bundle="addr"/>&nbsp;
				 	</div>
					<div style="float: left">					
						<div id="titleCodeSelect"></div>					
					</div>				
					<div class="fclear"></div>	
				</td>
				<td style="text-align:center">
					<a href="#" onclick="sendMailToTitle()" class="btn_basic"><span><tctl:msg key="org.popup.btn.001" bundle="addr"/></span></a>				
				</td>
			</tr>
			</c:if>
			<c:if test="${!empty classCodeList}">
			<tr>
				<th class="lbout"><tctl:msg key="org.info.label.003" bundle="addr"/></th>
				<td>
					<div style="float: left">		
				 	<input type="checkbox" id="classInclude" name="classInclude" value=""/><tctl:msg key="org.info.label.004" bundle="addr"/>&nbsp;
				 	</div>
					<div style="float: left">					
						<div id="classCodeSelect"></div>					
					</div>				
					<div class="fclear"></div>			 	
				</td>
				<td style="text-align:center">
					<a href="#" onclick="sendMailToClass()" class="btn_basic"><span><tctl:msg key="org.popup.btn.001" bundle="addr"/></span></a>				
				</td>
			</tr>
			</c:if>
			<tr>
				<th class="lbout"><tctl:msg key="org.info.label.005" bundle="addr"/></th>
				<td>
					<input type="checkbox" id="deptInclude" name="deptInclude" value=""/><tctl:msg key="org.info.label.004" bundle="addr"/>
				</td>
				<td style="text-align:center">
					<a href="#" onclick="sendMailToDept()" class="btn_basic"><span><tctl:msg key="org.popup.btn.001" bundle="addr"/></span></a>				
				</td>
			</tr>
		</table>
	</form>
	</div>	
</div>
<div id="addAddrConfirm" title="<tctl:msg key="addr.dialog.title.004" bundle="addr"/>" style="display:none">
    <div style="text-align:center;font-weight:bold;height:50px;line-height:30px">
        <span id="dupQuestion"></span><br>
        <span id="dupEmailAddress" style="color:#3D83C1"></span> : <span id="dupEmail" style="color:#FF8000"></span> / <span id="dupName" style="color:#FF8000"></span>
    </div>
</div>
<div id="helpLayer" class="helpLayer" style="display:none; position:absolute"></div>
<%@include file="/common/bottom.jsp"%>
</body>
</html>