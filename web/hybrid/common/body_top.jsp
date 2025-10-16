<script type="text/javascript">
	window.onload = function(){		
		document.getElementById("directLink").focus();
		var location = document.location.href;
		if(location.indexOf("#") > -1){
			location = location.substr(0,location.indexOf("#"));
		}
		document.location = location+"#";
	};
</script>

<div class="blind">
	<a href="#document_body" id="directLink" title="<tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/>"><tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/></a>				
</div>
			
<div class="header">
	<h1>
		<a href="javascript:excuteAction('home');" id="home_btn">
			<c:if test="${empty logoUrl}">
				<img src="/design/mobile/image/logo.gif" height="33px" alt="<tctl:msg key="conf.profile.70" bundle="setting"/>"/>
			</c:if>
			<c:if test="${!empty logoUrl}">
			<img src="${logoUrl}" height="33px" alt="<tctl:msg key="conf.profile.70" bundle="setting"/>"/>
			</c:if>
		</a>
	</h1>
</div>
<div id="select_menus" class="selectMenuWrap">
	<div id="menu_list" class="menuList">
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