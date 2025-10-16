<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tctl" uri="/terrace-tag.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>

<div id="lastLoginInfo" class="TM_lloginC"></div>
<div class="title_desc_l">
	<div class="title_desc_r">
		<span class="title_desc_icon"><tctl:msg key="intro.tooltip"/></span>
	</div>
</div>
<div id="incon" class="portlet_intro_body">
	<div class="mailFirst_menu">
		<div><a onclick="goFolder('Inbox');" href="javascript:;" class="btn_mailFirst1" title="<tctl:msg key="intro.mail.read.title"/>"></a></div>
		<dl>
			<dt><tctl:msg key="intro.mail.read.title"/></dt>
			<dd><tctl:msg key="intro.mail.read.tooltip"/></dd>
		</dl>
		<div class="dotLineH"></div>
		<div><a onclick="goWrite();" href="javascript:;" class="btn_mailFirst2" title="<tctl:msg key="intro.mail.write.title"/>"></a></div>
		<dl>
			<dt><tctl:msg key="intro.mail.write.title"/></dt>
			<dd><tctl:msg key="intro.mail.write.tooltip"/></dd>
		</dl>
		<div class="dotLineH"></div>
		<div><a href="/dynamic/addr/addrCommon.do" class="btn_mailFirst3" title="<tctl:msg key="intro.mail.addr.title"/>"></a></div>
		<dl>
			<dt><tctl:msg key="intro.mail.addr.title"/></dt>
			<dd><tctl:msg key="intro.mail.addr.tooltip"/></dd>
		</dl>
		<div class="dotLineH"></div>
		<div><a href="/setting/viewLayout.do" class="btn_mailFirst4" title="<tctl:msg key="intro.mail.home.title"/>"></a></div>
		<dl>
			<dt><tctl:msg key="intro.mail.home.title"/></dt>
			<dd><tctl:msg key="intro.mail.home.tooltip"/></dd>
		</dl>
		<div class="dotLineH"></div>
		<div><a href="javascript:;" onclick="viewHelp();" class="btn_mailFirst5" title="<tctl:msg key="intro.mail.help.title"/>"></a></div>
		<dl>
			<dt><tctl:msg key="intro.mail.help.title"/></dt>
			<dd><tctl:msg key="intro.mail.help.tooltip"/></dd>
		</dl>				
	</div>
	
</div>

<script type="text/javascript">	
	//PageMainLoadingManager.completeWork("portletA");
	//PageMainLoadingManager.completeWork("portletB");
	//PageMainLoadingManager.completeWork("portletC");
	//PageMainLoadingManager.completeWork("portletD");

	var workTitle = "<span class='TM_work_title'>"+comMsg.comn_top_mailhome+"</span>";
	var loginTime = "${loginTimeInfo}";
	var loginStr;
	if(loginTime != ""){
		var loginTimeInfo = loginTime.split("|");
		loginStr = "<div class='TM_lastLoginText'>"+			
					msgArgsReplace(comMsg.comn_logintime,
							[loginTimeInfo[0].substring(0,4),
							loginTimeInfo[0].substring(4,6),
							loginTimeInfo[0].substring(6,8),
							loginTimeInfo[0].substring(8,10),
							loginTimeInfo[0].substring(10,12),
							loginTimeInfo[0].substring(12,14),
							loginTimeInfo[1]])+"</div>";
		
	} else {
		loginStr = "<div class='TM_lastLoginText'>"+comMsg.comn_loginfirst+"</div>";
	}
	jQuery("#lastLoginInfo").html(loginStr);
	setWorkTitle(workTitle);		
	jQuery(window).autoResize(jQuery("#incon"),"#copyRight");
</script>

