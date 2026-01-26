<div id="mbodyLoadMask" class="loadBody"></div>
<div id="noticeSystemMessage" class="noticePop_warp">
	<div class="noticePop">
	<div style="position: absolute;right:0;top:-5px;">
		<a class="btn_X" href="javascript:;" id="noticeCloseBtn"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>
	<dl>
		<dt>
		<tctl:msg key="comn.notice" bundle="common"/>			
		</dt>
		<dd id="systemNoticeContent"></dd>
	</dl>
	</div>
</div>
<div id="storeData" style="display:none"></div>
<table width="100%" cellpadding="0" cellspacing="0" border="0">	
	<tr><td style="padding:0px 15px 0px 15px">	 	
	<img src="/design/common/image/blank.gif" style="width:968px;height:1px"/><br/>		
<%if(tmenuUse.equals("enable")) {%>		
<%if(skin.equals("default")){%>
<%@include file="/common/tmenuDefault.jsp"%>
<%} else if(skin.equals("skin1")){%>
<%@include file="/common/tmenuSkin1.jsp"%>
<%} else if(skin.equals("skin2")){%>
<%@include file="/common/tmenuSkin2.jsp"%>
<%} else if(skin.equals("skin3")){%>
<%@include file="/common/tmenuSkin3.jsp"%>
<%} else if(skin.equals("custom")){%>
<%@include file="/common/tmenuCustom.jsp"%>
<%}%>
<%}%>
<script type="text/javascript">
if(MENU_STATUS.setting && MENU_STATUS.setting == "on") {}
else {jQuery("#user_setting").attr("href","javascript:;");}
</script>