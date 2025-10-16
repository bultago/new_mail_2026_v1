<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

	<form name="shareFolderForm" onsubmit="return false;">
		<input type="hidden" name="folderPath" value="${folder.folderPath}">
		<input type="hidden" name="path" value="${path}">
		<input type="hidden" name="nodeNum" value="${nodeNum}">
		<input type="hidden" name="type" value="${type}">
		<input type="hidden" name="fuid" value="${folder.fuid}">
		<input type="hidden" name="mode" value="${mode}">
		<input type="hidden" name="shareMode">
	<table id="webfolderShareListTable" cellpadding="0" cellspacing="0">
		<tr>
			<th style="width:100px;"><tctl:msg key="folder.name" bundle="webfolder"/></th>
			<td class="webfolderTd">${folder.folderName}</td>
		</tr>
		<tr>
			<th style="width:100px;"><tctl:msg key="folder.description" bundle="webfolder"/></th>
			<td class="webfolderTd" style="text-align:left;padding:3px 10px;">
				<textarea name="comment" style="width:100%;font-size:9pt" rows="2">${folder.comment}</textarea>
			</td>
		</tr>
		<tr>
			<th style="width:100px;"><tctl:msg key="folder.totalshare" bundle="webfolder"/></th>
			<td class="webfolderTd" style="padding:3px 10px;">
				<div style="float:left">
					<input type="checkbox" name="alluserCheck" id="alluserCheck" <c:if test="${folder.alluserCheck == true}"> checked </c:if> onclick="changeShareType(this)">
					<tctl:msg key="folder.alluser" bundle="webfolder"/>
				</div>
				<div style="float:left;padding-left:5px;">
					<span id="alluserOption">
						<div id="readWriteAuthSelect"></div>
						<script type="text/javascript">
							var rwAuthArray = [];
							rwAuthArray.push({index:'<tctl:msg key="folder.readauth" bundle="webfolder" />',value:"R"});
							rwAuthArray.push({index:'<tctl:msg key="folder.rwauth" bundle="webfolder" />',value:"W"});
						</script>
					</span>
				</div>
			</td>
		</tr>
		<tr id="selectShare">
			<th style="width:100px;"><tctl:msg key="folder.privateuser" bundle="webfolder"/><br><tctl:msg key="folder.share" bundle="webfolder"/></th>
			<td style="padding:3px 10px">
				<table width="100%" class="TB_webfolder2">
					<tr>
						<td colspan = "3">
							<table class="TB_search">
								<tr>
									<td class="ID">
										<div style="float:left">
										<div id="searchTypeSelect"></div>
										<script type="text/javascript">
											var searchTypeArray = [];
											searchTypeArray.push({index:'<tctl:msg key="folder.userid" bundle="webfolder" />',value:"uid"});
											searchTypeArray.push({index:'<tctl:msg key="folder.username" bundle="webfolder" />',value:"name"});
											if(MENU_STATUS.org && MENU_STATUS.org == "on") {
											<c:if test="${orgUse}">
											searchTypeArray.push({index:'<tctl:msg key="folder.org" bundle="webfolder"/>',value:"org"});
											</c:if>
											}
										</script>
										</div>
										<div id="share_notorg" style="float:left;padding-left:1px;">
											<input type="text" id="keyWord" name="keyWord" class="IP200" style="width:220px;" onKeyPress="(keyEvent(event) == 13) ? searchUser() : '';">
											<input type="text" name="_tmp" style="display:none"/>
										</div>
										<div id="share_org" style="float:left;padding-left:1px;display:none">
											<div id="selectOrgSelect"></div>
										</div>
										<div id="share_notorg" style="float:left;padding-left:1px;">
											<ul>
												<li id="share_notorg_btn" style="margin-top:0px;"><a href="#" onclick="searchUser()" class="btn_style4"><span><tctl:msg key="folder.search" bundle="webfolder"/></span></a></li>
												<li id="searching_user" style="margin-top:4px;padding-left:5px;display:none"><font color="#7F9DB9"><tctl:msg key="folder.searching" bundle="webfolder"/>..</font></li>
												<li id="share_org_btn" style="margin-top:0px;display:none"><a href="#" onclick="setSharedOrgList()" class="btn_style4"><span><tctl:msg key="comn.add" bundle="common"/></span></a> <label><input type="checkbox" id="includeSub" name="includeSub"/> <tctl:msg key="org.info.label.004" bundle="addr"/></label></li>
											</ul>
										</div>
									</td>	
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td rowspan="3" width="40%">
							<ul class="contentArea_to">
								<li><span class="title_arrow4"><tctl:msg key="folder.search.result" bundle="webfolder"/></span></li>
								<li><select class="ST_folder1" name="resultBox" id="resultBox" size="7" multiple>
										<c:forEach var="member" items="${members}">
											<option value="${member.email}/${member.name}">${member.name}(${member.id})</option>
										</c:forEach>
									</select>
								</li>
							</ul>
						</td>
						<td class="btn_center">
							<ul>
								<li><a href="javascript:;" onclick="moveItem(shareFolderForm.resultBox,shareFolderForm.readAuthBox, shareFolderForm.rwAuthBox)" class="btn_addressAdd"><span><tctl:msg key="comn.add" bundle="common"/></span></a></li>
								<li><a href="javascript:;" onclick="deleteItem(shareFolderForm.readAuthBox)" class="btn_addressDel"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
							</ul>
						</td>
						<td width="40%">
							<ul class="contentArea_to">
								<li><span class="title_arrow4"><tctl:msg key="folder.readauth" bundle="webfolder"/></span></li>
								<li>
									<select class="ST_folder2" name="readAuthBox" id="readAuthBox" size="4" multiple>
									</select>
								</li>
							</ul>
						</td>
					</tr>
					<tr>
						<td/>
						<td class="btn_area" colspan="2" style="padding-bottom:5px">
							<a href="javascript:;" title="up" onclick="moveItem(shareFolderForm.rwAuthBox,shareFolderForm.readAuthBox)" class="btn_fileUp"/>
							<a href="javascript:;" title="down" onclick="moveItem(shareFolderForm.readAuthBox,shareFolderForm.rwAuthBox)" class="btn_fileDown"/>
						</td>
					</tr>
					<tr>
						<td class="btn_center">
							<ul>
								<li><a href="javascript:;" onclick="moveItem(shareFolderForm.resultBox,shareFolderForm.rwAuthBox, shareFolderForm.readAuthBox)" class="btn_addressAdd"><span><tctl:msg key="comn.add" bundle="common"/></span></a></li>
								<li><a href="javascript:;" onclick="deleteItem(shareFolderForm.rwAuthBox)" class="btn_addressDel"><span><tctl:msg key="comn.del" bundle="common"/></span></a></li>
							</ul>
						</td>
						<td>
							<ul class="contentArea_to">
								<li><span class="title_arrow4"><tctl:msg key="folder.rwauth" bundle="webfolder"/></span></li>
								<li>
									<select class="ST_folder2" name="rwAuthBox" id="rwAuthBox" size="4" multiple>
									</select>
								</li>
							</ul>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
	<script language="javascript">
	var rwAuth = "";
	<c:if test="${folder.allShareAuth == 'R'}">rwAuth = "R"</c:if>
	<c:if test="${folder.allShareAuth == 'W'}">rwAuth = "W"</c:if>
	
	jQuery("#readWriteAuthSelect").selectbox({selectId:"rwAuthSelect",width:150}, rwAuth, rwAuthArray);
	jQuery("#searchTypeSelect").selectbox({selectId:"searchType", selectFunc:checkSelectType, width:80}, "uid", searchTypeArray);

	<c:forEach var="user" items="${userList}">
		setUserShareList("${user.auth}", "${user.type}", "${user.email}", "${user.uid}", "${user.name}")
	</c:forEach>
	changeShareType(document.shareFolderForm.alluserCheck);
	</script>