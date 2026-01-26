package com.terracetech.tims.webmail.bbs.action;

import java.util.List;
import java.util.Map;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;

public class ViewContentReplyAction extends BaseAction {

	private BbsManager bbsManager = null;
	private List<BoardContentReplyVO> boardContentReplyList = null;
	private PageManager pm = null;
	private int bbsId = 0;
	private int contentId = 0;
	private int isNotice = 0;
	private int currentPage = 0;
	private int boardContentReplyListCount = 0;
	private boolean bbsAdmin = false;
	private int mailUserSeq = 0;

	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public String execute() throws Exception {
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int skipResult = 0;
		int pbase = 10;
		
		currentPage = (currentPage == 0) ? 1 : currentPage;
		skipResult = (currentPage - 1) * pbase;
		
		boardContentReplyListCount = bbsManager.getContentReplyListCount(bbsId, contentId, isNotice, mailDomainSeq);
		boardContentReplyList = bbsManager.getContentReplyList(bbsId, contentId, isNotice, mailDomainSeq, skipResult, pbase);
		pm = new PageManager();
		pm.initParameter(boardContentReplyListCount, pbase, 10);
		pm.setPage(currentPage);
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, mailUserSeq);
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		
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

	public List<BoardContentReplyVO> getBoardContentReplyList() {
		return boardContentReplyList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public PageManager getPm() {
		return pm;
	}

	public int getBoardContentReplyListCount() {
		return boardContentReplyListCount;
	}

	public boolean isBbsAdmin() {
		return bbsAdmin;
	}

	public int getMailUserSeq() {
		return mailUserSeq;
	}
}
