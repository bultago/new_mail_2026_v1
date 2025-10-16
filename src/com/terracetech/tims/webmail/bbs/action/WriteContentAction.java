package com.terracetech.tims.webmail.bbs.action;

import java.io.PrintWriter;
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
import com.terracetech.tims.webmail.bbs.vo.AttachFileVO;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringReplaceUtil;

public class WriteContentAction extends BaseAction {

	private BbsManager bbsManager = null;
	private List boardList = null;
	private BoardVO boardVo = null;
	private BoardContentVO contentVo = null;
	private List<AttachFileVO> attList = null;

	private boolean isReply = false;
	private boolean isModify = false;

	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;
	private int depth = 0;
	private int orderNo = 0;
	
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;

	public String execute() throws Exception {

		User user = UserAuthManager.getUser(request);
		I18nResources resource = getMessageResource("bbs");
		
		HttpSession session = request.getSession();
		Object objBbsId = session.getAttribute("bbsId");
		Object objContentId = session.getAttribute("contentId");
		Object objParentId = session.getAttribute("parentId");
		if (objBbsId != null) {
			bbsId = (Integer)objBbsId;
		}
		if (objContentId != null) {
			contentId = (Integer)objContentId;
		}
		if (objParentId != null) {
			parentId = (Integer)objParentId;
		}
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		boardList = bbsManager.readBoardList(domainSeq);
		boardVo = bbsManager.readBoard(bbsId, domainSeq);
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		
		if ("ADMIN".equals(boardVo.getWriteAuth())) { 
		    if (("F".equals(bbsAuth.get("isAdmin"))) && ("F".equals(bbsAuth.get("isCreator")))) {
	                    request.setAttribute("errMsg", resource.getMessage("bbs.error.001"));
	                    return "alertback";
	            } 		    
		}		
		
		return "success";
	}
	
	public String executeModify() throws Exception {
		
		User user = UserAuthManager.getUser(request);
		I18nResources resource = getMessageResource("bbs");
		
		HttpSession session = request.getSession();
		Object objBbsId = session.getAttribute("bbsId");
		Object objContentId = session.getAttribute("contentId");
		Object objParentId = session.getAttribute("parentId");
		if (objBbsId != null) {
			bbsId = (Integer)objBbsId;
		}
		if (objContentId != null) {
			contentId = (Integer)objContentId;
		}
		if (objParentId != null) {
			parentId = (Integer)objParentId;
		}
		
		String msg = "";
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		BoardContentVO contentVoTmp = new BoardContentVO();
		contentVoTmp = bbsManager.readBoardContent(bbsId, contentId, mailDomainSeq);
		if (Integer.parseInt(user.get(User.MAIL_USER_SEQ)) !=  contentVoTmp.getCreatorSeq() ) {
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			msg = resource.getMessage("bbs.error.001");
			String url = "/bbs/listContent.do?bbsId="+bbsId;
			out.print(MakeMessage.printAlertRedirect(msg, url));
		    out.flush();
		    return null;
		}
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		boardList = bbsManager.readBoardList(domainSeq);
		boardVo = bbsManager.readBoard(bbsId, domainSeq);
		contentVo = bbsManager.readBoardContent(bbsId, contentId, domainSeq);
		
		if ("text".equals(contentVo.getContentType())) {
			String content = "";
			if (contentVo.getContent() != null && contentVo.getContent().length > 0) {
				content = new String((contentVo.getContent()), "UTF-8");
				//TCUSTOM-3262 201706
				StringReplaceUtil sr = new StringReplaceUtil();
				content = sr.replace(content, false, true);
			}
			contentVo.setHtmlContent(content);
			
		} else {
			TMailStoreFactory factory = new TMailStoreFactory();
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			TMailStore store = factory.connect(request.getRemoteAddr(), confMap);
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
			
			try {
				contentVo = bbsManager.writeModifyContent(folder, contentVo, infoBean);
				folder.open(false);
				attList = bbsManager.makeAttachTmpFile(contentVo.getAttachFiles(), EnvConstants.getBasicSetting("tmpdir"), user.get(User.EMAIL));
				folder.close(false);
			} catch (NumberFormatException e) {
				LogManager.writeErr(this, e.getMessage(), e);
				throw new BoardContentNotFoundException(resource.getMessage("bbs.content.notfound"));
			} catch (Exception e) {
				LogManager.writeErr(this, e.getMessage(), e);
			}finally {
				if (store != null && store.isConnected()) {
					store.close();
				}
			}
		}
		
		Map bbsAuth = bbsManager.getBoardAuth(bbsId, domainSeq, Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
		if ("T".equals(bbsAuth.get("isAdmin"))) {
			bbsAdmin = true;
		}
		if ("T".equals(bbsAuth.get("isCreator"))) {
			bbsCreator = true;
		}
		return "success";
	}
	
	public List<AttachFileVO> getAttList() {
		return attList;
	}

	public void setAttList(List<AttachFileVO> attList) {
		this.attList = attList;
	}

	public BoardContentVO getContentVo() {
		return contentVo;
	}

	public void setContentVo(BoardContentVO contentVo) {
		this.contentVo = contentVo;
	}
	
	public boolean getIsModify() {
		return isModify;
	}
	
	public void setIsModify(boolean isModify) {
		this.isModify = isModify;
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
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	
	public boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	public BoardVO getBoardVo() {
		return boardVo;
	}

	public void setBoardVo(BoardVO boardVo) {
		this.boardVo = boardVo;
	}
	
	public BbsManager getBbsManager() {
		return bbsManager;
	}

	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}

	public List getBoardList() {
		return boardList;
	}

	public void setBoardList(List boardList) {
		this.boardList = boardList;
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
