<c:if test="${!isHybrid}">
<div class="footer">
	<div class="footer_btn">
		<%if (!BrowserUtil.isMobileOpera(agent)) {%>
		<a href="javascript:chageMailMode('pc');" class="btn2" title="PC <tctl:msg key="comn.version" bundle="common"/>"><span>PC <tctl:msg key="comn.version" bundle="common"/></span></a>
		<%}%>
		<a href="javascript:excuteAction('home')" class="btn2" title="<tctl:msg key="comn.top.home" bundle="common"/>"><span><tctl:msg key="comn.top.home" bundle="common"/></span></a>
		<a href="javascript:excuteAction('logout');" class="btn2" title="<tctl:msg key="comn.logout" bundle="common"/>"><span><tctl:msg key="comn.logout" bundle="common"/></span></a>
	</div>
	<address>${copyright}</address>
</div>
</c:if>