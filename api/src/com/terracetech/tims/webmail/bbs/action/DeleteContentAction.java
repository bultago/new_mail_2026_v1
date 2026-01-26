package com.terracetech.tims.webmail.bbs.action;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.bbs.exception.BoardContentNotFoundException;
import com.terracetech.tims.webmail.bbs.manager.BbsManager;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.MakeMessage;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.bbs.vo.BoardContentVO;
import com.terracetech.tims.webmail.bbs.vo.BoardVO;

public class DeleteContentAction extends BaseAction {
	private BoardVO boardVo = null;
	private boolean bbsAdmin = false;
	private boolean bbsCreator = false;
	private BbsManager bbsManager = null;
	
	private int bbsId = 0;
	private int isNotice = 0;
	
	private int[] contentSel = null;
	
	public String execute() throws Exception {
		 if (!checkPAID()) {
             return "paidError";
         }
		User user = UserAuthManager.getUser(request);
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
		
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		boardVo = bbsManager.readBoard(bbsId, domainSeq);
		
		try {
			int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
			
			Map<String, String> confMap = bbsManager.getBbsConnectionInfo(user.get(User.MAIL_DOMAIN));
			store = factory.connect(request.getRemoteAddr(), confMap);
			TMailFolder folder = store.getFolder(Integer.toString(bbsId));
	
			if(contentSel.length > 0){
				for(int i=0 ; i < contentSel.length; i++ ) {
					BoardContentVO contentVoTmp = new BoardContentVO();
					contentVoTmp = bbsManager.readBoardContent(bbsId, contentSel[i], mailDomainSeq);
					Map bbsAuth = bbsManager.getBoardAuth(bbsId, Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ)), Integer.parseInt(user.get(User.MAIL_USER_SEQ)));
					if ( "T".equals(bbsAuth.get("isCreator")) || "T".equals(bbsAuth.get("isAdmin")) ) {
						//PASS
					} else if (Integer.parseInt(user.get(User.MAIL_USER_SEQ)) !=  contentVoTmp.getCreatorSeq() ) {
						msg = resource.getMessage("bbs.content.delete.fail");
						String url = "/bbs/listContent.do?bbsId="+bbsId;
						out.print(MakeMessage.printAlertRedirect(msg, url));
					    out.flush();
					    return null;
					}
				}
			}
			
			String[] msgIds = bbsManager.readBoardContentMsgIds(bbsId, contentSel, mailDomainSeq);
			bbsManager.deleteBoardMimeContent(folder, bbsId, contentSel, msgIds);
			bbsManager.deleteBoardDBContent(bbsId, contentSel, mailDomainSeq);
			msg = resource.getMessage("bbs.content.delete.ok");
		} catch (NumberFormatException e) {
			LogManager.writeErr(this, e.getMessage(), e);
			throw new BoardContentNotFoundException(resource.getMessage("bbs.content.notfound"));
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			msg = resource.getMessage("bbs.content.delete.fail");
		} finally {
			if (store != null && store.isConnected()) {
				store.close();
			}
		}

		String url = "/bbs/listContent.do?bbsId="+bbsId;
		out.print(MakeMessage.printAlertRedirect(msg, url));
	    out.flush();
		
		return null;
	}
	
	public int getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}
	public BbsManager getBbsManager() {
		return bbsManager;
	}
	public void setBbsManager(BbsManager bbsManager) {
		this.bbsManager = bbsManager;
	}
	public int getBbsId() {
		return bbsId;
	}
	public void setBbsId(int bbsId) {
		this.bbsId = bbsId;
	}
	public int[] getContentSel() {
		return contentSel;
	}
	public void setContentSel(int[] contentSel) {
		this.contentSel = contentSel;
	}
}
