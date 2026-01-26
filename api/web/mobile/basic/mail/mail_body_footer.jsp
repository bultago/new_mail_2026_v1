<div class="energy">
	<tctl:msg key="mail.quota"/>
	<strong>${mailQuota.usageUnit}</strong>
	<span>
	/ ${mailQuota.limitUnit}(<em>${mailQuota.percent}%</em>)
</div>
<form name="searchForm">
	<fieldset class="search_wrap">
		<div class="search_area">
			<div class="search_area_content">
				<input type="text" name="inputKeyWord" class="ip_search" value="${fn:escapeXml(keyWord)}"/>
				<input type="text" name="_tmp" style="display:none;width:0px;height:0px;"/>
			</div>
			<input id="search_btn" type="button" class="btn_search" onclick="searchMail()" title="<tctl:msg key="mail.search"/>" value="<tctl:msg key="mail.search"/>" />
		</div>
	</fieldset>
</form>

<script language="javascript">
	maxPage = parseInt("${fn:escapeXml(menuNum)}", 10);	
	function searchMail() {
		var f = document.searchForm;
		var val = trim(f.inputKeyWord.value);
		var keyWord = doubleUrlEncode("${fn:escapeXml(keyWord)}");
		if (val == "") {
			alert('<tctl:msg key="alert.search.nostr"/>');
			return;
		}

		if(!checkInputSearch(f.inputKeyWord,2,255,true)){
			return;
		}

		keyWord = val;
		
		var url = "/mobile/mail/mailList.do";
		var paramArray = [];
		paramArray.push({name:"page", value:"1"});
		paramArray.push({name:"folderName",value:"all"});
		paramArray.push({name:"flag", value:"${fn:escapeXml(flag)}"});
		paramArray.push({name:"keyWord", value:doubleUrlEncode(keyWord)});
		
		executeUrl(url, paramArray);
	}
</script>