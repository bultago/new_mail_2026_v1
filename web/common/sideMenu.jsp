<%if(lmenuUse.equals("enable")) {%>

<div id="leftSideMenuContent" style="display:none;">
	<div style="height:182px; overflow: auto">
	<c:forEach var="menu" items="${menus}" varStatus="loop">
	<div class="TM_sideMenuLine" onclick="topLink('${menu.menuId}','${menu.menuUrl}','${menu.menuTarget}')" title="${menu.menuName}">
		<span class="smenuWrapper">${menu.menuName}</span>
	</div>
	</c:forEach>
	</div>	
</div>

<div>
<div id="leftSideMenu" viewmenu="hide" class="TM_sideMenuLineWrapper">
	<div id="leftSideMenuBtn" class="TM_sideMenuBtn" onclick="toogleSideMenu(event);">
		<tctl:msg key="comn.sidemenu" bundle="common"/>
		<c:if test="${noteUse}">
		<a href="/dynamic/note/noteCommon.do" class="sideNote"><font id="unseen_note_count_left"></font></a>
		</c:if>
		<a href="javascript:viewHelp();" class="sideHelp" id="sideHelpBtn"></a>
	</div>
	<div id="leftSideMenuBox"></div>
</div>

</div>

<%}%>