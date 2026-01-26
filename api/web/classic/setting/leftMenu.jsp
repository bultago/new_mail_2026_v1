<div>
<div id="ml_btnFMain_s">
	<div class="TM_topLeftMain">				
	<div class="mainBtn" >					
		<table cellpadding="0" cellspacing="0" border="0" class="TM_mainBtn_bg">
			<tr>
			<td nowrap style="white-space: nowrap;" >
				<div class="TM_t_left_btn"><a href="/setting/viewSetting.do" class="TM_mainBtn_split"><div class="TM_l_t_settingEnv"><tctl:msg key="menu_conf.profile" bundle="setting"/></div></a></div>
			</td>						
			<td nowrap style="white-space: nowrap;" >
				<div class="TM_t_right_btn"><a href="/setting/viewUserInfoAuth.do" ><div class="TM_l_t_settingProfile"><tctl:msg key="menu_conf.modify" bundle="setting"/></div></a></div>
			</td>
			</tr>
		</table>					
	</div>			
	</div>
</div>

<div id="leftMenuRcontentWrapper">
<div id="leftMenuRcontent">
<div>	

<ul id="settingLeftMenu">	
	<li><div class="ic_set_sign"><a id="sign_menu" href="/setting/viewSign.do"><tctl:msg key="menu_conf.sign" bundle="setting"/></a></div></li>
	<li><div class="ic_set_folder"><a id="folder_manage_menu" href="/dynamic/mail/viewFolderManage.do"><tctl:msg key="mail.foldermgnt"/></a></div></li>
	<li><div class="ic_set_mailhome"><a id="home_menu" href="/setting/viewLayout.do"><tctl:msg key="menu_conf.home" bundle="setting"/></a></div></li>
	<li><div class="ic_set_spam"><a id="spam_menu" href="/setting/viewSpamRule.do"><tctl:msg key="conf.spamrule.33" bundle="setting"/></a></div></li>
	<li><div class="ic_set_filter"><a id="filter_menu" href="/setting/viewFilter.do" ><tctl:msg key="menu_conf.filter" bundle="setting"/></a></div></li>
	<%
    //TCUSTOM-3763 20180129 S
	Object objAutoForwardMenu = session.getAttribute("autoForwardMenu");
    String strAutoForwardMenu = (objAutoForwardMenu != null) ? (String)objAutoForwardMenu : "disable";	
	%>
	<% if("enable".equalsIgnoreCase(strAutoForwardMenu)) {%>
    <li><div class="ic_set_forward"><a id="forward_menu" href="/setting/viewForward.do"><tctl:msg key="menu_conf.forward" bundle="setting"/></a></div></li>
	<% } 
    //TCUSTOM-3763 20180129 E
	%>
	<li><div class="ic_set_reply"><a id="autoreply_menu" href="/setting/viewAutoReply.do"><tctl:msg key="menu_conf.reply" bundle="setting"/></a></div></li>
	<li><div class="ic_set_extmail"><a id="extmail_menu" href="/setting/viewExtMail.do"><tctl:msg key="menu_conf.external" bundle="setting"/></a></div></li>
	<li id="scheduler_leftmenu"><div class="ic_set_scheduler"><a id="scheduler_menu" href="/setting/viewSchedulerSetting.do"><tctl:msg key="menu_conf.scheduler" bundle="setting"/></a></div></li>
	<li><div class="ic_set_lastrcpt"><a id="lastrcpt_menu" href="/setting/viewLastrcpt.do"><tctl:msg key="conf.lastrcpt.menu" bundle="setting"/></a></div></li>
	<li><div class="ic_set_signupdate"><a id="signupdate_menu" href="/setting/viewPKIUpdate.do"><tctl:msg key="conf.signupdate.menu" bundle="setting"/></a></div></li>
	<tctl:pkiCheck msie="true" loginMode="PKI">
	   <li><div class="ic_set_signupdate"><a id="signupdate_menu" href="/setting/viewPKIUpdate.do"><tctl:msg key="conf.signupdate.menu" bundle="setting"/></a></div></li>
	</tctl:pkiCheck>
</ul>

</div>
</div>
</div>

<%@include file="/common/sideMenu.jsp"%>
</div>