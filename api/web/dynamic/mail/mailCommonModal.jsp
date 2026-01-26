<div id="m_tagForm"  title="<tctl:msg key="mail.tag.setup"/>" style="display:none">
<form id="tagForm" >
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="80px"></col>
		<col></col>
		<tr>
		<th class="lbout"><tctl:msg key="mail.tag.name"/></th>
		<td><input type="text" id="tagName" name="tagName"/></td>
		</tr>
		<tr>
		<th class="lbout"><tctl:msg key="mail.tag.color"/></th>
		<td><input id="tagColor" name="tagColor" value="#ff0000" /></td>
		</tr>
	</table>
	<input type="hidden" name="oldTagId" id="oldTagId" value="-1"/> 
</form>	
</div>


<div id="m_searchFolderForm"  title="<tctl:msg key="mail.searchfolder.setup"/>" style="display:none">
<form id="searchFolderForm" >
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="150px"></col>
		<col></col>
		<tr>
			<th class="lbout"><tctl:msg key="mail.sfolder.name"/></th>
			<td><input type="text" id="searchFolderName" name="searchFolderName" class="IP300"/></td>
		</tr>
		<tr>
			<th class="lbout"><tctl:msg key="mail.from"/></th>
			<td><input type="text" id="searchFolderFrom" class="IP300"/></td>
		</tr>
		<tr>
			<th class="lbout"><tctl:msg key="mail.to"/> (<tctl:msg key="mail.sfolder.include.001"/>)</th>
			<td><input type="text" id="searchFolderTo" class="IP300"/></td>
		</tr>
		<tr>
			<th class="lbout"><tctl:msg key="mail.sword"/></th>
			<td>			
				<div style="float: left;"><div id="searchFolderConditionSelect"></div></div>			
				<div style="float: left; margin-left:3px;">
				<input type="text" id="searchFolderPattern" name="searchFolderPattern" class="IP100px" style="width:145px;"/>
				</div>		
			</td>
		</tr>					
		<tr>
			<th class="lbout"><tctl:msg key="mail.folder"/></th>
			<td>
				<div style="float: left;"><div id="s2folderSelect"></div></div>
				<div style="float: left; margin-left:3px;">
					<div id="s2AllfolderDesc" style="white-space:nowrap;"></div>
				</div>
			</td>
		</tr>
		<tr>
			<th class="lbout"><tctl:msg key="mail.sfolder.add.condition"/></th>
			<td>
				<label><input type="radio" name="searchFolderFlag" value="" checked/> <tctl:msg key="mail.mdn.notselect"/></label>
				<label><input type="radio" name="searchFolderFlag" value="F"/> <tctl:msg key="menu.quick.flag"/></label>
				<label><input type="radio" name="searchFolderFlag" value="T"/> <tctl:msg key="menu.quick.attach"/></label>
				<label><input type="radio" name="searchFolderFlag" value="S"/> <tctl:msg key="menu.quick.read"/></label>
				<label><input type="radio" name="searchFolderFlag" value="U"/> <tctl:msg key="menu.quick.unread"/></label>
			</td>
		</tr>
	</table>
	<input type="hidden" name="oldSFolderId" id="oldSFolderId"/> 
</form>	
</div>

<div id="bigAttachManager"  title="<tctl:msg key="mail.attach.manager"/>" style="display:none">		
	<div><div id="bigAttachContents"></div></div>
</div>

<div id="spamRuleRegist"  title="<tctl:msg key="reportspam.title"/>" style="display:none">
<form name="spamRuleForm" id="spamRuleForm">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">		
		<col></col>		
		<tr>
		<th class="lable"><strong>* <tctl:msg key="reportspam.message"/></strong></th>		
		</tr>
		<tr>		
		<td style="padding-left:10px;">
			<div id="add_spam_area">
			<input type="checkbox"  name="addspam" id="addspam"  checked/>
			<label for='addspam'>
			<tctl:msg key="reportspam.addspam"/>
			</label>
			</div>
			
			<div id="move_trash_area">
			<input type="checkbox"  name="movetrash" id="movetrash"  checked/>
			<label for='movetrash'>
			<tctl:msg key="reportspam.gotrash"/>
			</label>
			</div>					
		</td>
		</tr>		
	</table>
	<table id="reportSpamNcscTable" cellpadding="0" cellspacing="0" class="jq_innerTable" style="display:none;">		
		<col></col>		
		<tr>
			<th class="lable"><strong>* <tctl:msg key="reportspam.ncsc.message"/></strong></th>		
		</tr>
		<tr>		
			<td style="padding-left:10px;">
				<input type="checkbox"  name="reportSpamNcsc" id="reportSpamNcsc" />
				<label id="reportSpamNcscLabel" for="reportSpamNcsc"></label>
			</td>
		</tr>		
	</table>	
</form>
</div>

<div id="whiteRuleRegist"  title="<tctl:msg key="reportham.title"/>" style="display:none">
<form name="whiteRuleForm" id="whiteRuleForm">
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">		
		<col></col>		
		<tr>
		<th class="lable"><strong>* <tctl:msg key="reportham.message"/></strong></th>		
		</tr>
		<tr>		
		<td style="padding-left:10px;">
			<input type="checkbox"  name="addwhite" id="addwhite" checked/>
			<label for='addwhite'>
			<tctl:msg key="reportham.addwhite"/>
			</label>				
			<br/>
			
			<input type="checkbox"  name="moveinbox" id="moveinbox" checked/>
			<label for='moveinbox'>
			<tctl:msg key="reportham.moveinbox"/>
			</label>					
		</td>
		</tr>		
	</table>	
</form>
</div>

<div id="autoRuleRegist"  title="<tctl:msg bundle="setting" key="conf.filter.8"/>" style="display:none">
<form name="autoRuleForm" id="autoRuleForm">
<input type="hidden" name="policy"  id="policy">
<input type="hidden" name="parentFolder" id="parentFolder">

	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="200px"></col>
		<col></col>		
		<tr>
		<th class="lbout"><tctl:msg key="conf.filter.48" bundle="setting"/></th>
		<td>
			<input type="checkbox"  id="ruleSenderCheck" name="ruleSenderCheck" />
			<input type="text"  class="IP200"  maxlength="64" id="ruleSender" name="ruleSender" />
			<tctl:msg key="conf.filter.23" bundle="setting"/>
		</td>
		</tr>
		<tr>
		<th class="lbout"><tctl:msg key="conf.filter.49" bundle="setting"/></th>
		<td>
			<input type="checkbox"  id="ruleReceiverCheck" name="ruleReceiverCheck" />
			<input type="text"  class="IP200"  maxlength="64" id="ruleReceiver" name="ruleReceiver" />
			<tctl:msg key="conf.filter.23" bundle="setting"/>	
		</td>
		</tr>
		
		<tr>
		<th class="lbout"><tctl:msg key="conf.filter.50" bundle="setting"/></th>
		<td>
			<input type="checkbox"  id="ruleSubjectCheck" name="ruleSubjectCheck" />
			<input type="text"  class="IP200"  maxlength="64" id="ruleSubject" name="ruleSubject" />
			<tctl:msg key="conf.filter.30" bundle="setting"/>	
		</td>
		</tr>
		
		<tr>		
		<td colspan="2" style="padding:5px 5px 5px 150px;">
			<div id="ruleAddPFBox">
			<div id="currentSaveRuleFolder" >
			<div style="float:left"><tctl:msg key="conf.filter.14" bundle="setting"/></div>
			<div style="float:left">
				<a href="javascript:" id="parentFolder_default" onclick="viweStatus='pfBox'; menuLayerOpen('pfBox')" onMouseOut="viweStatus='out'; menuLayerOut('pfBox')" class="selectMailBoxA"> ::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> ::::: </a>
				<div id="pfBox" onMouseOver="viweStatus='pfBox'; menuLayerOpen('pfBox')" onMouseOut="viweStatus='out'; menuLayerOut('pfBox')" class="gutter_layer div_scroll selectMailBox">
					<ul id="ruleSaveBox" class="list_st_02" style="padding:0px 0px 0px 5px; margin:0px;">
						<li onmouseover='yellowline(this)' onmouseout='grayline(this)' style="background:none">
							<a href="javascript:selPFolder('::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::','', true)">::::: <tctl:msg key="conf.filter.savefolder" bundle="setting"/> :::::</a>
						</li>									
					</ul>
				</div>
			</div>
			<div style="float:left" nowrap>
				<tctl:msg key="conf.filter.34" bundle="setting"/>
			</div>			
			<div class="fclear"></div>
			</div>
			
			<div style="float:left">
				<input type="checkbox" name="mbox"  id="mbox"  onClick="toggleAddNewBox()"> <tctl:msg key="conf.filter.31" bundle="setting"/>				
			</div>
			<div id="newSaveRuleFolder" style="display:none;float:left" nowrap>
				<a href="javascript:" id="parentFolder_new" onclick="viweStatus='newPfBox'; menuLayerOpen('newPfBox')" onMouseOut="viweStatus='out'; menuLayerOut('newPfBox')" class="selectMailBoxA"> ::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> ::::: </a>
				<div id="newPfBox" onMouseOver="viweStatus='newPfBox'; menuLayerOpen('newPfBox')" onMouseOut="viweStatus='out'; menuLayerOut('newPfBox')" class="gutter_layer div_scroll selectMailBox">
					<ul id="ruleSaveNewBox"  class="list_st_02" style="padding:0px 0px 0px 5px; margin:0px;">
						<li onmouseover='yellowline(this)' onmouseout='grayline(this)' style="background:none">
							<a href="javascript:selPFolder('::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> :::::','', true, 'new')">::::: <tctl:msg key="conf.filter.parentfolder" bundle="setting"/> :::::</a>
						</li>									
					</ul>
				</div>	
				<input type="text" class="IP200" maxlength="64" name="boxName" id="boxName" disabled/>
				<tctl:msg key="conf.filter.34" bundle="setting"/>													
			</div>			
			<div class="fclear"></div>				
		</td>
		</tr>
		
	</table>
</form>
</div>

<div id="addAddrPop"  title="<tctl:msg key="mail.addradd"/>" style="display:none">
<form name="addAddrForm" id="addAddrForm" onsubmit="return false;">	
	<div id="addAddrListContents">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="30px"></col>
			<col width="150px"></col>
			<col></col>
			<col width="100px"></col>
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<input type="checkbox" name="allchk"  onclick="checkAll(this,document.addAddrForm.addAddrEmail)"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.info.label.000" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;"><tctl:msg bundle="addr" key="addr.info.label.006" /></th>
			<th style="text-align: center;border-left:none; border-right:none;"><tctl:msg bundle="addr" key="addr.tree.groups.label" /></th>
			</tr>
		</table>			
		<div style="height:150px; overflow-X:hidden;overflow-Y:auto; padding:1px;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="addAddrTable">
				<col width="30px"></col>
				<col width="150px"></col>
				<col></col>
				<col width="100px"></col>
			</table>		
		</div>
	</div>
</form>
</div>

<div id="addRcptNormalAddrPop"  title="<tctl:msg key="mail.rcptadd"/>" style="display:none">
<form name="addRcptNormalAddrForm" id="addRcptNormalAddrForm">	
	<div id="addRcptNormalAddrListContents">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="30px"></col>
			<col width="150px"></col>
			<col></col>
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<input type="checkbox" name="allchk" id="addRcptNormalAddrAllchk" onclick="checkAll(this,document.addRcptNormalAddrForm.addRcptAddrEmail)"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.001" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.002" />
			</th>
			</tr>
		</table>			
		<div style="height:150px; overflow-X:hidden;overflow-Y:auto; padding:1px;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="addRcptNormalAddrTable">
				<col width="30px"></col>
				<col width="150px"></col>
				<col></col>
			</table>		
		</div>
	</div>
</form>
</div>

<div id="addRcptOrgAddrPop"  title="<tctl:msg key="mail.rcptadd"/>" style="display:none">
<form name="addRcptOrgAddrForm" id="addRcptOrgAddrForm">	
	<div id="addRcptOrgAddrListContents">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable">
			<col width="30px"></col>
			<col width="80px"></col>
			<col width="80px"></col>
			<col width="80px"></col>			
			<col></col>
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<input type="checkbox" name="allchk" id="addRcptOrgAddrAllchk" onclick="checkAll(this,document.addRcptOrgAddrForm.addRcptAddrEmail)"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.001" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.011" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.013" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.002" />
			</th>
			</tr>
		</table>			
		<div style="height:150px; overflow-X:hidden;overflow-Y:auto; padding:1px;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="addRcptOrgAddrTable">
				<col width="30px"></col>
				<col width="80px"></col>
				<col width="80px"></col>
				<col width="80px"></col>	
				<col></col>
			</table>		
		</div>
	</div>
</form>
</div>

<div id="addRcptEmptyAddrPop"  title="<tctl:msg key="mail.rcptadd"/>" style="display:none">
<form name="addRcptEmptyAddrForm" id="addRcptEmptyAddrForm" onsubmit="return false;">	
	<div id="addRcptNormalAddrListContents">
		<div class="TM_notice_box"><tctl:msg key="mail.rcptempty.summay"/></div>	
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;">
			<col width="30px"></col>
			<col width="150px"></col>
			<col></col>
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<input type="checkbox" name="allchk" id="addRcptEmptyAddrAllchk" onclick="checkAll(this,document.addRcptEmptyAddrForm.addRcptAddrEmail)"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.001" />
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg bundle="addr" key="addr.table.header.002" />
			</th>
			</tr>
		</table>			
		<div style="height:150px; overflow-X:hidden;overflow-Y:auto; padding:1px;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="addRcptEmptyAddrTable" style="width:100%;*width:;">
				<col width="30px"></col>
				<col width="150px"></col>
				<col></col>
			</table>		
		</div>
	</div>
</form>
</div>

<div id="sharedFolderPop"  title="<tctl:msg key="mail.shared.title" />" style="display:none">
<form name="sharedFolderForm" id="sharedFolderForm" onsubmit="return false;">
	<input type="hidden" name="oldSharedFolderUid" id="oldSharedFolderUid"/>
	<input type="hidden" name="sharedfolderName" id="sharedfolderName"/>	
	
	<table cellpadding="0" cellspacing="0" class="jq_innerTable">
		<col width="100px"></col>
		<col></col>		
		<tr>
		<th class="lbout"><tctl:msg key="mail.shared.setting" /></th>
		<td style="padding-left: 5px;">
			<input type="radio" name="shreadUse" id="shreadUseEnabled" onclick="toggleSharedFolderSetting()"/>
			<tctl:msg bundle="common" key="comn.enabled" />
			<input type="radio" name="shreadUse" id="shreadUseDisabled" onclick="toggleSharedFolderSetting()"/>	
			<tctl:msg bundle="common" key="comn.disabled" />
		</td>		
		</tr>
		<tr>
		<td colspan="2" style="padding-left: 5px;"> 
			<div style="float:left;margin-top:3px;">								
				<div id="sharedSearchItemSelect"></div>				
			</div>
			<div style="float:left;margin-top:3px;margin-left:5px;">								
				<input type="text" name="readerSearchStr" id="readerSearchStr" class="IP200" maxlength="64" onKeyPress="(keyEvent(event) == 13) ? searchSharedUser() : '';"/>
			</div>
			<div style="float:left;margin-top:3px;margin-left:5px;">
				<a class="btn_basic" href="javascript:;" onclick="searchSharedUser()"><span><tctl:msg key="mail.search"/></span></a>
			</div>
			<div class="fclear"></div>	
		</td>
		</tr>
		<tr>
		<td colspan="2" style="text-align: center">
			<table class="jq_innerTable" cellpadding="0" cellspacing="0">
				<tr>
				<td width="50%" style="text-align: center">
					<select name="searchReaderList" id="searchReaderList" class="selectSharedBox" size="10" multiple="multiple"></select>
				</td>
				<td nowrap>
					<a class="btn_leftmove" href="javascript:;" onclick="addSharedUser()"><span><tctl:msg bundle="common" key="comn.add" /></span></a>
					<br/>
					<br/>
					<a class="btn_rightmove" href="javascript:;" onclick="removeSharedUser()"><span><tctl:msg bundle="common" key="comn.del" /></span></a>
				</td>
				<td width="50%" style="text-align: center">
					<select name="settingReaderList" id="settingReaderList" class="selectSharedBox" size="10" multiple="multiple"></select>
				</td>
				<tr>
			</table>			
		</td>
		</tr>
	</table>
</form>
</div>


<div id="checkSendInfoPop"  title="<tctl:msg key="mail.rcptcheck"/>" style="display:none">
<form name="checkSendInfoForm" id="checkSendInfoForm">	
	<div id="checkSendInfoListContents">
		<div class="TM_notice_box"><tctl:msg key="mail.rcptcheck.info"/></div>			
		<div style="overflow-X:hidden;overflow-Y:auto; height: 300px;">
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;table-layout: fixed;*border-bottom: #E6E6E6 solid 1px;">
			<col width="100px"></col>			
			<col></col>
			<tr>
			<th><tctl:msg key="mail.subject"/></th>
			<td id="subjectCheckL"></td>			
			</tr>
			
			<tr id="toRcptCheckBox">
			<th><tctl:msg key="mail.to"/></th>
			<td style='padding:0px;' id="toRcptCheckL">
			</td>			
			</tr>
			
			<tr id="ccRcptCheckBox" style='display:none'>
			<th><tctl:msg key="mail.cc"/></th>
			<td style='padding:0px;' id="ccRcptCheckL">			
			</td>			
			</tr>
			
			<tr id="bccRcptCheckBox" style='display:none'>
			<th><tctl:msg key="mail.bcc"/></th>
			<td style='padding:0px;' id="bccRcptCheckL">			
			</td>			
			</tr>
			
			<tr id="attachCheckBox" style='display:none'>
			<th><tctl:msg key="mail.attach"/></th>
			<td style='padding:0px;' id="attachCheckL">
			</td>			
			</tr>
		</table>
		</div>
		
	</div>
</form>
</div>

<div id="m_messageUploadForm"   class="popup_style2 TM_rule_modal">
	<div class="title">
		<span><tctl:msg key="mail.mailupload.setup"/></span>
		<a class="btn_X" href="javascript:;" onclick="closeUploadModal()"><tctl:msg bundle="common"  key="comn.close"/></a>
	</div>	

	<form id="messageUploadForm" name="messageUploadForm"  enctype="multipart/form-data">
	<div class="TM_modal_content_wrapper">		
		<div class="TM_modal_content" style="height:30px;">
			<div id="simpleEmlFileInit" style="display:none;padding: 3px">                                                             
	            <input type="file" size="25" onchange="uploadSimpleEmlFile()" id="simpleEmlFile" name="theFile" class="TM_attFile" style="margin: 0px; width:370px;" multiple="multiple">
	            <input type="hidden" name=upldtype value='upld'>
	            <input type="hidden" name="writeFile" value="true">
	            <input type="hidden" name="uploadType">
	            <input type="hidden" name="maxAttachFileSize">  
	            <input type="hidden" name="folder">
	        </div>
	        <div id="upload_eml_att_frame" ></div>  
		</div>									
	</div>	
	<div id="uploadHiddenFrame"></div>
	</form>	
	<div class="dotLine"></div>
	<div class="btnArea">		
		<a class="btn_style3" href="javascript:;" onclick="closeUploadModal()"><span><tctl:msg bundle="common" key="comn.close"/></span></a>
	</div>
</div>


<div id="attachUploadProgress"  title="<tctl:msg key="comn.upload.title" bundle="common"/>" style="display:none">
<form name="attachUploadProgressForm" id="attachUploadProgressForm">	
	<div id="attachUploadProgressContents">			
		<table cellpadding="0" cellspacing="0" class="jq_innerTable" style="table-layout:fixed;width:100%;*width:;">
			<col></col>
			<col width="100px"></col>			
			<tr>
			<th class="lbout" style="text-align: center; border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.filename" bundle="common"/>
			</th>
			<th style="text-align: center;border-left:none; border-right:none;">
				<tctl:msg key="comn.upload.status" bundle="common"/>
			</th>
			</tr>
		</table>			
		<div id="uploadLoadingBox" style="height:300px; overflow-X:hidden;overflow-Y:auto; padding:1px;position: relative;">
			<table border="0" cellspacing="0" cellpadding="0"  class="jq_innerTable" id="attachFileUploadTable" style="width:100%;*width:;table-layout:fixed;position: relative;">
				<col></col>
				<col width="100px"></col>				
			</table>
		</div>
	</div>
</form>
</div>
<div id="addAddrConfirm" title="<tctl:msg key="addr.dialog.title.004" bundle="addr"/>" style="display:none">
    <div style="text-align:center;font-weight:bold;height:50px;line-height:30px">
        <span id="dupQuestion"></span><br>
        <span id="dupEmailAddress" style="color:#3D83C1"></span> : <span id="dupEmail" style="color:#FF8000"></span> / <span id="dupName" style="color:#FF8000"></span>
    </div>
</div>

<div id="editorImageUpload" title="<tctl:msg key="mail.editor.image.upload.title"/>" style="display:none">
    <iframe id="editorImageUploadIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

<div id="searchAddr" title="" style="display:none">
    <iframe id="searchAddrIframe" name="searchAddrIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

<div id="writePreview" title="" style="display:none">
    <iframe id="writePreviewIframe" name="writePreviewIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

<div id="extpop3" title="" style="display:none">
    <iframe id="extpop3Iframe" name="extpop3Iframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

<div id="webfolder" title="" style="display:none">
    <iframe id="webfolderIframe" name="webfolderIframe" frameborder="0" src="" style="border:0px;overflow:hidden;" scrolling="no"></iframe>
</div>

<div id="geoIpViewer"  title="<tctl:msg key="mail.geoip.title"/>" style="display:none">		
	<div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="jq_innerTable" style="width:100%;*width:;table-layout:fixed;">
			<colgroup span="5">
				<col width="30px">		
				<col width="100px">		
				<col width="100px">	
			</colgroup>
			<tr>
				<th class="clistHeader"><tctl:msg key="mail.geoip.title.column1"/></th>
				<th class="clistHeader"><tctl:msg key="mail.geoip.title.column2"/></th>
				<th class="clistHeader"><tctl:msg key="mail.geoip.title.column3"/></th>	
			</tr>
			<tr>	
				<td colspan="3" align="center" style="padding:0px;">
					<div id="geoIpLoadingbar" style="padding-top:10px;"></div>
					<div id="geoIpListWrapper" style="vertical-align: top;height:150px;overflow:auto;">
						<table id="geoIpListTable" cellpadding="0" cellspacing="0" border="0" class="jq_innerTable" style="width:100%;*width:;table-layout:fixed;">
							<colgroup span="3">
								<col width="30px">		
								<col width="100px">		
								<col width="100px">	
							</colgroup>
						</table>	
					</div>
				</td>
			</tr>		
		</table>
	</div>
</div>