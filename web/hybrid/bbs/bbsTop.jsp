<script type="text/javascript">
	function goBBSList(bbsType){
		
		if(pageCategory == "writeBBS"){
			var titleObj = jQuery("#subject");
			var contentObj = jQuery("#contentText");
			if(titleObj.val() != "" || contentObj.val() != "" ){
				if(!confirm(mailMsg.confirm_BBSEscapewrite)){
					return;
				}
			}
		}
		
		document.location.href = "/hybrid/bbs/bbsContentList.do?bbsId="+bbsType;
	}
	
	var bbsMenu = [];
	<c:forEach var="bbs" items="${bbsInfoList}" varStatus="loop">
		//bbsMenu.push({id:"bbs_${loop.index}",name:"${bbs.bbsName}",depth:${bbs.bbsDepth},link:"/hybrid/bbs/bbsContentList.do?bbsId=${bbs.bbsId}"});
		bbsMenu.push({id:"bbs_${loop.index}",name:"${bbs.bbsName}",depth:${bbs.bbsDepth},link:"javascript:goBBSList('${bbs.bbsId}');"});
	</c:forEach>
</script>