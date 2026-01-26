<script type="text/javascript">
	var dfolderList = [];
	var ufolderList = [];
		
	<c:forEach var="folder" items="${defaultFolders}" varStatus="loop">			
		<c:if test="${folder.fullName != 'Reserved'}">
		dfolderList.push({id:"menu_item_${folder.fullName}",name:"${folder.name}",fullname:"${folder.fullName}",depth:0,link:"javascript:folderAction('${folder.fullName}')"});
		</c:if>
		<c:if test="${folder.fullName eq 'Sent'}">
		dfolderList.push({id:"menu_item_mdn",name:"<tctl:msg key="mail.receivenoti"/>",fullname:"<tctl:msg key="mail.receivenoti"/>",link:"javascript:mdnfolderAction();"});
		</c:if>		
	</c:forEach>		
	<c:if test="${!empty userFolders}">	
	<c:forEach var="folder" items="${userFolders}" varStatus="loop">
		ufolderList.push({id:"uf_${loop.index}",name:"${folder.name}",fullname:"${folder.fullName}",depth:${folder.depth},link:"javascript:folderAction('${folder.fullName}')"});
	</c:forEach>
	</c:if>
</script>