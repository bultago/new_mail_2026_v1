/**
 * ListMDNResponsesAction.java 2009. 3. 16.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.action;

import com.terracetech.tims.mail.TMailStore;
import com.terracetech.tims.webmail.common.BaseAction;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.ibean.MessageSortInfoBean;
import com.terracetech.tims.webmail.mail.manager.FolderHandler;
import com.terracetech.tims.webmail.mail.manager.MailManager;
import com.terracetech.tims.webmail.mail.manager.TMailStoreFactory;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.util.StringUtils;

/**
 * <p><strong>ListMDNResponsesAction.java</strong> Class Description</p>
 * <p>ÁÖ¿ä¼³¸í</p>
 * <ul>
 * <li></li>
 * </ul>
 * @author sshyun
 * @since Tims7
 * @version 7.0 
 */
public class ListMDNResponsesAction extends BaseAction {

	/**
	 * <p></p>
	 */
	private static final long serialVersionUID = -3301519526375316476L;
	private MailManager mailManager = null;
	
	public ListMDNResponsesAction(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public String execute() throws Exception {
		boolean isError = false;
		String page = request.getParameter("page");		
		String pattern = request.getParameter("pattern");		
		page = (page == null)?"1":page;
		String folderName = FolderHandler.SENT;
		String pageBase = user.get(User.PAGE_LINE_CNT);
		
		TMailStore store = null;
		TMailStoreFactory factory = new TMailStoreFactory();
		/*if (request.getMethod().equalsIgnoreCase("get")) {
			pattern = StringUtils.getDecodingUTF(pattern);
		}*/

		pattern = StringUtils.escapeImapQuote(pattern);
		
		try {
			MessageSortInfoBean sortBean = new MessageSortInfoBean();
			sortBean.setSortBy("date");
			sortBean.setSortDir("aesc");
			sortBean.setPattern(pattern);
			
			store = factory.connect(request.getRemoteAddr(), user);			
			mailManager.setProcessResource(store, getMessageResource());
			
			int total = mailManager.getSortTotal(store, folderName, sortBean);
			
			int npages = (int) Math.ceil((double) total / Integer.parseInt(pageBase));
			if (0 < npages && npages < Integer.parseInt(page)) {
				page = Integer.toString(npages);				
			}			
			sortBean.setPage(page);
			sortBean.setPageBase(pageBase);			
			
			MailMessageBean[] messages = mailManager.getMessageBeans(store, folderName, sortBean);
			
			pattern = StringUtils.unescapeImapQuote(pattern);
			
			request.setAttribute("messages", messages);
			request.setAttribute("currentPage", page);
			request.setAttribute("pageBase", pageBase);
			request.setAttribute("total", total);
			request.setAttribute("keyWord", pattern);
			
		}catch (Exception e) {
			LogManager.writeErr(this, e.getMessage(), e);
			isError = true;
		} finally{
			if(store !=null && store.isConnected())
				store.close();
		}
		
		return (isError)?"subError":"list";
	}
	

}
