<script type="text/javascript">
	var bbsMenu = [];
	<c:forEach var="bbs" items="${bbsInfoList}" varStatus="loop">
		bbsMenu.push({id:"bbs_${loop.index}",name:"${bbs.bbsName}",depth:${bbs.bbsDepth},link:"/mobile/bbs/bbsContentList.do?bbsId=${bbs.bbsId}"});
	</c:forEach>
</script>
<div class="blind">
	<a href="#document_body" id="directLink" title="<tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/>"><tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/></a>				
</div>
			
<div class="header">
	<h1>Terrace Mail Suite</h1>
	<c:if test="${!isHybrid}">
	<div class="ts">
		<div class="btn_l"><a href="javascript:excuteAction('bbs')"><span class="ic_tit_board"><tctl:msg key="bbs.title" bundle="bbs"/></span></a></div>
		<div class="btn_r"><a href="/mobile/common/home.do" class="btn0"><span><tctl:msg key="comn.top.mobilehome" bundle="common"/></span></a></div>
	</div>
	</c:if>
</div>
<div id="select_menus" class="selectMenuWrap">
	<div id="menu_list" class="menuList" style="top:28px;">
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