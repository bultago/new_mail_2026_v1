package com.terracetech.tims.service.tms.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.service.tms.IBbsService;
import com.terracetech.tims.service.tms.vo.BbsContentCondVO;
import com.terracetech.tims.service.tms.vo.BbsContentInfoVO;
import com.terracetech.tims.service.tms.vo.BbsContentListVO;
import com.terracetech.tims.service.tms.vo.BbsContentReplyVO;
import com.terracetech.tims.service.tms.vo.BbsContentVO;
import com.terracetech.tims.service.tms.vo.BbsInfoVO;
import com.terracetech.tims.service.tms.vo.BbsSaveCondVO;
import com.terracetech.tims.service.tms.vo.BbsViewContentCondVO;
import com.terracetech.tims.service.tms.vo.BbsWriteCondVO;
import com.terracetech.tims.service.tms.vo.BbsWriteInfoVO;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentReplyVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.MailUserManager;
import com.terracetech.tims.webmail.util.ApplicationBeanUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

public class BbsService implements IBbsService {

	public BbsManager bbsManager = null;

	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public BbsInfoVO[] readBbsList(String email, User user) {
		
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		
		BbsInfoVO[] bbsInfoArray = null;
		
		List<BoardVO> bbsList = bbsManager.readBoardList(mailDomainSeq);
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		String today = DATE_FORMAT.format(new Date());
		
		if (bbsList != null && bbsList.size() > 0) {
			bbsInfoArray = new BbsInfoVO[bbsList.size()];
			for (int i=0; i<bbsList.size(); i++) {
				
				bbsInfoArray[i] = convertBbsVo(bbsList.get(i),mailDomainSeq,today);
			}
		}
		return bbsInfoArray;
	}
	
	public BbsContentInfoVO readBbsContentList(BbsContentCondVO bbsContentCondVo, User user) {
		String email = bbsContentCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		BbsContentInfoVO bbsContentInfoVo = new BbsContentInfoVO();
		I18nResources msgResource = new I18nResources(new Locale(authUser.get(User.LOCALE)));
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		String searchType = bbsContentCondVo.getSearchType();
		searchType = (StringUtils.isEmpty(searchType)) ? "s" : searchType;
		String keyWord = bbsContentCondVo.getKeyWord();
		keyWord = (StringUtils.isEmpty(keyWord)) ? "" : keyWord;
		int currentPage = bbsContentCondVo.getCurrentPage();
		currentPage = (currentPage == 0) ? 1 : currentPage;
		
		int pageBase = (bbsContentCondVo.getPageBase() == 0) ? 15 : bbsContentCondVo.getPageBase();
		int noticeCount = (bbsContentCondVo.getNoticeCount() == 0) ? 5 : bbsContentCondVo.getNoticeCount();
		int skipResult = (currentPage - 1) * pageBase;
		
		String remoteIp = bbsContentCondVo.getRemoteIp();
		if (StringUtils.isEmpty(remoteIp)) {
			remoteIp = "127.0.0.1";
		}
		
		int bbsId = bbsContentCondVo.getBbsId();
		BoardVO boardVo = bbsManager.readBoard(bbsId, mailDomainSeq);
		String bbsType = boardVo.getBbsType();
		
		boolean bbsAdmin = false;
		boolean bbsCreator = false;
		Map<String, String> bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, mailUserSeq);
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsType)) {
			creatorSeq = mailUserSeq;
		}
		
		int contentListCount = 0;
		List<BoardContentVO> contentList = null;
		List<BoardContentVO> contentnoticeList = bbsManager.readBoardContentNoticeList(bbsId, mailDomainSeq, noticeCount);
		if (StringUtils.isEmpty(keyWord) || "secret".equalsIgnoreCase(bbsType)) {
			if ("7".equals(searchType)) {
				keyWord = authUser.get(User.MAIL_USER_SEQ);
			}
			contentList = bbsManager.readBoardContentList(bbsId, mailDomainSeq, creatorSeq, searchType, keyWord, skipResult, pageBase);
			contentListCount = bbsManager.readBoardContentListCount(bbsId, mailDomainSeq, creatorSeq, searchType, keyWord);
		} else {
			TMailStoreFactory factory = new TMailStoreFactory();
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(authUser.get(User.MAIL_DOMAIN));
			TMailStore store = null;
		
			try {
				store = factory.connect(remoteIp, confMap);
				bbsManager.setCommandResource(store, Integer.toString(bbsId), msgResource);
				
				MessageSortInfoBean sortBean = new MessageSortInfoBean();
				sortBean.setSortBy("date");
				sortBean.setSortDir("desc");
				sortBean.setAdSearchCategory(searchType);
				sortBean.setAdvanceMode(true);
				sortBean.setAdSearchPattern(keyWord);
				sortBean.setAdFromEmailPattern(keyWord);
				sortBean.setAdToEmailPattern(keyWord);			
				sortBean.setPage(Integer.toString(currentPage));
				sortBean.setPageBase(Integer.toString(pageBase));
				sortBean.setOperation("or");
				
				if ("c".equals(searchType)) {
					sortBean.setAdFromEmailPattern(keyWord);	
				}
				
				MailSortMessageBean[] messageBeans = bbsManager.getXSortMessageBeans(Integer.toString(bbsId), sortBean);
				contentListCount = bbsManager.getXCommandTotal();
				if (messageBeans != null && messageBeans.length > 0) {
					String[] messageIds = new String[messageBeans.length];
					for (int i=0; i<messageBeans.length; i++) {
						messageIds[i] = messageBeans[i].getMessage().getMessageID();
					}
					contentList = bbsManager.readBoardContentListByMessageIds(bbsId, mailDomainSeq, messageIds);
				}
	
				bbsManager.readBoardContentList(bbsContentCondVo.getBbsId(), mailDomainSeq, creatorSeq, searchType, keyWord, skipResult, pageBase);
			
			}catch (Exception e) {
				// TODO: handle exception
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				try {
					if(store != null && store.isConnected()) store.close();
				} catch (MessagingException ignore) {
					LogManager.writeErr(this, ignore.getMessage(), ignore);
				}
			}
		}

		BbsContentListVO[] noticeContentArray = null;
		if (contentnoticeList != null && contentnoticeList.size() > 0) {
			noticeContentArray = new BbsContentListVO[contentnoticeList.size()];
			for (int i=0; i<contentnoticeList.size(); i++) {
				noticeContentArray[i] = convertBbsContentVo(contentnoticeList.get(i));
			}
		}
		
		BbsContentListVO[] bbsContentArray = null;
		if (contentList != null && contentList.size() > 0) {
			bbsContentArray = new BbsContentListVO[contentList.size()];
			for (int i=0; i<contentList.size(); i++) {
				bbsContentArray[i] = convertBbsContentVo(contentList.get(i));
			}
		}
		
		bbsContentInfoVo.setBbsId(boardVo.getBbsId());
		bbsContentInfoVo.setBbsName(boardVo.getBbsName());
		bbsContentInfoVo.setTotalCount(contentListCount);
		bbsContentInfoVo.setNoticeContentList(noticeContentArray);
		bbsContentInfoVo.setBbsContentList(bbsContentArray);
		bbsContentInfoVo.setBbsType(bbsType);
		bbsContentInfoVo.setWriteAuth(boardVo.getWriteAuth());
		bbsContentInfoVo.setBbsAdmin(bbsAdmin);
		bbsContentInfoVo.setBbsCreator(bbsCreator);
		
		return bbsContentInfoVo;
	}
	
	public BbsContentVO writeBbsContent(BbsContentVO bbsWriteCondVO, User user) {
		String email = bbsWriteCondVO.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);

		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int bbsId = bbsWriteCondVO.getBbsId();
		int contentId = bbsWriteCondVO.getContentId();
		
		boolean bbsAdmin = false;
		boolean bbsCreator = false;
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}

		BoardVO boardVo = bbsManager.readBoard(bbsId, mailDomainSeq);
		
		BbsContentVO bbsContentVo = new BbsContentVO();
		bbsContentVo.setBbsId(boardVo.getBbsId());
		bbsContentVo.setBbsName(boardVo.getBbsName());
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setBbsAdmin(bbsAdmin);
		bbsContentVo.setCreator(bbsCreator);
		bbsContentVo.setBbsType(boardVo.getBbsType());
		bbsContentVo.setParentId(bbsWriteCondVO.getParentId());
		bbsContentVo.setReply(bbsWriteCondVO.isReply());
		bbsContentVo.setDepth(bbsWriteCondVO.getDepth());
		bbsContentVo.setOrderNo(bbsWriteCondVO.getOrderNo());
		
				
		return bbsContentVo;
	}
	
	private BbsInfoVO convertBbsVo(BoardVO boardVO, int domainSeq, String today) {
		BbsInfoVO bbsInfoVO = new BbsInfoVO();
		bbsInfoVO.setBbsId(boardVO.getBbsId());
		bbsInfoVO.setBbsName(boardVO.getBbsName());
		bbsInfoVO.setBbsType(boardVO.getBbsType());
		
		String bbsFullId = boardVO.getBbsFullId();
		if (StringUtils.isEmpty(bbsFullId)) {
			bbsInfoVO.setBbsDepth(0);
		} else {
			bbsInfoVO.setBbsDepth(bbsFullId.split("\\|").length-1);
		}
		
		bbsInfoVO.setBbsFullId(bbsFullId);
		boolean existNewContent = bbsManager.existNewContent(boardVO.getBbsId(),domainSeq, today) ;
		bbsInfoVO.setExistNew(existNewContent);
			
		return bbsInfoVO;
	}
	
	private BbsContentListVO convertBbsContentVo(BoardContentVO boardContentVo) {
		BbsContentListVO bbsContentVo = new BbsContentListVO();
		bbsContentVo.setBbsId(boardContentVo.getBbsId());
		bbsContentVo.setContentId(boardContentVo.getContentId());
		bbsContentVo.setSubject(boardContentVo.getSubject());
		bbsContentVo.setAttCnt(boardContentVo.getAttCnt());
		bbsContentVo.setRefCnt(boardContentVo.getRefCnt());
		bbsContentVo.setDepth(boardContentVo.getDepth());
		bbsContentVo.setCreatorName(boardContentVo.getCreatorName());
		bbsContentVo.setCreateTime(boardContentVo.getCreateTime());
		bbsContentVo.setParentId(boardContentVo.getParentId());
		bbsContentVo.setOrderNo(boardContentVo.getOrderNo());
		return bbsContentVo;
	}
	
	private User getAuthUser(String email, User user) {
		User authUser = null;
		if (user == null) {
			String[] data = email.split("@");
			MailUserManager mailUserManager = (MailUserManager)ApplicationBeanUtil.getApplicationBean("mailUserManager");
			authUser = mailUserManager.readUserAuthInfo(data[0], data[1]);
		} else {
			authUser = user;
		}
		
		return authUser;
	}
	public void saveBbsContent(BbsSaveCondVO bbsSaveCondVo, User user){
		
		
		String email = bbsSaveCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		String name = user.get(User.USER_NAME);		
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		int bbsId = bbsSaveCondVo.getBbsId();
		String creatorName = bbsSaveCondVo.getCreatorName();
		
		if ("any".equals(bbsSaveCondVo.getBbsType())) {
			name = creatorName;
			mailUserSeq = 0;
		}
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		try{
			BoardContentVO contentVo = null;
			MimeMessage mimeMessage = bbsManager.getBbsMimeMessage(null, bbsSaveCondVo.getContent(), "", "UTF-8", EnvConstants.getBasicSetting("tmpdir"));

			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(bbsSaveCondVo.getRemoteIp(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			if (!folder.exists()) {
				folder.create();
			}
			Map<String, String> resultMap = bbsManager.setMimeToFolder(mimeMessage, folder, bbsSaveCondVo.getSubject(), email, name, "UTF-8");
			String msgId = resultMap.get("msgId");
			String contentType = resultMap.get("contentType");	
			
			contentVo = new BoardContentVO();
			contentVo.setBbsId(bbsId);
			contentVo.setMailDomainSeq(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
			contentVo.setContentId(bbsSaveCondVo.getContentId());
			contentVo.setParentId(bbsSaveCondVo.getParentId());
			contentVo.setSubject(bbsSaveCondVo.getSubject());
			contentVo.setHtmlContent(bbsSaveCondVo.getContent());
			contentVo.setMsgId(msgId);
			contentVo.setCreatorName(name);
			contentVo.setCreatorSeq(mailUserSeq);
			contentVo.setIp(bbsSaveCondVo.getRemoteIp());
			contentVo.setIsNotice(bbsSaveCondVo.getIsNotice());
			contentVo.setOrderNo(bbsSaveCondVo.getOrderNo());
			contentVo.setDepth(bbsSaveCondVo.getDepth());
			contentVo.setContentType(contentType);
			contentVo.setIsNotice(bbsSaveCondVo.getIsNotice());
			
			bbsManager.saveBoardContent(contentVo, "1", bbsSaveCondVo.isReply(), null);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (store != null && store.isConnected()) store.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void updateBbsContent(BbsSaveCondVO bbsSaveCondVo, User user){
		String email = bbsSaveCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		String name = user.get(User.USER_NAME);		
		
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int bbsId = bbsSaveCondVo.getBbsId();
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		
		try{
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(bbsSaveCondVo.getRemoteIp(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			int[] contentIds = {bbsSaveCondVo.getContentId()};
			String[] msgIds = bbsManager.readBoardContentMsgIds(bbsId, contentIds, mailDomainSeq);
			
			bbsManager.deleteBoardMimeContent(folder, bbsId, contentIds, msgIds);
			bbsManager.deleteAttachFile(bbsId, contentIds, mailDomainSeq);
			
			MimeMessage mimeMessage = bbsManager.getBbsMimeMessage(null, bbsSaveCondVo.getContent(), "", "UTF-8", EnvConstants.getBasicSetting("tmpdir"));
			Map<String, String> resultMap = bbsManager.setMimeToFolder(mimeMessage, folder, bbsSaveCondVo.getSubject(), email, name, "UTF-8");
			String msgId = resultMap.get("msgId");
			String contentType = resultMap.get("contentType");
			
			BoardContentVO contentVo = new BoardContentVO();
			contentVo.setBbsId(bbsId);
			contentVo.setMailDomainSeq(mailDomainSeq);
			contentVo.setContentId(bbsSaveCondVo.getContentId());
			contentVo.setSubject(bbsSaveCondVo.getSubject());
			contentVo.setHtmlContent(bbsSaveCondVo.getContent());
			contentVo.setMsgId(msgId);
			contentVo.setCreatorName(user.get(User.USER_NAME));
			contentVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
			contentVo.setIp(bbsSaveCondVo.getRemoteIp());
			contentVo.setIsNotice(bbsSaveCondVo.getIsNotice());
			contentVo.setContentType(contentType);
			
			bbsManager.modifyBoardContent(contentVo, "1", null);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (store != null && store.isConnected()) store.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public BbsContentVO readBoardContent(BbsViewContentCondVO bbsViewContentCondVo, User user){
		
		String email = bbsViewContentCondVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		I18nResources msgResource = new I18nResources(new Locale(authUser.get(User.LOCALE)));
		
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int bbsId = bbsViewContentCondVo.getBbsId();
		int contentId = bbsViewContentCondVo.getContentId();
		int parentId = bbsViewContentCondVo.getParentId();
		int orderNo = bbsViewContentCondVo.getOrderNo();
		
		String remoteIp = bbsViewContentCondVo.getRequest().getRemoteAddr();
		String contextPath = bbsViewContentCondVo.getContext().getRealPath("/");
		String requestScheme = bbsViewContentCondVo.getRequest().getScheme();
		String requestServerName = bbsViewContentCondVo.getRequest().getServerName();
		int requestServerPort = bbsViewContentCondVo.getRequest().getServerPort();
		
		String searchType = bbsViewContentCondVo.getSearchType();
		searchType = (StringUtils.isEmpty(searchType)) ? "s" : searchType;
		String keyWord = bbsViewContentCondVo.getKeyWord();
		keyWord = (StringUtils.isEmpty(keyWord)) ? "" : keyWord;
		int currentPage = bbsViewContentCondVo.getCurrentPage();
		currentPage = (currentPage == 0) ? 1 : currentPage;
		int pageBase = (bbsViewContentCondVo.getPageBase() == 0) ? 15 : bbsViewContentCondVo.getPageBase();
		
		
		
		BoardContentVO boardContentVo = null;
		boardContentVo = bbsManager.readBoardContent(bbsId, contentId, mailDomainSeq);
		
		String contentCreator = bbsManager.readContentMailUid(boardContentVo.getCreatorSeq());
		if(StringUtils.isEmpty(contentCreator)){
			contentCreator = "";
		}else{
			contentCreator = contentCreator+"@"+authUser.get(User.MAIL_DOMAIN);
		}
		
		@SuppressWarnings("unused")
		BoardVO boardVo = null;
		boardVo = bbsManager.readBoard(bbsId, mailDomainSeq);
		
		if (mailUserSeq == boardContentVo.getCreatorSeq()) {
			boardContentVo.setCreator(true);
		}
		
		boolean bbsAdmin = false;
		boolean bbsCreator = false;
		List<BoardContentVO> prevContent = null;
		List<BoardContentVO> nextContent = null;
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, mailUserSeq);
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		
		int creatorSeq = 0;
		if (!bbsAdmin && "secret".equals(bbsViewContentCondVo.getBbsType())) {
			creatorSeq = mailUserSeq;
		}
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		Map<String, String> confMap = null;
		
		if ("text".equals(boardContentVo.getContentType())) {
			String content = "";
			if (boardContentVo.getContent() != null && boardContentVo.getContent().length > 0) {
				try {
					content = new String((boardContentVo.getContent()), "UTF-8");					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			boardContentVo.setHtmlContent(content);
		}else{
			
			confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			try {
				store = factory.connect(remoteIp, confMap);
			
				TMailFolder folder = store.getFolder(Integer.toString(bbsId));
				
				String attachesDir = contextPath + EnvConstants.getAttachSetting("attach.dir");
				String hostStr = requestScheme +"://"+ requestServerName +":"+ requestServerPort;
				MessageParserInfoBean infoBean = new MessageParserInfoBean();
				infoBean.setAttachesDir(attachesDir);
				infoBean.setAttachesUrl(EnvConstants.getAttachSetting("attach.url"));
				infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
				infoBean.setDefaultImg("/design/common/images/blank.gif");
				infoBean.setHiddenImg(false);
				infoBean.setHiddenTag(false);
				infoBean.setLocale(new Locale(EnvConstants.getMailSetting("default.charset")));
				infoBean.setStrLocalhost(hostStr);
				infoBean.setUserId(user.get(User.MAIL_UID));
				
					
				boardContentVo = bbsManager.readBoardContent(folder, boardContentVo, infoBean);
										
				 
			} catch (Exception e1) {
				LogManager.writeErr(this, e1.getMessage(), e1);
				e1.printStackTrace();
			} finally {
				try {
					if (store != null && store.isConnected()) {
						store.close();
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (StringUtils.isEmpty(remoteIp)) {
			remoteIp = "127.0.0.1";
		}
		
		if (StringUtils.isNotEmpty(bbsViewContentCondVo.getKeyWord())) {
			confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			
			try {
				store = factory.connect(remoteIp, confMap);
				bbsManager.setCommandResource(store, Integer.toString(bbsId), msgResource);
				
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
					prevContent = bbsManager.readPrevBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
					nextContent = bbsManager.readNextBoardContentByMessageId(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, messageIds);
				}
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			} finally {
				try {
					if (store != null && store.isConnected()) {
						store.close();
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			prevContent = bbsManager.readPrevBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
			nextContent = bbsManager.readNextBoardContent(bbsId, contentId, parentId, orderNo, mailDomainSeq, creatorSeq, searchType, keyWord);
		}
		
		bbsManager.modifyHitCount(bbsId, contentId, mailDomainSeq);
		BbsContentVO bbsContentVo = new BbsContentVO();
		bbsContentVo.setMailDomainSeq(mailDomainSeq);
		bbsContentVo.setIsNotice(boardContentVo.getIsNotice());
		bbsContentVo.setContentId(contentId);
		bbsContentVo.setParentId(parentId);
		bbsContentVo.setDepth(boardContentVo.getDepth());
		bbsContentVo.setOrderNo(orderNo);
		bbsContentVo.setSubject(boardContentVo.getSubject());
		bbsContentVo.setRefCnt(boardContentVo.getRefCnt());
		bbsContentVo.setHtmlContent(boardContentVo.getHtmlContent());
		bbsContentVo.setCreatorName(boardContentVo.getCreatorName());
		bbsContentVo.setBbsAdmin(bbsAdmin);
		bbsContentVo.setBbsCreator(bbsCreator);
		bbsContentVo.setCreateTime(boardContentVo.getCreateTime());
		bbsContentVo.setAttachFiles(boardContentVo.getAttachFiles());
		bbsContentVo.setEmail(contentCreator);
		bbsContentVo.setBbsId(boardContentVo.getBbsId());
		bbsContentVo.setBbsUid(boardContentVo.getBbsUid());
		bbsContentVo.setCurrentPage(currentPage);
		bbsContentVo.setSearchType(searchType);
		bbsContentVo.setKeyWord(keyWord);
		bbsContentVo.setBbsType(bbsViewContentCondVo.getBbsType());
		bbsContentVo.setWriteAuth(boardVo.getWriteAuth());
		bbsContentVo.setCreator(boardContentVo.isCreator());
		bbsContentVo.setBbsName(boardVo.getBbsName());
		
		BbsContentVO[] prevContentArray = null;
		if (prevContent != null && prevContent.size() > 0) {
			prevContentArray = new BbsContentVO[prevContent.size()];
			for (int i=0; i<prevContent.size(); i++) {
				prevContentArray[i] = convertPrevNextBbsContentVo(prevContent.get(i));
			}
		}
		
		BbsContentVO[] nextContentArray = null;
		if (nextContent != null && nextContent.size() > 0) {
			nextContentArray = new BbsContentVO[nextContent.size()];
			for (int i=0; i<nextContent.size(); i++) {
				nextContentArray[i] = convertPrevNextBbsContentVo(nextContent.get(i));
			}
		}
		
		bbsContentVo.setPrevContent(prevContentArray);
		bbsContentVo.setNextContent(nextContentArray);
			
		return bbsContentVo;
	}
	private BbsContentVO convertPrevNextBbsContentVo(BoardContentVO boardContentVo) {
		BbsContentVO bbsContentVo = new BbsContentVO();
		bbsContentVo.setBbsId(boardContentVo.getBbsId());
		bbsContentVo.setContentId(boardContentVo.getContentId());
		bbsContentVo.setSubject(boardContentVo.getSubject());
		bbsContentVo.setAttCnt(boardContentVo.getAttCnt());
		bbsContentVo.setRefCnt(boardContentVo.getRefCnt());
		bbsContentVo.setDepth(boardContentVo.getDepth());
		bbsContentVo.setCreatorName(boardContentVo.getCreatorName());
		bbsContentVo.setCreateTime(boardContentVo.getCreateTime());
		bbsContentVo.setParentId(boardContentVo.getParentId());
		bbsContentVo.setOrderNo(boardContentVo.getOrderNo());
		return bbsContentVo;
	}
	
	public BbsContentVO writeBbsModifyContent(BbsContentVO bbsContentVo, User user){
		
		String email = bbsContentVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		I18nResources msgResource = new I18nResources(new Locale(authUser.get(User.LOCALE)));
		TMailStore store = null;
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int bbsId = bbsContentVo.getBbsId();
		int contentId = bbsContentVo.getContentId();
		int parentId = bbsContentVo.getParentId();
		int orderNo = bbsContentVo.getOrderNo();
		String remoteIp = bbsContentVo.getRequest().getRemoteAddr();
				
		BoardContentVO contentVo = null;
		contentVo = bbsManager.readBoardContent(bbsId, contentId, mailDomainSeq);
		BoardVO boardVo = bbsManager.readBoard(bbsId, mailDomainSeq);
		
		if ("text".equals(contentVo.getContentType())) {
			String content = "";
			if (contentVo.getContent() != null && contentVo.getContent().length > 0) {
				try {
					content = new String((contentVo.getContent()), "UTF-8");					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			contentVo.setHtmlContent(content);
			
		} else {
			TMailStoreFactory factory = new TMailStoreFactory();
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(authUser.get(User.MAIL_DOMAIN));
		
			try {
				
				store = factory.connect(remoteIp, confMap);
				TMailFolder folder = store.getFolder(Integer.toString(bbsId));
				
				
				MessageParserInfoBean infoBean = new MessageParserInfoBean();
				infoBean.setAttachesDir(EnvConstants.getBasicSetting("tmpdir"));
				infoBean.setAttachesUrl(EnvConstants.getAttachSetting("upimage.url"));
				infoBean.setDefaultCharset(EnvConstants.getMailSetting("default.charset"));
				infoBean.setDefaultImg("/design/common/images/blank.gif");
				infoBean.setHiddenImg(false);
				infoBean.setHiddenTag(false);
				infoBean.setLocale(new Locale(EnvConstants.getMailSetting("default.charset")));
				infoBean.setUserId(user.get(User.MAIL_UID));
				
				
				contentVo = bbsManager.writeModifyContent(folder, contentVo, infoBean);
				folder.open(false);
				//attList = bbsManager.makeAttachTmpFile(contentVo.getAttachFiles(), EnvConstants.getBasicSetting("tmpdir"), user.get(User.EMAIL));
				folder.close(false);
			} catch (NumberFormatException e) {
				LogManager.writeErr(this, e.getMessage(), e);				
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}finally {
				try {
					if (store != null && store.isConnected()) {
						store.close();
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				
		boolean bbsAdmin = false;
		boolean bbsCreator = false;
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
	
		
		BbsContentVO ContentVo = new BbsContentVO();
		ContentVo.setBbsId(boardVo.getBbsId());
		ContentVo.setSubject(contentVo.getSubject());
		ContentVo.setContentId(contentId);
		ContentVo.setBbsAdmin(bbsAdmin);
		ContentVo.setCreator(bbsCreator);
		ContentVo.setBbsType(boardVo.getBbsType());
		ContentVo.setParentId(contentVo.getParentId());
		ContentVo.setDepth(contentVo.getDepth());
		ContentVo.setOrderNo(contentVo.getOrderNo());
		ContentVo.setHtmlContent(contentVo.getHtmlContent());
		ContentVo.setBbsName(boardVo.getBbsName());
		ContentVo.setIsNotice(contentVo.getIsNotice());
		
		
		return ContentVo;
	}
	
	public void deleteBbsModifyContent(BbsContentVO contentVo, User user){
		
		String email = contentVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		String maildomain = authUser.get(User.MAIL_DOMAIN);
		int bbsId = contentVo.getBbsId();
		
		int[] contentIds = {contentVo.getContentId()};
		String remoteIp = contentVo.getRequest().getRemoteAddr();
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		
		try {
						
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(maildomain);
			store = factory.connect(remoteIp, confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
	
			String[] msgIds = bbsManager.readBoardContentMsgIds(bbsId, contentIds, mailDomainSeq);
			bbsManager.deleteBoardMimeContent(folder, bbsId, contentIds, msgIds);
			bbsManager.deleteBoardDBContent(bbsId, contentIds, mailDomainSeq);
			
		} catch (NumberFormatException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			e.printStackTrace();			
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (store != null && store.isConnected()) {
					store.close();
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public int readContentReplyCount(BbsContentVO bbsContentVo, User user){
		String email = bbsContentVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int bbsId = bbsContentVo.getBbsId();
		int contentId = bbsContentVo.getContentId();
		int isNotice = bbsContentVo.getIsNotice();
		
		return  bbsManager.getContentReplyListCount(bbsId, contentId, isNotice, mailDomainSeq);
	}
	
	public BbsContentReplyVO[] readContentReply(BbsContentVO bbsContentVo,User user,int skipResult,int pbase){
		
		String email = bbsContentVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		int bbsId = bbsContentVo.getBbsId();
		int contentId = bbsContentVo.getContentId();
		int isNotice = bbsContentVo.getIsNotice();
		boolean bbsAdmin = false;
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, mailDomainSeq, mailUserSeq);
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		
				
		List<BoardContentReplyVO> boardContentReplyList = null;
		
		boardContentReplyList =  bbsManager.getContentReplyList(bbsId, contentId, isNotice, mailDomainSeq, skipResult, pbase);
		
		BbsContentReplyVO[] bbsContentReplyArray = null;
		if (boardContentReplyList != null && boardContentReplyList.size() > 0) {
			bbsContentReplyArray = new BbsContentReplyVO[boardContentReplyList.size()];
			for (int i=0; i<boardContentReplyList.size(); i++) {
				bbsContentReplyArray[i] = convertBbsContentReplyVo(boardContentReplyList.get(i),bbsAdmin);
			}
		}
		
		return bbsContentReplyArray;
	}
	public BbsContentReplyVO convertBbsContentReplyVo(BoardContentReplyVO boardConetntReplyVo,boolean bbsAdmin){
		
		BbsContentReplyVO bbsContentReplyVo = new BbsContentReplyVO();
		bbsContentReplyVo.setBbsId(boardConetntReplyVo.getBbsId());
		bbsContentReplyVo.setContent(boardConetntReplyVo.getContent());
		bbsContentReplyVo.setContentId(boardConetntReplyVo.getContentId());
		bbsContentReplyVo.setCreateTime(boardConetntReplyVo.getCreateTime());
		bbsContentReplyVo.setCreatorName(boardConetntReplyVo.getCreatorName());
		bbsContentReplyVo.setCreatorSeq(boardConetntReplyVo.getCreatorSeq());
		bbsContentReplyVo.setIp(boardConetntReplyVo.getIp());
		bbsContentReplyVo.setMailDomainSeq(boardConetntReplyVo.getMailDomainSeq());
		bbsContentReplyVo.setReplyId(boardConetntReplyVo.getReplyId());
		bbsContentReplyVo.setBbsAdmin(bbsAdmin);
		
		return bbsContentReplyVo;		
	}
	
	public boolean saveBbsContentReply(BbsContentReplyVO bbsContentReplyVo, User user){
		
		boolean isSuccess = false;
		String email = bbsContentReplyVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));
		int mailUserSeq = Integer.parseInt(authUser.get(User.MAIL_USER_SEQ));
		String userName = authUser.get(User.USER_NAME);
		String remoteIp = bbsContentReplyVo.getRequest().getRemoteAddr();
		
		BoardContentReplyVO boardContentReplyVo = new BoardContentReplyVO();
		boardContentReplyVo.setBbsId(bbsContentReplyVo.getBbsId());
		boardContentReplyVo.setContentId(bbsContentReplyVo.getContentId());
		String content = bbsContentReplyVo.getContent().replaceAll("\n", "<br>\n");
		boardContentReplyVo.setContent(content);
		boardContentReplyVo.setMailDomainSeq(mailDomainSeq);
		boardContentReplyVo.setCreatorSeq(mailUserSeq);
		boardContentReplyVo.setCreatorName(userName);
		boardContentReplyVo.setIp(remoteIp);
		boardContentReplyVo.setCreateTime(FormatUtil.getBasicDateStr());
		
		try{
			bbsManager.saveContentReply(boardContentReplyVo);
			isSuccess = true;
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			e.printStackTrace();
			isSuccess = false;
		}
		
		return isSuccess;
		
	}
	public boolean deleteBbsContentReply(BbsContentReplyVO bbsContentVo,User user){
		boolean isSuccess = false;
		String email = bbsContentVo.getEmail();
		User authUser = getAuthUser(email, user);
		email = authUser.get(User.EMAIL);
		int mailDomainSeq = Integer.parseInt(authUser.get(User.MAIL_DOMAIN_SEQ));		
		
		BoardContentReplyVO boardContentReplyVo = new BoardContentReplyVO();
		boardContentReplyVo.setBbsId(bbsContentVo.getBbsId());
		boardContentReplyVo.setContentId(bbsContentVo.getContentId());
		boardContentReplyVo.setReplyId(bbsContentVo.getReplyId());
		boardContentReplyVo.setMailDomainSeq(mailDomainSeq);
		
		try{
			bbsManager.deleteContentReply(boardContentReplyVo);
			isSuccess = true;
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isSuccess = false;
		}
		
		return isSuccess;
		
	}
	
	
	
}
