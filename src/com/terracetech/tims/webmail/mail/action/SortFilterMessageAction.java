/**
 * SortFilterMessageAction.java 2009. 4. 9.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import java.io.PrintWriter;

import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailSortMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;

/**
 * <p><strong>SortFilterMessageAction.java</strong> Class Description</p>
 * <p>주요설명</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class SortFilterMessageAction extends BaseAction {
	
	private MailManager mailManager = null;
	
	
	/**
	 * @param mailManager 파라미터를 mailManager값에 설정
	 */
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = -2509108598852616047L;
	
	public String execute() throws Exception {
		
		String fromFolder = request.getParameter("fromFolder");
		String sender = request.getParameter("ruleSender");
		String receiver = request.getParameter("ruleReceiver");
		String subject = request.getParameter("ruleSubject");
		String toFolderName = request.getParameter("toFolder");
		
		JSONObject jObj = new JSONObject();
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();		
		try {
			
			String folderEncodeName = TMailUtility.IMAPFolderEncode(fromFolder);
			
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy("arrival");
			sortBean.setSortDir("desc");			
			sortBean.setAdvanceMode(true);
			sortBean.setAdFromEmailPattern(sender);
			sortBean.setAdToEmailPattern(receiver);			
			sortBean.setAdSearchPattern(subject);
			sortBean.setAdSearchCategory("s");
			sortBean.setPage("0");
			sortBean.setPageBase("0");			
			
			store = factory.connect(request.getRemoteAddr(), user);			
			mailManager.setProcessResource(store, getMessageResource());			
			
			MailSortMessageBean[] messageBeans = mailManager.getXSortMessageBeans(folderEncodeName, null, sortBean);
			
			if(messageBeans != null && messageBeans.length > 0){
				long[] uids = new long[messageBeans.length];
				for (int i = 0; i < messageBeans.length; i++) {
					uids[i] = messageBeans[i].getId();
				}
				mailManager.moveMessage(uids, fromFolder, toFolderName);
			}		
			
			jObj.put("result", "success");
		} catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			jObj.put("result", "fail");
		} finally {
			if(store !=null && store.isConnected())
				store.close();
		}
		
		response.addHeader("Cache-Control","no-store");
        response.addHeader("Pragma", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(jObj);
		
		return null;
	}

}
