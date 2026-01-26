<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt"  uri="/WEB-INF/tld/fmt.tld"%>

<c:set var="mode" value="${param.mode}"/>
<c:if test="${empty mode}">
<c:set var="mode" value="copy"/>
</c:if>

<script type="text/javascript">

function setTargetFolder(type,path,nodeNum,userSeq){
	var actionType = '${fn:escapeXml(mode)}';
	window.fraContent.copyAndMove(actionType,type,path,nodeNum,userSeq);
	jQuery("#copyandmove").jQpopup("close");
}

</script>

<div class="webfolder_subContent">
	<iframe frameborder="0" width="100%" height="100%" align="top" src="/webfolder/folderTree.do?viewType=select" name='select_deeptree_user'></iframe>
</div>