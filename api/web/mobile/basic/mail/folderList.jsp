<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=cssLocation%>/css/mail_list_style.css" />

		<script type="text/javascript">
			function moveMailList(folderName) {			
				var url="/mobile/mail/mailList.do";
				var paramArray = [];
				paramArray.push({name:"folderName", value:doubleUrlEncode(folderName)});

				executeUrl(url, paramArray);
			}

			function moveFlagList(type){
				var flag;
				if(type == "unseen"){
					flag = "U";
				} else if(type == "flag"){
					flag = "F";
				}
				
				var url = "/mobile/mail/mailList.do";
				var paramArray = [];
				paramArray.push({name:"page", value:"1"});
				paramArray.push({name:"folderName",value:"all"});
				paramArray.push({name:"flag", value:flag});								
				executeUrl(url, paramArray);
			}

			function emptyFolder(folderName){

				var msg;

				if(folderName == "Trash"){
					msg = '<tctl:msg key="confirm.trashdelete"/>';
				} else if(folderName == "Spam"){
					msg = '<tctl:msg key="confirm.emptyspam"/>';
				} else {
					msg = '<tctl:msg key="confirm.delete"/>';
				}

				if(!confirm(msg)){
					return;
				}
				
				var url = "/mobile/mail/mailWork.do";
				var paramArray = [];				
				paramArray.push({name:"workMode", value:"empty"});			
				paramArray.push({name:"folderNames",value:doubleUrlEncode(folderName)});												
				executeUrl(url, paramArray);
			}

			function makeTopLink(){
				var topLink = document.getElementById("mailTopLink");
				topLink.innerHTML = "<a href=\"javascript:viewSelectMenu(this, 'folder');\" class='btn_dr'>"
					+"<tctl:msg key="mail.folderlist"/></a>";				

			}		
		</script>
	</head>
	<body>
		<div class="wrapper">	
			<%@include file="mail_top.jsp"%>
			<%@include file="/mobile/basic/mail/mail_body_top.jsp"%>		
			<script type="text/javascript">makeTopLink();</script>
			
			<div class="container">
				<div class="title_box">
					<div class="btn_l"><a href="javascript:history.back();" class="btn2" title="<tctl:msg key="mail.goback"/>"><span><tctl:msg key="mail.goback"/></span></a></div>
					<div class="btn_r"><a href="/mobile/mail/mailWrite.do" class="btn2" title="<tctl:msg key="mail.write"/>"><span><tctl:msg key="mail.write"/></span></a></div>
				</div>
				<div id="document_body">
					<ul class="normal_type mail_home">
						<c:forEach var="folder" items="${defaultFolders}" varStatus="loop">
						<c:if test="${folder.fullName ne 'Reserved'}">
						<li>
							<c:if test="${folder.fullName != 'Reserved'}">
							<a href="javascript:moveMailList('${folder.fullName}')" title="${folder.name}" class="${folder.fullName}">
								${folder.name}
								<c:if test="${folder.unseenCnt > 0}">
								<strong class="mail_num">${folder.unseenCnt}</strong>
								</c:if>																
							</a>
								
							</c:if>
							<c:choose>
								<c:when test="${folder.fullName eq 'Trash' || folder.fullName eq 'Spam'}">
									<button onclick="emptyFolder('${folder.fullName}')" class="bt1"><span><tctl:msg key="menu.empty"/></span></button>
								</c:when>								
							</c:choose>							
						</li>
						<c:if test="${folder.fullName eq 'Sent'}">
						<li>
							<a href="/mobile/mail/mailMdnList.do" class="Mdn" title="<tctl:msg key="mail.receivenoti"/>"><tctl:msg key="mail.receivenoti"/></a>
						</li>
						</c:if>
						</c:if>
						</c:forEach>						
						<li>
							<a href="#n" class="UserIndex"><tctl:msg key="mail.userbox"/></a>
						</li>						
						<c:forEach var="folder" items="${userFolders}" varStatus="loop">
							<li class="depth${folder.depth}">
								<a href="javascript:moveMailList('${folder.fullName}');" class="UserFolder">
									${fn:escapeXml(folder.name)}
									<c:if test="${folder.unseenCnt > 0}">
									<strong class="mail_num">${folder.unseenCnt}</strong>
									</c:if>
								</a>
								<button onclick="emptyFolder('${folder.fullName}')" class="bt1"><span><tctl:msg key="menu.empty"/></span></button>
							</li>
						</c:forEach>							
						<li><a id="#link_4" class="Unseen" href="javascript:moveFlagList('unseen');" title="<tctl:msg key="menu.quick.unread"/>"><tctl:msg key="menu.quick.unread"/></a></li>
						<li><a id="#link_5" class="Flag" href="javascript:moveFlagList('flag');" title="<tctl:msg key="menu.quick.flag"/>"><tctl:msg key="menu.quick.flag"/></a></li>						
					</ul>
				</div>
			</div>
			
			<%@include file="/mobile/basic/mail/mail_body_footer.jsp"%>
			
			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>