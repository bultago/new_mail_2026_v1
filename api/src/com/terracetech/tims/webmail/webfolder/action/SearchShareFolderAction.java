package com.terracetech.tims.webmail.webfolder.action;

import java.util.List;

import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.webfolder.manager.WebfolderManager;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderDataVO;
import com.terracetech.tims.webmail.webfolder.vo.WebfolderShareVO;

public class SearchShareFolderAction extends BaseAction {
	private WebfolderManager webfolderManager = null;
	private List<WebfolderShareVO> shareList = null;
	private String searchType = null;
	private String keyWord = null;
	private int currentPage = 0;
	private PageManager pm = null;
	
	public void setWebfolderManager(WebfolderManager webfolderManager) {
		this.webfolderManager = webfolderManager;
	}
	
	public String execute() throws Exception {

		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int pageBase = 10;
		int skipResult = 0;
		searchType = (searchType == null) ? "1" : searchType;
		keyWord = (keyWord == null) ? "" : keyWord;
		currentPage = (currentPage == 0) ? 1 : currentPage;
		skipResult = (currentPage - 1) * pageBase;
		int shareListCount = webfolderManager.getSearchShareFolderCount(userSeq, user.get(User.MAIL_USER_SEQ), searchType, keyWord);
		shareList = webfolderManager.getSearchShareFolder(userSeq, user.get(User.MAIL_USER_SEQ), searchType, keyWord, skipResult, pageBase);
		List<WebfolderDataVO> folderList = webfolderManager.getMyShareFolder(domainSeq, userSeq);
		
		if (folderList != null && folderList.size() > 0) {
			for (int i=0; i<shareList.size(); i++) {
				for (int j=0; j<folderList.size(); j++) {
					if (shareList.get(i).getFuid() == folderList.get(j).getFuid()) {
						shareList.get(i).setAlreadyShare(true);
						break;
					}
				}
			}
		}
		
		pm = new PageManager();
		pm.initParameter(shareListCount, pageBase, 10);
		pm.setPage(currentPage);
		
		return "success";
	}

	public List<WebfolderShareVO> getShareList() {
		return shareList;
	}

	public void setShareList(List<WebfolderShareVO> shareList) {
		this.shareList = shareList;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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

	public void setPm(PageManager pm) {
		this.pm = pm;
	}
	
}
