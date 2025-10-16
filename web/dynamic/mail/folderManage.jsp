<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>


<h4 class="TM_manage_header">
	<div class="arrow"><tctl:msg key="mail.foldermgnt"/></div>
</h4>
<table cellpadding="0" cellspacing="0" class="TB_mailAdmin">
	<tbody>
		<tr>
			<th class="mail_box"><tctl:msg key="mail.folder"/></th>
			<th class="mail_aging"><tctl:msg key="mail.aging"/></th>
			<th class="mail_count"><tctl:msg key="mail.count"/></th>			
			<th class="mail_capacity"><tctl:msg key="mail.quota"/></th>
		</tr>
		<tr>
			<th colspan="5" class="user_mailBox alignLeft">
				<strong><tctl:msg key="mail.defaultbox"/></strong>				
			</td>
		</tr>
		
		<c:forEach var="dfolder" items="${dfolders}">
		<tr>
			<td colspan="2">				
				<div class="TM_fm_dfolder_wrap" fname="${dfolder.encName}" fullname="${dfolder.fullName}" depth="${dfolder.depth}">					
					<div class="tcfolder"></div>										
					<a href="javascript:;" class="TM_mfolder" onclick="viewFolder('${dfolder.fullName}')">${dfolder.name}</a>
				</div>
			</td>
			<td class="alignCenter"><strong class="orange">${dfolder.unseenCnt}</strong><tctl:msg key="mail.countunit"/>/${dfolder.totalCnt}<tctl:msg key="mail.countunit"/></td>
			<td class="TD_bar last">
				<div class="TM_manage_graphBox">
					<div class="TM_graphBar" style="width:${dfolder.quota.percent}%"></div>
					<div class="TM_capacity">${dfolder.quota.usageUnit}</div>
					&nbsp;					
				</div>
			</td>
		</tr>
		</c:forEach>
		<tr>
			<th colspan=5 class="user_mailBox alignLeft">
				<strong><tctl:msg key="mail.userbox"/></strong>
				<input type="text"  id="ufolderMain"  class="basicInput" style="width:200px"/>
				<a href="javascript:;" class="btn_basic" onclick="addMainFolderService()"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
			</th>
		</tr>
		<c:forEach var="ufolder" items="${ufolders}" varStatus="info">
		<tr id="ufolder_tr_${info.index}">
			<td>
				<div fname="${ufolder.encName}" fullname="${ufolder.fullName}" depth="${ufolder.depth}" shared="${ufolder.share}" sharedUid="${ufolder.sharedUid}" class="TM_fm_ufolder_wrap">						
					<c:if test="${!ufolder.share}">
					<div class="tcfolder"></div>
					</c:if>
					<c:if test="${ufolder.share}">
					<div class="tsfolder"></div>
					</c:if>		
					<a href="javascript:;" id="ufolder_link_${info.index}" onclick="viewFolder('${ufolder.fullName}')">${ufolder.name}</a>					
				</div>											
			</td>	
			<td>
				<div style="float: left;">		
					<div id="againg_${info.index}_select" aging="${ufolder.againg}"></div>
				</div>
				<div class="cls"></div>
				<script type="text/javascript">
				jQuery("#againg_${info.index}_select").selectbox({selectId:"againg_${info.index}",
						selectFunc:function(val){changeAging('${info.index}','${ufolder.encName}',val);}},
						${ufolder.againg},
						[{index:mailMsg.folder_aging_noaging,value:-1},
						 {index:mailMsg.folder_aging_unlimited,value:0},
						 {index:mailMsg.folder_aging_30,value:30},
						 {index:mailMsg.folder_aging_90,value:90},
						 {index:mailMsg.folder_aging_120,value:100}]);		
				</script>			
								
			</td>		
			<td class="alignCenter"><strong class="orange">${ufolder.unseenCnt}</strong><tctl:msg key="mail.countunit"/>/${ufolder.totalCnt}<tctl:msg key="mail.countunit"/></td>
			<td class="TD_bar last">
				<div style="position: relative;">
				<div class="TM_manage_graphBox">
					<div class="TM_graphBar" style="width:${ufolder.quota.percent}%"></div>										
					<div class="TM_capacity">${ufolder.quota.usageUnit}</div>
					&nbsp;					
				</div>
				</div>			
					
			</td>
		</tr>
		</c:forEach>
		<tr>
			<th colspan=5 class="user_mailBox alignLeft">
				<strong><tctl:msg key="mail.shared.title"/></strong>				
			</th>
		</tr>
		<c:forEach var="shfolder" items="${sharedfolders}">
		<tr id="shfolder_tr_${info.index}">
			<td colspan="5">
				<div class="TM_fm_shfolder_wrap">
					<div class="tcfolder"></div>
					<a href="javascript:;" id="shfolder_link_${info.index}" onclick="viewSharedFolder('${shfolder.folderName}','${shfolder.sharedUserSeq}')">${shfolder.printFolderName}</a>					
				</div>											
			</td>
		</tr>	
		</c:forEach>
	</tbody>
</table>

<h4 class="TM_manage_header">
	<div class="arrow"><tctl:msg key="mail.sfoldermgnt"/></div>	
</h4>
<table cellpadding="0" cellspacing="0" class="TB_mailAdmin">
	<tbody>
		<tr>
			<th class="mailBox_name"><tctl:msg key="mail.folder"/></th>
			<th class="proviso"><tctl:msg key="mail.scond"/></th>
		</tr>
		<tr>
			<th colspan=2 class="user_mailBox alignLeft">
				<div class="TM_selectContentWrapper">
					<input type="text" class="basicInput" id="sfolderName" style="width:190px" />
				</div>
				<div class="TM_selectContentWrapper">
					<div id="sfolderTargetSelect" ></div>
				</div>			
				<div class="TM_selectContentWrapper">	
					<div id="sfolderConditionSelect"></div>
				</div>
				<div class="TM_selectContentWrapper">
					<input type="text" class="basicInput" id="sfolderValue" style="width:100px"/>
					<a href="javascript:;" onclick="addMainSearchFolderService()" class="btn_basic"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
				</div>
				<div class="cls"></div>
			</th>  
		</tr>
		<c:forEach var="sfolder" items="${sfolders}">
		<tr>
			<td>
				<div class="TM_fm_sfolder_wrap" sid="${sfolder.id}" qval="${sfolder.query}" fname="${sfolder.name}">
					<div class="tcfolder"></div>						
					<a href="javascript:;" onclick="viewSearchFolder('${sfolder.query}')">${sfolder.name}</a>
				</div>
			</td>
			<td><span class="TM_sfolder_query">${sfolder.query}</span></td>
		</tr>
		</c:forEach>							
	</tbody>
</table>

<h4 class="TM_manage_header">
	<div class="arrow"><tctl:msg key="mail.tagmgnt"/></div>	
</h4>
<table cellpadding="0" cellspacing="0" class="TB_tagAdmin">
	<tbody>
		<tr>
			<th class="alignLeft">
				<a href="javascript:;" onclick="addMTag()" class="btn_basic"><span><tctl:msg key="comn.add" bundle="common"/></span></a>
			</th>			
		</tr>		
		<c:forEach var="tag" items="${tags}">
		<tr>
			<td>
				<div class="TM_fm_tag_wrap" fname="${tag.name}" sid="${tag.id}" tcolor="${tag.color}">
					<div class="tagimg" style="background:${tag.color}"></div>
					<a href="javascript:;" onclick="viewTagMessage('${tag.id}')">${tag.name}</a>										
				</div>				
			</td>			
		</tr>	
		</c:forEach>					
	</tbody>
</table>
<div style='height:50px;'></div>

<script type="text/javascript">
var backupInfo = ${backupInfo.json};
var hideLeftScroll = setInterval(function(){
	jQuery("#m_leftMenu").css("overflow","hidden");
},100);

loadManage(backupInfo);

<c:if test="${reload eq 'off'}">
$("m_contentMain").scrollTop = 0;
</c:if>

</script>