<div id="logo">
	<table class="TB_topMenu">
		<tbody>
		<tr>
			<td>
				<!-- logo -->
				<div class="logo">
					<div class="logo_area">
						<a href="/common/welcome.do">
						<c:if test="${empty logoUrl}">
						<img src="/design/common/image/logo_tms.gif" height="33"/>
						</c:if>
						<c:if test="${!empty logoUrl}">
						<img src="${logoUrl}" height="33px"/>
						</c:if>
						</a>
					</div>
				</div>
			</td>
			<td class="menu">
				<!-- menu -->
				<div id="topMenu">
					<div id="topMenu_left">
						<ul>
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
					</div>
					<div id="topMenu_right">
						<div class="area">
							<a id="user_setting" href="/setting/viewUserInfoAuth.do" title="${mailUid}">${userName}</a>
							<span>|</span>
							<c:if test="${noteUse}">
							<span style="font-weight:bold;"><a href="/dynamic/note/noteCommon.do"><tctl:msg key="note.msg.000" bundle="common"/><span style="color:#ffffff;width:15px;display:inline-block;" id="unseen_note_count"></span></a></span>
							<span>|</span>
							</c:if>
							<a href="/common/logout.do?language=<%=locale %>"><tctl:msg key="comn.logout" bundle="common"/></a>
							<a class="btn_topMenu_help" href="javascript:viewHelp()"></a>
						</div>		
					</div>
				</div>
			</td>
		</tr>
		</tbody>
	</table>	
</div>