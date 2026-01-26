/**
 * 201802 201802 HTML5 Uploader
 */

var UploadSimpleBasicControl = Class.create({
	initialize: function(opt){
		this.opt = opt;
		this.defaultImgPath = "";		
	},
	init:function(){
		var opt = this.opt;
		this.listId = "#"+opt.listId;
		this.quota = {"hugeMax":0,"normalMax":0,"hugeUse":0,"normalUse":0};
		this.normalFileList = new HashMap();
		this.hugeFileList = new HashMap();
		this.uploadCompleteFile = [];
		this.hugeMode = true;
		this.defaultImgPath = '/design/common/image/btn/bg_attach_file_btn_'+opt.locale+'.gif';		
	},
	makeBtnControl:function(){
	},
	btnControldestory:function(){
	},
	getControlFile:function(){
	},
	startUpload:function(){
		
	},
	stopUpload:function(){
	},
	cancelUpload:function(){
	},
	resetUpload:function(){
		this.init();
		this.makeListControl();
	},
	makeListControl:function(){
		var swfTable = jQuery('<table cellpadding="0" cellspacing="0" class="listTable_s1" style="width:100%;*width:;table-layout:fixed"></table>');
			
		var swfTable_Tr1 = jQuery('<tr></tr>');
			swfTable_Tr1.append('<th style="width:30px;"><input type="checkbox" id="basicAttachChkAll" onclick="checkAll(this,document.uploadForm.basicAttachFileEl)"/></th>');
			swfTable_Tr1.append('<th>'+mailMsg.bigattach_list_001+'</th>');
			swfTable_Tr1.append('<th style="width:80px;">'+mailMsg.bigattach_list_002+'</th>');
			swfTable_Tr1.append('<th style="width:250px;">'+mailMsg.mail_attach_type+'</th>');
	
		swfTable.append(swfTable_Tr1);
		
		var swfContentDiv = jQuery('<div style="overflow-X:hidden;overflow-Y:auto; height: 77px"></div>');
		var swfContentTable = jQuery('<table cellpadding="0" cellspacing="0" id="basicAttachList" class="listTable_s1" style="border-top:none;table-layout:fixed;"></table>');
		swfContentDiv.append(swfContentTable);
			
		var lid = this.listId;
		setTimeout(function(){
			jQuery(lid).empty();
			jQuery(lid).append(swfTable);
			jQuery(lid).append(swfContentDiv);
		},100);
	},
	addAttachList:function(fileObj){
		if(fileObj.name){
			var isHugeType = (fileObj.type == "huge")?true:false;
			var listElement ='<tr id="'+fileObj.id+'Low"><td class="center" style="width:25px;">'
				+'<input type="checkbox" id="'+fileObj.id+'" name="basicAttachFileEl" value="" attsize="'+fileObj.size+'" atttype="'+((isHugeType)?'huge':'normal')+'"></td>'
				+'<td style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;" title="'+escape_tag(fileObj.name)+'">'
				+'<img src="/design/common/image/icon/ic_att_'+getFileTypeImage(fileObj.name)+'.gif"/> '
				+escape_tag(fileObj.name)+'</td><td class="right" style="width:80px;">'+printSize(fileObj.size)+'</td>';
				listElement += '<td class="center" id="'+fileObj.id+'TypeInfo" style="width:100px;">'+((isHugeType)?mailMsg.bigattach_11:mailMsg.bigattach_10)+'</td>';
				listElement += '<td class="center" style="width:150px;">';
				if(!fileObj.notChage){
					listElement += '<a href="#n" onclick="chgAttachFileType(\''+fileObj.id+'\')" class="btn_basic"><span id="'+fileObj.id+'ChgLink">'
					+((!isHugeType)?mailMsg.mail_attach_chg_bigattach:mailMsg.mail_attach_chg_normal)
					+'</span></a>';
				}
				listElement += '</td>';
			listElement += '</tr>';
			if(isHugeType){
				this.hugeFileList.put(fileObj.id,fileObj);
			} else {
				this.normalFileList.put(fileObj.id,fileObj);
			}
			jQuery("#basicAttachList").append(listElement);
		}
	},	
	getCheckAttachFileIds:function(){
		var checkIds = [];		
		jQuery("#basicAttachList input:checked").each(function(){
			checkIds.push(jQuery(this).attr("id"));
		});
		
		return checkIds;
	},
	deleteAttachList:function(fid){
		var type  = jQuery("#"+fid).attr("atttype");
		var size  = jQuery("#"+fid).attr("attsize");
		if(type == "huge"){
			this.quota.hugeUse -= size;
			this.hugeFileList.remove(fid);
		} else {
		this.quota.normalUse -= size;
		this.normalFileList.remove(fid);
		}
		jQuery("#"+fid+"Low").remove();		
	},
	setAttachSize:function(type,size){
		if(type == "hugeMax")this.quota.hugeMax = size;
		else if(type == "hugeUse")this.quota.hugeUse = size;
		else if(type == "normalMax")this.quota.normalMax = size;
		else if(type == "normalUse")this.quota.normalUse = size;
	},
	getAttachSize:function(type){
		var size = 0;
		if(type == "hugeMax")size = this.quota.hugeMax;
		else if(type == "hugeUse")size = this.quota.hugeUse;
		else if(type == "normalMax")size = this.quota.normalMax;
		else if(type == "normalUse")size = this.quota.normalUse;					
		return size;
	},
	getAttachQuotaInfo:function(){
		return this.quota;
	},
	getFileList:function(type){
		if(type == "huge")return this.hugeFileList.getValues();
		else return this.normalFileList.getValues();
	},
	setFileList:function(type,fileList){
		var nList = new HashMap();
		for ( var i = 0; i < fileList.length; i++) {
			nList.put(fileList[i].id,fileList[i]);
		}
		if(type == "huge") this.hugeFileList = nList;
		else this.normalFileList = nList;
	},
	getAttachFileInfo:function(type,fid){
		return (type == "huge")?this.hugeFileList.get(fid):this.normalFileList.get(fid);
	},
	setAttachFileInfo:function(type,fid,fObj){
		if(type == "huge") this.hugeFileList.put(fid,fObj);
		else this.normalFileList.put(fid,fObj);
	},
	setUploadCompleteFile:function(fid,file){
		var queueFile = this.hugeFileList.get(fid);
		if(queueFile){
			queueFile.uid = file.uid;
			queueFile.path = file.filePath;
			this.hugeFileList.remove(fid);
			this.hugeFileList.put(fid,queueFile);
		} else {
			queueFile = this.normalFileList.get(fid);
			if(queueFile){
				queueFile.uid = file.uid;
				queueFile.path = file.filePath;
				this.normalFileList.remove(fid);
				this.normalFileList.put(fid,queueFile);
			}
		}
	},	
	setHugeUploadUse:function(val){
		this.hugeMode = val;
	},
	getHugeUploadUse:function(){
		return this.hugeMode;
	},
	isHugeTypeFile:function(fid){
		var hugeFile = this.hugeFileList.get(fid);
		return (hugeFile)?true:false;
	},
	chageAttachType:function(fid){
		var type = jQuery("#"+fid).attr("atttype");
		var tempFile;		
		if(type == "huge"){
			tempFile = this.hugeFileList.get(fid);
			this.normalFileList.put(fid,tempFile);
			this.hugeFileList.remove(fid);
			jQuery("#"+fid).attr("atttype","normal");
			jQuery("#"+fid + "TypeInfo").text(mailMsg.bigattach_10);			
			jQuery("#"+fid + "ChgLink").text(mailMsg.mail_attach_chg_bigattach);
		} else {
			tempFile = this.normalFileList.get(fid);
			this.hugeFileList.put(fid,tempFile);
			this.normalFileList.remove(fid);
			var today = new Date();
			jQuery("#"+fid).attr("atttype","huge");
			jQuery("#"+fid + "TypeInfo").text(mailMsg.bigattach_11);
			jQuery("#"+fid + "ChgLink").text(mailMsg.mail_attach_chg_normal);
		}
	},
	destroy:function(){
		if(this.listId)jQuery(this.listId).empty();
		this.quota = {"hugeMax":0,"normalMax":0,"hugeUse":0,"normalUse":0};
		if(this.normalFileList)this.normalFileList.destroy();
		if(this.hugeFileList)this.hugeFileList.destroy();
		this.uploadCompleteFile = [];
	},
	emptyFileList:function(){
		if(this.listId)jQuery(this.listId).empty();
	}
});