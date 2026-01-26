package com.terracetech.tims.mobile.bbs.action;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BoardContentVO;
import com.terracetech.tims.service.tms.vo.BbsInfoVO;
import com.terracetech.tims.service.tms.vo.BbsWriteCondVO;
import com.terracetech.tims.service.tms.vo.BbsWriteInfoVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class BbsContentWriteAction extends BaseAction {

	private static final long serialVersionUID = -6977436884879330833L;
	
	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;
	private String wtype = null;
	
	private boolean isReply = false;
	private int depth = 0;
	private int orderNo = 0;
	
	private BbsInfoVO[] bbsInfoList = null;
	private BbsWriteInfoVO bbsWriteInfo = null;
	private BbsService bbsService = null;
	private BbsWriteCondVO bbsWriteCondVo = null;
	private BoardContentVO bbsContentVo = null;
	private String isModify = "false";
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		
		bbsInfoList = bbsService.readBbsList(null, user);
		
		bbsContentVo = new BoardContentVO();
		bbsContentVo.setBbsId(bbsId);
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setParentId(parentId);
		bbsContentVo.setReply(isReply);
		bbsContentVo.setDepth(depth);
		bbsContentVo.setOrderNo(orderNo);
				
		bbsContentVo = bbsService.writeBbsContent(bbsContentVo, user);
		
		return "success";
	}
	
	public String executeModify() throws Exception {
		
		bbsInfoList = bbsService.readBbsList(null, user);
		
		bbsContentVo = new BoardContentVO();
		bbsContentVo.setBbsId(bbsId);
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setRequest(request);
		setIsModify("true");
		
		bbsContentVo = bbsService.writeBbsModifyContent(bbsContentVo,user);		
		bbsContentVo.setHtmlContent(StringUtils.html2Text(bbsContentVo.getHtmlContent()));
		return "success";
	}
	
		
	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public int getContentId() {
		return contentId;
	}

	public int getParentId() {
		return parentId;
	}

	public String getWtype() {
		return wtype;
	}

	public BbsService getBbsService() {
		return bbsService;
	}

	public BoardContentVO getBbsContentVo() {
		return bbsContentVo;
	}

	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}

	public void setBbsInfoList(BbsInfoVO[] bbsInfoList) {
		this.bbsInfoList = bbsInfoList;
	}

	public void setBbsWriteInfo(BbsWriteInfoVO bbsWriteInfo) {
		this.bbsWriteInfo = bbsWriteInfo;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsContentVo(BoardContentVO bbsContentVo) {
		this.bbsContentVo = bbsContentVo;
	}

	public boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setWtype(String wtype) {
		this.wtype = wtype;
	}

	public BbsWriteInfoVO getBbsWriteInfo() {
		return bbsWriteInfo;
	}

	public BbsInfoVO[] getBbsInfoList() {
		return bbsInfoList;
	}
}
