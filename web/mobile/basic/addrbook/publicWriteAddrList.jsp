<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">			

			function moveto_page(page) {
				var url="/mobile/addr/writePublicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:page});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				executeUrl(url, paramArray);
			}

			function changeBookAddress(bookSeq) {
				var url="/mobile/addr/writePublicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"bookSeq", value:bookSeq});
				executeUrl(url, paramArray);
			}
			
			function changeGroup(groupSeq) {
				var url="/mobile/addr/writePublicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"groupSeq", value:groupSeq});
				executeUrl(url, paramArray);
			}

			function changeAddr(mode) {
				var paramArray = [];
				var url = "";
				if (mode == 'pri') {
					url="/mobile/addr/writePrivateAddrList.do";
				} else if (mode == 'pub') {
					url="/mobile/addr/writePublicAddrList.do";
				}
				executeUrl(url, paramArray);
			}

			function resizeAddrFrame() {
				var height = jQuery("#addr_wrapper").height();
				parent.resizeAddrIFrame(height);
			}

			function makeRcptAddress(type) {
				var f = document.addrForm;
				var obj = f.email;
				var address = "";

				if (checkedCnt(obj) == 0) {
					alert('<tctl:msg key="addr.info.msg.013" bundle="addr"/>');
					return;
				}

				var data;
				if (obj) {
					if(obj.length) {
						for (var i=0; i<obj.length; i++) {
							if (obj[i].checked) {
								data = obj[i].value.split('|');
								address += '"'+data[0]+'" <'+data[1]+'>,';
							}
						}
					} else {
						if (obj.checked) {
							data = obj.value.split('|');
							address += '"'+data[0]+'" <'+data[1]+'>';
						}
					}
				}
				parent.insertEmailAddress(type, address);
			}

			function viewGroupMenu() {
				if(menuType == "addrGBook"){
					closeMenuBox('');
				}
				
				var menu = document.getElementById("select_menus");
				var box = document.getElementById("menu_list");				
				menu.style.marginRight = 0+"px";
				box.style.top = 0+"px";	
				menuType = "addrGGroup";
				document.getElementById("menu_flist").innerHTML = getFolderListEl(menuType);
				document.getElementById("menu_flist").style.height = "170px";
				
				if (menu.style.display != 'block') {
					menu.style.display = 'block';
				} else {
					menu.style.display = 'none';
					closeMenuBox('');
				}
			}

			function viewAddrMenu() {
				if(menuType == "addrGGroup"){
					closeMenuBox('');
				}
				
				var menu = document.getElementById("select_menus");
				var box = document.getElementById("menu_list");				
				menu.style.marginRight = 90+"px";
				box.style.top = 0+"px";	
				menuType = "addrGBook";
				document.getElementById("menu_flist").innerHTML = getFolderListEl(menuType);
				document.getElementById("menu_flist").style.height = "170px";
				
				if (menu.style.display != 'block') {
					menu.style.display = 'block';
				} else {
					menu.style.display = 'none';
					closeMenuBox('');
				}
			}

			function searchAddress() {
				var f = document.addrForm;
				var keyWord = trim(f.inputKeyWord.value);
				if (keyWord == "") {
					alert('<tctl:msg key="addr.info.msg.022" bundle="addr"/>');
					return;
				}

				if(!checkInputSearch(f.inputKeyWord,2,255,true)){
					return;
				}
				
				var url="/mobile/addr/writePublicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"keyWord", value:doubleUrlEncode(keyWord)});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				executeUrl(url, paramArray);
			}
			function init() {
				parent.isLoadComplateAddr(true);
				parent.resizeInterval();
			}

			var addrGGroupList = [];
			var addrGBookList = [];
			<c:forEach var="contactGroup" items="${contactGroupVos}" varStatus="loop">			
			addrGGroupList.push({id:"addr_p_group_item_${contactGroup.groupSeq}",name:"${contactGroup.groupName}",fullname:"${contactGroup.groupName}",depth:0,link:"javascript:changeGroup('${contactGroup.groupSeq}')"});
			</c:forEach>
			<c:forEach var="contactBook" items="${contactBookVos}" varStatus="loop">
			addrGBookList.push({id:"addr_p_group_item_${contactBook.adrbookSeq}",name:"${contactBook.bookName}",fullname:"${contactBook.bookName}",depth:0,link:"javascript:changeBookAddress('${contactBook.adrbookSeq}')"});
			</c:forEach>		
 		</script>
	</head>
	<body onload="init()">
		<div id="addr_wrapper" class="wrapper">
			<div class="container">
				<form name="addrForm">
				<input type="hidden" name="page" value="${fn:escapeXml(page)}">
				<input type="hidden" name="groupSeq" value="${fn:escapeXml(groupSeq)}">
				<input type="hidden" name="bookSeq" value="${fn:escapeXml(bookSeq)}">
				<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}">
				
				<fieldset class="search_wrap">
					<div class="search_area">
						<div class="search_area_content">
							<input type="text" name="inputKeyWord" class="ip_search" value="${fn:escapeXml(keyWord)}"/>
								<input type="text" name="_tmp" style="display:none;width:0px;height:0px;"/>
						</div>
						<input type="button" class="btn_search" onclick="searchAddress()" value="<tctl:msg key="mail.search"/>" />
					</div>
				</fieldset>
				
				<div class="title_box">
				<c:if test="${!empty contactInfoVo.bookInfo}">
					<div style="width:100%">
						<p class="addr_public_title">
						<span class="title_name">
							<tctl:truncateString size="7">${contactInfoVo.bookInfo.bookName}</tctl:truncateString>
							<c:if test="${!empty contactInfoVo.groupInfo}">
								&gt; <tctl:truncateString size="7">${contactInfoVo.groupInfo.groupName}</tctl:truncateString>
							</c:if>
						</span>
						</p>
					</div>
				</c:if>
					<div class="btn_r">
						<a href="javascript:viewAddrMenu()" class="btn_dr3" style="margin-right:10px;"><tctl:msg key="addr.btn.select.addrbook" bundle="addr"/></a>
						<a href="javascript:viewGroupMenu()" class="btn_dr3"><tctl:msg key="addr.btn.select.group" bundle="addr"/></a>
					</div>
				</div>
				<div id="select_menus" class="selectMenuWrap">
					<div id="menu_list" class="menuList">
						<div class="menuWrap">
							<dl id="menus" class="menus">
								<dd><ul id="menu_flist"></ul></dd>
							</dl>
							<dl><dd>
								<ul class="menuArea">
									<li style="position:relative;" id="subMenuBox"></li>
								</ul>
							</dd></dl>
						</div>
					</div>
				</div>	
				<div class="list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.addrForm.email)" id="all_select1" check="off" class="btn6"><span><tctl:msg key="mail.select.all"/></span></a>
						<a href="javascript:parent.toggleAddress()" class="btn6"><span><tctl:msg key="comn.close" bundle="common"/></span></a>
					</div>
					<div class="btn_r" style="top:5px;">
						<a href="javascript:makeRcptAddress('to')" class="btn6"><span><tctl:msg key="mail.to"/></span></a>
						<a href="javascript:makeRcptAddress('cc')" class="btn6"><span><tctl:msg key="mail.cc"/></span></a>
						<a href="javascript:makeRcptAddress('bcc')" class="btn6"><span><tctl:msg key="mail.bcc"/></span></a>
					</div>
				</div>	
				<div class="mail_address">
					<div class="menu">
						<ul>
							<li><a href="javascript:changeAddr('pri')"><tctl:msg key="addr.tree.tab1.title" bundle="addr"/></a></li>
							<li><a href="javascript:changeAddr('pub')" class="on"><tctl:msg key="addr.tree.tab2.title" bundle="addr"/> (${contactInfoVo.totalCount})</a></li>
						</ul>
					</div>
					<div class="list">
						<ul class="addrList">
							<c:if test="${empty contactInfoVo.memberList}">
								<li style="text-align: center"><span><tctl:msg key="addr.info.msg.032" bundle="addr"/></span></li>
							</c:if>
							<c:if test="${!empty contactInfoVo.memberList}">
							<c:forEach var="member" items="${contactInfoVo.memberList}" varStatus="loop">
							<li>
                               <a href="#" class="wname">
	                               <input type="checkbox" name="email" value="${member.memberName}|${fn:escapeXml(member.memberEmail)}"/>
	                               ${member.memberName}
	                               ${fn:escapeXml(member.memberEmail)}
                               </a>
                            </li>
							</c:forEach>
							</c:if>
						</ul>
					</div>
				</div>
					
				<%@include file="/mobile/basic/common/pageCounter.jsp"%>
					
				<div class="list_box second_list_box">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.addrForm.email)" id="all_select2" check="off" class="btn3"><span><tctl:msg key="mail.select.all"/></span></a>
					</div>			
				</div>
			</form>
			</div>
		</div>
	</body>
</html>