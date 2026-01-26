<script type="text/javascript">
	var bbsMenu = [];
	<c:forEach var="bbs" items="${bbsInfoList}" varStatus="loop">
		bbsMenu.push({id:"bbs_${loop.index}",name:"${bbs.bbsName}",depth:${bbs.bbsDepth},link:"/mobile/bbs/bbsContentList.do?bbsId=${bbs.bbsId}"});
	</c:forEach>
</script>