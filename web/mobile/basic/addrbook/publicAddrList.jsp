<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		
		<script type="text/javascript">			

			function moveto_page(page) {
				var url="/mobile/addr/publicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:page});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				executeUrl(url, paramArray);
			}

			function changeBookAddress(bookSeq) {
				var url="/mobile/addr/publicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"bookSeq", value:bookSeq});
				executeUrl(url, paramArray);
			}

			function workGroup(groupSeq) {
				var url;				
				if(subMenuWorkType == "move"){					
					url="/mobile/addr/moveAddr.do";

					var f = document.addrForm;
					var obj = f.memberSeq;
					var address = "";

					if (checkedCnt(obj) == 0) {
						alert('<tctl:msg key="addr.info.msg.013" bundle="addr"/>');
						return;
					}
					
					var datas = makeMultiParam(f.memberSeq);					

					var paramArray = [];
					paramArray.push({name:"page", value:f.page.value});
					paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
					paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
					paramArray.push({name:"sourceGroupSeq", value:"${fn:escapeXml(groupSeq)}"});
					paramArray.push({name:"targetGroupSeq", value:groupSeq});
					paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
					paramArray.push({name:"mseqs", value:datas});
					paramArray.push({name:"addrType", value:"public"});
					executeUrl(url, paramArray);
					
				} else {
					url="/mobile/addr/publicAddrList.do";
					var paramArray = [];
					paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
					paramArray.push({name:"groupSeq", value:groupSeq});
					executeUrl(url, paramArray);
				}					
				
			}
			
			function changeAddr(mode) {
				var paramArray = [];
				var url = "";
				if (mode == 'pri') {
					url="/mobile/addr/privateAddrList.do";
				} else if (mode == 'pub') {
					url="/mobile/addr/publicAddrList.do";
				}
				executeUrl(url, paramArray);
			}

			var subMenuWorkType;		
			function viewGroupMenu(evt,type) {
				if(menuType == "addrGBook"){
					closeMenuBox('');
				}
				
				var menu = document.getElementById("select_menus");
				var box = document.getElementById("menu_list");

				if(type == "move"){					
					menu.style.marginLeft = 0+"px";					
					box.style.top = 34+"px";
					box.style.left = 0+"px";
					subMenuWorkType = "move";
				} else if(type == "moveBottom"){
					var posY;
					if (isMsie) {			
						posY = evt.srcElement.parentNode.parentNode.offsetTop;
						posY = posY + 10;
					} else {
						posY = evt.pageY;						
					}										
					menu.style.marginLeft = 0+"px";					
					box.style.top = (posY-350)+"px";
					box.style.left = 0+"px";
					subMenuWorkType = "move"						
				} else {
					menu.style.marginLeft = "auto";
					box.style.top = 0+"px";
					box.style.left = 0+"px";
					subMenuWorkType = "link";
				}

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
				menu.style.marginRight = "auto";
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
				
				var url="/mobile/addr/publicAddrList.do";
				var paramArray = [];
				paramArray.push({name:"keyWord", value:doubleUrlEncode(keyWord)});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				executeUrl(url, paramArray);
			}
			function init() {
				
			}

			function viewMember(memberSeq){
				var url="/mobile/addr/readAddrView.do";
				var f = document.addrForm;
				
				var paramArray = [];
				paramArray.push({name:"page", value:f.page.value});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"mseq", value:memberSeq});				
				paramArray.push({name:"addrType", value:"public"});

				executeUrl(url, paramArray);
				
			}

			function addAddrMember(){
				var url="/mobile/addr/writeAddrView.do";
				var f = document.addrForm;

				var paramArray = [];
				paramArray.push({name:"addType", value:"add"});
				paramArray.push({name:"page", value:f.page.value});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"addrType", value:"public"});
				executeUrl(url, paramArray);
			}

			function deleteAddr(){
				var url="/mobile/addr/deleteAddr.do";

				var f = document.addrForm;
				var obj = f.memberSeq;
				var address = "";

				if (checkedCnt(obj) == 0) {
					alert('<tctl:msg key="addr.info.msg.013" bundle="addr"/>');
					return;
				}
			
				var datas = makeMultiParam(f.memberSeq);
				

				var paramArray = [];
				paramArray.push({name:"page", value:f.page.value});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"bookSeq", value:"${fn:escapeXml(bookSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"mseqs", value:datas});
				paramArray.push({name:"addrType", value:"public"});
				executeUrl(url, paramArray);
			}

			function writeEmail(name,address){
				var url="/mobile/mail/mailWrite.do";
				var paramArray = [];
				var addrStr = (name !="" )?"\""+name+"\"<"+address+">":address;
				paramArray.push({name:"to", value:doubleUrlEncode(addrStr)});
				executeUrl(url, paramArray);
			}

			var addrGGroupList = [];
			var addrGBookList = [];
			addrGGroupList.push({id:"addr_group_item_0",name:"<tctl:msg key="addr.tree.all.label" bundle="addr"/>",fullname:"<tctl:msg key="addr.tree.all.label" bundle="addr"/>",depth:0,link:"javascript:workGroup('0')"});
			<c:forEach var="contactGroup" items="${contactGroupVos}" varStatus="loop">			
			addrGGroupList.push({id:"addr_p_group_item_${contactGroup.groupSeq}",name:"${contactGroup.groupName}",fullname:"${contactGroup.groupName}",depth:0,link:"javascript:workGroup('${contactGroup.groupSeq}')"});
			</c:forEach>
			<c:forEach var="contactBook" items="${contactBookVos}" varStatus="loop">
			addrGBookList.push({id:"addr_p_group_item_${contactBook.adrbookSeq}",name:"${contactBook.bookName}",fullname:"${contactBook.bookName}",depth:0,link:"javascript:changeBookAddress('${contactBook.adrbookSeq}')"});
			</c:forEach>		
 		</script>
	</head>
	<body onload="init()">
		<div id="addr_wrapper" class="wrapper">
		
			<%@include file="/mobile/basic/addrbook/addr_body_top.jsp"%>
		
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
					<div style="text-align:center; padding-top:10px;">
					
					
						<a href="javascript:viewAddrMenu()" class="btn_dr3" style="margin-right:10px;">
						<c:if test="${!empty contactInfoVo.bookInfo}">
							<tctl:truncateString size="7">${contactInfoVo.bookInfo.bookName}</tctl:truncateString>
						</c:if>
						<c:if test="${empty contactInfoVo.bookInfo}">						
							<tctl:msg key="addr.btn.select.addrbook" bundle="addr"/>
						</c:if>						
						</a>
						<a href="javascript:viewGroupMenu()" class="btn_dr3">
							<c:if test="${!empty contactInfoVo.groupInfo}">
								<tctl:truncateString size="7">${contactInfoVo.groupInfo.groupName}</tctl:truncateString>
							</c:if>
							<c:if test="${empty contactInfoVo.groupInfo}">
								<tctl:msg key="addr.tree.all.label" bundle="addr"/>
							</c:if>												
						</a>						
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
				<div class="list_box list_box2">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.addrForm.memberSeq)" id="all_select1" check="off" class="btn3"><span><tctl:msg key="mail.select.all"/></span></a>
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:deleteAddr();" class="btn3"><span><tctl:msg key="comn.del" bundle="common"/></span></a>						
						<a href="javascript:void(0);" onclick="viewGroupMenu(event,'move')" class="btn_dr3"><tctl:msg key="comn.move" bundle="common"/></a>
						</c:if>
					</div>
					<div class="btn_r" style="top:5px;">
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:addAddrMember();" class="btn3"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
						</c:if>	
					</div>
				</div>	
				<div class="mail_address">					
					<div class="list">
						<ul class="addrList" style="margin-top:0px">
							<c:if test="${empty contactInfoVo.memberList}">
								<li style="text-align: center"><span><tctl:msg key="addr.info.msg.032" bundle="addr"/></span></li>
							</c:if>
							<c:if test="${!empty contactInfoVo.memberList}">
							<c:forEach var="member" items="${contactInfoVo.memberList}" varStatus="loop">							
							<li style="position:relative;">
								<span><input type="checkbox" name="memberSeq" value="${member.memberSeq}"/></span>
                               <a href="javascript:viewMember('${member.memberSeq}')" class="name">
	                               ${member.memberName}
	                               ${fn:escapeXml(member.memberEmail)}
                               </a>
                               
                               <div style="position: absolute;right:5px;top:7px;"><a href="javascript:writeEmail('${fn:escapeXml(member.memberName)}','${fn:escapeXml(member.memberEmail)}')" class="btn_mail" alt="mail">send mail</a></div>
                            </li>
							</c:forEach>
							</c:if>
						</ul>
					</div>
				</div>
				<div class="list_box list_box2">
					<div class="btn_l">
						<a href="javascript:toggleAllSelect(document.addrForm.memberSeq)" id="all_select1" check="off" class="btn3"><span><tctl:msg key="mail.select.all"/></span></a>
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:deleteAddr();" class="btn3"><span><tctl:msg key="comn.del" bundle="common"/></span></a>						
						<a href="javascript:void(0);" onclick="viewGroupMenu(event,'moveBottom')" class="btn_dr3"><tctl:msg key="comn.move" bundle="common"/></a>
						</c:if>
					</div>
					<div class="btn_r" style="top:5px;">
						<c:if test="${authority.writeAuth eq 'Y'}">
						<a href="javascript:addAddrMember();" class="btn3"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
						</c:if>	
					</div>
				</div>
				<%@include file="/mobile/basic/common/pageCounter.jsp"%>
					
				
			</form>
			</div>
			
			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>