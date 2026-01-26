package com.terracetech.tims.webmail.setting.action;

import java.util.Locale;
import java.util.Vector;

import jakarta.mail.MessagingException;

import com.terracetech.tims.common.I18nConstants;
import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.manager.UserAuthManager;
import com.terracetech.tims.webmail.setting.manager.SettingManager;
import com.terracetech.tims.webmail.setting.vo.Pop3VO;

public class ViewExtMailAction extends BaseAction {

	private static final long serialVersionUID = 20090112L;

	private SettingManager manager = null;
	private MailManager mailManager = null;

	private Pop3VO[] pop3List = null;
	
	private MailFolderBean[] defaultFolderBeans = null;
	private MailFolderBean[] userFolderBeans = null;
	
	private String installLocale = null;

	public Pop3VO[] getPop3List() {
		return pop3List;
	}
	
	public MailFolderBean[] getDefaultFolderBeans() {
		return defaultFolderBeans;
	}

	public MailFolderBean[] getUserFolderBeans() {
		return userFolderBeans;
	}
	
	public String getInstallLocale() {
		return installLocale;
	}

	public void setManager(SettingManager manager) {
		this.manager = manager;
	}
	
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	public String execute() throws Exception {

		User user = UserAuthManager.getUser(request);
		
		int mailUserSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
				
		I18nResources resource = getMessageResource();

		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		
		try {
			pop3List = manager.readPop3List(mailUserSeq);
			
			store = factory.connect(request.getRemoteAddr(), user);
			mailManager.setProcessResource(store, getMessageResource());
			MailFolderBean[] defaultFolders = mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER,false,-1);
			
			Vector v = new Vector();
			for (int i=0; i<defaultFolders.length; i++) {
				if (!(defaultFolders[i].getFullName()).equalsIgnoreCase("Sent") &&
					!(defaultFolders[i].getFullName()).equalsIgnoreCase("Drafts") &&
					!(defaultFolders[i].getFullName()).equalsIgnoreCase("Reserved")) {
					
					v.add(defaultFolders[i]);
				}
			}
			defaultFolderBeans = new MailFolderBean[v.size()];
			v.copyInto(defaultFolderBeans);
			
			userFolderBeans = mailManager.getFolderList(EnvConstants.USER_FOLDER,false,-1);
			installLocale = EnvConstants.getBasicSetting("setup.state");
			
		}
		catch (MessagingException me) {
			LogManager.writeErr(this, me.getMessage(), me);
			request.setAttribute("errMsg", resource.getMessage("error.imapconn"));
			return "errorBack";
		}
		catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		}finally {
			if (store!= null && store.isConnected()) {
				store.close();
			}
		}

		return "success";
	}
}
