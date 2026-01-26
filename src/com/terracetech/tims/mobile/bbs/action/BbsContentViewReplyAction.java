package com.terracetech.tims.mobile.bbs.action;

import java.util.List;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mobile.common.action.BaseAction;
import com.terracetech.tims.service.tms.impl.BbsService;
import com.terracetech.tims.service.tms.vo.BbsContentReplyVO;
import com.terracetech.tims.service.tms.vo.BoardContentVO;
import com.terracetech.tims.service.tms.vo.BbsViewContentCondVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.mailuser.User;

public class BbsContentViewReplyAction extends BaseAction{
	
	private static final long serialVersionUID = -6113894593078363468L;
	
	private BbsService bbsService = null;
	private PageManager pm;
	private int bbsId = 0;
	private int contentId = 0;
	private int isNotice = 0;
	private int page = 0;
	private BbsContentReplyVO[] bbsContentReplyVo = null;
	private int boardContentReplyListCount = 0;
	private int currentCount = 0;
	private int pageBase = 10;
	private int mailUserSeq = 0;
	
	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}
	
	public String execute() throws Exception {
		int skipResult = 0;		
		
		mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		
		page = (page == 0) ? 1 : page;
		skipResult = (page - 1) * pageBase;
		
		BoardContentVO bbsContentCondVo = new BoardContentVO();
		
		bbsContentCondVo.setBbsId(bbsId);
		bbsContentCondVo.setContentId(contentId);
		bbsContentCondVo.setIsNotice(isNotice);
		
		boardContentReplyListCount = bbsService.readContentReplyCount(bbsContentCondVo,user);
		bbsContentReplyVo = bbsService.readContentReply(bbsContentCondVo,user,skipResult,pageBase);	
		if (bbsContentReplyVo != null && bbsContentReplyVo.length > 0) {
			currentCount = bbsContentReplyVo.length;
		}
		pm = new PageManager();
		pm.initParameter(boardContentReplyListCount, pageBase, 10);
		pm.setPage(page);	
		
		return "success";
	}
	
	public PageManager getPm() {
		return pm;
	}

	public void setPm(PageManager pm) {
		this.pm = pm;
	}

	public BbsContentReplyVO[] getBbsContentReplyVo() {
		return bbsContentReplyVo;
	}

	public void setBbsContentReplyVo(BbsContentReplyVO[] bbsContentReplyVo) {
		this.bbsContentReplyVo = bbsContentReplyVo;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public int getPageBase() {
		return pageBase;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}	
}
