var RendererReplaceValue = function(str,valueMap){		
	return str.replace(/{([^{}]*)}/g, function (a, b) {
		 var r = valueMap[b];
		 if (r == undefined) return a;
           return r;
   });
};

var MailListControl = Class.create({	
	initialize: function(){	
		this.cashMailSize = 0;
		this.cashMailMode = "N";
		this.cashMailListtemplate = "";
		
		this.templateSortMsg = new HashMap();
		this.templateSortMsg.put("from",mailMsg.mail_from);
		this.templateSortMsg.put("to",mailMsg.mail_to);
		this.templateSortMsg.put("subj",mailMsg.mail_subject);
		this.templateSortMsg.put("arrivalnormal",mailMsg.mail_receivedate);
		this.templateSortMsg.put("arrivalsent",mailMsg.mail_senddate);
		this.templateSortMsg.put("arrivaldraft",mailMsg.mail_writedate);
		this.templateSortMsg.put("arrivalreserved",mailMsg.mail_reserveddate);
		this.templateSortMsg.put("size",mailMsg.mail_size);
		this.eventList = [];
		this.previewList = [];
		this.previewEventList = [];
		this.tagList = [];
		this.flagList = [];
		this.folderType = "inbox";
		this.viewMode = "n";
		this.enableCcView=false;
		this.templates = {
				
			commonWrapper:"<form name='listForm' id='listForm'><input type='hidden' name='allSearchSelectCheck' id='allSearchSelectCheck' value='off'/><div class='TM_listWrapper' style='position:relative;'><table class='TM_b_list' id='msgListHeader'>",
			HHeaderNCols:"<col width='40px'></col><col width='{CC_MODE}' ></col>{FOLDER_NAME_INFO_COLS}<col width='80px'></col><col ></col><col width='20px'></col><col width='110px'></col><col width='70px'></col>",
			HHeaderECols:"<col width='40px'></col><col width='55px' ></col><col ></col><col width='20px'></col><col width='80px'></col><col width='110px'></col><col width='70px'></col>",
			VheaderCols:"<col width='40px'></col><col></col>",
			VBodyCols:"<col width='40px'></col><col width='{CC_MODE}' ></col><col ></col>",
			
			commonHeaderWrapper:"<tr>",
			commonHeaderNoneCols:"<th scope='col'>&nbsp;</th>",				
			
			HHeaderStart:"<th scope='col'><input type='checkbox' id='allChk' onclick=\"allCheckMessage(listForm.msgId,this.checked)\"></th><th scope='col'>"+mailMsg.mail_kind+"</th>{FOLDER_NAME_INFO_TH}",				                      
			HHeaderRepeater:"<th scope='col'><a href='javascript:;'  onclick=\"sortMessage('{SORT_VALUE}','{SORT_DIR}')\" {SORT_CLASS}>{SORT_IMAGE}{SORT_INDEX}</a></th>",
			
			VHeaderStart:"<th><input type='checkbox'  id='allChk' onclick=\"allCheckMessage(listForm.msgId,this.checked)\"></th>",
			VHeaderContent:"<th style='text-align: left'><div style='float:left;'><div id='sortMsgSelectBox'></div></div><div style='float:left;padding-top:3px;'><a href='javascript:;' onclick=\"sortMessage('{SORT_VALUE}','{SORT_DIR}')\" class='sortSelectItem'>{SORT_IMAGE}{SORT_INDEX}</a></div><div class='cls'></div></th>",
			
			commonHeaderWrapperEnd:"</tr></table></div>",
			
			commonBodyWrapperStart:"<div id='m_msgListWrapper' {MSG_WRAPPER_STYLE}><div id='m_msgListContent' class='TM_listWrapper'><table id='m_messageList'>",			
			commonBodyWrapper:"<tr id='{MSG_TR_ID}' class='{MSG_TR_CLASS}' {MSG_TR_MAKEDRAG}>",
			commonBodyCheckPart:"<td class='TM_chkTd'><input type='checkbox'  name='msgId' id='chk_{MSG_FOLDER}_{MSG_ID}' value='{MSG_ID}' femail='{MSG_FROM_EMAIL}' temail='{MSG_TO_EMAIL}' mailsize='{MSG_BYTE_SIZE}'/></td>",
			commonBodyFlagPart:"<td class='TM_list_flag'>{MSG_FLAG_CONTENTS}</td>",
			commonBodyEmptyPart:"<td>&nbsp;</td>",
			commonBodyPopupPart:"<td><a href='javascript:;' id='{MSG_TR_ID}_popupLink' type='popup' {MSG_READPOPUPMESSAGE}><img src='/design/common/image/blank.gif' class='popupReadIcon' width='13' height='12' title='"+mailMsg.mail_popupview+"'></a>&nbsp;</td>",
			
			HBodyFolderNamePart:"<td class='TM_list_from' title='{MSG_F_FOLDERNAME}'>{MSG_FOLDER_NAME}</td>",
			HBodySubjectPart:"<td class='TM_list_subject' id='m_s_{MSG_FOLDER}_{MSG_ID}' {MSG_READMESSAGE}> {MSG_PRIORITY} <a href='javascript:;' class='msubject' type='subject' id='msg_subject_{MSG_FOLDER}_{MSG_ID}' {MSG_OVEROUTPREVIEW}>{MSG_SUBJECT}</a></td>",
			HBodyEmailPart:"<td class='TM_list_from' title='{MSG_PF_EMAIL}'><div class='TM_list_from' nowrap><a href='javascript:;' id='m_a_{MSG_TR_ID}' type='addr' {MSG_ADDR_SUBMENU}>{MSG_P_EMAIL}</a></div><div id='{MSG_P_EMAIL_SUB_ID}'></div></td>",			
			HBodyDatePart:"<td class='TM_list_Date_Size' align='right'>{MSG_DATE}</td>",
			HBodySizePart:"<td class='TM_list_size'>{MSG_SIZE}</td>",		
			
			VBodyContentTableStart:"<table class='TM_VMsgContent' cellpadding='0' cellspacing='0' border='0' width='100%'><col></col><col></col><col width='20px'></col><tr class='TM_VLowwrapper'>",
			VBodyContentTableEnd:"</table>",			
			
			VBodyFolderNamePart:"<div class='TM_list_from TM_mlist_c_wrapper' title='{MSG_F_FOLDERNAME}' type='addr' {MSG_ADDR_SUBMENU}>{MSG_FOLDER_NAME}<div>",
			VBodySubjectSPart:"<td class='TM_list_subject' {MSG_READMESSAGE} nowrap>",
			VBodySubjectCPart:
					"<div class='TM_mlist_s_wrapper' id='m_s_{MSG_FOLDER}_{MSG_ID}'>{MSG_PRIORITY} <a href='javascript:;' class='msubject' type='subject' " +					
					"id='msg_subject_{MSG_FOLDER}_{MSG_ID}' {MSG_OVEROUTPREVIEW}>{MSG_SUBJECT}</a>" +
					"</div>",
			VBodySubjectEPart:"</td>",
			VBodyEmailPart:"<div class='TM_list_from TM_mlist_c_wrapper'  title='{MSG_PF_EMAIL}' id='m_a_{MSG_TR_ID}'><div class='TM_list_from' nowrap><a href='javascript:;' id='m_a_{MSG_TR_ID}' type='addr' {MSG_ADDR_SUBMENU}>{MSG_P_EMAIL}</a></div><div id='{MSG_P_EMAIL_SUB_ID}'></div>",						
			VBodyDateSizePart:"<td class='TM_list_Date_Size' align='right' nowrap>{MSG_DATE}</td>",			
			
			commonBodyWrapperEnd:"</tr>",
			commonWrapperEnd:"</table></div></div><div id='pageBottomNavi' class='pageNavi'></div></form>",
			commonWrapperEndNoPaging:"</table></div></div></form>",
			sortUpImage:"<img src='/design/common/image/icon/ic_bullet_up.gif' class='sortImg'>",
			sortDownImage:"<img src='/design/common/image/icon/ic_bullet_down.gif' class='sortImg'>",
			priorityImage:"<img src='/design/common/image/ic_import.gif'>"
		};
	},
	makeList:function(drowId,listData){		
		debugInfo("LIST_DROW_START");
		priviewList = [];
		this.eventList = [];
		this.previewList = [];
		this.tagList = [];
		this.flagList = [];
		this.previewEventList = [];
		this.folderType = listData.folderType;
		this.viewMode = listData.viewMode;
		this.enableCcView=listData.enableCcview;
		
		var sb = new StringBuffer();
		var templates = this.templates;
		sb.append(templates.commonWrapper);
		var isHmode = (listData.viewMode == "h" || listData.viewMode == "n");
		if(isHmode){
			sb.append(this.drowHmodeList(listData));
		} else {
			sb.append(this.drowVmodeList(listData));
		}
		if(listData.messageList.length>0){
			sb.append(templates.commonWrapperEnd);
		}else{
			sb.append(templates.commonWrapperEndNoPaging);
		}
		
		$(drowId).innerHTML = sb.toString();
		//this.eventFuncApply();
		
		if(!isHmode){
			var folderType = listData.folderType;
			var dateIndex = mailMsg.mail_receivedate;
			var isExceptFolder = (folderType == 'sent' || 
					folderType == 'draft' || 
					folderType == 'reserved')?true:false;
			
			var sortListArray = [];
			sortListArray.push({index:mailMsg.mail_sortselect,value:""});
			sortListArray.push({index:mailMsg.mail_subject,value:"subj"});
			if(isExceptFolder){
				sortListArray.push({index:mailMsg.mail_to,value:"to"});
			} else {
				sortListArray.push({index:mailMsg.mail_from,value:"from"});
			}
			
			if(folderType == "sent"){
				dateIndex = mailMsg.mail_senddate;
			} else if(folderType == "draft"){
				dateIndex = mailMsg.mail_writedate;
			} else if(folderType == "reserved"){
				dateIndex = mailMsg.mail_reserveddate;
			}
			sortListArray.push({index:dateIndex,value:"arrival"});			
			sortListArray.push({index:mailMsg.mail_size,value:"size"});
			
			jQuery("#sortMsgSelectBox").selectbox({selectId:"sortMsgSelect",
				selectFunc:sortMessageSelect},"",sortListArray);
		}		
		debugInfo("LIST_DROW_END");
		sb.destroy();
		loadMessageListPage(listData);
	},
	drowHmodeList:function(listData){		
		var sb = new StringBuffer();
		var templates = this.templates;
		var mlist = listData.messageList;
		var folderType = listData.folderType;
		var isAllFolder = (folderType == "all")?true:false;
		var isExceptFolder = (folderType == 'sent' || folderType == 'draft' || folderType == 'reserved')?true:false;
		var isSentFolder = (folderType == 'sent')?true:false;
		
		var listAllHeaderCols = "";
		var listAllHeaderContent = "";
		var currentModeFlag = "HN";
		var colsCtn = 7;
		var listColsContent;
		if(!isExceptFolder){					
			if(isAllFolder){
				listAllHeaderCols = "<col width='90px' ></col>";
				listAllHeaderContent = "<th scope='col'>"+mailMsg.mail_folder+"</th>";
				colsCtn = 8;
				currentModeFlag = "HA";
			}
			listColsContent = replaceAll(templates.HHeaderNCols,"\{FOLDER_NAME_INFO_COLS\}",listAllHeaderCols);
			var ccModeColSize = this.enableCcView?"70px":"55px";
			listColsContent =  replaceAll(listColsContent,"\{CC_MODE\}",ccModeColSize);
		} else {			
			listColsContent = templates.HHeaderECols;			
			currentModeFlag = "HE";
		}
		sb.append(listColsContent);
		sb.append(templates.commonHeaderWrapper);
		sb.append(replaceAll(templates.HHeaderStart,"\{FOLDER_NAME_INFO_TH\}",listAllHeaderContent));
		
		var sortBy = listData.sortBy;
		var sortDir = listData.sortDir;		
		if(!isExceptFolder){
			sb.append(this.drowHSortItem(sortBy,sortDir,"from"));
			sb.append(this.drowHSortItem(sortBy,sortDir,"subj"));
			sb.append(templates.commonHeaderNoneCols);
			sb.append(this.drowHSortItem(sortBy,sortDir,"arrival","normal"));
		} else {
			sb.append(this.drowHSortItem(sortBy,sortDir,"subj"));
			sb.append(templates.commonHeaderNoneCols);
			sb.append(this.drowHSortItem(sortBy,sortDir,"to"));
			sb.append(this.drowHSortItem(sortBy,sortDir,"arrival",folderType));
		}		
		sb.append(this.drowHSortItem(sortBy,sortDir,"size"));
		sb.append(templates.commonHeaderWrapperEnd);
		
		sb.append(replaceAll(templates.commonBodyWrapperStart,"\{MSG_WRAPPER_STYLE\}","style='position:relative'"));
		sb.append(listColsContent);
		if(mlist.length > 0){			
			var isReflash = false;
			var listContentBuffer;
			var listContentStr;
			
			if((this.cashMailSize != mlist.length) ||
					currentModeFlag != this.cashMailMode){
				listContentBuffer = new StringBuffer();
				listContentBuffer.append(templates.commonBodyWrapper);
				listContentBuffer.append(templates.commonBodyCheckPart);
				listContentBuffer.append(templates.commonBodyFlagPart);
				
				if(!isExceptFolder){
					if(isAllFolder){
						listContentBuffer.append(templates.HBodyFolderNamePart);
					}
					listContentBuffer.append(templates.HBodyEmailPart);
					listContentBuffer.append(templates.HBodySubjectPart);					
					if(folderType != 'quotaviolate'){
						listContentBuffer.append(templates.commonBodyPopupPart);
					}else {
						listContentBuffer.append(templates.commonBodyEmptyPart);
					}
				} else {
					listContentBuffer.append(templates.HBodySubjectPart);
					if(isSentFolder) listContentBuffer.append(templates.commonBodyPopupPart);
					else listContentBuffer.append(templates.commonBodyEmptyPart);
					listContentBuffer.append(templates.HBodyEmailPart);
				}
				listContentBuffer.append(templates.HBodyDatePart);
				listContentBuffer.append(templates.HBodySizePart);
				listContentBuffer.append(templates.commonBodyWrapperEnd);
				listContentStr = listContentBuffer.toString();
				this.cashMailMode = currentModeFlag;
				this.cashMailSize = mlist.length;
				this.cashMailListtemplate = listContentStr;
			} else {
				listContentStr = this.cashMailListtemplate;
			}		
			//var cdate = new Date();
			//jQuery("#logger").append(jQuery("<span>").append("makeListStart["+cdate.getMinutes()+":"+cdate.getSeconds()+":"+cdate.getMilliseconds()+"]"));
						
			for ( var i = 0; i < mlist.length; i++) {
				sb.append(this.listContentDrow("H",i,isExceptFolder, folderType, mlist[i], listContentStr));			
			}			
		} else {			
			sb.append("<tr><td colspan='"+colsCtn+"'>"+mailMsg.mail_nomessage+"</td></tr>");
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
	drowHSortItem:function(sortBy,sortDir,item,folderType){		
		var templates = this.templates;
		var sortImg = "";
		var sortMethod = "desc";
		var sortClass = "";
		var sortIndex = "";
		if(item == sortBy){
			sortClass = "class='sortSelectItem'";
			if(sortDir == "asce"){
				sortImg = templates.sortUpImage;				
			} else {
				sortImg = templates.sortDownImage;
				sortMethod = "asce";
			}
		}
		
		if(folderType){
			sortIndex = this.templateSortMsg.get(item+folderType);
		} else {			
			sortIndex = this.templateSortMsg.get(item);
		}
		
		var sortContent = templates.HHeaderRepeater;
		var valueMap = [];
		valueMap["SORT_VALUE"] = item;
		valueMap["SORT_CLASS"] = sortClass;		
		valueMap["SORT_DIR"] = sortMethod;
		valueMap["SORT_IMAGE"] = sortImg;
		valueMap["SORT_INDEX"] = sortIndex;		
		sortContent = RendererReplaceValue(sortContent,valueMap);
		return sortContent;
	},
	drowVmodeList:function(listData){
		var sb = new StringBuffer();
		var templates = this.templates;
		var mlist = listData.messageList;
		var folderType = listData.folderType;
		var isAllFolder = (folderType == "all")?true:false;
		var isExceptFolder = (folderType == 'sent' || folderType == 'draft' || folderType == 'reserved')?true:false;
		var isSentFolder = (folderType == 'sent')?true:false;
		var sortBy = listData.sortBy;
		var sortDir = listData.sortDir;
		var currentModeFlag = "VN";
		var colsCtn = 3;
		
		sb.append(templates.VheaderCols);
		sb.append(templates.commonHeaderWrapper);
		sb.append(templates.VHeaderStart);
		if(!isExceptFolder){
			if(isAllFolder){
				currentModeFlag = "VA";
			}
			sb.append(this.drowVSortItem(sortBy,sortDir));
		} else {
			sb.append(this.drowVSortItem(sortBy,sortDir,folderType));
			currentModeFlag = "VE";
		}
		sb.append(templates.commonHeaderWrapperEnd);		
		
		sb.append(replaceAll(templates.commonBodyWrapperStart,"\{MSG_WRAPPER_STYLE\}",""));
		var ccModeColSize = isExceptFolder?"50px":this.enableCcView?"65px":"50px";
		sb.append(replaceAll(templates.VBodyCols,"\{CC_MODE\}",ccModeColSize));
		if(mlist.length > 0){			
			var isReflash = false;
			var listContentBuffer;
			var listContentStr;
			
			if((this.cashMailSize != mlist.length) ||
					currentModeFlag != this.cashMailMode){
				listContentBuffer = new StringBuffer();
				listContentBuffer.append(templates.commonBodyWrapper);
				listContentBuffer.append(templates.commonBodyCheckPart);
				listContentBuffer.append(templates.commonBodyFlagPart);
				listContentBuffer.append(templates.VBodySubjectSPart);		
				
				if(!isExceptFolder){
					listContentBuffer.append(templates.VBodyContentTableStart);
					listContentBuffer.append("<td width='100%'>");
					if(isAllFolder){
						listContentBuffer.append(templates.VBodyFolderNamePart);
					}					
					listContentBuffer.append(templates.VBodyEmailPart);
					listContentBuffer.append("</td>");
					listContentBuffer.append(templates.VBodyDateSizePart);
					if(folderType != 'quotaviolate'){						
						listContentBuffer.append(templates.commonBodyPopupPart);
					}else {
						listContentBuffer.append(templates.commonBodyEmptyPart);
					}
					listContentBuffer.append(templates.VBodyContentTableEnd);					
					listContentBuffer.append(templates.VBodySubjectCPart);
					listContentBuffer.append(templates.VBodySubjectEPart);
					
				} else {
					listContentBuffer.append(templates.VBodyContentTableStart);
					listContentBuffer.append("<td width='100%'>");
					listContentBuffer.append(templates.VBodyEmailPart);
					listContentBuffer.append("</td>");
					listContentBuffer.append(templates.VBodyDateSizePart);
					
					if(isSentFolder)listContentBuffer.append(templates.commonBodyPopupPart);
					else listContentBuffer.append(templates.commonBodyEmptyPart);
					
					listContentBuffer.append(templates.VBodyContentTableEnd);
					listContentBuffer.append(templates.VBodySubjectCPart);
					listContentBuffer.append(templates.VBodySubjectEPart);
					
				}				
				listContentBuffer.append(templates.commonBodyWrapperEnd);
				listContentStr = listContentBuffer.toString();
				this.cashMailMode = currentModeFlag;
				this.cashMailSize = mlist.length;
				this.cashMailListtemplate = listContentStr;
			} else {
				listContentStr = this.cashMailListtemplate;
			}
						
			for ( var i = 0; i < mlist.length; i++) {
				sb.append(this.listContentDrow("V",i,isExceptFolder, folderType, mlist[i], listContentStr));			
			}			
		} else {			
			sb.append("<tr><td colspan='"+colsCtn+"'>"+mailMsg.mail_nomessage+"</td></tr>");
			if(jQuery.browser.safari){
			sb.append("<tr><td class='none'></td><td class='none'>" +
					"</td><td class='none'></td></tr>");
			}
		}
		
		var content = sb.toString();
		sb.destroy();
		return content;
		
	},
	drowVSortItem:function(sortBy,sortDir,folderType){
		var templates = this.templates;
		var sortImg = "";
		var sortMethod = "desc";
		var sortClass = "class='sortSelectItem'";
		var sortIndex = "";
		
		if(sortDir == "desc"){
			sortImg = templates.sortDownImage;
			sortMethod = "asce";
		} else {
			sortImg = templates.sortUpImage;	
		}	
		
		if(folderType){
			sortIndex = this.templateSortMsg.get((sortBy == "arrival")?sortBy+folderType:sortBy);
		} else {			
			sortIndex = this.templateSortMsg.get((sortBy == "arrival")?"arrivalnormal":sortBy);
		}
		
		var sortContent = templates.VHeaderContent;
		var valueMap = [];
		valueMap["SORT_VALUE"] = sortBy;
		valueMap["SORT_CLASS"] = sortClass;		
		valueMap["SORT_DIR"] = sortMethod;
		valueMap["SORT_IMAGE"] = sortImg;
		valueMap["SORT_INDEX"] = sortIndex;		
		sortContent = RendererReplaceValue(sortContent,valueMap);
		return sortContent;		
	},
	listContentDrow:function(mode,idx, isExceptFolder, folderType, msg, listContentStr){
		var templates = this.templates;
		var folderNameVal = getFolderNameEscape(msg.folderName);
		var valueMap = [];		
		var mtr_id = folderNameVal +"_"+msg.id;
		//this.eventList.push({id:mtr_id,ename:"mouseover",func:""});
		var isSentFolder = (folderType == 'sent')?true:false;
		
		valueMap["MSG_TR_MAKEDRAG"] = "";
		valueMap["MSG_TR_ID"] = mtr_id;
		valueMap["MSG_ID"] = msg.id;
		valueMap["MSG_FOLDER"] = folderNameVal;
		valueMap["MSG_TR_CLASS"] = (msg.seen)?"TM_mailLow":"TM_unseenLow";
		valueMap["MSG_FROM_EMAIL"] = msg.fromEscape;
		valueMap["MSG_TO_EMAIL"] = msg.toEscape;
		valueMap["MSG_BYTE_SIZE"] = msg.byteSize;
		valueMap["MSG_FLAG_CONTENTS"] = this.getFlagContents(msg.flag,mtr_id,folderType,isExceptFolder?"":msg.isCcMe);
		if(!isExceptFolder || isSentFolder){
			valueMap["MSG_TAG"] = msg.tagNameList;
			this.tagList.push({id:"m_s_"+mtr_id,mid:mtr_id,tag:msg.tagNameList});			
		}
		
		valueMap["MSG_F_FOLDERNAME"] = this.getFolderName(msg.folderFullName);
		valueMap["MSG_FOLDER_NAME"] = this.getFolderName(msg.folderDepthName);
		var msubject = jQuery.trim(msg.subject);		
		msubject = (msubject != "")?escape_tag(msubject):mailMsg.header_nosubject;
		valueMap["MSG_SUBJECT"] = (msg.spamRate < 0 && !isExceptFolder)?"<strike>"+msubject+"<strike>":msubject;
		valueMap["MSG_DATE"] = (folderType == "sent")?msg.sentDate:msg.date;		
		valueMap["MSG_SIZE"] = msg.size;
		valueMap["MSG_PRIORITY"] = (msg.priority != 3)?templates.priorityImage:"";
		
		
		if(!isExceptFolder){
			if(folderType != 'quotaviolate'){								
				valueMap["MSG_READMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
				if(mode == "V") valueMap["MSG_ADDR_SUBMENU"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"  addr=\""+msg.fromEscape+"\"";
			}
			valueMap["MSG_READPOPUPMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
			valueMap["MSG_P_EMAIL"] = msg.fromToSimple;
			valueMap["MSG_PF_EMAIL"] = msg.fromEscape;
			valueMap["MSG_P_EMAIL_SUB_ID"] = "from_"+idx;
			if(mode == "H") valueMap["MSG_ADDR_SUBMENU"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"  addr=\""+msg.fromEscape+"\"";
		} else {
			if(folderType != "draft"){
				valueMap["MSG_READPOPUPMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
				valueMap["MSG_READMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
				if(mode == "V") valueMap["MSG_ADDR_SUBMENU"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"  addr=\""+msg.toEscape+"\"";
			} else {				
				valueMap["MSG_READMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
				if(mode == "V") valueMap["MSG_ADDR_SUBMENU"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"  addr=\""+msg.toEscape+"\"";
			}
			if(isSentFolder)
				valueMap["MSG_READPOPUPMESSAGE"] = "fname=\""+msg.folderName+"\" uid=\""+msg.id+"\" idx=\""+idx+"\"";
			
			valueMap["MSG_P_EMAIL"] = msg.sendToSimple;
			valueMap["MSG_PF_EMAIL"] = msg.toEscape;
			valueMap["MSG_P_EMAIL_SUB_ID"] = "to_"+idx;
			if(mode == "H") valueMap["MSG_ADDR_SUBMENU"] = "idx="+idx+" addr=\""+msg.toEscape+"\"";
		}
		
		valueMap["MSG_OVEROUTPREVIEW"] = "mtr_id=\""+mtr_id+"\" privIdx=\""+priviewList.length+"\"";
		
		if(jQuery.trim(msg.preview) != ""){
			priviewList.push(msg.preview);
			this.previewList.push({id:"m_s_"+mtr_id,content:"<span id='msg_subject_"+mtr_id+"_priview'>&nbsp; -"+msg.preview+"-</span>"});
		} else {
			priviewList.push("");
		}
		var list = RendererReplaceValue(listContentStr,valueMap);
		valueMap = templates = null;
		return list;
		
	},
	getFolderType:function() {
		return this.folderType;
	},
	getViewMode:function() {
		return this.viewMode;
	},
	getFolderName:function(folderName){
		if(folderName == "Inbox"){
			folderName = mailMsg.folder_inbox;
		} else if(folderName == "Sent"){
			folderName = mailMsg.folder_sent;
		} else if(folderName == "Drafts"){
			folderName = mailMsg.folder_drafts;
		} else if(folderName == "Reserved"){
			folderName = mailMsg.folder_reserved;
		} else if(folderName == "Spam"){
			folderName = mailMsg.folder_spam;
		} else if(folderName == "Trash"){
			folderName = mailMsg.folder_trash;
		}
		
		return folderName;
		
	},	
	eventFuncApply:function(){
		try{
		var elist = this.eventList;
		for ( var i = 0; i < elist.length; i++) {
			addEventAttach(elist[i].id,elist[i].ename,elist[i].func,(elist[i].exceptObj)?elist[i].exceptObj:"");
		}
		this.eventList = null;
		}catch(e){
			this.eventList = null;
		}
	},
	updatePreview:function(){
		try{
		var plist = this.previewList;
		var pelist = this.previewEventList;		
		for ( var i = 0; i < plist.length; i++) {
			jQuery("#"+plist[i].id).append(jQuery(plist[i].content));
			addEventAttach(pelist[i].id,pelist[i].ename,pelist[i].func);
		}
		this.previewList = null;
		this.previewEventList = null;
		}catch(e){
			this.previewList = null;
			this.previewEventList = null;
		}
	},
	updateTag:function(func){
		try{
			var tlist = this.tagList;		
			for ( var i = 0; i < tlist.length; i++) {
				jQuery("#"+tlist[i].id).printTag(func,tlist[i].mid,tlist[i].tag);
			}
			this.tagList = null;
		}catch(e){
			this.tagList = null;
		}
	},
	getFlagContents:function(flagStr,mid,ftype,isCcMe){
		var flagContents = new StringBuffer();				
		try{
			if(flagStr.indexOf("F") > -1){
				flagContents.append("<span class='flagON' id='"+mid+"_flagedF' type='flag' mid='"+mid+"' flaged = 'false' "+ ((ftype != "shared") ? "" : "")+"></span>");
			} else {
				flagContents.append("<span class='flagOFF' id='"+mid+"_flagedF' type='flag' mid='"+mid+"' flaged = 'true' "+ ((ftype != "shared") ? "" : "")+"></span>");
			}
			
			/*
			if(ftype != "shared"){
				this.eventList.push({id:mid+"_flagedF",ename:"click",func:"switchFlagFlaged(['"+mid+"'],'F','"+mid+"_flagedF')"});
			}
			*/		
			flagContents.append("<span id='"+mid+"_readF'");
			if(flagStr.indexOf("A") > -1){
				flagContents.append(" class='flagRE' ");
			} else if(flagStr.indexOf("C") > -1){			
				flagContents.append(" class='flagFW' ");
			} else if(flagStr.indexOf("S") > -1){
				flagContents.append(" class='flagSE' ");
			}else {
				flagContents.append(" class='flagUNSE' ");
			}
			
			flagContents.append("></span>");
			if(this.enableCcView && isCcMe){
				flagContents.append("<img src='/design/common/image/icon/icon_cc.gif' style='float:left;padding-left:2px;'></img>");
			}
			
			if(flagStr.indexOf("T") > -1){
				flagContents.append("<span class='flagAT'></span>");			
			}
			
			return flagContents.toString();
		} finally{
			flagContents = null;
		}
	}
		
});


var MailReadControl = Class.create({
	initialize: function(){	
		this.templates = {
			commonBodyStart:"<div class='TM_content_wrapper' id='readWrapper'><form name='readMessageForm' id='readMessageForm'><input type='hidden' id='chk_{MSG_UID}' femail='{MSG_H_FROM}' temail='{MSG_H_TO}'/><div class='TM_mail_content'>",
			readHeaderStart:"<table cellpadding='0' cellspacing='0' border='0' class='TM_r_table'><col width='80px'></col><col></col>",
			readHeaderSubject:"<tr><td colspan='2' class='TM_r_subject'><span id='msg_subject_{MSG_UID}'>{MSG_SUBJECT}</span>&nbsp;"+
							"<a id='mailFromIpBtn' href='javascript:;' class='btn_basic' onclick='viewMailFromIp(\"{MSG_FNAME_ENC}\",\"{MSG_UID}\");' style='display:none;'><span class='geoipSpan'>"+mailMsg.mail_geoip_title+"</span></a>&nbsp;"+
							"<a href='javascript:;' onclick='viewSource(\"{MSG_FNAME_ENC}\",\"{MSG_UID}\")'>" +
							"<img src='/design/common/image/blank.gif' class='TM_r_source' title='"+mailMsg.mail_sourceview+"' align='absmiddle'></a>" +
							"&nbsp; &nbsp;<a href='javascript:;' onclick='viewRelationMsg(\"{MSG_FNAME_ENC}\",\"{MSG_UID}\")'><img src='/design/common/image/icon/ic_arrow_sort.gif' id='btnRelation' align='absmiddle'> "+mailMsg.mail_relation+"</a><div id='relationMsgPane' class='TM_relation_list'></div>" +
							"</td></tr>",
			readHeaderContent:"<tr><td class='TM_rh_index'>" +
							"<div style='float:left;padding-right:3px;'><a href='#none' onclick='toggleHeaderInfo()'>" +
							"<img src='/design/common/image/blank.gif' align='absmiddle' id='btnRcptInfo' class='closeRcptBtn'></a></div> "+mailMsg.mail_from+" :</td>" +
							"<td class='TM_rh_content'>" +
							"<a href='javascript:;'  onclick=\"writeMessage('{MSG_I_FROM}')\">{MSG_I_FROM}</a>",
			readHeaderFromAddAddr:"<a href='javascript:;' onclick='addAddr(\"from\")' class='TM_rh_func'>"+mailMsg.mail_addradd+"</a><input type='hidden' name='fromAddAddr'  value='{MSG_I_FROM}'></td></tr>",
			readHeaderEnd:"</table>",
			
			readExHeaderStart:"<table cellpadding='0' cellspacing='0' border='0' class='TM_r_table' id='rmsg_info' style='display:none'><col width='80px'></col><col></col>",
			
			readExHeaderDate:"<tr><td class='TM_rh_index_ex'>"+mailMsg.mail_senddate+" :</td><td class='TM_rh_content' valign='top'>{MSG_R_DATE}</td></tr>",
			
			readExHeaderToStart:"<tr><td class='TM_rh_index_ex' valign='top'>"+mailMsg.mail_to+" :</td><td class='TM_rh_content' valign='top'>",
			readExHeaderToContent:"<a href='javascript:;' onclick=\"writeMessage(\'{MSG_I_TO}\')\")'>{MSG_I_TO}</a><input type='hidden' name='toAddAddr'  value='{MSG_I_TO}'>",
			readExHeaderToAddAddr:"<a href='javascript:;' onclick='addAddr(\"to\")' class='TM_rh_func'>"+mailMsg.mail_addradd+"</a>",
			readExHeaderToEnd:"</td></tr>",
			
			readExHeaderCcStart:"<tr><td class='TM_rh_index_ex' valign='top'>"+mailMsg.mail_cc+" :</td><td class='TM_rh_content' valign='top'>",
			readExHeaderCcContent:"<a href='javascript:;' onclick=\"writeMessage(\'{MSG_I_CC}\')\">{MSG_I_CC}</a><input type='hidden' name='ccAddAddr'  value='{MSG_I_CC}'>",
			readExHeaderCcAddAddr:"<a href='javascript:;' onclick='addAddr(\"cc\")' class='TM_rh_func'>"+mailMsg.mail_addradd+"</a>",
			readExHeaderCcEnd:"</td></tr>",
			
			readExHeaderBccStart:"<tr><td class='TM_rh_index_ex' valign='top'>"+mailMsg.mail_bcc+" :</td><td class='TM_rh_content' valign='top'>",
			readExHeaderBccContent:"<a href='javascript:;' onclick=\"writeMessage(\'{MSG_I_BCC}\')\">{MSG_I_BCC}</a><input type='hidden' name='bccAddAddr'  value='{MSG_I_BCC}'>",
			readExHeaderBccAddAddr:"<a href='javascript:;' onclick='addAddr(\"bcc\")' class='TM_rh_func'>"+mailMsg.mail_addradd+"</a>",
			readExHeaderBccEnd:"</td></tr>",
			
			readExHeaderEnd:"</table>",
			
			readAttachBodyStart:"<table cellpadding='0' cellspacing='0' border='0' class='TM_r_atable'><col></col><col width='150px'></col>",
			readAttachIndex:"<tr><td class='TM_ra_l'>{MSG_ATTACH_C_INDEX}</td><td class='TM_ra_r'>{MSG_ATTACH_FUNC_BTN}</td></tr>",
			readAttachFileStart:"<tr><td id='attachList' class='TM_ra_c' colspan='2'>",			
			readAttachFileBtn:"<a href='javascript:;' class='btn_basic' id='attSaveAllBtn'  allpart='{ATTCH_ALL}' onclick='downloadAllAttach(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\")'><span>"+
							mailMsg.mail_saveall+"</span></a>"+
							"<a href='javascript:;' class='btn_basic' onclick='toggleAttachInfo()'><span>"+mailMsg.mail_viewlist+"</span></a>",			
			readAttachFileEnd:"</td></tr>",			
			readAttachExFuncStart:"<tr><td class='TM_ra_n' colspan='2'>",
			
			readAttachExFuncRule:"<img src='/design/common/image/blank.gif' class='TM_ra_nic' align='absmiddle'>SPAM RATE[<span class='TM_ra_rateval'>{MSG_SPAM_RATE}</span>] - ",
			readAttachExFuncRuleLink:"<a href='javascript:;' class='btn_basic' onclick='registBayesianRuleMessage(\"{RULE_TYPE}\",\"{MSG_FNAME_FULL}\",\"{MSG_UID}\")'><span>{RULE_INDEX}</span></a>",
			readAttachExFuncHiddenImg:"<img src='/design/common/image/blank.gif' class='TM_ra_nic' align='absmiddle'>"+mailMsg.mail_noimage+
							"<div class='func_link'>"+
							"<a href='javascript:;' onclick='readViewImg(\"{MSG_FNAME_ENC}\",\"{MSG_UID}\");' >"+
							mailMsg.mail_viewimage+
							"</a> <span id='view_setting'>| <a href='/setting/viewSetting.do' >"+mailMsg.mail_setting+"</a></span>"+
							"</div>",
			readAttachExFuncIntegrity:"<img src='/design/common/image/blank.gif' class='TM_ra_nic' align='absmiddle'>[<span id='integrityMsg'>"+
							mailMsg.mail_integrity_notcheck+"</span>]"+			
							"<span id='integrityBtn' ><a href='javascript:;' class='btn_basic' onclick=\"confirmIntegrity('{MSG_FNAME_ENC}','{MSG_UID}')\"><span>"+
							mailMsg.mail_integrity+"</span></a></span>",			
			readAttachExFuncEnd:"</td></tr>",
			readAttachBodyEnd:"</table>",
			
			readContentTextarea:"<textarea id='messageText' style='display:;'>{MSG_H_CONTENT}</textarea>",
			readContentStart:"<table cellpadding='0' cellspacing='0' border='0' class='TM_r_ctable'>",
			readContentHtml:"<tr><td align='center' class='TM_r_content'>"+			
							"<iframe frameborder='0' width='100%' height='300px' scrolling='no' src='messageContent.html' id='messageContentFrame'></iframe>"+
							"</td></tr>",
			readContentImg:"<div style='text-align:center;border-top:2px solid #d6d6d6;padding-top:5px'>{MSG_ATTACH_IMG}</div>",
			readContentEnd:"</table>",		
			commonBodyEnd:"</div></form></div>"
				
		};
	},
	makeRead:function(drowId,msgData){
		debugInfo("READ_DROW_START");
		var sb = new StringBuffer();
		var templates = this.templates;
		var msgContent = msgData.msgContent;
		
		var valueMap = [];
		valueMap["MSG_UID"] = msgContent.uid;
		valueMap["MSG_H_FROM"] = msgContent.fromHidden;
		valueMap["MSG_H_TO"] = msgContent.toHidden;
		valueMap["MSG_I_FROM"] = msgContent.from;		
		valueMap["MSG_R_DATE"] = msgContent.date;
		valueMap["MSG_FNAME_ENC"] = msgContent.folderEncName;
		valueMap["MSG_FNAME_FULL"] = msgContent.folderFullName;
		valueMap["MSG_SUBJECT"] = (msgContent.subject != "")?escape_tag(msgContent.subject):mailMsg.header_nosubject;
			
		sb.append(templates.commonBodyStart);
		sb.append(templates.readHeaderStart);
		sb.append(templates.readHeaderSubject);
		sb.append(templates.readHeaderContent);
		if(MENU_STATUS.addr && MENU_STATUS.addr == "on") {
			sb.append(templates.readHeaderFromAddAddr);
		}	
		sb.append(templates.readHeaderEnd);	
		
		sb.append(templates.readExHeaderStart);
		sb.append(templates.readExHeaderDate);
		
		sb.append(templates.readExHeaderToStart);		
		sb.append(this.drowAddr("to",templates.readExHeaderToContent,"{MSG_I_TO}",msgContent.toList));		
		if(MENU_STATUS.addr && MENU_STATUS.addr == "on") {
			sb.append(templates.readExHeaderToAddAddr);
		}
		sb.append(templates.readExHeaderToEnd);
		
		if(msgContent.ccList.length > 0){
			sb.append(templates.readExHeaderCcStart);		
			sb.append(this.drowAddr("cc",templates.readExHeaderCcContent,"{MSG_I_CC}",msgContent.ccList));		
			if(MENU_STATUS.addr && MENU_STATUS.addr == "on") {
				sb.append(templates.readExHeaderCcAddAddr);
			}
			sb.append(templates.readExHeaderCcEnd);
			sb.append(templates.readExHeaderEnd);
		}
		
		if(msgContent.bccList.length > 0){
			sb.append(templates.readExHeaderBccStart);		
			sb.append(this.drowAddr("bcc",templates.readExHeaderBccContent,"{MSG_I_BCC}",msgContent.bccList));		
			if(MENU_STATUS.addr && MENU_STATUS.addr == "on") {
				sb.append(templates.readExHeaderBccAddAddr);
			}
			sb.append(templates.readExHeaderBccEnd);
			sb.append(templates.readExHeaderEnd);
		}
		
		var fileLength = msgData.fileLength;
		sb.append(templates.readAttachBodyStart);		
		sb.append(this.drowAttrFile(fileLength,
				msgContent.folderFullName,
				msgData.sharedFlag,
				msgContent.attachList, 
				msgContent.vcardList));
		
		
		
		if(msgData.ruleAdmin){
			valueMap["MSG_SPAM_RATE"] = msgData.spamRate;			
			sb.append(templates.readAttachExFuncStart);
			sb.append(templates.readAttachExFuncRule);
			sb.append(templates.readAttachExFuncRuleLink);			
			if(msgData.spamAdmin){
				valueMap["RULE_TYPE"] = "spam";				
				valueMap["RULE_INDEX"] = mailMsg.bayesian_submitspam;
			} else if(msgData.hamAdmin){
				valueMap["RULE_TYPE"] = "white";
				valueMap["RULE_INDEX"] = mailMsg.bayesian_submitham;
			}
			sb.append(templates.readAttachExFuncEnd);
		}
		if(msgData.hiddenImg){
			sb.append(templates.readAttachExFuncStart);
			sb.append(templates.readAttachExFuncHiddenImg);
			sb.append(templates.readAttachExFuncEnd);			
		}
		
		if(msgData.integrityUse == "on" &&
				msgData.sharedFlag != "shared"){
			sb.append(templates.readAttachExFuncStart);
			sb.append(templates.readAttachExFuncIntegrity);
			sb.append(templates.readAttachExFuncEnd);		
		}
		sb.append(templates.readAttachBodyEnd);		
		sb.append("<textarea id='messageText' style='display:none;'>");
		sb.append(msgData.htmlContent);
		
		var imgList = msgContent.imgList;
		if(imgList.length > 0){
			var imgStr = "";
			for ( var i = 0; i < imgList.length; i++) {				
//				imgStr += "<hr>";
				imgStr += "<img src='"+imgList[i]+"'/>";
			}
			sb.append(replaceAll(templates.readContentImg,"{MSG_ATTACH_IMG}",imgStr));
			imgList = null;
		}		
		sb.append("</textarea>");		
		sb.append(templates.readContentStart);
		sb.append(templates.readContentHtml);
		
		sb.append(templates.readContentEnd);		
		sb.append(templates.commonBodyEnd);	
		
		var readContent = sb.toString();		
		readContent = RendererReplaceValue(readContent,valueMap);		
		readMsgData = {
				folderFullName:msgContent.folderFullName,
				folderEncName:msgContent.folderEncName,
				uid:msgContent.uid,
				preUid:msgData.preUid,
				nextUid:msgData.nextUid,
				fid:msgContent.folderFullName+"_"+msgContent.uid,
				sharedFlag:msgData.sharedFlag,
				sharedUserSeq:msgData.sharedUserSeq,
				sharedFolderName:msgData.sharedFolderName,
				size:msgContent.size,
				filesLength:msgData.fileLength,
				hiddenImg:msgData.hiddenImg};

		currentFolderType = msgData.folderType;
		isMDNSend = msgData.MDNCheck;
		
		$(drowId).innerHTML = readContent;
		debugInfo("READ_DROW_END");
		loadMessageReadPage();
		if(!MENU_STATUS.setting || MENU_STATUS.setting != "on") {
			jQuery("#view_setting").hide();
		}
		
		var folderType = msgData.folderType;
		var isExceptFolder = (folderType == 'sent' || folderType == 'draft' || folderType == 'reserved')?true:false;
		if (msgData.useGeoIp && !isExceptFolder) {
			jQuery("#mailFromIpBtn").show();
		}
		sb = null;
	},
	drowAddr:function(type,addrTemplate,key,addrList){
		var sb = new StringBuffer();
		
		
		var asize = addrList.length;
		var maxSize = asize;
		var startIdx = 0;
		var isSizeOver = false;
		if(asize > 5){
			maxSize = 5;
			isSizeOver = true;
		}
		
		sb.append(this.drowEmailStr(addrList,addrTemplate,key,startIdx,maxSize));
		if(isSizeOver){
			sb.append("<span id='moreAddr"+type+"' style='display:none'>");
			sb.append(this.drowEmailStr(addrList,addrTemplate,key,5,asize));
			sb.append("</span>");
			sb.append("<span class='smoreBtn' id='moreAddr"+type+"Btn' onclick='toggleEmailAddress(\""+type+"\")'>");
			sb.append(comMsg.comn_more);
			sb.append("</span>");
		}
		var content = sb.toString();
		try{
			return content;
		}finally{
			sb = null;
		}		
	},
	drowEmailStr:function(addrList,addrTemplate,key,startIdx, size){
		var sb = new StringBuffer();
		var personal,addr,paddr;
		for ( var i = startIdx; i < size; i++) {
			personal = addrList[i].personal;
			addr = addrList[i].address;
			if(i > 0){
				sb.append(",");
			}
			if(personal != ""){
				paddr = "&quot;"+personal+"&quot; &lt;"+addr+"&gt;";
			} else {
				paddr = "&lt;"+addr+"&gt;";
			}
			sb.append(" ");
			sb.append(replaceAll(addrTemplate,key,paddr));
		}
		
		var content = sb.toString();
		try{
			return content;
		}finally{
			sb = null;
		}	
	},
	drowAttrFile:function(fileLength,folderName,sharedFlag,attachList,vcardList){
		var templates = this.templates;
		var sb = new StringBuffer();
		var asb = new StringBuffer();
		var index;		
		if(fileLength > 0){
			index = replaceAll(templates.readAttachIndex,"{MSG_ATTACH_C_INDEX}",fileLength+" "+mailMsg.mail_existattach);			
			index = replaceAll(index,"{MSG_ATTACH_FUNC_BTN}",templates.readAttachFileBtn);			
			var allPart = "";
			var btnStr = "";			
			var attrElement;
			var tnefElementList;
			var tnefElement;
			for ( var i = 0; i < attachList.length; i++) {
				btnStr = "";
				attrElement = attachList[i];				
				if(attrElement.size > 0){
					btnStr +="<a href='javascript:;' onclick='downLoadAttach(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\",\""+attrElement.path+"\")' class='rdown'>";						
					btnStr +="<img src='/design/common/image/icon/"+this.checkImgFile(attrElement.fileType)+".gif' alt='"+attrElement.fileType+"' align='absmiddle'/> ";
					btnStr += attrElement.fileName;
					btnStr += "["+attrElement.fsize+"]";
					btnStr += "</a>";
					
					if(attrElement.isTnef){
						tnefElementList = attrElement.tnefList;
						if(tnefElementList.length > 0){
							btnStr += "[";
							for ( var j = 0; j < tnefElementList.length; j++) {
								if(j > 0){btnStr += ",";}
								btnStr += "<a href='javascript:;' onclick='downLoadTnefAttach(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\",\""+attrElement.path+"\",\""+tnefElementList[j].attachKey+"\")' class='rdown'>";
								btnStr += tnefElementList[j].fileName;							
								btnStr += "</a>";
							}
							btnStr += "]";
						}
					}
					if(attrElement.fileType == "eml"){
						btnStr +="<a href='javascript:;' onclick='readNestedMessage(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\",\""+attrElement.path+"\");'>" +
						"&nbsp;<img src='/design/common/image/blank.gif' alt='"+mailMsg.mail_popupview+"' title='"+mailMsg.mail_popupview+"'class='popupReadIcon' align='absmiddle'></a>";
					}
					if(sharedFlag != "shared" && 
							folderName != "Sent" && 
							folderName != "Reserved"){
						btnStr +="<a href='javascript:;' onclick='deleteAttachFile(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\",\""+attrElement.path+"\");'>" +
								"&nbsp;<img src='/design/common/image/blank.gif' class='TM_ra_del' align='absmiddle'></a>";
					}
					
					allPart += attrElement.path +"_";
				} else {
					btnStr +="<span class='rdeleted'><img src='/design/common/image/icon/"+this.checkImgFile(attrElement.fileType)+".gif' alt='"+attrElement.fileType+"' align='absmiddle'/>&nbsp;";
					btnStr += attrElement.fileName;
					btnStr += "["+mailMsg.mail_deleteattach+"]</span>";
				}
				btnStr += "<br/>";				
				
				asb.append(btnStr);
			}
			
			btnStr = "";
			if(vcardList.length > 0){
				asb.append("VCard ");
				for ( var i = 0; i < vcardList.length; i++) {
					btnStr += "<a href='javascript:;' onclick='downLoadAttach(\"{MSG_UID}\",\"{MSG_FNAME_ENC}\",\""+vcardList[i].path+"\")' class='rdown'>";				
					btnStr += "<img src='/design/common/image/icon/ic_vcard.png'  align='absmiddle'></a>";
					btnStr += "["+vcardList[i].size+"]";
					allPart += vcardList[i].path +"_";
					asb.append(btnStr);
				}
			}
			
			sb.append(replaceAll(index,"{ATTCH_ALL}",allPart));
			sb.append(templates.readAttachFileStart);
			sb.append(asb.toString());
			sb.append(templates.readAttachFileEnd);
			asb = btnStr = allPart = attrElement = tnefElementList = tnefElement = null;			
		} else {
			index = replaceAll(templates.readAttachIndex,"{MSG_ATTACH_C_INDEX}",mailMsg.mail_noattach);
			index = replaceAll(index,"{MSG_ATTACH_FUNC_BTN}","&nbsp;");
			index = replaceAll(index,"{ATTCH_ALL}","&nbsp;");
			sb.append(index);
		}
		
		try{
			return sb.toString();
		}finally{
			sb = null;
		}	
	},
	checkImgFile:function(fileType){
		fileType = fileType.toLowerCase();
		var imgFileName = "ic_att_";
		if(fileType=='doc' || 	fileType=='docx'|| 	fileType=='gif' || 
				fileType=='pdf' || 	fileType=='html'|| 	fileType=='hwp' || 
				fileType=='jpg' || 	fileType=='bmp' ||	fileType=='ppt' || 
				fileType=='pptx'|| 	fileType=='txt' || 	fileType=='xls' || 
				fileType=='xlsx'|| 	fileType=='zip' || 	fileType=='xml' ||
				fileType=='mpeg'||	fileType=='avi' || 	fileType=='htm' ||
				fileType=='mp3' ||	fileType=='mp4' || fileType=='eml'){			
			imgFileName += fileType;	
		} else {
			imgFileName += "unknown";			
		}
		
		return imgFileName;		
	}
	
});

var MailRealtionListControl = Class.create({
	initialize: function(){	
		this.tableStart = "<div style='height:130px;background:#FFFFFF;'><table cellpadding='0' cellspacing='0' border='0' class='TM_b_list'><col width='90px'></col>" +
				"<col></col><col width='80px'></col><col width='110px'></col>";
		this.tableEnd = "</table></div>";
		this.tableContent = "<tr><td>{FOLDER_NAME}</td><td class='TM_list_subject' onclick='readMessage(\"{MSG_FOLDERNAME}\",\"{MSG_UID}\")'><a href='javascript:;' title='{SUBJECT}'>{SUBJECT}</a></td>" +
				"<td class='TM_list_from title='{MSG_EMAIL}'><div class='TM_list_from' nowrap>{EMAIL}</div></td><td class='TM_list_Date_Size'>{DATE}</td></tr>";
		this.btnBar = "<div style='height:25px;text-align:right;padding:10px 5px 0px 0px;'> <a href='javascript:;' onclick='moveRelationList(1,\"{SORT_DIR}\")'>{SORT_DESC_TITLE}</a> &nbsp; &nbsp;{PAGEING}</div>";
	},
	makeList : function(data){		
		var listData = data.messageList;
		var sb = new StringBuffer();
		sb.append(this.tableStart);
		
		if(listData){			
			for ( var i = 0; i < listData.length; i++) {
				sb.append(this.drowList(listData[i],data.rfolderName,data.ruid));
			}
		}
		sb.append(this.tableEnd);
		sb.append(this.drowFunc(data));		
		return sb.toString();		
		
	},
	drowList:function(msg,folderName,ruid){
		var msubject = jQuery.trim(msg.subject);
		var mfolderName = msg.folderDepthName;		
		msubject = (msubject != "")?escape_tag(msubject):mailMsg.header_nosubject;
		
		var valueMap = [];		
		valueMap["EMAIL"] = (mfolderName == "Sent")?msg.sendToSimple:msg.fromToSimple;
		valueMap["MSG_EMAIL"] = (mfolderName == "Sent")?msg.toEscape:msg.fromEscape;
		valueMap["MSG_FOLDERNAME"] = msg.folderName;
		valueMap["MSG_UID"] = msg.id;
		valueMap["FOLDER_NAME"] = this.getFolderName(msg.folderDepthName);		
		valueMap["SUBJECT"] = msubject;				
		valueMap["DATE"] = (mfolderName == "Sent")?msg.sentDate:msg.date;
		
		return RendererReplaceValue(this.tableContent,valueMap);		
		
	},
	drowFunc:function(listInfo){		
		var sortdir,sorttitle;		
		if(listInfo.sortDir == "desc"){
			sortdir = "asce";
			sorttitle = mailMsg.mail_relation_old;
		} else {
			sortdir = "desc";
			sorttitle = mailMsg.mail_relation_new;
		}		
		
		var page = Number(listInfo.page);
		var pagingStr = "";
		if(!listInfo.isFistPage){
			pagingStr += "<a href='javascript:;' onclick='moveRelationList("+(page-1)+")'>" +
					"<img class='navi_img' src='/design/common/image/ic_paging_pre.gif'/>"+
					comMsg.comn_prelist+"</a>";
		}		
		
		if(!listInfo.isLastPage){
			if(!listInfo.isFistPage){
				pagingStr += " | ";
			}
			pagingStr += "<a href='javascript:;' onclick='moveRelationList("+(page+1)+")'>" +
				comMsg.comn_nextlist+
				"<img class='navi_img' src='/design/common/image/ic_paging_next.gif'/></a>";
		}
		
		var valueMap = [];		
		valueMap["SORT_DESC_TITLE"] = sorttitle;
		valueMap["SORT_DIR"] = sortdir;
		valueMap["PAGEING"] = pagingStr;
		
		return RendererReplaceValue(this.btnBar,valueMap);
	},
	getFolderName:function(folderName){
		if(folderName == "Inbox"){
			folderName = mailMsg.folder_inbox;
		} else if(folderName == "Sent"){
			folderName = mailMsg.folder_sent;
		} else if(folderName == "Drafts"){
			folderName = mailMsg.folder_drafts;
		} else if(folderName == "Reserved"){
			folderName = mailMsg.folder_reserved;
		} else if(folderName == "Spam"){
			folderName = mailMsg.folder_spam;
		} else if(folderName == "Trash"){
			folderName = mailMsg.folder_trash;
		}
		
		return folderName;
		
	}
});


jQuery.fn.MsgDroppable = function(droupSelector,opts,func){	
	return this.each(function() {
		
		function makeDrop(node,opts){
			node.droppable({
	            accept: droupSelector,
	            hoverClass: opts.dropOverClass,
	            tolerance: 'pointer',	            
	            over: function(ev, ui) {
					var dropElemId = ui.draggable.attr("id");
		            var finfo = MsgIdUtil.getID(dropElemId);
		            var fName = finfo.folder;	            
		            
		            var dFolder = jQuery(this);
		            var dropType = dFolder.attr("droptype");
	                dropType = (dropType)?dropType:"folder";
		            var dName = dFolder.attr("fullname");
		            var helper = jQuery("#"+opts.helperID);
		            var allSCheck = $("allSearchSelectCheck").value;
		            
		            if((!isAllFolder && dName == fName) 
		            	|| (dName == "Sent" || 
		            		dName == "Drafts" || 
		            		dName == "Reserved")||
		            	(allSCheck == "on"&& dropType == "tag")){
		            	enableDrop = false;
		            }else {
		            	helper.removeClass(opts.helperUnuseClass);
		            	helper.addClass(opts.helperUseClass);
		            }
				},
				out: function(ev, ui) {
					var helper = jQuery("#"+opts.helperID);
					helper.removeClass(opts.helperUseClass);
					helper.addClass(opts.helperUnuseClass);
					enableDrop = true;
				},
	            drop: function(ev, ui) {
	                var dropElemId = ui.draggable.attr("id");	                
	                var finfo = MsgIdUtil.getID(dropElemId);	                
	                var dFolder = jQuery(this);
	                var dName = dFolder.attr("fullname");
	                var dropType = dFolder.attr("droptype");
	                dropType = (dropType)?dropType:"folder";
	                var tagId = "";
	                var uids,fNames;
	                var allSCheck = $("allSearchSelectCheck").value;
	                
	                if((dName == "Sent" || 
		            	dName == "Drafts" || 
		            	dName == "Reserved")){
	                	return;
	                }
	                
	                if(dropType == "tag"){
	                	var tdivId = dFolder.attr("id");
	                	dName = tdivId.substring(7,tdivId.length);
	                }
	                
	                if(allSCheck == "on"){
	                	if (!isAllFolder){
	                		if(dropType == "folder" &&dName == finfo.folder) return;
	                	} 
	                	if(enableDrop){
	                		func(true,dropType,uids, fNames, dName);
	                	}
	                }else if(chkMsgCnt > 0){	                	
	                	var dropList = chkMsgHash.values();	                	
	                	if (!isAllFolder){
	                		if(dropType == "folder" && dName == finfo.folder) return;	                		
	                		uids = [];
	                		for ( var i = 0; i < dropList.length; i++) {	                			
	                			uids[uids.length] = MsgIdUtil.getID(dropList[i]).uid;
							}	                		
	                		fNames = [finfo.folder];
	                	} else {	                		
	                		uids = [];
	                		fNames = [];
	                		var dropValues;
	                		for ( var i = 0; i < dropList.length; i++) {	                			
	                			dropValues = MsgIdUtil.getID(dropList[i]);
	                			if(dropType == "folder" && dName != dropValues.folder){
	                				uids[uids.length] = dropValues.uid;
	                				fNames[fNames.length] = dropValues.folder;
	                				jQuery("#"+dropList[i]).remove();
	                			} else if(dropType == "tag"){
	                				uids[uids.length] = dropValues.uid;
	                				fNames[fNames.length] = dropValues.folder;
	                			}
							}	                		
	                	}
	               } else {         	   
	            	   if(dropType == "folder" && dName == finfo.folder) return;	            	   
		               uids = [parseInt(finfo.uid)];
		               fNames = [finfo.folder];       	
	               }				
					if(!dropActive && enableDrop){						
						func(false,dropType,uids, fNames, dName);
					}	                
					jQuery("#"+opts.helperID).remove();
	            }
	        });
		}		
		
		var node = jQuery(this);
		makeDrop(node,opts);				
		
	});
}


jQuery.fn.MsgDraggable = function(opts){	
	return this.each(function() {
		jQuery(this).draggable({helper:function(e){
			dropActive = false;
			var finfo = MsgIdUtil.getID(jQuery(this).attr("id"));
			var dragText;
			var allSCheck = $("allSearchSelectCheck").value;
			
			if(allSCheck == "on"){
				dragText = mailMsg.allselect_003;				
			} else if(chkMsgCnt > 0){				
				dragText = chkMsgCnt + mailMsg.mail_moremsg;				
			} else {
				dragText  = jQuery("#"+opts.helperRefID+finfo.fid+"_"+finfo.uid).text();
			}			
			
			return jQuery("<div><span>"+dragText+"</span></div>").attr("id",opts.helperID).addClass(opts.helperUnuseClass);	
		},		
		appendTo:"body",cursorAt: { top: 20, left: 1 },scroll: false});		
		
	});
}

var dropActive = false;
var enableDrop = true;
var DnDManager = Class.create({
	initialize: function(opts){
		this.opts = opts;
		this.tableSelector = null;
		this.func = null;		
	},
	applyDrag:function(id){		
		jQuery("#"+id).MsgDraggable(this.opts);
	},
	applyDrop:function(nodeList,tableSelector,func){
		this.tableSelector = tableSelector;
		this.func = func;
		for(var i = 0 ; i < nodeList.length ; i++){
			jQuery(nodeList[i]).MsgDroppable(this.tableSelector,this.opts,func);
		}		
	},
	reloadApplyDnD:function(nodeList){		
		if(this.tableSelector){			
			for(var i = 0 ; i < nodeList.length ; i++){		
				jQuery(nodeList[i]).MsgDroppable(this.tableSelector,this.opts,this.func);
			}
		}
	},
	destroyDrop:function(nodeList) {
		for(var i = 0 ; i < nodeList.length ; i++){
			jQuery(nodeList[i]).droppable("destroy");
		}
	}
		
});


function updateTag(tagList, func){
	for ( var i = 0; i < tagList.length; i++) {
		jQuery("#"+tagList[i].id).printTag(func,tagList[i].mid,tagList[i].tag);
	}
}
