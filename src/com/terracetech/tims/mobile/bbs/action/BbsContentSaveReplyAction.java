package com.terracetech.tims.mobile.bbs.action;


import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsContentReplyVO;

public class BbsContentSaveReplyAction extends BaseAction{
	private static final long serialVersionUID = -6977436884879330833L;
	private BbsService bbsService = null;
	
	private int bbsId = 0;
	private int contentId = 0;
	private int isNotice = 0;
	private String content = null;
		
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		
		boolean isSuccess = false;	
		
		BbsContentReplyVO boardContentReplyVo = new BbsContentReplyVO();
		boardContentReplyVo.setBbsId(bbsId);
		boardContentReplyVo.setContentId(contentId);
		boardContentReplyVo.setContent(content);
		boardContentReplyVo.setRequest(request);		
		isSuccess = bbsService.saveBbsContentReply(boardContentReplyVo,user);	
		
		request.setAttribute("isSuccess", isSuccess);
		request.setAttribute("job", "save");
		
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
