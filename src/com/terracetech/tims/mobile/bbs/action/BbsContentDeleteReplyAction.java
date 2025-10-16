package com.terracetech.tims.mobile.bbs.action;


import javax.servlet.http.HttpSession;

import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsContentReplyVO;

public class BbsContentDeleteReplyAction extends BaseAction{
	
	private static final long serialVersionUID = 296883066230750350L;
	private BbsService bbsService = null;	
	private int bbsId = 0;
	private int contentId = 0;
	private int isNotice = 0;
	private int replyId = 0;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		boolean isSuccess = false;		
		
		BbsContentReplyVO bbsContentVo = new BbsContentReplyVO();
		bbsContentVo.setBbsId(bbsId);
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setReplyId(replyId);
		
		isSuccess = bbsService.deleteBbsContentReply(bbsContentVo,user);	
		
		request.setAttribute("isSuccess", isSuccess);
		request.setAttribute("job", "delete");
		return "success";
		
	}

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	

}
