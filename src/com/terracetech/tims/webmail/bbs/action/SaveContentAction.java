package com.terracetech.tims.webmail.bbs.action;

import java.io.PrintWriter;
import com.terracetech.tims.webmail.util.StringReplaceUtil;
import java.util.Map;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.VirusManager;
import com.terracetech.tims.webmail.common.vo.VirusCheckVO;
import com.terracetech.tims.webmail.exception.VirusCheckException;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.util.StringUtils;

public class SaveContentAction extends BaseAction {
	
	private static final long serialVersionUID = 20090415L;
	
	private BbsManager bbsManager = null;
	private VirusManager virusManager = null;
	private BoardContentVO contentVo = null;

	private String content = null;
	private String subject = null;
	private String attFiles = null;
	private String writeMode = null;
	private String creatorName = null;
	private String bbsType = null;
	private boolean isReply = false;
	private int isNotice = 0;
	private int orderNo = 0;
	private int depth = 0;
	private int bbsId = 0;
	private int contentId = 0;
	private int parentId = 0;

	public String execute() throws Exception {
		if (!checkPAID()) {
			return "paidError";
	    }
		I18nResources resource = getMessageResource("bbs");
		I18nResources commonResource = getMessageResource("common");
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		
		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		String email = user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN);
		String name = user.get(User.USER_NAME);
		String msg = "";
		
		HttpSession session = request.getSession();
		Object objBbsId = session.getAttribute("bbsId");
		if (objBbsId != null) {
			bbsId = (Integer)objBbsId; 
		}
		
		if ("any".equals(bbsType)) {
			name = creatorName;
			mailUserSeq = 0;
		}
		
		try {
		    // 2013.04.03 mskim - 게시판 글 작성 시 권한 강화 로직 추가
		    int boardLevel = bbsManager.getBoadrdLevel(bbsId, Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
		    if (boardLevel == 3 || boardLevel == 5) {
		        Map bbsAuth = bbsManager.getBoardAuth(bbsId, Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
                if (!"T".equals(bbsAuth.get("isAdmin")) && !"T".equals(bbsAuth.get("isCreator"))) {
                        msg = resource.getMessage("bbs.content.save.fail");
                        String url = "/bbs/listContent.do?bbsId="+bbsId;
                        out.print(MakeMessage.printAlertRedirect(msg, url));
                    out.flush();
                    return null;
                }       
		    }
		   		        		 		    
			if(bbsId == 1){
				Map bbsAuth = bbsManager.getBoardAuth(bbsId, Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
				if (!"T".equals(bbsAuth.get("isAdmin")) && !"T".equals(bbsAuth.get("isCreator"))) {
					msg = resource.getMessage("bbs.content.save.fail");
					String url = "/bbs/listContent.do?bbsId="+bbsId;
					out.print(MakeMessage.printAlertRedirect(msg, url));
				    out.flush();
				    return null;
				}				
			}

			if (StringUtils.isNotEmpty(attFiles)) {
				String host = EnvConstants.getVirusSetting("virus.server");
				String portStr = EnvConstants.getVirusSetting("virus.server.port");
				int port = StringUtils.isNotEmpty(portStr) ? Integer.parseInt(portStr) : 17777;
				VirusCheckVO checkVO = virusManager.checkVirus(host, port, attFiles, commonResource);
				if (!checkVO.isSuccess()) {
					msg = checkVO.getCheckResultMsg();
					throw new VirusCheckException();
				}
			}
			
			MimeMessage mimeMessage = bbsManager.getBbsMimeMessage(null, content, attFiles, "UTF-8", EnvConstants.getBasicSetting("tmpdir"));

			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			if (!folder.exists()) {
				folder.create();
			}
			Map<String, String> resultMap = bbsManager.setMimeToFolder(mimeMessage, folder, subject, email, name, "UTF-8");
			String msgId = resultMap.get("msgId");
			String contentType = resultMap.get("contentType");

			contentVo = new BoardContentVO();
			contentVo.setBbsId(bbsId);
			contentVo.setMailDomainSeq(Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)));
			contentVo.setContentId(contentId);
			contentVo.setParentId(parentId);
			contentVo.setSubject(subject);
			contentVo.setHtmlContent(content);
			contentVo.setMsgId(msgId);
			contentVo.setCreatorName(name);
			contentVo.setCreatorSeq(mailUserSeq);
			contentVo.setIp(request.getRemoteAddr());
			contentVo.setIsNotice(isNotice);
			contentVo.setOrderNo(orderNo);
			contentVo.setDepth(depth);
			contentVo.setContentType(contentType);
			
			bbsManager.saveBoardContent(contentVo, writeMode, isReply, attFiles);
			msg = resource.getMessage("bbs.content.save.ok");
		}catch (VirusCheckException e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}
		catch (Exception e) {
			msg = resource.getMessage("bbs.content.save.fail");
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			bbsManager.deleteUploadFile(attFiles);
			if (store != null && store.isConnected()) store.close();
		}
		String url = "/bbs/listContent.do?bbsId="+bbsId;
		out.print(MakeMessage.printAlertRedirect(msg, url));
	    out.flush();
		
		return null;
	}
	
	public String executeUpdate() throws Exception {
		if (!checkPAID()) {
			return "paidError";
	    }
		User user = UserAuthManager.getUser(request);
		
		String email = user.get(User.MAIL_UID)+"@"+user.get(User.MAIL_DOMAIN);
		String name = user.get(User.USER_NAME);
		
		int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		I18nResources resource = getMessageResource("bbs");
		TMailStoreFactory factory = new TMailStoreFactory();
		TMailStore store = null;
		String msg = "";
		
		HttpSession session = request.getSession();
		Object objBbsId = session.getAttribute("bbsId");
		if (objBbsId != null) {
			bbsId = (Integer)objBbsId;
		}
		
		try {
			BoardContentVO contentVoTmp = new BoardContentVO();
			contentVoTmp = bbsManager.readBoardContent(bbsId, contentId, mailDomainSeq);
			Map bbsAuth = bbsManager.getBoardAuth(bbsId, Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
			if ( "T".equals(bbsAuth.get("isCreator")) || "T".equals(bbsAuth.get("isAdmin")) ) {
				//PASS
			} else if (Integer.parseInt(user.get(User.MAIL_USER_SEQ)) !=  contentVoTmp.getCreatorSeq() ) {
				msg = resource.getMessage("bbs.content.save.fail");
				String url = "/bbs/listContent.do?bbsId="+bbsId;
				out.print(MakeMessage.printAlertRedirect(msg, url));
			    out.flush();
			    return null;
			} 
			
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
			int[] contentIds = {contentId};
			String[] msgIds = bbsManager.readBoardContentMsgIds(bbsId, contentIds, mailDomainSeq);
			
			bbsManager.deleteBoardMimeContent(folder, bbsId, contentIds, msgIds);
			bbsManager.deleteAttachFile(bbsId, contentIds, mailDomainSeq);
			
			MimeMessage mimeMessage = bbsManager.getBbsMimeMessage(null, content, attFiles, "UTF-8", EnvConstants.getBasicSetting("tmpdir"));
			Map<String, String> resultMap = bbsManager.setMimeToFolder(mimeMessage, folder, subject, email, name, "UTF-8");
			String msgId = resultMap.get("msgId");
			String contentType = resultMap.get("contentType");
			
			contentVo = new BoardContentVO();
			contentVo.setBbsId(bbsId);
			contentVo.setMailDomainSeq(mailDomainSeq);
			contentVo.setContentId(contentId);
			contentVo.setSubject(subject);
			contentVo.setHtmlContent(content);
			contentVo.setMsgId(msgId);
			contentVo.setCreatorName(user.get(User.USER_NAME));
			contentVo.setCreatorSeq(Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
			contentVo.setIp(request.getRemoteAddr());
			contentVo.setIsNotice(isNotice);
			contentVo.setContentType(contentType);
			
			bbsManager.modifyBoardContent(contentVo, writeMode, attFiles);
			msg = resource.getMessage("bbs.content.modify.ok");
		} catch (NumberFormatException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new BoardContentNotFoundException(resource.getMessage("bbs.content.notfound"));
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("bbs.content.modify.fail");
		}
		finally {
			bbsManager.deleteUploadFile(attFiles);
			if (store != null && store.isConnected()) {
				store.close();
			}
		}
		String url = "/bbs/listContent.do?bbsId="+bbsId;
		out.print(MakeMessage.printAlertRedirect(msg, url));
	    out.flush();
		
		return null;
	}
	
	public void setVirusManager(VirusManager virusManager) {
		this.virusManager = virusManager;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
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
	
	public boolean getIsReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	public String getWriteMode() {
		return writeMode;
	}

	public void setWriteMode(String writeMode) {
		this.writeMode =  writeMode;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject =  subject;
	}
	
	public String getAttFiles() {
		return attFiles;
	}

	public void setAttFiles(String attFiles) {
		this.attFiles =  attFiles;
	}

	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	
	public void setContent(String content) {
		this.content =  content;
	}
	
	public int getBbsId() {
		return bbsId;
	}

	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	
	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName =  StringReplaceUtil.replaceEventAttr(creatorName);
	}

	public String getBbsType() {
		return bbsType;
	}

	public void setBbsType(String bbsType) {
		this.bbsType =  StringReplaceUtil.replaceEventAttr(bbsType);
	}
}
