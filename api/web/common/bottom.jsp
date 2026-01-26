<div id="copyRight" style="display:block;">
	<div>${copyright}</div>
</div>

</td></tr>
</table>

<div id="modalDialog" style="width:100%; display:none">
	<div style="text-align: center">
		<span id="msgContent"></span>
	</div>
</div>
<div id="historyStack"></div>
<%@include file="/common/xecureOcx.jsp" %>
<%if("enable".equalsIgnoreCase(debugUse) || isPDebug){%>
<div id="debugInfo" style="display:<%if(!"enable".equalsIgnoreCase(debugUse)){%>none<%}%>;position: absolute; bottom:0; left:0; background:#efefef;padding-top:5px;border:#E8363D solid 1px"></div>
<%}%>


