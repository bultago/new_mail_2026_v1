jQuery().ready(function(){
	
	var mainLayerPane = new LayerPane("m_mainBody","TM_m_mainBody");	
	var menuLayerPane = new LayerPane("m_leftMenu","TM_m_leftMenu",220,180,350);
	var contentLayerPaneWapper = new LayerPane("m_contentBodyWapper","",300,100,500);
	
	mainSplitter = new SplitterManager(mainLayerPane,
										menuLayerPane,
										contentLayerPaneWapper,
										"sm","mainvsplitbar","hsplitbar");	
	mainSplitter.setReferencePane(["m_contentBody","copyRight"]);	
	mainSplitter.setSplitter("v",true);
	jQuery(window).autoResize(jQuery("#m_mainBody"),"#copyRight");		
	if(IS_LMENU_USE){loadSideMenu();}
	resizeLeftMenuSize();
	
	var contentLayerPane = new LayerPane("m_contentBody","TM_m_contentBody");
	var listLayerPane = new LayerPane("m_contentMain","TM_m_contentMain",300,0,0);
	var previewLayerPane = new LayerPane("m_contentSub","TM_m_contentSub",400,0,0);		
	
	contentSplitter = new SplitterManager(contentLayerPane,
			listLayerPane,
			previewLayerPane,
			"sc","vsplitbar","hsplitbar");
	
	contentSplitter.setSplitter("n",true);	
	jQuery(window).autoResize(jQuery("#m_contentBody"),"#copyRight");	
	contentSplitter.setSplitter("n",true);
	
	noteControl = new NoteControl(noteOption);
	noteListControl = new NoteListControl();
	
	goFolder("Inbox");
	
	jQuery.removeProcessBodyMask();			
});

var NoteListControl = Class.create({	
	initialize: function(){	
		this.cashMailSize = 0;
		this.cashMailMode = "N";
		this.cashMailListtemplate = "";
		this.muids;
	
		this.templateSortMsg = new HashMap();
		this.templateSortMsg.put("from",comMsg.note_msg_057);
		this.templateSortMsg.put("to",comMsg.note_msg_058);
		this.templateSortMsg.put("fromTo",comMsg.note_msg_057+"/"+comMsg.note_msg_058);
		this.templateSortMsg.put("arrivalnormal",mailMsg.mail_receivedate);
		this.templateSortMsg.put("arrivalsent",mailMsg.mail_senddate);
		this.templateSortMsg.put("date",comMsg.note_msg_059);
		
		this.templateSortMsg.put("content",comMsg.note_msg_019);
		this.templateSortMsg.put("receivenoti",mailMsg.mail_receivenoti);
		
		this.templates = {
			commonWrapper:"<form name='listForm' id='listForm'><input type='hidden' name='allSearchSelectCheck' id='allSearchSelectCheck' value='off'/><div class='TM_listWrapper' style='position:relative;'><table class='TM_b_list' id='msgListHeader'>",
			HHeaderNCols:"<col width='40px'></col><col width='30px'></col><col width='100px'></col><col></col><col width='110px'></col>{RECEIVE_NOTI_COL}",
			commonHeaderWrapper:"<tr>",		
			
			HHeaderStart:"<th scope='col'><input type='checkbox' id='allChk' onclick=\"allCheckMessage(listForm.msgId,this.checked)\"></th><th scope='col'>"+mailMsg.mail_kind+"</th>{FOLDER_NAME_INFO_TH}",				                      
			HHeaderRepeater:"<th scope='col'>{SORT_INDEX}</th>",
			
			commonHeaderWrapperEnd:"</tr></table></div>",
			
			commonBodyWrapperStart:"<div id='m_msgListWrapper' {MSG_WRAPPER_STYLE}><div id='m_msgListContent' class='TM_listWrapper'><table id='m_messageList'>",			
			commonBodyWrapper:"<tr id='{MSG_TR_ID}' class='{MSG_TR_CLASS}'>",
			commonBodyCheckPart:"<td class='TM_chkTd'><input type='checkbox'  name='msgId' id='chk_{MSG_FOLDER}_{MSG_ID}' value='{MSG_ID}' onclick='checkMessage(this)' onfocus='this.blur()' femail='{MSG_FROM_EMAIL}' temail='{MSG_TO_EMAIL}'/></td>",
			commonBodyFlagPart:"<td class='TM_list_flag'>{MSG_FLAG_CONTENTS}</td>",
			commonBodyEmptyPart:"<td>&nbsp;</td>",
			
			HBodyFolderNamePart:"<td class='TM_list_from' title='{MSG_F_FOLDERNAME}'>{MSG_FOLDER_NAME}</td>",
			HBodySubjectPart:"<td class='TM_list_subject' id='m_s_{MSG_FOLDER}_{MSG_ID}' {MSG_READMESSAGE}> {MSG_PRIORITY} {MSG_SAVE_TYPE} <a href='javascript:;' class='msubject' id='msg_subject_{MSG_FOLDER}_{MSG_ID}'>{MSG_SUBJECT}</a></td>",
			HBodyEmailPart:"<td class='TM_list_from' title='{MSG_PF_EMAIL}'><div class='TM_list_from' nowrap><a href='javascript:;' id='m_a_{MSG_TR_ID}' {MSG_ADDR_SUBMENU}>{MSG_P_EMAIL}</a></div><div id='{MSG_P_EMAIL_SUB_ID}'></div></td>",			
			HBodyDatePart:"<td class='TM_list_Date_Size' align='right'>{MSG_DATE}</td>",
			HBodyReceiveNotiPart:"<td id='{MSG_RECEIVE_NOTI_ID}' class='TM_list_Date_Size ReceiveClass' style='text-align:center'><img src='/design/common/image/ajax-loader.gif' align='absmiddle' height='20' vapsce='5'/></td>",
			
			commonBodyWrapperEnd:"</tr>",
			commonWrapperEnd:"</table></div></div><div id='pageBottomNavi' class='pageNavi'></div></form>",
			commonWrapperEndNoPaging:"</table></div></div></form>",
			sortUpImage:"<img src='/design/common/image/icon/ic_bullet_up.gif' class='sortImg'>",
			sortDownImage:"<img src='/design/common/image/icon/ic_bullet_down.gif' class='sortImg'>",
			priorityImage:"<img src='/design/common/image/ic_import.gif'>"
		};
	},
	makeList:function(drowId,listData){
		
		var sb = new StringBuffer();
		var templates = this.templates;
		var folderName = listData.folderName;
		var isSentFolder = (folderName == 'Sent')?true:false;
		
		pagingInfo = {"total":listData.total,"pageBase":listData.pageBase};
		
		sb.append(templates.commonWrapper);
		sb.append(this.drowHmodeList(listData));
		if(listData.messageList.length > 0){
			sb.append(templates.commonWrapperEnd);
		}else{
			sb.append(templates.commonWrapperEndNoPaging);
		}
		
		$(drowId).innerHTML = sb.toString();
		
		sb.destroy();
		
		if (isSentFolder) {
			checkMdnInfo(listData.messageList);
		}
		
		$("m_contentMain").style.overflow = "auto";
		jQuery(window).trigger("resize");
	},
	drowHmodeList:function(listData){		
		var sb = new StringBuffer();
		var templates = this.templates;
		var mlist = listData.messageList;
		var folderName = listData.folderName;
		var isSentFolder = (folderName == 'Sent')?true:false;
		var isSaveFolder = (folderName == 'Save')?true:false;
		var noteModeFlag = "L";
		var listAllHeaderCols = "";
		var listAllHeaderContent = "";
		var colsCtn = 5;
		if(isSentFolder){
			listAllHeaderCols = "<col width='110px' ></col>";
			colsCtn = 6;
		}
		listColsContent = replaceAll(templates.HHeaderNCols,"\{RECEIVE_NOTI_COL\}",listAllHeaderCols);
		sb.append(listColsContent);
		
		sb.append(templates.commonHeaderWrapper);
		sb.append(replaceAll(templates.HHeaderStart,"\{FOLDER_NAME_INFO_TH\}",listAllHeaderContent));
		
		var sortBy = listData.sortBy;
		var sortDir = listData.sortDir;		
		if(!isSentFolder){
			var from = (isSaveFolder) ? "fromTo" :"from";
			sb.append(this.drowHSortItem(from));
			sb.append(this.drowHSortItem("content"));
			var date = (isSaveFolder) ? "date" :"arrivalnormal";
			sb.append(this.drowHSortItem(date));
		} else {
			sb.append(this.drowHSortItem("to"));
			sb.append(this.drowHSortItem("content"));
			sb.append(this.drowHSortItem("arrivalsent"));
			sb.append(this.drowHSortItem("receivenoti"));
			noteModeFlag = "S";
		}
		sb.append(templates.commonHeaderWrapperEnd);
		
		sb.append(replaceAll(templates.commonBodyWrapperStart,"\{MSG_WRAPPER_STYLE\}","style='position:relative'"));
		sb.append(listColsContent);
		
		if(mlist.length > 0){			
			var isReflash = false;
			var listContentBuffer;
			var listContentStr;
			
			if(this.cashMailSize != mlist.length || this.cashMailMode != noteModeFlag){
				listContentBuffer = new StringBuffer();
				listContentBuffer.append(templates.commonBodyWrapper);
				listContentBuffer.append(templates.commonBodyCheckPart);
				listContentBuffer.append(templates.commonBodyFlagPart);
				listContentBuffer.append(templates.HBodyEmailPart);
				listContentBuffer.append(templates.HBodySubjectPart);
				listContentBuffer.append(templates.HBodyDatePart);
				
				if(isSentFolder){
					listContentBuffer.append(templates.HBodyReceiveNotiPart);
				}
				
				listContentBuffer.append(templates.commonBodyWrapperEnd);
				listContentStr = listContentBuffer.toString();
				this.cashMailSize = mlist.length;
				this.cashMailListtemplate = listContentStr;
				this.cashMailMode = noteModeFlag;
			} else {
				listContentStr = this.cashMailListtemplate;
			}		
						
			for ( var i = 0; i < mlist.length; i++) {
				this.muids = new Array();
				this.muids[i] = mlist[i].id;
				sb.append(this.listContentDrow(i,isSentFolder, folderName, mlist[i], listContentStr));			
			}			
		} else {			
			sb.append("<tr><td colspan='"+colsCtn+"'>"+comMsg.note_msg_025+"</td></tr>");
			if(jQuery.browser.safari){
				sb.append("<tr>");				
				for ( var i = 1; i <= colsCtn; i++) {
					sb.append("<td class='none'></td>");
				}
				sb.append("</tr>");
			}
		}		
		var content = sb.toString();
		sb.destroy();		
		return content;
	},
	drowHSortItem:function(item){		
		var templates = this.templates;
		var sortIndex = this.templateSortMsg.get(item);
		
		var sortContent = templates.HHeaderRepeater;
		var valueMap = [];
		valueMap["SORT_VALUE"] = item;
		valueMap["SORT_INDEX"] = sortIndex;		
		sortContent = RendererReplaceValue(sortContent,valueMap);
		return sortContent;
	},
	listContentDrow:function(idx, isSentFolder, folderName, msg, listContentStr){
		var templates = this.templates;
		var isSaveFolder = (folderName == 'Save')?true:false;
		var folderNameVal = msg.folderName;
		var valueMap = [];		
		var mtr_id = folderNameVal +"_"+msg.id;
		valueMap["MSG_TR_ID"] = mtr_id;
		valueMap["MSG_ID"] = msg.id;
		valueMap["MSG_FOLDER"] = folderNameVal;
		valueMap["MSG_TR_CLASS"] = (msg.seen)?"TM_mailLow":"TM_unseenLow";
		valueMap["MSG_FROM_EMAIL"] = msg.fromEscape;
		valueMap["MSG_TO_EMAIL"] = msg.toEscape;
		valueMap["MSG_F_FOLDERNAME"] = folderNameVal;
		valueMap["MSG_FOLDER_NAME"] = folderNameVal;
		valueMap["MSG_FLAG_CONTENTS"] = this.getFlagContents(msg.flag, mtr_id);
		valueMap["MSG_SAVE_TYPE"] = this.getFromNoteFolder(msg.flag, isSaveFolder);
		var msubject = jQuery.trim(msg.preview);		
		msubject = (msubject != "")?escape_tag(msubject):comMsg.note_msg_060;
		valueMap["MSG_SUBJECT"] = msubject;
		valueMap["MSG_DATE"] = (isSentFolder)?msg.sentDate:this.getSaveNoteDate(msg);
		valueMap["MSG_PRIORITY"] = (msg.priority != 3)?templates.priorityImage:"";
		
		if(!isSentFolder){
			valueMap["MSG_READMESSAGE"] = "onclick=\"readNote('"+msg.folderName+"','"+msg.id+"')\"";
			valueMap["MSG_P_EMAIL"] = msg.fromToSimple;
			valueMap["MSG_PF_EMAIL"] = msg.fromEscape;
			valueMap["MSG_P_EMAIL_SUB_ID"] = "from_"+idx;
			valueMap["MSG_ADDR_SUBMENU"] = "onclick=\"writeToNote('"+msg.fromEscape+"')\"";
		} else {
			valueMap["MSG_READMESSAGE"] = "onclick=\"readNote('"+msg.folderName+"','"+msg.id+"')\"";
			valueMap["MSG_P_EMAIL"] = msg.sendToSimple;
			valueMap["MSG_PF_EMAIL"] = msg.toEscape;
			valueMap["MSG_P_EMAIL_SUB_ID"] = "to_"+idx;
			valueMap["MSG_ADDR_SUBMENU"] = "onclick=\"writeToNote('"+msg.toEscape+"')\"";
			valueMap["MSG_RECEIVE_NOTI_ID"] = "mdn_"+mtr_id;
		}
		
		var list = RendererReplaceValue(listContentStr,valueMap);
		valueMap = templates = null;
		return list;
	},
	getFlagContents:function(flagStr,mid){
		var flagContents = new StringBuffer();				
		try{
			flagContents.append("<span id='"+mid+"_readF'");
			if(flagStr.indexOf("S") > -1){
				flagContents.append(" class='noteFlagSE' ");
			} else {
				flagContents.append(" class='noteFlagUNSE' ");
			}
			
			flagContents.append("></span>");
			
			return flagContents.toString();
		} finally{
			flagContents = null;
		}
	},
	getFromNoteFolder : function(flagStr, isSaveFolder) {
		var fromFolder = "";
		
		if (isSaveFolder) {
			fromFolder = "["+comMsg.note_msg_043+"]";
			if(flagStr.indexOf("F") > -1){
				fromFolder = "["+comMsg.note_msg_044+"]";
			}
		}
		return fromFolder;
	},
	getSaveNoteDate : function(msg) {
		var date = msg.date;
		if(msg.flag.indexOf("F") > -1){
			date = msg.sentDate;
		}
		return date;
	}
});

var RendererReplaceValue = function(str,valueMap){		
	return str.replace(/{([^{}]*)}/g, function (a, b) {
		 var r = valueMap[b];
           return r;
   });
};