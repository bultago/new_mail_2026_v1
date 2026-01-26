<script type="text/javascript">
	
	function toggleAddrType(){
		var addrtypeBox = document.getElementById("addrType_box");
		if (addrtypeBox.style.display == "block") {
			addrtypeBox.style.display = "none";
			jQuery("#search_wrap").css("visibility","");
			
		} else {
			addrtypeBox.style.display = "block";
			jQuery("#search_wrap").css("visibility","hidden");
		}
	}
	function goAddrList(type){
		
		if(pageCategory == "addAddrForm"){
			var name = jQuery("#mName");
			var email = jQuery("#mEmail");
			if(name.val() != "" || email.val() != "" ){
				if(!confirm(mailMsg.confirm_addressEscapewrite)){
					return;
				}
			}
		}
		
		if(type == "private"){
			document.location.href = "/hybrid/addr/privateAddrList.do";
		}else{
			document.location.href = "/hybrid/addr/publicAddrList.do";
		}
	}
</script>

<div class="blind">
	<a href="#document_body" id="directLink" title="<tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/>"><tctl:msg key="search.body"/> <tctl:msg key="comn.direct" bundle="common"/></a>				
</div>


<div class="header">
	<h1>Terrace Mail Suite</h1>
	<div class="ts">
		<div class="btn_l"><a href="javascript:excuteAction('addr')"><span class="ic_tit_address"><tctl:msg key="comn.top.addr" bundle="common"/></span></a></div>
		<div class="btn_r"><a href="javascript:excuteAction('home')" class="btn0"><span><tctl:msg key="comn.top.mobilehome" bundle="common"/></span></a></div>
	</div>
	<div class="hh"><h2 id="mailTopLink">
	<a href="javascript:toggleAddrType();" class="btn_dr">
		<c:if test="${addrType eq 'private'}">
		<tctl:msg key="addr.tree.tab1.title" bundle="addr"/>
		</c:if>
		<c:if test="${addrType eq 'public'}">
		<tctl:msg key="addr.tree.tab2.title" bundle="addr"/>
		</c:if>
	</a>
	</h2></div>
</div>
<div id="addrType_box" class="selectMenuWrap">
	<div class="menuList" style="top:-7px;">
		<div class="menuWrap">
			<dl class="menus">
				<dd><ul style="height:80px;">
					<li><label><a href="javascript:goAddrList('private');"><tctl:msg key="addr.tree.tab1.title" bundle="addr"/></a></label></li>
					<li><label><a href="javascript:goAddrList('public');"><tctl:msg key="addr.tree.tab2.title" bundle="addr"/></a></label></li>
				</ul></dd>
			</dl>
			<dl><dd>
				<ul class="menuArea">
					<li style="position:relative;">
						<div style="display: block;" class="blankBox" id="blank_box">&nbsp;</div>
						<a class="btn_close x" onclick="toggleAddrType()" href="javascript:;">X</a>
					</li>
				</ul>
			</dd></dl>			
		</div>
	</div>
</div>