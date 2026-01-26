package com.terracetech.tims.webmail.mail.action;

import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailQuotaBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;

public class ViewQuotaAction extends BaseAction{

	private static final long serialVersionUID = 20090323L;
	
	private MailManager mailManager = null;	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public String execute() throws Exception{
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();	
				
		MailQuotaBean mailQuota = null;
		MailQuotaBean webfolderQuota = null;
		
		try {			
			store = factory.connect(request.getRemoteAddr(), user);
			mailManager.setProcessResource(store, getMessageResource());
			mailQuota = mailManager.getQuotaRootInfo(FolderHandler.INBOX);			
			store.close();
			
			user.put(User.IMAP_LOGIN_ARGS,user.get(User.WEBFOLDER_LOGIN_ARGS));
			store = factory.connect(request.getRemoteAddr(), user);
			store.getDefaultWebFolder();
			mailManager.setProcessResource(store, getMessageResource());
			webfolderQuota = mailManager.getQuotaRootInfo(
					EnvConstants.getBasicSetting("webfolder.root"));			
			
			request.setAttribute("mailQuota", mailQuota);
			request.setAttribute("webfolderQuota", webfolderQuota);
		} catch (Exception e) {			
			LogManager.writeErr(this, e.getMessage(), e);
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
	
		return "success";
	}
	
	public String executePart() throws Exception{
		return execute();
	}
}
