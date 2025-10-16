<div id="ml_btnFMain">				
	<div class="TM_topLeftMain">					
		<div class="mainBtn">					
			<table cellspacing="0" cellpadding="0" border="0" class="TM_mainBtn_bg">
				<tr>
					<td nowrap style="white-space: nowrap;" >
						<div class="TM_t_left_btn"><a href="javascript:;" onclick="goWrite();" class="TM_mainBtn_split" ><div class="TM_mainWriteBtn"><tctl:msg key="mail.write"/></div></a></div>
					</td>						
					<td nowrap style="white-space: nowrap;" >
						<div class="TM_t_right_btn"><a href="javascript:;" onclick="goMDNList();" ><div class="TM_mainReceiveNotiBtn"><tctl:msg key="mail.receivenoti"/></div></a></div>
					</td>
				</tr>
			</table>					
		</div>					
		<div id="ml_quotaBox">				
			<div class="TM_graphBox">
				<div id="ml_quotaGraphBar" class="TM_graphBar" ></div>
				<div class="TM_capacity">
					<span id="ml_quotaUseAge" class="TM_quotaUsage">0M</span> / <span id="ml_quotaTotal">0M</span>
				</div>
				&nbsp;					
			</div>
		</div>
	</div>
</div>
<tctl:extModuleCheck moduleName="expressE" msie="true">
<div class="TM_secureMailWrite" onclick="goWriteSecureMail();">
	<span class="swriteBtn"><tctl:msg key="mail.secure.write"/></span>
</div>
</tctl:extModuleCheck>
<div style="position:relative;padding-bottom:30px;overflow:auto;" id="leftMenuRcontentWrapper">	
	<div>			
		<div style="width:218px;" id="m_defultFolder" >
			<div style="clear: both;"></div>		
			<div id="df_Inbox"  fname="Inbox"  fullname="Inbox" class="TM_ml_defaultFolder TM_df_inbox">
				<a href="javascript:;" onclick="goFolder('Inbox')" id="link_folder_Inbox"><tctl:msg key="folder.inbox" /></a>
				<span id="mf_Inbox_newCnt"></span>
				<a id="folder_manage_link" href="#"  onclick="goFolderManage()" class="TM_ml_sideBtn jpf"><tctl:msg key="comn.mgnt"  bundle="common"/></a>
			</div>				
			<div id="df_Sent"  fname="Sent"  fullname="Sent" class="TM_ml_defaultFolder TM_df_sent">
				<a href="javascript:;" onclick="goFolder('Sent')" id="link_folder_Sent"><tctl:msg key="folder.sent" /></a>
				<span id=mf_Sent_newCnt></span>
			</div>
			<div id="df_Drafts"  fname="Drafts"  fullname="Drafts" class="TM_ml_defaultFolder TM_df_drafts">
				<a href="javascript:;" onclick="goFolder('Drafts')" id="link_folder_Drafts"><tctl:msg key="folder.drafts" /></a>
				<span id="mf_Drafts_newCnt"></span>
			</div>
			<div id="df_Reserved"  fname="Reserved"  fullname="Reserved" class="TM_ml_defaultFolder TM_df_reserved">
				<a href="javascript:;" onclick="goFolder('Reserved')" id="link_folder_Reserved"><tctl:msg key="folder.reserved" /></a>
				<span id="mf_Reserved_newCnt"></span>
			</div>
			<div id="df_Spam" fname="Spam"  fullname="Spam" class="TM_ml_defaultFolder TM_df_spam">
				<a href="javascript:;" onclick="goFolder('Spam')" id="link_folder_Spam"><tctl:msg key="folder.spam" /></a>
				<span id="mf_Spam_newCnt"></span>
				<a href="javascript:;"  onclick="emptyFolder('Spam')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.empty" /></a>
			</div>
			<div id="df_Trash" fname="Trash"  fullname="Trash"  class="TM_ml_defaultFolder TM_df_trash">
				<a href="javascript:;" onclick="goFolder('Trash')" id="link_folder_Trash"><tctl:msg key="folder.trash" /></a>
				<span id="mf_Trash_newCnt"></span>
				<a href="javascript:;" onclick="emptyFolder('Trash')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.empty" /></a>
			</div>
			
			<div id="df_Quotaviolate" fname="Quotaviolate"  fullname="Quotaviolate"  class="TM_ml_defaultFolder TM_df_quotaviolate">
				<a href="javascript:;" onclick="goFolder('Quotaviolate')" id="link_folder_Quotaviolate"><tctl:msg key="folder.quotaviolate" /></a>
				<span id="mf_Quotaviolate_newCnt"></span>					
			</div>	

		</div>
		
		<%if(isMsie){%>
		<c:if test="${localmail}">
		<div class="TM_ml_defaultFolder TM_df_localmail">
			<a href="/dynamic/mail/localMailbox.do" ><tctl:msg key="mail.localmail"/></a>				
		</div>
		</c:if>
		<%}%>		
					
		<div class="TM_ml_defaultFolder TM_df_outermail">				
			<a href="javascript:;" onclick="pop3Win()"><tctl:msg key="conf.pop.44" bundle="setting"/></a>
		</div>
		
		<c:if test="${useArchive}">
		<div class="TM_ml_defaultFolder TM_df_outermail">
			<a href="${archiveSSOUrl}"><tctl:msg key="mail.achive.link"/></a>				
		</div>
		</c:if>
		
				
		<div class="TM_ml_MenuTitle">
			<a href="javascript:;" onclick="toggleMenu('m_userFolderTree');"><img src="/design/common/image/btn_menu_mius.gif" id="m_userFolderTree_view"></a>
			<a href="#" onclick="toggleMenu('m_userFolderTree')"><tctl:msg key="folder.user" /></a>
			<a href="javascript:;" onclick="addNode('m_userFolderTree')" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
		</div>		
		<div style="width:218px;" id="m_userFolderTree"></div>	
		
		<div style="height: 2px; clear: both;"><div class="TM_ml_line"></div></div>			
		
		<div class="TM_ml_MenuTitle">
			<a href="javascript:;" onclick="toggleMenu('m_userTag');"><img src="/design/common/image/btn_menu_mius.gif" id="m_userTag_view"></a>
			<a href="#" onclick="toggleMenu('m_userTag')"><tctl:msg key="folder.tag" /></a>
			<a href="javascript:;" onclick="addTag()" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
		</div>			
		<div style="width:218px;"id="m_userTag" ></div>

		<div style="height: 2px; clear: both;"><div class="TM_ml_line"></div></div>	
		
		<div class="TM_ml_MenuTitle">
			<a href="javascript:;" onclick="toggleMenu('m_searchFolderWrapper');"><img src="/design/common/image/btn_menu_mius.gif" id="m_searchFolderWrapper_view"></a>
			<a href="#" onclick="toggleMenu('m_searchFolderWrapper')"><tctl:msg key="folder.search" /></a>
			<a href="javascript:;" onclick="addSearchFolder()" class="TM_ml_sideBtn jpf"><tctl:msg key="btn.add" /></a>				
		</div>
		<div id="m_searchFolderWrapper" >		
			<div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu1" onclick="viewQuickList('flaged',true,true)"><tctl:msg key="menu.quick.flag"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu2" onclick="viewQuickList('attached',true,true)"><tctl:msg key="menu.quick.attach"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu3" onclick="viewQuickList('unseen',true,true)"><tctl:msg key="menu.quick.unread"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu4" onclick="viewQuickList('seen',true,true)"><tctl:msg key="menu.quick.read"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu5" onclick="viewQuickList('yesterday',true,true)"><tctl:msg key="menu.quick.yesterday"/></a>
				</div>
				<div class="TM_tree_wrapper">
					<a href="javascript:;" class="TM_searchFolder_basic btn_iconMenu6" onclick="viewQuickList('today',true,true)"><tctl:msg key="menu.quick.today"/></a>
				</div>
			</div>
			<div style="width:218px;" id="m_userSearchFolder" ></div>
		</div>
		
		<div style="height: 2px; clear: both;"><div class="TM_ml_line"></div></div>	
		
		<div class="TM_ml_MenuTitle">
			<a href="javascript:;" onclick="toggleMenu('m_userSharringFolder')"><img src="/design/common/image/btn_menu_mius.gif" id="m_userSharringFolder_view"></a>
			<a href="#"  onclick="toggleMenu('m_userSharringFolder')"><tctl:msg key="mail.shared.title" /></a>								
		</div>
		<div style="width:218px;" id="m_userSharringFolder" ></div>

		<div style="height: 2px; clear: both;"><div class="TM_ml_line"></div></div>		
	</div>						
	</div>			
</div>