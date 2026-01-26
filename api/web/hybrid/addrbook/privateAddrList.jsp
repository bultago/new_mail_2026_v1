<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/hybrid/common/header.jsp"%>
		
		<script type="text/javascript">			

			function moveto_page(page) {
				var url="/hybrid/addr/privateAddrList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:page});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				executeUrl(url, paramArray);
			}

			function workGroup(groupSeq) {
				var url;				
				if(subMenuWorkType == "move"){					
					url="/hybrid/addr/moveAddr.do";

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
					paramArray.push({name:"sourceGroupSeq", value:"${fn:escapeXml(groupSeq)}"});
					paramArray.push({name:"targetGroupSeq", value:groupSeq});
					paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
					paramArray.push({name:"mseqs", value:datas});
					paramArray.push({name:"addrType", value:"private"});
					executeUrl(url, paramArray);
					
				} else {
					url="/hybrid/addr/privateAddrList.do";
					var paramArray = [];
					paramArray.push({name:"groupSeq", value:groupSeq});				
					executeUrl(url, paramArray);
				}					
				
			}

			function changeAddr(mode) {
				var paramArray = [];
				var url = "";
				if (mode == 'pri') {
					url="/hybrid/addr/privateAddrList.do";
				} else if (mode == 'pub') {
					url="/hybrid/addr/publicAddrList.do";
				}
				executeUrl(url, paramArray);
			}

			function resizeAddrFrame() {
				var height = document.getElementById("addr_wrapper").offsetHeight;
				parent.resizeAddrFrame(height);
			}

			var subMenuWorkType;
			function viewGroupMenu(evt,type) {
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
				document.getElementById("menu_flist").style.height = "170px";
					
				menuType = "addrPGroup";
				document.getElementById("menu_flist").innerHTML = getFolderListEl(menuType);
				
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
				
				var url="/hybrid/addr/privateAddrList.do";
				var paramArray = [];
				paramArray.push({name:"keyWord", value:doubleUrlEncode(keyWord)});
				executeUrl(url, paramArray);
			}
			function init() {				
			}


			function deleteAddr(){
				var url="/hybrid/addr/deleteAddr.do";

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
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"mseqs", value:datas});
				paramArray.push({name:"addrType", value:"private"});
				executeUrl(url, paramArray);
			}

			function viewMember(memberSeq){
				var url="/hybrid/addr/readAddrView.do";
				var f = document.addrForm;
				
				var paramArray = [];
				paramArray.push({name:"page", value:f.page.value});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"mseq", value:memberSeq});
				paramArray.push({name:"addrType", value:"private"});

				executeUrl(url, paramArray);
				
			}

			function addAddrMember(){
				var url="/hybrid/addr/writeAddrView.do";
				var f = document.addrForm;

				var paramArray = [];
				paramArray.push({name:"addType", value:"add"});
				paramArray.push({name:"page", value:f.page.value});
				paramArray.push({name:"groupSeq", value:"${fn:escapeXml(groupSeq)}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${fn:escapeXml(keyWord)}")});
				paramArray.push({name:"addrType", value:"private"});
				executeUrl(url, paramArray);
			}

			function writeEmail(name,address){
				var url="/hybrid/mail/mailWrite.do";
				var paramArray = [];
				var addrStr = (name !="" )?"\""+name+"\"<"+address+">":address;
				paramArray.push({name:"to", value:doubleUrlEncode(addrStr)});
				executeUrl(url, paramArray);
			}

			var addrGroupList = [];			

			addrGroupList.push({id:"addr_group_item_0",name:"<tctl:msg key="addr.tree.all.label" bundle="addr"/>",fullname:"<tctl:msg key="addr.tree.all.label" bundle="addr"/>",depth:0,link:"javascript:workGroup('0')"});
			<c:forEach var="contactGroup" items="${contactGroupVos}" varStatus="loop">				
				addrGroupList.push({id:"addr_group_item_${contactGroup.groupSeq}",name:"${contactGroup.groupName}",fullname:"${contactGroup.groupName}",depth:0,link:"javascript:workGroup('${contactGroup.groupSeq}')"}); 
			</c:forEach>
			
 		</script>
	</head>
	<body onload="init()">
		<div id="addr_wrapper" class="wrapper">
		
			<%@include file="/hybrid/addrbook/addr_body_top.jsp"%>
			
			<div class="container">
				<form name="addrForm">
				<input type="hidden" name="page" value="${fn:escapeXml(page)}">
				<input type="hidden" name="groupSeq" value="${fn:escapeXml(groupSeq)}">
				<input type="hidden" name="keyWord" value="${fn:escapeXml(keyWord)}">
				
				<fieldset class="search_wrap" id="search_wrap" >
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
						<a href="javascript:viewGroupMenu('link')" class="btn_dr3" title="${contactInfoVo.groupInfo.groupName}">
						<c:if test="${!empty contactInfoVo.groupInfo}">
						<tctl:truncateString size="20">${contactInfoVo.groupInfo.groupName}</tctl:truncateString>
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
						<a href="javascript:deleteAddr();" class="btn3"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
						<a href="javascript:void(0);" onclick="viewGroupMenu(event,'move');" class="btn_dr3"><tctl:msg key="comn.move" bundle="common"/></a>
					</div>
					<div class="btn_r" style="top:5px;">
						<a href="javascript:addAddrMember();" class="btn3"><span><tctl:msg key="comn.add" bundle="common"/></span></a>						
					</div>
				</div>	
				<div class="mail_address">
					
					<div class="list">
						<ul class="addrList" style="margin:0px;">
							<c:if test="${empty contactInfoVo.memberList}">
								<li style="text-align: center"><span><tctl:msg key="addr.info.msg.032" bundle="addr"/></span></li>
							</c:if>
							<c:if test="${!empty contactInfoVo.memberList}">
							<c:forEach var="member" items="${contactInfoVo.memberList}" varStatus="loop">
							<li style="position:relative;">
							<span><input type="checkbox" name="memberSeq" value="${member.memberSeq}"/></span>
                               <a href="javascript:viewMember('${member.memberSeq}')" class="name">	                               
	                               ${fn:escapeXml(member.memberName)}
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
						<a href="javascript:deleteAddr();" class="btn3"><span><tctl:msg key="comn.del" bundle="common"/></span></a>
						<a href="javascript:void(0);" onclick="viewGroupMenu(event,'moveBottom');" class="btn_dr3"><tctl:msg key="comn.move" bundle="common"/></a>
					</div>
					<div class="btn_r" style="top:5px;">
						<a href="javascript:addAddrMember();" class="btn3"><span><tctl:msg key="comn.add" bundle="common"/></span></a>						
					</div>
				</div>	
					
				<%@include file="/hybrid/common/pageCounter.jsp"%>
					
				
			</form>
			</div>
			
			<%@include file="/hybrid/common/footer.jsp"%>
		</div>
	</body>
</html>