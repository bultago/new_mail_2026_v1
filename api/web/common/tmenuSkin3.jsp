<div class="topMenu">
	<div class="topMenuLogo"><p class="logo">
		<a href="/common/welcome.do">
		<c:if test="${empty logoUrl}">
		<img src="/design/common/image/logo_tms.gif" height="33"/>
		</c:if>
		<c:if test="${!empty logoUrl}">
		<img src="${logoUrl}" height="33px"/>
		</c:if>
		</a>
	</p></div>
	<div class="menu_warp">
		<ul class="admin">
			<li><a id="user_setting" href="/setting/viewUserInfoAuth.do" title="${mailUid}">${userName}</a></li>
			<c:if test="${noteUse}">
			<li style="width:70px;"><span style="font-weight:bold;"><a href="/dynamic/note/noteCommon.do"><tctl:msg key="note.msg.000" bundle="common"/><font id="unseen_note_count"></font></a></span></li>
			</c:if>
			<li><a href="/common/logout.do?language=<%=locale %>" class="btn_style6"><span><tctl:msg key="comn.logout" bundle="common"/></span></a></li>
		</ul>
		<ul class="menu">
			<c:forEach var="menu" items="${menus}" varStatus="loop">
				<c:if test="${menu.menuId ne 'setting'}">
				<li id="mtop_menu_${menu.menuId}"><a href="#" onclick="topLink('${menu.menuId}','${menu.menuUrl}','${menu.menuTarget}')">${menu.menuName}</a></li>
				</c:if>
			</c:forEach>
			<c:forEach var="menu" items="${menus}" varStatus="loop">
				<c:if test="${menu.menuId eq 'setting'}">
				<li id="mtop_menu_${menu.menuId}"><a href="#" onclick="topLink('${menu.menuId}','${menu.menuUrl}','${menu.menuTarget}')">${menu.menuName}</a></li>
				</c:if>
			</c:forEach>
		</ul>
		<p class="help">
			<a class="btn_topMenu_help" href="javascript:viewHelp()"></a>
		</p>
	</div>
</div>