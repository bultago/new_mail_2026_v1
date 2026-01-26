package com.terracetech.tims.webmail.bbs.action;

import java.util.List;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.Validation;

public class ListNoticeContentAction extends BaseAction {

	private BbsManager bbsManager = null;
	private MailUserManager mailUserManager = null;
	private BoardVO noticeBbs = null;
	private List<BoardContentVO> noticeContentList = null;
	private PageManager pm = null;
	private String bbsIndex = null;
	private int currentPage = 0;
	
	public ListNoticeContentAction(boolean authcheck) {		
		setAuthCheck(authcheck);		
	}
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public void setMailUserManager(MailUserManager mailUserManager) {
		this.mailUserManager = mailUserManager;
	}

	public String execute() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		String domain = mailUserManager.readDefaultDomain();
		int defaultDomainSeq = mailUserManager.searchMailDomainSeq(domain);
		List<BoardVO> noticeBbsList = bbsManager.readNoticeBbsList(defaultDomainSeq);
		
		if (noticeBbsList != null) {
			String searchType = "1";
			String keyWord = "";
			currentPage = (currentPage == 0) ? 1 : currentPage;
			int bbsIndexNum = (Validation.isNumeric(bbsIndex)) ? Integer.parseInt(bbsIndex) : 0;
			bbsIndex = Integer.toString(bbsIndexNum);
			
			if (bbsIndexNum < noticeBbsList.size()) {
				noticeBbs = noticeBbsList.get(bbsIndexNum);
				int bbsId = noticeBbs.getBbsId();
				int pageBase = noticeBbs.getPagePerCnt();
				int creatorSeq = 0;
				pageBase = (pageBase == 0) ? 15 : pageBase;
				int skipResult = (currentPage - 1) * pageBase;
				noticeContentList = bbsManager.readBoardContentList(bbsId, defaultDomainSeq, creatorSeq, searchType, keyWord, skipResult, pageBase);
				int noticeCountListCount = bbsManager.readBoardContentListCount(bbsId, defaultDomainSeq, creatorSeq, searchType, keyWord);
				pm = new PageManager();
				pm.initParameter(noticeCountListCount, pageBase, 10);
				pm.setPage(currentPage);
			}
		}
		return "success";
	}

	public BoardVO getNoticeBbs() {
		return noticeBbs;
	}

	public List<BoardContentVO> getNoticeContentList() {
		return noticeContentList;
	}

	public String getBbsIndex() {
		return bbsIndex;
	}

	public void setBbsIndex(String bbsIndex) {
		this.bbsIndex = bbsIndex;
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

}
