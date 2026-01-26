package com.terracetech.tims.mobile.common.action;

import java.util.List;

import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.EnvConstants;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.scheduler.manager.SchedulerManager;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;

public class HomeAction extends BaseAction {
	
	private static final long serialVersionUID = -8574046828925429722L;
	
	private MailManager mailManager = null;
	private SchedulerManager schedulerManager = null;
	
	public void setMailManager(MailManager mailManager){
		this.mailManager = mailManager;
	}
	public void setSchedulerManager(SchedulerManager schedulerManager){
		this.schedulerManager = schedulerManager;
	}
	
	
	public String execute() throws Exception {
		int userSeq = Integer.parseInt(user.get(User.MAIL_USER_SEQ));
		int domainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
		String email = user.get(User.EMAIL);
		int unreadMailCount = 0;
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		List<SchedulerDataVO> todayScheduleList = null;
		
		try{
			store = factory.connect(request.getRemoteAddr(), user);
			mailManager.setProcessResource(store, getMessageResource());					
			MailFolderBean[] defaultFolder = mailManager.getFolderList(EnvConstants.DEFAULT_FOLDER, false, -1);
			MailFolderBean[] userFolder = mailManager.getFolderList(EnvConstants.USER_FOLDER, false, -1);
			
			for (MailFolderBean mailFolderBean : defaultFolder) {
				if(mailFolderBean.getFullName().equals(FolderHandler.INBOX)){
					unreadMailCount += 	mailFolderBean.getUnseenCnt();				
				}
			}
			
			for (MailFolderBean mailFolderBean : userFolder) {
				unreadMailCount += 	mailFolderBean.getUnseenCnt();		
			}
			todayScheduleList = schedulerManager.getDayScheduleList(0, 0, 0, domainSeq, userSeq, email);
			getMenuStatus();
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
		} finally {
			if(store !=null && store.isConnected())
				store.close();		
		}
		
		request.setAttribute("unreadMailCount", unreadMailCount);
		request.setAttribute("todaySchedule", todayScheduleList);
		request.setAttribute("loginTimeInfo", user.get(User.WEBMAIL_LOGIN_TIME));
		
		return "success";
		
	}
}
