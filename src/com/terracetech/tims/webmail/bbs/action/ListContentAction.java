package com.terracetech.tims.webmail.bbs.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.common.PageManager;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ContentListAction.java</strong> Class Description</p>
 * <p>�ֿ伳��</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author jhlee
 * @since Tims7
 * @version 7.0 
 */

public class ListContentAction extends BaseAction{
	
	private BbsManager bbsManager = null;
	private List<BoardContentVO> contentnoticeList = null;
	private List<BoardContentVO> contentList = null;
	private List<BoardVO> boardList = null;
	private BoardVO boardVO = null;
	private MailSortMessageBean[] messageBeans = null;
	
	private String searchType = null;
	private String keyWord = null;
	private int currentPage = 0;
	private int bbsId = 0;
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;
	
	private String readType = null;
	private int bbsUid = 0;
	private int noticePageBase = 0;
	private int pbase = 0;

	private PageManager pm = null;

	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("bbs");
		
		User user = UserAuthManager.getUser(request);
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int skipResult = 0;
		
		boardList = bbsManager.readBoardList(domainSeq);

		if (boardList == null || boardList.size() == 0) {
			request.setAttribute("errMsg", resource.getMessage("bbs.empty"));
			return "fail";
		}
		
		bbsId = (bbsId == 0) ? boardList.get(0).getBbsId() : bbsId;
		searchType = (searchType == null) ? "s" : searchType;
		currentPage = (currentPage == 0) ? 1 : currentPage;
		
		noticePageBase = (noticePageBase == 0) ? 5 : noticePageBase;

		if ("portlet".equalsIgnoreCase(readType)) {
			bbsId = bbsUid;
		}
		
		String portletPart = request.getParameter("part");
		
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		
		boardVO = bbsManager.readBoard(bbsId, domainSeq);
		String bbsType = boardVO.getBbsType();
		String pbaseStr = user.get(User.PAGE_LINE_CNT);
		if (StringUtils.isEmpty(pbaseStr)) {
			pbaseStr = "15";
		}
		int pageBase = Integer.parseInt(pbaseStr);
		pbase = (pbase == 0) ? pageBase : pbase;

		skipResult = (currentPage - 1) * pbase;

		contentnoticeList = bbsManager.readBoardContentNoticeList(bbsId, domainSeq, noticePageBase);

		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		int contentListCount = 0;
		if (StringUtils.isEmpty(keyWord) || "secret".equalsIgnoreCase(boardVO.getBbsType())) {
			
			if ("7".equals(searchType)) {
				keyWord = user.get(User.MAIL_USER_SEQ);
			}

			contentList = bbsManager.readBoardContentList(bbsId, domainSeq, creatorSeq, searchType, keyWord, skipResult, pbase);
			contentListCount = bbsManager.readBoardContentListCount(bbsId, domainSeq, creatorSeq, searchType, keyWord);
		} else {
			TMailStoreFactory factory = new TMailStoreFactory();
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			TMailStore store = factory.connect(request.getRemoteAddr(), confMap);
			bbsManager.setCommandResource(store, Integer.toString(bbsId), getMessageResource());
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy("date");
			sortBean.setSortDir("desc");
			sortBean.setAdSearchCategory(searchType);
			sortBean.setAdvanceMode(true);
			sortBean.setAdSearchPattern(keyWord);				
			sortBean.setPage(Integer.toString(currentPage));
			sortBean.setPageBase(Integer.toString(pbase));
			
			if ("c".equals(searchType)) {
				sortBean.setAdFromEmailPattern(keyWord);	
			}
			
			messageBeans = bbsManager.getXSortMessageBeans(Integer.toString(bbsId), sortBean);
			contentListCount = bbsManager.getXCommandTotal();
			if (messageBeans != null && messageBeans.length > 0) {
				String[] messageIds = new String[messageBeans.length];
				for (int i=0; i<messageBeans.length; i++) {
					messageIds[i] = messageBeans[i].getMessage().getMessageID();
				}
				contentList = bbsManager.readBoardContentListByMessageIds(bbsId, domainSeq, messageIds);
			}
		}
	
		pm = new PageManager();
		pm.initParameter(contentListCount, pbase, 10);
		pm.setPage(currentPage);
		
		HttpSession session = request.getSession();
		session.setAttribute("bbsId", bbsId);
		
		request.setAttribute("pageBase", pbase);
		request.setAttribute("totalCount", contentListCount);
		request.setAttribute("part", portletPart);
		
		return "success";
	}
	
	public int getPbase() {
		return pbase;
	}

	public void setPbase(int pbase) {
		this.pbase = pbase;
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

	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	
	public List<BoardContentVO> getContentnoticeList() {
		return contentnoticeList;
	}
	
	public void setContentnoticeList(List<BoardContentVO> contentnoticeList) {
		this.contentnoticeList = contentnoticeList;
	}
	
	public List<BoardContentVO> getContentList () {
		return contentList;
	}

	public void setContentList(List<BoardContentVO> contentList) {
		this.contentList = contentList;
	}
	
	public List<BoardVO> getBoardList() {
		return boardList;
	}
	public void setBoardList(List<BoardVO> boardList) {
		this.boardList = boardList;
	}

	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public PageManager getPm() {
		return pm;
	}
	public void setPm(PageManager pm) {
		this.pm = pm;
	}
	
	public BoardVO getBoardVO () {
		return boardVO;
	}
	public void setBoardVO(BoardVO boardVO) {
		this.boardVO = boardVO;
	}

	public boolean isBbsAdmin() {
		return bbsAdmin;
	}

	public void setBbsAdmin(boolean bbsAdmin) {
		this.bbsAdmin = bbsAdmin;
	}

	public boolean isBbsCreator() {
		return bbsCreator;
	}

	public void setBbsCreator(boolean bbsCreator) {
		this.bbsCreator = bbsCreator;
	}

	public void setReadType(String readType) {
		this.readType = readType;
	}

	public void setNoticePageBase(int noticePageBase) {
		this.noticePageBase = noticePageBase;
	}

	public void setBbsUid(int bbsUid) {
		this.bbsUid = bbsUid;
	}
	
	public MailSortMessageBean[] getMessageBeans() {
		return messageBeans;
	}


}
