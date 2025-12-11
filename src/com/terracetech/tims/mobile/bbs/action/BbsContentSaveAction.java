package com.terracetech.tims.mobile.bbs.action;

import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsSaveCondVO;
import com.terracetech.tims.webmail.util.StringUtils;

public class BbsContentSaveAction extends BaseAction{
	
	private static final long serialVersionUID = -6977436884879330833L;
	private BbsService bbsService = null;
	
	private int bbsId = 0;
	private String content = null;
	private String subject = null;
	private int isNotice = 0;
	private int orderNo = 0;
	private int depth = 0;
	private boolean isReply = false;
	private String bbsType = null;
	private String creatorName = null;
	private int parentId = 0;
	private int contentId = 0;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		BbsSaveCondVO bbsSaveCondVo = new BbsSaveCondVO();
		bbsSaveCondVo.setBbsId(bbsId);
		bbsSaveCondVo.setSubject(subject);
		bbsSaveCondVo.setContent(StringUtils.textToHTML(content));
		bbsSaveCondVo.setIsNotice(isNotice);
		bbsSaveCondVo.setRemoteIp(request.getRemoteAddr());
		bbsSaveCondVo.setOrderNo(orderNo);
		bbsSaveCondVo.setDepth(depth);
		bbsSaveCondVo.setReply(isReply);
		bbsSaveCondVo.setBbsType(bbsType);
		bbsSaveCondVo.setCreatorName(creatorName);
		bbsSaveCondVo.setParentId(parentId);
		
		bbsService.saveBbsContent(bbsSaveCondVo,user);
		setBbsId(bbsId);
		
		return "success";
	}
	
	public String executeUpdate() throws Exception {
		BbsSaveCondVO bbsSaveCondVo = new BbsSaveCondVO();
		bbsSaveCondVo.setBbsId(bbsId);
		bbsSaveCondVo.setSubject(subject);
		bbsSaveCondVo.setContent(content);
		bbsSaveCondVo.setIsNotice(isNotice);
		bbsSaveCondVo.setRemoteIp(StringUtils.textToHTML(request.getRemoteAddr()));
		bbsSaveCondVo.setOrderNo(orderNo);
		bbsSaveCondVo.setDepth(depth);
		bbsSaveCondVo.setReply(isReply);
		bbsSaveCondVo.setBbsType(bbsType);
		bbsSaveCondVo.setCreatorName(creatorName);
		bbsSaveCondVo.setParentId(parentId);
		bbsSaveCondVo.setContentId(contentId);
		
		bbsService.updateBbsContent(bbsSaveCondVo,user);
		setBbsId(bbsId);
		
		return "success";
		
	}
	
	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getBbsType() {
		return bbsType;
	}

	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}

	public BbsService getBbsService() {
		return bbsService;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	
}
