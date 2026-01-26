<%if(lmenuUse.equals("enable")) {%>

<div>
<div id="leftSideMenu" viewmenu="hide" class="TM_sideMenuLineWrapper" style="position:relative;">
	<div id="leftSideMenuBtn" class="TM_sideMenuBtn TM_sideMenuDown">
		<tctl:msg key="comn.sidemenu" bundle="common"/>
		<a href="javascript:viewHelp();" class="sideHelp" id="sideHelpBtn"></a>
	</div>
	<div id="leftSideMenuBox">
		<div id="leftSideMenuContent">
			<div style="height:182px; overflow: auto">
			<c:forEach var="menu" items="${menus}" varStatus="loop">
			<div class="TM_sideMenuLine" onclick="topLink('${menu.menuId}','${menu.menuUrl}','${menu.menuTarget}')" title="${menu.menuName}">
				<span class="smenuWrapper">${menu.menuName}</span>
			</div>
			</c:forEach>
			</div>	
		</div>
	</div>	
</div>
</div>

<%}%>