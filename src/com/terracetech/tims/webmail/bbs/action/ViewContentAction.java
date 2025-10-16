package com.terracetech.tims.webmail.bbs.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class ViewContentAction extends BaseAction {
	
	private BbsManager bbsManager = null;
	private BoardVO boardVo = null;
	private BoardContentVO contentVo = null;
	private List<BoardVO> boardList = null;
	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;
	private int orderNo = 0;

	private String searchType = null;
	private String keyWord = null;
	private int currentPage = 0;
	
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;
	
	List<BoardContentVO> prevContent = null;
	List<BoardContentVO> nextContent = null;

	
	
	public String execute() throws Exception {
		
		I18nResources resource = getMessageResource("bbs");
		
		User user = UserAuthManager.getUser(request);
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String pbaseStr = user.get(User.PAGE_LINE_CNT);
		
		if (StringUtils.isEmpty(pbaseStr)) {
			pbaseStr = "15";
		}
		int pageBase = Integer.parseInt(pbaseStr);
		
		String returnType = "success";
		String readType = request.getParameter("readType");
		
		searchType = (searchType == null) ? "s" : searchType;
		
		if (StringUtils.isNotEmpty(readType) && ("popup".equals(readType) || "preview".equals(readType) || "print".equals(readType))) {
			returnType = "popup";
		}
		
		if("print".equals(readType)) {
			returnType = "print";
		}
		
		boardList = bbsManager.readBoardList(domainSeq);
		boardVo = bbsManager.readBoard(bbsId, domainSeq);

		contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		if (userSeq == contentVo.getCreatorSeq()) {
			contentVo.setCreator(true);
		}
		
		int creatorSeq = 0;
		String bbsType = boardVo.getBbsType();
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = userSeq;
		}
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = null;
		if ("text".equals(contentVo.getContentType())) {
			String content = "";
			if (contentVo.getContent() != null && contentVo.getContent().length > 0) {
				StringReplaceUtil sr = new StringReplaceUtil();
				content = new String((contentVo.getContent()), "UTF-8");
				content = sr.replaceEventAttr(content);
				//게시판의 경우 html모드와 일반모드가 있어 모든 <, >를 치환해서는 안되기 때문에!
			}
			contentVo.setHtmlContent(content);
		} else {
			
			confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			
			String attachesDir = context.getRealPath("/") + EnvConstants.getAttachSetting("attach.dir");
			String hostStr = request.getScheme() +"://"+ request.getServerName() +":"+ request.getServerPort();
			MessageParserInfoBean infoBean = new MessageParserInfoBean();
			infoBean.setAttachesDir(attachesDir);
			infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
			infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
			infoBean.setDefaultImg("/design/common/images/blank.gif");
			infoBean.setHiddenImg(false);
			infoBean.setHiddenTag(true);
			infoBean.setLocale(new Locale(EnvConstants.getMailSetting("default.charset")));
			infoBean.setStrLocalhost(hostStr);
			infoBean.setUserId(user.get(User.MAIL_UID));
			
			try {
				contentVo = bbsManager.readBoardContent(folder, contentVo, infoBean);
			} catch (NumberFormatException e) {
				throw new BoardContentNotFoundException(resource.getMessage("bbs.content.notfound"));
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				if (store != null && store.isConnected()) {
					store.close();
				}
			}
		}
		
		if (StringUtils.isNotEmpty(keyWord)) {
			confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			
			try {
				store = factory.connect(request.getRemoteAddr(), confMap);
				bbsManager.setCommandResource(store, Integer.toString(bbsId), getMessageResource());
				
				MessageSortInfoBean sortBean = new MessageSortInfoBean();
				sortBean.setSortBy("date");
				sortBean.setSortDir("desc");
				sortBean.setAdSearchCategory(searchType);
				sortBean.setAdvanceMode(true);
				sortBean.setAdSearchPattern(keyWord);				
				sortBean.setPage(Integer.toString(currentPage));
				sortBean.setPageBase(Integer.toString(pageBase));
				
				if ("c".equals(searchType)) {
					sortBean.setAdFromEmailPattern(keyWord);	
				}
				MailSortMessageBean[] messageBeans = bbsManager.getXSortMessageBeans(Integer.toString(bbsId), sortBean);
	
				if (messageBeans != null && messageBeans.length > 0) {
					String[] messageIds = new String[messageBeans.length];
					for (int i=0; i<messageBeans.length; i++) {
						messageIds[i] = messageBeans[i].getMessage().getMessageID();
					}
					prevContent = bbsManager.readPrevBoardContentByMessageId(bbsId, contentId, parentId, orderNo, domainSeq, creatorSeq, messageIds);
					nextContent = bbsManager.readNextBoardContentByMessageId(bbsId, contentId, parentId, orderNo, domainSeq, creatorSeq, messageIds);
				}
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				if (store != null && store.isConnected()) {
					store.close();
				}
			}
		} else {
			prevContent = bbsManager.readPrevBoardContent(bbsId, contentId, parentId, orderNo, domainSeq, creatorSeq, searchType, keyWord);
			nextContent = bbsManager.readNextBoardContent(bbsId, contentId, parentId, orderNo, domainSeq, creatorSeq, searchType, keyWord);
		}
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		
		if(!"print".equals(readType)) {
			bbsManager.modifyHitCount(bbsId, contentId, domainSeq);
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("bbsId", bbsId);
		session.setAttribute("contentId", contentId);
		session.setAttribute("parentId", parentId);
		
		keyWord = StringReplaceUtil.replaceHtml(keyWord);
		request.setAttribute("readType", readType);
		return returnType;
	}
	
	public List<BoardContentVO> getPrevContent() {
		return prevContent;
	}

	public void setPrevContent(List<BoardContentVO> prevContent) {
		this.prevContent = prevContent;
	}

	public List<BoardContentVO> getNextContent() {
		return nextContent;
	}

	public void setNextContent(List<BoardContentVO> nextContent) {
		this.nextContent = nextContent;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
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

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public BbsManager getBbsManager() {
		return bbsManager;
	}
	
	public List<BoardVO> getBoardList() {
		return boardList;
	}

	public void setBoardList(List<BoardVO> boardList) {
		this.boardList = boardList;
	}
	
	public BoardContentVO getContentVo() {
		return contentVo;
	}

	public void setContentVo(BoardContentVO contentVo) {
		this.contentVo = contentVo;
	}

	public BoardVO getBoardVo() {
		return boardVo;
	}

	public void setBoardVo(BoardVO boardVo) {
		this.boardVo = boardVo;
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
}
